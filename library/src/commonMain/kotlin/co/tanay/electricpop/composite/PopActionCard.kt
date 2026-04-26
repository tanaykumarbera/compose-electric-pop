package co.tanay.electricpop.composite

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import co.tanay.electricpop.foundation.PopButton
import co.tanay.electricpop.foundation.PopButtonSize
import co.tanay.electricpop.foundation.PopButtonStyle
import co.tanay.electricpop.theme.ElectricPopTheme

/**
 * Represents a single action in a [PopActionCard].
 *
 * Plain class (not data class) because lambdas make equals/hashCode unreliable.
 *
 * @param label Button label text (rendered uppercase by PopButton).
 * @param onClick Click callback.
 * @param style Visual style for the button — defaults to Ghost.
 * @param icon Optional leading icon.
 * @param enabled Whether the action is interactive.
 */
class PopActionCardAction(
    val label: String,
    val onClick: () -> Unit,
    val style: PopButtonStyle = PopButtonStyle.Ghost,
    val icon: ImageVector? = null,
    val enabled: Boolean = true,
)

/**
 * A confirmation/decision card with an optional description, chip row, hero content slot,
 * and a row of equal-width action buttons.
 *
 * Layout (top to bottom):
 * 1. Title — uppercase, titleSmall, onSurfaceVariant
 * 2. Description (optional) — bodyMedium, onSurfaceVariant
 * 3. Chips slot (optional)
 * 4. Hero content slot (optional)
 * 5. Actions row — equal-width [PopButton] items
 *
 * Design rules enforced:
 * - **Rule 1 (No-Line):** surfaceContainer background, no dividers.
 * - **Rule 2 (Tonal Shadows):** shadow with onSurface at 10% opacity.
 * - **Rule 3 (Ghost Border):** outlineVariant at 15% opacity.
 * - **Rule 4 (Neon Glow):** Delegated to Primary [PopButton] internally.
 * - **Rule 5 (Kinetic):** Delegated to [PopButton] internally.
 * - **Rule 6 (Squircle):** Uses MaterialTheme.shapes.extraSmall.
 * - **Rule 7 (Typography Impact):** title rendered uppercase.
 *
 * @param title Card title (rendered uppercase).
 * @param actions List of [PopActionCardAction] rendered as equal-width buttons.
 * @param modifier Optional [Modifier] for the card container.
 * @param description Optional description text below the title.
 * @param chips Optional composable slot rendered as a horizontal chip row.
 * @param heroContent Optional composable slot rendered between chips and actions.
 */
@Composable
fun PopActionCard(
    title: String,
    actions: List<PopActionCardAction>,
    modifier: Modifier = Modifier,
    description: String? = null,
    chips: (@Composable RowScope.() -> Unit)? = null,
    heroContent: (@Composable () -> Unit)? = null,
) {
    val spacing = ElectricPopTheme.spacing
    val scheme = MaterialTheme.colorScheme
    val shape = MaterialTheme.shapes.extraSmall

    Column(
        modifier = modifier
            // Rule 2: Tonal shadow — onSurface at 10%
            .shadow(
                elevation = 8.dp,
                shape = shape,
                ambientColor = scheme.onSurface.copy(alpha = 0.10f),
                spotColor = scheme.onSurface.copy(alpha = 0.10f),
            )
            .clip(shape)
            // Rule 1: surfaceContainer background
            .background(scheme.surfaceContainer)
            // Rule 3: Ghost border — outlineVariant at 15%
            .border(1.dp, scheme.outlineVariant.copy(alpha = 0.15f), shape)
            .padding(spacing.xl),
        verticalArrangement = Arrangement.spacedBy(spacing.md),
    ) {
        // Title + optional description block
        Column(verticalArrangement = Arrangement.spacedBy(spacing.xxs)) {
            Text(
                text = title.uppercase(),
                style = MaterialTheme.typography.titleSmall,
                color = scheme.onSurfaceVariant,
            )
            if (description != null) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = scheme.onSurfaceVariant,
                )
            }
        }

        // Optional chips row
        if (chips != null) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacing.xs),
                content = chips,
            )
        }

        // Optional hero content slot
        if (heroContent != null) {
            heroContent()
        }

        // Actions row — equal-width buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing.md),
        ) {
            actions.forEach { action ->
                PopButton(
                    text = action.label,
                    onClick = action.onClick,
                    modifier = Modifier.weight(1f),
                    style = action.style,
                    size = PopButtonSize.Large,
                    icon = action.icon,
                    enabled = action.enabled,
                )
            }
        }
    }
}
