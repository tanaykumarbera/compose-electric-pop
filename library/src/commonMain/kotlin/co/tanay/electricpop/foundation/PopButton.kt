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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.tanay.electricpop.theme.ElectricPopTheme
import co.tanay.electricpop.theme.PopShapeFull

/**
 * Button visual style.
 */
enum class PopButtonStyle {
    /** Electric Lime — primary CTA. Gets a neon glow on Primary at XL/Large sizes. */
    Primary,

    /** Neon Magenta — secondary CTA. */
    Secondary,

    /** Cyber Cyan — tertiary CTA, used for utility actions like "Export". */
    Tertiary,

    /** Transparent button with a thin ghost border for low-priority/cancel actions. */
    Ghost,
}

/**
 * Button size presets matching the Stitch "Action Buttons Doc" spec.
 *
 * Each size brings its own padding, typography, shape, and (for filled styles)
 * its own slot in the color scheme — Large uses the **base** color tokens
 * (`primary` / `secondary` / `tertiary`) for a heavier, more confident look,
 * while XL and Small use the **container** color tokens.
 */
enum class PopButtonSize {
    /** Display — extraLarge squircle, headlineSmall italic Black, 40dp/24dp padding. Container colors. */
    XL,

    /** Standard — pill shape, titleMedium italic Bold, 32dp/16dp padding. Base colors (primary/secondary/tertiary). */
    Large,

    /** Utility — pill shape, labelSmall Black with wide tracking, 20dp/8dp padding. Container colors. */
    Small,
}

/**
 * Resolved color pair for a button [PopButtonStyle] at a given [PopButtonSize].
 */
@Immutable
data class PopButtonColors(
    val containerColor: Color,
    val contentColor: Color,
)

/**
 * Resolved dimensional + typographic properties for a button at a given size.
 */
@Immutable
internal data class PopButtonDimensions(
    val horizontalPadding: Dp,
    val verticalPadding: Dp,
    val iconSize: Dp,
    val gap: Dp,
    val shape: Shape,
    val textStyle: TextStyle,
)

/**
 * Resolves a [PopButtonStyle] to theme colors, varying by [PopButtonSize].
 *
 * - Large filled styles use the **base** color tokens (`primary` / `secondary` / `tertiary`).
 * - XL and Small filled styles use the **container** color tokens.
 * - Ghost is transparent at every size; content color uses `onSurface` at XL,
 *   `onSurfaceVariant` at Large, and `outline` at Small to match the Stitch spec.
 */
@Composable
fun PopButtonStyle.toColors(size: PopButtonSize = PopButtonSize.Large): PopButtonColors {
    val scheme = MaterialTheme.colorScheme
    val useBase = size == PopButtonSize.Large
    return when (this) {
        PopButtonStyle.Primary -> if (useBase) {
            PopButtonColors(scheme.primary, scheme.onPrimary)
        } else {
            PopButtonColors(scheme.primaryContainer, scheme.onPrimaryContainer)
        }
        PopButtonStyle.Secondary -> if (useBase) {
            PopButtonColors(scheme.secondary, scheme.onSecondary)
        } else {
            PopButtonColors(scheme.secondaryContainer, scheme.onSecondaryContainer)
        }
        PopButtonStyle.Tertiary -> if (useBase) {
            PopButtonColors(scheme.tertiary, scheme.onTertiary)
        } else {
            PopButtonColors(scheme.tertiaryContainer, scheme.onTertiaryContainer)
        }
        PopButtonStyle.Ghost -> {
            val ghostContent = when (size) {
                PopButtonSize.XL -> scheme.onSurface
                PopButtonSize.Large -> scheme.onSurfaceVariant
                PopButtonSize.Small -> scheme.outline
            }
            PopButtonColors(Color.Transparent, ghostContent)
        }
    }
}

/**
 * Resolves a [PopButtonSize] to padding, icon size, gap, shape, and typography.
 *
 * Values are derived from the Stitch "Action Buttons Doc" spec:
 * - **XL:** padding(40,24), 24dp icons, 16dp gap, extraLarge squircle, headlineSmall italic Black.
 * - **Large:** padding(32,16), 20dp icons, 12dp gap, pill, titleMedium italic Bold.
 * - **Small:** padding(20,8), 14dp icons, 8dp gap, pill, labelSmall Black with +0.05em tracking.
 */
