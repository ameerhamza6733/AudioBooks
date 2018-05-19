package com.ameerhamza6733.audioBooksFreeOnlineListen.viewModels;

import android.arch.lifecycle.LiveData;
import android.content.SharedPreferences;


import com.ameerhamza6733.audioBooksFreeOnlineListen.models.AudioBook;

import java.util.List;

/**
 * Created by apple on 5/12/18.
 */

public class BookMarkViewModel extends OfflineBooksViewModle {
    @Override
    public LiveData<List<AudioBook>> getAllSavedAudioBooks(SharedPreferences sharedPreferences) {
        return super.getAllSavedAudioBooks(sharedPreferences);
    }
}
