package co.tanay.electricpop.composite

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.unit.dp
import co.tanay.electricpop.foundation.PopChip
import co.tanay.electricpop.foundation.PopDisplayText
import co.tanay.electricpop.foundation.PopDisplayTextSize
import co.tanay.electricpop.foundation.PopIcon
import co.tanay.electricpop.foundation.PopIconSize
import co.tanay.electricpop.foundation.PopSurface
import co.tanay.electricpop.theme.ElectricPopTheme

/**
 * Visual style variant for [PopCarouselCard].
 *
 * Each style determines the card background color and the colors applied to
 * its icon container, icon tint, text elements (label, category, timestamp, value).
 */
enum class PopCarouselCardStyle {
    /** Neutral — surfaceContainerLowest background */
    Surface,

    /** Electric Lime — primaryContainer background */
    Primary,

    /** Neon Magenta — secondaryContainer background */
    Secondary,

    /** Cyber Cyan — tertiaryContainer background */
    Tertiary,
}

/**
 * Data model for a single [PopCarouselCard] item.
 *
 * @param icon Icon displayed in the top-left container.
 * @param iconContentDescription Accessibility description for the icon; null if decorative.
 * @param timestamp Short time string shown in the top-right (e.g., "08:42 AM").
 * @param category Category label shown as a chip below the spacer.
 * @param label Main card label displayed as a headline (uppercase).
 * @param value Numeric or text value displayed as a large display text.
 * @param style Visual style variant controlling background and content colors.
 * @param onClick Optional click handler. Enables kinetic hover/press animation when set.
 */
data class PopCarouselCardItem(
    val icon: ImageVector,
    val iconContentDescription: String? = null,
    val timestamp: String,
    val category: String,
    val label: String,
    val value: String,
    val style: PopCarouselCardStyle = PopCarouselCardStyle.Surface,
    val onClick: (() -> Unit)? = null,
)

/**
 * Internal color tokens resolved per [PopCarouselCardStyle].
 */
private data class CarouselCardColors(
    val cardBg: Color,
    val iconContainerBg: Color,
    val iconTint: Color,
    val labelColor: Color,
    val categoryColor: Color,
    val timestampColor: Color,
    val valueColor: Color,
)

@Composable
private fun PopCarouselCardStyle.toColors(): CarouselCardColors {
    val scheme = MaterialTheme.colorScheme
    return when (this) {
        PopCarouselCardStyle.Surface -> CarouselCardColors(
            cardBg = scheme.surfaceContainerLowest,
            iconContainerBg = scheme.surfaceContainerHigh,
            iconTint = scheme.onSurfaceVariant,
            labelColor = scheme.onSurface,
            categoryColor = scheme.onSurfaceVariant,
            timestampColor = scheme.outline,
            valueColor = scheme.onSurface,
        )
        PopCarouselCardStyle.Primary -> CarouselCardColors(
            cardBg = scheme.primaryContainer,
            iconContainerBg = scheme.surfaceContainerLowest,
            iconTint = scheme.primary,
            labelColor = scheme.onPrimaryContainer,
            categoryColor = scheme.primary,
            timestampColor = scheme.onPrimaryContainer.copy(alpha = 0.6f),
            valueColor = scheme.onPrimaryContainer,
        )
        PopCarouselCardStyle.Secondary -> CarouselCardColors(
            cardBg = scheme.secondaryContainer,
            iconContainerBg = Color.White.copy(alpha = 0.15f),
            iconTint = scheme.secondary,
            labelColor = scheme.onSecondaryContainer,
            categoryColor = scheme.onSecondaryContainer,
            timestampColor = scheme.onSecondaryContainer.copy(alpha = 0.6f),
            valueColor = scheme.onSecondaryContainer,
        )
        PopCarouselCardStyle.Tertiary -> CarouselCardColors(
            cardBg = scheme.tertiaryContainer,
            iconContainerBg = Color.White.copy(alpha = 0.15f),
            iconTint = scheme.tertiary,
            labelColor = scheme.onTertiaryContainer,
            categoryColor = scheme.onTertiaryContainer,
            timestampColor = scheme.onTertiaryContainer.copy(alpha = 0.6f),
            valueColor = scheme.onTertiaryContainer,
        )
    }
}

