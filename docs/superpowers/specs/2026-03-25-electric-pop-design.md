# Electric Pop UI Library — Design Specification

## 1. Overview

Electric Pop is a Kotlin Multiplatform UI component library built with Compose Multiplatform. It provides a high-energy, editorial design system inspired by the "Kinetic Pulse" aesthetic — targeting Gen-Z audiences with saturated color, bold typography, and kinetic interactions.

The library ships as a **single Gradle module** with package-level separation into foundation components, composite UX items, and data visualization charts. R8/ProGuard handles tree-shaking of unused components at consumer build time.

**Design source:** [Stitch project 7983075619754946215](https://stitch.withgoogle.com/projects/7983075619754946215)

---

## 2. Targets & Constraints

| Parameter | Value |
|---|---|
| Platforms | Android, iOS (arm64 + simulatorArm64), Desktop (JVM) |
| Min Android API | 24 (Nougat) |
| UI Framework | Compose Multiplatform |
| Build System | Gradle with version catalogs |
| Publishing | Maven Central (via vanniktech/gradle-maven-publish-plugin) |
| Module Structure | Single module `electric-pop`, package separation |
| Theming | Customizable via MaterialTheme-style API, Electric Pop as default |

---

## 3. Package Structure

```
com.electricpop/
  ├── theme/          // ElectricPopTheme, Colors, Typography, Shapes, Spacing
  ├── foundation/     // 20 basic components
  ├── composite/      // 7 composite UX components
  └── chart/          // 3 data visualization components
```

Separate `demo/` module for the showcase app (not published).

---

## 4. Theme System

### 4.1 Architecture

```kotlin
ElectricPopTheme(
    colorScheme: ElectricPopColors = ElectricPopColors.light(),
    typography: ElectricPopTypography = ElectricPopTypography.default(),
    shapes: ElectricPopShapes = ElectricPopShapes.default(),
    spacing: ElectricPopSpacing = ElectricPopSpacing.default()
) { content() }
```

- Wraps Material3 `MaterialTheme` internally
- `ElectricPopColors` maps to `MaterialTheme.colorScheme` (50+ named tokens)
- `ElectricPopTypography` maps to `MaterialTheme.typography` (Space Grotesk headlines, Manrope body)
- `ElectricPopShapes` maps to `MaterialTheme.shapes` (squircle radii: full/xl/lg/md)
- `ElectricPopSpacing` is a custom `CompositionLocal` (8dp base scale, Material3 has no spacing system)
- Consumers can override any layer; components read from `MaterialTheme` so custom themes work automatically

### 4.2 Color Palette (from Stitch)

**Light scheme — 50+ tokens including:**

| Token Family | Key Values |
|---|---|
| Primary (Electric Lime) | `#4e6300` / container: `#cafd00` / on: `#e1ff88` |
| Secondary (Neon Magenta) | `#a400a4` / container: `#ffbdf3` / on: `#ffeef8` |
| Tertiary (Cyber Cyan) | `#006666` / container: `#00ffff` / on: `#bbfffe` |
| Surface hierarchy | `#f5f6f7` (bright) → `#dadddf` (highest), 7 tonal levels |
| Error | `#b02500` / container: `#f95630` |
| Outline | `#757778` / variant: `#abadae` |

**Dark scheme:** Derived from the same seed colors but must be verified against the Stitch designs screen-by-screen. Each component doc has a dedicated dark variant (e.g., "Hero Pulse Card Doc (Dark)", "Action Buttons Doc (Dark)"). During implementation, the dark palette must be extracted from these dark screens rather than relying solely on Material3's auto-generated dark scheme — Stitch may have manual overrides for contrast or vibrancy that differ from algorithmic inversion.

### 4.3 Typography

| Role | Font | Weight | Style | Tracking |
|---|---|---|---|---|
| Display/Headline | Space Grotesk | Black (900) | Uppercase Italic | -0.02em |
| Body | Manrope | Regular (400) | Normal | 0 |
| Label/Metadata | Manrope | ExtraBold | Uppercase | +0.05em |

Fonts bundled as resources in the library.

### 4.4 Shapes

| Token | Radius | Usage |
|---|---|---|
| `full` | 9999px (pill) | Buttons, pills, badges |
| `xl` | 3rem (48px) | Main containers, cards |
| `lg` | 2rem (32px) | Secondary containers |
| `md` | 1rem (16px) | Inputs, code blocks |

All shapes use continuous curvature (squircle), not geometric rounded rectangles. **Implementation note:** Compose's built-in `RoundedCornerShape` uses geometric arcs. A custom `Shape` implementation or a third-party library (e.g., `smoothCorners` / `squircle-shape`) is required to achieve true continuous curvature. This must be evaluated during Phase 01 repo setup to choose the right approach before any component work begins.

### 4.5 Spacing

8dp base scale (design spacing scale = 3): 4, 8, 12, 16, 24, 32, 48, 64dp.

---

## 5. Design Rules (Enforced by Components)

These rules are baked into every component. They are not optional.

1. **No-Line Rule** — 1px borders are strictly prohibited. Separation via tonal surface shifts (`surface` → `surface_container_low`) and spacing (8-12dp increments).
2. **Tonal Shadows** — Shadow color matches background, 10% darker, 32px blur, 0 offset. No standard grey drop shadows.
3. **Ghost Border Fallback** — Where accessibility requires a stroke: `outline_variant` at 15% opacity only.
4. **Neon Glow** — Primary CTAs emit a 15-20% opacity spread of their base color to simulate neon emission.
5. **Kinetic Interactions** — Hover: scale 1.05x (200ms ease-in-out). Active: compress 0.95x for tactile feedback.
6. **Squircle Radii** — Continuous curvature on all rounded corners.
7. **Typography Impact** — Headlines are uppercase, italic, black weight, tight tracking. Display text dwarfs body text for visual hierarchy.

---

## 6. Component Inventory

### 6.1 Foundation (20 components)

| # | Component | Variants | Key Design Notes |
|---|---|---|---|
| 1 | **PopButton** | Primary, Secondary, Ghost &times; XL/Large/Small; Icon variant | Hover 1.05x, active 0.95x, primary gets neon glow |
| 2 | **PopTextField** | Standard, Password (visibility toggle), Error state | Label above, no bottom line — left accent bar on focus, error shifts to red |
| 3 | **PopRadioGroup** | Radio group with tonal shift | Tonal background grouping, no dividers, primary selection indicator |
| 4 | **PopSwitch** | On/Off toggle | Kinetic transition, secondary_container fill when active |
| 5 | **PopSlider** | Range slider with value display | 24px thumb, primary fill, primary_container border (4px) |
| 6 | **PopChip** | Category tags (primary/secondary/tertiary container colors) | Pill shape, full roundness |
| 7 | **PopIcon** | Material Symbols wrapper | Outlined, FILL:0, weight:400, opsz:24 |
| 8 | **PopSurface** | Themed surface container | Squircle radii, tonal hierarchy, no-line rule, tonal shadows |
| 9 | **PopBadge** | Directional badge with icon + value | Compact pill, backdrop-blur, semantic coloring (green/red) |
| 10 | **PopPill** | Label badges (Active, Locked, Live, etc.) | Small pill with colored background |
| 11 | **PopIconRow** | Dynamic icon cluster (1-N icons) | Horizontal row, evenly distributed, accepts list of icon references |
| 12 | **PopSectionHeader** | Accent label + title + accent line | Small uppercase label above main title, primary accent line, numbered variant |
| 13 | **PopTitleBar** | Title + inline PopPill | Headline font, uppercase italic + badge combo |
| 14 | **PopDisplayText** | Large emphasized text with optional fractional part | Space Grotesk Black Italic, directional coloring (green/red), split main + secondary sizing |
| 15 | **PopCodeBlock** | Pre-formatted code snippet | Monospace, surface-container bg, rounded corners (md), copy header |
| 16 | **PopIconListItem** | Icon + description text | check_circle/cancel/custom icon + body text, for guidelines/lists |
| 17 | **PopTable** | Colored rows with label + value | Alternating tonal rows, supports colored row variants |
| 18 | **PopStepList** | Numbered bullets with icons | 01/02/03 entries with icon symbols, headline label + body description |
| 19 | **PopBottomBar** | Icons only, Icons + text, with active highlight | Glassmorphic: surface_bright 70% opacity, backdrop-blur, active pill indicator, floating squircle |
| 20 | **PopDropdown** | Selector with expand icon | Label + dropdown affordance, primary accent on selection |

### 6.2 Composite (6 components)

> **Revised 2026-04-18 — see spec `2026-04-18-banner-card-refactor`.**
> **Revised 2026-04-25 — PopFeatureCard dropped; its hero-spotlight role is fulfilled by PopBannerCard (renamed from the earlier PopMetricCard in commit `92d5ec4`).**

| # | Component | Composed From | Description |
|---|---|---|---|
| 1 | **PopCarouselCard** | PopIcon, PopChip, PopDisplayText, PopSurface | Horizontal scroll card strip. Icon + timestamp + category + label + value delta. Cards bleed off viewport. |
| 2 | **PopDashboardCard** | PopSectionHeader, PopPill, PopDataRow, PopSurface | Data overview card. Tertiary gradient bg, status pills, icon + label + value data rows. |
| 3 | **PopDataRow** | PopIcon, PopDisplayText | Icon + title + category chip + value. Tonal shift separation (no borders). Directional value coloring. |
| 4 | **PopActionCard** | PopDropdown, PopDisplayText, PopButton, PopTextField | Card with input + actions. Recipient selector, dropdown, large input (7xl-9xl), action button group. |
| 5 | **PopBannerCard** | PopBadge, PopDisplayText, PopIcon, PopSurface | Metric banner: label, value, trend chip (italic black uppercase), overlapping icon cluster. Hero/Surface styles. Doubles as the hero-spotlight slot originally planned for PopFeatureCard. |
| 6 | **PopImageBannerCard** | PopSurface + Image overlay | Big image hero card with overlaid italic-black-uppercase headline. Configurable text anchor + scrim. |

### 6.3 Chart (3 components)

| # | Component | Description |
|---|---|---|
| 1 | **PopLineChart** | Trend line across time. Electric Lime primary, Cyber Cyan secondary. Active points emit glow shadow. |
| 2 | **PopBarChart** | Comparative bars. 24-48px stroke. Primary + tertiary. Active bar scales 1.02x. |
| 3 | **PopDonutChart** | Circular gauge. 110px radius. Primary for filled, surface for remaining. Center text. |

**Implementation note:** Chart specs are intentionally high-level. During implementation, implementors must reference the Stitch design screens ("Analytics & Charts with Sample Data" light + dark variants) for detailed axis labeling, grid treatment (no traditional gridlines per the design rules), animation curves, tooltip behavior, and data point interaction states.

### 6.4 Extensibility Note

This inventory covers the initial 30 components. Additional components may be identified through further review of the Stitch designs. The architecture supports adding new components at any tier without breaking changes.

---

## 7. Demo App

Separate `demo/` module (not published to Maven Central):

- Visual catalog of all 30 components with sample data
- Light/Dark theme toggle
- Per-component pages showing all variants and states
- Android, iOS, and Desktop targets
- Serves as both showcase and integration test

---

## 8. Project Phases

### Phase 01: Repo Setup
1. Init git repo at `git@github.com:tanaykumarbera/compose-electric-pop.git`
2. KMP project setup with latest stable tooling (Kotlin, Compose Multiplatform, AGP)
3. Library module + demo app module
4. Screenshot testing and unit test infrastructure
5. CI pipeline: lint, build, test, publish, release
6. GitHub Pages: home page with examples, wiki per component

### Phase 02: SOP & Agents
1. Detailed component creation instructions
2. Project-level skills for component creation, testing, commit messages, PRs
3. AGENTS.md with deep-dive project context
4. Agent hierarchy: Planner (Opus) → Implementor (Sonnet) → Reviewer (Opus) → Orchestrator "Pixy" (Opus)
5. Library code in library module, usage examples in demo app
6. Demo app as component catalog with theme switching

### Phase 03: Implementation
**Prerequisites:** Phase 01 and 02 must be fully complete. The repo must be in a state where the end-to-end workflow works: library builds, demo app builds, tests run, CI passes, and the Pixy agent pipeline is functional.

1. Theme system (light + dark) — extract both palettes from Stitch, verify dark scheme against dark variant screens
2. Foundation components (20) — each via Pixy agent workflow with PR per component
3. Composite components (7) — each via Pixy agent workflow with PR per component
4. Chart components (3) — each via Pixy agent workflow with PR per component
5. No manual component implementation — all component work flows through Pixy (plan → implement → review → fix loop)

### Phase 04: Execution Tracking
1. Plans written to `.execution-history/` directory
2. Step IDs and status markers ([PENDING] / [DONE])
3. Plans are editable with inline updates or appended references

---

## 9. Testing Strategy

- **Unit tests**: Component logic in `commonTest`
- **Compose UI tests**: Render and assert component structure
- **Screenshot tests**: Visual regression per component, per theme (light/dark), per platform
- **Demo app**: Integration testing via the catalog

---

## 10. Publishing

- **Artifact**: `com.electricpop:electric-pop`
- **Repository**: Maven Central
- **Plugin**: `vanniktech/gradle-maven-publish-plugin`
- **Versioning**: Semantic versioning (MAJOR.MINOR.PATCH)
- **Signing**: GPG for Maven Central
