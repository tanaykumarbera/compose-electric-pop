package com.electricpop.composite

import androidx.compose.ui.graphics.Color
import com.electricpop.foundation.PopDisplayTextDirection
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Unit tests for data classes used by [PopDashboardCard] and [PopDashboardCardCompact].
 *
 * Note: [PopDashboardCardRow] requires an [androidx.compose.ui.graphics.vector.ImageVector]
 * for the icon field. Since [com.electricpop.foundation.PopIcons] properties are @Composable
 * getters, they cannot be accessed outside composable scope in unit tests. The data class
 * semantics and associated types are tested here instead.
 *
 * Visual rendering is validated via the demo app and Roborazzi screenshot tests.
 */
class PopDashboardCardTest {

    @Test
    fun popDataRowChip_storesAllFields() {
        val chip = PopDataRowChip(
            label = "Savings",
            containerColor = Color(0xFF00FF00),
            contentColor = Color(0xFF000000),
        )
        assertEquals("Savings", chip.label)
        assertEquals(Color(0xFF00FF00), chip.containerColor)
        assertEquals(Color(0xFF000000), chip.contentColor)
    }

    @Test
    fun popDataRowChip_copyModifiesLabel() {
        val original = PopDataRowChip(
            label = "Alpha",
            containerColor = Color.Red,
            contentColor = Color.White,
        )
        val modified = original.copy(label = "Beta")
        assertEquals("Beta", modified.label)
        assertEquals(Color.Red, modified.containerColor)
        assertEquals(Color.White, modified.contentColor)
    }

    @Test
    fun popDataRowChip_listRetainsOrder() {
        val chips = listOf(
            PopDataRowChip("First", Color.Red, Color.White),
            PopDataRowChip("Second", Color.Blue, Color.White),
            PopDataRowChip("Third", Color.Green, Color.Black),
        )
        assertEquals(3, chips.size)
        assertEquals("First", chips[0].label)
        assertEquals("Second", chips[1].label)
        assertEquals("Third", chips[2].label)
    }

    @Test
    fun popDisplayTextDirection_allValuesPresent() {
        // Verify all direction enum values referenced in PopDashboardCardRow exist
        val entries = PopDisplayTextDirection.entries
        assertTrue(entries.contains(PopDisplayTextDirection.Neutral))
        assertTrue(entries.contains(PopDisplayTextDirection.Positive))
        assertTrue(entries.contains(PopDisplayTextDirection.Negative))
    }
}
