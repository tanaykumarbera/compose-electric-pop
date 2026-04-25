package co.tanay.electricpop.foundation

import kotlin.test.Test
import kotlin.test.assertEquals

class PopSliderTest {

    @Test
    fun formatSliderValue_integer_noDecimal() {
        assertEquals("0", formatSliderValue(0f))
        assertEquals("1", formatSliderValue(1f))
        assertEquals("50", formatSliderValue(50f))
        assertEquals("100", formatSliderValue(100f))
    }

    @Test
    fun formatSliderValue_decimal_roundsToOnePlace() {
        assertEquals("0.5", formatSliderValue(0.5f))
        assertEquals("0.3", formatSliderValue(0.3f))
        assertEquals("1.7", formatSliderValue(1.7f))
        assertEquals("99.9", formatSliderValue(99.9f))
    }

    @Test
    fun formatSliderValue_nearInteger_showsInteger() {
        // Values that are effectively integers after rounding
        assertEquals("1", formatSliderValue(1.0f))
        assertEquals("0", formatSliderValue(0.0f))
    }

    @Test
    fun formatSliderValue_negativeValues() {
        assertEquals("-1", formatSliderValue(-1f))
        assertEquals("-0.5", formatSliderValue(-0.5f))
    }
}
