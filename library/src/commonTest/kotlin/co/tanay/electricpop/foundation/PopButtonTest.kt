package co.tanay.electricpop.foundation

import kotlin.test.Test
import kotlin.test.assertEquals

class PopButtonTest {

    @Test
    fun popButtonStyle_entries_areStable() {
        val styles = PopButtonStyle.entries
        assertEquals(4, styles.size)
        assertEquals(PopButtonStyle.Primary, styles[0])
        assertEquals(PopButtonStyle.Secondary, styles[1])
        assertEquals(PopButtonStyle.Tertiary, styles[2])
        assertEquals(PopButtonStyle.Ghost, styles[3])
    }

    @Test
    fun popButtonSize_entries_areStable() {
        val sizes = PopButtonSize.entries
        assertEquals(3, sizes.size)
        assertEquals(PopButtonSize.XL, sizes[0])
        assertEquals(PopButtonSize.Large, sizes[1])
        assertEquals(PopButtonSize.Small, sizes[2])
    }

    @Test
    fun visualValidationViaDemo() {
        // PopButton visual appearance (neon glow, kinetic animations, theme colors,
        // shape, typography per size) is validated via the demo app and Roborazzi
        // screenshot tests. Color resolution requires MaterialTheme.
    }
}
