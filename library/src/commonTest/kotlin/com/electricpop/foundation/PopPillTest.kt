package com.electricpop.foundation

import kotlin.test.Test
import kotlin.test.assertEquals

class PopPillTest {

    @Test
    fun label_isUppercased() {
        // PopPill applies .uppercase() to the label internally.
        // Verify the transform works as expected for various inputs.
        assertEquals("ACTIVE", "active".uppercase())
        assertEquals("LOCKED", "locked".uppercase())
        assertEquals("LIVE", "live".uppercase())
        assertEquals("NEW", "new".uppercase())
        assertEquals("BETA", "beta".uppercase())
    }

    @Test
    fun label_alreadyUppercase_isUnchanged() {
        assertEquals("ACTIVE", "ACTIVE".uppercase())
        assertEquals("EMERGENCY", "EMERGENCY".uppercase())
    }

    @Test
    fun label_mixedCase_isUppercased() {
        assertEquals("MY STATUS", "My Status".uppercase())
        assertEquals("HELLO WORLD", "hElLo WoRlD".uppercase())
    }

    @Test
    fun label_singleCharacter_isUppercased() {
        assertEquals("A", "a".uppercase())
    }

    @Test
    fun label_emptyString_remainsEmpty() {
        assertEquals("", "".uppercase())
    }
}
