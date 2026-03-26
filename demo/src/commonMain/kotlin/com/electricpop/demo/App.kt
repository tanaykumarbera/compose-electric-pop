package com.electricpop.demo

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.electricpop.theme.ElectricPopTheme

@Composable
fun App() {
    var isDark by remember { mutableStateOf(false) }
    var selectedEntry by remember { mutableStateOf<CatalogEntry?>(null) }

    ElectricPopTheme(darkTheme = isDark) {
        Scaffold(
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (selectedEntry != null) {
                        TextButton(onClick = { selectedEntry = null }) {
                            Text("< Back")
                        }
                        Spacer(Modifier.width(8.dp))
                        Text(
                            selectedEntry!!.name,
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                    Spacer(Modifier.weight(1f))
                    Text(
                        if (isDark) "Dark" else "Light",
                        style = MaterialTheme.typography.labelMedium,
                    )
                    Spacer(Modifier.width(8.dp))
                    Switch(checked = isDark, onCheckedChange = { isDark = it })
                }
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
}
