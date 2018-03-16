package com.ameerhamza6733.audioBooksFreeOnlineListen.adupters;

/**
 * Created by AmeerHamza on 2/8/2018.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ameerhamza6733.audioBooksFreeOnlineListen.models.AudioBook;
import com.ameerhamza6733.audioBooksFreeOnlineListen.activitys.DetailActivity;
import com.ameerhamza6733.audioBooksFreeOnlineListen.R;
import com.ameerhamza6733.audioBooksFreeOnlineListen.Util;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;


import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private static final String TAG = "CustomAdapter";

    protected  List<AudioBook> mDataSet;

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public  class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewTitle;
        private final TextView textViewAuthor;
        private final TextView textViewRatingCount;
        private final TextView textViewMediaType;
        private final TextView textViewViewCount;
        private final ImageView imageView;
        private final Context context;

        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
                    Intent intent = new Intent(v.getContext(),DetailActivity.class);
                    intent.putExtra(DetailActivity.EXTRA_IDENTEFIER_NAME,mDataSet.get(getAdapterPosition()).getIdentifier());
                    v.getContext().startActivity(intent);
                }
            });
            textViewTitle = (TextView) v.findViewById(R.id.title);
            textViewAuthor=v.findViewById(R.id.source);
            textViewMediaType=v.findViewById(R.id.type);
            textViewRatingCount= v.findViewById(R.id.rating);
            textViewViewCount=v.findViewById(R.id.views);
            imageView= v.findViewById(R.id.imageView);
            context=v.getContext();
        }

        public TextView getTextViewTitle() {
            return textViewTitle;
        }

        public TextView getTextViewAuthor() {
            return textViewAuthor;
        }

        public TextView getTextViewRatingCount() {
            return textViewRatingCount;
        }

        public TextView getTextViewMediaType() {
            return textViewMediaType;
        }

        public TextView getTextViewViewCount() {
            return textViewViewCount;
        }

        public ImageView getImageView() {
            return imageView;
        }

        public Context getContext() {
            return context;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet containing the data to populate views to be used by RecyclerView.
     */
    public CustomAdapter(List<AudioBook> dataSet) {
        mDataSet = dataSet;
    }

    public CustomAdapter() {
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.each_book, viewGroup, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        viewHolder.getTextViewTitle().setText(mDataSet.get(position).getTitle());
        viewHolder.getTextViewRatingCount().setText(mDataSet.get(position).getAvg_rating());
        viewHolder.getTextViewMediaType().setText(mDataSet.get(position).getMediatype());
        viewHolder.getTextViewAuthor().setText("source: librivox");
        viewHolder.getTextViewViewCount().setText(mDataSet.get(position).getDownloads());
        Glide.with(viewHolder.getContext())
                .asBitmap()
                .load(Util.INSTANCE.toImageURI(mDataSet.get(position).getIdentifier()))
                .into(new SimpleTarget<Bitmap>(150,150) {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {

                        viewHolder.getImageView().setImageBitmap(resource);
                    }
                });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}