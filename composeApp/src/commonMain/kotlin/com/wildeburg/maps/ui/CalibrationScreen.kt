package com.wildeburg.maps.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wildeburg.maps.data.*
import com.wildeburg.maps.domain.*
import com.wildeburg.maps.platform.LocationProvider

private val BG     = Color(0xFF111111)
private val CARD   = Color(0xFF1C1C1C)
private val ACCENT = Color(0xFF2979FF)

@Composable
fun CalibrationScreen(
    calibration: CalibrationData,
    onSave: (CalibrationData) -> Unit,
    onBack: () -> Unit
) {
    val locationProvider = remember { LocationProvider() }
    var liveLocation by remember { mutableStateOf<LocationData?>(null) }

    DisposableEffect(Unit) {
        locationProvider.startUpdates(onUpdate = { liveLocation = it }, onError = {})
        onDispose { locationProvider.stopUpdates() }
    }

    var points by remember { mutableStateOf(calibration.points) }

    // Dialog state
    var showDialog by remember { mutableStateOf(false) }
    var dialogLabel by remember { mutableStateOf("") }
    var dialogLat   by remember { mutableStateOf("") }
    var dialogLon   by remember { mutableStateOf("") }
    var dialogImgX  by remember { mutableStateOf("") }
    var dialogImgY  by remember { mutableStateOf("") }

    val transform = remember(points) { buildTransform(points) }

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(BG)
            .statusBarsPadding().navigationBarsPadding(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = onBack) { Text("← Back", color = Color.White) }
                Spacer(Modifier.width(8.dp))
                Text("Calibration", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }
        }
        item {
            Text(
                "Add ≥ 2 reference points. For each point, enter its GPS coordinates " +
                "and its position on the festival map (0–1 fractions).",
                color = Color(0xFF999999), fontSize = 13.sp, lineHeight = 19.sp
            )
        }
        items(points) { pt ->
            Row(
                modifier = Modifier.fillMaxWidth().background(CARD, RoundedCornerShape(10.dp)).padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(28.dp).clip(CircleShape).background(ACCENT)
                ) {
                    Text("${points.indexOf(pt)+1}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
                Column(Modifier.weight(1f)) {
                    Text(pt.label, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    Text("${pt.gps.lat.format()}, ${pt.gps.lon.format()}",
                        color = Color(0xFF888888), fontSize = 11.sp, fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace)
                }
                Text("🗑", fontSize = 18.sp, modifier = Modifier.clickable {
                    points = points.filter { it.id != pt.id }
                })
            }
        }
        item {
            OutlinedButton(
                onClick = { showDialog = true },
                modifier = Modifier.fillMaxWidth(),
                border = ButtonDefaults.outlinedButtonBorder
            ) {
                Text("+ Add calibration point", color = Color.White)
            }
        }
        if (transform != null) {
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0x1A4CAF50), RoundedCornerShape(8.dp))
                        .padding(10.dp)
                ) {
                    Text("✅", fontSize = 16.sp)
                    Text("Transform ready (${points.size} points)", color = Color(0xFF4CAF50), fontSize = 13.sp)
                }
            }
        }
        if (points.size >= 2) {
            item {
                Button(
                    onClick = { onSave(CalibrationData(points = points)) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = ACCENT)
                ) {
                    Text("Save calibration", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            containerColor = Color(0xFF1C1C1C),
            title = { Text("Add calibration point", color = Color.White, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    CalibInput("Label (optional)", dialogLabel) { dialogLabel = it }
                    CalibInput("Latitude (e.g. 52.6878)", dialogLat, KeyboardType.Decimal) { dialogLat = it }
                    CalibInput("Longitude (e.g. 5.8611)", dialogLon, KeyboardType.Decimal) { dialogLon = it }
                    CalibInput("Map X (0–1, e.g. 0.19)", dialogImgX, KeyboardType.Decimal) { dialogImgX = it }
                    CalibInput("Map Y (0–1, e.g. 0.32)", dialogImgY, KeyboardType.Decimal) { dialogImgY = it }
                    liveLocation?.let { loc ->
                        TextButton(onClick = {
                            dialogLat = loc.lat.format()
                            dialogLon = loc.lon.format()
                        }) {
                            Text("📍 Use my GPS (${loc.lat.format()}, ${loc.lon.format()})", color = ACCENT, fontSize = 12.sp)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val lat = dialogLat.toDoubleOrNull() ?: return@TextButton
                    val lon = dialogLon.toDoubleOrNull() ?: return@TextButton
                    val ix  = dialogImgX.toFloatOrNull() ?: return@TextButton
                    val iy  = dialogImgY.toFloatOrNull() ?: return@TextButton
                    points = points + ControlPoint(
                        id = kotlin.random.Random.nextLong().toString(),
                        label = dialogLabel.ifBlank { "Point ${points.size + 1}" },
                        gps = GpsCoords(lat, lon),
                        imagePos = ImageRelPos(ix, iy)
                    )
                    dialogLabel = ""; dialogLat = ""; dialogLon = ""; dialogImgX = ""; dialogImgY = ""
                    showDialog = false
                }) { Text("Add", color = ACCENT) }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Cancel", color = Color(0xFFAAAAAA)) }
            }
        )
    }
}

@Composable
private fun CalibInput(label: String, value: String, kb: KeyboardType = KeyboardType.Text, onValue: (String) -> Unit) {
    OutlinedTextField(
        value = value, onValueChange = onValue, label = { Text(label, fontSize = 11.sp) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = kb),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White, unfocusedTextColor = Color.White,
            focusedLabelColor = Color(0xFF999999), unfocusedLabelColor = Color(0xFF666666),
            focusedBorderColor = Color(0xFF2979FF), unfocusedBorderColor = Color(0xFF333333)
        )
    )
}

private fun Double.format(): String {
    val factor = 100000.0
    val rounded = kotlin.math.round(this * factor) / factor
    return rounded.toString().let { s ->
        val dot = s.indexOf('.')
        if (dot < 0) "$s.00000"
        else s.take(dot + 6).padEnd(dot + 6, '0')
    }
}
