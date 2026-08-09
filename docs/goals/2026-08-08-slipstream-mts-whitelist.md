# Goal: Rust Slipstream through MTS whitelist

Status: complete
Source: user approval to qualify Rust Slipstream and multipath through the MTS whitelist SOCKS egress, 2026-08-08
Last updated: 2026-08-08

## Objective

Prove actual Rust Slipstream DNS payload through the MTS whitelist egress,
identify useful public resolvers, and retain multipath only when it materially
improves the best single resolver.

## Execution Directive

Complete the frozen Required Outcomes using the listed Change Envelope and
Primary Evidence. Work on the smallest unresolved outcome. Do not add
requirements from reviews, tests, tools, speculative risks, or optional source
text. Finish when every required outcome is resolved and affected constraints
remain satisfied.

## Frozen Contract

### Required Outcomes

- R1: prove actual Rust Slipstream through the MTS whitelist.
  - Source: user requires the MTS whitelist as the compatibility gate.
  - Acceptance: Rust Slipstream, strict SSH and one exact payload pass through a
    temporary local UDP-to-TCP DNS adapter, authenticated SOCKS5 and MTS egress.
  - Primary evidence: SSH readiness, exact byte count and SHA-256.
  - Status: verified
  - Evidence: Yandex `77.88.8.1` carried strict SSH and the exact 1 MiB payload
    through the temporary adapter and MTS SOCKS path. Setup took 37078 ms; the
    payload took 30530 ms at 34345 B/s and matched SHA-256.

- R2: qualify the thirteen currently live TCP DNS resolvers.
  - Source: user requested finding what is useful through the supplied SOCKS5.
  - Acceptance: each listed resolver gets one bounded actual Slipstream attempt;
    exact success or observed failure is recorded.
  - Primary evidence: setup status and one exact 1 MiB transfer under a
    180-second payload bound.
  - Status: verified
  - Evidence: nine of thirteen resolvers carried exact 1 MiB payloads. The two fastest were
    `188.0.190.35` at 47131 B/s and `77.88.8.3` at 47086 B/s. Yandex `.1`,
    `.88`, `.8`, `94.25.113.230`, `95.167.75.62`, `91.240.86.14` and
    `81.88.213.101` also passed. `84.53.201.130`, `82.151.127.188` and
    `85.237.61.86` did not reach SSH readiness; `95.167.26.10` reached a banner
    but did not sustain SSH authentication.

- R3: compare the fastest single resolver with the two fastest paths together.
  - Source: user goal is higher DNS VPN throughput and asked specifically about
    multipath.
  - Acceptance: best single and best pair each get two exact 8 MiB attempts on
    the same MTS path.
  - Primary evidence: exact hashes, elapsed time, rates and lower-of-two rate.
  - Status: verified
  - Evidence: `188.0.190.35` was fastest on 1 MiB but failed SSH authentication
    readiness on its first 8 MiB run, so `77.88.8.3` became the best reliable
    single. It passed exact 8 MiB at 50528 and 47462 B/s. The
    `188.0.190.35,77.88.8.3` pair passed exact 8 MiB at 111242 and 87393 B/s.

- R4: apply the Pareto multipath gate.
  - Source: approved plan to keep multipath only for material benefit.
  - Acceptance: retain multipath only if its lower-of-two rate is at least 1.25
    times the best single lower-of-two rate; otherwise select the single path.
  - Primary evidence: calculated lower-rate ratio.
  - Status: verified
  - Evidence: pair lower-of-two 87393 B/s divided by reliable single
    lower-of-two 47462 B/s is 1.841. Multipath exceeds the 1.25 times gate and
    is retained for this MTS compatibility path.

### Constraints

- C1: use the existing pinned Rust server, local Linux client digest
  `6ef7e9f0aad1f266e484486184a78775408a82d4defe023fc99d77114e23133c`,
  strict SSH host pin and existing private WARP egress.
- C2: the SOCKS path is TCP-only. The adapter changes only resolver transport to
  DNS-over-TCP; Slipstream still sends its real encoded DNS queries and receives
  authoritative responses through recursive resolvers.
- C3: do not use this bridge result as native UDP throughput evidence. Tele2
  native UDP remains the deployment performance surface.
- C4: no server, Owenclave runtime, database or APK changes in this concept test.
- C5: no direct carrier fallback, public SOCKS, persistent traffic output,
  credentials or TLS secrets in Git or logs.

### Non-goals

- Production DNS-over-TCP adapter.
- System TUN, DoT/DoH integration or seamless resolver failover.
- Bonding independent Slipstream sessions.

## Change Envelope

