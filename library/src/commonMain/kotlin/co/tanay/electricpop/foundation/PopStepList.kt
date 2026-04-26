package co.tanay.electricpop.foundation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import co.tanay.electricpop.theme.ElectricPopTheme

/**
 * Represents a single step in a [PopStepList].
 *
 * @param icon Optional [ImageVector] symbol for this step. Set to null for numbered-only mode.
 * @param label Short headline for this step.
 * @param description Longer explanation text for this step.
 */
@Immutable
data class PopStep(
    val icon: ImageVector? = null,
    val label: String,
    val description: String,
)

/**
 * Formats a 1-based step index as a zero-padded two-digit string.
 */
internal fun formatStepNumber(index: Int): String {
    return index.toString().padStart(2, '0')
}

/**
 * Displays a vertical list of steps with optional numbers and icons, labels, and descriptions.
 *
 * Two primary usage modes:
 * - **Numbered mode**: `showNumbers = true` (default), steps have no icons — shows 01, 02, 03 + text.
 * - **Icon mode**: `showNumbers = false`, steps provide icons — shows icon + text.
 * - **Both**: `showNumbers = true` with icons on each step — shows number + icon + text.
 *
 * Steps are separated by spacing (no divider lines, per the No-Line Rule).
 *
 * @param steps The list of [PopStep] items to display.
 * @param modifier Optional [Modifier] applied to the outer column.
 * @param showNumbers Whether to show zero-padded step numbers. Defaults to true.
 * @param numberColor Color for the step numbers. Defaults to primary.
 * @param iconTint Tint color for all step icons. Defaults to primary.
 */
@Composable
fun PopStepList(
    steps: List<PopStep>,
    modifier: Modifier = Modifier,
    showNumbers: Boolean = true,
    numberColor: Color = MaterialTheme.colorScheme.primary,
    iconTint: Color = MaterialTheme.colorScheme.primary,
) {
    if (steps.isEmpty()) return
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(ElectricPopTheme.spacing.lg),
    ) {
        steps.forEachIndexed { index, step ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(ElectricPopTheme.spacing.md),
                verticalAlignment = Alignment.Top,
            ) {
                if (showNumbers) {
                    Text(
                        text = formatStepNumber(index + 1),
                        style = MaterialTheme.typography.headlineLarge,
                        color = numberColor,
                    )
                }
                if (step.icon != null) {
                    PopIcon(
                        imageVector = step.icon,
                        contentDescription = null,
                        size = PopIconSize.Medium,
                        tint = iconTint,
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(ElectricPopTheme.spacing.xxs),
                ) {
                    Text(
                        text = step.label,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        text = step.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}
