package com.electricpop.demo.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.electricpop.foundation.PopButton
import com.electricpop.foundation.PopButtonSize
import com.electricpop.foundation.PopButtonStyle
import com.electricpop.foundation.PopIconButton
import com.electricpop.foundation.PopIcons
import com.electricpop.theme.ElectricPopTheme

@Composable
fun PopButtonDemo() {
    val spacing = ElectricPopTheme.spacing
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(spacing.lg),
        verticalArrangement = Arrangement.spacedBy(spacing.lg),
    ) {
        // Section: Primary Buttons
        Text(
            text = "PRIMARY BUTTONS",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopButton(
            text = "Get Started",
            onClick = {},
            style = PopButtonStyle.Primary,
            size = PopButtonSize.XL,
        )
        PopButton(
            text = "Send",
            onClick = {},
            style = PopButtonStyle.Primary,
            size = PopButtonSize.Large,
        )
        PopButton(
            text = "Continue",
            onClick = {},
            style = PopButtonStyle.Primary,
            size = PopButtonSize.Small,
        )

        // Section: Secondary Buttons
        Text(
            text = "SECONDARY BUTTONS",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopButton(
            text = "Learn More",
            onClick = {},
            style = PopButtonStyle.Secondary,
            size = PopButtonSize.XL,
        )
        PopButton(
            text = "Details",
            onClick = {},
            style = PopButtonStyle.Secondary,
            size = PopButtonSize.Large,
        )
        PopButton(
            text = "Info",
            onClick = {},
            style = PopButtonStyle.Secondary,
            size = PopButtonSize.Small,
        )

        // Section: Ghost Buttons
        Text(
            text = "GHOST BUTTONS",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopButton(
            text = "Cancel",
            onClick = {},
            style = PopButtonStyle.Ghost,
            size = PopButtonSize.XL,
        )
        PopButton(
            text = "Skip",
            onClick = {},
            style = PopButtonStyle.Ghost,
            size = PopButtonSize.Large,
        )
        PopButton(
            text = "Later",
            onClick = {},
            style = PopButtonStyle.Ghost,
            size = PopButtonSize.Small,
        )

        // Section: With Icon
        Text(
            text = "WITH ICON",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopButton(
            text = "Send",
            onClick = {},
            style = PopButtonStyle.Primary,
            size = PopButtonSize.Large,
            icon = PopIcons.Bolt,
        )
        PopButton(
            text = "Next",
            onClick = {},
            style = PopButtonStyle.Secondary,
            size = PopButtonSize.Large,
            icon = PopIcons.ArrowForward,
        )

        // Section: Icon Buttons
        Text(
            text = "ICON BUTTONS",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(spacing.md)) {
            PopIconButton(
                icon = PopIcons.Add,
                onClick = {},
                style = PopButtonStyle.Primary,
                contentDescription = "Add",
            )
            PopIconButton(
                icon = PopIcons.Heart,
                onClick = {},
                style = PopButtonStyle.Secondary,
                contentDescription = "Favorite",
            )
            PopIconButton(
                icon = PopIcons.Settings,
                onClick = {},
                style = PopButtonStyle.Ghost,
                contentDescription = "Settings",
            )
        }

        // Section: Disabled
        Text(
            text = "DISABLED",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopButton(
            text = "Disabled Primary",
            onClick = {},
            style = PopButtonStyle.Primary,
            enabled = false,
        )
        PopButton(
            text = "Disabled Secondary",
            onClick = {},
            style = PopButtonStyle.Secondary,
            enabled = false,
        )
        PopButton(
            text = "Disabled Ghost",
            onClick = {},
            style = PopButtonStyle.Ghost,
            enabled = false,
        )
    }
}
