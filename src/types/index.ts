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
