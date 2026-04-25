package com.electricpop.foundation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import com.electricpop.theme.ElectricPopTheme
import kotlin.jvm.JvmName

/**
 * A data class representing a single icon entry in a [PopIconRow].
 *
 * @param imageVector The vector image to render.
 * @param contentDescription Accessibility description; null if decorative.
 */
@Immutable
data class PopIconItem(
    val imageVector: ImageVector,
    val contentDescription: String? = null,
)

/**
 * Renders a horizontal row of icons with consistent sizing and spacing.
 *
 * PopIconRow is a pure layout component — no background, no border, no shadow.
 * It composes [PopIcon] instances for each item in the [icons] list.
 *
 * @param icons List of [PopIconItem] to render.
 * @param modifier Optional [Modifier] applied to the row.
 * @param iconSize Size preset for all icons in the row.
 * @param tint Tint color applied to all icons. Defaults to [MaterialTheme.colorScheme.onSurface].
 * @param spacing Horizontal spacing between icons. Defaults to [ElectricPopTheme.spacing.sm].
 */
@Composable
fun PopIconRow(
    icons: List<PopIconItem>,
    modifier: Modifier = Modifier,
    iconSize: PopIconSize = PopIconSize.Medium,
    tint: Color = MaterialTheme.colorScheme.onSurface,
    spacing: Dp = ElectricPopTheme.spacing.sm,
) {
    if (icons.isEmpty()) return
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        icons.forEach { item ->
            PopIcon(
                imageVector = item.imageVector,
                contentDescription = item.contentDescription,
                size = iconSize,
                tint = tint,
            )
        }
    }
}

/**
 * Convenience overload of [PopIconRow] that accepts a plain list of [ImageVector]s.
 *
 * Each vector is wrapped in a [PopIconItem] with a null content description.
 *
 * @param icons List of [ImageVector]s to render.
 * @param modifier Optional [Modifier] applied to the row.
 * @param iconSize Size preset for all icons in the row.
 * @param tint Tint color applied to all icons. Defaults to [MaterialTheme.colorScheme.onSurface].
 * @param spacing Horizontal spacing between icons. Defaults to [ElectricPopTheme.spacing.sm].
 */
@JvmName("PopIconRowVectors")
@Composable
fun PopIconRow(
    icons: List<ImageVector>,
    modifier: Modifier = Modifier,
    iconSize: PopIconSize = PopIconSize.Medium,
    tint: Color = MaterialTheme.colorScheme.onSurface,
    spacing: Dp = ElectricPopTheme.spacing.sm,
) {
    PopIconRow(
        icons = icons.map { PopIconItem(imageVector = it) },
        modifier = modifier,
        iconSize = iconSize,
        tint = tint,
        spacing = spacing,
    )
}
