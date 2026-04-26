# Implementation Plan: PopChart — Donut Style (Delta)

## Component Overview

This plan **adds a third style** — `PopChartStyle.Donut` — to the existing unified `PopChart` sealed-style API in `library/src/commonMain/kotlin/co/tanay/electricpop/chart/PopChart.kt`. `Line` and `Bar` (already shipped on `feat/pop-donut-chart`) are **not** modified — no signature changes, no helper changes, no golden churn for line/bar regions. The donut renders inside the same `PopSurface` chrome (title block + canvas + optional center labels) and reuses `PopChartSeries` so callers can keep one data type across all three styles. The component dispatches `when (style) { Line -> ...; Bar -> ...; Donut -> ... }` with a new private `drawDonutChart(...)` and a new private composable `DonutChartContent(...)` that overlays center text on the canvas via a `Box`.

The donut is dual-mode in a single style:
- **Gauge mode** — single-value series + an explicit `total` → fills `value/total` of the ring against a tonal track. Mirrors the Stitch "Budget Remaining 72%" card exactly.
- **Pie mode** — multi-slice series → each `series[i].values[0]` becomes a slice of the ring; total defaults to the sum.

## Files

- **Modify** `library/src/commonMain/kotlin/co/tanay/electricpop/chart/PopChart.kt`
  - Add `PopChartStyle.Donut` data class to the existing sealed hierarchy.
  - Add private `drawDonutChart(...)` `DrawScope` extension.
  - Add internal helpers `computeDonutSlices(...)`, `donutTotal(...)` for unit testing.
  - Replace the `Canvas { when (style) { ... } }` block with a `Box` wrapper that overlays a center-text `Column` when `style is Donut` (line/bar paths unchanged).
  - Donut ignores `xLabels` (it has no x-axis); document this in the `xLabels` KDoc.
- **Modify** `library/src/commonTest/kotlin/co/tanay/electricpop/chart/PopChartTest.kt`
  - Keep all 37 existing tests untouched.
  - Add 9 new tests for donut math (slice angle math, total fallback, gauge clamping, edge cases). Total → 46.
- **Modify** `library/src/desktopTest/kotlin/co/tanay/electricpop/chart/PopChartScreenshotTest.kt`
  - Append a `DONUT` section after `BAR` with 4 new variants.
  - Bump canvas height from `460×4200` to **`460×5400`** (≈+1200 to fit 4 donut cards × ~280dp each + section header + labels + spacing).
- **Re-record** `library/src/desktopTest/snapshots/PopChart_allVariants_light.png` + `_dark.png`.
- **Modify** `demo/src/commonMain/kotlin/co/tanay/electricpop/demo/components/PopChartDemo.kt`
  - Add `DONUT` section with the same 4 variants (after `BAR`, before `EMBEDDED`).
- **No changes** to `CatalogScreen.kt` — `PopChart` already registered.

## API Design

### Data-model decision

**Reuse `PopChartSeries` unchanged.** Each series contributes `values[0]` as its slice value. Other indices in `values` are ignored (the donut has no time/x dimension). This keeps the call-site type identical across Line/Bar/Donut and avoids a parallel `PopChartSlice` type.

- `series` empty / all-empty / sum == 0 → empty placeholder (a tonal track ring with no fill, plus center text if provided — matches the line/bar empty placeholder ethos).
- `xLabels` are ignored for donut (documented inline in KDoc + a `// xLabels: ignored for Donut style` comment in dispatch).
- `series[i].color` overrides per-slice color exactly as in line/bar; otherwise `defaultSeriesColor(i, scheme)` cycles primary→tertiary→secondary.
- `style.activeIndex` highlights one **slice** index (clamped via existing `clampActiveIndex`).

### Sealed style addition

Append after `Bar` in the existing sealed class:

