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
import com.electricpop.composite.PopMetricCard
import com.electricpop.composite.PopMetricCardStyle
import com.electricpop.foundation.PopBadgeDirection
import com.electricpop.foundation.PopDisplayTextDirection
import com.electricpop.foundation.PopDisplayTextSize
import com.electricpop.foundation.PopIconItem
import com.electricpop.foundation.PopIcons
import com.electricpop.theme.ElectricPopTheme

@Composable
fun PopMetricCardDemo() {
    val spacing = ElectricPopTheme.spacing
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(spacing.lg),
        verticalArrangement = Arrangement.spacedBy(spacing.lg),
    ) {
        // Section: Hero Metrics
        Text(
            "HERO METRICS",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Column(verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
            PopMetricCard(
                label = "Total Ecosystem Value",
                mainText = "\$42,069",
                fractionalText = ".42",
                badgeValue = "+12.4%",
                badgeDirection = PopBadgeDirection.Up,
                icons = listOf(
                    PopIconItem(PopIcons.Bolt, "Speed"),
                    PopIconItem(PopIcons.Sparkle, "Quality"),
                    PopIconItem(PopIcons.Layers, "Depth"),
                ),
                style = PopMetricCardStyle.Hero,
            )
            PopMetricCard(
                label = "Portfolio Balance",
                mainText = "\$128,500",
                fractionalText = ".00",
                style = PopMetricCardStyle.Hero,
            )
        }

        // Section: Surface Metrics
        Text(
            "SURFACE METRICS",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Column(verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
            PopMetricCard(
                label = "Monthly Revenue",
                mainText = "\$8,421",
                fractionalText = ".00",
                badgeValue = "+5.8%",
                badgeDirection = PopBadgeDirection.Up,
                displayDirection = PopDisplayTextDirection.Positive,
                style = PopMetricCardStyle.Surface,
            )
            PopMetricCard(
                label = "Operating Costs",
                mainText = "\$3,200",
                fractionalText = ".50",
                badgeValue = "-2.1%",
                badgeDirection = PopBadgeDirection.Down,
                displayDirection = PopDisplayTextDirection.Negative,
                style = PopMetricCardStyle.Surface,
            )
            PopMetricCard(
                label = "Total Assets",
                mainText = "\$52,000",
                badgeValue = "0.0%",
                badgeDirection = PopBadgeDirection.Neutral,
                style = PopMetricCardStyle.Surface,
            )
        }

        // Section: Display Sizes
        Text(
            "DISPLAY SIZES",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Column(verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
            PopMetricCard(
                label = "Large Display",
                mainText = "\$99,999",
                fractionalText = ".99",
                displaySize = PopDisplayTextSize.Large,
                style = PopMetricCardStyle.Surface,
            )
            PopMetricCard(
                label = "Medium Display",
                mainText = "\$99,999",
                fractionalText = ".99",
                displaySize = PopDisplayTextSize.Medium,
                style = PopMetricCardStyle.Surface,
            )
            PopMetricCard(
                label = "Small Display",
                mainText = "\$99,999",
                fractionalText = ".99",
                displaySize = PopDisplayTextSize.Small,
                style = PopMetricCardStyle.Surface,
            )
        }

        // Section: Minimal
        Text(
            "MINIMAL",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Column(verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
            PopMetricCard(
                label = "Active Users",
                mainText = "1,247",
                style = PopMetricCardStyle.Surface,
            )
            PopMetricCard(
                label = "Uptime",
                mainText = "99.9",
                fractionalText = "%",
                style = PopMetricCardStyle.Surface,
            )
        }
    }
}
