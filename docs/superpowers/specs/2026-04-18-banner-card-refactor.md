# Banner Card Refactor — Design Spec

**Date:** 2026-04-18
**Author:** Brainstorm session
**Status:** Approved by user, pending implementation plan

## 1. Problem

The composite tier currently has two overlapping cards:

- **`PopBannerCard`** (current): a numeric-hero card with a big value, label, optional trend pill, and overlapping vitals icons. Visually similar to `PopMetricCard`, and drifted from the original spec which called for *"Big image card with overlaid text. Bold italic uppercase headline."*
- **`PopMetricCard`**: a labeled metric with directional badge, display value, and coin-stack icon cluster with `+N` overflow.

Three specific issues surfaced in review:

1. The two cards are nearly redundant. The original spec intent for `PopBannerCard` (hero image with text overlay) is missing from the library.
2. The current `PopMetricCard` has several styling choices that could be better: chip typography feels underweighted, and the chip + icon-cluster arrangement uses two stacked rows when a single row reads cleaner.
3. The Hero-style primary-container screenshots look nearly identical between light and dark themes because (a) the brand lime `primaryContainer` is intentionally identical in both palettes, and (b) Roborazzi captures the composable tree without a page background — making the "outside the card" area white in both modes, erasing the visual cue that a theme has switched.

## 2. Goals

