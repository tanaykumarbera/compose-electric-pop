package com.electricpop.chart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.electricpop.foundation.PopSurface
import com.electricpop.foundation.PopSurfaceTone
import com.electricpop.theme.ElectricPopTheme
import sv.lib.squircleshape.SquircleShape
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 * One named data series for [PopChart].
 *
 * @param label Short label shown in the legend (uppercased at render time).
 * @param values The y-values. Must be non-empty for the series to render.
 *   Heterogeneous series lengths are supported; x-position is normalized to
 *   `index / (maxLength - 1)` per series.
 * @param color Optional color override. When null, PopChart picks a default from
 *   the theme in series order (primary-container lime → tertiary-container cyan → secondary-container magenta).
 */
@Immutable
data class PopChartSeries(
    val label: String,
    val values: List<Float>,
    val color: Color? = null,
)

/**
 * Selects the rendering style for [PopChart] and carries style-specific options.
 *
 * Rendering differs between [Line] and [Bar] (path stroke vs. filled rectangles,
 * glow dot vs. scale+glow on active), so options that only make sense for one
 * style live inside that style's subclass.
 */
@Immutable
sealed class PopChartStyle {

    /**
     * Line chart style.
     *
     * @param smoothness None = straight segments; Normal = monotone-cubic
     *   (Fritsch-Carlson, same visual as pre-migration `smooth = true`);
     *   High = Catmull-Rom with tension 0.5 (rounder, may overshoot spikes —
     *   intentional).
     * @param showArea When true, draw a vertical-gradient area fill under each
     *   series (top 20% alpha → bottom 0% alpha of the series color).
     * @param activeIndex Data index on the **first** series highlighted with a
     *   layered neon glow dot (Rule 4). Null → no active highlight.
     */
    @Immutable
    data class Line(
        val smoothness: Smoothness = Smoothness.Normal,
        val showArea: Boolean = false,
        val activeIndex: Int? = null,
    ) : PopChartStyle()

