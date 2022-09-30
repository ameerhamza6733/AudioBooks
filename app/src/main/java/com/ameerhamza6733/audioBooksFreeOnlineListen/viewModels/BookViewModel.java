package com.ameerhamza6733.audioBooksFreeOnlineListen.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by AmeerHamza on 2/8/2018.
 */

public class BookViewModel extends ViewModel {
private static final String TAG = "BookViewModel";
    JSONObject jsonObject;
    private MutableLiveData<List<AudioBook>> mutableLiveData;
    private List<AudioBook> audioBookList;
  private String url;

    public BookViewModel() {
    }

    public LiveData<List<AudioBook>> loadData(RequestQueue requestQueue, String url) {
        Log.d(TAG,"load data" +url);
        mutableLiveData = new MutableLiveData<List<AudioBook>>();
        audioBookList = new ArrayList<>();
        this.url = url;
        intiVolley(requestQueue);
        return mutableLiveData;
    }

    private void intiVolley(RequestQueue requestQueue) {
        Log.d(TAG,"intiVolley");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {

                Observable.fromCallable(() -> {
                    try {

                        jsonObject = Util.INSTANCE.toJson(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                        JSONArray jsonArray = jsonObject.getJSONObject("response").getJSONArray("docs");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject docs = jsonArray.getJSONObject(i);
                         //   Log.d(TAG,Util.INSTANCE.extractPublis(docs));
                            Log.d(TAG,Util.INSTANCE.extractCollection(docs));
                            if (Util.INSTANCE.isWorkFromCollectionDomain(docs) ){
                                audioBookList.add(new AudioBook(Util.INSTANCE.ExtractRating(docs), Util.INSTANCE.ExtractDescription(docs), Util.INSTANCE.ExtractNoOfDownloads(docs), docs.getString("identifier"), Util.INSTANCE.ExtractNoReviews(docs), docs.getString("title"), Util.INSTANCE.ExtractPublisher(docs), Util.INSTANCE.ExtractMediaType(docs),Util.INSTANCE.EtracCreator(docs),Util.INSTANCE.ExtractData(docs)));

                            }
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                    return audioBookList;
                }).subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe((result) -> {
                            if (result != null) {

                                mutableLiveData.setValue(audioBookList);
                            }else {
                                Log.d("BookFragment","no open source book find");
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
