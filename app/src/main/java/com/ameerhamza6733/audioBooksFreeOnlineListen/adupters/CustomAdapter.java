package com.ameerhamza6733.audioBooksFreeOnlineListen.adupters;

/**
 * Created by AmeerHamza on 2/8/2018.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.ameerhamza6733.audioBooksFreeOnlineListen.MySharedPref;
import com.ameerhamza6733.audioBooksFreeOnlineListen.R;
import com.ameerhamza6733.audioBooksFreeOnlineListen.Util;
import com.ameerhamza6733.audioBooksFreeOnlineListen.activitys.DetailTabActivity;
import com.ameerhamza6733.audioBooksFreeOnlineListen.models.AudioBook;
import com.bumptech.glide.Glide;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private static final String TAG = "CustomAdapter";

    protected List<AudioBook> mDataSet;

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

        viewHolder.getTextViewTitle().setText(mDataSet.get(position).getTitle());
        viewHolder.getTextViewRatingCount().setText(mDataSet.get(position).getAvg_rating());
        viewHolder.getCreator().setText(mDataSet.get(position).getCreator());
        viewHolder.getSubmittedAgo().setText(mDataSet.get(position).getData());
        viewHolder.getTextViewAuthor().setText("source: librivox");
        if (Util.INSTANCE.toImageURI(mDataSet.get(position).getIdentifier()) != null) {
            //    Log.d(TAG,"iamge url"+Util.INSTANCE.toImageURI(mDataSet.get(position).getIdentifier()));
            Glide.with(viewHolder.getContext())
                    .asBitmap()

                    .load(Util.INSTANCE.toImageURI(mDataSet.get(position).getIdentifier()))

                    .into(viewHolder.getImageView());
        } else {
            Glide.with(viewHolder.getContext()).clear(viewHolder.getImageView());
        }
        viewHolder.getAppCompatImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v, position);
            }
        });

    }


    private void showPopupMenu(View view, int position) {
        // inflate menu
        PopupMenu popup = new PopupMenu(view.getContext(), view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.popup, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(position, view.getContext()));
        popup.show();
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewTitle;
        private final TextView textViewAuthor;
        private final TextView textViewRatingCount;
        //        private final TextView textViewMediaType;
//        private final TextView textViewViewCount;
        private final TextView submittedAgo;
        private final ImageView imageView;
        private final TextView creator;
        private final Context context;
        private final AppCompatImageButton appCompatImageButton;


        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Please wait", Toast.LENGTH_SHORT).show();
                    boolean isSaved = MySharedPref.saveObjectToSharedPreference(v.getContext().getApplicationContext(), MySharedPref.SHARD_PREF_AUDIO_BOOK_FILE_NAME, DetailTabActivity.KEY_SHARD_PREF_AUDIO_BOOK, mDataSet.get(getAdapterPosition()));
                    MySharedPref.saveObjectToSharedPreference(v.getContext().getApplicationContext(), MySharedPref.SHARD_PREF_HISTORY_AUDIO_BOOK_FILE_NAME, mDataSet.get(getAdapterPosition()).getIdentifier(), mDataSet.get(getAdapterPosition()));
                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
                    Intent intent = new Intent(v.getContext(), DetailTabActivity.class);
                    if (isSaved)
                        v.getContext().startActivity(intent);
                    else {
                        Toast.makeText(v.getContext(), "Please restart app some thing wrong", Toast.LENGTH_LONG).show();
                    }
                }
            });
            textViewTitle = (TextView) v.findViewById(R.id.title);
            textViewAuthor = v.findViewById(R.id.source);
//            textViewMediaType=v.findViewById(R.id.type);
            textViewRatingCount = v.findViewById(R.id.rating);
//            textViewViewCount=v.findViewById(R.id.views);
            imageView = v.findViewById(R.id.imageView);
            creator = v.findViewById(R.id.creator);
            submittedAgo = v.findViewById(R.id.time);
            appCompatImageButton = v.findViewById(R.id.pop_menu);
            context = v.getContext();

        }

        public TextView getTextViewTitle() {
            return textViewTitle;
        }

        public TextView getTextViewAuthor() {
            return textViewAuthor;
        }

        public TextView getCreator() {
            return creator;
        }

        public TextView getTextViewRatingCount() {
            return textViewRatingCount;
        }

        public TextView getSubmittedAgo() {
            return submittedAgo;
        }
        //
//        public TextView getTextViewMediaType() {
//            return textViewMediaType;
//        }
//
//        public TextView getTextViewViewCount() {
//            return textViewViewCount;
//        }

        public ImageView getImageView() {
            return imageView;
        }

        public Context getContext() {
            return context;
        }

        public AppCompatImageButton getAppCompatImageButton() {
            return appCompatImageButton;
        }
    }

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        private int position;
        private Context context;

        public MyMenuItemClickListener(int positon, Context context) {
            this.position = positon;
            this.context = context;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {

                case R.id.menu_book_mark:
                    MySharedPref.saveObjectToSharedPreference(context.getApplicationContext(), MySharedPref.SHARD_PREF_BOOK_MARK_FILE_NAME, mDataSet.get(position).getIdentifier(), mDataSet.get(position));
                    Toast.makeText(context, "Bookmarks", Toast.LENGTH_LONG).show();
                    return true;

                default:
            }
            return false;
        }
    }
}