```kotlin
/**
 * Donut chart style.
 *
 * Two modes share one drawing path:
 *
 * - **Gauge mode** — pass exactly one [PopChartSeries] and an explicit [total]
 *   (e.g. budget cap). The donut fills `series[0].values[0] / total` of the
 *   ring as the filled arc; the remaining circumference is a tonal track in
 *   `surfaceContainerHigh`. The Stitch "Budget Remaining 72%" card is this mode.
 * - **Pie mode** — pass two or more [PopChartSeries], each contributing
 *   `values[0]` as a slice value. When [total] is null the total is the sum
 *   of slice values (every slice is shown). When [total] is non-null and
 *   greater than the slice sum, the leftover circumference renders in
 *   `surfaceContainerHigh` (so a multi-category gauge with headroom works).
 *
 * The arc starts at 12 o'clock (top) and grows clockwise, matching the Stitch
 * SVG `-rotate-90` convention.
 *
 * @param centerLabel Optional supporting label below [centerValue]
 *   (`labelSmall`, uppercased, `onSurfaceVariant`). Stitch: "Active Cap".
 * @param centerValue Optional headline rendered inside the donut hole
 *   (`displayLarge`, italic, black). Stitch: "72%". Caller is responsible for
 *   formatting (`"${pct.toInt()}%"`, `"$1.2K"`, etc.).
 * @param strokeRatio Ring thickness as a fraction of the donut **outer radius**
 *   (default `0.218f`, matching the Stitch design's 24/110 ≈ 21.8%). Resolves
 *   responsively as the canvas resizes — preferred over a fixed dp width.
 * @param total Explicit total denominator. When null, total = sum of slice
 *   values. Use a non-null total for gauge mode (single value over a cap)
 *   or to leave headroom in pie mode.
 * @param activeIndex Slice index highlighted with a layered neon halo + 1.02×
 *   radial outset (Rule 4 + Rule 5). Clamped via [clampActiveIndex]; null →
 *   no active highlight.
 */
@Immutable
data class Donut(
    val centerLabel: String? = null,
    val centerValue: String? = null,
    val strokeRatio: Float = 0.218f,
    val total: Float? = null,
    val activeIndex: Int? = null,
) : PopChartStyle()
```

No top-level `PopChart(...)` parameter changes. No new enums.

### Gauge convenience? — **No.**

The orchestrator brief explicitly prohibits new top-level params; the cleanest gauge construction is `PopChartStyle.Donut(total = 100f, centerValue = "${pct.toInt()}%")` with a single-series caller. Callers do not need a separate `Gauge` subclass — the same data class covers both modes.

## Geometry & rendering

### Layout

The unified `PopChart` `chartHeight` is the **diameter** of the donut for layout purposes. The drawing math takes the `min(size.width, size.height)` so a wider canvas centers the donut horizontally; a taller canvas centers it vertically. Default `chartHeight = 180.dp` is fine for in-context demos; the screenshot/demo variants override to **`220.dp`** so the donut feels card-like (Stitch uses `w-64 h-64` = 256px ≈ 16 % bigger than 220dp at default density — an acceptable production size).

### Dimensions

```
diameter = min(size.width, size.height)
strokeWidthPx = diameter * style.strokeRatio        // default 0.218f
outerRadius = diameter / 2f
centerRadius = outerRadius - strokeWidthPx / 2f     // arc is centered on this
center = Offset(size.width / 2f, size.height / 2f)
arcRect = Rect(
    center.x - centerRadius, center.y - centerRadius,
    center.x + centerRadius, center.y + centerRadius,
)
```

`drawArc(...)` strokes are anchored on `centerRadius`, so the **outer edge** sits at `centerRadius + strokeWidthPx/2 = outerRadius` and the **inner edge** sits at `centerRadius - strokeWidthPx/2 = outerRadius - strokeWidthPx`. The hole diameter is `(outerRadius - strokeWidthPx) * 2`.

At default `strokeRatio = 0.218f` and `chartHeight = 220.dp`:
- diameter ≈ 220.dp
- strokeWidth ≈ 48.dp
- outer radius ≈ 110.dp (matches Stitch spec exactly)
- inner hole diameter ≈ 124.dp (plenty of room for `displayLarge` "72%")

### Slice arc math

