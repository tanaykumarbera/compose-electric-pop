package co.tanay.electricpop.demo.components

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
import androidx.compose.ui.Modifier
import co.tanay.electricpop.foundation.PopTitleBar
import co.tanay.electricpop.theme.ElectricPopTheme

@Composable
fun PopTitleBarDemo() {
    val spacing = ElectricPopTheme.spacing
    val cs = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(spacing.lg),
        verticalArrangement = Arrangement.spacedBy(spacing.lg),
    ) {
        // Title only
        Text(
            text = "TITLE ONLY",
            style = MaterialTheme.typography.labelLarge,
            color = cs.onSurfaceVariant,
        )
        PopTitleBar(
            title = "Dashboard",
            modifier = Modifier.fillMaxWidth(),
        )

        // Title + status (matches Stitch "LIVE PREVIEW / SYSTEM ACTIVE")
        Text(
            text = "TITLE + STATUS",
            style = MaterialTheme.typography.labelLarge,
            color = cs.onSurfaceVariant,
        )
        PopTitleBar(
            title = "Live Preview",
            status = "System Active",
            modifier = Modifier.fillMaxWidth(),
        )

        // Another status variant
        Text(
            text = "TITLE + STATUS (LIVE)",
            style = MaterialTheme.typography.labelLarge,
            color = cs.onSurfaceVariant,
        )
        PopTitleBar(
            title = "Transactions",
            status = "Live",
            modifier = Modifier.fillMaxWidth(),
        )

        // Longer title
        Text(
            text = "LONGER TITLE",
            style = MaterialTheme.typography.labelLarge,
            color = cs.onSurfaceVariant,
        )
        PopTitleBar(
            title = "Portfolio Overview",
            status = "Updated",
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
