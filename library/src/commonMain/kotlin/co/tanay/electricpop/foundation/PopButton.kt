package co.tanay.electricpop.foundation

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import co.tanay.electricpop.theme.ElectricPopTheme
import co.tanay.electricpop.theme.PopShapeFull

/**
 * Button visual style.
 */
enum class PopButtonStyle {
    /** Electric Lime filled button with neon glow. Primary CTA. */
    Primary,

    /** Neon Magenta filled button. Secondary CTA. */
    Secondary,

    /** Transparent button with ghost border. Tertiary action. */
    Ghost,
}

/**
 * Button size presets.
 */
enum class PopButtonSize {
    /** 56dp height, xl horizontal padding */
    XL,

    /** 48dp height, lg horizontal padding */
    Large,

    /** 36dp height, md horizontal padding */
    Small,
}

/**
 * Resolved color pair for a button style.
 */
@Immutable
data class PopButtonColors(
    val containerColor: Color,
    val contentColor: Color,
)

/**
 * Resolved dimensional properties for a button size.
 */
@Immutable
internal data class PopButtonDimensions(
    val height: Dp,
    val horizontalPadding: Dp,
    val iconSize: Dp,
)

/**
 * Resolves a [PopButtonStyle] to theme colors.
 */
@Composable
fun PopButtonStyle.toColors(): PopButtonColors {
    val scheme = MaterialTheme.colorScheme
    return when (this) {
        PopButtonStyle.Primary -> PopButtonColors(
            containerColor = scheme.primaryContainer,
            contentColor = scheme.onPrimaryContainer,
        )
        PopButtonStyle.Secondary -> PopButtonColors(
            containerColor = scheme.secondaryContainer,
            contentColor = scheme.onSecondaryContainer,
        )
        PopButtonStyle.Ghost -> PopButtonColors(
            containerColor = Color.Transparent,
            contentColor = scheme.onSurface,
        )
    }
}

/**
 * Resolves a [PopButtonSize] to dimensional properties using theme spacing.
 */
@Composable
internal fun PopButtonSize.toDimensions(): PopButtonDimensions {
    val spacing = ElectricPopTheme.spacing
    return when (this) {
        PopButtonSize.XL -> PopButtonDimensions(
            height = 56.dp,
            horizontalPadding = spacing.xl,
            iconSize = 24.dp,
        )
        PopButtonSize.Large -> PopButtonDimensions(
            height = 48.dp,
            horizontalPadding = spacing.lg,
            iconSize = 20.dp,
        )
        PopButtonSize.Small -> PopButtonDimensions(
            height = 36.dp,
            horizontalPadding = spacing.md,
            iconSize = 18.dp,
        )
    }
}

/**
 * Computes the kinetic scale for hover/press interactions.
 *
 * - Pressed: 0.95x (takes priority)
 * - Hovered: 1.05x
 * - Default: 1.0x
 *
 * Animated with 200ms ease-in-out per Design Rule 5.
 */
@Composable
internal fun kineticScale(
    interactionSource: MutableInteractionSource,
    enabled: Boolean,
): Float {
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()

    val targetScale = when {
        !enabled -> 1.0f
        isPressed -> 0.95f
        isHovered -> 1.05f
        else -> 1.0f
    }

    val scale by animateFloatAsState(
        targetValue = targetScale,
        animationSpec = tween(durationMillis = 200, easing = EaseInOut),
    )
    return scale
}

/**
 * A styled button following the Kinetic Pulse design system.
 *
 * PopButton supports three visual styles ([PopButtonStyle.Primary], [Secondary][PopButtonStyle.Secondary],
 * [Ghost][PopButtonStyle.Ghost]) and three sizes ([PopButtonSize.XL], [Large][PopButtonSize.Large],
 * [Small][PopButtonSize.Small]).
 *
 * Design rules enforced:
 * - **Rule 1 (No-Line):** No 1px borders on Primary/Secondary. Ghost uses Rule 3 ghost border.
 * - **Rule 4 (Neon Glow):** Primary variant gets a glow shadow from primaryContainer at ~20% opacity.
 * - **Rule 5 (Kinetic Interactions):** Hover scales to 1.05x, press compresses to 0.95x, 200ms ease.
 * - **Rule 6 (Squircle Radii):** Uses [PopShapeFull] (pill shape).
 * - **Rule 7 (Typography Impact):** Button text is uppercase.
 *
 * @param text The button label (rendered uppercase).
 * @param onClick Callback when the button is clicked.
 * @param modifier Optional [Modifier] applied to the button.
 * @param style Visual style: Primary (neon glow), Secondary, or Ghost.
 * @param size Size preset: XL, Large, or Small.
 * @param enabled Whether the button is interactive. Disabled buttons reduce opacity.
 * @param icon Optional leading icon [ImageVector] displayed before the text.
 *
 * <!-- screenshots:start (auto-generated, do not edit) -->
 * | Scenario | Light | Dark |
 * | --- | --- | --- |
 * | allVariants | ![](https://tanaykumarbera.github.io/compose-electric-pop/snapshots/PopButton_allVariants_light.png) | ![](https://tanaykumarbera.github.io/compose-electric-pop/snapshots/PopButton_allVariants_dark.png) |
 * <!-- screenshots:end -->
 */