```kotlin
internal data class DonutSlice(
    val seriesIndex: Int,
    val color: Color?,        // null → resolve via defaultSeriesColor at draw time
    val startAngleDeg: Float, // 0° = top of circle (12 o'clock); CW positive
    val sweepAngleDeg: Float, // strictly > 0 for slices that draw
)

internal fun computeDonutSlices(
    series: List<PopChartSeries>,
    explicitTotal: Float?,
): List<DonutSlice>
```

Algorithm:
1. Map `series` → `(index, value, color)` triples where `value = values.firstOrNull()?.takeIf { !it.isNaN() && it >= 0f } ?: 0f`. Negative values clamped to 0 (donuts cannot represent negative magnitudes; document in KDoc).
2. `sliceSum = triples.sumOf { it.value }`. If `sliceSum <= 0f` → return empty list (caller draws track-only).
3. `effectiveTotal = explicitTotal?.takeIf { it > 0f } ?: sliceSum`. If `explicitTotal != null && explicitTotal < sliceSum`, fall back to `sliceSum` (we never let a slice exceed the ring; document and unit-test).
4. Walk slices in series order:
   - `startAngle = runningAngle` (start at 0° = top; first slice begins at top).
   - `sweep = (value / effectiveTotal) * 360f`.
   - Skip emit when `sweep <= 0f` (NaN, zero, or filtered).
   - Emit `DonutSlice(seriesIndex = i, color = series[i].color, startAngleDeg = startAngle, sweepAngleDeg = sweep)`.
   - `runningAngle += sweep`.
5. The "remaining" arc (when `effectiveTotal > sliceSum`) is **not** emitted as a `DonutSlice` — it's drawn separately as the track (`surfaceContainerHigh`) by the renderer in a single full-circle arc beneath the slices. This is simpler than emitting it because (a) the track is one full ring under everything, (b) the `surfaceContainerHigh` color is theme-dependent and not in the slice's data.

Coordinate conversion at draw time: Compose's `drawArc` uses 0° = **3 o'clock** (East, +X axis) and CCW-negative-CW-positive convention. To make 0° = top, draw with `startAngle = sliceStart - 90f`. We do this conversion in the renderer (slices keep "top-up" math; renderer is the only place that translates).

### Drawing pipeline

```
drawDonutChart(series, style, colorScheme):
    1. Compute geometry (diameter, outerRadius, strokeWidth, center, arcRect).
    2. Draw track: full-circle arc, color = surfaceContainerHigh,
       stroke width = strokeWidthPx, cap = Round.
       This produces the tonal "Available" ring beneath everything.
    3. Compute slices via computeDonutSlices(series, style.total).
    4. If slices is empty, return after track (empty placeholder).
    5. activeClamped = clampActiveIndex(style.activeIndex, slices.size)
    6. Draw non-active slices first (in series order):
         for slice in slices where index != activeClamped:
             drawArc(
                 color = slice.color ?: defaultSeriesColor(slice.seriesIndex, scheme),
                 startAngle = slice.startAngleDeg - 90f,
                 sweepAngle = slice.sweepAngleDeg,
                 useCenter = false,
                 topLeft = arcRect.topLeft,
                 size = arcRect.size,
                 style = Stroke(width = strokeWidthPx, cap = Round),
             )
    7. If activeClamped != null:
         a. Draw glow halos behind the active slice (see "Active slice treatment").
         b. Draw the active slice with 1.02× radial outset (see below).
```

### Stroke caps and overlap

Compose's `Stroke(cap = StrokeCap.Round)` adds a half-circle to each end of the arc, which means a "100%" arc would visually overlap itself by half a stroke width at the seam. We accept this: the Stitch design uses `stroke-linecap="round"` and shows the same minor seam at full coverage. For multi-slice (pie) mode, **adjacent slice ends overlap** because of round caps — this is by design and matches the soft Electric Pop aesthetic. If the reviewer flags it as awkward in dark theme, switch to `StrokeCap.Butt` and accept the harder transitions; do not introduce a "gap between slices" feature in v1.

### Center-text overlay

The donut canvas is wrapped in a `Box` so we can stack text over the canvas:

