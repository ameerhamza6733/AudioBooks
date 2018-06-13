package com.ameerhamza6733.audioBooksFreeOnlineListen.adupters;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ameerhamza6733.audioBooksFreeOnlineListen.MySharedPref;
import com.ameerhamza6733.audioBooksFreeOnlineListen.R;
import com.ameerhamza6733.audioBooksFreeOnlineListen.Util;
import com.ameerhamza6733.audioBooksFreeOnlineListen.activitys.DownloaderActivty;
import com.ameerhamza6733.audioBooksFreeOnlineListen.mediaPlayer.PlayerForegroundService;
import com.ameerhamza6733.audioBooksFreeOnlineListen.models.AudioBook;
import com.ameerhamza6733.audioBooksFreeOnlineListen.models.MataData;
import com.daimajia.numberprogressbar.NumberProgressBar;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import cn.woblog.android.downloader.callback.DownloadListener;
import cn.woblog.android.downloader.domain.DownloadInfo;
import cn.woblog.android.downloader.exception.DownloadException;

import static com.ameerhamza6733.audioBooksFreeOnlineListen.activitys.DetailTabActivity.KEY_SHARD_PREF_AUDIO_BOOK;
import static com.ameerhamza6733.audioBooksFreeOnlineListen.mediaPlayer.PlayerForegroundService.EXTRA_URI;


/**
 * Created by AmeerHamza on 5/9/18.
 */

public class DownloadingAdupter extends RecyclerView.Adapter<DownloadingAdupter.ViewHolder> {
    private static final String TAG = "DownloadingAdupter";
    protected Activity activity;
    private AudioBook downloadedAudioBook=null;
    private AudioBook CurrentAudioBook=null;
    private List<MataData> mataDataList;
    private HashMap<Integer, DownloadInfo> downloadInfoHashMap = new HashMap<>();
    Handler handler=new Handler();
    private File f;

