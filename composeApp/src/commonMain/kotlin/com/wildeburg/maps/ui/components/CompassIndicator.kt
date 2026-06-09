package com.wildeburg.maps.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CompassIndicator(heading: Float?, modifier: Modifier = Modifier) {
    if (heading == null) return
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(Color(0xBF141414))
    ) {
        // Needle
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.rotate(-heading)
        ) {
            Box(modifier = Modifier.width(4.dp).height(15.dp).background(Color(0xFFF44336)))
            Box(modifier = Modifier.width(4.dp).height(15.dp).background(Color(0xFFBDBDBD)))
        }
        // N label
        Text(
            "N", color = Color(0xFFF44336),
            fontSize = 8.sp, fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.align(Alignment.TopCenter).padding(top = 2.dp)
        )
    }
}
