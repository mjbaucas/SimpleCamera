package com.mjbaucas.simplecamera;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

public class CameraActivity extends AppCompatActivity implements SensorEventListener {
    CameraPreview camPrev;
    LinearLayout camLayout;
    SensorManager sensorManager;
    Sensor accSensor;

    float[] gravityVal;
    float lastAccelVal;
    float currAccelVal;
    float accelVal;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        camPrev = new CameraPreview(this);
        camLayout = findViewById(R.id.cam_layout);
        camLayout.addView(camPrev);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            gravityVal = event.values.clone();

            float x = gravityVal[0];
            float y = gravityVal[1];
            float z = gravityVal[2];

            lastAccelVal = currAccelVal;
            currAccelVal = (float) Math.sqrt(x*x + y*y + z*z);
            accelVal = accelVal * 0.9f + (currAccelVal - lastAccelVal);

            if(accelVal > 8){
                Log.d("ACCEL", "Value: " + accelVal);
                Toast.makeText(this, "Shake Detected", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
