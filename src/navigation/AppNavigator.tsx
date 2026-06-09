import React from 'react';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import { Ionicons } from '@expo/vector-icons';
import { MapScreen } from '../screens/MapScreen';
import { LegendScreen } from '../screens/LegendScreen';
import { CalibrationScreen } from '../screens/CalibrationScreen';

const Tab = createBottomTabNavigator();

export function AppNavigator() {
  return (
    <Tab.Navigator
      screenOptions={({ route }) => ({
        headerShown: false,
        tabBarStyle: {
          backgroundColor: '#141414',
          borderTopColor: '#222',
          height: 60,
          paddingBottom: 8,
        },
        tabBarActiveTintColor: '#FFB300',
        tabBarInactiveTintColor: '#666',
        tabBarLabelStyle: { fontSize: 11, fontWeight: '600' },
        tabBarIcon: ({ focused, color, size }) => {
          let iconName: React.ComponentProps<typeof Ionicons>['name'];
          if (route.name === 'Map') {
            iconName = focused ? 'map' : 'map-outline';
          } else if (route.name === 'Legend') {
            iconName = focused ? 'list' : 'list-outline';
          } else {
            iconName = focused ? 'options' : 'options-outline';
          }
          return <Ionicons name={iconName} size={size} color={color} />;
        },
      })}
    >
      <Tab.Screen
        name="Map"
        component={MapScreen}
        options={{ title: 'Map' }}
      />
      <Tab.Screen
        name="Legend"
        component={LegendScreen}
        options={{ title: 'Legend' }}
      />
      <Tab.Screen
        name="Calibration"
        component={CalibrationScreen}
        options={{ title: 'Calibrate' }}
      />
    </Tab.Navigator>
  );
}
