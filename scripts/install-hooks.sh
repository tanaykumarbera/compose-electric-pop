#!/usr/bin/env bash
# Install repo git hooks. Run once per clone.
#
#   ./scripts/install-hooks.sh
#
# Currently installs:
#   pre-commit — runs `:library:syncScreenshotKdoc` and aborts the commit
#                if the task produces a diff (means snapshots and KDoc tables
#                are out of sync; rerun the task and `git add` the result).

set -euo pipefail

repo_root="$(git rev-parse --show-toplevel)"
hooks_dir="$repo_root/.git/hooks"
mkdir -p "$hooks_dir"

cat > "$hooks_dir/pre-commit" <<'HOOK'
#!/usr/bin/env bash
# Auto-installed by scripts/install-hooks.sh
set -euo pipefail

repo_root="$(git rev-parse --show-toplevel)"
cd "$repo_root"

./gradlew --quiet :library:syncScreenshotKdoc

if ! git diff --quiet -- library/src/commonMain; then
    echo "pre-commit: syncScreenshotKdoc produced changes in library/src/commonMain." >&2
    echo "Stage the regenerated KDoc and re-run the commit:" >&2
    echo "  git add library/src/commonMain && git commit" >&2
    exit 1
fi
HOOK

chmod +x "$hooks_dir/pre-commit"
echo "Installed pre-commit hook at $hooks_dir/pre-commit"
