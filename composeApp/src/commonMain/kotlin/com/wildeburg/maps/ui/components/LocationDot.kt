package com.wildeburg.maps.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import kotlin.math.*

private val DOT_COLOR   = Color(0xFF2979FF)
private val DOT_BORDER  = Color.White
private val ACC_FILL    = Color(0x1F2979FF)
private val ACC_STROKE  = Color(0x592979FF)
private val CONE_COLOR  = Color(0x8C2979FF)

@Composable
fun LocationDot(
    x: Float,
    y: Float,
    accuracyRadius: Float? = null,
    heading: Float? = null
) {
    val dotR = 8f
    val pulse = rememberInfiniteTransition(label = "pulse")
    val pulseScale by pulse.animateFloat(
        initialValue = 1f, targetValue = 1.6f, label = "pulseScale",
        animationSpec = infiniteRepeatable(
            animation = tween(900),
            repeatMode = RepeatMode.Reverse
        )
    )

    Canvas(
        modifier = Modifier.layout { measurable, constraints ->
            val p = measurable.measure(constraints)
            layout(constraints.maxWidth, constraints.maxHeight) { p.place(0, 0) }
        }
    ) {
        val center = Offset(x, y)

        // Accuracy circle
        if (accuracyRadius != null && accuracyRadius > dotR) {
            drawCircle(color = ACC_FILL, radius = accuracyRadius, center = center)
            drawCircle(color = ACC_STROKE, radius = accuracyRadius, center = center, style =
                androidx.compose.ui.graphics.drawscope.Stroke(width = 1.5f))
        }

        // Heading cone
        if (heading != null) {
            val rad = heading * PI.toFloat() / 180f - PI.toFloat() / 2f
            val coneLen = 28f
            val coneHalf = PI.toFloat() / 12f
            val path = androidx.compose.ui.graphics.Path().apply {
                moveTo(center.x, center.y)
                lineTo(
                    center.x + coneLen * cos(rad - coneHalf),
                    center.y + coneLen * sin(rad - coneHalf)
                )
                lineTo(
                    center.x + coneLen * cos(rad + coneHalf),
                    center.y + coneLen * sin(rad + coneHalf)
                )
                close()
            }
            drawPath(path, CONE_COLOR)
        }

        // Pulsing ring
        drawCircle(color = DOT_COLOR.copy(alpha = 0.25f), radius = dotR * pulseScale, center = center)

        // Core dot
        drawCircle(color = DOT_COLOR, radius = dotR, center = center)
        drawCircle(color = DOT_BORDER, radius = dotR, center = center, style =
            androidx.compose.ui.graphics.drawscope.Stroke(width = 2.5f))
    }
}
