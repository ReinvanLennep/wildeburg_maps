# Wildeburg Maps 🗺️

Offline GPS navigation app for the Wildeburg festival at **Netl de Wildste Tuin, Kraggenburg**. Shows your real-time position on the festival map — works completely without internet.

## Features

- **Offline-first** — the festival map is bundled in the app; GPS works via satellite, no internet needed
- **Pre-loaded GPS coordinates** — all stages and locations are already georeferenced from satellite image analysis; works out of the box
- **Pinch to zoom / pan** — smooth gestures, double-tap to zoom in
- **Live location dot** — animated position with accuracy circle and heading cone
- **Nearest location chip** — live readout of the closest stage/facility and your distance to it
- **22 POI markers** — all stages, camping zones, services, and activities; tap any pin for details
- **Compass indicator** — always know which direction you're facing
- **Calibration system** — walk to any known spot and set a GPS reference point to sharpen accuracy from ~150 m to ~10 m

## Getting started

### Prerequisites

- [Node.js](https://nodejs.org) ≥ 18
- [Expo Go](https://expo.dev/client) app on your phone (iOS or Android)

### Run on your phone (Expo Go)

```bash
# Clone and switch to the dev branch
git clone https://github.com/reinvanlennep/wildeburg_maps.git
cd wildeburg_maps
git checkout claude/wildeburg-offline-map-app-9e5gc8

# Install dependencies
npm install

# Start the dev server
npx expo start
```

Scan the QR code with **Expo Go** (Android) or the **Camera app** (iOS). Your phone and computer must be on the same WiFi.

### Build a standalone app (share with friends)

```bash
npm install -g eas-cli
eas login
eas build --platform android --profile preview   # APK, no Play Store needed
eas build --platform ios                          # requires Apple Developer account
```

## GPS accuracy

The app ships with coordinates derived from pixel analysis of an annotated satellite screenshot of the venue, cross-referenced with the confirmed address (Leemringweg 19, Kraggenburg — 52.683°N, 5.874°E). Expected accuracy out of the box: **~50–150 m**.

### Improving accuracy on-site (optional)

Walk to any recognisable location and add a calibration point to tighten accuracy to ~10 m:

1. Open the **Calibrate** tab
2. Tap **"Add calibration point"**
3. Tap that spot on the festival map image
4. Press **"Use my current GPS"** while standing there
5. Repeat for a second (ideally third) point
6. Press **"Save calibration"**

## Venue

**Netl de Wildste Tuin**
Leemringweg 19, 8317 RD Kraggenburg, Flevoland, Netherlands
52.6830°N, 5.8736°E

## Project structure

```
src/
├── components/
│   ├── FestivalMap.tsx       # Pannable/zoomable map with overlays
│   ├── LocationDot.tsx       # Animated GPS position indicator
│   ├── POIMarker.tsx         # Tappable POI pin
│   └── CompassIndicator.tsx  # Compass rose
├── screens/
│   ├── MapScreen.tsx         # Main screen + nearest-POI chip
│   ├── CalibrationScreen.tsx # GPS calibration wizard
│   └── LegendScreen.tsx      # All POIs grouped by category
├── hooks/
│   ├── useLocation.ts        # expo-location wrapper
│   └── useCompass.ts         # Magnetometer heading
├── utils/
│   ├── geoTransform.ts       # Affine GPS ↔ pixel transform (2–4 control points)
│   └── storage.ts            # AsyncStorage helpers
└── data/
    ├── pois.ts               # All 22 POIs with GPS coords + image positions
    └── festivalConfig.ts     # Default 4-point calibration from satellite analysis
```

## Updating the festival map image

Replace `assets/images/festival-map.png` with the new map. Update POI image positions in `src/data/pois.ts` (values are 0–1 fractions of image width/height, measured on the new image).

## Tech stack

- **React Native + Expo SDK 51**
- `react-native-gesture-handler` + `react-native-reanimated` — smooth pan/zoom
- `expo-location` — GPS (works offline via satellite)
- `expo-sensors` — compass heading
- `@react-native-async-storage/async-storage` — persist calibration
- `@react-navigation/bottom-tabs` — tab navigation
