package co.tanay.electricpop.foundation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runDesktopComposeUiTest
import androidx.compose.ui.unit.dp
import co.tanay.electricpop.theme.ElectricPopTheme
import io.github.takahirom.roborazzi.captureRoboImage
import kotlin.test.Test

class PopSurfaceScreenshotTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun popSurface_allTones_light() = runDesktopComposeUiTest(width = 500, height = 700) {
        setContent {
            ElectricPopTheme(darkTheme = false) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    PopSurfaceTone.entries.forEach { tone ->
                        PopSurface(
                            tone = tone,
                            modifier = Modifier.fillMaxWidth().height(60.dp),
                        ) {
                            Text(
                                text = tone.name,
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    }
                }
            }
        }
        onRoot().captureRoboImage(
            filePath = "src/desktopTest/snapshots/PopSurface_allTones_light.png"
        )
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun popSurface_allTones_dark() = runDesktopComposeUiTest(width = 500, height = 700) {
        setContent {
            ElectricPopTheme(darkTheme = true) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    PopSurfaceTone.entries.forEach { tone ->
                        PopSurface(
                            tone = tone,
                            modifier = Modifier.fillMaxWidth().height(60.dp),
                        ) {
                            Text(
                                text = tone.name,
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    }
                }
            }
        }
        onRoot().captureRoboImage(
            filePath = "src/desktopTest/snapshots/PopSurface_allTones_dark.png"
        )
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun popSurface_ghostBorder_light() = runDesktopComposeUiTest(width = 500, height = 200) {
        setContent {
            ElectricPopTheme(darkTheme = false) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    PopSurface(
                        ghostBorder = true,
                        modifier = Modifier.fillMaxWidth().height(80.dp),
                    ) {
                        Text(
                            text = "Ghost Border",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
            }
        }
        onRoot().captureRoboImage(
            filePath = "src/desktopTest/snapshots/PopSurface_ghostBorder_light.png"
        )
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun popSurface_ghostBorder_dark() = runDesktopComposeUiTest(width = 500, height = 200) {
        setContent {
            ElectricPopTheme(darkTheme = true) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    PopSurface(
                        ghostBorder = true,
                        modifier = Modifier.fillMaxWidth().height(80.dp),
                    ) {
                        Text(
                            text = "Ghost Border",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
            }
        }
        onRoot().captureRoboImage(
            filePath = "src/desktopTest/snapshots/PopSurface_ghostBorder_dark.png"
        )
    }
}
