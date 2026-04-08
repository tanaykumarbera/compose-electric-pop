package com.electricpop.demo.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.electricpop.foundation.PopBottomBar
import com.electricpop.foundation.PopBottomBarItem
import com.electricpop.foundation.PopIcon
import com.electricpop.foundation.PopIcons
import com.electricpop.theme.ElectricPopTheme

@Composable
fun PopBottomBarDemo() {
    val spacing = ElectricPopTheme.spacing

    var iconsOnlySelected by remember { mutableIntStateOf(0) }
    var iconsLabelsSelected by remember { mutableIntStateOf(0) }
    var fiveItemsSelected by remember { mutableIntStateOf(0) }
    var threeItemsSelected by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(spacing.lg),
        verticalArrangement = Arrangement.spacedBy(spacing.sm),
    ) {
        Text(
            text = "ICONS ONLY",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopBottomBar(modifier = Modifier.fillMaxWidth()) {
            PopBottomBarItem(
                selected = iconsOnlySelected == 0,
                onClick = { iconsOnlySelected = 0 },
                icon = { PopIcon(PopIcons.Home, "Home", tint = LocalContentColor.current) },
            )
            PopBottomBarItem(
                selected = iconsOnlySelected == 1,
                onClick = { iconsOnlySelected = 1 },
                icon = { PopIcon(PopIcons.Search, "Search", tint = LocalContentColor.current) },
            )
            PopBottomBarItem(
                selected = iconsOnlySelected == 2,
                onClick = { iconsOnlySelected = 2 },
                icon = { PopIcon(PopIcons.Heart, "Saved", tint = LocalContentColor.current) },
            )
            PopBottomBarItem(
                selected = iconsOnlySelected == 3,
                onClick = { iconsOnlySelected = 3 },
                icon = { PopIcon(PopIcons.Person, "Profile", tint = LocalContentColor.current) },
            )
        }

        Spacer(Modifier.height(spacing.md))

        Text(
            text = "ICONS + LABELS",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopBottomBar(modifier = Modifier.fillMaxWidth()) {
            PopBottomBarItem(
                selected = iconsLabelsSelected == 0,
                onClick = { iconsLabelsSelected = 0 },
                icon = { PopIcon(PopIcons.Home, "Home", tint = LocalContentColor.current) },
                label = { Text("HOME") },
            )
            PopBottomBarItem(
                selected = iconsLabelsSelected == 1,
                onClick = { iconsLabelsSelected = 1 },
                icon = { PopIcon(PopIcons.Search, "Search", tint = LocalContentColor.current) },
                label = { Text("SEARCH") },
            )
            PopBottomBarItem(
                selected = iconsLabelsSelected == 2,
                onClick = { iconsLabelsSelected = 2 },
                icon = { PopIcon(PopIcons.Heart, "Saved", tint = LocalContentColor.current) },
                label = { Text("SAVED") },
            )
            PopBottomBarItem(
                selected = iconsLabelsSelected == 3,
                onClick = { iconsLabelsSelected = 3 },
                icon = { PopIcon(PopIcons.Person, "Profile", tint = LocalContentColor.current) },
                label = { Text("PROFILE") },
            )
        }

        Spacer(Modifier.height(spacing.md))

        Text(
            text = "5 ITEMS",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopBottomBar(modifier = Modifier.fillMaxWidth()) {
            PopBottomBarItem(
                selected = fiveItemsSelected == 0,
                onClick = { fiveItemsSelected = 0 },
                icon = { PopIcon(PopIcons.Home, "Home", tint = LocalContentColor.current) },
                label = { Text("HOME") },
            )
            PopBottomBarItem(
                selected = fiveItemsSelected == 1,
                onClick = { fiveItemsSelected = 1 },
                icon = { PopIcon(PopIcons.Search, "Search", tint = LocalContentColor.current) },
                label = { Text("SEARCH") },
            )
            PopBottomBarItem(
                selected = fiveItemsSelected == 2,
                onClick = { fiveItemsSelected = 2 },
                icon = { PopIcon(PopIcons.Add, "New", tint = LocalContentColor.current) },
                label = { Text("NEW") },
            )
            PopBottomBarItem(
                selected = fiveItemsSelected == 3,
                onClick = { fiveItemsSelected = 3 },
                icon = { PopIcon(PopIcons.Heart, "Saved", tint = LocalContentColor.current) },
                label = { Text("SAVED") },
            )
            PopBottomBarItem(
                selected = fiveItemsSelected == 4,
                onClick = { fiveItemsSelected = 4 },
                icon = { PopIcon(PopIcons.Person, "Profile", tint = LocalContentColor.current) },
                label = { Text("PROFILE") },
            )
        }

        Spacer(Modifier.height(spacing.md))

        Text(
            text = "3 ITEMS",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopBottomBar(modifier = Modifier.fillMaxWidth()) {
            PopBottomBarItem(
                selected = threeItemsSelected == 0,
                onClick = { threeItemsSelected = 0 },
                icon = { PopIcon(PopIcons.Home, "Home", tint = LocalContentColor.current) },
                label = { Text("HOME") },
            )
            PopBottomBarItem(
                selected = threeItemsSelected == 1,
                onClick = { threeItemsSelected = 1 },
                icon = { PopIcon(PopIcons.Sparkle, "Explore", tint = LocalContentColor.current) },
                label = { Text("EXPLORE") },
            )
            PopBottomBarItem(
                selected = threeItemsSelected == 2,
                onClick = { threeItemsSelected = 2 },
                icon = { PopIcon(PopIcons.Settings, "Settings", tint = LocalContentColor.current) },
                label = { Text("SETTINGS") },
            )
        }
    }
}
