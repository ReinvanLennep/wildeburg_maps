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
import platform.UIKit.UIWindow

@OptIn(ExperimentalForeignApi::class)
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
            // Re-apply on every update: Compose recreates its gesture recognizers
            // on each recomposition (~1 Hz from GPS), so a one-shot fix only patches
            // the original set and misses every replacement. Walking on each frame
            // costs ~microseconds (5 views × 3 recognizers max).
            // Stop before UIWindow — its system gate recognizers (home swipe, back
            // gesture) are forbidden from modification by iOS.
            if (map.window != null) {
                var v: UIView? = map
                while (v != null && v !is UIWindow) {
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
