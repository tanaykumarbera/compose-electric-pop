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
import com.electricpop.foundation.PopPillColor
import com.electricpop.foundation.PopTitleBar
import com.electricpop.foundation.PopTitleBarStyle
import com.electricpop.theme.ElectricPopTheme

@Composable
fun PopTitleBarDemo() {
    val spacing = ElectricPopTheme.spacing
    val cs = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(spacing.lg),
        verticalArrangement = Arrangement.spacedBy(spacing.lg),
    ) {
        // Title only
        Text(
            text = "TITLE ONLY",
            style = MaterialTheme.typography.labelLarge,
            color = cs.onSurfaceVariant,
        )
        PopTitleBar(title = "Dashboard")

        // Primary pill
        Text(
            text = "PRIMARY PILL",
            style = MaterialTheme.typography.labelLarge,
            color = cs.onSurfaceVariant,
        )
        PopTitleBar(title = "Transactions", pill = "Live")

        // Secondary pill
        Text(
            text = "SECONDARY PILL",
            style = MaterialTheme.typography.labelLarge,
            color = cs.onSurfaceVariant,
        )
        PopTitleBar(title = "Analytics", pill = "Beta", pillColor = PopPillColor.Secondary)

        // Tertiary pill
        Text(
            text = "TERTIARY PILL",
            style = MaterialTheme.typography.labelLarge,
            color = cs.onSurfaceVariant,
        )
        PopTitleBar(title = "Portfolio", pill = "New", pillColor = PopPillColor.Tertiary)

        // Error pill
        Text(
            text = "ERROR PILL",
            style = MaterialTheme.typography.labelLarge,
            color = cs.onSurfaceVariant,
        )
        PopTitleBar(title = "Alerts", pill = "Critical", pillColor = PopPillColor.Error)

        // Neutral pill
        Text(
            text = "NEUTRAL PILL",
            style = MaterialTheme.typography.labelLarge,
            color = cs.onSurfaceVariant,
        )
        PopTitleBar(title = "Settings", pill = "v2.0", pillColor = PopPillColor.Neutral)

        // Medium style
        Text(
            text = "MEDIUM STYLE",
            style = MaterialTheme.typography.labelLarge,
            color = cs.onSurfaceVariant,
        )
        PopTitleBar(title = "Reports", pill = "Weekly", style = PopTitleBarStyle.Medium)

        // Small style
        Text(
            text = "SMALL STYLE",
            style = MaterialTheme.typography.labelLarge,
            color = cs.onSurfaceVariant,
        )
        PopTitleBar(title = "Activity", pill = "Today", style = PopTitleBarStyle.Small)

        // Custom pill colors
        Text(
            text = "CUSTOM PILL COLORS",
            style = MaterialTheme.typography.labelLarge,
            color = cs.onSurfaceVariant,
        )
        PopTitleBar(
            title = "Staking",
            pill = "Active",
            pillContainerColor = cs.tertiary,
            pillContentColor = cs.onTertiary,
        )
    }
}
