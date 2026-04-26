package co.tanay.electricpop.composite

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
import androidx.compose.ui.unit.dp
import co.tanay.electricpop.foundation.PopBadgeDirection
import co.tanay.electricpop.foundation.PopDisplayTextDirection
import co.tanay.electricpop.foundation.PopDisplayTextSize
import co.tanay.electricpop.foundation.PopIconItem
import co.tanay.electricpop.foundation.PopIcons
import co.tanay.electricpop.theme.ElectricPopTheme
import io.github.takahirom.roborazzi.captureRoboImage
import kotlin.test.Test

class PopBannerCardScreenshotTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun allVariants_light() = runDesktopComposeUiTest(width = 420, height = 1500) {
        setContent { BannerCardContent(darkTheme = false) }
        onRoot().captureRoboImage(
            filePath = "src/desktopTest/snapshots/PopBannerCard_allVariants_light.png",
        )
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun allVariants_dark() = runDesktopComposeUiTest(width = 420, height = 1500) {
        setContent { BannerCardContent(darkTheme = true) }
        onRoot().captureRoboImage(
            filePath = "src/desktopTest/snapshots/PopBannerCard_allVariants_dark.png",
        )
    }
}

@Composable
private fun BannerCardContent(darkTheme: Boolean) {
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
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(spacing.md),
            ) {
            // 1. Hero + badge + icons (Slot C)
            PopBannerCard(
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
                style = PopBannerCardStyle.Hero,
            )
            // 2. Hero with >5 icons (overflow)
            PopBannerCard(
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
                style = PopBannerCardStyle.Hero,
            )
            // 3. Surface + badge (negative)
            PopBannerCard(
                label = "Monthly Revenue",
                mainText = "\$8,421",
                fractionalText = ".00",
                badgeValue = "-3.2%",
                badgeDirection = PopBadgeDirection.Down,
                displayDirection = PopDisplayTextDirection.Negative,
                style = PopBannerCardStyle.Surface,
            )
            // 4. Surface, no badge, small size
            PopBannerCard(
                label = "Active Users",
                mainText = "1,247",
                displaySize = PopDisplayTextSize.Small,
                style = PopBannerCardStyle.Surface,
            )
            }
        }
    }
}
