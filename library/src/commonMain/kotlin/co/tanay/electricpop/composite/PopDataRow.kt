package co.tanay.electricpop.composite

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
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.tanay.electricpop.foundation.PopDisplayTextDirection
import co.tanay.electricpop.foundation.PopIcon
import co.tanay.electricpop.foundation.PopIconSize
import co.tanay.electricpop.foundation.toColor
import co.tanay.electricpop.theme.ElectricPopTheme
import co.tanay.electricpop.theme.PopShapeFull

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
 * @param chips Optional list of [PopDataRowChip] rendered on the second line below the label.
 *   Supports multiple chips (e.g., category + tags). Rendered in a wrapping flow row.
 * @param subtitle Optional prefix text shown on the first line before [label] (e.g., a date "Oct 24").
 *   Rendered uppercase with wide letter spacing.
 * @param direction Semantic direction for the value color. Ignored if [valueColor] is non-null.
 * @param valueColor Explicit color override for the value text.
 * @param isAlternate If true, uses `surfaceContainerLowest` instead of `surfaceContainerLow`.
 * @param onClick Optional click handler. Enables kinetic hover/press scale animation when set.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PopDataRow(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    iconContainerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    iconContentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    chips: List<PopDataRowChip> = emptyList(),
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

        // Stacked: [date] / [title] / [chips]
        Column(modifier = Modifier.weight(1f)) {
            if (subtitle != null) {
                Text(
                    text = subtitle.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline,
                    letterSpacing = 1.5.sp,
                )
            }
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = if (subtitle != null) spacing.xxs else 0.dp),
            )
            if (chips.isNotEmpty()) {
                FlowRow(
                    modifier = Modifier.padding(top = spacing.xxs),
                    horizontalArrangement = Arrangement.spacedBy(spacing.xxs),
                    verticalArrangement = Arrangement.spacedBy(spacing.xxs),
                ) {
                    chips.forEach { chip ->
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
                }
            }
        }

        Spacer(Modifier.width(spacing.md))

        // Value on the right — italic per design
        Text(
            text = value.uppercase(),
            style = MaterialTheme.typography.titleLarge,
            fontStyle = FontStyle.Italic,
            color = resolvedValueColor,
        )
    }
}
