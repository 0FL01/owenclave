# Goal: increase accepted multipath DNS throughput

Status: active
Source: user instruction 2026-09-09, commit all changes first, then continue iterative multipath DNS optimization for more throughput.
Last updated: 2026-09-09

## Objective

Find and retain a verified throughput gain over accepted two-path 28/28 workers,
not over the weaker single-path control. Preserve losing experiments and an exact
accepted phone/runtime identity. Do not claim a universal optimum.

## Frozen Contract

- R1: Commit all appropriate existing changes before experimentation. Evidence:
  operations d3dc1b9 (existing explore model/variant), 70d8931 (DE/path docs),
  Owenclave 4ba57ad (accepted runtime, safety patches, tests and prior evidence).
  Kotlin/JVM and five native fault scenarios passed before commits; scoped secret
  scans and diff whitespace checks passed. Status: verified.
- R2: Diagnose and test meaningful hypotheses on the actual phone against fixed.apk.
  Primary evidence: immutable APKs, equal assets/native unless explicitly changed,
  exact 1MiB DL, acknowledged 128KiB UL, capped concurrent bulk, loaded p95/max,
  lifecycle and bounded aggregate resource samples. Two balanced screening pairs;
  adoption requires six NEW CT/TC pairs, paired-median target gain >=10%, other
  direction loss <=10%, >=4 target wins and no failed required transfers. Target
  direction declared per hypothesis before screen. Status: in_progress.
- R3: Keep only verified benefit, record wins/losses and final rollback, verify
  retained runtime with local correctness/fault tests and Automatic/manual/MP
  TCP/UDP WARP acceptance. Stop VPN with zero children, commit appropriate results,
  no push. Primary evidence: this document, existing harnesses, installed hashes
   and service gates. Status: verified for restored runtime; results committed with
   this goal document. No candidate performance code retained.

## Constraints And Envelope

- Keep 56 aggregate TCP workers, 64 queued queries, one native child,
  separate path endpoints, DCUBIC/authoritative mode, owner deadlines, cancellation,
  pinning/stdin secrets/authentication/gVisor/independent streams/fail-closed.
- Fixed verified Yandex Safe .88 + Basic .1 TCP53 initially. Mixed filtering is
  still explicit opt-in, not Safe guarantee or automatic/background ranking.
- Target SlipstreamInstance.kt worker allocation first; native scheduler patches
  only after evidence identifies a cause. Tests and ignored build/dns-multipath
  immutable helpers/artifacts allowed. No source-cache resets, persistent traffic
  logs, captures, full configs, tokens, delivered APK directory changes or push.
- Server d7667b1e6dde9ef4669cda08f576307d851b3ec8e32736ebdeabae4e9926191b
  must retain path-loss safety fix; no rollback to crashing predecessor. No server
  edits planned. FlowRelay/WARP, token, certs and routing unchanged.
- One phone owner, foreground bounded windows only. Preserve failed windows; only
  proved confounds invalidate them. No blind 112-worker increase: blocking carrier
  tasks already total61 of default IO64 before other app work.
- Phone battery temperature >=42C or level <=20% stops measurement for cooldown/
  charging; no battery improvement inference from CPU or USB-powered tests.
- Prior losing deadline/queue16/BBR3/Base36/extra ARQ ideas are not blind repeats.

## Execution Directive

Complete the frozen Required Outcomes using the listed Change Envelope and Primary
Evidence. Work on the smallest unresolved outcome. Do not add requirements from
reviews, tests, tools, speculative risks, or optional source text. Stop substantive
work at a proven external blocker, approved budget boundary, or when no remaining
in-scope action has a falsifiable expected result; record evidence and smallest unlock.

## Current Checkpoint

R2 remains open, no additional throughput optimization accepted. H4 did not pass
confirmation. Two consecutive bounded diagnostic checkpoints did not reproduce the
failure or establish its cause; stop this experiment batch rather than keep running
the same probes until a favorable series appears. This is not a proven external
blocker, a universal optimum, or completion of the throughput objective.

