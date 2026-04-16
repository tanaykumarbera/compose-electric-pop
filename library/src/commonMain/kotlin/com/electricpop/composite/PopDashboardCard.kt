package com.electricpop.composite

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
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
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.electricpop.foundation.PopSurface
import com.electricpop.theme.ElectricPopTheme
import com.electricpop.theme.PopShapeFull

/**
 * A single sub-item displayed inside [PopDashboardCard] as a compact label+value card.
 * Label and value are generic strings — no domain-specific field names.
 */
@Immutable
data class PopDashboardItem(
    val label: String,
    val value: String,
)

/**
 * A full-width dashboard hub card with a tertiary background, hero label+value header,
 * a 2-column grid of sub-item cards, and optional action/watermark slots.
 *
 * Design rules applied:
 * - Rule 1 (No-Line): Sub-item cards use tonal bg (onTertiary/5), not borders for separation.
 *   The ghost border (Rule 3) is applied on sub-items for accessibility.
 * - Rule 2 (Tonal Shadows): PopSurface handles tonal shadow automatically.
 * - Rule 3 (Ghost Border): Sub-item cards have onTertiaryContainer at 10% opacity border.
 * - Rule 4 (Neon Glow): N/A — data display card.
 * - Rule 5 (Kinetic): Card-level hover/press scale when onClick != null.
 *   Action button (FAB slot) handles its own kinetics.
 * - Rule 6 (Squircle Radii): MaterialTheme.shapes.extraSmall for outer card,
 *   MaterialTheme.shapes.medium for sub-item cards.
 * - Rule 7 (Typography): title uppercase, titleValue in displaySmall italic black.
 *
 * @param title Small uppercase label shown above [titleValue] (e.g., "Overview").
 * @param titleValue Large bold italic display value (e.g., "14,200").
 * @param items List of [PopDashboardItem] displayed as 2-column sub-cards at the bottom.
 * @param modifier Optional [Modifier].
 * @param backgroundIcon Optional watermark icon shown at 10% opacity in the bottom-right corner.
 *   Defaults to null (no watermark).
 * @param statusContent Optional slot for a status pill in the top-right area.
 * @param actionContent Optional FAB-style action slot appended after the last sub-card.
 *   Typically a circular button with a "+" icon. Null = no action button shown.
 * @param onClick Optional click handler for the card; enables kinetic animation when set.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PopDashboardCard(
    title: String,
    titleValue: String,
    items: List<PopDashboardItem>,
    modifier: Modifier = Modifier,
    backgroundIcon: ImageVector? = null,
    statusContent: (@Composable RowScope.() -> Unit)? = null,
    actionContent: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null,
) {
    val spacing = ElectricPopTheme.spacing

    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()

    val targetScale = when {
        onClick == null -> 1f
        isPressed -> 0.97f
        isHovered -> 1.02f
        else -> 1f
    }
    val scale by animateFloatAsState(
        targetValue = targetScale,
        animationSpec = tween(durationMillis = 200),
        label = "card_scale",
    )

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
                },
            ),
    ) {
        PopSurface(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            shape = MaterialTheme.shapes.extraSmall,
            shadowEnabled = true,
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                // Watermark icon — absolute bottom-right, 160dp size, 10% opacity
                if (backgroundIcon != null) {
                    Icon(
                        imageVector = backgroundIcon,
                        contentDescription = null,
                        modifier = Modifier
                            .size(160.dp)
                            .align(Alignment.BottomEnd)
                            .offset(x = 32.dp, y = 32.dp),
                        tint = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.1f),
                    )
                }

                // Card content column
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(spacing.xl),
                ) {
                    // Top: title area + status pill
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top,
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = title.uppercase(),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f),
                                letterSpacing = 3.sp,
                            )
                            Text(
                                text = titleValue,
                                style = MaterialTheme.typography.displaySmall,
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.onTertiaryContainer,
                            )
                        }
                        if (statusContent != null) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(spacing.xs),
                                content = statusContent,
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(spacing.xl))

                    // Bottom: 2-column wrapping sub-cards
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(spacing.md),
                        verticalArrangement = Arrangement.spacedBy(spacing.md),
                    ) {
                        items.forEach { item ->
                            DashboardSubCard(
                                item = item,
                                modifier = Modifier
                                    .weight(1f)
                                    .widthIn(min = 120.dp),
                            )
                        }
                    }

                    // FAB row — right-aligned, below sub-cards
                    if (actionContent != null) {
                        Spacer(modifier = Modifier.height(spacing.md))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                        ) {
                            actionContent()
                        }
                    }
                }
            }
        }
    }
}

/**
 * Status pill with an animated dot indicator, styled for use inside [PopDashboardCard].
 *
 * The dot pulses via an infinite alpha animation. In screenshot tests this animates
 * but the static capture will show a fixed alpha — expected behavior.
 *
 * @param label Pill label text (rendered uppercase).
 * @param modifier Optional [Modifier].
 * @param dotColor Color of the indicator dot. Defaults to [MaterialTheme.colorScheme.secondary].
 */
@Composable
fun PopDashboardStatusPill(
    label: String,
    modifier: Modifier = Modifier,
    dotColor: Color = MaterialTheme.colorScheme.secondary,
) {
    val spacing = ElectricPopTheme.spacing

    val infiniteTransition = rememberInfiniteTransition(label = "dot_pulse")
    val dotAlpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "dot_alpha",
    )

    Row(
        modifier = modifier
            .clip(PopShapeFull)
            .background(Color.White.copy(alpha = 0.2f))
            .padding(horizontal = spacing.md, vertical = spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacing.xs),
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(PopShapeFull)
                .background(dotColor.copy(alpha = dotAlpha)),
        )
        Text(
            text = label.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onTertiaryContainer,
            letterSpacing = 1.5.sp,
        )
    }
}

@Composable
private fun DashboardSubCard(item: PopDashboardItem, modifier: Modifier = Modifier) {
    val spacing = ElectricPopTheme.spacing
    val ghostBorderColor = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.1f)

    Column(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.05f))
            .border(1.dp, ghostBorderColor, MaterialTheme.shapes.medium)
            .padding(spacing.md),
        verticalArrangement = Arrangement.spacedBy(spacing.xxs),
    ) {
        Text(
            text = item.label.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.6f),
            letterSpacing = 2.sp,
        )
        Text(
            text = item.value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onTertiaryContainer,
        )
    }
}
