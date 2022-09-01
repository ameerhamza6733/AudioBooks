package com.ameerhamza6733.audioBooksFreeOnlineListen.activitys;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.ameerhamza6733.audioBooksFreeOnlineListen.R;
import com.ameerhamza6733.audioBooksFreeOnlineListen.adupters.MyPagerAdupter;
import com.google.ads.consent.ConsentForm;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardedVideoAd;


public class DetailTabActivity extends AppCompatActivity {


    public static final String KEY_SHARD_PREF_AUDIO_BOOK = "KEY_SHARD_PREF_AUDIO_BOOK";
    private InterstitialAd mInterstitialAd;
    private RewardedVideoAd mRewardedVideoAd;
    private ConsentForm form;
    private String TAG="DetailTabActivity";

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
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
       if (ConsentInformation.getInstance(this).getConsentStatus() == ConsentStatus.PERSONALIZED){
           showPersonlalizedAds();
       }else if (ConsentInformation.getInstance(this).getConsentStatus() == ConsentStatus.NON_PERSONALIZED){
           showNonPersonlzlizedAds();
       }

    }

    private void showNonPersonlzlizedAds() {
        mInterstitialAd.setAdUnitId("ca-app-pub-5168564707064012/3352880988");
        mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("B94C1B8999D3B59117198A259685D4F8").build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, getNonPersonalizedAdsBundle()).addTestDevice("B94C1B8999D3B59117198A259685D4F8").build());
            }

        });

        mRewardedVideoAd.loadAd("ca-app-pub-5168564707064012/7558135093", new AdRequest.Builder().addTestDevice("B94C1B8999D3B59117198A259685D4F8") .addNetworkExtrasBundle(AdMobAdapter.class, getNonPersonalizedAdsBundle()).build());

    }

    private void showPersonlalizedAds() {
        mInterstitialAd.setAdUnitId("ca-app-pub-5168564707064012/3352880988");
        mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("B94C1B8999D3B59117198A259685D4F8").build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("B94C1B8999D3B59117198A259685D4F8").build());
            }

        });


        mRewardedVideoAd.loadAd("ca-app-pub-5168564707064012/7558135093", new AdRequest.Builder().addTestDevice("B94C1B8999D3B59117198A259685D4F8") .build());

    }

    public Bundle getNonPersonalizedAdsBundle() {
        Bundle extras = new Bundle();
        extras.putString("npa", "1");

        return extras;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mRewardedVideoAd!=null && mRewardedVideoAd.isLoaded()){
            mRewardedVideoAd.show();
        }else {
            if (mInterstitialAd!=null && mInterstitialAd.isLoaded())
                mInterstitialAd.show();
        }
        finish();
    }

}
