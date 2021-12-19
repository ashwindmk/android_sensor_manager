package com.ashwin.android.sensor.magnetometer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener2;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.ashwin.android.sensor.Constant;
import com.ashwin.android.sensor.R;
import com.ashwin.android.sensor.SensorUtils;

import java.util.Arrays;

public class MagnetometerActivity extends AppCompatActivity implements SensorEventListener2 {
    private static final String SUB_TAG = MagnetometerActivity.class.getSimpleName();

    public static void launch(Context context) {
        context.startActivity(new Intent(context, MagnetometerActivity.class));
    }

    private SensorManager sensorManager;
    private Sensor magnetometerSensor;

    private TextView valueTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magnetometer);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        valueTextView = findViewById(R.id.value_text_view);

        TextView infoTextView = findViewById(R.id.info_text_view);

        if (SensorUtils.sensorAvailable(this, Sensor.TYPE_MAGNETIC_FIELD)) {
            valueTextView.setText("Available");

            magnetometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);

            Button registerButton = findViewById(R.id.register_button);
            registerButton.setOnClickListener(v -> {
                register();
            });

            Button unregisterButton = findViewById(R.id.unregister_button);
            unregisterButton.setOnClickListener(v -> {
                unregister();
            });

            infoTextView.setText(SensorUtils.getSensorAttributes(magnetometerSensor));
        } else {
            valueTextView.setText("Not Available");
            infoTextView.setText("-");
        }
    }

    private void register() {
        boolean res = sensorManager.registerListener(this, magnetometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
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
        float[] values = sensorEvent.values;  // All values are in micro-Tesla (uT)
        Log.d(Constant.APP_TAG, SUB_TAG + ": values: " + Arrays.toString(values));

        float x = values[0];  // Angular speed around the x-axis
        float y = values[1];  // Angular speed around the y-axis
        float z = values[2];  // Angular speed around the z-axis
        valueTextView.setText("Value: (x = " + x + ", y = " + y + ", z = " + z + ") uT");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        Log.d(Constant.APP_TAG, SUB_TAG + ": onAccuracyChanged( " + sensor + ", " + i + " )");
    }
}
