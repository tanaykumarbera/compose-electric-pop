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
import com.electricpop.chart.PopChart
import com.electricpop.chart.PopChartSeries
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

        // ── SINGLE SERIES ────────────────────────────────────────────────────
        Text(
            "SINGLE SERIES",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Column(verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
            // Smooth, no area
            PopChart(
                series = listOf(PopChartSeries("Revenue", primaryValues)),
                xLabels = xLabels,
                title = "Monthly Revenue",
                subtitle = "Smooth · No area",
                smooth = true,
                showArea = false,
            )
            // Smooth with area fill
            PopChart(
                series = listOf(PopChartSeries("Revenue", primaryValues)),
                xLabels = xLabels,
                title = "Monthly Revenue",
                subtitle = "Smooth · Area fill",
                smooth = true,
                showArea = true,
            )
            // Straight segments
            PopChart(
                series = listOf(PopChartSeries("Revenue", primaryValues)),
                xLabels = xLabels,
                title = "Monthly Revenue",
                subtitle = "Straight segments",
                smooth = false,
                showArea = false,
            )
        }

        // ── MULTI SERIES ─────────────────────────────────────────────────────
        Text(
            "MULTI SERIES",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Column(verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
            // Two series, no area
            PopChart(
                series = listOf(
                    PopChartSeries("This Year", primaryValues),
                    PopChartSeries("Last Year", secondaryValues),
                ),
                xLabels = xLabels,
                title = "Year Comparison",
                subtitle = "Smooth · No area",
                smooth = true,
                showArea = false,
            )
            // Two series, area fill, activeIndex
            PopChart(
                series = listOf(
                    PopChartSeries("This Year", primaryValues),
                    PopChartSeries("Last Year", secondaryValues),
                ),
                xLabels = xLabels,
                title = "Year Comparison",
                subtitle = "Area fill · August highlighted",
                smooth = true,
                showArea = true,
                activeIndex = 7,
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
                xLabels = xLabels,
                title = "Inside a Card",
                subtitle = "embedded = true",
                smooth = true,
                showArea = true,
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
