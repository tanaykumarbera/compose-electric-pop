# Electric Pop — Agent Guide

> **TL;DR for fresh sessions:**
> - **To build a component:** Use `/build-component <ComponentName>` in a normal `claude` session. The skill orchestrates planner → implementor → reviewer subagents and handles Telegram/GitHub/Stitch MCP from the main session.
> - **For ad-hoc tasks:** Dispatch `pixy-planner`, `pixy-implementor`, or `pixy-reviewer` directly via the Agent tool as needed.
> - **Avoid:** Running `claude --agent pixy` or `Agent(pixy)` — MCP tools don't propagate reliably to agent sessions (known Claude Code bug, anthropics/claude-code#30280).

## Project Context

Electric Pop is a Compose Multiplatform UI library implementing the "Kinetic Pulse" design system.

- **Repo:** github.com/tanaykumarbera/compose-electric-pop
- **Design:** [Stitch project 7983075619754946215](https://stitch.withgoogle.com/projects/7983075619754946215)
- **Spec:** `docs/superpowers/specs/2026-03-25-electric-pop-design.md`
- **Plan:** `docs/superpowers/plans/2026-03-25-electric-pop-implementation.md`
- **CLAUDE.md:** Project coding rules, SOP, and 7 design rules
- **Targets:** Android (API 24+), iOS, Desktop JVM
- **Stack:** Kotlin 2.3.20, Compose Multiplatform 1.10.3, AGP 8.7.3

## Component Inventory (30 total)

### Foundation (20) — `com.electricpop.foundation`
| # | Component | Variants | Key Notes |
|---|-----------|----------|-----------|
| 1 | PopButton | Primary, Secondary, Ghost × XL/Large/Small; Icon | Neon glow on primary, kinetic hover/active |
| 2 | PopTextField | Standard, Password, Error | Left accent bar on focus, no bottom line |
| 3 | PopRadioGroup | Tonal shift radio options | No dividers |
| 4 | PopSwitch | On/Off toggle | secondary_container fill |
| 5 | PopSlider | Range with value | 24px thumb |
| 6 | PopChip | Primary/Secondary/Tertiary container colors | Pill shape |
| 7 | PopIcon | Material Symbols wrapper | Outlined FILL:0 |
| 8 | PopSurface | Themed container | Squircle, tonal shadow |
| 9 | PopBadge | Directional + value | Semantic green/red |
| 10 | PopPill | Label badge | Small colored pill |
| 11 | PopIconRow | Dynamic 1-N icons | Horizontal cluster |
| 12 | PopSectionHeader | Accent label + title + line | Numbered variant |
| 13 | PopTitleBar | Title + inline PopPill | Headline italic |
| 14 | PopDisplayText | Large text + fractional | Directional coloring |
| 15 | PopCodeBlock | Pre-formatted code | Monospace, copy header — PR #21 merged |
| 16 | PopIconListItem | Icon + description | For guidelines/lists |
| 17 | PopTable | Label-value rows | Alternating tonal, colored variants |
| 18 | PopStepList | Numbered items + icons | 01/02/03 entries |
| 19 | PopBottomBar | Icons, Icons+text, active | Glassmorphic, backdrop-blur |
| 20 | PopDropdown | Selector + expand icon | Primary accent |

### Composite (7) — `com.electricpop.composite`
| # | Component | Composes | Description |
|---|-----------|----------|-------------|
| 1 | ~~PopFeatureCard~~ | — | **Covered by PopMetricCard** — same slot structure (label + display + badge + coin-stack icons), same Hero/Surface styles. PR #25 closed. |
| 2 | PopCarouselCard | PopIcon, PopChip, PopDisplayText, PopSurface | Horizontal scroll cards |
| 3 | PopDashboardCard | PopSectionHeader, PopPill, PopDataRow, PopSurface | Data overview |
| 4 | PopDataRow | PopIcon, PopDisplayText | Icon + label + value |
| 5 | PopActionCard | PopDropdown, PopDisplayText, PopButton, PopTextField | Input + actions |
| 6 | PopBannerCard | PopSurface + headline overlay | Big image + text |
| 7 | PopMetricCard | PopBadgeDirection, PopDisplayText, PopSurface | Metric display ✅ |

### Chart (3) — `com.electricpop.chart`
| # | Component | Description |
|---|-----------|-------------|
| 1 | PopLineChart | Trend line, glow on active points |
| 2 | PopBarChart | Comparative bars, active scales 1.02x |
| 3 | PopDonutChart | Circular gauge, center text |

---

## Agent System

### Building Components: `/build-component` Skill

**To build a component end-to-end, use the `/build-component` skill.** This runs the full plan → implement → review pipeline from the main session, where MCP tools (Telegram, GitHub, Stitch) work reliably.

```
/build-component PopCodeBlock foundation "Pre-formatted code block with monospace font and copy header"
```

The skill orchestrates `pixy-planner` → `pixy-implementor` → `pixy-reviewer` as subagents, handles Telegram notifications, and creates the PR.

### Subagents (independently usable)

Each subagent can also be dispatched directly via the Agent tool for ad-hoc tasks:

| Agent | File | Model | Role | When to use independently |
|-------|------|-------|------|--------------------------|
| Planner | `.claude/agents/pixy-planner.md` | opus | Creates implementation plans with Stitch design reference | Planning a component or change without building it yet |
| Implementor | `.claude/agents/pixy-implementor.md` | sonnet | Writes code, tests, demos; runs builds; commits | Executing a known plan or making targeted code changes |
| Reviewer | `.claude/agents/pixy-reviewer.md` | opus | Reviews against plan, design rules, and test quality | Reviewing existing code against spec/design rules |

**Note:** The `pixy.md` agent file is retained for reference but the `/build-component` skill is the preferred orchestration path. The skill runs in the main session where all MCP tools are available, avoiding the known issue where subagents cannot discover deferred MCP tools via ToolSearch.

### Key Design Decisions

**Why `/build-component` skill + subagents:**
- The skill runs in the main session where MCP tools (Telegram, GitHub, Stitch) are reliably available
- Each subagent has scoped tools (reviewer can't write, planner can access Stitch MCP)
- Agent definitions are version-controlled and reusable
- Models are locked per role (opus for planning/review, sonnet for implementation)
- Max turns prevent runaway agents
- Subagents can be used independently for ad-hoc tasks (planning, reviewing, fixing)

**Test quality enforcement:**
- Planner must define what IS and ISN'T testable for each component
- Implementor has explicit rules against stdlib-only tests
- Reviewer has an "acid test": would tests pass if component file were deleted?
- This prevents the garbage tests we saw in the initial pipeline test

**Icons — PopIcons (not material-icons-core):**
- The library provides `PopIcons` in `com.electricpop.foundation` with 16 built-in `ImageVector` icons
- All demos, tests, and screenshot tests MUST use `PopIcons.*` (e.g., `PopIcons.Star`, `PopIcons.Check`)
- NEVER add `material-icons-core` or `material-icons-extended` as a dependency
- Available icons: Star, Check, Close, Info, Warning, Heart, Home, Search, Settings, Add, ArrowUp, ArrowDown, ArrowBack, ArrowForward, Person, TrendUp, TrendDown, Bolt, Sparkle, CheckCircle, Layers, Puzzle, Tokens, Menu

**Stitch design comparison:**
- Reviewer agent now fetches Stitch screenshots at full resolution and compares against component output
- Append `=s0` to Google Photos URLs for original size
- Stitch project ID: `7983075619754946215`

**Branching and base branch resolution:**
- Each component gets its own `feat/pop-{name}` branch, chained off the previous component's branch
- PRs are merged in order; once merged, the base branch is **deleted from origin**
- When creating a PR, always check `git ls-remote --heads origin` first to confirm the intended base branch still exists on the remote
- If the base branch has been deleted (merged to main), use `main` as the PR base instead — this is expected behaviour, not an error
- Example: if `feat/pop-surface` was merged and deleted, `feat/pop-badge` PR should target `main`

**Android build notes:**
- `compileSdk = 36` required (squircle-shape dependency)
- Demo lint is disabled (AGP 8.7.3 / Kotlin 2.3.20 incompatibility)

---

## Recommended Component Build Order

Build in dependency order — foundations first, then composites that depend on them.

### Wave 1: Core Foundation (no dependencies)
1. PopPill (simplest — good for testing the pipeline)
2. PopIcon
3. PopSurface
4. PopBadge

### Wave 2: Input Foundation
5. PopButton
6. PopTextField
7. PopSwitch
8. PopSlider
9. PopRadioGroup
10. PopChip
11. PopDropdown

### Wave 3: Layout Foundation
12. PopIconRow
13. PopSectionHeader
14. PopTitleBar
15. PopDisplayText
16. PopIconListItem
17. PopStepList
18. PopTable
19. PopCodeBlock
20. PopBottomBar

### Wave 4: Composites (depend on foundation)
21. PopDataRow (needs PopIcon, PopDisplayText) ✅
22. PopMetricCard (needs PopBadgeDirection, PopDisplayText, PopSurface) ✅
23. ~~PopFeatureCard~~ — **skipped, covered by PopMetricCard**
24. PopDashboardCard (needs PopSectionHeader, PopPill, PopDataRow, PopSurface)
25. PopCarouselCard (needs PopIcon, PopChip, PopDisplayText, PopSurface)
26. PopActionCard (needs PopDropdown, PopDisplayText, PopButton, PopTextField)
27. PopBannerCard (needs PopSurface)

### Wave 5: Charts
28. PopLineChart
29. PopBarChart
30. PopDonutChart

---

## Ruflo / Claude-flow Integration

This project includes the [ruflo](https://github.com/ruvnet/ruflo) (claude-flow v3) catalog at `.claude/agents/` and `.claude-flow/`. These add memory, task tracking, swarm coordination, and hooks on top of the pixy component pipeline.

### Two separate runtimes — don't confuse them

| System | Invocation | MCP tools | Best for |
|--------|-----------|-----------|----------|
| **Pixy** (Claude Code agents) | `Agent(subagent_type="pixy-*")` in main session | Stitch, GitHub, Telegram | Component build/review — needs those MCPs |
| **Ruflo** (claude-flow v3) | `mcp__ruflo__*` tools | ruflo-internal | Memory, task tracking, swarm coordination, hooks |

**Why pixy agents cannot be launched through ruflo:** Ruflo's `agent_spawn` does not execute Claude at all — it registers a task record in a coordination queue and returns `"status": "registered"`. Tasks stay `"pending"` until an external runner (`claude -p` CLI or a background worker) polls and executes them. There is no way to run pixy agents synchronously through ruflo MCP tools or receive their results inline. The `/build-component` skill uses Claude Code's `Agent(subagent_type=...)` tool instead, which runs actual synchronous inference and returns results directly. (Verified experimentally: spawned agent remained idle with 0 tasks executed.)

### Where claude-flow adds real value alongside pixy

| Task | How |
|------|-----|
| **Persist component build state across sessions** | `mcp__ruflo__memory_store` to save which components are done, PR URLs, reviewer notes |
| **Track wave progress** | `mcp__ruflo__task_create/complete` to log each component as a task within a wave |
| **Cross-session memory** | `mcp__ruflo__memory_search` at session start to recall last-touched component and open items |
| **Hooks for learning** | `PostToolUse` hooks record edit patterns → `hooks_intelligence` learns project-specific patterns |
| **Code review (non-component work)** | `.claude/agents/core/reviewer.md` or `github/code-review-swarm` for PRs, hotfixes, infra changes |
| **Debugging build failures** | `.claude/agents/development/` agents for diagnosing Gradle/Kotlin errors |
| **GitHub automation** | `.claude/agents/github/` agents for issue triage, release notes, multi-repo work |

### Relationship to pixy

| Concern | Primary | Ruflo support |
|---------|---------|---------------|
| Component planning | `pixy-planner` | — |
| Component implementation | `pixy-implementor` | — |
| Component review | `pixy-reviewer` | `core/reviewer.md` (fallback) |
| Build-state memory | — | `memory_store/search` |
| Wave progress tracking | — | `task_create/complete` |
| Code review (non-component) | — | `github/code-review-swarm` |
| Debugging | — | `development/` agents |
| GitHub workflows | — | `github/` agents |

**Rule:** Component work goes through `/build-component` → pixy. Everything else (state memory, tracking, code review, debugging) can use ruflo tools directly from the main session.

### Ruflo agent categories

| Directory | Purpose |
|-----------|---------|
| `core/` | Fundamental roles: coder, tester, planner, reviewer, researcher |
| `github/` | PR management, code review swarms, issue tracking |
| `architecture/` | System design, DDD, security architecture |
| `development/` | Backend dev, mobile dev, frontend design |
| `devops/` | CI/CD pipelines, GitHub Actions |
| `optimization/` | Performance analysis, benchmarking |
| `swarm/` | Multi-agent coordination, topology optimization |
| `testing/` | TDD, comprehensive test generation |
| `specialized/` | Domain-specific: API docs, production validation |

### Ruflo hooks and routing

The `UserPromptSubmit` hook in `.claude/settings.json` routes tasks to optimal agents automatically. When you start a conversation, the hook analyzes your prompt and suggests the best agent type. This is advisory — you can override it.

Key hooks:
- **`PreToolUse` (Bash)** — Validates command safety before execution
- **`PostToolUse` (Write/Edit)** — Records edit outcomes for learning
- **`SessionStart`** — Restores previous session state, imports auto-memory
- **`SessionEnd`** — Persists session state for continuity
- **`SubagentStart/Stop`** — Tracks subagent lifecycle

### Ruflo skills

Extended skills are available at `.claude/skills/`. These complement the existing `/build-component` and `/git-pr` skills:

| Skill | Purpose |
|-------|---------|
| `github-code-review` | Comprehensive code review workflows |
| `github-workflow-automation` | GitHub Actions CI/CD setup |
| `pair-programming` | AI-assisted pair programming |
| `sparc-methodology` | SPARC development methodology |
| `swarm-orchestration` | Multi-agent swarm coordination |
| `verification-quality` | Truth verification and quality checks |

---

## Error Handling Protocol

- **Implementor fails same step twice** → Pixy STOPS, reports to human
- **Reviewer rejects 3 times** → Pixy STOPS, reports to human
- **Build fails** → Implementor reads error, attempts fix once, then reports
- **Never enter blind retry loops** — every retry must have a different approach
