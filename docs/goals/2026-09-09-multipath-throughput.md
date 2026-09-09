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
  adoption uses fresh balanced confirmation, initially six NEW CT/TC pairs, with
  uncertainty and failures retained. The user's latest instruction supersedes the
  arbitrary >=10% gate: a repeatable small upload or completion-latency benefit is
  useful, including approximately +5-8% UL with a minor DL loss. Target
  direction declared per hypothesis before screen. Status: in_progress. Reusable
  Python-on-Android smoke, H6/H7 re-screens, H8 native-only build/screen/six-pair
   confirmation, H9/H10 topology campaign and accepted-APK gVisor calibration executed
   below; no adopted gain. The executed campaigns are closed; the broader gain
   objective remains active, not claimed achieved or globally exhausted.
- R3: Keep only verified benefit, record wins/losses and final rollback, verify
  retained runtime with local correctness/fault tests and Automatic/manual/MP
  TCP/UDP WARP acceptance. Stop VPN with zero children, commit appropriate results,
  no push. Primary evidence: this document, existing harnesses, installed hashes
  and service gates. Status: verified for this campaign. Original production runtime,
  accepted APK and production-token manual MP restored, VPN stopped/zero children.
  Fresh final MP/Automatic/single TCP/UDP WARP and acknowledged-upload windows pass;
  earlier failures remain recorded, not claimed fixed. No performance code adopted.

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
  must retain path-loss safety fix; no rollback to crashing predecessor. Slipstream
  binary, ingress, certificate and target remain unchanged. Production-token
  FlowRelay resolver/TCP/UDP sockets remain bound to CloudflareWARP, without fallback.
- Latest user continuation authorizes a SEPARATE DE direct test path, not changing
  production Slipstream/WARP. Minimal new envelope: private loopback lab Slipstream
  and separate FlowRelay direct/WARP test instances, distinct lab token and pinned
  certificate, bounded resources/null output, removed from runtime after tests.
  Follow-up authorization permits disabled-by-default private FlowRelay test-token
  dispatch to one authenticated DE direct backend, replacing only the test OPEN
  token. Unknown tokens and failed test backends never enter production handling.
  Test-only binding and a narrow namespace-host address/port firewall rule are
  allowed; no public listener or broad namespace exception. Back up and test before
  the necessary FlowRelay restart; prove production TCP/UDP WARP before/after.
  A temporary phone test token must be restored exactly; delivered APK untouched.
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

Closed continuation: H9 adds Basic .8 as a third native path with19/19/18
workers and22/21/21 queues. H10 instead keeps two native paths and shards only the
second adapter between Basic .1/.8 using14 persistent workers each and its shared32
queue; primary .88 remains28/32. Both retain56/64, accepted ELF, exact workload,
strict Python query validation/deadlines and one foreground phone owner. No nested
task tool exists; independent hypotheses are compared by this single owner.
Predeclared target: acknowledged sequential/loaded8/32KiB completion and reliability,
with128/512KiB/DL/recovery as non-regression context. Modest reproducible UL gains
with minor DL loss qualify. Two balanced pairs screen each topology, then six NEW
confirmation pairs for a credible candidate; APK/gVisor promotion only afterward.
H9 tests exposed per-path QUIC scheduling; H10 tests extra resolver capacity without
extra native polling allowance, at the explicit cost of pooled RTT/loss on path2.
Neither equal workers nor same-provider addresses proves equal load/independent
capacity. Temporary direct dispatch/fixture require two disclosed shared restarts;
no production-token egress, WARP daemon, default profile or accepted APK change.

H9 screen CT/TC completed: all16 candidate uploads acknowledged, but sequential8KiB
+16.77/-39.67%, loaded32KiB+21.02/-19.23%, DL+12.85/-28.63%; reject expansion to a
third native path at this allocation. H10 screen all16 candidate uploads passed,
loaded8KiB+19.97/+7.56% merits six NEW confirmation pairs despite mixed sequential
8KiB-30.72/+9.13%, sequential32KiB+20.13/-13.16%, loaded32KiB-4.18/+0.27%,
DL-11.17/+3.93%. These regressions stay in the decision; no APK promotion.
Evidence labels `h9-screen`, `h10-screen` in `build/dns-smoke` (two pairs each).
Thirteen focused Python tests passed, unchanged Flowd race/vet/build passed. Fresh
direct/CloudflareWARP interface-bound HTTPS+transaction-STUN references matched
test/production dispatch respectively; unknown token and stopped test backend closed,
production still WARP during test-down. Backup `/root/backups/owenclave-rnd-20260909T154514Z`;
temporary `/run/owenclave-rnd`, `flowd-rnd-direct.service`, `owenclave-rnd-fixture.service`,
`/etc/systemd/system/flowd.service.d/90-rnd.conf`, `/usr/local/bin/flowd-rnd` are REMOVED,
including all temporary rules, keys and local/remote credential copies. WARP PID589
unchanged, Flowd/Slipstream NRestarts0. Initial missing directory/executable bit were
fixed before any restart. H10 six-pair confirmation rejects the screened gain;
details and closure below. No experiment/background phone work remains active.

### H9/H10 Confirmation And Closure

Six NEW balanced CT/TC H10 pairs (`h10-confirm`) used identical accepted ELF/APK,
fixture, script and LTE preflight. The primary was still Safe .88; only the secondary
adapter was worker-sharded over Basic .1/.8. All48 uploads per arm acknowledged exact
size/hash, all60 foreground requests per arm passed, no startup failure. Native
screen+confirmation totals:20 windows,160/160 uploads, including80 short8/32KiB
uploads across both arms and load conditions. All windows stopped with zero children.

| H10 confirmation metric | Median paired change | Wins | Range | Descriptive 95% median bootstrap interval |
| --- | ---: | ---: | ---: | ---: |
| Sequential8KiB upload | +13.62% | 4/6 | -42.87 to +86.57% | -30.59 to +57.18% |
| Sequential32KiB upload | -23.28% | 1/6 | -62.65 to +4.86% | -47.36 to +0.49% |
| Loaded8KiB upload | -3.81% | 3/6 | -13.69 to +22.85% | -12.33 to +18.57% |
| Loaded32KiB upload | -15.75% | 2/6 | -30.89 to +90.48% | -30.13 to +45.81% |
| Loaded512KiB upload | +5.09% | 5/6 | -18.91 to +24.34% | -8.59 to +23.55% |
| Sequential1MiB download | -17.30% | 1/6 | -24.25 to +19.73% | -23.82 to +8.43% |

