# Electric Pop — Design System

The "Kinetic Pulse" design language behind every Electric Pop component. This document is the load-bearing reference: design rules every component enforces, and the token vocabulary they read from.

For component-by-component API and screenshots, see the published reference site: <https://tanaykumarbera.github.io/compose-electric-pop/api/>.

---

## Philosophy

Electric Pop is a high-energy, editorial design system targeting Gen-Z audiences. It leans on saturated color, bold typography, and kinetic interactions — the opposite of restrained corporate UI. The aesthetic is built around three commitments:

- **Tonal hierarchy over lines.** Surfaces separate by lightness shifts and spacing, not 1px borders.
- **Color as light.** Primary CTAs emit a soft glow; shadows match their surface tinted darker, never neutral grey.
- **Type as object.** Headlines are uppercase, italic, black-weight — they sit in the layout like physical objects, not labels.

The design source of truth is the [Stitch project](https://stitch.withgoogle.com/projects/7983075619754946215). Components are extracted from those screens; this document captures the rules and tokens distilled out of them.

---

## The 7 Design Rules

Every component enforces these. Violation is a review rejection.

1. **No-Line Rule.** 1px borders are strictly prohibited. Separation comes from tonal surface shifts (`surfaceContainer` → `surfaceContainerLow`) and spacing in 8–12dp increments. If you reach for a `Border`, you're solving the wrong problem.

2. **Tonal Shadows.** Shadow color matches the background surface, 10% darker, 32px blur, 0px offset. No standard grey drop shadows — they read as Material defaults, not Electric Pop. Implemented via `Modifier.shadow()` with a tinted color.

3. **Ghost Border Fallback.** The only sanctioned stroke is for accessibility: `outlineVariant` at 15% opacity. Use it sparingly, only where contrast cannot be reached via tonal shift alone.

4. **Neon Glow.** Primary CTAs emit a 15–20% opacity spread of their base color to simulate neon emission. Implemented via `Modifier.shadow()` with the primary color (not black). Secondary and ghost variants do not glow.

5. **Kinetic Interactions.** Hover scales the affordance to **1.05×** with a 200ms ease-in-out. Active state compresses to **0.95×** for tactile feedback. Every interactive component uses these exact values — consistency across the system reads as a single deliberate physics, not arbitrary motion.

6. **Squircle Radii.** All rounded corners use continuous curvature (squircles), never geometric arcs. Compose's built-in `RoundedCornerShape` is geometric — Electric Pop ships shapes backed by the [`squircle-shape`](https://github.com/StoyanVuchev/SquircleShape) library (`sv.lib.squircleshape.SquircleShape`). Always read shapes from `MaterialTheme.shapes` or `PopShapeFull`.

7. **Typography Impact.** Headlines are uppercase, italic, black-weight (900), with tight tracking (-0.02em). Display text dwarfs body text — visual hierarchy is enforced by scale, not weight nuance. Use `.uppercase()` on headline strings; never bake casing into the data layer.

---

## Theme Architecture

```kotlin
ElectricPopTheme(
    colorScheme: ElectricPopColors = ElectricPopColors.light(),
    typography: ElectricPopTypography = ElectricPopTypography.default(),
    shapes: ElectricPopShapes = ElectricPopShapes.default(),
    spacing: ElectricPopSpacing = ElectricPopSpacing.default(),
) { content() }
```

- Wraps Material3 `MaterialTheme` internally.
- `ElectricPopColors` maps to `MaterialTheme.colorScheme` (50+ named tokens).
- `ElectricPopTypography` maps to `MaterialTheme.typography` (Space Grotesk headlines, Manrope body).
- `ElectricPopShapes` maps to `MaterialTheme.shapes` (squircle radii at four steps).
- `ElectricPopSpacing` is a custom `CompositionLocal` — Material3 has no spacing system, so Electric Pop adds one.
- Components always read from `MaterialTheme` (never from `ElectricPopColors` directly), so consumers can override any layer and the components follow.

---

## Color Tokens

The seed palette is three saturated hues — Electric Lime, Neon Magenta, Cyber Cyan — backed by a 7-step neutral surface ramp.

### Light scheme

| Token family | Key values |
|---|---|
| Primary (Electric Lime) | base `#4e6300` · container `#cafd00` · onContainer `#e1ff88` |
| Secondary (Neon Magenta) | base `#a400a4` · container `#ffbdf3` · onContainer `#ffeef8` |
| Tertiary (Cyber Cyan) | base `#006666` · container `#00ffff` · onContainer `#bbfffe` |
| Surface ramp (7 levels) | bright `#f5f6f7` → highest `#dadddf` |
| Error | base `#b02500` · container `#f95630` |
| Outline | base `#757778` · variant `#abadae` |

### Dark scheme

The dark palette is **not** an algorithmic inversion of light. It is extracted screen-by-screen from the Stitch dark variants ("Hero Pulse Card Doc (Dark)", "Action Buttons Doc (Dark)", etc.) because those screens contain manual contrast and vibrancy overrides Material3's auto-generation does not produce. Always verify dark-theme rendering against the matching dark Stitch screen, not by toggling `isSystemInDarkTheme()` and trusting the result.

---

## Typography Tokens

Two families ship bundled as Compose resources — Space Grotesk for impact, Manrope for body.

| Role | Font | Weight | Style | Tracking |
|---|---|---|---|---|
| Display / Headline | Space Grotesk | Black (900) | Uppercase Italic | -0.02em |
| Body | Manrope | Regular (400) | Normal | 0 |
| Label / Metadata | Manrope | ExtraBold (800) | Uppercase | +0.05em |

`ElectricPopTypography()` is a composable function (not a `val`) because the fonts are loaded as Compose resources and need a recomposition scope. Pass it through `ElectricPopTheme(typography = ...)` if you need overrides.

---

## Shape Tokens

All four steps are squircles (continuous curvature), not rounded rectangles.

| Token | Radius | Usage |
|---|---|---|
| `full` | 9999px (pill) | Buttons, pills, badges |
| `xl` | 48dp (3rem) | Main containers, hero cards |
| `lg` | 32dp (2rem) | Secondary containers |
| `md` | 16dp (1rem) | Inputs, code blocks |

Read from `MaterialTheme.shapes` (which Electric Pop wires to `ElectricPopShapes`) or use the `PopShapeFull` constant for pill-shaped surfaces.

---

## Spacing Tokens

8dp base scale, accessed via `ElectricPopTheme.spacing` (a `CompositionLocal`).

| Token | dp | Common use |
|---|---|---|
| `xs` | 4 | Inline gaps inside compact components |
| `sm` | 8 | Default item gap, icon padding |
| `md` | 12 | Section internal padding |
| `lg` | 16 | Card content padding |
| `xl` | 24 | Card-to-card gaps |
| `xxl` | 32 | Section-to-section separation |
| `huge` | 48 | Page hero spacing |
| `massive` | 64 | Layout-level breathing room |

Hardcoded `dp` values in components are a smell — they should resolve to one of these tokens.

---

## Implementation Notes

- **Never hardcode hex.** Read every color from `MaterialTheme.colorScheme.*`. Components written against `ElectricPopColors` directly break consumer overrides.
- **Never hardcode `TextStyle`.** Read from `MaterialTheme.typography.*`. Apply `.copy(textTransform = ...)` only for the uppercase rule, never for weight or family changes.
- **Composites compose foundation.** A composite component (e.g. `PopBannerCard`) renders its sub-pieces via `PopButton`, `PopBadge`, etc. — never by reimplementing them. This is what makes the design rules transitive.
- **Icons ship with the library.** Use `PopIcons.*` (a curated `ImageVector` set: Star, Check, Close, Info, Warning, Heart, Home, Search, Settings, Add, ArrowUp, ArrowDown, ArrowBack, ArrowForward, Person, TrendUp, TrendDown, Bolt, Sparkle, CheckCircle, Layers, Puzzle, Tokens, Menu). Never add `material-icons-core` as a dependency.
