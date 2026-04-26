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
import co.tanay.electricpop.foundation.PopIconItem
import co.tanay.electricpop.foundation.PopIconRow
import co.tanay.electricpop.foundation.PopIconSize
import co.tanay.electricpop.foundation.PopIcons
import co.tanay.electricpop.theme.ElectricPopTheme

@Composable
fun PopIconRowDemo() {
    val spacing = ElectricPopTheme.spacing
    val cs = MaterialTheme.colorScheme
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(spacing.lg),
        verticalArrangement = Arrangement.spacedBy(spacing.lg),
    ) {
        // Section: Single icon
        Text(
            text = "SINGLE ICON",
            style = MaterialTheme.typography.labelLarge,
            color = cs.onSurfaceVariant,
        )
        PopIconRow(icons = listOf(PopIconItem(PopIcons.Star, "Star")))

        // Section: 3 icons Medium
        Text(
            text = "3 ICONS (MEDIUM)",
            style = MaterialTheme.typography.labelLarge,
            color = cs.onSurfaceVariant,
        )
        PopIconRow(
            icons = listOf(
                PopIconItem(PopIcons.Star, "Star"),
                PopIconItem(PopIcons.Heart, "Heart"),
                PopIconItem(PopIcons.Bolt, "Bolt"),
            ),
        )

        // Section: 5 icons Medium
        Text(
            text = "5 ICONS (MEDIUM)",
            style = MaterialTheme.typography.labelLarge,
            color = cs.onSurfaceVariant,
        )
        PopIconRow(
            icons = listOf(
                PopIconItem(PopIcons.Star),
                PopIconItem(PopIcons.Heart),
                PopIconItem(PopIcons.Bolt),
                PopIconItem(PopIcons.Sparkle),
                PopIconItem(PopIcons.CheckCircle),
            ),
        )

        // Section: Small size
        Text(
            text = "SMALL SIZE",
            style = MaterialTheme.typography.labelLarge,
            color = cs.onSurfaceVariant,
        )
        PopIconRow(
            icons = listOf(
                PopIconItem(PopIcons.Star),
                PopIconItem(PopIcons.Heart),
                PopIconItem(PopIcons.Bolt),
            ),
            iconSize = PopIconSize.Small,
        )

        // Section: Medium size
        Text(
            text = "MEDIUM SIZE",
            style = MaterialTheme.typography.labelLarge,
            color = cs.onSurfaceVariant,
        )
        PopIconRow(
            icons = listOf(
                PopIconItem(PopIcons.Star),
                PopIconItem(PopIcons.Heart),
                PopIconItem(PopIcons.Bolt),
            ),
            iconSize = PopIconSize.Medium,
        )

        // Section: Large size
        Text(
            text = "LARGE SIZE",
            style = MaterialTheme.typography.labelLarge,
            color = cs.onSurfaceVariant,
        )
        PopIconRow(
            icons = listOf(
                PopIconItem(PopIcons.Star),
                PopIconItem(PopIcons.Heart),
                PopIconItem(PopIcons.Bolt),
            ),
            iconSize = PopIconSize.Large,
        )

        // Section: Primary tint
        Text(
            text = "PRIMARY TINT",
            style = MaterialTheme.typography.labelLarge,
            color = cs.onSurfaceVariant,
        )
        PopIconRow(
            icons = listOf(
                PopIconItem(PopIcons.Star),
                PopIconItem(PopIcons.Heart),
                PopIconItem(PopIcons.Bolt),
            ),
            tint = cs.primary,
        )

        // Section: Secondary tint
        Text(
            text = "SECONDARY TINT",
            style = MaterialTheme.typography.labelLarge,
            color = cs.onSurfaceVariant,
        )
        PopIconRow(
            icons = listOf(
                PopIconItem(PopIcons.Star),
                PopIconItem(PopIcons.Heart),
                PopIconItem(PopIcons.Bolt),
            ),
            tint = cs.secondary,
        )

        // Section: Tertiary tint
        Text(
            text = "TERTIARY TINT",
            style = MaterialTheme.typography.labelLarge,
            color = cs.onSurfaceVariant,
        )
        PopIconRow(
            icons = listOf(
                PopIconItem(PopIcons.Star),
                PopIconItem(PopIcons.Heart),
                PopIconItem(PopIcons.Bolt),
            ),
            tint = cs.tertiary,
        )

        // Section: Custom spacing
        Text(
            text = "CUSTOM SPACING (LG = 24dp)",
            style = MaterialTheme.typography.labelLarge,
            color = cs.onSurfaceVariant,
        )
        PopIconRow(
            icons = listOf(
                PopIconItem(PopIcons.Star),
                PopIconItem(PopIcons.Heart),
                PopIconItem(PopIcons.Bolt),
            ),
            spacing = spacing.lg,
        )
    }
}
