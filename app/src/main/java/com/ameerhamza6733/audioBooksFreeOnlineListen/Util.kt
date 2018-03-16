package com.ameerhamza6733.audioBooksFreeOnlineListen

import org.json.JSONException
import org.json.JSONObject
import kotlin.collections.ArrayList


/**
 * Created by AmeerHamza on 2/6/2018.
 */

object Util {

    val LIBRIVOX_URL = "https://archive.org/advancedsearch.php?q=subject%3A%22librivox%22+AND+mediatype%3Aaudio&fl[]=avg_rating&fl[]=publisher&fl[]=downloads&fl[]=identifier&fl[]=mediatype&fl[]=num_reviews&fl[]=title&sort[]=&sort[]=&sort[]=&rows=500&page=1&output=json&callback=callback&save=yes"
    val literature_URL = "https://archive.org/advancedsearch.php?q=subject%3A%22literature%22+AND+mediatype%3Aaudio&fl[]=avg_rating&fl[]=publisher&fl[]=downloads&fl[]=identifier&fl[]=mediatype&fl[]=num_reviews&fl[]=title&sort[]=&sort[]=&sort[]=&rows=100&page=1&output=json&callback=callback&save=yes"
    val poetry_URL = "https://archive.org/advancedsearch.php?q=subject%3A%22poetry%22+AND+mediatype%3Aaudio&fl[]=avg_rating&fl[]=publisher&fl[]=description&fl[]=downloads&fl[]=identifier&fl[]=mediatype&fl[]=num_reviews&fl[]=title&sort[]=&sort[]=&sort[]=&rows=100&page=1&output=json&callback=callback&save=yes"
    val fiction_URL = "https://archive.org/advancedsearch.php?q=subject%3A%22fiction%22+AND+mediatype%3Aaudio&fl[]=avg_rating&fl[]=publisher&fl[]=description&fl[]=downloads&fl[]=identifier&fl[]=mediatype&fl[]=num_reviews&fl[]=title&sort[]=&sort[]=&sort[]=&rows=100&page=1&output=json&callback=callback&save=yes"
    val Community_Audio_URL = "https://archive.org/advancedsearch.php?q=subject%3A%22opensource_audio%22+AND+mediatype%3Aaudio&fl[]=avg_rating&fl[]=publisher&fl[]=description&fl[]=downloads&fl[]=identifier&fl[]=mediatype&fl[]=num_reviews&fl[]=title&sort[]=&sort[]=&sort[]=&rows=100&page=1&output=json&callback=callback&save=yes"
    val philosophy_URL = "https://archive.org/advancedsearch.php?q=subject%3A%22philosophy%22+AND+mediatype%3Aaudio&fl[]=avg_rating&fl[]=publisher&fl[]=description&fl[]=downloads&fl[]=identifier&fl[]=mediatype&fl[]=num_reviews&fl[]=title&sort[]=&sort[]=&sort[]=&rows=100&page=1&output=json&callback=callback&save=yes"
    val children_URL = "https://archive.org/advancedsearch.php?q=subject%3A%22children%22+AND+mediatype%3Aaudio&fl[]=avg_rating&fl[]=publisher&fl[]=description&fl[]=downloads&fl[]=identifier&fl[]=mediatype&fl[]=num_reviews&fl[]=title&sort[]=&sort[]=&sort[]=&rows=100&page=1&output=json&callback=callback&save=yes"
    val plato_URL = "https://archive.org/advancedsearch.php?q=subject%3A%22plato%22+AND+mediatype%3Aaudio&fl[]=avg_rating&fl[]=publisher&fl[]=description&fl[]=downloads&fl[]=identifier&fl[]=mediatype&fl[]=num_reviews&fl[]=title&sort[]=&sort[]=&sort[]=&rows=100&page=1&output=json&callback=callback&save=yes"
    val romance_URL = "https://archive.org/advancedsearch.php?q=subject%3A%22romance%22+AND+mediatype%3Aaudio&fl[]=avg_rating&fl[]=publisher&fl[]=description&fl[]=downloads&fl[]=identifier&fl[]=mediatype&fl[]=num_reviews&fl[]=title&sort[]=&sort[]=&sort[]=&rows=100&page=1&output=json&callback=callback&save=yes"
    val history_URL = "https://archive.org/advancedsearch.php?q=subject%3A%22history%22+AND+mediatype%3Aaudio&fl[]=avg_rating&fl[]=publisher&fl[]=description&fl[]=downloads&fl[]=identifier&fl[]=mediatype&fl[]=num_reviews&fl[]=title&sort[]=&sort[]=&sort[]=&rows=100&page=1&output=json&callback=callback&save=yes"
    val nature_URL = "https://archive.org/advancedsearch.php?q=subject%3A%22nature%22+AND+mediatype%3Aaudio&fl[]=avg_rating&fl[]=publisher&fl[]=description&fl[]=downloads&fl[]=identifier&fl[]=mediatype&fl[]=num_reviews&fl[]=title&sort[]=&sort[]=&sort[]=&rows=100&page=1&output=json&callback=callback&save=yes"
    val humor_URL = "https://archive.org/advancedsearch.php?q=subject%3A%22humor%22+AND+mediatype%3Aaudio&fl[]=avg_rating&fl[]=publisher&fl[]=description&fl[]=downloads&fl[]=identifier&fl[]=mediatype&fl[]=num_reviews&fl[]=title&sort[]=&sort[]=&sort[]=&rows=100&page=1&output=json&callback=callback&save=yes"
    val mystery_URL = "https://archive.org/advancedsearch.php?q=subject%3A%22mystery%22+AND+mediatype%3Aaudio&fl[]=avg_rating&fl[]=publisher&fl[]=description&fl[]=downloads&fl[]=identifier&fl[]=mediatype&fl[]=num_reviews&fl[]=title&sort[]=&sort[]=&sort[]=&rows=100&page=1&output=json&callback=callback&save=yes"
    val poem_URL = "https://archive.org/advancedsearch.php?q=subject%3A%22poem%22+AND+mediatype%3Aaudio&fl[]=avg_rating&fl[]=publisher&fl[]=description&fl[]=downloads&fl[]=identifier&fl[]=mediatype&fl[]=num_reviews&fl[]=title&sort[]=&sort[]=&sort[]=&rows=100&page=1&output=json&callback=callback&save=yes"
    val animals_URL = "https://archive.org/advancedsearch.php?q=subject%3A%22animals%22+AND+mediatype%3Aaudio&fl[]=avg_rating&fl[]=publisher&fl[]=description&fl[]=downloads&fl[]=identifier&fl[]=mediatype&fl[]=num_reviews&fl[]=title&sort[]=&sort[]=&sort[]=&rows=100&page=1&output=json&callback=callback&save=yes"
    val novel_URL = "https://archive.org/advancedsearch.php?q=subject%3A%22novel%22+AND+mediatype%3Aaudio&fl[]=avg_rating&fl[]=publisher&fl[]=description&fl[]=downloads&fl[]=identifier&fl[]=mediatype&fl[]=num_reviews&fl[]=title&sort[]=&sort[]=&sort[]=&rows=100&page=1&output=json&callback=callback&save=yes"
    val dahszil_URL = "https://archive.org/advancedsearch.php?q=collection%3A%22audio_bookspoetry%22+AND+mediatype%3Aaudio&fl[]=avg_rating&fl[]=publisher&fl[]=description&fl[]=downloads&fl[]=identifier&fl[]=mediatype&fl[]=num_reviews&fl[]=title&sort[]=-downloads&sort[]=&sort[]=&rows=100&page=1&output=json&callback=callback&save=yes"
    val gaboora_URL = "https://archive.org/advancedsearch.php?q=collection%3A%22audio_bookspoetry%22+AND+mediatype%3Aaudio&fl[]=avg_rating&fl[]=publisher&fl[]=description&fl[]=downloads&fl[]=identifier&fl[]=mediatype&fl[]=num_reviews&fl[]=title&sort[]=-downloads&sort[]=&sort[]=&rows=100&page=1&output=json&callback=callback&save=yes"

