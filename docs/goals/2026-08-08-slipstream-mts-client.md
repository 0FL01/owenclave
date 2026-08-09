# Goal: MTS-ready Slipstream Owenclave client

Status: complete
Source: user requirement that the DNS VPN must work through the MTS whitelist, 2026-08-08
Last updated: 2026-08-08

## Objective

Replace Owenclave's inactive dnstt carrier with Rust Slipstream using the public
DNS-over-TCP multipath pair already proven through the MTS whitelist.

## Execution Directive

Complete the frozen Required Outcomes using the listed Change Envelope and
Primary Evidence. Work on the smallest unresolved outcome. Do not add
requirements from reviews, tests, tools, speculative risks, or optional source
text. Finish when every required outcome is resolved and affected constraints
remain satisfied.

## Frozen Contract

### Required Outcomes

- R1: run pinned Rust Slipstream through production DNS-over-TCP adapters.
  - Source: user rejects a carrier that cannot cross the MTS whitelist.
  - Acceptance: the existing DNS Tunnel profile starts the pinned Android Rust
    client with `77.88.8.3` and `188.0.190.35` as two TCP DNS paths and waits for
    the deterministic QUIC ready event before reporting ready.
  - Primary evidence: focused Kotlin compile, packaged Android ELF and runtime
    readiness on the connected Android device.
  - Status: verified
  - Evidence: the packaged pinned ELF started with both local DNS-over-TCP paths;
    `77.88.8.3` was intentionally first because it was the reliable MTS single.
    Owenclave reported carrier ready in one second on the final Android run.

- R2: preserve the existing whole-device VPN and authenticated egress path.
  - Source: the requested product remains an Android VPN/proxy with n-de1 WARP
    egress.
  - Acceptance: an imported existing profile carries one exact payload through
    Android VpnService, Rust Slipstream, strict SSH and WARP with no direct
    carrier fallback.
  - Primary evidence: exact byte count and SHA-256 plus observed carrier and VPN
    state on Android.
  - Status: verified
  - Evidence: the final installed APK carried an exact 4096-byte payload with
    SHA-256 `ad7facb2586fc6e966c004d7d1d16b024f5805ff7cb47c7a85dabd8b48892ca7`
    through VpnService and the private SSH target in 488 ms. Its external egress
    digest matched the live `warpns` WARP egress and differed from host direct.

- R3: deliver an installable arm64 APK.
  - Source: user instructed moving to the next implementation step.
  - Acceptance: the tested arm64 APK path and SHA-256 are recorded.
  - Primary evidence: successful assemble, install and artifact digest.
  - Status: verified
  - Evidence: `Owenclave-0.17.50-arm64-v8a-debug.apk` assembled, installed and
    ran on the connected Android device. SHA-256 is
    `ec9d9721c6634026d5eaba980898606db158a956c688bee5e1d0417e1c68d9f2`.

### Constraints

- C1: use the pinned Android Rust client from `dnstt-xyz` v2.2.0, Slipstream
  commit `bc772dd07d9a136dbd7553b0da575526de207847`, binary SHA-256
  `a68762441b79abda89ef1c68437e93f1eb706c52960a9bb12cedec8a1cb067ac`.
- C2: preserve the existing SSH bean, import link, host-key pin, private SSH
  target and isolated WARP egress; no Room migration or direct fallback.
- C3: support gVisor TUN only; System TUN remains rejected.
- C4: discard carrier output and do not add traffic logs or secrets to Git.
- C5: use only the MTS-proven pair `188.0.190.35,77.88.8.3`; do not retain a
  slower resolver or add automatic tuning.

### Non-goals

- dnstt fallback inside the APK, System TUN or seamless path failover.
- DoT, DoH, UDP resolver selection UI or independent session bonding.
- Non-arm64 APK support before the arm64 runtime path passes.

## Change Envelope

- Target: existing DNS Tunnel runtime ownership in Owenclave.
- Expected paths: sidecar lifecycle, executable inventory, pinned arm64 build
  script/artifact, public server certificate and this goal.
- Allowed artifacts: one Kotlin DNS-over-TCP adapter, one pinned client binary,
  one public certificate and one arm64 APK.
- Forbidden artifacts: database migration, new profile type, public proxy,
  persistent carrier output, credentials or server changes.
- Budget: reuse the current profile and one runtime path; no parallel legacy
  carrier implementation.

## Current Checkpoint

- Closes: none; the closure check is terminal.
- Smallest next action: none inside this goal.
- Expected evidence: complete.
- Stop or replan if: a later APK changes the resolver transport or carrier.

## Current State

- Resolved: the MTS-proven resolver transport is integrated into Owenclave and
  the final APK passed Android VpnService payload and WARP egress.
- Last relevant evidence: one-second carrier readiness and exact final payload.
- Blocker: none.
- Next: none.

## Material Decisions

- 2026-08-08: fixed Tele2 `.174/.175` is rejected for MTS; use the public pair
  proven by actual Slipstream through the MTS TCP DNS whitelist.
- 2026-08-08: retain the existing `dnstt://` profile contract while replacing
  only its runtime carrier; this avoids a migration and a second credential path.
- 2026-08-08: put reliable MTS single `77.88.8.3` first; `188.0.190.35` remains
  the second multipath lane.
- 2026-08-08: use Rust's deterministic `Connection ready` event for startup.
  Repeated SSH-banner probes created competing QUIC streams and caused false
  three-minute readiness failures; strict SSH is instead proved by the exact
  VpnService payload.
- 2026-08-08: removed the visible Copy logs shortcut at the user's request and
  restored the original Logcat screen reader before the final build.

## Completion

- Resolved outcomes: R1, R2 and R3 verified.
- Commands and artifacts: Docker JDK 21 `:app:assembleOssDebug`, pinned client
  digest verification, Android install, deterministic carrier readiness, exact
  payload and WARP egress comparison.
- Constraint and diff-scope check: existing profile/import/SSH/VpnService reused;
  no Room migration, direct fallback, server change, traffic output or secret in
  Git; test target and namespace alias removed; live carrier has zero restarts.
- Final status: complete.
