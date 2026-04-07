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

class PopStepListScreenshotTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun popStepList_allVariants_light() = runDesktopComposeUiTest(width = 500, height = 1400) {
        setContent {
            ElectricPopTheme(darkTheme = false) {
                val numberedSteps = listOf(
                    PopStep(label = "Respect Gridlines", description = "Use data-driven axis intervals. Place grid lines at meaningful breakpoints, not arbitrary ones."),
                    PopStep(label = "Color Intent", description = "Apply primary for positive trends, error for negative. Never use color purely for decoration."),
                    PopStep(label = "Technical Speed", description = "Ensure charts render within 200ms. Use data sampling for datasets exceeding 1000 points."),
                )
                val numberedCustomSteps = listOf(
                    PopStep(label = "Respect Gridlines", description = "Use data-driven axis intervals. Place grid lines at meaningful breakpoints, not arbitrary ones."),
                    PopStep(label = "Color Intent", description = "Apply primary for positive trends, error for negative. Never use color purely for decoration."),
                    PopStep(label = "Technical Speed", description = "Ensure charts render within 200ms. Use data sampling for datasets exceeding 1000 points."),
                )
                val iconSteps = listOf(
                    PopStep(icon = PopIcons.Search, label = "Find Token", description = "Search the marketplace for tokens that match your portfolio strategy"),
                    PopStep(icon = PopIcons.Bolt, label = "Quick Swap", description = "Execute instant swaps with zero slippage on supported pairs"),
                    PopStep(icon = PopIcons.CheckCircle, label = "Confirm", description = "Review and confirm your transaction details before signing"),
                )
                val iconCustomSteps = listOf(
                    PopStep(icon = PopIcons.Person, label = "Create Profile", description = "Set up your identity and customize your dashboard preferences"),
                    PopStep(icon = PopIcons.Settings, label = "Configure", description = "Adjust notification settings, security options, and display themes"),
                    PopStep(icon = PopIcons.Star, label = "Go Premium", description = "Unlock advanced analytics, priority support, and exclusive features"),
                )
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                ) {
                    Text(
                        text = "NUMBERED (DEFAULT)",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopStepList(steps = numberedSteps, showNumbers = true)

                    Text(
                        text = "NUMBERED (CUSTOM COLORS)",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopStepList(
                        steps = numberedCustomSteps,
                        showNumbers = true,
                        numberColor = MaterialTheme.colorScheme.tertiary,
                    )

                    Text(
                        text = "ICON (DEFAULT)",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopStepList(steps = iconSteps, showNumbers = false)

                    Text(
                        text = "ICON (CUSTOM COLORS)",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopStepList(
                        steps = iconCustomSteps,
                        showNumbers = false,
                        iconTint = MaterialTheme.colorScheme.secondary,
                    )
                }
            }
        }
        onRoot().captureRoboImage("src/desktopTest/snapshots/PopStepList_allVariants_light.png")
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun popStepList_allVariants_dark() = runDesktopComposeUiTest(width = 500, height = 1400) {
        setContent {
            ElectricPopTheme(darkTheme = true) {
                val numberedSteps = listOf(
                    PopStep(label = "Respect Gridlines", description = "Use data-driven axis intervals. Place grid lines at meaningful breakpoints, not arbitrary ones."),
                    PopStep(label = "Color Intent", description = "Apply primary for positive trends, error for negative. Never use color purely for decoration."),
                    PopStep(label = "Technical Speed", description = "Ensure charts render within 200ms. Use data sampling for datasets exceeding 1000 points."),
                )
                val numberedCustomSteps = listOf(
                    PopStep(label = "Respect Gridlines", description = "Use data-driven axis intervals. Place grid lines at meaningful breakpoints, not arbitrary ones."),
                    PopStep(label = "Color Intent", description = "Apply primary for positive trends, error for negative. Never use color purely for decoration."),
                    PopStep(label = "Technical Speed", description = "Ensure charts render within 200ms. Use data sampling for datasets exceeding 1000 points."),
                )
                val iconSteps = listOf(
                    PopStep(icon = PopIcons.Search, label = "Find Token", description = "Search the marketplace for tokens that match your portfolio strategy"),
                    PopStep(icon = PopIcons.Bolt, label = "Quick Swap", description = "Execute instant swaps with zero slippage on supported pairs"),
                    PopStep(icon = PopIcons.CheckCircle, label = "Confirm", description = "Review and confirm your transaction details before signing"),
                )
                val iconCustomSteps = listOf(
                    PopStep(icon = PopIcons.Person, label = "Create Profile", description = "Set up your identity and customize your dashboard preferences"),
                    PopStep(icon = PopIcons.Settings, label = "Configure", description = "Adjust notification settings, security options, and display themes"),
                    PopStep(icon = PopIcons.Star, label = "Go Premium", description = "Unlock advanced analytics, priority support, and exclusive features"),
                )
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                ) {
                    Text(
                        text = "NUMBERED (DEFAULT)",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopStepList(steps = numberedSteps, showNumbers = true)

                    Text(
                        text = "NUMBERED (CUSTOM COLORS)",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopStepList(
                        steps = numberedCustomSteps,
                        showNumbers = true,
                        numberColor = MaterialTheme.colorScheme.tertiary,
                    )

                    Text(
                        text = "ICON (DEFAULT)",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopStepList(steps = iconSteps, showNumbers = false)

                    Text(
                        text = "ICON (CUSTOM COLORS)",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopStepList(
                        steps = iconCustomSteps,
                        showNumbers = false,
                        iconTint = MaterialTheme.colorScheme.secondary,
                    )
                }
            }
        }
        onRoot().captureRoboImage("src/desktopTest/snapshots/PopStepList_allVariants_dark.png")
    }
}
