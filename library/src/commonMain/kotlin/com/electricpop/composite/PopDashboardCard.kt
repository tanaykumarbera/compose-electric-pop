package com.electricpop.composite

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.electricpop.foundation.PopDisplayTextDirection
import com.electricpop.foundation.PopSurface
import com.electricpop.theme.ElectricPopTheme

/**
 * A single data row descriptor for use in [PopDashboardCard] and [PopDashboardCardCompact].
 *
 * @param icon Leading icon identifying the row category.
 * @param label Primary label text.
 * @param value Value text displayed on the trailing side (rendered uppercase by [PopDataRow]).
 * @param chips Optional list of [PopDataRowChip] rendered below the label.
 * @param subtitle Optional prefix text shown before the label (e.g., a date).
 * @param direction Semantic direction for the value color. Ignored if [valueColor] is non-null.
 * @param valueColor Explicit color override for the value text.
 * @param onClick Optional click handler for this individual row.
 */
@Immutable
data class PopDashboardCardRow(
    val icon: ImageVector,
    val label: String,
    val value: String,
    val chips: List<PopDataRowChip> = emptyList(),
    val subtitle: String? = null,
    val direction: PopDisplayTextDirection = PopDisplayTextDirection.Neutral,
    val valueColor: Color? = null,
    val onClick: (() -> Unit)? = null,
)

/**
 * A data overview card with a tertiary-tinted background, optional section header,
 * status pills slot, and a list of data rows.
 *
 * Design rules applied:
 * - Rule 1 (No-Line): No 1px borders. Row separation is via tonal background shifts.
 * - Rule 2 (Tonal Shadows): PopSurface handles tonal shadow automatically.
 * - Rule 4 (Neon Glow): N/A — data display card, not a CTA.
 * - Rule 5 (Kinetic Interactions): Hover 1.02x / Active 0.97x, 200ms, only when onClick != null.
 * - Rule 6 (Squircle Radii): MaterialTheme.shapes.extraLarge for card.
 * - Rule 7 (Typography Impact): Title label uppercase, title uses headlineLarge italic bold.
 *
 * @param title Card title text. Rendered uppercase as a small label above [titleValue].
 * @param rows List of [PopDashboardCardRow] items to render as data rows inside the card.
 * @param modifier Optional [Modifier] for the card container.
 * @param titleValue Optional large display value shown below [title] (e.g., a total amount).
 *   When null, [title] is displayed in headlineLarge style directly.
 * @param statusContent Optional slot rendered in the top-right area for status pills.
 * @param onClick Optional click handler. Enables kinetic hover/press animation when set.
 */
@Composable
fun PopDashboardCard(
    title: String,
    rows: List<PopDashboardCardRow>,
    modifier: Modifier = Modifier,
    titleValue: String? = null,
    statusContent: (@Composable RowScope.() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
) {
    val spacing = ElectricPopTheme.spacing

    val header: @Composable () -> Unit = {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            Column {
                if (titleValue != null) {
                    // Small uppercase label above the value
                    Text(
                        text = title.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f),
                        letterSpacing = 3.sp,
                    )
                    // Large italic bold value
                    Text(
                        text = titleValue,
                        style = MaterialTheme.typography.headlineLarge,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                    )
                } else {
                    // Title in headlineLarge when no separate value
                    Text(
                        text = title.uppercase(),
                        style = MaterialTheme.typography.headlineLarge,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                    )
                }
            }
            if (statusContent != null) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(spacing.xs),
                    content = statusContent,
                )
            }
        }
    }

    DashboardCardContent(
        rows = rows,
        modifier = modifier,
        header = header,
        statusContent = null, // header handles status for this variant
        onClick = onClick,
    )
}

/**
 * A compact data overview card without a section header.
 *
 * Displays an optional status pills row at the top followed by data rows.
 * Uses the same tertiary-tinted background as [PopDashboardCard].
 *
 * @param rows List of [PopDashboardCardRow] items to render as data rows.
 * @param modifier Optional [Modifier] for the card container.
 * @param statusContent Optional slot at the top for status pills.
 * @param onClick Optional click handler. Enables kinetic hover/press animation when set.
 */
@Composable
fun PopDashboardCardCompact(
    rows: List<PopDashboardCardRow>,
    modifier: Modifier = Modifier,
    statusContent: (@Composable RowScope.() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
) {
    DashboardCardContent(
        rows = rows,
        modifier = modifier,
        header = null,
        statusContent = statusContent,
        onClick = onClick,
    )
}

@Composable
private fun DashboardCardContent(
    rows: List<PopDashboardCardRow>,
    modifier: Modifier = Modifier,
    header: (@Composable () -> Unit)? = null,
    statusContent: (@Composable RowScope.() -> Unit)? = null,
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
                }
            ),
    ) {
        PopSurface(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            shape = MaterialTheme.shapes.extraLarge,
            shadowEnabled = true,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = spacing.xxl,
                        start = spacing.xl,
                        end = spacing.xl,
                        bottom = spacing.xl,
                    ),
            ) {
                // Full header for default variant
                if (header != null) {
                    header()
                    Spacer(modifier = Modifier.height(spacing.lg))
                }

                // Compact variant: just show status pills right-aligned
                if (header == null && statusContent != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(spacing.xs),
                            content = statusContent,
                        )
                    }
                    Spacer(modifier = Modifier.height(spacing.sm))
                }

                // Data rows
                Column(verticalArrangement = Arrangement.spacedBy(spacing.xs)) {
                    rows.forEachIndexed { index, row ->
                        PopDataRow(
                            icon = row.icon,
                            label = row.label,
                            value = row.value,
                            iconContainerColor = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.1f),
                            iconContentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                            chips = row.chips,
                            subtitle = row.subtitle,
                            direction = row.direction,
                            valueColor = row.valueColor,
                            isAlternate = index % 2 == 1,
                            onClick = row.onClick,
                        )
                    }
                }
            }
        }
    }
}
