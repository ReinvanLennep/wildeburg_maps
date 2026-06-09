package com.wildeburg.maps.data

import kotlinx.serialization.Serializable

@Serializable
data class GpsCoords(val lat: Double, val lon: Double)

@Serializable
data class ImageRelPos(val x: Float, val y: Float)

@Serializable
data class ControlPoint(
    val id: String,
    val label: String,
    val gps: GpsCoords,
    val imagePos: ImageRelPos
)

@Serializable
data class CalibrationData(
    val points: List<ControlPoint>,
    val createdAt: Long = 0L
)

enum class POICategory {
    STAGE, CAMPING, FOOD, MEDICAL, WATER, NATURE, ACTIVITY, FACILITY
}

data class POI(
    val id: String,
    val name: String,
    val category: POICategory,
    val imagePos: ImageRelPos,
    val gps: GpsCoords,
    val description: String? = null
)

data class LocationData(
    val lat: Double,
    val lon: Double,
    val accuracy: Float,
    val heading: Float? = null
)
