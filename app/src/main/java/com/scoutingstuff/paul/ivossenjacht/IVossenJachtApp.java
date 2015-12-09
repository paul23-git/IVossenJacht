package com.scoutingstuff.paul.ivossenjacht;

import android.app.Activity;
import android.app.Application;

/**
 * Created by Paul on 12/8/2015.
 */
public class IVossenJachtApp extends Application {
    public void onCreate() {
        super.onCreate();
    }

    private Activity mCurrentActivity = null;
    public Activity getCurrentActivity(){
        return mCurrentActivity;
    }
    public void setCurrentActivity(Activity mCurrentActivity){
        this.mCurrentActivity = mCurrentActivity;
    }
}