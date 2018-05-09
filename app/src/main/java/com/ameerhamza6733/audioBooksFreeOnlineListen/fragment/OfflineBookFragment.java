package com.ameerhamza6733.audioBooksFreeOnlineListen.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ameerhamza6733.audioBooksFreeOnlineListen.MySharedPref;
import com.ameerhamza6733.audioBooksFreeOnlineListen.R;
import com.ameerhamza6733.audioBooksFreeOnlineListen.models.AudioBook;

import java.util.Map;

/**
 * Created by apple on 5/9/18.
 */

public class OfflineBookFragment extends Fragment {
    AudioBook audioBook;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.offline_saved_book_fragemnt,container,false);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Map<String, ?> allEntries =  MySharedPref.getAllKeys(getActivity().getApplicationContext(),MySharedPref.SHARD_PREF_DOWNLOADED_AUDIO_BOOK);
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
        }
    }
}
