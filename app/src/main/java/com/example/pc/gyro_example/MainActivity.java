package com.example.pc.gyro_example;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.media.MediaPlayer;



public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private TextView gyroX;
    private TextView gyroY;
    private TextView gyroZ;

    private Sensor mSensorGyr;
    private SensorManager mSensorManager;

    private static final float ROTATION_THRESHOLD = 2.0f;
    private static final int ROTATION_WAIT_TIME_MS = 100;

    private long mRotationTime = 0;


    private static MediaPlayer soundGyro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorGyr = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        gyroX = (TextView) findViewById(R.id.gyroX);
        gyroY = (TextView) findViewById(R.id.gyroY);
        gyroZ = (TextView) findViewById(R.id.gyroZ);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensorGyr, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    public void onSensorChanged(SensorEvent event) {

        if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {

            if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                gyroX.setText(R.string.act_main_no_acuracy);
                gyroY.setText(R.string.act_main_no_acuracy);
                gyroZ.setText(R.string.act_main_no_acuracy);
            }
            return;
        }

        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            gyroX.setText("x = " + Float.toString(event.values[0]));
            gyroY.setText("y = " + Float.toString(event.values[1]));
            gyroZ.setText("z = " + Float.toString(event.values[2]));
            detectRotation(event);
        }

    }

    private void detectRotation(SensorEvent event) {
        long now = System.currentTimeMillis();

        if ((now - mRotationTime) > ROTATION_WAIT_TIME_MS) {
            mRotationTime = now;

            // Change background color if rate of rotation around any
            // axis and in any direction exceeds threshold;
            // otherwise, reset the color
            if (Math.abs(event.values[0]) > ROTATION_THRESHOLD ||
                    Math.abs(event.values[1]) > ROTATION_THRESHOLD ||
                    Math.abs(event.values[2]) > ROTATION_THRESHOLD) {
                soundGyro.start();
            }
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
