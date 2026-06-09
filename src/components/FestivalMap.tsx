import React, { useCallback, useRef, useState } from 'react';
import {
  Image,
  LayoutChangeEvent,
  StyleSheet,
  View,
} from 'react-native';
import Animated, {
  useAnimatedStyle,
  useSharedValue,
  withSpring,
} from 'react-native-reanimated';
import {
  Gesture,
  GestureDetector,
} from 'react-native-gesture-handler';
import { LocationDot } from './LocationDot';
import { POIMarker } from './POIMarker';
import { buildTransform, gpsToImagePos, accuracyToImageRadius } from '../utils/geoTransform';
import { CalibrationData, POI, UserLocation } from '../types';

const festivalMapImage = require('../../assets/images/festival-map.png');

const MIN_SCALE = 0.5;
const MAX_SCALE = 6.0;

interface Props {
  calibration: CalibrationData | null;
  location: UserLocation | null;
  pois: POI[];
  onMapTap?: (imageRelX: number, imageRelY: number) => void;
}

export function FestivalMap({ calibration, location, pois, onMapTap }: Props) {
  // Container (screen) size
  const [containerSize, setContainerSize] = useState({ width: 0, height: 0 });
  // Natural image size after it loads
  const [naturalSize, setNaturalSize] = useState({ width: 508, height: 870 });

  // Displayed image size (respecting contain + container)
  const displayedSize = (() => {
    if (!containerSize.width || !containerSize.height) return { width: 0, height: 0 };
    const imgAspect = naturalSize.width / naturalSize.height;
    const contAspect = containerSize.width / containerSize.height;
    if (imgAspect > contAspect) {
      return { width: containerSize.width, height: containerSize.width / imgAspect };
    }
    return { width: containerSize.height * imgAspect, height: containerSize.height };
  })();

  // Pan / zoom animated values
  const scale = useSharedValue(1);
  const savedScale = useSharedValue(1);
  const translateX = useSharedValue(0);
  const translateY = useSharedValue(0);
  const savedTranslateX = useSharedValue(0);
  const savedTranslateY = useSharedValue(0);
  const focalX = useSharedValue(0);
  const focalY = useSharedValue(0);

  const clampTranslation = useCallback(
    (tx: number, ty: number, s: number) => {
      const { width: cw, height: ch } = containerSize;
      const { width: iw, height: ih } = displayedSize;
      const maxX = Math.max(0, (iw * s - cw) / 2);
      const maxY = Math.max(0, (ih * s - ch) / 2);
      return {
        x: Math.max(-maxX, Math.min(maxX, tx)),
        y: Math.max(-maxY, Math.min(maxY, ty)),
      };
    },
    [containerSize, displayedSize],
  );

  // ── Pinch gesture ─────────────────────────────────────────────────────────
  const pinchGesture = Gesture.Pinch()
    .onStart((e) => {
      focalX.value = e.focalX;
      focalY.value = e.focalY;
    })
    .onUpdate((e) => {
      const newScale = Math.min(MAX_SCALE, Math.max(MIN_SCALE, savedScale.value * e.scale));
      scale.value = newScale;

      // Zoom toward focal point
      const ds = newScale - savedScale.value;
      translateX.value = savedTranslateX.value - ds * (focalX.value - containerSize.width / 2);
      translateY.value = savedTranslateY.value - ds * (focalY.value - containerSize.height / 2);
    })
    .onEnd(() => {
      savedScale.value = scale.value;
      const clamped = clampTranslation(translateX.value, translateY.value, scale.value);
      translateX.value = withSpring(clamped.x, { damping: 20 });
      translateY.value = withSpring(clamped.y, { damping: 20 });
      savedTranslateX.value = clamped.x;
      savedTranslateY.value = clamped.y;
    });

  // ── Pan gesture ───────────────────────────────────────────────────────────
  const panGesture = Gesture.Pan()
    .minPointers(1)
    .maxPointers(2)
    .onUpdate((e) => {
      translateX.value = savedTranslateX.value + e.translationX;
      translateY.value = savedTranslateY.value + e.translationY;
    })
    .onEnd(() => {
      const clamped = clampTranslation(translateX.value, translateY.value, scale.value);
      translateX.value = withSpring(clamped.x, { damping: 20 });
      translateY.value = withSpring(clamped.y, { damping: 20 });
      savedTranslateX.value = clamped.x;
      savedTranslateY.value = clamped.y;
    });

  // ── Double-tap to zoom ────────────────────────────────────────────────────
  const doubleTap = Gesture.Tap()
    .numberOfTaps(2)
    .onEnd((e) => {
      const targetScale = scale.value < 2 ? 2.5 : 1;
      const ds = targetScale - scale.value;
      scale.value = withSpring(targetScale, { damping: 18 });
      savedScale.value = targetScale;
      const newTx = savedTranslateX.value - ds * (e.x - containerSize.width / 2);
      const newTy = savedTranslateY.value - ds * (e.y - containerSize.height / 2);
      const clamped = clampTranslation(newTx, newTy, targetScale);
      translateX.value = withSpring(clamped.x, { damping: 18 });
      translateY.value = withSpring(clamped.y, { damping: 18 });
      savedTranslateX.value = clamped.x;
      savedTranslateY.value = clamped.y;
    });

  // ── Single tap (for calibration) ──────────────────────────────────────────
  const singleTap = Gesture.Tap()
    .numberOfTaps(1)
    .runOnJS(true)
    .onEnd((e) => {
      if (!onMapTap || !displayedSize.width) return;
      // Convert screen tap to image-relative position
      const imgOffX = (containerSize.width - displayedSize.width) / 2;
      const imgOffY = (containerSize.height - displayedSize.height) / 2;
      const imgX = (e.x - translateX.value - imgOffX) / scale.value;
      const imgY = (e.y - translateY.value - imgOffY) / scale.value;
      onMapTap(imgX / displayedSize.width, imgY / displayedSize.height);
    });

  const composedGesture = Gesture.Simultaneous(
    panGesture,
    pinchGesture,
    Gesture.Exclusive(doubleTap, singleTap),
  );

  const animatedStyle = useAnimatedStyle(() => ({
    transform: [
      { translateX: translateX.value },
      { translateY: translateY.value },
      { scale: scale.value },
    ],
  }));

  // ── Center on user ────────────────────────────────────────────────────────
  const transform = calibration && buildTransform(calibration.points);

  const userImagePos = (() => {
    if (!location || !transform || !displayedSize.width) return null;
    const rel = gpsToImagePos({ lat: location.lat, lon: location.lon }, transform);
    return {
      x: rel.x * displayedSize.width,
      y: rel.y * displayedSize.height,
    };
  })();

  const userAccuracyRadius = (() => {
    if (!location?.accuracy || !transform || !displayedSize.width) return undefined;
    const rel = accuracyToImageRadius(
      location.accuracy,
      transform,
      { lat: location.lat, lon: location.lon },
    );
    return rel * displayedSize.width;
  })();

  const handleLayout = (e: LayoutChangeEvent) => {
    const { width, height } = e.nativeEvent.layout;
    setContainerSize({ width, height });
  };

  // Centering on user
  const centerOnUser = useCallback(() => {
    if (!userImagePos || !containerSize.width) return;
    const imgOffX = (containerSize.width - displayedSize.width) / 2;
    const imgOffY = (containerSize.height - displayedSize.height) / 2;
    const targetX = containerSize.width / 2 - (userImagePos.x * scale.value + imgOffX);
    const targetY = containerSize.height / 2 - (userImagePos.y * scale.value + imgOffY);
    const clamped = clampTranslation(targetX, targetY, scale.value);
    translateX.value = withSpring(clamped.x, { damping: 18 });
    translateY.value = withSpring(clamped.y, { damping: 18 });
    savedTranslateX.value = clamped.x;
    savedTranslateY.value = clamped.y;
  }, [userImagePos, containerSize, displayedSize, scale.value]);

  // Expose centerOnUser via a ref callback
  const centerOnUserRef = useRef(centerOnUser);
  centerOnUserRef.current = centerOnUser;

  return (
    <View style={styles.container} onLayout={handleLayout}>
      <GestureDetector gesture={composedGesture}>
        <Animated.View style={[styles.mapWrapper, animatedStyle]}>
          {/* Festival map image */}
          <Image
            source={festivalMapImage}
            style={[styles.mapImage, displayedSize]}
            resizeMode="contain"
            onLoad={(e) => {
              const { width, height } = e.nativeEvent.source;
              setNaturalSize({ width, height });
            }}
          />

          {/* POI markers */}
          {displayedSize.width > 0 &&
            pois.map((poi) => (
              <POIMarker
                key={poi.id}
                poi={poi}
                x={poi.imagePos.x * displayedSize.width}
                y={poi.imagePos.y * displayedSize.height}
              />
            ))}

          {/* User location dot */}
          {userImagePos && (
            <LocationDot
              x={userImagePos.x}
              y={userImagePos.y}
              accuracyRadius={userAccuracyRadius}
              heading={location?.heading ?? null}
            />
          )}
        </Animated.View>
      </GestureDetector>
    </View>
  );
}

// Expose centerOnUser imperatively via forwardRef
export type FestivalMapHandle = { centerOnUser: () => void };

const styles = StyleSheet.create({
  container: {
    flex: 1,
    overflow: 'hidden',
    backgroundColor: '#111',
  },
  mapWrapper: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  mapImage: {
    position: 'absolute',
  },
});
