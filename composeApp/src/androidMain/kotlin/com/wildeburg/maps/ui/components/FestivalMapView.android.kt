package com.wildeburg.maps.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.wildeburg.maps.data.FocusRequest
import com.wildeburg.maps.data.LocationData
import com.wildeburg.maps.data.POI

@Composable
actual fun FestivalMapView(
    pois: List<POI>,
    location: LocationData?,
    focusRequest: FocusRequest?,
    modifier: Modifier
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Text("Map view available on iOS", color = Color(0xFF888888), fontSize = 16.sp)
    }
}
