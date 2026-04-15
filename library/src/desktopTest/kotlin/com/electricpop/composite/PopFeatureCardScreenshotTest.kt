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

class PopFeatureCardScreenshotTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun allVariants_light() = runDesktopComposeUiTest(width = 440, height = 1600) {
        setContent { FeatureCardContent(darkTheme = false) }
        onRoot().captureRoboImage(
            filePath = "src/desktopTest/snapshots/PopFeatureCard_allVariants_light.png",
        )
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun allVariants_dark() = runDesktopComposeUiTest(width = 440, height = 1600) {
        setContent { FeatureCardContent(darkTheme = true) }
        onRoot().captureRoboImage(
            filePath = "src/desktopTest/snapshots/PopFeatureCard_allVariants_dark.png",
        )
    }
}

@Composable
private fun FeatureCardContent(darkTheme: Boolean) {
    ElectricPopTheme(darkTheme = darkTheme) {
        val spacing = ElectricPopTheme.spacing
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(spacing.md),
        ) {
            // 1. Full hero: badge + icons
            PopFeatureCard(
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
                style = PopFeatureCardStyle.Hero,
            )
            // 2. Hero minimal
            PopFeatureCard(
                label = "Portfolio Balance",
                mainText = "\$128,500",
                fractionalText = ".00",
                style = PopFeatureCardStyle.Hero,
            )
            // 3. Hero with overflow icons (7 icons)
            PopFeatureCard(
                label = "Network Health",
                mainText = "\$88,200",
                fractionalText = ".00",
                badgeValue = "+8.1%",
                badgeDirection = PopBadgeDirection.Up,
                icons = listOf(
                    PopIconItem(PopIcons.Bolt, "Speed"),
                    PopIconItem(PopIcons.Sparkle, "Quality"),
                    PopIconItem(PopIcons.Layers, "Depth"),
                    PopIconItem(PopIcons.Heart, "Health"),
                    PopIconItem(PopIcons.Star, "Rating"),
                    PopIconItem(PopIcons.CheckCircle, "Verified"),
                    PopIconItem(PopIcons.Home, "Home"),
                ),
                style = PopFeatureCardStyle.Hero,
            )
            // 4. Surface positive
            PopFeatureCard(
                label = "Monthly Revenue",
                mainText = "\$8,421",
                fractionalText = ".00",
                badgeValue = "+5.8%",
                badgeDirection = PopBadgeDirection.Up,
                displayDirection = PopDisplayTextDirection.Positive,
                style = PopFeatureCardStyle.Surface,
            )
            // 5. Surface negative
            PopFeatureCard(
                label = "Operating Costs",
                mainText = "\$3,200",
                fractionalText = ".50",
                badgeValue = "-2.1%",
                badgeDirection = PopBadgeDirection.Down,
                displayDirection = PopDisplayTextDirection.Negative,
                style = PopFeatureCardStyle.Surface,
            )
            // 6. Surface neutral, medium size
            PopFeatureCard(
                label = "Total Assets",
                mainText = "\$52,000",
                badgeValue = "0.0%",
                badgeDirection = PopBadgeDirection.Neutral,
                displaySize = PopDisplayTextSize.Medium,
                style = PopFeatureCardStyle.Surface,
            )
        }
    }
}
