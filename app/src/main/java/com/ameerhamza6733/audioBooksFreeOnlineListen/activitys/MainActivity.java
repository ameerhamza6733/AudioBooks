package com.ameerhamza6733.audioBooksFreeOnlineListen.activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.ameerhamza6733.audioBooksFreeOnlineListen.R;
import com.ameerhamza6733.audioBooksFreeOnlineListen.fragment.BookFragment;
import com.ameerhamza6733.audioBooksFreeOnlineListen.fragment.BookMarkFragemnt;
import com.ameerhamza6733.audioBooksFreeOnlineListen.fragment.BookSearchDialogFragment;
import com.ameerhamza6733.audioBooksFreeOnlineListen.fragment.HistoryFragment;
import com.ameerhamza6733.audioBooksFreeOnlineListen.fragment.OfflineSavedBookFragment;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BookSearchDialogFragment.QueryUpdate {
    public static String TAG = "MainActivityTAG";
    private BookFragment bookFragment;
    private ReciveQuery reciveQuery;
    private BottomNavigationView mBottomBar;
    private FragmentManager fm;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().hide();
        } catch (Exception e) {
        }

        bookFragment = new BookFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BookFragment.ARRGS_KEY, BookFragment.WelcomeCall);
        bookFragment.setArguments(bundle);
        startFragmentTransction(bookFragment);
        reciveQuery = bookFragment;


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mBottomBar = findViewById(R.id.bottom_navigation);

        mBottomBar.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.bn_watch_latter:
                           //     startActivity(new Intent(MainActivity.this, OfflineSavedBooksActivity.class));
                                OfflineSavedBookFragment offlineSavedBookFragment = new OfflineSavedBookFragment();
                                startFragmentTransction(offlineSavedBookFragment);

                                break;
                            case R.id.bn_my_history:

                                Fragment fragment =  new HistoryFragment();
                                startFragmentTransction(fragment);
                                break;
                            case R.id.bn_bookmark:
                                Fragment bookMarkFragemnt = new BookMarkFragemnt();
                                startFragmentTransction(bookMarkFragemnt);
                                break;
                            case R.id.bn_home:
                                bookFragment = new BookFragment();
                                Bundle bundle = new Bundle();
                                bundle.putString(BookFragment.ARRGS_KEY, BookFragment.WelcomeCall);
                                bookFragment.setArguments(bundle);
                                startFragmentTransction(bookFragment);
                                reciveQuery = bookFragment;
                                break;

                        }

                        return true;
                    }

                    ;
                });

        MobileAds.initialize(this, "ca-app-pub-5168564707064012~4212395459");
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    private void startFragmentTransction(Fragment fragment) {
        fm = getSupportFragmentManager();
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
           RateMe();
         //  super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_send_feedback) {
            // Handle the camera action
            startActivity(new Intent(this, feedbackActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void OnQueryUpdate(String query) {
        Log.d(TAG, "query: " + query);
        reciveQuery.OnRecivedQuery(query);
    }
    private void RateMe() {
        AlertDialog.Builder builder;

        builder = new AlertDialog.Builder(this);
        builder.setTitle("Rate Me")
                .setMessage("Audio book download need your help please rate us on google play")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + MainActivity.this.getPackageName())));
                    }
                })
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.this.finish();
                    }
                })
                .setIcon(android.R.drawable.star_big_on)
                .show();
    }

    public interface ReciveQuery {
        public void OnRecivedQuery(String query);
    }
}
