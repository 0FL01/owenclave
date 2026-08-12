# Goal: Ship a carrier-only Owenclave fork

Status: active
Source: user-approved 2026-08-12 protocol-pruning plan and fresh-install constraint
Last updated: 2026-08-12

## Objective

Ship a fresh-install Owenclave APK whose only user profile families are DNS Tunnel
and olcRTC, while olcRTC retains Jitsi, Jazz, Telemost and WBStream capabilities.
Remove the unused and weaker profile families from Android, packaging and the linked
core, then prove the retained paths on an arm64 Android device.

## Execution Directive

Complete the frozen Required Outcomes using the listed Change Envelope and Primary
Evidence. Work on the smallest unresolved outcome. Do not add requirements from
reviews, tests, tools, speculative risks or optional source text. Finish when every
required outcome is resolved and affected constraints remain satisfied. Commit and
push each verified implementation checkpoint requested by the user.

## Frozen Contract

### Required Outcomes

- R1: Expose and persist only DNS Tunnel and olcRTC profiles on a fresh install.
  - Source: user request to remove SS, VLESS and the other weak protocol families,
    retaining whitelist-oriented DNS Tunnel, Jazz and Jitsi; later correction to
    retain Telemost and WBStream too.
  - Acceptance: the profile model, creation UI, import/export and runtime compiler
    accept only `TYPE_DNSTT=17` and `TYPE_OLCRTC=31`. DNS Tunnel retains token/manual
    resolver setup and benchmarking. olcRTC retains the providers `jitsi`, `jazz`,
    `telemost` and `wbstream` and the transports required by those providers.
  - Primary evidence: source/manifest inventory, clean release compilation and
    fresh-install UI inspection showing no other profile family or ingress.
  - Status: pending
  - Evidence:

- R2: Remove obsolete protocol implementations and native payloads outside the
  retained paths.
  - Source: approved plan to make the fork materially smaller rather than only hide
    old UI.
  - Acceptance: old Bean/format/parser/settings/runtime/plugin families are absent;
    the arm64 APK contains `libslipstream.so`, `libolcrtc.so` and `libgojni.so`, and
    contains neither legacy `libdnstt.so` nor `libnaive.so`. A clean native build
    creates and verifies exactly the retained sidecars.
  - Primary evidence: compile/build checks, APK entry inventory and before/after APK
    size measurement.
  - Status: pending
  - Evidence:

- R3: Remove legacy proxy implementations from the linked Exclave core.
  - Source: approved final phase of the pruning plan.
  - Acceptance: a pinned reproducible minimal core accepts the configs generated for
    DNS Tunnel and olcRTC, retains gVisor TUN, routing/DNS/stats, SOCKS/UoT,
    IPC/dokodemo, freedom/blackhole and TCP/UDP, and does not link the removed profile
    protocol packages. Its compressed APK contribution is at least 20 percent smaller
    than the current `14,736,531` byte `libgojni.so` entry.
  - Primary evidence: clean arm64 core build, dependency/entry comparison and retained
    Android runtime acceptance.
  - Status: pending
  - Evidence:

- R4: Produce and validate the final fresh-install arm64 APK.
  - Source: user request for iterative builds and ADB device tests from start to end.
  - Acceptance: a signed release APK installs after removal of the previous app data;
    DNS Tunnel automatic/manual operation and benchmark pass through gVisor with one
    Slipstream child and expected egress, and available olcRTC Jitsi/Jazz/Telemost/
    WBStream profiles still reach sidecar readiness and payload for the configurations
    present in the acceptance environment. Cancellation and stop leave no child.
  - Primary evidence: release build/signature, bounded ADB observations and matching
    end-to-end path evidence without traffic logging.
  - Status: pending
  - Evidence:

### Constraints

- C1: This is a fresh-install fork. Do not implement migration or compatibility for
  old profile rows, backups or subscriptions; keep type IDs 17 and 31 stable.
- C2: Preserve the DNS Tunnel wire/runtime contract, gVisor-only operation, single
  resolver/child behavior, authenticated loopback SOCKS/UoT and fail-closed routing.
- C3: Preserve olcRTC client/server wire behavior and all four approved providers.
  Do not slim `libolcrtc.so` by removing Goolom, LiveKit or required media transports.
