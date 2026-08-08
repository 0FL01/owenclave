# Goal: DNS resolver lane sharding proof

Status: complete
Source: user instruction, 2026-08-08
Last updated: 2026-08-08

## Objective

Determine whether two independent dnstt and SSH lanes through different allowed
recursive resolvers provide at least 1.5 times the controlled single-lane
throughput before any bonding code is written.

## Execution Directive

Complete the frozen Required Outcomes using the listed Change Envelope and
Primary Evidence. Work on the smallest unresolved outcome. Do not add
requirements from reviews, tests, tools, speculative risks, or optional source
text. Finish when every required outcome is resolved and affected constraints
remain satisfied.

## Frozen Contract

### Required Outcomes

- R1: qualify the independent UDP resolver lanes available on the connected
  Android LTE network.
  - Source: user request to try sharding across several resolvers.
  - Acceptance: Tele2 `176.59.127.174`, Tele2 `176.59.127.175` and Yandex
    `77.88.8.8` each attempt the same exact 8 MiB transfer through their own
    dnstt and authenticated SSH lane; every success or failure is recorded.
  - Primary evidence: byte count, SHA-256, duration and byte rate under a
    180-second bound.
  - Status: verified
  - Evidence: Tele2 `.174` completed the exact payload at 162075 B/s. Tele2
    `.175` did not establish the SSH path within 60 seconds. Yandex UDP reached
    payload transfer but timed out after 2228236 bytes at 12379 B/s.

- R2: measure the two best qualified lanes concurrently without bonding.
  - Source: user request to try resolver sharding.
  - Acceptance: two independent dnstt clients and two independent SSH sessions
    concurrently fetch the same exact payload in two attempts; each payload hash
    matches and the aggregate lower-of-two rate is calculated.
  - Primary evidence: per-lane byte count, SHA-256 and duration plus aggregate
    completion time and rate.
  - Status: not_applicable
  - Evidence: only Tele2 `.174` qualified with one complete exact payload.
    There was no second qualified lane to form the required pair.

- R3: apply the 1.5 times gate before application implementation.
  - Source: approved DNSFS RECON conclusion.
  - Acceptance: proceed toward bonding only when the aggregate lower-of-two rate
    is at least 243186 B/s, 1.5 times the controlled single-lane baseline of
    162124 B/s; otherwise close sharding without application code changes.
  - Primary evidence: calculated ratio and unchanged stable runtime checks.
  - Status: verified
  - Evidence: sharding stopped before application changes. The only additional
    carrying lane measured 12379 B/s, so even its isolated sum with the baseline
    would be about 174454 B/s, below the 243186 B/s gate before accounting for
    concurrent contention.

### Constraints

- C1: use the real Android LTE, recursive DNS, authoritative dnstt, restricted
  SSH and WARP path with no direct carrier fallback.
- C2: use the same 8 MiB payload and SHA-256 as the completed performance goal.
- C3: do not change the stable dnstt server configuration or Owenclave code
  before the aggregate gate passes.
- C4: do not log keys, credentials, session identifiers or traffic metadata.

### Non-goals

- Bonding, packet scheduling, retransmission or application implementation
  before the 1.5 times gate passes.
- MTU, KCP, smux, SSH or WARP tuning.
- Slipstream implementation.

## Change Envelope

- Target: temporary Android Termux dnstt and SSH processes, one temporary exact
  payload target inside `warpns`, and this goal document.
- Expected path: `docs/goals/2026-08-08-dnstt-resolver-sharding.md`.
- Allowed artifacts: ephemeral benchmark scripts, payloads and bounded results.
- Forbidden artifacts: live service edits, application code, dependencies,
  persistent logs, secrets in Git or tool output and direct fallback.
- Budget: one qualification attempt per resolver and two concurrent attempts,
  each bounded to 180 seconds.

## Benchmark Contract

- Client: connected Android device, foreground Termux UID, LTE underlying
  network, Owenclave VPN stopped during isolated measurements.
- Payload: the exact 8 MiB object from the completed performance benchmark,
  served only on `127.0.0.1:19090` inside `warpns`.
- Lane: one resolver, one dnstt client, one authenticated SSH SOCKS endpoint and
  one payload download.
- Ranking: successful exact payload first, then byte rate.
- Aggregate: 16777216 exact bytes divided by wall-clock time until both lanes
  complete; the lower rate from two attempts is compared with the gate.

## Benchmark Log

- Tele2 `176.59.127.174:53`: PASS, 8388608 bytes in 51.757338 seconds,
  162075 B/s, SHA-256 matched.
- Tele2 `176.59.127.175:53`: FAIL, no SSH readiness within 60 seconds.
- Yandex `77.88.8.8:53`: FAIL, 2228236 of 8388608 bytes in 180.001002
  seconds, 12379 B/s; the partial payload hash did not match the exact object.
- Concurrent pair: not run because only one resolver qualified. The measured
  isolated upper bound of `.174` plus Yandex was 174454 B/s, 0.72 times the
  243186 B/s gate, before concurrent contention.

## Current Checkpoint

- Closes: R1-R3.
- Smallest next action: none; closure check passed.
- Expected evidence: complete.
- Stop or replan if: a materially different carrier is approved for RECON.

## Current State

- Resolved: R1-R3; only Tele2 `.174` qualified, so the pair precondition failed.
- Last relevant evidence: exact `.174` transfer at 162075 B/s and bounded
  failures for `.175` and Yandex UDP.
- Blocker: none.
- Next: Slipstream RECON if materially more throughput is required.

## Material Decisions

- 2026-08-08: test independent capacity before adding bonding code.
- 2026-08-08: require 1.5 times aggregate throughput so added complexity must
  produce a material gain.

## Checkpoint History

- 2026-08-08: contract frozen before runtime experiments.
- 2026-08-08: R1 complete; `.174` passed, `.175` failed readiness, and Yandex
  UDP timed out with a partial payload.
- 2026-08-08: R2 was not applicable because a second resolver did not qualify;
  R3 closed sharding before any application or server change.

## Completion

- Resolved outcomes: R1 and R3 verified; R2 not applicable with direct evidence.
- Commands and artifacts: three bounded resolver qualifications against one
  exact 8 MiB payload with SHA-256
  `2daeb1f36095b44b318410b3f4e8b5d989dcc7bb023d1426c492dab0a3053e74`.
- Constraint and diff-scope check: no server or application configuration was
  changed; temporary Android processes, files and server target were removed;
  stable dnstt, private SSH and WARP runtime remained active.
- Final status: complete; resolver sharding did not pass its entry gate.
