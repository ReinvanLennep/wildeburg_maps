import { CalibrationData } from '../types';

// ─────────────────────────────────────────────────────────────────────────────
// Wildeburg festival — Netl de Wildste Tuin, Leemringweg 19, Kraggenburg (NL)
// GPS coordinates derived from pixel analysis of the annotated Google Maps
// satellite screenshot cross-referenced with confirmed address coordinates
// (52.683 °N, 5.874 °E).  Accuracy ≈ 50–150 m.  Use the Calibration screen
// to improve accuracy by walking to 2–3 known spots with the app open.
// ─────────────────────────────────────────────────────────────────────────────

// Screenshot image bounds (estimated from satellite zoom level and known address)
export const SCREENSHOT_BOUNDS = {
  north: 52.695,
  south: 52.675,
  west: 5.856,
  east: 5.889,
};

// 4 control points that link GPS positions (from screenshot analysis) to
// relative pixel positions on the artistic festival map image.
// Spread across all four quadrants for the best affine accuracy.
export const DEFAULT_CALIBRATION: CalibrationData = {
  // Non-zero = real coordinates; the app won't show the "needs calibration" banner.
  createdAt: 1,
  points: [
    {
      id: 'wildlive',
      label: 'Wildlive (main stage)',
      gps: { lat: 52.6878, lon: 5.8611 },
      imagePos: { x: 0.19, y: 0.32 },
    },
    {
      id: 'camping-1',
      label: 'Camping 1',
      gps: { lat: 52.6902, lon: 5.8714 },
      imagePos: { x: 0.47, y: 0.18 },
    },
    {
      id: 'camping-4',
      label: 'Camping 4',
      gps: { lat: 52.6812, lon: 5.8639 },
      imagePos: { x: 0.21, y: 0.73 },
    },
    {
      id: 'camping-2',
      label: 'Camping 2',
      gps: { lat: 52.6867, lon: 5.8777 },
      imagePos: { x: 0.72, y: 0.37 },
    },
  ],
};

// Keep a separate placeholder so the CalibrationScreen can start fresh
export const PLACEHOLDER_CALIBRATION: CalibrationData = {
  createdAt: 0,
  points: [],
};