Other medians: sequential128KiB-10.87% (1/6 wins; interval-20.39 to-0.03%),
sequential512KiB-7.48% (2/6), loaded128KiB-3.13% (2/6), recovery-12.15% (2/6).
Sequential32KiB completion p50 C1.88495s -> T2.38791s, p95/max2.54747s ->5.02371s;
loaded32KiB p50 2.33646s ->2.73248s, max4.36240s ->3.45978s. Loaded512KiB p50
29.6007s ->27.4252s, max36.2352s ->34.8697s. Startup p50 .9521s ->1.0356s,
max1.2442s ->1.6918s. Report retains every size, phase, tail and failed-control ratio
semantics; intervals are descriptive with only six pairs, not independent LTE draws.

Decision: reject H10, not because +5.09% is below an arbitrary threshold, but because
the predeclared short-upload target regressed and download loss was not minor.
The screened loaded8KiB improvement disappeared. H9 was rejected at screen for
inconsistent short uploads and large second-pair DL loss. Neither result proves that
all third-path allocations or all scheduling strategies are bad. No credible candidate
qualified for Kotlin/native APK promotion, so no candidate APK was built or installed.
The retained changes only make both experiments reproducible in the smoke harness.

Actual accepted-APK gVisor calibration used two DE-direct windows
`rnd-gvisor-direct-{1,2}`:16/16 uploads plus download/recovery passed. Production
`rnd-gvisor-production-{mp,single,automatic}-1` passed24/24 uploads plus download/
recovery, HTTPS WARP and transaction-checked UDP. Each mode had one captured child;
stop returned to zero. These are additional real APK observations, not equivalence
between Python adapters and Kotlin's per-I/O blocking implementation. No synthetic
result establishes a Telegram fix, and no account/message access occurred. Earlier
TLS/ack failures remain evidence; zero failures in this campaign do not erase them.
This was forced DNS over available LTE, not proof of a restricted-LTE allowlist.

Restoration: original production binary/base unit/nftables byte-identical to preflight,
test token rejected by original FlowRelay, test listeners40003/40004/40005 absent,
private production40001 present, no test drop-in or retained traffic logs. Original
FlowRelay predates `-check`; that unsupported test failed before rollback, then a
bounded namespace-loopback startup/connect/terminate gate and original-unit verify
passed. Immediate post-restart connect initially raced Type=simple readiness; bounded
polling succeeded without another restart. Final actual MP/single/Automatic HTTPS/UDP
passed again after original-binary restoration, with fresh interface-bound WARP
references (final UDP fingerprint c1656e010c28 matched). WARP PID589 never restarted.
Two disclosed shared FlowRelay/Slipstream restarts were deployment and restoration.
No fixture or direct dispatch remains. Production-token manual MP restored, accepted
APK unchanged, VPN stopped, zero children; final preflight100%/28C at16:47:21Z.
Closure checks:13 smoke unit tests passed, all three retained reports regenerated
without manifest drift, both repositories' `git diff --check` and scoped secret
scans passed. Flowd race/vet/build and systemd/nft gates passed before deployment;
Android/native production source was unchanged, so no new APK build or broad lint
rerun was needed. Existing lint/clippy failures below remain unsuppressed.

Reproduce reports without a phone/server:

```sh
python3 bin/lib/slipstream/smoke/report.py --directory build/dns-smoke --label h9-screen --pairs 2
python3 bin/lib/slipstream/smoke/report.py --directory build/dns-smoke --label h10-screen --pairs 2
python3 bin/lib/slipstream/smoke/report.py --directory build/dns-smoke --label h10-confirm --pairs 6
```

For fresh measurements provision new private credentials/short-lived fixture using
the smoke README/DE operations boundary, then use `pair.py` with accepted fixed.apk
as both `--control` and `--candidate`, `--candidate-topology third` or `shard`, pairs1/2
for a new screen label. Confirmation uses six NEW pairs/new label. Artifact identity:
device.py SHA256482a1f2149cb6444eafe420b9795bc6996600e8faa1a00b3b307a8b11541e321;
fixture certificate SHA25603fe32c062ad3ba2af77d6a7eb08b5bfcfd0ee32988aac76d0d928c59282276b.
All retained JSONL is aggregate-only under ignored `build/dns-smoke`; credentials
are gone, so old secret paths cannot resume deployment. A next campaign must select
a materially different scheduling hypothesis, not repeat these same allocations or
promote the loaded512KiB subset while ignoring short uploads.

Executed campaign: reusable Python ON Android invokes native Slipstream under LTE
with VPN stopped, before any candidate APK build. See Android Native Smoke below.
Short synthetic audio-like 8/32/128/512KiB uploads measure startup, TLS, completion,
acknowledgement and stalls, both sequentially and under bounded load. No Telegram
account data or real message sending. Native smoke screens are not gVisor acceptance;
credible candidates must subsequently pass the actual APK workload and TCP/UDP gates.
H6 was re-evaluated for upload (its old DL-target rejection is not current policy),
then H7/H8 data-burst changes with polling unchanged. All rejected with current best
preserved. Two screen pairs select, fresh balanced confirmation estimates small
benefits; report every ratio, wins, p50/p95/max and bootstrap interval, not merely a
threshold. Extra candidate startup/upload failures prevent adoption; ambiguity can
justify further evidence, not erasure of failed windows or a fake global optimum.

Minimal R2 envelope expansion: tracked device Python/ADB orchestration, bounded
serial TCP adapters 28/32 per path, tests of the actual Python implementation and
an ephemeral authenticated, source-restricted HTTPS size/hash acknowledgement
fixture on DE. Private test-token dispatch may be reinstated with cap16/backend32;
production-token WARP semantics/pin remain unchanged. Disclose the two planned
Flowd/Slipstream restart boundaries before deployment/restoration. Fixture and test
credentials/runtime are removed after the campaign. Accepted APK/delivery unchanged
unless a candidate passes real gVisor confirmation. Python scheduling/timeout and
SOCKS timing differences require same-control APK calibration, never equivalence claims.

## Current State

