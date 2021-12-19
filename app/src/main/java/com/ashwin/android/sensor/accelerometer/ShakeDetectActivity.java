package com.ashwin.android.sensor.accelerometer;

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

public class ShakeDetectActivity extends AppCompatActivity implements SensorEventListener2 {
    private static final String SUB_TAG = ShakeDetectActivity.class.getSimpleName();

    public static void launch(Context context) {
        context.startActivity(new Intent(context, ShakeDetectActivity.class));
    }

    private SensorManager sensorManager;
    private Sensor accelerometerSensor;

    private TextView valueTextView;

    private float lastX, lastY, lastZ;
    private boolean isFirst = true;
    private long shakenTimestamp = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect_shake);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        valueTextView = findViewById(R.id.value_text_view);

        TextView infoTextView = findViewById(R.id.info_text_view);

        if (SensorUtils.sensorAvailable(this, Sensor.TYPE_ACCELEROMETER)) {
            valueTextView.setText("Available");

            accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

            Button registerButton = findViewById(R.id.register_button);
            registerButton.setOnClickListener(v -> {
                register();
            });

            Button unregisterButton = findViewById(R.id.unregister_button);
            unregisterButton.setOnClickListener(v -> {
                unregister();
            });

            infoTextView.setText(SensorUtils.getSensorAttributes(accelerometerSensor));
        } else {
            valueTextView.setText("Not Available");
            infoTextView.setText("-");
        }
    }

    private void register() {
        boolean res = sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
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

        float currX = values[0];
        float currY = values[1];
        float currZ = values[2];

        if (!isFirst) {
            float diffX = Math.abs(currX - lastX);
            float diffY = Math.abs(currY - lastY);
            float diffZ = Math.abs(currZ - lastZ);

            int count = 0;
            for (float diff : Arrays.asList(diffX, diffY, diffZ)) {
                if (diff >= 5f) {
                    count++;
                    if (count == 2) {
                        break;
                    }
                }
            }

            if (count >= 2) {
                // Shake detected
                Log.d(Constant.APP_TAG, SUB_TAG + ": SHAKEN (timestamp: " + sensorEvent.timestamp + ")");
                valueTextView.setText("Value: SHAKEN");
                shakenTimestamp = System.currentTimeMillis();
            } else {
                // No shake detected
                if (System.currentTimeMillis() - shakenTimestamp > 2000L) {  // Delay to see the 'SHAKEN' text
                    valueTextView.setText("Value: Stable");
                }
            }
        } else {
            isFirst = false;
        }

        lastX = currX;
        lastY = currY;
        lastZ = currZ;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        Log.d(Constant.APP_TAG, SUB_TAG + ": onAccuracyChanged( " + sensor + ", " + i + " )");
    }
}
