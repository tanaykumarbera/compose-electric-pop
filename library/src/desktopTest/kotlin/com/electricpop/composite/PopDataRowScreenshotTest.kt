package com.electricpop.composite

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runDesktopComposeUiTest
import androidx.compose.ui.unit.dp
import com.electricpop.foundation.PopDisplayTextDirection
import com.electricpop.foundation.PopIcons
import com.electricpop.theme.ElectricPopTheme
import io.github.takahirom.roborazzi.captureRoboImage
import kotlin.test.Test

class PopDataRowScreenshotTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun allVariants_light() = runDesktopComposeUiTest(width = 500, height = 520) {
        setContent {
            ElectricPopTheme(darkTheme = false) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    PopDataRow(
                        icon = PopIcons.Star,
                        label = "Apple Store",
                        value = "-\$1,299.00",
                        chip = PopDataRowDefaults.chipTertiary("Tech"),
                        subtitle = "Oct 24",
                        direction = PopDisplayTextDirection.Negative,
                        isAlternate = false,
                    )
                    PopDataRow(
                        icon = PopIcons.Heart,
                        label = "Gion Sushi",
                        value = "-\$84.50",
                        chip = PopDataRowDefaults.chipSecondary("Dining"),
                        subtitle = "Oct 22",
                        direction = PopDisplayTextDirection.Negative,
                        isAlternate = true,
                    )
                    PopDataRow(
                        icon = PopIcons.Bolt,
                        label = "Salary Deposit",
                        value = "+\$4,200.00",
                        chip = PopDataRowDefaults.chipPrimary("Income"),
                        subtitle = "Oct 20",
                        direction = PopDisplayTextDirection.Positive,
                        isAlternate = false,
                    )
                    PopDataRow(
                        icon = PopIcons.Home,
                        label = "Utilities Corp",
                        value = "-\$142.12",
                        chip = PopDataRowDefaults.chipSurface("Home"),
                        subtitle = "Oct 18",
                        direction = PopDisplayTextDirection.Negative,
                        isAlternate = true,
                    )
                    PopDataRow(
                        icon = PopIcons.Info,
                        label = "Balance Check",
                        value = "\$12,000.00",
                        direction = PopDisplayTextDirection.Neutral,
                        isAlternate = false,
                    )
                }
            }
        }
        onRoot().captureRoboImage(
            filePath = "src/desktopTest/snapshots/PopDataRow_allVariants_light.png",
        )
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun allVariants_dark() = runDesktopComposeUiTest(width = 500, height = 520) {
        setContent {
            ElectricPopTheme(darkTheme = true) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    PopDataRow(
                        icon = PopIcons.Star,
                        label = "Apple Store",
                        value = "-\$1,299.00",
                        chip = PopDataRowDefaults.chipTertiary("Tech"),
                        subtitle = "Oct 24",
                        direction = PopDisplayTextDirection.Negative,
                        isAlternate = false,
                    )
                    PopDataRow(
                        icon = PopIcons.Heart,
                        label = "Gion Sushi",
                        value = "-\$84.50",
                        chip = PopDataRowDefaults.chipSecondary("Dining"),
                        subtitle = "Oct 22",
                        direction = PopDisplayTextDirection.Negative,
                        isAlternate = true,
                    )
                    PopDataRow(
                        icon = PopIcons.Bolt,
                        label = "Salary Deposit",
                        value = "+\$4,200.00",
                        chip = PopDataRowDefaults.chipPrimary("Income"),
                        subtitle = "Oct 20",
                        direction = PopDisplayTextDirection.Positive,
                        isAlternate = false,
                    )
                    PopDataRow(
                        icon = PopIcons.Home,
                        label = "Utilities Corp",
                        value = "-\$142.12",
                        chip = PopDataRowDefaults.chipSurface("Home"),
                        subtitle = "Oct 18",
                        direction = PopDisplayTextDirection.Negative,
                        isAlternate = true,
                    )
                    PopDataRow(
                        icon = PopIcons.Info,
                        label = "Balance Check",
                        value = "\$12,000.00",
                        direction = PopDisplayTextDirection.Neutral,
                        isAlternate = false,
                    )
                }
            }
        }
        onRoot().captureRoboImage(
            filePath = "src/desktopTest/snapshots/PopDataRow_allVariants_dark.png",
        )
    }
}
