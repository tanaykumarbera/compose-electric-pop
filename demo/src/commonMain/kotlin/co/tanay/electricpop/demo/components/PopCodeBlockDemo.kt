package co.tanay.electricpop.demo.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import co.tanay.electricpop.foundation.PopCodeBlock
import co.tanay.electricpop.theme.ElectricPopTheme

@Composable
fun PopCodeBlockDemo() {
    val spacing = ElectricPopTheme.spacing

    val kotlinSnippet = """
val theme = ElectricPopTheme {
    colorScheme = darkColorScheme()
}
    """.trimIndent()

    val jsonSnippet = """
{
  "component": "PopCodeBlock",
  "tier": "foundation"
}
    """.trimIndent()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(spacing.lg),
        verticalArrangement = Arrangement.spacedBy(spacing.sm),
    ) {
        // 1. Kotlin snippet — label + copy
        Text(
            text = "KOTLIN SNIPPET",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopCodeBlock(
            code = kotlinSnippet,
            label = "KOTLIN",
            onCopy = { /* copy handled by platform */ },
        )

        Spacer(Modifier.height(spacing.md))

        // 2. JSON data — label + copy
        Text(
            text = "JSON DATA",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopCodeBlock(
            code = jsonSnippet,
            label = "JSON",
            onCopy = { /* copy handled by platform */ },
        )

        Spacer(Modifier.height(spacing.md))

        // 3. Shell command — label + copy
        Text(
            text = "SHELL COMMAND",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopCodeBlock(
            code = "./gradlew :library:build",
            label = "SHELL",
            onCopy = { /* copy handled by platform */ },
        )

        Spacer(Modifier.height(spacing.md))

        // 4. No header — bare code block
        Text(
            text = "NO HEADER",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopCodeBlock(
            code = "No label, no copy button.\nJust pre-formatted text.",
        )

        Spacer(Modifier.height(spacing.md))

        // 5. Label only — no copy button
        Text(
            text = "LABEL ONLY",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopCodeBlock(
            code = "SELECT * FROM components\nWHERE tier = 'foundation';",
            label = "SQL",
        )

        Spacer(Modifier.height(spacing.md))

        // 6. Custom colors — tertiaryContainer background
        Text(
            text = "CUSTOM COLORS",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopCodeBlock(
            code = "primary: #CAFD00\nsecondary: #FF2D78",
            label = "CONFIG",
            onCopy = { /* copy handled by platform */ },
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
        )
    }
}
