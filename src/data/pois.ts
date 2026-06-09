import { POI } from '../types';

// ─────────────────────────────────────────────────────────────────────────────
// All festival locations for Wildeburg @ Netl de Wildste Tuin, Kraggenburg.
//
// imagePos  — relative position (0–1) on the stylised festival map image
// gps       — derived from pixel analysis of the annotated satellite screenshot
//             Accuracy ≈ 50–150 m.
// ─────────────────────────────────────────────────────────────────────────────

export const POIS: POI[] = [
  // ── Stages & Venues ─────────────────────────────────────────────────────
  {
    id: 'wildlive',
    name: 'Wildlive',
    category: 'stage',
    imagePos: { x: 0.19, y: 0.32 },
    gps: { lat: 52.6878, lon: 5.8611 },
    description: 'Main stage — live music',
  },
  {
    id: 'de-spot',
    name: 'De Spot',
    category: 'stage',
    imagePos: { x: 0.14, y: 0.21 },
    gps: { lat: 52.6893, lon: 5.8608 },
    description: 'Electronic & DJ stage',
  },
  {
    id: 'radio-koperen-hond',
    name: 'Radio de Koperen Hond',
    category: 'stage',
    imagePos: { x: 0.43, y: 0.33 },
    gps: { lat: 52.6877, lon: 5.8635 },
    description: 'Stage & radio broadcast',
  },
  {
    id: 'bud-kas',
    name: 'Bud Kas',
    category: 'stage',
    imagePos: { x: 0.50, y: 0.42 },
    gps: { lat: 52.6854, lon: 5.8660 },
    description: 'Bar & stage',
  },
  {
    id: 'lod',
    name: 'LOD',
    category: 'stage',
    imagePos: { x: 0.59, y: 0.46 },
    gps: { lat: 52.6847, lon: 5.8675 },
    description: 'Stage',
  },
  {
    id: 'bamboe-bios',
    name: 'Bamboe Bios',
    category: 'activity',
    imagePos: { x: 0.31, y: 0.67 },
    gps: { lat: 52.6821, lon: 5.8659 },
    description: 'Outdoor cinema in the bamboo',
  },

  // ── Areas & Activities ───────────────────────────────────────────────────
  {
    id: 'strand',
    name: 'Strand',
    category: 'water',
    imagePos: { x: 0.12, y: 0.40 },
    gps: { lat: 52.6863, lon: 5.8596 },
    description: 'Beach on the lake',
  },
  {
    id: 'de-baan',
    name: 'De Baan',
    category: 'activity',
    imagePos: { x: 0.09, y: 0.46 },
    gps: { lat: 52.6867, lon: 5.8594 },
    description: 'Activity track',
  },
  {
    id: 'bamboe-bos',
    name: 'Bamboe Bos',
    category: 'nature',
    imagePos: { x: 0.46, y: 0.52 },
    gps: { lat: 52.6845, lon: 5.8671 },
    description: 'Bamboo forest',
  },
  {
    id: 'helling',
    name: 'Helling',
    category: 'nature',
    imagePos: { x: 0.12, y: 0.53 },
    gps: { lat: 52.6847, lon: 5.8606 },
    description: 'The slope',
  },
  {
    id: 'duin-pan',
    name: 'Duin/Pan',
    category: 'nature',
    imagePos: { x: 0.36, y: 0.57 },
    gps: { lat: 52.6836, lon: 5.8659 },
    description: 'Dune & pan area',
  },
  {
    id: 'achter-lijn',
    name: 'Achter Lijn',
    category: 'nature',
    imagePos: { x: 0.11, y: 0.63 },
    gps: { lat: 52.6823, lon: 5.8592 },
    description: 'Back line area',
  },
  {
    id: 'vuur-toren-strand',
    name: 'Vuur Toren Strand',
    category: 'water',
    imagePos: { x: 0.75, y: 0.66 },
    gps: { lat: 52.6814, lon: 5.8745 },
    description: 'Fire tower beach',
  },

  // ── Services & Facilities ────────────────────────────────────────────────
  {
    id: 'ehbo',
    name: 'EHBO',
    category: 'medical',
    imagePos: { x: 0.18, y: 0.17 },
    gps: { lat: 52.6917, lon: 5.8617 },
    description: 'First aid post',
  },
  {
    id: 'camping-winkel',
    name: 'Camping Winkel',
    category: 'facility',
    imagePos: { x: 0.24, y: 0.13 },
    gps: { lat: 52.6922, lon: 5.8625 },
    description: 'Camping store — gas, supplies',
  },
  {
    id: 'dorps-tafel',
    name: 'Dorps Tafel',
    category: 'food',
    imagePos: { x: 0.38, y: 0.22 },
    gps: { lat: 52.6904, lon: 5.8651 },
    description: 'Village table — communal dining',
  },
  {
    id: 'hourtentien-1',
    name: 'Hourtentien',
    category: 'facility',
    imagePos: { x: 0.74, y: 0.28 },
    gps: { lat: 52.6895, lon: 5.8800 },
    description: 'Showers & toilets',
  },
  {
    id: 'hourtentien-2',
    name: 'Hourtentien',
    category: 'facility',
    imagePos: { x: 0.78, y: 0.43 },
    gps: { lat: 52.6867, lon: 5.8813 },
    description: 'Showers & toilets',
  },
  {
    id: 'hourtentien-3',
    name: 'Hourtentien',
    category: 'facility',
    imagePos: { x: 0.69, y: 0.60 },
    gps: { lat: 52.6833, lon: 5.8784 },
    description: 'Showers & toilets',
  },

  // ── Camping ──────────────────────────────────────────────────────────────
  {
    id: 'camping-1',
    name: 'Camping 1',
    category: 'camping',
    imagePos: { x: 0.47, y: 0.18 },
    gps: { lat: 52.6902, lon: 5.8714 },
    description: 'Camping zone 1',
  },
  {
    id: 'camping-2',
    name: 'Camping 2',
    category: 'camping',
    imagePos: { x: 0.72, y: 0.37 },
    gps: { lat: 52.6867, lon: 5.8777 },
    description: 'Camping zone 2',
  },
  {
    id: 'camping-4',
    name: 'Camping 4',
    category: 'camping',
    imagePos: { x: 0.21, y: 0.73 },
    gps: { lat: 52.6812, lon: 5.8639 },
    description: 'Camping zone 4',
  },
  {
    id: 'camping-5',
    name: 'Camping 5',
    category: 'camping',
    imagePos: { x: 0.37, y: 0.78 },
    gps: { lat: 52.6800, lon: 5.8668 },
    description: 'Camping zone 5',
  },
];

export const POI_CATEGORY_COLORS: Record<string, string> = {
  stage: '#FF6B35',
  camping: '#4CAF50',
  food: '#FFB300',
  medical: '#F44336',
  water: '#29B6F6',
  nature: '#66BB6A',
  activity: '#AB47BC',
  facility: '#78909C',
  entrance: '#FFFFFF',
};

export const POI_CATEGORY_ICONS: Record<string, string> = {
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
