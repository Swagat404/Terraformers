package com.terraformers.avatars;

import java.util.HashMap;

public class AvatarModel {
    private String avatarID;
    private double x, y, z; // Position in 3D space
    private double batteryLevel;
    private HashMap<String, Double> sensorData; // Stores sensor readings

    public AvatarModel(String id, double startX, double startY, double startZ) {
        this.avatarID = id;
        this.x = startX;
        this.y = startY;
        this.z = startZ;
        this.batteryLevel = 100.0; // Start fully charged
        this.sensorData = new HashMap<>();
    }

    public boolean move(double newX, double newY, double newZ) {
        if (batteryLevel > 0) {
            this.x = newX;
            this.y = newY;
            this.z = newZ;
            batteryLevel -= 5.0; // Battery drains on movement
            return true;
        }
        return false; // Movement fails if battery is depleted
    }

    public void updateSensor(String sensorType, double value) {
        sensorData.put(sensorType, value);
    }

    public void rechargeBattery() {
        batteryLevel = 100.0;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getZ() { return z; }
    public double getBatteryLevel() { return batteryLevel; }
    public HashMap<String, Double> getSensorData() { return sensorData; }
}
