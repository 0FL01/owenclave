package io.nekohasekai.sagernet.fmt.ssh

import android.net.Uri
import io.nekohasekai.sagernet.ktx.joinHostPort
import java.net.URI

private val flowTokenPattern = Regex("[0-9a-f]{32}")

data class DnsttResolver(val transport: String, val host: String, val port: Int) {
    override fun toString() = "$transport://${joinHostPort(host, port)}"
}

fun parseDnsttResolver(value: String): DnsttResolver {
    val uri = URI(value.trim())
    val transport = uri.scheme?.lowercase()
    require(transport == "udp" || transport == "tcp") { "DNS resolver must use udp:// or tcp://" }
    require(uri.userInfo == null && uri.path.isNullOrEmpty() && uri.query == null && uri.fragment == null) {
        "invalid DNS resolver"
    }
    val host = uri.host.orEmpty()
    require(host.isNotEmpty() && uri.port in 1..65535) { "DNS resolver host and port are required" }
    return DnsttResolver(transport, host, uri.port)
}

fun isValidDnsttResolver(value: String) = runCatching { parseDnsttResolver(value) }.isSuccess

fun isValidDnsttToken(value: String) = flowTokenPattern.matches(value)

fun decodeDnsttToken(value: String): ByteArray {
    require(isValidDnsttToken(value)) { "invalid DNS Tunnel Flow token" }
    return value.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
}

fun parseDnstt(link: String): SSHBean {
    val uri = Uri.parse(link)
    require(uri.scheme.equals("dnstt", ignoreCase = true)) { "invalid DNS Tunnel scheme" }
    val domain = uri.host.orEmpty().trimEnd('.')
    val resolverValues = uri.getQueryParameters("resolver")
    val token = uri.getQueryParameter("token").orEmpty()
    require(domain.isNotEmpty()) { "DNS Tunnel domain is required" }
    require(resolverValues.size == 1) { "exactly one DNS resolver is required" }
    val resolver = parseDnsttResolver(resolverValues.single()).toString()
    require(!uri.queryParameterNames.any { it in setOf("pub", "key", "hostkey", "passphrase", "user") }) {
        "Legacy DNS Tunnel profile is unsupported; re-import an updated link"
    }
    require(isValidDnsttToken(token)) { "invalid DNS Tunnel Flow token" }

    return SSHBean().apply {
        name = uri.fragment.orEmpty().ifEmpty { "DNS Tunnel" }
        serverAddress = domain
        serverPort = 22
        username = ""
        authType = SSHBean.AUTH_TYPE_PASSWORD
        password = token
        keepaliveInterval = 0
        dnsttEnabled = true
        dnsttDomain = domain
        dnsttResolver = resolver
    }
}

fun SSHBean.toDnsttUri(): String {
    require(dnsttEnabled == true) { "not a DNS Tunnel profile" }
    val resolver = parseDnsttResolver(dnsttResolver).toString()
    require(isValidDnsttToken(password)) { "invalid DNS Tunnel Flow token" }
    return Uri.Builder()
        .scheme("dnstt")
        .authority(dnsttDomain)
        .appendQueryParameter("resolver", resolver)
        .appendQueryParameter("token", password)
        .fragment(name)
        .build()
        .toString()
}