- Accepted build/dns-multipath/fixed.apk SHA-256
  c8f2c1943dfd75dc40d35e5243942e5a302c5a6ae7907ce2447631a627ca36ac;
  native64cbc7d115c7b3b1f23be1687571c51ed7cde0403c5d9f4a150940028b9067e2.
- Existing detailed build/acceptance history: 2026-09-09-yandex-multipath.md.
- Installed accepted fixed.apk, native64cbc7 restored in ignored jniLibs; production
  Kotlin/test files exactly match4ba57ad. UI restored manual MP, VPN stopped,
  zero children. Delivered Torrents APK directory untouched. app/build output may
  still contain a rejected candidate: only immutable fixed.apk is accepted.
- ADB device available, latest100%/28C USB. Restored Slipstream/Flowd active,
   PIDs3119971/3119970, NRestarts0/0 stable, stdout/stderr null/no drop-ins. H9/H10
   had two planned shared restart boundaries (deployment/restoration), in addition
   to the preceding H6/H7/H8 smoke and dispatch campaigns. WARP PID589 never restarted.
- Precommit Kotlin and16 JVM tests passed, five native fault scenarios passed
  with the exact corrected Debian server. Existing lint2371 errors/27 hints and
  two strict-clippy baseline errors remain documented, not suppressed.
- Native source inspection: loop burst sums per-resolver allowances; Busy poll
  target uses per-path cwin/pacing/RTT and is not divided by Android worker count.
  Equal total workers does not imply equal DNS load or independent provider capacity.

## Checkpoint History

- H6 envelope: temporarily accept explicit tcp+mp://77.88.8.1:53 as the reversed
  existing pair in DnsttFmt.paths, with its parser regression test. Existing .88,
  single and Automatic semantics, native, worker/queue allocation remain unchanged.
  Adopt only after direct comparison and production safety gates; otherwise remove.
- H6 first direct screen: C DL228871/UL21785 B/s, fifth loaded request TLS rc35
  at0.516661s; T DL259028/UL23001, all payloads pass but loaded2.563150/2.511127s.
  All failures retained. Aggregate private backend socket observations during T
  reached the test dispatch ceiling8 for multiple three-second samples; test
  resource saturation is a material confound, not proof of the earlier TLS cause.
  Increase only the optional test cap8 to16 and backend max16 to32, still under
  the production total128 and unchanged64MiB/50% backend limits. Repeat fresh
  screen16 CT/TC, exclude cap8 windows from adoption. Flowd restart propagates to
  Slipstream through Requires; this is now explicitly anticipated. WARP unchanged.
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

## Executed Continuation

- Startup-only investigation: `python build/dns-multipath/startup-diagnostic.py`,
  immutable startup-diagnostic.jsonl. Six independent accepted-MP starts with35s
  intersession gaps, Connected at first UI check, one child, zero after each stop.
  Six first trace TLS requests and six acknowledged128KiB uploads all HTTP200.
  Trace total0.548-0.724s; upload4.919-7.059s; battery100%,26C. These are independent
  startups, unlike the prior12 requests inside one connection. No11.6s TLS failure
  reproduced; no conclusion that readiness or pipeline caused historical failures.
- Concurrent bounded server observation job1788951504589-41 completed:913 samples
  over480s, FlowRelay PID-only socket state/queue aggregates, no addresses/payloads.
  Max SYN-SENT11, ESTAB39, FIN-WAIT-1 8, recv9196/send5077 bytes. No per-flow
  attribution; clocks were not calibrated for exact phone/server timestamp joins.
  Counts alone neither prove session-limit saturation nor identify a failed dial.
  An attempted remote read of its local SSH spool failed; check_process recovered
  completed aggregate output. No production traffic logging was enabled.
- Readiness/call-chain inspection: SlipstreamInstance drains native QUIC
  `Connection ready`; streams.rs emits it on picoquic_callback_ready. It is not a
  destination probe. flow_relay.rs sends SOCKS success before returning OPEN, then
  streams.rs dispatches NewStream under the5s local SOCKS handshake timeout. Thus
  curl TCP connect timing inside gVisor is not proof of remote destination connect.
  FlowRelay main.go parses authenticated OPEN under5s header deadline, clears that
  read deadline and dials under10s total context; up to16 resolved addresses are
  attempted sequentially, all bound to CloudflareWARP. Relay then preserves normal
  independent stream lifetime, not an HTTP response deadline. No deadline change
  is justified by matching a wall-clock duration alone.
- The historical JVM accept timeout is in src/test-only DnsTcpExchangeTest, not the
  packaged adapter. Its read phase compares an accepted peer port with localPort of
  a Socket concurrently connecting, after leaving the write-phase peer in backlog.
  This unsynchronized comparison is a plausible scheduling race; the failed run
  did not establish it conclusively. It does not reproduce Android TLS or upload
  failure. Neither fixture nor production code was changed to hide that failure.

### H5: Retain Newer Queued Queries

- Predeclared target DL, same gates. Motivated by diagnostic adapter drops2.24% and
  mean queue waits28/46ms. MP-only DROP_OLDEST replaces oldest unsent queued query
  rather than rejecting new trySend on overflow; 28/28 workers,32/32 queues, active
  requests, deadlines, single/Auto, native polling and server unchanged. This might
  reduce stale polling, or instead lose useful payload/increase reordering.
- Gradle `:app:assembleOssRelease :app:compileOssReleaseKotlin
  :app:testOssDebugUnitTest --tests '*DnsMultipathAdapterTest'
  --tests '*DnsttMultipathTest' --console=plain --quiet` passed with
  ANDROID_HOME=/home/stfu/Android/Sdk; four focused JVM tests, not full lint/clippy.
  Frozen freshqueue.apk SHA-256
  fe5909eb76327f211ba293d9eded9af81cd01c81f970f6d442ac8bf95b367de4;
  native64cbc7 and assets/other native entries equal accepted control (comparison
  excludes generated assets/dexopt/baseline.prof and baseline.profm). Hashes
  checked locally and after each installation. Preserved source diff SHA-256
  acbee3b998ea50b34ff6c77a1f4f3e8b8b994d02a8094b35c621366d678d499d
  at ignored build/dns-multipath/freshqueue-source.patch.
- Commands: `python build/dns-multipath/optimize.py freshqueue settled 1 ct <hash>`
  and `settled 2 tc`; then NEW `confirm 1 ct` and `confirm 2 tc`, same frozen hash.
  The literal hash is above. All freshqueue-*.jsonl windows preserved, no overwrite.
