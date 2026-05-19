package com.sample.turnapp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.sample.turnapp.di.initKoin

fun main() = application {
    initKoin()
    Window(
        onCloseRequest = ::exitApplication,
        title = "Turn App",
    ) {
        App()
    }
}