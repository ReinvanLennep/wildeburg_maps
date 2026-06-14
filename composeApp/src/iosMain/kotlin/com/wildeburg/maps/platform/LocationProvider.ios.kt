package com.wildeburg.maps.platform

import com.wildeburg.maps.data.LocationData
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.CoreLocation.*
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class)
actual class LocationProvider actual constructor() {
    private val manager = CLLocationManager()
    private var onUpdate: ((LocationData) -> Unit)? = null

    private val delegate = object : NSObject(), CLLocationManagerDelegateProtocol {
        override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>) {
            val loc = didUpdateLocations.lastOrNull() as? CLLocation ?: return
            onUpdate?.invoke(LocationData(
                lat      = loc.coordinate.useContents { latitude },
                lon      = loc.coordinate.useContents { longitude },
                accuracy = loc.horizontalAccuracy.toFloat(),
                heading  = manager.heading?.magneticHeading?.toFloat()
            ))
        }
        override fun locationManager(manager: CLLocationManager, didFailWithError: platform.Foundation.NSError) {}
    }

    actual fun startUpdates(onUpdate: (LocationData) -> Unit, onError: (String) -> Unit) {
        this.onUpdate = onUpdate
        manager.delegate = delegate
        manager.desiredAccuracy = kCLLocationAccuracyBest
        manager.requestWhenInUseAuthorization()
        manager.startUpdatingLocation()
        manager.startUpdatingHeading()
    }

    actual fun stopUpdates() {
        manager.stopUpdatingLocation()
        manager.stopUpdatingHeading()
        onUpdate = null
    }
}