    /**
     * Bar chart style.
     *
     * @param grouping Clustered = bars side-by-side per x-tick; Stacked = bars
     *   stacked per x-tick (first series on bottom). For single-series data
     *   both modes render identically.
     * @param activeIndex x-tick (cluster) index highlighted. In [Grouping.Clustered]
     *   mode, ALL bars in that cluster scale to 1.02× and emit a neon glow in
     *   the matching series color. In [Grouping.Stacked] mode, the entire stack
     *   scales and glows using series[0].color (the stack is visually one unit).
     *   Null → no active highlight.
     */
    @Immutable
    data class Bar(
        val grouping: Grouping = Grouping.Clustered,
        val activeIndex: Int? = null,
    ) : PopChartStyle()

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
     * Negative slice values are clamped to 0 (donuts cannot represent negative magnitudes).
     * NaN slice values are filtered and treated as 0.
     *
     * @param centerLabel Optional supporting label below [centerValue]
     *   (`labelSmall`, uppercased, `onSurfaceVariant`). Stitch: "Active Cap".
     * @param centerValue Optional headline rendered inside the donut hole
     *   (`displayLarge`, italic, black). Stitch: "72%". Caller is responsible for
     *   formatting (`"${pct.toInt()}%"`, `"$1.2K"`, etc.).
     *   Note: at `chartHeight = 180.dp` with long values (e.g. "$10.5K"), text may
     *   approach the inner ring edge. Use `chartHeight = 220.dp` for donut variants.
     * @param strokeRatio Ring thickness as a fraction of the donut **outer radius**
     *   (default `0.218f`, matching the Stitch design's 24/110 ≈ 21.8%). Resolves
     *   responsively as the canvas resizes — preferred over a fixed dp width.
     * @param total Explicit total denominator. When null, total = sum of slice
     *   values. Use a non-null total for gauge mode (single value over a cap)
     *   or to leave headroom in pie mode. If explicit total < slice sum, falls back
     *   to slice sum (a slice can never exceed the ring).
     * @param activeIndex Slice index highlighted with a layered neon halo + 1.02×
     *   stroke width (Rule 4 + Rule 5). Clamped via [clampActiveIndex]; null →
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

    /** Curve algorithm for [Line.smoothness]. */
    enum class Smoothness { None, Normal, High }

    /** Bar arrangement for [Bar.grouping]. */
    enum class Grouping { Clustered, Stacked }
}

/**
 * A kinetic chart — line or bar — selected via [style].
 *
 * Follows Electric Pop chart rules:
 * - **No gridlines / no 1px borders.** Scale is implied by tonal background + spacing (Rule 1).
 * - **Neon glow on active.** Line: layered concentric circles at the active point.
 *   Bar: layered rounded-rect halos around the active cluster/stack (Rule 4).
 * - **Squircle container.** Self-contained via `PopSurface` (`surfaceContainerLow`,
 *   `MaterialTheme.shapes.extraSmall`). Set `embedded = true` to skip the container.
 *
 * @param series One or more [PopChartSeries]. Empty list or all-empty series → placeholder drawn.
 * @param style Render style. Default: `PopChartStyle.Line()` (Normal smoothness, no area, no active).
 * @param modifier Modifier for the outermost container.
 * @param xLabels Optional labels drawn below the canvas, evenly spaced. Length must match
 *   the max series length; extras ignored; shorter is truncated.
 *   **Ignored for [PopChartStyle.Donut]** — donuts have no x-axis.
 * @param title Optional uppercase headline drawn above the chart.
 * @param subtitle Optional supporting label (uppercase, labelSmall) under the title.
 * @param embedded When true, omits the outer PopSurface container (for nesting in another card).
 * @param chartHeight Fixed canvas height. Defaults to 180.dp.
 * @param contentPadding Padding inside the surface around title/chart/labels.
 *
 * NaN values in series data are filtered at path/bar build stage.
 */
@Composable
fun PopChart(
    series: List<PopChartSeries>,
    style: PopChartStyle = PopChartStyle.Line(),
    modifier: Modifier = Modifier,
    xLabels: List<String> = emptyList(),
    title: String? = null,
    subtitle: String? = null,
    embedded: Boolean = false,
    chartHeight: Dp = 180.dp,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = ElectricPopTheme.spacing.xl,
        vertical = ElectricPopTheme.spacing.xl,
    ),
) {
    val spacing = ElectricPopTheme.spacing
    val colorScheme = MaterialTheme.colorScheme
    val density = androidx.compose.ui.platform.LocalDensity.current

    val chartContent = @Composable {
        Column(modifier = Modifier.padding(contentPadding)) {
            // Title block
            if (title != null || subtitle != null) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column {
                        if (title != null) {
                            Text(
                                text = title.uppercase(),
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontStyle = FontStyle.Italic,
                                    fontWeight = FontWeight.Black,
                                ),
                                color = colorScheme.onSurface,
                            )
                        }
                        if (subtitle != null) {
                            Text(
                                text = subtitle.uppercase(),
                                style = MaterialTheme.typography.labelSmall,
                                color = colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(spacing.lg))
            }

            // Canvas chart area
            val hasData = series.any { it.values.isNotEmpty() }

            when (style) {
                is PopChartStyle.Donut -> {
                    // xLabels: ignored for Donut style (no x-axis)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(chartHeight),
                        contentAlignment = Alignment.Center,
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            if (!hasData) {
                                // Empty placeholder: tonal full-ring track only
                                drawDonutChart(emptyList(), style, colorScheme)
                            } else {
                                drawDonutChart(series, style, colorScheme)
                            }
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
                is PopChartStyle.Line -> {
                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(chartHeight),
                    ) {
                        if (!hasData) {
                            // Empty placeholder: a tonal horizontal bar
                            val strokeY = size.height / 2f
                            drawLine(
                                color = colorScheme.surfaceContainerHigh,
                                start = Offset(0f, strokeY),
                                end = Offset(size.width, strokeY),
                                strokeWidth = 2.dp.toPx(),
                                cap = StrokeCap.Round,
                            )
                            return@Canvas
                        }
                        drawLineChart(series, style, colorScheme, density)
                    }
                    // X-axis labels for Line
                    if (xLabels.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(spacing.xs))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            val maxLen = series.maxOfOrNull { it.values.size } ?: 0
                            val labelsToShow = xLabels.take(maxLen)
                            labelsToShow.forEach { label ->
                                Text(
                                    text = label.uppercase(),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center,
                                )
                            }
                        }
                    }
                }
                is PopChartStyle.Bar -> {
                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(chartHeight),
                    ) {
                        if (!hasData) {
                            // Empty placeholder: a tonal horizontal bar
                            val strokeY = size.height / 2f
                            drawLine(
                                color = colorScheme.surfaceContainerHigh,
                                start = Offset(0f, strokeY),
                                end = Offset(size.width, strokeY),
                                strokeWidth = 2.dp.toPx(),
                                cap = StrokeCap.Round,
                            )
                            return@Canvas
                        }
                        drawBarChart(series, style, colorScheme, density, spacing)
                    }
                    // X-axis labels for Bar
                    if (xLabels.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(spacing.xs))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            val maxLen = series.maxOfOrNull { it.values.size } ?: 0
                            val labelsToShow = xLabels.take(maxLen)
                            labelsToShow.forEach { label ->
                                Text(
                                    text = label.uppercase(),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center,
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (embedded) {
        Box(modifier = modifier) {
            chartContent()
        }
    } else {
        PopSurface(
            modifier = modifier,
            tone = PopSurfaceTone.Low,
            shape = MaterialTheme.shapes.extraSmall,
        ) {
            chartContent()
        }
    }
}

// --- Line chart drawing ---

private fun DrawScope.drawLineChart(
    series: List<PopChartSeries>,
    style: PopChartStyle.Line,
    colorScheme: ColorScheme,
    density: androidx.compose.ui.unit.Density,
) {
    val (yMin, yMax) = computeYBounds(series)
    series.forEachIndexed { seriesIdx, s ->
        if (s.values.isEmpty()) return@forEachIndexed

        val seriesColor = s.color ?: defaultSeriesColor(seriesIdx, colorScheme)
        val points = normalizePoints(s.values, yMin, yMax, size.width, size.height)

        // Area fill
        if (style.showArea && points.size >= 2) {
            val areaPath = buildLinePath(points, style.smoothness)
            // Close path to baseline
            areaPath.lineTo(points.last().x, size.height)
            areaPath.lineTo(points.first().x, size.height)
            areaPath.close()
            drawPath(
                path = areaPath,
                brush = Brush.verticalGradient(
                    colorStops = arrayOf(
                        0f to seriesColor.copy(alpha = 0.20f),
                        1f to seriesColor.copy(alpha = 0.00f),
                    ),
                    startY = 0f,
                    endY = size.height,
                ),
            )
        }

        // Line stroke
        if (points.size >= 2) {
            val linePath = buildLinePath(points, style.smoothness)
            drawPath(
                path = linePath,
                color = seriesColor,
                style = Stroke(
                    width = 3.dp.toPx(),
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round,
                ),
            )
        } else if (points.size == 1) {
            // Single point — draw a marker
            drawCircle(
                color = seriesColor,
                radius = 4.dp.toPx(),
                center = points[0],
            )
        }

        // Endpoint markers
        if (points.size >= 2) {
            drawCircle(color = seriesColor, radius = 4.dp.toPx(), center = points.first())
            drawCircle(color = seriesColor, radius = 4.dp.toPx(), center = points.last())
        }

        // Active-point glow (first series only)
        if (seriesIdx == 0 && style.activeIndex != null) {
            val clamped = clampActiveIndex(style.activeIndex, s.values.size)
            if (clamped != null && clamped < points.size) {
                val activePoint = points[clamped]
                drawGlow(this, activePoint, seriesColor, colorScheme)
            }
        }
    }
}

// --- Bar chart drawing ---

private fun DrawScope.drawBarChart(
    series: List<PopChartSeries>,
    style: PopChartStyle.Bar,
    colorScheme: ColorScheme,
    density: androidx.compose.ui.unit.Density,
    spacing: com.electricpop.theme.ElectricPopSpacing,
) {
    val clusterCount = series.maxOfOrNull { it.values.size } ?: 0
    val seriesCount = series.count { it.values.isNotEmpty() }
    if (clusterCount == 0 || seriesCount == 0) return

    val clusterGapPx = with(density) { spacing.sm.toPx() }
    val barGapPx = with(density) { spacing.xxs.toPx() }

    val slots = computeBarSlots(
        clusterCount = clusterCount,
        seriesCount = seriesCount,
        grouping = style.grouping,
        totalWidth = size.width,
        clusterGap = clusterGapPx,
        barGap = barGapPx,
    )
    if (slots.isEmpty()) return

    val (yMin, yMax) = when (style.grouping) {
        PopChartStyle.Grouping.Clustered -> computeYBounds(series)
        PopChartStyle.Grouping.Stacked -> {
            val (lo, hi) = computeStackedYBounds(series)
            Pair(lo, hi)
        }
    }

    fun yFor(value: Float): Float {
        val yRange = yMax - yMin
        return if (yRange == 0f) size.height * 0.5f
        else size.height - ((value - yMin) / yRange) * size.height
    }

    val baselineY = yFor(max(0f, yMin))
    val activeIndex = style.activeIndex
    val activeCluster = if (activeIndex != null) clampActiveIndex(activeIndex, clusterCount) else null

    // Draw non-active clusters first, then active cluster on top
    val clusterIndices = (0 until clusterCount).sortedBy { if (it == activeCluster) 1 else 0 }

    for (c in clusterIndices) {
        val clusterSlots = slots.filter { it.clusterIndex == c }
        val isActive = c == activeCluster

        if (style.grouping == PopChartStyle.Grouping.Clustered) {
            // Draw halos behind all bars in active cluster
            if (isActive) {
                clusterSlots.forEach { slot ->
                    val s = series.getOrNull(slot.seriesIndex) ?: return@forEach
                    val value = s.values.getOrNull(c) ?: return@forEach
                    if (value.isNaN()) return@forEach
                    val seriesColor = s.color ?: defaultSeriesColor(slot.seriesIndex, colorScheme)
                    val topY = yFor(value)
                    val barRect = Rect(slot.rect.left, topY, slot.rect.right, baselineY)
                    if (barRect.height <= 0f) return@forEach
                    val outerExpand = with(density) { 16.dp.toPx() }
                    val innerExpand = with(density) { 8.dp.toPx() }
                    drawBarHalo(barRect, seriesColor.copy(alpha = 0.18f), outerExpand, density)
                    drawBarHalo(barRect, seriesColor.copy(alpha = 0.28f), innerExpand, density)
                }
            }
            // Draw the bars
            clusterSlots.forEach { slot ->
                val s = series.getOrNull(slot.seriesIndex) ?: return@forEach
                val value = s.values.getOrNull(c) ?: return@forEach
                if (value.isNaN()) return@forEach
                val seriesColor = s.color ?: defaultSeriesColor(slot.seriesIndex, colorScheme)
                val topY = yFor(value)
                var barRect = Rect(slot.rect.left, topY, slot.rect.right, baselineY)
                if (barRect.height <= 0f) return@forEach
                if (isActive) {
                    // Scale 1.02× about bar center: inflate by 1% of each dimension on each side
                    val dw = barRect.width * 0.01f
                    val dh = barRect.height * 0.01f
                    barRect = Rect(barRect.left - dw, barRect.top - dh, barRect.right + dw, barRect.bottom + dh)
                }
                drawBar(barRect, seriesColor, density)
            }
        } else {
            // Stacked mode: draw all series in this cluster, building from baseline up
            var runningBottom = 0f
            val stackTopY: Float
            val stackBottomY: Float = baselineY

            // Calculate bounding rect for active glow
            var stackTop = 0f
            for (sIdx in series.indices) {
                val s = series[sIdx]
                val value = s.values.getOrNull(c)?.takeIf { !it.isNaN() } ?: 0f
                stackTop += value
            }
            stackTopY = yFor(stackTop)

            if (isActive) {
                val firstColor = series.firstOrNull()?.let {
                    it.color ?: defaultSeriesColor(0, colorScheme)
                } ?: colorScheme.primaryContainer
                val stackRect = Rect(
                    clusterSlots.firstOrNull()?.rect?.left ?: 0f,
                    stackTopY,
                    clusterSlots.firstOrNull()?.rect?.right ?: 0f,
                    stackBottomY,
                )
                if (stackRect.height > 0f) {
                    val outerExpand = with(density) { 16.dp.toPx() }
                    val innerExpand = with(density) { 8.dp.toPx() }
                    drawBarHalo(stackRect, firstColor.copy(alpha = 0.18f), outerExpand, density)
                    drawBarHalo(stackRect, firstColor.copy(alpha = 0.28f), innerExpand, density)
                }
            }

            for (sIdx in series.indices) {
                val s = series[sIdx]
                val slot = clusterSlots.getOrNull(sIdx) ?: continue
                val value = s.values.getOrNull(c)?.takeIf { !it.isNaN() } ?: continue
                val seriesColor = s.color ?: defaultSeriesColor(sIdx, colorScheme)
                val valueBottom = runningBottom
                val valueTop = runningBottom + value
                runningBottom = valueTop

                val topY = yFor(valueTop)
                val botY = yFor(valueBottom)
                var barRect = Rect(slot.rect.left, topY, slot.rect.right, botY)
                if (barRect.height <= 0f) continue
                if (isActive) {
                    val dw = barRect.width * 0.01f
                    val dh = barRect.height * 0.01f
                    barRect = Rect(barRect.left - dw, barRect.top - dh, barRect.right + dw, barRect.bottom + dh)
                }
                drawBar(barRect, seriesColor, density)
            }
        }
    }
}

private fun DrawScope.drawBarHalo(
    barRect: Rect,
    color: Color,
    expand: Float,
    density: androidx.compose.ui.unit.Density,
) {
    val expandedRect = barRect.inflate(expand)
    if (expandedRect.width <= 0f || expandedRect.height <= 0f) return
    drawBar(expandedRect, color, density, topOnly = false)
}

private fun DrawScope.drawBar(
    rect: Rect,
    color: Color,
    density: androidx.compose.ui.unit.Density,
    topOnly: Boolean = true,
) {
    val cornerPercent = 25
    val shape = if (topOnly) {
        SquircleShape(topStart = cornerPercent, topEnd = cornerPercent, bottomStart = 0, bottomEnd = 0)
    } else {
        SquircleShape(topStart = cornerPercent, topEnd = cornerPercent, bottomStart = cornerPercent, bottomEnd = cornerPercent)
    }
    val outline = shape.createOutline(
        size = Size(rect.width, rect.height),
        layoutDirection = LayoutDirection.Ltr,
        density = density,
    )
    val path = when (outline) {
        is androidx.compose.ui.graphics.Outline.Rounded -> Path().apply {
            addRoundRect(outline.roundRect)
        }
        is androidx.compose.ui.graphics.Outline.Generic -> outline.path
        is androidx.compose.ui.graphics.Outline.Rectangle -> Path().apply {
            addRect(outline.rect)
        }
    }
    translate(left = rect.left, top = rect.top) {
        drawPath(path = path, color = color)
    }
}

// --- Donut chart drawing ---

/**
 * Internal data class representing one computed arc slice for the donut renderer.
 *
 * Angles use "top-up" convention: 0° = 12 o'clock, positive = clockwise.
 * The renderer subtracts 90° when calling [drawArc] to convert to Compose's
 * native 0° = 3 o'clock convention.
 */
internal data class DonutSlice(
    val seriesIndex: Int,
    val color: Color?,          // null → resolve via defaultSeriesColor at draw time
    val startAngleDeg: Float,   // 0° = top of circle (12 o'clock); CW positive
    val sweepAngleDeg: Float,   // strictly > 0 for slices that draw
)

/**
 * Computes the effective total denominator for the donut ring.
 *
 * - When [explicit] is null → use [sliceSum].
 * - When [explicit] < [sliceSum] → fall back to [sliceSum] (a slice can never exceed the ring).
 * - When [explicit] > 0 and >= [sliceSum] → use [explicit] (allows headroom/gauge mode).
 */
internal fun donutTotal(explicit: Float?, sliceSum: Float): Float =
    explicit?.takeIf { it > 0f && it >= sliceSum } ?: sliceSum

/**
 * Computes the arc slices for a donut chart from a list of series.
 *
 * Each series contributes `values[0]` as its slice value. Other indices are ignored.
 * Negative values are clamped to 0. NaN values are treated as 0 and excluded.
 *
 * - Empty series list or all-zero values → returns empty list (caller draws track only).
 * - [explicitTotal] < slice sum → falls back to slice sum (via [donutTotal]).
 * - Angles use "top-up" convention (0° = 12 o'clock, CW positive); renderer converts for Compose.
 */
internal fun computeDonutSlices(
    series: List<PopChartSeries>,
    explicitTotal: Float?,
): List<DonutSlice> {
    // Extract (index, value, color) triples; clamp negative and NaN to 0
    data class Triple(val index: Int, val value: Float, val color: Color?)
    val triples = series.mapIndexed { i, s ->
        val raw = s.values.firstOrNull() ?: 0f
        val value = if (raw.isNaN() || raw < 0f) 0f else raw
        Triple(i, value, s.color)
    }
    val sliceSum = triples.sumOf { it.value.toDouble() }.toFloat()
    if (sliceSum <= 0f) return emptyList()

    val effectiveTotal = donutTotal(explicitTotal, sliceSum)
    var runningAngle = 0f
    return buildList {
        for (triple in triples) {
            val sweep = (triple.value / effectiveTotal) * 360f
            if (sweep <= 0f) continue
            add(
                DonutSlice(
                    seriesIndex = triple.index,
                    color = triple.color,
                    startAngleDeg = runningAngle,
                    sweepAngleDeg = sweep,
                ),
            )
            runningAngle += sweep
        }
    }
}

/**
 * Draws the donut chart on the current [DrawScope].
 *
 * Drawing pipeline:
 * 1. Compute geometry from canvas [size].
 * 2. Draw a full-circle track in `surfaceContainerHigh` (tonal "available" ring).
 * 3. Compute slices via [computeDonutSlices]. If empty → return (track-only placeholder).
 * 4. Draw non-active slices in series order.
 * 5. Draw active slice last with layered neon halos (Rule 4 + Rule 5).
 *
 * Note: `StrokeCap.Round` adds a half-circle cap at each arc end. At 100% coverage
 * the caps slightly overlap at the 12 o'clock seam — this matches the Stitch design's
 * `stroke-linecap="round"` behavior and is intentional.
 *
 * `xLabels` are ignored for this style (no x-axis on a donut).
 */
private fun DrawScope.drawDonutChart(
    series: List<PopChartSeries>,
    style: PopChartStyle.Donut,
    colorScheme: ColorScheme,
) {
    val diameter = min(size.width, size.height)
    val strokeWidthPx = diameter * style.strokeRatio
    val outerRadius = diameter / 2f
    val centerRadius = outerRadius - strokeWidthPx / 2f
    val center = Offset(size.width / 2f, size.height / 2f)
    val arcRect = Rect(
        center.x - centerRadius,
        center.y - centerRadius,
        center.x + centerRadius,
        center.y + centerRadius,
    )

    // 2. Draw full-circle track (tonal background ring)
    drawArc(
        color = colorScheme.surfaceContainerHigh,
        startAngle = 0f,
        sweepAngle = 360f,
        useCenter = false,
        topLeft = arcRect.topLeft,
        size = arcRect.size,
        style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round),
    )

    // 3. Compute slices
    val slices = computeDonutSlices(series, style.total)
    if (slices.isEmpty()) return  // track-only placeholder

    // 5. Determine active slice
    val activeClamped = clampActiveIndex(style.activeIndex, slices.size)

    // 6. Draw non-active slices first
    slices.forEachIndexed { i, slice ->
        if (i == activeClamped) return@forEachIndexed
        val sliceColor = slice.color ?: defaultSeriesColor(slice.seriesIndex, colorScheme)
        // Convert top-up angle to Compose's 3-o'clock convention by subtracting 90°
        drawArc(
            color = sliceColor,
            startAngle = slice.startAngleDeg - 90f,
            sweepAngle = slice.sweepAngleDeg,
            useCenter = false,
            topLeft = arcRect.topLeft,
            size = arcRect.size,
            style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round),
        )
    }

    // 7. Draw active slice last with glow halos (Rule 4 + Rule 5)
    if (activeClamped != null) {
        val activeSlice = slices[activeClamped]
        val sliceColor = activeSlice.color ?: defaultSeriesColor(activeSlice.seriesIndex, colorScheme)
        val composeStart = activeSlice.startAngleDeg - 90f
        val sweep = activeSlice.sweepAngleDeg

        // 7a. Outer glow halo — inflates 8dp on each side
        drawArc(
            color = sliceColor.copy(alpha = 0.18f),
            startAngle = composeStart,
            sweepAngle = sweep,
            useCenter = false,
            topLeft = arcRect.topLeft,
            size = arcRect.size,
            style = Stroke(width = strokeWidthPx + 16.dp.toPx(), cap = StrokeCap.Round),
        )
        // 7b. Inner glow halo — inflates 4dp on each side
        drawArc(
            color = sliceColor.copy(alpha = 0.28f),
            startAngle = composeStart,
            sweepAngle = sweep,
            useCenter = false,
            topLeft = arcRect.topLeft,
            size = arcRect.size,
            style = Stroke(width = strokeWidthPx + 8.dp.toPx(), cap = StrokeCap.Round),
        )
        // 7c. Active slice at 1.02× stroke width (subtle radial bulge — Rule 5)
        drawArc(
            color = sliceColor,
            startAngle = composeStart,
            sweepAngle = sweep,
            useCenter = false,
            topLeft = arcRect.topLeft,
            size = arcRect.size,
            style = Stroke(width = strokeWidthPx * 1.02f, cap = StrokeCap.Round),
        )
    }
}

// --- Internal helpers (unit-testable) ---

/**
 * Maps a series of y-values to canvas-space [Offset]s.
 *
 * - Empty input → empty output.
 * - Single value → x at `width / 2`, y derived from yMin/yMax.
 * - Flat series (yMin == yMax) → all points at y = `height * 0.5f`.
 * - NaN values: filtered out; their x positions are skipped.
 * - Y is inverted: higher value → smaller y (canvas origin is top-left).
 */
internal fun normalizePoints(
    values: List<Float>,
    yMin: Float,
    yMax: Float,
    width: Float,
    height: Float,
): List<Offset> {
    val filtered = values.filter { !it.isNaN() }
    if (filtered.isEmpty()) return emptyList()
    val n = filtered.size
    val yRange = yMax - yMin
    return filtered.mapIndexed { i, v ->
        val x = if (n == 1) width / 2f else i * (width / (n - 1).toFloat())
        val y = if (yRange == 0f) height * 0.5f else height - ((v - yMin) / yRange) * height
        Offset(x, y)
    }
}

/**
 * Computes global y-bounds across all series, with 10% vertical padding.
 *
 * - Empty / all-NaN series → returns `Pair(0f, 1f)` as a sentinel.
 * - Single unique value → range extended by ±0.5 before padding.
 */
internal fun computeYBounds(series: List<PopChartSeries>): Pair<Float, Float> {
    val allValues = series.flatMap { it.values }.filter { !it.isNaN() }
    if (allValues.isEmpty()) return Pair(0f, 1f)
    var minimum = allValues.min()
    var maximum = allValues.max()
    if (minimum == maximum) {
        minimum -= 0.5f
        maximum += 0.5f
    }
    val padding = (maximum - minimum) * 0.10f
    return Pair(minimum - padding, maximum + padding)
}

/**
 * Computes y-bounds for stacked bar charts.
 *
 * For each x-tick, sums non-NaN values across all series to get the stacked total.
 * The minimum is clamped to ≤ 0 (stacks sit on 0 for positive data; negative values
 * are out of scope for v1 — behavior is undefined if values < 0).
 *
 * - Empty / all-NaN → returns `Pair(0f, 1f)` sentinel.
 * - Applies 10% top padding on the maximum, same as [computeYBounds].
 */
internal fun computeStackedYBounds(series: List<PopChartSeries>): Pair<Float, Float> {
    val maxLen = series.maxOfOrNull { it.values.size } ?: 0
    if (maxLen == 0) return Pair(0f, 1f)

    var maxStack = Float.NEGATIVE_INFINITY
    var minTickValue = Float.POSITIVE_INFINITY
    var hasAnyValue = false

    for (i in 0 until maxLen) {
        var tickSum = 0f
        var tickHasValue = false
        for (s in series) {
            val v = s.values.getOrNull(i) ?: continue
            if (v.isNaN()) continue
            tickSum += v
            if (v < minTickValue) minTickValue = v
            tickHasValue = true
            hasAnyValue = true
        }
        if (tickHasValue && tickSum > maxStack) maxStack = tickSum
    }

    if (!hasAnyValue) return Pair(0f, 1f)

    val minBound = min(0f, minTickValue)
    val padding = maxStack * 0.10f
    return Pair(minBound, maxStack + padding)
}

/**
 * Clamps [activeIndex] to `0..seriesSize-1`.
 *
 * - null input → null output.
 * - seriesSize == 0 → null (no valid index exists).
 * - negative activeIndex → null (out of range, not clamped to 0).
 * - activeIndex >= seriesSize → null (out of range, not clamped to last).
 */
internal fun clampActiveIndex(activeIndex: Int?, seriesSize: Int): Int? {
    if (activeIndex == null) return null
    if (seriesSize == 0) return null
    if (activeIndex < 0 || activeIndex >= seriesSize) return null
    return activeIndex
}

/**
 * Computes Fritsch-Carlson monotone cubic tangents for a list of points.
 *
 * Guarantees no overshoot for monotone input sequences.
 * Returns one tangent [Offset] per input point (dx is horizontal step, dy is the tangent slope * dx).
 *
 * - Single point → returns a single zero tangent.
 * - Two points → both tangents equal the chord slope.
 */
internal fun monotoneCubicTangents(points: List<Offset>): List<Offset> {
    val n = points.size
    if (n == 0) return emptyList()
    if (n == 1) return listOf(Offset(0f, 0f))

    // Step 1: chord slopes
    val deltas = (0 until n - 1).map { i ->
        val dx = points[i + 1].x - points[i].x
        val dy = points[i + 1].y - points[i].y
        if (dx != 0f) dy / dx else 0f
    }

    // Step 2: initial tangents — average of adjacent chords (Catmull-Rom style)
    val tangents = MutableList(n) { 0f }
    tangents[0] = deltas[0]
    tangents[n - 1] = deltas[n - 2]
    for (i in 1 until n - 1) {
        tangents[i] = (deltas[i - 1] + deltas[i]) / 2f
    }

    // Step 3: Fritsch-Carlson monotonicity constraint
    for (i in 0 until n - 1) {
        val d = deltas[i]
        if (abs(d) < 1e-10f) {
            tangents[i] = 0f
            tangents[i + 1] = 0f
        } else {
            val a = tangents[i] / d
            val b = tangents[i + 1] / d
            val h = a * a + b * b
            if (h > 9f) {
                val t = 3f / kotlin.math.sqrt(h)
                tangents[i] = t * a * d
                tangents[i + 1] = t * b * d
            }
        }
    }

    // Convert to Offset form: dx = horizontal step per segment, dy = tangent * dx
    return points.mapIndexed { i, _ ->
        val dx = when (i) {
            0 -> points[1].x - points[0].x
            n - 1 -> points[n - 1].x - points[n - 2].x
            else -> (points[i + 1].x - points[i - 1].x) / 2f
        }
        Offset(dx, tangents[i] * dx)
    }
}

/**
 * Returns the default series color for a given index from the theme.
 *
 * - 0 → `primaryContainer` (Electric Lime)
 * - 1 → `tertiaryContainer` (Cyber Cyan)
 * - 2 → `secondaryContainer` (Magenta)
 * - 3+ → cycles back (index % 3)
 */
internal fun defaultSeriesColor(index: Int, scheme: ColorScheme): Color {
    return when (index % 3) {
        0 -> scheme.primaryContainer
        1 -> scheme.tertiaryContainer
        else -> scheme.secondaryContainer
    }
}

/**
 * Builds a canvas path for the given points using the specified smoothness algorithm.
 *
 * - [PopChartStyle.Smoothness.None] → straight segments.
 * - [PopChartStyle.Smoothness.Normal] → monotone-cubic (Fritsch-Carlson), no overshoot.
 * - [PopChartStyle.Smoothness.High] → Catmull-Rom tension=0.5, may overshoot on spikes (intentional).
 */
internal fun buildLinePath(points: List<Offset>, smoothness: PopChartStyle.Smoothness): Path =
    when (smoothness) {
        PopChartStyle.Smoothness.None -> buildStraightPath(points)
        PopChartStyle.Smoothness.Normal -> buildSmoothPath(points)
        PopChartStyle.Smoothness.High -> buildCatmullRomPath(points, tension = 0.5f)
    }

/**
 * Converts Catmull-Rom neighbors to cubic Bezier control points.
 *
 * Given four points `prev` (p0), `p1`, `p2`, `next` (p3) and a tension value,
 * the control points are computed as:
 * ```
 * scale = (1 - tension) / 3
 * c1 = p1 + (p2 - prev) * scale
 * c2 = p2 - (next - p1) * scale
 * ```
 * At tension = 0.5 this reduces to `c1 = p1 + (p2 - p0) / 6` and `c2 = p2 - (p3 - p1) / 6`.
 * At tension = 0 both control points equal their respective endpoints (straight cubic).
 *
 * End-segment callers should pass reflected phantom points for `prev`/`next`
 * (see [buildCatmullRomPath]).
 */
internal fun catmullRomControlPoints(
    prev: Offset,
    p1: Offset,
    p2: Offset,
    next: Offset,
    tension: Float = 0.5f,
): Pair<Offset, Offset> {
    val scale = (1f - tension) / 3f
    val c1 = p1 + (p2 - prev) * scale
    val c2 = p2 - (next - p1) * scale
    return Pair(c1, c2)
}

/**
 * A slot representing one bar (one series in one cluster) in a bar chart.
 *
 * The [rect] carries `left` and `right` in canvas-X space; `top` and `bottom`
 * are always 0f — the caller fills Y from value space during rendering.
 */
internal data class BarSlot(val clusterIndex: Int, val seriesIndex: Int, val rect: Rect)

/**
 * Computes per-cluster, per-series bar slot rectangles in canvas-X space.
 *
 * Returns slots in cluster-major, series-minor order. Rects carry `left`/`right`
 * canvas X coordinates. `top` and `bottom` are always 0 — the drawing code fills
 * Y from value space.
 *
 * Returns an empty list if any of: `clusterCount <= 0`, `seriesCount <= 0`,
 * `totalWidth <= 0`, or the computed `barWidth <= 0`.
 */
internal fun computeBarSlots(
    clusterCount: Int,
    seriesCount: Int,
    grouping: PopChartStyle.Grouping,
    totalWidth: Float,
    clusterGap: Float,
    barGap: Float,
): List<BarSlot> {
    if (clusterCount <= 0 || seriesCount <= 0 || totalWidth <= 0f) return emptyList()

    return when (grouping) {
        PopChartStyle.Grouping.Clustered -> {
            val usableWidth = totalWidth - clusterGap * (clusterCount - 1)
            val clusterWidth = usableWidth / clusterCount
            val barBlockWidth = clusterWidth - barGap * (seriesCount - 1)
            val barWidth = barBlockWidth / seriesCount
            if (barWidth <= 0f) return emptyList()

            val slots = mutableListOf<BarSlot>()
            for (c in 0 until clusterCount) {
                val clusterLeft = c * (clusterWidth + clusterGap)
                for (s in 0 until seriesCount) {
                    val barLeft = clusterLeft + s * (barWidth + barGap)
                    slots.add(BarSlot(c, s, Rect(barLeft, 0f, barLeft + barWidth, 0f)))
                }
            }
            slots
        }
        PopChartStyle.Grouping.Stacked -> {
            val usableWidth = totalWidth - clusterGap * (clusterCount - 1)
            val clusterWidth = usableWidth / clusterCount
            val barWidth = clusterWidth
            if (barWidth <= 0f) return emptyList()

            val slots = mutableListOf<BarSlot>()
            for (c in 0 until clusterCount) {
                val clusterLeft = c * (clusterWidth + clusterGap)
                for (s in 0 until seriesCount) {
                    slots.add(BarSlot(c, s, Rect(clusterLeft, 0f, clusterLeft + barWidth, 0f)))
                }
            }
            slots
        }
    }
}

// --- Private path builders ---

private fun buildSmoothPath(points: List<Offset>): Path {
    val path = Path()
    if (points.isEmpty()) return path
    if (points.size == 1) {
        path.moveTo(points[0].x, points[0].y)
        return path
    }
    val tangents = monotoneCubicTangents(points)
    path.moveTo(points[0].x, points[0].y)
    for (i in 0 until points.size - 1) {
        val p0 = points[i]
        val p1 = points[i + 1]
        val dx = (p1.x - p0.x) / 3f
        val cp1x = p0.x + dx
        val cp1y = p0.y + tangents[i].y / 3f
        val cp2x = p1.x - dx
        val cp2y = p1.y - tangents[i + 1].y / 3f
        path.cubicTo(cp1x, cp1y, cp2x, cp2y, p1.x, p1.y)
    }
    return path
}

private fun buildStraightPath(points: List<Offset>): Path {
    val path = Path()
    if (points.isEmpty()) return path
    path.moveTo(points[0].x, points[0].y)
    for (i in 1 until points.size) {
        path.lineTo(points[i].x, points[i].y)
    }
    return path
}

private fun buildCatmullRomPath(points: List<Offset>, tension: Float): Path {
    val path = Path()
    if (points.isEmpty()) return path
    if (points.size == 1) {
        path.moveTo(points[0].x, points[0].y)
        return path
    }
    val n = points.size
    path.moveTo(points[0].x, points[0].y)
    for (i in 0 until n - 1) {
        val p0 = if (i == 0) points[0] + (points[0] - points[1]) else points[i - 1]
        val p1 = points[i]
        val p2 = points[i + 1]
        val p3 = if (i == n - 2) points[n - 1] + (points[n - 1] - points[n - 2]) else points[i + 2]
        val (c1, c2) = catmullRomControlPoints(p0, p1, p2, p3, tension)
        path.cubicTo(c1.x, c1.y, c2.x, c2.y, p2.x, p2.y)
    }
    return path
}

// --- Glow drawing ---

private fun drawGlow(
    drawScope: DrawScope,
    center: Offset,
    color: Color,
    colorScheme: ColorScheme,
) {
    with(drawScope) {
        // Layered concentric circles (Rule 4 — Neon Glow)
        drawCircle(color = color.copy(alpha = 0.15f), radius = 18.dp.toPx(), center = center)
        drawCircle(color = color.copy(alpha = 0.25f), radius = 12.dp.toPx(), center = center)
        drawCircle(color = color.copy(alpha = 0.40f), radius = 8.dp.toPx(), center = center)
        // Surface-colored ring around core (punched-out look)
        drawCircle(color = colorScheme.surface, radius = 5.dp.toPx(), center = center)
        // Core marker
        drawCircle(color = color, radius = 3.dp.toPx(), center = center)
    }
}
