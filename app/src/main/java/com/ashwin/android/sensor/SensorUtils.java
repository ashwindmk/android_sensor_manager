package com.ashwin.android.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.List;

public class SensorUtils {
    public static void logSensors(Context context) {
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> deviceSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        int i = 1;
        for (Sensor sensor : deviceSensors) {
            Log.d(Constant.APP_TAG, i + ": sensor: " + sensor);
            i++;
        }
    }

    public static void logSensorNames(Context context) {
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> deviceSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        int i = 1;
        for (Sensor sensor : deviceSensors) {
            Log.d(Constant.APP_TAG, i + ": sensor: " + sensor.getName());
            i++;
        }
    }

    public static boolean sensorAvailable(Context context, int sensorType) {
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        return sensorManager.getDefaultSensor(sensorType) != null;
    }

    public static String getSensorAttributes(Sensor sensor) {
//        return "name: " + sensor.getName() + '\n'
//                + "type: " + sensor.getType() + '\n'
//                + "version: " + sensor.getVersion() + '\n'
//                + "power-consumption: " + sensor.getPower() + " mA" + '\n'  // milliAmpere
//                + "vendor: " + sensor.getVendor();
        return sensor.toString();
    }

    public static void logSensorAttributes(Sensor sensor) {
//        Log.d(Constant.APP_TAG, "name: " + sensor.getName());
//        Log.d(Constant.APP_TAG, "type: " + sensor.getType());
//        Log.d(Constant.APP_TAG, "version: " + sensor.getVersion());
//        Log.d(Constant.APP_TAG, "power-consumption: " + sensor.getPower() + " mA");  // milliAmpere
//        Log.d(Constant.APP_TAG, "vendor: " + sensor.getVendor());
        Log.d(Constant.APP_TAG, getSensorAttributes(sensor));
    }
}
