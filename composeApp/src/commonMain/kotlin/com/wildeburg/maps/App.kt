package com.wildeburg.maps

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wildeburg.maps.data.CalibrationData
import com.wildeburg.maps.domain.CalibrationStorage
import com.wildeburg.maps.ui.*

@Composable
fun App() {
    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
            Text("Wildeburg Maps loading...", color = Color.White)
        }
    }
}
