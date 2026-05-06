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

                // 3 + 4. Chip + icon cluster on a single row.
                // Chip typography: titleLarge italic/Black/uppercase.
                // Chip colors:
                //   Hero → onPrimaryContainer bg, primaryContainer content.
                //   Surface → derived from badgeDirection.toColors().
                // Icons: 40dp circles, -12dp overlap, +N overflow.
                val hasChip = badgeValue != null
                val hasIcons = icons.isNotEmpty()
                if (hasChip || hasIcons) {
                    Spacer(Modifier.height(spacing.sm))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(spacing.md),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        if (badgeValue != null) {
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
                        }

                        if (hasIcons) {
                            val maxVisible = 5
                            val visibleIcons = icons.take(maxVisible)
                            val overflowCount = icons.size - maxVisible
                            val coinSize = 40.dp
                            val overlapOffset = 12.dp
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
                                            .border(2.dp, borderColor, CircleShape),
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
                                            .border(2.dp, borderColor, CircleShape),
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
    }
}
