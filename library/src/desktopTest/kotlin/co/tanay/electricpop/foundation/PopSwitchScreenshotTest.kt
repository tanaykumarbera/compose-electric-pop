package co.tanay.electricpop.foundation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runDesktopComposeUiTest
import androidx.compose.ui.unit.dp
import co.tanay.electricpop.theme.ElectricPopTheme
import io.github.takahirom.roborazzi.captureRoboImage
import kotlin.test.Test

class PopSwitchScreenshotTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun allVariants_light() = runDesktopComposeUiTest(width = 500, height = 450) {
        setContent {
            ElectricPopTheme(darkTheme = false) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text(
                        text = "STATES",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopSwitch(checked = false, onCheckedChange = {})
                    PopSwitch(checked = true, onCheckedChange = {})

                    Text(
                        text = "WITH LABEL",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopSwitch(checked = false, onCheckedChange = {}, label = "Notifications")
                    PopSwitch(checked = true, onCheckedChange = {}, label = "Dark Mode")

                    Text(
                        text = "DISABLED",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopSwitch(checked = false, onCheckedChange = {}, enabled = false)
                    PopSwitch(checked = true, onCheckedChange = {}, enabled = false)
                    PopSwitch(checked = false, onCheckedChange = {}, enabled = false, label = "Unavailable")
                    PopSwitch(checked = true, onCheckedChange = {}, enabled = false, label = "Always On")
                }
            }
        }
        onRoot().captureRoboImage(
            filePath = "src/desktopTest/snapshots/PopSwitch_allVariants_light.png",
        )
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun allVariants_dark() = runDesktopComposeUiTest(width = 500, height = 450) {
        setContent {
            ElectricPopTheme(darkTheme = true) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text(
                        text = "STATES",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopSwitch(checked = false, onCheckedChange = {})
                    PopSwitch(checked = true, onCheckedChange = {})

                    Text(
                        text = "WITH LABEL",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopSwitch(checked = false, onCheckedChange = {}, label = "Notifications")
                    PopSwitch(checked = true, onCheckedChange = {}, label = "Dark Mode")

                    Text(
                        text = "DISABLED",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopSwitch(checked = false, onCheckedChange = {}, enabled = false)
                    PopSwitch(checked = true, onCheckedChange = {}, enabled = false)
                    PopSwitch(checked = false, onCheckedChange = {}, enabled = false, label = "Unavailable")
                    PopSwitch(checked = true, onCheckedChange = {}, enabled = false, label = "Always On")
                }
            }
        }
        onRoot().captureRoboImage(
            filePath = "src/desktopTest/snapshots/PopSwitch_allVariants_dark.png",
        )
    }
}
