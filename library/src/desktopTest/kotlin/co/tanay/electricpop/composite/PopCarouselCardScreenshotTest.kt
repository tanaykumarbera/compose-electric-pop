package co.tanay.electricpop.composite

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runDesktopComposeUiTest
import androidx.compose.ui.unit.dp
import co.tanay.electricpop.foundation.PopIcons
import co.tanay.electricpop.theme.ElectricPopTheme
import io.github.takahirom.roborazzi.captureRoboImage
import kotlin.test.Test

class PopCarouselCardScreenshotTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun allVariants_light() = runDesktopComposeUiTest(width = 1400, height = 500) {
        setContent { CarouselCardContent(darkTheme = false) }
        onRoot().captureRoboImage(
            filePath = "src/desktopTest/snapshots/PopCarouselCard_allVariants_light.png",
        )
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun allVariants_dark() = runDesktopComposeUiTest(width = 1400, height = 500) {
        setContent { CarouselCardContent(darkTheme = true) }
        onRoot().captureRoboImage(
            filePath = "src/desktopTest/snapshots/PopCarouselCard_allVariants_dark.png",
        )
    }
}

@Composable
private fun CarouselCardContent(darkTheme: Boolean) {
    ElectricPopTheme(darkTheme = darkTheme) {
        val items = listOf(
            PopCarouselCardItem(
                icon = PopIcons.Bolt,
                iconContentDescription = "Electric",
                timestamp = "08:42 AM",
                category = "Subscriptions",
                label = "Electric Grid Co.",
                value = "- \$142.00",
                style = PopCarouselCardStyle.Primary,
            ),
            PopCarouselCardItem(
                icon = PopIcons.Heart,
                iconContentDescription = "Lifestyle",
                timestamp = "11:15 AM",
                category = "Lifestyle",
                label = "Neon Market",
                value = "- \$64.50",
                style = PopCarouselCardStyle.Secondary,
            ),
            PopCarouselCardItem(
                icon = PopIcons.Add,
                iconContentDescription = "Incoming",
                timestamp = "02:30 PM",
                category = "Incoming",
                label = "P2P Transfer",
                value = "+ \$1,200.00",
                style = PopCarouselCardStyle.Tertiary,
            ),
            PopCarouselCardItem(
                icon = PopIcons.Star,
                iconContentDescription = "Dining",
                timestamp = "07:22 PM",
                category = "Dining",
                label = "Pixel Cafe",
                value = "- \$24.12",
                style = PopCarouselCardStyle.Surface,
            ),
        )
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            PopCarouselCardStrip(
                items = items,
                contentPadding = PaddingValues(horizontal = 16.dp),
            )
        }
    }
}
