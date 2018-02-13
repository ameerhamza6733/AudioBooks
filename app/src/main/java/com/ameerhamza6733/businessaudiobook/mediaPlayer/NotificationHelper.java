package com.ameerhamza6733.businessaudiobook.mediaPlayer;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by AmeerHamza on 2/13/2018.
 */

public  class NotificationHelper {



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

    public static class NotificationPlayButtonHandler extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context,"Play Clicked",Toast.LENGTH_SHORT).show();

        }
    }

    public static class NotificationFastForwardButtonHandler extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context,"fast forward Clicked",Toast.LENGTH_SHORT).show();

        }
    }

    public static class NotificationFastRewindButtonHandler extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Intent Intent = new Intent(context, PlayerForegroundService.class);
            Intent.setAction(PlayerForegroundService.STOP_FOREGROUND_ACTION);
            context.startService(Intent);
        }
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


    public static PendingIntent getButtonClosePendingIntent(Context context) {

        Intent buttonCloseIntent = new Intent(context, NotificationHelper.myNotificationCloseButtonHandler.class);
        buttonCloseIntent.putExtra("action", "close");
        return PendingIntent.getBroadcast(context, 0, buttonCloseIntent, 0);
    }
    public static PendingIntent getButtonFastForwadPendingIntent(Context context) {

        Intent buttonCloseIntent = new Intent(context, NotificationHelper.NotificationFastForwardButtonHandler.class);
        buttonCloseIntent.putExtra("action", "close");
        return PendingIntent.getBroadcast(context, 0, buttonCloseIntent, 0);
    }
    public static PendingIntent getButtonFastRewindPendingIntent(Context context) {

        Intent buttonCloseIntent = new Intent(context, NotificationHelper.NotificationFastRewindButtonHandler.class);
        buttonCloseIntent.putExtra("action", "close");
        return PendingIntent.getBroadcast(context, 0, buttonCloseIntent, 0);
    }


}