Next materially different checkpoint: accepted-MP startup-only windows, explicitly
record Connected state, TLS phases and first-stream failure alongside bounded
aggregate FlowRelay SYN state. The12 successful control probes below shared one
startup and do NOT cover repeated startup. A demonstrated cause must precede a
revised pipeline candidate or a new acceptance series; do not erase failed H4
confirmation or merely repeat until six passing pairs. No background work remains.

## Current State

- Accepted build/dns-multipath/fixed.apk SHA-256
  c8f2c1943dfd75dc40d35e5243942e5a302c5a6ae7907ce2447631a627ca36ac;
  native64cbc7d115c7b3b1f23be1687571c51ed7cde0403c5d9f4a150940028b9067e2.
- Existing detailed build/acceptance history: 2026-09-09-yandex-multipath.md.
- Installed accepted fixed.apk, native64cbc7 restored in ignored jniLibs; production
  Kotlin/test files exactly match4ba57ad. UI restored manual MP, VPN stopped,
  zero children. Delivered Torrents APK directory untouched. app/build output may
  still contain a rejected candidate: only immutable fixed.apk is accepted.
- ADB device available. Server active/enabled, restarts0/1, stdout/stderr null;
  initial Slipstream memory6.2MB, flowd6.5MB; Slipstream CPU quota one core.
- Precommit Kotlin and16 JVM tests passed, five native fault scenarios passed
  with the exact corrected Debian server. Existing lint2371 errors/27 hints and
  two strict-clippy baseline errors remain documented, not suppressed.
- Native source inspection: loop burst sums per-resolver allowances; Busy poll
  target uses per-path cwin/pacing/RTT and is not divided by Android worker count.
  Equal total workers does not imply equal DNS load or independent provider capacity.

## Checkpoint History

- Preflight and commits completed before optimization edits. No push or remote edit.
- H1 build/compile/16 JVM tests pass. skew40.apk SHA-256
  6aca458b98d7d644d6d58a7a3e2eac4bbcc6a4979b6190e67cc27aa543f9d087,
  assets/native equal to fixed.apk. Initial CT and TC screens each failed WARP
  trace in the SECOND window, once candidate and once control; no transfer data
  in failed windows. Retained skew40-screen-{1,2}-{c,t}.jsonl. Successful first
  windows C DL252324/UL23399 B/s; T DL237084/UL19581 B/s, NOT complete paired evidence.
  Server still active restarts0/1 and WARP on. Separate C restart passed trace
  (default and IPv4; IPv6 connect failed), DL253114/UL23530. No demonstrated root
  cause or basis to invalidate the two failures. Add uniform35s intersession gap
  (server idle timeout30s) and safe trace timing/rc; two settled screens distinguish
  a reproducible throughput effect from intersession state. No server change.
- LTE baseline: Wi-Fi0, LTE, USB powered100%,27C. ya.ru HTTP302 in0.376s,
  Cloudflare HTTP301 in0.349s directly: forced-DNS LTE only, restriction NOT proved.
- H1 settled CT/TC screens both complete, required failures0. DL C263330/T208166,
  C236489/T190086 B/s; UL C21452/T14547, C22554/T23607. Both DL losses around20%,
  upload mixed, fails improvement criterion. Loaded max C0.711889/T0.784791s;
  bulk8/8 complete each. H1 rejected. Intersession timing is only a hypothesis for
  original trace failures, not proven; all original failures retained. H2 tests
  reverse skew rather than repeating more workers on the primary.
- H2 skew16.apk SHA-256236436847262fb2b1a07f0d1b47a829d7b486f84dd79a8e5c447e40ecb99bafc;
  build/compile/JVM pass, assets/native equal. Settled pair2 DL C262839/T187138,
  UL C20877/T23222 B/s. Pair3 DL C263080/T215177; control UL failed75s after handing
  all131072 bytes to curl, candidate acknowledged22553 B/s. Pair3 T completed later
  separately after diagnosis, so not a fresh contiguous successful pair. Pair1 C
  failed TLS rc35 in11.46s before upload; no T window. All failures retained.
  H2 rejected: both observed DL ratios lose18-29%, no valid second UL ratio.
  H2 sampled native lifetime CPU17.4-19.7% one core/RSS9.3-9.7MB/one thread, bg95-98
  threads (total app runtime, not worker count). CPU is not battery evidence.
