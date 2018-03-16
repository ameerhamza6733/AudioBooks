package com.ameerhamza6733.audioBooksFreeOnlineListen.mediaPlayer;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.ameerhamza6733.audioBooksFreeOnlineListen.R;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;


import java.util.List;
import java.util.Locale;

import static com.ameerhamza6733.audioBooksFreeOnlineListen.mediaPlayer.PlayerForegroundService.EXTRA_URI;

public class playerActivty extends AppCompatActivity {

    public static final String EXTRA_PLAYER_URI = "EXTRA_PLAYER_URI";
    public static final String EXTRA_TITLE = "EXTRA_TITLE";
    public static final String EXTRA_SEEK_TO = "EXTRA_SEEK_TO";
    private String TAG = "playerActivty";
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer player;

    private Timeline.Window window;
    private DataSource.Factory mediaDataSourceFactory;
    private DefaultTrackSelector trackSelector;
    private boolean shouldAutoPlay;
    private BandwidthMeter bandwidthMeter;

    private String url;
    private String audioTitle;
    private long playBack;


    private ImageView ivHideControllerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_activty);
        //stop service if already runing
        Intent startIntent = new Intent(playerActivty.this, PlayerForegroundService.class);
        startIntent.putExtra(EXTRA_URI, url);
        startIntent.setAction(PlayerForegroundService.STOP_ACTION);
        startService(startIntent);
        AudioManager m_audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        m_audioManager.setSpeakerphoneOn(true);
        if (getIntent() != null) {
            url = getIntent().getStringExtra(playerActivty.EXTRA_PLAYER_URI);
            audioTitle = getIntent().getStringExtra(playerActivty.EXTRA_TITLE);
            playBack = getIntent().getLongExtra(playerActivty.EXTRA_SEEK_TO, 0);
        }
        try {
            getSupportActionBar().hide();

        } catch (Exception e) {
        }
        shouldAutoPlay = true;
        bandwidthMeter = new DefaultBandwidthMeter();
        mediaDataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "mediaPlayerSample"), (TransferListener<? super DataSource>) bandwidthMeter);
        window = new Timeline.Window();
        ivHideControllerButton = (ImageView) findViewById(R.id.exo_controller);

    }

    private void initializePlayer() {

        simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.player_view);
        simpleExoPlayerView.requestFocus();

        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);

        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

        simpleExoPlayerView.setPlayer(player);
        player.seekTo(playBack);
        player.setPlayWhenReady(shouldAutoPlay);
/*        MediaSource mediaSource = new HlsMediaSource(Uri.parse("https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8"),
                mediaDataSourceFactory, mainHandler, null);*/

        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(url),
                mediaDataSourceFactory, extractorsFactory, null, null);

        player.prepare(mediaSource);

//        ivHideControllerButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                simpleExoPlayerView.hideController();
//            }
//        });
    }

    private void releasePlayer() {
        if (player != null) {
            //start playback service;
            Intent startIntent = new Intent(playerActivty.this, PlayerForegroundService.class);
            startIntent.putExtra(EXTRA_URI, url);
            startIntent.putExtra(PlayerForegroundService.EXTRA_SEEK_TO, player.getContentPosition());
            startIntent.putExtra(PlayerForegroundService.EXTRA_TITLE, audioTitle);
            startIntent.setAction(PlayerForegroundService.START_FOREGROUND_ACTION);
            startService(startIntent);

            shouldAutoPlay = player.getPlayWhenReady();
            player.release();
            player = null;
            trackSelector = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                  List<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                  for (String s : result){
                      Log.d(TAG,"res"+s);
                  }
                }
                break;
            }
        }

//        add();
//        display();
    }
}