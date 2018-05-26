package com.ameerhamza6733.audioBooksFreeOnlineListen.activitys;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.ameerhamza6733.audioBooksFreeOnlineListen.R;
import com.ameerhamza6733.audioBooksFreeOnlineListen.adupters.DownloadingAdupter;
import com.ameerhamza6733.audioBooksFreeOnlineListen.models.AudioBook;
import com.ameerhamza6733.audioBooksFreeOnlineListen.models.MataData;
import com.ameerhamza6733.audioBooksFreeOnlineListen.viewModels.OfflineBooksViewModle;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

import cn.woblog.android.downloader.DownloadService;
import cn.woblog.android.downloader.callback.DownloadManager;
import lolodev.permissionswrapper.callback.OnRequestPermissionsCallBack;
import lolodev.permissionswrapper.wrapper.PermissionWrapper;

import static com.ameerhamza6733.audioBooksFreeOnlineListen.MySharedPref.SHARD_PREF_DOWNLOADED_AUDIO_BOOK;


public class DownloaderActivty extends AppCompatActivity {
    public static final String EXTRA_MATA_DATA_LIST = "EXTRA_MATA_DATA_LIST";
    public static final String EXTRA_BOOK_INDEX = "EXTRA_BOOK_INDEX";
    public static DownloadManager downloadManager;
    List<MataData> mataData;
    private RecyclerView recyclerView;
    private int bookNumber;
    private List<MataData> mataDataList = new ArrayList<>();
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloader_activty);
        bookNumber = getIntent().getIntExtra(EXTRA_BOOK_INDEX, 0);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("B94C1B8999D3B59117198A259685D4F8").build();
        mAdView.loadAd(adRequest);

        if (Build.VERSION.SDK_INT > 22) {
            checkRunTimePermission();
        } else
            setRecylerView();


    }


    private void checkRunTimePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            new PermissionWrapper.Builder(this)
                    .addPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
                    //enable rationale message with a custom message

                    //show settings dialog,in this case with default message base on requested permission/s
                    .addPermissionsGoSettings(true)
                    //enable callback to know what option was choosen
                    .addRequestPermissionsCallBack(new OnRequestPermissionsCallBack() {
                        @Override
                        public void onGrant() {
                            setRecylerView();
                        }

                        @Override
                        public void onDenied(String permission) {
                            Toast.makeText(DownloaderActivty.this, "app need read and write storage permission for downloading", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }).build().request();
        } else {
            setRecylerView();
        }

    }

    private void setRecylerView() {
        downloadManager = DownloadService.getDownloadManager(this.getApplicationContext());
        mataData = getIntent().getParcelableArrayListExtra(EXTRA_MATA_DATA_LIST);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DownloadingAdupter downloadingAdupter = new DownloadingAdupter(mataData, this);
        recyclerView.setAdapter(downloadingAdupter);
    }

    public void intiDataSet() {
        OfflineBooksViewModle offlineBooksViewModle = ViewModelProviders.of(this).get(OfflineBooksViewModle.class);
        offlineBooksViewModle.getAllSavedAudioBooks(this.getApplicationContext().getSharedPreferences(SHARD_PREF_DOWNLOADED_AUDIO_BOOK, 0)).observe(this, new Observer<List<AudioBook>>() {
            @Override
            public void onChanged(@Nullable List<AudioBook> audioBooks) {
                if (audioBooks != null && audioBooks.size() > 0) {
                    for (MataData mataData : audioBooks.get(bookNumber).getMataData())
                        if (mataData.isHasDownloaded())

                        {
                            mataDataList.add(mataData);
                            setRecylerView();
                        }


                } else {
                    setRecylerView();
                }
            }
        });
    }
}
