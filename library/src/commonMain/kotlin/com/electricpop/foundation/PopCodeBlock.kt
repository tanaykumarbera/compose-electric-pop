package com.electricpop.foundation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.electricpop.theme.ElectricPopTheme
import com.electricpop.theme.PopShapeFull

/**
 * A styled code block following the Kinetic Pulse design system.
 *
 * Displays pre-formatted code text in a monospace font with optional label and copy button.
 * The code area supports horizontal scrolling for long lines and text selection.
 *
 * Design rules enforced:
 * - **Rule 1 (No-Line):** No 1px borders. Copy button uses tonal surface shift (`surfaceContainerHigh`).
 * - **Rule 5 (Kinetic Interactions):** Copy button hover scales to 1.05x, press to 0.95x.
 * - **Rule 6 (Squircle Radii):** Outer container uses `MaterialTheme.shapes.medium`. Copy button uses `PopShapeFull`.
 * - **Rule 7 (Typography Impact):** Label text rendered uppercase.
 *
 * @param code The source code string to display.
 * @param modifier Optional [Modifier] applied to the outer container.
 * @param label Optional label shown in the header row (rendered uppercase).
 * @param onCopy Optional callback invoked with [code] when the copy button is tapped.
 *   If null, no copy button is shown.
 * @param containerColor Background color of the code block container.
 * @param contentColor Color for the code text, label, and copy icon.
 * @param shape Shape applied to the outer container. Defaults to `MaterialTheme.shapes.medium`.
 */
@Composable
fun PopCodeBlock(
    code: String,
    modifier: Modifier = Modifier,
    label: String? = null,
    onCopy: ((String) -> Unit)? = null,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    shape: Shape = MaterialTheme.shapes.small,
) {
    val spacing = ElectricPopTheme.spacing
    val showHeader = label != null || onCopy != null

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(containerColor),
    ) {
        // Header row — shown only when label or copy button is needed
        if (showHeader) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacing.lg, vertical = spacing.sm),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                if (label != null) {
                    Text(
                        text = label.uppercase(),
                        style = MaterialTheme.typography.labelLarge,
                        color = contentColor,
                    )
                } else {
                    Spacer(Modifier.weight(1f))
                }

                if (onCopy != null) {
                    PopCodeBlockCopyButton(
                        onCopy = { onCopy(code) },
                        tint = contentColor,
                    )
                }
            }
        }

        // Code content area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clipToBounds()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = spacing.lg, vertical = spacing.md),
        ) {
            SelectionContainer {
                Text(
                    text = code,
                    style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                    color = contentColor,
                )
            }
        }
    }
}

@Composable
private fun PopCodeBlockCopyButton(
    onCopy: () -> Unit,
    tint: Color,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val scale = kineticScale(interactionSource = interactionSource, enabled = true)

    Box(
        modifier = Modifier
            .size(32.dp)
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .clip(PopShapeFull)
            .background(tint.copy(alpha = 0.15f))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onCopy,
            )
            .hoverable(interactionSource),
        contentAlignment = Alignment.Center,
    ) {
        PopIcon(
            imageVector = PopIcons.Layers,
            contentDescription = "Copy code",
            modifier = Modifier.size(18.dp),
            tint = tint,
        )
    }
}
