package com.electricpop.composite

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.electricpop.foundation.PopDisplayTextDirection
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class PopDashboardCardTest {

    // Stub ImageVector to exercise PopDashboardCardRow without @Composable context.
    private val stubIcon: ImageVector = ImageVector.Builder(
        name = "stub",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).build()

    @Test
    fun popDashboardCardRow_defaultValues() {
        val row = PopDashboardCardRow(
            icon = stubIcon,
            label = "Vault A",
            value = "$24,000",
        )
        assertEquals(stubIcon, row.icon)
        assertEquals("Vault A", row.label)
        assertEquals("$24,000", row.value)
        assertTrue(row.chips.isEmpty())
        assertNull(row.subtitle)
        assertEquals(PopDisplayTextDirection.Neutral, row.direction)
        assertNull(row.valueColor)
        assertNull(row.onClick)
    }

    @Test
    fun popDashboardCardRow_copyModifiesValues() {
        val original = PopDashboardCardRow(
            icon = stubIcon,
            label = "Vault A",
            value = "$24,000",
        )
        val modified = original.copy(
            label = "Vault B",
            direction = PopDisplayTextDirection.Positive,
        )
        assertEquals("Vault B", modified.label)
        assertEquals("$24,000", modified.value)
        assertEquals(PopDisplayTextDirection.Positive, modified.direction)
        assertEquals(stubIcon, modified.icon)
    }

    @Test
    fun popDashboardCardRow_allFieldsSet() {
        val chip = PopDataRowChip(
            label = "Savings",
            containerColor = Color(0xFF00FF00),
            contentColor = Color(0xFF000000),
        )
        val row = PopDashboardCardRow(
            icon = stubIcon,
            label = "Emergency Fund",
            value = "$18,890",
            chips = listOf(chip),
            subtitle = "Oct 2024",
            direction = PopDisplayTextDirection.Positive,
            valueColor = Color.Green,
            onClick = {},
        )
        assertEquals("Emergency Fund", row.label)
        assertEquals(1, row.chips.size)
        assertEquals("Savings", row.chips[0].label)
        assertEquals("Oct 2024", row.subtitle)
        assertEquals(PopDisplayTextDirection.Positive, row.direction)
        assertEquals(Color.Green, row.valueColor)
    }

    @Test
    fun popDashboardCardRow_chipsListRetainsOrder() {
        val chips = listOf(
            PopDataRowChip("First", Color.Red, Color.White),
            PopDataRowChip("Second", Color.Blue, Color.White),
            PopDataRowChip("Third", Color.Green, Color.Black),
        )
        val row = PopDashboardCardRow(
            icon = stubIcon,
            label = "Multi-Chip Row",
            value = "$1,000",
            chips = chips,
        )
        assertEquals(3, row.chips.size)
        assertEquals("First", row.chips[0].label)
        assertEquals("Second", row.chips[1].label)
        assertEquals("Third", row.chips[2].label)
    }
}
