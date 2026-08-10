package io.nekohasekai.sagernet.fmt.dnstt

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
