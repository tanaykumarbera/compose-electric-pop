package co.tanay.electricpop.foundation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runDesktopComposeUiTest
import androidx.compose.ui.unit.dp
import co.tanay.electricpop.theme.ElectricPopTheme
import io.github.takahirom.roborazzi.captureRoboImage
import kotlin.test.Test

class PopChipScreenshotTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun popChip_allVariants_light() = runDesktopComposeUiTest(width = 700, height = 600) {
        setContent { ChipContent(darkTheme = false) }
        onRoot().captureRoboImage(
            filePath = "src/desktopTest/snapshots/PopChip_allVariants_light.png",
        )
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun popChip_allVariants_dark() = runDesktopComposeUiTest(width = 700, height = 600) {
        setContent { ChipContent(darkTheme = true) }
        onRoot().captureRoboImage(
            filePath = "src/desktopTest/snapshots/PopChip_allVariants_dark.png",
        )
    }
}

@Composable
private fun ChipContent(darkTheme: Boolean) {
    ElectricPopTheme(darkTheme = darkTheme) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                // Row 1 - all 5 colors at Medium
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    PopChipColor.entries.forEach { c -> PopChip(label = c.name, color = c) }
                }
                // Row 2 - three sizes at Primary
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    PopChip("Small", color = PopChipColor.Primary, size = PopChipSize.Small)
                    PopChip("Medium", color = PopChipColor.Primary, size = PopChipSize.Medium)
                    PopChip("Large", color = PopChipColor.Primary, size = PopChipSize.Large)
                }
                // Row 3 - icons + clickable
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    PopChip("Starred", color = PopChipColor.Primary, icon = PopIcons.Star)
                    PopChip("Favorites", color = PopChipColor.Secondary, icon = PopIcons.Heart)
                    PopChip("Filter", color = PopChipColor.Tertiary, onClick = {})
                }
                // Row 4 - Small uppercase replacement (former PopPill goldens)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    PopChip("Active", color = PopChipColor.Primary, size = PopChipSize.Small)
                    PopChip("Live", color = PopChipColor.Secondary, size = PopChipSize.Small)
                    PopChip("Synced", color = PopChipColor.Tertiary, size = PopChipSize.Small)
                    PopChip("Failed", color = PopChipColor.Error, size = PopChipSize.Small)
                    PopChip("Locked", color = PopChipColor.Neutral, size = PopChipSize.Small)
                }
            }
        }
    }
}
