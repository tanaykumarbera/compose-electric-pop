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
        verticalArrangement = Arrangement.spacedBy(spacing.xl),
    ) {
        // Full variant — highlight pill + titleAccent (closest to Stitch "Stash Hub Card Doc")
        Text(
            text = "HIGHLIGHT + TITLE ACCENT",
            style = MaterialTheme.typography.labelLarge,
            color = cs.onSurfaceVariant,
        )
        PopSectionHeader(
            title = "Stash Hub",
            highlight = "Component Guide",
            titleAccent = "Card",
        )

        // Highlight pill only (no titleAccent)
        Text(
            text = "HIGHLIGHT ONLY",
            style = MaterialTheme.typography.labelLarge,
            color = cs.onSurfaceVariant,
        )
        PopSectionHeader(
            title = "Live Ledger",
            highlight = "Feature Set",
        )

        // Title only — no pill, no accent
        Text(
            text = "TITLE ONLY",
            style = MaterialTheme.typography.labelLarge,
            color = cs.onSurfaceVariant,
        )
        PopSectionHeader(title = "Performance")

        // Title + description
        Text(
            text = "TITLE + DESCRIPTION",
            style = MaterialTheme.typography.labelLarge,
            color = cs.onSurfaceVariant,
        )
        PopSectionHeader(
            title = "Analytics",
            description = "Track your metrics in real time with live data feeds and customisable dashboards.",
        )

        // Full variant with description
        Text(
            text = "FULL VARIANT + DESCRIPTION",
            style = MaterialTheme.typography.labelLarge,
            color = cs.onSurfaceVariant,
        )
        PopSectionHeader(
            title = "Stash Hub",
            highlight = "Overview",
            titleAccent = "Pro",
            description = "Everything you need to manage your portfolio, in one place.",
        )

        // Custom titleAccentColor (secondary = magenta)
        Text(
            text = "CUSTOM ACCENT COLOR (SECONDARY)",
            style = MaterialTheme.typography.labelLarge,
            color = cs.onSurfaceVariant,
        )
        PopSectionHeader(
            title = "System",
            highlight = "Status",
            titleAccent = "Active",
            titleAccentColor = cs.secondary,
        )
    }
}
