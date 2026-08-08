package io.nekohasekai.sagernet.fmt.ssh

import android.net.Uri
import android.util.Base64

private val dnsttPublicKeyPattern = Regex("[0-9a-fA-F]{64}")

private fun decodeDnsttField(value: String): String = String(
    Base64.decode(value, Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP),
    Charsets.UTF_8,
)

private fun encodeDnsttField(value: String): String = Base64.encodeToString(
    value.toByteArray(Charsets.UTF_8),
    Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP,
)

fun parseDnstt(link: String): SSHBean {
    val uri = Uri.parse(link)
    require(uri.scheme.equals("dnstt", ignoreCase = true)) { "invalid DNS Tunnel scheme" }
    val domain = uri.host.orEmpty().trimEnd('.')
    val dnsttPublicKey = uri.getQueryParameter("pub").orEmpty()
    val privateKey = uri.getQueryParameter("key")?.let(::decodeDnsttField).orEmpty()
    val hostKey = uri.getQueryParameter("hostkey")?.let(::decodeDnsttField).orEmpty()
    require(domain.isNotEmpty()) { "DNS Tunnel domain is required" }
    require(dnsttPublicKeyPattern.matches(dnsttPublicKey)) { "invalid dnstt public key" }
    require(privateKey.isNotEmpty()) { "SSH private key is required" }
    require(hostKey.isNotEmpty()) { "SSH host key is required" }

    return SSHBean().apply {
        name = uri.fragment.orEmpty().ifEmpty { "DNS Tunnel" }
        serverAddress = domain
        serverPort = 22
        username = uri.getQueryParameter("user").orEmpty().ifEmpty { "root" }
        authType = SSHBean.AUTH_TYPE_PUBLIC_KEY
        this.privateKey = privateKey
        privateKeyPassphrase = uri.getQueryParameter("passphrase")?.let(::decodeDnsttField).orEmpty()
        publicKey = hostKey
        keepaliveInterval = 0
        dnsttEnabled = true
        dnsttDomain = domain
        this.dnsttPublicKey = dnsttPublicKey.lowercase()
    }
}

fun SSHBean.toDnsttUri(): String {
    require(dnsttEnabled == true) { "not a DNS Tunnel profile" }
    return Uri.Builder()
        .scheme("dnstt")
        .authority(dnsttDomain)
        .appendQueryParameter("pub", dnsttPublicKey)
        .appendQueryParameter("user", username)
        .appendQueryParameter("key", encodeDnsttField(privateKey))
        .appendQueryParameter("hostkey", encodeDnsttField(publicKey))
        .apply {
            if (privateKeyPassphrase.isNotEmpty()) {
                appendQueryParameter("passphrase", encodeDnsttField(privateKeyPassphrase))
            }
        }
        .fragment(name)
        .build()
        .toString()
}