```kotlin
when (style) {
    is PopChartStyle.Donut -> {
        Box(
            modifier = Modifier.fillMaxWidth().height(chartHeight),
            contentAlignment = Alignment.Center,
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawDonutChart(series, style, colorScheme)
            }
            if (style.centerValue != null || style.centerLabel != null) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    if (style.centerValue != null) {
                        Text(
                            text = style.centerValue,
                            style = MaterialTheme.typography.displayLarge.copy(
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Black,
                            ),
                            color = colorScheme.onSurface,
                        )
                    }
                    if (style.centerLabel != null) {
                        Text(
                            text = style.centerLabel.uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
    }
    is PopChartStyle.Line -> Canvas(... existing ...) { ... }
    is PopChartStyle.Bar  -> Canvas(... existing ...) { ... }
}
```

The line/bar branches **must remain inside their own `Canvas` block exactly as today** — only the Donut branch gets the `Box` wrapper. Refactor the existing `Canvas { ... when ... }` into a `when (style) { ... }` block where each branch wires up its own composable subtree. This keeps line/bar bytecode unchanged for existing goldens.

## Active slice treatment

Per Rule 4 (Neon Glow) + Rule 5 (Kinetic Interactions). Mirrors line/bar alphas so all three styles look like one family.

When `activeClamped != null`, draw the active slice **last** with the following layered passes (each is a `drawArc` on the same `arcRect` with the same start/sweep angles):

1. **Outer glow halo** — `Stroke(width = strokeWidthPx + 16.dp.toPx(), cap = Round)`, color = `seriesColor.copy(alpha = 0.18f)`. Inflates the stroke 8.dp on each side.
2. **Inner glow halo** — `Stroke(width = strokeWidthPx + 8.dp.toPx(), cap = Round)`, color = `seriesColor.copy(alpha = 0.28f)`. Inflates 4.dp each side.
3. **Scaled active slice** — `Stroke(width = strokeWidthPx * 1.02f, cap = Round)`, full opacity series color. The 1.02× scale on stroke width gives the slice a subtle radial bulge without offsetting its angular position. We do **not** inflate `arcRect` itself — that would shift the slice center off the donut center.

Draw order summary: `track → non-active slices → outer halo → inner halo → active slice (1.02× width)`. The halo arcs use the active slice's angles, so the glow precisely follows the slice geometry.

These alphas (0.18, 0.28) match the bar chart's halo treatment exactly — keep them in lockstep so a reviewer can flag a single number if dark-mode visibility breaks across both.

If `slices.size == 1` and the only slice covers the full ring (gauge at 100% with explicit-total = sliceSum), the active treatment still renders correctly because the halos draw a full ring at expanded stroke width — no special-casing needed.

## Variants to ship

4 new donut variants in both screenshot tests and the demo:

| # | Title | Subtitle | Series | `total` | `centerValue` | `centerLabel` | `activeIndex` |
|---|---|---|---|---|---|---|---|
| 11 | Budget Remaining | Gauge · 72%      | `[("Spent", [72f], primaryContainer-default)]` | `100f` | `"72%"`  | `"Active Cap"` | null |
| 12 | Budget Remaining | Gauge · Active   | same as #11 | `100f` | `"72%"`  | `"Active Cap"` | `0` |
| 13 | Allocation       | Pie · 4 slices   | `[("Marketing", [38f]), ("Engineering", [27f]), ("Ops", [21f]), ("Reserve", [14f])]` | null | `"$1.2K"` | `"Total"` | null |
| 14 | Allocation       | Pie · Active 1   | same as #13 | null | `"$1.2K"` | `"Total"` | `1` (Engineering — tertiaryContainer slice) |

Sample data is concrete and copyable. Variant #13's four values sum to 100 by design so the math reads cleanly in goldens (38°/100° relations are easy to visually audit against the plan).

For the screenshot test only, every donut variant uses `chartHeight = 220.dp` to give the rings room to breathe (the default 180.dp leaves the displayLarge center text crowded on dark theme). For the demo page, also use `220.dp`.

## Test strategy

### New unit tests in `PopChartTest.kt` (9 tests)

