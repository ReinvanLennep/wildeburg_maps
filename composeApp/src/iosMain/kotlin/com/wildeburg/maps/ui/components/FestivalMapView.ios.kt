package com.wildeburg.maps.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitInteropInteractionMode
import androidx.compose.ui.viewinterop.UIKitInteropProperties
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

@OptIn(ExperimentalForeignApi::class, ExperimentalComposeUiApi::class)
@Composable
actual fun FestivalMapView(
    pois: List<POI>,
    location: LocationData?,
    modifier: Modifier
) {
    val annotations  = remember { mutableListOf<MKPointAnnotation>() }
    val lastPoisSlot = remember { arrayOfNulls<List<POI>>(1) }

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
            // Only sync annotations when the list reference actually changes.
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
        modifier = modifier,
        // Compose's default "Cooperative" mode delays every touch (~150ms) while it
        // decides whether Compose or the native view should own the gesture. NonCooperative
        // hands touches straight to MKMapView so pan/pinch/rotate respond immediately.
        properties = UIKitInteropProperties(
            interactionMode = UIKitInteropInteractionMode.NonCooperative
        )
    )
}
