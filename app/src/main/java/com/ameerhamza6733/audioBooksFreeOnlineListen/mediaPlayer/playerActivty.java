package com.ameerhamza6733.audioBooksFreeOnlineListen.mediaPlayer;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.ameerhamza6733.audioBooksFreeOnlineListen.R;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
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

public class playerActivty extends  Fragment {
    public static final String EXTRA_PLAYER_URI = "EXTRA_PLAYER_URI";
    public static final String EXTRA_SEEK_TO = "EXTRA_SEEK_TO";
    public static String EXTRA_TITLE="EXTRA_TITLE";
    SimpleExoPlayerView mPlayerView;
    private SimpleExoPlayer mPlayer;


    public playerActivty() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_player_activty, container, false);
       mPlayerView=rootView.findViewById(R.id.player_view);

        getPlayer();
        return rootView;
    }

    private void getPlayer() {
        // URL of the video to stream
        String videoURL = "https://www.youtube.com/watch?v=3tmd-ClpJxA";

        // Handler for the video player
        Handler mainHandler = new Handler();

	/* A TrackSelector that selects tracks provided by the MediaSource to be consumed by each of the available Renderers.
	  A TrackSelector is injected when the player is created. */
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        // Create the player with previously created TrackSelector
        mPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);

        // Load the default controller
        mPlayerView.setUseController(true);
        mPlayerView.requestFocus();

        // Load the SimpleExoPlayerView with the created player
        mPlayerView.setPlayer(mPlayer);

        // Measures bandwidth during playback. Can be null if not required.
        DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();

        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                getContext(),
                Util.getUserAgent(getContext(), "MyAppName"),
                defaultBandwidthMeter);

        // Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource(
                Uri.parse(videoURL),
                dataSourceFactory,
                extractorsFactory,
                null,
                null);

        // Prepare the player with the source.
        mPlayer.prepare(videoSource);

        // Autoplay the video when the player is ready
        mPlayer.setPlayWhenReady(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();


        // Release the player when it is not needed
        mPlayer.release();
    }
}