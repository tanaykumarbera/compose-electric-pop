package co.tanay.electricpop.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography

@Composable
fun ElectricPopTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    colorScheme: ColorScheme? = null,
    typography: Typography = ElectricPopTypography(),
    shapes: Shapes = ElectricPopShapes,
    spacing: ElectricPopSpacing = ElectricPopSpacing(),
    content: @Composable () -> Unit,
) {
    val colors = colorScheme ?: if (darkTheme) {
        ElectricPopDarkColorScheme
    } else {
        ElectricPopLightColorScheme
    }

    CompositionLocalProvider(
        LocalElectricPopSpacing provides spacing,
    ) {
        MaterialTheme(
            colorScheme = colors,
            typography = typography,
            shapes = shapes,
            content = content,
        )
    }
}

object ElectricPopTheme {
    val spacing: ElectricPopSpacing
        @Composable
        @ReadOnlyComposable
        get() = LocalElectricPopSpacing.current
}
