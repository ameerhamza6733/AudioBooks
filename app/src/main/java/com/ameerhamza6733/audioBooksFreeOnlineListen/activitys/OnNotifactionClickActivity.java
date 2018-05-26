package com.ameerhamza6733.audioBooksFreeOnlineListen.activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ameerhamza6733.audioBooksFreeOnlineListen.R;
import com.ameerhamza6733.audioBooksFreeOnlineListen.mediaPlayer.PlayerForegroundService;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerControlView;

public class OnNotifactionClickActivity extends AppCompatActivity {
    Player player;
    private PlayerControlView mPlayerView;
    private String url;
    public static final String EXTRA_URL ="EXTRA_URL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_notifaction_click);
        mPlayerView = findViewById(R.id.player_view);
        if (PlayerForegroundService.player != null) {
            player = PlayerForegroundService.player;
            mPlayerView.setPlayer(player);
        }

    }
}
