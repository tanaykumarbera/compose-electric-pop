package com.electricpop.demo.components

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
import com.electricpop.composite.PopDataRow
import com.electricpop.composite.PopDataRowDefaults
import com.electricpop.foundation.PopDisplayTextDirection
import com.electricpop.foundation.PopIcons
import com.electricpop.theme.ElectricPopTheme

@Composable
fun PopDataRowDemo() {
    val spacing = ElectricPopTheme.spacing

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(spacing.lg),
        verticalArrangement = Arrangement.spacedBy(spacing.lg),
    ) {
        // Section 1: Transaction Rows
        Text(
            text = "TRANSACTION ROWS",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Column(verticalArrangement = Arrangement.spacedBy(spacing.xxs)) {
            PopDataRow(
                icon = PopIcons.Star,
                label = "Apple Store",
                value = "-\$1,299.00",
                chip = PopDataRowDefaults.chipTertiary("Tech"),
                subtitle = "Oct 24",
                direction = PopDisplayTextDirection.Negative,
                isAlternate = false,
            )
            PopDataRow(
                icon = PopIcons.Heart,
                label = "Gion Sushi",
                value = "-\$84.50",
                chip = PopDataRowDefaults.chipSecondary("Dining"),
                subtitle = "Oct 22",
                direction = PopDisplayTextDirection.Negative,
                isAlternate = true,
            )
            PopDataRow(
                icon = PopIcons.Bolt,
                label = "Salary Deposit",
                value = "+\$4,200.00",
                chip = PopDataRowDefaults.chipPrimary("Income"),
                subtitle = "Oct 20",
                direction = PopDisplayTextDirection.Positive,
                isAlternate = false,
            )
            PopDataRow(
                icon = PopIcons.Home,
                label = "Utilities Corp",
                value = "-\$142.12",
                chip = PopDataRowDefaults.chipSurface("Home"),
                subtitle = "Oct 18",
                direction = PopDisplayTextDirection.Negative,
                isAlternate = true,
            )
        }

        // Section 2: Directional Coloring
        Text(
            text = "DIRECTIONAL COLORING",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Column(verticalArrangement = Arrangement.spacedBy(spacing.xxs)) {
            PopDataRow(
                icon = PopIcons.TrendUp,
                label = "Revenue Growth",
                value = "+\$8,421.00",
                chip = PopDataRowDefaults.chipPrimary("Growth"),
                direction = PopDisplayTextDirection.Positive,
            )
            PopDataRow(
                icon = PopIcons.TrendDown,
                label = "Operating Costs",
                value = "-\$3,200.00",
                chip = PopDataRowDefaults.chipSecondary("Cost"),
                direction = PopDisplayTextDirection.Negative,
                isAlternate = true,
            )
            PopDataRow(
                icon = PopIcons.Layers,
                label = "Total Assets",
                value = "\$52,000.00",
                chip = PopDataRowDefaults.chipSurface("Balance"),
                direction = PopDisplayTextDirection.Neutral,
            )
        }

        // Section 3: Minimal
        Text(
            text = "MINIMAL",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Column(verticalArrangement = Arrangement.spacedBy(spacing.xxs)) {
            PopDataRow(
                icon = PopIcons.Info,
                label = "Balance Check",
                value = "\$12,000.00",
                direction = PopDisplayTextDirection.Neutral,
            )
            PopDataRow(
                icon = PopIcons.Settings,
                label = "System Fee",
                value = "-\$2.50",
                direction = PopDisplayTextDirection.Negative,
                isAlternate = true,
            )
        }

        // Section 4: Custom Icon Colors
        Text(
            text = "CUSTOM ICON COLORS",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Column(verticalArrangement = Arrangement.spacedBy(spacing.xxs)) {
            PopDataRow(
                icon = PopIcons.Sparkle,
                label = "Bonus Reward",
                value = "+\$500.00",
                direction = PopDisplayTextDirection.Positive,
                iconContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                iconContentColor = MaterialTheme.colorScheme.onTertiaryContainer,
            )
            PopDataRow(
                icon = PopIcons.Star,
                label = "Premium Sub",
                value = "-\$14.99",
                direction = PopDisplayTextDirection.Negative,
                iconContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                iconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                isAlternate = true,
            )
        }
    }
}
