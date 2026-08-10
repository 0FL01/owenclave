# Goal: Replace SSH proxy with automatic DNS Tunnel setup

Status: active
Source: user-approved full replacement after the 2026-08-10 Android DNS resolver RECON
Last updated: 2026-08-10

## Objective

Remove Owenclave's generic SSH proxy feature and replace the legacy SSH-backed DNS
Tunnel profile with one dedicated token-first profile. A user must be able to connect
by entering or scanning only a 32-hex Flow token; automatic mode must select one
working underlay or Yandex DNS resolver without recurring discovery work, while an
advanced manual mode accepts one exact resolver. The resulting arm64 APK must pass
Android ADB end-to-end and screen-off CPU/battery-mechanism gates.

## Execution Directive

Complete the frozen Required Outcomes using the listed Change Envelope and Primary
Evidence. Work on the smallest unresolved outcome. Do not add requirements from
reviews, tests, tools, speculative risks, or optional source text. Finish when every
required outcome is resolved and affected constraints remain satisfied. Stop
substantive work at a proven external blocker, an approved budget boundary, or when
no remaining in-scope action has a falsifiable expected result; record the exact
evidence and smallest unlock.

## Frozen Contract

### Required Outcomes

- R1: Remove the generic SSH proxy feature and give DNS Tunnel its own model.
  - Source: user instruction to remove SSH proxy completely from the Owenclave
    codebase because it is not used.
  - Acceptance: owned Android source has no generic SSH profile type, bean, URI
    import, create/edit UI, subscription conversion, generated outbound model or
    `ConfigBuilder` runtime branch. DNS Tunnel uses a dedicated `DnsttBean` and
    `TYPE_DNSTT`. The profile database migration removes legacy SSH/DNS Tunnel rows
    and their blob column while preserving unrelated profiles. Administrative SSH on
    operations nodes and unreachable upstream core internals are unaffected.
  - Primary evidence: targeted source search, Room schema/compile gate and release
    Kotlin/Java compilation.
  - Status: pending
  - Evidence:

- R2: Replace legacy provisioning with token-only automatic setup and one manual
  override.
  - Source: approved UX correction: quick setup accepts or scans only a 32-lowercase-
    hex token, embeds `t.x.ass-peak.de` and the existing certificate, defaults to
    automatic DNS, and offers manual DNS in advanced settings.
  - Acceptance: a new profile can be created from a raw token entered, pasted or
    scanned in the dedicated DNS Tunnel flow. The user does not enter a domain or
    resolver in automatic mode. Advanced manual mode accepts exactly one validated
    `udp://host:port` or `tcp://host:port` and uses only that resolver. Legacy
    `ssh://` and resolver-bearing `dnstt://` imports/exports are absent; old profiles
    are intentionally not migrated.
  - Primary evidence: focused token/resolver/model tests, release compilation and
    installed-device UI observation without printing the token.
  - Status: pending
  - Evidence:

- R3: Select exactly one automatic resolver within the existing fail-closed startup
  budget.
  - Source: approved resolver order and RECON proving Android exposes physical-link
    DNS through `LinkProperties`.
  - Acceptance: automatic candidates are the first two unique DNS addresses from the
    active non-VPN underlay, followed by UDP `77.88.8.8:53` and `77.88.8.1:53`, with
    duplicates removed. Candidates are attempted sequentially using real Slipstream
    `Connection ready`; at most one candidate child exists at a time and only the
    successful child remains. VPN startup shares one 15-second deadline and latency
    tests share one 5-second deadline across all attempts. Exhaustion fails closed.
    There is no simultaneous resolver use, multipath, periodic health probe, resolver
    ranking, persistent cache, Rostelecom hardcode or direct carrier fallback.
  - Primary evidence: focused candidate-order/deadline/cancellation tests and bounded
    Android process/readiness observations.
  - Status: pending
  - Evidence:

- R4: Preserve the accepted quiet-runtime battery and transport behavior.
  - Source: explicit requirement not to consume excessive Android battery or CPU and
    the accepted activity-aware scheduler baseline.
  - Acceptance: discovery and fallback stop after readiness; stable runtime retains
    one resolver, one Slipstream child, one application flow per QUIC stream, and the
    existing Busy/Warm/QuiescentOpen/Empty scheduler. No network-change polling or
    automatic underlay handover is added. In a matched ten-minute screen-off run,
    Slipstream CPU time and scheduler slices regress by no more than 10% from the
    accepted `4.214 CPU-s` and `10,521 slices` baseline. Existing 3-second screen-on
    statistics normalization remains intact.
  - Primary evidence: source/lifecycle inspection and a matched aggregate Perfetto
    trace on the connected Android device.
  - Status: pending
  - Evidence:

- R5: Ship and prove the full replacement on the connected Android phone.
  - Source: user instruction to implement end to end and then run ADB phone E2E.
  - Acceptance: the pinned arm64 Slipstream build and signed release APK build and
    install. A freshly created token-only automatic profile passes exact 4 KiB TCP,
    application UDP, 8 MiB TCP, WARP egress, cancellation recovery and ten-minute
    screen-off recovery through gVisor. Manual resolver mode independently connects
    and passes exact TCP and UDP. Android app/service/child PIDs remain stable during
    quiet acceptance, temporary probes/traces are deleted, and production
    `slipstream.service`/`flowd.service` restart counters do not grow.
  - Primary evidence: APK/native digests, bounded ADB probes and aggregate traces,
    plus read-only `n-de1` service/listener/restart observations.
  - Status: pending
  - Evidence:

