package com.electricpop.chart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.electricpop.foundation.PopSurface
import com.electricpop.foundation.PopSurfaceTone
import com.electricpop.theme.ElectricPopTheme
import kotlin.math.abs

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
 * A kinetic line chart.
 *
 * Draws one or more trend lines on a [Canvas]. Follows the Electric Pop chart rules:
 * - **No gridlines.** Scale is implied by tonal background + spacing.
 * - **Glow emission.** When [activeIndex] is non-null, the corresponding data point
 *   on the first series is highlighted with a layered neon glow (Rule 4).
 * - **Tonal container.** By default the chart is wrapped in a [PopSurface]
 *   (`surfaceContainerLow`, squircle `extraSmall`). Pass `embedded = true` to skip
 *   the container — useful when composing the chart inside another card.
 *
 * @param series One or more [PopChartSeries]. Empty list or all-empty series → placeholder is drawn.
 * @param modifier Modifier for the outermost container.
 * @param xLabels Optional labels drawn below the canvas, evenly spaced. If provided,
 *   length must match the max series length; extras are ignored; shorter is truncated.
 * @param title Optional uppercase headline drawn above the chart.
 * @param subtitle Optional supporting label (uppercase, labelSmall) shown under the title.
 * @param activeIndex Data index highlighted with a glowing marker on the first series.
 *   Coerced into `0..values.lastIndex` (or ignored if out of range for an empty series).
 * @param smooth Whether to render a smooth cubic path (monotone-cubic) through data
 *   points. When false, straight segments are drawn.
 * @param showArea Whether to draw a tonal area fill under each line (~12% alpha of the
 *   series color, fading to transparent at the bottom).
 * @param embedded When true, omits the outer PopSurface container so the chart can be
 *   placed inside another card. Default false (self-contained).
 * @param chartHeight Fixed canvas height. Defaults to 180.dp.
 * @param contentPadding Padding inside the surface around title/chart/labels.
 *   Default `PaddingValues(all = ElectricPopTheme.spacing.xl)`.
 *
 * NaN values in series data are filtered out at path-building stage and treated as
 * segment breaks (`moveTo` for the next valid point).
 */
@Composable
fun PopChart(
    series: List<PopChartSeries>,
    modifier: Modifier = Modifier,
    xLabels: List<String> = emptyList(),
    title: String? = null,
    subtitle: String? = null,
    activeIndex: Int? = null,
    smooth: Boolean = true,
    showArea: Boolean = false,
    embedded: Boolean = false,
    chartHeight: Dp = 180.dp,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = ElectricPopTheme.spacing.xl,
        vertical = ElectricPopTheme.spacing.xl,
    ),
) {
    val spacing = ElectricPopTheme.spacing
    val colorScheme = MaterialTheme.colorScheme

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
            val (yMin, yMax) = computeYBounds(series)

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

                series.forEachIndexed { seriesIdx, s ->
                    if (s.values.isEmpty()) return@forEachIndexed

                    val seriesColor = s.color ?: defaultSeriesColor(seriesIdx, colorScheme)
                    val points = normalizePoints(s.values, yMin, yMax, size.width, size.height)

                    // Area fill
                    if (showArea && points.size >= 2) {
                        val areaPath = if (smooth) buildSmoothPath(points) else buildStraightPath(points)
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
                        val linePath = if (smooth) buildSmoothPath(points) else buildStraightPath(points)
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
                    if (seriesIdx == 0 && activeIndex != null) {
                        val clamped = clampActiveIndex(activeIndex, s.values.size)
                        if (clamped != null && clamped < points.size) {
                            val activePoint = points[clamped]
                            drawGlow(this, activePoint, seriesColor, colorScheme)
                        }
                    }
                }
            }

            // X-axis labels
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
    var min = allValues.min()
    var max = allValues.max()
    if (min == max) {
        min -= 0.5f
        max += 0.5f
    }
    val padding = (max - min) * 0.10f
    return Pair(min - padding, max + padding)
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
    return points.mapIndexed { i, point ->
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
