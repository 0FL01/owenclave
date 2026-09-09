package io.nekohasekai.sagernet.fmt.dnstt

import io.nekohasekai.sagernet.ktx.joinHostPort
import java.net.URI
import java.net.InetAddress

private val flowTokenPattern = Regex("[0-9a-f]{32}")

data class DnsttResolver(val transport: String, val host: String, val port: Int) {
    override fun toString() = "$transport://${joinHostPort(host, port)}"

    fun paths(): List<DnsttResolver> = if (transport == "tcp+mp") {
        require(host == "77.88.8.88" && port == 53) { "Unsupported multipath pair" }
        listOf(DnsttResolver("tcp", host, port), DnsttResolver("tcp", "77.88.8.1", 53))
    } else listOf(this)
}

fun automaticDnsttResolvers(dnsServers: List<InetAddress>): List<DnsttResolver> {
    val hosts = linkedSetOf<String>()
    dnsServers.forEach { address ->
        if (hosts.size < 2) address.hostAddress?.let(hosts::add)
    }
    hosts.add("77.88.8.8")
    hosts.add("77.88.8.1")
    return hosts.map { DnsttResolver("tcp", it, 53) }
}

fun parseDnsttResolver(value: String): DnsttResolver {
    val uri = URI(value.trim())
    val transport = uri.scheme?.lowercase()
    require(transport == "udp" || transport == "tcp" || transport == "tcp+mp") {
        "DNS resolver must use udp://, tcp:// or the documented tcp+mp:// pair"
    }
    require(uri.userInfo == null && uri.path.isNullOrEmpty() && uri.query == null && uri.fragment == null) {
        "invalid DNS resolver"
    }
    val host = uri.host.orEmpty()
    require(host.isNotEmpty() && uri.port in 1..65535) { "DNS resolver host and port are required" }
    return DnsttResolver(transport, host, uri.port).also { it.paths() }
}

fun isValidDnsttResolver(value: String) = runCatching { parseDnsttResolver(value) }.isSuccess

fun isValidDnsttToken(value: String) = flowTokenPattern.matches(value)

fun decodeDnsttToken(value: String): ByteArray {
    require(isValidDnsttToken(value)) { "invalid DNS Tunnel Flow token" }
    return value.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
}
