# Goal: Close DNS Tunnel Wave 3 lifecycle defects

Status: active
Source: user-approved 2026-08-12 Wave 3 audit slice
Last updated: 2026-08-12

## Objective

Reproduce each Wave 3 finding under controlled fault injection, fix only confirmed
defects, and leave DNS Tunnel fail-closed with bounded process and connection ownership.

## Execution Directive

Complete the frozen Required Outcomes using the listed Change Envelope and Primary
Evidence. Work on the smallest unresolved outcome. Do not add requirements from
reviews, tests, tools, speculative risks or optional source text. Finish when every
required outcome is resolved and affected constraints remain satisfied. Reproduce,
fix, build, validate, commit and push each confirmed defect before starting the next.

## Frozen Contract

### Required Outcomes

- R1: A replacement Slipstream child must become ready or stop the stale Connected
  service state.
  - Source: user-approved W3.1.
  - Acceptance: after the ready child exits against a controlled unavailable resolver,
    the service cannot remain Connected indefinitely with a live unready replacement.
  - Primary evidence: disposable TCP resolver reproduction, then bounded service state,
    child count and payload observation on the fixed build.
  - Status: verified
  - Evidence: Android 15 connected through a disposable localhost TCP DNS forwarder.
    On the baseline, killing the ready child and removing the forwarder produced a new
    live child while the service stayed Connected beyond the normal readiness bound.
    The fixed build invokes a full service restart: the replacement uses the existing
    15-second startup readiness gate and VPN/child reached zero when it stayed unready.

- R2: Failed captured gVisor TCP dials release their connection state immediately.
  - Source: user-approved W3.2.
  - Acceptance: bounded failed dials do not retain the incoming connection, context or
    connection-list entry until VPN shutdown.
  - Primary evidence: controlled failing dial reproduction with before/after resource
    observation or a focused deterministic core test.
  - Status: pending
  - Evidence:

- R3: A final stop request wins over an already queued restart.
  - Source: user-approved W3.3.
  - Acceptance: user/fatal stop received during restart teardown leaves the service
    Stopped and does not start another VPN session.
  - Primary evidence: controlled teardown barrier reproducing the crossed requests,
    then the same ordering on the fixed build.
  - Status: pending
  - Evidence:

### Constraints

- C1: Preserve gVisor-only VPN mode, one child/resolver, authenticated carrier and
  fail-closed routing.
- C2: Do not disrupt the public DNS carrier or change router/server configuration,
  credentials, carrier protocol or olcRTC.
- C3: Do not print or commit tokens, complete profiles, databases, process arguments,
  keys or traffic logs.
- C4: Fault-injection hooks are temporary evidence only and must not remain in runtime
  artifacts or commits.
- C5: Commit and push each confirmed defect before editing the next one.

### Non-goals

- Reopening W2.3 or fixing additional audit findings.
- General lifecycle redesign, health polling or retry framework.
- Supporting a direct fallback after carrier failure.

## Change Envelope

- Target: post-readiness Slipstream ownership, gVisor TCP failure cleanup and service
  restart/stop intent ordering.
- Expected paths and direct consumers:
  - `app/src/main/java/io/nekohasekai/sagernet/bg/proto/SlipstreamInstance.kt`;
  - `app/src/main/java/io/nekohasekai/sagernet/bg/BaseService.kt`;
  - `library/core/tun.go` and focused tests if practical;
  - temporary controlled fault-injection code, removed before commit;
  - `docs/dns-tunnel.md`, the audit document and this goal.
- Allowed artifacts: minimal Kotlin/Go/docs changes and release APK build outputs.
- Forbidden artifacts: dependencies, migrations, services, persistent state, protocol
  or server changes, secret-bearing diagnostics and unrelated fixes.
- User budget: one outcome per checkpoint, focused E2E, separate commit and push.

## Current Checkpoint

- Closes: R1.
- Smallest next action: build and validate the non-debug release, commit/push R1, then
  begin R2 from the clean checkpoint.
- Expected evidence: signed release retains ordinary one-child DNS payload and the
  fault-injection build leaves no stale Connected state.
- Stop or replan if: the production build changes the tested lifecycle behavior.

## Current State

- Resolved: R1. Post-readiness child exit now reuses full service startup readiness
  instead of launching an unobserved replacement.
- Last relevant evidence: unavailable disposable resolver ended with zero VPN/child
  after the bounded replacement attempt; the public carrier was not changed.
- Blocker: none.
- Next: final R1 release validation and separate commit/push.

## Material Decisions

- 2026-08-12: execute W3.1, W3.2 and W3.3 sequentially; only reproduced defects receive
  runtime fixes.
- 2026-08-12: use disposable local forwarding and temporary barriers rather than alter
  live router/server behavior.

## Checkpoint History

- 2026-08-12: contract frozen; R1 is current.
- 2026-08-12: R1 reproduced and fixed by routing post-readiness exit through the
  existing full-service readiness and failure path.

## Completion

- Resolved outcomes:
- Commands and artifacts:
- Constraint and diff-scope check:
- Final status:
