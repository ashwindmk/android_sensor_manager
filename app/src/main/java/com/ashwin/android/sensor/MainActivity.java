package com.ashwin.android.sensor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.ashwin.android.sensor.accelerometer.AccelerometerActivity;
import com.ashwin.android.sensor.accelerometer.ShakeDetectActivity;
import com.ashwin.android.sensor.compass.CompassActivity;
import com.ashwin.android.sensor.gravity.FlipDetectorActivity;
import com.ashwin.android.sensor.gravity.GravitySensorActivity;
import com.ashwin.android.sensor.humidity.HumiditySensorActivity;
import com.ashwin.android.sensor.light.LightSensorActivity;
import com.ashwin.android.sensor.magnetometer.MagnetometerActivity;
import com.ashwin.android.sensor.pressure.PressureSensorActivity;
import com.ashwin.android.sensor.proximity.ProximitySensorActivity;
import com.ashwin.android.sensor.stepcounter.StepCounterActivity;
import com.ashwin.android.sensor.stepdetector.StepDetectorActivity;
import com.ashwin.android.sensor.temperature.TemperatureSensorActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        check();

        Button lightSensorButton = findViewById(R.id.light_sensor_button);
        lightSensorButton.setOnClickListener(v -> {
            LightSensorActivity.launch(this);
        });

        Button temperatureSensorButton = findViewById(R.id.temperature_sensor_button);
        temperatureSensorButton.setOnClickListener(v -> {
            TemperatureSensorActivity.launch(this);
        });

        Button humiditySensorButton = findViewById(R.id.humidity_sensor_button);
        humiditySensorButton.setOnClickListener(v -> {
            HumiditySensorActivity.launch(this);
        });

        Button pressureSensorButton = findViewById(R.id.pressure_sensor_button);
        pressureSensorButton.setOnClickListener(v -> {
            PressureSensorActivity.launch(this);
        });

        Button proximitySensorButton = findViewById(R.id.proximity_sensor_button);
        proximitySensorButton.setOnClickListener(v -> {
            ProximitySensorActivity.launch(this);
        });

        Button accelerometerSensorButton = findViewById(R.id.accelerometer_sensor_button);
        accelerometerSensorButton.setOnClickListener(v -> {
            AccelerometerActivity.launch(this);
        });

        Button shakeDetectButton = findViewById(R.id.shake_detect_button);
        shakeDetectButton.setOnClickListener(v -> {
            ShakeDetectActivity.launch(this);
        });

        Button stepCounterButton = findViewById(R.id.step_counter_button);
        stepCounterButton.setOnClickListener(v -> {
            StepCounterActivity.launch(this);
        });

        Button stepDetectorButton = findViewById(R.id.step_detector_button);
        stepDetectorButton.setOnClickListener(v -> {
            StepDetectorActivity.launch(this);
        });

        Button gravityButton = findViewById(R.id.gravity_button);
        gravityButton.setOnClickListener(v -> {
            GravitySensorActivity.launch(this);
        });

        Button flipDetectorButton = findViewById(R.id.flip_detector_button);
        flipDetectorButton.setOnClickListener(v -> {
            FlipDetectorActivity.launch(this);
        });

        Button magnetometerButton = findViewById(R.id.magnetometer_button);
        magnetometerButton.setOnClickListener(v -> {
            MagnetometerActivity.launch(this);
        });

        Button compassButton = findViewById(R.id.compass_button);
        compassButton.setOnClickListener(v -> {
            CompassActivity.launch(this);
        });
    }

    private void check() {
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        List<Sensor> deviceSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);

        // List all
        SensorUtils.logSensorNames(this);

        // Check if magnetometer is available in device
        Log.d(Constant.APP_TAG, "Magnetometer sensor available: " + SensorUtils.sensorAvailable(this, Sensor.TYPE_MAGNETIC_FIELD));
        Log.d(Constant.APP_TAG, "Heart-beat sensor available: " + SensorUtils.sensorAvailable(this, Sensor.TYPE_HEART_BEAT));

        // Sensor attributes
        Sensor magnetometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        SensorUtils.logSensorAttributes(magnetometerSensor);
    }
}
