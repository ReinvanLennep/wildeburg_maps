import React, { useState } from 'react';
import { StyleSheet, TouchableOpacity, Text, View } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { POI, POICategory } from '../types';
import { POI_CATEGORY_COLORS, POI_CATEGORY_ICONS } from '../data/pois';

interface Props {
  poi: POI;
  // Position in displayed-image-pixel space
  x: number;
  y: number;
}

const ICON_SIZE = 22;
const MARKER_SIZE = 32;

const IONICONS_MAP: Record<POICategory, React.ComponentProps<typeof Ionicons>['name']> = {
  stage: 'musical-notes',
  camping: 'bonfire',
  food: 'restaurant',
  medical: 'medkit',
  water: 'water',
  nature: 'leaf',
  activity: 'bicycle',
  facility: 'water-outline',
  entrance: 'enter',
};

export function POIMarker({ poi, x, y }: Props) {
  const [showLabel, setShowLabel] = useState(false);
  const color = POI_CATEGORY_COLORS[poi.category] ?? '#FFFFFF';
  const iconName = IONICONS_MAP[poi.category];

  return (
    <View
      style={[styles.container, { left: x - MARKER_SIZE / 2, top: y - MARKER_SIZE }]}
      pointerEvents="box-none"
    >
      {showLabel && (
        <View style={styles.label}>
          <Text style={styles.labelText}>{poi.name}</Text>
          {poi.description ? (
            <Text style={styles.descText}>{poi.description}</Text>
          ) : null}
        </View>
      )}

      <TouchableOpacity
        style={[styles.marker, { backgroundColor: color }]}
        onPress={() => setShowLabel((v) => !v)}
        activeOpacity={0.8}
      >
        <Ionicons name={iconName} size={ICON_SIZE} color="#111" />
      </TouchableOpacity>

      {/* Pin tail */}
      <View style={[styles.tail, { borderTopColor: color }]} />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    position: 'absolute',
    alignItems: 'center',
    width: MARKER_SIZE,
  },
  marker: {
    width: MARKER_SIZE,
    height: MARKER_SIZE,
    borderRadius: MARKER_SIZE / 2,
    alignItems: 'center',
    justifyContent: 'center',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.45,
    shadowRadius: 3,
    elevation: 6,
  },
  tail: {
    width: 0,
    height: 0,
    borderLeftWidth: 6,
    borderRightWidth: 6,
    borderTopWidth: 8,
    borderLeftColor: 'transparent',
    borderRightColor: 'transparent',
    marginTop: -1,
  },
  label: {
    position: 'absolute',
    bottom: MARKER_SIZE + 10,
    backgroundColor: 'rgba(20,20,20,0.92)',
    borderRadius: 8,
    paddingVertical: 6,
    paddingHorizontal: 10,
    minWidth: 110,
    maxWidth: 180,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.5,
    shadowRadius: 4,
    elevation: 8,
  },
  labelText: {
    color: '#FFF',
    fontWeight: '700',
    fontSize: 13,
    textAlign: 'center',
  },
  descText: {
    color: '#CCC',
    fontSize: 11,
    textAlign: 'center',
    marginTop: 2,
  },
});
