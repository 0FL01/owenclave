#!/usr/bin/env bash
set -euo pipefail

COMMIT="bc772dd07d9a136dbd7553b0da575526de207847"
RUST_VERSION="1.97.1"
CARGO_NDK_VERSION="4.1.2"
NDK_VERSION="29.0.14206865"
ROOT="$(cd "$(dirname "$0")/../../.." && pwd)"
PATCHES=(
  "$ROOT/bin/lib/slipstream/adaptive-idle.patch"
  "$ROOT/bin/lib/slipstream/flow-relay.patch"
)
CACHE="${XDG_CACHE_HOME:-$HOME/.cache}/owenclave-slipstream"
SRC="${SLIPSTREAM_SRC:-$CACHE/src}"
BUILD="${SLIPSTREAM_BUILD:-$CACHE/build}"
OUT="$ROOT/app/src/main/jniLibs/arm64-v8a/libslipstream.so"
TC="${ANDROID_NDK_HOME:?ANDROID_NDK_HOME is required}/toolchains/llvm/prebuilt/linux-x86_64/bin"

command -v git >/dev/null
command -v cargo >/dev/null
command -v cmake >/dev/null
command -v perl >/dev/null
rustc --version | grep -q "^rustc $RUST_VERSION "
cargo ndk --version | grep -q "cargo-ndk $CARGO_NDK_VERSION"
grep -q "Pkg.Revision = $NDK_VERSION" "$ANDROID_NDK_HOME/source.properties"
perl -MFindBin -MFile::Compare -e 1

mkdir -p "$CACHE" "$BUILD" "$(dirname "$OUT")"
if [ ! -d "$SRC/.git" ]; then
  rm -rf "$SRC"
  git clone --filter=blob:none --no-checkout https://github.com/Mygod/slipstream-rust.git "$SRC"
fi
if ! git -C "$SRC" cat-file -e "$COMMIT^{commit}"; then
  git -C "$SRC" fetch --depth 1 origin "$COMMIT"
fi
git -C "$SRC" checkout --detach --force "$COMMIT"
git -C "$SRC" reset --hard "$COMMIT"
rm -f "$SRC/crates/slipstream-client/src/flow_relay.rs"
git -C "$SRC" submodule update --init --recursive --depth 1 vendor/picoquic
for patch in "${PATCHES[@]}"; do
  git -C "$SRC" apply --check "$patch"
  git -C "$SRC" apply "$patch"
done

export ANDROID_ABI=arm64-v8a
export ANDROID_PLATFORM=android-21
export CARGO_TARGET_DIR="$BUILD/target"
export PICOQUIC_BUILD_DIR="$BUILD/picoquic"
export PKG_CONFIG_LIBDIR="$BUILD/pkgconfig"
export RUST_ANDROID_GRADLE_CC="$TC/aarch64-linux-android21-clang"
export RUST_ANDROID_GRADLE_AR="$TC/llvm-ar"
rm -rf "$PICOQUIC_BUILD_DIR"
mkdir -p "$PKG_CONFIG_LIBDIR"

(cd "$SRC" && cargo ndk -t arm64-v8a --platform 21 build \
  --release --locked -p slipstream-client \
  --features openssl-vendored,picoquic-minimal-build)
install -m 0755 "$CARGO_TARGET_DIR/aarch64-linux-android/release/slipstream-client" "$OUT"
sha256sum "$OUT"
