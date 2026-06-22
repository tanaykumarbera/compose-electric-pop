#!/usr/bin/env bash
# Claude Code PostToolUse hook: run the design-rule gate on a file an agent just
# edited/wrote, so violations surface in-session instead of at PR/CI time.
#
# Wired in .claude/settings.json on the Edit|Write matcher. Receives the hook event
# as JSON on stdin (see https://docs.claude.com/en/docs/claude-code/hooks).
# Exit 2 → stderr is fed back to the model as actionable feedback.
set -euo pipefail

root="${CLAUDE_PROJECT_DIR:-$(git rev-parse --show-toplevel 2>/dev/null || pwd)}"
gate="$root/scripts/check-design-rules.sh"
[[ -x "$gate" ]] || exit 0

payload="$(cat)"
file_path="$(printf '%s' "$payload" | jq -r '.tool_input.file_path // empty' 2>/dev/null || true)"
[[ -n "$file_path" ]] || exit 0
[[ "$file_path" == *.kt ]] || exit 0

# Only care about library sources (the gate self-scopes, but skip the subprocess otherwise).
case "$file_path" in
  *"/library/src/"*) ;;
  *) exit 0 ;;
esac

if ! out="$("$gate" "$file_path" 2>&1)"; then
  echo "Design-rule violation in the file you just edited:" >&2
  echo "$out" >&2
  exit 2
fi
exit 0
