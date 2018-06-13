package com.ameerhamza6733.audioBooksFreeOnlineListen.fragment;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ameerhamza6733.audioBooksFreeOnlineListen.MySharedPref;
import com.ameerhamza6733.audioBooksFreeOnlineListen.R;
import com.ameerhamza6733.audioBooksFreeOnlineListen.Util;
import com.ameerhamza6733.audioBooksFreeOnlineListen.activitys.DownloaderActivty;
import com.ameerhamza6733.audioBooksFreeOnlineListen.adupters.SpinAdapter;
import com.ameerhamza6733.audioBooksFreeOnlineListen.mediaPlayer.PlayerForegroundService;
import com.ameerhamza6733.audioBooksFreeOnlineListen.models.AudioBook;
import com.ameerhamza6733.audioBooksFreeOnlineListen.models.MataData;
import com.ameerhamza6733.audioBooksFreeOnlineListen.viewModels.BookChapterViewModel;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.ads.consent.ConsentForm;
import com.google.ads.consent.ConsentFormListener;
import com.google.ads.consent.ConsentInfoUpdateListener;
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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.ameerhamza6733.audioBooksFreeOnlineListen.activitys.DetailTabActivity.KEY_SHARD_PREF_AUDIO_BOOK;
import static com.ameerhamza6733.audioBooksFreeOnlineListen.mediaPlayer.PlayerForegroundService.EXTRA_URI;
import static com.ameerhamza6733.audioBooksFreeOnlineListen.mediaPlayer.PlayerForegroundService.STOP_ACTION;
import static com.ameerhamza6733.audioBooksFreeOnlineListen.mediaPlayer.PlayerForegroundService.player;


/**
 * Created by AmeerHamza on 4/23/18.
 */

public class PlayerFragment extends Fragment {
    public static final String BROADCAST_ACTION_BUFFRING = "ACTION_BUFFRING";
    public static final String BROADCAST_ACTION_PLAYER_START = "BROADCAST_ACTION_PLAYER_START";
    public static final String BROADCAST_ACTION_PLAYER_Closed = "BROADCAST_ACTION_PLAYER_Closed";
    public static final String BROADCAST_ACTION_SHOW_AD = "BROADCAST_ACTION_SHOW_AD";

    private static final String TAG = "PlayerFragment";
    Handler handler = new Handler();
    private Spinner mySpinner;
    private AudioBook audioBook;
    private TextView tvTitle;
    private ImageView imageView;
    private ProgressBar progressBar;
    private ProgressBar PlayerStatePB;
    private RelativeLayout rvBackground;
    private View rootView;
    private AdView mAdView;
    private PlayerControlView mPlayerView;
    private InterstitialAd mInterstitialAd;
    private FloatingActionButton fabDownload;

