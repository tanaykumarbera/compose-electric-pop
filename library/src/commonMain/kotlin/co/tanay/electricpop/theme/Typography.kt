package co.tanay.electricpop.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import compose_electric_pop.library.generated.resources.Res
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
