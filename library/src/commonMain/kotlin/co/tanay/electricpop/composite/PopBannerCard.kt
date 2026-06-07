package co.tanay.electricpop.composite

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
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import co.tanay.electricpop.foundation.PopBadge
import co.tanay.electricpop.foundation.PopBadgeDirection
import co.tanay.electricpop.foundation.PopBadgeSize
import co.tanay.electricpop.foundation.PopDisplayText
import co.tanay.electricpop.foundation.PopDisplayTextDirection
import co.tanay.electricpop.foundation.PopDisplayTextSize
import co.tanay.electricpop.foundation.PopIcon
import co.tanay.electricpop.foundation.PopIconItem
import co.tanay.electricpop.foundation.PopIconSize
import co.tanay.electricpop.foundation.PopSurface
import co.tanay.electricpop.foundation.PopSurfaceTone
import co.tanay.electricpop.theme.ElectricPopTheme

/**
 * Visual style variant for [PopBannerCard].
 */
enum class PopBannerCardStyle {
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
 * @param style [PopBannerCardStyle.Hero] uses primaryContainer background; [PopBannerCardStyle.Surface] uses default surface tone.
 * @param onClick Optional click handler. Enables kinetic hover/press scale animation when set.
 *
 * <!-- screenshots:start (auto-generated, do not edit) -->
 * | Scenario | Light | Dark |
 * | --- | --- | --- |
 * | allVariants | ![](https://tanaykumarbera.github.io/compose-electric-pop/snapshots/PopBannerCard_allVariants_light.png) | ![](https://tanaykumarbera.github.io/compose-electric-pop/snapshots/PopBannerCard_allVariants_dark.png) |
 * <!-- screenshots:end -->
 */
@Composable
fun PopBannerCard(
    label: String,
    mainText: String,
    modifier: Modifier = Modifier,
    fractionalText: String? = null,
    badgeValue: String? = null,
    badgeDirection: PopBadgeDirection = PopBadgeDirection.Neutral,
    displayDirection: PopDisplayTextDirection = PopDisplayTextDirection.Neutral,
    displaySize: PopDisplayTextSize = PopDisplayTextSize.Large,
    icons: List<PopIconItem> = emptyList(),
    style: PopBannerCardStyle = PopBannerCardStyle.Surface,
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

    val isHero = style == PopBannerCardStyle.Hero

    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = onClick,
                    )
                } else {
                    Modifier
                },
            ),
    ) {
        PopSurface(
            shape = MaterialTheme.shapes.extraSmall,
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

                // 3 + 4. Chip + icon cluster on a single row. Chip is anchored to the
                // start, coins flush to the end. Coin count adapts to available width.
                if (badgeValue != null || icons.isNotEmpty()) {
                    Spacer(Modifier.height(spacing.sm))
                    BannerCardActionRow(
                        badgeValue = badgeValue,
                        badgeDirection = badgeDirection,
                        icons = icons,
                        isHero = isHero,
                    )
                }
            }
        }
    }
}

private val CoinSize: Dp = 48.dp
private val CoinOverlap: Dp = 16.dp
private val BadgeClusterGap: Dp = 24.dp
private const val COIN_MAX_VISIBLE: Int = 4

