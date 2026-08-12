package io.nekohasekai.sagernet.ktx

import io.nekohasekai.sagernet.fmt.AbstractBean
import io.nekohasekai.sagernet.fmt.Serializable
import io.nekohasekai.sagernet.fmt.olcrtc.parseOLCRTCLink

fun parseShareLinks(text: String): List<AbstractBean> = text
    .lineSequence()
    .flatMap { it.trim().splitToSequence(' ') }
    .filter { it.startsWith("olcrtc://", ignoreCase = true) }
    .mapNotNull { runCatching { parseOLCRTCLink(it) }.getOrNull() }
    .toList()

fun <T : Serializable> T.applyDefaultValues(): T {
    initializeDefaultValues()
    return this
}
