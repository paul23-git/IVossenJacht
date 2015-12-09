package com.scoutingstuff.paul.ivossenjacht;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.os.Handler;

import com.scoutingstuff.paul.ivossenjacht.graphics.DirectionGLSurfaceView;

/**
 * Created by user on 8/27/15.
 */
public class MainActivity extends MyBaseActivity {

    private DirectionGLSurfaceView mGLView;
    private FallbackLocationTracker locationListener;
    private OrientationTracker sensorOrientationListener;
    private DownloadLocationTask downloader;
    private String stringUrl;
    private gameProcess gp;
    private ConnectivityManager connMgr;
    private FragmentManager fragMgr;

    private Handler login_handler;
    private Handler temporary_handles;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stringUrl= getString(R.string.server_url);
        temporary_handles = new Handler();


        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        mGLView = new DirectionGLSurfaceView(this);
        fragMgr = getFragmentManager();
        connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);




        gp = mGLView.getGameProcess();
        gp.dbg_activity= this;
        //mGLView.getGameProcess().dbg_activity = this;
        buildLogin();
        gp.setName("");
        gp.setPassword("");

        setContentView(mGLView);

        //doCheck();

        login_handler = new Handler();

        sensorOrientationListener = new OrientationTracker(this);
        sensorOrientationListener.start(gp);
    }

    public DirectionGLSurfaceView getmGLView() {
        return mGLView;
    }

    protected void onResume() {
        super.onResume();
        sensorOrientationListener.start();
        final int delay = 1000 * 5; //milliseconds - every 30 seconds
        final IVossenJachtApp application = (IVossenJachtApp)getApplication();
        if (locationListener == null) {
            temporary_handles.postDelayed(new Runnable() {
                public void run() {
                    Activity curr = application.getCurrentActivity();
                    boolean r = false;
                    if (curr instanceof MainActivity) {
                        r = startLocationUpdater((MainActivity) curr);
                    }
                    if (!r) {
                        temporary_handles.postDelayed(this, delay);
                    }
                }
            }, 1);
        }
        final int screen_update_delay = 100;
        temporary_handles.postDelayed(new Runnable() {
            public void run() {
                Activity curr = application.getCurrentActivity();
                mGLView.requestRender();
                temporary_handles.postDelayed(this, screen_update_delay);
            }
        }, screen_update_delay);
    }
    @Override
    protected void onPause() {
        super.onPause();
        sensorOrientationListener.stop();
        temporary_handles.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    static public boolean startLocationUpdater(MainActivity activity) {
        try {
            gameProcess gp = activity.gp;
            activity.locationListener = new FallbackLocationTracker(activity);
            activity.locationListener.start(gp);
            Location loc = activity.locationListener.getPossiblyStaleLocation();
            boolean found = false;
            if (loc != null) {
                gp.setLocation(loc);
                gp.updateLocation();
                found = true;
            } else {
                activity.buildAlert("No last known location - need to wait for satellites");
            }
            if (!found){
                try {
                    OpenGPSSettingsDialogFragment f = (OpenGPSSettingsDialogFragment) activity.fragMgr.findFragmentByTag("GPS settings");
                    if (f != null) {
                        f.dismiss();
                    }
                } catch (ClassCastException e) {
                }
            }
            return true;
        } catch (Exception e) {
            Fragment f = activity.fragMgr.findFragmentByTag("GPS settings");
            if (f == null&& activity.fragMgr.findFragmentByTag("Signin") == null ) {
                OpenGPSSettingsDialogFragment d = new OpenGPSSettingsDialogFragment();
                d.show(activity.fragMgr, "GPS settings");
            }
            return false;
        }

    }

    public void buildAlert(String msg) {
        if (fragMgr.findFragmentByTag("Alert") == null && fragMgr.findFragmentByTag("Signin") == null && fragMgr.findFragmentByTag("GPS settings") == null) {
            AlertDialogFragment d = new AlertDialogFragment();
            d.msg = msg;
            d.show(this.fragMgr, "Alert");
        }
    }

    public void doUpdate() {
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new UploadLocationTask(this, gp).execute(stringUrl);
        }
    }

    public void buildLogin() {
        if (fragMgr.findFragmentByTag("Signin") == null) {
            LoginDialogFragment d = new LoginDialogFragment();
            d.show(fragMgr, "Signin");
        }
    }
    public void startLoginHandler() {
        login_handler.removeCallbacksAndMessages(null);
        final int delay = 1000 * 15; //milliseconds - every 30 seconds
        login_handler.postDelayed(new Runnable() {
            public void run() {
                doUpdate();
                login_handler.postDelayed(this, delay);
            }
        }, 1);
    }
}