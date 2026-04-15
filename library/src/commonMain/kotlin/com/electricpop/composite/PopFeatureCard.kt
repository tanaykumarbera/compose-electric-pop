package com.electricpop.composite

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.unit.dp
import com.electricpop.foundation.PopBadgeDirection
import com.electricpop.foundation.toColors
import com.electricpop.foundation.PopDisplayText
import com.electricpop.foundation.PopDisplayTextDirection
import com.electricpop.foundation.PopDisplayTextSize
import com.electricpop.foundation.PopIcon
import com.electricpop.foundation.PopIconItem
import com.electricpop.foundation.PopIconSize
import com.electricpop.foundation.PopIcons
import com.electricpop.foundation.PopSurface
import com.electricpop.foundation.PopSurfaceTone
import com.electricpop.theme.ElectricPopTheme
import com.electricpop.theme.PopShapeFull

/**
 * Visual style variant for [PopFeatureCard].
 */
enum class PopFeatureCardStyle {
    /** Primary container background (primaryContainer). The default hero spotlight. */
    Hero,
    /** Surface container background. For secondary/supporting feature cards. */
    Surface,
}

/**
 * Primary spotlight card. Large metric, trend indicator, supporting icon cluster
 * on a `primaryContainer` background (Hero) or `surfaceContainer` background (Surface).
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
 * @param icons Optional list of [PopIconItem] rendered as overlapping coin-stack icons below the badge.
 * @param style [PopFeatureCardStyle.Hero] uses primaryContainer background; [PopFeatureCardStyle.Surface] uses default surface tone.
 * @param onClick Optional click handler. Enables kinetic hover/press scale animation when set.
 */
@Composable
fun PopFeatureCard(
    label: String,
    mainText: String,
    modifier: Modifier = Modifier,
    fractionalText: String? = null,
    badgeValue: String? = null,
    badgeDirection: PopBadgeDirection = PopBadgeDirection.Neutral,
    displayDirection: PopDisplayTextDirection = PopDisplayTextDirection.Neutral,
    displaySize: PopDisplayTextSize = PopDisplayTextSize.Large,
    icons: List<PopIconItem> = emptyList(),
    style: PopFeatureCardStyle = PopFeatureCardStyle.Hero,
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

    val isHero = style == PopFeatureCardStyle.Hero

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
            shape = MaterialTheme.shapes.extraLarge,
            tone = PopSurfaceTone.Default,
            containerColor = if (isHero) MaterialTheme.colorScheme.primaryContainer else Color.Unspecified,
            ghostBorder = !isHero,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(spacing.xl),
                verticalArrangement = Arrangement.spacedBy(spacing.xs),
            ) {
                // Slot A: Context label (uppercase)
                Text(
                    text = label.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isHero) {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                )

                // Slot B: Large metric display
                PopDisplayText(
                    mainText = mainText,
                    fractionalText = fractionalText,
                    direction = displayDirection,
                    size = displaySize,
                    color = if (isHero) MaterialTheme.colorScheme.onPrimaryContainer else null,
                )

                // Slot C: Badge pill
                if (badgeValue != null) {
                    Spacer(Modifier.height(spacing.xxs))
                    if (isHero) {
                        // Hero style: translucent onPrimaryContainer background
                        val badgeBackground = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.15f)
                        val badgeContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        Row(
                            modifier = Modifier
                                .clip(PopShapeFull)
                                .background(badgeBackground)
                                .padding(horizontal = spacing.md, vertical = spacing.xs),
                            horizontalArrangement = Arrangement.spacedBy(spacing.xs),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            when (badgeDirection) {
                                PopBadgeDirection.Up -> PopIcon(
                                    imageVector = PopIcons.TrendUp,
                                    contentDescription = "Trending up",
                                    size = PopIconSize.Medium,
                                    tint = badgeContentColor,
                                )
                                PopBadgeDirection.Down -> PopIcon(
                                    imageVector = PopIcons.TrendDown,
                                    contentDescription = "Trending down",
                                    size = PopIconSize.Medium,
                                    tint = badgeContentColor,
                                )
                                PopBadgeDirection.Neutral -> {}
                            }
                            Text(
                                text = badgeValue.uppercase(),
                                style = MaterialTheme.typography.labelLarge,
                                color = badgeContentColor,
                            )
                        }
                    } else {
                        // Surface style: semantic badge colors
                        val badgeColors = badgeDirection.toColors()
                        Row(
                            modifier = Modifier
                                .clip(PopShapeFull)
                                .background(badgeColors.containerColor)
                                .padding(horizontal = spacing.md, vertical = spacing.xs),
                            horizontalArrangement = Arrangement.spacedBy(spacing.xs),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            when (badgeDirection) {
                                PopBadgeDirection.Up -> PopIcon(
                                    imageVector = PopIcons.TrendUp,
                                    contentDescription = "Trending up",
                                    size = PopIconSize.Medium,
                                    tint = badgeColors.contentColor,
                                )
                                PopBadgeDirection.Down -> PopIcon(
                                    imageVector = PopIcons.TrendDown,
                                    contentDescription = "Trending down",
                                    size = PopIconSize.Medium,
                                    tint = badgeColors.contentColor,
                                )
                                PopBadgeDirection.Neutral -> {}
                            }
                            Text(
                                text = badgeValue.uppercase(),
                                style = MaterialTheme.typography.labelLarge,
                                color = badgeColors.contentColor,
                            )
                        }
                    }
                }

                // Slot D: Overlapping coin-stack icons
                if (icons.isNotEmpty()) {
                    Spacer(Modifier.height(spacing.xs))
                    val maxVisible = 5
                    val visibleIcons = icons.take(maxVisible)
                    val overflowCount = icons.size - maxVisible
                    val coinSize = 40.dp
                    val overlapOffset = 12.dp
                    val iconBgColor = if (isHero) {
                        MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.15f)
                    } else {
                        MaterialTheme.colorScheme.surfaceContainerHigh
                    }
                    val iconBorderColor = if (isHero) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.surfaceContainer
                    }
                    val iconTint = if (isHero) {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                    val totalItems = visibleIcons.size + if (overflowCount > 0) 1 else 0
                    Box(
                        modifier = Modifier
                            .size(
                                width = coinSize * totalItems - overlapOffset * (totalItems - 1),
                                height = coinSize,
                            ),
                    ) {
                        visibleIcons.forEachIndexed { index, item ->
                            Box(
                                modifier = Modifier
                                    .offset(x = (coinSize - overlapOffset) * index)
                                    .size(coinSize)
                                    .clip(CircleShape)
                                    .background(iconBgColor)
                                    .border(2.dp, iconBorderColor, CircleShape),
                                contentAlignment = Alignment.Center,
                            ) {
                                PopIcon(
                                    imageVector = item.imageVector,
                                    contentDescription = item.contentDescription,
                                    size = PopIconSize.Small,
                                    tint = iconTint,
                                )
                            }
                        }
                        if (overflowCount > 0) {
                            val overflowIndex = visibleIcons.size
                            Box(
                                modifier = Modifier
                                    .offset(x = (coinSize - overlapOffset) * overflowIndex)
                                    .size(coinSize)
                                    .clip(CircleShape)
                                    .background(iconBgColor)
                                    .border(2.dp, iconBorderColor, CircleShape),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = "+$overflowCount",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = iconTint,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
