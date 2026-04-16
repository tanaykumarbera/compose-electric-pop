package com.electricpop.demo.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.electricpop.composite.PopDashboardCard
import com.electricpop.composite.PopDashboardCardCompact
import com.electricpop.composite.PopDashboardCardRow
import com.electricpop.foundation.PopDisplayTextDirection
import com.electricpop.foundation.PopIcons
import com.electricpop.foundation.PopPill
import com.electricpop.foundation.PopPillColor
import com.electricpop.foundation.PopSectionHeader
import com.electricpop.theme.ElectricPopTheme

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
        // Section 1: Default Dashboard Card
        PopSectionHeader(title = "DEFAULT")
        PopDashboardCard(
            title = "Total Locked Wealth",
            titleValue = "$42,890.00",
            rows = listOf(
                PopDashboardCardRow(
                    icon = PopIcons.Layers,
                    label = "Vault A",
                    value = "$24,000",
                ),
                PopDashboardCardRow(
                    icon = PopIcons.Star,
                    label = "Emergency",
                    value = "$18,890",
                    direction = PopDisplayTextDirection.Positive,
                ),
                PopDashboardCardRow(
                    icon = PopIcons.Heart,
                    label = "Savings",
                    value = "$12,400",
                ),
            ),
            statusContent = {
                val onTertiary = MaterialTheme.colorScheme.onTertiaryContainer
                PopPill(
                    label = "Locked",
                    containerColor = Color.White.copy(alpha = 0.2f),
                    contentColor = onTertiary,
                )
                PopPill(label = "Active", color = PopPillColor.Tertiary)
            },
        )

        // Section 2: Portfolio Data
        PopSectionHeader(title = "PORTFOLIO")
        PopDashboardCard(
            title = "Portfolio",
            titleValue = "$34,500.00",
            rows = listOf(
                PopDashboardCardRow(
                    icon = PopIcons.Home,
                    label = "Checking",
                    value = "$8,200",
                    direction = PopDisplayTextDirection.Neutral,
                ),
                PopDashboardCardRow(
                    icon = PopIcons.TrendUp,
                    label = "Investments",
                    value = "$34,500",
                    direction = PopDisplayTextDirection.Positive,
                ),
                PopDashboardCardRow(
                    icon = PopIcons.Warning,
                    label = "Credit",
                    value = "-$2,100",
                    direction = PopDisplayTextDirection.Negative,
                ),
            ),
            statusContent = {
                PopPill(label = "Q1 2026", color = PopPillColor.Neutral)
            },
        )

        // Section 3: Compact Card
        PopSectionHeader(title = "COMPACT")
        PopDashboardCardCompact(
            rows = listOf(
                PopDashboardCardRow(
                    icon = PopIcons.Bolt,
                    label = "Electric Grid",
                    value = "-$142.00",
                    direction = PopDisplayTextDirection.Negative,
                ),
                PopDashboardCardRow(
                    icon = PopIcons.Sparkle,
                    label = "Streaming",
                    value = "-$15.99",
                ),
                PopDashboardCardRow(
                    icon = PopIcons.Heart,
                    label = "Gym",
                    value = "-$49.00",
                ),
            ),
            statusContent = {
                PopPill(label = "Subscriptions", color = PopPillColor.Tertiary)
            },
        )

        // Section 4: No Pills
        PopSectionHeader(title = "NO PILLS")
        PopDashboardCard(
            title = "Quick Glance",
            rows = listOf(
                PopDashboardCardRow(
                    icon = PopIcons.Person,
                    label = "Users",
                    value = "1,247",
                ),
                PopDashboardCardRow(
                    icon = PopIcons.CheckCircle,
                    label = "Uptime",
                    value = "99.9%",
                    direction = PopDisplayTextDirection.Positive,
                ),
            ),
        )

        // Section 5: Interactive
        PopSectionHeader(title = "INTERACTIVE")
        Text(
            text = "Tap / hover to see kinetic animation",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopDashboardCard(
            title = "Tap Me",
            rows = listOf(
                PopDashboardCardRow(
                    icon = PopIcons.Person,
                    label = "Users",
                    value = "1,247",
                ),
                PopDashboardCardRow(
                    icon = PopIcons.CheckCircle,
                    label = "Uptime",
                    value = "99.9%",
                    direction = PopDisplayTextDirection.Positive,
                ),
            ),
            onClick = {},
        )

        Spacer(modifier = Modifier.height(spacing.xl))
    }
}
