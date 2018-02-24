package com.ameerhamza6733.audioBooksFreeOnlineListen.adupters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ameerhamza6733.audioBooksFreeOnlineListen.Util;
import com.ameerhamza6733.audioBooksFreeOnlineListen.mediaPlayer.playerActivty;
import com.ameerhamza6733.audioBooksFreeOnlineListen.models.MataData;
import com.ameerhamza6733.audioBooksFreeOnlineListen.R;

import java.util.List;

/**
 * Created by AmeerHamza on 2/9/2018.
 */

public class BookEachMataDataAdupter extends RecyclerView.Adapter<BookEachMataDataAdupter.ViewHolder> {
    private static List<MataData> chapterList;

    private static String TAG = "BookEachMataDataAdupter";

    public BookEachMataDataAdupter(List<MataData> chapterList) {
        this.chapterList = chapterList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.each_book_meta_data, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

            holder.getChapterTitle().setText(chapterList.get(position).getName());
        Log.d(TAG,"isfomateSupported: "+Util.INSTANCE.isSuppotedFormate(chapterList.get(position).getName()));
    }

    @Override
    public int getItemCount() {
        return chapterList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView chapterTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            chapterTitle = itemView.findViewById(R.id.chapterName);
            itemView.setOnClickListener(view -> {
                Log.d(TAG, "chapter name: " + chapterList.get(getAdapterPosition()).getName());
                Intent intent = new Intent(view.getContext(),playerActivty.class);
                intent.putExtra(playerActivty.EXTRA_TITLE,chapterList.get(getAdapterPosition()).getName());
                intent.putExtra(playerActivty.EXTRA_PLAYER_URI,chapterList.get(getAdapterPosition()).getURL());
                view.getContext().startActivity(intent);
            });
        }

        public TextView getChapterTitle() {
            return chapterTitle;
        }
    }
}
