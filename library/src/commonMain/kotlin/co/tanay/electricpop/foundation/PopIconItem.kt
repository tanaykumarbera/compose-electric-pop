package co.tanay.electricpop.foundation

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * A data class describing a single icon entry — used by composites that render
 * icon clusters (e.g., the icon slot on [co.tanay.electricpop.composite.PopBannerCard]).
 *
 * @param imageVector The vector image to render.
 * @param contentDescription Accessibility description; null if decorative.
 */
@Immutable
data class PopIconItem(
    val imageVector: ImageVector,
    val contentDescription: String? = null,
)
