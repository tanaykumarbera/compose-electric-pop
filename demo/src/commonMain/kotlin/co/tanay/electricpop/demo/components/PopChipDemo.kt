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
import co.tanay.electricpop.foundation.PopChip
import co.tanay.electricpop.foundation.PopChipColor
import co.tanay.electricpop.foundation.PopChipSize
import co.tanay.electricpop.foundation.PopIcons
import co.tanay.electricpop.theme.ElectricPopTheme

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
        // Section: Color Presets (Medium)
        SectionLabel("COLOR PRESETS (MEDIUM)")
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(spacing.xs),
            verticalArrangement = Arrangement.spacedBy(spacing.xs),
        ) {
            PopChip(label = "Design", color = PopChipColor.Primary)
            PopChip(label = "Engineering", color = PopChipColor.Secondary)
            PopChip(label = "Analytics", color = PopChipColor.Tertiary)
            PopChip(label = "Failed", color = PopChipColor.Error)
            PopChip(label = "Locked", color = PopChipColor.Neutral)
        }

        // Section: Sizes (Primary)
        SectionLabel("SIZES (PRIMARY)")
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(spacing.xs),
            verticalArrangement = Arrangement.spacedBy(spacing.xs),
        ) {
            PopChip(label = "Small", color = PopChipColor.Primary, size = PopChipSize.Small)
            PopChip(label = "Medium", color = PopChipColor.Primary, size = PopChipSize.Medium)
            PopChip(label = "Large", color = PopChipColor.Primary, size = PopChipSize.Large)
        }

        // Section: Status Pills (Small - replaces PopPill)
        SectionLabel("STATUS PILLS (SMALL - replaces PopPill)")
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(spacing.xs),
            verticalArrangement = Arrangement.spacedBy(spacing.xs),
        ) {
            PopChip(label = "Active", color = PopChipColor.Primary, size = PopChipSize.Small)
            PopChip(label = "Live", color = PopChipColor.Secondary, size = PopChipSize.Small)
            PopChip(label = "Synced", color = PopChipColor.Tertiary, size = PopChipSize.Small)
            PopChip(label = "Failed", color = PopChipColor.Error, size = PopChipSize.Small)
            PopChip(label = "Locked", color = PopChipColor.Neutral, size = PopChipSize.Small)
        }

        // Section: With Icons
        SectionLabel("WITH ICONS")
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(spacing.xs),
            verticalArrangement = Arrangement.spacedBy(spacing.xs),
        ) {
            PopChip(label = "Starred", color = PopChipColor.Primary, icon = PopIcons.Star)
            PopChip(label = "Favorites", color = PopChipColor.Secondary, icon = PopIcons.Heart)
            PopChip(label = "Trending", color = PopChipColor.Tertiary, icon = PopIcons.TrendUp)
        }

        // Section: Clickable (Filter)
        SectionLabel("CLICKABLE (FILTER)")
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(spacing.xs),
            verticalArrangement = Arrangement.spacedBy(spacing.xs),
        ) {
            PopChip(label = "All", color = PopChipColor.Primary, onClick = {})
            PopChip(label = "Active", color = PopChipColor.Secondary, onClick = {})
            PopChip(label = "Archived", color = PopChipColor.Tertiary, onClick = {})
        }

        // Section: Various Lengths
        SectionLabel("VARIOUS LENGTHS")
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(spacing.xs),
            verticalArrangement = Arrangement.spacedBy(spacing.xs),
        ) {
            PopChip(label = "AI", color = PopChipColor.Primary)
            PopChip(label = "Machine Learning", color = PopChipColor.Secondary)
            PopChip(label = "Natural Language Processing", color = PopChipColor.Tertiary)
        }

        // Section: Custom Colors
        SectionLabel("CUSTOM COLORS")
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

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}
