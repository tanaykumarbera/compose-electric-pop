# Electric Pop — Agent Guide

> **TL;DR for fresh sessions:**
> - **Library is feature-complete.** Current track is repo cleanup + Maven Central publishing. Read `docs/superpowers/plans/SESSION-RESUME.md` first for next-step status; full plan at `docs/superpowers/plans/repo-cleanup-plan.md`.
> - **For project context that's not in code:** Consult the auto-memory index at `~/.claude/projects/-mnt-workspace-mobile-apps-ui-electric-pop/memory/MEMORY.md` (loaded automatically each session). Notable entries: release-readiness state, no-AI-branding rule, Telegram update rule, Stitch-review rule.
> - **To build a new component (rare now):** Use `/build-component <ComponentName>`. The skill orchestrates planner → implementor → reviewer subagents and handles Telegram/GitHub/Stitch MCP from the main session.
> - **For ad-hoc tasks:** Dispatch `pixy-planner`, `pixy-implementor`, or `pixy-reviewer` directly via the Agent tool as needed.
> - **Avoid:** Running `claude --agent pixy` or `Agent(pixy)` — MCP tools don't propagate reliably to agent sessions (known Claude Code bug, anthropics/claude-code#30280).

## Project Context

Electric Pop is a Compose Multiplatform UI library implementing the "Kinetic Pulse" design system.

- **Repo:** github.com/tanaykumarbera/compose-electric-pop
- **Design:** [Stitch project 7983075619754946215](https://stitch.withgoogle.com/projects/7983075619754946215)
- **Spec:** `docs/superpowers/specs/2026-03-25-electric-pop-design.md`
- **Release track:** `docs/superpowers/plans/SESSION-RESUME.md` (status) · `docs/superpowers/plans/repo-cleanup-plan.md` (full plan)
- **CLAUDE.md:** Project coding rules, SOP, and 7 design rules
- **Targets:** Android (API 24+), iOS, Desktop JVM
- **Stack:** Kotlin 2.3.20, Compose Multiplatform 1.10.3, AGP 8.7.3

## Component Inventory (30 total)

### Foundation (20) — `co.tanay.electricpop.foundation`
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

### Composite (6) — `co.tanay.electricpop.composite`
| # | Component | Composes | Description |
|---|-----------|----------|-------------|
| 1 | PopCarouselCard | PopIcon, PopChip, PopDisplayText, PopSurface | Horizontal scroll cards |
| 2 | PopDashboardCard | PopSectionHeader, PopPill, PopDataRow, PopSurface | Data overview |
| 3 | PopDataRow | PopIcon, PopDisplayText | Icon + label + value |
| 4 | PopActionCard | PopDropdown, PopDisplayText, PopButton, PopTextField | Input + actions |
| 5 | PopBannerCard | PopBadgeDirection, PopDisplayText, PopIcon, PopSurface | Hero / metric banner: label + value + trend chip + overlapping icon cluster (Hero/Surface styles). |
| 6 | PopImageBannerCard | PopSurface + Image | Big image card with overlaid headline, configurable text anchor + scrim. |

### Chart (3) — `co.tanay.electricpop.chart`
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
- The library provides `PopIcons` in `co.tanay.electricpop.foundation` with 16 built-in `ImageVector` icons
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
21. PopDataRow (needs PopIcon, PopDisplayText)
22. PopBannerCard (needs PopBadgeDirection, PopDisplayText, PopIcon, PopSurface) — also fills the hero-spotlight slot originally planned as PopFeatureCard
23. PopDashboardCard (needs PopSectionHeader, PopPill, PopDataRow, PopSurface)
24. PopCarouselCard (needs PopIcon, PopChip, PopDisplayText, PopSurface)
25. PopActionCard (needs PopDropdown, PopDisplayText, PopButton, PopTextField)
26. PopImageBannerCard (needs PopSurface)

### Wave 5: Charts
27. PopChart with PopChartStyle.Line / Bar / Donut variants

---

## Error Handling Protocol

- **Implementor fails same step twice** → Pixy STOPS, reports to human
- **Reviewer rejects 3 times** → Pixy STOPS, reports to human
- **Build fails** → Implementor reads error, attempts fix once, then reports
- **Never enter blind retry loops** — every retry must have a different approach
