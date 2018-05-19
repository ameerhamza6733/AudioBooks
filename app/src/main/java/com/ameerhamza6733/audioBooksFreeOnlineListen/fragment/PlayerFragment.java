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
import com.ameerhamza6733.audioBooksFreeOnlineListen.viewModels.MetaDataViewModel;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.List;

import static com.ameerhamza6733.audioBooksFreeOnlineListen.activitys.DetailTabActivity.KEY_SHARD_PREF_AUDIO_BOOK;
import static com.ameerhamza6733.audioBooksFreeOnlineListen.mediaPlayer.PlayerForegroundService.EXTRA_URI;
import static com.ameerhamza6733.audioBooksFreeOnlineListen.mediaPlayer.PlayerForegroundService.SERVICE_STAARTED;


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
   // private TextView tVrewind;
    //private TextView tVFarword;
   // private TextView tvPlay;
   // private TextView tvPass;
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
//                    if (tvPlay != null) {
//                        tvPlay.setVisibility(View.GONE);
                        if (PlayerStatePB != null)
                            PlayerStatePB.setVisibility(View.VISIBLE);
//                    }
                } else if (intent.getAction().equals(BROADCAST_ACTION_PLAYER_START)) {

                    if (PlayerStatePB != null)
                        PlayerStatePB.setVisibility(View.GONE);
//                    if (tvPass != null)
//                        tvPass.setVisibility(View.VISIBLE);
                    try{
                       mPlayerView.setPlayer(PlayerForegroundService.player);
                    }catch (Exception e){}
                } else if (intent.getAction().equals(SERVICE_STAARTED)) {
//                    if (tvPlay != null)
//                        tvPlay.setVisibility(View.INVISIBLE);
                } else if (intent.getAction().equals(BROADCAST_ACTION_PLAYER_Closed)) {
//                    tvPass.setVisibility(View.GONE);
//                    tvPlay.setVisibility(View.VISIBLE);

                } else if (intent.getAction().equals(BROADCAST_ACTION_SHOW_AD)) {
                    if (mInterstitialAd != null && mInterstitialAd.isLoaded())
                        mInterstitialAd.show();
                }
            }
        };

    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(receiver, filter);
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
        loadAd();
        return rootView;
    }

    private void loadAd() {

        mAdView = rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("B94C1B8999D3B59117198A259685D4F8").build();
        mAdView.loadAd(adRequest);
        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId("ca-app-pub-5168564707064012/3352880988");
        mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("B94C1B8999D3B59117198A259685D4F8").build());


    }



    private void intiDataSet() {
        progressBar.setVisibility(View.VISIBLE);
      //  this.tvPlay.setVisibility(View.GONE);
        this.PlayerStatePB.setVisibility(View.VISIBLE);
        MetaDataViewModel model = ViewModelProviders.of(this).get(MetaDataViewModel.class);
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
                    stopPlayerService(currentMataData);
                    handler.postDelayed(() -> {
                        startPlayerService(currentMataData, PlayerForegroundService.ACTION_START, null, 0);

                    }, 2000);
                }
//

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });
    }

    private void bindViews(View view) {
        tvTitle = view.findViewById(R.id.title);
//        tVrewind = view.findViewById(R.id.fast_rewind);
//        tVFarword = view.findViewById(R.id.fast_forword);
//        tvPlay = view.findViewById(R.id.play);
//        tvPass = view.findViewById(R.id.pass);
        imageView = view.findViewById(R.id.iamge);
        rvBackground = view.findViewById(R.id.RVimageView);
        progressBar = view.findViewById(R.id.prograss_bar);
        tvTitle.setText(audioBook.getTitle());
        PlayerStatePB = view.findViewById(R.id.plater_State_prograss_bar);
        fabDownload = view.findViewById(R.id.fab_download);
        mySpinner = (Spinner) rootView.findViewById(R.id.spinner1);
       mPlayerView= rootView.findViewById(R.id.player_view);


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

      //  tVrewind.setOnClickListener(v -> startPlayerService(currentMataData, FAST_REWIND_ACTION, PlayerForegroundService.EXTRA_REWIND_MILI_SECOND, 10000));
        //tVFarword.setOnClickListener(v -> startPlayerService(currentMataData, FAST_FORWARD_ACTION, PlayerForegroundService.EXTRA_FAST_FORWARD_MILI_SECONDS, 30000));

//        tvPass.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                tvPlay.setVisibility(View.VISIBLE);
//                handler.postDelayed(() -> tvPass.setVisibility(View.GONE), 500);
//                startPlayerService(currentMataData, PlayerForegroundService.PLAYER_PLAY_PAUSE_ACTION, null, 0);
//                if (mInterstitialAd != null && mInterstitialAd.isLoaded())
//                    mInterstitialAd.show();
//            }
//        });

//        tvPlay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //
//                if (PlayerForegroundService.isPlaying) {
//                    startPlayerService(currentMataData, PlayerForegroundService.PLAYER_PLAY_PAUSE_ACTION, null, 0);
//                } else {
//                    stopPlayerService(currentMataData);
//                    handler.postDelayed(() -> {
//                        startPlayerService(currentMataData, PlayerForegroundService.ACTION_START, null, 0);
//
//                    }, 2000);
//                }
//
//
//            }
//        });
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
            getActivity().startService(startIntent);
        } catch (Exception e) {
        }
    }

    private void stopPlayerService(MataData mataData) {
        Intent startIntent = new Intent(getActivity(), PlayerForegroundService.class);
        startIntent.putExtra(EXTRA_URI, mataData.getURL());
        startIntent.setAction(PlayerForegroundService.STOP_ACTION);
        getActivity().startService(startIntent);
    }

    public void ResumeTrackPermistionDialogFragment(long seekTo) {
        Log.d(TAG, "trying to ruesume the track ");
        try {
            final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                    .setTitle("Do you want resume track ")
                    .setPositiveButton("OK", (dialog, which) -> {
                        stopPlayerService(currentMataData);
                        handler.postDelayed(() -> {
                            startPlayerService(currentMataData, PlayerForegroundService.ACTION_START, PlayerForegroundService.EXTRA_SEEK_TO, seekTo);

                        }, 2000);
                    })
                    .setNegativeButton("NO", (dialog, which) -> {
                        stopPlayerService(currentMataData);
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
}
