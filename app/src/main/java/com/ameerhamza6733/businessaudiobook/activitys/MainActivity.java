package com.ameerhamza6733.businessaudiobook.activitys;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.ameerhamza6733.businessaudiobook.BookSearchDialogFragment;
import com.ameerhamza6733.businessaudiobook.R;
import com.ameerhamza6733.businessaudiobook.fragment.RecyclerViewFragment;


public class MainActivity extends AppCompatActivity implements  BookSearchDialogFragment.onSearchQuerySubmit {
    ;
    public static String TAG = "MainActivityTAG";
    private RecyclerViewFragment recyclerViewFragment;
    private onReciveQuery reciveQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        recyclerViewFragment =  new RecyclerViewFragment();
        fragmentTransaction.replace(R.id.fragment_contaner,recyclerViewFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        reciveQuery = recyclerViewFragment;
    }

    @Override
    public void onQuerySubmitButtonClick(String query) {
        Log.d(TAG,"query: "+query);
        reciveQuery.onQueryRecived(query);
    }

    public interface onReciveQuery{
        public void onQueryRecived(String query);
    }
}






