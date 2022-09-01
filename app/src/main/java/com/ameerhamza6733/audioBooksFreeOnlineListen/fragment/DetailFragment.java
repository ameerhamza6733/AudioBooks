package com.ameerhamza6733.audioBooksFreeOnlineListen.fragment;

import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ameerhamza6733.audioBooksFreeOnlineListen.MySharedPref;
import com.ameerhamza6733.audioBooksFreeOnlineListen.R;
import com.ameerhamza6733.audioBooksFreeOnlineListen.Util;
import com.ameerhamza6733.audioBooksFreeOnlineListen.activitys.DownloaderActivty;
import com.ameerhamza6733.audioBooksFreeOnlineListen.models.AudioBook;
import com.ameerhamza6733.audioBooksFreeOnlineListen.models.MataData;
import com.ameerhamza6733.audioBooksFreeOnlineListen.viewModels.BookChapterViewModel;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static com.ameerhamza6733.audioBooksFreeOnlineListen.activitys.DetailTabActivity.KEY_SHARD_PREF_AUDIO_BOOK;

/**
 * Created by AmeerHamza on 4/23/18.
 */

public class DetailFragment extends Fragment {
    private RelativeLayout RVimageView;
    private TextView mTVDetail;
    private TextView mtvRating;
    private TextView mTVDownloads;
    private TextView mTVCreator;
    private TextView mTVDate;
    private AudioBook audioBook;
    private ImageView imageView;
    private TextView mTVTitle;
    private FloatingActionButton fabDownlaod;
    private ArrayList<MataData> mataDataList;
    private static String TAG= "DetailFragment";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_fragment, container, false);
        audioBook = MySharedPref.getSavedObjectFromPreference(getActivity().getApplicationContext(), MySharedPref.SHARD_PREF_AUDIO_BOOK_FILE_NAME, KEY_SHARD_PREF_AUDIO_BOOK, AudioBook.class);
        bindViews(view);
        return view;
    }

    private void bindViews(View view) {
        RVimageView = view.findViewById(R.id.RVimageView);
        mTVDetail = view.findViewById(R.id.detail);
        mtvRating = view.findViewById(R.id.rating);
        mTVDownloads = view.findViewById(R.id.downloads);
        mTVCreator = view.findViewById(R.id.creator);

        imageView = view.findViewById(R.id.iamge);
        mTVDate=view.findViewById(R.id.pubdate);
        mTVTitle =view.findViewById(R.id.title);
        fabDownlaod=view.findViewById(R.id.fabDownlaod);
        mTVDate.setText(audioBook.getData());
        mTVDetail.setText(parseDicription());
        mtvRating.setText(audioBook.getAvg_rating());
        mTVDownloads.setText(audioBook.getDownloads());
        mTVCreator.setText(audioBook.getCreator());
        mTVTitle.setText(audioBook.getTitle());


        Glide.with(DetailFragment.this)
                .asBitmap()
                .load(Util.INSTANCE.toImageURI(audioBook.getIdentifier()))
                .into(imageView);
        fabDownlaod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"onClick");
                if (mataDataList != null && mataDataList.size() > 0) {
                    Intent downloadingIntent = new Intent(v.getContext(), DownloaderActivty.class);

                    v.getContext().startActivity(downloadingIntent);
                } else {
                    Toast.makeText(getActivity(), "Please wait ", Toast.LENGTH_LONG).show();
                }
            }
        });
        BookChapterViewModel model = ViewModelProviders.of(this).get(BookChapterViewModel.class);
        if (audioBook.getIdentifier() != null)
            model.loadData(Volley.newRequestQueue(getActivity()), Util.INSTANCE.toMetaDataURI(audioBook.getIdentifier()), audioBook.getIdentifier()).observe(this, mataDataList -> {
                // update UI
                if (mataDataList != null) {
                    this.mataDataList = (ArrayList<MataData>) mataDataList;
                    audioBook.setMataData(mataDataList);
                    Log.d(TAG,"dataset size"+mataDataList.size());

                } else {

                }


            });

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
