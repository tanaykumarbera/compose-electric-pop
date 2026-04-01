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

class PopIconRowScreenshotTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun allVariants_light() = runDesktopComposeUiTest(width = 600, height = 500) {
        setContent {
            ElectricPopTheme(darkTheme = false) {
                AllVariantsColumn()
            }
        }
        onRoot().captureRoboImage(filePath = "src/desktopTest/snapshots/PopIconRow_allVariants_light.png")
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun allVariants_dark() = runDesktopComposeUiTest(width = 600, height = 500) {
        setContent {
            ElectricPopTheme(darkTheme = true) {
                AllVariantsColumn()
            }
        }
        onRoot().captureRoboImage(filePath = "src/desktopTest/snapshots/PopIconRow_allVariants_dark.png")
    }
}

@Composable
private fun AllVariantsColumn() {
    val cs = MaterialTheme.colorScheme
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        // Single icon
        Text("Single icon", style = MaterialTheme.typography.labelSmall, color = cs.onSurfaceVariant)
        PopIconRow(icons = listOf(PopIconItem(PopIcons.Star, "Star")))

        // 3 icons Medium
        Text("3 icons (Medium)", style = MaterialTheme.typography.labelSmall, color = cs.onSurfaceVariant)
        PopIconRow(
            icons = listOf(
                PopIconItem(PopIcons.Star, "Star"),
                PopIconItem(PopIcons.Heart, "Heart"),
                PopIconItem(PopIcons.Bolt, "Bolt"),
            )
        )

        // 5 icons Medium
        Text("5 icons (Medium)", style = MaterialTheme.typography.labelSmall, color = cs.onSurfaceVariant)
        PopIconRow(
            icons = listOf(
                PopIconItem(PopIcons.Star),
                PopIconItem(PopIcons.Heart),
                PopIconItem(PopIcons.Bolt),
                PopIconItem(PopIcons.Sparkle),
                PopIconItem(PopIcons.CheckCircle),
            )
        )

        // 3 icons Small
        Text("3 icons (Small)", style = MaterialTheme.typography.labelSmall, color = cs.onSurfaceVariant)
        PopIconRow(
            icons = listOf(
                PopIconItem(PopIcons.Star),
                PopIconItem(PopIcons.Heart),
                PopIconItem(PopIcons.Bolt),
            ),
            iconSize = PopIconSize.Small,
        )

        // 3 icons Large
        Text("3 icons (Large)", style = MaterialTheme.typography.labelSmall, color = cs.onSurfaceVariant)
        PopIconRow(
            icons = listOf(
                PopIconItem(PopIcons.Star),
                PopIconItem(PopIcons.Heart),
                PopIconItem(PopIcons.Bolt),
            ),
            iconSize = PopIconSize.Large,
        )

        // Primary tint
        Text("Primary tint", style = MaterialTheme.typography.labelSmall, color = cs.onSurfaceVariant)
        PopIconRow(
            icons = listOf(
                PopIconItem(PopIcons.Star),
                PopIconItem(PopIcons.Heart),
                PopIconItem(PopIcons.Bolt),
            ),
            tint = cs.primary,
        )

        // Secondary tint
        Text("Secondary tint", style = MaterialTheme.typography.labelSmall, color = cs.onSurfaceVariant)
        PopIconRow(
            icons = listOf(
                PopIconItem(PopIcons.Star),
                PopIconItem(PopIcons.Heart),
                PopIconItem(PopIcons.Bolt),
            ),
            tint = cs.secondary,
        )

        // Tertiary tint
        Text("Tertiary tint", style = MaterialTheme.typography.labelSmall, color = cs.onSurfaceVariant)
        PopIconRow(
            icons = listOf(
                PopIconItem(PopIcons.Star),
                PopIconItem(PopIcons.Heart),
                PopIconItem(PopIcons.Bolt),
            ),
            tint = cs.tertiary,
        )
    }
}
