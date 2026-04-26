package co.tanay.electricpop.demo.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import co.tanay.electricpop.foundation.PopStep
import co.tanay.electricpop.foundation.PopStepList
import co.tanay.electricpop.theme.ElectricPopTheme
import co.tanay.electricpop.foundation.PopIcons

@Composable
fun PopStepListDemo() {
    val spacing = ElectricPopTheme.spacing
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(spacing.lg),
        verticalArrangement = Arrangement.spacedBy(spacing.sm),
    ) {
        // Numbered Steps
        Text(
            text = "NUMBERED STEPS",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopStepList(
            steps = listOf(
                PopStep(label = "Respect Gridlines", description = "Use data-driven axis intervals. Place grid lines at meaningful breakpoints, not arbitrary ones."),
                PopStep(label = "Color Intent", description = "Apply primary for positive trends, error for negative. Never use color purely for decoration."),
                PopStep(label = "Technical Speed", description = "Ensure charts render within 200ms. Use data sampling for datasets exceeding 1000 points."),
            ),
            showNumbers = true,
        )

        Spacer(Modifier.height(spacing.md))

        // Numbered Steps with Custom Colors
        Text(
            text = "NUMBERED (CUSTOM COLORS)",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopStepList(
            steps = listOf(
                PopStep(label = "Respect Gridlines", description = "Use data-driven axis intervals. Place grid lines at meaningful breakpoints, not arbitrary ones."),
                PopStep(label = "Color Intent", description = "Apply primary for positive trends, error for negative. Never use color purely for decoration."),
                PopStep(label = "Technical Speed", description = "Ensure charts render within 200ms. Use data sampling for datasets exceeding 1000 points."),
            ),
            showNumbers = true,
            numberColor = MaterialTheme.colorScheme.tertiary,
        )

        Spacer(Modifier.height(spacing.md))

        // Icon Steps
        Text(
            text = "ICON STEPS",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopStepList(
            steps = listOf(
                PopStep(icon = PopIcons.Search, label = "Find Token", description = "Search the marketplace for tokens that match your portfolio strategy"),
                PopStep(icon = PopIcons.Bolt, label = "Quick Swap", description = "Execute instant swaps with zero slippage on supported pairs"),
                PopStep(icon = PopIcons.CheckCircle, label = "Confirm", description = "Review and confirm your transaction details before signing"),
            ),
            showNumbers = false,
        )

        Spacer(Modifier.height(spacing.md))

        // Icon Steps with Custom Colors
        Text(
            text = "ICON (CUSTOM COLORS)",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopStepList(
            steps = listOf(
                PopStep(icon = PopIcons.Person, label = "Create Profile", description = "Set up your identity and customize your dashboard preferences"),
                PopStep(icon = PopIcons.Settings, label = "Configure", description = "Adjust notification settings, security options, and display themes"),
                PopStep(icon = PopIcons.Star, label = "Go Premium", description = "Unlock advanced analytics, priority support, and exclusive features"),
            ),
            showNumbers = false,
            iconTint = MaterialTheme.colorScheme.secondary,
        )

        Spacer(Modifier.height(spacing.md))

        // Many Steps
        Text(
            text = "MANY STEPS",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopStepList(
            steps = listOf(
                PopStep(label = "Open App", description = "Launch the application from your home screen"),
                PopStep(label = "Sign In", description = "Enter your credentials or use biometric authentication"),
                PopStep(label = "Explore", description = "Browse available assets and market data"),
                PopStep(label = "Trade", description = "Place orders and execute trades with real-time pricing"),
                PopStep(label = "Review", description = "Check your portfolio performance and transaction history"),
            ),
            showNumbers = true,
        )
    }
}
