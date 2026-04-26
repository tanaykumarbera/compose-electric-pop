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

class PopPillScreenshotTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun popPill_allPresets_light() = runDesktopComposeUiTest(width = 400, height = 200) {
        setContent {
            ElectricPopTheme(darkTheme = false) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    PopPillColor.entries.forEach { color ->
                        PopPill(label = color.name, color = color)
                    }
                }
            }
        }
        onRoot().captureRoboImage(
            filePath = "src/desktopTest/snapshots/PopPill_allPresets_light.png",
        )
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun popPill_allPresets_dark() = runDesktopComposeUiTest(width = 400, height = 200) {
        setContent {
            ElectricPopTheme(darkTheme = true) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    PopPillColor.entries.forEach { color ->
                        PopPill(label = color.name, color = color)
                    }
                }
            }
        }
        onRoot().captureRoboImage(
            filePath = "src/desktopTest/snapshots/PopPill_allPresets_dark.png",
        )
    }
}
