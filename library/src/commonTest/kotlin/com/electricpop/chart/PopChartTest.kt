package com.electricpop.chart

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Unit tests for internal helpers in PopChart.kt.
 * All tests exercise real helper logic — no stdlib-only assertions.
 */
class PopChartTest {

    // ── normalizePoints ──────────────────────────────────────────────────────

    @Test
    fun normalizePoints_emptyInput_returnsEmpty() {
        val result = normalizePoints(emptyList(), 0f, 100f, 300f, 200f)
        assertTrue(result.isEmpty())
    }

    @Test
    fun normalizePoints_singleValue_xAtCenter() {
        val result = normalizePoints(listOf(50f), 0f, 100f, 300f, 200f)
        assertEquals(1, result.size)
        assertEquals(150f, result[0].x, 1e-3f)
    }

    @Test
    fun normalizePoints_flatSeries_allYAtHalfHeight() {
        // yMin == yMax triggers flat-series path
        val result = normalizePoints(listOf(5f, 5f, 5f), 5f, 5f, 300f, 200f)
        assertEquals(3, result.size)
        result.forEach { offset ->
            assertEquals(200f * 0.5f, offset.y, 1e-3f)
        }
    }

    @Test
    fun normalizePoints_normalSeries_firstXIsZeroLastXIsWidth() {
        val values = listOf(10f, 50f, 90f)
        val result = normalizePoints(values, 0f, 100f, 300f, 200f)
        assertEquals(3, result.size)
        assertEquals(0f, result.first().x, 1e-3f)
        assertEquals(300f, result.last().x, 1e-3f)
    }

    @Test
    fun normalizePoints_inversion_higherValueSmallerY() {
        // y is inverted: higher value → smaller canvas y
        val values = listOf(10f, 90f) // 90f should produce smaller y
        val result = normalizePoints(values, 0f, 100f, 300f, 200f)
        assertTrue(result[1].y < result[0].y, "Higher value should map to smaller y (canvas is top-down)")
    }

    @Test
    fun normalizePoints_nanFiltered_skippedInOutput() {
        val values = listOf(10f, Float.NaN, 90f)
        val result = normalizePoints(values, 0f, 100f, 300f, 200f)
        // NaN is filtered; only 2 valid values remain
        assertEquals(2, result.size)
    }

    // ── computeYBounds ───────────────────────────────────────────────────────

    @Test
    fun computeYBounds_emptySeriesList_returnsSentinel() {
        val (lo, hi) = computeYBounds(emptyList())
        assertEquals(0f, lo, 1e-3f)
        assertEquals(1f, hi, 1e-3f)
    }

    @Test
    fun computeYBounds_allEmptySeries_returnsSentinel() {
        val series = listOf(
            PopChartSeries("A", emptyList()),
            PopChartSeries("B", emptyList()),
        )
        val (lo, hi) = computeYBounds(series)
        assertEquals(0f, lo, 1e-3f)
        assertEquals(1f, hi, 1e-3f)
    }

    @Test
    fun computeYBounds_oneSeries_minMaxWith10PercentPadding() {
        val series = listOf(PopChartSeries("A", listOf(10f, 20f, 30f)))
        val (lo, hi) = computeYBounds(series)
        val expectedPadding = (30f - 10f) * 0.10f
        assertEquals(10f - expectedPadding, lo, 1e-3f)
        assertEquals(30f + expectedPadding, hi, 1e-3f)
    }

    @Test
    fun computeYBounds_multiSeries_usesGlobalMinMax() {
        val series = listOf(
            PopChartSeries("A", listOf(5f, 50f)),
            PopChartSeries("B", listOf(1f, 100f)),
        )
        val (lo, hi) = computeYBounds(series)
        val expectedPadding = (100f - 1f) * 0.10f
        assertEquals(1f - expectedPadding, lo, 1e-3f)
        assertEquals(100f + expectedPadding, hi, 1e-3f)
    }

    @Test
    fun computeYBounds_singleUniqueValue_extendedBeforePadding() {
        // single value: min=max=42 → extended ±0.5 → 41.5..42.5 → padding applied
        val series = listOf(PopChartSeries("A", listOf(42f)))
        val (lo, hi) = computeYBounds(series)
        val extMin = 42f - 0.5f
        val extMax = 42f + 0.5f
        val padding = (extMax - extMin) * 0.10f
        assertEquals(extMin - padding, lo, 1e-3f)
        assertEquals(extMax + padding, hi, 1e-3f)
    }

    // ── clampActiveIndex ─────────────────────────────────────────────────────

    @Test
    fun clampActiveIndex_nullInput_returnsNull() {
        assertNull(clampActiveIndex(null, 5))
    }

    @Test
    fun clampActiveIndex_seriesSizeZero_returnsNull() {
        assertNull(clampActiveIndex(0, 0))
    }

    @Test
    fun clampActiveIndex_inRange_returnsSameValue() {
        assertEquals(3, clampActiveIndex(3, 5))
    }

    @Test
    fun clampActiveIndex_negative_returnsNull() {
        assertNull(clampActiveIndex(-1, 5))
    }

