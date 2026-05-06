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
import androidx.compose.ui.unit.Dp
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

    /** Error — errorContainer / onErrorContainer */
    Error,

    /** Neutral — surfaceContainerHigh / onSurface */
    Neutral,
}

/**
 * Size variant for a [PopChip].
 */
enum class PopChipSize {
    /** Compact uppercase pill — matches the former PopPill appearance. */
    Small,

    /** Default label chip — as-typed casing. */
    Medium,

    /** Prominent filter chip — as-typed casing, larger padding and typography. */
    Large,
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
        PopChipColor.Error -> PopChipColors(scheme.errorContainer, scheme.onErrorContainer)
        PopChipColor.Neutral -> PopChipColors(scheme.surfaceContainerHigh, scheme.onSurface)
    }
}

/**
 * A pill-shaped chip for tags, filters, and category labels.
 *
 * PopChip displays text inside a fully-rounded (pill) container with an
 * optional leading icon. It uses [PopShapeFull] for the shape and reads
 * all styling from the theme — no hardcoded colors, typography, or spacing.
 *
 * Size variants:
 * - [PopChipSize.Small] — uppercase casing, [labelSmall] typography, compact padding. Replaces the former PopPill.
 * - [PopChipSize.Medium] — as-typed casing, [labelLarge] typography (default).
 * - [PopChipSize.Large] — as-typed casing, [titleSmall] typography, wider padding.
 *
 * Supports kinetic interactions (hover: scale 1.05, press: scale 0.95)
 * when an [onClick] handler is provided.
 *
 * @param label The text to display inside the chip.
 * @param modifier Optional [Modifier] for the chip container.
 * @param color A [PopChipColor] preset that determines the container and content colors.
 * @param size Size variant; defaults to [PopChipSize.Medium].
 * @param icon Optional leading [ImageVector] icon displayed before the label.
 * @param onClick Optional click handler. When null, the chip is non-interactive.
 *
 * <!-- screenshots:start (auto-generated, do not edit) -->
 * | Scenario | Light | Dark |
 * | --- | --- | --- |
 * | allVariants | ![](https://tanaykumarbera.github.io/compose-electric-pop/snapshots/PopChip_allVariants_light.png) | ![](https://tanaykumarbera.github.io/compose-electric-pop/snapshots/PopChip_allVariants_dark.png) |
 * <!-- screenshots:end -->
 */
@Composable
fun PopChip(
    label: String,
    modifier: Modifier = Modifier,
    color: PopChipColor = PopChipColor.Primary,
    size: PopChipSize = PopChipSize.Medium,
    icon: ImageVector? = null,
    onClick: (() -> Unit)? = null,
) {
    val colors = color.toColors()
    PopChip(
        label = label,
        modifier = modifier,
        containerColor = colors.containerColor,
        contentColor = colors.contentColor,
        size = size,
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
 * @param size Size variant; defaults to [PopChipSize.Medium].
 * @param icon Optional leading [ImageVector] icon displayed before the label.
 * @param onClick Optional click handler. When null, the chip is non-interactive.
 */
@Composable
fun PopChip(
    label: String,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    size: PopChipSize = PopChipSize.Medium,
    icon: ImageVector? = null,
    onClick: (() -> Unit)? = null,
) {
    val spacing = ElectricPopTheme.spacing
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()

    // Kinetic Interactions: hover → 1.05x, active → 0.95x (rule 5)
    val targetScale = when {
        onClick != null && isPressed -> 0.95f
        onClick != null && isHovered -> 1.05f
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

    val paddingH: Dp
    val paddingV: Dp
    val iconSize: Dp
    val gap: Dp
    val displayLabel: String

    when (size) {
        PopChipSize.Small -> {
            paddingH = spacing.sm
            paddingV = spacing.xxs
            iconSize = 12.dp
            gap = spacing.xxs
            displayLabel = label.uppercase()
        }
        PopChipSize.Medium -> {
            paddingH = spacing.md
            paddingV = spacing.xs
            iconSize = 16.dp
            gap = spacing.xs
            displayLabel = label
        }
        PopChipSize.Large -> {
            paddingH = spacing.lg
            paddingV = spacing.sm
            iconSize = 20.dp
            gap = spacing.sm
            displayLabel = label
        }
    }

    val textStyle = when (size) {
        PopChipSize.Small -> MaterialTheme.typography.labelSmall
        PopChipSize.Medium -> MaterialTheme.typography.labelLarge
        PopChipSize.Large -> MaterialTheme.typography.titleSmall
    }

    Row(
        modifier = modifier
            .scale(scale)
            .clip(PopShapeFull)
            .background(containerColor)
            .then(clickModifier)
            .padding(
                horizontal = paddingH,
                vertical = paddingV,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(gap),
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(iconSize),
            )
        }
        Text(
            text = displayLabel,
            color = contentColor,
            style = textStyle,
        )
    }
}
