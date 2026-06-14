package com.wildeburg.maps.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wildeburg.maps.data.LocationData
import com.wildeburg.maps.data.POI

@Composable
expect fun FestivalMapView(
    pois: List<POI>,
    location: LocationData?,
    modifier: Modifier = Modifier
)
