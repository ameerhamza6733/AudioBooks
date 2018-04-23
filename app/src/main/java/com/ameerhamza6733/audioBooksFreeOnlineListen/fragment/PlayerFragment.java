package com.ameerhamza6733.audioBooksFreeOnlineListen.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.util.List;

import static com.ameerhamza6733.audioBooksFreeOnlineListen.activitys.DetailTabActivity.KEY_SHARD_PREF_AUDIO_BOOK;
import static com.ameerhamza6733.audioBooksFreeOnlineListen.mediaPlayer.PlayerForegroundService.EXTRA_URI;
import static com.ameerhamza6733.audioBooksFreeOnlineListen.mediaPlayer.PlayerForegroundService.FAST_FORWARD_ACTION;
import static com.ameerhamza6733.audioBooksFreeOnlineListen.mediaPlayer.PlayerForegroundService.FAST_REWIND_ACTION;

/**
 * Created by apple on 4/23/18.
 */

public class PlayerFragment extends Fragment {
    private Spinner mySpinner;
    private AudioBook audioBook;
    private TextView tvTitle;
    private TextView tVrewind;
    private TextView tVFarword;
    private ImageView imageView;
    private ProgressBar progressBar;
    private RelativeLayout rvBackground;
    private MataData mataData;
    private View rootView;

    Handler handler = new Handler();
    private AdView mAdView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.player_fragment, container, false);
        audioBook = MySharedPref.getSavedObjectFromPreference(getActivity().getApplicationContext(), MySharedPref.SHARD_PREF_AUDIO_BOOK, KEY_SHARD_PREF_AUDIO_BOOK, AudioBook.class);
        mySpinner = (Spinner) rootView.findViewById(R.id.spinner1);
        bindViews(rootView);
        intiDataSet();
        loadAd();
        return rootView;
    }

    private void loadAd() {

        mAdView = rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
       


    }

    private void intiDataSet() {
        progressBar.setVisibility(View.VISIBLE);
        MetaDataViewModel model = ViewModelProviders.of(this).get(MetaDataViewModel.class);
        if (audioBook.getIdentifier() != null)
            model.loadData(Volley.newRequestQueue(getActivity()), Util.INSTANCE.toMetaDataURI(audioBook.getIdentifier()), audioBook.getIdentifier()).observe(this, audioFileList -> {
                // update UI
                if (audioFileList != null) {
                    SetToSpinner(audioFileList);
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Error:", Toast.LENGTH_LONG).show();
                }


            });
    }

    private void SetToSpinner(List<MataData> mataDataList) {
        SpinAdapter adapter = new SpinAdapter(getActivity(),
                R.layout.each_audio_file_spinner,
                mataDataList);

        mySpinner.setAdapter(adapter); // Set the custom adapter to the spinner
        // You can create an anonymous listener to handle the event when is selected an spinner item
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {

                rootView.findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       stopPlayerService(adapter.getItem(position));
                        handler.postDelayed(() -> startPlayerService(adapter.getItem(position), PlayerForegroundService.START_FOREGROUND_ACTION, null, 0), 2000);
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });
    }

    private void bindViews(View view) {
        tvTitle = view.findViewById(R.id.title);
        tVrewind = view.findViewById(R.id.fast_rewind);
        tVFarword = view.findViewById(R.id.fast_forword);
        imageView = view.findViewById(R.id.iamge);
        rvBackground = view.findViewById(R.id.RVimageView);
        progressBar = view.findViewById(R.id.prograss_bar);
        tvTitle.setText(audioBook.getTitle());

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

        tVrewind.setOnClickListener(v -> startPlayerService(null, FAST_REWIND_ACTION, PlayerForegroundService.EXTRA_REWIND_MILI_SECOND, 10000));
        tVFarword.setOnClickListener(v -> startPlayerService(null, FAST_FORWARD_ACTION, PlayerForegroundService.EXTRA_FAST_FORWARD_MILI_SECONDS, 30000));

    }

    private void startPlayerService(MataData mataData, String Action, String extraKey, int miliSecond) {
        Intent startIntent = new Intent(getActivity(), PlayerForegroundService.class);
        if (mataData != null) {
            startIntent.putExtra(EXTRA_URI, mataData.getURL());
            startIntent.putExtra(PlayerForegroundService.EXTRA_TITLE, mataData.getName());
        }
        startIntent.putExtra(extraKey, miliSecond);
        startIntent.setAction(Action);
        getActivity().startService(startIntent);
    }

    private void stopPlayerService(MataData mataData) {
        Intent startIntent = new Intent(getActivity(), PlayerForegroundService.class);
        startIntent.putExtra(EXTRA_URI, mataData.getURL());
        startIntent.setAction(PlayerForegroundService.STOP_ACTION);
        getActivity().startService(startIntent);
    }
}
