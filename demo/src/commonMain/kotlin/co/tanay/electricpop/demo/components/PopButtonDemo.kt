package co.tanay.electricpop.demo.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import co.tanay.electricpop.foundation.PopButton
import co.tanay.electricpop.foundation.PopButtonSize
import co.tanay.electricpop.foundation.PopButtonStyle
import co.tanay.electricpop.foundation.PopIconButton
import co.tanay.electricpop.foundation.PopIcons
import co.tanay.electricpop.theme.ElectricPopTheme

@OptIn(ExperimentalLayoutApi::class)
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
        Section("SIZE: XL (DISPLAY)") {
            PopButton("Confirm Action", onClick = {}, style = PopButtonStyle.Primary, size = PopButtonSize.XL, icon = PopIcons.ArrowForward)
            PopButton("Alert Flow", onClick = {}, style = PopButtonStyle.Secondary, size = PopButtonSize.XL, icon = PopIcons.Warning)
            PopButton("Ghost Action", onClick = {}, style = PopButtonStyle.Ghost, size = PopButtonSize.XL)
        }

        Section("SIZE: LARGE (STANDARD)") {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(spacing.md),
                verticalArrangement = Arrangement.spacedBy(spacing.md),
            ) {
                PopButton("Primary CTA", onClick = {}, style = PopButtonStyle.Primary, size = PopButtonSize.Large, icon = PopIcons.Add)
                PopButton("Secondary", onClick = {}, style = PopButtonStyle.Secondary, size = PopButtonSize.Large)
                PopButton("Cancel", onClick = {}, style = PopButtonStyle.Ghost, size = PopButtonSize.Large, icon = PopIcons.Close)
            }
        }

        Section("SIZE: SMALL (UTILITY)") {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(spacing.sm),
                verticalArrangement = Arrangement.spacedBy(spacing.sm),
            ) {
                PopButton("Small Primary", onClick = {}, style = PopButtonStyle.Primary, size = PopButtonSize.Small)
                PopButton("Small Secondary", onClick = {}, style = PopButtonStyle.Secondary, size = PopButtonSize.Small)
                PopButton("Small Ghost", onClick = {}, style = PopButtonStyle.Ghost, size = PopButtonSize.Small)
                PopButton("Export", onClick = {}, style = PopButtonStyle.Tertiary, size = PopButtonSize.Small, icon = PopIcons.ArrowDown)
            }
        }

        Section("DISABLED") {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(spacing.md),
                verticalArrangement = Arrangement.spacedBy(spacing.md),
            ) {
                PopButton("Disabled", onClick = {}, style = PopButtonStyle.Primary, size = PopButtonSize.Large, enabled = false)
                PopButton("Disabled", onClick = {}, style = PopButtonStyle.Secondary, size = PopButtonSize.Large, enabled = false)
                PopButton("Disabled", onClick = {}, style = PopButtonStyle.Ghost, size = PopButtonSize.Large, enabled = false)
            }
        }

        Section("ICON BUTTONS") {
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacing.md),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                PopIconButton(icon = PopIcons.Add, onClick = {}, style = PopButtonStyle.Primary, contentDescription = "Add")
                PopIconButton(icon = PopIcons.Heart, onClick = {}, style = PopButtonStyle.Secondary, contentDescription = "Favorite")
                PopIconButton(icon = PopIcons.Bolt, onClick = {}, style = PopButtonStyle.Tertiary, contentDescription = "Boost")
                PopIconButton(icon = PopIcons.Settings, onClick = {}, style = PopButtonStyle.Ghost, contentDescription = "Settings")
            }
        }
    }
}

@Composable
private fun Section(title: String, content: @Composable () -> Unit) {
    val spacing = ElectricPopTheme.spacing
    Column(verticalArrangement = Arrangement.spacedBy(spacing.md)) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        content()
    }
}
