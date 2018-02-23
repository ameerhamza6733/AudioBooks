package com.ameerhamza6733.businessaudiobook.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ameerhamza6733.businessaudiobook.R;
import com.ameerhamza6733.businessaudiobook.Util;
import com.ameerhamza6733.businessaudiobook.fragment.BookSearchDialogFragment;
import com.ameerhamza6733.businessaudiobook.fragment.RecyclerViewFragment;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BookSearchDialogFragment.onSearchQuerySubmit {
    public static String TAG = "MainActivityTAG";
    private RecyclerViewFragment recyclerViewFragment;
    private onReciveQuery reciveQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        recyclerViewFragment = new RecyclerViewFragment();
        fragmentTransaction.replace(R.id.fragment_contaner, recyclerViewFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        reciveQuery = recyclerViewFragment;


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_send_feedback) {
            // Handle the camera action
            startActivity(new Intent(this, feedbackActivity.class));
        } else if (id == R.id.poetry) {
            reciveQuery.onNewBookCatugury(Util.INSTANCE.getPoetry_URL());
        } else if (id == R.id.nav_librivox) {
            reciveQuery.onNewBookCatugury(Util.INSTANCE.getLIBRIVOX_URL());
        } else if (id == R.id.nav_fiction) {
            reciveQuery.onNewBookCatugury(Util.INSTANCE.getFiction_URL());
        }else if (id == R.id.nav_literature){
            reciveQuery.onNewBookCatugury(Util.INSTANCE.getLiterature_URL());
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onQuerySubmitButtonClick(String query) {
        Log.d(TAG, "query: " + query);
        reciveQuery.onNewBookCatugury(Util.INSTANCE.quraryBuilder(query));
    }

    public interface onReciveQuery {
        public void onNewBookCatugury(String query);
    }
}
