# Electric Pop — Agent Guide

> **TL;DR for fresh sessions:** To build a component, dispatch `pixy-planner` → `pixy-implementor` → `pixy-reviewer` directly via the Agent tool. Do NOT use the `pixy` orchestrator — it causes multi-level nesting failures. See "How to Build a Component" below.

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
| 15 | PopCodeBlock | Pre-formatted code | Monospace, copy header |
| 16 | PopIconListItem | Icon + description | For guidelines/lists |
| 17 | PopTable | Label-value rows | Alternating tonal, colored variants |
| 18 | PopStepList | Numbered items + icons | 01/02/03 entries |
| 19 | PopBottomBar | Icons, Icons+text, active | Glassmorphic, backdrop-blur |
| 20 | PopDropdown | Selector + expand icon | Primary accent |

### Composite (7) — `com.electricpop.composite`
| # | Component | Composes | Description |
|---|-----------|----------|-------------|
| 1 | PopFeatureCard | PopDisplayText, PopBadge, PopIconRow, PopSurface | Hero spotlight card |
| 2 | PopCarouselCard | PopIcon, PopChip, PopDisplayText, PopSurface | Horizontal scroll cards |
| 3 | PopDashboardCard | PopSectionHeader, PopPill, PopDataRow, PopSurface | Data overview |
| 4 | PopDataRow | PopIcon, PopDisplayText | Icon + label + value |
| 5 | PopActionCard | PopDropdown, PopDisplayText, PopButton, PopTextField | Input + actions |
| 6 | PopBannerCard | PopSurface + headline overlay | Big image + text |
| 7 | PopMetricCard | PopBadge, PopDisplayText, PopSurface | Metric display |

### Chart (3) — `com.electricpop.chart`
| # | Component | Description |
|---|-----------|-------------|
| 1 | PopLineChart | Trend line, glow on active points |
| 2 | PopBarChart | Comparative bars, active scales 1.02x |
| 3 | PopDonutChart | Circular gauge, center text |

---

## Agent System

Agents are defined as custom Claude Code agents in `.claude/agents/`. Each agent is a markdown file with YAML frontmatter defining its model, tools, and behavior.

### Agent Files

| Agent | File | Model | Role |
|-------|------|-------|------|
| Pixy | `.claude/agents/pixy.md` | opus | Orchestrator — dispatches planner, implementor, reviewer |
| Planner | `.claude/agents/pixy-planner.md` | opus | Creates implementation plans with Stitch design reference |
| Implementor | `.claude/agents/pixy-implementor.md` | sonnet | Writes code, tests, demos; runs builds; commits |
| Reviewer | `.claude/agents/pixy-reviewer.md` | opus | Reviews against plan, design rules, and test quality |

### How to Build a Component

The main Claude session acts as the orchestrator. Dispatch the three subagents directly — **do NOT use the `pixy` orchestrator agent**. Multi-level agent nesting (main → pixy → subagents) causes pixy to fall back to spawning `claude` CLI subprocesses instead of using the Agent tool.

**Correct pattern (2-level nesting):**

```
Step 1 — Plan:
Agent(subagent_type="pixy-planner", prompt="""
  Component: {NAME}, Tier: {foundation/composite/chart}
  Variants: {list}, Key notes: {notes}
  7 design rules: [copy from CLAUDE.md]
  Theme API: [colors, typography, spacing, shapes]
  Stitch project ID: 7983075619754946215
  For composites: foundation component code they depend on
""")

Step 2 — Implement (using the plan from Step 1):
Agent(subagent_type="pixy-implementor", prompt="""
  [Full plan from Step 1]
  Branch: create feat/pop-{name} from current HEAD
  Build commands, commit format, reference files to read
""")

Step 3 — Review:
Agent(subagent_type="pixy-reviewer", prompt="""
  [Plan from Step 1 as the spec]
  Files created: [list from Step 2]
  7 design rules
  Stitch project ID for visual comparison
  NOTE: crop Stitch images >4000px before reading
""")

Step 4 — Fix loop (if ISSUES_FOUND):
  Re-dispatch pixy-implementor with reviewer's fix list
  Then re-dispatch pixy-reviewer. Max 3 iterations.

Step 5 — After APPROVED:
  git push + create PR via GitHub MCP
  PR base = previous component's branch (or main if merged/deleted)
  Update .execution-history/phase-03-implementation.md
```

### Key Design Decisions

**Why custom agents instead of inline prompts:**
- Each agent has scoped tools (reviewer can't write, planner can access Stitch MCP)
- Agent definitions are version-controlled and reusable
- Models are locked per role (opus for planning/review, sonnet for implementation)
- Max turns prevent runaway agents

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

### Wave 1: Core Foundation — **COMPLETE** (all merged to main)
1. PopPill ✓
2. PopIcon ✓
3. PopSurface ✓
4. PopBadge ✓ (PR #2 open, branch: feat/pop-badge)

### Wave 2: Input Foundation — **NEXT** (start from feat/pop-badge)
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
22. PopMetricCard (needs PopBadge, PopDisplayText, PopSurface)
23. PopFeatureCard (needs PopDisplayText, PopBadge, PopIconRow, PopSurface)
24. PopDashboardCard (needs PopSectionHeader, PopPill, PopDataRow, PopSurface)
25. PopCarouselCard (needs PopIcon, PopChip, PopDisplayText, PopSurface)
26. PopActionCard (needs PopDropdown, PopDisplayText, PopButton, PopTextField)
27. PopBannerCard (needs PopSurface)

### Wave 5: Charts
28. PopLineChart
29. PopBarChart
30. PopDonutChart

---

## Error Handling Protocol

- **Implementor fails same step twice** → Pixy STOPS, reports to human
- **Reviewer rejects 3 times** → Pixy STOPS, reports to human
- **Build fails** → Implementor reads error, attempts fix once, then reports
- **Never enter blind retry loops** — every retry must have a different approach
