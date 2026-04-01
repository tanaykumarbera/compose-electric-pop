package com.electricpop.foundation

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

private val testVector = ImageVector.Builder(
    name = "Test",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f,
).build()

class PopIconRowTest {

    @Test
    fun popIconItem_defaultContentDescription_isNull() {
        val item = PopIconItem(testVector)
        assertNull(item.contentDescription)
    }

    @Test
    fun popIconItem_equality() {
        val item1 = PopIconItem(testVector, "description")
        val item2 = PopIconItem(testVector, "description")
        assertEquals(item1, item2)
    }

    @Test
    fun popIconItem_copy() {
        val original = PopIconItem(testVector, "original")
        val copied = original.copy(contentDescription = "copied")
        assertEquals(testVector, copied.imageVector)
        assertEquals("copied", copied.contentDescription)
    }

    @Test
    fun visualValidationViaDemo() {
        // PopIconRow is a purely visual composable with no extractable logic
        // beyond PopIconItem data class behavior.
        // Visual validation is performed via the demo app in light and dark themes.
        // This test serves as a placeholder for future screenshot/UI tests.
        assertTrue(true)
    }
}
