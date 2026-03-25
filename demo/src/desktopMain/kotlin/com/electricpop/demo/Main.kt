package com.electricpop.demo

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Electric Pop Demo"
    ) {
        App()
    }
}
