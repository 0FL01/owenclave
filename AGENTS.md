# Owenclave

Android proxy client based on Exclave/SagerNet. Application ID is
`org.owenewans.owenclave`; Kotlin namespace is `io.nekohasekai.sagernet`.

## Map

- `docs/dns-tunnel.md` - stable Android DNS Tunnel, Slipstream and FlowRelay contract and ownership boundary.
- `docs/android-network-routing.md` - TUN, per-app and network-type routing terminology and ownership.
- `app/src/main/java/io/nekohasekai/sagernet/fmt/dnstt/DnsttFmt.kt` - token, resolver and automatic candidate validation.
- `app/src/main/java/io/nekohasekai/sagernet/fmt/dnstt/DnsttBean.java` - dedicated DNS Tunnel profile persistence.
- `app/src/main/java/io/nekohasekai/sagernet/fmt/ConfigBuilder.kt` - authenticated local carrier boundary.
- `app/src/main/java/io/nekohasekai/sagernet/bg/proto/SlipstreamInstance.kt` - single-resolver Rust Slipstream lifecycle and optional TCP DNS adapter.
- `app/src/main/java/io/nekohasekai/sagernet/bg/proto/V2RayInstance.kt` - sidecar ownership and gVisor restriction.
- `bin/lib/slipstream/build.sh` - pinned arm64 Android client artifact and patches.
- `bin/lib/slipstream/flowd/` - private FlowRelay backend and focused tests.

## DNS Tunnel rules

- DNS Tunnel uses `DnsttBean`/`TYPE_DNSTT`; generic SSH profiles and `ssh://` or
  `dnstt://` provisioning are intentionally absent.
- Automatic setup accepts only a 32-lowercase-hex token and snapshots the first two
  unique non-VPN underlay DNS addresses, then TCP `77.88.8.8:53` and
  `77.88.8.1:53`. Manual mode accepts exactly one `udp://host:port` or
  `tcp://host:port` override.
- Try automatic candidates sequentially under one deadline. Stop after real
  `Connection ready` and retain one child/resolver. A physical underlay change
  restarts DNS Tunnel; automatic mode takes a fresh DNS snapshot and a pinned
  resolver remains strict.
- Keep DNS benchmarking explicit and foreground-only. Test candidates sequentially
  with one child, select one available Cloudflare/OVH/Hetzner download host for the
  whole run, rank sustained speed before latency, persist only a resolver the user
  selects, and never add multipath, background ranking, persistent benchmark cache
  or periodic health checks.
- Rust Slipstream accepts authenticated loopback SOCKS5 and emits FlowRelay OPEN before payload. Pass the Flow token and ephemeral local credentials only through child stdin; do not add dnstt, multipath or direct-carrier fallback.
- DNS Tunnel supports gVisor TUN only. A child process cannot use the System TUN socket-protection path.
- Keep one application flow per local TCP connection and independent Slipstream QUIC stream. Do not enable Exclave mux/smux or add health-probe streams.
- Keep Slipstream polling activity-aware: Busy retains pacing and 400 ms keepalive,
  Warm permits one poll per 400 ms, quiet open streams one poll per 2 seconds, and
  empty connections no explicit polls. Quiet and empty keepalive remains 5 seconds.
- Bound aggregate DNS carrier readiness to 15 seconds for VPN startup and 5 seconds for latency tests; candidate exhaustion stays fail-closed.
- Serialize DNS Tunnel latency tests process-wide and keep launch, cancellation and cleanup under one test owner so benchmark children never overlap.
- The bundled Slipstream client is currently arm64-only and generated under ignored `jniLibs/`; a clean build must run the pinned build script.
- Never commit or print Flow tokens, private keys, complete connection links,
  keystores or credential databases.

## Build

Prerequisites: JDK 21, Go 1.26+ with Go Mobile, Android SDK 37.0,
Build-Tools 37.0.0 and NDK r29. `nix develop` is the supported environment.

```sh
./run lib core
./bin/lib/slipstream/build.sh
./gradlew :app:downloadAssets
./gradlew :app:assembleOssRelease
```

Release APKs are written to `app/build/outputs/apk/oss/release/`.

## Verify

```sh
./gradlew :app:compileOssReleaseKotlin
./gradlew :app:lintOssRelease
./gradlew test
git diff --check
```

For DNS Tunnel runtime changes, also install the arm64 APK and prove automatic and
manual setup, one live child, exact payload through gVisor and the expected remote
egress.
