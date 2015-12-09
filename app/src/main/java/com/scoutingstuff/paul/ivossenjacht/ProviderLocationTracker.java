package com.scoutingstuff.paul.ivossenjacht;

/**
 * Created by Paul on 12/3/2015.
 */
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.util.Iterator;


public class ProviderLocationTracker implements LocationListener, LocationTracker {

    // The minimum distance to change Updates in meters
    private static final long MIN_UPDATE_DISTANCE = 0;

    // The minimum time between updates in milliseconds
    private static final long MIN_UPDATE_TIME = 1000*2;
    GPSStatusListener mGPSStatusListener;

    public enum ProviderType{
        NETWORK,
        GPS
    }
    private String provider;

    private Location lastLocation;
    private long lastTime;

    private boolean isRunning;

    private LocationUpdateListener listener;
    private LocationManager lm;
    private IVossenJachtApp app = null;

    public ProviderLocationTracker(Context context, ProviderType type, IVossenJachtApp app) throws Exception {
        this(context, type);
        this.app = app;
    }

    public ProviderLocationTracker(Context context, ProviderType type) throws Exception {
        lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (type == ProviderType.NETWORK) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else {
            provider = LocationManager.GPS_PROVIDER;
            mGPSStatusListener = new GPSStatusListener();
            GpsStatus  GPSStat = lm.getGpsStatus(null);
            int i = GPSStat.getTimeToFirstFix();
            Iterable satlist = GPSStat.getSatellites();
            Iterator it = satlist.iterator();
            while (it.hasNext()) {
                GpsSatellite sat = (GpsSatellite)it.next();
                it.next();
            }

            lm.addGpsStatusListener(mGPSStatusListener);
        }

        if (!lm.isProviderEnabled(provider)) {
            throw new Exception("Provider is not working");
        }

    }



    public void start(){
        if(isRunning){
            //Already running, do nothing
            return;
        }
        //The provider is on, so start getting updates.  Update current location
        isRunning = true;
        lm.requestLocationUpdates(provider, MIN_UPDATE_TIME, MIN_UPDATE_DISTANCE, this);
        lastLocation = null;
        lastTime = 0;
    }

    public void start(LocationUpdateListener update) {
        start();
        listener = update;
    }


    public void stop(){
        if(isRunning){
            lm.removeUpdates(this);
            isRunning = false;
            listener = null;
        }
    }

    public boolean hasLocation(){
        return !(lastLocation == null || (System.currentTimeMillis() - lastTime > 10 * MIN_UPDATE_TIME));
    }

    public boolean hasPossiblyStaleLocation(){
        return lastLocation != null || lm.getLastKnownLocation(provider)!= null;
    }

    public Location getLocation(){
        if(lastLocation == null){
            return null;
        }
        if(System.currentTimeMillis() - lastTime > 10 * MIN_UPDATE_TIME){
            return null; //stale
        }
        return lastLocation;
    }

    public Location getPossiblyStaleLocation(){
        if(lastLocation != null){
            return lastLocation;
        }
        return lm.getLastKnownLocation(provider);
    }

    public void onLocationChanged(Location newLoc) {
        long now = System.currentTimeMillis();
        if (app != null) {
            Activity curr = app.getCurrentActivity();
            if (curr instanceof MainActivity) {
                MainActivity main_curr = (MainActivity) curr;
                main_curr.buildAlert("Updating location using " + provider);
            }
            if (listener != null) {
                listener.onLocationUpdate(lastLocation, lastTime, newLoc, now);
            }
            lastLocation = newLoc;
            lastTime = now;
        }
    }

    public void onProviderDisabled(String arg0) {

    }

    public void onProviderEnabled(String arg0) {

    }

    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
    }
}