package co.tanay.electricpop.demo.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import co.tanay.electricpop.foundation.PopIconListItem
import co.tanay.electricpop.foundation.PopIconSize
import co.tanay.electricpop.foundation.PopIcons
import co.tanay.electricpop.theme.ElectricPopTheme

@Composable
fun PopIconListItemDemo() {
    val spacing = ElectricPopTheme.spacing
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(spacing.lg),
        verticalArrangement = Arrangement.spacedBy(spacing.sm),
    ) {
        // Default items
        Text(
            text = "DEFAULT ITEMS",
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
        PopIconListItem(
            icon = PopIcons.CheckCircle,
            text = "Real-time balance updates",
        )

        Spacer(Modifier.height(spacing.md))

        // Negative items
        Text(
            text = "NEGATIVE ITEMS",
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

        Spacer(Modifier.height(spacing.md))

        // Custom icons
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

        Spacer(Modifier.height(spacing.md))

        // Tinted icons
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

        Spacer(Modifier.height(spacing.md))

        // Sizing
        Text(
            text = "SIZING",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopIconListItem(
            icon = PopIcons.CheckCircle,
            text = "Small icon size",
            iconSize = PopIconSize.Small,
        )
        PopIconListItem(
            icon = PopIcons.CheckCircle,
            text = "Medium icon size",
            iconSize = PopIconSize.Medium,
        )
        PopIconListItem(
            icon = PopIcons.CheckCircle,
            text = "Large icon size",
            iconSize = PopIconSize.Large,
        )

        Spacer(Modifier.height(spacing.md))

        // Multi-line
        Text(
            text = "MULTI-LINE",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopIconListItem(
            icon = PopIcons.Info,
            text = "This is a longer description text that demonstrates how the component handles wrapping when the content exceeds a single line of available horizontal space",
        )
    }
}
