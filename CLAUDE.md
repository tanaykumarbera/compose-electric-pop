# Electric Pop — Project Instructions

## Overview

Electric Pop is a Compose Multiplatform UI component library implementing the "Kinetic Pulse" design system. Single Gradle module, package-level separation.

- **Repo:** github.com/tanaykumarbera/compose-electric-pop
- **Design source:** [Stitch project 7983075619754946215](https://stitch.withgoogle.com/projects/7983075619754946215)
- **Spec:** `docs/superpowers/specs/2026-03-25-electric-pop-design.md`
- **Plan:** `docs/superpowers/plans/2026-03-25-electric-pop-implementation.md`
- **Targets:** Android (API 24+, compileSdk 36), iOS (arm64 + simulatorArm64), Desktop (JVM)
- **Stack:** Kotlin 2.3.20, Compose Multiplatform 1.10.3, AGP 8.7.3
- **Fonts:** Space Grotesk (headlines) + Manrope (body/labels) — bundled as Compose resources
- **Icons:** Built-in `PopIcons` object — no material-icons-core dependency

## Project Structure

```
library/src/commonMain/kotlin/com/electricpop/
├── theme/        → ElectricPopTheme, Color, Typography, Shape, Spacing
├── foundation/   → 20 basic components (PopButton, PopTextField, etc.)
├── composite/    → 7 composite components (PopFeatureCard, PopDataRow, etc.)
└── chart/        → 3 chart components (PopLineChart, PopBarChart, PopDonutChart)

demo/src/commonMain/kotlin/com/electricpop/demo/
├── App.kt           → Root app with theme toggle
├── CatalogScreen.kt → Component catalog list
└── components/      → Per-component demo pages
```

## Build Commands

```bash
./gradlew build                              # Build everything
./gradlew :library:build                     # Library only
./gradlew :demo:build                        # Demo only
./gradlew :library:allTests                  # All tests
./gradlew :library:desktopTest               # Desktop tests (fastest)
./gradlew :library:recordRoborazziDesktop    # Record golden screenshots
./gradlew :library:verifyRoborazziDesktop    # Verify screenshots match goldens
./gradlew :demo:run                          # Run desktop demo
```

## Component Creation SOP

Every component follows this exact workflow:

### 1. Understand
- Read spec section 6 for the component's variants and design notes
- Reference Stitch designs for visual details (light + dark screens)
- Check which foundation components a composite depends on

### 2. Create Component File
- Path: `library/src/commonMain/kotlin/com/electricpop/{tier}/{ComponentName}.kt`
- One file per component, one composable function per variant
- Package: `com.electricpop.{tier}` (foundation, composite, or chart)

### 3. Coding Rules
- **MUST** read colors from `MaterialTheme.colorScheme`, NEVER hardcode hex values
- **MUST** read typography from `MaterialTheme.typography`, NEVER hardcode TextStyles
- **MUST** use `ElectricPopTheme.spacing` for spacing values
- **MUST** use shapes from `MaterialTheme.shapes` or `PopShapeFull`
- **MUST** follow all 7 design rules (see below)
- Composites **MUST** compose from foundation components, not duplicate their code
- **MUST** use `PopIcons.*` for icons in demos/tests — NEVER add `material-icons-core` as a dependency
- Typography is loaded via `ElectricPopTypography()` (composable function, not a val) — fonts are bundled resources

### 4. Test
- Path: `library/src/commonTest/kotlin/com/electricpop/{tier}/{ComponentName}Test.kt`
- Compose UI tests (runComposeUiTest) are NOT available in commonTest — do NOT use them there
- Tests MUST exercise the component's actual code, NOT Kotlin stdlib
- For components with logic (parsing, formatting, state machines): unit-test that logic
- For purely visual components: write a minimal placeholder test documenting that visual validation is via demo app
- NEVER write tests that only call stdlib functions (e.g., String.uppercase()) — these will be rejected
- Run: `./gradlew :library:desktopTest`

### 4b. Screenshot Test
- Path: `library/src/desktopTest/kotlin/com/electricpop/{tier}/{ComponentName}ScreenshotTest.kt`
- Uses Roborazzi: `runDesktopComposeUiTest` + `captureRoboImage` (import `io.github.takahirom.roborazzi`)
- MUST capture light AND dark theme screenshots showing ALL variants
- Golden images: `library/src/desktopTest/snapshots/{ComponentName}_*.png`
- Record: `./gradlew :library:recordRoborazziDesktop`
- Verify: `./gradlew :library:verifyRoborazziDesktop`

### 5. Demo Page
- Path: `demo/src/commonMain/kotlin/com/electricpop/demo/components/{ComponentName}Demo.kt`
- Show ALL variants with sample data
- Register in `CatalogScreen.kt` → add entry to `catalogEntries` list
- Verify light + dark themes both render correctly

### 6. Branch, Commit, PR
- Branch from current HEAD: `git checkout -b feat/pop-{component-name-kebab}`
- Commit format: `feat(foundation): add PopComponentName with variants`
- No AI branding in commits (no Co-Authored-By, no tool attribution)
- One component per branch/PR
- Push the **feature branch** only — never push to `main`. Merging to main is the user's action via PR.
- Branch from the previous component's branch (for code continuity), but **all PRs target `main` directly**
- Never set a feature branch as a PR's base

## 7 Design Rules (Non-Negotiable)

Every component must enforce these. Violation = review rejection.

1. **No-Line Rule** — No 1px borders. Use tonal surface shifts (`surfaceContainer` → `surfaceContainerLow`) and spacing
2. **Tonal Shadows** — Shadow color matches background, 10% darker, 32px blur, 0 offset. No grey `elevation` shadows
3. **Ghost Border** — Only for accessibility: `outlineVariant` at 15% opacity
4. **Neon Glow** — Primary CTAs get 15-20% opacity color spread. Use `Modifier.shadow()` with primary color
5. **Kinetic Interactions** — Hover: `Modifier.scale(1.05f)` with 200ms ease. Active: `scale(0.95f)`
6. **Squircle Radii** — Use `ElectricPopShapes` (backed by squircle-shape library), not `RoundedCornerShape`
7. **Typography Impact** — Headlines: uppercase text via `.uppercase()`, use `headlineLarge` / `displayLarge` styles

## Component Inventory

### Foundation (20)
PopButton, PopTextField, PopRadioGroup, PopSwitch, PopSlider, PopChip, PopIcon, PopSurface, PopBadge, PopPill, PopIconRow, PopSectionHeader, PopTitleBar, PopDisplayText, PopCodeBlock, PopIconListItem, PopTable, PopStepList, PopBottomBar, PopDropdown

### Composite (7)
PopFeatureCard, PopCarouselCard, PopDashboardCard, PopDataRow, PopActionCard, PopBannerCard, PopMetricCard

### Chart (3)
PopLineChart, PopBarChart, PopDonutChart

## Key Dependencies

- `sv.lib.squircleshape.SquircleShape` — for squircle corner shapes (NOT `com.stoyanvuchev`). Requires compileSdk 36.
- `compose.material3` — Material3 theming
- `compose.foundation` — layout primitives
- `compose.ui` — core UI
- **NO** `material-icons-core` — library provides `PopIcons` (Star, Check, Close, Info, Warning, Heart, Home, Search, Settings, Add, ArrowUp, ArrowDown, ArrowBack, ArrowForward, Person, TrendUp, TrendDown, Bolt, Sparkle, CheckCircle, Layers, Puzzle, Tokens, Menu)

## Stitch Design Reference

When comparing implementations against Stitch designs:
- Stitch project ID: `7983075619754946215`
- Append `=s0` to Google Photos screenshot URLs for full resolution
- For tall images (>4000px), crop sections before viewing
- Always compare BOTH light and dark theme variants
- The reviewer agent fetches Stitch screenshots automatically during review

## MCP Tools (Important — read carefully)

GitHub and Stitch operations use MCP plugins — **never** use `gh` CLI (not installed), raw `curl`, or `claude` CLI commands for GitHub API.

- **GitHub:** `mcp__plugin_github_github__*` — PRs, issues, commits, code search.
- **Stitch:** `mcp__stitch__*` — design screens, project data.
- **Context7:** `mcp__plugin_context7_context7__*` — library/framework documentation lookup.

### How to use MCP tools

MCP tools are **deferred** — listed by name in `<system-reminder>` tags but their schemas must be loaded before you can call them. To load them:

1. Call the **`ToolSearch` tool** (it's a built-in tool, like `Read` or `Bash` — invoke it directly, NOT via the `claude` CLI)
2. Example: `ToolSearch(query="select:mcp__plugin_github_github__list_pull_requests", max_results=1)`
3. This returns the tool's full schema, after which you can call it normally

**Common mistakes to avoid:**
- Do NOT run `claude tools list`, `claude tools search`, `claude mcp list`, or any `claude` CLI subprocess — these do not work from within a session
- Do NOT use `Bash` to invoke MCP tools — call them directly as tool calls after loading via `ToolSearch`
- Do NOT fall back to `gh` CLI (not installed) or `curl` with GitHub API

## Preferences
- Single module library — R8 handles tree-shaking
- Generic component names (Pop* prefix, no domain-specific naming)
- Keep things simple — no over-engineering
- Escalate blockers after 2-3 failed attempts — don't loop endlessly
