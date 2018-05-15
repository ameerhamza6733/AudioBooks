package com.ameerhamza6733.audioBooksFreeOnlineListen.viewModels;


import android.arch.lifecycle.LiveData;
import android.content.SharedPreferences;
import android.util.Log;

import com.ameerhamza6733.audioBooksFreeOnlineListen.models.AudioBook;

import java.util.List;

/**
 * Created by apple on 5/10/18.
 */

public class HistoryViewModel extends OfflineBooksViewModle {
    @Override
    public LiveData<List<AudioBook>> getAudioBook(SharedPreferences sharedPreferences) {
        return super.getAudioBook(sharedPreferences);
    }
}