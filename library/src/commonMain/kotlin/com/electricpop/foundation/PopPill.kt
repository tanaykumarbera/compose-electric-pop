package com.electricpop.foundation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.electricpop.theme.ElectricPopTheme
import com.electricpop.theme.PopShapeFull

/**
 * A small pill-shaped label badge used for status indicators like "Active", "Locked", "Live", etc.
 *
 * Colors default to primaryContainer/onPrimaryContainer but can be customized to use
 * any container color pair from [MaterialTheme.colorScheme].
 *
 * @param label The text to display. Will be rendered in uppercase.
 * @param modifier Modifier for the pill container.
 * @param containerColor Background color of the pill. Defaults to [MaterialTheme.colorScheme.primaryContainer].
 * @param contentColor Text color inside the pill. Defaults to [MaterialTheme.colorScheme.onPrimaryContainer].
 * @param textStyle Typography style for the label. Defaults to [MaterialTheme.typography.labelSmall].
 */
@Composable
fun PopPill(
    label: String,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    textStyle: TextStyle = MaterialTheme.typography.labelSmall,
) {
    val spacing = ElectricPopTheme.spacing

    Box(
        modifier = modifier
            .clip(PopShapeFull)
            .background(containerColor)
            .padding(
                horizontal = spacing.sm,
                vertical = spacing.xxs,
            ),
    ) {
        Text(
            text = label.uppercase(),
            color = contentColor,
            style = textStyle,
        )
    }
}
