package com.sample.turnapp

import androidx.compose.runtime.Composable
import com.sample.turnapp.core.ui.theme.ClyTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext

@Composable
@Preview
fun App() {
    ClyTheme {
        KoinContext {
            AppScaffold()
        }
    }
}

@Composable
fun AppScaffold() {

}