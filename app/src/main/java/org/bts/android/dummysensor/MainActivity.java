package org.bts.android.dummysensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private static final String TAG = MainActivity.class.getSimpleName();;
    private SensorManager mSensorManager;
    private Sensor mSensorProximity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        List<Sensor> sensorList = this.mSensorManager.getSensorList(Sensor.TYPE_ALL);

        printSensorList(sensorList);

        this.mSensorProximity = this.mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(this.mSensorProximity != null) {
            this.mSensorManager.registerListener(this, this.mSensorProximity, SensorManager.SENSOR_DELAY_NORMAL);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(this.mSensorProximity != null) {
            this.mSensorManager.unregisterListener(this, this.mSensorProximity);
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
        long eventTime = event.timestamp;
        Log.i(MainActivity.TAG, String.valueOf(eventTime));
        Toast.makeText(this, String.valueOf(eventTime), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