    private BroadcastReceiver receiver;
    private IntentFilter filter;
    private MataData currentMataData;
    private ArrayList<MataData> mataDataList;
    private RewardedVideoAd mRewardedVideoAd;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filter = new IntentFilter();
        filter.addAction(BROADCAST_ACTION_BUFFRING);
        filter.addAction(BROADCAST_ACTION_PLAYER_START);
        filter.addAction(BROADCAST_ACTION_PLAYER_Closed);
        filter.addAction(BROADCAST_ACTION_SHOW_AD);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "action recived: " + intent.getAction());
                //do something based on the intent's action
                if (intent.getAction().equals(BROADCAST_ACTION_BUFFRING)) {

                    if (PlayerStatePB != null)
                        PlayerStatePB.setVisibility(View.VISIBLE);

                } else if (intent.getAction().equals(BROADCAST_ACTION_PLAYER_START)) {

                    if (PlayerStatePB != null)
                        PlayerStatePB.setVisibility(View.GONE);
                    if (player!=null)
                    mPlayerView.setPlayer(PlayerForegroundService.player);

                } else if (intent.getAction().equals(BROADCAST_ACTION_SHOW_AD)) {
                    if (mRewardedVideoAd!=null && mRewardedVideoAd.isLoaded()) {
                        mRewardedVideoAd.show();
                    }else {
                        if (mInterstitialAd != null && mInterstitialAd.isLoaded())
                            mInterstitialAd.show();
                    }

                } else if (intent.getAction().equals(STOP_ACTION)) {

                    if (mPlayerView != null) {
                        mPlayerView.setPlayer(null);
                    }
                }
            }
        };

    }

    @Override
    public void onResume() {
        getActivity().registerReceiver(receiver, filter);
        super.onResume();
    }

    @Override
    public void onPause() {
        if (receiver != null) {
            getActivity().unregisterReceiver(receiver);
            receiver = null;
        }
        super.onPause();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.player_fragment, container, false);
        audioBook = MySharedPref.getSavedObjectFromPreference(getActivity().getApplicationContext(), MySharedPref.SHARD_PREF_AUDIO_BOOK_FILE_NAME, KEY_SHARD_PREF_AUDIO_BOOK, AudioBook.class);

        bindViews(rootView);
        intiDataSet();
        checkForConsent();
         //loadAd();
        return rootView;
    }

    private void loadAd() {

        mAdView = rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("B94C1B8999D3B59117198A259685D4F8")
                .build();
        mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId("ca-app-pub-5168564707064012/3352880988");
        mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("B94C1B8999D3B59117198A259685D4F8").build());
        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("B94C1B8999D3B59117198A259685D4F8").build());
            }
        });

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getActivity());
        mRewardedVideoAd.loadAd("ca-app-pub-5168564707064012/7558135093", new AdRequest.Builder().addTestDevice("B94C1B8999D3B59117198A259685D4F8").build());


    }


    private void intiDataSet() {
        progressBar.setVisibility(View.VISIBLE);
        //  this.tvPlay.setVisibility(View.GONE);
        this.PlayerStatePB.setVisibility(View.VISIBLE);
        BookChapterViewModel model = ViewModelProviders.of(this).get(BookChapterViewModel.class);
        if (audioBook.getIdentifier() != null)
            model.loadData(Volley.newRequestQueue(getActivity()), Util.INSTANCE.toMetaDataURI(audioBook.getIdentifier()), audioBook.getIdentifier()).observe(this, mataDataList -> {
                // update UI
                if (mataDataList != null) {
                    this.mataDataList = (ArrayList<MataData>) mataDataList;
                    SetToSpinner(mataDataList);
                    progressBar.setVisibility(View.GONE);
                } else {
                    this.progressBar.setVisibility(View.GONE);
                    this.PlayerStatePB.setVisibility(View.GONE);
                    Snackbar.make(progressBar, "Error: ", Snackbar.LENGTH_INDEFINITE)
                            .setAction("try again", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    intiDataSet();
                                }
                            })

                            .show();
                }


            });
    }

    private void SetToSpinner(List<MataData> mataDataList) {
        SpinAdapter adapter = new SpinAdapter(getActivity(),
                R.layout.each_audio_file_spinner,
                mataDataList);

        mySpinner.setAdapter(adapter); // Set the custom adapter to the spinner
        // You can create an anonymous listener to handle the event when is selected an spinner item
        Spinner spinner;
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                currentMataData = adapter.getItem(position);
                long seelTo = Util.INSTANCE.isResumeAble(getActivity(), currentMataData.getURL());
                if (seelTo > 1)
                    ResumeTrackPermistionDialogFragment(seelTo);
                else {
                    startPlayerService(currentMataData, PlayerForegroundService.STOP_ACTION, null, 0);

                    handler.postDelayed(() -> {
                        startPlayerService(currentMataData, PlayerForegroundService.ACTION_START, null, 0);

                    }, 2000);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });
    }

    private void bindViews(View view) {
        tvTitle = view.findViewById(R.id.title);
        imageView = view.findViewById(R.id.iamge);
        rvBackground = view.findViewById(R.id.RVimageView);
        progressBar = view.findViewById(R.id.prograss_bar);
        tvTitle.setText(audioBook.getTitle());
        PlayerStatePB = view.findViewById(R.id.plater_State_prograss_bar);
        fabDownload = view.findViewById(R.id.fab_download);
        mySpinner = (Spinner) rootView.findViewById(R.id.spinner1);
        mPlayerView = rootView.findViewById(R.id.player_view);


        Glide.with(this).asBitmap()
                .load(Util.INSTANCE.toImageURI(audioBook.getIdentifier()))
                .apply(new RequestOptions().override(8, 8))
                .into(new SimpleTarget<Bitmap>(rvBackground.getWidth(), rvBackground.getHeight()) {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        imageView.setImageBitmap(resource);
                        Drawable drawable = new BitmapDrawable(getActivity().getResources(), resource);
                        rvBackground.setBackground(drawable);
                    }
                });
        Glide.with(this)
                .asBitmap()
                .load(Util.INSTANCE.toImageURI(audioBook.getIdentifier()))
                .into(imageView);

        fabDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mataDataList != null && mataDataList.size() > 0) {
                    Intent downloadingIntent = new Intent(v.getContext(), DownloaderActivty.class);
                    downloadingIntent.putParcelableArrayListExtra(DownloaderActivty.EXTRA_MATA_DATA_LIST, mataDataList);

                    v.getContext().startActivity(downloadingIntent);
                } else {
                    Toast.makeText(getActivity(), "Please wait ", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void startPlayerService(MataData mataData, String Action, String extraKey, long miliSecond) {

        try {
            Intent startIntent = new Intent(getActivity(), PlayerForegroundService.class);
            if (mataData != null) {
                startIntent.putExtra(EXTRA_URI, mataData.getURL());
                startIntent.putExtra(PlayerForegroundService.EXTRA_TITLE, mataData.getName());
            }
            startIntent.putExtra(extraKey, miliSecond);
            startIntent.setAction(Action);
            if (Build.VERSION.SDK_INT>25){
                getActivity().startForegroundService(startIntent);
            }else
            getActivity().startService(startIntent);
        } catch (Exception e) {
        }
    }



    public void ResumeTrackPermistionDialogFragment(long seekTo) {
        Log.d(TAG, "trying to ruesume the track ");
        try {
            final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                    .setTitle("Do you want resume track ")
                    .setPositiveButton("OK", (dialog, which) -> {
                        startPlayerService(currentMataData, PlayerForegroundService.STOP_ACTION, null, 0);

                        handler.postDelayed(() -> {
                            startPlayerService(currentMataData, PlayerForegroundService.ACTION_START, PlayerForegroundService.EXTRA_SEEK_TO, seekTo);

                        }, 2000);
                    })
                    .setNegativeButton("NO", (dialog, which) -> {
                        startPlayerService(currentMataData, PlayerForegroundService.STOP_ACTION, null, 0);

                        handler.postDelayed(() -> {
                            startPlayerService(currentMataData, PlayerForegroundService.ACTION_START, null, 0);

                        }, 2000);
                    })
                    .create();
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    //======================================

    private void checkForConsent() {
        ConsentInformation consentInformation = ConsentInformation.getInstance(getActivity());
       // ConsentInformation.getInstance(getActivity()).addTestDevice("B94C1B8999D3B59117198A259685D4F8");

        String[] publisherIds = {"pub-5168564707064012"};
        consentInformation.requestConsentInfoUpdate(publisherIds, new ConsentInfoUpdateListener() {
            @Override
            public void onConsentInfoUpdated(ConsentStatus consentStatus) {
                // User's consent status successfully updated.
                switch (consentStatus) {
                    case PERSONALIZED:
                        Log.d(TAG, "Showing Personalized ads");
                        showPersonalizedAds();
                        break;
                    case NON_PERSONALIZED:
                        Log.d(TAG, "Showing Non-Personalized ads");
                        showNonPersonalizedAds();
                        break;
                    case UNKNOWN:
                        Log.d(TAG, "Requesting Consent");
                        if (ConsentInformation.getInstance(getActivity())
                                .isRequestLocationInEeaOrUnknown()) {
                            requestConsent();
                        } else {
                            showPersonalizedAds();
                        }
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailedToUpdateConsentInfo(String errorDescription) {
                // User's consent status failed to update.
            }
        });
    }
ConsentForm form;
    private void requestConsent() {
        URL privacyUrl = null;
        try {
            // TODO: Replace with your app's privacy policy URL.
            privacyUrl = new URL("http://alphapk6733.blogspot.com/2018/05/privacy-policy-for-audio-book.html");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            // Handle error.
        }
        form = new ConsentForm.Builder(getActivity(), privacyUrl)
                .withListener(new ConsentFormListener() {
                    @Override
                    public void onConsentFormLoaded() {
                        // Consent form loaded successfully.
                        Log.d(TAG, "Requesting Consent: onConsentFormLoaded");
                        showForm();
                    }

                    @Override
                    public void onConsentFormOpened() {
                        // Consent form was displayed.
                        Log.d(TAG, "Requesting Consent: onConsentFormOpened");
                    }

                    @Override
                    public void onConsentFormClosed(
                            ConsentStatus consentStatus, Boolean userPrefersAdFree) {
                        Log.d(TAG, "Requesting Consent: onConsentFormClosed");
                        if (userPrefersAdFree) {
                            // Buy or Subscribe
                            Log.d(TAG, "Requesting Consent: User prefers AdFree");
                        } else {
                            Log.d(TAG, "Requesting Consent: Requesting consent again");
                            switch (consentStatus) {
                                case PERSONALIZED:
                                    showPersonalizedAds();break;
                                case NON_PERSONALIZED:
                                    showNonPersonalizedAds();break;
                                case UNKNOWN:
                                    showNonPersonalizedAds();break;
                            }

                        }
                        // Consent form was closed.
                    }

                    @Override
                    public void onConsentFormError(String errorDescription) {
                        Log.d(TAG, "Requesting Consent: onConsentFormError. Error - " + errorDescription);
                        // Consent form error.
                    }
                })
                .withPersonalizedAdsOption()
                .withNonPersonalizedAdsOption()
                .withAdFreeOption()
                .build();
        form.load();
    }

    private void showPersonalizedAds() {
        ConsentInformation.getInstance(getActivity()).setConsentStatus(ConsentStatus.PERSONALIZED);
        mAdView = rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("B94C1B8999D3B59117198A259685D4F8")
                .build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
             Log.d(TAG,"onAdLoaded() CALLBACK");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                super.onAdFailedToLoad(errorCode);
                Log.d(TAG,"onAdFailedToLoad() CALLBACK code: " + errorCode);
            }
        });
        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId("ca-app-pub-5168564707064012/3352880988");
        mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("B94C1B8999D3B59117198A259685D4F8").build());
        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("B94C1B8999D3B59117198A259685D4F8").build());
            }
        });

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getActivity());
        mRewardedVideoAd.loadAd("ca-app-pub-5168564707064012/7558135093", new AdRequest.Builder().addTestDevice("B94C1B8999D3B59117198A259685D4F8").build());


    }

    private void showNonPersonalizedAds() {
        ConsentInformation.getInstance(getActivity()).setConsentStatus(ConsentStatus.NON_PERSONALIZED);
        mAdView = rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("B94C1B8999D3B59117198A259685D4F8")
                .build();
        mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId("ca-app-pub-5168564707064012/3352880988");
        mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("B94C1B8999D3B59117198A259685D4F8") .addNetworkExtrasBundle(AdMobAdapter.class, getNonPersonalizedAdsBundle()).build());
        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("B94C1B8999D3B59117198A259685D4F8") .addNetworkExtrasBundle(AdMobAdapter.class, getNonPersonalizedAdsBundle()).build());
            }
        });

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getActivity());
        mRewardedVideoAd.loadAd("ca-app-pub-5168564707064012/7558135093", new AdRequest.Builder().addTestDevice("B94C1B8999D3B59117198A259685D4F8") .addNetworkExtrasBundle(AdMobAdapter.class, getNonPersonalizedAdsBundle()).build());


    }
    public Bundle getNonPersonalizedAdsBundle() {
        Bundle extras = new Bundle();
        extras.putString("npa", "1");

        return extras;
    }
    private void showForm() {
        if (form == null) {
            Log.d(TAG, "Consent form is null");
        }
        if (form != null) {
            Log.d(TAG, "Showing consent form");
            form.show();
        } else {
            Log.d(TAG, "Not Showing consent form");
        }
    }
}
