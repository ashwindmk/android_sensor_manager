package com.ashwin.android.sensor.proximity;

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

public class ProximitySensorActivity extends AppCompatActivity implements SensorEventListener2 {
    private static final String SUB_TAG = ProximitySensorActivity.class.getSimpleName();

    public static void launch(Context context) {
        context.startActivity(new Intent(context, ProximitySensorActivity.class));
    }

    private SensorManager sensorManager;
    private Sensor proximitySensor;

    private TextView valueTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proximity);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        valueTextView = findViewById(R.id.value_text_view);

        TextView infoTextView = findViewById(R.id.info_text_view);

        if (SensorUtils.sensorAvailable(this, Sensor.TYPE_PROXIMITY)) {
            valueTextView.setText("Available");

            proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

            Button registerButton = findViewById(R.id.register_button);
            registerButton.setOnClickListener(v -> {
                register();
            });

            Button unregisterButton = findViewById(R.id.unregister_button);
            unregisterButton.setOnClickListener(v -> {
                unregister();
            });

            infoTextView.setText(SensorUtils.getSensorAttributes(proximitySensor));
        } else {
            valueTextView.setText("Not Available");
            infoTextView.setText("-");
        }
    }

    private void register() {
        boolean res = sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
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
        if (values.length == 1) {
            valueTextView.setText("Value: " + values[0] + " cm");
        } else {
            valueTextView.setText("Value: (max = " + values[0] + ", min = " + values[1] + ") cm");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        Log.d(Constant.APP_TAG, SUB_TAG + ": onAccuracyChanged( " + sensor + ", " + i + " )");
    }
}
