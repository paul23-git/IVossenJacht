package com.scoutingstuff.paul.ivossenjacht;

/**
 * Created by Paul on 12/3/2015.
 */
public interface IOrientationTracker {
    interface OrientationUpdateListener{
        void onOrientationSensorUpdate(float[] old_orientation, float[] new_orientation);
    }

    void start();
    void start(OrientationUpdateListener update);

    void stop();
}
