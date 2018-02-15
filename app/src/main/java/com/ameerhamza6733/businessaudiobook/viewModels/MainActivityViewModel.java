package com.ameerhamza6733.businessaudiobook.viewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.ameerhamza6733.businessaudiobook.models.AudioBook;
import com.ameerhamza6733.businessaudiobook.activitys.MainActivity;
import com.ameerhamza6733.businessaudiobook.Util;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AmeerHamza on 2/8/2018.
 */

public class MainActivityViewModel extends ViewModel {

    private MutableLiveData<List<AudioBook>> mutableLiveData;
    private List<AudioBook> jsonObjectList;
    private String url;
    public LiveData<List<AudioBook>> loadData(RequestQueue requestQueue,String url){
        if (mutableLiveData==null || !this.url.equalsIgnoreCase(url)){
            mutableLiveData=   new MutableLiveData<List<AudioBook>>();
            jsonObjectList= new ArrayList<>();
            this.url=url;
            intiVolley(requestQueue);

        }
        return mutableLiveData;
    }
    JSONObject jsonObject;

    private void intiVolley(RequestQueue requestQueue) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET,url, response -> {
            try {
                Log.d("RecyclerViewFragment","url: "+url);
                jsonObject = Util.INSTANCE.toJson(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                JSONArray jsonArray = jsonObject.getJSONObject("response").getJSONArray("docs");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject docs =  jsonArray.getJSONObject(i);
                    jsonObjectList.add(new AudioBook(Util.INSTANCE.ExtractRating(docs),Util.INSTANCE.ExtractDescription(docs),Util.INSTANCE.ExtractNoOfDownloads(docs)
                    ,docs.getString("identifier"),Util.INSTANCE.ExtractNoReviews(docs),docs.getString("title"),Util.INSTANCE.ExtractPublisher(docs),Util.INSTANCE.ExtractMediaType(docs)));
                }
                mutableLiveData.setValue(jsonObjectList);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d(MainActivity.TAG, "volley response");
        }, (error) -> {
            error.printStackTrace();
            mutableLiveData.setValue(null);
            Log.d(MainActivity.TAG, "volley error" + error.getMessage());
        });
        requestQueue.add(stringRequest);

    }
}
