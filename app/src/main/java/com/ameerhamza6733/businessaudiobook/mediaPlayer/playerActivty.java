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

public class playerActivty extends AppCompatActivity {

    public static final String EXTRA_PLAYER_URI = "EXTRA_PLAYER_URI";
    public static final String EXTRA_TITLE = "EXTRA_TITLE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getIntent()!=null){
            Intent startIntent = new Intent(playerActivty.this, PlayerForegroundService.class);
            startIntent.putExtra(EXTRA_URI,getIntent().getStringExtra(EXTRA_PLAYER_URI));
            startIntent.putExtra(PlayerForegroundService.EXTRA_TITLE,getIntent().getStringExtra(playerActivty.EXTRA_TITLE));
            startIntent.setAction(PlayerForegroundService.START_FOREGROUND_ACTION);
            startService(startIntent);
            finish();

        }


    }


}