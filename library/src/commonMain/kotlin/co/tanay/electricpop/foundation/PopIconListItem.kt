package co.tanay.electricpop.foundation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import co.tanay.electricpop.theme.ElectricPopTheme

/**
 * A list-item composable pairing an icon with descriptive body text.
 *
 * Used for guidelines, feature lists, and bullet-point-style content.
 * All visual variants (check, cancel, custom icon, tinted icon) are
 * achieved by passing different parameter values.
 *
 * @param icon The [ImageVector] to display (e.g., `PopIcons.CheckCircle`).
 * @param text The description text.
 * @param modifier Optional [Modifier] applied to the row.
 * @param iconTint Tint color for the icon.
 * @param iconSize Size preset for the icon.
 * @param textStyle Text style for the description.
 * @param textColor Text color for the description.
 *
 * <!-- screenshots:start (auto-generated, do not edit) -->
 * | Scenario | Light | Dark |
 * | --- | --- | --- |
 * | allVariants | ![](https://tanaykumarbera.github.io/compose-electric-pop/snapshots/PopIconListItem_allVariants_light.png) | ![](https://tanaykumarbera.github.io/compose-electric-pop/snapshots/PopIconListItem_allVariants_dark.png) |
 * <!-- screenshots:end -->
 */
@Composable
fun PopIconListItem(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier,
    iconTint: Color = MaterialTheme.colorScheme.onSurface,
    iconSize: PopIconSize = PopIconSize.Medium,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(ElectricPopTheme.spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PopIcon(
            imageVector = icon,
            contentDescription = null,
            size = iconSize,
            tint = iconTint,
        )
        Text(
            text = text,
            modifier = Modifier.weight(1f),
            style = textStyle,
            color = textColor,
        )
    }
}
