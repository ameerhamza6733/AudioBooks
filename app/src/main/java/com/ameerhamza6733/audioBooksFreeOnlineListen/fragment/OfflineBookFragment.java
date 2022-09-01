package com.ameerhamza6733.audioBooksFreeOnlineListen.fragment;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ameerhamza6733.audioBooksFreeOnlineListen.R;
import com.ameerhamza6733.audioBooksFreeOnlineListen.adupters.OfflineBookAdupter;
import com.ameerhamza6733.audioBooksFreeOnlineListen.models.AudioBook;
import com.ameerhamza6733.audioBooksFreeOnlineListen.viewModels.OfflineBooksViewModle;

import java.util.List;

import static com.ameerhamza6733.audioBooksFreeOnlineListen.MySharedPref.SHARD_PREF_DOWNLOADED_AUDIO_BOOK;

/**
 * Created by AmeerHamza on 5/9/18.
 */

public class OfflineBookFragment extends Fragment {
    private String TAG = "OfflineBookFragment";
    private View rootView;
    private List<AudioBook> audioBookList;
    private RecyclerView recyclerView;
    private OfflineBookAdupter offlineBookAdupter;

    public OfflineBookFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null)
            rootView = inflater.inflate(R.layout.offline_chapter_fragemnt, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerViewOfilineSavedBooks);
        Log.d(TAG, "onCreateView");
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        try {
            ((AppCompatActivity) getActivity()).getSupportActionBar().show();
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Downloaded Books");
        } catch (Exception e) {
            e.printStackTrace();
        }
        intiDataSet();
        return rootView;
    }

    private void intiDataSet() {
        OfflineBooksViewModle offlineBooksViewModle = ViewModelProviders.of(getActivity()).get(OfflineBooksViewModle.class);
        offlineBooksViewModle.getAllSavedAudioBooks(getActivity().getApplicationContext().getSharedPreferences(SHARD_PREF_DOWNLOADED_AUDIO_BOOK, 0)).observe(this, new Observer<List<AudioBook>>() {
            @Override
            public void onChanged(@Nullable List<AudioBook> audioBooks) {
                if (audioBooks != null && audioBooks.size() > 0) {
                    audioBookList = audioBooks;
                    setAupter();

                } else {
                    Toast.makeText(getActivity(), "You don't have any offline book", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setAupter() {
        offlineBookAdupter = new OfflineBookAdupter(audioBookList, getActivity());
        recyclerView.setAdapter(offlineBookAdupter);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }
}
