package co.tanay.electricpop.demo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import co.tanay.electricpop.foundation.PopIcon
import co.tanay.electricpop.foundation.PopIconSize
import co.tanay.electricpop.foundation.PopIcons
import co.tanay.electricpop.foundation.PopSwitch
import co.tanay.electricpop.foundation.PopSwitchColor
import co.tanay.electricpop.theme.ElectricPopTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    var isDark by remember { mutableStateOf(false) }
    var selectedEntry by remember { mutableStateOf<CatalogEntry?>(null) }
    val catalogListState = rememberLazyListState()

    ElectricPopTheme(darkTheme = isDark) {
        val spacing = ElectricPopTheme.spacing
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = selectedEntry?.name ?: "Electric Pop",
                            style = MaterialTheme.typography.titleMedium,
                        )
                    },
                    navigationIcon = {
                        if (selectedEntry != null) {
                            IconButton(onClick = { selectedEntry = null }) {
                                PopIcon(
                                    imageVector = PopIcons.ArrowBack,
                                    contentDescription = "Back",
                                    size = PopIconSize.Medium,
                                )
                            }
                        }
                    },
                    actions = {
                        PopSwitch(
                            checked = isDark,
                            onCheckedChange = { isDark = it },
                            label = if (isDark) "Dark" else "Light",
                            color = PopSwitchColor.Primary,
                            modifier = Modifier.padding(end = spacing.md),
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                    ),
                )
            },
        ) { padding ->
            Box(Modifier.padding(padding)) {
                val entry = selectedEntry
                if (entry != null) {
                    entry.content()
                } else {
                    CatalogScreen(
                        listState = catalogListState,
                        onSelect = { selectedEntry = it },
                    )
                }
            }
        }
    }

    // Handle system back
    BackHandler(enabled = selectedEntry != null) {
        selectedEntry = null
    }
}
