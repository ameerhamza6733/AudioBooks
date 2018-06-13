package com.ameerhamza6733.audioBooksFreeOnlineListen.adupters;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ameerhamza6733.audioBooksFreeOnlineListen.R;
import com.ameerhamza6733.audioBooksFreeOnlineListen.activitys.MainActivity;
import com.ameerhamza6733.audioBooksFreeOnlineListen.fragment.OfflineBookChapterFragment;
import com.ameerhamza6733.audioBooksFreeOnlineListen.models.AudioBook;
import com.bumptech.glide.Glide;

import java.util.List;


import static com.ameerhamza6733.audioBooksFreeOnlineListen.fragment.OfflineBookChapterFragment.BUNDEL_KEY_BOOK_NO;

/**
 * Created by apple on 5/10/18.
 */

public class OfflineBookAdupter extends RecyclerView.Adapter<OfflineBookAdupter.ViewHolder>  {
    private List<AudioBook> audioBookList;
    private MainActivity activity;
    private static String TAG="OfflineSavedBookFragment";

    public OfflineBookAdupter(List<AudioBook> audioBookList,Activity activity) {
        Log.d(TAG,"OfflineBookAdupter");
        this.audioBookList = audioBookList;
        this.activity= (MainActivity) activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG,"onCreateViewHolder");
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.each_offline_saved_book_mata_data, parent, false);

        return new OfflineBookAdupter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(TAG,"onBindViewHolder");
        holder.getTitle().setText(audioBookList.get(position).getTitle());
        Glide.with(holder.getIcon().getContext())
                .load(R.drawable.audio_book_drawble)
                .into(holder.getIcon());
        holder.getRootView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OfflineBookChapterFragment offlineSavedMataDataFragment = new OfflineBookChapterFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(BUNDEL_KEY_BOOK_NO, position);
                offlineSavedMataDataFragment.setArguments(bundle);
                startFragmentTransction(offlineSavedMataDataFragment);
            }
        });
    }

    private void startFragmentTransction(Fragment fragment) {

        FragmentTransaction fragmentTransaction =activity. getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_contaner, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    @Override
    public int getItemCount() {
        return audioBookList.size();
    }

    public class ViewHolder extends RecyclerView. ViewHolder{
        private final TextView title;
        private final ImageView icon;
        private final View rootView;
        public ViewHolder(View itemView) {
            super(itemView);
            Log.d(TAG,"ViewHolder");
            rootView=itemView;
            title=itemView.findViewById(R.id.tvTitle);
            icon=itemView.findViewById(R.id.IV_ICon);
        }

        public View getRootView() {
            return rootView;
        }

        public TextView getTitle() {
            return title;
        }

        public ImageView getIcon() {
            return icon;
        }
    }
}
