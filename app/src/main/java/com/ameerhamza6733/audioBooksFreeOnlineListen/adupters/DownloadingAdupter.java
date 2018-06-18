package com.ameerhamza6733.audioBooksFreeOnlineListen.adupters;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
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
import com.daimajia.numberprogressbar.NumberProgressBar;

import java.io.File;
import java.util.HashMap;

import cn.woblog.android.downloader.callback.DownloadListener;
import cn.woblog.android.downloader.domain.DownloadInfo;
import cn.woblog.android.downloader.exception.DownloadException;

import static com.ameerhamza6733.audioBooksFreeOnlineListen.activitys.DetailTabActivity.KEY_SHARD_PREF_AUDIO_BOOK;


/**
 * Created by AmeerHamza on 5/9/18.
 */

public class DownloadingAdupter extends RecyclerView.Adapter<DownloadingAdupter.ViewHolder> {
    private static final String TAG = "DownloadingAdupter";
    protected Activity activity;
    Handler handler = new Handler();
    private AudioBook downloadedAudioBook = null;
    private AudioBook currentAudioBook = null;
    private HashMap<Integer, DownloadInfo> downloadInfoHashMap = new HashMap<>();
    private File f;

    public DownloadingAdupter(Activity context) {

        currentAudioBook = MySharedPref.getSavedObjectFromPreference(context.getApplicationContext(), MySharedPref.SHARD_PREF_AUDIO_BOOK_FILE_NAME, KEY_SHARD_PREF_AUDIO_BOOK, AudioBook.class);

        if (currentAudioBook != null) {

            downloadedAudioBook = MySharedPref.getSavedObjectFromPreference(context.getApplicationContext(), MySharedPref.SHARD_PREF_DOWNLOADED_AUDIO_BOOK, currentAudioBook.getIdentifier(), AudioBook.class);

           if (downloadedAudioBook != null) {
                currentAudioBook = downloadedAudioBook;
            }

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
        if (currentAudioBook.getMataData().get(position).hasDownloaded()) {
            holder.getmRootDownloadButton().setVisibility(View.GONE);
            holder.getBtViewDownload().setVisibility(View.VISIBLE);
            try {
                Log.d(TAG, "sd card path" + currentAudioBook.getMataData().get(position).getSdPath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            holder.getmRootDownloadButton().setVisibility(View.VISIBLE);
            holder.getBtViewDownload().setVisibility(View.GONE);
        }
        holder.getTitle().setText(currentAudioBook.getMataData().get(position).getName() + " (" + Util.INSTANCE.bytesToMb(currentAudioBook.getMataData().get(position).getSize()) + ")");
        holder.getBtDownload().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentAudioBook != null) {
                    final DownloadInfo downloadInfo = new DownloadInfo
                            .Builder()
                            .setUrl(currentAudioBook.getMataData().get(position).getURL())
                            .setPath(Util.INSTANCE.getDir("/sdcard/currentAudioBook/") + currentAudioBook.getMataData().get(position).getName())
                            .build();

                    downloadInfo.setDownloadListener(holder.getDownloadListener());
                    DownloaderActivty.downloadManager.download(downloadInfo);
                    downloadInfoHashMap.put(position, downloadInfo);
                } else {
                    Toast.makeText(v.getContext(), "Some thing wrong please restart app and try again", Toast.LENGTH_LONG).show();
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
                Toast.makeText(v.getContext(), "starting media player ", Toast.LENGTH_SHORT).show();

                if (PlayerForegroundService.player != null) {
                    startPlayerService( PlayerForegroundService.ACTION_UPDATE_MEDIA_SOURCE, position);
                } else {
                    startPlayerService( null, position);
                }

            }
        });
    }

//    private boolean isAlreadyDonlaod(int position) {
//
//        try {
//            f = new File(downloadedAudioBook.getMataData().get(position).getSdPath());
//            if (f.exists()) {
//                return downloadedAudioBook != null && downloadedAudioBook.getMataData() != null && downloadedAudioBook.getMataData().size() > 0 && downloadedAudioBook.getMataData().get(position).hasDownloaded();
//            } else {
//                return false;
//            }
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//            return false;
//        }
//
//    }

    @Override
    public int getItemCount() {
        return currentAudioBook.getMataData().size();
    }

    private void startPlayerService( String Action, int position) {
        MySharedPref.saveObjectToSharedPreference(DownloadingAdupter.this.activity, MySharedPref.SHARD_PREF_AUDIO_BOOK_FILE_NAME, PlayerForegroundService.KEY_SHARD_PREF_AUDIO_BOOK, currentAudioBook);
        MySharedPref.saveObjectToSharedPreference(DownloadingAdupter.this.activity,MySharedPref.SHARD_PREF_AUDIO_BOOK_FILE_NAME,PlayerForegroundService.KEY_PREFF_CURRENT_TRACK_INDEX,String.valueOf(position));
        MySharedPref.saveObjectToSharedPreference(DownloadingAdupter.this.activity,MySharedPref.SHARD_PREF_AUDIO_BOOK_FILE_NAME,PlayerForegroundService.IS_SOUCE_LOCAL_DISK,"1");

        Intent startIntent = new Intent(activity, PlayerForegroundService.class);
        if (Action != null)
            startIntent.setAction(Action);

        if (Build.VERSION.SDK_INT > 25) {
            activity.startForegroundService(startIntent);
        } else
            activity.startService(startIntent);

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
            btViewDownload = v.findViewById(R.id.btViewDownloadFile);
            mRootDownloadButton = v.findViewById(R.id.rootLayoutDownloadingButton);

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


                    getNumberProgressBar().setProgress(100);
                    currentAudioBook.getMataData();
                    currentAudioBook.getMataData().get(getAdapterPosition()).setHasDownloaded(true);
                    currentAudioBook.getMataData().get(getAdapterPosition()).setSdPath(downloadInfoHashMap.get(getAdapterPosition()).getPath());
                    MySharedPref.saveObjectToSharedPreference(DownloadingAdupter.this.activity.getApplicationContext(), MySharedPref.SHARD_PREF_DOWNLOADED_AUDIO_BOOK, currentAudioBook.getIdentifier(), currentAudioBook);
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

}
