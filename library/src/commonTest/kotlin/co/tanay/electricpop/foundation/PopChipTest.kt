package co.tanay.electricpop.foundation

import kotlin.test.Test
import kotlin.test.assertEquals

class PopChipTest {

    @Test
    fun popChipColor_entries_areStable() {
        val entries = PopChipColor.entries
        assertEquals(5, entries.size)
        assertEquals(PopChipColor.Primary, entries[0])
        assertEquals(PopChipColor.Secondary, entries[1])
        assertEquals(PopChipColor.Tertiary, entries[2])
        assertEquals(PopChipColor.Error, entries[3])
        assertEquals(PopChipColor.Neutral, entries[4])
    }

    @Test
    fun popChipSize_entries_areStable() {
        val entries = PopChipSize.entries
        assertEquals(3, entries.size)
        assertEquals(PopChipSize.Small, entries[0])
        assertEquals(PopChipSize.Medium, entries[1])
        assertEquals(PopChipSize.Large, entries[2])
    }

    @Test
    fun visualValidationViaDemo() {
        // Color resolution requires MaterialTheme; visual validation via Roborazzi.
    }
}
