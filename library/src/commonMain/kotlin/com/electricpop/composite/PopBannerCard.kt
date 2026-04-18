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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.electricpop.foundation.PopIcons
import com.electricpop.foundation.PopSurface
import com.electricpop.foundation.PopSurfaceTone
import com.electricpop.theme.ElectricPopTheme
import com.electricpop.theme.PopShapeFull

/**
 * The trend direction for [PopBannerCardTrend].
 */
enum class PopBannerCardTrendDirection { Up, Down, Neutral }

/**
 * A trend indicator shown at the bottom of [PopBannerCard].
 *
 * @param label The trend label text (rendered uppercase).
 * @param direction The trend direction — controls default icon selection.
 * @param icon Optional override icon; falls back to direction-based default.
 */
@Immutable
data class PopBannerCardTrend(
    val label: String,
    val direction: PopBannerCardTrendDirection = PopBannerCardTrendDirection.Up,
    val icon: ImageVector? = null,
)

/**
 * A vital indicator icon rendered in an overlapping circle cluster in [PopBannerCard].
 *
 * @param icon The icon to display.
 * @param contentDescription Accessibility description for the icon.
 */
@Immutable
data class PopBannerCardVital(
    val icon: ImageVector,
    val contentDescription: String? = null,
)

/**
 * A hero metric banner card showing a large primary value, label, optional fractional value,
 * trend pill, and vitals cluster.
 *
 * Design rules enforced:
 * - **Rule 1 (No-Line):** solid container color, no borders on the card itself.
 * - **Rule 2 (Tonal Shadows):** delegated to [PopSurface].
 * - **Rule 3 (Ghost Border):** not used — decorative hero card.
 * - **Rule 4 (Neon Glow):** not applicable for banner.
 * - **Rule 5 (Kinetic):** hover → scale(1.02), press → scale(0.97), 200ms tween (when [onClick] != null).
 * - **Rule 6 (Squircle):** `MaterialTheme.shapes.extraSmall` for card; `PopShapeFull` for pill + vitals.
 * - **Rule 7 (Typography Impact):** label uppercase + wide letterSpacing; value `displayLarge`;
 *   trend uppercase + Black Italic.
 *
 * @param label Card label (rendered uppercase with wide tracking).
 * @param value Primary metric value text (rendered as displayLarge).
 * @param modifier Optional [Modifier] for the outer container.
 * @param fractionalValue Optional fractional/supplemental value shown next to [value].
 * @param trend Optional trend indicator pill shown below the value block.
 * @param vitals Optional list of vital icon indicators shown as overlapping circles.
 * @param containerColor Card background color — defaults to `primaryContainer`.
 * @param contentColor Content color — defaults to `onPrimaryContainer`.
 * @param onClick Optional click handler; enables kinetic scale interaction when set.
 */
@Composable
fun PopBannerCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    fractionalValue: String? = null,
    trend: PopBannerCardTrend? = null,
    vitals: List<PopBannerCardVital> = emptyList(),
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    onClick: (() -> Unit)? = null,
) {
    val spacing = ElectricPopTheme.spacing
    val shape = MaterialTheme.shapes.extraSmall

    // Rule 5: Kinetic interaction
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()
    val targetScale = when {
        onClick == null -> 1f
        isPressed -> 0.97f
        isHovered -> 1.02f
        else -> 1f
    }
    val scale by animateFloatAsState(targetScale, tween(200), label = "banner_scale")

    Box(
        modifier = modifier
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .then(
                if (onClick != null) {
                    Modifier.clickable(interactionSource = interactionSource, indication = null, onClick = onClick)
                } else {
                    Modifier
                }
            ),
    ) {
        PopSurface(
            modifier = Modifier.fillMaxWidth(),
            tone = PopSurfaceTone.Default,
            shape = shape,
            shadowEnabled = true,
            ghostBorder = false,
            containerColor = containerColor,
            contentColor = contentColor,
        ) {
            Column(
                modifier = Modifier.padding(spacing.xl),
                verticalArrangement = Arrangement.spacedBy(spacing.xl),
            ) {
                // Block 1 — Label + value
                Column(verticalArrangement = Arrangement.spacedBy(spacing.xs)) {
                    // Rule 7: uppercase label with wide letterSpacing
                    Text(
                        text = label.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        letterSpacing = 2.4.sp,
                        color = contentColor.copy(alpha = 0.6f),
                    )
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(spacing.xs),
                    ) {
                        // Rule 7: displayLarge for primary value
                        Text(
                            text = value,
                            style = MaterialTheme.typography.displayLarge,
                            color = contentColor,
                        )
                        if (fractionalValue != null) {
                            Text(
                                text = fractionalValue,
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontStyle = FontStyle.Italic,
                                    fontWeight = FontWeight.Bold,
                                ),
                                color = contentColor,
                                modifier = Modifier.padding(bottom = 8.dp),
                            )
                        }
                    }
                }

                // Block 2 — Trend pill + vitals cluster (only if needed)
                if (trend != null || vitals.isNotEmpty()) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(spacing.md),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        // Trend pill
                        if (trend != null) {
                            Row(
                                modifier = Modifier
                                    .clip(PopShapeFull)
                                    .background(contentColor.copy(alpha = 0.15f))
                                    .padding(
                                        horizontal = spacing.md,
                                        vertical = spacing.xs,
                                    ),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(spacing.sm),
                            ) {
                                Icon(
                                    imageVector = trend.icon ?: when (trend.direction) {
                                        PopBannerCardTrendDirection.Up -> PopIcons.TrendUp
                                        PopBannerCardTrendDirection.Down -> PopIcons.TrendDown
                                        PopBannerCardTrendDirection.Neutral -> PopIcons.Sparkle
                                    },
                                    contentDescription = null,
                                    tint = contentColor,
                                    modifier = Modifier.size(20.dp),
                                )
                                // Rule 7: uppercase, Black Italic for trend label
                                Text(
                                    text = trend.label.uppercase(),
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontStyle = FontStyle.Italic,
                                        fontWeight = FontWeight.Black,
                                    ),
                                    color = contentColor,
                                )
                            }
                        }

                        // Vitals cluster — overlapping circles (Rule 6: PopShapeFull)
                        if (vitals.isNotEmpty()) {
                            Row(horizontalArrangement = Arrangement.spacedBy((-12).dp)) {
                                vitals.forEach { vital ->
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(PopShapeFull)
                                            .background(contentColor.copy(alpha = 0.15f))
                                            .border(2.dp, containerColor, PopShapeFull),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        Icon(
                                            imageVector = vital.icon,
                                            contentDescription = vital.contentDescription,
                                            tint = contentColor,
                                            modifier = Modifier.size(16.dp),
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
