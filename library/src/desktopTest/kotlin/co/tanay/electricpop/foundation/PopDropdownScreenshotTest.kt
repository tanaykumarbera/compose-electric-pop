package co.tanay.electricpop.foundation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runDesktopComposeUiTest
import androidx.compose.ui.unit.dp
import co.tanay.electricpop.theme.ElectricPopTheme
import io.github.takahirom.roborazzi.captureRoboImage
import kotlin.test.Test

class PopDropdownScreenshotTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun allVariants_light() = runDesktopComposeUiTest(width = 500, height = 500) {
        setContent {
            ElectricPopTheme(darkTheme = false) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    PopDropdown(
                        options = listOf("USD", "EUR", "GBP", "JPY"),
                        selectedOption = "USD",
                        onOptionSelected = {},
                        label = "Currency",
                        modifier = Modifier.fillMaxWidth(),
                    )
                    PopDropdown(
                        options = listOf("Savings", "Checking", "Investment"),
                        selectedOption = "Savings",
                        onOptionSelected = {},
                        modifier = Modifier.fillMaxWidth(),
                    )
                    PopDropdown(
                        options = listOf("Personal", "Business", "Joint"),
                        selectedOption = "",
                        onOptionSelected = {},
                        label = "Account Type",
                        placeholder = "Select an account",
                        modifier = Modifier.fillMaxWidth(),
                    )
                    PopDropdown(
                        options = listOf("North America", "Europe", "Asia"),
                        selectedOption = "North America",
                        onOptionSelected = {},
                        label = "Region",
                        enabled = false,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
        onRoot().captureRoboImage(
            filePath = "src/desktopTest/snapshots/PopDropdown_allVariants_light.png",
        )
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun allVariants_dark() = runDesktopComposeUiTest(width = 500, height = 500) {
        setContent {
            ElectricPopTheme(darkTheme = true) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    PopDropdown(
                        options = listOf("USD", "EUR", "GBP", "JPY"),
                        selectedOption = "USD",
                        onOptionSelected = {},
                        label = "Currency",
                        modifier = Modifier.fillMaxWidth(),
                    )
                    PopDropdown(
                        options = listOf("Savings", "Checking", "Investment"),
                        selectedOption = "Savings",
                        onOptionSelected = {},
                        modifier = Modifier.fillMaxWidth(),
                    )
                    PopDropdown(
                        options = listOf("Personal", "Business", "Joint"),
                        selectedOption = "",
                        onOptionSelected = {},
                        label = "Account Type",
                        placeholder = "Select an account",
                        modifier = Modifier.fillMaxWidth(),
                    )
                    PopDropdown(
                        options = listOf("North America", "Europe", "Asia"),
                        selectedOption = "North America",
                        onOptionSelected = {},
                        label = "Region",
                        enabled = false,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
        onRoot().captureRoboImage(
            filePath = "src/desktopTest/snapshots/PopDropdown_allVariants_dark.png",
        )
    }

    // Expanded trigger state — shows surfaceContainer bg, accent bar, rotated chevron.
    // Note: DropdownMenu popup renders in a separate window layer and cannot be captured here.
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun expandedTrigger_light() = runDesktopComposeUiTest(width = 500, height = 300) {
        setContent {
            ElectricPopTheme(darkTheme = false) {
                Column(modifier = Modifier.padding(16.dp)) {
                    PopDropdown(
                        options = listOf("USD", "EUR", "GBP", "JPY"),
                        selectedOption = "USD",
                        onOptionSelected = {},
                        label = "Currency",
                        initialExpanded = true,
                        showMenuOnExpand = false,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
        onRoot().captureRoboImage(
            filePath = "src/desktopTest/snapshots/PopDropdown_expandedTrigger_light.png",
        )
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun expandedTrigger_dark() = runDesktopComposeUiTest(width = 500, height = 300) {
        setContent {
            ElectricPopTheme(darkTheme = true) {
                Column(modifier = Modifier.padding(16.dp)) {
                    PopDropdown(
                        options = listOf("USD", "EUR", "GBP", "JPY"),
                        selectedOption = "USD",
                        onOptionSelected = {},
                        label = "Currency",
                        initialExpanded = true,
                        showMenuOnExpand = false,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
        onRoot().captureRoboImage(
            filePath = "src/desktopTest/snapshots/PopDropdown_expandedTrigger_dark.png",
        )
    }
}
