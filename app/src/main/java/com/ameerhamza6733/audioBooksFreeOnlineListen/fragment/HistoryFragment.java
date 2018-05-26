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

import com.ameerhamza6733.audioBooksFreeOnlineListen.adupters.CustomAdapter;
import com.ameerhamza6733.audioBooksFreeOnlineListen.models.AudioBook;
import com.ameerhamza6733.audioBooksFreeOnlineListen.viewModels.HistoryViewModel;

import java.util.List;

import static com.ameerhamza6733.audioBooksFreeOnlineListen.MySharedPref.SHARD_PREF_HISTORY_AUDIO_BOOK_FILE_NAME;

/**
 * Created by apple on 5/12/18.
 */

public class HistoryFragment extends RecyclerViewFragment {
    private String TAG = "HistoryFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mySpinner.setVisibility(View.GONE);
        mySpinnerFilter.setVisibility(View.GONE);
        floatingActionButton.setVisibility(View.GONE);
        getHistory(SHARD_PREF_HISTORY_AUDIO_BOOK_FILE_NAME);
        try{
            ((AppCompatActivity)getActivity()).getSupportActionBar().show();
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Audio Books ");
            ((AppCompatActivity)getActivity()).getSupportActionBar() .setSubtitle("History");
        }catch (Exception e){
            e.printStackTrace();
        }
        return view;
    }


    private void getHistory(String key) {
        Log.d(TAG, "getHistory");
        HistoryViewModel historyViewModel = ViewModelProviders.of(getActivity()).get(HistoryViewModel.class);
        historyViewModel.getAllSavedAudioBooks(getActivity().getApplicationContext().getSharedPreferences(key, 0)).observe(this, new Observer<List<AudioBook>>() {
            @Override
            public void onChanged(@Nullable List<AudioBook> audioBooks) {
                if (audioBooks != null && audioBooks.size() > 0) {
                    progressBar.setVisibility(View.GONE);
                    CustomAdapter mAdapter = new CustomAdapter(audioBooks);
                    mRecyclerViewPoetry.setAdapter(mAdapter);
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "You don't have any history", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
