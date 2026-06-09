package com.wildeburg.maps.platform

import com.wildeburg.maps.data.LocationData

expect class LocationProvider() {
    fun startUpdates(onUpdate: (LocationData) -> Unit, onError: (String) -> Unit)
    fun stopUpdates()
}