@Composable
fun PopButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: PopButtonStyle = PopButtonStyle.Primary,
    size: PopButtonSize = PopButtonSize.Large,
    enabled: Boolean = true,
    icon: ImageVector? = null,
) {
    val colors = style.toColors()
    val dimensions = size.toDimensions()
    val interactionSource = remember { MutableInteractionSource() }
    val scale = kineticScale(interactionSource, enabled)

    val disabledAlpha = if (enabled) 1f else 0.38f

    val typography = when (size) {
        PopButtonSize.Small -> MaterialTheme.typography.labelMedium
        else -> MaterialTheme.typography.labelLarge
    }

    // Design Rule 3: Ghost border
    val ghostBorderModifier = if (style == PopButtonStyle.Ghost) {
        Modifier.border(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.15f),
            shape = PopShapeFull,
        )
    } else {
        Modifier
    }

    // Design Rule 4: Neon glow on Primary
    val neonGlowModifier = if (style == PopButtonStyle.Primary && enabled) {
        Modifier.shadow(
            elevation = 16.dp,
            shape = PopShapeFull,
            ambientColor = colors.containerColor.copy(alpha = 0.20f),
            spotColor = colors.containerColor.copy(alpha = 0.20f),
        )
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            // Design Rule 5: Kinetic scale
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
            )
            .then(neonGlowModifier)
            .then(ghostBorderModifier)
            .clip(PopShapeFull)
            .background(colors.containerColor.copy(alpha = disabledAlpha))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                role = Role.Button,
                onClick = onClick,
            )
            .hoverable(interactionSource)
            .height(dimensions.height)
            .padding(horizontal = dimensions.horizontalPadding),
        contentAlignment = Alignment.Center,
    ) {
        if (icon != null) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(ElectricPopTheme.spacing.xs),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(dimensions.iconSize),
                    tint = colors.contentColor.copy(alpha = disabledAlpha),
                )
                Text(
                    text = text.uppercase(),
                    color = colors.contentColor.copy(alpha = disabledAlpha),
                    style = typography,
                )
            }
        } else {
            Text(
                text = text.uppercase(),
                color = colors.contentColor.copy(alpha = disabledAlpha),
                style = typography,
            )
        }
    }
}

/**
 * An icon-only button following the Kinetic Pulse design system.
 *
 * Circular shape, consistent styling with [PopButton].
 *
 * @param icon The [ImageVector] to display.
 * @param onClick Callback when the button is clicked.
 * @param modifier Optional [Modifier] applied to the button.
 * @param contentDescription Accessibility description for the icon.
 * @param style Visual style: Primary (neon glow), Secondary, or Ghost.
 * @param enabled Whether the button is interactive.
 */
@Composable
fun PopIconButton(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    style: PopButtonStyle = PopButtonStyle.Primary,
    enabled: Boolean = true,
) {
    val colors = style.toColors()
    val interactionSource = remember { MutableInteractionSource() }
    val scale = kineticScale(interactionSource, enabled)
    val disabledAlpha = if (enabled) 1f else 0.38f

    // Design Rule 3: Ghost border
    val ghostBorderModifier = if (style == PopButtonStyle.Ghost) {
        Modifier.border(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.15f),
            shape = PopShapeFull,
        )
    } else {
        Modifier
    }

    // Design Rule 4: Neon glow on Primary
    val neonGlowModifier = if (style == PopButtonStyle.Primary && enabled) {
        Modifier.shadow(
            elevation = 16.dp,
            shape = PopShapeFull,
            ambientColor = colors.containerColor.copy(alpha = 0.20f),
            spotColor = colors.containerColor.copy(alpha = 0.20f),
        )
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
            )
            .then(neonGlowModifier)
            .then(ghostBorderModifier)
            .clip(PopShapeFull)
            .background(colors.containerColor.copy(alpha = disabledAlpha))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                role = Role.Button,
                onClick = onClick,
            )
            .hoverable(interactionSource)
            .size(48.dp),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(24.dp),
            tint = colors.contentColor.copy(alpha = disabledAlpha),
        )
    }
}