1. **Restore the image-banner concept** via a new component `PopImageBannerCard`, matching the original spec intent.
2. **Consolidate** the current numeric `PopBannerCard` and `PopMetricCard` into a single component named `PopBannerCard` (keeping `PopMetricCard`'s API surface as the base and porting the chip/icon styling from the old `PopBannerCard`).
3. **Improve dark-mode visual parity** in screenshot goldens by painting a theme-aware page background behind the captured tree.
4. **Update all documentation and tracking files** so the component inventory and historical markers reflect the refactor.

## 3. Non-goals

- Changing the `primaryContainer` brand color to differ between light and dark modes — this is intentional and stays.
- Retroactively re-recording goldens for the ~15 existing components that don't paint a page background. That's a follow-up branch.
- Introducing a remote-image loading dependency in the library. Consumers who need remote images compose `rememberAsyncImagePainter` (Coil 3) or `asyncPainterResource` (Kamel) themselves — both return a `Painter` that plugs into the new API.

## 4. Architecture

Both banner-tier composites live in `co.tanay.electricpop.composite` and compose from existing foundation primitives. No new foundation component is needed.

```
composite/
├── PopBannerCard.kt          (renamed from PopMetricCard, enhanced)
└── PopImageBannerCard.kt     (new)
```

`PopBannerCard` keeps the same foundation dependencies as today's `PopMetricCard`: `PopBadge`, `PopDisplayText`, `PopIcon`, `PopSurface`. `PopImageBannerCard` uses `PopSurface` for the squircle clip/elevation and a raw `Image` for the media layer; no other foundation primitives.

## 5. Component: PopImageBannerCard (new)

### 5.1 Public API

```kotlin
enum class PopImageBannerTextAnchor {
    TopStart, TopCenter, TopEnd,
    CenterStart, Center, CenterEnd,
    BottomStart, BottomCenter, BottomEnd,
}

@Composable
fun PopImageBannerCard(
    painter: Painter,
    headline: String,
    modifier: Modifier = Modifier,
    eyebrow: String? = null,
    subtitle: String? = null,
    textAnchor: PopImageBannerTextAnchor = PopImageBannerTextAnchor.BottomStart,
    scrim: Boolean = true,
    scrimColor: Color = Color.Black.copy(alpha = 0.55f),
    contentColor: Color = Color.White,
    contentDescription: String? = null,
    onClick: (() -> Unit)? = null,
)
```

### 5.2 Layout

- Outer `Box` clipped to `MaterialTheme.shapes.extraSmall` (consistent with other composite cards).
- Sizing: the card preserves the painter's intrinsic aspect ratio via
  `Modifier.aspectRatio(painter.intrinsicSize.width / painter.intrinsicSize.height, matchHeightConstraintsFirst)`.
  The `matchHeightConstraintsFirst` flag is computed at composition time: `true` if the incoming height constraint is tighter than the width constraint, `false` otherwise. This makes the card adapt to whichever dimension the parent constrains, without cropping or distorting the image.
- `Image(painter, contentScale = ContentScale.Crop, modifier = Modifier.matchParentSize())` fills the box. Crop (rather than Fit) is chosen because the card box is already sized to the image's aspect — there's no letterboxing scenario in practice, and Crop gives clean edges if rounding produces a 1px mismatch.
- Scrim: when `scrim = true`, a `Box(Modifier.matchParentSize().background(brush))` overlay where `brush` is a linear gradient derived from `textAnchor`:
  - Bottom anchors: gradient from `scrimColor` at bottom → `Color.Transparent` at ~40% height from bottom.
  - Top anchors: mirrored (scrimColor at top → transparent downward).
  - Side anchors (`CenterStart` / `CenterEnd`): horizontal gradient, scrimColor at the anchored side → transparent at 60% width.
  - `Center`: a small radial gradient (opaque center, transparent at radius = 0.5 × diagonal).
- Text column: aligned inside the outer Box via `Alignment` mapped from `textAnchor`. Padded by `ElectricPopTheme.spacing.xl` (24dp) from the anchored edges. Column uses `Arrangement.spacedBy(spacing.xs)` between eyebrow/headline/subtitle.

### 5.3 Typography (design rule 7)

- **eyebrow** (optional): `MaterialTheme.typography.labelSmall`, uppercase, `letterSpacing = 2.4.sp`, `color = contentColor.copy(alpha = 0.75f)`.
- **headline** (required): `MaterialTheme.typography.displayMedium.copy(fontStyle = Italic, fontWeight = Black)`, uppercase via `.uppercase()`, `color = contentColor`. Explicit `\n` in the input string is honored so callers can force multi-line layout (e.g. `"ENCRYPTED\nWEALTH\nSIMPLIFIED"`).
- **subtitle** (optional): `MaterialTheme.typography.bodyMedium`, `color = contentColor.copy(alpha = 0.9f)`.

### 5.4 Kinetic interaction (design rule 5)

When `onClick != null`:
- Hover → `scale(1.02)`
- Press → `scale(0.97)`
- 200ms tween
- Click propagation via `clickable(interactionSource, indication = null, onClick)`

When `onClick == null`, the card is inert (no interactionSource overhead).

### 5.5 Design rule compliance

| Rule | Compliance |
|---|---|
| 1 — No-Line | No borders on the card. Scrim is a gradient, not a line. |
| 2 — Tonal Shadows | Delegated to `PopSurface` for the outer clip container; the banner card itself doesn't add shadow because images are naturally high-contrast and a shadow would muddy the edge. If needed, apply shadow via caller's modifier. |
| 3 — Ghost Border | Not used (image provides the visual edge). |
| 4 — Neon Glow | Not applicable (decorative hero card, not a CTA). |
| 5 — Kinetic | Hover/press scale when clickable (see 5.4). |
| 6 — Squircle | `MaterialTheme.shapes.extraSmall` for the outer clip. |
| 7 — Typography Impact | Headline is `displayMedium` + italic + Black + uppercase. |

### 5.6 Sample asset

A single bundled sample hero image lives at `library/src/commonMain/composeResources/drawable/pop_banner_hero.png` — approximately 1200×800, under 100KB, abstract/geometric content that works as a visual placeholder for the demo and screenshot tests. Consumer apps bring their own images.

### 5.7 Screenshot test variants

Five configurations per theme:

1. **BottomStart** (default) — eyebrow + headline + subtitle, scrim on.
2. **TopEnd** — eyebrow + headline, no subtitle, scrim on.
3. **Center** — headline only (multi-line via `\n`), radial scrim.
4. **BottomCenter** — headline + subtitle, `scrim = false` (demonstrates the off-state).
5. **Constrained height** — wrapped in `Modifier.height(120.dp).fillMaxWidth()` to demonstrate the height-constraint-wins sizing behavior.

## 6. Component: PopBannerCard (renamed from PopMetricCard, enhanced)

### 6.1 Public API

```kotlin
enum class PopBannerCardStyle { Surface, Hero }

@Composable
fun PopBannerCard(
    label: String,
    mainText: String,
    modifier: Modifier = Modifier,
    fractionalText: String? = null,
    badgeValue: String? = null,
    badgeDirection: PopBadgeDirection = PopBadgeDirection.Neutral,
    displayDirection: PopDisplayTextDirection = PopDisplayTextDirection.Neutral,
    displaySize: PopDisplayTextSize = PopDisplayTextSize.Large,
    icons: List<PopIconItem> = emptyList(),
    style: PopBannerCardStyle = PopBannerCardStyle.Surface,
    onClick: (() -> Unit)? = null,
)
```

Parameter surface is identical to today's `PopMetricCard`. Only enum name changes: `PopMetricCardStyle` → `PopBannerCardStyle`.

The types previously exposed by the old (numeric) `PopBannerCard` — `PopBannerCardTrend`, `PopBannerCardTrendDirection`, `PopBannerCardVital` — are **deleted**. They are not replaced; callers use the existing `PopBadgeDirection` and `PopIconItem` types already used by `PopMetricCard`.

### 6.2 Enhancement 1 — Chip typography (from old Banner) + chip colors (from Metric)

Current `PopMetricCard` chip: `labelLarge` text. Change to the chunkier styling from the old (numeric) `PopBannerCard`:

- Text: `MaterialTheme.typography.titleLarge.copy(fontStyle = Italic, fontWeight = Black)`, uppercased via `.uppercase()`.
- Icon: `PopIconSize.Medium` (~20dp).
- Padding: `horizontal = spacing.md`, `vertical = spacing.xs` (unchanged).

Chip **colors stay as today's `PopMetricCard`**:

- **Hero style:** background `onPrimaryContainer`, text/icon `primaryContainer` (inverted from the surrounding card).
- **Surface style:** colors derived from `badgeDirection.toColors()` — Up=green container/onGreen content, Down=red/onRed, Neutral=transparent.

### 6.3 Enhancement 2 — Chip + icons on the same row

Current layout stacks chip and icon cluster vertically with a `Spacer(xxs)` between. Change to a single `Row`:

```kotlin
Spacer(Modifier.height(spacing.sm))
Row(
    horizontalArrangement = Arrangement.spacedBy(spacing.md),
    verticalAlignment = Alignment.CenterVertically,
) {
    // chip (when badgeValue != null)
    // icon cluster (when icons.isNotEmpty())
}
```

The icon cluster is **preserved as-is from `PopMetricCard`**:

- 40dp circles (`CircleShape`), `-12dp` overlap offset, 2dp border in card-bg color.
- Background: `Color.White.copy(alpha = 0.4f)` on Hero style, `MaterialTheme.colorScheme.surfaceContainerHigh` on Surface style.
- Tint: `onPrimaryContainer` on Hero, `onSurfaceVariant` on Surface.
- Max 5 visible icons, then a `+N` overflow circle (same styling as the icon circles; text is `labelSmall`).
- Inner icon glyph: `PopIconSize.Small`.

The two prior `Spacer`s (`xxs` above chip, `xs` above icon row) collapse to one `Spacer(spacing.sm)` above the combined Row. When both `badgeValue` is null and `icons` is empty, neither the Spacer nor the Row is emitted — matching today's behavior.

### 6.4 Enhancement 3 — Dark-mode visual parity

This is **not a component change**. The perceived sameness between light and dark Hero screenshots is a consequence of (a) the intentional brand-lime `primaryContainer` being identical across themes, and (b) the screenshot test not painting a page background.

The fix lives in the `pixy-implementor` agent template (Section 8) and in the banner-related screenshot tests touched by this branch.

### 6.5 Design rule compliance

Unchanged from today's `PopMetricCard`. The chip typography change (6.2) still satisfies rule 7 (italic + black + uppercase). The same-row layout (6.3) doesn't impact any of the seven rules.

### 6.6 Screenshot test variants

Same four configurations as today's `PopMetricCard`:

1. Hero + badge (Up) + 3 icons, `fractionalText = ".42"`.
2. Hero + badge (Up) + 7 icons (demonstrates `+2` overflow).
3. Surface + badge (Down) + `displayDirection = Negative`, no icons.
4. Surface, no badge, `displaySize = Small`, no icons.

Re-recorded after enhancements 1 & 2 and the dark-bg test wrapper (Section 8) are applied.

## 7. Migration & cleanup

### 7.1 File operations

**Delete:**
- `library/src/commonMain/kotlin/co/tanay/electricpop/composite/PopBannerCard.kt` (current numeric impl)
- `library/src/commonTest/kotlin/co/tanay/electricpop/composite/PopBannerCardTest.kt`
- `library/src/desktopTest/kotlin/co/tanay/electricpop/composite/PopBannerCardScreenshotTest.kt`
- `demo/src/commonMain/kotlin/co/tanay/electricpop/demo/components/PopBannerCardDemo.kt`
- `library/src/desktopTest/snapshots/PopBannerCard_allVariants_light.png`
- `library/src/desktopTest/snapshots/PopBannerCard_allVariants_dark.png`

**Rename via `git mv`** (then apply Section 6 enhancements + rename class/function/enum symbols in the moved files):
- `PopMetricCard.kt` → `PopBannerCard.kt`
- `PopMetricCardTest.kt` → `PopBannerCardTest.kt`
- `PopMetricCardScreenshotTest.kt` → `PopBannerCardScreenshotTest.kt`
- `PopMetricCardDemo.kt` → `PopBannerCardDemo.kt`

**Delete old Metric goldens** (new goldens are recorded post-rename with the dark-bg wrapper):
- `library/src/desktopTest/snapshots/PopMetricCard_allVariants_light.png`
- `library/src/desktopTest/snapshots/PopMetricCard_allVariants_dark.png`

**Create:**
- `library/src/commonMain/kotlin/co/tanay/electricpop/composite/PopImageBannerCard.kt`
- `library/src/commonTest/kotlin/co/tanay/electricpop/composite/PopImageBannerCardTest.kt`
- `library/src/desktopTest/kotlin/co/tanay/electricpop/composite/PopImageBannerCardScreenshotTest.kt`
- `demo/src/commonMain/kotlin/co/tanay/electricpop/demo/components/PopImageBannerCardDemo.kt`
- `library/src/commonMain/composeResources/drawable/pop_banner_hero.png`
- `library/src/desktopTest/snapshots/PopImageBannerCard_*.png` (5 variants × 2 themes = 10 goldens)
- `library/src/desktopTest/snapshots/PopBannerCard_*.png` (4 variants × 2 themes = 8 goldens, re-recorded)

### 7.2 CatalogScreen update

`demo/src/commonMain/kotlin/co/tanay/electricpop/demo/CatalogScreen.kt`:
- Remove both current entries: `PopBannerCard` (numeric) and `PopMetricCard`.
- Add `PopBannerCard` (renamed, enhanced).
- Add `PopImageBannerCard` (new).

### 7.3 Documentation updates

| File | Change |
|---|---|
| `CLAUDE.md` | Composite inventory section: replace `PopMetricCard` mention with `PopImageBannerCard`. Count stays at 7. |
| `AGENTS.md` | Same inventory fix. |
| `README.md` | Rename row 82 `PopBannerCard` → `PopImageBannerCard` (keep description "Big image card with overlay headline" — it already describes the new component). Rename row 83 `PopMetricCard` → `PopBannerCard` and rewrite its description for the enhanced metric-flavored card. |
| `PROMPT-PHASE-03.md` | Wave 4 table: update row 22 (`PopMetricCard`) → `PopBannerCard (enhanced)`. Update row 27 (`PopBannerCard`) → `PopImageBannerCard`. Adjust `⬅ NEXT` marker. |
| `.execution-history/phase-03-implementation.md` | Append a dated note: "2026-04-18 — PopMetricCard renamed to PopBannerCard; old PopBannerCard (numeric hero) deleted and replaced by PopImageBannerCard. See spec `2026-04-18-banner-card-refactor`." |
| `.execution-history/execution-summary-phase-1-2.md` | Append one-line historical note at the end. Do not rewrite past entries. |
| `docs/superpowers/plans/2026-03-25-electric-pop-implementation.md` | Append a one-line historical note near the banner/metric rows. Original plan content left intact. |
| `docs/superpowers/specs/2026-03-25-electric-pop-design.md` | Section 6.2 table: update `PopBannerCard` row description, replace `PopMetricCard` row with `PopImageBannerCard`. Add a dated note at the section header: "Revised 2026-04-18 — see spec 2026-04-18-banner-card-refactor." |
| `library/src/commonMain/kotlin/co/tanay/electricpop/foundation/PopSurface.kt` | Line 84 comment: `PopMetricCard` → `PopBannerCard`. |

## 8. pixy-implementor agent change

**Edit target:** `.claude/agents/pixy-implementor.md`, screenshot-test template (lines ~82–122).

**Change:** wrap screenshot content in a full-bleed `Box` painted with `MaterialTheme.colorScheme.background`:

```kotlin
setContent {
    ElectricPopTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // variants
            }
        }
    }
}
```

Same wrapper for the dark variant. Add a prose note to Step 7 of the agent doc:

> **Required:** wrap screenshot content in `Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background))` so dark-mode goldens have a dark page background for visual parity with light goldens. `ElectricPopTheme` alone does not paint a page background.

**Scope:** this rule applies to the two screenshot tests re-recorded in this branch (renamed `PopBannerCard`, new `PopImageBannerCard`) and to all future screenshot tests. Retroactively re-recording goldens for the ~15 pre-existing components is deliberately **out of scope** for this branch — called out as a follow-up at PR time.

## 9. Commit strategy

One branch. Two commits:

1. `feat(composite): rename PopMetricCard to PopBannerCard and enhance chip/icon row`
   - `git mv` the four Metric files
   - Delete old PopBannerCard files + goldens
   - Rename symbols; apply Section 6 enhancements
   - Apply Section 8 test wrapper to the renamed screenshot test; re-record PopBannerCard goldens
   - Update `.claude/agents/pixy-implementor.md`
   - Update all 9 doc files (Section 7.3)

2. `feat(composite): add PopImageBannerCard with configurable text anchor`
   - New component, test, screenshot test, demo, catalog entry, sample drawable
   - Uses the Section 8 test wrapper
   - Record PopImageBannerCard goldens

Two commits keeps each logical change reviewable in isolation.

## 10. Risks & open items

- **Bundled sample image license:** the hero PNG must be original or clearly-licensed (e.g., CC0). The implementor will generate/select an abstract image free of IP encumbrances.
- **Aspect-ratio edge case:** if a caller passes a painter whose `intrinsicSize` is `Size.Unspecified` (some vector painters), the `aspectRatio` modifier will throw. The component will fall back to a default `16f/9f` aspect ratio in that case, logged via a runtime check.
- **Coil/Kamel integration note:** the README entry for `PopImageBannerCard` should mention that remote-image callers pair the component with `rememberAsyncImagePainter` (Coil 3) or `asyncPainterResource` (Kamel). This is a doc note; no code dependency.
- **Roborazzi font rendering:** changing the page background from default-white to `MaterialTheme.colorScheme.background` means existing goldens for other components would diff if re-recorded. The scope-limit in Section 8 avoids that churn for this branch.
