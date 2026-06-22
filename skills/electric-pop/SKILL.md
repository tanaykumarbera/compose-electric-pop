---
name: electric-pop
description: >
  Build UIs with the Electric Pop Compose Multiplatform component library and its
  "Kinetic Pulse" design system. Use when adding the co.tanay:compose-electric-pop
  dependency, theming with ElectricPopTheme, choosing a Pop* component, or applying
  Electric Pop's design rules (neon glow, squircle radii, no-line tonal separation,
  impact typography). Targets Android, iOS, and Desktop (JVM).
metadata:
  tags: [compose, compose-multiplatform, kotlin, design-system, ui-library, android, ios]
  homepage: https://tanaykumarbera.github.io/compose-electric-pop/
  source: https://github.com/tanaykumarbera/compose-electric-pop
---

# Electric Pop

Electric Pop is a Compose Multiplatform UI library implementing the **Kinetic Pulse**
design system: saturated color, impact typography, squircle corners, neon glow, and
tonal (line-free) separation. Use this skill when building Compose UI that should adopt
Electric Pop instead of plain Material 3.

## When to use

- **Use** for Kotlin **Compose Multiplatform / Jetpack Compose** UI work (Android, iOS,
  Desktop JVM) that should look like Electric Pop.
- **Don't use** for non-Compose stacks (React, SwiftUI, XML Views) — the components are
  Kotlin `@Composable` functions only.

## Install

Add the dependency (check the latest version on
[Maven Central](https://central.sonatype.com/artifact/co.tanay/compose-electric-pop)):

```kotlin
// build.gradle.kts
dependencies {
    implementation("co.tanay:compose-electric-pop:0.0.1")
}
```

Requirements: `compileSdk 36` (the squircle shapes use the `squircle-shape` library,
which needs it). Fonts (Space Grotesk, Manrope) and icons (`PopIcons`) ship bundled — do
**not** add `material-icons-core`.

## Set up the theme

Wrap your app in `ElectricPopTheme`. Every `Pop*` component reads from `MaterialTheme`,
so theming flows automatically:

```kotlin
ElectricPopTheme {            // or ElectricPopTheme(darkTheme = true) { … }
    PopButton(text = "Get started", onClick = { /* … */ })
}
```

**Hard rules when writing UI with Electric Pop** (these mirror the library's own coding
rules — agents that break them produce off-system output):

- Read color from `MaterialTheme.colorScheme.*` — **never** hardcode hex.
- Read type from `MaterialTheme.typography.*` — **never** construct a raw `TextStyle`.
- Read shapes from `MaterialTheme.shapes` or `PopShapeFull` — never `RoundedCornerShape`.
- Use `ElectricPopTheme.spacing.{xs,sm,md,lg,xl,xxl,huge,massive}` — never hardcode `dp`.
- Prefer composing existing `Pop*` components over building new widgets by hand.

## The 7 design rules (summary)

1. **No-Line** — separate surfaces with tonal shifts + spacing, never 1px borders.
2. **Tonal Shadows** — shadows are the surface color darkened, 32px blur, 0 offset.
3. **Ghost Border** — only sanctioned stroke: `outlineVariant` @ 15% opacity, sparingly.
4. **Neon Glow** — primary CTAs emit a 15–20% glow of their own color (not black/grey).
5. **Kinetic Interactions** — hover scales 1.05×, press 0.95×, 200ms ease-in-out.
6. **Squircle Radii** — continuous-curvature corners, never geometric `RoundedCornerShape`.
7. **Typography Impact** — headlines uppercase + italic + black (900), tight tracking.

Full text with do/don't examples: [reference/design-rules.md](reference/design-rules.md).

## Pick a component

- **Foundation (20)** — buttons, fields, switches, sliders, chips, dropdowns, badges,
  surfaces, icons, tables, code blocks, list items, display text, bottom/title bars.
- **Composite (6)** — banner cards, dashboard cards, action cards, carousels, data rows
  (these compose foundation pieces; reuse them instead of assembling by hand).
- **Chart (1)** — `PopChart` with `PopChartStyle.Line`, `.Bar`, `.Donut`.

Full per-component summaries, key params, and preview links:
[reference/components.md](reference/components.md). Authoritative signatures, every
param, and inline light+dark screenshots:
<https://tanaykumarbera.github.io/compose-electric-pop/api/>.

## Icons

Use the bundled set: `PopIcons.Star`, `.Check`, `.Close`, `.Info`, `.Warning`, `.Heart`,
`.Home`, `.Search`, `.Settings`, `.Add`, `.ArrowUp/Down/Back/Forward`, `.Person`,
`.TrendUp/Down`, `.Bolt`, `.Sparkle`, `.CheckCircle`, `.Layers`, `.Puzzle`, `.Tokens`,
`.Menu`. `PopIcon(imageVector = …)` accepts any `ImageVector` if you need more.

## Reference

Load these only when you need the detail:

- **[reference/design-rules.md](reference/design-rules.md)** — the 7 rules in full, with do/don't snippets.
- **[reference/tokens.md](reference/tokens.md)** — color, typography, shape, spacing tokens + the theme override API.
- **[reference/components.md](reference/components.md)** — generated cheat-sheet of every component (summary, key params, preview links).
- **[reference/recipes.md](reference/recipes.md)** — copy-paste screens (dashboard, form, metric row).
- **Live API docs** — <https://tanaykumarbera.github.io/compose-electric-pop/api/>
