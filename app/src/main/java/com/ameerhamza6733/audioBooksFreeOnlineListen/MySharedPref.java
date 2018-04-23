package com.ameerhamza6733.audioBooksFreeOnlineListen;

import android.content.Context;
import android.content.SharedPreferences;

import com.ameerhamza6733.audioBooksFreeOnlineListen.models.AudioBook;
import com.google.gson.Gson;

/**
 * Created by apple on 4/23/18.
 */

public class MySharedPref {
    public static final String SHARD_PREF_AUDIO_BOOK="SHARD_PREF_AUDIO_BOOK";
    public static void saveObjectToSharedPreference(Context context, String preferenceFileName, String serializedObjectKey, Object object) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, 0);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        final Gson gson = new Gson();
        String serializedObject = gson.toJson(object);
        sharedPreferencesEditor.putString(serializedObjectKey, serializedObject);
        sharedPreferencesEditor.apply();
    }

    public static AudioBook getSavedObjectFromPreference(Context context, String preferenceFileName, String preferenceKey, Class<AudioBook> classType) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, 0);
        if (sharedPreferences.contains(preferenceKey)) {
            final Gson gson = new Gson();
            return gson.fromJson(sharedPreferences.getString(preferenceKey, ""), classType);
        }
        return null;
    }

}
