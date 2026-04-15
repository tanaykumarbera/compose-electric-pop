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
import androidx.compose.ui.unit.dp
import com.electricpop.composite.PopFeatureCard
import com.electricpop.composite.PopFeatureCardStyle
import com.electricpop.foundation.PopBadgeDirection
import com.electricpop.foundation.PopDisplayTextDirection
import com.electricpop.foundation.PopDisplayTextSize
import com.electricpop.foundation.PopIconItem
import com.electricpop.foundation.PopIcons
import com.electricpop.theme.ElectricPopTheme

@Composable
fun PopFeatureCardDemo() {
    val spacing = ElectricPopTheme.spacing
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(spacing.lg),
        verticalArrangement = Arrangement.spacedBy(spacing.lg),
    ) {
        // Section: Hero Spotlight
        Text(
            "HERO SPOTLIGHT",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Column(verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
            PopFeatureCard(
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
                style = PopFeatureCardStyle.Hero,
            )
            PopFeatureCard(
                label = "Portfolio Balance",
                mainText = "\$128,500",
                fractionalText = ".00",
                style = PopFeatureCardStyle.Hero,
            )
            PopFeatureCard(
                label = "Network Health",
                mainText = "\$88,200",
                fractionalText = ".00",
                badgeValue = "+8.1%",
                badgeDirection = PopBadgeDirection.Up,
                icons = listOf(
                    PopIconItem(PopIcons.Bolt, "Speed"),
                    PopIconItem(PopIcons.Sparkle, "Quality"),
                    PopIconItem(PopIcons.Layers, "Depth"),
                    PopIconItem(PopIcons.Heart, "Health"),
                    PopIconItem(PopIcons.Star, "Rating"),
                    PopIconItem(PopIcons.CheckCircle, "Verified"),
                    PopIconItem(PopIcons.Home, "Home"),
                ),
                style = PopFeatureCardStyle.Hero,
            )
        }

        Spacer(Modifier.height(spacing.md))

        // Section: Surface Cards
        Text(
            "SURFACE CARDS",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Column(verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
            PopFeatureCard(
                label = "Monthly Revenue",
                mainText = "\$8,421",
                fractionalText = ".00",
                badgeValue = "+5.8%",
                badgeDirection = PopBadgeDirection.Up,
                displayDirection = PopDisplayTextDirection.Positive,
                style = PopFeatureCardStyle.Surface,
            )
            PopFeatureCard(
                label = "Operating Costs",
                mainText = "\$3,200",
                fractionalText = ".50",
                badgeValue = "-2.1%",
                badgeDirection = PopBadgeDirection.Down,
                displayDirection = PopDisplayTextDirection.Negative,
                style = PopFeatureCardStyle.Surface,
            )
            PopFeatureCard(
                label = "Total Assets",
                mainText = "\$52,000",
                badgeValue = "0.0%",
                badgeDirection = PopBadgeDirection.Neutral,
                style = PopFeatureCardStyle.Surface,
            )
        }

        Spacer(Modifier.height(spacing.md))

        // Section: Display Sizes
        Text(
            "DISPLAY SIZES",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Column(verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
            PopFeatureCard(
                label = "Large Display",
                mainText = "\$99,999",
                fractionalText = ".99",
                displaySize = PopDisplayTextSize.Large,
                style = PopFeatureCardStyle.Surface,
            )
            PopFeatureCard(
                label = "Medium Display",
                mainText = "\$99,999",
                fractionalText = ".99",
                displaySize = PopDisplayTextSize.Medium,
                style = PopFeatureCardStyle.Surface,
            )
            PopFeatureCard(
                label = "Small Display",
                mainText = "\$99,999",
                fractionalText = ".99",
                displaySize = PopDisplayTextSize.Small,
                style = PopFeatureCardStyle.Surface,
            )
        }

        Spacer(Modifier.height(spacing.md))

        // Section: Minimal
        Text(
            "MINIMAL",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Column(verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
            PopFeatureCard(
                label = "Active Users",
                mainText = "1,247",
                style = PopFeatureCardStyle.Surface,
            )
            PopFeatureCard(
                label = "Uptime",
                mainText = "99.9",
                fractionalText = "%",
                style = PopFeatureCardStyle.Surface,
            )
        }
    }
}
