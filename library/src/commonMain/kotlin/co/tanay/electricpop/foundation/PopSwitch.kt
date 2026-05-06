package co.tanay.electricpop.foundation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import co.tanay.electricpop.theme.ElectricPopTheme
import co.tanay.electricpop.theme.PopShapeFull

// Track and thumb dimensions — defined as file-level constants, not hardcoded in composable body
private val TRACK_WIDTH = 52.dp
private val TRACK_HEIGHT = 32.dp
private val THUMB_DIAMETER = 24.dp
private val THUMB_OFFSET_UNCHECKED = 4.dp
private val THUMB_OFFSET_CHECKED = 24.dp // = TRACK_WIDTH - THUMB_DIAMETER - THUMB_OFFSET_UNCHECKED

/**
 * Color presets for [PopSwitch]. Drives the checked-state track and thumb colors;
 * unchecked-state colors stay neutral across all presets.
 *
 * Each preset maps to the corresponding `*Container` / `on*Container` token pair from
 * [MaterialTheme.colorScheme].
 */
enum class PopSwitchColor {
    /** Electric Lime — primaryContainer / onPrimaryContainer */
    Primary,

    /** Neon Magenta — secondaryContainer / onSecondaryContainer (default) */
    Secondary,

    /** Cyber Cyan — tertiaryContainer / onTertiaryContainer */
    Tertiary,
}

/**
 * Resolved color slots for a [PopSwitch].
 */
@Immutable
data class PopSwitchColors(
    val checkedTrackColor: Color,
    val checkedThumbColor: Color,
    val uncheckedTrackColor: Color,
    val uncheckedThumbColor: Color,
)

/**
 * Resolves a [PopSwitchColor] preset to actual theme colors.
 */
@Composable
fun PopSwitchColor.toColors(): PopSwitchColors {
    val scheme = MaterialTheme.colorScheme
    val uncheckedTrack = scheme.surfaceContainerHigh
    val uncheckedThumb = scheme.onSurfaceVariant
    return when (this) {
        PopSwitchColor.Primary -> PopSwitchColors(
            checkedTrackColor = scheme.primaryContainer,
            checkedThumbColor = scheme.onPrimaryContainer,
            uncheckedTrackColor = uncheckedTrack,
            uncheckedThumbColor = uncheckedThumb,
        )
        PopSwitchColor.Secondary -> PopSwitchColors(
            checkedTrackColor = scheme.secondaryContainer,
            checkedThumbColor = scheme.onSecondaryContainer,
            uncheckedTrackColor = uncheckedTrack,
            uncheckedThumbColor = uncheckedThumb,
        )
        PopSwitchColor.Tertiary -> PopSwitchColors(
            checkedTrackColor = scheme.tertiaryContainer,
            checkedThumbColor = scheme.onTertiaryContainer,
            uncheckedTrackColor = uncheckedTrack,
            uncheckedThumbColor = uncheckedThumb,
        )
    }
}

/**
 * On/off toggle switch following the Kinetic Pulse design system.
 *
 * Design rules enforced:
 * - **Rule 1 (No-Line):** No border on track; tonal contrast between [surfaceContainerHigh] (off)
 *   and the chosen container color (on).
 * - **Rule 5 (Kinetic Interactions):** Hover scales to 1.05x, press compresses to 0.95x, 200ms
 *   ease. Thumb position animates with tween(200ms, EaseInOut).
 * - **Rule 6 (Squircle Radii):** Track and thumb use [PopShapeFull] (pill/circle shape).
 *
 * @param checked Current on/off state.
 * @param onCheckedChange Callback when toggled; pass `null` to make the switch non-interactive.
 * @param modifier Optional [Modifier] applied to the root layout.
 * @param enabled Whether the switch is interactive. Disabled switches reduce opacity to 38%.
 * @param label Optional text label displayed to the left of the switch.
 * @param color Color preset for the on-state; defaults to [PopSwitchColor.Secondary] (Neon Magenta)
 *   for back-compat. Use [PopSwitchColor.Primary] for Electric Lime, [PopSwitchColor.Tertiary] for
 *   Cyber Cyan.
 *
 * <!-- screenshots:start (auto-generated, do not edit) -->
 * | Scenario | Light | Dark |
 * | --- | --- | --- |
 * | allVariants | ![](https://tanaykumarbera.github.io/compose-electric-pop/snapshots/PopSwitch_allVariants_light.png) | ![](https://tanaykumarbera.github.io/compose-electric-pop/snapshots/PopSwitch_allVariants_dark.png) |
 * <!-- screenshots:end -->
 */
