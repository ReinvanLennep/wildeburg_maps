package com.wildeburg.maps.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wildeburg.maps.data.*

private val BG = Color(0xFF111111)

@Composable
fun LegendScreen(onPoiClick: (POI) -> Unit = {}) {
    val grouped = POIS.groupBy { it.category }
    val order = POICategory.values().toList()

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(BG)
            .statusBarsPadding().navigationBarsPadding(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text("Legend", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.ExtraBold)
            Text("All locations on the Wildeburg terrain",
                color = Color(0xFF888888), fontSize = 13.sp)
        }
        items(order.size) { idx ->
            val cat = order[idx]
            val pois = grouped[cat] ?: return@items
            val color = poiColor(cat)
            val emoji = poiEmoji(cat)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1A1A1A), RoundedCornerShape(12.dp))
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(36.dp).clip(CircleShape).background(color)
                    ) {
                        Text(emoji, fontSize = 18.sp)
                    }
                    Text(
                        categoryDisplayName(cat),
                        color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp
                    )
                }
                pois.forEach { poi ->
                    Row(
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onPoiClick(poi) }
                            .padding(start = 4.dp, top = 4.dp, bottom = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier.size(8.dp).clip(CircleShape).background(color)
                                .padding(top = 4.dp)
                        )
                        Column {
                            Text(poi.name, color = Color(0xFFDDDDDD), fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                            poi.description?.let {
                                Text(it, color = Color(0xFF777777), fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
        item {
            Text(
                "Wildeburg Maps • Offline GPS\nTap any marker on the map for details.",
                color = Color(0xFF555555), fontSize = 12.sp, lineHeight = 18.sp,
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )
        }
    }
}
