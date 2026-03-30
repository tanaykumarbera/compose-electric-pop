package com.electricpop.foundation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PopButtonTest {

    @Test
    fun popButtonStyleHasThreeEntries() {
        val styles = PopButtonStyle.entries
        assertEquals(3, styles.size)
        assertTrue(styles.contains(PopButtonStyle.Primary))
        assertTrue(styles.contains(PopButtonStyle.Secondary))
        assertTrue(styles.contains(PopButtonStyle.Ghost))
    }

    @Test
    fun popButtonSizeHasThreeEntries() {
        val sizes = PopButtonSize.entries
        assertEquals(3, sizes.size)
        assertTrue(sizes.contains(PopButtonSize.XL))
        assertTrue(sizes.contains(PopButtonSize.Large))
        assertTrue(sizes.contains(PopButtonSize.Small))
    }

    @Test
    fun visualValidationViaDemo() {
        // PopButton visual appearance (neon glow, kinetic animations, theme colors,
        // pill shape, typography) is validated via the demo app and screenshot tests.
    }
}
