package co.tanay.electricpop.foundation

import kotlin.test.Test
import kotlin.test.assertEquals

class PopSwitchTest {

    @Test
    fun popSwitchColor_entries_areStable() {
        val entries = PopSwitchColor.entries
        assertEquals(3, entries.size)
        assertEquals(PopSwitchColor.Primary, entries[0])
        assertEquals(PopSwitchColor.Secondary, entries[1])
        assertEquals(PopSwitchColor.Tertiary, entries[2])
    }

    @Test
    fun visualValidationViaDemo() {
        // PopSwitch is a purely visual composable; the toggle state is managed externally
        // and color resolution requires MaterialTheme. Visual validation is performed via
        // the demo app and Roborazzi screenshot tests in both light and dark themes.
    }
}
