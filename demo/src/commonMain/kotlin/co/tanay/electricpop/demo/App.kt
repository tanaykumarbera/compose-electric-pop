package co.tanay.electricpop.demo

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import co.tanay.electricpop.foundation.PopIcon
import co.tanay.electricpop.foundation.PopIcons
import co.tanay.electricpop.foundation.PopIconSize
import co.tanay.electricpop.theme.ElectricPopTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    var isDark by remember { mutableStateOf(false) }
    var selectedEntry by remember { mutableStateOf<CatalogEntry?>(null) }

    ElectricPopTheme(darkTheme = isDark) {
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
                        Text(
                            if (isDark) "Dark" else "Light",
                            style = MaterialTheme.typography.labelMedium,
                        )
                        Switch(checked = isDark, onCheckedChange = { isDark = it })
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
                    CatalogScreen(onSelect = { selectedEntry = it })
                }
            }
        }
    }

    // Handle system back
    BackHandler(enabled = selectedEntry != null) {
        selectedEntry = null
    }
}
