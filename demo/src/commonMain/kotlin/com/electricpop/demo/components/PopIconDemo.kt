package com.electricpop.demo.components

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.electricpop.foundation.PopIcon
import com.electricpop.foundation.PopIconSize
import com.electricpop.foundation.PopIcons
import com.electricpop.theme.ElectricPopTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PopIconDemo() {
    val spacing = ElectricPopTheme.spacing
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(spacing.lg),
        verticalArrangement = Arrangement.spacedBy(spacing.lg),
    ) {
        // Section: Sizes
        Text(
            text = "SIZES",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(spacing.lg),
            verticalArrangement = Arrangement.spacedBy(spacing.sm),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(spacing.xxs),
            ) {
                PopIcon(imageVector = PopIcons.Star, contentDescription = null, size = PopIconSize.Small)
                Text(text = "Small", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(spacing.xxs),
            ) {
                PopIcon(imageVector = PopIcons.Star, contentDescription = null, size = PopIconSize.Medium)
                Text(text = "Medium", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(spacing.xxs),
            ) {
                PopIcon(imageVector = PopIcons.Star, contentDescription = null, size = PopIconSize.Large)
                Text(text = "Large", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        // Section: Default Tint
        Text(
            text = "DEFAULT TINT",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(spacing.sm),
            verticalArrangement = Arrangement.spacedBy(spacing.sm),
        ) {
            PopIcon(imageVector = PopIcons.Star, contentDescription = "Star")
            PopIcon(imageVector = PopIcons.Heart, contentDescription = "Heart")
            PopIcon(imageVector = PopIcons.Home, contentDescription = "Home")
            PopIcon(imageVector = PopIcons.Search, contentDescription = "Search")
            PopIcon(imageVector = PopIcons.Settings, contentDescription = "Settings")
            PopIcon(imageVector = PopIcons.Add, contentDescription = "Add")
            PopIcon(imageVector = PopIcons.Close, contentDescription = "Close")
            PopIcon(imageVector = PopIcons.Check, contentDescription = "Check")
            PopIcon(imageVector = PopIcons.Person, contentDescription = "Person")
            PopIcon(imageVector = PopIcons.Info, contentDescription = "Info")
        }

        // Section: Theme Color Tints
        Text(
            text = "THEME COLOR TINTS",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(spacing.lg),
            verticalArrangement = Arrangement.spacedBy(spacing.sm),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(spacing.xxs),
            ) {
                PopIcon(imageVector = PopIcons.Star, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Text(text = "primary", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(spacing.xxs),
            ) {
                PopIcon(imageVector = PopIcons.Star, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                Text(text = "secondary", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(spacing.xxs),
            ) {
                PopIcon(imageVector = PopIcons.Star, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary)
                Text(text = "tertiary", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(spacing.xxs),
            ) {
                PopIcon(imageVector = PopIcons.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                Text(text = "error", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(spacing.xxs),
            ) {
                PopIcon(imageVector = PopIcons.Info, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(text = "onSurfaceVariant", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        // Section: Trend Icons
        Text(
            text = "TREND ICONS",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(spacing.lg),
            verticalArrangement = Arrangement.spacedBy(spacing.sm),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(spacing.xxs),
            ) {
                PopIcon(imageVector = PopIcons.TrendUp, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Text(text = "TrendUp", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(spacing.xxs),
            ) {
                PopIcon(imageVector = PopIcons.TrendDown, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                Text(text = "TrendDown", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        // Section: New Icons (from Stitch designs)
        Text(
            text = "STITCH DESIGN ICONS",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(spacing.lg),
            verticalArrangement = Arrangement.spacedBy(spacing.md),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(spacing.xxs),
            ) {
                PopIcon(imageVector = PopIcons.Bolt, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Text(text = "Bolt", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(spacing.xxs),
            ) {
                PopIcon(imageVector = PopIcons.Sparkle, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Text(text = "Sparkle", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(spacing.xxs),
            ) {
                PopIcon(imageVector = PopIcons.ArrowForward, contentDescription = null)
                Text(text = "ArrowForward", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(spacing.xxs),
            ) {
                PopIcon(imageVector = PopIcons.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Text(text = "CheckCircle", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(spacing.xxs),
            ) {
                PopIcon(imageVector = PopIcons.Layers, contentDescription = null)
                Text(text = "Layers", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(spacing.xxs),
            ) {
                PopIcon(imageVector = PopIcons.Puzzle, contentDescription = null)
                Text(text = "Puzzle", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(spacing.xxs),
            ) {
                PopIcon(imageVector = PopIcons.Tokens, contentDescription = null)
                Text(text = "Tokens", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(spacing.xxs),
            ) {
                PopIcon(imageVector = PopIcons.Menu, contentDescription = null)
                Text(text = "Menu", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
