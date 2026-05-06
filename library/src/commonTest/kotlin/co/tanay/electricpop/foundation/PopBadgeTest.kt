package co.tanay.electricpop.foundation

import kotlin.test.Test
import kotlin.test.assertEquals

class PopBadgeTest {

    @Test
    fun popBadgeDirection_entries_areStable() {
        val entries = PopBadgeDirection.entries
        assertEquals(3, entries.size)
        assertEquals(PopBadgeDirection.Up, entries[0])
        assertEquals(PopBadgeDirection.Down, entries[1])
        assertEquals(PopBadgeDirection.Neutral, entries[2])
    }

    @Test
    fun popBadgeSize_entries_areStable() {
        val entries = PopBadgeSize.entries
        assertEquals(2, entries.size)
        assertEquals(PopBadgeSize.Small, entries[0])
        assertEquals(PopBadgeSize.Large, entries[1])
    }

    @Test
    fun visualValidationViaDemo() {
        // PopBadge is a purely visual composable; layout validation happens via Roborazzi.
    }
}
