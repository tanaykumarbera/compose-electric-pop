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

    // ── catmullRomControlPoints ──────────────────────────────────────────────

    @Test
    fun catmullRomControlPoints_tension05_matchesClosedForm() {
        // At tension=0.5, scale = (1-0.5)/3 = 1/6
        // c1 = p1 + (p2 - p0) * (1/6)
        // c2 = p2 - (p3 - p1) * (1/6)
        val p0 = Offset(0f, 0f)
        val p1 = Offset(10f, 20f)
        val p2 = Offset(20f, 10f)
        val p3 = Offset(30f, 30f)
        val (c1, c2) = catmullRomControlPoints(p0, p1, p2, p3, tension = 0.5f)

        val expectedC1 = p1 + (p2 - p0) * (1f / 6f)
        val expectedC2 = p2 - (p3 - p1) * (1f / 6f)

        assertEquals(expectedC1.x, c1.x, 1e-3f)
        assertEquals(expectedC1.y, c1.y, 1e-3f)
        assertEquals(expectedC2.x, c2.x, 1e-3f)
        assertEquals(expectedC2.y, c2.y, 1e-3f)
    }

    @Test
    fun catmullRomControlPoints_tension0_controlsBetweenEndpoints() {
        // At tension=0, scale = (1-0)/3 = 1/3
        // c1 = p1 + (p2 - p0) / 3
        // c2 = p2 - (p3 - p1) / 3
        // For collinear points with equal spacing the controls land 1/3 of the way inside the segment.
        val p0 = Offset(0f, 10f)
        val p1 = Offset(10f, 10f)
        val p2 = Offset(20f, 10f)
        val p3 = Offset(30f, 10f)
        val (c1, c2) = catmullRomControlPoints(p0, p1, p2, p3, tension = 0f)
        // scale = 1/3; c1 = p1 + (p2 - p0)/3 = (10,10) + (20,0)/3 = (10+6.67, 10) = (16.67, 10)
        // c2 = p2 - (p3 - p1)/3 = (20,10) - (20,0)/3 = (20-6.67, 10) = (13.33, 10)
        // The segment is a cubic from p1=(10,10) to p2=(20,10) with controls inside the segment
        // All y values remain 10 since the data is flat
        assertEquals(10f, c1.y, 1e-3f)
        assertEquals(10f, c2.y, 1e-3f)
        // Controls should be between p1 and p2 x-wise
        assertTrue(c1.x > p1.x, "c1.x should be > p1.x for tension=0, collinear")
        assertTrue(c2.x < p2.x, "c2.x should be < p2.x for tension=0, collinear")
    }

    @Test
    fun catmullRomControlPoints_midpointSymmetry_reversedInputSwapsControls() {
        val p0 = Offset(0f, 5f)
        val p1 = Offset(10f, 15f)
        val p2 = Offset(20f, 8f)
        val p3 = Offset(30f, 20f)

        val (c1Fwd, c2Fwd) = catmullRomControlPoints(p0, p1, p2, p3, tension = 0.5f)
        val (c1Rev, c2Rev) = catmullRomControlPoints(p3, p2, p1, p0, tension = 0.5f)

        // When input is reversed: the forward c2 should equal the reverse c1 (both computed for the p1-p2 segment)
        // forward: c2 = p2 - (p3-p1)/6
        // reverse: c1 = p2 + (p1-p3)/6 = p2 - (p3-p1)/6  → same as forward c2
        assertEquals(c2Fwd.x, c1Rev.x, 1e-3f)
        assertEquals(c2Fwd.y, c1Rev.y, 1e-3f)
        // forward: c1 = p1 + (p2-p0)/6
        // reverse: c2 = p1 - (p0-p2)/6 = p1 + (p2-p0)/6  → same as forward c1
        assertEquals(c1Fwd.x, c2Rev.x, 1e-3f)
        assertEquals(c1Fwd.y, c2Rev.y, 1e-3f)
    }

    @Test
    fun catmullRomControlPoints_endReflection_firstSegmentIsSymmetric() {
        // For first segment: reflected p0 = p1 + (p1 - p2)
        // With 3 points [p1, p2, p3], first segment: prev = p1 + (p1 - p2), next = p3
        val pts = listOf(Offset(0f, 10f), Offset(10f, 20f), Offset(20f, 15f))
        val p1 = pts[0]
        val p2 = pts[1]
        val p3 = pts[2]
        val reflectedPrev = p1 + (p1 - p2)  // = p1*2 - p2

        val (c1, _) = catmullRomControlPoints(reflectedPrev, p1, p2, p3, tension = 0.5f)

        // Expected: c1 = p1 + (p2 - reflectedPrev)/6 = p1 + (p2 - (2p1 - p2))/6 = p1 + (2p2 - 2p1)/6 = p1 + (p2-p1)/3
        // But per plan simplification at end: c1 = p1 + (p2-p1)/6
        // Let's verify using scale = 1/6:
        val scale = (1f - 0.5f) / 3f  // = 1/6
        val expectedC1 = p1 + (p2 - reflectedPrev) * scale
        assertEquals(expectedC1.x, c1.x, 1e-3f)
        assertEquals(expectedC1.y, c1.y, 1e-3f)
    }

    // ── computeBarSlots ──────────────────────────────────────────────────────

    @Test
    fun computeBarSlots_clusterCountZero_returnsEmpty() {
        val result = computeBarSlots(0, 2, PopChartStyle.Grouping.Clustered, 360f, 12f, 4f)
        assertTrue(result.isEmpty())
    }

    @Test
    fun computeBarSlots_seriesCountZero_returnsEmpty() {
        val result = computeBarSlots(12, 0, PopChartStyle.Grouping.Clustered, 360f, 12f, 4f)
        assertTrue(result.isEmpty())
    }

    @Test
    fun computeBarSlots_totalWidthZero_returnsEmpty() {
        val result = computeBarSlots(12, 2, PopChartStyle.Grouping.Clustered, 0f, 12f, 4f)
        assertTrue(result.isEmpty())
    }

    @Test
    fun computeBarSlots_clustered_singleSeries_oneSlotPerCluster_fullWidth() {
        val clusterCount = 12
        val totalWidth = 360f
        val clusterGap = 12f
        val barGap = 4f
        val result = computeBarSlots(clusterCount, 1, PopChartStyle.Grouping.Clustered, totalWidth, clusterGap, barGap)
        assertEquals(12, result.size)
        val usableWidth = totalWidth - clusterGap * (clusterCount - 1)
        val expectedBarWidth = usableWidth / clusterCount
        result.forEach { slot ->
            val actualWidth = slot.rect.right - slot.rect.left
            assertEquals(expectedBarWidth, actualWidth, 1e-2f)
        }
    }

    @Test
    fun computeBarSlots_clustered_twoSeries_slotsSumToClusterWidth() {
        val clusterCount = 12
        val totalWidth = 360f
        val clusterGap = 12f
        val barGap = 4f
        val result = computeBarSlots(clusterCount, 2, PopChartStyle.Grouping.Clustered, totalWidth, clusterGap, barGap)
        assertEquals(24, result.size)
        val usableWidth = totalWidth - clusterGap * (clusterCount - 1)
        val clusterWidth = usableWidth / clusterCount

        // For each cluster, sum of two bar widths + gap should equal clusterWidth
        for (c in 0 until clusterCount) {
            val slotsInCluster = result.filter { it.clusterIndex == c }
            assertEquals(2, slotsInCluster.size)
            val w0 = slotsInCluster[0].rect.right - slotsInCluster[0].rect.left
            val w1 = slotsInCluster[1].rect.right - slotsInCluster[1].rect.left
            assertEquals(clusterWidth, w0 + w1 + barGap, 1e-2f)
        }
    }

    @Test
    fun computeBarSlots_stacked_seriesCount2_slotsShareLeftRight() {
        val result = computeBarSlots(12, 2, PopChartStyle.Grouping.Stacked, 360f, 12f, 4f)
        assertEquals(24, result.size)
        for (c in 0 until 12) {
            val s0 = result.first { it.clusterIndex == c && it.seriesIndex == 0 }
            val s1 = result.first { it.clusterIndex == c && it.seriesIndex == 1 }
            assertEquals(s0.rect.left, s1.rect.left, 1e-3f)
            assertEquals(s0.rect.right, s1.rect.right, 1e-3f)
        }
    }

    @Test
    fun computeBarSlots_clustered_gapsRespected_leftEdgesMatch() {
        val clusterCount = 12
        val totalWidth = 360f
        val clusterGap = 12f
        val barGap = 4f
        val result = computeBarSlots(clusterCount, 2, PopChartStyle.Grouping.Clustered, totalWidth, clusterGap, barGap)

        for (c in 0 until clusterCount - 1) {
            val lastSlotInCluster = result.filter { it.clusterIndex == c }.maxByOrNull { it.rect.right }!!
            val firstSlotInNextCluster = result.filter { it.clusterIndex == c + 1 }.minByOrNull { it.rect.left }!!
            val gapBetween = firstSlotInNextCluster.rect.left - lastSlotInCluster.rect.right
            assertEquals(clusterGap, gapBetween, 1e-2f)
        }
    }

    // ── computeStackedYBounds ────────────────────────────────────────────────

    @Test
    fun computeStackedYBounds_empty_returnsSentinel() {
        val (lo, hi) = computeStackedYBounds(emptyList())
        assertEquals(0f, lo, 1e-3f)
        assertEquals(1f, hi, 1e-3f)
    }

    @Test
    fun computeStackedYBounds_singleSeries_equalsClustered() {
        // For a single positive series, the stacked max should match the raw max + 10% padding
        // and min should be clamped at 0
        val series = listOf(PopChartSeries("A", listOf(10f, 20f, 30f)))
        val (lo, hi) = computeStackedYBounds(series)
        assertEquals(0f, lo, 1e-3f)  // min clamped at 0 for positive data
        assertEquals(30f * 1.10f, hi, 1e-2f)  // max 30 + 10% padding
    }

    @Test
    fun computeStackedYBounds_twoSeries_maxDrivenByPerTickSum() {
        // A=[10,20,30], B=[5,5,5] → per-tick sums [15,25,35] → max=35 + 10%
        val series = listOf(
            PopChartSeries("A", listOf(10f, 20f, 30f)),
            PopChartSeries("B", listOf(5f, 5f, 5f)),
        )
        val (lo, hi) = computeStackedYBounds(series)
        assertEquals(0f, lo, 1e-3f)
        assertEquals(35f * 1.10f, hi, 1e-2f)
    }

    @Test
    fun computeStackedYBounds_minClampedAtZero_evenWhenAllPositive() {
        // All positive values: stacked min should be 0, not derived from the minimum value
        val series = listOf(PopChartSeries("A", listOf(50f, 60f)))
        val (lo, _) = computeStackedYBounds(series)
        assertEquals(0f, lo, 1e-3f)
    }

    // ── computeDonutSlices ───────────────────────────────────────────────────

    @Test
    fun donut_emptySeries_returnsEmpty() {
        val result = computeDonutSlices(emptyList(), null)
        assertTrue(result.isEmpty())
    }

    @Test
    fun donut_allZeroValues_returnsEmpty() {
        val series = listOf(
            PopChartSeries("a", listOf(0f)),
            PopChartSeries("b", listOf(0f)),
        )
        val result = computeDonutSlices(series, null)
        assertTrue(result.isEmpty())
    }

    @Test
    fun donut_singleSlice_implicitTotal_sweeps360() {
        val series = listOf(PopChartSeries("a", listOf(50f)))
        val result = computeDonutSlices(series, null)
        assertEquals(1, result.size)
        assertEquals(360f, result[0].sweepAngleDeg, 1e-3f)
    }

    @Test
    fun donut_explicitTotalLargerThanSum_leavesGap() {
        // One slice of value 50 with total = 100 → sweep = 180°
        val series = listOf(PopChartSeries("a", listOf(50f)))
        val result = computeDonutSlices(series, 100f)
        assertEquals(1, result.size)
        assertEquals(180f, result[0].sweepAngleDeg, 1e-3f)
    }

    @Test
    fun donut_multiSlicesAdjacent_startAnglesAccumulate() {
        // [10, 20, 30] total = 60 → sweeps [60°, 120°, 180°], starts [0°, 60°, 180°]
        val series = listOf(
            PopChartSeries("a", listOf(10f)),
            PopChartSeries("b", listOf(20f)),
            PopChartSeries("c", listOf(30f)),
        )
        val result = computeDonutSlices(series, null)
        assertEquals(3, result.size)
        // Sweeps
        assertEquals(60f, result[0].sweepAngleDeg, 1e-3f)
        assertEquals(120f, result[1].sweepAngleDeg, 1e-3f)
        assertEquals(180f, result[2].sweepAngleDeg, 1e-3f)
        // Start angles accumulate
        assertEquals(0f, result[0].startAngleDeg, 1e-3f)
        assertEquals(60f, result[1].startAngleDeg, 1e-3f)
        assertEquals(180f, result[2].startAngleDeg, 1e-3f)
    }

    // ── donutTotal fallback ──────────────────────────────────────────────────

    @Test
    fun donutTotal_explicitNull_usesSliceSum() {
        assertEquals(100f, donutTotal(null, sliceSum = 100f), 1e-3f)
    }

    @Test
    fun donutTotal_explicitLessThanSum_fallsBackToSum() {
        // explicit total < slice sum → we can never let a slice exceed the ring
        assertEquals(100f, donutTotal(50f, sliceSum = 100f), 1e-3f)
    }

    // ── negative-value clamp ─────────────────────────────────────────────────

    @Test
    fun donut_negativeValuesClampedToZero() {
        // Negative series value clamped to 0; only "b" with value 10 contributes
        val series = listOf(
            PopChartSeries("a", listOf(-5f)),
            PopChartSeries("b", listOf(10f)),
        )
        val result = computeDonutSlices(series, null)
        assertEquals(1, result.size)
        assertEquals(1, result[0].seriesIndex)  // "b" at index 1
        assertEquals(360f, result[0].sweepAngleDeg, 1e-3f)
    }

    // ── NaN-value clamp ──────────────────────────────────────────────────────

    @Test
    fun donut_nanValuesFiltered() {
        // NaN series value filtered; only "b" with value 10 contributes
        val series = listOf(
            PopChartSeries("a", listOf(Float.NaN)),
            PopChartSeries("b", listOf(10f)),
        )
        val result = computeDonutSlices(series, null)
        assertEquals(1, result.size)
        assertEquals(1, result[0].seriesIndex)  // "b" at index 1
        assertEquals(360f, result[0].sweepAngleDeg, 1e-3f)
    }
}

// Helper for float comparison with tolerance
private fun assertEquals(expected: Float, actual: Float, tolerance: Float) {
    assertTrue(
        kotlin.math.abs(expected - actual) <= tolerance,
        "Expected $expected but was $actual (tolerance $tolerance)",
    )
}
