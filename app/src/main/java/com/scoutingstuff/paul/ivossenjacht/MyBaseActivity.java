package com.scoutingstuff.paul.ivossenjacht;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by Paul on 12/8/2015.
 */
public class MyBaseActivity extends Activity {
    protected IVossenJachtApp mIVossenJachtApp;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIVossenJachtApp = (IVossenJachtApp)this.getApplication();
    }
    protected void onResume() {
        super.onResume();
        mIVossenJachtApp.setCurrentActivity(this);
    }
    protected void onPause() {
        clearReferences();
        super.onPause();
    }
    protected void onDestroy() {
        clearReferences();
        super.onDestroy();
    }

    private void clearReferences(){
        Activity currActivity = mIVossenJachtApp.getCurrentActivity();
        if (this.equals(currActivity))
            mIVossenJachtApp.setCurrentActivity(null);
    }
}