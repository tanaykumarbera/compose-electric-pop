package com.electricpop.foundation

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
import com.electricpop.theme.ElectricPopTheme
import io.github.takahirom.roborazzi.captureRoboImage
import kotlin.test.Test

class PopCodeBlockScreenshotTest {

    private val kotlinSnippet = """
val theme = ElectricPopTheme {
    colorScheme = darkColorScheme()
}
""".trimIndent()

    private val jsonSnippet = """
{
  "component": "PopCodeBlock",
  "tier": "foundation"
}
""".trimIndent()

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun popCodeBlock_allVariants_light() = runDesktopComposeUiTest(width = 550, height = 1500) {
        setContent {
            ElectricPopTheme(darkTheme = false) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                ) {
                    Text(
                        text = "KOTLIN SNIPPET",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopCodeBlock(
                        code = kotlinSnippet,
                        label = "KOTLIN",
                        onCopy = {},
                    )

                    Text(
                        text = "JSON DATA",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopCodeBlock(
                        code = jsonSnippet,
                        label = "JSON",
                        onCopy = {},
                    )

                    Text(
                        text = "SHELL COMMAND",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopCodeBlock(
                        code = "./gradlew :library:build",
                        label = "SHELL",
                        onCopy = {},
                    )

                    Text(
                        text = "NO HEADER",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopCodeBlock(
                        code = "No label, no copy button.\nJust pre-formatted text.",
                    )

                    Text(
                        text = "LABEL ONLY",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopCodeBlock(
                        code = "SELECT * FROM components\nWHERE tier = 'foundation';",
                        label = "SQL",
                    )

                    Text(
                        text = "CUSTOM COLORS",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopCodeBlock(
                        code = "primary: #CAFD00\nsecondary: #FF2D78",
                        label = "CONFIG",
                        onCopy = {},
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                    )
                }
            }
        }
        onRoot().captureRoboImage("src/desktopTest/snapshots/PopCodeBlock_allVariants_light.png")
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun popCodeBlock_allVariants_dark() = runDesktopComposeUiTest(width = 550, height = 1500) {
        setContent {
            ElectricPopTheme(darkTheme = true) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                ) {
                    Text(
                        text = "KOTLIN SNIPPET",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopCodeBlock(
                        code = kotlinSnippet,
                        label = "KOTLIN",
                        onCopy = {},
                    )

                    Text(
                        text = "JSON DATA",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopCodeBlock(
                        code = jsonSnippet,
                        label = "JSON",
                        onCopy = {},
                    )

                    Text(
                        text = "SHELL COMMAND",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopCodeBlock(
                        code = "./gradlew :library:build",
                        label = "SHELL",
                        onCopy = {},
                    )

                    Text(
                        text = "NO HEADER",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopCodeBlock(
                        code = "No label, no copy button.\nJust pre-formatted text.",
                    )

                    Text(
                        text = "LABEL ONLY",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopCodeBlock(
                        code = "SELECT * FROM components\nWHERE tier = 'foundation';",
                        label = "SQL",
                    )

                    Text(
                        text = "CUSTOM COLORS",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopCodeBlock(
                        code = "primary: #CAFD00\nsecondary: #FF2D78",
                        label = "CONFIG",
                        onCopy = {},
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                    )
                }
            }
        }
        onRoot().captureRoboImage("src/desktopTest/snapshots/PopCodeBlock_allVariants_dark.png")
    }
}
