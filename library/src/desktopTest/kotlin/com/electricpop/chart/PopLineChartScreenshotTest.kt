package com.electricpop.chart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runDesktopComposeUiTest
import com.electricpop.foundation.PopSurface
import com.electricpop.foundation.PopSurfaceTone
import com.electricpop.theme.ElectricPopTheme
import io.github.takahirom.roborazzi.captureRoboImage
import kotlin.test.Test

private val revenueMonths = listOf("JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL")
private val revenueValues = listOf(4_200f, 5_100f, 4_800f, 6_200f, 7_400f, 6_900f, 8_100f)
private val thisYearMonths = listOf("JAN", "FEB", "MAR", "APR", "MAY", "JUN")
private val thisYearValues = listOf(10_000f, 12_500f, 11_800f, 15_200f, 14_600f, 18_300f)
private val lastYearValues = listOf(8_500f, 9_200f, 10_100f, 11_800f, 12_400f, 13_900f)

class PopLineChartScreenshotTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun allVariants_light() = runDesktopComposeUiTest(width = 600, height = 1200) {
        setContent {
            ElectricPopTheme(darkTheme = false) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                ) {
                    LineChartContent()
                }
            }
        }
        onRoot().captureRoboImage(
            filePath = "src/desktopTest/snapshots/PopLineChart_allVariants_light.png",
        )
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun allVariants_dark() = runDesktopComposeUiTest(width = 600, height = 1200) {
        setContent {
            ElectricPopTheme(darkTheme = true) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                ) {
                    LineChartContent()
                }
            }
        }
        onRoot().captureRoboImage(
            filePath = "src/desktopTest/snapshots/PopLineChart_allVariants_dark.png",
        )
    }
}

@Composable
private fun LineChartContent() {
    val spacing = ElectricPopTheme.spacing
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(spacing.lg),
        verticalArrangement = Arrangement.spacedBy(spacing.lg),
    ) {
        // Section A: Primary, no active
        SectionCard {
            PopLineChart(
                series = listOf(
                    PopLineChartSeries("REVENUE", revenueValues, SeriesRole.Primary),
                ),
                xAxisLabels = revenueMonths,
                animationsEnabled = false,
            )
        }
        // Section B: Primary, active index 4
        SectionCard {
            PopLineChart(
                series = listOf(
                    PopLineChartSeries("REVENUE", revenueValues, SeriesRole.Primary),
                ),
                xAxisLabels = revenueMonths,
                activePointIndex = 4,
                animationsEnabled = false,
            )
        }
        // Section C: Dual, no active
        SectionCard {
            PopLineChart(
                series = listOf(
                    PopLineChartSeries("THIS YEAR", thisYearValues, SeriesRole.Primary),
                    PopLineChartSeries("LAST YEAR", lastYearValues, SeriesRole.Secondary),
                ),
                xAxisLabels = thisYearMonths,
                animationsEnabled = false,
            )
        }
        // Section D: Dual, active index 3
        SectionCard {
            PopLineChart(
                series = listOf(
                    PopLineChartSeries("THIS YEAR", thisYearValues, SeriesRole.Primary),
                    PopLineChartSeries("LAST YEAR", lastYearValues, SeriesRole.Secondary),
                ),
                xAxisLabels = thisYearMonths,
                activePointIndex = 3,
                animationsEnabled = false,
            )
        }
    }
}

@Composable
private fun SectionCard(content: @Composable () -> Unit) {
    val spacing = ElectricPopTheme.spacing
    PopSurface(
        tone = PopSurfaceTone.Lowest,
        shape = MaterialTheme.shapes.extraSmall,
    ) {
        Column(modifier = Modifier.padding(spacing.lg)) {
            content()
        }
    }
}
