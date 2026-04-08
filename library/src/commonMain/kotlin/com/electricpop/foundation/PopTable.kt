package com.electricpop.foundation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.graphics.Shape
import com.electricpop.theme.ElectricPopTheme

/**
 * Represents a single row in a [PopTable].
 */
@Immutable
data class PopTableRow(
    val label: String,
    val value: String,
)

/**
 * Displays label-value rows inside a colored squircle container.
 *
 * The table renders as a single container with a background color,
 * an optional bold italic uppercase title, and rows separated by
 * ghost-border dividers (outlineVariant at 15% opacity).
 *
 * @param rows The data to display.
 * @param modifier Optional [Modifier].
 * @param title Optional section title rendered in headlineLarge uppercase inside the container.
 * @param containerColor Background color of the table container.
 * @param contentColor Text color for title, labels, and values.
 * @param shape Shape of the outer container.
 */
@Composable
fun PopTable(
    rows: List<PopTableRow>,
    modifier: Modifier = Modifier,
    title: String? = null,
    containerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
    shape: Shape = MaterialTheme.shapes.extraSmall,
    contentPadding: PaddingValues = PaddingValues(ElectricPopTheme.spacing.xl),
) {
    if (rows.isEmpty()) return

    val spacing = ElectricPopTheme.spacing
    val dividerColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.15f)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(containerColor)
            .padding(contentPadding),
    ) {
        if (title != null) {
            Text(
                text = title.uppercase(),
                style = MaterialTheme.typography.headlineLarge,
                color = contentColor,
            )
        }

        rows.forEachIndexed { index, row ->
            if (index > 0 || title != null) {
                HorizontalDivider(
                    color = dividerColor,
                    thickness = androidx.compose.ui.unit.Dp.Hairline,
                    modifier = Modifier.padding(vertical = spacing.md),
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = row.label.uppercase(),
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.labelLarge,
                    color = contentColor,
                )
                Text(
                    text = row.value.uppercase(),
                    style = MaterialTheme.typography.labelLarge,
                    color = contentColor,
                )
            }
        }
    }
}