- Screening rates B/s: pair1 C240161/20619, T284301/29877 (DL/UL); pair2
  C257215/22402, T284170/27977. Paired medians DL+14.43%,UL+34.89%; required failures0,
  bulk8/8 both. Loaded inclusive p95 C0.772434/T0.723218s, max0.809191/0.748224s.
  This promising screening signal did not survive independent confirmation.
- Confirmation pair1 C266990/29143, T245818/22007: DL-7.93%,UL-24.49%.
  Pair2 T235131/21670, C265162/ULfailed: DL-11.33%; UL ratio invalid. Accepted control
  again hit75.002329s timeout, HTTP0, curl size_upload131072, TTFB0; trace, DL,
  following small requests, bulk and recovery passed. Failure retained, no invented
  acknowledgement and no deletion as an outlier. Both candidates lost DL; this is
  not a six-pair result. H5 rejected, production source restored exactly to4ba57ad.
- Incomplete confirmation loaded p95 C0.704815/T0.773457s, max0.704952/0.796988s;
  bulk8/8 both. Candidate native lifetime CPU14.2-17.7% of one core, RSS9184-9648KiB,
  bg threads92-97. Control17.2-18.1%, RSS9176-9456KiB excluding timeout-window
  lifetime average4.4%/9492KiB; these are not interval CPU or battery measurements.

### Upload Phase Discrimination

- `python build/dns-multipath/upload-phase.py`: four fresh accepted-MP startups,
  each128KiB upload before, exact1MiB DL,128KiB upload after,25s diagnostic deadlines,
  full aggregate DNS/connect/TLS/TTFB/total timing. This is diagnosis, not replacement
  of the75s failed required transfer. All12 HTTP200, exact sizes, zero children after
  every stop; immutable upload-phase.jsonl. Before/after upload seconds:
  4.749907/4.783519,5.678950/5.961456,4.874935/5.353200,4.707378/11.496193.
  Last slow upload had TLS complete0.316448s, first response11.496077s. It is a
  post-handshake delay, not the FlowRelay10s destination-dial timeout. No deterministic
  post-DL failure demonstrated, and no endpoint/transport root cause established.
- The75s timeout with size_upload131072 is distinct from H4's rc35/TLS0 first-trace
  failure. Bytes accepted by curl's TLS write do not prove remote HTTP consumption.
  Working subsequent independent streams argue against a persistent whole-carrier
  outage, not against a per-stream loss/stall or external endpoint problem.
- Concurrent job1788953180972-43 completed608 aggregate samples/320s: max SYN-SENT1,
  ESTAB14, FIN-WAIT-1 14, recv0/send111046 bytes across FlowRelay sockets. Aggregates
  include other sessions and cannot locate the delayed stream. No server changes,
  no packet/payload logging. All observation jobs are terminal.
- Final owning-node gates: corrected server hash d7667b1e unchanged;
  slipstream/flowd active, NRestarts0/1 unchanged, stdout/stderr null,
  memory6270976/5832704 bytes. Retained APK and Kotlin/native source identities are
  unchanged from the already verified three-mode TCP/UDP acceptance above; the
  diagnostic runs do not replace or generalize that evidence. No delivered artifact
  directory edits. No performance candidate source retained; only this goal changed.

### Isolated DE Direct Laboratory

- Latest user instruction requests separate direct egress to isolate suspected WARP
  contribution, expressly prohibiting production WARP Slipstream reconfiguration.
  Two independent test Slipstream listeners used127.0.0.1:5303/5304, both domain
  direct-lab.invalid and a fresh pinned certificate. Separate authenticated FlowRelay
  instances used127.0.0.1:40003 bound to eth0 and10.200.0.2:40003 bound to
  CloudflareWARP. Production195.128.101.186:53 ->10.200.0.2:40001 was untouched.
- Four /run/systemd/system/owenclave-lab-{direct,warp,slip-direct,slip-warp}.service
  units were never enabled, had null output, DynamicUser, finite CPU/memory/tasks,
  RuntimeMaxSec1800 and TimeoutStopSec5. Direct FlowRelay additionally denied private
  destination ranges. Units/certificate were verified before starting only test units.
  Wrong pin exited1 with zero payload; wrong token failed before HTTPS payload.
  A host-reachable private SOCKS control was denied through authenticated test direct
  FlowRelay with zero reply bytes/RST. Initial EOF-only assertion failed; accepting
  EOF or RST corrected the harness, not backend policy.
- Native lab client SHA-256
  fa99d3810f653c42cd4bda8dc8e13b1c093294068bb589fc5d0444d045ee6de2,
  built offline in existing Debian12 build image from retained patched source, used
  the unchanged corrected server binary. Registry-mount omission initially failed
  offline compilation; supplying the existing read-only registry resolved it. /run
  is noexec, so execution used a private /opt test copy, subsequently removed.
- Ignored helper build/dns-multipath/egress-lab.py implements two loopback DNS
  adapters,28/28 workers and32/32 queues,150ms minimum exchange service or zero-delay
  control. Native credentials/token enter through stdin; child output is discarded
  except readiness. Only aggregate metrics are retained. This is native DE loopback,
  NOT TCP Yandex recursion, Android/gVisor or LTE. Upstream UDP source sockets,
  synthetic scheduling, destination routing/namespace and sample order remain
  confounds. No endpoint/payload captures or production traffic logs were enabled.
- Direct HTTPS proved DE/warp:off and the node's public IPv4; transaction-matched
  STUN20-byte requests/32-byte replies independently proved direct UDP. WARP windows
  that reached acceptance proved DE/warp:on and different TCP/UDP mapped addresses.
  The WARP UDP fingerprint was6f75822ac616; direct00a97914de95 (lab-specific hash).

Rates below are B/s. Failed/incomplete windows are preserved, not repaired or counted
as paired confirmation. Payload workload is1MiB DL, acknowledged128KiB UL, four
concurrent1MiB/18s requests, five loaded4KiB/20s requests and recovery.

