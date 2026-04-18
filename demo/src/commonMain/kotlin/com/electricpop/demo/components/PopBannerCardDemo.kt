package com.electricpop.demo.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.electricpop.composite.PopBannerCard
import com.electricpop.composite.PopBannerCardTrend
import com.electricpop.composite.PopBannerCardTrendDirection
import com.electricpop.composite.PopBannerCardVital
import com.electricpop.foundation.PopIcons
import com.electricpop.theme.ElectricPopTheme

@Composable
fun PopBannerCardDemo() {
    val spacing = ElectricPopTheme.spacing

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
            .padding(spacing.lg),
        verticalArrangement = Arrangement.spacedBy(spacing.lg),
    ) {
        // Section 1: Full variant
        SectionLabel("TOTAL VALUE")
        PopBannerCard(
            label = "Total Ecosystem Value",
            value = "\$42,069",
            fractionalValue = ".42",
            trend = PopBannerCardTrend("+12.4%", PopBannerCardTrendDirection.Up),
            vitals = listOf(
                PopBannerCardVital(PopIcons.Bolt, "Bolt"),
                PopBannerCardVital(PopIcons.Sparkle, "Sparkle"),
                PopBannerCardVital(PopIcons.CheckCircle, "Check Circle"),
            ),
        )

        // Section 2: Negative trend
        SectionLabel("MONTHLY BURN (NEGATIVE TREND)")
        PopBannerCard(
            label = "Monthly Burn",
            value = "\$8,420",
            fractionalValue = ".50",
            trend = PopBannerCardTrend("-3.8%", PopBannerCardTrendDirection.Down),
            vitals = listOf(
                PopBannerCardVital(PopIcons.TrendDown, "Trend Down"),
                PopBannerCardVital(PopIcons.Warning, "Warning"),
            ),
        )

        // Section 3: Secondary color, neutral trend
        SectionLabel("SECONDARY COLOR")
        PopBannerCard(
            label = "Active Nodes",
            value = "1,402",
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            trend = PopBannerCardTrend("STABLE", PopBannerCardTrendDirection.Neutral),
        )

        // Section 4: Tertiary color with vitals
        SectionLabel("TERTIARY COLOR")
        PopBannerCard(
            label = "Response Time",
            value = "142ms",
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
            vitals = listOf(
                PopBannerCardVital(PopIcons.Bolt, "Bolt"),
                PopBannerCardVital(PopIcons.Sparkle, "Sparkle"),
            ),
        )

        // Section 5: Minimal
        SectionLabel("MINIMAL")
        PopBannerCard(
            label = "Temperature",
            value = "72°F",
        )

        // Section 6: Clickable (kinetic)
        SectionLabel("CLICKABLE (KINETIC)")
        PopBannerCard(
            label = "Total Ecosystem Value",
            value = "\$42,069",
            fractionalValue = ".42",
            trend = PopBannerCardTrend("+12.4%", PopBannerCardTrendDirection.Up),
            vitals = listOf(
                PopBannerCardVital(PopIcons.Bolt, "Bolt"),
                PopBannerCardVital(PopIcons.Sparkle, "Sparkle"),
                PopBannerCardVital(PopIcons.CheckCircle, "Check Circle"),
            ),
            onClick = {},
        )
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
