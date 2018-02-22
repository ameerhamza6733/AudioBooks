package com.ameerhamza6733.businessaudiobook.mediaPlayer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.ameerhamza6733.businessaudiobook.R;
import com.ameerhamza6733.businessaudiobook.Util;
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
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import static com.ameerhamza6733.businessaudiobook.activitys.DetailActivity.TAG;

/**
 * Created by AmeerHamza on 2/11/2018.
 */

public class PlayerForegroundService extends Service implements Player.EventListener {

    public static String MAIN_ACTION = "com.ameerhamza6733.businessaudiobook.mediaPlayer.action.main";
    public static final String EXTRA_URI = "com.ameerhamza6733.businessaudiobook.mediaPlayer.PlayerForegroundService.uri";
    public static final String EXTRA_TITLE = "com.ameerhamza6733.businessaudiobook.mediaPlayer.PlayerForegroundService.title";
    public static final String EXTRA_SEEK_TO="EXTRA_SEEK_TO";


    protected final static String FAST_FORWARD_ACTION = "com.ameerhamza6733.businessaudiobook.mediaPlayer.action.forward";
    protected final static String FAST_REWIND_ACTION = "com.ameerhamza6733.businessaudiobook.mediaPlayer.action.rewind";
    protected final static String START_FOREGROUND_ACTION = "com.ameerhamza6733.businessaudiobook.mediaPlayer.action.startforeground";
    protected final static String STOP_ACTION = "com.ameerhamza6733.businessaudiobook.mediaPlayer.action.stop";
    protected final static String PLAY_PAUSE_ACTION = "com.ameerhamza6733.businessaudiobook.mediaPlayer.action.playOrpause";
    protected static final int ONGOING_NOTIFICATION_ID = 101;
    protected final static String START_ACTIVITY_BROAD_CAST ="START_ACTIVITY_BROAD_CAST";


    protected String uri;
    private Boolean isPlaying;
    private SimpleExoPlayer player;
    private String title;
    private long seekto=0;
    private StartPlayerActivtyBrodcastReciver activtyBrodcastReciver;


