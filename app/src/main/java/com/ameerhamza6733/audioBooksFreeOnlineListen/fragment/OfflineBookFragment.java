package com.ameerhamza6733.audioBooksFreeOnlineListen.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ameerhamza6733.audioBooksFreeOnlineListen.MySharedPref;
import com.ameerhamza6733.audioBooksFreeOnlineListen.R;
import com.ameerhamza6733.audioBooksFreeOnlineListen.adupters.OfflineBookAdupter;
import com.ameerhamza6733.audioBooksFreeOnlineListen.models.AudioBook;
import com.ameerhamza6733.audioBooksFreeOnlineListen.viewModels.OfflineBooksViewModle;

import java.util.List;
import java.util.Map;

import static com.ameerhamza6733.audioBooksFreeOnlineListen.MySharedPref.SHARD_PREF_DOWNLOADED_AUDIO_BOOK;

/**
 * Created by AmeerHamza on 5/9/18.
 */

public class OfflineBookFragment extends Fragment {
    private String TAG="OfflineBookFragment";
    private View rootView;
    private List<AudioBook> audioBookList;
    private RecyclerView recyclerView;


    public OfflineBookFragment() {
    }

    private OfflineBookAdupter offlineBookAdupter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView==null)
        rootView = inflater.inflate(R.layout.offline_saved_book_fragemnt,container,false);
         recyclerView = rootView.findViewById(R.id.recyclerViewOfilineSavedBooks);
        Log.d(TAG,"onCreateView");
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
       try{
           ((AppCompatActivity)getActivity()).getSupportActionBar().show();
           ((AppCompatActivity)getActivity()).getSupportActionBar() .setTitle("Downloaded Books");
       }catch (Exception e){
e.printStackTrace();
       }
        intiDataSet();
        return rootView;
    }

    private void intiDataSet() {
        OfflineBooksViewModle offlineBooksViewModle = ViewModelProviders.of(getActivity()).get(OfflineBooksViewModle.class);
        offlineBooksViewModle.getAudioBook(getActivity().getApplicationContext().getSharedPreferences(SHARD_PREF_DOWNLOADED_AUDIO_BOOK, 0)).observe(this, new Observer<List<AudioBook>>() {
            @Override
            public void onChanged(@Nullable List<AudioBook> audioBooks) {
                if (audioBooks!=null && audioBooks.size()>0){
                    audioBookList=audioBooks;
                    setAupter();

                }else {
                    Toast.makeText(getActivity(),"You don't have any offline book",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setAupter() {
        offlineBookAdupter = new OfflineBookAdupter(audioBookList,getActivity());
        recyclerView.setAdapter(offlineBookAdupter);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");
    }
}
