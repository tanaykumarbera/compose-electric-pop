package com.electricpop.foundation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runDesktopComposeUiTest
import androidx.compose.ui.unit.dp
import com.electricpop.theme.ElectricPopTheme
import io.github.takahirom.roborazzi.captureRoboImage
import kotlin.test.Test

class PopSectionHeaderScreenshotTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun allVariants_light() = runDesktopComposeUiTest(width = 600, height = 700) {
        setContent {
            ElectricPopTheme(darkTheme = false) {
                AllVariantsColumn()
            }
        }
        onRoot().captureRoboImage(filePath = "src/desktopTest/snapshots/PopSectionHeader_allVariants_light.png")
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun allVariants_dark() = runDesktopComposeUiTest(width = 600, height = 700) {
        setContent {
            ElectricPopTheme(darkTheme = true) {
                AllVariantsColumn()
            }
        }
        onRoot().captureRoboImage(filePath = "src/desktopTest/snapshots/PopSectionHeader_allVariants_dark.png")
    }
}

@Composable
private fun AllVariantsColumn() {
    val cs = MaterialTheme.colorScheme
    Column(
        modifier = Modifier.padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        // 1. Title only
        Text("Title only", style = MaterialTheme.typography.labelSmall, color = cs.onSurfaceVariant)
        PopSectionHeader(title = "Performance")

        // 2. Label + Title
        Text("Label + Title", style = MaterialTheme.typography.labelSmall, color = cs.onSurfaceVariant)
        PopSectionHeader(title = "System Status", label = "Overview")

        // 3. Numbered
        Text("Numbered", style = MaterialTheme.typography.labelSmall, color = cs.onSurfaceVariant)
        PopSectionHeader(title = "Getting Started", number = 1)

        // 4. Numbered with Label
        Text("Numbered with Label", style = MaterialTheme.typography.labelSmall, color = cs.onSurfaceVariant)
        PopSectionHeader(title = "Configuration", label = "Setup Guide", number = 2)

        // 5. Secondary accent
        Text("Secondary accent", style = MaterialTheme.typography.labelSmall, color = cs.onSurfaceVariant)
        PopSectionHeader(title = "Highlights", label = "Featured", accentColor = cs.secondary)

        // 6. Tertiary accent
        Text("Tertiary accent", style = MaterialTheme.typography.labelSmall, color = cs.onSurfaceVariant)
        PopSectionHeader(title = "Analytics", label = "Data", accentColor = cs.tertiary)
    }
}
