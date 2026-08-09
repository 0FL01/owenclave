# Goal: Rust Slipstream DNS carrier

Status: unmet
Source: user instruction to replace dnstt and C/C++ Slipstream with the Rust implementation, 2026-08-08
Last updated: 2026-08-08

## Objective

Replace the DNS carrier with pinned Rust Slipstream, prove the existing
authenticated SSH and WARP path over the real Android Tele2 whitelist, and use
it in Owenclave only if it is at least 1.5 times as fast as controlled dnstt.

## Execution Directive

Complete the frozen Required Outcomes using the listed Change Envelope and
Primary Evidence. Work on the smallest unresolved outcome. Do not add
requirements from reviews, tests, tools, speculative risks, or optional source
text. Finish when every required outcome is resolved and affected constraints
remain satisfied.

## Frozen Contract

### Required Outcomes

- R1: produce a pinned standalone Rust Slipstream path.
  - Source: user selected the Rust implementation as priority one.
  - Acceptance: the pinned server and native Android arm64 client expose one
    local raw TCP path to private SSH `10.200.0.2:41924`; authenticated SSH and
    one exact payload through WARP pass.
  - Primary evidence: source pin, binary digests, Android ELF check, SSH host-key
    verification, exact payload hash and WARP egress proof.
  - Status: verified
  - Evidence: official server SHA-256
    `236b76c41211510ee94ee61616bb2ae8735c70bd6cf9df00d3c668c2837c6f93`;
    Android client SHA-256
    `a68762441b79abda89ef1c68437e93f1eb706c52960a9bb12cedec8a1cb067ac`.
    The Android ELF uses `/system/bin/linker64` and only `libdl.so`/`libc.so`.
    Strict pinned SSH carried exact payloads, and an external probe matched the
    `warpns` WARP address while differing from host direct egress.

- R2: measure Rust Slipstream over the real operator resolver.
  - Source: user selected one operator UDP resolver first.
  - Acceptance: two exact 8 MiB downloads complete through Tele2 `.174`, public
    recursive DNS, Rust Slipstream authoritative pacing, authenticated SSH and
    WARP.
  - Primary evidence: exact byte count and SHA-256, setup time, duration and byte
    rate under a 180-second bound per attempt.
  - Status: verified
  - Evidence: both 8388608-byte downloads matched SHA-256. Attempt 1 took
    57015 ms at 147129 B/s; attempt 2 took 58853 ms at 142534 B/s.

- R3: apply the material replacement gate.
  - Source: previously approved 1.5 times performance gate.
  - Acceptance: continue to Owenclave only when both transfers pass and the
    lower rate is at least 243186 B/s, 1.5 times the controlled dnstt baseline
    of 162124 B/s.
  - Primary evidence: calculated lower-of-two rate and ratio.
  - Status: verified
  - Evidence: lower-of-two 142534 B/s is 0.879 times dnstt and 100652 B/s below
    the 243186 B/s gate. Rust Slipstream did not qualify for app integration.

- R4: replace the Owenclave carrier after a passing gate.
  - Source: user instructed implementation work, not RECON only.
  - Acceptance: the existing imported SSH-backed profile starts bundled Rust
    Slipstream in gVisor mode and one exact Android VpnService payload exits via
    WARP; an installable arm64 APK is produced.
  - Primary evidence: Android import/profile state, exact payload, observed WARP
    egress, APK path and SHA-256.
  - Status: not_applicable
  - Evidence: R3 did not pass; the frozen contract forbids integration below
    the 1.5 times gate.

### Constraints

- C1: use the official Rust server release `v0.1.1`, commit
  `c7b265c11dd13d6dfd601bc81d54d61ee9bdde23`; use the official DNSTT Client
  v2.2.0 Android artifact whose Slipstream submodule is pinned to
  `bc772dd07d9a136dbd7553b0da575526de207847`; record both digests.
- C2: preserve SSH authentication, host-key pin, private
  `10.200.0.2:41924` target and isolated WARP egress; no direct fallback.
- C3: use the completed benchmark payload: 8388608 bytes, SHA-256
  `2daeb1f36095b44b318410b3f4e8b5d989dcc7bb023d1426c492dab0a3053e74`.
- C4: no persistent carrier output, traffic logs, credentials or TLS secrets.
- C5: dnstt downtime is approved. Stop and disable it, retain rollback files
  until Rust Slipstream passes, and do not run both public UDP `:53` listeners.
