#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "$0")" && pwd)"

cp "$ROOT/settings.gradle.kts" settings.gradle.kts
cp "$ROOT/gradle.properties" gradle.properties
cp "$ROOT/app/build.gradle.kts" app/build.gradle.kts
cp "$ROOT/app/src/main/AndroidManifest.xml" app/src/main/AndroidManifest.xml
cp "$ROOT/.github/workflows/android-ci.yml" .github/workflows/android-ci.yml
cp "$ROOT/README.md" README.md

rm -f app/src/main/java/ai/lust/MainActivity.kt

echo "Fixed files applied."
echo "Now run:"
echo "  git status"
echo "  ./gradlew :app:assembleDebug"
