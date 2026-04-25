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

class PopTableScreenshotTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun popTable_allVariants_light() = runDesktopComposeUiTest(width = 500, height = 1300) {
        setContent {
            ElectricPopTheme(darkTheme = false) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                ) {
                    Text(
                        text = "DEFAULT (SECONDARY CONTAINER)",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopTable(
                        title = "Tokens",
                        rows = listOf(
                            PopTableRow("Radius", "3rem (XL)"),
                            PopTableRow("Font (H)", "Space Grotesk"),
                            PopTableRow("Weight", "900 Italic"),
                            PopTableRow("Elevation", "32px Blur"),
                            PopTableRow("Accent", "Lime #CAFD00"),
                        ),
                    )

                    Text(
                        text = "TERTIARY CONTAINER",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopTable(
                        title = "Network",
                        rows = listOf(
                            PopTableRow("Protocol", "HTTPS"),
                            PopTableRow("Latency", "42ms"),
                            PopTableRow("Uptime", "99.9%"),
                        ),
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                    )

                    Text(
                        text = "PRIMARY CONTAINER",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopTable(
                        title = "Performance",
                        rows = listOf(
                            PopTableRow("First Paint", "340ms"),
                            PopTableRow("Hot Restart", "120ms"),
                            PopTableRow("Bundle Size", "2.4mb"),
                        ),
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            }
        }
        onRoot().captureRoboImage("src/desktopTest/snapshots/PopTable_allVariants_light.png")
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun popTable_allVariants_dark() = runDesktopComposeUiTest(width = 500, height = 1300) {
        setContent {
            ElectricPopTheme(darkTheme = true) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                ) {
                    Text(
                        text = "DEFAULT (SECONDARY CONTAINER)",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopTable(
                        title = "Tokens",
                        rows = listOf(
                            PopTableRow("Radius", "3rem (XL)"),
                            PopTableRow("Font (H)", "Space Grotesk"),
                            PopTableRow("Weight", "900 Italic"),
                            PopTableRow("Elevation", "32px Blur"),
                            PopTableRow("Accent", "Lime #CAFD00"),
                        ),
                    )

                    Text(
                        text = "TERTIARY CONTAINER",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopTable(
                        title = "Network",
                        rows = listOf(
                            PopTableRow("Protocol", "HTTPS"),
                            PopTableRow("Latency", "42ms"),
                            PopTableRow("Uptime", "99.9%"),
                        ),
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                    )

                    Text(
                        text = "PRIMARY CONTAINER",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopTable(
                        title = "Performance",
                        rows = listOf(
                            PopTableRow("First Paint", "340ms"),
                            PopTableRow("Hot Restart", "120ms"),
                            PopTableRow("Bundle Size", "2.4mb"),
                        ),
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            }
        }
        onRoot().captureRoboImage("src/desktopTest/snapshots/PopTable_allVariants_dark.png")
    }
}