| Window | Direct | WARP | Interpretation |
| --- | --- | --- | --- |
| pair-1,150ms | DL245753,UL27289; required pass | DL249491; UL75.003555s timeout after TLS0.334134s/curl131072 sent, HTTP0 | Previous post-handshake symptom reproduced without Android/Yandex |
| pair-2,reverse | Not run after failed first arm | DL257592; UL rc35/TLS0/HTTP0 in11.512862s | Incomplete pair; following requests recover |
| zero-1 | DL1303766,UL164053; pass | DL1112867,UL118722; pass | Single zero-delay pair, not a gain claim |
| fixed-3,payload IPv4 | DL258318,UL27144; pass | Preliminary hostname trace rc35/TLS0 in11.448580s | Fixed payload never reached on WARP; DNS not eliminated for trace |
| ipv4-4,all TCP fixed | Not run after failed first arm | Fixed-IP trace rc35/TLS0 in11.550563s | DNS/IPv6 not necessary for this lab startup symptom |
| single-warp-5 | Not run | One path56/64, DL242172,UL25812; all required pass | Single-path diagnostic, not LTE control |
| mp-warp-6 | Not run | DL257847,UL29452; loaded request20.002211s timeout after TLS0.471267s | MP failure also occurs after successful upload |

- Exact windows: ignored build/dns-multipath/direct-lab-{pair-1,pair-2,zero-1,
  fixed-3,ipv4-4,single-warp-5,mp-warp-6}.jsonl. pair-1 was copied without alteration
  from completed SSH spool1788954483073-68.log. Other files were transferred without
  overwrite. Original helper retained as egress-lab-original.py. Fixed variants use
  curl --connect-to speed.cloudflare.com:443:162.159.140.220:443, preserving TLS SNI;
  all-TCP variant uses the same host/IP for trace and payload. All transfer workers
  report zero socket errors; overflow drops still occur (pair-1 direct581 versus
  WARP2559). This does not isolate a queue cause or measure real resolver capacity.
  Pair-1 loaded inclusive p95 direct0.862919/WARP1.402852s, max0.878057/1.403766s;
  four concurrent bulk requests pass on each arm. The failed required upload still
  disqualifies that WARP window; these latency values do not rehabilitate it.
- Layer controls: direct interface-bound curl passed a27kB/s acknowledged upload;
  first WARP-bound curl to the same fixed IPv4 failed TLS before sending payload at
 25.001747s WITHOUT Slipstream/FlowRelay. Four subsequent fixed-IP uploads across
  two destination IPv4s passed, then balanced direct/WARP/WARP/direct rate-limited
  uploads all passed in4.856-4.858s. No persistent IP block or slow-upload cause proved.
- build/dns-multipath/flowd-lab.py bypassed Slipstream but retained authenticated
  private test FlowRelay, fixed destination IPv4 and verified HTTPS. WARP/direct/
  direct/WARP27kB/s uploads all acknowledged131072 bytes, TLS0.029-0.045s,
  total4.886-4.900s; exact direct-lab-flowd-only.jsonl. Therefore neither WARP alone,
  FlowRelay alone nor MP alone is established as sole cause. Synthetic MP failures
  warrant preserving the reproducer, not blindly changing worker/poll budgets.
- Resource observation was bounded: native RSS3352KiB/one thread, lifetime CPU1.4%
  of one core; Python RSS10384KiB/4.2%. These are lab snapshots, not phone interval CPU
  or battery results. Test units had NRestarts0 during all windows. Final test-WARP
  FlowRelay stop hit the5s stop timeout; systemd terminated it and retained a failed
  record. That failure is preserved here; only the deleted test unit's failed state
  was reset. All four test units are now inactive/not-found,5303/5304/40003 absent,
  lab key/cert/token/upload/client deleted. Only inert aggregate files/helpers remain
  under /run/owenclave-direct-lab until reboot. Unit-only snapshots and unchanged
  production preflight copies remain /root/backups/direct-lab-20260909.

### Superseded Ingress Inference

- Authoritative queries to aiden.ns.cloudflare.com prove t.x.ass-peak.de delegates
  to x.ass-peak.de ->195.128.101.186; x has no AAAA. Test direct.x.ass-peak.de has no
  NS/A and returns SOA. DE has one public IPv4, no host public IPv6, and public UDP53
  already belongs to production. No available separate delegation/zone API config
  was found in the inspected operational boundary.
- Server source resolves ONE target_address and shares it across every configured
  domain. Adding a domain cannot choose an isolated direct backend. A different
  client port cannot tell Yandex recursive DNS to use authoritative UDP5303; NS
  delegation does not encode a port. These observations do NOT establish a blocker:
  Slipstream transparently forwards OPEN to private FlowRelay, which can select a
  separately authenticated fixed backend after checking an explicit test token.
  Follow-up authorization permits this additive route without changing production
  token semantics, public carrier or certificate. The earlier categorical need for
  a second authority is withdrawn. A WARP trip to a DE HTTP endpoint alone would
  still not prove direct carrier isolation.
- Fresh local native fault suite passed all five retained scenarios with the exact
  corrected Debian server: reorder/duplicate, primary loss, dead bootstrap preserve
 131072 bytes each way/EOF; both dead and wrong pin fail closed with zero payload.
   At that earlier lab checkpoint, Kotlin/native matched4ba57ad and the phone was
   unchanged/stopped/zero children,100%/25C. This is historical lab evidence, not
   the final state of the subsequent phone campaign below.
- At that earlier lab checkpoint production binaries, units and nftables matched preflight;
  Slipstream PID3001224/NRestarts0 and FlowRelay PID698/NRestarts1 remain active with
  null sinks. nft --check passes, public UDP53 and private40001 remain; no test or
  retired40002/private41924 listener. Every lab/observation job is terminal.

### Actual Phone Direct Dispatch

- Source: bin/lib/slipstream/flowd/main.go and dispatch_test.go. Optional four-flag
  configuration is all-or-none and disabled by default. Private numeric backend,
  three distinct16-byte credentials, fixed test-interface binding, max16 test
  sessions within production128. Authenticated test OPEN only is forwarded after
  replacing its token with the separate backend credential. Unknown/backend tokens
  cannot select production handling; backend failure never falls back. Production
  resolver/TCP/UDP interface binding remains CloudflareWARP.
