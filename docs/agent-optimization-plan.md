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
- [ ] **BLOCKED (needs user approval):** wire the hook + script permissions into `.claude/settings.json`.
      The harness classifies editing settings.json to add a PostToolUse hook as self-modification.
      User must apply the snippet below (in plan doc "Pending manual settings.json change").
- [ ] `Stop` hook → remind to run `:library:desktopTest` when library files changed

### Phase 4 — Scaffolding
- [ ] `scripts/new-component.sh` — stamp component + tests + demo + catalog registration

### Phase 5 — Canonical verify path
- [x] `scripts/verify-change.sh` — single entry point mirroring CI order; verified green end-to-end
- [x] Documented in AGENTS.md ("Verifying a Change")

### Phase 6 — Hygiene
- [ ] Resolve dead ruflo/claude-flow guidance (`.claude-flow/`, global CLAUDE.md note)
- [ ] PR-title / Conventional-Commit lint (the deferred "step 8")
- [ ] Drop stale `.execution-history/` permission entries in `.claude/settings.json`

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

## Pending manual settings.json change (Phase 3 wiring — needs user approval)

Add to `.claude/settings.json`:
- Under `permissions.allow`: `"Bash(./scripts/check-design-rules.sh *)"`,
  `"Bash(scripts/check-design-rules.sh *)"`, `"Bash(./scripts/verify-change.sh)"`
- A top-level `hooks` block:

```json
"hooks": {
  "PostToolUse": [
    {
      "matcher": "Edit|Write|MultiEdit",
      "hooks": [
        { "type": "command", "command": "$CLAUDE_PROJECT_DIR/scripts/hooks/design-rule-posttooluse.sh" }
      ]
    }
  ]
}
```

## Commits on this branch
- Phase 1: `scripts/check-design-rules.sh` + CI "Design-rule gate" step
- Phase 3 (partial): `scripts/hooks/design-rule-posttooluse.sh` (wiring pending user approval)
