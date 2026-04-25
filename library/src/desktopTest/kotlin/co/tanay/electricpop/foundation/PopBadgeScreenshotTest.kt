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

class PopBadgeScreenshotTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun popBadge_allVariants_light() = runDesktopComposeUiTest(width = 400, height = 150) {
        setContent {
            ElectricPopTheme(darkTheme = false) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    PopBadge(value = "+12.5%", direction = PopBadgeDirection.Up)
                    PopBadge(value = "-3.2%", direction = PopBadgeDirection.Down)
                    PopBadge(value = "0.0%", direction = PopBadgeDirection.Neutral)
                }
            }
        }
        onRoot().captureRoboImage(
            filePath = "src/desktopTest/snapshots/PopBadge_allVariants_light.png"
        )
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun popBadge_allVariants_dark() = runDesktopComposeUiTest(width = 400, height = 150) {
        setContent {
            ElectricPopTheme(darkTheme = true) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    PopBadge(value = "+12.5%", direction = PopBadgeDirection.Up)
                    PopBadge(value = "-3.2%", direction = PopBadgeDirection.Down)
                    PopBadge(value = "0.0%", direction = PopBadgeDirection.Neutral)
                }
            }
        }
        onRoot().captureRoboImage(
            filePath = "src/desktopTest/snapshots/PopBadge_allVariants_dark.png"
        )
    }
}
