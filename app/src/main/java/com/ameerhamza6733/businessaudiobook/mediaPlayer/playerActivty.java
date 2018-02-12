package com.ameerhamza6733.businessaudiobook.mediaPlayer;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.ameerhamza6733.businessaudiobook.R;


import static com.ameerhamza6733.businessaudiobook.mediaPlayer.PlayerForegroundService.EXTRA_URI;

public class playerActivty extends AppCompatActivity implements ServiceConnection {

    public static final String EXTRA_PLAYER_URI = "EXTRA_PLAYER_URI";
    private static final String TAG = "playerActivtyTAG";

    private PlayerForegroundService playerForegroundService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getIntent()!=null){
            Intent startIntent = new Intent(playerActivty.this, PlayerForegroundService.class);
            startIntent.putExtra(EXTRA_URI,getIntent().getStringExtra(EXTRA_PLAYER_URI));
            startIntent.setAction(PlayerForegroundService.START_FOREGROUND_ACTION);
            startService(startIntent);

        }


    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder binder) {
        PlayerForegroundService.MyBinder b = (PlayerForegroundService.MyBinder) binder;
        playerForegroundService = b.getService();
        Toast.makeText(playerActivty.this, "Connected service name: "+PlayerForegroundService.class.getSimpleName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        playerForegroundService = null;
    }
}