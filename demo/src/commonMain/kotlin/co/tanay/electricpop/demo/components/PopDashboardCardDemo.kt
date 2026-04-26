package co.tanay.electricpop.demo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import co.tanay.electricpop.composite.PopDashboardCard
import co.tanay.electricpop.composite.PopDashboardItem
import co.tanay.electricpop.composite.PopDashboardStatusPill
import co.tanay.electricpop.foundation.PopIcon
import co.tanay.electricpop.foundation.PopIcons
import co.tanay.electricpop.foundation.PopSectionHeader
import co.tanay.electricpop.theme.ElectricPopTheme
import co.tanay.electricpop.theme.PopShapeFull

@Composable
fun PopDashboardCardDemo() {
    val spacing = ElectricPopTheme.spacing

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(spacing.lg),
        verticalArrangement = Arrangement.spacedBy(spacing.lg),
    ) {
        // Section 1 — With watermark, status pill, action button
        PopSectionHeader(title = "WITH WATERMARK + STATUS + ACTION")
        PopDashboardCard(
            title = "Total Overview",
            titleValue = "14,200",
            items = listOf(
                PopDashboardItem("Category A", "8,400"),
                PopDashboardItem("Category B", "5,800"),
            ),
            backgroundIcon = PopIcons.Layers,
            statusContent = {
                PopDashboardStatusPill(label = "Active")
            },
            actionContent = {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(PopShapeFull)
                        .background(MaterialTheme.colorScheme.onTertiaryContainer),
                    contentAlignment = Alignment.Center,
                ) {
                    PopIcon(
                        imageVector = PopIcons.Add,
                        contentDescription = "Add",
                        tint = MaterialTheme.colorScheme.tertiaryContainer,
                    )
                }
            },
        )

        // Section 2 — Minimal (no watermark, no pill, no action)
        PopSectionHeader(title = "MINIMAL")
        PopDashboardCard(
            title = "Quick Summary",
            titleValue = "99.9%",
            items = listOf(
                PopDashboardItem("Uptime", "99.9%"),
                PopDashboardItem("Errors", "3"),
            ),
        )

        // Section 3 — Interactive with onClick
        PopSectionHeader(title = "INTERACTIVE")
        Text(
            text = "Hover or tap to see kinetic animation",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopDashboardCard(
            title = "Tap To Expand",
            titleValue = "2,048",
            items = listOf(
                PopDashboardItem("Active", "1,247"),
                PopDashboardItem("Pending", "801"),
            ),
            backgroundIcon = PopIcons.Puzzle,
            statusContent = {
                PopDashboardStatusPill(
                    label = "Live",
                    dotColor = MaterialTheme.colorScheme.tertiary,
                )
            },
            onClick = {},
        )

        Spacer(modifier = Modifier.height(spacing.xl))
    }
}
