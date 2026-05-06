package co.tanay.electricpop.foundation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
    fun popBadge_allVariants_light() = runDesktopComposeUiTest(width = 600, height = 350) {
        setContent { BadgeContent(darkTheme = false) }
        onRoot().captureRoboImage(
            filePath = "src/desktopTest/snapshots/PopBadge_allVariants_light.png",
        )
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun popBadge_allVariants_dark() = runDesktopComposeUiTest(width = 600, height = 350) {
        setContent { BadgeContent(darkTheme = true) }
        onRoot().captureRoboImage(
            filePath = "src/desktopTest/snapshots/PopBadge_allVariants_dark.png",
        )
    }
}

@Composable
private fun BadgeContent(darkTheme: Boolean) {
    ElectricPopTheme(darkTheme = darkTheme) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                // Row 1 - Small x {Up, Down, Neutral}
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    PopBadge("+12.5%", PopBadgeDirection.Up, size = PopBadgeSize.Small)
                    PopBadge("-3.2%", PopBadgeDirection.Down, size = PopBadgeSize.Small)
                    PopBadge("0.0%", PopBadgeDirection.Neutral, size = PopBadgeSize.Small)
                }
                // Row 2 - Large x {Up, Down, Neutral}
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    PopBadge("+12.5%", PopBadgeDirection.Up, size = PopBadgeSize.Large)
                    PopBadge("-3.2%", PopBadgeDirection.Down, size = PopBadgeSize.Large)
                    PopBadge("0.0%", PopBadgeDirection.Neutral, size = PopBadgeSize.Large)
                }
                // Row 3 - Hero color override (custom-color overload, Large)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    PopBadge(
                        value = "+12.5%",
                        direction = PopBadgeDirection.Up,
                        containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        contentColor = MaterialTheme.colorScheme.primaryContainer,
                        size = PopBadgeSize.Large,
                    )
                }
            }
        }
    }
}
