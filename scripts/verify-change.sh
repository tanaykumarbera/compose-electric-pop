#!/usr/bin/env bash
# Canonical local verification for Electric Pop changes — the one command to run
# before opening a PR. Mirrors the CI gate order so a green run here means CI is
# very likely green too. Fails fast on the first failing gate.
#
#   scripts/verify-change.sh
#
# Covers: Detekt + Spotless, the design-rule gate, screenshot-KDoc drift,
# component snapshot presence, demo catalog registration, desktop unit tests,
# and Roborazzi screenshot verification.
set -euo pipefail

root="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$root"

step() { echo ""; echo "──▶ $1"; }

step "Detekt + Spotless"
./gradlew detekt spotlessCheck --console=plain

step "Design-rule gate"
./scripts/check-design-rules.sh

step "Screenshot KDoc tables in sync"
./gradlew :library:syncScreenshotKdoc --console=plain
if ! git diff --quiet -- library/src/commonMain; then
  echo "✗ Screenshot KDoc tables are stale. Stage the regenerated KDoc:" >&2
  echo "    git add library/src/commonMain" >&2
  exit 1
fi

step "Agent Skill manifest in sync"
./gradlew :library:generateSkillManifest --console=plain
if ! git diff --quiet -- skills/electric-pop/reference/components.md; then
  echo "✗ Agent Skill component cheat-sheet is stale. Stage the regenerated manifest:" >&2
  echo "    git add skills/electric-pop/reference/components.md" >&2
  exit 1
fi

step "Snapshot presence + catalog registration"
./gradlew :library:checkScreenshotPresence :demo:checkCatalogRegistration --console=plain

step "Desktop unit tests"
./gradlew :library:desktopTest --console=plain

step "Verify Roborazzi screenshots"
./gradlew :library:verifyRoborazziDesktop --console=plain

echo ""
echo "✓ verify-change: all gates passed."
