# Wildeburg Maps

Offline GPS navigation app for the Wildeburg festival at **Netl de Wildste Tuin, Kraggenburg**. Shows your real-time position on the festival map — works completely without internet.

## Features

- **Offline-first** — festival map is bundled; GPS works via satellite, no internet needed
- **Pre-loaded calibration** — all stages georeferenced from satellite analysis; works out of the box (~50–150 m accuracy)
- **Pinch to zoom / pan** — smooth gestures, double-tap to reset zoom
- **Live location dot** — animated position with accuracy circle and heading cone
- **Nearest location chip** — closest stage/facility and distance in real time
- **22 POI markers** — all stages, camping zones, services, activities; tap any pin for details
- **Compass indicator** — rotating needle always points north
- **Calibration system** — improve accuracy to ~10 m by adding GPS reference points on-site

## Tech stack

- **Kotlin Multiplatform (KMP)** with **Compose Multiplatform** — shared UI on Android and iOS
- `CLLocationManager` (iOS) / `FusedLocationProviderClient` (Android) — GPS
- `SensorManager` (Android) / `CLLocationManager.startUpdatingHeading()` (iOS) — compass
- `multiplatform-settings` — persisted calibration data
- Affine 3-point GPS ↔ image pixel transform (Gaussian elimination)

## Prerequisites

| Tool | Minimum version |
|------|----------------|
| Android Studio (Hedgehog or later) | with KMP plugin |
| JDK | 17 |
| Android SDK | API 24+ |
| Xcode (iOS only) | 15+ |
| macOS (iOS only) | required for iOS build |

## Run on Android

1. Clone the repo and check out the dev branch:
   ```bash
   git clone https://github.com/reinvanlennep/wildeburg_maps.git
   cd wildeburg_maps
   git checkout claude/wildeburg-offline-map-app-9e5gc8
   ```
2. Open **Android Studio** → *Open* → select the `wildeburg_maps` folder.
3. Let Gradle sync finish (first sync downloads ~1 GB of dependencies).
4. Connect an Android device or start an emulator.
5. Run the `:composeApp` configuration (green Play button).

## Run on iOS

1. Open the project in Android Studio and let Gradle sync complete.
2. In a terminal, build the iOS framework:
   ```bash
   ./gradlew :composeApp:linkDebugFrameworkIosSimulatorArm64
   ```
3. Open `iosApp/iosApp.xcodeproj` in Xcode.  
   *(If you don't have the `.xcodeproj` yet, create a new Xcode project named `iosApp` in the `iosApp/` folder and wire up `ContentView.swift`.)*
4. Select your simulator or device and press Run.

## GPS accuracy

Ships with 4-point calibration derived from satellite image analysis of the venue (52.683°N, 5.874°E). Expected out-of-the-box accuracy: **~50–150 m**.

### Improve accuracy on-site (optional)

Walk to any recognisable spot and add a calibration point to reach ~10 m:

1. Open the **Calibrate** tab
2. Tap **"Add calibration point"**
3. Enter the spot's label and tap it on the map image
4. Press **"Use my current GPS"** while standing there
5. Repeat for a second (ideally third) point
6. Press **Save**

## Venue

**Netl de Wildste Tuin**  
Leemringweg 19, 8317 RD Kraggenburg, Flevoland, Netherlands  
52.6830°N, 5.8736°E

## Project structure

```
composeApp/src/
├── commonMain/kotlin/com/wildeburg/maps/
│   ├── App.kt                          # Root composable, navigation, dark theme
│   ├── data/
│   │   ├── Types.kt                    # GpsCoords, POI, LocationData, etc.
│   │   ├── POIs.kt                     # All 22 POIs with coordinates
│   │   └── FestivalConfig.kt           # Default calibration + map aspect ratio
│   ├── domain/
│   │   ├── GeoTransform.kt             # Affine GPS ↔ pixel transform
│   │   └── CalibrationStorage.kt       # Persist / load calibration
│   ├── platform/
│   │   ├── LocationProvider.kt         # expect class
│   │   └── CompassProvider.kt          # expect class
│   └── ui/
│       ├── MapScreen.kt
│       ├── CalibrationScreen.kt
│       ├── LegendScreen.kt
│       └── components/
│           ├── FestivalMapView.kt       # Pannable/zoomable map
│           ├── LocationDot.kt           # Animated GPS dot
│           ├── POIMarker.kt             # Tappable POI pin
│           └── CompassIndicator.kt      # Compass rose
├── androidMain/                        # FusedLocationProviderClient, SensorManager
└── iosMain/                            # CLLocationManager delegate

iosApp/iosApp/                          # Swift entry point
composeApp/src/commonMain/composeResources/drawable/
└── festival_map.png                    # Bundled offline map image
```

## Updating the festival map

Replace `composeApp/src/commonMain/composeResources/drawable/festival_map.png` with the new image. Update POI image positions in `data/POIs.kt` (values are 0–1 fractions of image width/height). Update `MAP_ASPECT_RATIO` in `data/FestivalConfig.kt` if the aspect ratio changed.