@Composable
fun PopSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String? = null,
    color: PopSwitchColor = PopSwitchColor.Secondary,
) {
    val colors = color.toColors()
    PopSwitch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        label = label,
        checkedTrackColor = colors.checkedTrackColor,
        checkedThumbColor = colors.checkedThumbColor,
        uncheckedTrackColor = colors.uncheckedTrackColor,
        uncheckedThumbColor = colors.uncheckedThumbColor,
    )
}

/**
 * On/off toggle switch with custom track and thumb colors.
 *
 * This overload allows specifying arbitrary colors instead of a [PopSwitchColor] preset.
 *
 * @param checked Current on/off state.
 * @param onCheckedChange Callback when toggled; pass `null` to make the switch non-interactive.
 * @param modifier Optional [Modifier] applied to the root layout.
 * @param enabled Whether the switch is interactive. Disabled switches reduce opacity to 38%.
 * @param label Optional text label displayed to the left of the switch.
 * @param checkedTrackColor Track background when [checked] is true.
 * @param checkedThumbColor Thumb fill when [checked] is true.
 * @param uncheckedTrackColor Track background when [checked] is false.
 * @param uncheckedThumbColor Thumb fill when [checked] is false.
 */
@Composable
fun PopSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String? = null,
    checkedTrackColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    checkedThumbColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
    uncheckedTrackColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
    uncheckedThumbColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val scale = kineticScale(interactionSource, enabled)
    val disabledAlpha = if (enabled) 1f else 0.38f

    val trackColor by animateColorAsState(
        targetValue = if (checked) {
            checkedTrackColor.copy(alpha = disabledAlpha)
        } else {
            uncheckedTrackColor.copy(alpha = disabledAlpha)
        },
        animationSpec = tween(durationMillis = 200, easing = EaseInOut),
    )

    val thumbColor by animateColorAsState(
        targetValue = if (checked) {
            checkedThumbColor.copy(alpha = disabledAlpha)
        } else {
            uncheckedThumbColor.copy(alpha = disabledAlpha)
        },
        animationSpec = tween(durationMillis = 200, easing = EaseInOut),
    )

    val thumbOffsetX by animateDpAsState(
        targetValue = if (checked) THUMB_OFFSET_CHECKED else THUMB_OFFSET_UNCHECKED,
        animationSpec = tween(durationMillis = 200, easing = EaseInOut),
    )

    val thumbOffsetY = (TRACK_HEIGHT - THUMB_DIAMETER) / 2

    val interactiveModifier = if (onCheckedChange != null) {
        Modifier
            .toggleable(
                value = checked,
                onValueChange = onCheckedChange,
                role = Role.Switch,
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
            )
            .hoverable(interactionSource)
    } else {
        Modifier
    }

    Row(
        modifier = modifier
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .then(interactiveModifier),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (label != null) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = disabledAlpha),
                modifier = Modifier.padding(end = ElectricPopTheme.spacing.sm),
            )
        }

        Box(
            modifier = Modifier
                .size(width = TRACK_WIDTH, height = TRACK_HEIGHT)
                .clip(PopShapeFull)
                .background(trackColor),
        ) {
            Box(
                modifier = Modifier
                    .size(THUMB_DIAMETER)
                    .offset(x = thumbOffsetX, y = thumbOffsetY)
                    .clip(PopShapeFull)
                    .background(thumbColor),
            )
        }
    }
}
