import React, { useCallback, useEffect, useState } from 'react';
import {
  Alert,
  SafeAreaView,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { FestivalMap } from '../components/FestivalMap';
import { CompassIndicator } from '../components/CompassIndicator';
import { useLocation } from '../hooks/useLocation';
import { useCompass } from '../hooks/useCompass';
import { loadCalibration } from '../utils/storage';
import { PLACEHOLDER_CALIBRATION } from '../data/festivalConfig';
import { POIS } from '../data/pois';
import { CalibrationData } from '../types';

interface Props {
  navigation: any;
}

export function MapScreen({ navigation }: Props) {
  const { location, status } = useLocation();
  const heading = useCompass();
  const [calibration, setCalibration] = useState<CalibrationData | null>(null);
  const [showPOIs, setShowPOIs] = useState(true);

  // ── Load calibration on mount ─────────────────────────────────────────────
  useEffect(() => {
    loadCalibration().then((saved) => {
      setCalibration(saved ?? PLACEHOLDER_CALIBRATION);
    });
  }, []);

  // Reload calibration when navigating back from calibration screen
  const reloadCalibration = useCallback(() => {
    loadCalibration().then((saved) => {
      if (saved) setCalibration(saved);
    });
  }, []);

  useEffect(() => {
    const unsubscribe = navigation.addListener('focus', reloadCalibration);
    return unsubscribe;
  }, [navigation, reloadCalibration]);

  const isUncalibrated = calibration?.createdAt === 0;

  const gpsStatusColor = () => {
    switch (status) {
      case 'active': return location ? '#4CAF50' : '#FFC107';
      case 'denied': return '#F44336';
      case 'error': return '#F44336';
      default: return '#9E9E9E';
    }
  };

  const gpsStatusLabel = () => {
    switch (status) {
      case 'active':
        return location
          ? `±${Math.round(location.accuracy ?? 0)}m`
          : 'Waiting…';
      case 'denied': return 'No permission';
      case 'error': return 'GPS error';
      default: return 'GPS…';
    }
  };

  return (
    <View style={styles.root}>
      {/* ── Map ────────────────────────────────────────────────────────── */}
      <FestivalMap
        calibration={calibration}
        location={location}
        pois={showPOIs ? POIS : []}
      />

      {/* ── Top HUD ────────────────────────────────────────────────────── */}
      <SafeAreaView style={styles.topHud} pointerEvents="box-none">
        <View style={styles.topRow}>
          {/* GPS status chip */}
          <View style={[styles.chip, { borderColor: gpsStatusColor() }]}>
            <View style={[styles.chipDot, { backgroundColor: gpsStatusColor() }]} />
            <Text style={styles.chipText}>{gpsStatusLabel()}</Text>
          </View>

          <View style={{ flex: 1 }} />

          {/* Compass */}
          <CompassIndicator heading={heading} />
        </View>

        {/* Uncalibrated warning banner */}
        {isUncalibrated && (
          <TouchableOpacity
            style={styles.calibrationBanner}
            onPress={() => navigation.navigate('Calibration')}
          >
            <Ionicons name="warning-outline" size={16} color="#FFB300" />
            <Text style={styles.calibrationBannerText}>
              Map uses placeholder GPS — tap to calibrate
            </Text>
          </TouchableOpacity>
        )}
      </SafeAreaView>

      {/* ── Bottom FABs ────────────────────────────────────────────────── */}
      <SafeAreaView style={styles.bottomHud} pointerEvents="box-none">
        <View style={styles.fabRow}>
          {/* Toggle POI markers */}
          <TouchableOpacity
            style={styles.fab}
            onPress={() => setShowPOIs((v) => !v)}
            activeOpacity={0.8}
          >
            <Ionicons
              name={showPOIs ? 'location' : 'location-outline'}
              size={22}
              color={showPOIs ? '#FFB300' : '#BDBDBD'}
            />
          </TouchableOpacity>

          {/* Calibrate shortcut */}
          <TouchableOpacity
            style={styles.fab}
            onPress={() => navigation.navigate('Calibration')}
            activeOpacity={0.8}
          >
            <Ionicons name="options-outline" size={22} color="#BDBDBD" />
          </TouchableOpacity>
        </View>
      </SafeAreaView>
    </View>
  );
}

const styles = StyleSheet.create({
  root: {
    flex: 1,
    backgroundColor: '#111',
  },
  topHud: {
    position: 'absolute',
    top: 0,
    left: 0,
    right: 0,
  },
  topRow: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: 16,
    paddingTop: 8,
    gap: 8,
  },
  chip: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 6,
    backgroundColor: 'rgba(20,20,20,0.8)',
    borderRadius: 20,
    borderWidth: 1,
    paddingHorizontal: 10,
    paddingVertical: 5,
  },
  chipDot: {
    width: 8,
    height: 8,
    borderRadius: 4,
  },
  chipText: {
    color: '#EEE',
    fontSize: 12,
    fontWeight: '600',
  },
  calibrationBanner: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 6,
    marginHorizontal: 16,
    marginTop: 8,
    backgroundColor: 'rgba(30,20,0,0.88)',
    borderRadius: 8,
    borderWidth: 1,
    borderColor: '#FFB300',
    paddingVertical: 8,
    paddingHorizontal: 12,
  },
  calibrationBannerText: {
    color: '#FFB300',
    fontSize: 12,
    fontWeight: '600',
    flex: 1,
  },
  bottomHud: {
    position: 'absolute',
    bottom: 0,
    right: 0,
  },
  fabRow: {
    flexDirection: 'column',
    gap: 12,
    paddingRight: 16,
    paddingBottom: 16,
    alignItems: 'flex-end',
  },
  fab: {
    width: 48,
    height: 48,
    borderRadius: 24,
    backgroundColor: 'rgba(20,20,20,0.88)',
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.15)',
    alignItems: 'center',
    justifyContent: 'center',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.5,
    shadowRadius: 4,
    elevation: 6,
  },
});
