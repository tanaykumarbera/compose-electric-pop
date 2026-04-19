package com.electricpop.demo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.electricpop.chart.PopLineChart
import com.electricpop.chart.PopLineChartSeries
import com.electricpop.chart.SeriesRole
import com.electricpop.foundation.PopSectionHeader
import com.electricpop.foundation.PopSurface
import com.electricpop.foundation.PopSurfaceTone
import com.electricpop.theme.ElectricPopTheme

private val revenueMonths = listOf("JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL")
private val revenueValues = listOf(4_200f, 5_100f, 4_800f, 6_200f, 7_400f, 6_900f, 8_100f)
private val thisYearMonths = listOf("JAN", "FEB", "MAR", "APR", "MAY", "JUN")
private val thisYearValues = listOf(10_000f, 12_500f, 11_800f, 15_200f, 14_600f, 18_300f)
private val lastYearValues = listOf(8_500f, 9_200f, 10_100f, 11_800f, 12_400f, 13_900f)

@Composable
fun PopLineChartDemo() {
    val spacing = ElectricPopTheme.spacing
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(spacing.lg),
        verticalArrangement = Arrangement.spacedBy(spacing.lg),
    ) {
        // 1. Primary single-series
        PopSectionHeader(title = "PRIMARY SINGLE-SERIES")
        PopSurface(
            tone = PopSurfaceTone.Lowest,
            shape = MaterialTheme.shapes.extraSmall,
        ) {
            Column(modifier = Modifier.padding(spacing.lg)) {
                PopLineChart(
                    series = listOf(
                        PopLineChartSeries("REVENUE", revenueValues, SeriesRole.Primary),
                    ),
                    xAxisLabels = revenueMonths,
                )
            }
        }

        // 2. Primary with active point
        PopSectionHeader(title = "PRIMARY WITH ACTIVE POINT")
        PopSurface(
            tone = PopSurfaceTone.Lowest,
            shape = MaterialTheme.shapes.extraSmall,
        ) {
            Column(modifier = Modifier.padding(spacing.lg)) {
                PopLineChart(
                    series = listOf(
                        PopLineChartSeries("REVENUE", revenueValues, SeriesRole.Primary),
                    ),
                    xAxisLabels = revenueMonths,
                    activePointIndex = 5,
                )
            }
        }

        // 3. Dual comparison
        PopSectionHeader(title = "DUAL COMPARISON")
        PopSurface(
            tone = PopSurfaceTone.Lowest,
            shape = MaterialTheme.shapes.extraSmall,
        ) {
            Column(modifier = Modifier.padding(spacing.lg)) {
                PopLineChart(
                    series = listOf(
                        PopLineChartSeries("THIS YEAR", thisYearValues, SeriesRole.Primary),
                        PopLineChartSeries("LAST YEAR", lastYearValues, SeriesRole.Secondary),
                    ),
                    xAxisLabels = thisYearMonths,
                    activePointIndex = 3,
                    showLegend = true,
                )
            }
        }

        // 4. Interactive active index
        PopSectionHeader(title = "INTERACTIVE ACTIVE INDEX")
        val activeIndex = remember { mutableStateOf(3) }
        PopSurface(
            tone = PopSurfaceTone.Lowest,
            shape = MaterialTheme.shapes.extraSmall,
        ) {
            Column(modifier = Modifier.padding(spacing.lg)) {
                PopLineChart(
                    series = listOf(
                        PopLineChartSeries("THIS YEAR", thisYearValues, SeriesRole.Primary),
                        PopLineChartSeries("LAST YEAR", lastYearValues, SeriesRole.Secondary),
                    ),
                    xAxisLabels = thisYearMonths,
                    activePointIndex = activeIndex.value,
                    showLegend = true,
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = spacing.xs),
                    horizontalArrangement = Arrangement.spacedBy(spacing.xs),
                ) {
                    thisYearMonths.forEachIndexed { i, month ->
                        val isSelected = i == activeIndex.value
                        Box(
                            modifier = Modifier
                                .background(
                                    color = if (isSelected) colorScheme.primary else colorScheme.surfaceContainerHigh,
                                    shape = MaterialTheme.shapes.extraSmall,
                                )
                                .clickable { activeIndex.value = i }
                                .padding(horizontal = spacing.xs, vertical = spacing.xxs),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = month.uppercase(),
                                style = typography.labelSmall,
                                color = if (isSelected) colorScheme.onPrimary else colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }
        }

        // 5. Edge cases
        PopSectionHeader(title = "EDGE CASES")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing.md),
        ) {
            // Empty
            PopSurface(
                modifier = Modifier.weight(1f),
                tone = PopSurfaceTone.Lowest,
                shape = MaterialTheme.shapes.extraSmall,
            ) {
                Column(modifier = Modifier.padding(spacing.sm)) {
                    Text(
                        text = "EMPTY",
                        style = typography.labelSmall,
                        color = colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = spacing.xxs),
                    )
                    PopLineChart(
                        series = emptyList(),
                        xAxisLabels = emptyList(),
                        chartHeight = 120.dp,
                        showYAxisLabels = false,
                        showXAxisLabels = false,
                        showLegend = false,
                        animationsEnabled = false,
                    )
                }
            }
            // Single point
            PopSurface(
                modifier = Modifier.weight(1f),
                tone = PopSurfaceTone.Lowest,
                shape = MaterialTheme.shapes.extraSmall,
            ) {
                Column(modifier = Modifier.padding(spacing.sm)) {
                    Text(
                        text = "SINGLE",
                        style = typography.labelSmall,
                        color = colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = spacing.xxs),
                    )
                    PopLineChart(
                        series = listOf(
                            PopLineChartSeries("A", listOf(5f), SeriesRole.Primary),
                        ),
                        xAxisLabels = listOf("A"),
                        chartHeight = 120.dp,
                        showYAxisLabels = false,
                        showXAxisLabels = false,
                        showLegend = false,
                        animationsEnabled = false,
                    )
                }
            }
            // Flat line
            PopSurface(
                modifier = Modifier.weight(1f),
                tone = PopSurfaceTone.Lowest,
                shape = MaterialTheme.shapes.extraSmall,
            ) {
                Column(modifier = Modifier.padding(spacing.sm)) {
                    Text(
                        text = "FLAT",
                        style = typography.labelSmall,
                        color = colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = spacing.xxs),
                    )
                    PopLineChart(
                        series = listOf(
                            PopLineChartSeries("A", listOf(7f, 7f, 7f, 7f, 7f, 7f), SeriesRole.Primary),
                        ),
                        xAxisLabels = listOf("A", "B", "C", "D", "E", "F"),
                        chartHeight = 120.dp,
                        showYAxisLabels = false,
                        showXAxisLabels = false,
                        showLegend = false,
                        animationsEnabled = false,
                    )
                }
            }
        }
    }
}
