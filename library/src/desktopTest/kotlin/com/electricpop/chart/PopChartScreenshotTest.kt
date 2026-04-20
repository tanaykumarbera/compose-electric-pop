package com.electricpop.chart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runDesktopComposeUiTest
import androidx.compose.ui.unit.dp
import com.electricpop.theme.ElectricPopTheme
import io.github.takahirom.roborazzi.captureRoboImage
import kotlin.test.Test

class PopChartScreenshotTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun allVariants_light() = runDesktopComposeUiTest(width = 460, height = 1800) {
        setContent { PopChartContent(darkTheme = false) }
        onRoot().captureRoboImage(
            filePath = "src/desktopTest/snapshots/PopChart_allVariants_light.png",
        )
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun allVariants_dark() = runDesktopComposeUiTest(width = 460, height = 1800) {
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
                // Variant 1: Single series, smooth, no area
                Text(
                    "SINGLE SERIES · SMOOTH",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                PopChart(
                    series = listOf(PopChartSeries("Revenue", primaryValues)),
                    xLabels = xLabels,
                    title = "Monthly Revenue",
                    subtitle = "This Year",
                    smooth = true,
                    showArea = false,
                )

                // Variant 2: Single series, smooth, area fill
                Text(
                    "SINGLE SERIES · AREA FILL",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                PopChart(
                    series = listOf(PopChartSeries("Revenue", primaryValues)),
                    xLabels = xLabels,
                    title = "Monthly Revenue",
                    subtitle = "With Area",
                    smooth = true,
                    showArea = true,
                )

                // Variant 3: Two series, smooth, no area
                Text(
                    "TWO SERIES · SMOOTH",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                PopChart(
                    series = listOf(
                        PopChartSeries("This Year", primaryValues),
                        PopChartSeries("Last Year", secondaryValues),
                    ),
                    xLabels = xLabels,
                    title = "Year Comparison",
                    smooth = true,
                    showArea = false,
                )

                // Variant 4: Two series, smooth, area fill, activeIndex = 7
                Text(
                    "TWO SERIES · AREA FILL · ACTIVE INDEX",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                PopChart(
                    series = listOf(
                        PopChartSeries("This Year", primaryValues),
                        PopChartSeries("Last Year", secondaryValues),
                    ),
                    xLabels = xLabels,
                    title = "Year Comparison",
                    subtitle = "August highlighted",
                    smooth = true,
                    showArea = true,
                    activeIndex = 7,
                )

                // Variant 5: Single series, straight lines
                Text(
                    "SINGLE SERIES · STRAIGHT",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                PopChart(
                    series = listOf(PopChartSeries("Revenue", primaryValues)),
                    xLabels = xLabels,
                    title = "Monthly Revenue",
                    subtitle = "Linear segments",
                    smooth = false,
                    showArea = false,
                )
            }
        }
    }
}
