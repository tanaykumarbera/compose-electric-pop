package com.electricpop.demo.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.electricpop.foundation.PopDropdown
import com.electricpop.theme.ElectricPopTheme

@Composable
fun PopDropdownDemo() {
    val spacing = ElectricPopTheme.spacing

    var currency by remember { mutableStateOf("USD") }
    var account by remember { mutableStateOf("Savings") }
    var accountType by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(spacing.lg),
        verticalArrangement = Arrangement.spacedBy(spacing.lg),
    ) {
        // Section: With Label
        Text(
            text = "WITH LABEL",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopDropdown(
            options = listOf("USD", "EUR", "GBP", "JPY"),
            selectedOption = currency,
            onOptionSelected = { currency = it },
            label = "Currency",
            modifier = Modifier.fillMaxWidth(),
        )

        // Section: Without Label
        Text(
            text = "WITHOUT LABEL",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopDropdown(
            options = listOf("Savings", "Checking", "Investment"),
            selectedOption = account,
            onOptionSelected = { account = it },
            modifier = Modifier.fillMaxWidth(),
        )

        // Section: Placeholder
        Text(
            text = "PLACEHOLDER",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopDropdown(
            options = listOf("Personal", "Business", "Joint"),
            selectedOption = accountType,
            onOptionSelected = { accountType = it },
            label = "Account Type",
            placeholder = "Select an account",
            modifier = Modifier.fillMaxWidth(),
        )

        // Section: Disabled
        Text(
            text = "DISABLED",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopDropdown(
            options = listOf("North America", "Europe", "Asia", "South America"),
            selectedOption = "North America",
            onOptionSelected = {},
            label = "Region",
            enabled = false,
            modifier = Modifier.fillMaxWidth(),
        )

        // Section: Many Options
        Text(
            text = "MANY OPTIONS",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopDropdown(
            options = listOf(
                "United States",
                "United Kingdom",
                "Canada",
                "Australia",
                "Germany",
                "France",
                "Japan",
                "Brazil",
            ),
            selectedOption = country,
            onOptionSelected = { country = it },
            label = "Country",
            placeholder = "Select a country",
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
