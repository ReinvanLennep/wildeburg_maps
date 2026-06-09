package com.wildeburg.maps.data

// Default calibration derived from pixel analysis of the annotated satellite
// screenshot. Accuracy ≈ 50–150 m. Use the Calibrate screen to improve.
val DEFAULT_CALIBRATION = CalibrationData(
    createdAt = 1L,
    points = listOf(
        ControlPoint("wildlive",  "Wildlive (main stage)",
            GpsCoords(52.6878, 5.8611), ImageRelPos(0.19f, 0.32f)),
        ControlPoint("camping-1", "Camping 1",
            GpsCoords(52.6902, 5.8714), ImageRelPos(0.47f, 0.18f)),
        ControlPoint("camping-4", "Camping 4",
            GpsCoords(52.6812, 5.8639), ImageRelPos(0.21f, 0.73f)),
        ControlPoint("camping-2", "Camping 2",
            GpsCoords(52.6867, 5.8777), ImageRelPos(0.72f, 0.37f)),
    )
)

// Natural pixel dimensions of the bundled festival map image
const val MAP_NATURAL_WIDTH  = 508f
const val MAP_NATURAL_HEIGHT = 870f
const val MAP_ASPECT_RATIO   = MAP_NATURAL_WIDTH / MAP_NATURAL_HEIGHT
