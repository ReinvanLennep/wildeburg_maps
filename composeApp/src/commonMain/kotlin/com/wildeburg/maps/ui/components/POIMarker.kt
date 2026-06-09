package com.wildeburg.maps.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wildeburg.maps.data.POI
import com.wildeburg.maps.data.poiColor
import com.wildeburg.maps.data.poiEmoji

private val BG = Color(0xE6141414)

@Composable
fun POIMarker(poi: POI, x: Float, y: Float) {
    var showLabel by remember { mutableStateOf(false) }
    val color = poiColor(poi.category)
    val markerSize = 28.dp

    Box(
        modifier = Modifier
            .layout { measurable, constraints ->
                val p = measurable.measure(constraints)
                layout(constraints.maxWidth, constraints.maxHeight) {
                    // Place the bottom-center of the marker at (x, y)
                    p.place(
                        x = (x - p.width / 2).toInt(),
                        y = (y - p.height).toInt()
                    )
                }
            }
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Label bubble
            if (showLabel) {
                Box(
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                        .background(BG, RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 5.dp)
                        .widthIn(min = 80.dp, max = 160.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(poi.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        poi.description?.let {
                            Text(it, color = Color(0xFFAAAAAA), fontSize = 10.sp)
                        }
                    }
                }
            }

            // Circle marker
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(markerSize)
                    .clip(CircleShape)
                    .background(color)
                    .clickable { showLabel = !showLabel }
            ) {
                Text(poiEmoji(poi.category), fontSize = 13.sp)
            }

            // Pin tail
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(6.dp)
                    .background(color)
            )
        }
    }
}
