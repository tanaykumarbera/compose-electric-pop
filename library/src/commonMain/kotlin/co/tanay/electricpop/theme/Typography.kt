package co.tanay.electricpop.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import compose_electric_pop.library.generated.resources.Res
import compose_electric_pop.library.generated.resources.jetbrains_mono
import compose_electric_pop.library.generated.resources.manrope
import compose_electric_pop.library.generated.resources.space_grotesk
import org.jetbrains.compose.resources.Font

/**
 * Creates the Space Grotesk [FontFamily] from bundled resources.
 *
 * Space Grotesk is a variable font, so a single file covers all weights.
 * We register it for the specific weights used in the type scale.
 */
@Composable
fun SpaceGroteskFontFamily(): FontFamily = FontFamily(
    Font(Res.font.space_grotesk, weight = FontWeight.Normal),
    Font(Res.font.space_grotesk, weight = FontWeight.Medium),
    Font(Res.font.space_grotesk, weight = FontWeight.Bold),
    Font(Res.font.space_grotesk, weight = FontWeight.Black),
)

/**
 * Creates the Manrope [FontFamily] from bundled resources.
 *
 * Manrope is a variable font covering ExtraLight to ExtraBold.
 */
@Composable
fun ManropeFontFamily(): FontFamily = FontFamily(
    Font(Res.font.manrope, weight = FontWeight.Normal),
    Font(Res.font.manrope, weight = FontWeight.Medium),
    Font(Res.font.manrope, weight = FontWeight.Bold),
    Font(Res.font.manrope, weight = FontWeight.ExtraBold),
)

/**
 * Creates the JetBrains Mono [FontFamily] from bundled resources.
 *
 * JetBrains Mono is the monospace face used by [co.tanay.electricpop.foundation.PopCodeBlock]
 * and any other code/IDE-style surface in the design system. Bundling it (rather than relying
 * on `FontFamily.Monospace`, which resolves to a platform default) gives byte-identical
 * rendering on Android, Desktop, and iOS — a hard requirement for the screenshot test suite.
 *
 * JetBrains Mono is a variable font; a single file covers Thin → ExtraBold.
 */
@Composable
fun JetBrainsMonoFontFamily(): FontFamily = FontFamily(
    Font(Res.font.jetbrains_mono, weight = FontWeight.Normal),
    Font(Res.font.jetbrains_mono, weight = FontWeight.Medium),
    Font(Res.font.jetbrains_mono, weight = FontWeight.Bold),
)

/**
 * Builds the Electric Pop [Typography] with bundled fonts.
 *
 * Headlines: Space Grotesk Black Italic, -0.02em
 * Body: Manrope Regular, 18px/1.5
 * Labels: Manrope ExtraBold, 0.1em spacing
 */
@Composable
fun ElectricPopTypography(): Typography {
    val spaceGrotesk = SpaceGroteskFontFamily()
    val manrope = ManropeFontFamily()

    return Typography(
        displayLarge = TextStyle(
            fontFamily = spaceGrotesk,
            fontWeight = FontWeight.Black,
            fontStyle = FontStyle.Italic,
            fontSize = 57.sp,
            letterSpacing = (-0.02).sp,
        ),
        displayMedium = TextStyle(
            fontFamily = spaceGrotesk,
            fontWeight = FontWeight.Black,
            fontStyle = FontStyle.Italic,
            fontSize = 45.sp,
            letterSpacing = (-0.02).sp,
        ),
        displaySmall = TextStyle(
            fontFamily = spaceGrotesk,
            fontWeight = FontWeight.Black,
            fontStyle = FontStyle.Italic,
            fontSize = 36.sp,
            letterSpacing = (-0.02).sp,
        ),
        headlineLarge = TextStyle(
            fontFamily = spaceGrotesk,
            fontWeight = FontWeight.Black,
            fontStyle = FontStyle.Italic,
            fontSize = 32.sp,
            letterSpacing = (-0.02).sp,
        ),
        headlineMedium = TextStyle(
            fontFamily = spaceGrotesk,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
        ),
        headlineSmall = TextStyle(
            fontFamily = spaceGrotesk,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
        ),
        titleLarge = TextStyle(
            fontFamily = spaceGrotesk,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
        ),
        titleMedium = TextStyle(
            fontFamily = manrope,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            letterSpacing = 0.15.sp,
        ),
        titleSmall = TextStyle(
            fontFamily = manrope,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            letterSpacing = 0.1.sp,
        ),
        bodyLarge = TextStyle(
            fontFamily = manrope,
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp,
            lineHeight = 27.sp, // 18 * 1.5
        ),
        bodyMedium = TextStyle(
            fontFamily = manrope,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 20.sp,
        ),
        bodySmall = TextStyle(
            fontFamily = manrope,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 16.sp,
        ),
        labelLarge = TextStyle(
            fontFamily = manrope,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 14.sp,
            letterSpacing = 0.1.sp,
        ),
        labelMedium = TextStyle(
            fontFamily = manrope,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 12.sp,
            letterSpacing = 0.1.sp,
        ),
        labelSmall = TextStyle(
            fontFamily = manrope,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 11.sp,
            letterSpacing = 0.1.sp,
        ),
    )
}
