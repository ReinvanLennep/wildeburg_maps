package com.wildeburg.maps

import androidx.compose.ui.window.ComposeUIViewController

fun MainViewController() = ComposeUIViewController(
    configure = { enforceStrictPlistSanityCheck = false }
) {
    println(">>> COMPOSE: starting App()")
    App()
    println(">>> COMPOSE: App() returned (no crash)")
}
