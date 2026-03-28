package com.electricpop.foundation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runDesktopComposeUiTest
import androidx.compose.ui.unit.dp
import com.electricpop.theme.ElectricPopTheme
import io.github.takahirom.roborazzi.captureRoboImage
import kotlin.test.Test

class PopIconScreenshotTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun allVariants_light() = runDesktopComposeUiTest(width = 500, height = 400) {
        setContent {
            ElectricPopTheme(darkTheme = false) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        PopIcon(imageVector = PopIcons.Star, contentDescription = null, size = PopIconSize.Small)
                        PopIcon(imageVector = PopIcons.Star, contentDescription = null, size = PopIconSize.Medium)
                        PopIcon(imageVector = PopIcons.Star, contentDescription = null, size = PopIconSize.Large)
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        PopIcon(imageVector = PopIcons.Star, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        PopIcon(imageVector = PopIcons.Heart, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        PopIcon(imageVector = PopIcons.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        PopIcon(imageVector = PopIcons.Star, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                        PopIcon(imageVector = PopIcons.Heart, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                        PopIcon(imageVector = PopIcons.Check, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        PopIcon(imageVector = PopIcons.Star, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary)
                        PopIcon(imageVector = PopIcons.Heart, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary)
                        PopIcon(imageVector = PopIcons.Check, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary)
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        PopIcon(imageVector = PopIcons.Star, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                        PopIcon(imageVector = PopIcons.Heart, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                        PopIcon(imageVector = PopIcons.Check, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
        onRoot().captureRoboImage(filePath = "src/desktopTest/snapshots/PopIcon_allVariants_light.png")
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun allVariants_dark() = runDesktopComposeUiTest(width = 500, height = 400) {
        setContent {
            ElectricPopTheme(darkTheme = true) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        PopIcon(imageVector = PopIcons.Star, contentDescription = null, size = PopIconSize.Small)
                        PopIcon(imageVector = PopIcons.Star, contentDescription = null, size = PopIconSize.Medium)
                        PopIcon(imageVector = PopIcons.Star, contentDescription = null, size = PopIconSize.Large)
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        PopIcon(imageVector = PopIcons.Star, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        PopIcon(imageVector = PopIcons.Heart, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        PopIcon(imageVector = PopIcons.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        PopIcon(imageVector = PopIcons.Star, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                        PopIcon(imageVector = PopIcons.Heart, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                        PopIcon(imageVector = PopIcons.Check, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        PopIcon(imageVector = PopIcons.Star, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary)
                        PopIcon(imageVector = PopIcons.Heart, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary)
                        PopIcon(imageVector = PopIcons.Check, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary)
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        PopIcon(imageVector = PopIcons.Star, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                        PopIcon(imageVector = PopIcons.Heart, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                        PopIcon(imageVector = PopIcons.Check, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
        onRoot().captureRoboImage(filePath = "src/desktopTest/snapshots/PopIcon_allVariants_dark.png")
    }
}
