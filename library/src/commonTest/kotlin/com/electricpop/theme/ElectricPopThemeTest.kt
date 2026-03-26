package com.electricpop.theme

import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class ElectricPopThemeTest {

    @Test
    fun lightColorScheme_hasPrimaryElectricLime() {
        assertEquals(ElectricLime, ElectricPopLightColorScheme.primary)
    }

    @Test
    fun darkColorScheme_differFromLight() {
        assertNotEquals(
            ElectricPopLightColorScheme.surface,
            ElectricPopDarkColorScheme.surface,
        )
    }

    @Test
    fun spacing_hasCorrectDefaults() {
        val spacing = ElectricPopSpacing()
        assertEquals(4.dp, spacing.xxs)
        assertEquals(64.dp, spacing.xxxl)
    }
}
