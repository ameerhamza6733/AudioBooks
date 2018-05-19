package com.ameerhamza6733.audioBooksFreeOnlineListen.activitys;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ameerhamza6733.audioBooksFreeOnlineListen.R;
import com.ameerhamza6733.audioBooksFreeOnlineListen.fragment.OfflineSavedBookFragment;

public class OfflineSavedBooksActivity extends AppCompatActivity {
private String TAG ="OfflineSavedBooksActivity";
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState==null){
           OfflineSavedBookFragment offlineSavedBookFragment = new OfflineSavedBookFragment();
            startFragmentTransction(offlineSavedBookFragment);
        }
        setContentView(R.layout.activity_offline);

    }
    private void startFragmentTransction(Fragment fragment) {
         fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_contaner, fragment);
        fragmentTransaction.commit();
    }


}
