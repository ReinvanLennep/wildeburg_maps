import React, { useEffect, useRef } from 'react';
import { Animated, StyleSheet, View } from 'react-native';

interface Props {
  // Position in image-pixel space (after multiplying by displayed image size)
  x: number;
  y: number;
  accuracyRadius?: number; // in image-pixel space
  heading?: number | null;
}

export function LocationDot({ x, y, accuracyRadius, heading }: Props) {
  const pulse = useRef(new Animated.Value(1)).current;

  useEffect(() => {
    Animated.loop(
      Animated.sequence([
        Animated.timing(pulse, { toValue: 1.6, duration: 900, useNativeDriver: true }),
        Animated.timing(pulse, { toValue: 1, duration: 900, useNativeDriver: true }),
      ]),
    ).start();
  }, [pulse]);

  const DOT_SIZE = 16;

  return (
    <View
      style={[
        styles.container,
        { left: x - DOT_SIZE / 2, top: y - DOT_SIZE / 2 },
      ]}
      pointerEvents="none"
    >
      {/* Accuracy circle */}
      {accuracyRadius != null && accuracyRadius > DOT_SIZE / 2 && (
        <View
          style={[
            styles.accuracy,
            {
              width: accuracyRadius * 2,
              height: accuracyRadius * 2,
              borderRadius: accuracyRadius,
              marginLeft: -(accuracyRadius - DOT_SIZE / 2),
              marginTop: -(accuracyRadius - DOT_SIZE / 2),
            },
          ]}
        />
      )}

      {/* Heading cone */}
      {heading != null && (
        <View
          style={[styles.headingCone, { transform: [{ rotate: `${heading}deg` }] }]}
        />
      )}

      {/* Pulsing outer ring */}
      <Animated.View
        style={[
          styles.pulse,
          { transform: [{ scale: pulse }], width: DOT_SIZE, height: DOT_SIZE, borderRadius: DOT_SIZE / 2 },
        ]}
      />

      {/* Core dot */}
      <View style={[styles.dot, { width: DOT_SIZE, height: DOT_SIZE, borderRadius: DOT_SIZE / 2 }]} />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    position: 'absolute',
    width: 16,
    height: 16,
    alignItems: 'center',
    justifyContent: 'center',
  },
  dot: {
    backgroundColor: '#2979FF',
    borderWidth: 2,
    borderColor: '#FFFFFF',
    position: 'absolute',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.4,
    shadowRadius: 2,
    elevation: 4,
  },
  pulse: {
    backgroundColor: 'rgba(41, 121, 255, 0.25)',
    position: 'absolute',
  },
  accuracy: {
    position: 'absolute',
    backgroundColor: 'rgba(41, 121, 255, 0.12)',
    borderWidth: 1,
    borderColor: 'rgba(41, 121, 255, 0.35)',
  },
  headingCone: {
    position: 'absolute',
    width: 0,
    height: 0,
    borderLeftWidth: 7,
    borderRightWidth: 7,
    borderBottomWidth: 20,
    borderLeftColor: 'transparent',
    borderRightColor: 'transparent',
    borderBottomColor: 'rgba(41, 121, 255, 0.55)',
    top: -18,
  },
});
