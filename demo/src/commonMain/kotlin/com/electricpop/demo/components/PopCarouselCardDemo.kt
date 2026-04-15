package com.electricpop.demo.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.electricpop.composite.PopCarouselCard
import com.electricpop.composite.PopCarouselCardItem
import com.electricpop.composite.PopCarouselCardStrip
import com.electricpop.composite.PopCarouselCardStyle
import com.electricpop.foundation.PopIcons
import com.electricpop.theme.ElectricPopTheme

@Composable
fun PopCarouselCardDemo() {
    val spacing = ElectricPopTheme.spacing

    // Build sample items inside composable scope — icons are @Composable getters
    val primaryCard = PopCarouselCardItem(
        icon = PopIcons.Bolt,
        iconContentDescription = "Electric",
        timestamp = "08:42 AM",
        category = "Subscriptions",
        label = "Electric Grid Co.",
        value = "- \$142.00",
        style = PopCarouselCardStyle.Primary,
    )
    val secondaryCard = PopCarouselCardItem(
        icon = PopIcons.Heart,
        iconContentDescription = "Lifestyle",
        timestamp = "11:15 AM",
        category = "Lifestyle",
        label = "Neon Market",
        value = "- \$64.50",
        style = PopCarouselCardStyle.Secondary,
    )
    val tertiaryCard = PopCarouselCardItem(
        icon = PopIcons.Add,
        iconContentDescription = "Incoming",
        timestamp = "02:30 PM",
        category = "Incoming",
        label = "P2P Transfer",
        value = "+ \$1,200.00",
        style = PopCarouselCardStyle.Tertiary,
    )
    val surfaceCard = PopCarouselCardItem(
        icon = PopIcons.Star,
        iconContentDescription = "Dining",
        timestamp = "07:22 PM",
        category = "Dining",
        label = "Pixel Cafe",
        value = "- \$24.12",
        style = PopCarouselCardStyle.Surface,
    )
    val sampleItems = listOf(primaryCard, secondaryCard, tertiaryCard, surfaceCard)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = spacing.lg),
            verticalArrangement = Arrangement.spacedBy(spacing.sm),
        ) {
            Text(
                text = "CAROUSEL STRIP",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        Spacer(Modifier.height(spacing.xs))

        // Strip goes full-width (no horizontal padding on outer Column)
        PopCarouselCardStrip(items = sampleItems)

        Column(
            modifier = Modifier.padding(horizontal = spacing.lg),
            verticalArrangement = Arrangement.spacedBy(spacing.md),
        ) {
            Spacer(Modifier.height(spacing.lg))
            Text(
                text = "INDIVIDUAL STYLES",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            PopCarouselCard(
                item = primaryCard,
                modifier = Modifier.width(300.dp),
            )
            PopCarouselCard(
                item = secondaryCard,
                modifier = Modifier.width(300.dp),
            )
            PopCarouselCard(
                item = tertiaryCard,
                modifier = Modifier.width(300.dp),
            )
            PopCarouselCard(
                item = surfaceCard,
                modifier = Modifier.width(300.dp),
            )
            Spacer(Modifier.height(spacing.xl))
        }
    }
}
