# Goal: Reduce Android DNS Tunnel battery drain

Status: active
Source: user-approved battery plan after the 2026-08-10 multi-agent code and plan audits
Last updated: 2026-08-10

## Objective

Reduce reproducible Android DNS Tunnel idle battery drain by fixing the notification
polling unit bug and replacing stream-count-driven Slipstream polling with a measured
activity-aware scheduler, while preserving FlowRelay behavior, bounded readiness,
background delivery, throughput, and fail-closed routing.

## Execution Directive

Complete the frozen Required Outcomes using the listed Change Envelope and Primary
Evidence. Work on the smallest unresolved outcome. Do not add requirements from
reviews, tests, tools, speculative risks, or optional source text. Finish when every
required outcome is resolved and affected constraints remain satisfied.

## Frozen Contract

### Required Outcomes

- R1: Attribute the excess drain before transport edits.
  - Source: approved CP0 measurement gate, superseded by the user-approved fast path.
  - Acceptance: record the live DNS profile power-affecting settings and compare it
    with one frozen direct Owenclave gVisor profile using aggregate CPU scheduling,
    radio-active, process, and WakeLock evidence without packet or destination capture.
    Continue only when the DNS child shows a material scheduler/radio mechanism absent
    from the direct control. Do not claim battery percentage or mAh from this fast path.
  - Primary evidence: bounded device settings/capability inventory and separate 10–15
    minute diagnostic traces for the DNS and direct profiles.
  - Status: verified
  - Evidence: the live profile used gVisor and a direct UDP resolver with profile
    statistics on at the persisted 3 ms defect, while app statistics, PCAP, WakeLock,
    battery exemption, and debug logging were off. Separate screen-off Perfetto traces
    showed the DNS-only Rust child consume 52.375 CPU seconds and 156,623 scheduler
    slices over 599.934 seconds, with a median 331.5 slices/second while running and
    20 quiet application flows retained. The matched direct gVisor control had no
    Slipstream child. Batterystats history correlated Owenclave activity with
    `cellular_high_tx_power`; server restart baselines stayed 4/0. Raw traces were
    deleted after aggregate extraction. This proves a large transport scheduling
    mechanism, not a battery percentage or mAh delta.

- R2: Correct the screen-on statistics interval independently.
  - Source: confirmed `3` milliseconds versus `3000` milliseconds unit defect and
    approved CP1.
  - Acceptance: a missing value defaults to `3000`; persisted exact `3` migrates to
    `3000`; `0` remains disabled; every other nonzero value below `500`, including
    negative values, normalizes to `500`; values `500` and above are unchanged.
    Screen-on profile statistics run at approximately three seconds and stop when
    the screen turns off.
  - Primary evidence: focused value matrix, release Kotlin compilation, and bounded
    live callback cadence observation.
  - Status: pending
  - Evidence:

- R3: Replace open-stream polling with one activity-aware client scheduler.
  - Source: approved CP2 and audited first-sufficient scheduler.
  - Acceptance: Busy preserves existing pacing, 400 ms keepalive, and 50 ms slice;
    Warm emits at most one explicit poll per 400 ms; QuiescentOpen emits at most one
    explicit poll per 2 seconds with the proven 5-second keepalive and no permanent
    50 ms clamp; Empty keeps zero explicit polls and the proven 5-second keepalive.
    OPEN, local/remote payload, and local/remote FIN restore Busy immediately. QUIC
    ACK, PTO, retransmission, close, and path deadlines always take precedence.
  - Primary evidence: focused Rust transition/deadline tests, clean pinned patch
    replay, arm64 source build digest, and aggregate carrier/loop-wake observations.
  - Status: pending
  - Evidence:

- R4: Prove measured efficacy and preserve the accepted runtime contract.
  - Source: approved CP3 efficacy and regression gate, superseded by the user-approved
    fast path.
  - Acceptance: a 10–15 minute current-versus-scheduler diagnostic shows at least 80%
    fewer Slipstream scheduler wakeups for quiet open flows and no more than one carrier
    query per second in QuiescentOpen. Remote quiescent data/FIN p99 stays within 2.5
    seconds, 8 MiB throughput regresses by no more than 10%, 4 KiB TCP and application
    UDP median regress by no more than 100 ms, and exact TCP/UDP, cancellation, WARP,
    ten-minute screen-off, process, and restart gates pass. Report mechanism evidence
    only; do not claim battery percentage or mAh improvement.
  - Primary evidence: current and scheduler aggregate traces plus exact Android runtime
    probes and server restart inventory.
  - Status: pending
  - Evidence:

### Constraints

- C1: Preserve the existing FlowRelay wire, certificate pin, stdin-only credentials,
  one profile-supplied resolver, one app flow per QUIC stream, gVisor-only operation,
  and 15-second VPN/5-second latency-test fail-closed readiness.
