package com.ameerhamza6733.businessaudiobook.activitys;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ameerhamza6733.businessaudiobook.R;
import com.ameerhamza6733.businessaudiobook.fragment.RecyclerViewFragment;


public class MainActivity extends AppCompatActivity {
    ;
    public static String TAG = "MainActivityTAG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_contaner, new RecyclerViewFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}






