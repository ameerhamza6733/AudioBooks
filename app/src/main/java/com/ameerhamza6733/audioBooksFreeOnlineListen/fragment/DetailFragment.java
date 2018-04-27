package com.ameerhamza6733.audioBooksFreeOnlineListen.fragment;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.ameerhamza6733.audioBooksFreeOnlineListen.MySharedPref;
import com.ameerhamza6733.audioBooksFreeOnlineListen.R;
import com.ameerhamza6733.audioBooksFreeOnlineListen.Util;
import com.ameerhamza6733.audioBooksFreeOnlineListen.models.AudioBook;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import static com.ameerhamza6733.audioBooksFreeOnlineListen.activitys.DetailTabActivity.KEY_SHARD_PREF_AUDIO_BOOK;

/**
 * Created by apple on 4/23/18.
 */

public class DetailFragment extends Fragment {
    private RelativeLayout RVimageView;
    private TextView mTVDetail;
    private TextView mtvRating;
    private TextView mTVViews;
    private TextView mTVCreator;
    private TextView mTVDate;
    private Spinner mChapterSpinner;
    private AudioBook audioBook;
    private ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_fragment, container, false);
        audioBook = MySharedPref.getSavedObjectFromPreference(getActivity().getApplicationContext(), MySharedPref.SHARD_PREF_AUDIO_BOOK, KEY_SHARD_PREF_AUDIO_BOOK, AudioBook.class);
        bindViews(view);
        return view;
    }

    private void bindViews(View view) {
        RVimageView = view.findViewById(R.id.RVimageView);
        mTVDetail = view.findViewById(R.id.detail);
        mtvRating = view.findViewById(R.id.rating);
        mTVViews = view.findViewById(R.id.views);
        mTVCreator = view.findViewById(R.id.creator);
        mChapterSpinner = view.findViewById(R.id.spinner1);
        imageView = view.findViewById(R.id.iamge);
        mTVDate=view.findViewById(R.id.pubdate);

        mTVDate.setText(audioBook.getData());
        mTVDetail.setText(parseDicription());
        mtvRating.setText(audioBook.getAvg_rating());
        mTVViews.setText(audioBook.getNum_reviews());
        mTVCreator.setText(audioBook.getCreator());
        Glide.with(this).asBitmap()
                .load(Util.INSTANCE.toImageURI(audioBook.getIdentifier()))
                .apply(new RequestOptions().override(8, 8))
                .into(new SimpleTarget<Bitmap>(RVimageView.getWidth(), RVimageView.getHeight()) {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        imageView.setImageBitmap(resource);
                        Drawable drawable = new BitmapDrawable(getActivity().getResources(), resource);
                        RVimageView.setBackground(drawable);
                    }
                });
        Glide.with(this)
                .asBitmap()
                .load(Util.INSTANCE.toImageURI(audioBook.getIdentifier()))
                .into(imageView);

    }

    private String parseDicription() {

        try {
            StringBuffer stringBuffer = new StringBuffer(audioBook.getDescription());
            return stringBuffer.replace(audioBook.getDescription().indexOf("For further information,"), audioBook.getDescription().length(), "").toString();

        } catch (Exception e) {
            return "";
        }
    }
}