- Three direct WARP POST131072 controls on DE acknowledged HTTP200 in2.53/0.27/0.30s.
  Slipstream cpu.stat nr_throttled=0, no CPU quota saturation observed, restarts0/1.
  Does not prove every failed phone upload is carrier-caused. Both skew directions
  lose DL; preserve equal allocation, investigate aggregate queue/service pressure.
- H3 diagnostic-only APK63d9a071e8e76eb55555b6559e79708bfaecc2104a18fc1d988640e4251af228,
  fixed native/assets and28/28, completed full window DL259001/UL14408 B/s, bulk4/4,
  loaded max0.707553. Volatile aggregate close counters, no traffic identities:
  path1 received4545/drop161/completed4384/response bytes3016528/queue123807ms/
  service657081ms; path2 received6412/drop84/completed6328/bytes4610069/
  queue289044ms/service924783ms; errors0. Approximate close snapshots:2.24% drops,
  mean queue28.2/45.7ms, service149.9/146.1ms. Supports pressure hypothesis, not proof
  that suppressing polls helps; response bytes include DNS overhead, not all useful
  payload. Native changed only at Busy authoritative send budget to test that cause.
- H3 pollcap.apk f2400d62f1ae6075d5835f46c4be437f851a23a7e2461014fdcbbbf248d5a167,
  native6f386b1bfc6ed1a5d4308fd18565fdfb34ba28164c5c3d1a5d1c1e28ed25215f.
  Incremental Android build, assemble/compile/JVM pass. Fixed assets and all other
  native libs equal. Initial build retried with existing build/android-ndk-r29 and
  build/bbr3/perl-root environment; no downloads or shared-source reset.
  Screening CT: DL C235674/T253429, UL23358/26886 B/s; TC: DL233846/242606,
  UL24274/29678. Target UL+15.1/+22.3%, DL+7.5/+3.7%; no required failures, bulk8/8
  each. Loaded max C1.114925/T1.143398s. Promising, not yet retained; screens excluded
  from six fresh confirmation pairs.
- H3 confirmation failed. Pair1 DL C270421/T266391, UL29890/23476 (-21.5%). Pair2 T
  DL280501 but UL rc28 after75.002s/131072 handed to curl, no HTTP acknowledgement;
  no C window. Required failure disqualifies adoption; no selective retry or erased
  window. Five local fault scenarios pass with corrected server. Revert poll cap.
