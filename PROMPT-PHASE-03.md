# Electric Pop — Phase 03: Component Implementation

## How to Start This Session

**Recommended:** Start Claude as the pixy orchestrator directly:
```bash
claude --agent pixy
```
Then paste this file's contents (or `@PROMPT-PHASE-03.md`) as your first message. Pixy will read the project context and resume from the next PENDING component.

**Why this way:** Running `claude --agent pixy` makes the main session *be* pixy — 2-level nesting (pixy → sub-agents). The broken pattern was main → pixy (via Agent tool) → sub-agents (3 levels), which caused pixy to fall back to CLI subprocess dispatch.

**Alternative (regular Claude session):** If you start a plain `claude` session, use the direct dispatch pattern described in the "Per-Component Steps" section below — dispatch pixy-planner, pixy-implementor, and pixy-reviewer yourself without going through the pixy orchestrator.

---

## Pending Actions from Previous Session

1. **Create PR for PopDisplayText** — branch `feat/pop-display-text` is pushed but PR was not created (MCP tools were unavailable). Create PR targeting `main` before starting the next component.
2. **Send Telegram notification** for PopDisplayText completion.

## Context

Read these files first to understand the project:
- `CLAUDE.md` — Project rules, SOP, 7 design rules, build commands
- `AGENTS.md` — Component inventory, agent system, build order
- `.execution-history/phase-03-implementation.md` — Progress tracker (check what's already DONE)
- `.execution-history/resume-context.md` — If this file exists, read it FIRST. It contains pending actions from a previous session (e.g., PRs not yet created). Complete those actions before starting new components, then delete the file.

## MCP Tool Discovery (Important)

MCP tools (GitHub, Stitch, Telegram) are **deferred** — their schemas must be loaded before calling them. At session start, run these ToolSearch calls to activate them:

```
ToolSearch(query="select:mcp__plugin_github_github__create_pull_request", max_results=1)
ToolSearch(query="select:mcp__plugin_telegram_telegram__reply", max_results=1)
ToolSearch(query="select:mcp__stitch__list_screens", max_results=1)
```

Without this step, MCP tool calls will fail with "No such tool available" even though the servers are connected.

## Your Job

Build components **one at a time**, in wave order. Check the progress tracker to find the **next PENDING component** and start from there.

### Per-Component Steps

For each component, run these three sub-agents **in sequence** (wait for each to finish before starting the next):

**Step 1 — Plan:**
```
Agent(subagent_type="pixy-planner", prompt="""
  Component: {NAME}, Tier: {foundation/composite/chart}
  Variants: {list from AGENTS.md}
  Key notes: {notes from AGENTS.md}
  [7 design rules from CLAUDE.md]
  [Theme API: colors, typography, spacing, shapes]
  Stitch project ID: 7983075619754946215
  For composites: paste actual code of foundation dependencies
""")
```

**Step 2 — Implement** (using the full plan from Step 1):
```
Agent(subagent_type="pixy-implementor", prompt="""
  [Full plan from Step 1]
  Branch: create feat/pop-{name} from current HEAD (feat/pop-{previous})
  Build: ./gradlew :library:desktopTest
  Screenshots: ./gradlew :library:recordRoborazziDesktop then verifyRoborazziDesktop
  Commit format: feat(foundation): add {ComponentName} with variants
  No Co-Authored-By or AI attribution in commits
""")
```

**Step 3 — Review** (using the plan as spec):
```
Agent(subagent_type="pixy-reviewer", prompt="""
  [Plan from Step 1 as the spec]
  Files created: [list from Step 2]
  [7 design rules]
  Stitch project ID: 7983075619754946215 — fetch BOTH light and dark screens
  NOTE: crop Stitch images >4000px before reading
""")
```

**Step 4 — Fix loop** (if reviewer returns ISSUES_FOUND):
- Re-dispatch pixy-implementor with the fix list
- Re-dispatch pixy-reviewer. Max 3 iterations — stop and report if still failing.

**Step 5 — After APPROVED:**
- Push branch and create PR via GitHub MCP
  - owner: `tanaykumarbera`, repo: `compose-electric-pop`
  - Check base branch still exists: `git ls-remote --heads origin feat/pop-{previous}`
  - If deleted (merged), use `main` as base
- Update `.execution-history/phase-03-implementation.md` — mark component DONE
- Branch next component off this branch: `git checkout -b feat/pop-{next}`

---

## Build Order

Check `.execution-history/phase-03-implementation.md` for current status. Resume from the first PENDING component.

### Wave 1: Core Foundation — DONE
| # | Component | Status |
|---|-----------|--------|
| 1 | PopPill | DONE |
| 2 | PopIcon | DONE |
| 3 | PopSurface | DONE |
| 4 | PopBadge | DONE |

### Wave 2: Input Foundation — DONE
| # | Component | Status |
|---|-----------|--------|
| 5 | PopButton | DONE |
| 6 | PopTextField | DONE |
| 7 | PopSwitch | DONE |
| 8 | PopSlider | DONE |
| 9 | PopRadioGroup | DONE |
| 10 | PopChip | DONE |
| 11 | PopDropdown | DONE |

### Wave 3: Layout Foundation — DONE
| # | Component | Tier | Status |
|---|-----------|------|--------|
| 12 | PopIconRow | foundation | DONE |
| 13 | PopSectionHeader | foundation | DONE — redesigned per Stitch (PR #14) |
| 14 | PopTitleBar | foundation | DONE — redesigned per Stitch (PR #14) |
| 15 | PopDisplayText | foundation | DONE |
| 16 | PopIconListItem | foundation | DONE |
| 17 | PopStepList | foundation | DONE |
| 18 | PopTable | foundation | DONE |
| 19 | PopCodeBlock | foundation | DONE — PR #21 merged |
| 20 | PopBottomBar | foundation | DONE — PR #20 merged |

### Wave 4: Composites — IN PROGRESS (1 remaining)
| # | Component | Tier | Composes | Status |
|---|-----------|------|----------|--------|
| 21 | PopDataRow | composite | PopIcon, PopDisplayText | DONE — PR #22 merged |
| 22 | PopBannerCard (enhanced) | composite | PopBadge, PopDisplayText, PopIcon, PopSurface | DONE — PR #23 merged; renamed from PopMetricCard 2026-04-18 |
| 23 | PopFeatureCard | composite | PopDisplayText, PopBadge, PopIconRow, PopSurface | DONE — PR merged |
| 24 | PopDashboardCard | composite | PopSectionHeader, PopPill, PopDataRow, PopSurface | DONE — PR merged |
| 25 | PopCarouselCard | composite | PopIcon, PopChip, PopDisplayText, PopSurface | DONE — PR merged |
| 26 | PopActionCard | composite | PopButton, PopChip, PopDisplayText (slot-based) | DONE — PR #28 open |
| **27** | **PopImageBannerCard** | **composite** | **PopSurface + Image overlay** | **⬅ NEXT** |

### Wave 5: Charts (3 components)
| # | Component | Tier | Notes |
|---|-----------|------|-------|
| 28 | PopLineChart | chart | Trend line, glow on active points |
| 29 | PopBarChart | chart | Comparative bars, active scales 1.02x |
| 30 | PopDonutChart | chart | Circular gauge, center text |

---

## What Each Sub-Agent Does

| Agent | Model | Role |
|-------|-------|------|
| pixy-planner | opus | Creates implementation plan, fetches Stitch designs |
| pixy-implementor | sonnet | Writes component, unit tests, screenshot tests, demo; runs builds; commits |
| pixy-reviewer | opus | Validates against plan, design rules, test quality, Stitch visual comparison |

Each component produces:
- `library/src/commonMain/.../foundation/{Component}.kt`
- `library/src/commonTest/.../foundation/{Component}Test.kt`
- `library/src/desktopTest/.../foundation/{Component}ScreenshotTest.kt`
- `library/src/desktopTest/snapshots/{Component}_*.png` — light + dark goldens
- `demo/.../components/{Component}Demo.kt`
- Updated `CatalogScreen.kt`

---

## Rules

- **One component at a time** — wait for all three agents to finish before starting the next
- **Branch per component** — each component gets `feat/pop-{name}` branch chained off the previous
- **Never skip screenshot tests** — every component needs light + dark goldens
- **Stop at wave boundaries** — report progress and let user decide whether to continue
- **If a component fails after 3 reviewer iterations** — stop and report, don't loop
- **Clean working tree** — after each component, `git status` must be clean
- **No AI attribution** — no Co-Authored-By or tool branding in commits
- **PopIcons for icons** — use `PopIcons.*`, never add material-icons-core
