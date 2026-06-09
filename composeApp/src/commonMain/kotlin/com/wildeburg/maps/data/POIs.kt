package com.wildeburg.maps.data

import androidx.compose.ui.graphics.Color

val POIS = listOf(
    // ── Stages ────────────────────────────────────────────────────────────────
    POI("wildlive", "Wildlive", POICategory.STAGE,
        ImageRelPos(0.19f, 0.32f), GpsCoords(52.6878, 5.8611), "Main stage — live music"),
    POI("de-spot", "De Spot", POICategory.STAGE,
        ImageRelPos(0.14f, 0.21f), GpsCoords(52.6893, 5.8608), "Electronic & DJ stage"),
    POI("radio-koperen-hond", "Radio de Koperen Hond", POICategory.STAGE,
        ImageRelPos(0.43f, 0.33f), GpsCoords(52.6877, 5.8635), "Stage & radio broadcast"),
    POI("bud-kas", "Bud Kas", POICategory.STAGE,
        ImageRelPos(0.50f, 0.42f), GpsCoords(52.6854, 5.8660), "Bar & stage"),
    POI("lod", "LOD", POICategory.STAGE,
        ImageRelPos(0.59f, 0.46f), GpsCoords(52.6847, 5.8675), "Stage"),
    POI("bamboe-bios", "Bamboe Bios", POICategory.ACTIVITY,
        ImageRelPos(0.31f, 0.67f), GpsCoords(52.6821, 5.8659), "Outdoor cinema in the bamboo"),

    // ── Areas ─────────────────────────────────────────────────────────────────
    POI("strand", "Strand", POICategory.WATER,
        ImageRelPos(0.12f, 0.40f), GpsCoords(52.6863, 5.8596), "Beach on the lake"),
    POI("de-baan", "De Baan", POICategory.ACTIVITY,
        ImageRelPos(0.09f, 0.46f), GpsCoords(52.6867, 5.8594), "Activity track"),
    POI("bamboe-bos", "Bamboe Bos", POICategory.NATURE,
        ImageRelPos(0.46f, 0.52f), GpsCoords(52.6845, 5.8671), "Bamboo forest"),
    POI("helling", "Helling", POICategory.NATURE,
        ImageRelPos(0.12f, 0.53f), GpsCoords(52.6847, 5.8606), "The slope"),
    POI("duin-pan", "Duin/Pan", POICategory.NATURE,
        ImageRelPos(0.36f, 0.57f), GpsCoords(52.6836, 5.8659), "Dune & pan area"),
    POI("achter-lijn", "Achter Lijn", POICategory.NATURE,
        ImageRelPos(0.11f, 0.63f), GpsCoords(52.6823, 5.8592), "Back line area"),
    POI("vuur-toren-strand", "Vuur Toren Strand", POICategory.WATER,
        ImageRelPos(0.75f, 0.66f), GpsCoords(52.6814, 5.8745), "Fire tower beach"),

    // ── Services ──────────────────────────────────────────────────────────────
    POI("ehbo", "EHBO", POICategory.MEDICAL,
        ImageRelPos(0.18f, 0.17f), GpsCoords(52.6917, 5.8617), "First aid post"),
    POI("camping-winkel", "Camping Winkel", POICategory.FACILITY,
        ImageRelPos(0.24f, 0.13f), GpsCoords(52.6922, 5.8625), "Camping store — gas, supplies"),
    POI("dorps-tafel", "Dorps Tafel", POICategory.FOOD,
        ImageRelPos(0.38f, 0.22f), GpsCoords(52.6904, 5.8651), "Village table — communal dining"),
    POI("hourtentien-1", "Hourtentien", POICategory.FACILITY,
        ImageRelPos(0.74f, 0.28f), GpsCoords(52.6895, 5.8800), "Showers & toilets"),
    POI("hourtentien-2", "Hourtentien", POICategory.FACILITY,
        ImageRelPos(0.78f, 0.43f), GpsCoords(52.6867, 5.8813), "Showers & toilets"),
    POI("hourtentien-3", "Hourtentien", POICategory.FACILITY,
        ImageRelPos(0.69f, 0.60f), GpsCoords(52.6833, 5.8784), "Showers & toilets"),

    // ── Camping ───────────────────────────────────────────────────────────────
    POI("camping-1", "Camping 1", POICategory.CAMPING,
        ImageRelPos(0.47f, 0.18f), GpsCoords(52.6902, 5.8714), "Camping zone 1"),
    POI("camping-2", "Camping 2", POICategory.CAMPING,
        ImageRelPos(0.72f, 0.37f), GpsCoords(52.6867, 5.8777), "Camping zone 2"),
    POI("camping-4", "Camping 4", POICategory.CAMPING,
        ImageRelPos(0.21f, 0.73f), GpsCoords(52.6812, 5.8639), "Camping zone 4"),
    POI("camping-5", "Camping 5", POICategory.CAMPING,
        ImageRelPos(0.37f, 0.78f), GpsCoords(52.6800, 5.8668), "Camping zone 5"),
)

fun poiColor(category: POICategory) = when (category) {
    POICategory.STAGE    -> Color(0xFFFF6B35)
    POICategory.CAMPING  -> Color(0xFF4CAF50)
    POICategory.FOOD     -> Color(0xFFFFB300)
    POICategory.MEDICAL  -> Color(0xFFF44336)
    POICategory.WATER    -> Color(0xFF29B6F6)
    POICategory.NATURE   -> Color(0xFF66BB6A)
    POICategory.ACTIVITY -> Color(0xFFAB47BC)
    POICategory.FACILITY -> Color(0xFF78909C)
}

fun poiEmoji(category: POICategory) = when (category) {
    POICategory.STAGE    -> "🎵"
    POICategory.CAMPING  -> "⛺"
    POICategory.FOOD     -> "🍺"
    POICategory.MEDICAL  -> "🏥"
    POICategory.WATER    -> "🏊"
    POICategory.NATURE   -> "🌲"
    POICategory.ACTIVITY -> "🎪"
    POICategory.FACILITY -> "🚿"
}
