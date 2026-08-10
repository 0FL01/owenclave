package io.nekohasekai.sagernet.bg.test

import android.os.SystemClock
import io.nekohasekai.sagernet.SagerNet
import io.nekohasekai.sagernet.bg.proto.V2RayInstance
import io.nekohasekai.sagernet.database.ProxyEntity
import io.nekohasekai.sagernet.fmt.dnstt.DnsttResolver
import io.nekohasekai.sagernet.fmt.dnstt.automaticDnsttResolvers
import io.nekohasekai.sagernet.ktx.mkPort
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.HttpURLConnection
import java.net.InetSocketAddress
import java.net.Proxy
import java.net.URL

internal data class DnsttBenchmarkResult(
    val resolver: DnsttResolver,
    val speedsMbps: List<Double> = emptyList(),
    val latenciesMs: List<Long> = emptyList(),
    val failed: Boolean = false,
) {
    val complete get() = !failed && speedsMbps.size == RUNS
    val sustainedMbps get() = if (complete) minOf(speedsMbps[1], speedsMbps[2]) else 0.0
    val burstMbps get() = speedsMbps.maxOrNull() ?: 0.0
    val latencyMs get() = latenciesMs.sorted().getOrNull(latenciesMs.size / 2) ?: 0L

    companion object {
        const val RUNS = 3
    }
}

internal fun List<DnsttBenchmarkResult>.sortedDnsttBenchmarkResults() = sortedWith(
    compareByDescending<DnsttBenchmarkResult> { it.complete }
        .thenByDescending { it.sustainedMbps }
        .thenBy { it.latencyMs },
)

internal class DnsttBenchmark(private val profile: ProxyEntity) {
    private companion object {
        const val RUN_DURATION_MS = 3_000L
        const val RUN_BYTE_LIMIT = 6L * 1024L * 1024L
        const val REQUEST_BYTES = RUN_BYTE_LIMIT
        const val CONNECT_TIMEOUT_MS = 5_000
        const val READ_TIMEOUT_MS = 1_000
        const val BENCHMARK_URL = "https://speed.cloudflare.com/__down"
    }

    private class NetworkChangedException : IOException("Network changed; run benchmark again")

    suspend fun run(onUpdate: suspend (DnsttBenchmarkResult) -> Unit): List<DnsttBenchmarkResult> {
        require(profile.type == ProxyEntity.TYPE_DNSTT)
        val network = V2RayInstance.underlayNetwork() ?: throw IOException("No physical network")
        val dnsServers = SagerNet.connectivity.getLinkProperties(network)?.dnsServers.orEmpty()
        val results = mutableListOf<DnsttBenchmarkResult>()

        for (resolver in automaticDnsttResolvers(dnsServers)) {
            ensureNetwork(network)
            var result = DnsttBenchmarkResult(resolver)
            onUpdate(result)
            val candidate = profile.copy(
                dnsttBean = profile.dnsttBean?.clone()?.apply { this.resolver = resolver.toString() },
            )
            val httpPort = mkPort()
            val instance = V2RayTestInstance(candidate, testHttpPort = httpPort)
            try {
                instance.runTest {
                    repeat(DnsttBenchmarkResult.RUNS) {
                        ensureNetwork(network)
                        val (speed, latency) = download(httpPort)
                        result = result.copy(
                            speedsMbps = result.speedsMbps + speed,
                            latenciesMs = result.latenciesMs + latency,
                        )
                        onUpdate(result)
                    }
                }
                ensureNetwork(network)
            } catch (error: CancellationException) {
                throw error
            } catch (_: NetworkChangedException) {
                throw NetworkChangedException()
            } catch (_: Throwable) {
                if (V2RayInstance.underlayNetwork() != network) throw NetworkChangedException()
                result = result.copy(failed = true)
                onUpdate(result)
            }
            results += result
        }
        return results.sortedDnsttBenchmarkResults()
    }

    private fun ensureNetwork(expected: android.net.Network) {
        if (V2RayInstance.underlayNetwork() != expected) throw NetworkChangedException()
    }

    private suspend fun download(httpPort: Int) = withContext(Dispatchers.IO) {
        val proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress("127.0.0.1", httpPort))
        val url = URL("$BENCHMARK_URL?bytes=$REQUEST_BYTES&r=${SystemClock.elapsedRealtimeNanos()}")
        val startedAt = SystemClock.elapsedRealtimeNanos()
        val connection = (url.openConnection(proxy) as HttpURLConnection).apply {
            connectTimeout = CONNECT_TIMEOUT_MS
            readTimeout = READ_TIMEOUT_MS
            useCaches = false
            setRequestProperty("Cache-Control", "no-store")
        }
        try {
            if (connection.responseCode != HttpURLConnection.HTTP_OK) throw IOException("Benchmark unavailable")
            val transferStartedAt = SystemClock.elapsedRealtimeNanos()
            val latencyMs = (transferStartedAt - startedAt) / 1_000_000L
            val deadline = transferStartedAt + RUN_DURATION_MS * 1_000_000L
            var received = 0L
            var elapsedMs = 1L
            val buffer = ByteArray(32 * 1024)
            connection.inputStream.use { input ->
                while (received < RUN_BYTE_LIMIT && SystemClock.elapsedRealtimeNanos() < deadline) {
                    val length = minOf(buffer.size.toLong(), RUN_BYTE_LIMIT - received).toInt()
                    val read = input.read(buffer, 0, length)
                    if (read < 0) break
                    received += read
                }
                elapsedMs = ((SystemClock.elapsedRealtimeNanos() - transferStartedAt) / 1_000_000L)
                    .coerceAtLeast(1L)
            }
            if (received == 0L) throw IOException("Benchmark returned no data")
            (received * 8.0 / elapsedMs / 1000.0) to latencyMs
        } finally {
            connection.disconnect()
        }
    }
}
