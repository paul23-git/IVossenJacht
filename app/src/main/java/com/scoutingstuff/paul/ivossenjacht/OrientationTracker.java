package com.scoutingstuff.paul.ivossenjacht;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by Paul on 12/3/2015.
 */
public class OrientationTracker implements SensorEventListener, IOrientationTracker {
    private SensorManager sensorManager;
    private Sensor magnet;
    private Sensor grav;
    private float[] last_grav_reading = null;
    private float[] last_magnet_reading = null;
    private float[] mat_inclination = new float[9];
    private float[] mat_rotation = new float[9];
    private float[] old_orientation;
    private float[] orientation;
    private boolean isRunning;
    private OrientationUpdateListener listener;

    private Context t;


    public OrientationTracker(Context context) {
        sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        this.magnet = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        this.grav =  sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        this.orientation = new float[]{0,0,0};
        isRunning = false;
        if (this.magnet == null || this.grav == null) {
            ((MainActivity)context).buildAlert("Sensor not working, app won't rotate correctly");
        }
    }

    public void start(){
        if(isRunning) {
            //Already running, do nothing
            return;
        }
        registerSensors();
        isRunning = true;
    }
    public void start(OrientationUpdateListener listener) {
        start();
        this.listener = listener;
    }
    public void stop() {
        unregisterSensors();
        isRunning = false;
    }


    public void registerSensors() {
        sensorManager.registerListener(this, magnet, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, grav, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void unregisterSensors() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        old_orientation = orientation;
        if(event.sensor == grav) {
            if (last_grav_reading == null) {
                last_grav_reading = new float[event.values.length];
            }
            System.arraycopy(event.values, 0, last_grav_reading, 0, event.values.length);
        } else {
            if (last_magnet_reading == null) {
                last_magnet_reading = new float[event.values.length];
            }
            System.arraycopy(event.values, 0, last_magnet_reading, 0, event.values.length);
        }
        if (last_grav_reading != null && last_magnet_reading != null) {
            SensorManager.getRotationMatrix(mat_rotation, mat_inclination, last_grav_reading, last_magnet_reading);
            SensorManager.getOrientation(mat_rotation, orientation);
            if (listener != null) {
                listener.onOrientationSensorUpdate(old_orientation, orientation);
            }
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
