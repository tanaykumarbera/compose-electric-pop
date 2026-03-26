# Electric Pop — Agent Guide

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

## Agent Hierarchy

### Pixy (Orchestrator) — model: opus

The brain. Takes a component name, orchestrates the full build cycle. **Does NOT write code.**

#### How to Invoke Pixy

```
Agent(
    subagent_type="general-purpose",
    model="opus",
    prompt="""
You are Pixy, the Electric Pop orchestrator agent.

## Your Task
Build the {COMPONENT_NAME} component for the Electric Pop UI library.

## Context
- Read CLAUDE.md for project rules and SOP
- Read the spec at docs/superpowers/specs/2026-03-25-electric-pop-design.md
- Component details: {PASTE COMPONENT ROW FROM TABLE ABOVE}
- Stitch design reference: https://stitch.withgoogle.com/projects/7983075619754946215
  - Use mcp__stitch__get_screen to fetch the relevant screen for visual details

## Your Workflow
You are an orchestrator. You DO NOT write code. You delegate to subagents.

### Step 1: Plan
Dispatch a Planner agent (model: opus) to create a detailed implementation plan.
The planner must:
- Fetch the Stitch design screen(s) for this component (light + dark variants)
- Define exact file paths, function signatures, parameters
- List all variants and states
- Include test cases and demo page specification
- Return a complete plan as text

### Step 2: Implement
Dispatch an Implementor agent (model: sonnet) with the plan from Step 1.
The implementor must:
- Create the component file in library/src/commonMain/kotlin/com/electricpop/{tier}/
- Create the test file in library/src/commonTest/kotlin/com/electricpop/{tier}/
- Create the demo page in demo/src/commonMain/kotlin/com/electricpop/demo/components/
- Update CatalogScreen.kt to register the component
- Run ./gradlew :library:desktopTest to verify tests pass
- Run ./gradlew :library:compileKotlinDesktop :demo:compileKotlinDesktop to verify builds
- Commit with message: feat({tier}): add {ComponentName} with variants
- Report: DONE | DONE_WITH_CONCERNS | BLOCKED | NEEDS_CONTEXT

### Step 3: Review
Dispatch a Reviewer agent (model: opus) to review the implementation.
The reviewer must check:
- All variants implemented per spec
- 7 design rules followed (no hardcoded colors, no borders, squircle shapes, etc.)
- Tests cover all variants
- Demo page shows all variants
- Light + dark theme both work (check that colors come from MaterialTheme)
- No code duplication (composites must use foundation components)
Return: APPROVED | ISSUES_FOUND with specific fixes needed

### Step 4: Fix Loop (if needed)
If reviewer found issues:
- Dispatch Implementor with specific fix instructions
- Re-dispatch Reviewer to verify fixes
- Max 3 iterations. If still failing, STOP and report to human.

### Step 5: Summary
After approval, report:
- Component name and tier
- Files created/modified
- Test results
- Any concerns or notes for the human

## Error Handling
- If Implementor reports BLOCKED: assess and either provide context or escalate to human
- If Implementor reports NEEDS_CONTEXT: fetch the needed info and re-dispatch
- If same error appears twice: STOP, do not loop blindly
- If Reviewer rejects 3 times: STOP, summarize issues for human

## IMPORTANT
- Never write code yourself — always delegate
- Never skip the review step
- Always fetch Stitch designs for visual reference
- All colors must come from MaterialTheme.colorScheme, all typography from MaterialTheme.typography
- Shapes from MaterialTheme.shapes or PopShapeFull
- Spacing from ElectricPopTheme.spacing
"""
)
```

### Planner — model: opus

Creates detailed step-by-step implementation plans for a single component.

**Input:** Component name, spec reference, Stitch screen data
**Output:** Complete plan with file paths, code structure, test cases, demo spec
**Must include:** All variants, light + dark theme support, demo page, tests

### Implementor — model: sonnet

Executes implementation plans. Writes code, runs tests, fixes build errors.

**Input:** Plan from Planner
**Output:** Working code committed to branch
**Rules:**
- Follow CLAUDE.md SOP exactly
- Run tests after each step
- Stop if same error appears twice
- Report status: DONE | DONE_WITH_CONCERNS | BLOCKED | NEEDS_CONTEXT

### Reviewer — model: opus

Reviews implementation against plan and design spec.

**Input:** Implementation diff, plan, design spec
**Output:** APPROVED | ISSUES_FOUND (with specific fixes)
**Checks:**
- All variants implemented
- 7 design rules followed
- Tests cover all variants and states
- Demo page shows all variants
- Light + dark theme both work
- No hardcoded colors/sizes — must use theme tokens
- Composites must compose from foundation components

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
