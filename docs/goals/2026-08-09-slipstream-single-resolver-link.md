# Goal: Provisioned single-resolver Slipstream VPN

Status: unmet
Source: user instruction to remove multipath and pass DNS configuration inside the `dnstt://` key, 2026-08-09
Last updated: 2026-08-09

## Objective

Import one resolver with a DNS Tunnel profile and use only that resolver for one
Rust Slipstream path through the existing SSH, gVisor VPN and WARP chain.

## Execution Directive

Complete the frozen Required Outcomes using the listed Change Envelope and
Primary Evidence. Work on the smallest unresolved outcome. Do not add
requirements from reviews, tests, tools, speculative risks, or optional source
text. Finish when every required outcome is resolved and affected constraints
remain satisfied.

## Frozen Contract

### Required Outcomes

- R1: remove temporary state from the superseded multipath experiments.
  - Source: user requested cleanup before the architecture change.
  - Acceptance: no temporary target, listener, address alias, Android test
    process or adaptive test file remains.
  - Primary evidence: bounded n-de1 and Android process/file/listener checks.
  - Status: verified
  - Evidence: `/run/slipstream-adaptive`, the private `:19090` target and its
    temporary namespace address were removed; no Owenclave, Slipstream or
    `adaptive-*` Android test process/file remained.

- R2: import and persist exactly one resolver in the `dnstt://` profile.
  - Source: user prohibited hardcoded DNS and requires DNS configuration inside
    the imported key.
  - Acceptance: `resolver=udp://host:port` or `resolver=tcp://host:port` is
    required, survives profile persistence/export and rejects missing, malformed
    or multiple resolver values.
  - Primary evidence: focused parser/serialization check and redacted Android
    import/export observation.
  - Status: verified
  - Evidence: `resolver=tcp://77.88.8.3:53` was added to the private link,
    imported without exposing credentials and survived APK replacement. The
    persisted profile reconnected through TCP sockets to that exact endpoint.

- R3: run one Rust Slipstream resolver path with no automatic alternative.
  - Source: user explicitly removed multipath from the architecture.
  - Acceptance: runtime emits exactly one `--authoritative` from the profile;
    no hardcoded resolver, probing, ranking, rotation, multipath, adaptive
    tuning, debug polling or direct carrier fallback remains.
  - Primary evidence: changed runtime inspection and one live carrier process.
  - Status: verified
  - Evidence: the installed process had one `--authoritative`, no
    `--debug-poll`, one `libslipstream.so` process and no resolver inventory,
    probing, ranking or fallback in the DNS Tunnel runtime.

- R4: prove the imported profile as an Android VPN after abrupt load cancellation.
  - Source: the architecture change replaces the path that regressed after a
    cancelled speed test.
  - Acceptance: an exact 4096-byte payload passes before load and again after a
    bounded abrupt-cancel sequence without manually reconnecting the VPN.
  - Primary evidence: byte count, SHA-256 and monotonic post-cancel recovery time
    on the connected Android device.
  - Status: blocked
  - Evidence: the exact pre-load payload passed, but eight downloads cancelled
    after 15 seconds left the next probe with an empty response beyond 60
    seconds. Bounding server read-ahead to one 4096-byte chunk failed; combining
    it with a 1 MiB Android receive window also failed. Both experiments were
    rolled back. The persistent SSH connection and all its application channels
    occupy one ordered Slipstream stream, so abandoned bytes cannot be skipped.

- R5: produce the tested arm64 APK.
  - Source: continuing Android client implementation scope.
  - Acceptance: the installed APK path and SHA-256 are recorded after R2-R4 pass.
  - Primary evidence: successful focused APK build, install and artifact digest.
  - Status: verified
  - Evidence: the final arm64 APK installed and connected in two seconds at
    `app/build/outputs/apk/oss/debug/Owenclave-0.17.50-arm64-v8a-debug.apk`,
    SHA-256 `90f6b9771b57480cff7baca54991f4cb754b112e3760da1c938ed69db8b76409`.
    Its external egress digest matched `warpns` WARP and differed from host
    direct egress.

### Constraints

