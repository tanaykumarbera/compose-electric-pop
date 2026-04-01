package com.electricpop.foundation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.electricpop.theme.ElectricPopTheme
import com.electricpop.theme.PopShapeFull

/**
 * Formats a section number with zero-padding for single digits.
 *
 * - 0 → "00"
 * - 1 → "01"
 * - 9 → "09"
 * - 12 → "12"
 * - 100 → "100"
 */
internal fun formatSectionNumber(number: Int): String {
    return if (number in 0..9) "0$number" else number.toString()
}

/**
 * A section header component for Electric Pop.
 *
 * Renders a vertical stack: optional accent label, accent bar, then the title.
 * When [number] is provided the title row shows a zero-padded number prefix.
 *
 * @param title The section title. Rendered uppercase.
 * @param modifier Optional [Modifier] applied to the outer column.
 * @param label Optional label rendered above the accent bar in uppercase. Defaults to null.
 * @param number Optional section number. Single digits are zero-padded (1 → "01"). Defaults to null.
 * @param accentColor Color used for the label, accent bar, and (when numbered) the number prefix.
 */
@Composable
fun PopSectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    label: String? = null,
    number: Int? = null,
    accentColor: Color = MaterialTheme.colorScheme.primary,
) {
    val spacing = ElectricPopTheme.spacing

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(spacing.xs),
    ) {
        // Accent label (optional)
        if (label != null) {
            Text(
                text = label.uppercase(),
                style = MaterialTheme.typography.labelMedium,
                color = accentColor,
            )
        }

        // Accent bar — decorative 3dp bar, NOT a 1px border (No-Line Rule)
        Box(
            modifier = Modifier
                .width(32.dp)
                .height(3.dp)
                .clip(PopShapeFull)
                .background(accentColor),
        )

        // Title row
        if (number != null) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacing.sm),
                verticalAlignment = Alignment.Bottom,
            ) {
                Text(
                    text = formatSectionNumber(number),
                    style = MaterialTheme.typography.headlineLarge,
                    color = accentColor,
                )
                Text(
                    text = title.uppercase(),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        } else {
            Text(
                text = title.uppercase(),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}
