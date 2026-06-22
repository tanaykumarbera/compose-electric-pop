# Electric Pop — Design Tokens

Read every value from `MaterialTheme` (or `ElectricPopTheme.spacing`). Never hardcode the
raw values below — they're listed so you understand the system and can override it.

## Theme architecture

```kotlin
ElectricPopTheme(
    colorScheme: ElectricPopColors = ElectricPopColors.light(),
    typography: ElectricPopTypography = ElectricPopTypography.default(),
    shapes: ElectricPopShapes = ElectricPopShapes.default(),
    spacing: ElectricPopSpacing = ElectricPopSpacing.default(),
) { content() }
```

- Wraps Material 3 `MaterialTheme` internally.
- `ElectricPopColors` → `MaterialTheme.colorScheme` (50+ tokens).
- `ElectricPopTypography` → `MaterialTheme.typography` (Space Grotesk + Manrope).
- `ElectricPopShapes` → `MaterialTheme.shapes` (squircles).
- `ElectricPopSpacing` → `ElectricPopTheme.spacing` (a `CompositionLocal`; Material has no
  spacing system).
- Components always read from `MaterialTheme`, so overriding any layer makes every `Pop*`
  component follow. `ElectricPopTheme(darkTheme = true) { … }` selects the dark scheme.

## Color tokens

Seed palette: **Electric Lime, Neon Magenta, Cyber Cyan**, over a 7-step neutral surface ramp.

### Light scheme

| Token family | Key values |
|---|---|
| Primary (Electric Lime) | base `#4e6300` · container `#cafd00` · onContainer `#e1ff88` |
| Secondary (Neon Magenta) | base `#a400a4` · container `#ffbdf3` · onContainer `#ffeef8` |
| Tertiary (Cyber Cyan) | base `#006666` · container `#00ffff` · onContainer `#bbfffe` |
| Surface ramp (7 levels) | bright `#f5f6f7` → highest `#dadddf` |
| Error | base `#b02500` · container `#f95630` |
| Outline | base `#757778` · variant `#abadae` |

The dark scheme is hand-tuned per screen (not an algorithmic inversion). Always verify
dark rendering visually, e.g. against the published dark screenshots.

Usage: `MaterialTheme.colorScheme.primaryContainer`, `.onSurfaceVariant`,
`.surfaceContainerLow`, etc.

## Typography tokens

Two bundled families: Space Grotesk (impact), Manrope (body).

| Role | Font | Weight | Style | Tracking |
|---|---|---|---|---|
| Display / Headline | Space Grotesk | Black (900) | Uppercase Italic | -0.02em |
| Body | Manrope | Regular (400) | Normal | 0 |
| Label / Metadata | Manrope | ExtraBold (800) | Uppercase | +0.05em |

Usage: `MaterialTheme.typography.displayMedium`, `.bodyLarge`, `.labelSmall`, etc.
`ElectricPopTypography()` is a composable function (fonts are Compose resources).

## Shape tokens

All squircles (continuous curvature).

| Token | Radius | Usage | Access |
|---|---|---|---|
| `full` | pill | Buttons, pills, badges | `PopShapeFull` |
| `xl` | 48dp | Main containers, hero cards | `MaterialTheme.shapes.extraLarge` |
| `lg` | 32dp | Secondary containers | `MaterialTheme.shapes.large` |
| `md` | 16dp | Inputs, code blocks | `MaterialTheme.shapes.medium` |

## Spacing tokens

8dp base scale, via `ElectricPopTheme.spacing`.

| Token | dp | Common use |
|---|---|---|
| `xs` | 4 | Inline gaps in compact components |
| `sm` | 8 | Default item gap, icon padding |
| `md` | 12 | Section internal padding |
| `lg` | 16 | Card content padding |
| `xl` | 24 | Card-to-card gaps |
| `xxl` | 32 | Section-to-section separation |
| `huge` | 48 | Page hero spacing |
| `massive` | 64 | Layout-level breathing room |

Usage: `Modifier.padding(ElectricPopTheme.spacing.lg)`,
`Arrangement.spacedBy(ElectricPopTheme.spacing.sm)`.