- Test backend10.200.0.1:40003 bound eth0, -deny-private rejects non-public resolved
  destinations including CGNAT, max32/64MiB/50%CPU/128tasks, null sinks, one-hour
  runtime bound. One static host rule accepted only warp-host source10.200.0.2 to
  destination10.200.0.1 TCP40003; existing namespace private routing was reused,
  with no namespace firewall or public listener change. No production token crossed
  the direct boundary. Tokens came from protected credential files and child stdin.
- go test -race -count=1 ./... passed after cap16, go vet ./... passed. Regression
  tests cover production/test/unknown/disabled auth, TCP/UDP header preservation,
  rewritten-backend authentication, exact bidirectional payload/half-close,
  unavailable/timed-out backend, cancellation, slow header, session cap and bounded
  max273-byte OPEN, private destination rejection and configuration validation.
  Config -check, systemd-analyze verify and nft --check preceded deployment.
- Backup /root/backups/flowd-test-dispatch-20260909T123023Z contains original
  flowd binary/unit/nftables; cap8 and unit-only cap16 snapshots are under
  /root/backups/flowd-test-cap-20260909T130503Z. No secret snapshots were retained.
  Runtime override selected a NEW /usr/local/bin/flowd-test-dispatch binary; original
  /usr/local/bin/flowd and all base units/nftables remained byte-identical.
- Deployment, cap correction and restoration each restarted Flowd AND Slipstream
  through Requires=flowd.service. First propagation was not anticipated and was
  disclosed; later two were explicit. Slipstream executable/pin/authority/target
  stayed fixed; WARP PID589/NRestarts0 and WARP routing never changed. Do not infer
  no restart from reset NRestarts counters. Immediate cap-change probe initially
  hit Type=simple startup ConnectionRefused; subsequent listener-gated proof passed.
- Actual phone same accepted APK, gVisor, same .88+.1 Yandex TCP53 carrier and
  certificate: production HTTPS warp:on/DE and STUN20/32; test HTTPS warp:off/DE,
  exact DE direct address and STUN fingerprint425a872168cd matched the server.
  Before/after deployment production proofs passed; WARP fingerprints rotated and
  were matched against fresh references, not the old hardcoded acceptance hash.
  An initial interface STUN assertion failed before successful fresh reference;
  an Android autofill password prompt blocked the first direct Start before any
  child/payload, then was dismissed without saving credentials. Both failures remain.
- Remote backend-down test rejected test payload while production HTTPS/STUN still
  used WARP. Authenticated test private destination and unknown token yielded
  EOF/reset with zero payload. TLS pinning is unchanged on the actual public carrier;
  wrong-pin and both-path-down native tests failed closed with zero payload.

Balanced route attribution, same fixed speed.cloudflare.com IPv4162.159.140.220,
HTTPS/SNI preserved, 56/64 and immutable route-pair{1,2}-{direct,warp}.jsonl:

| Pair/order | Direct DL/UL B/s | WARP DL/UL B/s | Required failure |
| --- | --- | --- | --- |
| 1 D/W | 286570 / 19542 | 223949 / 19352 | WARP loaded4KiB timeout20.000861s |
| 2 W/D | 269424 / 21580 | 255082 / 24125 | none |

Direct DL wins both (+27.96%,+5.63%), UL mixed (+0.98%,-10.55%); all bulk4/4 per
window. This is attribution, not a clean adoption sample or proof WARP alone causes
the failures. These route windows used test cap8, a later observed potential
resource confound. Same carrier removes delegation/pin confounds, but fixed-backend
hop, interface/namespace and temporal network variability remain. Forced DNS LTE
was used; Restricted LTE was not re-established.

### H6/H7 Direct Screens

Fresh cap16 CT/TC windows, all required payloads pass, bulk8/8 for each variant:

| Candidate | Pair | C DL/UL B/s | T DL/UL B/s |
| --- | --- | --- | --- |
| H6 reverse primary | 1 | 228081 / 21922 | 243758 / 21313 |
| H6 reverse primary | 2 | 268215 / 22871 | 246394 / 26991 |
| H7 data burst40 | 1 | 186412 / 16682 | 257433 / 20420 |
| H7 data burst40 | 2 | 254452 / 20817 | 237031 / 15088 |

- H6 paired median DL-0.63%, UL+7.61%, target DL rejected. Loaded inclusive p95
  C0.723578/T0.903890s, max0.727908/0.982273. Control recovery3.760475s retained.
  reverse.apk742459f8464bed76fbe189bc6167017ebd6a58479667331d1a70fd637914cfc2;
  same native/assets. Source parser/test edits removed. Immutable labels
  reverse-direct-screen16-{1,2}-{c,t}; earlier cap8 failure remains separate.
- H7 authoritative MP-only data-loop burst80->40, polls/pacing/receive burst/56/64
  unchanged. Source upper bound80 data plus80 polls exceeds120 adapter slots; prior
  counters proved drops but not packet class. This is distinct from rejected H3
  poll-budget cap. Target UL declared before build. UL gains+22.41%/-27.52%, paired
  median-2.56%; DL+15.63% is not permission to change target after seeing results.
  Loaded p95 C1.111158/T0.871351s, max1.326707/0.965083. No six-pair confirmation.
- H7 burst.apk511100c5b15a26093029b5c209bce51dea6e47eda6b047ad28e1d85bf2dc7644,
  native351df8567c3ea8af10eb2ef64b1ce3587a3ad42d848e8b0d257fd7cd239f8427;
  assets/other native equal control, excluding generated dexopt profiles. Android
  incremental NDK29/Rust1.97.1 build, release Kotlin/APK and focused JVM gates pass.
  Host candidate passed five real native reorder/duplicate/path-loss/dead-bootstrap/
  all-down/wrong-pin scenarios, exact131072 bytes each way plus EOF when live.
  Source patch preserved only as ignored build/dns-multipath/burst-source.patch;
  cache runtime edit/build-list entry removed and accepted native restored.
- Immutable burst-direct-screen-{1,2}-{c,t}.jsonl and corresponding helper retain
  APK identity per window. Native samples one thread, RSS9500-10480KiB, lifetime
  CPU13.7-15.4% of one core; bg93-95 threads in H7. These are snapshots, not interval
  CPU or battery benefits. Phone100%/26C USB, one child live/zero after every window.

### Dispatch Restoration

- Phone production token restored through masked UI, accepted c8f2c194 installed,
  native64cbc7 restored in jniLibs, manual MP selected, VPN stopped/zero children.
  Android/native source matches4ba57ad. Delivered APK unchanged. No direct test
  profile remains selected; local production/test credential copies were deleted.
