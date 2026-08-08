# Goal: DNS tunnel performance Pareto test

Status: complete
Source: user-approved Pareto plan, 2026-08-08
Last updated: 2026-08-08

## Objective

Measure the allowed Android resolver paths, test three safe dnstt server MTUs on
the winner, and retain a change only when it improves sustained throughput by at
least 25%.

## Execution Directive

Complete the frozen Required Outcomes using the listed Change Envelope and
Primary Evidence. Work on the smallest unresolved outcome. Do not add
requirements from reviews, tests, tools, speculative risks, or optional source
text. Finish when every required outcome is resolved and affected constraints
remain satisfied.

## Frozen Contract

### Required Outcomes

- R1: measure every resolver profile currently allowed by Owenclave.
  - Source: approved plan step 1.
  - Acceptance: underlying-network UDP DNS, Yandex DoT and Cloudflare DoH each
    attempt two exact 8 MiB downloads through the same dnstt and authenticated
    SSH path at server MTU 1232; each success or failure is recorded.
  - Primary evidence: payload SHA-256 plus curl duration and byte rate, with a
    180-second timeout per attempt.
  - Status: verified
  - Evidence: underlying UDP completed both exact downloads at 162351 and
    162124 B/s. Yandex DoT reached the SSH backend but both transfers hit the
    180-second limit. Cloudflare DoH reached SSH but timed out after 5570531
    bytes at 30947 B/s and 4620312 bytes at 25668 B/s.

- R2: compare MTU 1232, 1400 and 1472 on the winning resolver.
  - Source: approved plan step 2.
  - Acceptance: each MTU attempts the same two-download benchmark on the stable
    resolver with the highest lower-of-two rate from R1; failure of the common
    SSH readiness gate conclusively marks that MTU unusable before payload.
  - Primary evidence: payload SHA-256 plus curl duration and byte rate for every
    MTU attempt.
  - Status: verified
  - Evidence: MTU 1232 completed both exact transfers. MTU 1400 and 1472 each
    left dnstt running but could not deliver an SSH banner within 60 seconds, so
    neither could begin a payload attempt on this LTE/resolver path.

- R3: apply the 25% stop rule and leave the live service in the chosen state.
  - Source: approved plan steps 3 and 4.
  - Acceptance: retain a non-default MTU only if its lower-of-two rate is at
    least 25% above MTU 1232; otherwise restore 1232 and record Slipstream as a
    separate RECON direction rather than tuning dnstt further.
  - Primary evidence: calculated ratio, live `dnstt.service` command and one
    final exact payload through the retained configuration.
  - Status: verified
  - Evidence: neither non-default MTU produced a usable sample, so neither could
    meet the 1.25x gate. The explicit MTU argument was removed, the live default
    is 1232, and a final exact transfer passed at 164810 B/s.

### Constraints

- C1: benchmark the real Android LTE, recursive DNS, authoritative dnstt and
  restricted SSH path; no direct carrier fallback.
- C2: use one generated payload and one target for every run; do not log keys,
  credentials, session identifiers or traffic metadata.
- C3: back up the live unit before its first edit, validate it before restart,
  and restart only `dnstt.service` while changing MTU.
- C4: preserve resolver failover and WARP egress architecture.

### Non-goals

- KCP, smux, polling, SSH, WARP, bonding or multipath tuning.
- MTU above 1472.
- Slipstream implementation; it is only the next RECON if the 25% gate fails.
- Internet speed-test sites or comparison against unrelated transports.

## Change Envelope

- Target: one temporary benchmark target in `warpns`, temporary Android Termux
  processes, `dnstt.service` MTU argument and this goal document.
- Expected paths: `docs/goals/2026-08-08-dnstt-performance.md` and, only if a
  non-default MTU wins, `/etc/systemd/system/dnstt.service`.
- Allowed artifacts: one timestamped unit backup and ephemeral payload/results.
- Forbidden artifacts: application code changes, new dependencies, persistent
  traffic logs, secrets in Git/tool output and direct transport fallback.
- Budget: two 8 MiB exact downloads per profile, 180 seconds per attempt.

## Benchmark Contract

