package co.tanay.electricpop.foundation

import androidx.compose.ui.graphics.Color
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PopSurfaceTest {

    // Color stores channels as 8-bit values internally, so we need tolerance
    // of ~1/255 = ~0.004 to account for quantization.
    private val colorTolerance = 0.005f

    @Test
    fun darkenByZeroReturnsOriginalColor() {
        val color = Color(red = 0.8f, green = 0.6f, blue = 0.4f, alpha = 1.0f)
        val result = color.darken(0.0f)
        assertEquals(color.red, result.red, colorTolerance)
        assertEquals(color.green, result.green, colorTolerance)
        assertEquals(color.blue, result.blue, colorTolerance)
        assertEquals(color.alpha, result.alpha, colorTolerance)
    }

    @Test
    fun darkenByTenPercentReducesChannels() {
        val color = Color(red = 1.0f, green = 0.5f, blue = 0.0f, alpha = 1.0f)
        val result = color.darken(0.10f)
        assertEquals(0.90f, result.red, colorTolerance)
        assertEquals(0.45f, result.green, colorTolerance)
        assertEquals(0.0f, result.blue, colorTolerance)
        assertEquals(1.0f, result.alpha, colorTolerance)
    }

    @Test
    fun darkenByOneReturnsBlack() {
        val color = Color(red = 0.8f, green = 0.6f, blue = 0.4f, alpha = 0.75f)
        val result = color.darken(1.0f)
        assertEquals(0.0f, result.red, colorTolerance)
        assertEquals(0.0f, result.green, colorTolerance)
        assertEquals(0.0f, result.blue, colorTolerance)
        // Alpha preserved
        assertEquals(0.75f, result.alpha, colorTolerance)
    }

    @Test
    fun darkenPreservesAlpha() {
        val color = Color(red = 0.5f, green = 0.5f, blue = 0.5f, alpha = 0.3f)
        val result = color.darken(0.5f)
        assertEquals(0.3f, result.alpha, colorTolerance)
    }

    @Test
    fun darkenProducesStrictlyDarkerColor() {
        val color = Color(red = 0.8f, green = 0.6f, blue = 0.4f, alpha = 1.0f)
        val result = color.darken(0.10f)
        assertTrue(result.red < color.red, "Red channel should be darker")
        assertTrue(result.green < color.green, "Green channel should be darker")
        assertTrue(result.blue < color.blue, "Blue channel should be darker")
    }

    @Test
    fun popSurfaceToneEntriesCoverAllLevels() {
        // Verify all 7 tonal levels are defined
        val tones = PopSurfaceTone.entries
        assertEquals(7, tones.size)
        assertTrue(tones.contains(PopSurfaceTone.Default))
        assertTrue(tones.contains(PopSurfaceTone.Low))
        assertTrue(tones.contains(PopSurfaceTone.Lowest))
        assertTrue(tones.contains(PopSurfaceTone.High))
        assertTrue(tones.contains(PopSurfaceTone.Highest))
        assertTrue(tones.contains(PopSurfaceTone.Bright))
        assertTrue(tones.contains(PopSurfaceTone.Dim))
    }

    @Test
    fun visualValidationViaDemo() {
        // PopSurface visual appearance (squircle shape, tonal shadow rendering,
        // ghost border) is validated via the demo app and screenshot tests.
    }
}