- Target: temporary host adapter and standalone Rust/SSH benchmark only.
- Expected paths: this goal and temporary files outside the repository.
- Allowed runtime: one owned Cloudflare Access process, local adapters, Rust
  client, OpenSSH and temporary n-de1 loopback payload target.
- Forbidden artifacts: persistent services, firewall changes, app code, APK,
  new credentials or another public listener.
- Budget: one 1 MiB qualification per resolver, then two 8 MiB attempts for the
  best single and pair.

## Benchmark Contract

- Path: local Rust client -> local UDP adapter -> DNS-over-TCP through
  authenticated SOCKS5 and MTS -> recursive resolver -> public Slipstream server
  -> private SSH in `warpns` -> exact HTTP payload.
- Candidates: `77.88.8.3`, `77.88.8.1`, `77.88.8.88`, `77.88.8.8`,
  `84.53.201.130`, `94.25.113.230`, `188.0.190.35`, `82.151.127.188`,
  `95.167.75.62`, `91.240.86.14`, `81.88.213.101`, `95.167.26.10`,
  `85.237.61.86`.
- Ranking: exact transfer rate; timeout, early EOF, crash or hash mismatch fails.

## Current Checkpoint

- Closes: none; the closure check is terminal.
- Smallest next action: none inside this concept test.
- Expected evidence: complete.
- Stop or replan if: a later deployment changes resolver transport or paths.

## Current State

- Resolved: actual Rust Slipstream passed through MTS, nine resolvers qualified,
  and the fastest reliable single and pair completed two exact 8 MiB runs.
- Last relevant evidence: pair lower-of-two 87393 B/s versus single 47462 B/s.
- Blocker: none.
- Next: use a separate integration goal; native Tele2 UDP remains the deployment
  performance surface.

## Material Decisions

- 2026-08-08: use the MTS SOCKS path as a whitelist compatibility gate, not as a
  native UDP performance surface.
- 2026-08-08: added `95.167.75.62` and `91.240.86.14` after the user supplied an
  updated successful MTS scan.
- 2026-08-08: added `81.88.213.101`, `95.167.26.10` and `85.237.61.86` from the
  next user-supplied MTS scan before selecting benchmark finalists.

## Experiment Log

- Yandex `77.88.8.1`: PASS, setup 37078 ms, exact 1 MiB in 30530 ms at
  34345 B/s.
- Yandex `77.88.8.3`: PASS, setup 71428 ms, 47086 B/s.
- Yandex `77.88.8.88`: PASS, setup 53410 ms, 14924 B/s.
- Yandex `77.88.8.8`: PASS, setup 48502 ms, 7841 B/s.
- Rostelecom `84.53.201.130`: SETUP FAIL.
- Rostelecom `94.25.113.230`: PASS, setup 269192 ms, 5980 B/s.
- Rostelecom `82.151.127.188`: SETUP FAIL.
- Vainah `188.0.190.35`: PASS, setup 89540 ms, 47131 B/s.
- Rostelecom `95.167.75.62`: PASS, setup 148473 ms, 14405 B/s.
- IOT `91.240.86.14`: PASS, setup 73168 ms, 34654 B/s.
- Extreme `81.88.213.101`: PASS, setup 82678 ms, 15908 B/s.
- Rostelecom `95.167.26.10`: AUTH FAIL after reaching the Rust path.
- Rostelecom `85.237.61.86`: SETUP FAIL.
- Vainah `188.0.190.35` 8 MiB finalist: AUTH FAIL on the first standalone run,
  so it was not used as the reliable single baseline.
- Yandex `77.88.8.3` reliable single: PASS twice, exact 8 MiB in 166017 and
  176740 ms at 50528 and 47462 B/s.
- Vainah and Yandex multipath: PASS twice, exact 8 MiB in 75408 and 95987 ms at
  111242 and 87393 B/s.
- Multipath lower rate was 1.841 times the reliable single lower rate and passed
  the 1.25 times Pareto gate. These TCP-bridge rates are compatibility evidence,
  not native UDP performance predictions.

## Completion

- Resolved outcomes: R1, R2, R3 and R4 verified.
- Commands and artifacts: pinned Linux Rust client, temporary 32-worker
  UDP-to-TCP DNS adapters, owned Cloudflare Access tunnel, strict SSH and exact
  1 MiB and 8 MiB transfers through MTS.
- Constraint and diff-scope check: server and Owenclave runtime unchanged; no
  direct fallback or persistent output; owned tunnel, adapters, payloads,
  processes and n-de1 target removed; Slipstream, private SSH and WARP remain
  active with zero carrier restarts.
- Final status: complete; actual Slipstream works through the MTS whitelist and
  the two-resolver path materially outperforms the reliable single path.
