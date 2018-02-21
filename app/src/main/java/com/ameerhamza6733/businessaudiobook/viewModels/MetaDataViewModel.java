package com.ameerhamza6733.businessaudiobook.viewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.ameerhamza6733.businessaudiobook.Util;
import com.ameerhamza6733.businessaudiobook.activitys.DetailActivity;
import com.ameerhamza6733.businessaudiobook.models.MataData;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by AmeerHamza on 2/8/2018.
 */

public class MetaDataViewModel extends ViewModel {

    private MutableLiveData<List<MataData>> mutableLiveData;

    private String META_DATA_CONNECTION_URL;
    private String TAG = DetailActivity.TAG;
    private String identifier;

    public LiveData<List<MataData>> loadData(RequestQueue requestQueue, String url,String iidentifier) {
        if (this.mutableLiveData == null || !url.equalsIgnoreCase(this.META_DATA_CONNECTION_URL)) {
           this. mutableLiveData = new MutableLiveData<List<MataData>>();
            this.audioFileList = new ArrayList<>();
            this.META_DATA_CONNECTION_URL=url;
            this.identifier=iidentifier;
            intiVolley(requestQueue);

        }
        return mutableLiveData;
    }

    private List<MataData> audioFileList;
    private void intiVolley(RequestQueue requestQueue) {
        Log.d(TAG,"intiVolley and url: "+META_DATA_CONNECTION_URL);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, META_DATA_CONNECTION_URL, null, response -> {
            try {

                JSONArray jsonArray = response.getJSONArray("files");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject docs = jsonArray.getJSONObject(i);
                    if (docs.getString("source").equalsIgnoreCase("original") && Util.INSTANCE.isSuppotedFormate( docs.getString("name"))) {
                        Log.d(TAG, "name: " + docs.getString("name"));
                        MataData mataData= new MataData(docs.getString("name"),Long.parseLong(docs.getString("size")),Util.INSTANCE.toDownloadAbleFileUri(docs.getString("name"),identifier));
                        audioFileList.add(mataData);
                    }
                }
                //try alternative source
                if (audioFileList.size()==0){
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject docs = jsonArray.getJSONObject(i);
                        if (docs.getString("source").equalsIgnoreCase("derivative") && Util.INSTANCE.isSuppotedFormate( docs.getString("name"))) {
                            Log.d(TAG, "name: " + docs.getString("name"));
                            MataData mataData= new MataData(docs.getString("name"),Long.parseLong(docs.getString("size")),Util.INSTANCE.toDownloadAbleFileUri(docs.getString("name"),identifier));
                            audioFileList.add(mataData);
                        }
                    }
                }
               mutableLiveData.setValue(audioFileList);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d(TAG, "volley response");
        }, (error) -> {
            mutableLiveData.setValue(null);
            Log.d(TAG, "volley error" + error.getMessage());
        });
        requestQueue.add(stringRequest);

    }
}