# Goal: increase accepted multipath DNS throughput

Status: blocked
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
   direction declared per hypothesis before screen. Status: blocked for the latest
   requested isolated direct/LTE comparison; missing independent recursive ingress
   is documented below. Lab evidence does not satisfy the phone adoption gate.
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
- Latest user continuation authorizes a SEPARATE DE direct test path, not changing
  production Slipstream/WARP. Minimal new envelope: private loopback lab Slipstream
  and separate FlowRelay direct/WARP test instances, distinct lab token and pinned
  certificate, bounded resources/null output, removed from runtime after tests.
  No production domain dispatch, public listener, firewall or profile change.
  Public-recursive phone comparisons require independently delegated/reachable
  authority; local delayed-path measurements cannot substitute for LTE adoption.
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

Direct/WARP native laboratory and layer-isolation experiments are executed and torn
down. R2 is blocked on a separately delegated/reachable UDP53 authority for the
direct phone path. Smallest unlock: an independently reachable authority/address
and delegation, without replacing production ingress or its single backend.
Then prove explicit test-profile TCP/UDP direct egress before balanced phone tests.
No new candidate is retained; do not reinterpret synthetic results as LTE adoption.

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

### Direct Phone Blocker

- Authoritative queries to aiden.ns.cloudflare.com prove t.x.ass-peak.de delegates
  to x.ass-peak.de ->195.128.101.186; x has no AAAA. Test direct.x.ass-peak.de has no
  NS/A and returns SOA. DE has one public IPv4, no host public IPv6, and public UDP53
  already belongs to production. No available separate delegation/zone API config
  was found in the inspected operational boundary.
- Server source resolves ONE target_address and shares it across every configured
  domain. Adding a domain cannot choose an isolated direct backend. A different
  client port cannot tell Yandex recursive DNS to use authoritative UDP5303; NS
  delegation does not encode a port. Replacing production ingress/dispatch is outside
  the explicit boundary. A WARP trip to a DE HTTP endpoint is not direct carrier
  isolation. Thus the authorized lab work is complete, but no available separate
  recursive ingress permits the required direct phone comparison. Unlock R2 with a
  separate reachable authority/address and delegation, not production reconfiguration.
- Fresh local native fault suite passed all five retained scenarios with the exact
  corrected Debian server: reorder/duplicate, primary loss, dead bootstrap preserve
 131072 bytes each way/EOF; both dead and wrong pin fail closed with zero payload.
  Production Kotlin/native source still exactly4ba57ad, installed APK c8f2c194,
  phone unchanged/stopped/zero children,100%/25C at final check. No app build/install
  or delivered artifact edits. Prior three-mode TCP/UDP acceptance remains the
  applicable unchanged-runtime evidence, not a newly executed phone comparison.
- Final production binaries, unit files and nftables are byte-identical to preflight;
  Slipstream PID3001224/NRestarts0 and FlowRelay PID698/NRestarts1 remain active with
  null sinks. nft --check passes, public UDP53 and private40001 remain; no test or
  retired40002/private41924 listener. Every lab/observation job is terminal.

## Completion

R1 verified; R3 retained-runtime safety verified. R2/objective blocked, not complete:
five prior hypotheses rejected, separate direct/WARP lab and layer controls executed,
no further gain adopted. The missing independent recursive authority blocks requested
direct/LTE adoption tests. Accepted c8f2c194 APK/manual MP remains stopped; production
unchanged, no active test runtime or background jobs. Only evidence and the affected
node boundary are committed; no push or performance source change.
