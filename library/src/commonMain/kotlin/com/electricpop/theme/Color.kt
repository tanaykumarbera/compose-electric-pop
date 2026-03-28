package com.electricpop.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Primary — Electric Lime
val ElectricLime = Color(0xFF4E6300)
val ElectricLimeContainer = Color(0xFFCAFD00)
val OnElectricLime = Color(0xFFE1FF88)
val OnElectricLimeContainer = Color(0xFF4A5E00)
val ElectricLimeDim = Color(0xFF435600)

// Secondary — Neon Magenta
val NeonMagenta = Color(0xFFA400A4)
val NeonMagentaContainer = Color(0xFFFFBDF3)
val OnNeonMagenta = Color(0xFFFFEEF8)
val OnNeonMagentaContainer = Color(0xFF820082)

// Tertiary — Cyber Cyan
val CyberCyan = Color(0xFF006666)
val CyberCyanContainer = Color(0xFF00FFFF)
val OnCyberCyan = Color(0xFFBBFFFE)
val OnCyberCyanContainer = Color(0xFF005D5D)

// Error
val PopError = Color(0xFFB02500)
val PopErrorContainer = Color(0xFFF95630)
val OnPopError = Color(0xFFFFEFEC)
val OnPopErrorContainer = Color(0xFF520C00)

// Surface hierarchy (light)
val PopSurface = Color(0xFFF5F6F7)
val PopSurfaceBright = Color(0xFFF5F6F7)
val PopSurfaceDim = Color(0xFFD1D5D7)
val PopSurfaceContainer = Color(0xFFE6E8EA)
val PopSurfaceContainerHigh = Color(0xFFE0E3E4)
val PopSurfaceContainerHighest = Color(0xFFDADDDF)
val PopSurfaceContainerLow = Color(0xFFEFF1F2)
val PopSurfaceContainerLowest = Color(0xFFFFFFFF)

// Outline
val PopOutline = Color(0xFF757778)
val PopOutlineVariant = Color(0xFFABADAE)

// On-surface
val PopOnBackground = Color(0xFF2C2F30)
val PopOnSurface = Color(0xFF2C2F30)
val PopOnSurfaceVariant = Color(0xFF595C5D)

// Inverse
val PopInverseSurface = Color(0xFF0C0F10)
val PopInverseOnSurface = Color(0xFF9B9D9E)
val PopInversePrimary = Color(0xFFCAFD00)

val ElectricPopLightColorScheme = lightColorScheme(
    primary = ElectricLime,
    onPrimary = OnElectricLime,
    primaryContainer = ElectricLimeContainer,
    onPrimaryContainer = OnElectricLimeContainer,
    secondary = NeonMagenta,
    onSecondary = OnNeonMagenta,
    secondaryContainer = NeonMagentaContainer,
    onSecondaryContainer = OnNeonMagentaContainer,
    tertiary = CyberCyan,
    onTertiary = OnCyberCyan,
    tertiaryContainer = CyberCyanContainer,
    onTertiaryContainer = OnCyberCyanContainer,
    error = PopError,
    onError = OnPopError,
    errorContainer = PopErrorContainer,
    onErrorContainer = OnPopErrorContainer,
    background = PopSurface,
    onBackground = PopOnBackground,
    surface = PopSurface,
    onSurface = PopOnSurface,
    onSurfaceVariant = PopOnSurfaceVariant,
    surfaceBright = PopSurfaceBright,
    surfaceDim = PopSurfaceDim,
    surfaceContainer = PopSurfaceContainer,
    surfaceContainerHigh = PopSurfaceContainerHigh,
    surfaceContainerHighest = PopSurfaceContainerHighest,
    surfaceContainerLow = PopSurfaceContainerLow,
    surfaceContainerLowest = PopSurfaceContainerLowest,
    outline = PopOutline,
    outlineVariant = PopOutlineVariant,
    inverseSurface = PopInverseSurface,
    inverseOnSurface = PopInverseOnSurface,
    inversePrimary = PopInversePrimary,
    surfaceTint = ElectricLime,
)

// Dark surface hierarchy — extracted from Stitch dark variant screens
val PopSurfaceDark = Color(0xFF0C0F10)
val PopSurfaceBrightDark = Color(0xFF1A1D1E)
val PopSurfaceDimDark = Color(0xFF040405)
val PopSurfaceContainerDark = Color(0xFF1A1D1E)
val PopSurfaceContainerHighDark = Color(0xFF252829)
val PopSurfaceContainerHighestDark = Color(0xFF2F3233)
val PopSurfaceContainerLowDark = Color(0xFF141718)
val PopSurfaceContainerLowestDark = Color(0xFF0C0F10)
val PopOnSurfaceDark = Color(0xFFE0E3E4)
val PopOnSurfaceVariantDark = Color(0xFF9B9D9E)
val PopOutlineDark = Color(0xFF4B4E4E)
val PopOutlineVariantDark = Color(0xFF3A3D3E)

// Dark scheme — derived from Stitch dark variant screens
// Primary (lime) stays vivid, containers are muted
val ElectricPopDarkColorScheme = darkColorScheme(
    primary = ElectricLimeContainer,          // #CAFD00 — lime as headline/accent color
    onPrimary = Color(0xFF1C2600),            // dark text on lime
    primaryContainer = Color(0xFF3C4917),     // muted lime for containers
    onPrimaryContainer = ElectricLimeContainer, // #CAFD00 text on dark lime
    secondary = Color(0xFFFFB1EE),            // lighter magenta for dark
    onSecondary = Color(0xFF5C005C),          // dark text on light magenta
    secondaryContainer = Color(0xFF7A007A),   // deep magenta containers
    onSecondaryContainer = NeonMagentaContainer, // light pink text
    tertiary = CyberCyanContainer,            // #00FFFF — cyan stays vivid
    onTertiary = Color(0xFF003333),           // dark text on cyan
    tertiaryContainer = Color(0xFF004D4D),    // deep teal containers
    onTertiaryContainer = CyberCyanContainer, // vivid cyan text
    error = Color(0xFFFF897A),                // lighter red for dark
    onError = Color(0xFF3D0700),
    errorContainer = Color(0xFF7A1A00),
    onErrorContainer = Color(0xFFFFDAD4),
    background = PopSurfaceDark,
    onBackground = PopOnSurfaceDark,
    surface = PopSurfaceDark,
    onSurface = PopOnSurfaceDark,
    onSurfaceVariant = PopOnSurfaceVariantDark,
    surfaceBright = PopSurfaceBrightDark,
    surfaceDim = PopSurfaceDimDark,
    surfaceContainer = PopSurfaceContainerDark,
    surfaceContainerHigh = PopSurfaceContainerHighDark,
    surfaceContainerHighest = PopSurfaceContainerHighestDark,
    surfaceContainerLow = PopSurfaceContainerLowDark,
    surfaceContainerLowest = PopSurfaceContainerLowestDark,
    outline = PopOutlineDark,
    outlineVariant = PopOutlineVariantDark,
    inverseSurface = PopSurface,
    inverseOnSurface = PopOnSurface,
    inversePrimary = ElectricLime,
    surfaceTint = ElectricLimeContainer,
)
