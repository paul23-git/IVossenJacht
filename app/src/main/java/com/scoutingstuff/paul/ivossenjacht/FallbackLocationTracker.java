package com.scoutingstuff.paul.ivossenjacht;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

/**
 * Created by Paul on 12/3/2015.
 */
public class FallbackLocationTracker  implements LocationTracker, LocationTracker.LocationUpdateListener {


    private boolean isRunning;

    private ProviderLocationTracker gps;
    private ProviderLocationTracker net;

    private LocationUpdateListener listener;

    Location lastLoc;
    long lastTime;

    public FallbackLocationTracker(MainActivity context) throws Exception{
        boolean gps_worked = true;
        boolean net_worked = true;
        try {
            gps = new ProviderLocationTracker(context, ProviderLocationTracker.ProviderType.GPS, (IVossenJachtApp)context.getApplication());
        } catch (Exception e) {
            //context.
            gps_worked = false;
        }
        try {
            net = new ProviderLocationTracker(context, ProviderLocationTracker.ProviderType.NETWORK, (IVossenJachtApp)context.getApplication());
        } catch (Exception e) {
            net_worked = false;
        }
        if (!gps_worked && !net_worked) {
            throw new Exception("Both location services not working.");
        }
    }

    public void start(){
        if(isRunning){
            //Already running, do nothing
            return;
        }
        //Start both
        if (gps != null) {
            gps.start(this);
        }
        if (net != null) {
            net.start(this);
        }
        isRunning = true;
    }

    public void start(LocationUpdateListener update) {
        start();
        listener = update;
    }


    public void stop(){
        if(isRunning){
            if (gps != null)
                gps.stop();
            if (net!= null)
                net.stop();
            isRunning = false;
            listener = null;
        }
    }

    public boolean hasLocation(){
        //If either has a location, use it
        return (gps!= null && gps.hasLocation()) || (net != null && net.hasLocation());
    }

    public boolean hasPossiblyStaleLocation(){
        //If either has a location, use it
        return (gps != null && gps.hasPossiblyStaleLocation()) || (net != null && net.hasPossiblyStaleLocation());
    }

    public Location getLocation(){
        Location ret = null;
        if (gps != null)
            ret = gps.getLocation();
        if(ret == null && net != null){
            ret = net.getLocation();
        }
        return ret;
    }

    public Location getPossiblyStaleLocation(){
        Location ret = null;
        if (gps != null)
            ret = gps.getPossiblyStaleLocation();
        if(ret == null && net != null){
            ret = net.getPossiblyStaleLocation();
        }
        return ret;
    }

    public void onLocationUpdate(Location oldLoc, long oldTime, Location newLoc, long newTime) {
        //We should update only if there is no last location, the provider is the same, or the provider is more accurate, or the old location is stale
        if(lastLoc == null ||
                lastLoc.getProvider().equals(newLoc.getProvider()) ||
                newLoc.getProvider().equals(LocationManager.GPS_PROVIDER) ||
                newTime - lastTime > 5 * 60 * 1000){
            if(listener != null){
                listener.onLocationUpdate(lastLoc, lastTime, newLoc, newTime);
            }
            lastLoc = newLoc;
            lastTime = newTime;
        }
    }
}