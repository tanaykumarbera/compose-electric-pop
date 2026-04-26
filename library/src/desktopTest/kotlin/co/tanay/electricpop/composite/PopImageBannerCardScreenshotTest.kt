package co.tanay.electricpop.composite

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runDesktopComposeUiTest
import androidx.compose.ui.unit.dp
import co.tanay.electricpop.theme.ElectricPopTheme
import compose_electric_pop.library.generated.resources.Res
import compose_electric_pop.library.generated.resources.pop_banner_hero
import io.github.takahirom.roborazzi.captureRoboImage
import org.jetbrains.compose.resources.painterResource
import kotlin.test.Test

class PopImageBannerCardScreenshotTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun allVariants_light() = runDesktopComposeUiTest(width = 420, height = 1600) {
        setContent { Scene(darkTheme = false) }
        onRoot().captureRoboImage(
            filePath = "src/desktopTest/snapshots/PopImageBannerCard_allVariants_light.png",
        )
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun allVariants_dark() = runDesktopComposeUiTest(width = 420, height = 1600) {
        setContent { Scene(darkTheme = true) }
        onRoot().captureRoboImage(
            filePath = "src/desktopTest/snapshots/PopImageBannerCard_allVariants_dark.png",
        )
    }
}

@Composable
private fun Scene(darkTheme: Boolean) {
    ElectricPopTheme(darkTheme = darkTheme) {
        val spacing = ElectricPopTheme.spacing
        val hero = painterResource(Res.drawable.pop_banner_hero)
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
                PopImageBannerCard(
                    painter = hero,
                    eyebrow = "Electric Pop",
                    headline = "Encrypted\nWealth\nSimplified",
                    subtitle = "A single surface for everything you own.",
                    textAnchor = PopImageBannerTextAnchor.BottomStart,
                    modifier = Modifier.fillMaxWidth(),
                )
                PopImageBannerCard(
                    painter = hero,
                    eyebrow = "Limited",
                    headline = "Drop 04",
                    textAnchor = PopImageBannerTextAnchor.TopEnd,
                    modifier = Modifier.fillMaxWidth(),
                )
                PopImageBannerCard(
                    painter = hero,
                    headline = "Encrypted\nWealth\nSimplified",
                    textAnchor = PopImageBannerTextAnchor.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
                PopImageBannerCard(
                    painter = hero,
                    headline = "Pop Off",
                    subtitle = "For the people who refuse beige.",
                    textAnchor = PopImageBannerTextAnchor.BottomCenter,
                    scrim = false,
                    modifier = Modifier.fillMaxWidth(),
                )
                PopImageBannerCard(
                    painter = hero,
                    eyebrow = "Thin banner",
                    headline = "Stay Charged",
                    textAnchor = PopImageBannerTextAnchor.CenterStart,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                )
            }
        }
    }
}
