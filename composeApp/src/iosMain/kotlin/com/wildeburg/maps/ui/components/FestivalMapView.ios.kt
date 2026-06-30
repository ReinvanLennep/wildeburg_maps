package com.wildeburg.maps.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitInteractionMode
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
    val annotations      = remember { mutableListOf<MKPointAnnotation>() }
    val lastPoisSlot     = remember { arrayOfNulls<List<POI>>(1) }
    val gestureFixDone   = remember { booleanArrayOf(false) }

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
            // One-time: walk up to the Compose root and tell every ancestor gesture
            // recognizer not to delay or cancel touches. Without this, Compose holds
            // the touch for ~300 ms waiting to claim it, so the map feels sluggish.
            if (!gestureFixDone[0]) {
                gestureFixDone[0] = true
                var v: UIView? = map.superview
                while (v != null) {
                    v.gestureRecognizers?.forEach { gr ->
                        (gr as? UIGestureRecognizer)?.apply {
                            cancelsTouchesInView = false
                            delaysTouchesBegan  = false
                        }
                    }
                    v = v.superview
                }
            }

            // Guard: only sync annotations when the list reference changes.
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
        interactionMode = UIKitInteractionMode.NonCooperative
    )
}
