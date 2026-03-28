---
name: clean-commit
description: Create a clean git commit for an Electric Pop component. Handles staging, .gitignore hygiene, and post-commit verification. Use after a component passes all builds, tests, and screenshot recording.
allowed-tools: Bash, Read, Edit, Grep, Glob
argument-hint: "<ComponentName> <tier>"
---

Create a clean, complete commit for the **$0** component in the **$1** tier.

## Step 1: Pre-commit audit

Run `git status` and inspect the output carefully.

### Classify every entry:

**MUST stage** — files created/modified for this component:
- `library/src/commonMain/kotlin/com/electricpop/$1/$0.kt`
- `library/src/commonTest/kotlin/com/electricpop/$1/${0}Test.kt`
- `library/src/desktopTest/kotlin/com/electricpop/$1/${0}ScreenshotTest.kt`
- `library/src/desktopTest/snapshots/$0_*.png`
- `demo/src/commonMain/kotlin/com/electricpop/demo/components/${0}Demo.kt`
- `demo/src/commonMain/kotlin/com/electricpop/demo/CatalogScreen.kt`
- `.gitignore` (if you modified it)
- Any other file you intentionally changed for this component

**MUST gitignore** — untracked files that are build artifacts, caches, or tool metadata:
- `.kotlin/` — Kotlin compiler metadata
- `*.class` — compiled bytecode
- `out/`, `bin/` — IDE output directories
- `.cxx/` — Android native build cache
- `captures/` — Android layout inspector
- `*.log` — log files
- `local.properties` — already ignored but check
- Any `build/` directory at any depth
- `.fleet/`, `.run/` — IDE run configs
- `kotlin-js-store/` — JS target cache
- Any directory or file that is clearly not source code

**LEAVE ALONE** — untracked files that belong to the user but aren't part of this component:
- Other source files being worked on
- Documentation drafts
- Requirement files
- Anything you didn't create

## Step 2: Update .gitignore

If Step 1 found files that should be gitignored but aren't covered by the current `.gitignore`:

1. Read `.gitignore`
2. Add the missing patterns with a comment explaining why
3. The new patterns take effect immediately for `git status`

## Step 3: Stage and commit

```bash
# Stage component files — use explicit paths, not git add -A
git add library/src/commonMain/kotlin/com/electricpop/$1/$0.kt
git add library/src/commonTest/kotlin/com/electricpop/$1/${0}Test.kt
git add library/src/desktopTest/kotlin/com/electricpop/$1/${0}ScreenshotTest.kt
git add "library/src/desktopTest/snapshots/$0_"*.png
git add demo/src/commonMain/kotlin/com/electricpop/demo/components/${0}Demo.kt
git add demo/src/commonMain/kotlin/com/electricpop/demo/CatalogScreen.kt
# Include .gitignore if it was updated
git add .gitignore 2>/dev/null

git commit -m "feat($1): add $0 with variants"
```

If any of the expected files don't exist, STOP and report — the component is incomplete.

## Step 4: Post-commit verification (MANDATORY)

Run `git status` again.

**Check:** Are there any staged or modified files related to this component still showing? If yes:
- Stage them and amend the commit: `git commit --amend --no-edit`
- Run `git status` again

**Check:** Are there new untracked files that appeared during the build/test cycle? If they're artifacts, add to `.gitignore` and amend.

**The component is NOT done until the working tree has zero uncommitted changes related to this component.**

Report the final `git status` output so the caller can verify.
