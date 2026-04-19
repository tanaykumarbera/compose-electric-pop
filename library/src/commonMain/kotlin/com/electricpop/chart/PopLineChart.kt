package com.electricpop.chart

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.electricpop.theme.ElectricPopTheme
import kotlin.math.abs

@Immutable
data class PopLineChartSeries(
    val label: String,
    val points: List<Float>,
    val role: SeriesRole = SeriesRole.Primary,
)

enum class SeriesRole { Primary, Secondary }

internal data class NormalizedSeries(
    val role: SeriesRole,
    val sampledOffsets: List<Offset?>, // null for NaN / gap
)

internal fun autoYRange(series: List<PopLineChartSeries>): ClosedFloatingPointRange<Float> {
    val finiteValues = series
        .flatMap { it.points }
        .filter { it.isFinite() }

    if (finiteValues.isEmpty()) return 0f..1f

    val min = finiteValues.min()
    val max = finiteValues.max()

    return if (min == max) {
        (min - 1f)..(max + 1f)
    } else {
        min..max
    }
}

internal fun defaultYAxisFormatter(value: Float): String {
    val absVal = abs(value)
    val sign = if (value < 0) "-" else ""

    return when {
        absVal >= 1_000_000f -> {
            val v = absVal / 1_000_000f
            val formatted = formatWithOneDec(v)
            "${sign}${formatted}M"
        }
        absVal >= 1_000f -> {
            val v = absVal / 1_000f
            val formatted = formatWithOneDec(v)
            "${sign}${formatted}K"
        }
        else -> {
            if (value.toInt().toFloat() == value) {
                value.toInt().toString()
            } else {
                val intPart = value.toInt()
                val dec = abs(value - intPart)
                val decRounded = (dec * 10).toInt()
                "${intPart}.${decRounded}"
            }
        }
    }
}

private fun formatWithOneDec(v: Float): String {
    val intPart = v.toInt()
    val dec = (v - intPart)
    val decRounded = (dec * 10).toInt()
    return if (decRounded == 0) {
        intPart.toString()
    } else {
        "${intPart}.${decRounded}"
    }
}

internal fun normalizeSeries(
    series: List<PopLineChartSeries>,
    plotSize: Size,
    yRange: ClosedFloatingPointRange<Float>?,
): List<NormalizedSeries> {
    if (series.isEmpty()) return emptyList()

    val range = yRange ?: autoYRange(series)
    val rangeSpan = range.endInclusive - range.start

    return series.map { s ->
        val offsets = s.points.mapIndexed { i, v ->
            if (!v.isFinite()) {
                null
            } else {
                val x = if (plotSize.width == 0f) {
                    0f
                } else if (s.points.size == 1) {
                    plotSize.width / 2f
                } else {
                    plotSize.width * i.toFloat() / s.points.lastIndex.toFloat()
                }
                val y = if (plotSize.height == 0f || rangeSpan == 0f) {
                    0f
                } else {
                    plotSize.height * (1f - (v - range.start) / rangeSpan)
                }
                Offset(x, y)
            }
        }
        NormalizedSeries(role = s.role, sampledOffsets = offsets)
    }
}

