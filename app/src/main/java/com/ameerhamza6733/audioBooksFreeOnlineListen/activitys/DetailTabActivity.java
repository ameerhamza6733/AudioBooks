package com.ameerhamza6733.audioBooksFreeOnlineListen.activitys;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ameerhamza6733.audioBooksFreeOnlineListen.R;
import com.ameerhamza6733.audioBooksFreeOnlineListen.adupters.MyPagerAdupter;


public class DetailTabActivity extends AppCompatActivity {


    public static final String KEY_SHARD_PREF_AUDIO_BOOK = "KEY_SHARD_PREF_AUDIO_BOOK";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(new MyPagerAdupter(getSupportFragmentManager()) {
        });
        TabLayout mTabLayout = findViewById(R.id.simpleTabLayout);
        mTabLayout.setupWithViewPager(viewPager);
    }


}
