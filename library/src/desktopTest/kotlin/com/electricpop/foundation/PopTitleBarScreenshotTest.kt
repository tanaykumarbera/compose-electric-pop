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

class PopTitleBarScreenshotTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun allVariants_light() = runDesktopComposeUiTest(width = 700, height = 700) {
        setContent {
            ElectricPopTheme(darkTheme = false) {
                AllVariantsColumn()
            }
        }
        onRoot().captureRoboImage(filePath = "src/desktopTest/snapshots/PopTitleBar_allVariants_light.png")
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun allVariants_dark() = runDesktopComposeUiTest(width = 700, height = 700) {
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
        modifier = Modifier.padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        // 1. Title only
        Text("Title only", style = MaterialTheme.typography.labelSmall, color = cs.onSurfaceVariant)
        PopTitleBar(title = "Dashboard")

        // 2. Title + Primary pill
        Text("Title + Primary pill", style = MaterialTheme.typography.labelSmall, color = cs.onSurfaceVariant)
        PopTitleBar(title = "Transactions", pill = "Live")

        // 3. Title + Secondary pill
        Text("Title + Secondary pill", style = MaterialTheme.typography.labelSmall, color = cs.onSurfaceVariant)
        PopTitleBar(title = "Analytics", pill = "Beta", pillColor = PopPillColor.Secondary)

        // 4. Title + Tertiary pill
        Text("Title + Tertiary pill", style = MaterialTheme.typography.labelSmall, color = cs.onSurfaceVariant)
        PopTitleBar(title = "Portfolio", pill = "New", pillColor = PopPillColor.Tertiary)

        // 5. Title + Error pill
        Text("Title + Error pill", style = MaterialTheme.typography.labelSmall, color = cs.onSurfaceVariant)
        PopTitleBar(title = "Alerts", pill = "Critical", pillColor = PopPillColor.Error)

        // 6. Title + Neutral pill
        Text("Title + Neutral pill", style = MaterialTheme.typography.labelSmall, color = cs.onSurfaceVariant)
        PopTitleBar(title = "Settings", pill = "v2.0", pillColor = PopPillColor.Neutral)

        // 7. Medium style
        Text("Medium style", style = MaterialTheme.typography.labelSmall, color = cs.onSurfaceVariant)
        PopTitleBar(title = "Reports", pill = "Weekly", style = PopTitleBarStyle.Medium)

        // 8. Small style
        Text("Small style", style = MaterialTheme.typography.labelSmall, color = cs.onSurfaceVariant)
        PopTitleBar(title = "Activity", pill = "Today", style = PopTitleBarStyle.Small)
    }
}