- Client: connected Android device, foreground Termux UID, LTE underlying
  network, Owenclave VPN stopped during isolated measurements.
- Payload: one 8 MiB file served only on `127.0.0.1:19090` inside `warpns`.
- Path: selected resolver -> public dnstt UDP `:53` -> private SSH in `warpns`
  -> loopback HTTP target.
- Ranking: highest lower-of-two successful byte rate; any incomplete or
  hash-mismatched attempt fails that profile or MTU.

## Benchmark Log

- MTU 1232, underlying UDP `176.59.127.174:53`: PASS, 8388608 bytes in
  51.669378 seconds, 162351 B/s, SHA-256 matched.
- MTU 1232, underlying UDP `176.59.127.174:53`: PASS, 8388608 bytes in
  51.741699 seconds, 162124 B/s, SHA-256 matched.
- MTU 1232, Yandex DoT `77.88.8.8:853`: FAIL, curl timeout after 180 seconds;
  the first diagnostic harness discarded partial-byte metrics.
- MTU 1232, Yandex DoT `77.88.8.8:853`: FAIL, curl timeout after 180 seconds;
  the first diagnostic harness discarded partial-byte metrics.
- MTU 1232, Cloudflare DoH `https://1.1.1.1/dns-query`: FAIL, 5570531 of
  8388608 bytes in 180.000905 seconds, 30947 B/s.
- MTU 1232, Cloudflare DoH `https://1.1.1.1/dns-query`: FAIL, 4620312 of
  8388608 bytes in 180.001012 seconds, 25668 B/s.
- Resolver winner: underlying UDP, lower-of-two rate 162124 B/s.
- MTU 1400, underlying UDP: FAIL, no SSH banner within 60 seconds; payload
  attempts were not reachable.
- MTU 1472, underlying UDP: FAIL, no SSH banner within 60 seconds; payload
  attempts were not reachable.
- Restored MTU 1232, underlying UDP: PASS, 8388608 bytes in 50.898516 seconds,
  164810 B/s, SHA-256 matched.
- MTU decision: retain 1232. No non-default candidate met the 25% gate.

## Current Checkpoint

- Closes: R1-R3.
- Smallest next action: none; closure check passed.
- Expected evidence: complete.
- Stop or replan if: a separate Slipstream RECON is approved.

## Current State

- Resolved: R1-R3; underlying UDP won and MTU 1232 remained the only working
  tested value.
- Last relevant evidence: final exact MTU 1232 transfer at 164810 B/s; live
  service active with zero restarts and no explicit MTU argument.
- Blocker: none.
- Next: separate Slipstream RECON only if materially more throughput is needed.

## Material Decisions

- 2026-08-08: use exact local payload transfer instead of an internet speed test
  so only resolver and dnstt behavior vary.
- 2026-08-08: two sustained samples and the lower rate represent stability with
  the smallest useful test budget.
- 2026-08-08: an MTU that cannot establish the common SSH path is a bounded
  benchmark failure; do not spend two payload timeouts on an unusable path.

## Checkpoint History

- 2026-08-08: frozen contract created before benchmark state changes.
- 2026-08-08: R1 complete; underlying UDP passed twice, while Yandex DoT and
  Cloudflare DoH reached SSH but failed the 180-second payload bound.
- 2026-08-08: R2 complete; MTU 1400 and 1472 both failed SSH readiness. Restored
  MTU 1232 and passed one final exact payload.
- 2026-08-08: R3 complete; no candidate met the 25% gate, temporary Android and
  server benchmark state was removed, and Owenclave VPN was reconnected.

## Completion

- Resolved outcomes: R1-R3 verified.
- Commands and artifacts: six resolver attempts, two bounded non-default MTU
  failures and one final exact 8 MiB payload with SHA-256
  `2daeb1f36095b44b318410b3f4e8b5d989dcc7bb023d1426c492dab0a3053e74`.
- Constraint and diff-scope check: the temporary target and Android processes
  were removed; only `dnstt.service` was restarted during MTU tests; the live
  unit was restored byte-for-behavior to default MTU 1232; resolver failover,
  private SSH and WARP architecture were unchanged.
- Final status: complete.
