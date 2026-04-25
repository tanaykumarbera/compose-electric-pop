package co.tanay.electricpop.foundation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import co.tanay.electricpop.theme.ElectricPopTheme
import co.tanay.electricpop.theme.PopShapeFull

/**
 * Preset color configurations for [PopPill].
 *
 * Each entry pairs a container color with its matching on-container content color
 * from [MaterialTheme.colorScheme].
 */
enum class PopPillColor {
    /** Electric Lime — primaryContainer / onPrimaryContainer */
    Primary,
    /** Neon Magenta — secondaryContainer / onSecondaryContainer */
    Secondary,
    /** Cyber Cyan — tertiaryContainer / onTertiaryContainer */
    Tertiary,
    /** Error — errorContainer / onErrorContainer */
    Error,
    /** Neutral — surfaceContainerHigh / onSurface */
    Neutral,
}

/**
 * Resolved container + content color pair for a [PopPill].
 */
@Immutable
data class PopPillColors(
    val containerColor: Color,
    val contentColor: Color,
)

/**
 * Resolves a [PopPillColor] preset to actual theme colors.
 */
@Composable
fun PopPillColor.toColors(): PopPillColors {
    val scheme = MaterialTheme.colorScheme
    return when (this) {
        PopPillColor.Primary -> PopPillColors(scheme.primaryContainer, scheme.onPrimaryContainer)
        PopPillColor.Secondary -> PopPillColors(scheme.secondaryContainer, scheme.onSecondaryContainer)
        PopPillColor.Tertiary -> PopPillColors(scheme.tertiaryContainer, scheme.onTertiaryContainer)
        PopPillColor.Error -> PopPillColors(scheme.errorContainer, scheme.onErrorContainer)
        PopPillColor.Neutral -> PopPillColors(scheme.surfaceContainerHigh, scheme.onSurface)
    }
}

/**
 * A small pill-shaped label badge for status indicators.
 *
 * PopPill displays uppercase text inside a fully-rounded (pill) container.
 * It uses [PopShapeFull] for the squircle/pill shape and reads all styling
 * from the theme — no hardcoded colors, typography, or spacing.
 *
 * @param label The text to display inside the pill (rendered uppercase).
 * @param modifier Optional [Modifier] for the pill container.
 * @param color A [PopPillColor] preset that determines the container and content colors.
 */
@Composable
fun PopPill(
    label: String,
    modifier: Modifier = Modifier,
    color: PopPillColor = PopPillColor.Primary,
) {
    val colors = color.toColors()
    PopPill(
        label = label,
        modifier = modifier,
        containerColor = colors.containerColor,
        contentColor = colors.contentColor,
    )
}

/**
 * A small pill-shaped label badge for status indicators with custom colors.
 *
 * This overload allows specifying arbitrary container and content colors
 * instead of using a [PopPillColor] preset.
 *
 * @param label The text to display inside the pill (rendered uppercase).
 * @param modifier Optional [Modifier] for the pill container.
 * @param containerColor Background color of the pill.
 * @param contentColor Text color inside the pill.
 */
@Composable
fun PopPill(
    label: String,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
) {
    val spacing = ElectricPopTheme.spacing
    Box(
        modifier = modifier
            .clip(PopShapeFull)
            .background(containerColor),
    ) {
        Text(
            text = label.uppercase(),
            modifier = Modifier.padding(
                horizontal = spacing.sm,
                vertical = spacing.xxs,
            ),
            color = contentColor,
            style = MaterialTheme.typography.labelSmall,
        )
    }
}
