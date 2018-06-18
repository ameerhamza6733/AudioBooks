package com.ameerhamza6733.audioBooksFreeOnlineListen.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ameerhamza6733.audioBooksFreeOnlineListen.MySharedPref;
import com.ameerhamza6733.audioBooksFreeOnlineListen.R;
import com.ameerhamza6733.audioBooksFreeOnlineListen.Util;
import com.ameerhamza6733.audioBooksFreeOnlineListen.mediaPlayer.PlayerForegroundService;
import com.ameerhamza6733.audioBooksFreeOnlineListen.models.AudioBook;
import com.ameerhamza6733.audioBooksFreeOnlineListen.models.MataData;
import com.ameerhamza6733.audioBooksFreeOnlineListen.viewModels.BookChapterViewModel;
import com.android.volley.toolbox.Volley;
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
    ConsentForm form;
    private AudioBook audioBook;
    private ProgressBar progressBar;
    private ProgressBar playerStatePB;
    private RecyclerView recyclerView;
    private View rootView;
    private AdView mAdView;
    private PlayerControlView playerView;
    private InterstitialAd mInterstitialAd;
    private BroadcastReceiver receiver;
    private IntentFilter filter;
    private ArrayList<MataData> mataDataList;
    private RewardedVideoAd mRewardedVideoAd;
    private ChapterAudpter adupter;
    private FloatingActionButton fabStop;

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

                    if (playerStatePB != null)
                        playerStatePB.setVisibility(View.VISIBLE);

                } else if (intent.getAction().equals(BROADCAST_ACTION_PLAYER_START)) {
                    fabStop.setVisibility(View.VISIBLE);
                    playerStatePB.setVisibility(View.GONE);
                    if (player != null) {
                        playerView.setVisibility(View.VISIBLE);
                        playerView.setPlayer(PlayerForegroundService.player);
                    }

                } else if (intent.getAction().equals(BROADCAST_ACTION_SHOW_AD)) {
                    if (mRewardedVideoAd != null && mRewardedVideoAd.isLoaded()) {
                        mRewardedVideoAd.show();
                    } else {
                        if (mInterstitialAd != null && mInterstitialAd.isLoaded())
                            mInterstitialAd.show();
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
        bindViews(rootView);
        intiDataSet();
        checkForConsent();
        //loadAd();
        return rootView;
    }

    private void intiDataSet() {
        progressBar.setVisibility(View.VISIBLE);
        audioBook = MySharedPref.getSavedObjectFromPreference(getActivity().getApplicationContext(), MySharedPref.SHARD_PREF_AUDIO_BOOK_FILE_NAME, KEY_SHARD_PREF_AUDIO_BOOK, AudioBook.class);

        BookChapterViewModel model = ViewModelProviders.of(this).get(BookChapterViewModel.class);
        if (audioBook.getIdentifier() != null)
            model.loadData(Volley.newRequestQueue(getActivity()), Util.INSTANCE.toMetaDataURI(audioBook.getIdentifier()), audioBook.getIdentifier()).observe(this, mataDataList -> {
                // update UI
                if (mataDataList != null) {
                    this.mataDataList = (ArrayList<MataData>) mataDataList;
                    audioBook.setMataData(mataDataList);
                    MySharedPref.saveObjectToSharedPreference(getActivity(), MySharedPref.SHARD_PREF_AUDIO_BOOK_FILE_NAME, PlayerForegroundService.KEY_SHARD_PREF_AUDIO_BOOK, audioBook);
                    MySharedPref.saveObjectToSharedPreference(getActivity(), MySharedPref.SHARD_PREF_AUDIO_BOOK_FILE_NAME, KEY_SHARD_PREF_AUDIO_BOOK, audioBook);

                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    if (adupter == null) {
                        adupter = new PlayerFragment.ChapterAudpter();
                        recyclerView.setAdapter(adupter);
                    }
                    progressBar.setVisibility(View.GONE);


                } else {
                    this.progressBar.setVisibility(View.GONE);

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


    }

    private void bindViews(View view) {
        progressBar = view.findViewById(R.id.progressBarPlayer);
        playerStatePB = view.findViewById(R.id.playerProgrssbar);
        playerView = rootView.findViewById(R.id.playerView);
        recyclerView = view.findViewById(R.id.listViewPlayer);
        fabStop = view.findViewById(R.id.fabStop);
        if (player != null) {
            fabStop.setVisibility(View.VISIBLE);
            playerView.setVisibility(View.VISIBLE);
            playerView.setPlayer(PlayerForegroundService.player);
        }
        fabStop.setOnClickListener((view1) -> {
            startPlayerService(PlayerForegroundService.STOP_ACTION, 0);
            playerView.setVisibility(View.INVISIBLE);
            fabStop.setVisibility(View.INVISIBLE);
        });

    }

    private void startPlayerService(String Action, long miliSecond) {


        Intent startIntent = new Intent(getActivity(), PlayerForegroundService.class);

        startIntent.putExtra(PlayerForegroundService.KEY_PREFF_CURRENT_TRACK_INDEX, miliSecond);
        if (Action != null)
            startIntent.setAction(Action);

        if (Build.VERSION.SDK_INT > 25) {
            PlayerFragment.this.getActivity().startForegroundService(startIntent);
        } else
            getActivity().startService(startIntent);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

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

    //======================================

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
                                    showPersonalizedAds();
                                    break;
                                case NON_PERSONALIZED:
                                    showNonPersonalizedAds();
                                    break;
                                case UNKNOWN:
                                    showNonPersonalizedAds();
                                    break;
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
        mAdView = rootView.findViewById(R.id.adViewPlayer);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("B94C1B8999D3B59117198A259685D4F8")
                .build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.d(TAG, "onAdLoaded() CALLBACK");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                super.onAdFailedToLoad(errorCode);
                Log.d(TAG, "onAdFailedToLoad() CALLBACK code: " + errorCode);
            }
        });
        if(getActivity()!=null){

            mInterstitialAd = new InterstitialAd(getActivity());
            mInterstitialAd.setAdUnitId("ca-app-pub-5168564707064012/3352880988");
            mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("B94C1B8999D3B59117198A259685D4F8").build());
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("B94C1B8999D3B59117198A259685D4F8").build());
                }
            });

            mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getActivity());
            mRewardedVideoAd.loadAd("ca-app-pub-5168564707064012/7558135093", new AdRequest.Builder().addTestDevice("B94C1B8999D3B59117198A259685D4F8").build());

        }

    }

    private void showNonPersonalizedAds() {
        ConsentInformation.getInstance(getActivity()).setConsentStatus(ConsentStatus.NON_PERSONALIZED);
        mAdView = rootView.findViewById(R.id.playerView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("B94C1B8999D3B59117198A259685D4F8")
                .build();
        mAdView.loadAd(adRequest);

        if (getActivity()!=null){
            mInterstitialAd = new InterstitialAd(getActivity());
            mInterstitialAd.setAdUnitId("ca-app-pub-5168564707064012/3352880988");
            mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("B94C1B8999D3B59117198A259685D4F8").addNetworkExtrasBundle(AdMobAdapter.class, getNonPersonalizedAdsBundle()).build());
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("B94C1B8999D3B59117198A259685D4F8").addNetworkExtrasBundle(AdMobAdapter.class, getNonPersonalizedAdsBundle()).build());
                }
            });

            mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getActivity());
            mRewardedVideoAd.loadAd("ca-app-pub-5168564707064012/7558135093", new AdRequest.Builder().addTestDevice("B94C1B8999D3B59117198A259685D4F8").addNetworkExtrasBundle(AdMobAdapter.class, getNonPersonalizedAdsBundle()).build());

        }

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

    private class ChapterAudpter extends RecyclerView.Adapter<ChapterAudpter.ViewHolder> {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.each_offline_saved_book_mata_data, parent, false);

            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.textView.setText(mataDataList.get(position).getName());

        }

        @Override
        public int getItemCount() {
            return mataDataList.size();
        }

        protected class ViewHolder extends RecyclerView.ViewHolder {
            TextView textView;

            public ViewHolder(View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.tvTitle);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MySharedPref.saveObjectToSharedPreference(v.getContext(), MySharedPref.SHARD_PREF_AUDIO_BOOK_FILE_NAME, PlayerForegroundService.KEY_PREFF_CURRENT_TRACK_INDEX, String.valueOf(getAdapterPosition()));
                        MySharedPref.saveObjectToSharedPreference(v.getContext(), MySharedPref.SHARD_PREF_AUDIO_BOOK_FILE_NAME, PlayerForegroundService.IS_SOUCE_LOCAL_DISK, "0");
                        playerStatePB.setVisibility(View.VISIBLE);
                        if (player != null)
                            startPlayerService(PlayerForegroundService.ACTION_UPDATE_MEDIA_SOURCE, 0);
                        else {
                            startPlayerService(null, 0);
                        }
                        playerView.setVisibility(View.VISIBLE);
                    }
                });
            }
        }
    }


}
