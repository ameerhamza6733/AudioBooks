package com.ameerhamza6733.businessaudiobook.activitys;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ameerhamza6733.businessaudiobook.R;
import com.ameerhamza6733.businessaudiobook.fragment.BookMetaDataRecylerView;
import com.ameerhamza6733.businessaudiobook.fragment.RecyclerViewFragment;

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
}
