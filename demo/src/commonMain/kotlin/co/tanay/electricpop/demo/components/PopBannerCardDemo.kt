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
import co.tanay.electricpop.composite.PopBannerCard
import co.tanay.electricpop.composite.PopBannerCardStyle
import co.tanay.electricpop.foundation.PopBadgeDirection
import co.tanay.electricpop.foundation.PopDisplayTextDirection
import co.tanay.electricpop.foundation.PopDisplayTextSize
import co.tanay.electricpop.foundation.PopIconItem
import co.tanay.electricpop.foundation.PopIcons
import co.tanay.electricpop.theme.ElectricPopTheme

@Composable
fun PopBannerCardDemo() {
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
            PopBannerCard(
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
                style = PopBannerCardStyle.Hero,
            )
            PopBannerCard(
                label = "Portfolio Balance",
                mainText = "\$128,500",
                fractionalText = ".00",
                style = PopBannerCardStyle.Hero,
            )
            PopBannerCard(
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
                style = PopBannerCardStyle.Hero,
            )
        }

        // Section: Surface Metrics
        Text(
            "SURFACE METRICS",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Column(verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
            PopBannerCard(
                label = "Monthly Revenue",
                mainText = "\$8,421",
                fractionalText = ".00",
                badgeValue = "+5.8%",
                badgeDirection = PopBadgeDirection.Up,
                displayDirection = PopDisplayTextDirection.Positive,
                style = PopBannerCardStyle.Surface,
            )
            PopBannerCard(
                label = "Operating Costs",
                mainText = "\$3,200",
                fractionalText = ".50",
                badgeValue = "-2.1%",
                badgeDirection = PopBadgeDirection.Down,
                displayDirection = PopDisplayTextDirection.Negative,
                style = PopBannerCardStyle.Surface,
            )
            PopBannerCard(
                label = "Total Assets",
                mainText = "\$52,000",
                badgeValue = "0.0%",
                badgeDirection = PopBadgeDirection.Neutral,
                style = PopBannerCardStyle.Surface,
            )
        }

        // Section: Display Sizes
        Text(
            "DISPLAY SIZES",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Column(verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
            PopBannerCard(
                label = "Large Display",
                mainText = "\$99,999",
                fractionalText = ".99",
                displaySize = PopDisplayTextSize.Large,
                style = PopBannerCardStyle.Surface,
            )
            PopBannerCard(
                label = "Medium Display",
                mainText = "\$99,999",
                fractionalText = ".99",
                displaySize = PopDisplayTextSize.Medium,
                style = PopBannerCardStyle.Surface,
            )
            PopBannerCard(
                label = "Small Display",
                mainText = "\$99,999",
                fractionalText = ".99",
                displaySize = PopDisplayTextSize.Small,
                style = PopBannerCardStyle.Surface,
            )
        }

        // Section: Minimal
        Text(
            "MINIMAL",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Column(verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
            PopBannerCard(
                label = "Active Users",
                mainText = "1,247",
                style = PopBannerCardStyle.Surface,
            )
            PopBannerCard(
                label = "Uptime",
                mainText = "99.9",
                fractionalText = "%",
                style = PopBannerCardStyle.Surface,
            )
        }
    }
}
