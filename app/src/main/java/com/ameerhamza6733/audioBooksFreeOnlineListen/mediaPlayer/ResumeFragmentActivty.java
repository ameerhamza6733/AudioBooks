package com.ameerhamza6733.audioBooksFreeOnlineListen.mediaPlayer;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ResumeFragmentActivty extends AppCompatActivity {
private static ResumeTrackPermistionDialogFragment resumeTrackPermistionDialogFragment;
public static final String RESUME="com.ameerhamza6733.audioBooksFreeOnlineListen.mediaPlayer.Resume";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        resumeTrackPermistionDialogFragment = new ResumeTrackPermistionDialogFragment();
        resumeTrackPermistionDialogFragment.show(getFragmentManager(),"ResumeTrackPermistionDialogFragment");


    }

    public static class ResumeTrackPermistionDialogFragment extends DialogFragment{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState)
        {
            return new AlertDialog.Builder(getActivity())
                    .setTitle("Do you want resume track ")
                    .setPositiveButton("OK", (dialog, which) -> {

                    })
                    .setNegativeButton("NO", (dialog, which) -> {

                    })
                    .create();
        }
    }


protected  void sendBackResumeChoose(boolean resume){
    Intent intent = new Intent();
    intent.putExtra(RESUME, resume);
    setResult(RESULT_OK, intent);
    finish();
}
}
