package com.electricpop.composite

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.electricpop.theme.ElectricPopTheme

/**
 * Anchor position for the text block inside [PopImageBannerCard].
 */
enum class PopImageBannerTextAnchor {
    TopStart, TopCenter, TopEnd,
    CenterStart, Center, CenterEnd,
    BottomStart, BottomCenter, BottomEnd,
}

/**
 * A big image card with overlaid editorial headline. Use for marketing/hero banners.
 *
 * Sizing: clips to `MaterialTheme.shapes.extraSmall` and preserves the painter's
 * intrinsic aspect ratio; callers can also constrain height explicitly via [modifier].
 *
 * Scrim: when [scrim] is true, a linear (or radial, for [PopImageBannerTextAnchor.Center])
 * gradient is drawn from [scrimColor] at the anchored edge fading toward transparent,
 * ensuring readable text contrast on busy images.
 *
 * Design rule compliance:
 * - Rule 1 (No-Line): no borders; scrim is a gradient.
 * - Rule 5 (Kinetic): hover → 1.02×, press → 0.97× when [onClick] is provided.
 * - Rule 6 (Squircle): outer clip uses `MaterialTheme.shapes.extraSmall`.
 * - Rule 7 (Typography Impact): headline is `displayMedium` italic/Black, uppercased;
 *   eyebrow is `labelSmall` with wide tracking.
 *
 * @param painter Image source. Callers can compose with Coil's `rememberAsyncImagePainter`
 *   or Kamel's `asyncPainterResource` for remote images — both return a [Painter].
 * @param headline Required hero headline; rendered italic + Black + uppercased. `\n` is honored.
 * @param modifier Optional [Modifier]; consumer may pass `.height(…).fillMaxWidth()` to override aspect-ratio sizing.
 * @param eyebrow Optional small uppercased label above the headline.
 * @param subtitle Optional body-sized line below the headline.
 * @param textAnchor Where the text column sits within the card. Also drives scrim direction.
 * @param scrim Whether to paint a contrast gradient behind the text.
 * @param scrimColor Base color for the scrim gradient (defaults to 55% black).
 * @param contentColor Text color; eyebrow/subtitle use softened alpha.
 * @param contentDescription Accessibility description for the image. When null, the image is marked decorative.
 * @param onClick Optional click handler; enables kinetic scale interaction when set.
 */
@Composable
fun PopImageBannerCard(
    painter: Painter,
    headline: String,
    modifier: Modifier = Modifier,
    eyebrow: String? = null,
    subtitle: String? = null,
    textAnchor: PopImageBannerTextAnchor = PopImageBannerTextAnchor.BottomStart,
    scrim: Boolean = true,
    scrimColor: Color = Color.Black.copy(alpha = 0.55f),
    contentColor: Color = Color.White,
    contentDescription: String? = null,
    onClick: (() -> Unit)? = null,
) {
    val spacing = ElectricPopTheme.spacing
    val shape = MaterialTheme.shapes.extraSmall

    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()
    val targetScale = when {
        onClick == null -> 1f
        isPressed -> 0.97f
        isHovered -> 1.02f
        else -> 1f
    }
    val scale by animateFloatAsState(targetScale, tween(200), label = "image_banner_scale")

    val intrinsic = painter.intrinsicSize
    val aspect = if (intrinsic == Size.Unspecified || intrinsic.width <= 0f || intrinsic.height <= 0f) {
        16f / 9f
    } else {
        intrinsic.width / intrinsic.height
    }

    Box(
        modifier = modifier
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .clip(shape)
            .aspectRatio(aspect, matchHeightConstraintsFirst = true)
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = onClick,
                    )
                } else {
                    Modifier
                }
            )
            .then(
                if (contentDescription != null) {
                    Modifier.semantics { this.contentDescription = contentDescription }
                } else {
                    Modifier
                }
            ),
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )

        if (scrim) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(scrimBrush(textAnchor, scrimColor)),
            )
        }

        Column(
            modifier = Modifier
                .align(alignmentFor(textAnchor))
                .padding(spacing.lg),
            verticalArrangement = Arrangement.spacedBy(spacing.xs),
            horizontalAlignment = horizontalAlignmentFor(textAnchor),
        ) {
            if (eyebrow != null) {
                Text(
                    text = eyebrow.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    letterSpacing = 2.4.sp,
                    color = contentColor.copy(alpha = 0.75f),
                )
            }
            Text(
                text = headline.uppercase(),
                style = MaterialTheme.typography.displayMedium.copy(
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Black,
                ),
                color = contentColor,
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = contentColor.copy(alpha = 0.9f),
                )
            }
        }
    }
}

internal fun alignmentFor(anchor: PopImageBannerTextAnchor): Alignment = when (anchor) {
    PopImageBannerTextAnchor.TopStart -> Alignment.TopStart
    PopImageBannerTextAnchor.TopCenter -> Alignment.TopCenter
    PopImageBannerTextAnchor.TopEnd -> Alignment.TopEnd
    PopImageBannerTextAnchor.CenterStart -> Alignment.CenterStart
    PopImageBannerTextAnchor.Center -> Alignment.Center
    PopImageBannerTextAnchor.CenterEnd -> Alignment.CenterEnd
    PopImageBannerTextAnchor.BottomStart -> Alignment.BottomStart
    PopImageBannerTextAnchor.BottomCenter -> Alignment.BottomCenter
    PopImageBannerTextAnchor.BottomEnd -> Alignment.BottomEnd
}

internal fun horizontalAlignmentFor(anchor: PopImageBannerTextAnchor): Alignment.Horizontal = when (anchor) {
    PopImageBannerTextAnchor.TopEnd,
    PopImageBannerTextAnchor.CenterEnd,
    PopImageBannerTextAnchor.BottomEnd -> Alignment.End
    PopImageBannerTextAnchor.TopCenter,
    PopImageBannerTextAnchor.Center,
    PopImageBannerTextAnchor.BottomCenter -> Alignment.CenterHorizontally
    else -> Alignment.Start
}

internal fun scrimBrush(anchor: PopImageBannerTextAnchor, color: Color): Brush = when (anchor) {
    PopImageBannerTextAnchor.TopStart,
    PopImageBannerTextAnchor.TopCenter,
    PopImageBannerTextAnchor.TopEnd -> Brush.verticalGradient(
        0f to color, 0.6f to Color.Transparent,
    )
    PopImageBannerTextAnchor.BottomStart,
    PopImageBannerTextAnchor.BottomCenter,
    PopImageBannerTextAnchor.BottomEnd -> Brush.verticalGradient(
        0.4f to Color.Transparent, 1f to color,
    )
    PopImageBannerTextAnchor.CenterStart -> Brush.horizontalGradient(
        0f to color, 0.6f to Color.Transparent,
    )
    PopImageBannerTextAnchor.CenterEnd -> Brush.horizontalGradient(
        0.4f to Color.Transparent, 1f to color,
    )
    PopImageBannerTextAnchor.Center -> Brush.radialGradient(
        0f to color, 1f to Color.Transparent,
    )
}
