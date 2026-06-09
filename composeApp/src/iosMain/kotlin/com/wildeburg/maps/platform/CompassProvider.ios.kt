package com.wildeburg.maps.platform

import platform.CoreLocation.*
import platform.darwin.NSObject

actual class CompassProvider actual constructor() {
    private val manager = CLLocationManager()
    private var onHeading: ((Float) -> Unit)? = null

    private val delegate = object : NSObject(), CLLocationManagerDelegateProtocol {
        override fun locationManager(manager: CLLocationManager, didUpdateHeading: CLHeading) {
            onHeading?.invoke(didUpdateHeading.magneticHeading.toFloat())
        }
    }

    actual fun start(onHeading: (Float) -> Unit) {
        this.onHeading = onHeading
        manager.delegate = delegate
        manager.startUpdatingHeading()
    }

    actual fun stop() {
        manager.stopUpdatingHeading()
        onHeading = null
    }
}
