package com.electricpop.chart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runDesktopComposeUiTest
import com.electricpop.theme.ElectricPopTheme
import io.github.takahirom.roborazzi.captureRoboImage
import kotlin.test.Test

class PopChartScreenshotTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun allVariants_light() = runDesktopComposeUiTest(width = 460, height = 5800) {
        setContent { PopChartContent(darkTheme = false) }
        onRoot().captureRoboImage(
            filePath = "src/desktopTest/snapshots/PopChart_allVariants_light.png",
        )
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun allVariants_dark() = runDesktopComposeUiTest(width = 460, height = 5800) {
        setContent { PopChartContent(darkTheme = true) }
        onRoot().captureRoboImage(
            filePath = "src/desktopTest/snapshots/PopChart_allVariants_dark.png",
        )
    }
}

private val primaryValues = listOf(42f, 58f, 51f, 67f, 73f, 68f, 82f, 95f, 88f, 79f, 71f, 84f)
private val secondaryValues = primaryValues.map { it * 0.75f }
private val xLabels = listOf("J", "F", "M", "A", "M", "J", "J", "A", "S", "O", "N", "D")

@Composable
private fun PopChartContent(darkTheme: Boolean) {
    ElectricPopTheme(darkTheme = darkTheme) {
        val spacing = ElectricPopTheme.spacing
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(spacing.md),
                verticalArrangement = Arrangement.spacedBy(spacing.md),
            ) {
                // ── LINE section ─────────────────────────────────────────────
                Text(
                    "LINE",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                // Variant 1: Single series, Normal smoothness, no area
                Text(
                    "SINGLE SERIES · NORMAL",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                PopChart(
                    series = listOf(PopChartSeries("Revenue", primaryValues)),
                    style = PopChartStyle.Line(smoothness = PopChartStyle.Smoothness.Normal),
                    xLabels = xLabels,
                    title = "Monthly Revenue",
                    subtitle = "Normal smoothness",
                )

                // Variant 2: Single series, Normal + area
                Text(
                    "SINGLE SERIES · NORMAL · AREA",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                PopChart(
                    series = listOf(PopChartSeries("Revenue", primaryValues)),
                    style = PopChartStyle.Line(smoothness = PopChartStyle.Smoothness.Normal, showArea = true),
                    xLabels = xLabels,
                    title = "Monthly Revenue",
                    subtitle = "With area fill",
                )

                // Variant 3: Two series, Normal, no area
                Text(
                    "TWO SERIES · NORMAL",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                PopChart(
                    series = listOf(
                        PopChartSeries("This Year", primaryValues),
                        PopChartSeries("Last Year", secondaryValues),
                    ),
                    style = PopChartStyle.Line(smoothness = PopChartStyle.Smoothness.Normal),
                    xLabels = xLabels,
                    title = "Year Comparison",
                )

                // Variant 4: Two series, Normal + area + activeIndex = 7
                Text(
                    "TWO SERIES · NORMAL · AREA · ACTIVE",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                PopChart(
                    series = listOf(
                        PopChartSeries("This Year", primaryValues),
                        PopChartSeries("Last Year", secondaryValues),
                    ),
                    style = PopChartStyle.Line(
                        smoothness = PopChartStyle.Smoothness.Normal,
                        showArea = true,
                        activeIndex = 7,
                    ),
                    xLabels = xLabels,
                    title = "Year Comparison",
                    subtitle = "August highlighted",
                )

                // Variant 5: Single series, None (straight)
                Text(
                    "SINGLE SERIES · NONE (STRAIGHT)",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                PopChart(
                    series = listOf(PopChartSeries("Revenue", primaryValues)),
                    style = PopChartStyle.Line(smoothness = PopChartStyle.Smoothness.None),
                    xLabels = xLabels,
                    title = "Monthly Revenue",
                    subtitle = "Straight segments",
                )

                // Variant 6: Single series, High (Catmull-Rom)
                Text(
                    "SINGLE SERIES · HIGH (CATMULL-ROM)",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                PopChart(
                    series = listOf(PopChartSeries("Revenue", primaryValues)),
                    style = PopChartStyle.Line(smoothness = PopChartStyle.Smoothness.High),
                    xLabels = xLabels,
                    title = "Monthly Revenue",
                    subtitle = "High smoothness (Catmull-Rom)",
                )

                Spacer(modifier = Modifier.height(spacing.sm))

                // ── BAR section ──────────────────────────────────────────────
                Text(
                    "BAR",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                // Variant 7: Single, Clustered
                Text(
                    "SINGLE SERIES · CLUSTERED",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                PopChart(
                    series = listOf(PopChartSeries("Revenue", primaryValues)),
                    style = PopChartStyle.Bar(grouping = PopChartStyle.Grouping.Clustered),
                    xLabels = xLabels,
                    title = "Monthly Revenue",
                    subtitle = "Clustered",
                )

                // Variant 8: Single, Clustered + activeIndex = 7
                Text(
                    "SINGLE SERIES · CLUSTERED · ACTIVE",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                PopChart(
                    series = listOf(PopChartSeries("Revenue", primaryValues)),
                    style = PopChartStyle.Bar(grouping = PopChartStyle.Grouping.Clustered, activeIndex = 7),
                    xLabels = xLabels,
                    title = "Monthly Revenue",
                    subtitle = "August highlighted",
                )

                // Variant 9: Two series, Clustered
                Text(
                    "TWO SERIES · CLUSTERED",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                PopChart(
                    series = listOf(
                        PopChartSeries("This Year", primaryValues),
                        PopChartSeries("Last Year", secondaryValues),
                    ),
                    style = PopChartStyle.Bar(grouping = PopChartStyle.Grouping.Clustered),
                    xLabels = xLabels,
                    title = "Year Comparison",
                    subtitle = "Clustered",
                )

                // Variant 10: Two series, Stacked
                Text(
                    "TWO SERIES · STACKED",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                PopChart(
                    series = listOf(
                        PopChartSeries("This Year", primaryValues),
                        PopChartSeries("Last Year", secondaryValues),
                    ),
                    style = PopChartStyle.Bar(grouping = PopChartStyle.Grouping.Stacked),
                    xLabels = xLabels,
                    title = "Year Comparison",
                    subtitle = "Stacked",
                )

                Spacer(modifier = Modifier.height(spacing.sm))

                // ── DONUT section ────────────────────────────────────────────
                Text(
                    "DONUT",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                // Variant 11: Gauge · 72%
                Text(
                    "GAUGE · 72%",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
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

                // Variant 12: Gauge · Active
                Text(
                    "GAUGE · ACTIVE",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
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

                // Variant 13: Pie · 4 slices
                Text(
                    "PIE · 4 SLICES",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
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

                // Variant 14: Pie · Active 1 (Engineering)
                Text(
                    "PIE · ACTIVE 1",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
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
        }
    }
}
