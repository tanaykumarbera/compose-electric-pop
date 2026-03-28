package com.electricpop.foundation

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Predefined sizes for [PopIcon].
 *
 * Each size maps to a Dp value matching the Material Symbols
 * optical-size concept (opsz).
 */
enum class PopIconSize {
    /** 16 dp — inline / badge usage */
    Small,
    /** 24 dp — default, matches opsz:24 spec */
    Medium,
    /** 32 dp — emphasis / header usage */
    Large,
}

/**
 * Resolves a [PopIconSize] to its corresponding [Dp] value.
 */
internal fun PopIconSize.toDp(): Dp = when (this) {
    PopIconSize.Small  -> 16.dp
    PopIconSize.Medium -> 24.dp
    PopIconSize.Large  -> 32.dp
}

/**
 * Renders a Material Symbols icon with consistent sizing and theme-aware tinting.
 *
 * PopIcon wraps Compose's [Icon] composable, enforcing the Electric Pop
 * design system defaults: Outlined style, FILL:0, weight:400, opsz:24.
 * Consumers pass any [ImageVector] (e.g., `Icons.Outlined.Star`).
 *
 * @param imageVector The [ImageVector] to render.
 * @param contentDescription Accessibility description; null if decorative.
 * @param modifier Optional [Modifier] applied to the icon.
 * @param size One of [PopIconSize] presets controlling the rendered size.
 * @param tint Icon tint color. Defaults to [MaterialTheme.colorScheme.onSurface].
 */
@Composable
fun PopIcon(
    imageVector: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    size: PopIconSize = PopIconSize.Medium,
    tint: Color = MaterialTheme.colorScheme.onSurface,
) {
    val sizeDp = size.toDp()
    Icon(
        imageVector = imageVector,
        contentDescription = contentDescription,
        modifier = modifier.size(sizeDp),
        tint = tint,
    )
}
