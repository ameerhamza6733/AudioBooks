package com.ameerhamza6733.businessaudiobook.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ameerhamza6733.businessaudiobook.BookSearchDialogFragment;
import com.ameerhamza6733.businessaudiobook.Util;
import com.ameerhamza6733.businessaudiobook.activitys.MainActivity;
import com.ameerhamza6733.businessaudiobook.adupters.CustomAdapter;
import com.ameerhamza6733.businessaudiobook.R;
import com.ameerhamza6733.businessaudiobook.viewModels.MainActivityViewModel;
import com.android.volley.toolbox.Volley;

/**
 * Created by AmeerHamza on 2/8/2018.
 */

public class RecyclerViewFragment extends Fragment  implements MainActivity.onReciveQuery  {

    private static final String TAG = "RecyclerViewFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private CustomAdapter mAdapterDahzil;

    @Override
    public void onQueryRecived(String query) {
        Log.d(TAG,"query: "+query);
        mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
        mRecyclerViewPoetry.removeAllViews();
        initDatasetForPoetry(Util.INSTANCE.quraryBuilder(query));

    }


    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected LayoutManagerType mCurrentLayoutManagerType;


    protected RecyclerView mRecyclerViewPoetry;
    protected RecyclerView recyclerViewDahzil;


    protected RecyclerView.LayoutManager mLayoutManager;
    protected ProgressBar progressBar;
    protected FloatingActionButton floatingActionButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recycler_view_frag, container, false);
        rootView.setTag(TAG);

        mRecyclerViewPoetry = (RecyclerView) rootView.findViewById(R.id.recyclerView_poetry);
        recyclerViewDahzil = (RecyclerView) rootView.findViewById(R.id.recyclerView_dahszil);


        floatingActionButton= rootView.findViewById(R.id.fab);
        progressBar = rootView.findViewById(R.id.progressBar);

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);

        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
        floatingActionButton.setOnClickListener(v -> {
            DialogFragment bookSearchDialogFragment=BookSearchDialogFragment.newInstance();
            bookSearchDialogFragment.show(getFragmentManager(),"bookSearchDialogFragment");
        });
        initDatasetForPoetry(Util.INSTANCE.getCONN_URL());


        return rootView;
    }

    /**
     * Set RecyclerView's LayoutManager to the one given.
     *
     * @param layoutManagerType Type of layout manager to switch to.
     */
    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;
        int scrollPostionDahzial=0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerViewPoetry.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerViewPoetry.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();

        }if (recyclerViewDahzil.getLayoutManager() != null) {
            scrollPostionDahzial = ((LinearLayoutManager) recyclerViewDahzil.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();

        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(getActivity(), 2);
                mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        mRecyclerViewPoetry.setLayoutManager(mLayoutManager);
        mRecyclerViewPoetry.scrollToPosition(scrollPosition);
      recyclerViewDahzil.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));


    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        // Save currently selected layout manager.
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.action_search:
                Log.d(TAG,"search iteam clicked");
                DialogFragment bookSearchDialogFragment=BookSearchDialogFragment.newInstance();
                bookSearchDialogFragment.show(getFragmentManager(),"bookSearchDialogFragment");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }





    /**
     * Generates Strings for RecyclerView's adapter. This data would usually come
     * from a local content provider or remote server.
     */
    private void initDatasetForPoetry(String url) {
        progressBar.setVisibility(View.VISIBLE);

        MainActivityViewModel model = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        model.loadData(Volley.newRequestQueue(getActivity()), url).observe(this, updatedAudioBookList -> {
            // update UI
            if (updatedAudioBookList != null) {
                Log.d(TAG,"initDatasetForPoetry");
                progressBar.setVisibility(View.GONE);
               CustomAdapter mAdapter = new CustomAdapter(updatedAudioBookList);
                mRecyclerViewPoetry.setAdapter(mAdapter);
                initDatasetForDahzil(Util.INSTANCE.getDahszil_URL());
            } else {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Try again: ", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void initDatasetForDahzil (String url) {
        progressBar.setVisibility(View.VISIBLE);

        MainActivityViewModel model = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        model.loadData(Volley.newRequestQueue(getActivity()), url).observe(this, updatedAudioBookList -> {
            // update UI
            if (updatedAudioBookList != null) {
                Log.d(TAG,"initDatasetForDahzil");
                progressBar.setVisibility(View.GONE);
                mAdapterDahzil = new CustomAdapter(updatedAudioBookList);
                recyclerViewDahzil.setAdapter(mAdapterDahzil);

            } else {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Try again: ", Toast.LENGTH_LONG).show();
            }
        });
    }


}