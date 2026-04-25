package com.electricpop.demo.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.electricpop.chart.PopChart
import com.electricpop.chart.PopChartSeries
import com.electricpop.chart.PopChartStyle
import com.electricpop.foundation.PopSurface
import com.electricpop.foundation.PopSurfaceTone
import com.electricpop.theme.ElectricPopTheme

private val primaryValues = listOf(42f, 58f, 51f, 67f, 73f, 68f, 82f, 95f, 88f, 79f, 71f, 84f)
private val secondaryValues = primaryValues.map { it * 0.75f }
private val xLabels = listOf("J", "F", "M", "A", "M", "J", "J", "A", "S", "O", "N", "D")

@Composable
fun PopChartDemo() {
    val spacing = ElectricPopTheme.spacing
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(spacing.lg),
        verticalArrangement = Arrangement.spacedBy(spacing.lg),
    ) {

        // ── LINE ─────────────────────────────────────────────────────────────
        Text(
            "LINE",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Column(verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
            // Single series, Normal, no area
            PopChart(
                series = listOf(PopChartSeries("Revenue", primaryValues)),
                style = PopChartStyle.Line(smoothness = PopChartStyle.Smoothness.Normal),
                xLabels = xLabels,
                title = "Monthly Revenue",
                subtitle = "Normal · No area",
            )
            // Single series, Normal, area fill
            PopChart(
                series = listOf(PopChartSeries("Revenue", primaryValues)),
                style = PopChartStyle.Line(smoothness = PopChartStyle.Smoothness.Normal, showArea = true),
                xLabels = xLabels,
                title = "Monthly Revenue",
                subtitle = "Normal · Area fill",
            )
            // Single series, None (straight)
            PopChart(
                series = listOf(PopChartSeries("Revenue", primaryValues)),
                style = PopChartStyle.Line(smoothness = PopChartStyle.Smoothness.None),
                xLabels = xLabels,
                title = "Monthly Revenue",
                subtitle = "None · Straight segments",
            )
            // Two series, Normal, no area
            PopChart(
                series = listOf(
                    PopChartSeries("This Year", primaryValues),
                    PopChartSeries("Last Year", secondaryValues),
                ),
                style = PopChartStyle.Line(smoothness = PopChartStyle.Smoothness.Normal),
                xLabels = xLabels,
                title = "Year Comparison",
                subtitle = "Normal · No area",
            )
            // Two series, Normal, area fill, activeIndex = 7
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
                subtitle = "Area fill · August highlighted",
            )
        }

        // ── SMOOTHNESS ───────────────────────────────────────────────────────
        Text(
            "SMOOTHNESS",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Column(verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
            PopChart(
                series = listOf(PopChartSeries("Revenue", primaryValues)),
                style = PopChartStyle.Line(smoothness = PopChartStyle.Smoothness.None),
                xLabels = xLabels,
                title = "Smoothness: None",
                subtitle = "Straight segments",
            )
            PopChart(
                series = listOf(PopChartSeries("Revenue", primaryValues)),
                style = PopChartStyle.Line(smoothness = PopChartStyle.Smoothness.Normal),
                xLabels = xLabels,
                title = "Smoothness: Normal",
                subtitle = "Monotone-cubic (Fritsch-Carlson)",
            )
            PopChart(
                series = listOf(PopChartSeries("Revenue", primaryValues)),
                style = PopChartStyle.Line(smoothness = PopChartStyle.Smoothness.High),
                xLabels = xLabels,
                title = "Smoothness: High",
                subtitle = "Catmull-Rom tension=0.5",
            )
        }

        // ── BAR ──────────────────────────────────────────────────────────────
        Text(
            "BAR",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Column(verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
            // Single, Clustered
            PopChart(
                series = listOf(PopChartSeries("Revenue", primaryValues)),
                style = PopChartStyle.Bar(grouping = PopChartStyle.Grouping.Clustered),
                xLabels = xLabels,
                title = "Clustered",
                subtitle = "Single series",
            )
            // Single, Clustered + active
            PopChart(
                series = listOf(PopChartSeries("Revenue", primaryValues)),
                style = PopChartStyle.Bar(grouping = PopChartStyle.Grouping.Clustered, activeIndex = 7),
                xLabels = xLabels,
                title = "Clustered · August active",
                subtitle = "Active glow on index 7",
            )
            // Two series, Clustered
            PopChart(
                series = listOf(
                    PopChartSeries("This Year", primaryValues),
                    PopChartSeries("Last Year", secondaryValues),
                ),
                style = PopChartStyle.Bar(grouping = PopChartStyle.Grouping.Clustered),
                xLabels = xLabels,
                title = "Two series · Clustered",
                subtitle = "Side-by-side bars",
            )
            // Two series, Stacked
            PopChart(
                series = listOf(
                    PopChartSeries("This Year", primaryValues),
                    PopChartSeries("Last Year", secondaryValues),
                ),
                style = PopChartStyle.Bar(grouping = PopChartStyle.Grouping.Stacked),
                xLabels = xLabels,
                title = "Two series · Stacked",
                subtitle = "Cumulative bars",
            )
        }

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

        // ── EMBEDDED ─────────────────────────────────────────────────────────
        Text(
            "EMBEDDED",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopSurface(tone = PopSurfaceTone.Default) {
            PopChart(
                series = listOf(PopChartSeries("Revenue", primaryValues)),
                style = PopChartStyle.Line(showArea = true),
                xLabels = xLabels,
                title = "Inside a Card",
                subtitle = "embedded = true",
                embedded = true,
            )
        }

        // ── EMPTY & DEGENERATE ───────────────────────────────────────────────
        Text(
            "EMPTY & DEGENERATE",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Column(verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
            // No series — placeholder line
            PopChart(
                series = emptyList(),
                title = "No Data",
                subtitle = "Empty series placeholder",
            )
            // Single data point
            PopChart(
                series = listOf(PopChartSeries("Snapshot", listOf(72f))),
                title = "Single Point",
                subtitle = "One data point marker",
            )
        }
    }
}