#### `computeDonutSlices` (5 tests)
- `donut_emptySeries_returnsEmpty` — `computeDonutSlices(emptyList(), null) == []`.
- `donut_allZeroValues_returnsEmpty` — `[Series("a", [0f]), Series("b", [0f])]` with null total → empty.
- `donut_singleSlice_implicitTotal_sweeps360` — one series, value 50, total null → one slice with sweep ≈ 360°.
- `donut_explicitTotalLargerThanSum_leavesGap` — one series, value 50, total = 100 → one slice with sweep ≈ 180°.
- `donut_multiSlicesAdjacent_startAnglesAccumulate` — `[10, 20, 30]` total null → slices with starts `[0°, 60°, 120°]` and sweeps `[60°, 120°, 180°]` (within 1e-3 tolerance). Confirms cumulative-angle correctness.

#### `donutTotal` fallback rule (2 tests)
- `donutTotal_explicitNull_usesSliceSum` — `donutTotal(null, sliceSum = 100f) == 100f`.
- `donutTotal_explicitLessThanSum_fallsBackToSum` — `donutTotal(50f, sliceSum = 100f) == 100f` (we never overflow the ring).

(Implementation note: `donutTotal` is a tiny pure helper — `internal fun donutTotal(explicit: Float?, sliceSum: Float): Float = explicit?.takeIf { it > 0f && it >= sliceSum } ?: sliceSum` — extracted so it can be tested without constructing slice lists.)

#### Negative-value clamp (1 test)
- `donut_negativeValuesClampedToZero` — `[Series("a", [-5f]), Series("b", [10f])]` total null → 1 slice (b), sweep 360° (negative is filtered out, not added to sliceSum).

#### NaN-value clamp (1 test)
- `donut_nanValuesFiltered` — `[Series("a", [Float.NaN]), Series("b", [10f])]` total null → 1 slice (b), sweep 360°.

**clampActiveIndex** — already covered by existing tests; behavior is identical for donut (input slices count, output clamp). No new tests needed.

### Screenshot test changes

`PopChartScreenshotTest.kt`: append `DONUT` section after `BAR`, height `460×4200` → `460×5400`. Each donut variant uses `chartHeight = 220.dp`. Section header `"DONUT"` (`labelLarge`, `onSurfaceVariant`), then per-variant labels `"GAUGE · 72%"`, `"GAUGE · ACTIVE"`, `"PIE · 4 SLICES"`, `"PIE · ACTIVE 1"` in `labelSmall`.

Re-record both light and dark goldens. Existing line/bar renderings up the page MUST be byte-identical — if they aren't, the implementor has accidentally touched non-donut code.

## Demo additions

In `PopChartDemo.kt`, insert a new `DONUT` section between `BAR` and `EMBEDDED`:

```kotlin
// ── DONUT ────────────────────────────────────────────────────────────
Text(
    "DONUT",
    style = MaterialTheme.typography.labelLarge,
    color = MaterialTheme.colorScheme.onSurfaceVariant,
)
Column(verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
    // Gauge mode
    PopChart(
        series = listOf(PopChartSeries("Spent", listOf(72f))),
        style = PopChartStyle.Donut(
            total = 100f,
            centerValue = "72%",
            centerLabel = "Active Cap",
        ),
        title = "Budget Remaining",
        subtitle = "Gauge · 72%",
        chartHeight = 220.dp,
    )
    // Gauge mode + active glow
    PopChart(
        series = listOf(PopChartSeries("Spent", listOf(72f))),
        style = PopChartStyle.Donut(
            total = 100f,
            centerValue = "72%",
            centerLabel = "Active Cap",
            activeIndex = 0,
        ),
        title = "Budget Remaining",
        subtitle = "Gauge · Active",
        chartHeight = 220.dp,
    )
    // Pie — 4 slices (38 + 27 + 21 + 14 = 100)
    PopChart(
        series = listOf(
            PopChartSeries("Marketing", listOf(38f)),
            PopChartSeries("Engineering", listOf(27f)),
            PopChartSeries("Ops", listOf(21f)),
            PopChartSeries("Reserve", listOf(14f)),
        ),
        style = PopChartStyle.Donut(
            centerValue = "\$1.2K",
            centerLabel = "Total",
        ),
        title = "Allocation",
        subtitle = "Pie · 4 slices",
        chartHeight = 220.dp,
    )
    // Pie + active = 1 (Engineering — tertiaryContainer)
    PopChart(
        series = listOf(
            PopChartSeries("Marketing", listOf(38f)),
            PopChartSeries("Engineering", listOf(27f)),
            PopChartSeries("Ops", listOf(21f)),
            PopChartSeries("Reserve", listOf(14f)),
        ),
        style = PopChartStyle.Donut(
            centerValue = "\$1.2K",
            centerLabel = "Total",
            activeIndex = 1,
        ),
        title = "Allocation",
        subtitle = "Pie · Active 1",
        chartHeight = 220.dp,
    )
}
```

