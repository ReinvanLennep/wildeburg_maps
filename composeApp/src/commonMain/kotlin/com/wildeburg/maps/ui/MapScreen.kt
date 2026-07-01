package com.wildeburg.maps.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wildeburg.maps.data.*
import com.wildeburg.maps.domain.*
import com.wildeburg.maps.platform.CompassProvider
import com.wildeburg.maps.platform.LocationProvider
import com.wildeburg.maps.ui.components.*

private val CHIP = Color(0xCC141414)

@Composable
fun MapScreen(focusRequest: FocusRequest? = null) {
    val locationProvider = remember { LocationProvider() }
    val compassProvider  = remember { CompassProvider() }

    var location by remember { mutableStateOf<LocationData?>(null) }
    var heading  by remember { mutableStateOf<Float?>(null) }
    var showPOIs by remember { mutableStateOf(true) }

    // A Legend tap should always reveal the pin it's pointing at, even if the
    // user had previously hidden POIs with the toggle FAB.
    LaunchedEffect(focusRequest) {
        if (focusRequest != null) showPOIs = true
    }

    val nearest: Pair<POI, Int>? = remember(location) {
        if (location == null) return@remember null
        POIS.minByOrNull { haversineMeters(GpsCoords(location!!.lat, location!!.lon), it.gps) }
            ?.let { poi ->
                val dist = haversineMeters(GpsCoords(location!!.lat, location!!.lon), poi.gps)
                if (dist < 2000) poi to dist.toInt() else null
            }
    }

    DisposableEffect(Unit) {
        locationProvider.startUpdates(onUpdate = { location = it }, onError = {})
        compassProvider.start(onHeading = { heading = it })
        onDispose {
            locationProvider.stopUpdates()
            compassProvider.stop()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        FestivalMapView(
            pois         = if (showPOIs) POIS else emptyList(),
            location     = location?.copy(heading = heading),
            focusRequest = focusRequest,
            modifier     = Modifier.fillMaxSize()
        )

        // Top HUD
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .statusBarsPadding()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                GpsChip(location = location)
                Spacer(Modifier.weight(1f))
                CompassIndicator(heading = heading)
            }

            nearest?.let { (poi, dist) ->
                Surface(color = CHIP, shape = RoundedCornerShape(20.dp), tonalElevation = 0.dp) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(poiEmoji(poi.category), fontSize = 13.sp)
                        Text(poi.name, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        Text("${dist}m", color = Color(0xFFFFB300), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // POI toggle FAB
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .navigationBarsPadding()
                .padding(12.dp)
        ) {
            SmallFab(label = if (showPOIs) "📍" else "📌") { showPOIs = !showPOIs }
        }
    }
}

@Composable
private fun GpsChip(location: LocationData?) {
    val (color, label) = when {
        location != null -> Color(0xFF4CAF50) to "±${location.accuracy.toInt()}m"
        else             -> Color(0xFF9E9E9E) to "GPS…"
    }
    Surface(color = CHIP, shape = RoundedCornerShape(20.dp), tonalElevation = 0.dp) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Surface(color = color, shape = RoundedCornerShape(4.dp),
                modifier = Modifier.size(8.dp)) {}
            Text(label, color = Color(0xFFEEEEEE), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun SmallFab(label: String, onClick: () -> Unit) {
    Surface(
        color = Color(0xCC141414),
        shape = RoundedCornerShape(24.dp),
        tonalElevation = 4.dp,
        modifier = Modifier.size(48.dp)
    ) {
        TextButton(onClick = onClick, modifier = Modifier.fillMaxSize()) {
            Text(label, fontSize = 20.sp)
        }
    }
}
