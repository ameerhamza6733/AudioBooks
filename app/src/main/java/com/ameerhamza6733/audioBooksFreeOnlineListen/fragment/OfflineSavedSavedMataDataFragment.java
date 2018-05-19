package com.ameerhamza6733.audioBooksFreeOnlineListen.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ameerhamza6733.audioBooksFreeOnlineListen.R;
import com.ameerhamza6733.audioBooksFreeOnlineListen.adupters.OfflineMataDataAdupter;
import com.ameerhamza6733.audioBooksFreeOnlineListen.models.AudioBook;
import com.ameerhamza6733.audioBooksFreeOnlineListen.models.MataData;
import com.ameerhamza6733.audioBooksFreeOnlineListen.viewModels.OfflineBooksViewModle;

import java.util.ArrayList;
import java.util.List;

import static com.ameerhamza6733.audioBooksFreeOnlineListen.MySharedPref.SHARD_PREF_DOWNLOADED_AUDIO_BOOK;

/**
 * Created by AmeerHamza on 5/9/18.
 */

public class OfflineSavedSavedMataDataFragment extends OfflineSavedBookFragment {
    public static final String BUNDEL_KEY_BOOK_NO = "BUNDEL_KEY_BOOK_NO";
    private View rootView;
    private List<MataData> mataDataList = new ArrayList<>();
    private RecyclerView recyclerView;
    int bookNumber;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null)
            rootView = inflater.inflate(R.layout.offline_saved_book_fragemnt, container, false);
      bookNumber=  getArguments().getInt(BUNDEL_KEY_BOOK_NO);
        recyclerView = rootView.findViewById(R.id.recyclerViewOfilineSavedBooks);
        intiDataSet();
        try {
            ((AppCompatActivity) getActivity()).getSupportActionBar().show();
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(" Chapters");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rootView;
    }


    public void intiDataSet() {
        OfflineBooksViewModle offlineBooksViewModle = ViewModelProviders.of(getActivity()).get(OfflineBooksViewModle.class);
        offlineBooksViewModle.getAllSavedAudioBooks(getActivity().getApplicationContext().getSharedPreferences(SHARD_PREF_DOWNLOADED_AUDIO_BOOK, 0)).observe(this, new Observer<List<AudioBook>>() {
            @Override
            public void onChanged(@Nullable List<AudioBook> audioBooks) {
                if (audioBooks != null && audioBooks.size() > 0) {
                    for (MataData mataData : audioBooks.get(bookNumber).getMataData())
                        if (mataData.isHasDownloaded())
                           mataDataList.add(mataData);


                        OfflineMataDataAdupter offlineMataDataAdupter = new OfflineMataDataAdupter(audioBooks, getActivity(),mataDataList);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        recyclerView.setAdapter(offlineMataDataAdupter);
                }else {
                    Toast.makeText(getActivity(), "You don't have any offline book", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}
