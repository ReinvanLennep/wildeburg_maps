package com.wildeburg.maps.data

import kotlinx.serialization.Serializable

@Serializable
data class GpsCoords(val lat: Double, val lon: Double)

enum class POICategory {
    STAGE, CAMPING, FOOD, MEDICAL, WATER, NATURE, ACTIVITY, FACILITY
}

data class POI(
    val id: String,
    val name: String,
    val category: POICategory,
    val gps: GpsCoords,
    val description: String? = null
)

data class LocationData(
    val lat: Double,
    val lon: Double,
    val accuracy: Float,
    val heading: Float? = null
)
