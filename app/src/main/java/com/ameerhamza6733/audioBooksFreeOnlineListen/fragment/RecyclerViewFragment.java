package com.ameerhamza6733.audioBooksFreeOnlineListen.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.ameerhamza6733.audioBooksFreeOnlineListen.MySharedPref;
import com.ameerhamza6733.audioBooksFreeOnlineListen.R;
import com.ameerhamza6733.audioBooksFreeOnlineListen.Util;
import com.ameerhamza6733.audioBooksFreeOnlineListen.activitys.MainActivity;
import com.ameerhamza6733.audioBooksFreeOnlineListen.adupters.BookCatalogueAdupter;
import com.ameerhamza6733.audioBooksFreeOnlineListen.adupters.CustomAdapter;
import com.ameerhamza6733.audioBooksFreeOnlineListen.models.AudioBook;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by AmeerHamza on 2/8/2018.
 */

public class RecyclerViewFragment extends Fragment implements MainActivity.ReciveQuery {

    public static final java.lang.String ARRGS_KEY = "ARRGS_KEY";
    public static final String MAKE_API_CALL = "MAKE_API_CALL";
    protected static final String KEY_LAYOUT_MANAGER = "layoutManager";
    protected static final String KEY_SHARED_PREF_CURRENT_CATALOG = "KEY_SHARED_PREF_CURRENT_CATALOG";
    private static final String TAG = "RecyclerViewFragment";
    protected LayoutManagerType mCurrentLayoutManagerType;
    protected RecyclerView mRecyclerViewPoetry;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected ProgressBar progressBar;
    protected FloatingActionButton floatingActionButton;
    protected Spinner mySpinner;
    protected Spinner mySpinnerFilter;
    private RequestQueue requsestQueue;
    private String type;

    @Override
    public void OnRecivedQuery(String query) {

        Log.d(TAG, "query: " + query);
        intiVolley(query);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.audio_book_list_frag, container, false);
        rootView.setTag(TAG);

        mRecyclerViewPoetry = (RecyclerView) rootView.findViewById(R.id.recyclerView_poetry);


        floatingActionButton = rootView.findViewById(R.id.fab);
        progressBar = rootView.findViewById(R.id.progressBar);

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
        floatingActionButton.setOnClickListener(v -> {
            DialogFragment bookSearchDialogFragment = BookSearchDialogFragment.newInstance();
            bookSearchDialogFragment.show(getFragmentManager(), "bookSearchDialogFragment");
        });

        requsestQueue = Volley.newRequestQueue(getActivity());

        mySpinner = rootView.findViewById(R.id.catalogue);
        mySpinnerFilter = rootView.findViewById(R.id.filter);
        setHasOptionsMenu(true);
        SetToSpinner(rootView);
        try {
            if (getArguments().getString(ARRGS_KEY).equalsIgnoreCase(MAKE_API_CALL)) {
                MySharedPref.saveObjectToSharedPreference(getActivity().getApplicationContext(), MySharedPref.SHARD_PREF_AUDIO_BOOK_FILE_NAME, KEY_SHARED_PREF_CURRENT_CATALOG, "librivox");
                String welcomeUrl = Util.INSTANCE.qurarySortBuilder("librivox", "addeddate+desc");
                intiVolley(welcomeUrl);
            }
        } catch (Exception e) {
        }
        try{
            ((AppCompatActivity)getActivity()).getSupportActionBar().show();
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Audio Books ");

        }catch (Exception e){
            e.printStackTrace();
        }
        return rootView;
    }


    /**
     * Set RecyclerView's LayoutManager to the one given.
     *
     * @param layoutManagerType Type of layout manager to switch to.
     */
    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;
        int scrollPostionDahzial = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerViewPoetry.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerViewPoetry.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();

        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(getActivity(), 2);
                mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        mRecyclerViewPoetry.setLayoutManager(mLayoutManager);
        mRecyclerViewPoetry.scrollToPosition(scrollPosition);

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        // Save currently selected layout manager.
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.meun_filter, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.action_filter:
                Log.d(TAG, "search iteam clicked");
                DialogFragment bookSearchDialogFragment = new FilterDialogFragment();
                bookSearchDialogFragment.show(getFragmentManager(), "filterDialog");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void SetToSpinner(View root) {
        List<String> catalogueList = Arrays.asList(getResources().getStringArray(R.array.catlog));
        List<String> filterList = Arrays.asList(getResources().getStringArray(R.array.filters));

        /*
        bind catalogue spinner
         */
        ArrayAdapter adapter = new BookCatalogueAdupter(getActivity(),
                R.layout.each_audio_file_spinner, catalogueList
        );


        mySpinner.setAdapter(adapter); // Set the custom adapter to the spinner
        // You can create an anonymous listener to handle the event when is selected an spinner item
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                if (position > 0) {
                    intiVolley(Util.INSTANCE.BuildQuery(catalogueList.get(position)));
                    MySharedPref.saveObjectToSharedPreference(getActivity().getApplicationContext(), MySharedPref.SHARD_PREF_AUDIO_BOOK_FILE_NAME, KEY_SHARED_PREF_CURRENT_CATALOG, catalogueList.get(position));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });
        /*
         bind filter spinner
         */
        ArrayAdapter filteradapter = new BookCatalogueAdupter(getActivity(),
                R.layout.each_audio_file_spinner, filterList
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mySpinnerFilter.setAdapter(filteradapter);
        mySpinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                if (position > 0) {
                    intiVolley(Util.INSTANCE.qurarySortBuilder(MySharedPref.getSavedObjectFromPreference(getActivity().getApplicationContext(), MySharedPref.SHARD_PREF_AUDIO_BOOK_FILE_NAME, RecyclerViewFragment.KEY_SHARED_PREF_CURRENT_CATALOG), filterList.get(position) + "+" + "desc"));

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });

    }

    /**
     * Generates Strings for RecyclerView's adapter. This data would usually come
     * from a local content provider or remote server.
     */
