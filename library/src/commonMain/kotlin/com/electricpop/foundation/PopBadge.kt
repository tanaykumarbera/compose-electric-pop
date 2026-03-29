package com.electricpop.foundation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.electricpop.theme.ElectricPopTheme
import com.electricpop.theme.PopShapeFull

/**
 * Direction for a [PopBadge], determining its semantic coloring and icon.
 */
enum class PopBadgeDirection {
    /** Positive trend — green (primaryContainer) with TrendUp icon */
    Up,
    /** Negative trend — red (errorContainer) with TrendDown icon */
    Down,
    /** No trend — neutral (surfaceContainerHigh) with no icon */
    Neutral,
}

/**
 * Resolved container + content color pair for a [PopBadge].
 */
@Immutable
data class PopBadgeColors(
    val containerColor: Color,
    val contentColor: Color,
)

/**
 * Resolves a [PopBadgeDirection] to theme-based colors.
 */
@Composable
fun PopBadgeDirection.toColors(): PopBadgeColors {
    val scheme = MaterialTheme.colorScheme
    return when (this) {
        PopBadgeDirection.Up -> PopBadgeColors(
            containerColor = scheme.primaryContainer,
            contentColor = scheme.onPrimaryContainer,
        )
        PopBadgeDirection.Down -> PopBadgeColors(
            containerColor = scheme.errorContainer,
            contentColor = scheme.onErrorContainer,
        )
        PopBadgeDirection.Neutral -> PopBadgeColors(
            containerColor = scheme.surfaceContainerHigh,
            contentColor = scheme.onSurface,
        )
    }
}

/**
 * A compact directional badge showing a trend icon alongside a value.
 *
 * PopBadge displays a trend direction (up/down/neutral) with semantic coloring:
 * green for positive (primaryContainer), red for negative (errorContainer),
 * and neutral (surfaceContainerHigh). An arrow icon is shown for directional
 * variants. The value text is rendered uppercase in a pill-shaped container.
 *
 * Uses [PopShapeFull] for the pill shape and reads all styling from the theme.
 *
 * @param value The text to display (e.g., "+12.5%", "-3.2%").
 * @param direction The trend direction determining icon and color.
 * @param modifier Optional [Modifier] for the badge container.
 */
@Composable
fun PopBadge(
    value: String,
    direction: PopBadgeDirection,
    modifier: Modifier = Modifier,
) {
    val colors = direction.toColors()
    val spacing = ElectricPopTheme.spacing

    Row(
        modifier = modifier
            .clip(PopShapeFull)
            .background(colors.containerColor)
            .padding(
                horizontal = spacing.sm,
                vertical = spacing.xxs,
            ),
        horizontalArrangement = Arrangement.spacedBy(spacing.xxs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        when (direction) {
            PopBadgeDirection.Up -> PopIcon(
                imageVector = PopIcons.TrendUp,
                contentDescription = "Trending up",
                size = PopIconSize.Small,
                tint = colors.contentColor,
            )
            PopBadgeDirection.Down -> PopIcon(
                imageVector = PopIcons.TrendDown,
                contentDescription = "Trending down",
                size = PopIconSize.Small,
                tint = colors.contentColor,
            )
            PopBadgeDirection.Neutral -> { /* No icon for neutral */ }
        }

        Text(
            text = value.uppercase(),
            color = colors.contentColor,
            style = MaterialTheme.typography.labelSmall,
        )
    }
}
