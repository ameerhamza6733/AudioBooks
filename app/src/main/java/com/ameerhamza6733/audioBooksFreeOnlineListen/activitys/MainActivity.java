package com.ameerhamza6733.audioBooksFreeOnlineListen.activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.ameerhamza6733.audioBooksFreeOnlineListen.R;
import com.ameerhamza6733.audioBooksFreeOnlineListen.fragment.BookFragment;
import com.ameerhamza6733.audioBooksFreeOnlineListen.fragment.BookMarkFragemnt;
import com.ameerhamza6733.audioBooksFreeOnlineListen.fragment.BookSearchDialogFragment;
import com.ameerhamza6733.audioBooksFreeOnlineListen.fragment.HistoryFragment;
import com.ameerhamza6733.audioBooksFreeOnlineListen.fragment.OfflineBookFragment;
import com.google.ads.consent.ConsentForm;
import com.google.ads.consent.ConsentFormListener;
import com.google.ads.consent.ConsentInfoUpdateListener;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BookSearchDialogFragment.QueryUpdate {
    public static String TAG = "MainActivityTAG";
    private BookFragment bookFragment;
    private ReciveQuery reciveQuery;
    private BottomNavigationView mBottomBar;
    private FragmentManager fm;
    private FirebaseAnalytics mFirebaseAnalytics;
    private ConsentForm form;

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
                                OfflineBookFragment offlineBookFragment = new OfflineBookFragment();
                                startFragmentTransction(offlineBookFragment);

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

        List<String> testDeviceIds = Arrays.asList("246E5D30FD3684C5C7E7B8BE020B95A3");
        RequestConfiguration configuration = new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
       // MobileAds.initialize(this, "ca-app-pub-5168564707064012~4212395459");
        MobileAds.initialize(this);
        MobileAds.setRequestConfiguration(configuration);
        MobileAds.setAppMuted(true);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        checkForConsent();
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
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

    //
    private void checkForConsent() {
        ConsentInformation consentInformation = ConsentInformation.getInstance(this);
        consentInformation.addTestDevice("246E5D30FD3684C5C7E7B8BE020B95A3");
        // ConsentInformation.getInstance(getActivity()).addTestDevice("B94C1B8999D3B59117198A259685D4F8");

        String[] publisherIds = {"pub-5168564707064012"};
        consentInformation.requestConsentInfoUpdate(publisherIds, new ConsentInfoUpdateListener() {
            @Override
            public void onConsentInfoUpdated(ConsentStatus consentStatus) {
                // User's consent status successfully updated.
                switch (consentStatus) {
                    case PERSONALIZED:

                        showPersonalizedAds();
                        break;
                    case NON_PERSONALIZED:

                        showNonPersonalizedAds();
                        break;
                    case UNKNOWN:

                        if (ConsentInformation.getInstance(MainActivity.this)
                                .isRequestLocationInEeaOrUnknown()) {
                            requestConsent();
                        } else {
                            showPersonalizedAds();
                        }
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailedToUpdateConsentInfo(String errorDescription) {
                // User's consent status failed to update.
            }
        });
    }

    //======================================

    private void requestConsent() {
        URL privacyUrl = null;
        try {
            // TODO: Replace with your app's privacy policy URL.
            privacyUrl = new URL("http://alphapk6733.blogspot.com/2018/05/privacy-policy-for-audio-book.html");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            // Handle error.
        }
        form = new ConsentForm.Builder(this, privacyUrl)
                .withListener(new ConsentFormListener() {
                    @Override
                    public void onConsentFormLoaded() {
                        // Consent form loaded successfully.

                        showForm();
                    }

                    @Override
                    public void onConsentFormOpened() {
                        // Consent form was displayed.
                        Log.d(TAG, "Requesting Consent: onConsentFormOpened");
                    }

                    @Override
                    public void onConsentFormClosed(
                            ConsentStatus consentStatus, Boolean userPrefersAdFree) {
                        Log.d(TAG, "Requesting Consent: onConsentFormClosed");
                        if (userPrefersAdFree) {
                            // Buy or Subscribe
                            Log.d(TAG, "Requesting Consent: User prefers AdFree");
                        } else {
                            Log.d(TAG, "Requesting Consent: Requesting consent again");
                            switch (consentStatus) {
                                case PERSONALIZED:
                                    showPersonalizedAds();
                                    break;
                                case NON_PERSONALIZED:
                                    showNonPersonalizedAds();
                                    break;
                                case UNKNOWN:
                                    showNonPersonalizedAds();
                                    break;
                            }

                        }
                        // Consent form was closed.
                    }

                    @Override
                    public void onConsentFormError(String errorDescription) {
                        Log.d(TAG, "Requesting Consent: onConsentFormError. Error - " + errorDescription);
                        // Consent form error.
                    }
                })
                .withPersonalizedAdsOption()
                .withNonPersonalizedAdsOption()
                .withAdFreeOption()
                .build();
        form.load();
    }

    private void showPersonalizedAds() {
        ConsentInformation.getInstance(this).setConsentStatus(ConsentStatus.PERSONALIZED);

    }



    private void showNonPersonalizedAds() {
        ConsentInformation.getInstance(this).setConsentStatus(ConsentStatus.NON_PERSONALIZED);

    }


    private void showForm() {
        if (form == null) {
            Log.d(TAG, "Consent form is null");
        }
        if (form != null) {
            Log.d(TAG, "Showing consent form");
            form.show();
        } else {
            Log.d(TAG, "Not Showing consent form");
        }
    }
}
