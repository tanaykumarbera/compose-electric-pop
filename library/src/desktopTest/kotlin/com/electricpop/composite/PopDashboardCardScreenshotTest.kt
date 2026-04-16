package com.electricpop.composite

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runDesktopComposeUiTest
import androidx.compose.ui.unit.dp
import com.electricpop.foundation.PopDisplayTextDirection
import com.electricpop.foundation.PopIcons
import com.electricpop.foundation.PopPill
import com.electricpop.foundation.PopPillColor
import com.electricpop.theme.ElectricPopTheme
import io.github.takahirom.roborazzi.captureRoboImage
import kotlin.test.Test

class PopDashboardCardScreenshotTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun allVariants_light() = runDesktopComposeUiTest(width = 500, height = 1200) {
        setContent { DashboardCardScreenshotContent(darkTheme = false) }
        onRoot().captureRoboImage(
            filePath = "src/desktopTest/snapshots/PopDashboardCard_allVariants_light.png",
        )
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun allVariants_dark() = runDesktopComposeUiTest(width = 500, height = 1200) {
        setContent { DashboardCardScreenshotContent(darkTheme = true) }
        onRoot().captureRoboImage(
            filePath = "src/desktopTest/snapshots/PopDashboardCard_allVariants_dark.png",
        )
    }
}

@Composable
private fun DashboardCardScreenshotContent(darkTheme: Boolean) {
    ElectricPopTheme(darkTheme = darkTheme) {
        val onTertiary = MaterialTheme.colorScheme.onTertiaryContainer
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Default variant — full card with title, value, pills, and rows
            PopDashboardCard(
                title = "Total Locked Wealth",
                titleValue = "$42,890.00",
                rows = listOf(
                    PopDashboardCardRow(PopIcons.Layers, "Vault A", "$24,000"),
                    PopDashboardCardRow(
                        PopIcons.Star,
                        "Emergency",
                        "$18,890",
                        direction = PopDisplayTextDirection.Positive,
                    ),
                    PopDashboardCardRow(PopIcons.Heart, "Savings", "$12,400"),
                ),
                statusContent = {
                    PopPill(
                        label = "Locked",
                        containerColor = Color.White.copy(alpha = 0.2f),
                        contentColor = onTertiary,
                    )
                    PopPill(label = "Active", color = PopPillColor.Tertiary)
                },
            )

            // Compact variant — no header, just pills + rows
            PopDashboardCardCompact(
                rows = listOf(
                    PopDashboardCardRow(
                        PopIcons.Bolt,
                        "Electric Grid",
                        "-$142.00",
                        direction = PopDisplayTextDirection.Negative,
                    ),
                    PopDashboardCardRow(PopIcons.Home, "Rent Payment", "-$1,200.00"),
                ),
                statusContent = {
                    PopPill(label = "Q1 2026", color = PopPillColor.Neutral)
                },
            )
        }
    }
}
