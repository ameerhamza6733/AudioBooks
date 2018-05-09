package com.ameerhamza6733.audioBooksFreeOnlineListen.activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ameerhamza6733.audioBooksFreeOnlineListen.R;
import com.ameerhamza6733.audioBooksFreeOnlineListen.adupters.DownloadingAdupter;
import com.ameerhamza6733.audioBooksFreeOnlineListen.models.MataData;
import com.golshadi.majid.core.DownloadManagerPro;
import com.liulishuo.filedownloader.FileDownloader;

import java.util.List;

public class DownloaderActivty extends AppCompatActivity {
    public static final String EXTRA_MATA_DATA_LIST = "EXTRA_MATA_DATA_LIST";
    public static DownloadManagerPro dm;
    List<MataData> mataData;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloader_activty);
        FileDownloader.setup(this.getApplicationContext());
        mataData = getIntent().getParcelableArrayListExtra(EXTRA_MATA_DATA_LIST);
        recyclerView = findViewById(R.id.recyclerView);
        dm = new DownloadManagerPro(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DownloadingAdupter downloadingAdupter = new DownloadingAdupter(mataData,this);
        recyclerView.setAdapter(downloadingAdupter);
    }
}
