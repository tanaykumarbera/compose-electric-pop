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
import com.electricpop.foundation.PopIcons
import com.electricpop.foundation.PopPasswordField
import com.electricpop.foundation.PopTextField
import com.electricpop.theme.ElectricPopTheme

@Composable
fun PopTextFieldDemo() {
    val spacing = ElectricPopTheme.spacing

    // State for interactive fields
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var search by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = androidx.compose.ui.Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(spacing.lg),
        verticalArrangement = Arrangement.spacedBy(spacing.md),
    ) {
        // Section 1: Standard Text Fields
        Text(
            text = "STANDARD TEXT FIELDS",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopTextField(
            value = username,
            onValueChange = { username = it },
            label = "Username",
            placeholder = "Enter your username",
        )
        PopTextField(
            value = email,
            onValueChange = { email = it },
            label = "Email",
            placeholder = "you@example.com",
        )

        // Section 2: With Icons
        Text(
            text = "WITH ICONS",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopTextField(
            value = search,
            onValueChange = { search = it },
            label = "Search",
            placeholder = "Search items...",
            leadingIcon = PopIcons.Search,
        )
        PopTextField(
            value = amount,
            onValueChange = { amount = it },
            label = "Amount",
            placeholder = "0.00",
            trailingIcon = PopIcons.Bolt,
        )

        // Section 3: Password Field
        Text(
            text = "PASSWORD FIELD",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopPasswordField(
            value = password,
            onValueChange = { password = it },
            label = "Password",
            placeholder = "Enter password",
        )

        // Section 4: Error States
        Text(
            text = "ERROR STATES",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopTextField(
            value = "not-an-email",
            onValueChange = {},
            label = "Email",
            isError = true,
            errorMessage = "Please enter a valid email",
        )
        PopTextField(
            value = "",
            onValueChange = {},
            label = "Required Field",
            isError = true,
            errorMessage = "This field is required",
        )

        // Section 5: Disabled
        Text(
            text = "DISABLED",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopTextField(
            value = "Cannot edit",
            onValueChange = {},
            label = "Disabled Field",
            enabled = false,
        )
    }
}