    public DownloadingAdupter(List<MataData> mataDataList, Activity context) {
        this.mataDataList = mataDataList;
        CurrentAudioBook = MySharedPref.getSavedObjectFromPreference(context.getApplicationContext(), MySharedPref.SHARD_PREF_AUDIO_BOOK_FILE_NAME, KEY_SHARD_PREF_AUDIO_BOOK, AudioBook.class);

        if (CurrentAudioBook !=null) {
            CurrentAudioBook.setMataData(mataDataList);
            downloadedAudioBook = MySharedPref.getSavedObjectFromPreference(context.getApplicationContext(), MySharedPref.SHARD_PREF_DOWNLOADED_AUDIO_BOOK, CurrentAudioBook.getIdentifier(), AudioBook.class);

        }

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
        if (isAlreadyDonlaod(position)){
            holder.getmRootDownloadButton().setVisibility(View.GONE);
            holder.getBtViewDownload().setVisibility(View.VISIBLE);
           try{
               Log.d(TAG,"sd card path"+downloadedAudioBook.getMataData().get(position).getSdPath());
           }catch (Exception e){
               e.printStackTrace();
           }
        }
        holder.getTitle().setText(mataDataList.get(position).getName() + " (" + Util.INSTANCE.bytesToMb(mataDataList.get(position).getSize()) + ")");
        holder.getBtDownload().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CurrentAudioBook!=null){
                    final DownloadInfo downloadInfo = new DownloadInfo
                            .Builder()
                            .setUrl(mataDataList.get(position).getURL())
                            .setPath(Util.INSTANCE.getDir("/sdcard/CurrentAudioBook/") + mataDataList.get(position).getName())
                            .build();

                    downloadInfo.setDownloadListener(holder.getDownloadListener());
                    DownloaderActivty.downloadManager.download(downloadInfo);
                    downloadInfoHashMap.put(position, downloadInfo);
                }else {
                    Toast.makeText(v.getContext(),"Some thing wrong please restart app and try again",Toast.LENGTH_LONG).show();
                }

            }
        });
        holder.getBtDownloadingCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloaderActivty.downloadManager.pause(downloadInfoHashMap.get(position));
                DownloaderActivty.downloadManager.remove(downloadInfoHashMap.get(position));
                holder.getNumberProgressBar().setProgress(0);
                if (holder.getEndlessPrograssBar().isActivated())
                    holder.getEndlessPrograssBar().setVisibility(View.GONE);

            }
        });

        holder.getBtViewDownload().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"starting media player ",Toast.LENGTH_SHORT).show();
                startPlayerService(mataDataList.get(position), PlayerForegroundService.STOP_ACTION, null, 0);
                handler.postDelayed(() -> {
                    startPlayerService(mataDataList.get(position), PlayerForegroundService.ACTION_START, null, 0);

                }, 2000);
            }
        });
    }

    private boolean isAlreadyDonlaod(int position) {

       try{
            f = new File( downloadedAudioBook.getMataData().get(position).getSdPath());
           if(f.exists()) {
               return   downloadedAudioBook!=null && downloadedAudioBook.getMataData()!=null && downloadedAudioBook.getMataData().size()>0 && downloadedAudioBook.getMataData().get(position).isHasDownloaded();
           }else {
               return false;
           }
       }catch (NullPointerException e) {
           e.printStackTrace();
           return false;
       }

    }

    @Override
    public int getItemCount() {
        return mataDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final NumberProgressBar numberProgressBar;
        private final View rootView;
        private final ProgressBar endlessPrograssBar;
        private final TextView btDownload;
        private final TextView btDownloadingCancel;
        private final TextView btViewDownload;
        private final LinearLayout mRootDownloadButton;

        private final DownloadListener downloadListener;

        public ViewHolder(View v) {
            super(v);
            rootView = v;
            // Define click listener for the ViewHolder's View.
            title = v.findViewById(R.id.title);
            numberProgressBar = v.findViewById(R.id.number_progress_bar);
            endlessPrograssBar = v.findViewById(R.id.progressBarEndless);
            btDownload = v.findViewById(R.id.btDownload);
            btDownloadingCancel = v.findViewById(R.id.btDownloadingCancel);
            btViewDownload=v.findViewById(R.id.btViewDownloadFile);
            mRootDownloadButton=v.findViewById(R.id.rootLayoutDownloadingButton);

            downloadListener = new cn.woblog.android.downloader.callback.DownloadListener() {
                @Override
                public void onStart() {
                    getEndlessPrograssBar().setVisibility(View.VISIBLE);
                }

                @Override
                public void onWaited() {

                }

                @Override
                public void onPaused() {

                }

                @Override
                public void onDownloading(long progress, long size) {
                    try {
                        getEndlessPrograssBar().setVisibility(View.GONE);
                        getNumberProgressBar().setVisibility(View.VISIBLE);
                        getNumberProgressBar().setProgress((int) (progress * 100.0 / size));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onRemoved() {
                    getNumberProgressBar().setProgress(0);
                }

                @Override
                public void onDownloadSuccess() {
                    Log.d(TAG, "onDownloadSuccess");

                        if (downloadedAudioBook == null)
                            downloadedAudioBook = CurrentAudioBook;
                        getNumberProgressBar().setProgress(100);
                        List<MataData> DownloadedMataDataList = downloadedAudioBook.getMataData();
                        DownloadedMataDataList.get(getAdapterPosition()).setHasDownloaded(true);
                        DownloadedMataDataList.get(getAdapterPosition()).setSdPath(downloadInfoHashMap.get(getAdapterPosition()).getPath());
                        MySharedPref.saveObjectToSharedPreference(DownloadingAdupter.this.activity.getApplicationContext(), MySharedPref.SHARD_PREF_DOWNLOADED_AUDIO_BOOK, downloadedAudioBook.getIdentifier(), downloadedAudioBook);
                        notifyDataSetChanged();

                }

                @Override
                public void onDownloadFailed(DownloadException e) {
                    e.printStackTrace();
                    //update the shared pref
                    getNumberProgressBar().setProgress(0);
                    Toast.makeText(v.getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();

                }
            };


        }

        public TextView getTitle() {
            return title;
        }

        public NumberProgressBar getNumberProgressBar() {
            return numberProgressBar;
        }


        public DownloadListener getDownloadListener() {
            return downloadListener;
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

        public LinearLayout getmRootDownloadButton() {
            return mRootDownloadButton;
        }

        public TextView getBtViewDownload() {
            return btViewDownload;
        }
    }

    private void startPlayerService(MataData mataData, String Action, String extraKey, long miliSecond)   {

        try {

            Intent startIntent = new Intent(activity, PlayerForegroundService.class);
            if (mataData != null) {
                startIntent.putExtra(EXTRA_URI, mataData.getSdPath());
                startIntent.putExtra(PlayerForegroundService.EXTRA_TITLE, mataData.getName());
            }
            startIntent.putExtra(extraKey, miliSecond);
            startIntent.setAction(Action);
            activity.startService(startIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
