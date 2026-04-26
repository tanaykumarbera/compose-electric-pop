package co.tanay.electricpop.demo

import androidx.compose.runtime.Composable

@Composable
actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) {
    // iOS uses swipe-back gesture at the platform level
}
