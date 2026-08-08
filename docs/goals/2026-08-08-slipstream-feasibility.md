# Goal: Slipstream DNS carrier feasibility

Status: active
Source: user instruction to focus on EndPositive/slipstream, 2026-08-08
Last updated: 2026-08-08

## Objective

Determine whether pinned Slipstream can carry the existing authenticated SSH
and WARP path over the real Android Tele2 DNS whitelist at least 1.5 times as
fast as controlled dnstt, then restore the stable dnstt service.

## Execution Directive

Complete the frozen Required Outcomes using the listed Change Envelope and
Primary Evidence. Work on the smallest unresolved outcome. Do not add
requirements from reviews, tests, tools, speculative risks, or optional source
text. Finish when every required outcome is resolved and affected constraints
remain satisfied.

## Frozen Contract

### Required Outcomes

- R1: produce a bounded standalone Slipstream client and server proof.
  - Source: user request to evaluate Slipstream as the next DNS carrier.
  - Acceptance: pinned `v0.1.1` server and an Android arm64 client expose one
    local raw TCP path to the existing private SSH target; authenticated SSH and
    one exact payload through WARP pass before public DNS benchmarking.
  - Primary evidence: pinned source and artifact digests, Android ELF dependency
    check, SSH host-key verification, exact payload hash and WARP egress proof.
  - Status: pending
  - Evidence:

- R2: compare single-resolver and native multipath Slipstream on the real path.
  - Source: user performance objective and Slipstream multipath design.
  - Acceptance: single Tele2 `.174` and multipath `.174`, `.175`, Yandex UDP
    each attempt two exact 8 MiB downloads through recursive DNS, Slipstream,
    authenticated SSH and WARP; every success or failure is recorded.
  - Primary evidence: exact byte count and SHA-256, setup time, payload duration
    and byte rate under a 180-second bound per attempt.
  - Status: pending
  - Evidence:

- R3: apply the material replacement gate.
  - Source: previously approved 1.5 times performance gate.
  - Acceptance: continue toward Owenclave integration only when the better
    Slipstream mode completes both exact transfers and its lower-of-two rate is
    at least 243186 B/s, 1.5 times the controlled dnstt baseline of 162124 B/s.
  - Primary evidence: calculated lower-of-two rate and ratio.
  - Status: pending
  - Evidence:

- R4: leave the production DNS reserve path in its stable state.
  - Source: repository safe-change workflow and user reserve-channel objective.
  - Acceptance: dnstt is restored after the bounded test regardless of result;
    its public UDP listener, private SSH target, WARP egress and Android exact
    payload work, with temporary Slipstream state removed.
  - Primary evidence: service/listener/restart checks and one final dnstt exact
    payload after rollback.
  - Status: pending
  - Evidence:

### Constraints

- C1: use EndPositive/slipstream `v0.1.1`, commit
  `397850b16e70a52513269134b2c7b9aa13a56745`, and record any minimal local
  patch separately.
- C2: preserve the existing SSH authentication, host-key pin, private
  `10.200.0.2:41924` target and isolated WARP egress; no direct fallback.
- C3: use the same 8 MiB payload and SHA-256 as the completed dnstt benchmarks.
- C4: no persistent Slipstream output, traffic logs, credentials or TLS secrets.
- C5: public UDP `:53` is exclusive. Obtain explicit approval immediately before
  the bounded dnstt outage, arm an automatic dnstt rollback first, and never
  leave both carriers stopped.
- C6: Owenclave and production integration remain unchanged until R3 passes.

### Non-goals

- Owenclave profile, UI, Room, APK or VpnService changes before the gate.
- DoT or DoH support; pinned Slipstream supports raw UDP resolvers only.
- A permanent public Slipstream service, DNS demultiplexer or second IP.
- Fixing upstream lifecycle, authentication or general production-hardening
  issues beyond a blocker to this bounded single-client experiment.
- System TUN support.

## Change Envelope

- Target: reproducible arm64 Android test client, bounded n-de1 test server,
  temporary Android Termux processes, one exact payload target and this goal.
- Expected repository paths: this goal and, only if needed for reproducibility,
  one pinned build script under `bin/lib/slipstream/`.
- Allowed remote artifacts: temporary binaries, certificate, automatic rollback
  unit and payload under a dedicated test directory; no enabled Slipstream unit.
- Forbidden artifacts: Room migration, new proxy type, permanent listener,
  public SOCKS, recursive DNS, direct fallback, persistent logs or secrets in
  Git/tool output.
