package com.ameerhamza6733.audioBooksFreeOnlineListen.mediaPlayer;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by AmeerHamza on 2/13/2018.
 */

public class NotificationHelper {


    /**
     * Called when user clicks the "skip" button on the on-going system Notification.
     */
    public static class NotificationSkipButtonHandler extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "Next Clicked", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Called when user clicks the "previous" button on the on-going system Notification.
     */
    public static class NotificationPrevButtonHandler extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "Previous Clicked", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Called when user clicks the "pause" button on the on-going system Notification.
     */
    public static class NotificationPlayOrPauseButtonHandler extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Intent startIntent = new Intent(context, PlayerForegroundService.class);
            startIntent.setAction(PlayerForegroundService.PLAY_PAUSE_ACTION);
            context.startService(startIntent);
        }
    }

    /**
     * Called when user clicks the "fast forward" button on the on-going system Notification.
     */
    public static class NotificationFastForwardButtonHandler extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Intent startIntent = new Intent(context, PlayerForegroundService.class);
            startIntent.setAction(PlayerForegroundService.FAST_FORWARD_ACTION);
            context.startService(startIntent);
        }
    }

    /**
     * Called when user clicks the "fast rewind" button on the on-going system Notification.
     */
    public static class NotificationFastRewindButtonHandler extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Intent startIntent = new Intent(context, PlayerForegroundService.class);
            startIntent.setAction(PlayerForegroundService.FAST_REWIND_ACTION);
            context.startService(startIntent);
        }
    }

    /**
     * Called when user clicks the "close" button on the on-going system Notification.
     */
    public static class myNotificationCloseButtonHandler extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "Close Clicked", Toast.LENGTH_SHORT).show();
            Intent startIntent = new Intent(context, PlayerForegroundService.class);
            startIntent.setAction(PlayerForegroundService.STOP_ACTION);
            context.startService(startIntent);
        }


    }




    //create "close" button intent
    public static PendingIntent getButtonClosePendingIntent(Context context) {

        Intent buttonCloseIntent = new Intent(context, NotificationHelper.myNotificationCloseButtonHandler.class);
        buttonCloseIntent.putExtra("action", "close");
        return PendingIntent.getBroadcast(context, 0, buttonCloseIntent, 0);
    }

    //create "fast forwad"
    public static PendingIntent getButtonFastForwadPendingIntent(Context context) {

        Intent buttonCloseIntent = new Intent(context, NotificationHelper.NotificationFastForwardButtonHandler.class);
        buttonCloseIntent.putExtra("action", "close");
        return PendingIntent.getBroadcast(context, 0, buttonCloseIntent, 0);
    }

    //fast rewind
    public static PendingIntent getButtonFastRewindPendingIntent(Context context) {

        Intent buttonCloseIntent = new Intent(context, NotificationHelper.NotificationFastRewindButtonHandler.class);
        buttonCloseIntent.putExtra("action", "rewind");
        return PendingIntent.getBroadcast(context, 0, buttonCloseIntent, 0);
    }

    //passe
    public static PendingIntent getButtonPlayOrPausePendingIntent(Context context) {

        Intent buttonCloseIntent = new Intent(context, NotificationPlayOrPauseButtonHandler.class);
        return PendingIntent.getBroadcast(context, 0, buttonCloseIntent, 0);
    }

    //passe
    public static PendingIntent getButtonPlayPendingIntent(Context context, TextView textViewPause,TextView textViewPlay) {
        textViewPause.setVisibility(View.GONE);
        textViewPlay.setVisibility(View.VISIBLE);
        Intent buttonCloseIntent = new Intent(context, NotificationPlayOrPauseButtonHandler.class);
        buttonCloseIntent.putExtra("action", "pause");
        return PendingIntent.getBroadcast(context, 0, buttonCloseIntent, 0);
    }

    //create "resume" button intent
    public static PendingIntent getButtonResumePendingIntent(Context context) {

        Intent buttonCloseIntent = new Intent(context, NotificationPlayOrPauseButtonHandler.class);
        buttonCloseIntent.putExtra("action", "resume");
        return PendingIntent.getBroadcast(context, 0, buttonCloseIntent, 0);
    }


}