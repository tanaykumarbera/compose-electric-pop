package com.electricpop.foundation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.electricpop.theme.ElectricPopTheme

/**
 * Tonal levels for [PopSurface], mapping to the Material3 surface color hierarchy.
 *
 * Each level resolves to the corresponding `surfaceContainer*` token from
 * [MaterialTheme.colorScheme], providing tonal separation without borders
 * (Design Rule 1: No-Line Rule).
 */
enum class PopSurfaceTone {
    /** `surfaceContainer` — default card-level surface */
    Default,
    /** `surfaceContainerLow` — slightly lighter than default */
    Low,
    /** `surfaceContainerLowest` — lightest surface level (white in light theme) */
    Lowest,
    /** `surfaceContainerHigh` — slightly darker than default */
    High,
    /** `surfaceContainerHighest` — darkest container level */
    Highest,
    /** `surfaceBright` — bright variant */
    Bright,
    /** `surfaceDim` — dim variant */
    Dim,
}

/**
 * Resolves a [PopSurfaceTone] to its corresponding color from the current theme.
 */
@Composable
fun PopSurfaceTone.toColor(): Color {
    val scheme = MaterialTheme.colorScheme
    return when (this) {
        PopSurfaceTone.Default -> scheme.surfaceContainer
        PopSurfaceTone.Low -> scheme.surfaceContainerLow
        PopSurfaceTone.Lowest -> scheme.surfaceContainerLowest
        PopSurfaceTone.High -> scheme.surfaceContainerHigh
        PopSurfaceTone.Highest -> scheme.surfaceContainerHighest
        PopSurfaceTone.Bright -> scheme.surfaceBright
        PopSurfaceTone.Dim -> scheme.surfaceDim
    }
}

/**
 * Darkens a color by the given [fraction] (0.0 = no change, 1.0 = black).
 *
 * Used internally to compute tonal shadow colors per Design Rule 2.
 */
internal fun Color.darken(fraction: Float): Color {
    return Color(
        red = red * (1f - fraction),
        green = green * (1f - fraction),
        blue = blue * (1f - fraction),
        alpha = alpha,
    )
}

/**
 * A themed container with squircle shape and tonal shadow.
 *
 * PopSurface is the foundational container for the Electric Pop design system.
 * It enforces the core design rules:
 *
 * - **Rule 1 (No-Line):** No 1px borders. Tonal separation via [tone].
 * - **Rule 2 (Tonal Shadows):** Shadow color matches background, darkened 10%,
 *   32dp blur, 0 offset. No grey elevation shadows.
 * - **Rule 3 (Ghost Border):** Optional accessibility border using
 *   `outlineVariant` at 15% opacity — enabled via [ghostBorder].
 * - **Rule 6 (Squircle Radii):** Uses [ElectricPopShapes] by default.
 *
 * Many composite components (PopFeatureCard, PopBannerCard, PopDashboardCard, etc.)
 * compose from PopSurface as their outer container.
 *
 * @param modifier Optional [Modifier] applied to the container.
 * @param tone The tonal level from the surface hierarchy. Defaults to [PopSurfaceTone.Default].
 * @param shape The clip and shadow shape. Defaults to [MaterialTheme.shapes.extraLarge] (squircle XL).
 * @param shadowEnabled Whether to draw the tonal shadow. Defaults to true.
 * @param ghostBorder Whether to draw the accessibility ghost border. Defaults to false.
 * @param containerColor Optional override for the background color. When null, resolved from [tone].
 * @param contentColor The content color passed through for children to read (informational).
 * @param content The composable content placed inside the surface.
 */
@Composable
fun PopSurface(
    modifier: Modifier = Modifier,
    tone: PopSurfaceTone = PopSurfaceTone.Default,
    shape: Shape = MaterialTheme.shapes.extraLarge,
    shadowEnabled: Boolean = true,
    ghostBorder: Boolean = false,
    containerColor: Color = Color.Unspecified,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    content: @Composable () -> Unit,
) {
    val resolvedColor = if (containerColor == Color.Unspecified) {
        tone.toColor()
    } else {
        containerColor
    }

    // Design Rule 2: Tonal shadow — background darkened 10%, 32dp blur, 0 offset
    val shadowColor = resolvedColor.darken(0.10f)

    // Design Rule 3: Ghost border — outlineVariant at 15% opacity
    val ghostBorderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.15f)

    val shadowModifier = if (shadowEnabled) {
        Modifier.shadow(
            elevation = 32.dp,
            shape = shape,
            ambientColor = shadowColor,
            spotColor = shadowColor,
        )
    } else {
        Modifier
    }

    val borderModifier = if (ghostBorder) {
        Modifier.border(
            width = 1.dp,
            color = ghostBorderColor,
            shape = shape,
        )
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .then(shadowModifier)
            .then(borderModifier)
            .clip(shape)
            .background(resolvedColor),
    ) {
        content()
    }
}
