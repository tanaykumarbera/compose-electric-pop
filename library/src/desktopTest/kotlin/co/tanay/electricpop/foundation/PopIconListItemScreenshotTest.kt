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
import co.tanay.electricpop.foundation.PopIcons
import io.github.takahirom.roborazzi.captureRoboImage
import kotlin.test.Test

class PopIconListItemScreenshotTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun popIconListItem_allVariants_light() = runDesktopComposeUiTest(width = 500, height = 700) {
        setContent {
            ElectricPopTheme(darkTheme = false) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = "DEFAULT (CHECK)",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopIconListItem(
                        icon = PopIcons.CheckCircle,
                        text = "Transactions are encrypted end-to-end",
                    )
                    PopIconListItem(
                        icon = PopIcons.CheckCircle,
                        text = "Two-factor authentication enabled",
                    )

                    Text(
                        text = "NEGATIVE (CANCEL)",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopIconListItem(
                        icon = PopIcons.Close,
                        text = "Unverified accounts cannot send funds",
                        iconTint = MaterialTheme.colorScheme.error,
                        textColor = MaterialTheme.colorScheme.error,
                    )
                    PopIconListItem(
                        icon = PopIcons.Close,
                        text = "Expired sessions are automatically terminated",
                        iconTint = MaterialTheme.colorScheme.error,
                        textColor = MaterialTheme.colorScheme.error,
                    )

                    Text(
                        text = "CUSTOM ICONS",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopIconListItem(
                        icon = PopIcons.Info,
                        text = "Review your account details before proceeding",
                    )
                    PopIconListItem(
                        icon = PopIcons.Warning,
                        text = "This action cannot be undone",
                        iconTint = MaterialTheme.colorScheme.tertiary,
                    )
                    PopIconListItem(
                        icon = PopIcons.Star,
                        text = "Premium features require an active subscription",
                    )

                    Text(
                        text = "TINTED ICONS",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopIconListItem(
                        icon = PopIcons.CheckCircle,
                        text = "Payment verified successfully",
                        iconTint = MaterialTheme.colorScheme.primary,
                    )
                    PopIconListItem(
                        icon = PopIcons.Bolt,
                        text = "Instant transfer available",
                        iconTint = MaterialTheme.colorScheme.secondary,
                    )
                    PopIconListItem(
                        icon = PopIcons.Heart,
                        text = "Added to favorites",
                        iconTint = MaterialTheme.colorScheme.tertiary,
                    )

                    Text(
                        text = "MULTI-LINE TEXT",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopIconListItem(
                        icon = PopIcons.Info,
                        text = "This is a longer description that should wrap to multiple lines to demonstrate how the component handles text overflow and line wrapping behavior",
                    )
                }
            }
        }
        onRoot().captureRoboImage("src/desktopTest/snapshots/PopIconListItem_allVariants_light.png")
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun popIconListItem_allVariants_dark() = runDesktopComposeUiTest(width = 500, height = 700) {
        setContent {
            ElectricPopTheme(darkTheme = true) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = "DEFAULT (CHECK)",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopIconListItem(
                        icon = PopIcons.CheckCircle,
                        text = "Transactions are encrypted end-to-end",
                    )
                    PopIconListItem(
                        icon = PopIcons.CheckCircle,
                        text = "Two-factor authentication enabled",
                    )

                    Text(
                        text = "NEGATIVE (CANCEL)",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopIconListItem(
                        icon = PopIcons.Close,
                        text = "Unverified accounts cannot send funds",
                        iconTint = MaterialTheme.colorScheme.error,
                        textColor = MaterialTheme.colorScheme.error,
                    )
                    PopIconListItem(
                        icon = PopIcons.Close,
                        text = "Expired sessions are automatically terminated",
                        iconTint = MaterialTheme.colorScheme.error,
                        textColor = MaterialTheme.colorScheme.error,
                    )

                    Text(
                        text = "CUSTOM ICONS",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopIconListItem(
                        icon = PopIcons.Info,
                        text = "Review your account details before proceeding",
                    )
                    PopIconListItem(
                        icon = PopIcons.Warning,
                        text = "This action cannot be undone",
                        iconTint = MaterialTheme.colorScheme.tertiary,
                    )
                    PopIconListItem(
                        icon = PopIcons.Star,
                        text = "Premium features require an active subscription",
                    )

                    Text(
                        text = "TINTED ICONS",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopIconListItem(
                        icon = PopIcons.CheckCircle,
                        text = "Payment verified successfully",
                        iconTint = MaterialTheme.colorScheme.primary,
                    )
                    PopIconListItem(
                        icon = PopIcons.Bolt,
                        text = "Instant transfer available",
                        iconTint = MaterialTheme.colorScheme.secondary,
                    )
                    PopIconListItem(
                        icon = PopIcons.Heart,
                        text = "Added to favorites",
                        iconTint = MaterialTheme.colorScheme.tertiary,
                    )

                    Text(
                        text = "MULTI-LINE TEXT",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopIconListItem(
                        icon = PopIcons.Info,
                        text = "This is a longer description that should wrap to multiple lines to demonstrate how the component handles text overflow and line wrapping behavior",
                    )
                }
            }
        }
        onRoot().captureRoboImage("src/desktopTest/snapshots/PopIconListItem_allVariants_dark.png")
    }
}
