/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smartcampus.data;

/**
 *
 * @author LENOVO
 */

import com.mycompany.smartcampus.models.Room;
import com.mycompany.smartcampus.models.Sensor;
import com.mycompany.smartcampus.models.SensorReading;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataStore {
    private static final Map<String, Room> rooms = new HashMap<>();
    private static final Map<String, Sensor> sensors = new HashMap<>();
    private static final Map<String, List<SensorReading>> sensorReadings = new HashMap<>();

    public static Map<String, Room> getRooms() {
        return rooms;
    }

    public static Map<String, Sensor> getSensors() {
        return sensors;
    }

    public static Map<String, List<SensorReading>> getSensorReadings() {
        return sensorReadings;
    }

    public static void addRoom(Room room) {
        rooms.put(room.getId(), room);
    }

    public static void removeRoom(String roomId) {
        rooms.remove(roomId);
    }

    public static void addSensor(Sensor sensor) {
        sensors.put(sensor.getId(), sensor);
    }

    public static void addSensorReading(String sensorId, SensorReading reading) {
        List<SensorReading> readings = sensorReadings.getOrDefault(sensorId, new ArrayList<>());
        readings.add(reading);
        sensorReadings.put(sensorId, readings);
    }
}
