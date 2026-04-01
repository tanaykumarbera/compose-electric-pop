# Electric Pop — Phase 03: Component Implementation

You are orchestrating the build of Electric Pop UI components using the Pixy agent pipeline.

## Context

Read these files first to understand the project:
- `CLAUDE.md` — Project rules, SOP, 7 design rules, build commands
- `AGENTS.md` — Component inventory, agent system, build order
- `.execution-history/phase-03-implementation.md` — Progress tracker (check what's already DONE)

## Your Job

Build components **one at a time**, in wave order, using the Pixy agent pipeline. Check the progress tracker to find the **next PENDING component** and start from there.

### Per-Component Steps

For each component:

1. **Dispatch Pixy** as a foreground agent (wait for completion):
```
Agent(
    subagent_type="pixy",
    prompt="Build the {COMPONENT_NAME} component for Electric Pop.

Component details from inventory:
- Tier: {foundation/composite/chart}
- Variants: {copy from AGENTS.md component table}
- Key notes: {copy from AGENTS.md component table}
- Dependencies: {for composites, list foundation components}

Build order context: Wave {W}, component #{N}. Previously completed: {list of DONE components}."
)
```

**If `subagent_type="pixy"` is not available** (error: "Agent type 'pixy' not found"), STOP immediately and tell the user:
- The custom agents in `.claude/agents/` are not being discovered
- Ask them to verify the files exist: `ls .claude/agents/pixy*.md`
- Do NOT attempt a workaround or inline the agent prompts

2. **Check the result** — Pixy should report APPROVED with a summary. If it reports issues, assess whether to retry or stop.

3. **Update progress** — Mark the component as DONE in `.execution-history/phase-03-implementation.md`.

4. **Move to the next component** in wave order.

### Wave Boundaries

After completing each wave, **STOP** and report to the user:
- How many components were built
- Any concerns or patterns noticed

This lets the user decide whether to continue in this session or start fresh.

## Build Order

Check `.execution-history/phase-03-implementation.md` for current status. Resume from the first PENDING component.

### Wave 1: Core Foundation
| # | Component | Tier | Variants | Notes |
|---|-----------|------|----------|-------|
| 1 | PopPill | foundation | Color presets | DONE |
| 2 | PopIcon | foundation | Material Symbols wrapper | DONE |
| 3 | PopSurface | foundation | Themed container | Squircle, tonal shadow |
| 4 | PopBadge | foundation | Directional + value | Semantic green/red |

### Wave 2: Input Foundation (7 components)
| # | Component | Tier | Variants | Notes |
|---|-----------|------|----------|-------|
| 5 | PopButton | foundation | Primary, Secondary, Ghost x XL/Large/Small; Icon | Neon glow on primary, kinetic hover/active |
| 6 | PopTextField | foundation | Standard, Password, Error | Left accent bar on focus, no bottom line |
| 7 | PopSwitch | foundation | On/Off toggle | secondary_container fill |
| 8 | PopSlider | foundation | Range with value | 24px thumb |
| 9 | PopRadioGroup | foundation | Tonal shift radio options | No dividers |
| 10 | PopChip | foundation | Primary/Secondary/Tertiary container colors | Pill shape |
| 11 | PopDropdown | foundation | Selector + expand icon | Primary accent |

### Wave 3: Layout Foundation (9 components)
| # | Component | Tier | Variants | Notes |
|---|-----------|------|----------|-------|
| 12 | PopIconRow | foundation | Dynamic 1-N icons | Horizontal cluster |
| 13 | PopSectionHeader | foundation | Accent label + title + line | Numbered variant |
| 14 | PopTitleBar | foundation | Title + inline PopPill | Headline italic |
| 15 | PopDisplayText | foundation | Large text + fractional | Directional coloring |
| 16 | PopIconListItem | foundation | Icon + description | For guidelines/lists |
| 17 | PopStepList | foundation | Numbered items + icons | 01/02/03 entries |
| 18 | PopTable | foundation | Label-value rows | Alternating tonal, colored variants |
| 19 | PopCodeBlock | foundation | Pre-formatted code | Monospace, copy header |
| 20 | PopBottomBar | foundation | Icons, Icons+text, active | Glassmorphic, backdrop-blur |

### Wave 4: Composites (7 components)
| # | Component | Tier | Composes | Notes |
|---|-----------|------|----------|-------|
| 21 | PopDataRow | composite | PopIcon, PopDisplayText | Icon + label + value |
| 22 | PopMetricCard | composite | PopBadge, PopDisplayText, PopSurface | Metric display |
| 23 | PopFeatureCard | composite | PopDisplayText, PopBadge, PopIconRow, PopSurface | Hero spotlight card |
| 24 | PopDashboardCard | composite | PopSectionHeader, PopPill, PopDataRow, PopSurface | Data overview |
| 25 | PopCarouselCard | composite | PopIcon, PopChip, PopDisplayText, PopSurface | Horizontal scroll cards |
| 26 | PopActionCard | composite | PopDropdown, PopDisplayText, PopButton, PopTextField | Input + actions |
| 27 | PopBannerCard | composite | PopSurface + headline overlay | Big image + text |

### Wave 5: Charts (3 components)
| # | Component | Tier | Notes |
|---|-----------|------|-------|
| 28 | PopLineChart | chart | Trend line, glow on active points |
| 29 | PopBarChart | chart | Comparative bars, active scales 1.02x |
| 30 | PopDonutChart | chart | Circular gauge, center text |

## What Pixy Does Per Component

Pixy orchestrates three sub-agents:
1. **Planner** (opus) — Creates implementation plan, fetches Stitch designs
2. **Implementor** (sonnet) — Writes component, unit tests, screenshot tests, demo page; runs builds; commits
3. **Reviewer** (opus) — Validates against plan, design rules, test quality, screenshots, Stitch design comparison, and on-device vision check

Each component produces:
- `library/src/commonMain/.../foundation/{Component}.kt` — Component code
- `library/src/commonTest/.../foundation/{Component}Test.kt` — Unit tests
- `library/src/desktopTest/.../foundation/{Component}ScreenshotTest.kt` — Screenshot tests
- `library/src/desktopTest/snapshots/{Component}_*.png` — Golden images
- `demo/.../components/{Component}Demo.kt` — Demo page
- Updated `CatalogScreen.kt` — Registration

## Branching Strategy

Each component gets its own feature branch and PR:

1. **Before starting a component**, create a feature branch from the current HEAD:
   ```bash
   git checkout -b feat/pop-{component-name-kebab}
   ```
   The first component branches off `main`. Each subsequent component branches off the
   previous component's branch (not `main`), since PRs are merged in order.

2. **After Pixy completes and the component is committed**, push and create a PR:
   ```bash
   git push -u origin feat/pop-{component-name-kebab}
   ```
   Then create a PR using the GitHub MCP server (use whichever GitHub MCP tools are available):
   - **owner:** `tanaykumarbera`, **repo:** `compose-electric-pop`
   - **head:** `feat/pop-{component-name-kebab}`
   - **base:** `{previous-branch-or-main}`
   - **title/body:** component summary + test plan checklist

   **Before creating the PR**, verify the intended base branch still exists on the remote:
   ```bash
   git ls-remote --heads origin feat/pop-{previous-component}
   ```
   If it returns nothing, the branch was merged and deleted — use `main` as the base instead.

   If GitHub MCP is unavailable, skip PR creation — just push the branch.

3. **Then start the next component** by branching off this branch:
   ```bash
   git checkout -b feat/pop-{next-component-name-kebab}
   ```

This creates a chain: `main` ← `feat/pop-surface` ← `feat/pop-badge` ← ...
PRs are merged in order, so each PR's base is the previous component's branch.
**Note:** once a PR is merged, its branch is deleted from origin — so the next PR's base will appear to be missing. That's expected; use `main` in that case.

## Rules

- **One component at a time** — wait for Pixy to finish before starting the next
- **Branch per component** — each component gets `feat/pop-{name}` branch and a PR
- **Chain branches** — each new branch starts from the previous component's branch
- **Stop at wave boundaries** — report progress and let user decide
- **Never skip screenshot tests** — every component needs light + dark goldens
- **If Pixy fails twice on the same component** — stop and report to user, don't loop
- **Commit per component** — use the `git-commit` skill for clean commits with no AI branding
- **Clean working tree** — after each component, verify `git status` shows no leftover changes
- **Track progress** — update `.execution-history/phase-03-implementation.md` after each
- **PopIcons for icons** — use `PopIcons.*` (vector resources), never add material-icons-core
