package com.ameerhamza6733.audioBooksFreeOnlineListen.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ameerhamza6733.audioBooksFreeOnlineListen.MySharedPref;
import com.ameerhamza6733.audioBooksFreeOnlineListen.R;
import com.ameerhamza6733.audioBooksFreeOnlineListen.adupters.CustomAdapter;
import com.ameerhamza6733.audioBooksFreeOnlineListen.models.AudioBook;
import com.ameerhamza6733.audioBooksFreeOnlineListen.viewModels.BookMarkViewModel;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.List;

/**
 * Created by apple on 5/12/18.
 */

public class BookMarkFragemnt extends BookFragment {


    private AdView mAdView;
    private String TAG = "BookMarkFragemnt";
    private AdRequest adRequest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mySpinner.setVisibility(View.GONE);
        mySpinnerFilter.setVisibility(View.GONE);
        floatingActionButton.setVisibility(View.GONE);
        mAdView = view.findViewById(R.id.adViewBookMark);
        mAdView.setVisibility(View.VISIBLE);
        if (ConsentInformation.getInstance(getActivity()).getConsentStatus() == ConsentStatus.PERSONALIZED)
            adRequest = new AdRequest.Builder().addTestDevice("B94C1B8999D3B59117198A259685D4F8").build();
        else if (ConsentStatus.NON_PERSONALIZED == ConsentInformation.getInstance(getActivity()).getConsentStatus()) {

            adRequest = new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, getNonPersonalizedAdsBundle()).addTestDevice("B94C1B8999D3B59117198A259685D4F8").build();
        }
        if (adRequest != null)
            mAdView.loadAd(adRequest);


        getHistory(MySharedPref.SHARD_PREF_BOOK_MARK_FILE_NAME);
        try {
            ((AppCompatActivity) getActivity()).getSupportActionBar().show();
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Audio Books ");
            ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("Bookmarks");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    private void getHistory(String key) {
        Log.d(TAG, "BookMarkFragemnt");
        BookMarkViewModel BookMarkViewModel = ViewModelProviders.of(getActivity()).get(BookMarkViewModel.class);
        BookMarkViewModel.getAllSavedAudioBooks(getActivity().getApplicationContext().getSharedPreferences(key, 0)).observe(this, new Observer<List<AudioBook>>() {
            @Override
            public void onChanged(@Nullable List<AudioBook> audioBooks) {
                if (audioBooks != null && audioBooks.size() > 0) {
                    progressBar.setVisibility(View.GONE);
                    CustomAdapter mAdapter = new CustomAdapter(audioBooks);
                    recyclerView.setAdapter(mAdapter);
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "You don't have any Bookmarks", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public Bundle getNonPersonalizedAdsBundle() {
        Bundle extras = new Bundle();
        extras.putString("npa", "1");

        return extras;
    }

    @Override
    public void onPause() {
        super.onPause();

    }
}

