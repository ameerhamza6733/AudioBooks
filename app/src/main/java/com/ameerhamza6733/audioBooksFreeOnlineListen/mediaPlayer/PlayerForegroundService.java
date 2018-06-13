package com.ameerhamza6733.audioBooksFreeOnlineListen.mediaPlayer;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.ameerhamza6733.audioBooksFreeOnlineListen.MySharedPref;
import com.ameerhamza6733.audioBooksFreeOnlineListen.R;
import com.ameerhamza6733.audioBooksFreeOnlineListen.fragment.PlayerFragment;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;


/**
 * Created by AmeerHamza on 2/11/2018.
 */

public class PlayerForegroundService extends Service implements Player.EventListener {

    public static final String EXTRA_URI = "com.ameerhamza6733.businessaudiobook.mediaPlayer.PlayerForegroundService.url";
    public static final String EXTRA_TITLE = "com.ameerhamza6733.businessaudiobook.mediaPlayer.PlayerForegroundService.title";
    public static final String EXTRA_SEEK_TO = "EXTRA_SEEK_TO";
    public final static String ACTION_START = "com.ameerhamza6733.businessaudiobook.mediaPlayer.action.startforeground";
    public final static String STOP_ACTION = "com.ameerhamza6733.businessaudiobook.mediaPlayer.action.stop";

   // public final static String BROAD_CAST_SERVICE_STAARTED = "com.ameerhamza6733.businessaudiobook.mediaPlayer.BROAD_CAST_SERVICE_STAARTED";

    public static final int ONGOING_NOTIFICATION_ID = 101;
    public final static String START_ACTIVITY_BROAD_CAST = "START_ACTIVITY_BROAD_CAST";
    private static final String TAG = "PlayerForegroundService";
    private static final String NOTIFICATION_CHANNEL_ID = "audio_book_playback_channel";
    public static String MAIN_ACTION = "com.ameerhamza6733.businessaudiobook.mediaPlayer.action.main";
    public static Boolean isPlaying = false;
    public static SimpleExoPlayer player;
    protected static String url;
    private static String title;

    private long seekto = 0;

    private PlayerNotificationManager playerNotificationManager;


    @Override
    public void onCreate() {
        super.onCreate();
        isPlaying = true;
        getPlayerNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(PlayerForegroundService.ACTION_START)) {
            url = intent.getStringExtra(EXTRA_URI);
            try {
                seekto = MySharedPref.getSavedLongFromPreference(getApplicationContext(), MySharedPref.SHARD_PREF_AUDIO_BOOK_FILE_NAME, url);
            } catch (Exception e) {
                Log.d(TAG, ":" + e.getMessage());
                e.printStackTrace();
            }
            seekto = intent.getLongExtra(PlayerForegroundService.EXTRA_SEEK_TO, 0);
            title = intent.getStringExtra(PlayerForegroundService.EXTRA_TITLE);
            Toast.makeText(this, "Start Service", Toast.LENGTH_SHORT).show();

            initMediaPlayer(url);

        } else if (intent.getAction().equals(PlayerForegroundService.STOP_ACTION)) {

            SendMediaStateBroadCast(PlayerFragment.BROADCAST_ACTION_PLAYER_Closed);
            stopForeground(true);
            stopSelf();
        }
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {

        if (player != null) {
            SaveSongState();
            playerNotificationManager.setPlayer(null);
            player.stop();
            player.release();
            player = null;
        }

        PlayerForegroundService.isPlaying = false;
        super.onDestroy();

    }

    private void SaveSongState() {
        MySharedPref.saveObjectToSharedPreference(getApplicationContext(), MySharedPref.SHARD_PREF_AUDIO_BOOK_FILE_NAME, url, player.getCurrentPosition());
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Used only in case of bound services.
        return null;
    }


    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        Log.d(TAG, "" + playbackState);
        if (playbackState == Player.STATE_READY) {
            SendMediaStateBroadCast(PlayerFragment.BROADCAST_ACTION_PLAYER_START);

        }
        if (playbackState == Player.STATE_BUFFERING) {
            SendMediaStateBroadCast(PlayerFragment.BROADCAST_ACTION_BUFFRING);

        }

    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }

    private void initMediaPlayer(String url) {
        Log.d(TAG, "url: " + url);

        DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(getApplicationContext());
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter(); //Provides estimates of the currently available bandwidth.
        AdaptiveTrackSelection.Factory trackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        DefaultTrackSelector trackSelector = new DefaultTrackSelector(trackSelectionFactory);
        DefaultLoadControl loadControl = new DefaultLoadControl();
        player = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl);



        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(getApplicationContext(), "ExoplayerDemo");
        MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(url));
        player.prepare(mediaSource);
        player.seekTo(seekto);
        player.addListener(this);
        player.setPlayWhenReady(true);


    }

    private void SendMediaStateBroadCast(String action) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(action);
        sendBroadcast(broadcastIntent);
    }

    public PlayerNotificationManager getPlayerNotification() {

        PlayerNotificationManager.MediaDescriptionAdapter mediaDescriptionAdapter = new PlayerNotificationManager.MediaDescriptionAdapter() {
            @Override
            public String getCurrentContentTitle(Player player) {
                return title;
            }

            @Nullable
            @Override
            public PendingIntent createCurrentContentIntent(Player player) {

                return null;
            }

            @Nullable
            @Override
            public String getCurrentContentText(Player player) {
                return "";
            }

            @Nullable
            @Override
            public Bitmap getCurrentLargeIcon(Player player, PlayerNotificationManager.BitmapCallback callback) {
                return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);


            }

        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(this, NOTIFICATION_CHANNEL_ID, R.string.playback_chennel_name, ONGOING_NOTIFICATION_ID, mediaDescriptionAdapter);

        } else {
            playerNotificationManager = new PlayerNotificationManager(this, NOTIFICATION_CHANNEL_ID, ONGOING_NOTIFICATION_ID, mediaDescriptionAdapter);
        }


        playerNotificationManager.setNotificationListener(new PlayerNotificationManager.NotificationListener() {
            @Override
            public void onNotificationStarted(int notificationId, Notification notification) {
                startForeground(notificationId, notification);
            }

            @Override
            public void onNotificationCancelled(int notificationId) {
                stopSelf();
            }
        });
        playerNotificationManager.setUseNavigationActions(true);
        playerNotificationManager.setUsePlayPauseActions(true);
        playerNotificationManager.setPriority(NotificationCompat.PRIORITY_MAX);
        playerNotificationManager.setStopAction(PlayerNotificationManager.ACTION_STOP);
        playerNotificationManager.setPlayer(player);
        return playerNotificationManager;
    }


}