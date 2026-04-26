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

class PopTextFieldScreenshotTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun allVariants_light() = runDesktopComposeUiTest(width = 500, height = 900) {
        setContent {
            ElectricPopTheme(darkTheme = false) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    // 1. Standard with label and value
                    Text(
                        text = "STANDARD",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopTextField(
                        value = "john_doe",
                        onValueChange = {},
                        label = "Username",
                    )

                    // 2. Standard empty with placeholder
                    PopTextField(
                        value = "",
                        onValueChange = {},
                        placeholder = "Enter email",
                    )

                    // 3. With leading icon
                    Text(
                        text = "WITH ICONS",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopTextField(
                        value = "",
                        onValueChange = {},
                        label = "Search",
                        placeholder = "Search...",
                        leadingIcon = PopIcons.Search,
                    )

                    // 4. Password field (masked)
                    Text(
                        text = "PASSWORD",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopPasswordField(
                        value = "secret123",
                        onValueChange = {},
                        label = "Password",
                    )

                    // 5. Error state
                    Text(
                        text = "ERROR STATE",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopTextField(
                        value = "invalid",
                        onValueChange = {},
                        label = "Email",
                        isError = true,
                        errorMessage = "Invalid email address",
                    )

                    // 6. Disabled state
                    Text(
                        text = "DISABLED",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopTextField(
                        value = "Read only",
                        onValueChange = {},
                        label = "Disabled",
                        enabled = false,
                    )
                }
            }
        }
        onRoot().captureRoboImage(
            filePath = "src/desktopTest/snapshots/PopTextField_allVariants_light.png",
        )
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun allVariants_dark() = runDesktopComposeUiTest(width = 500, height = 900) {
        setContent {
            ElectricPopTheme(darkTheme = true) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    // 1. Standard with label and value
                    Text(
                        text = "STANDARD",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopTextField(
                        value = "john_doe",
                        onValueChange = {},
                        label = "Username",
                    )

                    // 2. Standard empty with placeholder
                    PopTextField(
                        value = "",
                        onValueChange = {},
                        placeholder = "Enter email",
                    )

                    // 3. With leading icon
                    Text(
                        text = "WITH ICONS",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopTextField(
                        value = "",
                        onValueChange = {},
                        label = "Search",
                        placeholder = "Search...",
                        leadingIcon = PopIcons.Search,
                    )

                    // 4. Password field (masked)
                    Text(
                        text = "PASSWORD",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopPasswordField(
                        value = "secret123",
                        onValueChange = {},
                        label = "Password",
                    )

                    // 5. Error state
                    Text(
                        text = "ERROR STATE",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopTextField(
                        value = "invalid",
                        onValueChange = {},
                        label = "Email",
                        isError = true,
                        errorMessage = "Invalid email address",
                    )

                    // 6. Disabled state
                    Text(
                        text = "DISABLED",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopTextField(
                        value = "Read only",
                        onValueChange = {},
                        label = "Disabled",
                        enabled = false,
                    )
                }
            }
        }
        onRoot().captureRoboImage(
            filePath = "src/desktopTest/snapshots/PopTextField_allVariants_dark.png",
        )
    }
}
