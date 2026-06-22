# Electric Pop — The 7 Design Rules

Every Electric Pop component enforces these. When you write or compose UI with the
library, honor them too — they are what makes the output read as one deliberate system
rather than generic Material. Source of truth: the library's `DESIGN.md`.

---

## 1. No-Line Rule

1px borders are prohibited. Separate surfaces with **tonal shifts** (`surfaceContainer` →
`surfaceContainerLow`) and **spacing** in 8–12dp increments.

```kotlin
// Don't — a literal divider/border
Box(Modifier.border(1.dp, Color.Gray))

// Do — stack tonal surfaces with spacing between
Surface(color = MaterialTheme.colorScheme.surfaceContainer) { /* row */ }
Spacer(Modifier.height(ElectricPopTheme.spacing.sm))
Surface(color = MaterialTheme.colorScheme.surfaceContainerLow) { /* row */ }
```

## 2. Tonal Shadows

Shadow color matches the background surface, ~10% darker, **32px blur, 0 offset**. Never a
neutral grey drop shadow (that reads as a Material default).

```kotlin
Modifier.shadow(
    elevation = 24.dp,
    shape = MaterialTheme.shapes.large,
    ambientColor = surfaceTintedDarker,
    spotColor = surfaceTintedDarker,
)
```

## 3. Ghost Border Fallback

The only sanctioned stroke is for accessibility contrast: `outlineVariant` at **15%
opacity**. Use it sparingly, only where a tonal shift alone can't reach enough contrast.

```kotlin
Modifier.border(
    width = 1.dp,
    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.15f),
    shape = MaterialTheme.shapes.large,
)
```

## 4. Neon Glow

Primary CTAs emit a **15–20% opacity** spread of *their own* base color to simulate neon
emission. Secondary and ghost variants do **not** glow.

```kotlin
// Glow uses the primary color, never black
Modifier.shadow(
    elevation = 20.dp,
    shape = PopShapeFull,
    spotColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.20f),
)
```

## 5. Kinetic Interactions

Hover scales the affordance to **1.05×**; active/press compresses to **0.95×**; both with a
**200ms ease-in-out**. Use these exact values everywhere — consistency reads as one physics.

```kotlin
val scale by animateFloatAsState(
    targetValue = if (pressed) 0.95f else if (hovered) 1.05f else 1f,
    animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
)
Modifier.graphicsLayer { scaleX = scale; scaleY = scale }
```

Built-in `Pop*` components already do this — you get it for free by using them.

## 6. Squircle Radii

All rounded corners use **continuous curvature** (squircles), never geometric arcs.
Compose's `RoundedCornerShape` is geometric — don't use it. Read from
`MaterialTheme.shapes` (wired to squircles) or use the `PopShapeFull` pill constant.

```kotlin
// Don't
Modifier.clip(RoundedCornerShape(24.dp))

// Do
Modifier.clip(MaterialTheme.shapes.large)   // or PopShapeFull for pills
```

## 7. Typography Impact

Headlines are **uppercase, italic, black-weight (900)**, with tight tracking (-0.02em).
Display text dwarfs body text — hierarchy comes from scale, not weight nuance. Apply
`.uppercase()` at render time; never bake casing into your data.

```kotlin
Text(
    text = title.uppercase(),
    style = MaterialTheme.typography.displayMedium, // already Space Grotesk Black Italic
)
```

---

See [tokens.md](tokens.md) for the exact color/type/shape/spacing values these rules read.
