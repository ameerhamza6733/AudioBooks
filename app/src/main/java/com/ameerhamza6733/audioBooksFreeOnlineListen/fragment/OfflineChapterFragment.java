package com.ameerhamza6733.audioBooksFreeOnlineListen.fragment;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ameerhamza6733.audioBooksFreeOnlineListen.R;
import com.ameerhamza6733.audioBooksFreeOnlineListen.adupters.OfflineChapterAdupter;
import com.ameerhamza6733.audioBooksFreeOnlineListen.mediaPlayer.PlayerForegroundService;
import com.ameerhamza6733.audioBooksFreeOnlineListen.models.AudioBook;
import com.ameerhamza6733.audioBooksFreeOnlineListen.models.MataData;
import com.ameerhamza6733.audioBooksFreeOnlineListen.viewModels.OfflineBooksViewModle;
import com.google.android.exoplayer2.ui.PlayerControlView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.ameerhamza6733.audioBooksFreeOnlineListen.MySharedPref.SHARD_PREF_DOWNLOADED_AUDIO_BOOK;
import static com.ameerhamza6733.audioBooksFreeOnlineListen.fragment.ChaptersFragment.BROADCAST_ACTION_BUFFRING;
import static com.ameerhamza6733.audioBooksFreeOnlineListen.fragment.ChaptersFragment.BROADCAST_ACTION_PLAYER_Closed;
import static com.ameerhamza6733.audioBooksFreeOnlineListen.fragment.ChaptersFragment.BROADCAST_ACTION_PLAYER_START;
import static com.ameerhamza6733.audioBooksFreeOnlineListen.fragment.ChaptersFragment.BROADCAST_ACTION_SHOW_AD;
import static com.ameerhamza6733.audioBooksFreeOnlineListen.mediaPlayer.PlayerForegroundService.player;

/**
 * Created by AmeerHamza on 5/9/18.
 */

public class OfflineChapterFragment extends OfflineBookFragment {
    public static final String BUNDEL_KEY_BOOK_NO = "BUNDEL_KEY_BOOK_NO";
    int bookNumber;
    private View rootView;
    private List<MataData> mataDataList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PlayerControlView playerView;
    private FloatingActionButton fabStop;
    private BroadcastReceiver receiver;
    private IntentFilter filter;
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

                //do something based on the intent's action
                if (intent.getAction().equals(BROADCAST_ACTION_BUFFRING)) {


                } else if (intent.getAction().equals(BROADCAST_ACTION_PLAYER_START)) {
                    fabStop.setVisibility(View.VISIBLE);

                    if (player != null) {
                        playerView.setVisibility(View.VISIBLE);
                        playerView.setPlayer(PlayerForegroundService.player);
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
        if (rootView == null)
            rootView = inflater.inflate(R.layout.offline_chapter_fragemnt, container, false);
        bookNumber = getArguments().getInt(BUNDEL_KEY_BOOK_NO);
        recyclerView = rootView.findViewById(R.id.recyclerViewOfilineSavedBooks);
        playerView = rootView.findViewById(R.id.playerView);
        fabStop = rootView.findViewById(R.id.fabStop);
        intiDataSet();
        setHasOptionsMenu(false);
        try {
            ((AppCompatActivity) getActivity()).getSupportActionBar().show();
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(" Chapters");
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        return rootView;
    }

    private void startPlayerService(String Action, long miliSecond) {


        Intent startIntent = new Intent(getActivity(), PlayerForegroundService.class);

        startIntent.putExtra(PlayerForegroundService.KEY_PREFF_CURRENT_TRACK_INDEX, miliSecond);
        if (Action != null)
            startIntent.setAction(Action);

        if (Build.VERSION.SDK_INT > 25) {
            OfflineChapterFragment.this.getActivity().startForegroundService(startIntent);
        } else
            getActivity().startService(startIntent);

    }
    public void intiDataSet() {
        OfflineBooksViewModle offlineBooksViewModle = ViewModelProviders.of(getActivity()).get(OfflineBooksViewModle.class);
        offlineBooksViewModle.getAllSavedAudioBooks(getActivity().getApplicationContext().getSharedPreferences(SHARD_PREF_DOWNLOADED_AUDIO_BOOK, 0)).observe(this, new Observer<List<AudioBook>>() {
            @Override
            public void onChanged(@Nullable List<AudioBook> audioBooks) {
                if (audioBooks != null && audioBooks.size() > 0) {
                    for (MataData mataData : audioBooks.get(bookNumber).getMataData()) {
                        File f = new File(mataData.getSdPath());
                        if (f.exists() && mataData.hasDownloaded()) {
                            mataDataList.add(mataData);
                        }
                    }

                    OfflineChapterAdupter offlineChapterAdupter = new OfflineChapterAdupter(audioBooks, getActivity(), mataDataList, bookNumber);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(offlineChapterAdupter);
                } else {
                    Toast.makeText(getActivity(), "You don't have any offline book", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}
