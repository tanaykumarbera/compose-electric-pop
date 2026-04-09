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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.electricpop.foundation.PopDisplayTextDirection
import com.electricpop.foundation.PopIcon
import com.electricpop.foundation.PopIconSize
import com.electricpop.foundation.toColor
import com.electricpop.theme.ElectricPopTheme
import com.electricpop.theme.PopShapeFull

/**
 * A chip descriptor for use in [PopDataRow].
 *
 * @param label The chip text (rendered uppercase).
 * @param containerColor Background color of the chip.
 * @param contentColor Text color of the chip.
 */
@Immutable
data class PopDataRowChip(
    val label: String,
    val containerColor: Color,
    val contentColor: Color,
)

/**
 * Factory helpers for common chip color combinations.
 */
object PopDataRowDefaults {

    @Composable
    fun chipPrimary(label: String): PopDataRowChip = PopDataRowChip(
        label = label,
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
    )

    @Composable
    fun chipSecondary(label: String): PopDataRowChip = PopDataRowChip(
        label = label,
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
    )

    @Composable
    fun chipTertiary(label: String): PopDataRowChip = PopDataRowChip(
        label = label,
        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
    )

    @Composable
    fun chipSurface(label: String): PopDataRowChip = PopDataRowChip(
        label = label,
        containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

/**
 * A data row component for displaying labeled values with an icon, optional chip/subtitle,
 * directional value coloring, and tonal alternation.
 *
 * Follows the No-Line Rule: alternation is achieved via `surfaceContainerLow` vs
 * `surfaceContainerLowest` background shifts rather than dividers.
 *
 * @param icon Leading icon identifying the row category.
 * @param label Primary label text.
 * @param value Value text displayed on the trailing side (rendered uppercase).
 * @param modifier Optional [Modifier] applied to the row container.
 * @param iconContainerColor Background color of the circular icon container.
 * @param iconContentColor Tint color of the icon.
 * @param chip Optional [PopDataRowChip] rendered below the label.
 * @param subtitle Optional subtitle text rendered below the label (rendered uppercase).
 * @param direction Semantic direction for the value color. Ignored if [valueColor] is non-null.
 * @param valueColor Explicit color override for the value text.
 * @param isAlternate If true, uses `surfaceContainerLowest` instead of `surfaceContainerLow`.
 * @param onClick Optional click handler. Enables kinetic hover/press scale animation when set.
 */
@Composable
fun PopDataRow(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    iconContainerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    iconContentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    chip: PopDataRowChip? = null,
    subtitle: String? = null,
    direction: PopDisplayTextDirection = PopDisplayTextDirection.Neutral,
    valueColor: Color? = null,
    isAlternate: Boolean = false,
    onClick: (() -> Unit)? = null,
) {
    val spacing = ElectricPopTheme.spacing
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()

    val targetScale = when {
        onClick == null -> 1f
        isPressed -> 0.98f
        isHovered -> 1.02f
        else -> 1f
    }
    val scale by animateFloatAsState(
        targetValue = targetScale,
        animationSpec = tween(durationMillis = 200),
    )

    val backgroundColor = if (isAlternate) {
        MaterialTheme.colorScheme.surfaceContainerLowest
    } else {
        MaterialTheme.colorScheme.surfaceContainerLow
    }

    val resolvedValueColor = valueColor ?: direction.toColor()

    val clickModifier = if (onClick != null) {
        Modifier.clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = onClick,
        )
    } else {
        Modifier
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .clip(MaterialTheme.shapes.small)
            .background(backgroundColor)
            .then(clickModifier)
            .padding(spacing.md),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Icon in circular container
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(PopShapeFull)
                .background(iconContainerColor),
            contentAlignment = Alignment.Center,
        ) {
            PopIcon(
                imageVector = icon,
                contentDescription = null,
                size = PopIconSize.Medium,
                tint = iconContentColor,
            )
        }

        Spacer(Modifier.width(spacing.md))

        // Label + chip/subtitle column
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            if (chip != null || subtitle != null) {
                Row(
                    modifier = Modifier.padding(top = spacing.xxs),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(spacing.xs),
                ) {
                    if (chip != null) {
                        Text(
                            text = chip.label.uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = chip.contentColor,
                            modifier = Modifier
                                .clip(PopShapeFull)
                                .background(chip.containerColor)
                                .padding(horizontal = spacing.xs, vertical = 2.dp),
                        )
                    }
                    if (subtitle != null) {
                        Text(
                            text = subtitle.uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline,
                            letterSpacing = 1.5.sp,
                        )
                    }
                }
            }
        }

        Spacer(Modifier.width(spacing.md))

        // Value on the right
        Text(
            text = value.uppercase(),
            style = MaterialTheme.typography.titleLarge,
            color = resolvedValueColor,
        )
    }
}
