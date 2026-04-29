package co.tanay.electricpop.foundation

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import co.tanay.electricpop.theme.ElectricPopTheme
import co.tanay.electricpop.theme.PopShapeFull
import kotlin.math.roundToInt

// Slider dimensions
private val TRACK_HEIGHT = 8.dp
private val THUMB_DIAMETER = 24.dp
private val THUMB_BORDER_WIDTH = 4.dp

/**
 * Range slider with value display following the Kinetic Pulse design system.
 *
 * Design rules enforced:
 * - **Rule 1 (No-Line):** No 1px borders on track; tonal contrast between active (primary)
 *   and inactive (surfaceContainerHigh) portions.
 * - **Rule 4 (Neon Glow):** Thumb gets a subtle glow from primaryContainer at ~15% opacity.
 * - **Rule 5 (Kinetic Interactions):** Thumb scales to 1.15x on drag, 1.05x on hover.
 * - **Rule 6 (Squircle Radii):** Track and thumb use [PopShapeFull] (pill shape).
 *
 * @param value Current slider value.
 * @param onValueChange Callback when the value changes during drag.
 * @param modifier Optional [Modifier] applied to the root layout.
 * @param enabled Whether the slider is interactive. Disabled sliders reduce opacity to 38%.
 * @param valueRange The range of values this slider can represent.
 * @param steps Number of discrete steps. 0 means continuous.
 * @param label Optional text label displayed above the slider.
 * @param showValue Whether to show the current value display above the thumb.
 *
 * <!-- screenshots:start (auto-generated, do not edit) -->
 * | Scenario | Light | Dark |
 * | --- | --- | --- |
 * | allVariants | ![](https://tanaykumarbera.github.io/compose-electric-pop/snapshots/PopSlider_allVariants_light.png) | ![](https://tanaykumarbera.github.io/compose-electric-pop/snapshots/PopSlider_allVariants_dark.png) |
 * <!-- screenshots:end -->
 */
@Composable
fun PopSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    label: String? = null,
    showValue: Boolean = false,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    var isDragging by remember { mutableStateOf(false) }
    val disabledAlpha = if (enabled) 1f else 0.38f

    // Kinetic scale for thumb — Rule 5
    val thumbScale by animateFloatAsState(
        targetValue = when {
            !enabled -> 1.0f
            isDragging -> 1.15f
            isHovered -> 1.05f
            else -> 1.0f
        },
        animationSpec = tween(durationMillis = 200, easing = EaseInOut),
    )

    // Normalize value to 0..1 fraction
    val range = valueRange.endInclusive - valueRange.start
    val fraction = if (range > 0f) {
        ((value - valueRange.start) / range).coerceIn(0f, 1f)
    } else {
        0f
    }

    // Snap to steps helper
    val snapToStep: (Float) -> Float = { rawFraction ->
        if (steps > 0) {
            val stepFraction = 1f / (steps + 1)
            val snapped = (rawFraction / stepFraction).roundToInt() * stepFraction
            snapped.coerceIn(0f, 1f)
        } else {
            rawFraction.coerceIn(0f, 1f)
        }
    }

    val density = LocalDensity.current
    var trackWidthPx by remember { mutableStateOf(0f) }
    val thumbRadiusPx = with(density) { THUMB_DIAMETER.toPx() / 2f }

    Column(
        modifier = modifier
            .graphicsLayer(alpha = disabledAlpha),
    ) {
        // Optional label and value display row
        if (label != null || showValue) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = ElectricPopTheme.spacing.xs),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (label != null) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
                if (showValue) {
                    Text(
                        text = formatSliderValue(value),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }

        // Track + thumb area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(THUMB_DIAMETER + 8.dp) // Extra space for thumb overflow
                .onSizeChanged { size ->
                    trackWidthPx = size.width.toFloat()
                }
                .hoverable(interactionSource, enabled = enabled)
                .then(
                    if (enabled) {
                        Modifier
                            .pointerInput(valueRange, steps) {
                                detectTapGestures { offset ->
                                    val usableWidth = trackWidthPx - thumbRadiusPx * 2
                                    if (usableWidth > 0f) {
                                        val rawFraction = ((offset.x - thumbRadiusPx) / usableWidth).coerceIn(0f, 1f)
                                        val snappedFraction = snapToStep(rawFraction)
                                        val newValue = valueRange.start + snappedFraction * range
                                        onValueChange(newValue)
                                    }
                                }
                            }
                            .pointerInput(valueRange, steps) {
                                detectHorizontalDragGestures(
                                    onDragStart = { isDragging = true },
                                    onDragEnd = { isDragging = false },
                                    onDragCancel = { isDragging = false },
                                    onHorizontalDrag = { change, _ ->
                                        val usableWidth = trackWidthPx - thumbRadiusPx * 2
                                        if (usableWidth > 0f) {
                                            val rawFraction = ((change.position.x - thumbRadiusPx) / usableWidth).coerceIn(0f, 1f)
                                            val snappedFraction = snapToStep(rawFraction)
                                            val newValue = valueRange.start + snappedFraction * range
                                            onValueChange(newValue)
                                        }
                                    },
                                )
                            }
                    } else {
                        Modifier
                    },
                ),
            contentAlignment = Alignment.CenterStart,
        ) {
            // Inactive track background — Rule 1: no border, tonal surface
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(TRACK_HEIGHT)
                    .clip(PopShapeFull)
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh),
            )

            // Active track fill
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = fraction.coerceAtLeast(0.01f))
                    .height(TRACK_HEIGHT)
                    .clip(PopShapeFull)
                    .background(MaterialTheme.colorScheme.primary),
            )

            // Thumb — 24px, primary_container border, kinetic scale
            val thumbOffsetPx = if (trackWidthPx > 0f) {
                val usableWidth = trackWidthPx - thumbRadiusPx * 2
                (fraction * usableWidth).roundToInt()
            } else {
                0
            }

            Box(
                modifier = Modifier
                    .offset { IntOffset(x = thumbOffsetPx, y = 0) }
                    .graphicsLayer(scaleX = thumbScale, scaleY = thumbScale)
                    // Rule 4: Neon glow on thumb
                    .shadow(
                        elevation = 8.dp,
                        shape = PopShapeFull,
                        ambientColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f),
                        spotColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f),
                    )
                    .size(THUMB_DIAMETER)
                    .clip(PopShapeFull)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(THUMB_BORDER_WIDTH)
                    .clip(PopShapeFull)
                    .background(MaterialTheme.colorScheme.primary),
            )
        }
    }
}

/**
 * Formats a slider value for display.
 * Shows integers without decimals, floats with up to 1 decimal place.
 */
internal fun formatSliderValue(value: Float): String = if (value == value.toInt().toFloat()) {
    value.toInt().toString()
} else {
    val rounded = (value * 10).roundToInt() / 10f
    if (rounded == rounded.toInt().toFloat()) {
        rounded.toInt().toString()
    } else {
        rounded.toString()
    }
}
