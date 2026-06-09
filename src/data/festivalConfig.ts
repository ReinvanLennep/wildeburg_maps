import { CalibrationData } from '../types';

// Approximate GPS bounding box for the Wildeburg festival terrain.
// !! These are PLACEHOLDER coordinates !!
// Use the Calibration screen to set real GPS reference points once on-site.
// The festival is somewhere in the Netherlands – replace with actual coords.
export const PLACEHOLDER_CALIBRATION: CalibrationData = {
  createdAt: 0, // 0 = not yet calibrated
  points: [
    {
      id: 'nw-corner',
      label: 'Top-left corner (NW)',
      gps: { lat: 51.760, lon: 5.228 },
      imagePos: { x: 0.05, y: 0.08 },
    },
    {
      id: 'se-corner',
      label: 'Bottom-right corner (SE)',
      gps: { lat: 51.749, lon: 5.245 },
      imagePos: { x: 0.92, y: 0.90 },
    },
  ],
};

// How many metres does one degree of latitude / longitude represent
// at 51.75 °N (approximate festival latitude)?
export const METERS_PER_DEG_LAT = 111_320;
export const METERS_PER_DEG_LON = 111_320 * Math.cos((51.75 * Math.PI) / 180);

// Natural image dimensions (pixels). Used for accuracy calculations.
export const MAP_IMAGE = {
  // These are updated at runtime via Image.getSize(); defaults keep
  // the aspect ratio consistent before the image loads.
  width: 508,
  height: 870,
};