- C6: first test only Tele2 UDP resolver `176.59.127.174:53`; use the smallest
  upstream pacing mode that establishes the real recursive path.

### Non-goals

- Original EndPositive C/C++ Slipstream implementation.
- Resolver multipath, DoT, DoH or authoritative direct mode.
- New proxy type, Room migration, direct fallback or System TUN support.
- Tuning before the controlled single-resolver result.

## Change Envelope

- Target: pinned Rust server/client, bounded n-de1 cutover, Android arm64 proof,
  then the smallest Owenclave carrier replacement after the gate.
- Expected paths: this goal, `bin/lib/slipstream/`, existing SSH bean/import and
  sidecar lifecycle files, four ABI artifacts only when their builds succeed.
- Allowed remote artifacts: one server binary, TLS material, systemd unit and
  existing private SSH target; timestamped backups and temporary benchmark data.
- Forbidden artifacts: public SOCKS, recursive DNS, direct fallback, persistent
  traffic output, secrets in Git or a second DNS listener.
- Budget: one standalone smoke and two 8 MiB attempts before app integration.

## Benchmark Contract

- Client: connected Android, foreground Termux UID, Tele2 LTE, Owenclave VPN
  stopped during standalone measurements.
- Path: local Rust Slipstream TCP -> existing pinned OpenSSH -> private SSH in
  `warpns` -> exact HTTP payload -> WARP.
- DNS: recursive `176.59.127.174:53`, domain `t.x.ass-peak.de`, client
  `--authoritative` pacing.
- Ranking: lower of two exact byte rates; timeout, early EOF, crash or hash
  mismatch fails the attempt.

## Current Checkpoint

- Closes: none; the closure check is terminal.
- Smallest next action: none inside the frozen performance gate.
- Expected evidence: complete.
- Stop or replan if: a new user instruction accepts lower performance or selects
  another carrier.

## Current State

- Resolved: standalone Rust Slipstream, authenticated SSH, exact payload, WARP
  egress and two controlled measurements all passed functionally.
- Last relevant evidence: lower-of-two is 142534 B/s versus dnstt 162124 B/s.
- Blocker: the mandatory 1.5 times gate is unmet.
- Next: none inside this goal.

## Material Decisions

- 2026-08-08: superseded the C/C++ `EndPositive/slipstream` direction and its
  restore-after-test requirement by explicit user instruction.
- 2026-08-08: use official prebuilt, source-pinned server and Android client
  artifacts for the performance gate; defer an NDK rebuild until the gate passes.
- 2026-08-08: keep SSH as the authenticated access boundary and test one proven
  operator resolver before any multipath work.

## Experiment Log

- C/C++ RECON completed without deployment; that implementation was discarded.
- Rust upstream frozen before runtime changes; dnstt rollback artifacts retained.
- A host source build failed against Fedora OpenSSL 3.5 because upstream
  picoquic includes removed `openssl/engine.h`; a vendored OpenSSL attempt then
  failed because host Perl lacks `FindBin`. Official pinned artifacts avoided
  changing the host toolchain.
- Recursive client pacing reached only idle timeout through Tele2 `.174`; the
  same resolver and DNS wire format reached QUIC ready with authoritative pacing.
- dnstt was stopped and disabled after backup
  `/etc/systemd/system/dnstt.service.bak.20260808T181906Z-rust-slipstream-cutover`.
  Rust `slipstream.service` now exclusively owns public UDP `:53`, targets only
  private SSH and is active/enabled with zero restarts.
- Two exact transfers passed but measured 147129 and 142534 B/s, below both the
  prior dnstt result and the replacement gate. Owenclave remained unchanged.

## Completion

- Resolved outcomes: R1 and R2 verified; R3 applied and failed; R4 is not
  applicable because its prerequisite did not pass.
- Commands and artifacts: official pinned binaries, `systemd-analyze verify`,
  Android ELF/digest checks, strict SSH, exact SHA-256 transfers and WARP egress.
- Constraint and diff-scope check: one operator resolver, no multipath, no app or
  database change, no direct fallback, temporary payload and client processes
  removed.
- Final status: unmet; Rust works through the whitelist but is slower than the
  controlled dnstt baseline and far below the 1.5 times gate.
