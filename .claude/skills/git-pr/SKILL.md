---
name: git-pr
description: Create a well-structured GitHub PR for Electric Pop components. Includes changes table, screenshot table (light/dark goldens), dependency callouts, and cons/gotchas. Use after a component or fix is approved and ready to push.
allowed-tools: Bash, Read, Glob, Grep
argument-hint: "[component name] [branch name]"
---

# Create a Well-Structured PR for Electric Pop

Use the GitHub MCP tool `mcp__plugin_github_github__create_pull_request` to create the PR. Never use `gh` CLI (not installed).

Owner: `tanaykumarbera` | Repo: `compose-electric-pop`

## Step 1: Gather facts

Run these before writing the PR body:

```bash
# What changed vs main
git diff --name-only origin/main...HEAD

# Commit summary
git log --oneline origin/main...HEAD

# Confirm golden snapshots exist
ls library/src/desktopTest/snapshots/
```

## Step 2: Build the PR body

Use the template below. Fill every section — do not omit any section even if short.

---

### Template

```
## Summary
- Add/Fix {ComponentName} ({tier}) — {one-line description of what this PR does}
- {Key design decision or notable behavior}
- {Any variant or API highlights}

## Changes

| File | Change |
|------|--------|
| `library/.../foundation/{Component}.kt` | New component / Rewritten — {brief description} |
| `library/.../foundation/{Component}Test.kt` | {Unit tests / Placeholder — reason} |
| `library/.../foundation/{Component}ScreenshotTest.kt` | Screenshot tests: {variant list} |
| `demo/.../components/{Component}Demo.kt` | Demo page: {variant list shown} |
| `demo/.../CatalogScreen.kt` | Registered in catalog |

## Screenshots

Golden snapshots from `library/src/desktopTest/snapshots/`. GitHub renders inline PNGs from the repo — use relative paths from repo root.

| Variant | Light | Dark |
|---------|-------|------|
| All variants | ![Light](library/src/desktopTest/snapshots/{Component}_allVariants_light.png) | ![Dark](library/src/desktopTest/snapshots/{Component}_allVariants_dark.png) |

> If multiple screenshot test functions exist, add one row per function.

## Dependencies

**Composes (for composites):**
- `PopXxx` — reason it's used

**Will be used by:**
- `PopXxx` (Wave N, planned) — how it uses this component

**Breaking changes from previous version** (for fix PRs):
- Removed param `oldParam` — replaced by `newParam`
- Removed `OldEnum` entirely

## Watch Out For

- **API breaking change**: {yes/no — describe if yes}
- **Theme sensitivity**: {Any colors/typography that look different at unusual theme overrides?}
- **Animation**: {Any infinite transitions that could affect screenshot tests?}
- **Accessibility**: {Any ghost borders, contrast concerns?}
- **Known limitations**: {e.g., pulsing dot freezes in screenshot test — expected, static capture}
```

---

## Step 3: Set PR metadata

- **Title format**: `feat({tier}): add {ComponentName} with variants` OR `fix({tier}): {what was fixed}`
- **Head**: current feature/fix branch
- **Base**: always `main` — never another feature branch
- Verify base branch exists: `git ls-remote --heads origin main`

## Step 4: After creating the PR

Report:
- PR URL
- Whether screenshots rendered inline (GitHub renders PNGs from default branch — they may not show until merged)
- Any items in "Watch Out For" the reviewer should manually verify
