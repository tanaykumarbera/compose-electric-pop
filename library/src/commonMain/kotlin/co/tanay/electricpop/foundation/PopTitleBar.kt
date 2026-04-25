package co.tanay.electricpop.foundation

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import co.tanay.electricpop.theme.ElectricPopTheme

/**
 * A page-level title bar matching the "Live Ledger Cards Doc" Stitch design.
 *
 * Renders a full-width horizontal row:
 * - Left: title in headlineMedium italic uppercase.
 * - Right (optional): pulsing status dot + uppercase status label.
 *
 * Follows all 7 design rules:
 * - Typography Impact (Rule 7): title in uppercase with headlineMedium + italic.
 * - Kinetic Interactions (Rule 5): status dot uses infinite alpha animation for a pulse effect.
 * - No pill, no border, no elevation.
 *
 * @param title The title text. Rendered uppercase.
 * @param modifier Optional [Modifier] applied to the outer row.
 * @param status Optional right-side status label (e.g., "SYSTEM ACTIVE"). When non-null,
 *   a pulsing dot and the status text are shown on the right side.
 */
@Composable
fun PopTitleBar(
    title: String,
    modifier: Modifier = Modifier,
    status: String? = null,
) {
    val spacing = ElectricPopTheme.spacing
    val cs = MaterialTheme.colorScheme

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        // Title — headlineMedium with italic override, uppercase
        Text(
            text = title.uppercase(),
            style = MaterialTheme.typography.headlineMedium.copy(fontStyle = FontStyle.Italic),
            color = cs.onSurface,
        )

        // Optional status section
        if (status != null) {
            StatusRow(label = status)
        }
    }
}

@Composable
private fun StatusRow(label: String) {
    val spacing = ElectricPopTheme.spacing
    val cs = MaterialTheme.colorScheme

    // Pulsing alpha animation for the dot
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "dotAlpha",
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacing.xs),
    ) {
        // Pulsing dot — 8dp circle in secondary color
        Box(
            modifier = Modifier
                .size(8.dp)
                .alpha(alpha)
                .background(color = cs.secondary, shape = CircleShape),
        )

        // Status label — labelSmall, uppercase, 60% opacity
        Text(
            text = label.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = cs.onSurface.copy(alpha = 0.6f),
        )
    }
}
