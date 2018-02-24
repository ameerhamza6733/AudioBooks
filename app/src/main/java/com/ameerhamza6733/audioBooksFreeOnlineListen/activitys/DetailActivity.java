package com.ameerhamza6733.audioBooksFreeOnlineListen.activitys;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ameerhamza6733.audioBooksFreeOnlineListen.R;
import com.ameerhamza6733.audioBooksFreeOnlineListen.fragment.BookMetaDataRecylerView;

public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_IDENTEFIER_NAME="EXTRA_IDENTEFIER_NAME";
    public static final String TAG = "DetailActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        String identifier = intent.getStringExtra(EXTRA_IDENTEFIER_NAME);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.audio_player_contaner, BookMetaDataRecylerView.newInstance(identifier));
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
