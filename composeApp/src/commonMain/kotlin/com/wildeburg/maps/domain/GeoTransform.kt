package com.wildeburg.maps.domain

import com.wildeburg.maps.data.GpsCoords
import kotlin.math.*

fun haversineMeters(a: GpsCoords, b: GpsCoords): Double {
    val R = 6_371_000.0
    val dLat = (b.lat - a.lat) * PI / 180
    val dLon = (b.lon - a.lon) * PI / 180
    val h = sin(dLat / 2).pow(2) +
            cos(a.lat * PI / 180) * cos(b.lat * PI / 180) * sin(dLon / 2).pow(2)
    return 2 * R * asin(sqrt(h))
}
