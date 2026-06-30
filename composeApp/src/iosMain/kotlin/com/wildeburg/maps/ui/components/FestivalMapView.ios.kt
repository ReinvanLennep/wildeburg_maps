package com.wildeburg.maps.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import com.wildeburg.maps.data.FESTIVAL_LAT
import com.wildeburg.maps.data.FESTIVAL_LON
import com.wildeburg.maps.data.LocationData
import com.wildeburg.maps.data.POI
import com.wildeburg.maps.data.poiEmoji
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreLocation.CLLocationCoordinate2DMake
import platform.MapKit.MKCoordinateRegionMakeWithDistance
import platform.MapKit.MKMapView
import platform.MapKit.MKPointAnnotation

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun FestivalMapView(
    pois: List<POI>,
    location: LocationData?,
    modifier: Modifier
) {
    val annotations    = remember { mutableListOf<MKPointAnnotation>() }
    // Holds the last list reference we rendered — using an array so we can
    // mutate the slot without triggering recomposition (unlike mutableStateOf).
    val lastPoisSlot   = remember { arrayOfNulls<List<POI>>(1) }

    UIKitView(
        factory = {
            MKMapView().apply {
                showsUserLocation = true
                val center = CLLocationCoordinate2DMake(FESTIVAL_LAT, FESTIVAL_LON)
                setRegion(
                    MKCoordinateRegionMakeWithDistance(center, 1200.0, 1200.0),
                    animated = false
                )
            }
        },
        update = { map ->
            // Guard: skip annotation sync unless the list reference changed.
            // Uses === (identity) because pois is always either the global POIS
            // object or Kotlin's emptyList() singleton — both stable references.
            // This prevents ~1 Hz annotation teardown during GPS recompositions,
            // which was interrupting pan/zoom gesture recognizers.
            if (pois !== lastPoisSlot[0]) {
                lastPoisSlot[0] = pois
                annotations.forEach { map.removeAnnotation(it) }
                annotations.clear()
                pois.forEach { poi ->
                    val ann = MKPointAnnotation()
                    ann.setCoordinate(CLLocationCoordinate2DMake(poi.gps.lat, poi.gps.lon))
                    ann.setTitle("${poiEmoji(poi.category)} ${poi.name}")
                    poi.description?.let { ann.setSubtitle(it) }
                    map.addAnnotation(ann)
                    annotations += ann
                }
            }
        },
        modifier = modifier
    )
}