/**
 * A single carousel card displaying an icon, timestamp, category chip, label, and value.
 *
 * PopCarouselCard follows the 7 design rules:
 * - Rule 1 (No-Line): No borders. Card background uses tonal surface colors.
 * - Rule 2 (Tonal Shadows): PopSurface applies tonal shadow automatically.
 * - Rule 3 (Ghost Border): NOT applied — content cards, not accessibility-critical.
 * - Rule 4 (Neon Glow): NOT applied — content cards, not CTAs.
 * - Rule 5 (Kinetic Interactions): hover:1.02f press:0.97f, 200ms, when onClick != null.
 * - Rule 6 (Squircle Radii): MaterialTheme.shapes.extraSmall for card, shapes.small for icon container.
 * - Rule 7 (Typography Impact): timestamp.uppercase(), label.uppercase(), value via PopDisplayText.
 *
 * @param item The [PopCarouselCardItem] data to display.
 * @param modifier Optional [Modifier] for the outer container. Set width for card sizing (default: 300.dp in strip).
 */
@Composable
fun PopCarouselCard(
    item: PopCarouselCardItem,
    modifier: Modifier = Modifier,
) {
    val spacing = ElectricPopTheme.spacing
    val colors = item.style.toColors()

    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()

    val targetScale = when {
        item.onClick == null -> 1f
        isPressed -> 0.97f
        isHovered -> 1.02f
        else -> 1f
    }
    val scale by animateFloatAsState(
        targetValue = targetScale,
        animationSpec = tween(durationMillis = 200),
    )

    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .then(
                if (item.onClick != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = item.onClick,
                    )
                } else {
                    Modifier
                },
            ),
    ) {
        PopSurface(
            shape = MaterialTheme.shapes.extraSmall,
            containerColor = colors.cardBg,
            shadowEnabled = true,
            ghostBorder = false,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(spacing.xl),
            ) {
                // Row: icon container (top-left) + timestamp (top-right)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top,
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(MaterialTheme.shapes.small)
                            .background(colors.iconContainerBg),
                        contentAlignment = Alignment.Center,
                    ) {
                        PopIcon(
                            imageVector = item.icon,
                            contentDescription = item.iconContentDescription,
                            size = PopIconSize.Large,
                            tint = colors.iconTint,
                        )
                    }
                    Text(
                        text = item.timestamp.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.timestampColor,
                    )
                }

                // Large gap between icon row and category chip
                Spacer(modifier = Modifier.height(spacing.xxl))

                // Category chip — offset left to compensate chip's internal horizontal padding
                PopChip(
                    label = item.category,
                    modifier = Modifier.offset(x = -spacing.md),
                    containerColor = Color.Transparent,
                    contentColor = colors.categoryColor,
                )

                Spacer(modifier = Modifier.height(spacing.xxs))

                // Main label — uppercase
                Text(
                    text = item.label.uppercase(),
                    style = MaterialTheme.typography.headlineSmall,
                    color = colors.labelColor,
                )

                Spacer(modifier = Modifier.height(spacing.md))

                // Value — displayed as PopDisplayText (internally uppercase)
                PopDisplayText(
                    mainText = item.value,
                    size = PopDisplayTextSize.Small,
                    color = colors.valueColor,
                )
            }
        }
    }
}

/**
 * A horizontally scrollable strip of [PopCarouselCard]s with snap-to-item fling behavior.
 *
 * @param items The list of [PopCarouselCardItem]s to display.
 * @param modifier Optional [Modifier] for the strip container.
 * @param contentPadding Padding applied to the content area of the [LazyRow].
 *   Defaults to horizontal padding of [ElectricPopTheme.spacing.lg].
 */
@Composable
fun PopCarouselCardStrip(
    items: List<PopCarouselCardItem>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = ElectricPopTheme.spacing.lg),
) {
    val spacing = ElectricPopTheme.spacing
    val lazyListState = rememberLazyListState()
    val snapFlingBehavior = rememberSnapFlingBehavior(lazyListState)

    LazyRow(
        state = lazyListState,
        modifier = modifier,
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(spacing.md),
        flingBehavior = snapFlingBehavior,
    ) {
        items(items.size) { index ->
            PopCarouselCard(
                item = items[index],
                modifier = Modifier.width(300.dp),
            )
        }
    }
}
