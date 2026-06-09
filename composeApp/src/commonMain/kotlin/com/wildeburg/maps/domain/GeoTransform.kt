package com.wildeburg.maps.domain

import com.wildeburg.maps.data.CalibrationData
import com.wildeburg.maps.data.ControlPoint
import com.wildeburg.maps.data.GpsCoords
import com.wildeburg.maps.data.ImageRelPos
import kotlin.math.*

data class AffineTransform(
    val a: Double, val b: Double, val c: Double,
    val d: Double, val e: Double, val f: Double
)

private fun solve3x3(A: Array<DoubleArray>, b: DoubleArray): DoubleArray? {
    val n = 3
    val m = Array(n) { i -> doubleArrayOf(*A[i], b[i]) }
    for (col in 0 until n) {
        var maxRow = col
        for (row in col + 1 until n) {
            if (abs(m[row][col]) > abs(m[maxRow][col])) maxRow = row
        }
        val tmp = m[col]; m[col] = m[maxRow]; m[maxRow] = tmp
        if (abs(m[col][col]) < 1e-12) return null
        for (row in col + 1 until n) {
            val factor = m[row][col] / m[col][col]
            for (k in col..n) m[row][k] -= factor * m[col][k]
        }
    }
    val x = DoubleArray(n)
    for (i in n - 1 downTo 0) {
        x[i] = m[i][n]
        for (j in i + 1 until n) x[i] -= m[i][j] * x[j]
        x[i] /= m[i][i]
    }
    return x
}

fun buildTransform(points: List<ControlPoint>): AffineTransform? {
    if (points.size < 2) return null

    if (points.size == 2) {
        val (p1, p2) = points
        val dLon = p2.gps.lon - p1.gps.lon
        val dLat = p2.gps.lat - p1.gps.lat
        if (abs(dLon) < 1e-12 || abs(dLat) < 1e-12) return null
        val a = (p2.imagePos.x - p1.imagePos.x) / dLon
        val e = (p2.imagePos.y - p1.imagePos.y) / dLat
        return AffineTransform(
            a = a, b = 0.0, c = p1.imagePos.x - a * p1.gps.lon,
            d = 0.0, e = e, f = p1.imagePos.y.toDouble() - e * p1.gps.lat
        )
    }

    val (p1, p2, p3) = points
    val M = arrayOf(
        doubleArrayOf(p1.gps.lon, p1.gps.lat, 1.0),
        doubleArrayOf(p2.gps.lon, p2.gps.lat, 1.0),
        doubleArrayOf(p3.gps.lon, p3.gps.lat, 1.0),
    )
    val bx = doubleArrayOf(p1.imagePos.x.toDouble(), p2.imagePos.x.toDouble(), p3.imagePos.x.toDouble())
    val by = doubleArrayOf(p1.imagePos.y.toDouble(), p2.imagePos.y.toDouble(), p3.imagePos.y.toDouble())
    val xC = solve3x3(M, bx) ?: return null
    val yC = solve3x3(M, by) ?: return null
    return AffineTransform(xC[0], xC[1], xC[2], yC[0], yC[1], yC[2])
}

fun gpsToImagePos(coords: GpsCoords, t: AffineTransform): ImageRelPos = ImageRelPos(
    x = (t.a * coords.lon + t.b * coords.lat + t.c).toFloat(),
    y = (t.d * coords.lon + t.e * coords.lat + t.f).toFloat()
)

fun haversineMeters(a: GpsCoords, b: GpsCoords): Double {
    val R = 6_371_000.0
    val dLat = (b.lat - a.lat) * PI / 180
    val dLon = (b.lon - a.lon) * PI / 180
    val h = sin(dLat / 2).pow(2) +
            cos(a.lat * PI / 180) * cos(b.lat * PI / 180) * sin(dLon / 2).pow(2)
    return 2 * R * asin(sqrt(h))
}

fun accuracyToImageRadius(accuracyMeters: Float, t: AffineTransform, ref: GpsCoords): Float {
    val shifted = GpsCoords(ref.lat + accuracyMeters / 111_320.0, ref.lon)
    val origin = gpsToImagePos(ref, t)
    val dest   = gpsToImagePos(shifted, t)
    return sqrt((dest.x - origin.x).pow(2) + (dest.y - origin.y).pow(2))
}
