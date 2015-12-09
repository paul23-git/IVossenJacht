package com.scoutingstuff.paul.ivossenjacht;

import android.location.GpsStatus;

/**
 * Created by Paul on 12/8/2015.
 */
class GPSStatusListener implements GpsStatus.Listener
{
    public void onGpsStatusChanged(int event)
    {
        switch(event)
        {
        case GpsStatus.GPS_EVENT_STARTED:
            //System.out.println("TAG - GPS searching: ");
        break;
        case GpsStatus.GPS_EVENT_STOPPED:
            //System.out.println("TAG - GPS Stopped");
        break;
        case GpsStatus.GPS_EVENT_FIRST_FIX:

            /*
             * GPS_EVENT_FIRST_FIX Event is called when GPS is locked
             */
            //System.out.println("Fixed");
        break;
        case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
        //                 System.out.println("TAG - GPS_EVENT_SATELLITE_STATUS");
        break;
        }
    }
}