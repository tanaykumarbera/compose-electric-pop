package co.tanay.electricpop.demo.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import co.tanay.electricpop.composite.PopActionCard
import co.tanay.electricpop.composite.PopActionCardAction
import co.tanay.electricpop.foundation.PopButtonStyle
import co.tanay.electricpop.foundation.PopChip
import co.tanay.electricpop.foundation.PopChipColor
import co.tanay.electricpop.foundation.PopDisplayText
import co.tanay.electricpop.foundation.PopDisplayTextDirection
import co.tanay.electricpop.foundation.PopDisplayTextSize
import co.tanay.electricpop.foundation.PopIcons
import co.tanay.electricpop.theme.ElectricPopTheme

@Composable
fun PopActionCardDemo() {
    val spacing = ElectricPopTheme.spacing

    // Read composable icon values at composable scope
    val iconCheckCircle = PopIcons.CheckCircle
    val iconClose = PopIcons.Close
    val iconBolt = PopIcons.Bolt

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(spacing.lg),
        verticalArrangement = Arrangement.spacedBy(spacing.lg),
    ) {
        // Section: Transaction Review
        Text(
            "TRANSACTION REVIEW",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopActionCard(
            title = "Transaction Review",
            description = "Do you want to track this transaction?",
            chips = {
                PopChip(label = "Bitcoin", color = PopChipColor.Primary)
                PopChip(label = "Transfer", color = PopChipColor.Tertiary)
            },
            heroContent = {
                PopDisplayText(
                    mainText = "\$42,069",
                    fractionalText = ".42",
                    direction = PopDisplayTextDirection.Positive,
                    size = PopDisplayTextSize.Large,
                )
            },
            actions = listOf(
                PopActionCardAction("Skip", {}, PopButtonStyle.Ghost),
                PopActionCardAction("Track", {}, PopButtonStyle.Primary, iconCheckCircle),
            ),
        )

        // Section: Payment Confirmation
        Text(
            "PAYMENT CONFIRMATION",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopActionCard(
            title = "Payment Confirmation",
            description = "Confirm your outgoing payment to Wallet #4821.",
            chips = {
                PopChip(label = "Ethereum", color = PopChipColor.Secondary)
                PopChip(label = "Outgoing", color = PopChipColor.Primary)
            },
            heroContent = {
                PopDisplayText(
                    mainText = "-\$1,200",
                    fractionalText = ".00",
                    direction = PopDisplayTextDirection.Negative,
                    size = PopDisplayTextSize.Large,
                )
            },
            actions = listOf(
                PopActionCardAction("Cancel", {}, PopButtonStyle.Ghost, iconClose),
                PopActionCardAction("Pay", {}, PopButtonStyle.Primary, iconBolt),
            ),
        )

        // Section: Without Description
        Text(
            "WITHOUT DESCRIPTION",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopActionCard(
            title = "Quick Action",
            chips = {
                PopChip(label = "Bitcoin", color = PopChipColor.Primary)
                PopChip(label = "Transfer", color = PopChipColor.Tertiary)
            },
            heroContent = {
                PopDisplayText(
                    mainText = "\$8,400",
                    fractionalText = ".00",
                    direction = PopDisplayTextDirection.Positive,
                    size = PopDisplayTextSize.Medium,
                )
            },
            actions = listOf(
                PopActionCardAction("Skip", {}, PopButtonStyle.Ghost),
                PopActionCardAction("Track", {}, PopButtonStyle.Primary, iconCheckCircle),
            ),
        )

        // Section: Minimal
        Text(
            "MINIMAL",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopActionCard(
            title = "Are You Sure?",
            description = "This action cannot be undone. Proceed with caution.",
            actions = listOf(
                PopActionCardAction("Dismiss", {}, PopButtonStyle.Ghost),
                PopActionCardAction("Confirm", {}, PopButtonStyle.Primary),
            ),
        )
    }
}
