# Goal: Rust Slipstream resolver multipath

Status: complete
Source: user approval to test two-resolver Rust Slipstream multipath, 2026-08-08
Last updated: 2026-08-08

## Objective

Determine whether two independent UDP resolvers in one Rust Slipstream QUIC
session can reach the existing 1.5 times dnstt replacement gate on Android LTE.

## Execution Directive

Complete the frozen Required Outcomes using the listed Change Envelope and
Primary Evidence. Work on the smallest unresolved outcome. Do not add
requirements from reviews, tests, tools, speculative risks, or optional source
text. Finish when every required outcome is resolved and affected constraints
remain satisfied.

## Frozen Contract

### Required Outcomes

- R1: qualify the available secondary UDP resolvers with Rust Slipstream.
  - Source: approved plan to test Tele2 `.175` and Yandex UDP after `.174`.
  - Acceptance: each candidate gets one bounded standalone attempt using the
    same exact 8 MiB payload and authoritative pacing; exact success or the
    observed failure is recorded.
  - Primary evidence: SSH readiness, byte count, SHA-256, elapsed time and rate,
    with a 180-second payload bound and a 60-second setup bound.
  - Status: verified
  - Evidence: Tele2 `.175` reached authenticated SSH in 3267 ms and passed the
    exact payload in 12069 ms at 695054 B/s. Yandex UDP reached authenticated
    SSH in 11715 ms but timed out after 180057 ms with 3698688 bytes and no
    complete payload hash.

- R2: measure useful two-resolver combinations.
  - Source: user approved the multipath proof of concept for aggregation.
  - Acceptance: `.174` plus every secondary that reaches the Rust path is tested
    twice in one QUIC session against the same exact 8 MiB payload.
  - Primary evidence: resolver order, exact byte count and SHA-256, elapsed time
    and rate for both attempts under the same bounds.
  - Status: verified
  - Evidence: Tele2 `.174,.175` passed at 1046613 B/s and reversed
    `.175,.174` passed at 692415 B/s; both exact hashes matched. Tele2 `.174`
    with Yandex passed at 82959 B/s and the reversed order passed at 106373
    B/s; both exact hashes matched, but Yandex materially reduced throughput.

- R3: apply the existing material replacement gate.
  - Source: previously approved 1.5 times dnstt gate.
  - Acceptance: qualify for Owenclave work only if one pair completes both exact
    transfers and its lower rate is at least 243186 B/s.
  - Primary evidence: lower-of-two pair rate and ratio to controlled dnstt
    baseline 162124 B/s and Rust `.174` lower rate 142534 B/s.
  - Status: verified
  - Evidence: the useful Tele2 pair lower rate is 692415 B/s, 4.271 times the
    controlled dnstt baseline and 2.847 times the 243186 B/s gate. It qualifies
    for a separate Owenclave integration goal.

### Constraints

- C1: use the already pinned Rust server and Android client artifacts.
- C2: keep authenticated SSH, pinned host key, private `10.200.0.2:41924`
  target, isolated WARP egress and no direct fallback.
- C3: use the 8388608-byte payload with SHA-256
  `2daeb1f36095b44b318410b3f4e8b5d989dcc7bb023d1426c492dab0a3053e74`.
- C4: Android Termux is foreground, Owenclave VPN is stopped, and no other
  carrier process runs during measurements.
- C5: do not persist carrier output, traffic logs, credentials or TLS secrets.

### Non-goals

- Reliable resolver failover or seamless TCP-session survival.
- Owenclave integration before the performance gate passes.
- DoT, DoH, System TUN, carrier tuning or server changes.

## Change Envelope

- Target: standalone Android multipath benchmark and this goal document.
- Expected paths: this goal and temporary Android/n-de1 benchmark state only.
- Allowed artifacts: temporary exact payload target, runner state and bounded
  client/SSH processes.
- Forbidden artifacts: app code, APK changes, public SOCKS, direct fallback,
  persistent traffic output or another public UDP listener.
- Budget: one attempt per secondary and two attempts per useful pair.

## Benchmark Contract

- Client: connected Android, foreground Termux UID, Tele2 LTE, Owenclave stopped.
- Carrier: Rust Slipstream authoritative pacing through recursive UDP resolvers
  to public UDP `:53`; one QUIC session for each multipath pair.
- Inner path: strict OpenSSH host pin and pubkey authentication to private SSH in
  `warpns`, then exact loopback HTTP payload through WARP.
- Ranking: lower of two exact pair rates. Timeout, early EOF, crash or hash
  mismatch fails an attempt.

## Current Checkpoint

- Closes: none; the closure check is terminal.
- Smallest next action: none inside this benchmark goal.
- Expected evidence: complete.
- Stop or replan if: a later integration changes the measured carrier settings.

## Current State

- Resolved: both secondary resolvers and both two-resolver combinations were
  measured; the Tele2 pair passed the replacement gate.
- Last relevant evidence: Tele2 pair lower-of-two 692415 B/s with exact hashes.
- Blocker: none.
- Next: a separate Owenclave integration goal may use `.174,.175` multipath.

## Material Decisions

- 2026-08-08: multipath is evaluated only for bandwidth aggregation, not
  resolver-level reliability.
- 2026-08-08: default Owenclave gVisor would avoid per-socket protection, but app
  integration remains outside this goal until the performance gate passes.

## Experiment Log

- Tele2 `.175`: PASS, setup 3267 ms, payload 12069 ms, 695054 B/s, exact hash.
- Yandex UDP: FAIL, setup 11715 ms, payload timeout 180057 ms, 3698688 bytes.
- Tele2 `.174,.175`: PASS, setup 3070 ms, payload 8015 ms, 1046613 B/s,
  exact hash.
- Tele2 `.175,.174`: PASS, setup 1794 ms, payload 12115 ms, 692415 B/s,
  exact hash.
- Tele2 `.174`, Yandex: PASS, setup 4240 ms, payload 101117 ms, 82959 B/s,
  exact hash.
- Yandex, Tele2 `.174`: PASS, setup 8842 ms, payload 78860 ms, 106373 B/s,
  exact hash.
- The Tele2 pair passed the existing replacement gate. Its lower result was
  effectively equal to `.175` alone, while the `.174`-first attempt reached
  1046613 B/s. This proves a viable fast pair but not order-independent
  aggregation beyond the fastest single resolver.

## Completion

- Resolved outcomes: R1, R2 and R3 verified.
- Commands and artifacts: pinned Android Rust client, existing Rust server,
  strict SSH, two candidate qualifications and four exact pair transfers.
- Constraint and diff-scope check: server and app code unchanged; no direct
  fallback or persistent output; Android runner, payloads, processes and n-de1
  target removed; stay-awake restored; carrier, private SSH and WARP active with
  zero carrier restarts.
- Final status: complete; Tele2 `.174,.175` multipath qualifies for a separate
  Owenclave integration goal, while Yandex UDP is rejected for this path.
