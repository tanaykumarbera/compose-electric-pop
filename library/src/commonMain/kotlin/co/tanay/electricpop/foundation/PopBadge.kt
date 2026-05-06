package co.tanay.electricpop.foundation

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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import co.tanay.electricpop.theme.ElectricPopTheme
import co.tanay.electricpop.theme.PopShapeFull

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
 * Size variant for a [PopBadge].
 */
enum class PopBadgeSize {
    /** Compact badge — labelSmall typography, 8dp/2dp padding, 16dp icon. */
    Small,

    /** Hero badge — titleLarge italic Black typography, 16dp/8dp padding, 24dp icon. */
    Large,
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
 * Size variants:
 * - [PopBadgeSize.Small] — compact label badge with [labelSmall] typography.
 * - [PopBadgeSize.Large] — hero badge with italic Black [titleLarge] typography, matching the Stitch hero-banner spec.
 *
 * Uses [PopShapeFull] for the pill shape and reads all styling from the theme.
 *
 * @param value The text to display (e.g., "+12.5%", "-3.2%").
 * @param direction The trend direction determining icon and color.
 * @param modifier Optional [Modifier] for the badge container.
 * @param size Size variant; defaults to [PopBadgeSize.Small] for back-compat.
 *
 * <!-- screenshots:start (auto-generated, do not edit) -->
 * | Scenario | Light | Dark |
 * | --- | --- | --- |
 * | allVariants | ![](https://tanaykumarbera.github.io/compose-electric-pop/snapshots/PopBadge_allVariants_light.png) | ![](https://tanaykumarbera.github.io/compose-electric-pop/snapshots/PopBadge_allVariants_dark.png) |
 * <!-- screenshots:end -->
 */
@Composable
fun PopBadge(
    value: String,
    direction: PopBadgeDirection,
    modifier: Modifier = Modifier,
    size: PopBadgeSize = PopBadgeSize.Small,
) {
    val colors = direction.toColors()
    PopBadge(
        value = value,
        direction = direction,
        containerColor = colors.containerColor,
        contentColor = colors.contentColor,
        modifier = modifier,
        size = size,
    )
}

/**
 * A compact directional badge with explicit container and content colors.
 *
 * This overload allows specifying arbitrary colors — used by the Hero card variant
 * where the badge uses inverted primary colors.
 *
 * @param value The text to display (e.g., "+12.5%", "-3.2%").
 * @param direction The trend direction determining the icon (Up / Down / Neutral).
 * @param containerColor Background color of the badge.
 * @param contentColor Text and icon tint color inside the badge.
 * @param modifier Optional [Modifier] for the badge container.
 * @param size Size variant; defaults to [PopBadgeSize.Small].
 */
@Composable
fun PopBadge(
    value: String,
    direction: PopBadgeDirection,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier,
    size: PopBadgeSize = PopBadgeSize.Small,
) {
    val spacing = ElectricPopTheme.spacing

    val paddingH: Dp
    val paddingV: Dp
    val iconSize: PopIconSize
    val gap: Dp

    when (size) {
        PopBadgeSize.Small -> {
            paddingH = spacing.sm
            paddingV = spacing.xxs
            iconSize = PopIconSize.Small
            gap = spacing.xxs
        }
        PopBadgeSize.Large -> {
            paddingH = spacing.lg
            paddingV = spacing.sm
            iconSize = PopIconSize.Medium
            gap = spacing.md
        }
    }

    val textStyle = when (size) {
        PopBadgeSize.Small -> MaterialTheme.typography.labelSmall
        PopBadgeSize.Large -> MaterialTheme.typography.titleLarge.copy(
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Black,
        )
    }

    Row(
        modifier = modifier
            .clip(PopShapeFull)
            .background(containerColor)
            .padding(
                horizontal = paddingH,
                vertical = paddingV,
            ),
        horizontalArrangement = Arrangement.spacedBy(gap),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        when (direction) {
            PopBadgeDirection.Up -> PopIcon(
                imageVector = PopIcons.TrendUp,
                contentDescription = "Trending up",
                size = iconSize,
                tint = contentColor,
            )
            PopBadgeDirection.Down -> PopIcon(
                imageVector = PopIcons.TrendDown,
                contentDescription = "Trending down",
                size = iconSize,
                tint = contentColor,
            )
            PopBadgeDirection.Neutral -> { /* No icon for neutral */ }
        }

        Text(
            text = value.uppercase(),
            color = contentColor,
            style = textStyle,
        )
    }
}
