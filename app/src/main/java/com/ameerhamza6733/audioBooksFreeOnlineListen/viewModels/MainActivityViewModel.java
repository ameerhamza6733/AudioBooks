package com.ameerhamza6733.audioBooksFreeOnlineListen.viewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.ameerhamza6733.audioBooksFreeOnlineListen.Util;
import com.ameerhamza6733.audioBooksFreeOnlineListen.activitys.MainActivity;
import com.ameerhamza6733.audioBooksFreeOnlineListen.models.AudioBook;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by AmeerHamza on 2/8/2018.
 */

public class MainActivityViewModel extends ViewModel {

    JSONObject jsonObject;
    private MutableLiveData<List<AudioBook>> mutableLiveData;
    private List<AudioBook> audioBookList;
    private String url;

    public LiveData<List<AudioBook>> loadData(RequestQueue requestQueue, String url) {
        if (mutableLiveData == null ||  !this.url.equalsIgnoreCase(url) ) {
            mutableLiveData = new MutableLiveData<List<AudioBook>>();
            audioBookList = new ArrayList<>();
            this.url = url;
            intiVolley(requestQueue);

        }
        return mutableLiveData;
    }

    private void intiVolley(RequestQueue requestQueue) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {

                Observable.fromCallable(() -> {
                    try {
                        Log.d("RecyclerViewFragment", "url: " + url);
                        jsonObject = Util.INSTANCE.toJson(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                        JSONArray jsonArray = jsonObject.getJSONObject("response").getJSONArray("docs");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject docs = jsonArray.getJSONObject(i);
                            if (docs.getString("identifier").toLowerCase().contains("librivox")) {
                                audioBookList.add(new AudioBook(Util.INSTANCE.ExtractRating(docs), Util.INSTANCE.ExtractDescription(docs), Util.INSTANCE.ExtractNoOfDownloads(docs)
                                        , docs.getString("identifier"), Util.INSTANCE.ExtractNoReviews(docs), docs.getString("title"), Util.INSTANCE.ExtractPublisher(docs), Util.INSTANCE.ExtractMediaType(docs)));

                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                    return audioBookList;
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe((result) -> {
                            if (result != null) {

                                mutableLiveData.setValue(audioBookList);
                            }else {
                                Log.d("RecyclerViewFragment","no open source book find");
                            }
                        });

            Log.d(MainActivity.TAG, "volley response");
        }, (error) -> {
            error.printStackTrace();
            this.url="non";
            mutableLiveData.setValue(null);
            Log.d(MainActivity.TAG, "volley error" + error.getMessage());
        });
        requestQueue.add(stringRequest);

    }
}
