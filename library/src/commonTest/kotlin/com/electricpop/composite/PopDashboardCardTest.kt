package com.electricpop.composite

import kotlin.test.Test
import kotlin.test.assertEquals

class PopDashboardCardTest {

    @Test
    fun popDashboardItem_fieldsCorrect() {
        val item = PopDashboardItem(label = "Category A", value = "1,240")
        assertEquals("Category A", item.label)
        assertEquals("1,240", item.value)
    }

    @Test
    fun popDashboardItem_copy() {
        val original = PopDashboardItem("Label", "Value")
        val modified = original.copy(value = "New Value")
        assertEquals("Label", modified.label)
        assertEquals("New Value", modified.value)
    }

    @Test
    fun popDashboardItem_equality() {
        val a = PopDashboardItem("Label", "100")
        val b = PopDashboardItem("Label", "100")
        assertEquals(a, b)
    }
}
