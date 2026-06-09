import { useState, useEffect, useCallback } from 'react';
import * as Location from 'expo-location';
import { UserLocation } from '../types';

export type LocationStatus =
  | 'idle'
  | 'requesting'
  | 'denied'
  | 'active'
  | 'error';

export function useLocation() {
  const [location, setLocation] = useState<UserLocation | null>(null);
  const [status, setStatus] = useState<LocationStatus>('idle');

  const start = useCallback(async () => {
    setStatus('requesting');

    const { status: perm } = await Location.requestForegroundPermissionsAsync();
    if (perm !== 'granted') {
      setStatus('denied');
      return;
    }

    setStatus('active');

    // Subscribe to continuous location updates
    const sub = await Location.watchPositionAsync(
      {
        accuracy: Location.Accuracy.BestForNavigation,
        timeInterval: 1000,
        distanceInterval: 1,
      },
      (loc) => {
        setLocation({
          lat: loc.coords.latitude,
          lon: loc.coords.longitude,
          accuracy: loc.coords.accuracy,
          heading: loc.coords.heading,
        });
      },
    );

    return () => sub.remove();
  }, []);

  useEffect(() => {
    let cleanup: (() => void) | undefined;
    start().then((fn) => { cleanup = fn; });
    return () => { cleanup?.(); };
  }, [start]);

  return { location, status };
}
