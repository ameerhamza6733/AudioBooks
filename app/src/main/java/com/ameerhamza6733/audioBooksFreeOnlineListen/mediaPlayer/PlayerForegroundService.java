package com.ameerhamza6733.audioBooksFreeOnlineListen.mediaPlayer;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.ameerhamza6733.audioBooksFreeOnlineListen.MySharedPref;
import com.ameerhamza6733.audioBooksFreeOnlineListen.R;
import com.ameerhamza6733.audioBooksFreeOnlineListen.Util;
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

    public static final String EXTRA_URI = "com.ameerhamza6733.businessaudiobook.mediaPlayer.PlayerForegroundService.uri";
    public static final String EXTRA_TITLE = "com.ameerhamza6733.businessaudiobook.mediaPlayer.PlayerForegroundService.title";
    public static final String EXTRA_SEEK_TO = "EXTRA_SEEK_TO";
    public final static String EXTRA_FAST_FORWARD_MILI_SECONDS = "com.ameerhamza6733.businessaudiobook.mediaPlayer.EXTRA_FAST_FORWARD_MILI_SECONDS";
    public final static String EXTRA_REWIND_MILI_SECOND = "com.ameerhamza6733.businessaudiobook.mediaPlayer.EXTRA_REWIND_MILI_SECOND";

    public final static String FAST_FORWARD_ACTION = "com.ameerhamza6733.businessaudiobook.mediaPlayer.action.forward";
    public final static String FAST_REWIND_ACTION = "com.ameerhamza6733.businessaudiobook.mediaPlayer.action.rewind";
    public final static String ACTION_START = "com.ameerhamza6733.businessaudiobook.mediaPlayer.action.startforeground";
    public final static String STOP_ACTION = "com.ameerhamza6733.businessaudiobook.mediaPlayer.action.stop";
    public final static String PLAYER_PLAY_PAUSE_ACTION = "com.ameerhamza6733.businessaudiobook.mediaPlayer.action.playOrpause";

    public final static String SERVICE_STAARTED = "com.ameerhamza6733.businessaudiobook.mediaPlayer.SERVICE_STAARTED";

    public static final int ONGOING_NOTIFICATION_ID = 101;
    public final static String START_ACTIVITY_BROAD_CAST = "START_ACTIVITY_BROAD_CAST";
    private static final String TAG = "PlayerForegroundService";
    private static final String NOTIFICATION_CHANNEL_ID = "audio_book_playback_channel";
    public static String MAIN_ACTION = "com.ameerhamza6733.businessaudiobook.mediaPlayer.action.main";
    public static Boolean isPlaying = false;
    public static SimpleExoPlayer player;
    protected static String uri;
    private static String title;
    Handler handler;
    Runnable runnable;
    NotificationManager mNotificationManager;
    RemoteViews NotificationRemoteView;
    Notification notification;
    NotificationCompat.Builder mBuilder;
    private long seekto = 0;
    private StartPlayerActivtyBrodcastReciver activtyBrodcastReciver;
    private PlayerNotificationManager playerNotificationManager;


    @Override
    public void onCreate() {
        super.onCreate();
        isPlaying = true;

        activtyBrodcastReciver = new StartPlayerActivtyBrodcastReciver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        // set the custom action
        intentFilter.addAction(START_ACTIVITY_BROAD_CAST);
        registerReceiver(activtyBrodcastReciver, intentFilter);
        SendMediaStateBroadCast(SERVICE_STAARTED);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(PlayerForegroundService.ACTION_START)) {

            uri = intent.getStringExtra(EXTRA_URI);
            try {
                seekto = MySharedPref.getSavedLongFromPreference(getApplicationContext(), MySharedPref.SHARD_PREF_AUDIO_BOOK_FILE_NAME, uri);
            } catch (Exception e) {
                Log.d(TAG, ":" + e.getMessage());
                e.printStackTrace();
            }
            seekto = intent.getLongExtra(PlayerForegroundService.EXTRA_SEEK_TO, 0);
            title = intent.getStringExtra(PlayerForegroundService.EXTRA_TITLE);
            Toast.makeText(this, "Start Service", Toast.LENGTH_SHORT).show();
            initMediaPlayer(uri);
            getNotification();
        } else if (intent.getAction().equals(PlayerForegroundService.STOP_ACTION)) {
            if (handler != null)
                handler.removeCallbacks(runnable);
            SendMediaStateBroadCast(PlayerFragment.BROADCAST_ACTION_PLAYER_Closed);
            stopForeground(true);
            stopSelf();
        } else if (intent.getAction().equals(FAST_FORWARD_ACTION)) {
            long second = intent.getLongExtra(EXTRA_FAST_FORWARD_MILI_SECONDS, 0);
            if (player != null)
                player.seekTo(player.getCurrentPosition() + second);

        } else if (intent.getAction().equals(PLAYER_PLAY_PAUSE_ACTION)) {
            if (player != null) {
                seekto = player.getContentPosition();
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
            int second = intent.getIntExtra(EXTRA_REWIND_MILI_SECOND, 0);
            player.seekTo(player.getCurrentPosition() - second);
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {

        if (player != null) {
            SaveSongState();
            playerNotificationManager.setPlayer(null);
            player.stop();
            player.release();
            player=null;
        }
        if (activtyBrodcastReciver != null) {
            unregisterReceiver(activtyBrodcastReciver);
        }
        PlayerForegroundService.isPlaying = false;
        super.onDestroy();

    }

    private void SaveSongState() {
        MySharedPref.saveObjectToSharedPreference(getApplicationContext(), MySharedPref.SHARD_PREF_AUDIO_BOOK_FILE_NAME, uri, player.getCurrentPosition());
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
        Log.d(TAG,""+playbackState);
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
        Log.d(TAG, "uri: " + url);
        DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(getApplicationContext());
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter(); //Provides estimates of the currently available bandwidth.
        AdaptiveTrackSelection.Factory trackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        DefaultTrackSelector trackSelector = new DefaultTrackSelector(trackSelectionFactory);
        DefaultLoadControl loadControl = new DefaultLoadControl();
        player = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl);
      //  player.addListener(this);


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
        player.addListener(this);
        player.setPlayWhenReady(true);



    }

    private void SendMediaStateBroadCast(String action) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(action);
        sendBroadcast(broadcastIntent);
    }

    public Notification getNotification() {
        int icon = R.mipmap.ic_launcher_round;
        long when = System.currentTimeMillis();
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
                return  BitmapFactory.decodeResource(PlayerForegroundService.this.getResources(),
                        R.drawable.ic_play_arrow_black_24dp);
            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(this, NOTIFICATION_CHANNEL_ID, R.string.playback_chennel_name, ONGOING_NOTIFICATION_ID, mediaDescriptionAdapter);

        }else {
            playerNotificationManager=  new PlayerNotificationManager(this,NOTIFICATION_CHANNEL_ID,ONGOING_NOTIFICATION_ID,mediaDescriptionAdapter);
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

       playerNotificationManager.setPlayer(player);

//        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//            createChannel(mNotificationManager);
//        mBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID ).setSmallIcon(android.R.drawable.ic_media_play).setContentTitle(title);
//        notification = mBuilder.build();
//
//
//        NotificationRemoteView = new RemoteViews(getPackageName(), R.layout.notification);
//        NotificationRemoteView.setOnClickPendingIntent(R.id.close, NotificationHelper.getButtonClosePendingIntent(this));
//        NotificationRemoteView.setOnClickPendingIntent(R.id.fast_forword, NotificationHelper.getButtonFastForwadPendingIntent(this));
//        NotificationRemoteView.setOnClickPendingIntent(R.id.fast_rewind, NotificationHelper.getButtonFastRewindPendingIntent(this));
//        NotificationRemoteView.setOnClickPendingIntent(R.id.pause, NotificationHelper.getButtonPlayOrPausePendingIntent(this));
//
//        NotificationRemoteView.setTextViewText(R.id.title, title);
//        NotificationRemoteView.setTextViewText(R.id.audio_track_time, "");
//        mBuilder.setCustomContentView(NotificationRemoteView);
//        notification.contentView = NotificationRemoteView;
//        //this will start player activity with resume media play back
//        Intent i = new Intent(START_ACTIVITY_BROAD_CAST);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, i, 0);
//        notification.contentIntent = pendingIntent;
//
//        if (mNotificationManager != null) {
//            mNotificationManager.notify(ONGOING_NOTIFICATION_ID, mBuilder.build());
//        }
        return notification;
    }

    @TargetApi(26)
    private void createChannel(NotificationManager notificationManager) {
        String name = title;
        int importance = NotificationManager.IMPORTANCE_LOW;

        NotificationChannel mChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance);

        mChannel.enableLights(true);
        mChannel.setLightColor(Color.BLUE);
        notificationManager.createNotificationChannel(mChannel);
    }

    //this will start activity with resume media player  if user click on notification
    public static class StartPlayerActivtyBrodcastReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case START_ACTIVITY_BROAD_CAST:
                    Log.d(TAG, "trying to start activty from service ");
                    Intent intent1 = new Intent(context, PlayerActivty.class);

//                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent1.putExtra(PlayerActivty.EXTRA_PLAYER_URI, uri);
//                    intent1.putExtra(PlayerActivty.EXTRA_TITLE, title);
//                    intent1.putExtra(PlayerActivty.EXTRA_SEEK_TO, player.getContentPosition());
//                    activity.startActivity(intent1);
//
//                    //stop this service
//                    Intent startIntent = new Intent(activity, PlayerForegroundService.class);
//                    startIntent.setAction(PlayerForegroundService.STOP_ACTION);
//                    activity.startService(startIntent);

                    break;
            }
        }
    }
}