- C4: Do not print or commit tokens, room values, encryption keys, profile links,
  keystores or credential databases. Generated olcRTC YAML must not be logged.
- C5: Add no replacement proxy protocol, migration layer, background discovery,
  persistent benchmark state or new dependency.

### Non-goals

- Supporting in-place upgrades, old databases, generic backups, subscriptions or
  removed protocol links.
- Changing DE server topology, rooms, credentials, Slipstream/FlowRelay wire format or
  olcRTC protocol semantics.
- Renaming the package/application or redesigning routing, Compose or persistence
  beyond deletion required by the two-profile fresh schema.
- Shrinking olcRTC itself in this objective.

## Change Envelope

- Target: the Owenclave Android app, its native build inputs and the smallest pinned
  Exclave-core fork required to remove linked legacy protocols.
- Expected paths and direct consumers:
  - `app/src/main/java/io/nekohasekai/sagernet/{database,fmt,group,bg,ui,ktx}/`;
  - `app/src/main/{AndroidManifest.xml,res}/`, Room schema and app dependencies;
  - `settings.gradle.kts`, `bin/`, `library/core/` and GitHub build workflows;
  - `AGENTS.md`, `README.md`, stable DNS/Android docs and this goal document;
  - ignored generated `app/libs/` and `app/src/main/jniLibs/` only as build outputs.
- Allowed artifacts: source deletions, one fresh Room schema, a local pinned minimal
  core source/build boundary, release APKs and bounded build/device evidence.
- Forbidden artifacts: migration code, copied secrets/profile databases, server
  changes, traffic logs, packet captures, new protocol/runtime dependency or
  unrelated UI refactoring.
- User budget: complete iteratively from start to finish; commit and push verified
  checkpoints, build release APKs and test on the connected ADB device.

## Current Checkpoint

- Closes: R1 and the reproducibility prerequisite of R2.
- Smallest next action: reduce the fresh-install Android model/ingress to DNSTT and
  OLCRTC, remove stale native/plugin packaging, make the clean retained-native build
  reproducible, then compile before deleting deeper runtime consumers.
- Expected evidence: source references are reduced to retained/shared infrastructure,
  clean Kotlin compilation passes, and an APK contains only the three required native
  artifacts.
- Stop or replan if: a retained profile requires any removed Bean/profile branch, or a
  reproducible olcRTC build cannot retain all four approved providers.

## Current State

- Resolved: RECON proved DNS Tunnel is type 17; Jazz, Jitsi, Telemost and WBStream all
  use type 31 and one `libolcrtc.so`. Generic user SOCKS profiles are removable, but
  internal SOCKS/UoT, routing, DNS, freedom and gVisor are retained infrastructure.
- Last relevant evidence: clean Owenclave `dev` at `a53dbe7`; current arm64 APK is
  `43,224,565` bytes. Its compressed native entries include `libgojni.so` 14,736,531,
  `libolcrtc.so` 10,497,864, stale `libdnstt.so` 3,307,860, `libnaive.so` 3,306,635
  and retained `libslipstream.so` 2,827,849 bytes.
- Blocker: none for the current checkpoint.
- Next: implement R1/R2 in compile-safe vertical slices, commit the verified Android
  checkpoint, then prototype and measure the narrow core for R3.

## Material Decisions

- 2026-08-12: fresh install only; no database/profile migration is required.
- 2026-08-12: retain the whole existing olcRTC provider/transport capability because
  Telemost/Goolom and WBStream/LiveKit are approved reserve carriers.
- 2026-08-12: remove legacy `libdnstt.so`; the retained DNS Tunnel uses Rust
  Slipstream, not the old dnstt executable.
- 2026-08-12: remove Naive from both product and packaging; retain no optional legacy
  plugin path.
- 2026-08-12: measure real native/APK reduction; hiding picker entries alone does not
  satisfy the objective.

## Checkpoint History

- 2026-08-12: contract frozen after parallel source, provisioning, native-size,
  olcRTC and minimal-core RECON. R1 is current.

## Completion

- Resolved outcomes:
- Commands and artifacts:
- Constraint and diff-scope check:
- Final status:
