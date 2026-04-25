package co.tanay.electricpop.demo.components

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
import co.tanay.electricpop.foundation.PopRadioGroup
import co.tanay.electricpop.theme.ElectricPopTheme

@Composable
fun PopRadioGroupDemo() {
    val spacing = ElectricPopTheme.spacing

    var themeSelection by remember { mutableStateOf(0) }
    var sizeSelection by remember { mutableStateOf(1) }
    var planSelection by remember { mutableStateOf(2) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(spacing.lg),
        verticalArrangement = Arrangement.spacedBy(spacing.lg),
    ) {
        // Section: THEME
        Text(
            text = "THEME",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopRadioGroup(
            options = listOf("Light", "Dark", "System"),
            selectedIndex = themeSelection,
            onSelectedChange = { themeSelection = it },
        )

        // Section: SIZE
        Text(
            text = "SIZE",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopRadioGroup(
            options = listOf("Small", "Medium", "Large", "Extra Large"),
            selectedIndex = sizeSelection,
            onSelectedChange = { sizeSelection = it },
        )

        // Section: PLAN
        Text(
            text = "PLAN",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopRadioGroup(
            options = listOf("Free", "Pro", "Enterprise"),
            selectedIndex = planSelection,
            onSelectedChange = { planSelection = it },
        )

        // Section: DISABLED
        Text(
            text = "DISABLED",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopRadioGroup(
            options = listOf("Option A", "Option B", "Option C"),
            selectedIndex = 1,
            onSelectedChange = {},
            enabled = false,
        )
    }
}
