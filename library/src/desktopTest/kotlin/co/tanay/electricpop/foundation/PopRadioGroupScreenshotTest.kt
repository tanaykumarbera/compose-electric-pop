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

class PopRadioGroupScreenshotTest {

    private val sampleOptions = listOf("Option Alpha", "Option Beta", "Option Gamma")

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun allVariants_light() = runDesktopComposeUiTest(width = 500, height = 550) {
        setContent {
            ElectricPopTheme(darkTheme = false) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Text(
                        text = "SELECTED (INDEX 1)",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopRadioGroup(
                        options = sampleOptions,
                        selectedIndex = 1,
                        onSelectedChange = {},
                    )

                    Text(
                        text = "NONE SELECTED",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopRadioGroup(
                        options = sampleOptions,
                        selectedIndex = -1,
                        onSelectedChange = {},
                    )

                    Text(
                        text = "DISABLED",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopRadioGroup(
                        options = sampleOptions,
                        selectedIndex = 0,
                        onSelectedChange = {},
                        enabled = false,
                    )
                }
            }
        }
        onRoot().captureRoboImage(
            filePath = "src/desktopTest/snapshots/PopRadioGroup_allVariants_light.png",
        )
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun allVariants_dark() = runDesktopComposeUiTest(width = 500, height = 550) {
        setContent {
            ElectricPopTheme(darkTheme = true) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Text(
                        text = "SELECTED (INDEX 1)",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopRadioGroup(
                        options = sampleOptions,
                        selectedIndex = 1,
                        onSelectedChange = {},
                    )

                    Text(
                        text = "NONE SELECTED",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopRadioGroup(
                        options = sampleOptions,
                        selectedIndex = -1,
                        onSelectedChange = {},
                    )

                    Text(
                        text = "DISABLED",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopRadioGroup(
                        options = sampleOptions,
                        selectedIndex = 0,
                        onSelectedChange = {},
                        enabled = false,
                    )
                }
            }
        }
        onRoot().captureRoboImage(
            filePath = "src/desktopTest/snapshots/PopRadioGroup_allVariants_dark.png",
        )
    }
}
