package com.wildeburg.maps.data

import androidx.compose.ui.graphics.Color

val POIS = listOf(
    // ── Stages ───────────────────────────────────────────────────────────────
    POI("wildlive", "Wildlive", POICategory.STAGE,
        GpsCoords(52.680115, 5.880332), "Main stage — live music"),
    POI("radio-koperen-hond", "Radio de Koperen Hond", POICategory.STAGE,
        GpsCoords(52.681806, 5.880589), "Stage & radio broadcast"),
    POI("bud-kas", "Bud Kas", POICategory.STAGE,
        GpsCoords(52.682528, 5.880851), "Bar & stage"),
    POI("lodge", "LODGE", POICategory.STAGE,
        GpsCoords(52.682968, 5.881256), "Stage"),
    POI("strand", "Strand", POICategory.STAGE,
        GpsCoords(52.680824, 5.881047), "Beach on the lake"),
    POI("de-baan", "De Baan", POICategory.STAGE,
        GpsCoords(52.680678, 5.882156), "Activity track"),
    POI("bamboe-bos", "Bamboe Bos", POICategory.STAGE,
        GpsCoords(52.682438, 5.881688), "Bamboo forest"),
    POI("helling", "Helling", POICategory.STAGE,
        GpsCoords(52.681479, 5.883310), "The slope"),
    POI("duin-pan", "Duin/Pan", POICategory.STAGE,
        GpsCoords(52.682970, 5.882118), "Dune & pan area"),
    POI("achter-tuin", "Achter Tuin", POICategory.STAGE,
        GpsCoords(52.682208, 5.885464), "Back line area"),
    POI("eiland", "Eiland", POICategory.STAGE,
        GpsCoords(52.683679, 5.883569)),

    // ── Areas ────────────────────────────────────────────────────────────────
    POI("de-spot", "De Spot", POICategory.AREA,
        GpsCoords(52.679827, 5.878452), "Electronic & DJ stage"),
    POI("vuur-toren-strand", "Vuur Toren Strand", POICategory.AREA,
        GpsCoords(52.684752, 5.884237), "Fire tower beach"),
    POI("bamboe-bios", "Bamboe Bios", POICategory.AREA,
        GpsCoords(52.683301, 5.884510), "Outdoor cinema in the bamboo"),

    // ── Services ─────────────────────────────────────────────────────────────
    POI("ehbo", "EHBO", POICategory.SERVICE,
        GpsCoords(52.679597, 5.879487), "First aid post"),
    POI("camping-winkel", "Camping Winkel", POICategory.SERVICE,
        GpsCoords(52.679685, 5.877996), "Camping store — gas, supplies"),

    // ── Camping ──────────────────────────────────────────────────────────────
    POI("camping-1", "Camping 1", POICategory.CAMPING,
        GpsCoords(52.680517, 5.878141), "Camping zone 1"),
    POI("camping-2", "Camping 2", POICategory.CAMPING,
        GpsCoords(52.683609, 5.878030), "Camping zone 2"),
    POI("camping-3", "Camping 3", POICategory.CAMPING,
        GpsCoords(52.685295, 5.880089), "Camping zone 3"),
    POI("camping-3-5", "Camping 3.5", POICategory.CAMPING,
        GpsCoords(52.685800, 5.883023), "Camping zone 3.5"),
    POI("camping-4", "Camping 4", POICategory.CAMPING,
        GpsCoords(52.683310, 5.885849), "Camping zone 4"),

    // ── Easter Eggs ──────────────────────────────────────────────────────────
    POI("shrub-1", "Shrub 1", POICategory.EASTER_EGG,
        GpsCoords(52.681756, 5.881749)),
    POI("the-shell", "The Shell", POICategory.EASTER_EGG,
        GpsCoords(52.681176, 5.880945)),
    POI("the-lost-condom", "The Lost Condom", POICategory.EASTER_EGG,
        GpsCoords(52.681418, 5.880753)),
    POI("home-base", "Home Base", POICategory.EASTER_EGG,
        GpsCoords(52.683410, 5.885377)),
    POI("showers", "Showers", POICategory.EASTER_EGG,
        GpsCoords(52.685214, 5.884922)),
    POI("chinese-wraps", "Chinese Wraps", POICategory.EASTER_EGG,
        GpsCoords(52.679532, 5.879131)),
    POI("erwins-lighter", "Erwin's Lighter", POICategory.EASTER_EGG,
        GpsCoords(52.681106, 5.882725)),
    POI("parking-for-legends", "Parking for Legends", POICategory.EASTER_EGG,
        GpsCoords(52.673824, 5.882195)),
    POI("drugs-in-the-bread", "Drugs in the Bread", POICategory.EASTER_EGG,
        GpsCoords(52.687407, 5.868129)),
)

fun poiColor(category: POICategory) = when (category) {
    POICategory.STAGE      -> Color(0xFFFF6B35)
    POICategory.AREA       -> Color(0xFFAB47BC)
    POICategory.SERVICE    -> Color(0xFF78909C)
    POICategory.CAMPING    -> Color(0xFF4CAF50)
    POICategory.EASTER_EGG -> Color(0xFFFFD54F)
}

fun poiEmoji(category: POICategory) = when (category) {
    POICategory.STAGE      -> "🎵"
    POICategory.AREA       -> "🎪"
    POICategory.SERVICE    -> "ℹ️"
    POICategory.CAMPING    -> "⛺"
    POICategory.EASTER_EGG -> "🥚"
}

fun categoryDisplayName(category: POICategory) = when (category) {
    POICategory.STAGE      -> "Stages"
    POICategory.AREA       -> "Areas"
    POICategory.SERVICE    -> "Services"
    POICategory.CAMPING    -> "Camping"
    POICategory.EASTER_EGG -> "Easter Eggs"
}
