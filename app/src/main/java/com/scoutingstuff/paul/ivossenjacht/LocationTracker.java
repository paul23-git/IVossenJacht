package com.scoutingstuff.paul.ivossenjacht;

/**
 * Created by Paul on 12/3/2015.
 */
import android.location.Location;

public interface LocationTracker {
    interface LocationUpdateListener{
        void onLocationUpdate(Location oldLoc, long oldTime, Location newLoc, long newTime);
    }

    void start();
    void start(LocationUpdateListener update);

    void stop();

    boolean hasLocation();

    boolean hasPossiblyStaleLocation();

    Location getLocation();

    Location getPossiblyStaleLocation();

}