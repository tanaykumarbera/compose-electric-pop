package co.tanay.electricpop.foundation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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

class PopSliderScreenshotTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun allVariants_light() = runDesktopComposeUiTest(width = 500, height = 550) {
        setContent {
            ElectricPopTheme(darkTheme = false) {
                Column(
                    modifier = Modifier.padding(16.dp).width(460.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Text(
                        text = "DEFAULT",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopSlider(value = 0f, onValueChange = {})
                    PopSlider(value = 0.5f, onValueChange = {})
                    PopSlider(value = 1f, onValueChange = {})

                    Text(
                        text = "WITH LABEL + VALUE",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopSlider(
                        value = 75f,
                        onValueChange = {},
                        valueRange = 0f..100f,
                        label = "Volume",
                        showValue = true,
                    )

                    Text(
                        text = "WITH STEPS",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopSlider(
                        value = 40f,
                        onValueChange = {},
                        valueRange = 0f..100f,
                        steps = 4,
                        label = "Quality",
                        showValue = true,
                    )

                    Text(
                        text = "DISABLED",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopSlider(value = 0.3f, onValueChange = {}, enabled = false)
                    PopSlider(
                        value = 60f,
                        onValueChange = {},
                        valueRange = 0f..100f,
                        enabled = false,
                        label = "Brightness",
                        showValue = true,
                    )
                }
            }
        }
        onRoot().captureRoboImage(
            filePath = "src/desktopTest/snapshots/PopSlider_allVariants_light.png",
        )
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun allVariants_dark() = runDesktopComposeUiTest(width = 500, height = 550) {
        setContent {
            ElectricPopTheme(darkTheme = true) {
                Column(
                    modifier = Modifier.padding(16.dp).width(460.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Text(
                        text = "DEFAULT",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopSlider(value = 0f, onValueChange = {})
                    PopSlider(value = 0.5f, onValueChange = {})
                    PopSlider(value = 1f, onValueChange = {})

                    Text(
                        text = "WITH LABEL + VALUE",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopSlider(
                        value = 75f,
                        onValueChange = {},
                        valueRange = 0f..100f,
                        label = "Volume",
                        showValue = true,
                    )

                    Text(
                        text = "WITH STEPS",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopSlider(
                        value = 40f,
                        onValueChange = {},
                        valueRange = 0f..100f,
                        steps = 4,
                        label = "Quality",
                        showValue = true,
                    )

                    Text(
                        text = "DISABLED",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopSlider(value = 0.3f, onValueChange = {}, enabled = false)
                    PopSlider(
                        value = 60f,
                        onValueChange = {},
                        valueRange = 0f..100f,
                        enabled = false,
                        label = "Brightness",
                        showValue = true,
                    )
                }
            }
        }
        onRoot().captureRoboImage(
            filePath = "src/desktopTest/snapshots/PopSlider_allVariants_dark.png",
        )
    }
}
