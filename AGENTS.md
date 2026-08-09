# Owenclave

Android proxy client based on Exclave/SagerNet. Application ID is
`org.owenewans.owenclave`; Kotlin namespace is `io.nekohasekai.sagernet`.

## Map

- `app/src/main/java/io/nekohasekai/sagernet/fmt/ssh/DNSTTFmt.kt` - `dnstt://` import/export and resolver validation.
- `app/src/main/java/io/nekohasekai/sagernet/fmt/ssh/SSHBean.java` - versioned DNS Tunnel and SSH profile persistence.
- `app/src/main/java/io/nekohasekai/sagernet/fmt/ConfigBuilder.kt` - existing SSH outbound and local carrier boundary.
- `app/src/main/java/io/nekohasekai/sagernet/bg/proto/SlipstreamInstance.kt` - single-resolver Rust Slipstream lifecycle and optional TCP DNS adapter.
- `app/src/main/java/io/nekohasekai/sagernet/bg/proto/V2RayInstance.kt` - sidecar ownership and gVisor restriction.
- `bin/lib/slipstream/build.sh` - pinned arm64 Android client artifact and patches.
- `bin/lib/slipstream/flowd/` - private FlowRelay backend and focused tests.

## DNS Tunnel rules

- DNS Tunnel remains an `SSHBean`; do not add a Room entity or a second VPN implementation.
- Each profile must provide exactly one `resolver=udp://host:port` or `resolver=tcp://host:port`. Never hardcode, discover, rank, rotate or combine resolvers.
- Rust Slipstream accepts authenticated loopback SOCKS5 and emits FlowRelay OPEN before payload. Pass the Flow token and ephemeral local credentials only through child stdin; do not add dnstt, multipath or direct-carrier fallback.
- DNS Tunnel supports gVisor TUN only. A child process cannot use the System TUN socket-protection path.
- Keep one application flow per local TCP connection and independent Slipstream QUIC stream. Do not enable Exclave mux/smux or add health-probe streams.
- Bound aggregate DNS carrier readiness to 15 seconds for VPN startup and 5 seconds for latency tests; failures stay fail-closed without resolver fallback.
- Serialize DNS Tunnel latency tests process-wide and keep launch, cancellation and cleanup under one test owner so benchmark children never overlap.
- The bundled Slipstream client is currently arm64-only and generated under ignored `jniLibs/`; a clean build must run the pinned build script.
- Never commit or print private keys, complete connection links, keystores or credential databases.

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

For DNS Tunnel runtime changes, also install the arm64 APK and prove one imported
resolver, exact payload through gVisor and the expected remote egress.
