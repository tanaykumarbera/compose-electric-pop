package com.electricpop.chart

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class PopLineChartTest {

    // ---- autoYRange tests ----

    @Test
    fun autoYRange_singleSeries_returnsMinMax() {
        val series = listOf(PopLineChartSeries("A", listOf(5f, 10f, 15f)))
        val range = autoYRange(series)
        assertEquals(5f, range.start, absoluteTolerance = 0.1f)
        assertEquals(15f, range.endInclusive, absoluteTolerance = 0.1f)
    }

    @Test
    fun autoYRange_multiSeries_combinesAcrossSeries() {
        val series = listOf(
            PopLineChartSeries("A", listOf(0f, 5f)),
            PopLineChartSeries("B", listOf(-2f, 20f)),
        )
        val range = autoYRange(series)
        assertEquals(-2f, range.start, absoluteTolerance = 0.1f)
        assertEquals(20f, range.endInclusive, absoluteTolerance = 0.1f)
    }

    @Test
    fun autoYRange_empty_returnsDefaultRange() {
        val range = autoYRange(emptyList())
        assertEquals(0f, range.start, absoluteTolerance = 0.1f)
        assertEquals(1f, range.endInclusive, absoluteTolerance = 0.1f)
    }

    @Test
    fun autoYRange_allSameValue_padsRange() {
        val series = listOf(PopLineChartSeries("A", listOf(7f, 7f, 7f)))
        val range = autoYRange(series)
        assertEquals(6f, range.start, absoluteTolerance = 0.1f)
        assertEquals(8f, range.endInclusive, absoluteTolerance = 0.1f)
    }

    @Test
    fun autoYRange_skipsNonFiniteValues() {
        val series = listOf(
            PopLineChartSeries("A", listOf(Float.NaN, 5f, Float.POSITIVE_INFINITY, 10f)),
        )
        val range = autoYRange(series)
        assertEquals(5f, range.start, absoluteTolerance = 0.1f)
        assertEquals(10f, range.endInclusive, absoluteTolerance = 0.1f)
    }

    @Test
    fun autoYRange_onlyNonFiniteValues_returnsDefault() {
        val series = listOf(
            PopLineChartSeries("A", listOf(Float.NaN, Float.NEGATIVE_INFINITY)),
        )
        val range = autoYRange(series)
        assertEquals(0f, range.start, absoluteTolerance = 0.1f)
        assertEquals(1f, range.endInclusive, absoluteTolerance = 0.1f)
    }

    // ---- defaultYAxisFormatter tests ----

    @Test
    fun defaultYAxisFormatter_smallInt_plain() {
        assertEquals("42", defaultYAxisFormatter(42f))
    }

    @Test
    fun defaultYAxisFormatter_thousands_abbreviated() {
        assertEquals("1.2K", defaultYAxisFormatter(1234f))
        assertEquals("12.3K", defaultYAxisFormatter(12345f))
    }

    @Test
    fun defaultYAxisFormatter_millions_abbreviated() {
        assertEquals("1.2M", defaultYAxisFormatter(1_234_567f))
    }

    @Test
    fun defaultYAxisFormatter_negative() {
        assertEquals("-1.5K", defaultYAxisFormatter(-1500f))
    }

    @Test
    fun defaultYAxisFormatter_zero() {
        assertEquals("0", defaultYAxisFormatter(0f))
    }

    // ---- normalizeSeries tests ----

    @Test
    fun normalizeSeries_empty_returnsEmpty() {
        val result = normalizeSeries(emptyList(), Size(100f, 100f), null)
        assertTrue(result.isEmpty())
    }

    @Test
    fun normalizeSeries_singleSeries_mapsPointsToOffsets() {
        val series = listOf(PopLineChartSeries("A", listOf(0f, 10f)))
        val result = normalizeSeries(series, Size(100f, 100f), null)
        assertEquals(1, result.size)
        val offsets = result[0].sampledOffsets
        assertEquals(2, offsets.size)

        val o0 = offsets[0]!!
        val o1 = offsets[1]!!
        assertEquals(0f, o0.x, absoluteTolerance = 0.1f)
        assertEquals(100f, o0.y, absoluteTolerance = 0.1f)
        assertEquals(100f, o1.x, absoluteTolerance = 0.1f)
        assertEquals(0f, o1.y, absoluteTolerance = 0.1f)
    }

    @Test
    fun normalizeSeries_nanIsGap() {
        val series = listOf(PopLineChartSeries("A", listOf(1f, Float.NaN, 3f)))
        val result = normalizeSeries(series, Size(100f, 100f), 0f..3f)
        assertEquals(1, result.size)
        val offsets = result[0].sampledOffsets
        assertEquals(3, offsets.size)
        assertTrue(offsets[0] != null)
        assertNull(offsets[1])
        assertTrue(offsets[2] != null)
    }

    @Test
    fun normalizeSeries_respectsExplicitYRange() {
        val series = listOf(PopLineChartSeries("A", listOf(10f, 20f)))
        val result = normalizeSeries(series, Size(100f, 100f), 0f..100f)
        assertEquals(1, result.size)
        val offsets = result[0].sampledOffsets
        val y0 = offsets[0]!!.y
        val y1 = offsets[1]!!.y
        assertEquals(90f, y0, absoluteTolerance = 0.1f)
        assertEquals(80f, y1, absoluteTolerance = 0.1f)
    }
}

private fun assertEquals(expected: Float, actual: Float, absoluteTolerance: Float) {
    val diff = kotlin.math.abs(expected - actual)
    assertTrue(
        diff <= absoluteTolerance,
        "Expected $expected but was $actual (tolerance $absoluteTolerance, diff $diff)",
    )
}
