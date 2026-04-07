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
import com.electricpop.foundation.PopSectionHeader
import com.electricpop.theme.ElectricPopTheme

@Composable
fun PopSectionHeaderDemo() {
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
        PopSectionHeader(title = "Performance")

        // Label + Title
        Text(
            text = "LABEL + TITLE",
            style = MaterialTheme.typography.labelLarge,
            color = cs.onSurfaceVariant,
        )
        PopSectionHeader(title = "System Status", label = "Overview")

        // Numbered: 01
        Text(
            text = "NUMBERED (01)",
            style = MaterialTheme.typography.labelLarge,
            color = cs.onSurfaceVariant,
        )
        PopSectionHeader(title = "Getting Started", number = 1)

        // Numbered: 02
        Text(
            text = "NUMBERED WITH LABEL (02)",
            style = MaterialTheme.typography.labelLarge,
            color = cs.onSurfaceVariant,
        )
        PopSectionHeader(title = "Configuration", label = "Setup Guide", number = 2)

        // Numbered: 03
        Text(
            text = "NUMBERED (03)",
            style = MaterialTheme.typography.labelLarge,
            color = cs.onSurfaceVariant,
        )
        PopSectionHeader(title = "Advanced Topics", number = 3)

        // Secondary accent
        Text(
            text = "SECONDARY ACCENT",
            style = MaterialTheme.typography.labelLarge,
            color = cs.onSurfaceVariant,
        )
        PopSectionHeader(title = "Highlights", label = "Featured", accentColor = cs.secondary)

        // Tertiary accent
        Text(
            text = "TERTIARY ACCENT",
            style = MaterialTheme.typography.labelLarge,
            color = cs.onSurfaceVariant,
        )
        PopSectionHeader(title = "Analytics", label = "Data", accentColor = cs.tertiary)
    }
}
