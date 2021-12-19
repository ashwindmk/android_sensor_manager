package com.ashwin.android.sensor.compass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener2;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ashwin.android.sensor.Constant;
import com.ashwin.android.sensor.R;
import com.ashwin.android.sensor.SensorUtils;
import com.ashwin.android.sensor.magnetometer.MagnetometerActivity;

public class CompassActivity extends AppCompatActivity implements SensorEventListener2 {
    private static final String SUB_TAG = MagnetometerActivity.class.getSimpleName();

    public static void launch(Context context) {
        context.startActivity(new Intent(context, CompassActivity.class));
    }

    private SensorManager sensorManager;

    // Accelerometer + Magnetometer -> Rotation matrix -> Orientation
    private Sensor accelerometerSensor;
    private Sensor magnetometerSensor;

    private TextView valueTextView;
    private ImageView compassImageView;

    private float[] lastAccelerometerValues = new float[3];
    private float[] lastMagnetometerValues = new float[3];
    private float[] rotationMatrix = new float[9];
    private float[] orientation = new float[3];

    private boolean isLastAccelerometerValuesCopied = false;
    private boolean isLastMagnetometerValuesCopied = false;

    long lastUpdateTime = 0;
    float currDeg = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        valueTextView = findViewById(R.id.value_text_view);
        compassImageView = findViewById(R.id.compass_image_view);

        if (SensorUtils.sensorAvailable(this, Sensor.TYPE_ACCELEROMETER) && SensorUtils.sensorAvailable(this, Sensor.TYPE_MAGNETIC_FIELD)) {
            valueTextView.setText("Available");

            accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            magnetometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

            Button registerButton = findViewById(R.id.register_button);
            registerButton.setOnClickListener(v -> {
                register();
            });

            Button unregisterButton = findViewById(R.id.unregister_button);
            unregisterButton.setOnClickListener(v -> {
                unregister();
            });
        } else {
            valueTextView.setText("Not Available");
        }
    }

    private void register() {
        boolean res1 = sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        boolean res2 = sensorManager.registerListener(this, magnetometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        Log.d(Constant.APP_TAG, SUB_TAG + ": register: " + res1 + ", " + res2);
    }

    private void unregister() {
        Log.d(Constant.APP_TAG, SUB_TAG + ": unregister");
        sensorManager.unregisterListener(this, accelerometerSensor);
        sensorManager.unregisterListener(this, magnetometerSensor);
    }

    @Override
    public void onFlushCompleted(Sensor sensor) {
        Log.d(Constant.APP_TAG, SUB_TAG + ": onFlushCompleted( " + sensor + " )");
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Log.d(Constant.APP_TAG, SUB_TAG + ": onSensorChanged( " + sensorEvent.sensor.getName() + ": " + sensorEvent + " )");

        if (sensorEvent.sensor == accelerometerSensor) {
            System.arraycopy(sensorEvent.values, 0, lastAccelerometerValues, 0, 3);
            isLastAccelerometerValuesCopied = true;
        }
        else if (sensorEvent.sensor == magnetometerSensor) {
            System.arraycopy(sensorEvent.values, 0, lastMagnetometerValues, 0, 3);
            isLastMagnetometerValuesCopied = true;
        }

        if (isLastAccelerometerValuesCopied && isLastMagnetometerValuesCopied && System.currentTimeMillis() - lastUpdateTime > 250) {
            SensorManager.getRotationMatrix(rotationMatrix, null, lastAccelerometerValues, lastMagnetometerValues);
            SensorManager.getOrientation(rotationMatrix, orientation);

            float azimuthRad = orientation[0];
            float azimuthDeg = (float) Math.toDegrees(azimuthRad);

            RotateAnimation animation = new RotateAnimation(currDeg, -azimuthDeg, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            animation.setDuration(250);
            animation.setFillAfter(true);

            compassImageView.startAnimation(animation);

            currDeg = -azimuthDeg;
            lastUpdateTime = System.currentTimeMillis();

            int x = (int) azimuthDeg;
            valueTextView.setText("Value: " + x + " °");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        Log.d(Constant.APP_TAG, SUB_TAG + ": onAccuracyChanged( " + sensor + ", " + i + " )");
    }
}
