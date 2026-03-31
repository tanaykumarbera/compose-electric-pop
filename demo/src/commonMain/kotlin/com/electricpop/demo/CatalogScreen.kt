package com.electricpop.demo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.electricpop.demo.components.PopIconDemo
import com.electricpop.demo.components.PopPillDemo
import com.electricpop.demo.components.PopSurfaceDemo
import com.electricpop.demo.components.PopBadgeDemo
import com.electricpop.demo.components.PopButtonDemo
import com.electricpop.demo.components.PopTextFieldDemo
import com.electricpop.demo.components.PopSwitchDemo
import com.electricpop.demo.components.PopSliderDemo
import com.electricpop.demo.components.PopRadioGroupDemo
import com.electricpop.demo.components.PopChipDemo

data class CatalogEntry(
    val name: String,
    val tier: String,
    val content: @Composable () -> Unit,
)

// Components register themselves here as they are implemented via Pixy.
// Add entries in the format:
//   CatalogEntry("PopButton", "Foundation") { PopButtonDemo() }
val catalogEntries = listOf<CatalogEntry>(
    // Wave 1: Core Foundation
    CatalogEntry("PopPill", "Foundation") { PopPillDemo() },
    CatalogEntry("PopIcon", "Foundation") { PopIconDemo() },
    CatalogEntry("PopSurface", "Foundation") { PopSurfaceDemo() },
    CatalogEntry("PopBadge", "Foundation") { PopBadgeDemo() },

    // Wave 2: Input Foundation
    CatalogEntry("PopButton", "Foundation") { PopButtonDemo() },
    CatalogEntry("PopTextField", "Foundation") { PopTextFieldDemo() },
    CatalogEntry("PopSwitch", "Foundation") { PopSwitchDemo() },
    CatalogEntry("PopSlider", "Foundation") { PopSliderDemo() },
    CatalogEntry("PopRadioGroup", "Foundation") { PopRadioGroupDemo() },
    CatalogEntry("PopChip", "Foundation") { PopChipDemo() },
    // CatalogEntry("PopDropdown", "Foundation") { PopDropdownDemo() },

    // Wave 3: Layout Foundation
    // CatalogEntry("PopIconRow", "Foundation") { PopIconRowDemo() },
    // CatalogEntry("PopSectionHeader", "Foundation") { PopSectionHeaderDemo() },
    // CatalogEntry("PopTitleBar", "Foundation") { PopTitleBarDemo() },
    // CatalogEntry("PopDisplayText", "Foundation") { PopDisplayTextDemo() },
    // CatalogEntry("PopIconListItem", "Foundation") { PopIconListItemDemo() },
    // CatalogEntry("PopStepList", "Foundation") { PopStepListDemo() },
    // CatalogEntry("PopTable", "Foundation") { PopTableDemo() },
    // CatalogEntry("PopCodeBlock", "Foundation") { PopCodeBlockDemo() },
    // CatalogEntry("PopBottomBar", "Foundation") { PopBottomBarDemo() },

    // Wave 4: Composites
    // CatalogEntry("PopDataRow", "Composite") { PopDataRowDemo() },
    // CatalogEntry("PopMetricCard", "Composite") { PopMetricCardDemo() },
    // CatalogEntry("PopFeatureCard", "Composite") { PopFeatureCardDemo() },
    // CatalogEntry("PopDashboardCard", "Composite") { PopDashboardCardDemo() },
    // CatalogEntry("PopCarouselCard", "Composite") { PopCarouselCardDemo() },
    // CatalogEntry("PopActionCard", "Composite") { PopActionCardDemo() },
    // CatalogEntry("PopBannerCard", "Composite") { PopBannerCardDemo() },

    // Wave 5: Charts
    // CatalogEntry("PopLineChart", "Chart") { PopLineChartDemo() },
    // CatalogEntry("PopBarChart", "Chart") { PopBarChartDemo() },
    // CatalogEntry("PopDonutChart", "Chart") { PopDonutChartDemo() },
)

@Composable
fun CatalogScreen(onSelect: (CatalogEntry) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item {
            Text(
                text = "ELECTRIC POP",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Component Catalog",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(24.dp))
        }

        if (catalogEntries.isEmpty()) {
            item {
                Text(
                    text = "No components yet. Invoke Pixy to build them.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        items(catalogEntries) { entry ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelect(entry) },
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                ),
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(entry.name, style = MaterialTheme.typography.titleMedium)
                        Text(
                            entry.tier,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
    }
}
