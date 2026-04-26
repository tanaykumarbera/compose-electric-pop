package co.tanay.electricpop.composite

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runDesktopComposeUiTest
import androidx.compose.ui.unit.dp
import co.tanay.electricpop.foundation.PopButtonStyle
import co.tanay.electricpop.foundation.PopChip
import co.tanay.electricpop.foundation.PopChipColor
import co.tanay.electricpop.foundation.PopDisplayText
import co.tanay.electricpop.foundation.PopDisplayTextDirection
import co.tanay.electricpop.foundation.PopDisplayTextSize
import co.tanay.electricpop.foundation.PopIcons
import co.tanay.electricpop.theme.ElectricPopTheme
import io.github.takahirom.roborazzi.captureRoboImage
import kotlin.test.Test

class PopActionCardScreenshotTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun allVariants_light() = runDesktopComposeUiTest(width = 420, height = 1600) {
        setContent { ActionCardContent(darkTheme = false) }
        onRoot().captureRoboImage(
            filePath = "src/desktopTest/snapshots/PopActionCard_allVariants_light.png",
        )
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun allVariants_dark() = runDesktopComposeUiTest(width = 420, height = 1600) {
        setContent { ActionCardContent(darkTheme = true) }
        onRoot().captureRoboImage(
            filePath = "src/desktopTest/snapshots/PopActionCard_allVariants_dark.png",
        )
    }
}

@Composable
private fun ActionCardContent(darkTheme: Boolean) {
    ElectricPopTheme(darkTheme = darkTheme) {
        // Read composable icon values at composable scope
        val iconCheckCircle = PopIcons.CheckCircle

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            // Variant 1: Full card
            PopActionCard(
                title = "Transaction Review",
                description = "Do you want to track this transaction?",
                chips = {
                    PopChip(label = "Bitcoin", color = PopChipColor.Primary)
                    PopChip(label = "Transfer", color = PopChipColor.Tertiary)
                },
                heroContent = {
                    PopDisplayText(
                        mainText = "\$42,069",
                        fractionalText = ".42",
                        direction = PopDisplayTextDirection.Positive,
                        size = PopDisplayTextSize.Large,
                    )
                },
                actions = listOf(
                    PopActionCardAction("Skip", {}, PopButtonStyle.Ghost),
                    PopActionCardAction("Track", {}, PopButtonStyle.Primary, iconCheckCircle),
                ),
            )

            // Variant 2: No description
            PopActionCard(
                title = "Transaction Review",
                chips = {
                    PopChip(label = "Bitcoin", color = PopChipColor.Primary)
                    PopChip(label = "Transfer", color = PopChipColor.Tertiary)
                },
                heroContent = {
                    PopDisplayText(
                        mainText = "\$42,069",
                        fractionalText = ".42",
                        direction = PopDisplayTextDirection.Positive,
                        size = PopDisplayTextSize.Large,
                    )
                },
                actions = listOf(
                    PopActionCardAction("Skip", {}, PopButtonStyle.Ghost),
                    PopActionCardAction("Track", {}, PopButtonStyle.Primary, iconCheckCircle),
                ),
            )

            // Variant 3: No chips
            PopActionCard(
                title = "Transaction Review",
                description = "Do you want to track this transaction?",
                heroContent = {
                    PopDisplayText(
                        mainText = "\$42,069",
                        fractionalText = ".42",
                        direction = PopDisplayTextDirection.Positive,
                        size = PopDisplayTextSize.Large,
                    )
                },
                actions = listOf(
                    PopActionCardAction("Skip", {}, PopButtonStyle.Ghost),
                    PopActionCardAction("Track", {}, PopButtonStyle.Primary, iconCheckCircle),
                ),
            )

            // Variant 4: Minimal (title + description + actions only)
            PopActionCard(
                title = "Transaction Review",
                description = "Do you want to track this transaction?",
                actions = listOf(
                    PopActionCardAction("Skip", {}, PopButtonStyle.Ghost),
                    PopActionCardAction("Track", {}, PopButtonStyle.Primary, iconCheckCircle),
                ),
            )
        }
    }
}
