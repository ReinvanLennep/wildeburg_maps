import AsyncStorage from '@react-native-async-storage/async-storage';
import { CalibrationData } from '../types';

const CALIBRATION_KEY = '@wildeburg_calibration';

export async function loadCalibration(): Promise<CalibrationData | null> {
  try {
    const raw = await AsyncStorage.getItem(CALIBRATION_KEY);
    return raw ? (JSON.parse(raw) as CalibrationData) : null;
  } catch {
    return null;
  }
}

export async function saveCalibration(data: CalibrationData): Promise<void> {
  await AsyncStorage.setItem(CALIBRATION_KEY, JSON.stringify(data));
}

export async function clearCalibration(): Promise<void> {
  await AsyncStorage.removeItem(CALIBRATION_KEY);
}
