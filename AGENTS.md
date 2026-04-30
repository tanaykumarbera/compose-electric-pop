# Electric Pop — Agent Guide

Operating manual for agents (and humans) working in this repo. Library is feature-complete; new component work is rare. The default mode now is targeted maintenance, doc tweaks, and the occasional one-off component.

## Orientation

- **Repo:** [github.com/tanaykumarbera/compose-electric-pop](https://github.com/tanaykumarbera/compose-electric-pop)
- **Design language:** [`DESIGN.md`](DESIGN.md) — the 7 design rules, theme architecture, color/typography/shape/spacing tokens. Read this first if you're touching components.
- **Component reference:** <https://tanaykumarbera.github.io/compose-electric-pop/api/> — Dokka-generated API docs with inline light + dark screenshots for every component. Authoritative source for "what does PopX look like and accept."
- **Stitch designs:** [project 7983075619754946215](https://stitch.withgoogle.com/projects/7983075619754946215). Append `=s0` to image URLs for full resolution. Always compare both light and dark screens.
- **Project context not in code:** Auto-memory at `~/.claude/projects/-mnt-workspace-mobile-apps-ui-electric-pop/memory/MEMORY.md` (loaded each session). Notable entries: no-AI-branding, Telegram update rule, Stitch-review rule.
- **Build-phase archive:** `.history/2026-04-26-build-phase-summary.md` — distilled retrospective from the original build sprint. The only place that captures "how we got here" prose.
- **Targets:** Android (API 24+, compileSdk 36), iOS (arm64 + simulatorArm64), Desktop (JVM). Stack: Kotlin 2.3.20, Compose Multiplatform 1.10.3, AGP 8.7.3.

## Building a Component (rare)

Use the `/build-component` skill. It runs the full plan → implement → review pipeline from the **main session**, where MCP tools (Telegram, GitHub, Stitch) work reliably.

```
/build-component PopXxx foundation "One-line description"
```

The skill orchestrates `pixy-planner` → `pixy-implementor` → `pixy-reviewer` as subagents, handles Telegram notifications, and creates the PR.

**Do not** run `claude --agent pixy` or `Agent(pixy)` directly — MCP tools don't propagate reliably to top-level agent sessions ([anthropics/claude-code#30280](https://github.com/anthropics/claude-code/issues/30280)).

## Subagents (independently usable)

For ad-hoc tasks that don't need the full pipeline, dispatch one subagent directly via the `Agent` tool:

| Agent | File | Model | Role | When to use solo |
|-------|------|-------|------|------------------|
| Planner | `.claude/agents/pixy-planner.md` | opus | Writes implementation plans, references Stitch + DESIGN.md | Planning a change without building it yet |
| Implementor | `.claude/agents/pixy-implementor.md` | sonnet | Writes code, tests, demos; runs builds; commits | Executing a known plan, targeted code changes |
| Reviewer | `.claude/agents/pixy-reviewer.md` | opus | Reviews against plan, design rules, test quality | Auditing existing code before merge |

Each subagent has scoped tools (reviewer can't write, planner has Stitch MCP). Models are locked per role. Max-turns guards against runaways.

## MCP Tools

GitHub, Stitch, and Context7 operations all use MCP plugins. **Never** use `gh` CLI (not installed), raw `curl`, or `claude` CLI subprocesses for these.

- **GitHub:** `mcp__plugin_github_github__*` — PRs, issues, commits, code search.
- **Stitch:** `mcp__stitch__*` — design screens, project data.
- **Context7:** `mcp__plugin_context7_context7__*` — library/framework docs.

MCP tools are **deferred**: their names appear in `<system-reminder>` tags but schemas must be loaded before calling. To load:

```
ToolSearch(query="select:mcp__plugin_github_github__list_pull_requests", max_results=1)
```

This returns the schema; after that, call the tool normally as a tool call (not via Bash).

**Common mistakes:**
- Don't run `claude tools list`, `claude mcp list`, or any `claude` subprocess — these don't work from inside a session.
- Don't invoke MCP tools through Bash. They're tool calls.
- Don't fall back to `gh` (not installed) or raw `curl` for GitHub.

## Branching and Commit Rules

- Each unit of work gets its own `feat/...`, `chore/...`, or `docs/...` branch off `main`.
- Conventional Commits required (Detekt + Spotless are gated; PR-title lint coming in step 8 of the release track).
- **No AI branding** in commits or PR bodies. No `Co-Authored-By: Claude`, no tool attribution. This is a hard rule.
- Push the **feature branch only** — never to `main`. Merging is the user's action via PR.
- All PRs target `main` directly, even when chained off another open branch for code continuity.

## Test Quality Rules

These exist because the initial build pipeline produced stdlib-only "tests" that never touched component code. Reviewer rejects on sight now.

- Planner must declare what IS and ISN'T testable per component.
- Implementor cannot write tests that exercise only Kotlin stdlib (`String.uppercase()`, etc.).
- Reviewer's acid test: would the test still pass if the component file were deleted? If yes, the test is fake.
- For purely visual components, a placeholder test documenting "validation via demo + Roborazzi" is acceptable.

## Stitch Comparison

Reviewer fetches Stitch screenshots at full resolution and compares against the rendered component. This is non-optional — a memory of past slips means we always do the visual diff.

- Project ID: `7983075619754946215`
- Append `=s0` to Google Photos URLs for original size
- Crop tall images (>4000px) before viewing
- Always compare BOTH light and dark variants

## Error Handling Protocol

- **Implementor fails the same step twice** → STOP, report to human.
- **Reviewer rejects 3 times** → STOP, report to human.
- **Build fails** → Implementor reads error, attempts fix once, then reports.
- **Never enter blind retry loops.** Every retry must change approach.

## Telegram Notifications

`chat_id: 1402731017` — the user reads progress on Telegram, not the terminal.

**Notify on:**
- Any PR creation (component, doc, fix — all of them)
- Wave or checkpoint completion
- Decision needed from the user
- Blocker hit after 2–3 failed attempts

**Do not notify on:** intermediate build output, sub-step progress, planner/reviewer dispatches.
