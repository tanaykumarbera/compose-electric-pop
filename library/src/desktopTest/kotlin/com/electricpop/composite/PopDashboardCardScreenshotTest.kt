package com.electricpop.composite

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runDesktopComposeUiTest
import androidx.compose.ui.unit.dp
import com.electricpop.foundation.PopIcon
import com.electricpop.foundation.PopIcons
import com.electricpop.theme.ElectricPopTheme
import com.electricpop.theme.PopShapeFull
import io.github.takahirom.roborazzi.captureRoboImage
import kotlin.test.Test

class PopDashboardCardScreenshotTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun allVariants_light() = runDesktopComposeUiTest(width = 500, height = 500) {
        setContent { DashboardCardContent(darkTheme = false) }
        onRoot().captureRoboImage(
            filePath = "src/desktopTest/snapshots/PopDashboardCard_allVariants_light.png",
        )
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun allVariants_dark() = runDesktopComposeUiTest(width = 500, height = 500) {
        setContent { DashboardCardContent(darkTheme = true) }
        onRoot().captureRoboImage(
            filePath = "src/desktopTest/snapshots/PopDashboardCard_allVariants_dark.png",
        )
    }
}

@Composable
private fun DashboardCardContent(darkTheme: Boolean) {
    ElectricPopTheme(darkTheme = darkTheme) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            PopDashboardCard(
                title = "Total Overview",
                titleValue = "14,200",
                items = listOf(
                    PopDashboardItem("Category A", "8,400"),
                    PopDashboardItem("Category B", "5,800"),
                ),
                backgroundIcon = PopIcons.Layers,
                statusContent = {
                    PopDashboardStatusPill(label = "Active")
                },
                actionContent = {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(PopShapeFull)
                            .background(MaterialTheme.colorScheme.onTertiaryContainer),
                        contentAlignment = Alignment.Center,
                    ) {
                        PopIcon(
                            imageVector = PopIcons.Add,
                            contentDescription = "Add",
                            tint = MaterialTheme.colorScheme.tertiaryContainer,
                        )
                    }
                },
            )
        }
    }
}
