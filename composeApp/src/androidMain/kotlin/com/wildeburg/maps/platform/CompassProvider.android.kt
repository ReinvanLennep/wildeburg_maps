package com.wildeburg.maps.platform

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.atan2

actual class CompassProvider actual constructor() {
    private var sensorManager: SensorManager? = null
    private var listener: SensorEventListener? = null

    actual fun start(onHeading: (Float) -> Unit) {
        val ctx = AppContext.get()
        sensorManager = ctx.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val mag = sensorManager?.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) ?: return

        listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val x = event.values[0]
                val y = event.values[1]
                var angle = Math.toDegrees(atan2(y.toDouble(), x.toDouble())).toFloat()
                if (angle < 0) angle += 360f
                onHeading(angle)
            }
            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
        }
        sensorManager?.registerListener(listener, mag, SensorManager.SENSOR_DELAY_UI)
    }

    actual fun stop() {
        listener?.let { sensorManager?.unregisterListener(it) }
        listener = null
    }
}
