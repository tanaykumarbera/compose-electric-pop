package com.electricpop.composite

import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class PopImageBannerCardTest {

    @Test
    fun alignmentFor_mapsEveryAnchorToUniqueAlignment() {
        val pairs = PopImageBannerTextAnchor.values().map { it to alignmentFor(it) }
        assertEquals(pairs.size, pairs.map { it.second }.toSet().size)
    }

    @Test
    fun alignmentFor_wellKnownCases() {
        assertEquals(Alignment.TopStart, alignmentFor(PopImageBannerTextAnchor.TopStart))
        assertEquals(Alignment.Center, alignmentFor(PopImageBannerTextAnchor.Center))
        assertEquals(Alignment.BottomEnd, alignmentFor(PopImageBannerTextAnchor.BottomEnd))
    }

    @Test
    fun horizontalAlignmentFor_followsAnchorSide() {
        assertEquals(Alignment.Start, horizontalAlignmentFor(PopImageBannerTextAnchor.TopStart))
        assertEquals(Alignment.Start, horizontalAlignmentFor(PopImageBannerTextAnchor.BottomStart))
        assertEquals(Alignment.CenterHorizontally, horizontalAlignmentFor(PopImageBannerTextAnchor.Center))
        assertEquals(Alignment.CenterHorizontally, horizontalAlignmentFor(PopImageBannerTextAnchor.BottomCenter))
        assertEquals(Alignment.End, horizontalAlignmentFor(PopImageBannerTextAnchor.TopEnd))
        assertEquals(Alignment.End, horizontalAlignmentFor(PopImageBannerTextAnchor.CenterEnd))
    }

    @Test
    fun scrimBrush_isNonNullForEveryAnchor() {
        PopImageBannerTextAnchor.values().forEach { anchor ->
            assertNotNull(scrimBrush(anchor, Color.Black))
        }
    }
}
