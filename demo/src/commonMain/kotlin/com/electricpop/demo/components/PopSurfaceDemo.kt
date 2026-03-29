package com.electricpop.demo.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.electricpop.foundation.PopSurface
import com.electricpop.foundation.PopSurfaceTone
import com.electricpop.theme.ElectricPopTheme

@Composable
fun PopSurfaceDemo() {
    val spacing = ElectricPopTheme.spacing
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(spacing.lg),
        verticalArrangement = Arrangement.spacedBy(spacing.lg),
    ) {
        // Section: Tonal Levels
        Text(
            text = "TONAL LEVELS",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopSurfaceTone.entries.forEach { tone ->
            PopSurface(
                tone = tone,
                modifier = Modifier.fillMaxWidth().height(72.dp),
            ) {
                Column(modifier = Modifier.padding(spacing.md)) {
                    Text(
                        text = tone.name.uppercase(),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = "PopSurface with ${tone.name} tone",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }

        // Section: Shadow vs No Shadow
        Text(
            text = "SHADOW COMPARISON",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopSurface(
            shadowEnabled = true,
            modifier = Modifier.fillMaxWidth().height(72.dp),
        ) {
            Text(
                text = "With tonal shadow",
                modifier = Modifier.padding(spacing.md),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
        PopSurface(
            shadowEnabled = false,
            modifier = Modifier.fillMaxWidth().height(72.dp),
        ) {
            Text(
                text = "Without shadow",
                modifier = Modifier.padding(spacing.md),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }

        // Section: Ghost Border
        Text(
            text = "GHOST BORDER (ACCESSIBILITY)",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopSurface(
            ghostBorder = true,
            modifier = Modifier.fillMaxWidth().height(72.dp),
        ) {
            Text(
                text = "Ghost border enabled (outlineVariant @ 15%)",
                modifier = Modifier.padding(spacing.md),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }

        // Section: Custom Container Color
        Text(
            text = "CUSTOM CONTAINER COLOR",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopSurface(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.fillMaxWidth().height(72.dp),
        ) {
            Text(
                text = "Primary container color",
                modifier = Modifier.padding(spacing.md),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
        PopSurface(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            modifier = Modifier.fillMaxWidth().height(72.dp),
        ) {
            Text(
                text = "Tertiary container color",
                modifier = Modifier.padding(spacing.md),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
            )
        }

        // Section: Different Shapes
        Text(
            text = "SHAPE VARIANTS",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopSurface(
            shape = MaterialTheme.shapes.extraLarge,
            modifier = Modifier.fillMaxWidth().height(72.dp),
        ) {
            Text(
                text = "Extra Large (default squircle)",
                modifier = Modifier.padding(spacing.md),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
        PopSurface(
            shape = MaterialTheme.shapes.large,
            modifier = Modifier.fillMaxWidth().height(72.dp),
        ) {
            Text(
                text = "Large squircle",
                modifier = Modifier.padding(spacing.md),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
        PopSurface(
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth().height(72.dp),
        ) {
            Text(
                text = "Medium squircle",
                modifier = Modifier.padding(spacing.md),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}