@Composable
fun PopLineChart(
    series: List<PopLineChartSeries>,
    xAxisLabels: List<String>,
    modifier: Modifier = Modifier,
    activePointIndex: Int? = null,
    yAxisFormatter: (Float) -> String = { defaultYAxisFormatter(it) },
    showYAxisLabels: Boolean = true,
    showXAxisLabels: Boolean = true,
    showLegend: Boolean = true,
    yRange: ClosedFloatingPointRange<Float>? = null,
    chartHeight: Dp = 220.dp,
    animationsEnabled: Boolean = true,
    contentDescription: String? = null,
) {
    val spacing = ElectricPopTheme.spacing
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    val semanticsModifier = if (contentDescription != null) {
        modifier.semantics {
            this.contentDescription = contentDescription
        }
    } else {
        modifier
    }

    Column(modifier = semanticsModifier) {
        // Legend row (only when multiple series)
        if (showLegend && series.size > 1) {
            LegendRow(series = series, spacing = spacing, colorScheme = colorScheme, typography = typography)
        }

        // Chart row: Y-axis labels + Canvas
        Row(modifier = Modifier.height(chartHeight)) {
            if (showYAxisLabels) {
                val resolvedYRange = yRange ?: autoYRange(series)
                YAxisLabelsColumn(
                    yRange = resolvedYRange,
                    yAxisFormatter = yAxisFormatter,
                    colorScheme = colorScheme,
                    typography = typography,
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
            ) {
                val isEmpty = series.isEmpty() || series.all { it.points.isEmpty() }

                if (isEmpty) {
                    Text(
                        text = "—",
                        style = typography.labelSmall,
                        color = colorScheme.onSurfaceVariant,
                        modifier = Modifier.align(Alignment.Center),
                    )
                }

                // Reveal animation
                val reveal = remember { Animatable(if (animationsEnabled) 0f else 1f) }
                LaunchedEffect(series) {
                    if (animationsEnabled) {
                        reveal.snapTo(0f)
                        reveal.animateTo(
                            targetValue = 1f,
                            animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
                        )
                    } else {
                        reveal.snapTo(1f)
                    }
                }

                // Pulse animation for active point
                val infiniteTransition = rememberInfiniteTransition(label = "pulse")
                val pulse = if (animationsEnabled) {
                    infiniteTransition.animateFloat(
                        initialValue = 1f,
                        targetValue = 1.15f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(1200, easing = FastOutSlowInEasing),
                            repeatMode = RepeatMode.Reverse,
                        ),
                        label = "pulse",
                    ).value
                } else {
                    1.0f
                }

                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = spacing.xs, vertical = spacing.xs),
                ) {
                    val resolvedYRange = yRange ?: autoYRange(series)
                    val normalizedList = normalizeSeries(series.take(2), size, resolvedYRange)

                    // Tick dots at 4 Y-levels (No-Line Rule: dots instead of gridlines)
                    val yLevels = listOf(0f, 1f / 3f, 2f / 3f, 1f)
                    for (level in yLevels) {
                        val y = size.height * level
                        drawCircle(
                            color = colorScheme.surfaceContainerHigh,
                            radius = 2.dp.toPx(),
                            center = Offset(0f, y),
                        )
                    }

                    clipRect(right = size.width * reveal.value) {
                        // Draw secondary series first so primary lands on top
                        val sortedSeries = normalizedList.sortedByDescending { it.role == SeriesRole.Primary }

                        for ((idx, normalized) in sortedSeries.withIndex()) {
                            val originalSeries = series.take(2).firstOrNull { it.role == normalized.role }
                                ?: continue
                            val seriesColor = when (normalized.role) {
                                SeriesRole.Primary -> colorScheme.primary
                                SeriesRole.Secondary -> colorScheme.tertiary
                            }

                            // Area fill for primary series
                            if (normalized.role == SeriesRole.Primary) {
                                val validOffsets = normalized.sampledOffsets.filterNotNull()
                                if (validOffsets.isNotEmpty()) {
                                    val areaPath = Path()
                                    val firstOffset = normalized.sampledOffsets.indexOfFirst { it != null }
                                    val lastOffset = normalized.sampledOffsets.indexOfLast { it != null }

                                    var started = false
                                    for (offset in normalized.sampledOffsets) {
                                        if (offset != null) {
                                            if (!started) {
                                                areaPath.moveTo(offset.x, offset.y)
                                                started = true
                                            } else {
                                                // TODO(smoothing): cubic bezier
                                                areaPath.lineTo(offset.x, offset.y)
                                            }
                                        }
                                    }

                                    val firstValidOffset = normalized.sampledOffsets[firstOffset]!!
                                    val lastValidOffset = normalized.sampledOffsets[lastOffset]!!
                                    areaPath.lineTo(lastValidOffset.x, size.height)
                                    areaPath.lineTo(firstValidOffset.x, size.height)
                                    areaPath.close()

                                    drawPath(
                                        path = areaPath,
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                seriesColor.copy(alpha = 0.18f),
                                                seriesColor.copy(alpha = 0f),
                                            ),
                                            startY = 0f,
                                            endY = size.height,
                                        ),
                                    )
                                }
                            }

                            // Line path
                            val linePath = Path()
                            var lineStarted = false
                            for (offset in normalized.sampledOffsets) {
                                if (offset != null) {
                                    if (!lineStarted) {
                                        linePath.moveTo(offset.x, offset.y)
                                        lineStarted = true
                                    } else {
                                        // TODO(smoothing): cubic bezier
                                        linePath.lineTo(offset.x, offset.y)
                                    }
                                } else {
                                    // Gap — reset for next sub-path
                                    lineStarted = false
                                }
                            }
                            drawPath(
                                path = linePath,
                                color = seriesColor,
                                style = Stroke(
                                    width = 3.dp.toPx(),
                                    cap = StrokeCap.Round,
                                    join = StrokeJoin.Round,
                                ),
                            )

                            // Point dots
                            for ((pointIdx, offset) in normalized.sampledOffsets.withIndex()) {
                                if (offset != null) {
                                    val isActive = pointIdx == activePointIndex
                                    val dotAlpha = when {
                                        activePointIndex == null -> 1f
                                        isActive -> 1f
                                        else -> 0.6f
                                    }
                                    drawCircle(
                                        color = seriesColor.copy(alpha = dotAlpha),
                                        radius = 4.dp.toPx(),
                                        center = offset,
                                    )
                                }
                            }
                        }

                        // Active point glow + solid dot
                        if (activePointIndex != null) {
                            // Find primary series first, fallback to first series
                            val primaryNormalized = normalizedList.firstOrNull { it.role == SeriesRole.Primary }
                                ?: normalizedList.firstOrNull()

                            if (primaryNormalized != null &&
                                activePointIndex in primaryNormalized.sampledOffsets.indices
                            ) {
                                val activeOffset = primaryNormalized.sampledOffsets[activePointIndex]
                                if (activeOffset != null) {
                                    val activeColor = colorScheme.primary

                                    // Concentric glow circles
                                    drawCircle(
                                        color = activeColor.copy(alpha = 0.08f),
                                        radius = 20.dp.toPx() * pulse,
                                        center = activeOffset,
                                    )
                                    drawCircle(
                                        color = activeColor.copy(alpha = 0.14f),
                                        radius = 14.dp.toPx() * pulse,
                                        center = activeOffset,
                                    )
                                    drawCircle(
                                        color = activeColor.copy(alpha = 0.22f),
                                        radius = 10.dp.toPx() * pulse,
                                        center = activeOffset,
                                    )
                                    // Solid dot (fixed, not scaled)
                                    drawCircle(
                                        color = activeColor,
                                        radius = 6.dp.toPx(),
                                        center = activeOffset,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // X-axis labels
        if (showXAxisLabels) {
            XAxisLabelsRow(
                xAxisLabels = xAxisLabels,
                activePointIndex = activePointIndex,
                showYAxisLabels = showYAxisLabels,
                spacing = spacing,
                colorScheme = colorScheme,
                typography = typography,
            )
        }
    }
}

@Composable
private fun LegendRow(
    series: List<PopLineChartSeries>,
    spacing: com.electricpop.theme.ElectricPopSpacing,
    colorScheme: androidx.compose.material3.ColorScheme,
    typography: androidx.compose.material3.Typography,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = spacing.xs),
        horizontalArrangement = Arrangement.spacedBy(spacing.md, alignment = Alignment.End),
    ) {
        for (s in series.take(2)) {
            val seriesColor = when (s.role) {
                SeriesRole.Primary -> colorScheme.primary
                SeriesRole.Secondary -> colorScheme.tertiary
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(spacing.xxs),
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(seriesColor),
                )
                Text(
                    text = s.label.uppercase(),
                    style = typography.labelSmall,
                    color = colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun YAxisLabelsColumn(
    yRange: ClosedFloatingPointRange<Float>,
    yAxisFormatter: (Float) -> String,
    colorScheme: androidx.compose.material3.ColorScheme,
    typography: androidx.compose.material3.Typography,
) {
    val levels = listOf(
        yRange.endInclusive,
        yRange.start + (yRange.endInclusive - yRange.start) * 2f / 3f,
        yRange.start + (yRange.endInclusive - yRange.start) * 1f / 3f,
        yRange.start,
    )
    Column(
        modifier = Modifier
            .width(48.dp)
            .fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        for (level in levels) {
            Text(
                text = yAxisFormatter(level).uppercase(),
                style = typography.labelSmall,
                color = colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            )
        }
    }
}

@Composable
private fun XAxisLabelsRow(
    xAxisLabels: List<String>,
    activePointIndex: Int?,
    showYAxisLabels: Boolean,
    spacing: com.electricpop.theme.ElectricPopSpacing,
    colorScheme: androidx.compose.material3.ColorScheme,
    typography: androidx.compose.material3.Typography,
) {
    val startPadding = if (showYAxisLabels) 48.dp else 0.dp
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = startPadding),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        xAxisLabels.forEachIndexed { i, label ->
            val isActive = i == activePointIndex
            Text(
                text = label.uppercase(),
                style = typography.labelSmall,
                color = if (isActive) colorScheme.onSurface else colorScheme.onSurfaceVariant,
            )
        }
    }
}
