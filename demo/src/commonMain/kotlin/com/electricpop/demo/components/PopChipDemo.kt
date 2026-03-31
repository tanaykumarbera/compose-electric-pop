package com.electricpop.demo.components

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
import com.electricpop.foundation.PopChip
import com.electricpop.foundation.PopChipColor
import com.electricpop.foundation.PopIcons
import com.electricpop.theme.ElectricPopTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PopChipDemo() {
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
            PopChip(label = "Design", color = PopChipColor.Primary)
            PopChip(label = "Engineering", color = PopChipColor.Secondary)
            PopChip(label = "Analytics", color = PopChipColor.Tertiary)
        }

        // Section: With Icons
        Text(
            text = "WITH ICONS",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(spacing.xs),
            verticalArrangement = Arrangement.spacedBy(spacing.xs),
        ) {
            PopChip(label = "Starred", color = PopChipColor.Primary, icon = PopIcons.Star)
            PopChip(label = "Favorites", color = PopChipColor.Secondary, icon = PopIcons.Heart)
            PopChip(label = "Trending", color = PopChipColor.Tertiary, icon = PopIcons.TrendUp)
        }

        // Section: Clickable Chips (filter use case)
        Text(
            text = "CLICKABLE (FILTER)",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(spacing.xs),
            verticalArrangement = Arrangement.spacedBy(spacing.xs),
        ) {
            PopChip(label = "All", color = PopChipColor.Primary, onClick = {})
            PopChip(label = "Active", color = PopChipColor.Secondary, onClick = {})
            PopChip(label = "Archived", color = PopChipColor.Tertiary, onClick = {})
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
            PopChip(label = "AI", color = PopChipColor.Primary)
            PopChip(label = "Machine Learning", color = PopChipColor.Secondary)
            PopChip(label = "Natural Language Processing", color = PopChipColor.Tertiary)
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
            PopChip(
                label = "Inverse",
                containerColor = MaterialTheme.colorScheme.inverseSurface,
                contentColor = MaterialTheme.colorScheme.inverseOnSurface,
            )
            PopChip(
                label = "Surface",
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                contentColor = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}
