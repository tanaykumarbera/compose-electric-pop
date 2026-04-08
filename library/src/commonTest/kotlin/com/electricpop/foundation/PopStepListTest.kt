package com.electricpop.foundation

import kotlin.test.Test
import kotlin.test.assertEquals

class PopStepListTest {
    @Test
    fun formatStepNumber_singleDigit_zeroPadded() {
        assertEquals("01", formatStepNumber(1))
        assertEquals("09", formatStepNumber(9))
    }

    @Test
    fun formatStepNumber_doubleDigit_notPadded() {
        assertEquals("10", formatStepNumber(10))
        assertEquals("99", formatStepNumber(99))
    }

    @Test
    fun formatStepNumber_zero() {
        assertEquals("00", formatStepNumber(0))
    }

    @Test
    fun formatStepNumber_tripleDigit() {
        assertEquals("100", formatStepNumber(100))
    }
}
