package co.tanay.electricpop.foundation

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
import co.tanay.electricpop.theme.ElectricPopTheme
import io.github.takahirom.roborazzi.captureRoboImage
import kotlin.test.Test

class PopSectionHeaderScreenshotTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun allVariants_light() = runDesktopComposeUiTest(width = 700, height = 900) {
        setContent {
            ElectricPopTheme(darkTheme = false) {
                AllVariantsColumn()
            }
        }
        onRoot().captureRoboImage(filePath = "src/desktopTest/snapshots/PopSectionHeader_allVariants_light.png")
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun allVariants_dark() = runDesktopComposeUiTest(width = 700, height = 900) {
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
        verticalArrangement = Arrangement.spacedBy(32.dp),
    ) {
        // 1. Highlight + titleAccent (full variant — closest to Stitch design)
        Text("Highlight + TitleAccent", style = MaterialTheme.typography.labelSmall, color = cs.onSurfaceVariant)
        PopSectionHeader(
            title = "Stash Hub",
            highlight = "Component Guide",
            titleAccent = "Card",
        )

        // 2. Highlight only
        Text("Highlight only", style = MaterialTheme.typography.labelSmall, color = cs.onSurfaceVariant)
        PopSectionHeader(
            title = "Live Ledger",
            highlight = "Feature Set",
        )

        // 3. Title only
        Text("Title only", style = MaterialTheme.typography.labelSmall, color = cs.onSurfaceVariant)
        PopSectionHeader(title = "Performance")

        // 4. Title + description
        Text("Title + description", style = MaterialTheme.typography.labelSmall, color = cs.onSurfaceVariant)
        PopSectionHeader(
            title = "Analytics",
            description = "Track your metrics in real time with live data feeds and customisable dashboards.",
        )

        // 5. Custom titleAccentColor
        Text("Custom accent color", style = MaterialTheme.typography.labelSmall, color = cs.onSurfaceVariant)
        PopSectionHeader(
            title = "System",
            highlight = "Status",
            titleAccent = "Active",
            titleAccentColor = cs.secondary,
        )
    }
}
