package co.tanay.electricpop.foundation

import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals

class PopIconTest {

    @Test
    fun smallSizeMapsTo16dp() {
        assertEquals(16.dp, PopIconSize.Small.toDp())
    }

    @Test
    fun mediumSizeMapsTo24dp() {
        assertEquals(24.dp, PopIconSize.Medium.toDp())
    }

    @Test
    fun largeSizeMapsTo32dp() {
        assertEquals(32.dp, PopIconSize.Large.toDp())
    }
}
