package com.ameerhamza6733.audioBooksFreeOnlineListen.activitys;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ameerhamza6733.audioBooksFreeOnlineListen.R;
import com.ameerhamza6733.audioBooksFreeOnlineListen.fragment.FragmetnHistory;
import com.ameerhamza6733.audioBooksFreeOnlineListen.fragment.RecyclerViewFragment;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Fragment fragment =  new FragmetnHistory();
        Bundle bundle = new Bundle();
        bundle.putString("dataType", "history");
        fragment.setArguments(bundle);
        startFragmentTransction(fragment);
    }

    private void startFragmentTransction(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_contaner, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