Total demo sections after change: 6.

## Visual spec

- **Container** — `PopSurface(tone = PopSurfaceTone.Low, shape = MaterialTheme.shapes.extraSmall)`. Inherited from existing `PopChart` chrome. No change. (HTML `rounded-xl` 12px → `extraSmall` per planner mapping.)
- **Inner padding** — `contentPadding` default = `spacing.xl` = 32.dp. Unchanged.
- **Title** — `headlineSmall`, italic, FontWeight.Black, uppercased. Unchanged.
- **Subtitle** — `labelSmall`, uppercased, `onSurfaceVariant`. Unchanged.
- **Title → canvas gap** — `spacing.lg` = 24.dp. Unchanged.
- **Donut diameter** — `min(canvas.width, chartHeight)` (typically `chartHeight`). Default 180.dp; recommended 220.dp for donut variants.
- **Stroke width** — `diameter * 0.218f`. At 220.dp diameter → 48.dp ring (Stitch parity with 24/110 = 21.8 %).
- **Stroke cap** — `StrokeCap.Round` everywhere (track + slices + halos).
- **Track color** — `colorScheme.surfaceContainerHigh`. Stitch light: `surface-container-high`; Stitch dark: `zinc-800`. Both resolve to `surfaceContainerHigh` in our theme.
- **Slice colors** — `series[i].color ?: defaultSeriesColor(i, scheme)` (primaryContainer → tertiaryContainer → secondaryContainer cycle).
- **Active outer halo** — stroke width = `strokeWidth + 16.dp`, color = sliceColor @ alpha 0.18.
- **Active inner halo** — stroke width = `strokeWidth + 8.dp`, color = sliceColor @ alpha 0.28.
- **Active slice** — stroke width = `strokeWidth * 1.02`. No alpha adjust.
- **Center value** — `displayLarge`, italic, FontWeight.Black, color = `onSurface`. (Stitch: `font-headline text-5xl font-black italic tracking-tighter`. `displayLarge` is the closest theme typography token; `letterSpacing` is already tight in the theme.)
- **Center label** — `labelSmall`, uppercased, color = `onSurfaceVariant`. (Stitch: `font-label text-xs font-bold tracking-widest uppercase text-on-surface-variant`.)
- **Start angle** — 12 o'clock (top). Slices grow clockwise. Compose `drawArc` uses 3 o'clock as 0°, so renderer subtracts 90°.

## Design Rule compliance

