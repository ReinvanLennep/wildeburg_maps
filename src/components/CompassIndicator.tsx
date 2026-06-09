import React from 'react';
import { StyleSheet, View, Text } from 'react-native';

interface Props {
  heading: number | null;
}

export function CompassIndicator({ heading }: Props) {
  if (heading === null) return null;

  const rotation = `${-heading}deg`;

  return (
    <View style={styles.wrapper} pointerEvents="none">
      <View style={[styles.needle, { transform: [{ rotate: rotation }] }]}>
        <View style={styles.north} />
        <View style={styles.south} />
      </View>
      <Text style={styles.label}>N</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  wrapper: {
    width: 44,
    height: 44,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: 'rgba(20,20,20,0.75)',
    borderRadius: 22,
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.2)',
  },
  needle: {
    width: 4,
    height: 30,
    alignItems: 'center',
    justifyContent: 'center',
  },
  north: {
    flex: 1,
    width: 4,
    backgroundColor: '#F44336',
    borderTopLeftRadius: 2,
    borderTopRightRadius: 2,
  },
  south: {
    flex: 1,
    width: 4,
    backgroundColor: '#BDBDBD',
    borderBottomLeftRadius: 2,
    borderBottomRightRadius: 2,
  },
  label: {
    position: 'absolute',
    top: 3,
    fontSize: 9,
    fontWeight: '800',
    color: '#F44336',
    letterSpacing: 0.5,
  },
});
