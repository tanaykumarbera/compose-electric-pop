package co.tanay.electricpop.foundation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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

class PopButtonScreenshotTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun popButton_allVariants_light() = runDesktopComposeUiTest(width = 600, height = 700) {
        setContent {
            ElectricPopTheme(darkTheme = false) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    // Text buttons — all style x size combinations
                    PopButtonStyle.entries.forEach { style ->
                        Text(
                            text = style.name.uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        PopButtonSize.entries.forEach { size ->
                            PopButton(
                                text = "${style.name} ${size.name}",
                                onClick = {},
                                style = style,
                                size = size,
                            )
                        }
                    }

                    // Icon buttons
                    Text(
                        text = "ICON BUTTONS",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        PopButtonStyle.entries.forEach { style ->
                            PopIconButton(
                                icon = PopIcons.Bolt,
                                onClick = {},
                                style = style,
                                contentDescription = style.name,
                            )
                        }
                    }
                }
            }
        }
        onRoot().captureRoboImage(
            filePath = "src/desktopTest/snapshots/PopButton_allVariants_light.png"
        )
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun popButton_allVariants_dark() = runDesktopComposeUiTest(width = 600, height = 700) {
        setContent {
            ElectricPopTheme(darkTheme = true) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    PopButtonStyle.entries.forEach { style ->
                        Text(
                            text = style.name.uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        PopButtonSize.entries.forEach { size ->
                            PopButton(
                                text = "${style.name} ${size.name}",
                                onClick = {},
                                style = style,
                                size = size,
                            )
                        }
                    }

                    Text(
                        text = "ICON BUTTONS",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        PopButtonStyle.entries.forEach { style ->
                            PopIconButton(
                                icon = PopIcons.Bolt,
                                onClick = {},
                                style = style,
                                contentDescription = style.name,
                            )
                        }
                    }
                }
            }
        }
        onRoot().captureRoboImage(
            filePath = "src/desktopTest/snapshots/PopButton_allVariants_dark.png"
        )
    }
}
