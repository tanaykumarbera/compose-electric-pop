package co.tanay.electricpop.demo

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import co.tanay.electricpop.demo.components.PopActionCardDemo
import co.tanay.electricpop.demo.components.PopBadgeDemo
import co.tanay.electricpop.demo.components.PopBannerCardDemo
import co.tanay.electricpop.demo.components.PopBottomBarDemo
import co.tanay.electricpop.demo.components.PopButtonDemo
import co.tanay.electricpop.demo.components.PopCarouselCardDemo
import co.tanay.electricpop.demo.components.PopChartDemo
import co.tanay.electricpop.demo.components.PopChipDemo
import co.tanay.electricpop.demo.components.PopCodeBlockDemo
import co.tanay.electricpop.demo.components.PopDashboardCardDemo
import co.tanay.electricpop.demo.components.PopDataRowDemo
import co.tanay.electricpop.demo.components.PopDisplayTextDemo
import co.tanay.electricpop.demo.components.PopDropdownDemo
import co.tanay.electricpop.demo.components.PopIconDemo
import co.tanay.electricpop.demo.components.PopIconListItemDemo
import co.tanay.electricpop.demo.components.PopImageBannerCardDemo
import co.tanay.electricpop.demo.components.PopRadioGroupDemo
import co.tanay.electricpop.demo.components.PopSectionHeaderDemo
import co.tanay.electricpop.demo.components.PopSliderDemo
import co.tanay.electricpop.demo.components.PopStepListDemo
import co.tanay.electricpop.demo.components.PopSurfaceDemo
import co.tanay.electricpop.demo.components.PopSwitchDemo
import co.tanay.electricpop.demo.components.PopTableDemo
import co.tanay.electricpop.demo.components.PopTextFieldDemo
import co.tanay.electricpop.demo.components.PopTitleBarDemo

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
    CatalogEntry("PopDropdown", "Foundation") { PopDropdownDemo() },

    // Wave 3: Layout Foundation
    CatalogEntry("PopSectionHeader", "Foundation") { PopSectionHeaderDemo() },
    CatalogEntry("PopTitleBar", "Foundation") { PopTitleBarDemo() },
    CatalogEntry("PopDisplayText", "Foundation") { PopDisplayTextDemo() },
    CatalogEntry("PopIconListItem", "Foundation") { PopIconListItemDemo() },
    CatalogEntry("PopStepList", "Foundation") { PopStepListDemo() },
    CatalogEntry("PopTable", "Foundation") { PopTableDemo() },
    CatalogEntry("PopCodeBlock", "Foundation") { PopCodeBlockDemo() },
    CatalogEntry("PopBottomBar", "Foundation") { PopBottomBarDemo() },

    // Wave 4: Composites
    CatalogEntry("PopDataRow", "Composite") { PopDataRowDemo() },
    CatalogEntry("PopBannerCard", "Composite") { PopBannerCardDemo() },
    CatalogEntry("PopImageBannerCard", "Composite") { PopImageBannerCardDemo() },
    CatalogEntry("PopDashboardCard", "Composite") { PopDashboardCardDemo() },
    CatalogEntry("PopCarouselCard", "Composite") { PopCarouselCardDemo() },
    CatalogEntry("PopActionCard", "Composite") { PopActionCardDemo() },

    // Wave 5: Charts
    CatalogEntry("PopChart", "Chart") { PopChartDemo() },
)

@Composable
fun CatalogScreen(
    onSelect: (CatalogEntry) -> Unit,
    listState: LazyListState = rememberLazyListState(),
) {
    LazyColumn(
        state = listState,
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
            CatalogRow(entry = entry, onSelect = onSelect)
        }
    }
}

@Composable
private fun CatalogRow(entry: CatalogEntry, onSelect: (CatalogEntry) -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = tween(durationMillis = 200),
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(),
            ) { onSelect(entry) },
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
