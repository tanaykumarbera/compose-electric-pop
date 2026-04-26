package co.tanay.electricpop.foundation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runDesktopComposeUiTest
import androidx.compose.ui.unit.dp
import co.tanay.electricpop.theme.ElectricPopTheme
import io.github.takahirom.roborazzi.captureRoboImage
import kotlin.test.Test

class PopBottomBarScreenshotTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun popBottomBar_allVariants_light() = runDesktopComposeUiTest(width = 420, height = 550) {
        setContent {
            ElectricPopTheme(darkTheme = false) {
                Column(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Text(
                        text = "ICONS ONLY",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopBottomBar(modifier = Modifier.fillMaxWidth()) {
                        PopBottomBarItem(
                            selected = true,
                            onClick = {},
                            icon = { PopIcon(PopIcons.Home, "Home", tint = LocalContentColor.current) },
                        )
                        PopBottomBarItem(
                            selected = false,
                            onClick = {},
                            icon = { PopIcon(PopIcons.Search, "Search", tint = LocalContentColor.current) },
                        )
                        PopBottomBarItem(
                            selected = false,
                            onClick = {},
                            icon = { PopIcon(PopIcons.Heart, "Saved", tint = LocalContentColor.current) },
                        )
                        PopBottomBarItem(
                            selected = false,
                            onClick = {},
                            icon = { PopIcon(PopIcons.Person, "Profile", tint = LocalContentColor.current) },
                        )
                    }
                    Text(
                        text = "ICONS + LABELS",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopBottomBar(modifier = Modifier.fillMaxWidth()) {
                        PopBottomBarItem(
                            selected = false,
                            onClick = {},
                            icon = { PopIcon(PopIcons.Home, "Home", tint = LocalContentColor.current) },
                            label = { Text("HOME") },
                        )
                        PopBottomBarItem(
                            selected = true,
                            onClick = {},
                            icon = { PopIcon(PopIcons.Search, "Search", tint = LocalContentColor.current) },
                            label = { Text("SEARCH") },
                        )
                        PopBottomBarItem(
                            selected = false,
                            onClick = {},
                            icon = { PopIcon(PopIcons.Heart, "Saved", tint = LocalContentColor.current) },
                            label = { Text("SAVED") },
                        )
                        PopBottomBarItem(
                            selected = false,
                            onClick = {},
                            icon = { PopIcon(PopIcons.Person, "Profile", tint = LocalContentColor.current) },
                            label = { Text("PROFILE") },
                        )
                    }
                    Text(
                        text = "DIFFERENT SELECTION",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopBottomBar(modifier = Modifier.fillMaxWidth()) {
                        PopBottomBarItem(
                            selected = false,
                            onClick = {},
                            icon = { PopIcon(PopIcons.Home, "Home", tint = LocalContentColor.current) },
                            label = { Text("HOME") },
                        )
                        PopBottomBarItem(
                            selected = false,
                            onClick = {},
                            icon = { PopIcon(PopIcons.Search, "Search", tint = LocalContentColor.current) },
                            label = { Text("SEARCH") },
                        )
                        PopBottomBarItem(
                            selected = true,
                            onClick = {},
                            icon = { PopIcon(PopIcons.Heart, "Saved", tint = LocalContentColor.current) },
                            label = { Text("SAVED") },
                        )
                        PopBottomBarItem(
                            selected = false,
                            onClick = {},
                            icon = { PopIcon(PopIcons.Person, "Profile", tint = LocalContentColor.current) },
                            label = { Text("PROFILE") },
                        )
                    }
                }
            }
        }
        onRoot().captureRoboImage("src/desktopTest/snapshots/PopBottomBar_allVariants_light.png")
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun popBottomBar_allVariants_dark() = runDesktopComposeUiTest(width = 420, height = 550) {
        setContent {
            ElectricPopTheme(darkTheme = true) {
                Column(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Text(
                        text = "ICONS ONLY",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopBottomBar(modifier = Modifier.fillMaxWidth()) {
                        PopBottomBarItem(
                            selected = true,
                            onClick = {},
                            icon = { PopIcon(PopIcons.Home, "Home", tint = LocalContentColor.current) },
                        )
                        PopBottomBarItem(
                            selected = false,
                            onClick = {},
                            icon = { PopIcon(PopIcons.Search, "Search", tint = LocalContentColor.current) },
                        )
                        PopBottomBarItem(
                            selected = false,
                            onClick = {},
                            icon = { PopIcon(PopIcons.Heart, "Saved", tint = LocalContentColor.current) },
                        )
                        PopBottomBarItem(
                            selected = false,
                            onClick = {},
                            icon = { PopIcon(PopIcons.Person, "Profile", tint = LocalContentColor.current) },
                        )
                    }
                    Text(
                        text = "ICONS + LABELS",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopBottomBar(modifier = Modifier.fillMaxWidth()) {
                        PopBottomBarItem(
                            selected = false,
                            onClick = {},
                            icon = { PopIcon(PopIcons.Home, "Home", tint = LocalContentColor.current) },
                            label = { Text("HOME") },
                        )
                        PopBottomBarItem(
                            selected = true,
                            onClick = {},
                            icon = { PopIcon(PopIcons.Search, "Search", tint = LocalContentColor.current) },
                            label = { Text("SEARCH") },
                        )
                        PopBottomBarItem(
                            selected = false,
                            onClick = {},
                            icon = { PopIcon(PopIcons.Heart, "Saved", tint = LocalContentColor.current) },
                            label = { Text("SAVED") },
                        )
                        PopBottomBarItem(
                            selected = false,
                            onClick = {},
                            icon = { PopIcon(PopIcons.Person, "Profile", tint = LocalContentColor.current) },
                            label = { Text("PROFILE") },
                        )
                    }
                    Text(
                        text = "DIFFERENT SELECTION",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PopBottomBar(modifier = Modifier.fillMaxWidth()) {
                        PopBottomBarItem(
                            selected = false,
                            onClick = {},
                            icon = { PopIcon(PopIcons.Home, "Home", tint = LocalContentColor.current) },
                            label = { Text("HOME") },
                        )
                        PopBottomBarItem(
                            selected = false,
                            onClick = {},
                            icon = { PopIcon(PopIcons.Search, "Search", tint = LocalContentColor.current) },
                            label = { Text("SEARCH") },
                        )
                        PopBottomBarItem(
                            selected = true,
                            onClick = {},
                            icon = { PopIcon(PopIcons.Heart, "Saved", tint = LocalContentColor.current) },
                            label = { Text("SAVED") },
                        )
                        PopBottomBarItem(
                            selected = false,
                            onClick = {},
                            icon = { PopIcon(PopIcons.Person, "Profile", tint = LocalContentColor.current) },
                            label = { Text("PROFILE") },
                        )
                    }
                }
            }
        }
        onRoot().captureRoboImage("src/desktopTest/snapshots/PopBottomBar_allVariants_dark.png")
    }
}
