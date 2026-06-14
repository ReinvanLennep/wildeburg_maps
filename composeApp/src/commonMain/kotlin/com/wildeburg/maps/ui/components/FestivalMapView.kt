package com.wildeburg.maps.ui.components

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import com.wildeburg.maps.data.*
import com.wildeburg.maps.domain.*
import org.jetbrains.compose.resources.painterResource
import androidx.compose.foundation.Image
import wildeburgmaps.composeapp.generated.resources.Res
import wildeburgmaps.composeapp.generated.resources.festival_map

@Composable
fun FestivalMapView(
    calibration: CalibrationData?,
    location: LocationData?,
    pois: List<POI>,
    onMapTap: ((Float, Float) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var containerSize by remember { mutableStateOf(IntSize.Zero) }

    val transformState = rememberTransformableState { zoom, pan, _ ->
        scale = (scale * zoom).coerceIn(0.5f, 6f)
        offset += pan
    }

    // Displayed image size (contain mode)
    val displayedW: Float
    val displayedH: Float
    val imgOffX: Float
    val imgOffY: Float
    if (containerSize.width > 0 && containerSize.height > 0) {
        val cW = containerSize.width.toFloat()
        val cH = containerSize.height.toFloat()
        val imgAspect = MAP_ASPECT_RATIO
        val contAspect = cW / cH
        if (imgAspect > contAspect) {
            displayedW = cW; displayedH = cW / imgAspect
        } else {
            displayedH = cH; displayedW = cH * imgAspect
        }
        imgOffX = (cW - displayedW) / 2f
        imgOffY = (cH - displayedH) / 2f
    } else {
        displayedW = 0f; displayedH = 0f; imgOffX = 0f; imgOffY = 0f
    }

    // GPS → screen pixel position
    val transform = remember(calibration) {
        calibration?.let { buildTransform(it.points) }
    }

    val userPixel: Offset? = remember(location, transform, displayedW) {
        if (location == null || transform == null || displayedW == 0f) null
        else {
            val rel = gpsToImagePos(GpsCoords(location.lat, location.lon), transform)
            Offset(imgOffX + rel.x * displayedW, imgOffY + rel.y * displayedH)
        }
    }

    val accuracyPx: Float? = remember(location, transform, displayedW) {
        if (location?.accuracy == null || transform == null || displayedW == 0f) null
        else accuracyToImageRadius(location.accuracy, transform,
            GpsCoords(location.lat, location.lon)) * displayedW
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .onGloballyPositioned { containerSize = it.size }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(
                    scaleX = scale, scaleY = scale,
                    translationX = offset.x, translationY = offset.y
                )
                .transformable(state = transformState)
                .pointerInput(onMapTap, displayedW, displayedH) {
                    detectTapGestures(
                        onDoubleTap = {
                            val target = if (scale < 2f) 2.5f else 1f
                            scale = target; offset = Offset.Zero
                        },
                        onTap = { tap ->
                            if (onMapTap != null && displayedW > 0f) {
                                val relX = (tap.x - imgOffX) / displayedW
                                val relY = (tap.y - imgOffY) / displayedH
                                if (relX in 0f..1f && relY in 0f..1f) onMapTap(relX, relY)
                            }
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(Res.drawable.festival_map),
                contentDescription = "Festival Map",
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
            )

            if (displayedW > 0f) {
                // POI markers
                pois.forEach { poi ->
                    val px = imgOffX + poi.imagePos.x * displayedW
                    val py = imgOffY + poi.imagePos.y * displayedH
                    POIMarker(poi = poi, x = px, y = py)
                }

                // User location dot
                userPixel?.let { pos ->
                    LocationDot(
                        x = pos.x, y = pos.y,
                        accuracyRadius = accuracyPx,
                        heading = location?.heading
                    )
                }
            }
        }
    }
}
