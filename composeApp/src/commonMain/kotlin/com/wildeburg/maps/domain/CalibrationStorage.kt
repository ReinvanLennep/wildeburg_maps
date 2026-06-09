package com.wildeburg.maps.domain

import com.russhwolf.settings.Settings
import com.wildeburg.maps.data.CalibrationData
import com.wildeburg.maps.data.DEFAULT_CALIBRATION
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private const val KEY = "calibration_v1"
private val json = Json { ignoreUnknownKeys = true }

object CalibrationStorage {
    private val settings = Settings()

    fun load(): CalibrationData {
        val raw = settings.getStringOrNull(KEY) ?: return DEFAULT_CALIBRATION
        return try { json.decodeFromString(raw) } catch (e: Exception) { DEFAULT_CALIBRATION }
    }

    fun save(data: CalibrationData) {
        settings.putString(KEY, json.encodeToString(data))
    }

    fun clear() {
        settings.remove(KEY)
    }
}
