package co.tanay.electricpop.foundation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import co.tanay.electricpop.theme.ElectricPopTheme
import co.tanay.electricpop.theme.PopShapeFull

/**
 * A section header component matching the "Stash Hub Card Doc" Stitch design.
 *
 * Renders a vertical stack:
 * - Optional pill (highlight label) followed by a decorative horizontal line — shown when [highlight] is non-null.
 * - Title text rendered uppercase in [displayLarge] style, with an optional [titleAccent] portion in accent color.
 * - Optional description paragraph in [bodyLarge].
 *
 * Follows all 7 design rules:
 * - No-Line Rule: the 2dp thick decorative bar is intentional and NOT a 1px border.
 * - Typography Impact (Rule 7): title rendered uppercase with displayLarge style.
 * - Squircle Radii (Rule 6): pill uses PopShapeFull.
 *
 * @param title Main title text (e.g., "STASH HUB"). Rendered uppercase in onSurface color.
 * @param modifier Optional [Modifier] applied to the outer column.
 * @param highlight Optional pill label (e.g., "COMPONENT GUIDE"). When non-null, a pill and decorative line are shown.
 * @param titleAccent Optional colored word appended after [title] (e.g., "CARD"). Rendered uppercase in [titleAccentColor].
 * @param titleAccentColor Color for [titleAccent]. Defaults to [MaterialTheme.colorScheme.tertiary].
 * @param description Optional description paragraph rendered in bodyLarge / onSurfaceVariant.
 */
@Composable
fun PopSectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    highlight: String? = null,
    titleAccent: String? = null,
    titleAccentColor: Color? = null,
    description: String? = null,
) {
    val spacing = ElectricPopTheme.spacing
    val cs = MaterialTheme.colorScheme
    val accentColor = titleAccentColor ?: cs.tertiary

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(spacing.xs),
    ) {
        // Pill + decorative line row (only when highlight is provided)
        if (highlight != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(spacing.sm),
            ) {
                // Pill
                Box(
                    modifier = Modifier
                        .background(color = cs.tertiaryContainer, shape = PopShapeFull)
                        .padding(PaddingValues(horizontal = spacing.sm, vertical = spacing.xxs)),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = highlight.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = cs.onTertiaryContainer,
                    )
                }

                // Decorative line — 48dp wide, 2dp tall, NOT a 1px border (complies with No-Line Rule)
                Box(
                    modifier = Modifier
                        .width(48.dp)
                        .height(2.dp)
                        .background(color = cs.tertiaryContainer),
                )
            }
        }

        // Title text — displayLarge, uppercase, two-part annotated string
        val titleText = buildAnnotatedString {
            withStyle(
                style = MaterialTheme.typography.displayLarge.toSpanStyle().copy(
                    color = cs.onSurface,
                ),
            ) {
                append(title.uppercase())
            }
            if (titleAccent != null) {
                append(" ")
                withStyle(
                    style = MaterialTheme.typography.displayLarge.toSpanStyle().copy(
                        color = accentColor,
                    ),
                ) {
                    append(titleAccent.uppercase())
                }
            }
        }
        Text(
            text = titleText,
            style = MaterialTheme.typography.displayLarge,
        )

        // Optional description
        if (description != null) {
            Text(
                text = description,
                style = MaterialTheme.typography.bodyLarge,
                color = cs.onSurfaceVariant,
            )
        }
    }
}
