package io.nekohasekai.sagernet.bg.proto

import android.os.Build
import io.nekohasekai.sagernet.SagerNet
import io.nekohasekai.sagernet.bg.AbstractInstance
import io.nekohasekai.sagernet.fmt.DnsttClientConfig
import io.nekohasekai.sagernet.ktx.Logs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStream
import java.net.InetSocketAddress
import java.net.Socket

internal class DnsttInstance(
    private val config: DnsttClientConfig,
    private val underlyingDns: () -> String?,
) : AbstractInstance {
    private data class Resolver(val name: String, val flag: String, val endpoint: String)

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val lock = Any()
    private var process: Process? = null
    private var nextResolver = 0
    private var monitor: Job? = null
    @Volatile
    private var closed = false

    private fun resolvers(): List<Resolver> = buildList {
        underlyingDns()?.let { add(Resolver("underlying-udp", "-udp", it)) }
        add(Resolver("yandex-dot", "-dot", "77.88.8.8:853"))
        add(Resolver("cloudflare-doh", "-doh", "https://1.1.1.1/dns-query"))
    }

    // Do not copy dnstt session identifiers into application logs.
    private fun drain(stream: InputStream) {
        scope.launch {
            runCatching {
                stream.use {
                    val buffer = ByteArray(4096)
                    while (isActive && it.read(buffer) >= 0) Unit
                }
            }
        }
    }

    private fun isAlive(child: Process?): Boolean {
        if (child == null) return false
        return try {
            child.exitValue()
            false
        } catch (_: IllegalThreadStateException) {
            true
        }
    }

    private fun stopLocked() {
        val child = process ?: return
        child.destroy()
        repeat(10) {
            if (!isAlive(child)) return@repeat
            Thread.sleep(50)
        }
        if (isAlive(child) && Build.VERSION.SDK_INT >= 26) child.destroyForcibly()
        process = null
    }

    private fun startNextLocked() {
        val profiles = resolvers()
        var failure: IOException? = null
        repeat(profiles.size) {
            val resolver = profiles[nextResolver.mod(profiles.size)]
            nextResolver = (nextResolver + 1).mod(profiles.size)
            val binary = SagerNet.application.applicationInfo.nativeLibraryDir + "/libdnstt.so"
            val command = listOf(
                binary,
                resolver.flag,
                resolver.endpoint,
                "-pubkey",
                config.publicKey,
                config.domain,
                "127.0.0.1:${config.localPort}",
            )
            try {
                val child = ProcessBuilder(command)
                    .directory(SagerNet.application.noBackupFilesDir)
                    .start()
                process = child
                drain(child.inputStream)
                drain(child.errorStream)
                Logs.i("dnstt: starting ${resolver.name}")
                return
            } catch (e: IOException) {
                failure = e
            }
        }
        throw failure ?: IOException("no dnstt resolver profile available")
    }

    private fun rotate() {
        synchronized(lock) {
            if (closed) return
            stopLocked()
            startNextLocked()
        }
    }

    private fun hasSshBanner(): Boolean {
        if (!isAlive(process)) return false
        return runCatching {
            Socket().use { socket ->
                socket.connect(InetSocketAddress("127.0.0.1", config.localPort), 2_000)
                socket.soTimeout = 15_000
                socket.getInputStream().bufferedReader().readLine().startsWith("SSH-2.0-")
            }
        }.getOrDefault(false)
    }

    override fun launch() {
        synchronized(lock) {
            check(!closed)
            startNextLocked()
        }
    }

    override suspend fun awaitReady() = withContext(Dispatchers.IO) {
        val attempts = resolvers().size * 2
        repeat(attempts) { index ->
            if (hasSshBanner()) {
                startMonitor()
                Logs.i("dnstt: SSH path ready")
                return@withContext
            }
            if (index + 1 < attempts) rotate()
        }
        throw IOException("dnstt could not reach the SSH backend")
    }

    private fun startMonitor() {
        if (monitor != null) return
        monitor = scope.launch {
            while (isActive) {
                delay(15_000)
                if (!isAlive(process)) {
                    Logs.w("dnstt: resolver process failed, rotating")
                    runCatching { awaitReady() }
                        .onFailure {
                            Logs.w("dnstt: no resolver path ready")
                            synchronized(lock) { stopLocked() }
                        }
                }
            }
        }
    }

    override fun close() {
        synchronized(lock) {
            if (closed) return
            closed = true
            monitor?.cancel()
            stopLocked()
        }
        scope.cancel()
    }
}
