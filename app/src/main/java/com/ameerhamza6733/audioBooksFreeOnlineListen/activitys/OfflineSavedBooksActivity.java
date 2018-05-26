package com.ameerhamza6733.audioBooksFreeOnlineListen.activitys;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ameerhamza6733.audioBooksFreeOnlineListen.R;
import com.ameerhamza6733.audioBooksFreeOnlineListen.fragment.OfflineSavedBookFragment;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class OfflineSavedBooksActivity extends AppCompatActivity {
private String TAG ="OfflineSavedBooksActivity";
    private FragmentManager fragmentManager;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("B94C1B8999D3B59117198A259685D4F8").build();
        mAdView.loadAd(adRequest);
        if (savedInstanceState==null){
           OfflineSavedBookFragment offlineSavedBookFragment = new OfflineSavedBookFragment();
            startFragmentTransction(offlineSavedBookFragment);
        }


    }
    private void startFragmentTransction(Fragment fragment) {
         fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_contaner, fragment);
        fragmentTransaction.commit();
    }


}