- Override /run/systemd/system/flowd.service.d/test-dispatch.conf and test unit
  /run/systemd/system/owenclave-direct-test.service removed, daemon reloaded, original
  Flowd restored. Test unit inactive/not-found; private40003 and rule comment
  owenclave-direct-test absent. Test executable and both remote test credentials
  removed. Only inert helpers/access batch remain in /run/owenclave-test-dispatch;
  no automatic restoration, enabled test unit, listener or background sampler.
- Final original Flowd/Slipstream PIDs3078733/3078737, NRestarts0/0 stable through
  acceptance, both null sinks/no drop-ins. Original binary/unit/nftables hashes
  unchanged; nft --check passes, public UDP53/private40001 present, test5303/5304/
  40003 and retired40002/41924 absent. Final remote HTTPS/STUN production WARP pass.
  Test token rejected after restoration. First negative invocation accidentally used
  `testclosed` instead of `test closed`, producing unhandled reset; correct negative
  mode proved zero payload without a server or helper change.
- Final phone dispatch-final-multipath and dispatch-final-single: HTTPS WARP/DE,
  STUN20/32 fingerprint41c736ee658d matches fresh pre-window server reference;
  exact1MiB DL, acknowledged128KiB UL and recovery pass, zero child after Stop.
- dispatch-final-automatic: HTTPS WARP/DE, same matched STUN, exact1MiB DL pass,
  upload FAIL rc35/HTTP0/sent0 at10.667784s, recovery4096 passes. Original APK and
  original server, no test dispatch, so neither MP nor dispatch is necessary for
  this symptom. Failed window retained, no blind retry or all-green claim. A later
  server STUN rotated to a367af216dcd; it does not invalidate the earlier matched
  before-window reference. Automatic upload acceptance remains unresolved.

## Android Native Smoke

- Implemented `bin/lib/slipstream/smoke/{device,host,pair,report,fixture,test_smoke}.py`
  and its README. Native executes in Termux on Moto g54, not the host: accepted ELF
  arm64 hash64cbc7, Python3.14.6, Wi-Fi0/LTE,LTE, battery100%/29-30C. Signed Termux
  apt installation needed an explicit PATH; initial missing-GPG failure did not
  justify unsigned packages. No root or SELinux change. Device direct baseline
  reached ya.ru302, cloudflare.com301, google.com301, speed.cloudflare.com200: this
  proves LTE performance, NOT restricted-LTE/allowlist traversal.
- Two bounded28-worker/32-queue serial persistent TCP adapters match budgets but
  not Kotlin scheduling: Python whole-exchange timeout and strict DNS ID/QR checks
  differ from Kotlin per-I/O timeouts/no ID/QR check. Same accepted native and Python
  adapter control every candidate. Source data-send80 then poll up-to40 per path are
  upper bounds, not unconditional160 sends; readiness/pacing/cwin limit actual work.
  Aggregate adapter capacity120 does not prove instantaneous overflow.
- Workload: fresh incompressible8/32/128/512KiB HTTPS uploads sequentially and each
  under one replenished1MiB download; exactsize/SHA256 acknowledgement, sequential
  1MiB DL,4KiB recovery. Deadline40s, failures zeroackBps, phases/cumulative TLS/startup/
  completion/local drain stalls retained. Local submitted bytes are NOT remote ACKs.
  Native logs discarded, credentials stdin only, results aggregate JSONL exclusive-create.
- Preserved initial harness failures calibration-native-{1,2,3}: VPN callback mistaken
  for active VPN, ADB stdin EOF/copy timeout, generated-copy syntax/exec-out exit status.
  calibration-native-4 passed all8 uploads; startup0.99975s, sequential completion
  1.466/1.892/5.508/17.309s, loaded1.145/2.493/6.827/23.218s, DL259462Bps.
  These are fixturev1. Fixturev2 adds source identity/version; all H8 confirmation
  windows share script7d75bfc4 and fixture-cert36a844f4, unchanged workloads.
- H6 two fresh balanced screens used accepted ELF, reversed order only. Median
  within-pair upload ratios+2.12%/+0.65%, DL-14.82%/-14.56%. Sequential8KiB
  -28.65%/-19.08%,32KiB-5.37%/-14.05%; loaded512KiB+3.84%/+40.86%. All32 uploads
  passed. Rejected for repeat short sequential/DL regressions, NOT the old10% rule.
- H7 extracted ELF351df856 from existing burst.apk, no APK build/install. Data-send
  max80->40 only, polling unchanged. Two screen UL medians+20.44%/+1.39%, DL+34.46%/
  +4.46%; loaded512KiB-43.52%/-24.43%, completion37.763/33.174s. All uploads passed,
  but repeat loaded-large regression rejected. Not an unconditional throughput win.
- H8 compromise max80->60 for multiple Authoritative resolvers only, polling unchanged.
  Isolated `build/dns-smoke/h8-src` copied pinnedbc772dd/picoquic4bd356c plus existing
  safety patches; no cache reset or accepted jniLibs/source edit. Android ELF build18s,
  `h8-native` SHA2560ad7668745c7d903a84c59173403c18be99ab750f999f82196a8d0615b728eb1;
  `h8-source.patch` preserves the exact minimal change. Source fmt and24 native
  client tests pass. Host test build initially failed on Perl paths/version/vendor
  modules, resolved using retained bbr3/perl-root; no source workaround. Strict
  workspace/all-target Clippy fails existing collapsible_if at runtime.rs656 (control
  corresponding line648), not H8 burst code; no suppression or clean-lint claim.
- H8 screen UL medians+2.29%/+6.10%, DL-6.18%/+11.54%; loaded32KiB+24.96%/+23.46%
  justified six fresh confirmation pairs despite mixed other sizes, not adoption.
  `h8-confirm-p{1..6}-{control,candidate}.jsonl` are immutable. Report command:
  `python3 bin/lib/slipstream/smoke/report.py --directory build/dns-smoke --label h8-confirm --pairs 6`.