- C1: retain Rust Slipstream, strict SSH authentication and host pin, the private
  SSH target, isolated WARP egress and gVisor VpnService.
- C2: no Room migration, direct carrier fallback, persistent traffic logs,
  credentials in Git or unrelated dirty-file changes.
- C3: preserve the untracked keystore and pre-existing deleted ABI `.gitkeep`
  state.

### Non-goals

- Multipath, resolver discovery, ranking, failover, System TUN, DoT/DoH, server
  redesign or seamless preservation of existing application TCP sessions.

## Change Envelope

- Target: DNS Tunnel profile serialization/import and the Rust Slipstream
  single-resolver sidecar.
- Expected paths: `SSHBean`, `DNSTTFmt`, `ConfigBuilder`, Compose profile mapping,
  `SlipstreamInstance`, obsolete dnstt runtime removal, this goal and regenerated
  arm64 APK. R4 may additionally change one pinned Rust server queue bound and
  the installed n-de1 server binary if the unchanged server fails its direct
  cancellation test.
- Allowed artifacts: one resolver URI field, one optional TCP DNS adapter and
  redacted runtime evidence.
- Forbidden artifacts: hardcoded DNS addresses, another carrier runtime,
  database migration, persistent diagnostic output or secrets.
- Budget: one resolver, one Slipstream process and the existing SSH/VPN path.

## Current Checkpoint

- Closes: none inside the approved envelope.
- Smallest next action: choose a new inner boundary where application
  connections do not share one persistent ordered SSH stream.
- Expected evidence: a cancelled high-volume connection can be reset without
  discarding unrelated connections.
- Stop or replan if: SSH must remain one persistent Slipstream stream.

## Current State

- Resolved: cleanup, link-owned resolver, single-path runtime, APK build/import
  and normal WARP egress.
- Last relevant evidence: normal payload and egress passed; the post-cancel
  exact probe failed after both bounded queue experiments.
- Blocker: ordered abandoned data inside the persistent SSH-over-Slipstream TCP
  stream cannot be selectively reset by the current app/runtime boundary.
- Next: external decision on replacing that inner boundary.

## Material Decisions

- 2026-08-09: use one canonical resolver URI so transport and endpoint cannot
  diverge; no implicit default exists.
- 2026-08-09: retain Rust's `Connection ready` lifecycle event; SSH-banner probes
  create competing streams and are not a valid health check.
- 2026-08-09: R2-R3 compiled and the imported TCP resolver connected with one
  Rust process, but R4 reproduced an empty response for more than 120 seconds
  after eight downloads were cancelled. Source inspection found a 4-16 MiB
  server target-reader queue ahead of QUIC. The prior server flow-window setting
  constrained the opposite direction; the smallest owner-level fix is the
  pinned server's one-line `mpsc::channel(1)` queue bound.
- 2026-08-09: bounding only the server target-reader queue did not recover the
  exact probe within 60 seconds and was rolled back. The next bounded experiment
  combines that source fix with a 1 MiB Android receive window, which constrains
  the correct server-to-client direction and remains sixteen times the QUIC
  reserve.
- 2026-08-09: the combined queue bound and correct-direction receive window also
  failed the 60-second post-cancel probe and were rolled back. Immediate recovery
  requires discarding the one ordered Slipstream stream, but it contains the
  persistent SSH connection and the app has no application-cancellation signal.
  Solving that requires a different inner proxy boundary or a core SSH lifecycle
  change, both outside the approved single-carrier change envelope.

## Completion

- Resolved outcomes: R1, R2, R3 and R5 verified; R4 remains blocked by the
  preserved persistent SSH boundary.
- Commands and artifacts: focused Kotlin compile, arm64 debug assemble/install,
  redacted import, one-process/one-authority inspection, exact payload,
  cancellation reproduction and WARP egress comparison.
- Constraint and diff-scope check: one profile resolver and one Rust path remain;
  both failed server/client queue experiments were rolled back and temporary
  targets were removed.
- Final status: unmet because stable post-cancel recovery cannot be delivered
  inside the approved single-stream SSH architecture.
