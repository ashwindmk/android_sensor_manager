package com.ashwin.android.sensor.stepdetector;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener2;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.ashwin.android.sensor.Constant;
import com.ashwin.android.sensor.R;
import com.ashwin.android.sensor.SensorUtils;

import java.util.Arrays;

public class StepDetectorActivity extends AppCompatActivity implements SensorEventListener2 {
    private static final String SUB_TAG = StepDetectorActivity.class.getSimpleName();
    private static final int REQ_CODE = 1;

    public static void launch(Context context) {
        context.startActivity(new Intent(context, StepDetectorActivity.class));
    }

    private SensorManager sensorManager;
    private Sensor stepDetectorSensor;

    private TextView valueTextView;

    private boolean hasPermission = false;
    private int totalStepCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detector);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        valueTextView = findViewById(R.id.value_text_view);

        TextView infoTextView = findViewById(R.id.info_text_view);

        if (SensorUtils.sensorAvailable(this, Sensor.TYPE_STEP_DETECTOR)) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
                Log.e(Constant.APP_TAG, SUB_TAG + ": No permission");
                hasPermission = false;
                requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, REQ_CODE);
            } else {
                Log.d(Constant.APP_TAG, SUB_TAG + ": Has permission");
                hasPermission = true;
            }

            valueTextView.setText("Available (has permission: " + hasPermission + ")");

            stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

            Button registerButton = findViewById(R.id.register_button);
            registerButton.setOnClickListener(v -> {
                register();
            });

            Button unregisterButton = findViewById(R.id.unregister_button);
            unregisterButton.setOnClickListener(v -> {
                unregister();
            });

            infoTextView.setText(SensorUtils.getSensorAttributes(stepDetectorSensor));
        } else {
            valueTextView.setText("Not Available");
            infoTextView.setText("-");
        }
    }

    private void register() {
        boolean res = sensorManager.registerListener(this, stepDetectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
        Log.d(Constant.APP_TAG, SUB_TAG + ": register: " + res);
    }

    private void unregister() {
        Log.d(Constant.APP_TAG, SUB_TAG + ": unregister");
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onFlushCompleted(Sensor sensor) {
        Log.d(Constant.APP_TAG, SUB_TAG + ": onFlushCompleted( " + sensor + " )");
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Log.d(Constant.APP_TAG, SUB_TAG + ": onSensorChanged( " + sensorEvent + " )");
        float[] values = sensorEvent.values;
        Log.d(Constant.APP_TAG, SUB_TAG + ": values: " + Arrays.toString(values));

        valueTextView.setText("Value: " + values[0]);  // Always 1, delay of 2 sec.

        totalStepCount = totalStepCount + (int) values[0];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        Log.d(Constant.APP_TAG, SUB_TAG + ": onAccuracyChanged( " + sensor + ", " + i + " )");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQ_CODE) {
            if (grantResults.length > 0) {
                hasPermission = true;
            }
            valueTextView.setText("Available (has permission: " + hasPermission + ")");
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
