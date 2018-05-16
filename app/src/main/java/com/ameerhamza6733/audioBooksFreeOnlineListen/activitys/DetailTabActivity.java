package com.ameerhamza6733.audioBooksFreeOnlineListen.activitys;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ameerhamza6733.audioBooksFreeOnlineListen.R;
import com.ameerhamza6733.audioBooksFreeOnlineListen.adupters.MyPagerAdupter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;


public class DetailTabActivity extends AppCompatActivity {


    public static final String KEY_SHARD_PREF_AUDIO_BOOK = "KEY_SHARD_PREF_AUDIO_BOOK";
    private InterstitialAd mInterstitialAd;

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
        try {
            getSupportActionBar().hide();
        }catch (Exception e){}
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-5168564707064012/3352880988");
        mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("B94C1B8999D3B59117198A259685D4F8").build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("B94C1B8999D3B59117198A259685D4F8").build());
            }

        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mInterstitialAd!=null && mInterstitialAd.isLoaded())
            mInterstitialAd.show();
        finish();
    }
}
