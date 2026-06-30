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
import platform.UIKit.UIGestureRecognizer
import platform.UIKit.UIView

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun FestivalMapView(
    pois: List<POI>,
    location: LocationData?,
    modifier: Modifier
) {
    val annotations    = remember { mutableListOf<MKPointAnnotation>() }
    val lastPoisSlot   = remember { arrayOfNulls<List<POI>>(1) }
    val gestureFixDone = remember { booleanArrayOf(false) }

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
            // Wait until the view is attached to a UIWindow before walking the
            // gesture-recognizer chain. The first update() can fire before the
            // view is in the window hierarchy, in which case the walk is a no-op
            // and we must retry on the next update call.
            if (!gestureFixDone[0] && map.window != null) {
                gestureFixDone[0] = true
                // Walk every ancestor (including the map itself) and disable the
                // Compose root's delaysTouchesBegan / cancelsTouchesInView so the
                // map responds to pan/pinch immediately instead of after ~300 ms.
                var v: UIView? = map
                while (v != null) {
                    v.gestureRecognizers?.forEach { gr ->
                        (gr as? UIGestureRecognizer)?.apply {
                            cancelsTouchesInView = false
                            delaysTouchesBegan   = false
                        }
                    }
                    v = v.superview
                }
            }

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
        modifier = modifier
    )
}
