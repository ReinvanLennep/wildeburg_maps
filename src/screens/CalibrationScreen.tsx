import React, { useCallback, useEffect, useState } from 'react';
import {
  Alert,
  Image,
  LayoutChangeEvent,
  SafeAreaView,
  ScrollView,
  StyleSheet,
  Text,
  TextInput,
  TouchableOpacity,
  View,
} from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { ControlPoint, CalibrationData, ImageRelPos } from '../types';
import { saveCalibration, clearCalibration, loadCalibration } from '../utils/storage';
import { useLocation } from '../hooks/useLocation';
import { buildTransform } from '../utils/geoTransform';

const festivalMapImage = require('../../assets/images/festival-map.png');

interface Props {
  navigation: any;
}

type Step = 'list' | 'tap-map' | 'enter-gps';

export function CalibrationScreen({ navigation }: Props) {
  const { location } = useLocation();
  const [calibration, setCalibration] = useState<CalibrationData>({
    points: [],
    createdAt: 0,
  });
  const [step, setStep] = useState<Step>('list');
  const [pendingImagePos, setPendingImagePos] = useState<ImageRelPos | null>(null);
  const [pendingLabel, setPendingLabel] = useState('');
  const [pendingLat, setPendingLat] = useState('');
  const [pendingLon, setPendingLon] = useState('');

  // Image display size for tap calculation
  const [containerSize, setContainerSize] = useState({ width: 0, height: 0 });

  useEffect(() => {
    loadCalibration().then((saved) => {
      if (saved) setCalibration(saved);
    });
  }, []);

  const transform = buildTransform(calibration.points);
  const canSave = calibration.points.length >= 2;

  // ── Tap on the map to pick a pixel position ───────────────────────────────
  const handleMapTap = useCallback(
    (evt: any) => {
      if (step !== 'tap-map') return;
      const tapX = evt.nativeEvent.locationX;
      const tapY = evt.nativeEvent.locationY;

      // The image fills the container with "contain" mode. Calculate offsets.
      const imgAspect = 508 / 870;
      const contAspect = containerSize.width / containerSize.height;
      let iw: number, ih: number, offX: number, offY: number;
      if (imgAspect > contAspect) {
        iw = containerSize.width;
        ih = containerSize.width / imgAspect;
        offX = 0;
        offY = (containerSize.height - ih) / 2;
      } else {
        ih = containerSize.height;
        iw = containerSize.height * imgAspect;
        offX = (containerSize.width - iw) / 2;
        offY = 0;
      }

      const relX = (tapX - offX) / iw;
      const relY = (tapY - offY) / ih;

      if (relX < 0 || relX > 1 || relY < 0 || relY > 1) return;
      setPendingImagePos({ x: relX, y: relY });
      setStep('enter-gps');
    },
    [step, containerSize],
  );

  // ── Confirm a control point ───────────────────────────────────────────────
  const confirmPoint = useCallback(() => {
    if (!pendingImagePos) return;
    const lat = parseFloat(pendingLat);
    const lon = parseFloat(pendingLon);
    if (isNaN(lat) || isNaN(lon)) {
      Alert.alert('Invalid coordinates', 'Enter valid decimal GPS coordinates.');
      return;
    }
    const newPoint: ControlPoint = {
      id: Date.now().toString(),
      label: pendingLabel || `Point ${calibration.points.length + 1}`,
      gps: { lat, lon },
      imagePos: pendingImagePos,
    };
    setCalibration((prev) => ({ ...prev, points: [...prev.points, newPoint] }));
    setPendingImagePos(null);
    setPendingLabel('');
    setPendingLat('');
    setPendingLon('');
    setStep('list');
  }, [pendingImagePos, pendingLabel, pendingLat, pendingLon, calibration.points.length]);

  // ── Use current GPS for the point ─────────────────────────────────────────
  const useCurrentGps = useCallback(() => {
    if (!location) return;
    setPendingLat(location.lat.toFixed(7));
    setPendingLon(location.lon.toFixed(7));
  }, [location]);

  // ── Save calibration ──────────────────────────────────────────────────────
  const handleSave = useCallback(async () => {
    const updated: CalibrationData = { ...calibration, createdAt: Date.now() };
    await saveCalibration(updated);
    Alert.alert('Saved', 'Calibration saved. Your location will now appear on the map.', [
      { text: 'OK', onPress: () => navigation.goBack() },
    ]);
  }, [calibration, navigation]);

  // ── Reset ─────────────────────────────────────────────────────────────────
  const handleReset = useCallback(() => {
    Alert.alert('Reset calibration?', 'All calibration points will be deleted.', [
      { text: 'Cancel', style: 'cancel' },
      {
        text: 'Reset',
        style: 'destructive',
        onPress: async () => {
          await clearCalibration();
          setCalibration({ points: [], createdAt: 0 });
        },
      },
    ]);
  }, []);

  // ─── Render ───────────────────────────────────────────────────────────────

  if (step === 'tap-map') {
    return (
      <View style={styles.root}>
        <SafeAreaView>
          <View style={styles.tapHeader}>
            <TouchableOpacity onPress={() => setStep('list')}>
              <Ionicons name="close" size={28} color="#FFF" />
            </TouchableOpacity>
            <Text style={styles.tapInstruction}>
              Tap the exact location on the map
            </Text>
          </View>
        </SafeAreaView>

        <View
          style={styles.tapMapContainer}
          onLayout={(e: LayoutChangeEvent) => {
            const { width, height } = e.nativeEvent.layout;
            setContainerSize({ width, height });
          }}
        >
          <Image
            source={festivalMapImage}
            style={StyleSheet.absoluteFill}
            resizeMode="contain"
            onTouchEnd={handleMapTap}
          />
        </View>
      </View>
    );
  }

  if (step === 'enter-gps') {
    return (
      <SafeAreaView style={styles.root}>
        <ScrollView contentContainerStyle={styles.formContainer}>
          <Text style={styles.formTitle}>Enter GPS for this point</Text>

          <Text style={styles.fieldLabel}>Label (optional)</Text>
          <TextInput
            style={styles.input}
            placeholder="e.g. Main stage entrance"
            placeholderTextColor="#666"
            value={pendingLabel}
            onChangeText={setPendingLabel}
          />

          <Text style={styles.fieldLabel}>Latitude</Text>
          <TextInput
            style={styles.input}
            placeholder="e.g. 51.7583"
            placeholderTextColor="#666"
            keyboardType="decimal-pad"
            value={pendingLat}
            onChangeText={setPendingLat}
          />

          <Text style={styles.fieldLabel}>Longitude</Text>
          <TextInput
            style={styles.input}
            placeholder="e.g. 5.2341"
            placeholderTextColor="#666"
            keyboardType="decimal-pad"
            value={pendingLon}
            onChangeText={setPendingLon}
          />

          {location && (
            <TouchableOpacity style={styles.useGpsBtn} onPress={useCurrentGps}>
              <Ionicons name="navigate" size={16} color="#2979FF" />
              <Text style={styles.useGpsText}>
                Use my current GPS ({location.lat.toFixed(5)}, {location.lon.toFixed(5)})
              </Text>
            </TouchableOpacity>
          )}

          <Text style={styles.tipText}>
            Tip: Walk to this exact spot on the festival grounds and press "Use my current GPS".
          </Text>

          <View style={styles.formButtons}>
            <TouchableOpacity style={styles.cancelBtn} onPress={() => setStep('tap-map')}>
              <Text style={styles.cancelBtnText}>Back</Text>
            </TouchableOpacity>
            <TouchableOpacity style={styles.confirmBtn} onPress={confirmPoint}>
              <Text style={styles.confirmBtnText}>Add point</Text>
            </TouchableOpacity>
          </View>
        </ScrollView>
      </SafeAreaView>
    );
  }

  // ── List view ─────────────────────────────────────────────────────────────
  return (
    <SafeAreaView style={styles.root}>
      <ScrollView contentContainerStyle={styles.listContainer}>
        <Text style={styles.title}>Map Calibration</Text>
        <Text style={styles.subtitle}>
          Add at least 2 reference points — tap a location on the festival map, then
          enter (or walk to) its real GPS coordinates.
        </Text>

        {calibration.points.length === 0 ? (
          <View style={styles.emptyState}>
            <Ionicons name="map-outline" size={48} color="#555" />
            <Text style={styles.emptyText}>No calibration points yet</Text>
          </View>
        ) : (
          calibration.points.map((pt, i) => (
            <View key={pt.id} style={styles.pointRow}>
              <View style={styles.pointIndex}>
                <Text style={styles.pointIndexText}>{i + 1}</Text>
              </View>
              <View style={styles.pointInfo}>
                <Text style={styles.pointLabel}>{pt.label}</Text>
                <Text style={styles.pointCoords}>
                  {pt.gps.lat.toFixed(6)}, {pt.gps.lon.toFixed(6)}
                </Text>
              </View>
              <TouchableOpacity
                onPress={() =>
                  setCalibration((prev) => ({
                    ...prev,
                    points: prev.points.filter((p) => p.id !== pt.id),
                  }))
                }
              >
                <Ionicons name="trash-outline" size={20} color="#F44336" />
              </TouchableOpacity>
            </View>
          ))
        )}

        <TouchableOpacity
          style={styles.addBtn}
          onPress={() => setStep('tap-map')}
        >
          <Ionicons name="add-circle-outline" size={20} color="#FFF" />
          <Text style={styles.addBtnText}>Add calibration point</Text>
        </TouchableOpacity>

        {transform && (
          <View style={styles.statusGood}>
            <Ionicons name="checkmark-circle" size={18} color="#4CAF50" />
            <Text style={styles.statusGoodText}>
              Transform ready ({calibration.points.length} points)
            </Text>
          </View>
        )}

        <View style={styles.bottomButtons}>
          {calibration.points.length > 0 && (
            <TouchableOpacity style={styles.resetBtn} onPress={handleReset}>
              <Text style={styles.resetBtnText}>Reset all</Text>
            </TouchableOpacity>
          )}
          {canSave && (
            <TouchableOpacity style={styles.saveBtn} onPress={handleSave}>
              <Text style={styles.saveBtnText}>Save calibration</Text>
            </TouchableOpacity>
          )}
        </View>
      </ScrollView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  root: { flex: 1, backgroundColor: '#111' },
  // Tap-map view
  tapHeader: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 12,
    paddingHorizontal: 16,
    paddingVertical: 12,
    backgroundColor: 'rgba(20,20,20,0.95)',
  },
  tapInstruction: { color: '#EEE', fontSize: 14, fontWeight: '600', flex: 1 },
  tapMapContainer: { flex: 1, backgroundColor: '#000' },
  // Form view
  formContainer: { padding: 24, gap: 8 },
  formTitle: { color: '#FFF', fontSize: 20, fontWeight: '700', marginBottom: 8 },
  fieldLabel: { color: '#AAA', fontSize: 13, marginTop: 10 },
  input: {
    backgroundColor: '#222',
    borderRadius: 8,
    borderWidth: 1,
    borderColor: '#333',
    color: '#FFF',
    paddingHorizontal: 14,
    paddingVertical: 11,
    fontSize: 15,
    marginTop: 4,
  },
  useGpsBtn: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 8,
    paddingVertical: 10,
    marginTop: 4,
  },
  useGpsText: { color: '#2979FF', fontSize: 13, fontWeight: '600' },
  tipText: { color: '#777', fontSize: 12, lineHeight: 18, marginTop: 8 },
  formButtons: { flexDirection: 'row', gap: 12, marginTop: 24 },
  cancelBtn: {
    flex: 1,
    paddingVertical: 14,
    borderRadius: 10,
    backgroundColor: '#222',
    alignItems: 'center',
  },
  cancelBtnText: { color: '#AAA', fontWeight: '600', fontSize: 15 },
  confirmBtn: {
    flex: 1,
    paddingVertical: 14,
    borderRadius: 10,
    backgroundColor: '#2979FF',
    alignItems: 'center',
  },
  confirmBtnText: { color: '#FFF', fontWeight: '700', fontSize: 15 },
  // List view
  listContainer: { padding: 20, gap: 12 },
  title: { color: '#FFF', fontSize: 24, fontWeight: '800' },
  subtitle: { color: '#999', fontSize: 14, lineHeight: 21, marginTop: 4 },
  emptyState: { alignItems: 'center', paddingVertical: 40, gap: 12 },
  emptyText: { color: '#555', fontSize: 15 },
  pointRow: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: '#1C1C1C',
    borderRadius: 10,
    padding: 12,
    gap: 12,
  },
  pointIndex: {
    width: 28,
    height: 28,
    borderRadius: 14,
    backgroundColor: '#2979FF',
    alignItems: 'center',
    justifyContent: 'center',
  },
  pointIndexText: { color: '#FFF', fontWeight: '700', fontSize: 13 },
  pointInfo: { flex: 1 },
  pointLabel: { color: '#FFF', fontWeight: '600', fontSize: 14 },
  pointCoords: { color: '#888', fontSize: 12, marginTop: 2, fontFamily: 'monospace' },
  addBtn: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 10,
    backgroundColor: '#222',
    borderRadius: 10,
    borderWidth: 1,
    borderColor: '#333',
    borderStyle: 'dashed',
    paddingVertical: 14,
    paddingHorizontal: 16,
    justifyContent: 'center',
  },
  addBtnText: { color: '#FFF', fontWeight: '600', fontSize: 15 },
  statusGood: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 8,
    backgroundColor: 'rgba(76,175,80,0.1)',
    borderRadius: 8,
    borderWidth: 1,
    borderColor: 'rgba(76,175,80,0.35)',
    paddingVertical: 8,
    paddingHorizontal: 12,
  },
  statusGoodText: { color: '#4CAF50', fontSize: 13, fontWeight: '600' },
  bottomButtons: {
    flexDirection: 'row',
    gap: 12,
    marginTop: 12,
  },
  resetBtn: {
    flex: 1,
    paddingVertical: 14,
    borderRadius: 10,
    backgroundColor: '#1C1C1C',
    alignItems: 'center',
    borderWidth: 1,
    borderColor: '#333',
  },
  resetBtnText: { color: '#F44336', fontWeight: '600', fontSize: 15 },
  saveBtn: {
    flex: 2,
    paddingVertical: 14,
    borderRadius: 10,
    backgroundColor: '#2979FF',
    alignItems: 'center',
  },
  saveBtnText: { color: '#FFF', fontWeight: '700', fontSize: 15 },
});
