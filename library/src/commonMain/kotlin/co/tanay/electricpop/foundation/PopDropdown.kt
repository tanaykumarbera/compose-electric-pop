package co.tanay.electricpop.foundation

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import co.tanay.electricpop.theme.ElectricPopTheme

/**
 * A selector dropdown with label, selected value display, expand/collapse icon, and popup menu.
 *
 * String convenience overload — internally delegates to the generic [PopDropdown].
 *
 * Design rules enforced:
 * - **Rule 1 (No-Line):** Background shifts from surfaceContainerLow (collapsed) to surfaceContainer (expanded).
 * - **Rule 2 (Tonal Shadows):** Tonal shadow applied when expanded (8dp, onSurface 10%).
 * - **Rule 3 (Ghost Border):** outlineVariant at 15% opacity for accessibility.
 * - **Rule 5 (Kinetic Interactions):** Hover 1.05x, Press 0.95x, 200ms ease.
 * - **Rule 6 (Squircle Radii):** MaterialTheme.shapes.small (squircle 35%).
 *
 * @param options List of string options to display.
 * @param selectedOption Currently selected option value.
 * @param onOptionSelected Callback when the user selects an option.
 * @param modifier Optional [Modifier] applied to the outer column.
 * @param label Optional label displayed above the dropdown.
 * @param placeholder Placeholder text shown when nothing is selected.
 * @param enabled Whether the dropdown is interactive.
 */
@Composable
fun PopDropdown(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    enabled: Boolean = true,
) {
    PopDropdown(
        options = options,
        selectedOption = selectedOption.ifEmpty { null },
        onOptionSelected = onOptionSelected,
        modifier = modifier,
        label = label,
        placeholder = placeholder,
        enabled = enabled,
        optionLabel = { it },
    )
}

/**
 * A generic selector dropdown with label, selected value display, expand/collapse icon, and popup menu.
 *
 * Design rules enforced:
 * - **Rule 1 (No-Line):** Background shifts from surfaceContainerLow (collapsed) to surfaceContainer (expanded).
 * - **Rule 2 (Tonal Shadows):** Tonal shadow applied when expanded (8dp, onSurface 10%).
 * - **Rule 3 (Ghost Border):** outlineVariant at 15% opacity for accessibility.
 * - **Rule 5 (Kinetic Interactions):** Hover 1.05x, Press 0.95x, 200ms ease.
 * - **Rule 6 (Squircle Radii):** MaterialTheme.shapes.small (squircle 35%).
 *
 * @param options List of options to display.
 * @param selectedOption Currently selected option, or null if nothing is selected.
 * @param onOptionSelected Callback when the user selects an option.
 * @param modifier Optional [Modifier] applied to the outer column.
 * @param label Optional label displayed above the dropdown.
 * @param placeholder Placeholder text shown when [selectedOption] is null.
 * @param enabled Whether the dropdown is interactive.
 * @param optionLabel Function to convert an option to a display string.
 */
@Composable
fun <T> PopDropdown(
    options: List<T>,
    selectedOption: T?,
    onOptionSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    enabled: Boolean = true,
    optionLabel: (T) -> String = { it.toString() },
    initialExpanded: Boolean = false,
    showMenuOnExpand: Boolean = true,
) {
    val spacing = ElectricPopTheme.spacing
    val scheme = MaterialTheme.colorScheme
    val shape = MaterialTheme.shapes.small

    var expanded by remember { mutableStateOf(initialExpanded) }
    val interactionSource = remember { MutableInteractionSource() }
    val scale = kineticScale(interactionSource, enabled)

    val disabledAlpha = if (enabled) 1f else 0.38f

    // Background: surfaceContainerLow collapsed, surfaceContainer expanded (Rule 1)
    val backgroundColor = if (expanded) scheme.surfaceContainer else scheme.surfaceContainerLow

    // Left accent bar animated alpha (0 → 1 when expanded)
    val accentAlpha by animateFloatAsState(
        targetValue = if (expanded) 1f else 0f,
        animationSpec = tween(durationMillis = 200, easing = EaseInOut),
        label = "accentBarAlpha",
    )

    // Trailing icon rotation: 0° collapsed, 180° expanded
    val iconRotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec = tween(durationMillis = 200, easing = EaseInOut),
        label = "arrowRotation",
    )

    // Rule 2: Tonal shadow when expanded
    val shadowMod = if (expanded) {
        Modifier.shadow(
            elevation = 8.dp,
            shape = shape,
            ambientColor = scheme.onSurface.copy(alpha = 0.10f),
            spotColor = scheme.onSurface.copy(alpha = 0.10f),
        )
    } else {
        Modifier
    }

    Column(modifier = modifier.graphicsLayer(alpha = disabledAlpha)) {
        // Label above dropdown
        if (label != null) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = scheme.onSurface,
                modifier = Modifier.padding(bottom = spacing.xs),
            )
        }

        Box {
            // Selector row — Rule 5 kinetic scale applied here
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .graphicsLayer(scaleX = scale, scaleY = scale)
                    .then(shadowMod)
                    .clip(shape)
                    .background(backgroundColor)
                    // Rule 3: Ghost border at 15% opacity
                    .border(
                        width = 1.dp,
                        color = scheme.outlineVariant.copy(alpha = 0.15f),
                        shape = shape,
                    )
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        enabled = enabled,
                        role = Role.DropdownList,
                        onClick = { expanded = !expanded },
                    )
                    .hoverable(interactionSource),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                        .padding(
                            start = 3.dp + spacing.xs,
                            end = spacing.md,
                            top = spacing.sm,
                            bottom = spacing.sm,
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(spacing.xs),
                ) {
                    // Selected value or placeholder
                    val displayText = selectedOption?.let { optionLabel(it) }
                    Box(modifier = Modifier.weight(1f)) {
                        if (displayText != null) {
                            Text(
                                text = displayText,
                                style = MaterialTheme.typography.bodyLarge,
                                color = scheme.primary,
                            )
                        } else if (placeholder != null) {
                            Text(
                                text = placeholder,
                                style = MaterialTheme.typography.bodyLarge,
                                color = scheme.onSurfaceVariant,
                            )
                        }
                    }

                    // Trailing arrow icon — rotates 180° when expanded
                    Icon(
                        imageVector = PopIcons.ArrowDown,
                        contentDescription = if (expanded) "Collapse" else "Expand",
                        modifier = Modifier
                            .size(20.dp)
                            .graphicsLayer(rotationZ = iconRotation),
                        tint = scheme.onSurfaceVariant,
                    )
                }

                // Left accent bar overlay — animated fade on expand
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .fillMaxHeight()
                        .width(3.dp)
                        .graphicsLayer(alpha = accentAlpha)
                        .background(scheme.primary),
                )
            }

            // Dropdown menu — suppressed when showMenuOnExpand=false (e.g. screenshot tests)
            DropdownMenu(
                expanded = expanded && showMenuOnExpand,
                onDismissRequest = { expanded = false },
            ) {
                options.forEach { option ->
                    val isSelected = option == selectedOption
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = optionLabel(option),
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (isSelected) scheme.primary else scheme.onSurface,
                            )
                        },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        },
                        trailingIcon = if (isSelected) {
                            {
                                Icon(
                                    imageVector = PopIcons.Check,
                                    contentDescription = "Selected",
                                    modifier = Modifier.size(16.dp),
                                    tint = scheme.primary,
                                )
                            }
                        } else null,
                    )
                }
            }
        }
    }
}
