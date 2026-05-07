package co.tanay.electricpop.foundation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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

class PopButtonScreenshotTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun popButton_allVariants_light() = runDesktopComposeUiTest(width = 720, height = 1100) {
        setContent {
            ElectricPopTheme(darkTheme = false) { ButtonContent() }
        }
        onRoot().captureRoboImage(
            filePath = "src/desktopTest/snapshots/PopButton_allVariants_light.png",
        )
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun popButton_allVariants_dark() = runDesktopComposeUiTest(width = 720, height = 1100) {
        setContent {
            ElectricPopTheme(darkTheme = true) { ButtonContent() }
        }
        onRoot().captureRoboImage(
            filePath = "src/desktopTest/snapshots/PopButton_allVariants_dark.png",
        )
    }

    @Composable
    private fun ButtonContent() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
        ) {
            ButtonContentInner()
        }
    }

    @Composable
    private fun ButtonContentInner() {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            SectionLabel("SIZE: XL (DISPLAY)")
            PopButton("Confirm Action", onClick = {}, style = PopButtonStyle.Primary, size = PopButtonSize.XL, icon = PopIcons.ArrowForward)
            PopButton("Alert Flow", onClick = {}, style = PopButtonStyle.Secondary, size = PopButtonSize.XL, icon = PopIcons.Warning)
            PopButton("Ghost Action", onClick = {}, style = PopButtonStyle.Ghost, size = PopButtonSize.XL)

            SectionLabel("SIZE: LARGE (STANDARD)")
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                PopButton("Primary CTA", onClick = {}, style = PopButtonStyle.Primary, size = PopButtonSize.Large, icon = PopIcons.Add)
                PopButton("Secondary", onClick = {}, style = PopButtonStyle.Secondary, size = PopButtonSize.Large)
                PopButton("Cancel", onClick = {}, style = PopButtonStyle.Ghost, size = PopButtonSize.Large, icon = PopIcons.Close)
            }

            SectionLabel("SIZE: SMALL (UTILITY)")
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                PopButton("Small Primary", onClick = {}, style = PopButtonStyle.Primary, size = PopButtonSize.Small)
                PopButton("Small Secondary", onClick = {}, style = PopButtonStyle.Secondary, size = PopButtonSize.Small)
                PopButton("Small Ghost", onClick = {}, style = PopButtonStyle.Ghost, size = PopButtonSize.Small)
                PopButton("Export", onClick = {}, style = PopButtonStyle.Tertiary, size = PopButtonSize.Small, icon = PopIcons.ArrowDown)
            }

            SectionLabel("DISABLED")
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                PopButton("Disabled", onClick = {}, style = PopButtonStyle.Primary, size = PopButtonSize.Large, enabled = false)
                PopButton("Disabled", onClick = {}, style = PopButtonStyle.Secondary, size = PopButtonSize.Large, enabled = false)
                PopButton("Disabled", onClick = {}, style = PopButtonStyle.Ghost, size = PopButtonSize.Large, enabled = false)
            }

            SectionLabel("ICON BUTTONS")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                PopButtonStyle.entries.forEach { style ->
                    PopIconButton(
                        icon = PopIcons.Bolt,
                        onClick = {},
                        style = style,
                        contentDescription = style.name,
                    )
                }
            }
        }
    }

    @Composable
    private fun SectionLabel(text: String) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
