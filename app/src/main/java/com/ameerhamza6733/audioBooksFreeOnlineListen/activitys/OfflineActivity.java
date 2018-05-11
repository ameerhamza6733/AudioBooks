package com.ameerhamza6733.audioBooksFreeOnlineListen.activitys;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.ameerhamza6733.audioBooksFreeOnlineListen.R;
import com.ameerhamza6733.audioBooksFreeOnlineListen.fragment.OfflineBookFragment;

public class OfflineActivity extends AppCompatActivity {
private String TAG ="OfflineActivity";
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState==null){
           OfflineBookFragment offlineBookFragment= new OfflineBookFragment();
            startFragmentTransction(offlineBookFragment);
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
