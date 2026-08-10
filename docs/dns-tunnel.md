# DNS Tunnel

This is the stable Android contract for the DNS Tunnel profile, Rust Slipstream
sidecar and FlowRelay boundary. Historical implementation and acceptance evidence
lives in [`goals/`](goals/).

## Data path

```text
application TCP or UDP
  -> Owenclave gVisor VPN
  -> authenticated loopback SOCKS5
  -> Rust Slipstream sidecar
  -> one QUIC stream per application flow or UDP association
  -> server-side FlowRelay
  -> deployment-owned egress
```

DNS Tunnel is a dedicated `DnsttBean`/`TYPE_DNSTT` profile. Quick setup accepts only
a raw 32-lowercase-hex Flow token; `t.x.ass-peak.de` and its bundled certificate are
fixed by the app. Generic SSH profiles and `ssh://` or resolver-bearing `dnstt://`
imports and exports are intentionally absent.

Automatic mode stores no resolver. At startup Owenclave snapshots the active
non-VPN underlay and tries the first two unique DNS addresses, then UDP
`77.88.8.8:53` and `77.88.8.1:53`, with duplicates removed. Candidates run
sequentially until real Slipstream `Connection ready`; a failed child is stopped
before the next starts. Manual mode instead uses exactly one validated
`udp://host:port` or `tcp://host:port` with no fallback.

The Exclave outbound uses the authenticated loopback SOCKS boundary. Application
TCP and UDP are supported; each TCP flow or UDP association gets an independent
local connection and Slipstream QUIC stream. Mux, health-probe streams and a
second carrier path are not part of this contract.

## Runtime constraints

- DNS Tunnel supports gVisor TUN only. System TUN cannot protect sockets opened by
  the separate Slipstream process.
- The Flow token and ephemeral loopback credentials reach the child only through
  stdin, not argv, environment variables or temporary files.
- VPN startup allows 15 seconds for aggregate carrier readiness; latency tests
  allow 5 seconds and are serialized process-wide.
- Automatic candidate exhaustion, carrier failure and FlowRelay failure are
  fail-closed. Discovery ends after readiness; there is no periodic health check,
  ranking, persistent cache, automatic handover, direct carrier, legacy SSH path or
  direct destination fallback.
- Stable runtime retains one resolver and one child. Busy uses the existing 50 ms
  pacing and 400 ms keepalive; Warm polls at most once per 400 ms; quiet open streams
  poll at most once per 2 seconds; empty connections do not explicitly poll. Quiet
  and empty keepalive remains 5 seconds.
- The generated Android Slipstream artifact is currently arm64-only and ignored
  under `app/src/main/jniLibs/`. A clean build must run
  `./bin/lib/slipstream/build.sh`.

## Ownership

This repository owns profile persistence and setup, generated Exclave config,
Android sidecar lifecycle, pinned Slipstream patches, the FlowRelay wire contract,
and the focused `flowd` source/tests. The relevant boundaries are:

- `app/src/main/java/io/nekohasekai/sagernet/fmt/dnstt/DnsttFmt.kt`
- `app/src/main/java/io/nekohasekai/sagernet/fmt/dnstt/DnsttBean.java`
- `app/src/main/java/io/nekohasekai/sagernet/fmt/ConfigBuilder.kt`
- `app/src/main/java/io/nekohasekai/sagernet/bg/proto/SlipstreamInstance.kt`
- `app/src/main/java/io/nekohasekai/sagernet/bg/proto/V2RayInstance.kt`
- `bin/lib/slipstream/`

The Slipstream implementation is built from a pinned external upstream and runs
as an Android native sidecar. Public authoritative listeners, deployed `flowd`,
service units, firewall policy, resolver delegation and final egress are owned by
the upper operations repository and live server configuration. Although `flowd`
source stays here beside its wire tests, this repository is not the deployment
source of truth.

Do not commit or print tokens, complete profile links, private keys, keystores or
credential databases.