- H4 minimal R2 envelope expansion: both worker skews lose DL; poll suppression
  fails confirmation. Diagnostic146-150ms serial service time and queue/drop pressure
  identify synchronous per-connection waiting as a different throughput constraint.
  Test depth2 without adding blocking threads/connections or unbounded queues.
  This increases bounded active queries and may increase offered DNS load; no CPU/
  battery saving claim. RFC7766 sections6.2.1.1/7 permit pipelining and require
  out-of-order ID matching (https://www.rfc-editor.org/rfc/rfc7766.html).
- H4 local adapter reorder/retry/close test passes (only unanswered query replayed).
  Initial combined Gradle run:16/17 tests pass, old isolated deadline fixture
  closeCancelsStalledConnectWriteAndRead fails with accept timeout. No fixture edit;
  this fixture is not production adapter. Assemble had not completed: pipeline2.apk
  is an uninstalled stale pollcap duplicate, detected by hash, NOT a pipeline build.
  Separate assemble/compile successful. Frozen pipeline2built.apk SHA-256
  f867f2d27a8f2a49cc8bc403a95c71adecb709d9ec68e3659255a63c405b87e7;
  native restored to64cbc7, all assets/native byte-equal to fixed.apk.
  Settled CT: DL C237443/T274592, UL21332/23142 B/s; TC: DL244772/293874,
  UL22007/21938. DL+15.6/+20.1%, UL+8.5/-0.3%, required failures0, bulk8/8 each.
  Loaded max C0.805431/T0.899026s. Start six fresh confirmation pairs, not adoption.
- H4 confirmation1 C DL245959/UL22186 B/s passes, T fails trace TLS rc35/HTTP000
  in11.598032s after DNS0.076616/connect0.082325s; no candidate transfer measurements.
  Retain pipeline2built-confirm-1-{c,t}.jsonl. NOT an invalidated window and no
  six-pair result. Candidate not adopted. Screening-only paired median DL+17.85%,
  UL+4.09%; inclusive interpolated loaded p95 C0.737209/T0.829507s, max
  C0.805431/T0.899026s. Native lifetime CPU C17.8-20.2%/T21.1% one core,
  RSS C9336-9452/T9568-9824KiB, bg threads C93-95/T95-96, USB100%/27C.
  This does not establish a battery benefit, independent capacity, or safe adoption.
- H4 diagnostic pipeline2-connect-diagnostic.jsonl: alternating default/IPv4,
  four traces and four128KiB acknowledged uploads all pass, UL5.21-5.64s. Direct
  DE WARP IPv4 HTTPS controls pass; IPv6 fails immediately, not the observed11.6s
  delay. control-connect-diagnostic.jsonl:12 accepted-control TLS probes sharing
  one startup all pass. Simultaneous90s aggregate FlowRelay SYN-SENT sampling:
  334 samples, max0. No endpoints/payloads captured, no service edits. FlowRelay's
 10s connect timeout is a hypothesis for the11.6s symptom, NOT a demonstrated cause.
- Rejected pipeline exact source/test diff retained only at ignored
  build/dns-multipath/pipeline2-source.patch, SHA-256
  eb673a9de8fe9f1fe5f1d6a9f352be059660e0f8081ec4d1b1ef81d36d400dc3.
  Native source poll cap removed; fixed Android native restored from fixed.apk.
  Do not use cached H3 build output as an accepted binary. No safety patch removed.
- Final restored compile and four focused MP JVM tests pass. Five exact native
  correctness/fault cases pass again:131072 bytes each direction + half-close for
  reorder/duplicate, path loss after ready, dead primary before ready; both dead and
  wrong pin exit1 with zero payload. Preexisting deadline fixture accept timeout
  during H4 remains recorded; no claim of a fresh all17-test success.
- Final fixed.apk TCP acceptance: MP1MiB DL3.678615s/128KiB UL5.260470s,
  manual4.859426/8.510348s, Automatic4.638312/7.955386s, HTTP200 exact bytes,
  HTTPS DE/WARP and one child each, then zero. Not paired throughput comparisons.
  Evidence optimization-final-ready-{multipath,single,automatic}.txt.
  Added readiness guard uses actual Stop content-description, shown only while
  Connected. Initial optimization-final-multipath.txt failed an incorrect helper
  text check: statusText Connected is computed but not displayed. Proven harness
  bug corrected, not a network failure and not an explanation of earlier failures.
- UDP acceptance preserved volatile WARP changes instead of treating stale IPs as
  passes: initial nine replies matched preceding2e571d reference but following
  reference changed; subsequent series stopped on Automatic mismatch. Background
  reference job1788950441577-34 failed after one successful probe with a5s UDP
  timeout. Do not claim that failed concurrent series verified every phone probe.
  Final short per-mode checks each have three fresh server references before/after:
  Automatic3/3 matches5f5bedec (phone epoch1788950655-666); MP3/3 matches43b1a0a4
  (epoch1788950976-987); manual3/3 matches43b1a0a4 (epoch1788951042-1053).
  All20-byte STUN requests/32-byte replies have matching fresh transactions. These
  checks resolve final egress acceptance, not historical failed performance windows.
- Final service state: Slipstream/FlowRelay active, NRestarts0/1 unchanged, output
  sinks null, memory4337664/4636672 bytes; WARP namespace unit active NRestarts0.
  No remote config/binary/routing changes. All diagnostic background jobs terminal.

## Completion

R1 verified; R3 restored-runtime safety and acceptance verified. R2 and objective
remain active: four meaningful hypotheses tested, no new gain adopted. Final phone
is accepted c8f2c194 APK with manual MP selected and VPN stopped. Only this evidence
document is newly committed; no push, rejected production changes, or hidden jobs.
