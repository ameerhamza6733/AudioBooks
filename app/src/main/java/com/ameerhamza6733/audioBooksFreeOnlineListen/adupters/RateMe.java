package com.ameerhamza6733.audioBooksFreeOnlineListen.adupters;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ameerhamza6733.audioBooksFreeOnlineListen.R;

public class RateMe extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_me);
        RateMe();
    }
    private void RateMe() {
        AlertDialog.Builder builder;

        builder = new AlertDialog.Builder(this);
        builder.setTitle("Rate Me")
                .setMessage(" AudioBook need your help please rate us on google play")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + RateMe.this.getPackageName())));
                    }
                })

                .setIcon(android.R.drawable.star_big_on)
                .show();
    }
}
