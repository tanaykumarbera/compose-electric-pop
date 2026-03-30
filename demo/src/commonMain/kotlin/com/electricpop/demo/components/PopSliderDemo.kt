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
import com.electricpop.foundation.PopSlider
import com.electricpop.theme.ElectricPopTheme

@Composable
fun PopSliderDemo() {
    val spacing = ElectricPopTheme.spacing

    // Interactive states
    var basicValue by remember { mutableStateOf(0.5f) }
    var volumeValue by remember { mutableStateOf(75f) }
    var qualityValue by remember { mutableStateOf(40f) }
    var brightnessValue by remember { mutableStateOf(80f) }
    var temperatureValue by remember { mutableStateOf(22f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(spacing.lg),
        verticalArrangement = Arrangement.spacedBy(spacing.lg),
    ) {
        // Section: DEFAULT
        Text(
            text = "DEFAULT",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopSlider(
            value = basicValue,
            onValueChange = { basicValue = it },
        )

        // Section: WITH LABEL + VALUE
        Text(
            text = "WITH LABEL + VALUE",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopSlider(
            value = volumeValue,
            onValueChange = { volumeValue = it },
            valueRange = 0f..100f,
            label = "Volume",
            showValue = true,
        )
        PopSlider(
            value = brightnessValue,
            onValueChange = { brightnessValue = it },
            valueRange = 0f..100f,
            label = "Brightness",
            showValue = true,
        )

        // Section: WITH STEPS
        Text(
            text = "WITH STEPS",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopSlider(
            value = qualityValue,
            onValueChange = { qualityValue = it },
            valueRange = 0f..100f,
            steps = 4,
            label = "Quality",
            showValue = true,
        )

        // Section: CUSTOM RANGE
        Text(
            text = "CUSTOM RANGE",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopSlider(
            value = temperatureValue,
            onValueChange = { temperatureValue = it },
            valueRange = 16f..30f,
            label = "Temperature",
            showValue = true,
        )

        // Section: DISABLED
        Text(
            text = "DISABLED",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopSlider(
            value = 0.3f,
            onValueChange = {},
            enabled = false,
        )
        PopSlider(
            value = 60f,
            onValueChange = {},
            valueRange = 0f..100f,
            enabled = false,
            label = "Locked",
            showValue = true,
        )
    }
}