    @Override
    public void onCreate() {
        super.onCreate();
        activtyBrodcastReciver = new StartPlayerActivtyBrodcastReciver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        // set the custom action
        intentFilter.addAction(START_ACTIVITY_BROAD_CAST);

        registerReceiver(activtyBrodcastReciver, intentFilter);
    }




    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(PlayerForegroundService.START_FOREGROUND_ACTION)) {
            uri = intent.getStringExtra(EXTRA_URI);
            seekto=intent.getLongExtra(PlayerForegroundService.EXTRA_SEEK_TO,0);
            title = intent.getStringExtra(PlayerForegroundService.EXTRA_TITLE);
            Toast.makeText(this, "Start Service", Toast.LENGTH_SHORT).show();
            Notification notification = getNotification();
            startForeground(ONGOING_NOTIFICATION_ID, notification);
            initMediaPlayer(uri);
        } else if (intent.getAction().equals(PlayerForegroundService.STOP_ACTION)) {
            if (handler != null)
                handler.removeCallbacks(runnable);
            stopForeground(true);
            stopSelf();
        } else if (intent.getAction().equals(FAST_FORWARD_ACTION)) {
            if (player != null)
                player.seekTo(player.getCurrentPosition() + 5000);

        } else if (intent.getAction().equals(PLAY_PAUSE_ACTION)) {
            if (player != null) {
                if (handler != null) {
                    handler.removeCallbacks(runnable);
                }
                if (player.getPlayWhenReady()) {
                    NotificationRemoteView.setTextViewCompoundDrawables(R.id.pause, 0, R.drawable.ic_play_arrow_black_24dp, 0, 0);
                    player.setPlayWhenReady(false);

                } else {

                    NotificationRemoteView.setTextViewCompoundDrawables(R.id.pause, 0, R.drawable.ic_pause_black_24dp, 0, 0);
                    player.setPlayWhenReady(true);

                }
                mNotificationManager.notify(ONGOING_NOTIFICATION_ID, notification);
            }

        } else if (intent.getAction().equals(FAST_REWIND_ACTION)) {
            Log.e(TAG, "FAST_REWIND_ACTION");
            player.seekTo(player.getCurrentPosition() - 5000);
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (player != null)
            player.release();
        unregisterReceiver(activtyBrodcastReciver);
        super.onDestroy();

    }

    @Override
    public IBinder onBind(Intent intent) {
        // Used only in case of bound services.
        return null;
    }


    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    Handler handler;
    Runnable runnable;

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == Player.STATE_READY) {
            isPlaying = true;
            handler = new Handler();
            runnable = () -> {
                if (mNotificationManager != null) {

                    NotificationRemoteView.setTextViewText(R.id.audio_track_time, String.valueOf(Util.INSTANCE.formatSeconds((player.getContentPosition()) / 1000)) + " / " + String.valueOf(Util.INSTANCE.formatSeconds((player.getDuration() / 1000))));
                    mNotificationManager.notify(ONGOING_NOTIFICATION_ID, notification);
                }
                handler.postDelayed(runnable, 1000);
            };
            handler.postDelayed(runnable, 0);
        }
        if (playbackState == Player.STATE_BUFFERING) {
            Log.e(TAG, "STATE_BUFFERING...");
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
        Log.d(TAG, "uri: " + url);
        DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(getApplicationContext());
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter(); //Provides estimates of the currently available bandwidth.
        AdaptiveTrackSelection.Factory trackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        DefaultTrackSelector trackSelector = new DefaultTrackSelector(trackSelectionFactory);
        DefaultLoadControl loadControl = new DefaultLoadControl();
        player = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl);
        player.addListener(this);


        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(getApplicationContext(), "ExoplayerDemo");
        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        Handler mainHandler = new Handler();
        ExtractorMediaSource mediaSource = new ExtractorMediaSource(Uri.parse(url),
                dataSourceFactory,
                extractorsFactory,
                mainHandler,
                null);
        player.prepare(mediaSource);
        player.seekTo(seekto);
        player.setPlayWhenReady(true);
        player.setPlayWhenReady(true);


    }

    NotificationManager mNotificationManager;
    RemoteViews NotificationRemoteView;
    Notification notification;

    public Notification getNotification() {
        int icon = R.mipmap.ic_launcher_round;
        long when = System.currentTimeMillis();
        notification = new Notification(icon, "Custom Notification", when);

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        NotificationRemoteView = new RemoteViews(getPackageName(), R.layout.notification);
        NotificationRemoteView.setOnClickPendingIntent(R.id.close, NotificationHelper.getButtonClosePendingIntent(this));
        NotificationRemoteView.setOnClickPendingIntent(R.id.fast_forword, NotificationHelper.getButtonFastForwadPendingIntent(this));
        NotificationRemoteView.setOnClickPendingIntent(R.id.fast_rewind, NotificationHelper.getButtonFastRewindPendingIntent(this));
        NotificationRemoteView.setOnClickPendingIntent(R.id.pause, NotificationHelper.getButtonPlayOrPausePendingIntent(this));

        NotificationRemoteView.setTextViewText(R.id.title, title);
        NotificationRemoteView.setTextViewText(R.id.audio_track_time, "");
        notification.contentView = NotificationRemoteView;
        //this will start player activity with resume media play back
        Intent i = new Intent(START_ACTIVITY_BROAD_CAST);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, i, 0);
        notification.contentIntent = pendingIntent;

        if (mNotificationManager != null) {
            mNotificationManager.notify(ONGOING_NOTIFICATION_ID, notification);
        }
        return notification;
    }


    //this will start activity with resume media player  if user click on notification
    public  class StartPlayerActivtyBrodcastReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch(action) {
                case START_ACTIVITY_BROAD_CAST:
                    Log.d(TAG,"trying to start activty from service ");
                    Intent intent1 = new Intent(context,playerActivty.class);

                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent1.putExtra(playerActivty.EXTRA_PLAYER_URI,uri);
                    intent1.putExtra(playerActivty.EXTRA_TITLE,title);
                    intent1.putExtra(playerActivty.EXTRA_SEEK_TO,player.getContentPosition());
                    startActivity(intent1);

                    //stop this service
                    Intent startIntent = new Intent(context, PlayerForegroundService.class);
                    startIntent.setAction(PlayerForegroundService.STOP_ACTION);
                    context.startService(startIntent);

                    break;
            }
        }
    }
}