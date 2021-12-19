package com.ashwin.android.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

import java.util.Arrays;

public class SensorListener implements SensorEventListener {
    private static final String SUB_TAG = SensorListener.class.getSimpleName();

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Log.d(Constant.APP_TAG, SUB_TAG + ": onSensorChanged( " + sensorEvent + " )");
        float[] values = sensorEvent.values;
        Log.d(Constant.APP_TAG, SUB_TAG + ": values: " + Arrays.toString(values));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        Log.d(Constant.APP_TAG, SUB_TAG + ": onAccuracyChanged( " + sensor + ", " + i + " )");
    }
}
