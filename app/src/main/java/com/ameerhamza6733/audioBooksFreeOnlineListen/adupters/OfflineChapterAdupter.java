package com.ameerhamza6733.audioBooksFreeOnlineListen.adupters;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ameerhamza6733.audioBooksFreeOnlineListen.MySharedPref;
import com.ameerhamza6733.audioBooksFreeOnlineListen.R;
import com.ameerhamza6733.audioBooksFreeOnlineListen.mediaPlayer.PlayerForegroundService;
import com.ameerhamza6733.audioBooksFreeOnlineListen.models.AudioBook;
import com.ameerhamza6733.audioBooksFreeOnlineListen.models.MataData;

import java.util.List;

import static com.ameerhamza6733.audioBooksFreeOnlineListen.mediaPlayer.PlayerForegroundService.player;

/**
 * Created by AmeerHamza on 5/10/18.
 */

public class OfflineChapterAdupter extends OfflineBookAdupter {

    private String TAG = "OfflineChapterAdupter";
    private List<MataData> mataDataList;
    private Activity activity;
    private AudioBook audioBook;

    public OfflineChapterAdupter(List<AudioBook> audioBookList, Activity activity, List<MataData> mataDataList, int bookNumber) {
        super(audioBookList, activity);
        audioBook =audioBookList.get(bookNumber);
        audioBook.setMataData(mataDataList);
        MySharedPref.saveObjectToSharedPreference(activity, MySharedPref.SHARD_PREF_AUDIO_BOOK_FILE_NAME, PlayerForegroundService.KEY_SHARD_PREF_AUDIO_BOOK, audioBook);
        this.activity = activity;
        this.mataDataList = mataDataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.each_offline_saved_book_mata_data, parent, false);

        return new OfflineChapterAdupter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.getTitle().setText(mataDataList.get(position).getName());
        holder.getTitle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MySharedPref.saveObjectToSharedPreference(v.getContext(), MySharedPref.SHARD_PREF_AUDIO_BOOK_FILE_NAME, PlayerForegroundService.KEY_SHARD_PREF_AUDIO_BOOK, audioBook);
                MySharedPref.saveObjectToSharedPreference(v.getContext(),MySharedPref.SHARD_PREF_AUDIO_BOOK_FILE_NAME,PlayerForegroundService.KEY_PREFF_CURRENT_TRACK_INDEX,String.valueOf(position));

                if (player != null)
                    startPlayerService(PlayerForegroundService.ACTION_UPDATE_MEDIA_SOURCE, 0);
                else {
                    startPlayerService(null, 0);
                }

            }
        });

    }

    private void startPlayerService( String Action, long miliSecond) {
        Intent startIntent = new Intent(activity, PlayerForegroundService.class);
        startIntent.putExtra(PlayerForegroundService.KEY_PREFF_CURRENT_TRACK_INDEX, miliSecond);
        if (Action!=null)
            startIntent.setAction(Action);

        if (Build.VERSION.SDK_INT > 25) {
            activity.startForegroundService(startIntent);
        } else
            activity.startService(startIntent);

    }

    @Override
    public int getItemCount() {
        if (mataDataList == null) {
            Log.d(TAG, "null");
        }
        return mataDataList.size();
    }
}
