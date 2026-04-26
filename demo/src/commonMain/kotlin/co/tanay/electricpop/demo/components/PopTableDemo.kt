package co.tanay.electricpop.demo.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import co.tanay.electricpop.foundation.PopTable
import co.tanay.electricpop.foundation.PopTableRow
import co.tanay.electricpop.theme.ElectricPopTheme

@Composable
fun PopTableDemo() {
    val spacing = ElectricPopTheme.spacing
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(spacing.lg),
        verticalArrangement = Arrangement.spacedBy(spacing.sm),
    ) {
        // Default Table — secondaryContainer (pink)
        Text(
            text = "DEFAULT TABLE",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopTable(
            title = "Tokens",
            rows = listOf(
                PopTableRow("Radius", "3rem (XL)"),
                PopTableRow("Font (H)", "Space Grotesk"),
                PopTableRow("Weight", "900 Italic"),
                PopTableRow("Elevation", "32px Blur"),
                PopTableRow("Accent", "Lime #CAFD00"),
            ),
        )

        Spacer(Modifier.height(spacing.md))

        // Tertiary Container — cyan
        Text(
            text = "TERTIARY CONTAINER",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopTable(
            title = "Network",
            rows = listOf(
                PopTableRow("Protocol", "HTTPS"),
                PopTableRow("Latency", "42ms"),
                PopTableRow("Uptime", "99.9%"),
            ),
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
        )

        Spacer(Modifier.height(spacing.md))

        // Primary Container — lime
        Text(
            text = "PRIMARY CONTAINER",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopTable(
            title = "Performance",
            rows = listOf(
                PopTableRow("First Paint", "340ms"),
                PopTableRow("Hot Restart", "120ms"),
                PopTableRow("Bundle Size", "2.4mb"),
            ),
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        )

        Spacer(Modifier.height(spacing.md))

        // No Title
        Text(
            text = "NO TITLE",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopTable(
            rows = listOf(
                PopTableRow("Protocol", "HTTPS"),
                PopTableRow("Latency", "42ms"),
                PopTableRow("Uptime", "99.9%"),
            ),
        )

        Spacer(Modifier.height(spacing.md))

        // Many Rows
        Text(
            text = "MANY ROWS",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PopTable(
            title = "Network Stats",
            rows = listOf(
                PopTableRow("Peers", "142"),
                PopTableRow("Block Height", "18,294,011"),
                PopTableRow("Hash Rate", "342 TH/s"),
                PopTableRow("Difficulty", "52.4T"),
                PopTableRow("Mempool", "2,847 tx"),
                PopTableRow("Avg Block", "12.3s"),
            ),
        )
    }
}
