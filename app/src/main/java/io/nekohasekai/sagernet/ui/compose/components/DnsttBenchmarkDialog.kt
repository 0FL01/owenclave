package io.nekohasekai.sagernet.ui.compose.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.nekohasekai.sagernet.bg.test.DnsttBenchmarkResult
import io.nekohasekai.sagernet.bg.test.sortedDnsttBenchmarkResults
import java.util.Locale

@Composable
internal fun DnsttBenchmarkDialog(
    results: List<DnsttBenchmarkResult>,
    running: Boolean,
    error: String?,
    currentResolver: String,
    onSelect: (DnsttBenchmarkResult) -> Unit,
    onAutomatic: () -> Unit,
    onDismiss: () -> Unit,
) {
    val sorted = results.sortedDnsttBenchmarkResults()
    val recommended = if (running) null else sorted.firstOrNull { it.complete }
    ExpressiveDialog(onDismissRequest = onDismiss) {
        Text("DNS benchmark", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(6.dp))
        Text(
            "Three short runs per resolver, up to 18 MiB each. Keep the current network unchanged.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(16.dp))

        if (running && results.isEmpty()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            sorted.forEach { result ->
                val selectable = !running && result.complete
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(enabled = selectable) { onSelect(result) },
                    shape = MaterialTheme.shapes.large,
                    color = if (result.resolver.toString() == currentResolver) {
                        MaterialTheme.colorScheme.secondaryContainer
                    } else {
                        MaterialTheme.colorScheme.surfaceContainerHighest
                    },
                ) {
                    Column(Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
                        Text(
                            buildString {
                                append(result.resolver)
                                if (result === recommended) append(" · Recommended")
                            },
                            style = MaterialTheme.typography.titleSmall,
                        )
                        Text(
                            result.summary(),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }

        if (error != null) {
            Spacer(Modifier.height(10.dp))
            Text(error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }
        Spacer(Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            TextButton(onClick = onAutomatic) { Text("Automatic") }
            TextButton(onClick = onDismiss) { Text(if (running) "Cancel" else "Close") }
        }
    }
}

private fun DnsttBenchmarkResult.summary() = when {
    failed -> "Unavailable"
    speedsMbps.isEmpty() -> "Connecting…"
    !complete -> "${speedsMbps.size}/${DnsttBenchmarkResult.RUNS} runs complete"
    burstMbps > sustainedMbps * 1.5 -> String.format(
        Locale.US,
        "%.2f → %.2f Mbit/s sustained · %d ms",
        burstMbps,
        sustainedMbps,
        latencyMs,
    )
    else -> String.format(Locale.US, "%.2f Mbit/s sustained · %d ms", sustainedMbps, latencyMs)
}
