import { POI } from '../types';

// Image-relative positions derived from the festival map image.
// x=0 is left edge, x=1 is right edge; y=0 is top, y=1 is bottom.
// These positions match the stylised Wildeburg map image.
export const POIS: POI[] = [
  // ── Stages & Venues ─────────────────────────────────────────────
  {
    id: 'wildlive',
    name: 'Wildlive',
    category: 'stage',
    imagePos: { x: 0.19, y: 0.32 },
    description: 'Main stage – live music',
  },
  {
    id: 'de-spot',
    name: 'De Spot',
    category: 'stage',
    imagePos: { x: 0.14, y: 0.21 },
    description: 'Electronic & DJ stage',
  },
  {
    id: 'radio-koperen-hond',
    name: 'Radio de Koperen Hond',
    category: 'stage',
    imagePos: { x: 0.43, y: 0.33 },
    description: 'Stage & radio broadcast',
  },
  {
    id: 'bud-kas',
    name: 'Bud Kas',
    category: 'stage',
    imagePos: { x: 0.50, y: 0.42 },
    description: 'Bar & stage',
  },
  {
    id: 'lod',
    name: 'LOD',
    category: 'stage',
    imagePos: { x: 0.59, y: 0.46 },
    description: 'Stage',
  },
  {
    id: 'bamboe-bios',
    name: 'Bamboe Bios',
    category: 'activity',
    imagePos: { x: 0.31, y: 0.67 },
    description: 'Outdoor cinema in the bamboo',
  },

  // ── Areas & Activities ───────────────────────────────────────────
  {
    id: 'strand',
    name: 'Strand',
    category: 'water',
    imagePos: { x: 0.12, y: 0.40 },
    description: 'Beach area',
  },
  {
    id: 'de-baan',
    name: 'De Baan',
    category: 'activity',
    imagePos: { x: 0.09, y: 0.46 },
    description: 'Activity track',
  },
  {
    id: 'bamboe-bos',
    name: 'Bamboe Bos',
    category: 'nature',
    imagePos: { x: 0.46, y: 0.52 },
    description: 'Bamboo forest',
  },
  {
    id: 'helling',
    name: 'Helling',
    category: 'nature',
    imagePos: { x: 0.12, y: 0.53 },
    description: 'The slope',
  },
  {
    id: 'duin-pan',
    name: 'Duin/Pan',
    category: 'nature',
    imagePos: { x: 0.36, y: 0.57 },
    description: 'Dune area',
  },
  {
    id: 'achter-lijn',
    name: 'Achter Lijn',
    category: 'nature',
    imagePos: { x: 0.11, y: 0.63 },
    description: 'Back line area',
  },
  {
    id: 'vuur-toren-strand',
    name: 'Vuur Toren Strand',
    category: 'water',
    imagePos: { x: 0.75, y: 0.66 },
    description: 'Fire tower beach',
  },

  // ── Services & Facilities ────────────────────────────────────────
  {
    id: 'ehbo',
    name: 'EHBO',
    category: 'medical',
    imagePos: { x: 0.18, y: 0.17 },
    description: 'First aid post',
  },
  {
    id: 'camping-winkel',
    name: 'Camping Winkel',
    category: 'facility',
    imagePos: { x: 0.24, y: 0.13 },
    description: 'Camping store – gas, supplies',
  },
  {
    id: 'dorps-tafel',
    name: 'Dorps Tafel',
    category: 'food',
    imagePos: { x: 0.38, y: 0.22 },
    description: 'Village table – communal dining',
  },
  {
    id: 'hourtentien-1',
    name: 'Hourtentien',
    category: 'facility',
    imagePos: { x: 0.74, y: 0.28 },
    description: 'Showers & toilets',
  },
  {
    id: 'hourtentien-2',
    name: 'Hourtentien',
    category: 'facility',
    imagePos: { x: 0.78, y: 0.43 },
    description: 'Showers & toilets',
  },
  {
    id: 'hourtentien-3',
    name: 'Hourtentien',
    category: 'facility',
    imagePos: { x: 0.69, y: 0.60 },
    description: 'Showers & toilets',
  },

  // ── Camping ──────────────────────────────────────────────────────
  {
    id: 'camping-1',
    name: 'Camping 1',
    category: 'camping',
    imagePos: { x: 0.47, y: 0.18 },
    description: 'Camping zone 1',
  },
  {
    id: 'camping-2',
    name: 'Camping 2',
    category: 'camping',
    imagePos: { x: 0.72, y: 0.37 },
    description: 'Camping zone 2',
  },
  {
    id: 'camping-4',
    name: 'Camping 4',
    category: 'camping',
    imagePos: { x: 0.21, y: 0.73 },
    description: 'Camping zone 4',
  },
  {
    id: 'camping-5',
    name: 'Camping 5',
    category: 'camping',
    imagePos: { x: 0.37, y: 0.78 },
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
