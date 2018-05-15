package com.ameerhamza6733.audioBooksFreeOnlineListen.adupters;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ameerhamza6733.audioBooksFreeOnlineListen.R;
import com.ameerhamza6733.audioBooksFreeOnlineListen.mediaPlayer.PlayerForegroundService;
import com.ameerhamza6733.audioBooksFreeOnlineListen.models.AudioBook;
import com.ameerhamza6733.audioBooksFreeOnlineListen.models.MataData;

import java.util.List;

import static com.ameerhamza6733.audioBooksFreeOnlineListen.mediaPlayer.PlayerForegroundService.EXTRA_URI;

/**
 * Created by AmeerHamza on 5/10/18.
 */

public class OfflineMataDataAdupter extends OfflineBookAdupter {
private String TAG = "OfflineMataDataAdupter";
private List<MataData >mataDataList;
private Activity activity;

    Handler handler=new Handler();
    public OfflineMataDataAdupter(List<AudioBook> audioBookList, Activity activity,List<MataData> mataDataList) {
        super(audioBookList, activity);
        this.activity=activity;
        this.mataDataList=mataDataList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.each_offline_saved_book_mata_data, parent, false);

        return new OfflineMataDataAdupter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

            holder.getTitle().setText(mataDataList.get(position).getName());
            holder.getTitle().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (PlayerForegroundService.isPlaying) {
                        startPlayerService(mataDataList.get(position), PlayerForegroundService.PLAYER_PLAY_PAUSE_ACTION, null, 0);
                    } else {
                        stopPlayerService(mataDataList.get(position));


                        handler.postDelayed(() -> {
                            startPlayerService(mataDataList.get(position), PlayerForegroundService.ACTION_START, null, 0);

                        }, 2000);
                    }}
            });

    }
    private void stopPlayerService(MataData mataData)  {
        try {
            Intent startIntent = new Intent(activity, PlayerForegroundService.class);
            startIntent.putExtra(EXTRA_URI, mataData.getSdPath());
            startIntent.setAction(PlayerForegroundService.STOP_ACTION);
            activity.startService(startIntent);
        }catch (Exception e){e.printStackTrace();}
    }
    private void startPlayerService(MataData mataData, String Action, String extraKey, long miliSecond)   {

        try {
            Intent startIntent = new Intent(activity, PlayerForegroundService.class);
            if (mataData != null) {
                startIntent.putExtra(EXTRA_URI, mataData.getSdPath());
                startIntent.putExtra(PlayerForegroundService.EXTRA_TITLE, mataData.getName());
            }
            startIntent.putExtra(extraKey, miliSecond);
            startIntent.setAction(Action);
            activity.startService(startIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public int getItemCount() {
if (mataDataList==null){
    Log.d(TAG,"null");
}
        return mataDataList.size();
    }
}