//    protected void initDatasetForPoetry(String url) {
//
//        progressBar.setVisibility(View.VISIBLE);
//        Log.d(TAG, "url: " + url);
//        MainActivityViewModel model = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);
//        model.loadData(Volley.newRequestQueue(getActivity()), url).observe(this, updatedAudioBookList -> {
//            // update UI
//            if (updatedAudioBookList != null) {
//                progressBar.setVisibility(View.GONE);
//                if (updatedAudioBookList.size() == 0) {
//                    Toast.makeText(getActivity(), "No results matched your criteria.", Toast.LENGTH_LONG).show();
//                } else {
//                    CustomAdapter mAdapter = new CustomAdapter(updatedAudioBookList);
//                    mRecyclerViewPoetry.setAdapter(mAdapter);
//                }
//
//            } else {
//                progressBar.setVisibility(View.GONE);
//                Snackbar
//
//                        .make(mRecyclerViewPoetry, "Connection Error", Snackbar.LENGTH_LONG)
//                        .setAction("Try again", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                initDatasetForPoetry(url);
//                            }
//                        }).setDuration(Snackbar.LENGTH_INDEFINITE).show();
//
//
//            }
//        });
//    }
    private void intiVolley(String s) {
        progressBar.setVisibility(View.VISIBLE);
        Log.d(TAG, "intiVolley");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, s, response -> {

            Observable.fromCallable(() -> {
                List<AudioBook> audioBookList = new ArrayList<>();
                try {
                    JSONObject jsonObject = Util.INSTANCE.toJson(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                    JSONArray jsonArray = jsonObject.getJSONObject("response").getJSONArray("docs");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject docs = jsonArray.getJSONObject(i);
                        if (docs.getString("identifier").toLowerCase().contains("librivox")) {
                            audioBookList.add(new AudioBook(Util.INSTANCE.ExtractRating(docs), Util.INSTANCE.ExtractDescription(docs), Util.INSTANCE.ExtractNoOfDownloads(docs), docs.getString("identifier"), Util.INSTANCE.ExtractNoReviews(docs), docs.getString("title"), Util.INSTANCE.ExtractPublisher(docs), Util.INSTANCE.ExtractMediaType(docs), Util.INSTANCE.EtracCreator(docs), Util.INSTANCE.ExtractData(docs)));

                        }
                    }

                    // Collections.shuffle(audioBookList);

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
                return audioBookList;
            }).subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((result) -> {
                        if (result != null) {

                            progressBar.setVisibility(View.GONE);
                            if (result.size() == 0) {
                                Toast.makeText(getActivity(), "No results matched your criteria.", Toast.LENGTH_LONG).show();
                            } else {
                                CustomAdapter mAdapter = new CustomAdapter(result);
                                mRecyclerViewPoetry.setAdapter(mAdapter);
                            }
                        } else {
                            Log.d("RecyclerViewFragment", "no open source book find");
                        }
                    });

            Log.d(MainActivity.TAG, "volley response");
        }, (error) -> {
            error.printStackTrace();
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "Error:", Toast.LENGTH_LONG).show();
            Log.d(MainActivity.TAG, "volley error" + error.getMessage());
        });
        requsestQueue.add(stringRequest);

    }

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }


}
