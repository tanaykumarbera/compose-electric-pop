package co.tanay.electricpop.foundation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runDesktopComposeUiTest
import androidx.compose.ui.unit.dp
import co.tanay.electricpop.theme.ElectricPopTheme
import io.github.takahirom.roborazzi.captureRoboImage
import kotlin.test.Test

class PopChipScreenshotTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun popChip_allVariants_light() = runDesktopComposeUiTest(width = 500, height = 400) {
        setContent {
            ElectricPopTheme(darkTheme = false) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Color presets
                    PopChipColor.entries.forEach { color ->
                        PopChip(label = color.name, color = color)
                    }
                    // With icon
                    PopChip(
                        label = "With Icon",
                        color = PopChipColor.Primary,
                        icon = PopIcons.Star,
                    )
                    PopChip(
                        label = "With Icon",
                        color = PopChipColor.Secondary,
                        icon = PopIcons.Heart,
                    )
                    PopChip(
                        label = "With Icon",
                        color = PopChipColor.Tertiary,
                        icon = PopIcons.Bolt,
                    )
                }
            }
        }
        onRoot().captureRoboImage(
            filePath = "src/desktopTest/snapshots/PopChip_allVariants_light.png"
        )
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun popChip_allVariants_dark() = runDesktopComposeUiTest(width = 500, height = 400) {
        setContent {
            ElectricPopTheme(darkTheme = true) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Color presets
                    PopChipColor.entries.forEach { color ->
                        PopChip(label = color.name, color = color)
                    }
                    // With icon
                    PopChip(
                        label = "With Icon",
                        color = PopChipColor.Primary,
                        icon = PopIcons.Star,
                    )
                    PopChip(
                        label = "With Icon",
                        color = PopChipColor.Secondary,
                        icon = PopIcons.Heart,
                    )
                    PopChip(
                        label = "With Icon",
                        color = PopChipColor.Tertiary,
                        icon = PopIcons.Bolt,
                    )
                }
            }
        }
        onRoot().captureRoboImage(
            filePath = "src/desktopTest/snapshots/PopChip_allVariants_dark.png"
        )
    }
}
