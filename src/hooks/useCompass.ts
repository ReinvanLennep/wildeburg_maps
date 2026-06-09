import { useState, useEffect, useRef } from 'react';
import { Magnetometer } from 'expo-sensors';

export function useCompass() {
  const [heading, setHeading] = useState<number | null>(null);
  const subRef = useRef<ReturnType<typeof Magnetometer.addListener> | null>(null);

  useEffect(() => {
    Magnetometer.setUpdateInterval(200);

    subRef.current = Magnetometer.addListener(({ x, y }) => {
      // Convert magnetometer reading to compass heading (degrees from north)
      let angle = Math.atan2(y, x) * (180 / Math.PI);
      angle = (angle + 360) % 360;
      setHeading(Math.round(angle));
    });

    return () => subRef.current?.remove();
  }, []);

  return heading;
}