1. **No-Line Rule** — Applies. No 1px borders. Track = tonal `surfaceContainerHigh`, not a stroke. Slice transitions are color-only (round caps overlap softly).
2. **Tonal Shadows** — Applies. Inherited via `PopSurface` outer container. Donut itself has no extra shadow.
3. **Ghost Border** — N/A in default rendering. The donut hole's edge is implicit from the stroke geometry.
4. **Neon Glow** — Applies. Active slice gets layered halos at 0.18/0.28 alpha — matches `Bar` cluster halo and `Line` active-point glow alpha range.
5. **Kinetic Interactions** — Applies. Active slice stroke width × 1.02 (analogous to Bar's bar 1.02× scale). No hover transitions in v1 — Compose hover is platform-specific.
6. **Squircle Radii** — Applies. Outer container `shapes.extraSmall` (squircle 25 %). The donut itself is a circle, which is its own squircle — no shape primitive needed.
7. **Typography Impact** — Applies. Center value uses `displayLarge` italic black (Space Grotesk). Center label uses `labelSmall` uppercase (Manrope). Title chrome is unchanged from existing `PopChart`.

## Stitch references

- **Source screen** — "Analytics & Charts with Sample Data" (light: `41a0a5389999425083081381a6d168c5`, dark: `23ae9e9b06d440cfa83544e9f7b2c3ca`). Donut sits in a 5-column bento card titled `BUDGET REMAINING` in both themes.
- **HTML cache (light)** — `tmp/stitch-cache/PopChart_light.html` lines 127–153.
- **HTML cache (dark)** — `tmp/stitch-cache/PopChart_dark.html` lines 127–153.
- **PNGs** — Both `PopChart_light.png` and `PopChart_dark.png` returned **HTTP 403** in the planning session (same as PR #32 planning). The HTML markup is sufficient — every dimension and color is explicit in the SVG (`r="110"`, `stroke-width="24"`, viewBox `256x256`, `transform="-rotate-90"`, `stroke-linecap="round"`, fill color `#cafd00`, drop-shadow `0 0 12-15px rgba(202,253,0,0.5-0.6)`). Reviewer can retry the PNG fetch later if needed; the implementation does not depend on it.
- **Key derivations from HTML**:
  - `r=110, stroke-width=24, viewBox=256x256` → ring is 24/(256/2) = 18.75 % of half-canvas. Donut center radius is on the SVG circle's `r`, but Compose `drawArc` strokes are anchored at the radius midline. Translating: `outerRadius = 110 + 12 = 122`, `strokeWidth = 24`, ratio = `24/122 ≈ 0.197`. Bumped slightly to `0.218f` so the ring reads heavier on small canvases — matches the Stitch silhouette tightly while staying visible at default sizes. Documented as the default; callers can override via `strokeRatio`.
  - `text-5xl` ≈ 48px with `font-black italic tracking-tighter` → maps to `displayLarge` italic black. Theme `displayLarge` is `Space Grotesk` 48-57 sp depending on size class — visually matches.
  - `drop-shadow rgba(202,253,0,0.5-0.6)` → split into our two-ring halo at 0.18/0.28 (the same dampening used for `Bar` halos — single-blur 50–60 % alpha is too punchy on multiplatform; layered low-alpha rings read softer and dark-mode-safer).
  - Track `bg-surface-container-high` (light) / `zinc-800` (dark) → `colorScheme.surfaceContainerHigh` (single token works in both themes).

## Notes / flags

- **Screenshot canvas height** — bumping from 4200 to **5400** (4 new variants × ~280dp each + section gap = ~1180dp; round to 5400 for headroom). Width unchanged at 460. If the implementor finds the bottom donut clipped, bump to 5600 — do not exceed 6000 (Roborazzi rendering perf).
- **Line/bar goldens MUST be unchanged** above the donut section. If the diff shows line/bar pixels moved, the implementor accidentally re-laid-out those branches — reject and rework.
- **Round-cap overlap at 100 %** — Acceptable per Stitch precedent. Document inline in `drawDonutChart`.
- **Negative values** — Clamped to 0. Documented in `Donut` KDoc + tested.
- **`xLabels` ignored** — Documented in the `xLabels` KDoc and re-stated in `drawDonutChart`'s implementation comment.
- **Single-slice 100 % gauge with explicit total = sum** — End-cap overlap creates a tiny seam at 12 o'clock. Mirrors Stitch behavior — leave as-is.
- **Stroke width must be integer-pixel-aligned for crisp Roborazzi captures** — Compose handles this internally; no manual rounding needed.
- **`displayLarge` may overflow narrow canvases** — At `chartHeight = 180.dp` and a 5-character `centerValue` (e.g. "$10.5K"), the text could touch the inner ring. The 220.dp default for donut demos avoids this. The runtime does not auto-shrink — if a caller hits this, they shorten their value or bump `chartHeight`. Document as a known constraint; not a bug.
- **PR #32 (line+bar) is already merged into the branch** — this plan is a pure additive delta. No deprecation, no API break.
