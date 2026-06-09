export interface GpsCoords {
  lat: number;
  lon: number;
}

export interface ImageRelPos {
  // Relative position on the festival map image (0–1 range)
  x: number;
  y: number;
}

export interface ControlPoint {
  id: string;
  label: string;
  gps: GpsCoords;
  imagePos: ImageRelPos;
}

export interface CalibrationData {
  points: ControlPoint[];
  createdAt: number;
}

export type POICategory =
  | 'stage'
  | 'camping'
  | 'food'
  | 'medical'
  | 'water'
  | 'nature'
  | 'activity'
  | 'facility'
  | 'entrance';

export interface POI {
  id: string;
  name: string;
  category: POICategory;
  imagePos: ImageRelPos;
  // Real GPS coordinates derived from satellite image analysis.
  // Accuracy ≈ 50–150 m; improves after on-site calibration.
  gps: GpsCoords;
  description?: string;
}

export interface UserLocation {
  lat: number;
  lon: number;
  accuracy: number | null;
  heading: number | null;
}

export interface MapTransform {
  scale: number;
  translateX: number;
  translateY: number;
}