| H8 confirmation workload | Paired median | Wins | Bootstrap95 median interval |
| --- | ---: | ---: | ---: |
| Sequential8KiB upload | -13.44% | 1/6 | -25.74% to+20.13% |
| Sequential32KiB upload | -19.04% | 0/6 | -25.13% to-4.87% |
| Sequential128KiB upload | +0.72% | 3/6 | -9.11% to+25.41% |
| Sequential512KiB upload | +0.97% | 3/6 | -11.07% to+16.20% |
| Loaded8KiB upload | +2.15% | 3/6 | -4.64% to+12.96% |
| Loaded32KiB upload | +7.35% | 4/6 | -11.94% to+26.28% |
| Loaded128KiB upload | +2.32% | 4/6 | -8.19% to+28.39% |
| Loaded512KiB upload | -3.32% | 2/5 defined | -100% to+2.74% |
| Sequential1MiB download | +5.38% | 4/6 | -8.97% to+23.16% |

- Both variants failed1/48 uploads: control pair1 loaded512KiB40.00096s and candidate
  pair6 loaded512KiB40.00156s, phaseack/submitted524288/zeroack. Undefined ratio for
  failed control retained separately, candidate failure contributes-100%. Startup
  C/T p50 .769/.860s, p95=max2.298/1.686s; TLS phase p95 .856/.980s, max1.413/1.506s.
  Sequential32KiB completion C/T p50 1.988/2.362s, p95=max2.221/2.722s. Loaded32KiB
  p50 2.842/2.817s, p95=max3.633/3.433s; paired ratios and ratio-of-medians differ.
  Six pairs give descriptive uncertainty, not a carrier-wide guarantee. H8 rejected:
  its modest loaded gain is eligible in principle, but cannot outweigh repeat short
  sequential regression and unresolved loaded timeout. No candidate APK promotion
  or build was justified. Current best preserved; no arbitrary attempt cap invoked.
- Calibration on actual accepted APK/gVisor: first direct window blocked by harness
  parser (CELLULAR|VPN not exact VPN), after TCP trace/UDP already passed. Preserved
  `calibration-gvisor-direct-1.jsonl`; fixed/tested active-agent transport parsing,
  not a tunnel failure. Fresh direct-{2,3} all16 uploads/2DL/2recovery pass. Sequential
  8/32/128/512KiB completion1.040/1.883/4.409/14.499s and1.142/1.788/4.561/14.928s;
  DL281311/277311Bps. Larger sequential uploads faster than Python controls in this
  session; these nonrandomized calibrations prove the harness is NOT APK-equivalent.
- WARP calibration gVisor1 failed sequential512KiB: TLS .368s, submitted .384s,
  no ACK, IncompleteReadError24.01165s; all other uploads/DL/recovery passed. Native
  WARP1 failed loaded512KiB40.00226s. After restoring ORIGINAL Flowd, native WARP2
  first8KiB failed in TLS10.27961s/submitted0; subsequent32/128/512KiB sequential
  uploads passed, loaded512KiB timed out40.00052s. Thus neither gVisor nor optional
  test dispatch is necessary for all observed failure modes; WARP-only causation
  and Telegram fixes are NOT proven. No Telegram/account content sent.
- Final restored-server gVisor MP2 all8 uploads/DL/recovery pass: sequential
  1.172/2.215/5.676/16.282s, loaded1.010/2.634/6.609/19.602s, DL211074Bps. Fresh
  Automatic and single(.8 TCP) each all8 uploads/DL/recovery pass, DL254618/251225Bps.
  All three modes independently passed HTTPS WARP/DE and STUN20->32 matching
  fresh server fingerprint609b85ac032a. Earlier original Automatic TLS10.667784s
  failure remains valid history; last successful gates do not establish a fix.
- Eleven Python tests pass: fragmented persistent DNS/affinity, malformed/truncated/
  wrong-ID responses, drop-new bounds, timeout/path recovery, cancellation/reaping
  one real helper child and56 workers, exacthash TLS fixture, wrong auth/no leaked
  token, malformed/slow headers, VPN agent parsing, report failure handling.
  Real phone TERM while active also passed: host-2, no owner.pid, zero children/VPN
  off, recorded cancel-term-1.jsonl. EOF cancels before TERM fallback, avoiding a
  double cancellation during cleanup. That lifecycle-only script revision has hash
  bd365194 and was used for WARP checks; not mixed into H8 confirmation statistics.
- Server campaign backup `/root/backups/owenclave-smoke-20260909T135858Z/`. Two planned
  shared restart boundaries, no WARP restart. Temporary direct cap16/backend32;
  isolated backend runtime extended1h->4h with only its own restart, no shared PID
  change. Fixture DynamicUser/nullsinks/64MiB/CPU50%/Tasks16, source-restricted40004
  and independent32-byte auth, never a proxy. WARP destination-dependent IPs needed
  a test-only104.28/16 ingress allowance plus auth, not any production route change.
- Original Flowd SHAeb07424f and corrected Slipstream SHAd7667b1 restored/unchanged;
  base units/nftables byte-identical backup. Final PIDs3099337/3099339, NRestarts0/0,
  null sinks/no drop-ins; WARP589 unchanged. Unit verify/nft-check pass, publicUDP53
  and privatewarpns40001 present;40003/40004/40002/41924private/9100 absent, host route
  direct. One remote original-flowd1.1.1.1 TLS reset retained; interface-bound WARP
  trace and alternate1.0.0.1 authenticated FlowRelay TCP/UDP passed. Retired test and
  unknown tokens returned zero payload. Test units/binary/rules, both /run test dirs,
  private keys and local/remote credential copies removed (a harmless generated
  __pycache__ initially prevented rmdir; inspected and removed). No timers/auto-redeploy.
- Phone remains accepted c8f2c194, native64cbc7, production-token manualMP, stopped
  with zero children. Delivery directory not accessed; no APK built/installed this
  campaign. Native H7/H8 ELF experiments and aggregate JSONLs remain ignored artifacts.

## Completion

R1 verified; this native-smoke implementation/screening/calibration campaign is
closed with evidence-backed H6/H7/H8 rejections and R3 restoration verified. The
broader R2 optimization objective remains active, not a global optimum or a Telegram
fix. Future carrier changes use this native screen before any APK build; only a
credible non-regressing signal advances to actual gVisor candidate confirmation.
Known TLS/ack failures are now measured separately from bulk speed and must not be
erased by successful later windows. All test runtime/credentials removed, production
unchanged, no push or delivered-artifact change. Appropriate tracked harness/docs
are the only retained implementation changes.
