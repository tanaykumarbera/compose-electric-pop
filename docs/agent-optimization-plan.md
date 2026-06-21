# Agent Optimization Plan — Execution State

> Durable progress tracker for the "optimize repo for automated agent-based development"
> work. Update the checkboxes as items land so the work can resume across sessions.
> Branch: `chore/agent-optimization`.

## Goal

Close the gaps where agent correctness currently depends on LLM judgment or manual
steps that should be mechanical. Convert "MUST" rules into fast, local, deterministic
gates; reduce per-task friction; remove sources of agent confusion.

## Phases & status

### Phase 1 — Machine-checkable design rules
- [x] `scripts/check-design-rules.sh` — grep gate (hex colors, inline TextStyle, material-icons imports)
- [x] Wire into CI as a "Design-rule gate" step
- [ ] (optional later) promote to custom Detekt ruleset

### Phase 2 — Structural-completeness gates
- [x] `checkCatalogRegistration` — assert every public `Pop*` has a CatalogScreen entry (demo/build.gradle.kts + catalog-allowlist.txt + CI step)
- [ ] Generated component manifest (`library/components.json`) + README count assertion

### Phase 3 — In-session Claude Code hooks
- [x] `scripts/hooks/design-rule-posttooluse.sh` — wrapper, tested (exit 2 + feedback on library violations, skips demo/non-kt)
- [x] Wired the PostToolUse hook + script permissions into `.claude/settings.json` (user-approved)
- [ ] `Stop` hook → remind to run `:library:desktopTest` when library files changed (optional)

### Phase 4 — Scaffolding
- [ ] `scripts/new-component.sh` — stamp component + tests + demo + catalog registration

### Phase 5 — Canonical verify path
- [x] `scripts/verify-change.sh` — single entry point mirroring CI order; verified green end-to-end
- [x] Documented in AGENTS.md ("Verifying a Change")

### Phase 6 — Hygiene
- [x] Removed ruflo entirely: global `~/.claude/CLAUDE.md`, `.claude-flow/` dir, `.gitignore` entry,
      the npm binary (`npm uninstall -g ruflo`), and both `mcpServers.ruflo` registrations in
      `~/.claude.json`.
- [x] PR-title / Conventional-Commit lint — `.github/workflows/pr-title-lint.yml` (regex-tested)
- [x] PR template now points at `./scripts/verify-change.sh`
- [x] Dropped stale `.execution-history/` permission entries from `.claude/settings.json`

### Phase 7 — Observability (optional, last)
- [ ] Lightweight per-run ledger via Stop hook

## Calibration notes (so the gate stays zero-false-positive)
- `Color(0x…)` hex appears only in KDoc examples (chart) → strip comment lines before grep.
- `TextStyle(` construction: 0 real uses; `ProvideTextStyle(` must NOT match (word boundary).
- `Color.White/.Black.copy(alpha=)` scrims + `Color.Transparent/.Unspecified` sentinels are
  legitimate → NOT flagged.
- Demo sources legitimately use hex colors as sample data (e.g. `PopChartSeries(color = Color(0x…))`)
  → color/TextStyle rules apply to **library main sources only**; the material-icons import ban
  applies repo-wide (library + demo).
- Theme files (`theme/Color.kt`, `theme/Typography.kt`) are the definition sites → exempt.

## Commits on this branch
- Phase 1: `scripts/check-design-rules.sh` + CI "Design-rule gate" step
- Phase 2: `:demo:checkCatalogRegistration` gate + CI step
- Phase 5: `scripts/verify-change.sh` + AGENTS.md
- Phase 6: PR-title lint workflow + PR template update
- Phase 3 + 6 (settings/ruflo): hook wiring, `.execution-history` cleanup, ruflo removal
