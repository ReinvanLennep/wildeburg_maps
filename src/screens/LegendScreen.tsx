import React from 'react';
import {
  SafeAreaView,
  ScrollView,
  StyleSheet,
  Text,
  View,
} from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { POIS, POI_CATEGORY_COLORS } from '../data/pois';
import { POICategory } from '../types';

type CategoryInfo = {
  icon: React.ComponentProps<typeof Ionicons>['name'];
  label: string;
  description: string;
};

const CATEGORIES: Record<POICategory, CategoryInfo> = {
  stage: { icon: 'musical-notes', label: 'Stage', description: 'Live music & performances' },
  camping: { icon: 'bonfire', label: 'Camping', description: 'Tent camping zones' },
  food: { icon: 'restaurant', label: 'Food & Drink', description: 'Food stalls, bars' },
  medical: { icon: 'medkit', label: 'EHBO / First Aid', description: 'Medical assistance' },
  water: { icon: 'water', label: 'Water / Beach', description: 'Swim, chill by the water' },
  nature: { icon: 'leaf', label: 'Nature Area', description: 'Forests, dunes, slopes' },
  activity: { icon: 'bicycle', label: 'Activity', description: 'Things to do' },
  facility: { icon: 'water-outline', label: 'Facility', description: 'Showers, toilets, supplies' },
  entrance: { icon: 'enter', label: 'Entrance', description: 'Festival entrance / exit' },
};

export function LegendScreen() {
  const groupedPois = POIS.reduce<Partial<Record<POICategory, typeof POIS>>>(
    (acc, poi) => {
      if (!acc[poi.category]) acc[poi.category] = [];
      acc[poi.category]!.push(poi);
      return acc;
    },
    {},
  );

  return (
    <SafeAreaView style={styles.root}>
      <ScrollView contentContainerStyle={styles.container}>
        <Text style={styles.title}>Map Legend</Text>
        <Text style={styles.subtitle}>All locations on the Wildeburg festival terrain</Text>

        {(Object.keys(CATEGORIES) as POICategory[]).map((cat) => {
          const info = CATEGORIES[cat];
          const pois = groupedPois[cat];
          if (!pois?.length) return null;
          const color = POI_CATEGORY_COLORS[cat];

          return (
            <View key={cat} style={styles.categoryBlock}>
              <View style={styles.categoryHeader}>
                <View style={[styles.categoryIcon, { backgroundColor: color }]}>
                  <Ionicons name={info.icon} size={18} color="#111" />
                </View>
                <View>
                  <Text style={styles.categoryLabel}>{info.label}</Text>
                  <Text style={styles.categoryDesc}>{info.description}</Text>
                </View>
              </View>

              {pois.map((poi) => (
                <View key={poi.id} style={styles.poiRow}>
                  <View style={[styles.poiDot, { backgroundColor: color }]} />
                  <View style={styles.poiInfo}>
                    <Text style={styles.poiName}>{poi.name}</Text>
                    {poi.description ? (
                      <Text style={styles.poiDesc}>{poi.description}</Text>
                    ) : null}
                  </View>
                </View>
              ))}
            </View>
          );
        })}

        <View style={styles.footer}>
          <Text style={styles.footerText}>
            Wildeburg Maps • Offline GPS Navigation{'\n'}
            Tap any marker on the map for details.
          </Text>
        </View>
      </ScrollView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  root: { flex: 1, backgroundColor: '#111' },
  container: { padding: 20, gap: 16 },
  title: { color: '#FFF', fontSize: 26, fontWeight: '800' },
  subtitle: { color: '#888', fontSize: 14, marginTop: 2 },
  categoryBlock: {
    backgroundColor: '#1A1A1A',
    borderRadius: 12,
    padding: 14,
    gap: 10,
  },
  categoryHeader: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 12,
    marginBottom: 4,
  },
  categoryIcon: {
    width: 36,
    height: 36,
    borderRadius: 18,
    alignItems: 'center',
    justifyContent: 'center',
  },
  categoryLabel: { color: '#FFF', fontWeight: '700', fontSize: 16 },
  categoryDesc: { color: '#888', fontSize: 12, marginTop: 1 },
  poiRow: {
    flexDirection: 'row',
    alignItems: 'flex-start',
    gap: 10,
    paddingLeft: 4,
  },
  poiDot: {
    width: 8,
    height: 8,
    borderRadius: 4,
    marginTop: 4,
  },
  poiInfo: { flex: 1 },
  poiName: { color: '#DDD', fontWeight: '600', fontSize: 14 },
  poiDesc: { color: '#777', fontSize: 12, marginTop: 2 },
  footer: {
    alignItems: 'center',
    paddingVertical: 20,
  },
  footerText: {
    color: '#555',
    fontSize: 12,
    textAlign: 'center',
    lineHeight: 18,
  },
});
