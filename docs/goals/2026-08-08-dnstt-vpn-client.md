# Goal: DNS Tunnel VPN client

Status: complete
Source: user instruction, 2026-08-08
Last updated: 2026-08-08

## Objective

Owenclave imports one DNS Tunnel connection link and runs Android VPN traffic
through `dnstt`, recursive DNS and the authenticated SSH egress on `n-de1`.

## Execution Directive

Complete the frozen Required Outcomes using the listed Change Envelope and
Primary Evidence. Work on the smallest unresolved outcome. Do not add
requirements from reviews, tests, tools, speculative risks, or optional source
text. Finish when every required outcome is resolved and affected constraints
remain satisfied.

## Frozen Contract

### Required Outcomes

- R1: Owenclave stores and imports a DNS Tunnel profile.
  - Source: user wants to paste keys and connect, including link import.
  - Acceptance: one deterministic `dnstt://` link imports the tunnel domain,
    dnstt server public key, SSH username/private key and pinned SSH host key
    into one selectable profile.
  - Primary evidence: focused parser/serialization checks and profile visible on
    the Android device after import.
  - Status: verified
  - Evidence: the generated link imported as `DNS Reserve App`, survived APK
    updates and exported through the Android share action with the DNS Tunnel
    fields intact.

- R2: Owenclave carries Android VPN traffic through DNS to `n-de1`.
  - Source: user selected Owenclave as the Android client for the working DNS
    reserve proxy.
  - Acceptance: the selected profile starts bundled dnstt, connects the existing
    SSH outbound only to the dnstt loopback listener and transfers a controlled
    payload from another Android app through the established VPN.
  - Primary evidence: installed APK, Android VPN state and end-to-end payload
    proof through the live delegated DNS path.
  - Status: verified
  - Evidence: the installed APK started bundled dnstt through the delegated
    zone, authenticated the existing core SSH outbound and transferred an exact
    4096-byte controlled payload from Termux through Android VpnService.

- R3: Owenclave performs ordered resolver failover.
  - Source: the reserve proxy must use several working whitelist resolvers.
  - Acceptance: the client tries underlying-network UDP DNS, Yandex DoT and
    Cloudflare DoH without a direct TCP fallback; after the active dnstt process
    fails, a later profile restores new proxy traffic automatically.
  - Primary evidence: controlled Android failover with successful payload after
    profile rotation.
  - Status: verified
  - Evidence: killing the active Android dnstt child triggered the ordered
    resolver sequence, re-established the SSH-banner gate and restored the exact
    4096-byte payload without a direct transport fallback.

- R4: a tested APK and connection link are delivered.
  - Source: explicit user request for the APK location and connection link.
  - Acceptance: the tested APK remains at a stable workspace path; the secret
    link is imported on the device and can be copied through Owenclave without
    being committed or printed in tool output.
  - Primary evidence: APK artifact, package/version state and successful import
    from the same generated link.
  - Status: verified
  - Evidence: the same generated link passed Android import/export. The tested
    APK is `app/build/outputs/apk/oss/debug/Owenclave-0.17.50-arm64-v8a-debug.apk`
    with SHA-256 `e110ce9ed8de6d861bbf6e99a5655e19df8253bb42efad7e85be39378555a298`.

### Constraints

- C1: external Android-to-server traffic uses DNS resolvers and public UDP `:53`;
  no direct SSH fallback is added.
- C2: the dnstt private key remains only on `n-de1`; client SSH private material
  is not written to Git, logs, goal text or tool output.
- C3: preserve the pre-existing `V2RayInstance.kt` diff and unrelated workspace
  state.
- C4: reuse the existing SSH outbound and Android VpnService; do not add a second
  VPN implementation or Room entity type.
- C5: the first runtime supports the default gVisor TUN path. Unsupported System
  TUN must fail explicitly rather than loop traffic into the VPN.

### Non-goals

- Seamless preservation of existing TCP sessions during resolver failover.
- Multipath, bonding or throughput tuning.
- A new public SOCKS service or recursive DNS service on `n-de1`.
- Changes to olcRTC, Jazz, Xray or WARP.
- Provisioning UI or a remote account-management API.

## Change Envelope

- Target: SSH profile model/config, dnstt process lifecycle, Compose profile UI,
  URI import/export and bundled Android executable.
- Expected paths: `SSHBean.java`, `ConfigBuilder.kt`, `V2RayInstance.kt`, one
  dnstt runtime class, Compose profile files, `Formats.kt`, `ProxyEntity.kt`,
  `Executable.kt`, `AndroidManifest.xml`, `bin/lib/dnstt/` and `jniLibs`.
- Allowed artifacts: pinned official dnstt source build, four ABI executables,
  generated APK and one test-only client credential outside Git.
- Forbidden artifacts: database migration, new network dependency in app code,
  secrets in repository/tool output and direct carrier fallback.
- Budget: one profile type reused through `SSHBean`, one sidecar supervisor and
  one Android device E2E path.

## Current Checkpoint

- Closes: R1-R4.
- Smallest next action: none; closure check passed.
- Expected evidence: complete.
- Stop or replan if: a new objective is approved.

## Current State

- Resolved: R1-R4.
- Last relevant evidence: final APK installed as version `0.17.50`; Android
  import/export, VPN payload and controlled resolver failover passed.
- Blocker: none.
- Next: none.

## Material Decisions

- 2026-08-08: reuse `TYPE_SSH` and versioned `SSHBean`; no Room migration.
- 2026-08-08: dnstt is a raw TCP sidecar; the existing SSH outbound remains the
  authenticated proxy and `n-de1` egress owner.
- 2026-08-08: recursive resolvers are carriers, not backend services.

## Checkpoint History

- 2026-08-08: RECON located the existing SSH, VpnService, external process and
  Compose integration paths; baseline Kotlin compile passed in Docker JDK 21.
- 2026-08-08: one DNS-mode SSH profile, bundled dnstt lifecycle and deterministic
  URI import/export compiled and installed.
- 2026-08-08: corrected the SSH host-key algorithm pin and disabled core SSH
  keepalive for the high-latency DNS carrier; exact VPN payload passed.
- 2026-08-08: controlled child failure rotated resolver profiles and restored
  new payload traffic; final APK and share link passed on Android.

## Completion

- Resolved outcomes: R1-R4 verified.
- Commands and artifacts: Docker JDK 21 `:app:assembleOssDebug`, installed APK,
  Android deep-link import/share, exact 4096-byte payload and child-kill failover.
- Constraint and diff-scope check: SSH/VpnService were reused, System TUN is
  rejected, no Room migration or direct carrier fallback was added, and secrets
  remain outside Git.
- Final status: complete.
