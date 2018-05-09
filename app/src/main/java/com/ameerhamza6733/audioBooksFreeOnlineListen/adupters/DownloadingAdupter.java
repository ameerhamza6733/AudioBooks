package com.ameerhamza6733.audioBooksFreeOnlineListen.adupters;

import android.app.Activity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ameerhamza6733.audioBooksFreeOnlineListen.MySharedPref;
import com.ameerhamza6733.audioBooksFreeOnlineListen.R;
import com.ameerhamza6733.audioBooksFreeOnlineListen.models.AudioBook;
import com.ameerhamza6733.audioBooksFreeOnlineListen.models.MataData;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.golshadi.majid.report.listener.DownloadManagerListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static com.ameerhamza6733.audioBooksFreeOnlineListen.activitys.DetailTabActivity.KEY_SHARD_PREF_AUDIO_BOOK;
import static com.ameerhamza6733.audioBooksFreeOnlineListen.activitys.DownloaderActivty.dm;


/**
 * Created by apple on 5/9/18.
 */

public class DownloadingAdupter extends RecyclerView.Adapter<DownloadingAdupter.ViewHolder> {
    private static final String TAG = "DownloadingAdupter";
    private   AudioBook audioBook;
    protected Activity activity;
    private List<MataData> mataDataList;
    private HashMap<Integer, Integer> downloadingTokensMap;

    public DownloadingAdupter(List<MataData> mataDataList, Activity context) {
        this.mataDataList = mataDataList;
        downloadingTokensMap = new HashMap<>();
        audioBook = MySharedPref.getSavedObjectFromPreference(context.getApplicationContext(), MySharedPref.SHARD_PREF_AUDIO_BOOK_FILE_NAME, KEY_SHARD_PREF_AUDIO_BOOK, AudioBook.class);
        this.activity = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.downloading_recylerview, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(TAG, "onBind");
        holder.getTitle().setText(mataDataList.get(position).getName());
        holder.getBtDownload().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dm.init("audioBooks/", 12, holder.getDownloadManagerListener());
                int taskToken = dm.
                        addTask(mataDataList.get(position).getName().replace(".mp3", ""),
                                mataDataList.get(position).getURL(), false, false);
                downloadingTokensMap.put(position, taskToken);
                holder.getEndlessPrograssBar().setVisibility(View.VISIBLE);
                try {
                    dm.startDownload(taskToken);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        holder.getBtDownloadingCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dm.pauseDownload(downloadingTokensMap.get(position));
                dm.delete(downloadingTokensMap.get(position),true);
                holder.getNumberProgressBar().setProgress(0);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mataDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final NumberProgressBar numberProgressBar;
        private final DownloadManagerListener downloadManagerListener;
        private final View rootView;
        private final ProgressBar endlessPrograssBar;
        private final TextView btDownload;
        private final TextView btDownloadingCancel;

        public ViewHolder(View v) {
            super(v);
            rootView = v;
            // Define click listener for the ViewHolder's View.
            title = v.findViewById(R.id.title);
            numberProgressBar = v.findViewById(R.id.number_progress_bar);
            endlessPrograssBar = v.findViewById(R.id.progressBarEndless);
            btDownload=v.findViewById(R.id.btDownload);
            btDownloadingCancel=v.findViewById(R.id.btDownloadingCancel);
            downloadManagerListener = new DownloadManagerListener() {
                @Override
                public void OnDownloadStarted(long taskId) {
                    Log.d(TAG, "OnDownloadStarted");

                    DownloadingAdupter.this.activity.runOnUiThread(() -> {
                                if (numberProgressBar.getVisibility() == View.GONE) {
                                    numberProgressBar.setVisibility(View.VISIBLE);
                                    numberProgressBar.setProgress(0);
                                }
                            }
                    );
                }

                @Override
                public void OnDownloadPaused(long taskId) {

                }

                @Override
                public void onDownloadProcess(long taskId, double percent, long downloadedLength) {


                    DownloadingAdupter.this.activity.runOnUiThread(() -> {
                        if (getEndlessPrograssBar().getVisibility() == View.VISIBLE)
                            getEndlessPrograssBar().setVisibility(View.GONE);
                        numberProgressBar.incrementProgressBy(1);
                    });
                }

                @Override
                public void OnDownloadFinished(long taskId) {
                    Log.d(TAG, "OnDownloadFinished");
                }

                @Override
                public void OnDownloadRebuildStart(long taskId) {

                }

                @Override
                public void OnDownloadRebuildFinished(long taskId) {
                    Log.d(TAG, "OnDownloadRebuildFinished");
                }

                @Override
                public void onRebuildError(String errorMessage) {

                }

                @Override
                public void OnDownloadCompleted(long taskId) {
                    Log.d(TAG, "OnDownloadCompleted");
                    DownloadingAdupter.this.activity.runOnUiThread(() -> numberProgressBar.setProgress(100));
                    Log.d(TAG, "task path" + dm.singleDownloadStatus(downloadingTokensMap.get(getAdapterPosition())).saveAddress);
                    mataDataList.get(getAdapterPosition()).setHasDownloaded(true);
                    mataDataList.get(getAdapterPosition()).setSdPath(dm.singleDownloadStatus(downloadingTokensMap.get(getAdapterPosition())).saveAddress);
                    audioBook.setMataData(mataDataList);
                    MySharedPref.saveObjectToSharedPreference(DownloadingAdupter.this.activity.getApplicationContext(),MySharedPref.SHARD_PREF_DOWNLOADED_AUDIO_BOOK,audioBook.getIdentifier(),audioBook);

                }

                @Override
                public void connectionLost(long taskId) {

                }
            };


        }

        public TextView getTitle() {
            return title;
        }

        public NumberProgressBar getNumberProgressBar() {
            return numberProgressBar;
        }


        public DownloadManagerListener getDownloadManagerListener() {
            return downloadManagerListener;
        }

        public ProgressBar getEndlessPrograssBar() {
            return endlessPrograssBar;
        }

        public View getRootView() {
            return rootView;
        }

        public TextView getBtDownload() {
            return btDownload;
        }

        public TextView getBtDownloadingCancel() {
            return btDownloadingCancel;
        }
    }

}
