# Wildeburg Maps 🗺️

Offline GPS navigation app for the Wildeburg festival. Shows your real-time position on the festival map — works without internet.

## Features

- **Offline-first** — the festival map is bundled in the app; GPS works via satellite, no internet needed
- **Pinch to zoom / pan** — smooth gestures on the festival map
- **Live location dot** — your position with accuracy circle and heading cone
- **POI markers** — all stages, camping zones, services, and activities
- **Compass indicator** — always know which way you're facing
- **Calibration system** — align GPS coordinates to the map image using 2–4 reference points

## Getting started

### Prerequisites

- [Node.js](https://nodejs.org) ≥ 18
- [Expo CLI](https://docs.expo.dev/get-started/installation/): `npm install -g expo-cli`
- [Expo Go](https://expo.dev/client) app on your phone **or** an iOS/Android simulator

### Install & run

```bash
npm install
npx expo start
```

Scan the QR code with Expo Go (Android) or the Camera app (iOS).

### Build for production

```bash
# iOS
npx eas build --platform ios

# Android
npx eas build --platform android
```

## Calibrating the map

The app ships with **placeholder GPS coordinates**. Before the festival, calibrate the map so your location dot is accurate:

1. Open the **Calibrate** tab
2. Tap **"Add calibration point"**
3. Tap a recognisable location on the festival map (e.g. main stage entrance)
4. Either:
   - Walk to that exact spot and press **"Use my current GPS"**, or
   - Enter the GPS coordinates manually
5. Repeat for a second (ideally third) point
6. Press **"Save calibration"**

Two calibration points give a good result. Three or more further improve accuracy.

## Project structure

```
src/
├── components/
│   ├── FestivalMap.tsx       # Pannable/zoomable map with overlays
│   ├── LocationDot.tsx       # Animated GPS position indicator
│   ├── POIMarker.tsx         # Tappable POI pin
│   └── CompassIndicator.tsx  # Compass rose
├── screens/
│   ├── MapScreen.tsx         # Main screen
│   ├── CalibrationScreen.tsx # GPS calibration wizard
│   └── LegendScreen.tsx      # All POIs explained
├── hooks/
│   ├── useLocation.ts        # expo-location wrapper
│   └── useCompass.ts         # Magnetometer heading
├── utils/
│   ├── geoTransform.ts       # Affine GPS ↔ pixel transform
│   └── storage.ts            # AsyncStorage helpers
└── data/
    ├── pois.ts               # All festival POIs with image positions
    └── festivalConfig.ts     # Default calibration & map config
```

## Updating the festival map image

Replace `assets/images/festival-map.png` with the new map. Then update the POI image positions in `src/data/pois.ts` (values are 0–1 fractions of image width/height).

## Tech stack

- **React Native + Expo SDK 51**
- `react-native-gesture-handler` + `react-native-reanimated` — smooth pan/zoom
- `expo-location` — GPS (works offline via satellite)
- `expo-sensors` — compass heading
- `@react-native-async-storage/async-storage` — persist calibration
- `@react-navigation/bottom-tabs` — tab navigation
