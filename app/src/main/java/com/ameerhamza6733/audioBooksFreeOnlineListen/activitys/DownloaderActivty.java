package com.ameerhamza6733.audioBooksFreeOnlineListen.activitys;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Toast;

import com.ameerhamza6733.audioBooksFreeOnlineListen.R;
import com.ameerhamza6733.audioBooksFreeOnlineListen.adupters.DownloadingAdupter;
import com.ameerhamza6733.audioBooksFreeOnlineListen.mediaPlayer.PlayerForegroundService;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardedVideoAd;

import cn.woblog.android.downloader.DownloadService;
import cn.woblog.android.downloader.callback.DownloadManager;
import lolodev.permissionswrapper.callback.OnRequestPermissionsCallBack;
import lolodev.permissionswrapper.wrapper.PermissionWrapper;

import static com.ameerhamza6733.audioBooksFreeOnlineListen.fragment.ChaptersFragment.BROADCAST_ACTION_PLAYER_START;
import static com.ameerhamza6733.audioBooksFreeOnlineListen.mediaPlayer.PlayerForegroundService.player;


public class DownloaderActivty extends AppCompatActivity {
    public static final String EXTRA_MATA_DATA_LIST = "EXTRA_MATA_DATA_LIST";
    public static final String EXTRA_BOOK_INDEX = "EXTRA_BOOK_INDEX";
    public static DownloadManager downloadManager;
    private RecyclerView recyclerView;
    private int bookNumber;
    private BroadcastReceiver receiver;
    private IntentFilter filter;
    private AdView mAdView;
    private PlayerControlView playerView;
    private FloatingActionButton fabStop;
    private InterstitialAd mInterstitialAd;


    @Override
    public void onResume() {
        this.registerReceiver(receiver, filter);
        super.onResume();
    }

    @Override
    public void onPause() {
        if (receiver != null) {
           this.unregisterReceiver(receiver);
            receiver = null;
        }
        super.onPause();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloader_activty);
        filter = new IntentFilter();

        filter.addAction(BROADCAST_ACTION_PLAYER_START);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                 if (intent.getAction().equals(BROADCAST_ACTION_PLAYER_START)) {
                    fabStop.setVisibility(View.VISIBLE);
                    if (player != null) {
                        playerView.setVisibility(View.VISIBLE);
                        playerView.setPlayer(PlayerForegroundService.player);
                    }

                }
            }
        };
        bookNumber = getIntent().getIntExtra(EXTRA_BOOK_INDEX, 0);
        mAdView = findViewById(R.id.adView);
        playerView = findViewById(R.id.playerView);
        fabStop = findViewById(R.id.fabStop);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("B94C1B8999D3B59117198A259685D4F8").build();
        mAdView.loadAd(adRequest);
        if (PlayerForegroundService.player != null) {
            playerView.setVisibility(View.VISIBLE);
            fabStop.setVisibility(View.VISIBLE);
            playerView.setPlayer(PlayerForegroundService.player);
        }
        fabStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPlayerService(PlayerForegroundService.STOP_ACTION,0);
                playerView.setVisibility(View.INVISIBLE);
                fabStop.setVisibility(View.INVISIBLE);
            }
        });
        if (Build.VERSION.SDK_INT > 22) {
            checkRunTimePermission();
        } else
            setRecylerView();
        mInterstitialAd = new InterstitialAd(this);
        if (ConsentInformation.getInstance(this).getConsentStatus() == ConsentStatus.PERSONALIZED){
            showPersonlalizedAds();
        }else if (ConsentInformation.getInstance(this).getConsentStatus() == ConsentStatus.NON_PERSONALIZED){
            showNonPersonlzlizedAds();
        }

    }

    private void startPlayerService(String Action, long miliSecond) {


        Intent startIntent = new Intent(this, PlayerForegroundService.class);

        startIntent.putExtra(PlayerForegroundService.KEY_PREFF_CURRENT_TRACK_INDEX, miliSecond);
        if (Action != null)
            startIntent.setAction(Action);

        if (Build.VERSION.SDK_INT > 25) {
            this.startForegroundService(startIntent);
        } else
            this.startService(startIntent);

    }
    private void checkRunTimePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            new PermissionWrapper.Builder(this)
                    .addPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
                    //enable rationale message with a custom message

                    //show settings dialog,in this case with default message base on requested permission/s
                    .addPermissionsGoSettings(true)
                    //enable callback to know what option was choosen
                    .addRequestPermissionsCallBack(new OnRequestPermissionsCallBack() {
                        @Override
                        public void onGrant() {
                            setRecylerView();
                        }

                        @Override
                        public void onDenied(String permission) {
                            Toast.makeText(DownloaderActivty.this, "app need read and write storage permission for downloading", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }).build().request();
        } else {
            setRecylerView();
        }

    }

    private void setRecylerView() {
        downloadManager = DownloadService.getDownloadManager(this.getApplicationContext());
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DownloadingAdupter downloadingAdupter = new DownloadingAdupter( this);
        recyclerView.setAdapter(downloadingAdupter);
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



    }

    public Bundle getNonPersonalizedAdsBundle() {
        Bundle extras = new Bundle();
        extras.putString("npa", "1");

        return extras;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mInterstitialAd!=null && mInterstitialAd.isLoaded())
            mInterstitialAd.show();
        finish();
    }

}
