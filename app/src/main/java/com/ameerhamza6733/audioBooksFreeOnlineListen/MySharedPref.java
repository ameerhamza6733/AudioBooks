package com.ameerhamza6733.audioBooksFreeOnlineListen;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.ameerhamza6733.audioBooksFreeOnlineListen.models.AudioBook;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;

/**
 * Created by AmeerHamza on 4/23/18.
 */

public class MySharedPref {
    public static final String SHARD_PREF_AUDIO_BOOK_FILE_NAME ="SHARD_PREF_AUDIO_BOOK_FILE_NAME";
    public static final String SHARD_PREF_DOWNLOADED_AUDIO_BOOK ="SHARD_PREF_DOWNLOADED_AUDIO_BOOK";
    public static final String  SHARD_PREF_HISTORY_AUDIO_BOOK_FILE_NAME="SHARD_PREF_HISTORY_AUDIO_BOOK_FILE_NAME";
   public static final String SHARD_PREF_BOOK_MARK_FILE_NAME="SHARD_PREF_BOOK_MARK_FILE_NAME";
    public static boolean saveObjectToSharedPreference(Context context, String preferenceFileName, String serializedObjectKey, Object object) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, 0);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        final Gson gson = new Gson();
        String serializedObject = gson.toJson(object);
        sharedPreferencesEditor.putString(serializedObjectKey, serializedObject);
      return  sharedPreferencesEditor.commit();
    }

    public static Map<String,?> getAllKeys(SharedPreferences sharedPreferences){

      return sharedPreferences.getAll();

    }

    public static synchronized AudioBook getSavedObjectFromPreference(Context context, String preferenceFileName, String preferenceKey, Class<AudioBook> classType) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, 0);
        if (sharedPreferences.contains(preferenceKey)) {
            final Gson gson = new Gson();
            return gson.fromJson(sharedPreferences.getString(preferenceKey, ""), classType);
        }
        return null;
    }

    public static synchronized String getSavedObjectFromPreference(Context context, String preferenceFileName, String preferenceKey) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, 0);
        if (sharedPreferences.contains(preferenceKey)) {
            final Gson gson = new Gson();
            return gson.fromJson(sharedPreferences.getString(preferenceKey, ""), String.class);
        }
        return null;
    }


    public static synchronized Long getSavedLongFromPreference(Context context, String preferenceFileName, String preferenceKey) throws Exception{
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, 0);
        if (sharedPreferences.contains(preferenceKey)) {
            final Gson gson = new Gson();
            return gson.fromJson(sharedPreferences.getString(preferenceKey, ""), Long.class);
        }
        return null;
    }
}