- C2: Do not add fallback, resolver ranking/rotation, mux, health-probe streams,
  traffic or destination logging, PCAP, packet capture, persistent measurement dumps,
  a service, dependency, schema, Exclave fork, JNI boundary, or server protocol change.
- C3: Keep the negotiated-30-second QUIC connection safe: quiescent and empty
  ACK-eliciting keepalive remains five seconds in this objective.
- C4: Production `slipstream.service` and `flowd.service` restart counts must not grow
  during controlled validation. The known server close/reconnect crash blocks
  zero-flow dormancy and any churn-heavy transport work.
- C5: Do not print or commit tokens, complete profile links, keystores, credentials,
  private databases, raw traces, or broad Android diagnostic dumps.

### Non-goals

- Zero-flow disconnect/dormancy, graceful QUIC close, server fatal-path repair,
  server-held long polling, larger QUIC idle timeouts, screen-off suspension, or
  changes to Exclave association lifetimes.
- Event-driven child supervision, TCP adapter redesign, System/mixed TUN, direct
  FlowRelay outbound, JNI embedding, SOCKS removal, generalized zero-copy work,
  crypto tuning, or metered-network policy changes without CP3 evidence.

## Change Envelope

- Target: Android preference correctness and the pinned Rust Slipstream client
  scheduler only.
- Expected paths:
  - `app/src/main/java/io/nekohasekai/sagernet/database/DataStore.kt`
  - `bin/lib/slipstream/adaptive-idle.patch`
  - `bin/lib/slipstream/build.sh` only if patch replay requires it
  - ignored generated `app/src/main/jniLibs/arm64-v8a/libslipstream.so` and release APK
  - `AGENTS.md` and this goal after verified behavior changes
- Allowed artifacts: focused local tests, disposable source/build trees, ignored
  signed arm64 APK/native binary, and aggregate measurement summaries. Raw temporary
  traces belong only in memory-backed storage and are deleted after aggregation.
- Forbidden artifacts: server/runtime changes, new dependencies or architecture,
  secret-bearing output, packet traces/logs, and generated measurement archives in Git.
- Budget: first sufficient fast-path evidence. Stop if the scheduler does not reduce
  quiet-open wakeups by at least 80%; stop after R4 if the mechanism and runtime gates
  pass. Physical charge/mAh measurement is explicitly deferred by the user.

## Current Checkpoint

- Closes: R2.
- Smallest next action: commit the independent speed-interval correction, build and
  install it, then verify the migrated live cadence before starting R3.
- Expected evidence: the normalization matrix, release build, and screen-on callback
  cadence near three seconds with no screen-off callback.
- Stop or replan if: migration changes the disabled value, release compilation fails,
  or the installed cadence remains sub-500 ms.

## Current State

- Resolved: the device exposes charge/current/voltage and Perfetto power counters; the
  live settings are gVisor, profile statistics on, app statistics/PCAP/WakeLock off,
  no battery exemption, Error logging, and an actual `Speed Interval` of `3ms`. A
  credential-free direct gVisor control profile was added and passed a TCP connect.
- Last relevant evidence: separate ten-minute screen-off diagnostics found the DNS
  Rust child using 52.375 CPU seconds and 156,623 scheduler slices; in its 380 runnable
  seconds the median was 331.5 slices/second. Twenty loopback application flows remained
  open. The direct control had no Rust child. Raw traces were deleted after aggregation;
  server Slipstream/flowd stayed at restart baselines 4/0.
- Blocker: none; the user superseded the physical charge gate with the bounded fast path.
- Next: finish and commit R2, then implement the client-only R3 scheduler.

## Material Decisions

- 2026-08-10: keep one conservative five-second ACK-eliciting keepalive for open-idle
  and empty states; reject the unaudited 15-second taper.
- 2026-08-10: measure before the Rust scheduler; fix the independent 3 ms correctness
  defect in a separate checkpoint/build.
- 2026-08-10: defer zero-flow dormancy until the known server close/reconnect fatal
  path and graceful client shutdown are solved as a separate objective.
- 2026-08-10: user approved the fast path: use aggregate scheduler/carrier mechanism
  evidence and full runtime regression gates, but make no battery percentage or mAh
  claim without the deferred physically unplugged paired runs.

## Checkpoint History

- 2026-08-10: contract frozen from the approved audited plan; R1 started with the
  connected Android device and clean repository at `9dc9bea`.
- 2026-08-10: CP0 inventory and diagnostic traces confirmed the live 3 ms defect and
  high DNS-child scheduling with 20 quiet open flows. A minimal direct gVisor control
  was created on-device. The `DataStore.kt` R2 fix is locally implemented and release
  Kotlin compilation passes, but it remains uninstalled until the current-build energy
  baseline is frozen.
- 2026-08-10: the user superseded the long physical charge gate. R1 is verified from
  bounded mechanism evidence; R2 is now the current checkpoint.

## Completion

- Resolved outcomes:
- Commands and artifacts:
- Constraint and diff-scope check:
- Final status:
