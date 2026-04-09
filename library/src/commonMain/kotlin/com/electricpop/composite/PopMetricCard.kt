package com.electricpop.composite

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import com.electricpop.foundation.PopBadge
import com.electricpop.foundation.PopBadgeDirection
import com.electricpop.foundation.PopDisplayText
import com.electricpop.foundation.PopDisplayTextDirection
import com.electricpop.foundation.PopDisplayTextSize
import com.electricpop.foundation.PopIcon
import com.electricpop.foundation.PopIconItem
import com.electricpop.foundation.PopIconSize
import com.electricpop.foundation.PopSurface
import com.electricpop.foundation.PopSurfaceTone
import com.electricpop.theme.ElectricPopTheme
import com.electricpop.theme.PopShapeFull

/**
 * Visual style variant for [PopMetricCard].
 */
enum class PopMetricCardStyle {
    /** Neutral surface background (surfaceContainer). For secondary/supporting metrics. */
    Surface,
    /** Primary container background (primaryContainer). For the hero/primary metric. */
    Hero,
}

/**
 * A card that displays a labeled metric value with optional fractional text, directional badge,
 * and Hero or Surface styling.
 *
 * Follows the No-Line Rule: tonal surface shifts separate content areas — no dividers.
 * Ghost border is enabled for Surface style to provide accessibility separation.
 * Kinetic hover/press animation is enabled when [onClick] is non-null.
 *
 * @param label Context label shown uppercase above the main value.
 * @param mainText The primary metric value (e.g., "$42,069"). Rendered uppercase via [PopDisplayText].
 * @param modifier Optional [Modifier] for the outer container.
 * @param fractionalText Optional fractional/suffix text (e.g., ".42", "%").
 * @param badgeValue If non-null, a trend badge is shown below the display value (e.g., "+12.4%").
 * @param badgeDirection Semantic direction for the badge arrow and color.
 * @param displayDirection Semantic direction for the [PopDisplayText] color.
 * @param displaySize Size variant for the main display value.
 * @param icons Optional list of [PopIconItem] rendered as Slot C (vital indicators) below the badge.
 *   On Hero style these are tinted with [onPrimaryContainer]; on Surface style with [onSurfaceVariant].
 * @param style [PopMetricCardStyle.Hero] uses primaryContainer background; [PopMetricCardStyle.Surface] uses default surface tone.
 * @param onClick Optional click handler. Enables kinetic hover/press scale animation when set.
 */
@Composable
fun PopMetricCard(
    label: String,
    mainText: String,
    modifier: Modifier = Modifier,
    fractionalText: String? = null,
    badgeValue: String? = null,
    badgeDirection: PopBadgeDirection = PopBadgeDirection.Neutral,
    displayDirection: PopDisplayTextDirection = PopDisplayTextDirection.Neutral,
    displaySize: PopDisplayTextSize = PopDisplayTextSize.Large,
    icons: List<PopIconItem> = emptyList(),
    style: PopMetricCardStyle = PopMetricCardStyle.Surface,
    onClick: (() -> Unit)? = null,
) {
    val spacing = ElectricPopTheme.spacing
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()

    val targetScale = when {
        onClick == null -> 1f
        isPressed -> 0.97f
        isHovered -> 1.03f
        else -> 1f
    }
    val scale by animateFloatAsState(
        targetValue = targetScale,
        animationSpec = tween(durationMillis = 200),
    )

    val isHero = style == PopMetricCardStyle.Hero

    Box(
        modifier = modifier
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = onClick,
                    )
                } else {
                    Modifier
                }
            ),
    ) {
        PopSurface(
            shape = MaterialTheme.shapes.large,
            tone = PopSurfaceTone.Default,
            containerColor = if (isHero) MaterialTheme.colorScheme.primaryContainer else Color.Unspecified,
            ghostBorder = !isHero,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(spacing.lg),
                verticalArrangement = Arrangement.spacedBy(spacing.xs),
            ) {
                // 1. Context label
                Text(
                    text = label.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isHero) {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                )

                // 2. Display value
                PopDisplayText(
                    mainText = mainText,
                    fractionalText = fractionalText,
                    direction = displayDirection,
                    size = displaySize,
                    color = if (isHero) MaterialTheme.colorScheme.onPrimaryContainer else null,
                )

                // 3. Badge — on Hero, invert colors so it contrasts against primaryContainer bg
                if (badgeValue != null) {
                    Spacer(Modifier.height(spacing.xxs))
                    if (isHero) {
                        // Custom inverted pill: onPrimaryContainer bg, primaryContainer text
                        Row(
                            modifier = Modifier
                                .clip(PopShapeFull)
                                .background(MaterialTheme.colorScheme.onPrimaryContainer)
                                .padding(horizontal = spacing.sm, vertical = spacing.xxs),
                            horizontalArrangement = Arrangement.spacedBy(spacing.xxs),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            when (badgeDirection) {
                                PopBadgeDirection.Up -> PopIcon(
                                    imageVector = com.electricpop.foundation.PopIcons.TrendUp,
                                    contentDescription = "Trending up",
                                    size = PopIconSize.Small,
                                    tint = MaterialTheme.colorScheme.primaryContainer,
                                )
                                PopBadgeDirection.Down -> PopIcon(
                                    imageVector = com.electricpop.foundation.PopIcons.TrendDown,
                                    contentDescription = "Trending down",
                                    size = PopIconSize.Small,
                                    tint = MaterialTheme.colorScheme.primaryContainer,
                                )
                                PopBadgeDirection.Neutral -> {}
                            }
                            Text(
                                text = badgeValue.uppercase(),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primaryContainer,
                            )
                        }
                    } else {
                        PopBadge(value = badgeValue, direction = badgeDirection)
                    }
                }

                // 4. Icon row — Slot C: vital indicators (optional)
                // Each icon sits in a small circular container so it's visible against the card bg
                if (icons.isNotEmpty()) {
                    Spacer(Modifier.height(spacing.xxs))
                    // Hero: solid dark circles (onPrimaryContainer bg) with lime icons
                    // Surface: tonal container with onSurfaceVariant icons
                    val iconContainerColor = if (isHero) {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    } else {
                        MaterialTheme.colorScheme.surfaceContainerHigh
                    }
                    val iconTint = if (isHero) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(spacing.xs)) {
                        icons.forEach { item ->
                            Box(
                                modifier = Modifier
                                    .clip(PopShapeFull)
                                    .background(iconContainerColor)
                                    .padding(spacing.sm),
                                contentAlignment = Alignment.Center,
                            ) {
                                PopIcon(
                                    imageVector = item.imageVector,
                                    contentDescription = item.contentDescription,
                                    size = PopIconSize.Medium,
                                    tint = iconTint,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
