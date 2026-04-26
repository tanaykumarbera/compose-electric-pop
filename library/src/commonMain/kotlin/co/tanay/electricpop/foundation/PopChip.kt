package co.tanay.electricpop.foundation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import co.tanay.electricpop.theme.ElectricPopTheme
import co.tanay.electricpop.theme.PopShapeFull

/**
 * Color presets for [PopChip].
 *
 * Each preset maps to the corresponding container / onContainer token pair
 * from [MaterialTheme.colorScheme].
 */
enum class PopChipColor {
    /** Electric Lime — primaryContainer / onPrimaryContainer */
    Primary,

    /** Neon Magenta — secondaryContainer / onSecondaryContainer */
    Secondary,

    /** Cyber Cyan — tertiaryContainer / onTertiaryContainer */
    Tertiary,
}

/**
 * Resolved container + content color pair for a [PopChip].
 */
@Immutable
data class PopChipColors(
    val containerColor: Color,
    val contentColor: Color,
)

/**
 * Resolves a [PopChipColor] preset to actual theme colors.
 */
@Composable
fun PopChipColor.toColors(): PopChipColors {
    val scheme = MaterialTheme.colorScheme
    return when (this) {
        PopChipColor.Primary -> PopChipColors(scheme.primaryContainer, scheme.onPrimaryContainer)
        PopChipColor.Secondary -> PopChipColors(scheme.secondaryContainer, scheme.onSecondaryContainer)
        PopChipColor.Tertiary -> PopChipColors(scheme.tertiaryContainer, scheme.onTertiaryContainer)
    }
}

/**
 * A pill-shaped chip for tags, filters, and category labels.
 *
 * PopChip displays text inside a fully-rounded (pill) container with an
 * optional leading icon. It uses [PopShapeFull] for the shape and reads
 * all styling from the theme — no hardcoded colors, typography, or spacing.
 *
 * Supports kinetic interactions (hover: scale 1.05, press: scale 0.95)
 * when an [onClick] handler is provided.
 *
 * @param label The text to display inside the chip.
 * @param modifier Optional [Modifier] for the chip container.
 * @param color A [PopChipColor] preset that determines the container and content colors.
 * @param icon Optional leading [ImageVector] icon displayed before the label.
 * @param onClick Optional click handler. When null, the chip is non-interactive.
 */
@Composable
fun PopChip(
    label: String,
    modifier: Modifier = Modifier,
    color: PopChipColor = PopChipColor.Primary,
    icon: ImageVector? = null,
    onClick: (() -> Unit)? = null,
) {
    val colors = color.toColors()
    PopChip(
        label = label,
        modifier = modifier,
        containerColor = colors.containerColor,
        contentColor = colors.contentColor,
        icon = icon,
        onClick = onClick,
    )
}

/**
 * A pill-shaped chip with custom colors.
 *
 * This overload allows specifying arbitrary container and content colors
 * instead of using a [PopChipColor] preset.
 *
 * @param label The text to display inside the chip.
 * @param modifier Optional [Modifier] for the chip container.
 * @param containerColor Background color of the chip.
 * @param contentColor Text and icon tint color inside the chip.
 * @param icon Optional leading [ImageVector] icon displayed before the label.
 * @param onClick Optional click handler. When null, the chip is non-interactive.
 */
@Composable
fun PopChip(
    label: String,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    icon: ImageVector? = null,
    onClick: (() -> Unit)? = null,
) {
    val spacing = ElectricPopTheme.spacing
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()

    // Kinetic Interactions: hover → 1.05x, active → 0.95x (rule 5)
    val targetScale = when {
        isPressed -> 0.95f
        isHovered -> 1.05f
        else -> 1f
    }
    val scale by animateFloatAsState(
        targetValue = targetScale,
        animationSpec = tween(durationMillis = 200),
    )

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
            .scale(scale)
            .clip(PopShapeFull)
            .background(containerColor)
            .then(clickModifier)
            .padding(
                horizontal = spacing.md,
                vertical = spacing.xs,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacing.xs),
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(16.dp),
            )
        }
        Text(
            text = label,
            color = contentColor,
            style = MaterialTheme.typography.labelLarge,
        )
    }
}
