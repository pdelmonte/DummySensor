package org.bts.android.dummysensor;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private static final String TAG = MainActivity.class.getSimpleName();;
    private SensorManager mSensorManager;
    private Sensor mSensorProximity;
    private Sensor mAccelerometerSensor;
    private RelativeLayout mMainRelativeLayout;
    private long mOldTimestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mMainRelativeLayout = (RelativeLayout) findViewById(R.id.relative_layout_activity_main);
        this.mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        List<Sensor> sensorList = this.mSensorManager.getSensorList(Sensor.TYPE_ALL);

        printSensorList(sensorList);

        this.mSensorProximity = this.mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        this.mAccelerometerSensor = this.mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Log.d(MainActivity.TAG, this.mAccelerometerSensor ==null ? "Null" : "Available");
        Log.d(MainActivity.TAG, "Range: " + this.mAccelerometerSensor.getMaximumRange() +", Resolution" + this.mAccelerometerSensor.getResolution());

        this.mOldTimestamp = System.currentTimeMillis();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(this.mSensorProximity != null) {
            this.mSensorManager.registerListener(this, this.mSensorProximity, SensorManager.SENSOR_DELAY_NORMAL);
        }

        if(this.mAccelerometerSensor != null) {
            this.mSensorManager.registerListener(this, this.mAccelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(this.mSensorProximity != null) {
            this.mSensorManager.unregisterListener(this, this.mSensorProximity);
        }

        if(this.mAccelerometerSensor != null) {
            this.mSensorManager.unregisterListener(this, this.mAccelerometerSensor);
        }

    }

    private void printSensorList(List<Sensor> sensorList) {

        StringBuilder sensorAvailableString = new StringBuilder();

        for (Sensor sensor : sensorList) {

            sensorAvailableString.append(sensor.getName()).append("\n");

        }

        Log.i(MainActivity.TAG, sensorAvailableString.toString());
        Toast.makeText(this, sensorAvailableString.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if(event.sensor.getType() == Sensor.TYPE_PROXIMITY) {

            Log.i(MainActivity.TAG, "Timestamp: " + event.timestamp);
            Toast.makeText(this, "Timestamp: " + event.timestamp, Toast.LENGTH_SHORT).show();

        } else if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            turnScreenColour(event);
            //Toast.makeText(this, "Values: " + event.values[0]+", "+ event.values[1]+", "+event.values[2], Toast.LENGTH_SHORT).show();
        }

    }

    private void turnScreenColour(SensorEvent event) {

        float xAcc = event.values[0];
        float yAcc = event.values[1];
        float zAcc = event.values[2];

        double magnitude = (xAcc * xAcc + yAcc * yAcc + zAcc * zAcc) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);

        long oldTimestamp;
        long currentTimestamp = event.timestamp;

        if (magnitude > 5.0 && (currentTimestamp - this.mOldTimestamp) > 1000000000 ) {
            Log.i(MainActivity.TAG, "Shuffle! Value: " + magnitude);

            this.mOldTimestamp = currentTimestamp;

            if (((ColorDrawable) this.mMainRelativeLayout.getBackground()).getColor() == ContextCompat.getColor(this, R.color.colorCyan)) {
                this.mMainRelativeLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorRed));
            } else {
                this.mMainRelativeLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorCyan));
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
