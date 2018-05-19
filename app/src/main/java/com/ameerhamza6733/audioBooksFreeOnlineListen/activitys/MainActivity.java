package com.ameerhamza6733.audioBooksFreeOnlineListen.activitys;

import android.content.Intent;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.ameerhamza6733.audioBooksFreeOnlineListen.R;
import com.ameerhamza6733.audioBooksFreeOnlineListen.fragment.BookMarkFragemnt;
import com.ameerhamza6733.audioBooksFreeOnlineListen.fragment.BookSearchDialogFragment;
import com.ameerhamza6733.audioBooksFreeOnlineListen.fragment.FragmetnHistory;
import com.ameerhamza6733.audioBooksFreeOnlineListen.fragment.RecyclerViewFragment;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BookSearchDialogFragment.QueryUpdate {
    public static String TAG = "MainActivityTAG";
    private RecyclerViewFragment recyclerViewFragment;
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

        recyclerViewFragment = new RecyclerViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(RecyclerViewFragment.ARRGS_KEY, RecyclerViewFragment.MAKE_API_CALL);
        recyclerViewFragment.setArguments(bundle);
        startFragmentTransction(recyclerViewFragment);
        reciveQuery = recyclerViewFragment;


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
                               startActivity(new Intent(MainActivity.this, OfflineSavedBooksActivity.class));
                                break;
                            case R.id.bn_my_history:
                              Fragment fragmetnHistory = new FragmetnHistory();
                              startFragmentTransction(fragmetnHistory);
                                break;
                            case R.id.bn_bookmark:
                                Fragment bookMarkFragemnt = new BookMarkFragemnt();
                                startFragmentTransction(bookMarkFragemnt);
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
fm= getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_contaner, fragment);
        fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(fm.getBackStackEntryCount()>1){
              fm.popBackStack();
          }else {
              super.onBackPressed();
          }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_rate_me) {
//            Uri uri = Uri.parse("market://details?id=" + this.getPackageName());
//            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
//            // To count with Play market backstack, After pressing back button,
//            // to taken back to our application, we need to add following flags to intent.
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
//                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
//                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
//            }else {
//                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
//                        Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
//                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
//            }
//            try {
//                startActivity(goToMarket);
//            } catch (ActivityNotFoundException e) {
//                startActivity(new Intent(Intent.ACTION_VIEW,
//                        Uri.parse("http://play.google.com/store/apps/details?id=" + this.getPackageName())));
//            }
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_send_feedback) {
            // Handle the camera action
            startActivity(new Intent(this, feedbackActivity.class));
        }
//        } else if (id == R.id.poetry) {
//            reciveQuery.OnRecivedQuery(Util.INSTANCE.getPoetry_URL());
//        } else if (id == R.id.nav_librivox) {
//            reciveQuery.OnRecivedQuery(Util.INSTANCE.getLIBRIVOX_URL());
//        } else if (id == R.id.nav_fiction) {
//            reciveQuery.OnRecivedQuery(Util.INSTANCE.getFiction_URL());
//        } else if (id == R.id.nav_literature) {
//            reciveQuery.OnRecivedQuery(Util.INSTANCE.getLiterature_URL());
//        } else if (id == R.id.nav_Community_Audio) {
//            reciveQuery.OnRecivedQuery(Util.INSTANCE.getCommunity_Audio_URL());
//        } else if (id == R.id.nav_philosophy) {
//            reciveQuery.OnRecivedQuery(Util.INSTANCE.getPhilosophy_URL());
//        } else if (id == R.id.nav_children) {
//            reciveQuery.OnRecivedQuery(Util.INSTANCE.getChildren_URL());
//        } else if (id == R.id.nav_plato) {
//            reciveQuery.OnRecivedQuery(Util.INSTANCE.getPlato_URL());
//        } else if (id == R.id.nav_romance) {
//            reciveQuery.OnRecivedQuery(Util.INSTANCE.getRomance_URL());
//        } else if (id == R.id.nav_history) {
//            reciveQuery.OnRecivedQuery(Util.INSTANCE.getHistory_URL());
//        } else if (id == R.id.nav_nature) {
//            reciveQuery.OnRecivedQuery(Util.INSTANCE.getNature_URL());
//        } else if (id == R.id.nav_humor) {
//            reciveQuery.OnRecivedQuery(Util.INSTANCE.getHumor_URL());
//        } else if (id == R.id.nav_mystery) {
//            reciveQuery.OnRecivedQuery(Util.INSTANCE.getMystery_URL());
//        } else if (id == R.id.nav_poem) {
//            reciveQuery.OnRecivedQuery(Util.INSTANCE.getPoem_URL());
//        } else if (id == R.id.nav_animals) {
//            reciveQuery.OnRecivedQuery(Util.INSTANCE.getAnimals_URL());
//        } else if (id == R.id.nav_novel) {
//            reciveQuery.OnRecivedQuery(Util.INSTANCE.getNovel_URL());
//        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void OnQueryUpdate(String query) {
        Log.d(TAG, "query: " + query);
        reciveQuery.OnRecivedQuery(query);
    }


    public interface ReciveQuery {
        public void OnRecivedQuery(String query);
    }
}
