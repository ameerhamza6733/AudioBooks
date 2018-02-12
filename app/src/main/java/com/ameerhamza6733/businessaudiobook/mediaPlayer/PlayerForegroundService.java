package com.ameerhamza6733.businessaudiobook.mediaPlayer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.ameerhamza6733.businessaudiobook.R;
import com.ameerhamza6733.businessaudiobook.activitys.MainActivity;
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

import java.util.concurrent.TimeUnit;

import static com.ameerhamza6733.businessaudiobook.activitys.DetailActivity.TAG;

/**
 * Created by AmeerHamza on 2/11/2018.
 */

public class PlayerForegroundService extends Service implements Player.EventListener {
    public static String MAIN_ACTION = "com.ameerhamza6733.businessaudiobook.mediaPlayer.action.main";
    public static final String EXTRA_URI = "com.ameerhamza6733.businessaudiobook.mediaPlayer.PlayerForegroundService";
    protected final static String START_FOREGROUND_ACTION = "com.ameerhamza6733.businessaudiobook.mediaPlayer.action.startforeground";
    protected final static String STOP_FOREGROUND_ACTION = "com.ameerhamza6733.businessaudiobook.mediaPlayer.action.stopforeground";
    protected static final int ONGOING_NOTIFICATION_ID = 101;

    private String uri;
    private SimpleExoPlayer player;
    private final IBinder mBinder = new MyBinder();

    @Override
    public void onCreate() {
        super.onCreate();
    }


    private void initMediaPlayer(String url) {
        Log.d(TAG,"uri: "+url);
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
        player.setPlayWhenReady(true);
        player.setPlayWhenReady(true);


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(PlayerForegroundService.START_FOREGROUND_ACTION)) {
            uri = intent.getStringExtra(EXTRA_URI);
            Toast.makeText(this, "Start Service", Toast.LENGTH_SHORT).show();
            Notification notification = getNotification();
            startForeground(ONGOING_NOTIFICATION_ID, notification);
            initMediaPlayer(uri);
        } else if (intent.getAction().equals(PlayerForegroundService.STOP_FOREGROUND_ACTION)) {
            if (handler!=null)
            handler.removeCallbacks(runnable);
            stopForeground(true);
            stopSelf();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (player!=null)
            player.release();
        super.onDestroy();

    }

    @Override
    public IBinder onBind(Intent intent) {
        // Used only in case of bound services.
        return mBinder;
    }

    NotificationManager mNotificationManager;
    RemoteViews NotificationRemoteView;
    Notification notification;
    public Notification getNotification() {
        int icon = R.mipmap.ic_launcher_round;
        long when = System.currentTimeMillis();
         notification = new Notification(icon, "Custom Notification", when);

         mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        NotificationRemoteView = new RemoteViews(getPackageName(), R.layout.notification);
        NotificationRemoteView.setOnClickPendingIntent( R.id.title,getButtonClosePendingIntent());
        NotificationRemoteView.setTextViewText(R.id.title, "Custom notification");
        NotificationRemoteView.setTextViewText(R.id.passs,"pass");
        notification.contentView = NotificationRemoteView;

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notification.contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);



        if (mNotificationManager != null) {
            mNotificationManager.notify(ONGOING_NOTIFICATION_ID, notification);
        }
        return notification;
    }

    public PendingIntent getButtonClosePendingIntent() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Intent buttonCloseIntent = new Intent(this, myNotificationCloseButtonHandler.class);
        buttonCloseIntent.putExtra("action", "close");

        return PendingIntent.getBroadcast(this, 0, buttonCloseIntent, 0);
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
        if (playbackState == Player.STATE_READY ) {
            handler = new Handler();
            runnable = () -> {
                if (mNotificationManager != null) {
                    NotificationRemoteView.setTextViewText(R.id.audio_track_time,String.valueOf(((player.getDuration() - player.getContentPosition())) / 1000)+" / "+String.valueOf((player.getDuration() / 1000)));
                    mNotificationManager.notify(ONGOING_NOTIFICATION_ID, notification);
                }
//                    Log.e(TAG, "Current: " + (player.getContentPosition() / 1000) + "s.");
//                    Log.e(TAG, "Left: " + ((player.getDuration() - player.getContentPosition()) / 1000) + "s.");
                handler.postDelayed(runnable, 1000);
            };
            handler.postDelayed(runnable, 0);
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


    public static class myNotificationCloseButtonHandler extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "Close Clicked", Toast.LENGTH_SHORT).show();
            Intent startIntent = new Intent(context, PlayerForegroundService.class);
            startIntent.setAction(PlayerForegroundService.STOP_FOREGROUND_ACTION);
            context.startService(startIntent);
        }


    }

    public class MyBinder extends Binder {
        PlayerForegroundService getService() {
            return PlayerForegroundService.this;
        }
    }

    public static class NotificationPlayButtonHandler extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context,"Play Clicked",Toast.LENGTH_SHORT).show();

        }
    }

    /**
     * Called when user clicks the "skip" button on the on-going system Notification.
     */
    public static class NotificationSkipButtonHandler extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context,"Next Clicked",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Called when user clicks the "previous" button on the on-going system Notification.
     */
    public static class NotificationPrevButtonHandler extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context,"Previous Clicked",Toast.LENGTH_SHORT).show();
        }
    }

}