@Composable
internal fun PopButtonSize.toDimensions(): PopButtonDimensions {
    val typography = MaterialTheme.typography
    return when (this) {
        PopButtonSize.XL -> PopButtonDimensions(
            horizontalPadding = 40.dp,
            verticalPadding = 24.dp,
            iconSize = 24.dp,
            gap = ElectricPopTheme.spacing.lg,
            shape = MaterialTheme.shapes.extraLarge,
            textStyle = typography.headlineSmall.copy(
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Black,
            ),
        )
        PopButtonSize.Large -> PopButtonDimensions(
            horizontalPadding = 32.dp,
            verticalPadding = 16.dp,
            iconSize = 20.dp,
            gap = ElectricPopTheme.spacing.md,
            shape = PopShapeFull,
            textStyle = typography.titleMedium.copy(
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
            ),
        )
        PopButtonSize.Small -> PopButtonDimensions(
            horizontalPadding = 20.dp,
            verticalPadding = 8.dp,
            iconSize = 14.dp,
            gap = ElectricPopTheme.spacing.sm,
            shape = PopShapeFull,
            textStyle = typography.labelSmall.copy(
                fontWeight = FontWeight.Black,
                letterSpacing = 0.5.sp,
            ),
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
 * A styled button following the Kinetic Pulse design system, sized and shaped per
 * the Stitch "Action Buttons Doc" spec.
 *
 * Three styles ([PopButtonStyle.Primary], [Secondary][PopButtonStyle.Secondary],
 * [Tertiary][PopButtonStyle.Tertiary], [Ghost][PopButtonStyle.Ghost]) × three sizes
 * ([PopButtonSize.XL], [Large][PopButtonSize.Large], [Small][PopButtonSize.Small]).
 *
 * Color mapping varies by size:
 * - **Large** filled styles use the base color tokens (`primary`, `secondary`, `tertiary`)
 *   for a heavier, more confident look — e.g. dark olive Primary CTA.
 * - **XL** and **Small** filled styles use the container tokens (lighter, more decorative).
 *
 * Design rules enforced:
 * - **Rule 1 (No-Line):** No 1px borders on filled styles. Ghost uses Rule 3 ghost border.
 * - **Rule 4 (Neon Glow):** Primary at XL and Large gets a glow shadow tinted with its
 *   own container at ~15–20% opacity.
 * - **Rule 5 (Kinetic Interactions):** Hover scales to 1.05x, press compresses to 0.95x.
 * - **Rule 6 (Squircle Radii):** XL uses [MaterialTheme.shapes.extraLarge]; Large/Small
 *   use [PopShapeFull].
 * - **Rule 7 (Typography Impact):** Button text is uppercase. XL/Large render italic.
 *
 * @param text The button label (rendered uppercase).
 * @param onClick Callback when the button is clicked.
 * @param modifier Optional [Modifier] applied to the button.
 * @param style Visual style: Primary, Secondary, Tertiary, or Ghost.
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
    val colors = style.toColors(size)
    val dimensions = size.toDimensions()
    val interactionSource = remember { MutableInteractionSource() }
    val scale = kineticScale(interactionSource, enabled)

    val disabledAlpha = if (enabled) 1f else 0.38f

    // Design Rule 3: Ghost border at all sizes — kept consistent per project preference.
    val ghostBorderModifier = if (style == PopButtonStyle.Ghost) {
        Modifier.border(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.15f),
            shape = dimensions.shape,
        )
    } else {
        Modifier
    }

    // Design Rule 4: Neon glow on Primary at XL and Large.
    val neonGlowModifier = if (style == PopButtonStyle.Primary && enabled && size != PopButtonSize.Small) {
        val glowColor = MaterialTheme.colorScheme.primaryContainer
        val glowAlpha = if (size == PopButtonSize.XL) 0.15f else 0.20f
        val glowElevation = if (size == PopButtonSize.XL) 24.dp else 16.dp
        Modifier.shadow(
            elevation = glowElevation,
            shape = dimensions.shape,
            ambientColor = glowColor.copy(alpha = glowAlpha),
            spotColor = glowColor.copy(alpha = glowAlpha),
        )
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .then(neonGlowModifier)
            .then(ghostBorderModifier)
            .clip(dimensions.shape)
            .background(
                if (colors.containerColor == Color.Transparent) {
                    Color.Transparent
                } else {
                    colors.containerColor.copy(alpha = disabledAlpha)
                },
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                role = Role.Button,
                onClick = onClick,
            )
            .hoverable(interactionSource)
            .padding(
                horizontal = dimensions.horizontalPadding,
                vertical = dimensions.verticalPadding,
            ),
        contentAlignment = Alignment.Center,
    ) {
        if (icon != null) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(dimensions.gap),
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
                    style = dimensions.textStyle,
                    maxLines = 1,
                    softWrap = false,
                )
            }
        } else {
            Text(
                text = text.uppercase(),
                color = colors.contentColor.copy(alpha = disabledAlpha),
                style = dimensions.textStyle,
                maxLines = 1,
                softWrap = false,
            )
        }
    }
}

/**
 * An icon-only button following the Kinetic Pulse design system.
 *
 * Circular shape, consistent styling with [PopButton]. Uses the Large color mapping
 * (base color tokens) for filled styles since icon buttons are typically used for
 * primary actions.
 *
 * @param icon The [ImageVector] to display.
 * @param onClick Callback when the button is clicked.
 * @param modifier Optional [Modifier] applied to the button.
 * @param contentDescription Accessibility description for the icon.
 * @param style Visual style: Primary, Secondary, Tertiary, or Ghost.
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
    val colors = style.toColors(PopButtonSize.Large)
    val interactionSource = remember { MutableInteractionSource() }
    val scale = kineticScale(interactionSource, enabled)
    val disabledAlpha = if (enabled) 1f else 0.38f

    val ghostBorderModifier = if (style == PopButtonStyle.Ghost) {
        Modifier.border(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.15f),
            shape = PopShapeFull,
        )
    } else {
        Modifier
    }

    val neonGlowModifier = if (style == PopButtonStyle.Primary && enabled) {
        val glowColor = MaterialTheme.colorScheme.primaryContainer
        Modifier.shadow(
            elevation = 16.dp,
            shape = PopShapeFull,
            ambientColor = glowColor.copy(alpha = 0.20f),
            spotColor = glowColor.copy(alpha = 0.20f),
        )
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .then(neonGlowModifier)
            .then(ghostBorderModifier)
            .clip(PopShapeFull)
            .background(
                if (colors.containerColor == Color.Transparent) {
                    Color.Transparent
                } else {
                    colors.containerColor.copy(alpha = disabledAlpha)
                },
            )
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
