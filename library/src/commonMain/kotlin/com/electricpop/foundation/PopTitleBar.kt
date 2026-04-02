package com.electricpop.foundation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.electricpop.theme.ElectricPopTheme

/**
 * Size variants for [PopTitleBar].
 *
 * Controls the typography style used to render the title text.
 */
enum class PopTitleBarStyle {
    /** Uses headlineLarge (32sp, Black Italic) */
    Large,
    /** Uses headlineMedium (28sp, Bold) */
    Medium,
    /** Uses headlineSmall (24sp, Bold) */
    Small,
}

/**
 * Resolves the [TextStyle] for this [PopTitleBarStyle] from the current theme.
 */
@Composable
internal fun PopTitleBarStyle.toTextStyle(): TextStyle {
    return when (this) {
        PopTitleBarStyle.Large -> MaterialTheme.typography.headlineLarge
        PopTitleBarStyle.Medium -> MaterialTheme.typography.headlineMedium
        PopTitleBarStyle.Small -> MaterialTheme.typography.headlineSmall
    }
}

/**
 * A page-level title bar that renders an uppercase headline with an optional status pill.
 *
 * Uses a preset [PopPillColor] for the pill. Follows Rule 7 (Typography Impact) by
 * rendering the title in uppercase using headline typography.
 *
 * @param title The title text. Rendered uppercase.
 * @param modifier Optional [Modifier] applied to the outer row.
 * @param pill Optional pill label. When non-null, a [PopPill] is rendered beside the title.
 * @param pillColor Preset color from [PopPillColor] for the pill badge.
 * @param style Size variant that controls the headline typography level.
 */
@Composable
fun PopTitleBar(
    title: String,
    modifier: Modifier = Modifier,
    pill: String? = null,
    pillColor: PopPillColor = PopPillColor.Primary,
    style: PopTitleBarStyle = PopTitleBarStyle.Large,
) {
    val spacing = ElectricPopTheme.spacing
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacing.sm),
    ) {
        Text(
            text = title.uppercase(),
            style = style.toTextStyle(),
            color = MaterialTheme.colorScheme.onSurface,
        )
        if (pill != null) {
            PopPill(label = pill, color = pillColor)
        }
    }
}

/**
 * A page-level title bar with custom pill colors.
 *
 * This overload allows specifying arbitrary container and content colors for the pill
 * instead of using a [PopPillColor] preset. Follows Rule 7 (Typography Impact) by
 * rendering the title in uppercase using headline typography.
 *
 * @param title The title text. Rendered uppercase.
 * @param modifier Optional [Modifier] applied to the outer row.
 * @param pill Optional pill label. When non-null, a [PopPill] is rendered beside the title.
 * @param pillContainerColor Background color of the pill.
 * @param pillContentColor Text color inside the pill.
 * @param style Size variant that controls the headline typography level.
 */
@Composable
fun PopTitleBar(
    title: String,
    modifier: Modifier = Modifier,
    pill: String? = null,
    pillContainerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    pillContentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    style: PopTitleBarStyle = PopTitleBarStyle.Large,
) {
    val spacing = ElectricPopTheme.spacing
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacing.sm),
    ) {
        Text(
            text = title.uppercase(),
            style = style.toTextStyle(),
            color = MaterialTheme.colorScheme.onSurface,
        )
        if (pill != null) {
            PopPill(
                label = pill,
                containerColor = pillContainerColor,
                contentColor = pillContentColor,
            )
        }
    }
}
