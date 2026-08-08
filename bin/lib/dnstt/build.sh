#!/usr/bin/env bash
set -euo pipefail

DNSTT_COMMIT="${DNSTT_COMMIT:-0c5c52a57d89}"
DNSTT_REF="${DNSTT_REF:-v1.20260501.0}"
DNSTT_SRC="${DNSTT_SRC:-${XDG_CACHE_HOME:-$HOME/.cache}/owenclave-dnstt}"
OUT_ROOT="${OUT_ROOT:-$(cd "$(dirname "$0")/../../.." && pwd)/app/src/main/jniLibs}"

if [ ! -d "$DNSTT_SRC/.git" ]; then
  rm -rf "$DNSTT_SRC"
  git clone https://www.bamsoftware.com/git/dnstt.git "$DNSTT_SRC"
fi
git -C "$DNSTT_SRC" fetch --tags origin
git -C "$DNSTT_SRC" checkout --detach "$DNSTT_REF"
case "$(git -C "$DNSTT_SRC" rev-parse HEAD)" in
  "$DNSTT_COMMIT"*) ;;
  *) echo "unexpected dnstt commit" >&2; exit 1 ;;
esac

build() {
  local abi="$1" goarch="$2" goarm="${3:-}"
  local out="$OUT_ROOT/$abi/libdnstt.so"
  mkdir -p "$(dirname "$out")"
  (
    cd "$DNSTT_SRC"
    # A static Linux ELF runs directly on Android and needs no NDK libc.
    export CGO_ENABLED=0 GOOS=linux GOARCH="$goarch"
    if [ -n "$goarm" ]; then export GOARM="$goarm"; fi
    go build -trimpath -ldflags "-s -w" -o "$out" ./dnstt-client
  )
  chmod 0755 "$out"
}

build arm64-v8a arm64
build armeabi-v7a arm 7
build x86_64 amd64
build x86 386