- Budget: one direct smoke proof, two 8 MiB attempts per Slipstream mode and a
  maximum 20-minute public dnstt outage.

## Benchmark Contract

- Client: connected Android device, foreground Termux UID, Tele2 LTE underlying
  network, Owenclave VPN stopped during isolated measurements.
- Server: n-de1, Slipstream target exactly `10.200.0.2:41924`.
- Application path: local Slipstream TCP -> existing OpenSSH SOCKS -> exact HTTP
  payload inside `warpns`; SSH remains the authenticated end-to-end layer.
- DNS modes:
  - single: `176.59.127.174:53`;
  - multipath: `.174` first, then `176.59.127.175:53` and `77.88.8.8:53`.
- Slipstream defaults: DCUBIC, GSO off, 400 ms keepalive.
- Payload: 8388608 bytes, SHA-256
  `2daeb1f36095b44b318410b3f4e8b5d989dcc7bb023d1426c492dab0a3053e74`.
- Ranking: highest lower-of-two exact byte rate; any timeout, early EOF, crash or
  hash mismatch fails that attempt.

## RECON Findings

- Latest release is `v0.1.1` from 2026-04-12; main points to the same commit.
- Slipstream maps local TCP streams to one fixed server TCP target over QUIC in
  TXT DNS queries and responses. Repeated `--resolver` creates QUIC paths.
- Resolver zero establishes the connection; later resolvers are not startup
  fallback. A dead first resolver prevents connection establishment.
- Pinned source supports UDP DNS only, not DoT or DoH.
- The official Linux arm64 binary uses glibc, `/lib/ld-linux-aarch64.so.1` and
  dynamic OpenSSL, so it cannot run natively on stock Android. The test client
  needs an NDK/Bionic build with its crypto dependencies packaged or static.
- The client TCP listener binds `0.0.0.0`; the Android test artifact must bind
  loopback only.
- The client does not verify the server certificate and Slipstream has no client
  authorization. Existing pinned SSH remains the identity and access boundary.
- Upstream PR 27 documents unmerged server FD leaks and a use-after-free under
  active use. This blocks unattended production use, not a bounded foreground
  feasibility test with external timeout and guaranteed rollback.
- Stock Slipstream binds wildcard UDP `:53` without socket sharing. n-de1 has one
  public IPv4 and dnstt already owns that endpoint; a recursive test therefore
  requires a brief exclusive cutover. DNS cannot advertise another authority
  port, and adding a demultiplexer is outside this Pareto experiment.
- If the gate passes, Owenclave can reuse `TYPE_SSH`, SSHBean, the existing raw
  loopback TCP boundary, SSH-banner readiness, gVisor-only policy and WARP path.
  A versioned carrier discriminator can avoid a Room migration.

## Experiment Log

- RECON complete; no live service or application changes were made.

## Current Checkpoint

- Closes: R1.
- Smallest next action: create and verify one pinned Android arm64 client build,
  then run a private high-port direct smoke without touching public UDP `:53`.
- Expected evidence: native Android execution, exact payload and authenticated
  SSH/WARP proof.
- Stop or replan if: a native bounded client cannot be built without broad
  upstream or Owenclave changes.

## Current State

- Resolved: architecture, pin, resolver semantics, Android gap, security limits
  and public listener conflict are known.
- Last relevant evidence: dnstt remains active and Owenclave working; RECON was
  read-only.
- Blocker: public recursive benchmark later requires explicit approval for a
  maximum 20-minute dnstt outage. R1 can proceed without that outage.
- Next: arm64 client build and direct smoke.

## Material Decisions

- 2026-08-08: benchmark standalone transport before any Owenclave integration.
- 2026-08-08: retain SSH host verification and authentication because pinned
  Slipstream does not authenticate its endpoint.
- 2026-08-08: use a bounded sequential carrier cutover instead of adding a DNS
  demultiplexer, second IP or permanent parallel stack.
- 2026-08-08: use `.174` as resolver zero because source shows no initial-path
  fallback and `.174` is the only proven high-throughput resolver.

## Checkpoint History

- 2026-08-08: RECON completed against upstream docs, release metadata, source,
  open issues, Owenclave runtime and live n-de1 listener topology; contract
  frozen before implementation or service changes.

## Completion

- Resolved outcomes:
- Commands and artifacts:
- Constraint and diff-scope check:
- Final status:
