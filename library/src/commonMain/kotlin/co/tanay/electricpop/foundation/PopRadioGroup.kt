package co.tanay.electricpop.foundation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import co.tanay.electricpop.theme.ElectricPopTheme

// Radio indicator dimensions
private val RADIO_OUTER_SIZE = 22.dp
private val RADIO_INNER_SIZE = 12.dp
private val RADIO_STROKE_WIDTH = 2.dp

/**
 * Radio group with tonal shift selection following the Kinetic Pulse design system.
 *
 * Design rules enforced:
 * - **Rule 1 (No-Line):** No dividers between options. Selected vs unselected distinguished
 *   by tonal surface shifts ([primaryContainer] vs [surfaceContainerLow]).
 * - **Rule 3 (Ghost Border):** Radio circle outline uses [outlineVariant] at 15% opacity
 *   for unselected state.
 * - **Rule 5 (Kinetic Interactions):** Hover scales option to 1.05x, press to 0.95x.
 * - **Rule 6 (Squircle Radii):** Each option row uses squircle shape from theme.
 *
 * @param options List of option labels to display.
 * @param selectedIndex Index of the currently selected option, or -1 for none.
 * @param onSelectedChange Callback invoked with the index of the newly selected option.
 * @param modifier Optional [Modifier] applied to the root column.
 * @param enabled Whether the radio group is interactive. Disabled reduces opacity to 38%.
 */
@Composable
fun PopRadioGroup(
    options: List<String>,
    selectedIndex: Int,
    onSelectedChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val spacing = ElectricPopTheme.spacing
    val disabledAlpha = if (enabled) 1f else 0.38f

    Column(
        modifier = modifier.selectableGroup(),
        verticalArrangement = Arrangement.spacedBy(spacing.xs),
    ) {
        options.forEachIndexed { index, option ->
            PopRadioOption(
                label = option,
                selected = index == selectedIndex,
                onClick = { onSelectedChange(index) },
                enabled = enabled,
                disabledAlpha = disabledAlpha,
            )
        }
    }
}

/**
 * A single radio option row within a [PopRadioGroup].
 */
@Composable
private fun PopRadioOption(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    enabled: Boolean,
    disabledAlpha: Float,
) {
    val spacing = ElectricPopTheme.spacing
    val interactionSource = remember { MutableInteractionSource() }
    val scale = kineticScale(interactionSource, enabled)

    // Animated background — primaryContainer (selected) vs surfaceContainerLow (unselected)
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = disabledAlpha)
        } else {
            MaterialTheme.colorScheme.surfaceContainerLow.copy(alpha = disabledAlpha)
        },
        animationSpec = tween(durationMillis = 200, easing = EaseInOut),
    )

    // Animated text color
    val textColor by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = disabledAlpha)
        } else {
            MaterialTheme.colorScheme.onSurface.copy(alpha = disabledAlpha)
        },
        animationSpec = tween(durationMillis = 200, easing = EaseInOut),
    )

    // Radio indicator colors
    // Both ring and dot use onPrimaryContainer — dark ink on lime row background.
    // Cannot use primary for the ring: in dark theme primary==primaryContainer (both #CAFD00),
    // making the ring invisible against the lime background. onPrimaryContainer is guaranteed
    // contrast on primaryContainer in both themes. The lime gap between ring and dot shows through.
    val ringColor = if (selected) {
        MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = disabledAlpha)
    } else {
        MaterialTheme.colorScheme.outline.copy(alpha = disabledAlpha)
    }
    val dotColor = if (selected) {
        MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = disabledAlpha)
    } else {
        Color.Transparent
    }

    Row(
        modifier = Modifier
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .clip(MaterialTheme.shapes.small)
            .background(backgroundColor)
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.RadioButton,
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
            )
            .hoverable(interactionSource)
            .padding(horizontal = spacing.md, vertical = spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacing.sm),
    ) {
        // Custom radio indicator — ghost border circle with filled inner when selected
        RadioIndicator(
            selected = selected,
            ringColor = ringColor,
            dotColor = dotColor,
        )

        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = textColor,
        )
    }
}

/**
 * Custom radio circle indicator drawn via Canvas.
 * Uses ghost border (Rule 3) for the outer ring on unselected state.
 */
@Composable
private fun RadioIndicator(
    selected: Boolean,
    ringColor: Color,
    dotColor: Color,
) {
    val strokeWidth = RADIO_STROKE_WIDTH
    Box(
        modifier = Modifier
            .size(RADIO_OUTER_SIZE)
            .drawBehind {
                // Outer ring
                drawCircle(
                    color = ringColor,
                    radius = size.minDimension / 2 - strokeWidth.toPx() / 2,
                    style = Stroke(width = strokeWidth.toPx()),
                )
                // Inner filled circle when selected
                if (selected) {
                    drawCircle(
                        color = dotColor,
                        radius = RADIO_INNER_SIZE.toPx() / 2,
                    )
                }
            },
    )
}
