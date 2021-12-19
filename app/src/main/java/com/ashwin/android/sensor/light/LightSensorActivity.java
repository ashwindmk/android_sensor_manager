package com.ashwin.android.sensor.light;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.ashwin.android.sensor.Constant;
import com.ashwin.android.sensor.R;
import com.ashwin.android.sensor.SensorUtils;

import java.util.Arrays;

public class LightSensorActivity extends AppCompatActivity implements SensorEventListener {
    private static final String SUB_TAG = LightSensorActivity.class.getSimpleName();

    public static void launch(Context context) {
        context.startActivity(new Intent(context, LightSensorActivity.class));
    }

    private SensorManager sensorManager;
    private Sensor lightSensor;

    private TextView valueTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        valueTextView = findViewById(R.id.value_text_view);

        TextView infoTextView = findViewById(R.id.info_text_view);

        if (SensorUtils.sensorAvailable(this, Sensor.TYPE_AMBIENT_TEMPERATURE)) {
            valueTextView.setText("Available");

            lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

            Button registerButton = findViewById(R.id.register_button);
            registerButton.setOnClickListener(v -> {
                register();
            });

            Button unregisterButton = findViewById(R.id.unregister_button);
            unregisterButton.setOnClickListener(v -> {
                unregister();
            });

            infoTextView.setText(SensorUtils.getSensorAttributes(lightSensor));
        } else {
            valueTextView.setText("Not Available");
            infoTextView.setText("-");
        }
    }

    private void register() {
        Log.d(Constant.APP_TAG, SUB_TAG + ": register");
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void unregister() {
        Log.d(Constant.APP_TAG, SUB_TAG + ": unregister");
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(Constant.APP_TAG, SUB_TAG + ": onResume");
        //register();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Log.d(Constant.APP_TAG, SUB_TAG + ": onSensorChanged( " + sensorEvent + " )");
        float[] values = sensorEvent.values;
        Log.d(Constant.APP_TAG, SUB_TAG + ": values: " + Arrays.toString(values));
        valueTextView.setText("Light value: " + values[0]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        Log.d(Constant.APP_TAG, SUB_TAG + ": onAccuracyChanged( " + sensor + ", " + i + " )");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(Constant.APP_TAG, SUB_TAG + ": onPause");
        //unregister();
    }
}