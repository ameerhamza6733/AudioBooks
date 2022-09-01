package com.ameerhamza6733.audioBooksFreeOnlineListen.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import android.content.SharedPreferences;
import android.util.Log;

import com.ameerhamza6733.audioBooksFreeOnlineListen.MySharedPref;
import com.ameerhamza6733.audioBooksFreeOnlineListen.models.AudioBook;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ameerhamza on 5/10/18.
 */

public class OfflineBooksViewModle extends ViewModel {
    protected MutableLiveData<List<AudioBook>> listMutableAudioBook;
    protected List<AudioBook> audioBookList;
    private static final String TAG = "OfflineBooksViewModle";

    public LiveData<List<AudioBook>> getAllSavedAudioBooks(SharedPreferences sharedPreferences) {
        if (listMutableAudioBook == null) {
            listMutableAudioBook = new MutableLiveData<>();
            audioBookList = new ArrayList<>();
            Map<String, ?> allEntries = MySharedPref.getAllKeys(sharedPreferences);
            for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                final Gson gson = new Gson();
                String data = (String) allEntries.get(entry.getKey());
                AudioBook audioBook = gson.fromJson(data, AudioBook.class);
                audioBookList.add(audioBook);
            }
            listMutableAudioBook.setValue(audioBookList);
        }else {
            Log.d(TAG,"returng old data");
        }
        return listMutableAudioBook;

    }

}
