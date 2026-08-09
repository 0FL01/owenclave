# Goal: Stable adaptive Slipstream multipath

Status: complete
Source: user report of speed regression and network loss after abruptly stopping a speed test, 2026-08-09
Last updated: 2026-08-09
Superseded by: `docs/goals/2026-08-09-slipstream-single-resolver-link.md`

## Objective

Keep the MTS-compatible DNS VPN usable after abrupt high-load cancellation while
using multipath only when it improves the current network.

## Execution Directive

Complete the frozen Required Outcomes using the listed Change Envelope and
Primary Evidence. Work on the smallest unresolved outcome. Do not add
requirements from reviews, tests, tools, speculative risks, or optional source
text. Finish when every required outcome is resolved and affected constraints
remain satisfied.

## Frozen Contract

### Required Outcomes

- R1: reproduce and measure the current multipath regression.
  - Source: user reports lower speed and loss of new traffic after stopping a
    speed test and switching applications.
  - Acceptance: current pair and single-lane controls use the same bounded
    payload and abrupt-cancel sequence; throughput and post-cancel recovery are
    recorded.
  - Primary evidence: exact payload rates plus repeated load-cancel-small-probe
    results on the connected Android device.
  - Status: verified
  - Evidence: the current fixed pair passed exact 8 MiB downloads at 133555 and
    132562 B/s. After eight parallel downloads were cancelled at 15 seconds, an
    exact 4096-byte probe remained unusable beyond 65 seconds and then returned
    an empty response. Android still reported a validated VPN, the Rust process
    remained alive, the cellular INTERNET network remained validated, and the
    server target plus all n-de1 services were healthy. Killing only the Rust
    child let the existing monitor restart it and restored the exact probe in 11
    seconds, confirming a silent client carrier stall rather than server failure.

- R2: select only DNS-over-TCP lanes that work on the current underlying network.
  - Source: the client must work in the MTS whitelist while avoiding a harmful
    multipath lane on another operator.
  - Acceptance: startup uses the fastest proven operator UDP lane when the
    Tele2 pair is the current underlying DNS, otherwise probes the approved MTS
    TCP DNS candidates and gives Slipstream at most the first two successful
    lanes in measured full-tunnel preference order.
  - Primary evidence: selected lanes and successful carrier readiness on Android
    plus the previously proven MTS path.
  - Status: superseded
  - Evidence: the user replaced automatic lane selection with one resolver from
    the imported profile.

- R3: recover automatically from a silent post-load carrier stall.
  - Source: user requires the next application to retain network after abruptly
    stopping a speed test.
  - Acceptance: ten load-cancel-small-probe cycles complete without manually
    reconnecting the VPN; each small probe succeeds within ten seconds.
  - Primary evidence: monotonic per-cycle timings, exact small-payload hash and
    unchanged VPN ownership.
  - Status: superseded
  - Evidence: the user removed adaptive multipath rather than retaining a
    restart heuristic for it.

- R4: deliver the corrected APK without a throughput regression.
  - Source: user requested benchmark, stability validation and implementation.
  - Acceptance: the corrected adaptive configuration is not slower than its
    fastest healthy single-lane control and the tested arm64 APK is recorded.
  - Primary evidence: lower repeated exact-payload rate, successful Android
    install and APK SHA-256.
  - Status: superseded
  - Evidence: the successor goal owns the single-resolver APK and validation.

### Constraints

- C1: retain Rust Slipstream, the existing SSH profile/import, strict host pin,
  private SSH target, isolated WARP egress and gVisor VpnService.
- C2: no direct carrier fallback, Room migration, persistent traffic logs or
  credentials in Git.
- C3: candidates remain TCP DNS endpoints from the user-provided MTS whitelist;
  prefer Yandex and Rostelecom.
- C4: preserve unrelated dirty files and the untracked keystore.

### Non-goals

- System TUN, DoT/DoH, server redesign or seamless preservation of an existing
  application TCP connection across a carrier restart.
- Tuning the public n-de1 server unless evidence identifies it as the fault.

## Change Envelope

- Target: `SlipstreamInstance` lane selection, queue lifetime and liveness.
- Expected paths: Slipstream Android runtime, this goal and regenerated arm64
  debug APK only.
- Allowed artifacts: volatile aggregate diagnostic counters and temporary exact
  benchmark targets.
- Forbidden artifacts: resolver UI, another carrier implementation, remote
  traffic logging, secrets or public test listeners.
- Budget: one adaptive runtime path; add no dependency or database state.

## Current Checkpoint

- Closes: R1.
- Smallest next action: benchmark the installed current pair and a single-lane
  control with the same exact payload and abrupt-cancel probe.
- Expected evidence: a measured regression or evidence that changes the first
  implementation fix.
- Stop or replan if: the server or WARP fails independently of Android traffic.

## Current State

- Resolved: n-de1 carrier, private SSH and WARP are active; Slipstream has zero
  service restarts.
- Last relevant evidence: current pair lower throughput was 132562 B/s; the
  parallel abrupt-cancel case stalled new traffic until the Rust child was
  restarted.
- Blocker: none.
- Next: current-pair baseline.

## Material Decisions

- 2026-08-09: multipath is conditional optimization, not a reason to retain an
  unhealthy lane.
- 2026-08-09: aggregate volatile counters may diagnose queue and liveness state;
  query names, destinations and payload metadata remain unlogged.
- 2026-08-09: a separate loss of the cellular INTERNET APN was observed before
  the controlled run and was not attributed to Owenclave. The controlled stall
  was reproduced after the APN returned and while it remained validated.
- 2026-08-09: the `.3` single-lane control passed at 130614 and 128437 B/s and
  naturally recovered from the parallel abrupt-cancel case in 27 seconds. The
  first adaptive cross-AS pair selected `.1` and Rostelecom `.62`, passed exact
  payload integrity but fell to 99994 B/s. Ordinary DNS RTT did not predict
  tunnel throughput, so stable full-tunnel preference order replaced RTT ranking.
- 2026-08-09: Yandex `.3,.1` also regressed to 122796 and 81312 B/s. Public TCP
  resolver multipath is therefore rejected on the connected Tele2 network;
  Tele2 uses its previously proven native UDP `.174,.175` pair, while other
  networks retain the MTS-proven TCP `.3,.35` pair with Yandex/Rostelecom
  fallbacks.
- 2026-08-09: the whole-device Tele2 UDP pair passed at 231964 then 117419 B/s.
  This reproduced the order/variance problem and its lower result lost to the
  single control. Earlier exact standalone evidence already showed `.175`
  single at 695054 B/s and the pair's lower result at 692415 B/s, so Tele2 now
  selects `.175` alone; multipath remains enabled only on the MTS TCP path where
  it previously measured 1.841 times its reliable single.

## Completion

- Resolved outcomes: R1 reproduced the post-load stall; R2-R4 were superseded by
  the user's explicit decision to remove multipath and provision one resolver in
  the imported profile.
- Commands and artifacts: exact payload and cancellation evidence above; no
  adaptive runtime was accepted as the final architecture.
- Constraint and diff-scope check: the successor goal retains Rust Slipstream,
  SSH, gVisor VPN and WARP while deleting adaptive resolver selection.
- Final status: complete by supersession, not by accepting adaptive multipath.
