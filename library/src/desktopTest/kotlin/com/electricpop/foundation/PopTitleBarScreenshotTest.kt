package com.electricpop.foundation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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

class PopTitleBarScreenshotTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun allVariants_light() = runDesktopComposeUiTest(width = 700, height = 400) {
        setContent {
            ElectricPopTheme(darkTheme = false) {
                AllVariantsColumn()
            }
        }
        onRoot().captureRoboImage(filePath = "src/desktopTest/snapshots/PopTitleBar_allVariants_light.png")
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun allVariants_dark() = runDesktopComposeUiTest(width = 700, height = 400) {
        setContent {
            ElectricPopTheme(darkTheme = true) {
                AllVariantsColumn()
            }
        }
        onRoot().captureRoboImage(filePath = "src/desktopTest/snapshots/PopTitleBar_allVariants_dark.png")
    }
}

@Composable
private fun AllVariantsColumn() {
    val cs = MaterialTheme.colorScheme
    Column(
        modifier = Modifier.padding(24.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        // 1. Title only
        Text("Title only", style = MaterialTheme.typography.labelSmall, color = cs.onSurfaceVariant)
        PopTitleBar(title = "Dashboard")

        // 2. Title + status (matches Stitch "LIVE PREVIEW / SYSTEM ACTIVE")
        Text("Title + Status", style = MaterialTheme.typography.labelSmall, color = cs.onSurfaceVariant)
        PopTitleBar(title = "Live Preview", status = "System Active")

        // 3. Another status example
        Text("Title + Status (variant)", style = MaterialTheme.typography.labelSmall, color = cs.onSurfaceVariant)
        PopTitleBar(title = "Transactions", status = "Live")
    }
}
