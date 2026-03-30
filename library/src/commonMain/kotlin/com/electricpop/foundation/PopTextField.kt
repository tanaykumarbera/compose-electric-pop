package com.electricpop.foundation

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.electricpop.theme.ElectricPopTheme

/**
 * A styled text input field following the Kinetic Pulse design system.
 *
 * Features a label-above layout, animated left accent bar on focus, optional leading/trailing
 * icons, and a distinct error state.
 *
 * Design rules enforced:
 * - **Rule 1 (No-Line):** No bottom border. Surface tonal shift communicates focus.
 * - **Rule 2 (Tonal Shadows):** Tonal shadow applied when focused (8dp elevation with surface color).
 * - **Rule 3 (Ghost Border):** `outlineVariant` at 15% opacity, always visible for accessibility.
 * - **Rule 6 (Squircle Radii):** Container uses `MaterialTheme.shapes.small` (SquircleShape 35%).
 *
 * @param value Current text value.
 * @param onValueChange Callback when text changes.
 * @param modifier Optional [Modifier] applied to the outer column.
 * @param label Label text displayed above the field.
 * @param placeholder Placeholder text shown when the field is empty.
 * @param isError Whether to display error styling.
 * @param errorMessage Error message displayed below the field when [isError] is true.
 * @param enabled Whether the field is interactive. Disabled fields reduce opacity to 38%.
 * @param readOnly Whether the field is read-only.
 * @param singleLine Whether input is constrained to a single line.
 * @param leadingIcon Optional leading icon displayed before the text.
 * @param trailingIcon Optional trailing icon displayed after the text.
 * @param onTrailingIconClick Click handler for the trailing icon.
 * @param keyboardOptions Keyboard options for the input.
 * @param keyboardActions Keyboard actions for the input.
 * @param visualTransformation Visual transformation applied to the input (e.g. password masking).
 */
@Composable
fun PopTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    val spacing = ElectricPopTheme.spacing
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val scheme = MaterialTheme.colorScheme
    val shape = MaterialTheme.shapes.small

    // Rule 1: Background shifts between surfaceContainerLow (unfocused) and surfaceContainer (focused)
    val backgroundColor = if (isFocused) scheme.surfaceContainer else scheme.surfaceContainerLow

    // Left accent bar: animated alpha — shows when focused OR when in error state
    val accentAlpha by animateFloatAsState(
        targetValue = if (isFocused || isError) 1f else 0f,
        animationSpec = tween(durationMillis = 200, easing = EaseInOut),
        label = "accentBarAlpha",
    )
    val accentColor = if (isError) scheme.error else scheme.primary

    val labelColor = if (isError) scheme.error else scheme.onSurface
    val disabledAlpha = if (enabled) 1f else 0.38f

    // Rule 2: Tonal shadow on focused container
    val shadowMod = if (isFocused) {
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
        // Label above field (Rule 7 N/A — label is not a headline)
        if (label != null) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = labelColor,
                modifier = Modifier.padding(bottom = spacing.xs),
            )
        }

        // Container: Rule 6 squircle, Rule 3 ghost border, Rule 1 tonal background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .then(shadowMod)
                .clip(shape)
                .background(backgroundColor)
                // Rule 3: Ghost border at 15% opacity for accessibility
                .border(
                    width = 1.dp,
                    color = scheme.outlineVariant.copy(alpha = 0.15f),
                    shape = shape,
                ),
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                enabled = enabled,
                readOnly = readOnly,
                singleLine = singleLine,
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                visualTransformation = visualTransformation,
                interactionSource = interactionSource,
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = scheme.onSurface,
                ),
                cursorBrush = SolidColor(scheme.primary),
                decorationBox = { innerTextField ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min)
                            .padding(
                                // Leave space at start for the accent bar (3.dp) + gap (xs)
                                start = 3.dp + spacing.xs,
                                end = spacing.md,
                                top = spacing.sm,
                                bottom = spacing.sm,
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(spacing.xs),
                    ) {
                        // Leading icon
                        if (leadingIcon != null) {
                            Icon(
                                imageVector = leadingIcon,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = scheme.onSurfaceVariant,
                            )
                        }

                        // Input area with placeholder
                        Box(modifier = Modifier.weight(1f)) {
                            if (value.isEmpty() && placeholder != null) {
                                Text(
                                    text = placeholder,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = scheme.onSurfaceVariant,
                                )
                            }
                            innerTextField()
                        }

                        // Trailing icon
                        if (trailingIcon != null) {
                            Icon(
                                imageVector = trailingIcon,
                                contentDescription = null,
                                modifier = if (onTrailingIconClick != null) {
                                    Modifier.size(20.dp).clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null,
                                        onClick = onTrailingIconClick,
                                    )
                                } else {
                                    Modifier.size(20.dp)
                                },
                                tint = scheme.onSurfaceVariant,
                            )
                        }
                    }
                },
            )

            // Left accent bar overlay — animated fade on focus/error
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .fillMaxHeight()
                    .width(3.dp)
                    .graphicsLayer(alpha = accentAlpha)
                    .background(accentColor),
            )
        }

        // Error message below field
        if (isError && errorMessage != null) {
            Spacer(Modifier.height(spacing.xxs))
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodySmall,
                color = scheme.error,
            )
        }
    }
}

/**
 * A password input field with a built-in visibility toggle.
 *
 * Wraps [PopTextField] and manages the `passwordVisible` state internally.
 * The trailing icon toggles between [PopIcons.Close] (hidden) and [PopIcons.Check] (visible).
 *
 * @param value Current text value.
 * @param onValueChange Callback when text changes.
 * @param modifier Optional [Modifier] applied to the outer column.
 * @param label Label text displayed above the field.
 * @param placeholder Placeholder text shown when the field is empty.
 * @param isError Whether to display error styling.
 * @param errorMessage Error message displayed below the field when [isError] is true.
 * @param enabled Whether the field is interactive.
 */
@Composable
fun PopPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    enabled: Boolean = true,
) {
    var passwordVisible by remember { mutableStateOf(false) }
    // Load both icons unconditionally (composable calls must not be conditional)
    val checkIcon = PopIcons.Check
    val closeIcon = PopIcons.Close

    PopTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = label,
        placeholder = placeholder,
        isError = isError,
        errorMessage = errorMessage,
        enabled = enabled,
        trailingIcon = if (passwordVisible) checkIcon else closeIcon,
        onTrailingIconClick = { passwordVisible = !passwordVisible },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = if (passwordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
    )
}
