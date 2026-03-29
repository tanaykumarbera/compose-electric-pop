package com.electricpop.demo.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.electricpop.foundation.PopBadge
import com.electricpop.foundation.PopBadgeDirection
import com.electricpop.theme.ElectricPopTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PopBadgeDemo() {
    val spacing = ElectricPopTheme.spacing
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(spacing.lg),
        verticalArrangement = Arrangement.spacedBy(spacing.lg),
    ) {
        // Section: Directional Variants
        Text(
            text = "DIRECTIONAL VARIANTS",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(spacing.xs),
            verticalArrangement = Arrangement.spacedBy(spacing.xs),
        ) {
            PopBadge(value = "+12.5%", direction = PopBadgeDirection.Up)
            PopBadge(value = "-3.2%", direction = PopBadgeDirection.Down)
            PopBadge(value = "0.0%", direction = PopBadgeDirection.Neutral)
        }

        // Section: Various Values
        Text(
            text = "VARIOUS VALUES",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(spacing.xs),
            verticalArrangement = Arrangement.spacedBy(spacing.xs),
        ) {
            PopBadge(value = "+2%", direction = PopBadgeDirection.Up)
            PopBadge(value = "+0.1%", direction = PopBadgeDirection.Up)
            PopBadge(value = "-15.3%", direction = PopBadgeDirection.Down)
            PopBadge(value = "-100%", direction = PopBadgeDirection.Down)
            PopBadge(value = "N/A", direction = PopBadgeDirection.Neutral)
        }

        // Section: Inline Context
        Text(
            text = "INLINE CONTEXT",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacing.xs),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Revenue",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            PopBadge(value = "+12.5%", direction = PopBadgeDirection.Up)
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacing.xs),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Churn Rate",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            PopBadge(value = "-3.2%", direction = PopBadgeDirection.Down)
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacing.xs),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Conversion",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            PopBadge(value = "0.0%", direction = PopBadgeDirection.Neutral)
        }
    }
}
