#!/usr/bin/env bash
set -euo pipefail

VERSION="v2.2.0"
APK_SHA256="dd123e71c24cbe4968e910bbe70bf42fb9f0ea8459e198bff93a1e685ddfdc46"
CLIENT_SHA256="a68762441b79abda89ef1c68437e93f1eb706c52960a9bb12cedec8a1cb067ac"
CACHE="${XDG_CACHE_HOME:-$HOME/.cache}/owenclave-slipstream"
APK="$CACHE/DNSTT-Client-$VERSION-Android-arm64-v8a.apk"
OUT="$(cd "$(dirname "$0")/../../.." && pwd)/app/src/main/jniLibs/arm64-v8a/libslipstream.so"

mkdir -p "$CACHE" "$(dirname "$OUT")"
if [ ! -f "$APK" ]; then
  curl -fL "https://github.com/dnstt-xyz/dnstt_xyz_app/releases/download/$VERSION/DNSTT-Client-$VERSION-Android-arm64-v8a.apk" -o "$APK"
fi
echo "$APK_SHA256  $APK" | sha256sum --check --status
unzip -p "$APK" lib/arm64-v8a/libslipstream_client.so > "$OUT"
echo "$CLIENT_SHA256  $OUT" | sha256sum --check --status
chmod 0755 "$OUT"
