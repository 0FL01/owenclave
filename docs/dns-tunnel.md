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
a raw 32-lowercase-hex Flow token through the profile editor, scanner or Configuration
clipboard action; `t.x.ass-peak.de` and its bundled certificate are fixed by the app.
Generic SSH profiles and `ssh://` or resolver-bearing `dnstt://` imports and exports
are intentionally absent.

Automatic mode stores no resolver. At startup Owenclave snapshots the active
non-VPN underlay and tries the first two unique DNS addresses over TCP, then TCP
`77.88.8.8:53` and `77.88.8.1:53`, with duplicates removed. Candidates run
sequentially until real Slipstream `Connection ready`; a failed child is stopped
before the next starts. Manual mode instead uses exactly one validated
`udp://host:port` or `tcp://host:port` with no fallback.

Using UDP instead of TCP for the same resolver is a rejected performance path for the
tested LTE route. In a short production-gVisor A/B, UDP remained functional but exact
download throughput averaged 18.121 kB/s versus 197.070 kB/s over TCP, the acknowledged
upload did not complete and 4 KiB latency was 4.66x worse. The strict TCP selection was
restored. Do not repeat this transport-only experiment without materially new network
evidence; see
[`goals/2026-08-12-dns-tunnel-udp-resolver-ab.md`](goals/2026-08-12-dns-tunnel-udp-resolver-ab.md).

The explicit DNS benchmark keeps a separate labeled pool: the automatic candidates,
all Yandex Basic, Safe and Family IPv4 pairs, and the nine user-supplied Rostelecom
scan candidates. The latter are marked Experimental and are never part of automatic
startup. Candidates run sequentially while the VPN is stopped; each gets one bounded
512 KiB or three-second download through one temporary child. Results rank measured
throughput before latency without an automatic recommendation. Selecting a result
stores it as the existing strict manual override; choosing Automatic clears it.
The last fully completed result set and selected benchmark host are stored with that
profile. Reopening the benchmark shows this snapshot without starting a child; Retest
runs the same foreground benchmark and replaces the snapshot only after success, so
cancellation or failure keeps the previous one. Cancel keeps the dialog visible until
the active child has stopped, then makes connection controls available again. Invalid
saved snapshots are ignored. The first resolver or Automatic choice is accepted and
the selector is disabled while cleanup and persistence finish.
Profile batch testing is unavailable while the proxy service is starting or connected,
so it cannot create a second DNS Tunnel child beside the active VPN.
The first reachable Cloudflare, OVH or Hetzner download endpoint is fixed for the
whole benchmark so endpoint fallback does not multiply runs or make resolver scores
incomparable.

The Exclave outbound uses the authenticated loopback SOCKS boundary. Application
TCP and UDP are supported; each TCP flow or UDP association gets an independent
local connection and Slipstream QUIC stream. Mux, health-probe streams and a
second carrier path are not part of this contract.

Global Route Mode does not override that boundary for DNS Tunnel. In particular,
`Direct` remains available for other profile types but cannot route DNS Tunnel or its
benchmark payload around the authenticated carrier.

## Rejected direct UDP upgrade

A restricted-LTE probe used the DNS Tunnel only for authenticated rendezvous and
retained one cellular UDP socket after successful STUN. DNS control succeeded, but
`n-de1` received no direct UDP and the client received neither server-first nor
simultaneous-punch probes. DNS bootstrap therefore did not open a direct `n-de1`
path under the tested carrier allowlist.

Do not build a direct `n-de1` QUIC or GOST path on this mechanism. Any future fast
transport must first prove that its endpoint is independently reachable through
the carrier allowlist; DNS remains the data path rather than a one-time bootstrap.

## Runtime constraints

- DNS Tunnel supports gVisor TUN only. System TUN cannot protect sockets opened by
  the separate Slipstream process. Proxy service mode is also rejected because it
  creates no Android VPN and cannot capture application traffic through gVisor.
- The Flow token and ephemeral loopback credentials reach the child only through
  stdin, not argv, environment variables or temporary files.
- VPN startup allows 15 seconds for aggregate carrier readiness; latency tests
  allow 5 seconds and are serialized process-wide.
- Automatic candidate exhaustion, carrier failure and FlowRelay failure are
  fail-closed. When Android changes the physical underlay, Owenclave closes the old
  child and restarts DNS Tunnel; automatic mode discovers the new network's DNS,
  while a manually selected resolver stays pinned. A child exit after readiness
  restarts the full DNS Tunnel session through the same bounded readiness gate; if no
  resolver becomes ready, the service stops instead of remaining falsely Connected.
  A user or fatal stop received during restart teardown cancels that queued restart.
  There is no periodic health check, background ranking, direct carrier, legacy SSH
  path or direct destination fallback.
- Stable runtime retains one resolver and one child. Busy uses the existing 50 ms
  pacing and 400 ms keepalive; Warm polls at most once per 400 ms; quiet open streams
  poll at most once per 2 seconds; empty connections do not explicitly poll. Quiet
  and empty keepalive remains 5 seconds.
- The TCP DNS adapter uses 48 serial workers with a separate 64-query queue;
  increasing workers must not implicitly grow the queue. Each persistent resolver
  connection has one outstanding exchange, with no pipelining. A clean-APK six-pair
  LTE comparison measured +34.9% paired download, -6.6% upload and improved loaded
  p95 on the tested TCP resolver; this is path-specific, not a universal guarantee.
  See [performance experiments and acceptance](goals/2026-09-07-dns-tunnel-performance.md).
- A captured TCP flow whose carrier dial fails is closed and removed from the active
  connection set immediately; failed flows are not retained until VPN shutdown.
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