@Composable
private fun BannerCardActionRow(
    badgeValue: String?,
    badgeDirection: PopBadgeDirection,
    icons: List<PopIconItem>,
    isHero: Boolean,
) {
    val borderColor = if (isHero) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceContainer
    }
    val iconBgColor = if (isHero) {
        Color.White.copy(alpha = 0.4f)
    } else {
        MaterialTheme.colorScheme.surfaceContainerHigh
    }
    val iconTint = if (isHero) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    SubcomposeLayout(modifier = Modifier.fillMaxWidth()) { constraints ->
        val maxWidthPx = if (constraints.hasBoundedWidth) constraints.maxWidth else Int.MAX_VALUE
        val coinPx = CoinSize.roundToPx()
        val overlapPx = CoinOverlap.roundToPx()

        val badgePlaceable = if (badgeValue != null) {
            subcompose(BannerCardSlot.Badge) {
                if (isHero) {
                    PopBadge(
                        value = badgeValue,
                        direction = badgeDirection,
                        containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        contentColor = MaterialTheme.colorScheme.primaryContainer,
                        size = PopBadgeSize.Large,
                    )
                } else {
                    PopBadge(
                        value = badgeValue,
                        direction = badgeDirection,
                        size = PopBadgeSize.Large,
                    )
                }
            }.first().measure(Constraints())
        } else {
            null
        }

        val badgeWidth = badgePlaceable?.width ?: 0
        val gapPx = if (badgeWidth > 0) BadgeClusterGap.roundToPx() else 0
        val widthForCoins = (maxWidthPx - badgeWidth - gapPx).coerceAtLeast(0)
        val maxCoinsThatFit = if (coinPx > overlapPx) {
            ((widthForCoins - overlapPx).coerceAtLeast(0)) / (coinPx - overlapPx)
        } else {
            icons.size
        }
        val (visibleCount, overflowCount) = resolveCoinCounts(
            iconCount = icons.size,
            requestedMaxVisible = COIN_MAX_VISIBLE,
            maxCoinsThatFit = maxCoinsThatFit,
        )

        val showCluster = icons.isNotEmpty() && (visibleCount > 0 || overflowCount > 0)
        val clusterPlaceable = if (showCluster) {
            subcompose(BannerCardSlot.Cluster) {
                CoinCluster(
                    visibleIcons = icons.take(visibleCount),
                    overflowCount = overflowCount,
                    iconBgColor = iconBgColor,
                    borderColor = borderColor,
                    iconTint = iconTint,
                )
            }.first().measure(Constraints())
        } else {
            null
        }

        val height = maxOf(badgePlaceable?.height ?: 0, clusterPlaceable?.height ?: 0)
        val width = if (constraints.hasBoundedWidth) {
            constraints.maxWidth
        } else {
            (badgePlaceable?.width ?: 0) + (clusterPlaceable?.width ?: 0)
        }

        layout(width, height) {
            badgePlaceable?.placeRelative(
                x = 0,
                y = (height - badgePlaceable.height) / 2,
            )
            clusterPlaceable?.placeRelative(
                x = width - clusterPlaceable.width,
                y = (height - clusterPlaceable.height) / 2,
            )
        }
    }
}

private enum class BannerCardSlot { Badge, Cluster }

@Composable
private fun CoinCluster(
    visibleIcons: List<PopIconItem>,
    overflowCount: Int,
    iconBgColor: Color,
    borderColor: Color,
    iconTint: Color,
) {
    val totalItems = visibleIcons.size + if (overflowCount > 0) 1 else 0
    if (totalItems == 0) return
    Box(
        modifier = Modifier.size(
            width = CoinSize * totalItems - CoinOverlap * (totalItems - 1),
            height = CoinSize,
        ),
    ) {
        visibleIcons.forEachIndexed { index, item ->
            Box(
                modifier = Modifier
                    .offset(x = (CoinSize - CoinOverlap) * index)
                    .size(CoinSize)
                    .clip(CircleShape)
                    .background(iconBgColor)
                    .border(2.dp, borderColor, CircleShape),
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
        if (overflowCount > 0) {
            val overflowIndex = visibleIcons.size
            Box(
                modifier = Modifier
                    .offset(x = (CoinSize - CoinOverlap) * overflowIndex)
                    .size(CoinSize)
                    .clip(CircleShape)
                    .background(iconBgColor)
                    .border(2.dp, borderColor, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "+$overflowCount",
                    style = MaterialTheme.typography.labelMedium,
                    color = iconTint,
                )
            }
        }
    }
}

private fun resolveCoinCounts(
    iconCount: Int,
    requestedMaxVisible: Int,
    maxCoinsThatFit: Int,
): Pair<Int, Int> {
    if (iconCount == 0) return 0 to 0
    val requestedTotal = if (iconCount > requestedMaxVisible) requestedMaxVisible + 1 else iconCount
    val totalAllowed = requestedTotal.coerceAtMost(maxCoinsThatFit).coerceAtLeast(1)
    return if (iconCount <= totalAllowed) {
        iconCount to 0
    } else {
        val visible = (totalAllowed - 1).coerceAtLeast(0)
        visible to (iconCount - visible)
    }
}
