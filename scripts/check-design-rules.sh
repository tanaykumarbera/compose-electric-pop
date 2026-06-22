#!/usr/bin/env bash
# Design-rule gate for Electric Pop.
#
# Enforces the machine-checkable subset of the CLAUDE.md / DESIGN.md "MUST" rules so
# violations fail fast and deterministically instead of relying on reviewer attention:
#
#   1. No hardcoded hex colors  — `Color(0x...)` in component code. Use MaterialTheme.colorScheme.
#   2. No inline TextStyle()     — construct nothing; read MaterialTheme.typography (.copy() is fine).
#   3. No material-icons imports — the library ships PopIcons; material-icons-core is banned.
#
# Scope:
#   - Rules 1 & 2 apply to LIBRARY MAIN sources only (commonMain/androidMain/desktopMain/iosMain),
#     excluding the theme definition files. Demos legitimately use hex colors as sample data, and
#     tests are out of scope.
#   - Rule 3 (material-icons import) applies repo-wide across library/ and demo/.
#
# Usage:
#   scripts/check-design-rules.sh                 # scan all in-scope sources (CI mode)
#   scripts/check-design-rules.sh <file> [file…]  # scan only the given files (hook mode)
#
# Exit 0 = clean, 1 = violations found.
set -euo pipefail

repo_root="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$repo_root"

lib_main_roots=(
  "library/src/commonMain/kotlin/co/tanay/electricpop"
  "library/src/androidMain/kotlin/co/tanay/electricpop"
  "library/src/desktopMain/kotlin/co/tanay/electricpop"
  "library/src/iosMain/kotlin/co/tanay/electricpop"
)
icon_roots=("library/src" "demo/src")

# Collect candidate files.
declare -a files
if [[ $# -gt 0 ]]; then
  for f in "$@"; do
    [[ "$f" == *.kt ]] && files+=("$f")
  done
else
  while IFS= read -r f; do files+=("$f"); done < <(
    find "${lib_main_roots[@]}" "${icon_roots[@]}" -name '*.kt' 2>/dev/null | sort -u
  )
fi
[[ ${#files[@]} -eq 0 ]] && exit 0

found=0

# Blank out comment lines (KDoc/line/block-open) while preserving line numbers, so doc
# examples don't trip the greps.
strip_comments() {
  awk '
    /^[[:space:]]*\*/   { print ""; next }
    /^[[:space:]]*\/\// { print ""; next }
    /^[[:space:]]*\/\*/ { print ""; next }
    { print }
  ' "$1"
}

is_lib_main() {
  case "$1" in
    library/src/commonMain/kotlin/co/tanay/electricpop/*|\
    library/src/androidMain/kotlin/co/tanay/electricpop/*|\
    library/src/desktopMain/kotlin/co/tanay/electricpop/*|\
    library/src/iosMain/kotlin/co/tanay/electricpop/*) return 0 ;;
    *) return 1 ;;
  esac
}

emit() { # $1 rel path, $2 rule label, $3 matches block. Sets `found` in the caller's shell.
  echo "  ✗ $1 — $2" >&2
  printf '%s\n' "$3" | sed 's/^/      /' >&2
  found=1
}

for f in "${files[@]}"; do
  [[ -f "$f" ]] || continue
  rel="${f#"$repo_root"/}"
  rel="${rel#./}"

  # Rule 3: material-icons imports (repo-wide). Imports aren't comments → scan raw.
  m="$(grep -nE '^[[:space:]]*import[[:space:]].*material[._]icons' "$f" || true)"
  [[ -n "$m" ]] && emit "$rel" "material-icons import banned (use PopIcons)" "$m"

  # Rules 1 & 2 are library-main only, theme files exempt.
  is_lib_main "$rel" || continue
  stripped="$(strip_comments "$f")"

  if [[ "$rel" != */theme/Color.kt ]]; then
    m="$(printf '%s\n' "$stripped" | grep -nE 'Color\(0x[0-9A-Fa-f]' || true)"
    [[ -n "$m" ]] && emit "$rel" "hardcoded hex color (use MaterialTheme.colorScheme)" "$m"
  fi

  if [[ "$rel" != */theme/Typography.kt ]]; then
    m="$(printf '%s\n' "$stripped" | grep -nE '(^|[^A-Za-z])TextStyle\(' || true)"
    [[ -n "$m" ]] && emit "$rel" "inline TextStyle() (read MaterialTheme.typography)" "$m"
  fi
done

if [[ "$found" -ne 0 ]]; then
  echo "" >&2
  echo "Design-rule gate failed. See CLAUDE.md coding rules / DESIGN.md." >&2
  exit 1
fi
echo "Design-rule gate: clean (${#files[@]} file(s) scanned)."
