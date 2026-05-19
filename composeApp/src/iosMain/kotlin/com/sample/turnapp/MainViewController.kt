package com.sample.turnapp

import androidx.compose.ui.window.ComposeUIViewController
import com.sample.turnapp.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = { initKoin() }
) { App() }