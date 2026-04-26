package co.tanay.electricpop.demo

import androidx.compose.runtime.Composable

@Composable
actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) {
    // No system back on desktop — top bar arrow handles navigation
}