    val CONN_META_DATA_URL="https://archive.org/metadata/moby_dick_librivox"
    @Throws(JSONException::class)
    fun toJson(s: String): JSONObject {
        return JSONObject(s)
    }

    fun quraryBuilder(queary : String) : String{
        return "https://archive.org/advancedsearch.php?q=title%3A%22"+queary+"%22+AND+mediatype%3Aaudio&fl[]=avg_rating&fl[]=publisher&fl[]=description&fl[]=downloads&fl[]=identifier&fl[]=mediatype&fl[]=num_reviews&fl[]=title&sort[]=&sort[]=&sort[]=&rows=50&page=1&output=json&callback=callback&save=yes"
    }

    fun isTitleTitle(jsonObject: JSONObject, title: String): Boolean {
        try {
            return jsonObject.getString("title").replace("[^A-Za-z0-9]".toRegex(), " ").toLowerCase().contains(title)

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return false
    }

    fun isTypeAudio(jsonObject: JSONObject): Boolean {
        try {
            return jsonObject.getString("mediatype").equals("audio", ignoreCase = true)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return false
    }

    fun ExtractRating(jsonObj: JSONObject): String {
        try {
            return jsonObj.getString("avg_rating");
        } catch (e: Exception) {
            return "N/A"
        }
    }

    fun ExtractNoOfDownloads(jsonObject: JSONObject): String {
        try {
            return jsonObject.getString("downloads")
        } catch (e: Exception) {
            return "N/A"
        }
    }

    fun ExtractDescription(jsonObject: JSONObject): String {
        try {
            return jsonObject.getString("description")
        } catch (e: Exception) {
            return "N/A"
        }
    }

    fun ExtractNoReviews(jsonObject: JSONObject): String {
        try {
            return jsonObject.getString("num_reviews")
        } catch (e: Exception) {
            return "N/A"
        }
    }

    fun ExtractPublisher(jsonObject: JSONObject):String{
        return try {
           // Log.d("ExtractPublisher","json:  "+jsonObject.toString());
            jsonObject.getString("publisher")
        } catch (e: Exception) {
            "N/A"
        }
    }

    fun ExtractMediaType(jsonObject: JSONObject) : String{
        return try {
            jsonObject.getString("mediatype")
        } catch (e: Exception) {
            "N/A"
        }
    }

    fun cleanData(JsonList: List<JSONObject>): List<JSONObject> {
        var updateJsonObjectList: ArrayList<JSONObject>
        for (singalObject in JsonList) {

        }
        return JsonList
    }

    val supportedFormate = arrayOf("MP4", "M4A", "FMP4", "WebM", "MKV", "MP3", "Ogg", "WAV", "MPEG-TS", "MPEG-PS", "FLV")
     //https://archive.org/download/$identifier/$filename
    fun toDownloadAbleFileUri(fileName : String, identifier: String): String{
        return "https://archive.org/download/$identifier/$fileName"
    }

    fun toMetaDataURI(identifier: String) :String{
        return "https://archive.org/metadata/" + identifier
    }

    fun toImageURI(identifier: String):String{
        return "https://archive.org/services/img/"+identifier
    }

    fun isSuppotedFormate(fileName: String):Boolean{
       var isSuppoted  = false;
        for (formate in supportedFormate){
            if ( fileName.toLowerCase().endsWith(formate.toLowerCase())){
                isSuppoted=true;
            }
        }
       return isSuppoted;
    }


    fun formatSeconds(timeInSeconds: Long): String {
        val hours = timeInSeconds / 3600
        val secondsLeft = timeInSeconds - hours * 3600
        val minutes = secondsLeft / 60
        val seconds = secondsLeft - minutes * 60

        var formattedTime = ""
        if (hours < 10)
            formattedTime += "0"
        formattedTime += hours.toString() + ":"

        if (minutes < 10)
            formattedTime += "0"
        formattedTime += minutes.toString() + ":"

        if (seconds < 10)
            formattedTime += "0"
        formattedTime += seconds

        return formattedTime
    }
}
