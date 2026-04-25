package co.tanay.electricpop.demo.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import co.tanay.electricpop.foundation.PopPill
import co.tanay.electricpop.foundation.PopPillColor
import co.tanay.electricpop.theme.ElectricPopTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PopPillDemo() {
    val spacing = ElectricPopTheme.spacing
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(spacing.lg),
        verticalArrangement = Arrangement.spacedBy(spacing.lg),
    ) {
        // Section: Color Presets
        Text(
            text = "COLOR PRESETS",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(spacing.xs),
            verticalArrangement = Arrangement.spacedBy(spacing.xs),
        ) {
            PopPill(label = "Active", color = PopPillColor.Primary)
            PopPill(label = "Live", color = PopPillColor.Secondary)
            PopPill(label = "Synced", color = PopPillColor.Tertiary)
            PopPill(label = "Failed", color = PopPillColor.Error)
            PopPill(label = "Locked", color = PopPillColor.Neutral)
        }

        // Section: Primary Variant Labels
        Text(
            text = "PRIMARY VARIANT",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(spacing.xs),
            verticalArrangement = Arrangement.spacedBy(spacing.xs),
        ) {
            PopPill(label = "Beta")
            PopPill(label = "New")
            PopPill(label = "Updated")
        }

        // Section: Custom Colors
        Text(
            text = "CUSTOM COLORS",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(spacing.xs),
            verticalArrangement = Arrangement.spacedBy(spacing.xs),
        ) {
            PopPill(
                label = "Inverse",
                containerColor = MaterialTheme.colorScheme.inverseSurface,
                contentColor = MaterialTheme.colorScheme.inverseOnSurface,
            )
            PopPill(
                label = "Surface",
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                contentColor = MaterialTheme.colorScheme.onSurface,
            )
        }

        // Section: Various Lengths
        Text(
            text = "VARIOUS LENGTHS",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(spacing.xs),
            verticalArrangement = Arrangement.spacedBy(spacing.xs),
        ) {
            PopPill(label = "OK", color = PopPillColor.Primary)
            PopPill(label = "Pending Approval", color = PopPillColor.Secondary)
            PopPill(label = "In Progress", color = PopPillColor.Tertiary)
        }
    }
}
