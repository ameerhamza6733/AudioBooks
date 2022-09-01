package com.ameerhamza6733.audioBooksFreeOnlineListen.mediaPlayer;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;

import com.ameerhamza6733.audioBooksFreeOnlineListen.MySharedPref;
import com.ameerhamza6733.audioBooksFreeOnlineListen.R;
import com.ameerhamza6733.audioBooksFreeOnlineListen.activitys.DetailTabActivity;
import com.ameerhamza6733.audioBooksFreeOnlineListen.activitys.MainActivity;
import com.ameerhamza6733.audioBooksFreeOnlineListen.adupters.RateMe;
import com.ameerhamza6733.audioBooksFreeOnlineListen.fragment.ChaptersFragment;
import com.ameerhamza6733.audioBooksFreeOnlineListen.models.AudioBook;
import com.ameerhamza6733.audioBooksFreeOnlineListen.models.MataData;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector;
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import java.io.File;
import java.util.List;


/**
 * Created by AmeerHamza on 2/11/2018.
 */

public class PlayerForegroundService extends Service implements Player.EventListener {

    public static final String KEY_PREFF_CURRENT_TRACK_INDEX = "KEY_PREFF_CURRENT_TRACK_INDEX";
    public final static String ACTION_UPDATE_MEDIA_SOURCE = "com.ameerhamza6733.businessaudiobook.mediaPlayer.action.startforeground";
    public final static String STOP_ACTION = "com.ameerhamza6733.businessaudiobook.mediaPlayer.action.stop";
    public final static String KEY_SHARD_PREF_AUDIO_BOOK = "PlayerForegroundService.KEY_SHARD_PREF_AUDIO_BOOK";
    public static final int ONGOING_NOTIFICATION_ID = 101;
    public static final int FLAG_OFFLINE = 404;
    private static final String TAG = "PlayerForegroundService";
    private static final String NOTIFICATION_CHANNEL_ID = "audio_book_playback_channel";
    private static final java.lang.String MEDIA_SESSION_TAG = "MEDIA_SESSION_TAG";
    public static final java.lang.String IS_SOUCE_LOCAL_DISK = "IS_SOUCE_LOCAL_DISK";
    public static Boolean isPlaying = false;
    public static String currentPlayingChapter=null;
    public static SimpleExoPlayer player;
    private static String title;
    protected List<MataData> mataDataList;
    private AudioBook audioBook;
    private long seekto = 0;
    private String currentTrackIndex = "0";

    private PlayerNotificationManager playerNotificationManager;
    private MediaSessionCompat mediaSessionCompat;
    private MediaSessionConnector mediaSessionConntor;
    private ConcatenatingMediaSource concatenatingMediaSource;
    private ExtractorMediaSource mediaSource;
    private String isMediaSouceLocalDisk = "0";


    @Override
    public void onCreate() {
        super.onCreate();
        isPlaying = true;
        initMediaPlayer();


    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null && intent.getAction() != null) {
            if (intent.getAction().equals(PlayerForegroundService.ACTION_UPDATE_MEDIA_SOURCE)) {
                addMediaSourceToPlayer();
            } else if (intent.getAction().equals(PlayerForegroundService.STOP_ACTION)) {
                Intent intent1 = new Intent(this, RateMe.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
                SendMediaStateBroadCast(ChaptersFragment.BROADCAST_ACTION_PLAYER_Closed);
                stopForeground(true);
                stopSelf();
            }
        }
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {

        if (mediaSessionCompat != null)
            mediaSessionCompat.release();
        if (mediaSessionConntor != null) {
            mediaSessionConntor.setPlayer(null, null);
        }
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
        MySharedPref.saveObjectToSharedPreference(getApplicationContext(), MySharedPref.SHARD_PREF_AUDIO_BOOK_FILE_NAME, mataDataList.get(player.getCurrentWindowIndex()).getURL(), player.getCurrentPosition());
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
            SendMediaStateBroadCast(ChaptersFragment.BROADCAST_ACTION_PLAYER_START);

        }
        if (playbackState == Player.STATE_BUFFERING) {
            SendMediaStateBroadCast(ChaptersFragment.BROADCAST_ACTION_BUFFRING);

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

    private void initMediaPlayer() {


        DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(getApplicationContext());
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter(); //Provides estimates of the currently available bandwidth.
        AdaptiveTrackSelection.Factory trackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        DefaultTrackSelector trackSelector = new DefaultTrackSelector(trackSelectionFactory);
        DefaultLoadControl loadControl = new DefaultLoadControl();
        player = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl);


        addMediaSourceToPlayer();
        getPlayerNotification();
        player.addListener(this);
        mediaSessionCompat = new MediaSessionCompat(this, MEDIA_SESSION_TAG);
        mediaSessionCompat.setActive(true);
        playerNotificationManager.setMediaSessionToken(mediaSessionCompat.getSessionToken());
        mediaSessionConntor = new MediaSessionConnector(mediaSessionCompat);
        mediaSessionConntor.setQueueNavigator(new TimelineQueueNavigator(mediaSessionCompat) {
            @Override
            public MediaDescriptionCompat getMediaDescription(Player player, int windowIndex) {
                return new MediaDescriptionCompat.Builder().setTitle(mataDataList.get(windowIndex).getName()).build();
            }
        });
        mediaSessionConntor.setPlayer(player, null);

    }

    private void SendMediaStateBroadCast(String action) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(action);
        sendBroadcast(broadcastIntent);
    }

    private void addMediaSourceToPlayer() {
        audioBook = MySharedPref.getSavedObjectFromPreference(this, MySharedPref.SHARD_PREF_AUDIO_BOOK_FILE_NAME, KEY_SHARD_PREF_AUDIO_BOOK, AudioBook.class);
        currentTrackIndex = MySharedPref.getSavedObjectFromPreference(this, MySharedPref.SHARD_PREF_AUDIO_BOOK_FILE_NAME, KEY_PREFF_CURRENT_TRACK_INDEX);
        isMediaSouceLocalDisk = MySharedPref.getSavedObjectFromPreference(this, MySharedPref.SHARD_PREF_AUDIO_BOOK_FILE_NAME, IS_SOUCE_LOCAL_DISK);
        if (currentTrackIndex.isEmpty())
            currentTrackIndex = "0";
        if (isMediaSouceLocalDisk.isEmpty())
            isMediaSouceLocalDisk = "0";
        if (audioBook != null && audioBook.getMataData() != null) {
            mataDataList = audioBook.getMataData();
            title = audioBook.getTitle();
            DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(getApplicationContext(), "ExoplayerDemo");
            concatenatingMediaSource = new ConcatenatingMediaSource();
            for (MataData mataData : mataDataList) {
                if (mataData.hasDownloaded()) {
                    File file = new File(mataData.getSdPath());
                    if (file.exists()) {
                        Log.d(TAG, "" + mataData.getSdPath());
                        mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(mataData.getSdPath()));
                    } else {
                        if (isMediaSouceLocalDisk.equals("0"))
                            mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(mataData.getURL()));

                    }

                } else {
                    if (isMediaSouceLocalDisk.equals("0"))
                        mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(mataData.getURL()));

                }

                concatenatingMediaSource.addMediaSource(mediaSource);
            }

            player.prepare(concatenatingMediaSource);
            player.seekTo(Integer.parseInt(currentTrackIndex), seekto);
            player.setPlayWhenReady(true);

        }
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
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                Intent detailIntent = new Intent (getApplicationContext(), DetailTabActivity.class);
                stackBuilder.addNextIntentWithParentStack(detailIntent);
                PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                return resultPendingIntent;
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