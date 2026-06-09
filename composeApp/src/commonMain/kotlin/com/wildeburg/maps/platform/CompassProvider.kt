package com.wildeburg.maps.platform

expect class CompassProvider() {
    fun start(onHeading: (Float) -> Unit)
    fun stop()
}
