package com.ameerhamza6733.audioBooksFreeOnlineListen.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

/**
 * Created by AmeerHamza on 4/26/18.
 */

public class ResumeTrackDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle("Do you want resume track ")
                .setPositiveButton("OK", (dialog, which) -> {

                })
                .setNegativeButton("NO", (dialog, which) -> {

                })
                .create();
    }
}
