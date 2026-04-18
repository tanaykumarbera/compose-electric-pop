package com.electricpop.composite

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runDesktopComposeUiTest
import androidx.compose.ui.unit.dp
import com.electricpop.foundation.PopIcons
import com.electricpop.theme.ElectricPopTheme
import io.github.takahirom.roborazzi.captureRoboImage
import kotlin.test.Test

class PopBannerCardScreenshotTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun allVariants_light() = runDesktopComposeUiTest(width = 420, height = 1400) {
        setContent { BannerCardContent(darkTheme = false) }
        onRoot().captureRoboImage(
            filePath = "src/desktopTest/snapshots/PopBannerCard_allVariants_light.png",
        )
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun allVariants_dark() = runDesktopComposeUiTest(width = 420, height = 1400) {
        setContent { BannerCardContent(darkTheme = true) }
        onRoot().captureRoboImage(
            filePath = "src/desktopTest/snapshots/PopBannerCard_allVariants_dark.png",
        )
    }
}

@Composable
private fun BannerCardContent(darkTheme: Boolean) {
    ElectricPopTheme(darkTheme = darkTheme) {
        // Read composable icon values at composable scope BEFORE the Column
        val iconBolt = PopIcons.Bolt
        val iconSparkle = PopIcons.Sparkle
        val iconCheckCircle = PopIcons.CheckCircle
        val iconTrendDown = PopIcons.TrendDown

        val secondaryContainer = MaterialTheme.colorScheme.secondaryContainer
        val onSecondaryContainer = MaterialTheme.colorScheme.onSecondaryContainer

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            // Variant 1: Full — Up trend, fractional, vitals
            PopBannerCard(
                label = "Total Ecosystem Value",
                value = "\$42,069",
                fractionalValue = ".42",
                trend = PopBannerCardTrend("+12.4%", PopBannerCardTrendDirection.Up),
                vitals = listOf(
                    PopBannerCardVital(iconBolt, "Bolt"),
                    PopBannerCardVital(iconSparkle, "Sparkle"),
                    PopBannerCardVital(iconCheckCircle, "Check"),
                ),
            )

            // Variant 2: Down trend, no fractional
            PopBannerCard(
                label = "Monthly Burn",
                value = "\$8,420",
                trend = PopBannerCardTrend("-3.8%", PopBannerCardTrendDirection.Down),
                vitals = listOf(
                    PopBannerCardVital(iconTrendDown, "Trend Down"),
                ),
            )

            // Variant 3: Secondary color, neutral trend, no vitals
            PopBannerCard(
                label = "Active Nodes",
                value = "1,402",
                containerColor = secondaryContainer,
                contentColor = onSecondaryContainer,
                trend = PopBannerCardTrend("STABLE", PopBannerCardTrendDirection.Neutral),
            )

            // Variant 4: Minimal — no fractional, no trend, no vitals
            PopBannerCard(
                label = "Temperature",
                value = "72°F",
            )
        }
    }
}
