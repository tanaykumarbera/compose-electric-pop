package com.electricpop.demo.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.electricpop.foundation.PopSwitch
import com.electricpop.theme.ElectricPopTheme

@Composable
fun PopSwitchDemo() {
    val spacing = ElectricPopTheme.spacing

    // Interactive state for STATES section
    var uncheckedState by remember { mutableStateOf(false) }
    var checkedState by remember { mutableStateOf(true) }

    // Interactive state for WITH LABEL section
    var notifications by remember { mutableStateOf(false) }
    var darkMode by remember { mutableStateOf(true) }
    var autoSync by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(spacing.lg),
        verticalArrangement = Arrangement.spacedBy(spacing.lg),
    ) {
        // Section: STATES
        Text(
            text = "STATES",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopSwitch(
            checked = uncheckedState,
            onCheckedChange = { uncheckedState = it },
        )
        PopSwitch(
            checked = checkedState,
            onCheckedChange = { checkedState = it },
        )

        // Section: WITH LABEL
        Text(
            text = "WITH LABEL",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopSwitch(
            checked = notifications,
            onCheckedChange = { notifications = it },
            label = "Notifications",
        )
        PopSwitch(
            checked = darkMode,
            onCheckedChange = { darkMode = it },
            label = "Dark Mode",
        )
        PopSwitch(
            checked = autoSync,
            onCheckedChange = { autoSync = it },
            label = "Auto-Sync",
        )

        // Section: DISABLED
        Text(
            text = "DISABLED",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopSwitch(
            checked = false,
            onCheckedChange = {},
            enabled = false,
        )
        PopSwitch(
            checked = true,
            onCheckedChange = {},
            enabled = false,
        )
        PopSwitch(
            checked = false,
            onCheckedChange = {},
            enabled = false,
            label = "Unavailable",
        )
        PopSwitch(
            checked = true,
            onCheckedChange = {},
            enabled = false,
            label = "Always On",
        )
    }
}
