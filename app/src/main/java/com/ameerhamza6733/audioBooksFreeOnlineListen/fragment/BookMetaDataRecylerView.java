package com.ameerhamza6733.audioBooksFreeOnlineListen.fragment;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.ameerhamza6733.audioBooksFreeOnlineListen.R;
import com.ameerhamza6733.audioBooksFreeOnlineListen.Util;
import com.ameerhamza6733.audioBooksFreeOnlineListen.adupters.BookEachMataDataAdupter;
import com.ameerhamza6733.audioBooksFreeOnlineListen.viewModels.MetaDataViewModel;
import com.android.volley.toolbox.Volley;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookMetaDataRecylerView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookMetaDataRecylerView extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final String ARG_IDENTIFIER = "ARG_IDENTIFIER";
    private static final String TAG ="EachChapterRecylerView";

    private String identifier;
    private RecyclerView mRecyclerView;
    private ProgressBar progressBar;

    public BookMetaDataRecylerView() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param identifier identifier.
     * @return A new instance of fragment BookMetaDataRecylerView.
     */
    // TODO: Rename and change types and number of parameters
    public static BookMetaDataRecylerView newInstance(String identifier) {
        BookMetaDataRecylerView fragment = new BookMetaDataRecylerView();
        Bundle args = new Bundle();
        args.putString(ARG_IDENTIFIER, identifier);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_audio_file_recyler_view, container, false);
        rootView.setTag(TAG);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView_poetry);
        progressBar = rootView.findViewById(R.id.progressBar);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        if (getArguments() != null) {
            identifier= getArguments().getString(ARG_IDENTIFIER);
            intiDataSet();
        }

        return rootView;
    }


    private BookEachMataDataAdupter mataDataAdupter;
    private void intiDataSet(){
        progressBar.setVisibility(View.VISIBLE);
       MetaDataViewModel model = ViewModelProviders.of(this).get(MetaDataViewModel.class);
       if (getActivity()!=null && identifier!=null)
       model.loadData(Volley.newRequestQueue(getActivity()), Util.INSTANCE.toMetaDataURI(identifier),identifier).observe(this, audioFileList -> {
           // update UI
           if (audioFileList != null) {
               progressBar.setVisibility(View.GONE);
               if (mataDataAdupter==null){
                   mataDataAdupter = new BookEachMataDataAdupter(audioFileList);
                   mRecyclerView.setAdapter(mataDataAdupter);
               }else {
                   mataDataAdupter.notifyDataSetChanged();
               }


           }
       });
    }

}
