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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
 * On/off toggle switch following the Kinetic Pulse design system.
 *
 * Design rules enforced:
 * - **Rule 1 (No-Line):** No border on track; tonal contrast between [surfaceContainerHigh] (off)
 *   and [secondaryContainer] (on).
 * - **Rule 5 (Kinetic Interactions):** Hover scales to 1.05x, press compresses to 0.95x, 200ms
 *   ease. Thumb position animates with tween(200ms, EaseInOut).
 * - **Rule 6 (Squircle Radii):** Track and thumb use [PopShapeFull] (pill/circle shape).
 *
 * @param checked Current on/off state.
 * @param onCheckedChange Callback when toggled; pass `null` to make the switch non-interactive.
 * @param modifier Optional [Modifier] applied to the root layout.
 * @param enabled Whether the switch is interactive. Disabled switches reduce opacity to 38%.
 * @param label Optional text label displayed to the left of the switch.
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
) {
    val interactionSource = remember { MutableInteractionSource() }
    val scale = kineticScale(interactionSource, enabled)
    val disabledAlpha = if (enabled) 1f else 0.38f

    // Animated track background — surfaceContainerHigh (off) → secondaryContainer (on)
    val trackColor by animateColorAsState(
        targetValue = if (checked) {
            MaterialTheme.colorScheme.secondaryContainer.copy(alpha = disabledAlpha)
        } else {
            MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = disabledAlpha)
        },
        animationSpec = tween(durationMillis = 200, easing = EaseInOut),
    )

    // Animated thumb color — onSurfaceVariant (off) → onSecondaryContainer (on)
    val thumbColor by animateColorAsState(
        targetValue = if (checked) {
            MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = disabledAlpha)
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = disabledAlpha)
        },
        animationSpec = tween(durationMillis = 200, easing = EaseInOut),
    )

    // Animated horizontal position of thumb — kinetic transition (Rule 5)
    val thumbOffsetX by animateDpAsState(
        targetValue = if (checked) THUMB_OFFSET_CHECKED else THUMB_OFFSET_UNCHECKED,
        animationSpec = tween(durationMillis = 200, easing = EaseInOut),
    )

    // Vertical centering of thumb inside track
    val thumbOffsetY = (TRACK_HEIGHT - THUMB_DIAMETER) / 2

    // Build the interactive modifier — toggleable + hoverable only when onCheckedChange is provided
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
            // Rule 5: kinetic scale applied to the whole switch (including optional label)
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .then(interactiveModifier),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Optional label placed before the switch
        if (label != null) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = disabledAlpha),
                modifier = Modifier.padding(end = ElectricPopTheme.spacing.sm),
            )
        }

        // Track — Rule 1: no border, tonal contrast only
        Box(
            modifier = Modifier
                .size(width = TRACK_WIDTH, height = TRACK_HEIGHT)
                .clip(PopShapeFull)
                .background(trackColor),
        ) {
            // Thumb — circular, animates horizontally
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
