package com.wildeburg.maps.platform

import android.annotation.SuppressLint
import android.os.Looper
import com.google.android.gms.location.*
import com.wildeburg.maps.data.LocationData

actual class LocationProvider actual constructor() {
    private var client: FusedLocationProviderClient? = null
    private var callback: LocationCallback? = null

    @SuppressLint("MissingPermission")
    actual fun startUpdates(onUpdate: (LocationData) -> Unit, onError: (String) -> Unit) {
        val ctx = AppContext.get()
        client = LocationServices.getFusedLocationProviderClient(ctx)
        val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1_000L)
            .setMinUpdateDistanceMeters(1f)
            .build()
        callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val loc = result.lastLocation ?: return
                onUpdate(LocationData(
                    lat = loc.latitude,
                    lon = loc.longitude,
                    accuracy = loc.accuracy,
                    heading = if (loc.hasBearing()) loc.bearing else null
                ))
            }
            override fun onLocationAvailability(a: LocationAvailability) {
                if (!a.isLocationAvailable) onError("Location unavailable")
            }
        }
        client?.requestLocationUpdates(request, callback!!, Looper.getMainLooper())
    }

    actual fun stopUpdates() {
        callback?.let { client?.removeLocationUpdates(it) }
        callback = null
    }
}
