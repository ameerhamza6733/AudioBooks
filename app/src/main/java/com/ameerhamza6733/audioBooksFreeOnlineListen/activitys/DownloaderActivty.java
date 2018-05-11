package com.ameerhamza6733.audioBooksFreeOnlineListen.activitys;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.ameerhamza6733.audioBooksFreeOnlineListen.R;
import com.ameerhamza6733.audioBooksFreeOnlineListen.adupters.DownloadingAdupter;
import com.ameerhamza6733.audioBooksFreeOnlineListen.models.MataData;

import java.util.List;

import cn.woblog.android.downloader.DownloadService;
import cn.woblog.android.downloader.callback.DownloadManager;
import lolodev.permissionswrapper.callback.OnRequestPermissionsCallBack;
import lolodev.permissionswrapper.wrapper.PermissionWrapper;


public class DownloaderActivty extends AppCompatActivity {
    public static final String EXTRA_MATA_DATA_LIST = "EXTRA_MATA_DATA_LIST";
    public static DownloadManager downloadManager;
    List<MataData> mataData;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloader_activty);
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
}
