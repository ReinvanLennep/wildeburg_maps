import { ControlPoint, ImageRelPos, GpsCoords } from '../types';

// ─── Linear algebra helpers ───────────────────────────────────────────────────

function solve3x3(A: number[][], b: number[]): number[] | null {
  // Gaussian elimination with partial pivoting on a 3×3 augmented matrix.
  const n = 3;
  const m = A.map((row, i) => [...row, b[i]]);

  for (let col = 0; col < n; col++) {
    let maxRow = col;
    for (let row = col + 1; row < n; row++) {
      if (Math.abs(m[row][col]) > Math.abs(m[maxRow][col])) maxRow = row;
    }
    [m[col], m[maxRow]] = [m[maxRow], m[col]];
    if (Math.abs(m[col][col]) < 1e-12) return null;

    for (let row = col + 1; row < n; row++) {
      const factor = m[row][col] / m[col][col];
      for (let k = col; k <= n; k++) m[row][k] -= factor * m[col][k];
    }
  }

  const x = new Array(n).fill(0);
  for (let i = n - 1; i >= 0; i--) {
    x[i] = m[i][n];
    for (let j = i + 1; j < n; j++) x[i] -= m[i][j] * x[j];
    x[i] /= m[i][i];
  }
  return x;
}

// ─── Affine transform building ────────────────────────────────────────────────

export interface AffineTransform {
  // [relX] = [a b c] · [lon lat 1]ᵀ
  // [relY] = [d e f] · [lon lat 1]ᵀ
  a: number; b: number; c: number;
  d: number; e: number; f: number;
}

/**
 * Build an affine transform from control points.
 * Requires at least 2 points (uses simple scale+translate for exactly 2,
 * full affine for 3+).
 */
export function buildTransform(points: ControlPoint[]): AffineTransform | null {
  if (points.length < 2) return null;

  if (points.length === 2) {
    const [p1, p2] = points;
    const dLon = p2.gps.lon - p1.gps.lon;
    const dLat = p2.gps.lat - p1.gps.lat;
    if (Math.abs(dLon) < 1e-12 || Math.abs(dLat) < 1e-12) return null;

    const a = (p2.imagePos.x - p1.imagePos.x) / dLon;
    const d = (p2.imagePos.y - p1.imagePos.y) / dLat;
    return {
      a, b: 0, c: p1.imagePos.x - a * p1.gps.lon,
      d: 0, e: d, f: p1.imagePos.y - d * p1.gps.lat,
    };
  }

  // 3+ points – least-squares using the first three (simplification;
  // for more points extend with QR or SVD decomposition).
  const [p1, p2, p3] = points;
  const M = [
    [p1.gps.lon, p1.gps.lat, 1],
    [p2.gps.lon, p2.gps.lat, 1],
    [p3.gps.lon, p3.gps.lat, 1],
  ];
  const bx = [p1.imagePos.x, p2.imagePos.x, p3.imagePos.x];
  const by = [p1.imagePos.y, p2.imagePos.y, p3.imagePos.y];

  const xCoefs = solve3x3(M, bx);
  const yCoefs = solve3x3(M, by);
  if (!xCoefs || !yCoefs) return null;

  return {
    a: xCoefs[0], b: xCoefs[1], c: xCoefs[2],
    d: yCoefs[0], e: yCoefs[1], f: yCoefs[2],
  };
}

/** Convert GPS coords to relative image position (0–1 range). */
export function gpsToImagePos(
  coords: GpsCoords,
  transform: AffineTransform,
): ImageRelPos {
  return {
    x: transform.a * coords.lon + transform.b * coords.lat + transform.c,
    y: transform.d * coords.lon + transform.e * coords.lat + transform.f,
  };
}

/** Convert relative image position back to GPS coords (inverse). */
export function imagePosToGps(
  pos: ImageRelPos,
  transform: AffineTransform,
): GpsCoords | null {
  // Solve the 2×2 system:
  // a*lon + b*lat = relX - c
  // d*lon + e*lat = relY - f
  const det = transform.a * transform.e - transform.b * transform.d;
  if (Math.abs(det) < 1e-12) return null;

  const rx = pos.x - transform.c;
  const ry = pos.y - transform.f;
  return {
    lon: (transform.e * rx - transform.b * ry) / det,
    lat: (transform.a * ry - transform.d * rx) / det,
  };
}

/** Haversine distance between two GPS points in metres. */
export function haversineMeters(a: GpsCoords, b: GpsCoords): number {
  const R = 6_371_000;
  const dLat = ((b.lat - a.lat) * Math.PI) / 180;
  const dLon = ((b.lon - a.lon) * Math.PI) / 180;
  const sinLat = Math.sin(dLat / 2);
  const sinLon = Math.sin(dLon / 2);
  const h =
    sinLat * sinLat +
    Math.cos((a.lat * Math.PI) / 180) *
      Math.cos((b.lat * Math.PI) / 180) *
      sinLon * sinLon;
  return 2 * R * Math.asin(Math.sqrt(h));
}

/** GPS accuracy in metres → radius in image-relative units. */
export function accuracyToImageRadius(
  accuracyMeters: number,
  transform: AffineTransform,
  refPoint: GpsCoords,
): number {
  // Shift by ~accuracyMeters north and measure pixel distance.
  const shifted: GpsCoords = {
    lat: refPoint.lat + accuracyMeters / 111_320,
    lon: refPoint.lon,
  };
  const origin = gpsToImagePos(refPoint, transform);
  const dest = gpsToImagePos(shifted, transform);
  return Math.sqrt(
    Math.pow(dest.x - origin.x, 2) + Math.pow(dest.y - origin.y, 2),
  );
}
