package com.ameerhamza6733.audioBooksFreeOnlineListen.mediaPlayer;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.ameerhamza6733.audioBooksFreeOnlineListen.R;
import com.ameerhamza6733.audioBooksFreeOnlineListen.models.MataData;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 5/12/18.
 */

public class AudioPlayerService extends Service {
    SimpleExoPlayer player;
    String url="https://ia601505.us.archive.org/6/items/cartas_de_inglaterra_1805_librivox/cartasdeinglaterra_01_queiros_128kb.mp3";
    List<String> stringList = new ArrayList<>();
    MataData mataData;
    @Override
    public void onCreate() {
        super.onCreate();
        final Context context= this;
        player = ExoPlayerFactory.newSimpleInstance(context, new DefaultTrackSelector());
      stringList.add(url);
      stringList.add(url);
        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(context, Util.getUserAgent(context,"AudioDemo"));
        ConcatenatingMediaSource concatenatingMediaSource = new ConcatenatingMediaSource();
       for (String s: stringList){
           MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                   .createMediaSource(Uri.parse(url));
          concatenatingMediaSource.addMediaSource(mediaSource);
       }
        player.prepare(concatenatingMediaSource);
        player.setPlayWhenReady(true);

        PlayerNotificationManager playerNotificationManager =PlayerNotificationManager.createWithNotificationChannel(
                context, "22", R.string.app_name, 11, new PlayerNotificationManager.MediaDescriptionAdapter() {
                    @Override
                    public String getCurrentContentTitle(Player player) {
                        return "title";
                    }

                    @Nullable
                    @Override
                    public PendingIntent createCurrentContentIntent(Player player) {
                        return null;
                    }

                    @Nullable
                    @Override
                    public String getCurrentContentText(Player player) {
                        return "dic";
                    }

                    @Nullable
                    @Override
                    public Bitmap getCurrentLargeIcon(Player player, PlayerNotificationManager.BitmapCallback callback) {
                        return BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.audio_book_drawble);
                    }
                });


  playerNotificationManager.setNotificationListener(new PlayerNotificationManager.NotificationListener() {
      @Override
      public void onNotificationStarted(int notificationId, Notification notification) {
          startForeground(notificationId,notification);
      }

      @Override
      public void onNotificationCancelled(int notificationId) {
stopSelf();
      }
  });
  playerNotificationManager.setPlayer(player);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
