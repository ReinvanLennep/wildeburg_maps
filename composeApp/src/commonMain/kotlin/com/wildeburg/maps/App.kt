package com.wildeburg.maps

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wildeburg.maps.ui.*

private enum class Tab { MAP, LEGEND }

@Composable
fun App() {
    var selectedTab by remember { mutableStateOf(Tab.MAP) }

    MaterialTheme(
        colorScheme = darkColorScheme(
            primary    = Color(0xFFFFB300),
            surface    = Color(0xFF141414),
            background = Color(0xFF111111)
        )
    ) {
        Scaffold(
            containerColor = Color(0xFF111111),
            bottomBar = {
                NavigationBar(containerColor = Color(0xFF141414), tonalElevation = 0.dp) {
                    NavigationBarItem(
                        selected = selectedTab == Tab.MAP,
                        onClick  = { selectedTab = Tab.MAP },
                        icon     = { Text("🗺", fontSize = 20.sp) },
                        label    = { Text("Map") }
                    )
                    NavigationBarItem(
                        selected = selectedTab == Tab.LEGEND,
                        onClick  = { selectedTab = Tab.LEGEND },
                        icon     = { Text("📋", fontSize = 20.sp) },
                        label    = { Text("Legend") }
                    )
                }
            }
        ) { padding ->
            Box(modifier = Modifier.padding(padding).fillMaxSize()) {
                when (selectedTab) {
                    Tab.MAP    -> MapScreen()
                    Tab.LEGEND -> LegendScreen()
                }
            }
        }
    }
}
