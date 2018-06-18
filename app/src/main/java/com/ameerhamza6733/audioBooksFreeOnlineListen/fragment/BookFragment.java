package com.ameerhamza6733.audioBooksFreeOnlineListen.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
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
import com.ameerhamza6733.audioBooksFreeOnlineListen.mediaPlayer.PlayerForegroundService;
import com.ameerhamza6733.audioBooksFreeOnlineListen.viewModels.BookViewModel;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.Arrays;
import java.util.List;

/**
 * Created by AmeerHamza on 2/8/2018.
 */

public class BookFragment extends Fragment implements MainActivity.ReciveQuery {

    public static final java.lang.String ARRGS_KEY = "ARRGS_KEY";
    public static final String WelcomeCall = "WelcomeCall";
    protected static final String KEY_LAYOUT_MANAGER = "layoutManager";
    protected static final String KEY_SHARED_PREF_CURRENT_CATALOG = "KEY_SHARED_PREF_CURRENT_CATALOG";
    private static final String TAG = "BookFragment";
    protected LayoutManagerType mCurrentLayoutManagerType;
    protected RecyclerView recyclerView;
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
        makeApiCall(query);

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

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView_poetry);



        floatingActionButton = rootView.findViewById(R.id.fabStop);
        progressBar = rootView.findViewById(R.id.progressBar);

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        floatingActionButton.setOnClickListener(v -> {
           if (PlayerForegroundService.player!=null){
             startPlayerService(PlayerForegroundService.STOP_ACTION,0);
               floatingActionButton.setVisibility(View.INVISIBLE);
           }
        });

        requsestQueue = Volley.newRequestQueue(getActivity());

        mySpinner = rootView.findViewById(R.id.catalogue);
        mySpinnerFilter = rootView.findViewById(R.id.filter);
        setHasOptionsMenu(true);
        SetToSpinner(rootView);
        try {

            if (getArguments().getString(ARRGS_KEY).equalsIgnoreCase(WelcomeCall)) {
                MySharedPref.saveObjectToSharedPreference(getActivity().getApplicationContext(), MySharedPref.SHARD_PREF_AUDIO_BOOK_FILE_NAME, KEY_SHARED_PREF_CURRENT_CATALOG, "librivox");
                String welcomeUrl = Util.INSTANCE.SubjectSortBuilder("librivox", "addeddate+desc");
                makeApiCall(welcomeUrl);
            }
        } catch (Exception e) {
        }
        try{
            ((AppCompatActivity)getActivity()).getSupportActionBar().show();
            ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle("Home");
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Audio Books ");

        }catch (Exception e){
            e.printStackTrace();
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (PlayerForegroundService.player!=null && floatingActionButton!=null)
            floatingActionButton.setVisibility(View.VISIBLE);
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
        if (recyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();

        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(getActivity(), 2);
                mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.scrollToPosition(scrollPosition);

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        // Save currently selected layout manager.
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.meun_search, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.action_search:
                Log.d(TAG, "search iteam clicked");
                DialogFragment bookSearchDialogFragment = BookSearchDialogFragment.newInstance();
                bookSearchDialogFragment.show(getFragmentManager(), "bookSearchDialogFragment");
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
                    makeApiCall(Util.INSTANCE.BuildQuery(catalogueList.get(position)));
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
                    makeApiCall(Util.INSTANCE.SubjectSortBuilder(MySharedPref.getSavedObjectFromPreference(getActivity().getApplicationContext(), MySharedPref.SHARD_PREF_AUDIO_BOOK_FILE_NAME, BookFragment.KEY_SHARED_PREF_CURRENT_CATALOG), filterList.get(position) + "+" + "desc"));

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
    protected void makeApiCall(String url) {

        progressBar.setVisibility(View.VISIBLE);
        Log.d(TAG, "url: " + url);
        BookViewModel model = ViewModelProviders.of(getActivity()).get(BookViewModel.class);
        model.loadData(Volley.newRequestQueue(getActivity()), url).observe(this, updatedAudioBookList -> {
            // update UI
            if (updatedAudioBookList != null) {
                progressBar.setVisibility(View.GONE);
                /*
                hide felting option so filter can't apply on search reasult
                 */
                if (url.startsWith("https://archive.org/advancedsearch.php?q=title"))
                {
                    mySpinner.setVisibility(View.GONE);
                    mySpinnerFilter.setVisibility(View.GONE);
                }else {
                    mySpinner.setVisibility(View.VISIBLE);
                    mySpinnerFilter.setVisibility(View.VISIBLE);
                }
                if (updatedAudioBookList.size() == 0) {
                    Toast.makeText(getActivity(), "No results matched your criteria.", Toast.LENGTH_LONG).show();
                } else {
                    CustomAdapter mAdapter = new CustomAdapter(updatedAudioBookList);
                    recyclerView.setAdapter(mAdapter);
                }

            } else {
                progressBar.setVisibility(View.GONE);
                NetWorkErrorFragment(url);



            }
        });
    }
    private void startPlayerService(String Action, long miliSecond) {


        Intent startIntent = new Intent(getActivity(), PlayerForegroundService.class);

        startIntent.putExtra(PlayerForegroundService.KEY_PREFF_CURRENT_TRACK_INDEX, miliSecond);
        if (Action != null)
            startIntent.setAction(Action);

        if (Build.VERSION.SDK_INT > 25) {
            BookFragment.this.getActivity().startForegroundService(startIntent);
        } else
            getActivity().startService(startIntent);

    }

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    public void NetWorkErrorFragment(String url) {
        Log.d(TAG, "trying to ruesume the track ");
        try {
            final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                    .setTitle("Network Error")
                    .setPositiveButton("Go offline", (dialog, which) -> {
                        OfflineBookFragment offlineBookFragment = new OfflineBookFragment();
                     startFragmentTransction(offlineBookFragment);
                     dialog.dismiss();
                    })
                    .setNegativeButton("Try Again", (dialog, which) -> {
                        makeApiCall(url);
                        dialog.dismiss();

                    })
                    .create();
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void startFragmentTransction(Fragment fragment) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        if (fm.findFragmentByTag(fragment.getClass().getSimpleName()) != null) {
            fragmentTransaction.show(fm.findFragmentByTag(fragment.getClass().getSimpleName())).commit();
            Log.d(TAG,"fragment already in backstack");
        } else {
            fragmentTransaction.replace(R.id.fragment_contaner, fragment);
            fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());

            fragmentTransaction.commit();
        }

    }

}