### Constraints

- C1: Preserve the FlowRelay wire, 16-byte bearer token, bundled certificate pin,
  stdin-only bootstrap credentials, gVisor-only operation and WARP-only server egress.
- C2: Preserve one local application flow or UDP association per independent QUIC
  stream; do not add Exclave mux/smux, health streams or another carrier.
- C3: Preserve the Rust scheduler: Busy keeps the 50 ms slice and 400 ms keepalive,
  Warm permits one poll per 400 ms, QuiescentOpen one poll per 2 seconds, Empty no
  explicit polls, and quiet/empty keepalive remains 5 seconds.
- C4: Automatic resolver work is startup-only. A physical network change is handled
  on the next VPN connection; churn-heavy automatic handover remains excluded while
  the known server close/reconnect fatal path exists.
- C5: Do not expose or commit tokens, complete profile links, certificate private
  keys, keystores, credential databases, raw traces, packet captures or destination
  metadata. Server traffic logging remains disabled.
- C6: Preserve all non-SSH proxy profiles and their stored database rows when removing
  the shared legacy SSH/DNS Tunnel model.

### Non-goals

- Per-client tokens, token rotation/revocation UI or a server authentication change.
- Rostelecom or carrier-specific hardcoded resolver tables, MCC/MNC/APN detection,
  DNS-over-TLS/HTTPS or simultaneous resolver paths.
- Automatic Wi-Fi/LTE handover, zero-flow disconnect, server fatal-path repair,
  System TUN support or a second VPN implementation.
- Removing administrative SSH from operations nodes or forking upstream
  `libexclavecore` solely to delete an otherwise unreachable internal SSH protocol.
- Migrating or preserving legacy generic SSH and legacy resolver-bearing DNS Tunnel
  profiles.

## Change Envelope

- Target: Owenclave-owned profile/import/UI/config/lifecycle code, focused tests,
  Room schema, generated arm64 artifact and stable documentation.
- Expected paths and direct consumers:
  - `app/src/main/java/io/nekohasekai/sagernet/fmt/ssh/`
  - a dedicated DNS Tunnel bean/format package
  - `app/src/main/java/io/nekohasekai/sagernet/database/ProxyEntity.kt`
  - `app/src/main/java/io/nekohasekai/sagernet/database/SagerDatabase.kt`
  - `app/src/main/java/io/nekohasekai/sagernet/fmt/KryoConverters.java`
  - `app/src/main/java/io/nekohasekai/sagernet/fmt/TypeMap.kt`
  - `app/src/main/java/io/nekohasekai/sagernet/fmt/ConfigBuilder.kt`
  - SSH branches in URI, Clash, sing-box and V2Ray JSON importers
  - Compose and legacy profile creation/editing surfaces
  - `app/src/main/java/io/nekohasekai/sagernet/bg/proto/SlipstreamInstance.kt`
  - `app/src/main/java/io/nekohasekai/sagernet/bg/proto/V2RayInstance.kt`
  - focused tests, `AGENTS.md`, `README.md`, `docs/dns-tunnel.md`, operations docs
- Allowed artifacts: one Room migration preserving unrelated profiles, focused local
  tests, ignored generated arm64 native binary/release APK, memory-backed aggregate
  traces and disposable ADB probe files.
- Forbidden artifacts: new dependency/service/server runtime, resolver daemon,
  background worker, persistent resolver cache, packet logging/capture, secret-bearing
  output or committed generated binaries.
- User budget: full replacement rather than compatibility. Git history is the rollback
  for removed SSH and legacy DNS Tunnel behavior; use the first sufficient design.

## Current Checkpoint

- Closes: R1.
- Smallest next action: introduce the dedicated DNS Tunnel bean/type and database
  transition, then remove direct generic SSH consumers in one compile-bounded change.
- Expected evidence: Room schema generation, targeted source search and release
  Kotlin/Java compilation prove a dedicated DNS Tunnel profile with no reachable
  generic SSH feature.
- Stop or replan if: preserving unrelated profile rows requires destructive database
  reset, DNS Tunnel still needs generic SSH runtime fields, or upstream core changes
  become necessary for the owned Android feature removal.

## Current State

- Resolved: RECON only. Android already exposes underlay DNS; the existing olcRTC
  helper reads it. The connected phone exposes separate VPN and physical-link DNS.
- Last relevant evidence: repository `dev` was clean at `148384b`; live `n-de1`
  Slipstream/flowd were active with restart baselines 4/0, public UDP `:53`, private
  FlowRelay `:40001`, null output sinks and no private DNS SSH listener.
- Blocker: none.
- Next: commit this frozen goal, then execute R1.

## Material Decisions

- 2026-08-10: generic SSH proxy and all legacy SSH-backed DNS Tunnel provisioning are
  intentionally removed without profile compatibility; unrelated proxy rows remain.
- 2026-08-10: automatic mode uses up to two Android underlay DNS addresses, then the
  two official basic Yandex DNS IPv4 addresses; manual mode is one strict override.
- 2026-08-10: candidate selection is sequential and startup-only under one existing
  readiness deadline; no multipath, health polling, persistent cache or handover.
- 2026-08-10: the accepted activity-aware Rust scheduler and server runtime are reused
  unchanged; full replacement refers to the Android SSH/profile/provisioning path.

## Checkpoint History

- 2026-08-10: contract frozen from the approved RECON and UX/full-removal decisions;
  R1 is current.

## Completion

- Resolved outcomes:
- Commands and artifacts:
- Constraint and diff-scope check:
- Final status:
