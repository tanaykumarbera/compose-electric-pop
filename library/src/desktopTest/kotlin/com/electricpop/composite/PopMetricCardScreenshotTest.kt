package com.electricpop.composite

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runDesktopComposeUiTest
import androidx.compose.ui.unit.dp
import com.electricpop.foundation.PopBadgeDirection
import com.electricpop.foundation.PopDisplayTextDirection
import com.electricpop.foundation.PopDisplayTextSize
import com.electricpop.foundation.PopIconItem
import com.electricpop.foundation.PopIcons
import com.electricpop.theme.ElectricPopTheme
import io.github.takahirom.roborazzi.captureRoboImage
import kotlin.test.Test

class PopMetricCardScreenshotTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun allVariants_light() = runDesktopComposeUiTest(width = 420, height = 1200) {
        setContent { MetricCardContent(darkTheme = false) }
        onRoot().captureRoboImage(
            filePath = "src/desktopTest/snapshots/PopMetricCard_allVariants_light.png",
        )
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun allVariants_dark() = runDesktopComposeUiTest(width = 420, height = 1200) {
        setContent { MetricCardContent(darkTheme = true) }
        onRoot().captureRoboImage(
            filePath = "src/desktopTest/snapshots/PopMetricCard_allVariants_dark.png",
        )
    }
}

@Composable
private fun MetricCardContent(darkTheme: Boolean) {
    ElectricPopTheme(darkTheme = darkTheme) {
        val spacing = ElectricPopTheme.spacing
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(spacing.md),
        ) {
            // 1. Hero + badge + icons (Slot C)
            PopMetricCard(
                label = "Total Ecosystem Value",
                mainText = "\$42,069",
                fractionalText = ".42",
                badgeValue = "+12.4%",
                badgeDirection = PopBadgeDirection.Up,
                icons = listOf(
                    PopIconItem(PopIcons.Bolt, "Speed"),
                    PopIconItem(PopIcons.Sparkle, "Quality"),
                    PopIconItem(PopIcons.Layers, "Depth"),
                ),
                style = PopMetricCardStyle.Hero,
            )
            // 2. Hero, no badge
            PopMetricCard(
                label = "Portfolio Balance",
                mainText = "\$128,500",
                style = PopMetricCardStyle.Hero,
            )
            // 3. Surface + badge (negative)
            PopMetricCard(
                label = "Monthly Revenue",
                mainText = "\$8,421",
                fractionalText = ".00",
                badgeValue = "-3.2%",
                badgeDirection = PopBadgeDirection.Down,
                displayDirection = PopDisplayTextDirection.Negative,
                style = PopMetricCardStyle.Surface,
            )
            // 4. Surface, no badge, small size
            PopMetricCard(
                label = "Active Users",
                mainText = "1,247",
                displaySize = PopDisplayTextSize.Small,
                style = PopMetricCardStyle.Surface,
            )
        }
    }
}
