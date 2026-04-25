package co.tanay.electricpop.foundation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runDesktopComposeUiTest
import androidx.compose.ui.unit.dp
import co.tanay.electricpop.theme.ElectricPopTheme
import io.github.takahirom.roborazzi.captureRoboImage
import kotlin.test.Test

class PopIconScreenshotTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun allVariants_light() = runDesktopComposeUiTest(width = 600, height = 500) {
        setContent {
            ElectricPopTheme(darkTheme = false) {
                AllIconsGrid()
            }
        }
        onRoot().captureRoboImage(filePath = "src/desktopTest/snapshots/PopIcon_allVariants_light.png")
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun allVariants_dark() = runDesktopComposeUiTest(width = 600, height = 500) {
        setContent {
            ElectricPopTheme(darkTheme = true) {
                AllIconsGrid()
            }
        }
        onRoot().captureRoboImage(filePath = "src/desktopTest/snapshots/PopIcon_allVariants_dark.png")
    }
}

@androidx.compose.runtime.Composable
private fun AllIconsGrid() {
    val cs = androidx.compose.material3.MaterialTheme.colorScheme
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Row 1: Sizes
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            PopIcon(imageVector = PopIcons.Star, contentDescription = null, size = PopIconSize.Small)
            PopIcon(imageVector = PopIcons.Star, contentDescription = null, size = PopIconSize.Medium)
            PopIcon(imageVector = PopIcons.Star, contentDescription = null, size = PopIconSize.Large)
        }
        // Row 2: Original icons (default tint)
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            PopIcon(imageVector = PopIcons.Star, contentDescription = null)
            PopIcon(imageVector = PopIcons.Heart, contentDescription = null)
            PopIcon(imageVector = PopIcons.Home, contentDescription = null)
            PopIcon(imageVector = PopIcons.Search, contentDescription = null)
            PopIcon(imageVector = PopIcons.Settings, contentDescription = null)
            PopIcon(imageVector = PopIcons.Add, contentDescription = null)
            PopIcon(imageVector = PopIcons.Close, contentDescription = null)
            PopIcon(imageVector = PopIcons.Check, contentDescription = null)
            PopIcon(imageVector = PopIcons.Person, contentDescription = null)
        }
        // Row 3: Info, Warning, arrows
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            PopIcon(imageVector = PopIcons.Info, contentDescription = null)
            PopIcon(imageVector = PopIcons.Warning, contentDescription = null)
            PopIcon(imageVector = PopIcons.ArrowUp, contentDescription = null)
            PopIcon(imageVector = PopIcons.ArrowDown, contentDescription = null)
            PopIcon(imageVector = PopIcons.ArrowBack, contentDescription = null)
            PopIcon(imageVector = PopIcons.ArrowForward, contentDescription = null)
            PopIcon(imageVector = PopIcons.TrendUp, contentDescription = null, tint = cs.primary)
            PopIcon(imageVector = PopIcons.TrendDown, contentDescription = null, tint = cs.error)
        }
        // Row 4: New Stitch design icons
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            PopIcon(imageVector = PopIcons.Bolt, contentDescription = null, tint = cs.primary)
            PopIcon(imageVector = PopIcons.Sparkle, contentDescription = null, tint = cs.primary)
            PopIcon(imageVector = PopIcons.CheckCircle, contentDescription = null, tint = cs.primary)
            PopIcon(imageVector = PopIcons.Layers, contentDescription = null)
            PopIcon(imageVector = PopIcons.Puzzle, contentDescription = null)
            PopIcon(imageVector = PopIcons.Tokens, contentDescription = null)
            PopIcon(imageVector = PopIcons.Menu, contentDescription = null)
        }
        // Row 5: Theme tints on key icons
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            PopIcon(imageVector = PopIcons.Bolt, contentDescription = null, tint = cs.secondary)
            PopIcon(imageVector = PopIcons.CheckCircle, contentDescription = null, tint = cs.tertiary)
            PopIcon(imageVector = PopIcons.Warning, contentDescription = null, tint = cs.error)
            PopIcon(imageVector = PopIcons.Sparkle, contentDescription = null, tint = cs.tertiary)
            PopIcon(imageVector = PopIcons.Layers, contentDescription = null, tint = cs.secondary)
        }
    }
}
