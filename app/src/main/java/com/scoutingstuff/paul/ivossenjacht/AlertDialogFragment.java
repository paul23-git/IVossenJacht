package com.scoutingstuff.paul.ivossenjacht;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Paul on 12/8/2015.
 */
public class AlertDialogFragment extends DialogFragment {
    //private AlertDialog gps_dialog;
    public String msg = "";
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(msg);
        // Create the AlertDialog object and return it
        return builder.create();
    }
}