    @Test
    fun clampActiveIndex_equalToSize_returnsNull() {
        // index >= seriesSize is out of range
        assertNull(clampActiveIndex(5, 5))
    }

    @Test
    fun clampActiveIndex_greaterThanSize_returnsNull() {
        assertNull(clampActiveIndex(10, 5))
    }

    @Test
    fun clampActiveIndex_zeroIndex_validRange_returnsZero() {
        assertEquals(0, clampActiveIndex(0, 3))
    }

    @Test
    fun clampActiveIndex_lastIndex_returnsLastIndex() {
        assertEquals(4, clampActiveIndex(4, 5))
    }

    // ── monotoneCubicTangents ────────────────────────────────────────────────

    @Test
    fun monotoneCubicTangents_emptyPoints_returnsEmpty() {
        val result = monotoneCubicTangents(emptyList())
        assertTrue(result.isEmpty())
    }

    @Test
    fun monotoneCubicTangents_singlePoint_returnsZeroTangent() {
        val result = monotoneCubicTangents(listOf(Offset(0f, 0f)))
        assertEquals(1, result.size)
        assertEquals(0f, result[0].x, 1e-3f)
        assertEquals(0f, result[0].y, 1e-3f)
    }

    @Test
    fun monotoneCubicTangents_twoPoints_tangentEqualsChordSlope() {
        // Two points: (0,0) → (10,20). Chord slope = 20/10 = 2.
        // Both tangents should be based on the chord slope.
        val points = listOf(Offset(0f, 0f), Offset(10f, 20f))
        val result = monotoneCubicTangents(points)
        assertEquals(2, result.size)
        // tangents[0] = deltas[0] = 2, stored as dy = tangent * dx
        // tangents[1] = deltas[n-2] = 2
        // Offset.y = tangent * dx = 2 * 10 = 20
        assertEquals(20f, result[0].y, 1e-3f)
        assertEquals(20f, result[1].y, 1e-3f)
    }

    @Test
    fun monotoneCubicTangents_monotoneIncreasing_tangentsNonNegative() {
        val points = listOf(
            Offset(0f, 100f),
            Offset(10f, 80f),
            Offset(20f, 60f),
            Offset(30f, 40f),
        )
        // Canvas y decreases as values increase (inverted axis); monotone increasing in value space
        val result = monotoneCubicTangents(points)
        assertEquals(4, result.size)
        // All dy components should be non-positive (going down in canvas = positive value)
        // Actually for monotone decrease in y (which is monotone increase in value), slopes are negative
        result.forEach { tangent ->
            assertTrue(tangent.y <= 1e-3f, "Tangent dy should be non-positive for monotone decreasing y: ${tangent.y}")
        }
    }

    @Test
    fun monotoneCubicTangents_flatSegment_zeroTangents() {
        // Flat: all y identical → slope = 0 → Fritsch-Carlson zeroes out tangents
        val points = listOf(Offset(0f, 50f), Offset(10f, 50f), Offset(20f, 50f))
        val result = monotoneCubicTangents(points)
        assertEquals(3, result.size)
        result.forEach { tangent ->
            assertEquals(0f, tangent.y, 1e-3f)
        }
    }

    // ── defaultSeriesColor ───────────────────────────────────────────────────

    private fun makeScheme(): ColorScheme {
        // Use lightColorScheme defaults so primaryContainer / tertiaryContainer / secondaryContainer are distinct
        return lightColorScheme()
    }

    @Test
    fun defaultSeriesColor_index0_returnsPrimaryContainer() {
        val scheme = makeScheme()
        val color = defaultSeriesColor(0, scheme)
        assertEquals(scheme.primaryContainer, color)
    }

    @Test
    fun defaultSeriesColor_index1_returnsTertiaryContainer() {
        val scheme = makeScheme()
        val color = defaultSeriesColor(1, scheme)
        assertEquals(scheme.tertiaryContainer, color)
    }

    @Test
    fun defaultSeriesColor_index2_returnsSecondaryContainer() {
        val scheme = makeScheme()
        val color = defaultSeriesColor(2, scheme)
        assertEquals(scheme.secondaryContainer, color)
    }

    @Test
    fun defaultSeriesColor_index3_cyclesBackToPrimaryContainer() {
        val scheme = makeScheme()
        val color3 = defaultSeriesColor(3, scheme)
        val color0 = defaultSeriesColor(0, scheme)
        assertEquals(color0, color3)
    }

    @Test
    fun defaultSeriesColor_index4_cyclesBackToTertiaryContainer() {
        val scheme = makeScheme()
        val color4 = defaultSeriesColor(4, scheme)
        val color1 = defaultSeriesColor(1, scheme)
        assertEquals(color1, color4)
    }
}

// Helper for float comparison with tolerance
private fun assertEquals(expected: Float, actual: Float, tolerance: Float) {
    assertTrue(
        kotlin.math.abs(expected - actual) <= tolerance,
        "Expected $expected but was $actual (tolerance $tolerance)",
    )
}
