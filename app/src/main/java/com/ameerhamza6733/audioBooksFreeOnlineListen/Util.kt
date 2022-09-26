package com.ameerhamza6733.audioBooksFreeOnlineListen

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Environment
import org.json.JSONException
import org.json.JSONObject
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by AmeerHamza on 2/6/2018.
 */

object Util {

//    val LIBRIVOX_URL = "https://archive.org/advancedsearch.php?q=subject%3A%22librivox%22+AND+mediatype%3Aaudio&fl[]=avg_rating&fl[]=publisher&fl[]=description&fl[]=downloads&fl[]=identifier&fl[]=mediatype&fl[]=num_reviews&fl[]=creator&fl[]=title&sort[]=&sort[]=&sort[]=&rows=500&page=1&output=json&callback=callback&save=yes"
//    val literature_URL = "https://archive.org/advancedsearch.php?q=subject%3A%22literature%22+AND+mediatype%3Aaudio&fl[]=avg_rating&fl[]=publisher&fl[]=downloads&fl[]=identifier&fl[]=mediatype&fl[]=num_reviews&fl[]=title&sort[]=&sort[]=&sort[]=&rows=100&page=1&output=json&callback=callback&save=yes"
//    val poetry_URL = "https://archive.org/advancedsearch.php?q=subject%3A%22poetry%22+AND+mediatype%3Aaudio&fl[]=avg_rating&fl[]=publisher&fl[]=description&fl[]=downloads&fl[]=identifier&fl[]=mediatype&fl[]=num_reviews&fl[]=title&sort[]=&sort[]=&sort[]=&rows=100&page=1&output=json&callback=callback&save=yes"
//    val fiction_URL = "https://archive.org/advancedsearch.php?q=subject%3A%22fiction%22+AND+mediatype%3Aaudio&fl[]=avg_rating&fl[]=publisher&fl[]=description&fl[]=downloads&fl[]=identifier&fl[]=mediatype&fl[]=num_reviews&fl[]=title&sort[]=&sort[]=&sort[]=&rows=100&page=1&output=json&callback=callback&save=yes"
//    val Community_Audio_URL = "https://archive.org/advancedsearch.php?q=subject%3A%22opensource_audio%22+AND+mediatype%3Aaudio&fl[]=avg_rating&fl[]=publisher&fl[]=description&fl[]=downloads&fl[]=identifier&fl[]=mediatype&fl[]=num_reviews&fl[]=title&sort[]=&sort[]=&sort[]=&rows=100&page=1&output=json&callback=callback&save=yes"
//    val philosophy_URL = "https://archive.org/advancedsearch.php?q=subject%3A%22philosophy%22+AND+mediatype%3Aaudio&fl[]=avg_rating&fl[]=publisher&fl[]=description&fl[]=downloads&fl[]=identifier&fl[]=mediatype&fl[]=num_reviews&fl[]=title&sort[]=&sort[]=&sort[]=&rows=100&page=1&output=json&callback=callback&save=yes"
//    val children_URL = "https://archive.org/advancedsearch.php?q=subject%3A%22children%22+AND+mediatype%3Aaudio&fl[]=avg_rating&fl[]=publisher&fl[]=description&fl[]=downloads&fl[]=identifier&fl[]=mediatype&fl[]=num_reviews&fl[]=title&sort[]=&sort[]=&sort[]=&rows=100&page=1&output=json&callback=callback&save=yes"
//    val plato_URL = "https://archive.org/advancedsearch.php?q=subject%3A%22plato%22+AND+mediatype%3Aaudio&fl[]=avg_rating&fl[]=publisher&fl[]=description&fl[]=downloads&fl[]=identifier&fl[]=mediatype&fl[]=num_reviews&fl[]=title&sort[]=&sort[]=&sort[]=&rows=100&page=1&output=json&callback=callback&save=yes"
//    val romance_URL = "https://archive.org/advancedsearch.php?q=subject%3A%22romance%22+AND+mediatype%3Aaudio&fl[]=avg_rating&fl[]=publisher&fl[]=description&fl[]=downloads&fl[]=identifier&fl[]=mediatype&fl[]=num_reviews&fl[]=title&sort[]=&sort[]=&sort[]=&rows=100&page=1&output=json&callback=callback&save=yes"
//    val history_URL = "https://archive.org/advancedsearch.php?q=subject%3A%22history%22+AND+mediatype%3Aaudio&fl[]=avg_rating&fl[]=publisher&fl[]=description&fl[]=downloads&fl[]=identifier&fl[]=mediatype&fl[]=num_reviews&fl[]=title&sort[]=&sort[]=&sort[]=&rows=100&page=1&output=json&callback=callback&save=yes"
//    val nature_URL = "https://archive.org/advancedsearch.php?q=subject%3A%22nature%22+AND+mediatype%3Aaudio&fl[]=avg_rating&fl[]=publisher&fl[]=description&fl[]=downloads&fl[]=identifier&fl[]=mediatype&fl[]=num_reviews&fl[]=title&sort[]=&sort[]=&sort[]=&rows=100&page=1&output=json&callback=callback&save=yes"
//    val humor_URL = "https://archive.org/advancedsearch.php?q=subject%3A%22humor%22+AND+mediatype%3Aaudio&fl[]=avg_rating&fl[]=publisher&fl[]=description&fl[]=downloads&fl[]=identifier&fl[]=mediatype&fl[]=num_reviews&fl[]=title&sort[]=&sort[]=&sort[]=&rows=100&page=1&output=json&callback=callback&save=yes"
//    val mystery_URL = "https://archive.org/advancedsearch.php?q=subject%3A%22mystery%22+AND+mediatype%3Aaudio&fl[]=avg_rating&fl[]=publisher&fl[]=description&fl[]=downloads&fl[]=identifier&fl[]=mediatype&fl[]=num_reviews&fl[]=title&sort[]=&sort[]=&sort[]=&rows=100&page=1&output=json&callback=callback&save=yes"
//    val poem_URL = "https://archive.org/advancedsearch.php?q=subject%3A%22poem%22+AND+mediatype%3Aaudio&fl[]=avg_rating&fl[]=publisher&fl[]=description&fl[]=downloads&fl[]=identifier&fl[]=mediatype&fl[]=num_reviews&fl[]=title&sort[]=&sort[]=&sort[]=&rows=100&page=1&output=json&callback=callback&save=yes"
//    val animals_URL = "https://archive.org/advancedsearch.php?q=subject%3A%22animals%22+AND+mediatype%3Aaudio&fl[]=avg_rating&fl[]=publisher&fl[]=description&fl[]=downloads&fl[]=identifier&fl[]=mediatype&fl[]=num_reviews&fl[]=title&sort[]=&sort[]=&sort[]=&rows=100&page=1&output=json&callback=callback&save=yes"
//    val novel_URL = "https://archive.org/advancedsearch.php?q=subject%3A%22novel%22+AND+mediatype%3Aaudio&fl[]=avg_rating&fl[]=publisher&fl[]=description&fl[]=downloads&fl[]=identifier&fl[]=mediatype&fl[]=num_reviews&fl[]=title&sort[]=&sort[]=&sort[]=&rows=100&page=1&output=json&callback=callback&save=yes"
//    val dahszil_URL = "https://archive.org/advancedsearch.php?q=collection%3A%22audio_bookspoetry%22+AND+mediatype%3Aaudio&fl[]=avg_rating&fl[]=publisher&fl[]=description&fl[]=downloads&fl[]=identifier&fl[]=mediatype&fl[]=num_reviews&fl[]=title&sort[]=-downloads&sort[]=&sort[]=&rows=100&page=1&output=json&callback=callback&save=yes"
//    val gaboora_URL = "https://archive.org/advancedsearch.php?q=collection%3A%22audio_bookspoetry%22+AND+mediatype%3Aaudio&fl[]=avg_rating&fl[]=publisher&fl[]=description&fl[]=downloads&fl[]=identifier&fl[]=mediatype&fl[]=num_reviews&fl[]=title&sort[]=-downloads&sort[]=&sort[]=&rows=100&page=1&output=json&callback=callback&save=yes"

    fun BuildQuery(catalogue : String) : String{
       return "https://archive.org/advancedsearch.php?q=subject%3A%22"+catalogue+"%22+AND+mediatype%3Aaudio&sort=addeddate+desc&rows=100&page=1&output=json&callback=callback&save=yes"
        //return "https://archive.org/advancedsearch.php?q=subject%3A%22"+catalogue+"%22+AND+mediatype%3Aaudio&fl%5B%5D=avg_rating&fl%5B%5D=backup_location&fl%5B%5D=btih&fl%5B%5D=call_number&fl%5B%5D=collection&fl%5B%5D=contributor&fl%5B%5D=coverage&fl%5B%5D=creator&fl%5B%5D=date&fl%5B%5D=description&fl%5B%5D=downloads&fl%5B%5D=external-identifier&fl%5B%5D=foldoutcount&fl%5B%5D=format&fl%5B%5D=genre&fl%5B%5D=headerImage&fl%5B%5D=identifier&fl%5B%5D=imagecount&fl%5B%5D=indexflag&fl%5B%5D=item_size&fl%5B%5D=language&fl%5B%5D=licenseurl&fl%5B%5D=mediatype&fl%5B%5D=members&fl%5B%5D=month&fl%5B%5D=name&fl%5B%5D=noindex&fl%5B%5D=num_reviews&fl%5B%5D=oai_updatedate&fl%5B%5D=publicdate&fl%5B%5D=publisher&fl%5B%5D=related-external-id&fl%5B%5D=reviewdate&fl%5B%5D=rights&fl%5B%5D=scanningcentre&fl%5B%5D=source&fl%5B%5D=stripped_tags&fl%5B%5D=subject&fl%5B%5D=title&fl%5B%5D=type&fl%5B%5D=volume&fl%5B%5D=week&fl%5B%5D=year&sort%5B%5D=addeddate+desc&sort%5B%5D=&sort%5B%5D=&rows=50&page=1&output=json&callback=callback&save=yes"
    }
    val CONN_META_DATA_URL="https://archive.org/metadata/moby_dick_librivox"
    @Throws(JSONException::class)
    fun toJson(s: String): JSONObject {
        return JSONObject(s)
    }

    fun quraryBuilder( queary : String) : String {

        return "https://archive.org/advancedsearch.php?q=title%3A%22$queary~%22+AND+mediatype%3Aaudio&f&rows=100&page=1&output=json&callback=callback&save=yes"
    }


    fun SubjectSortBuilder(queary : String, sort : String) : String {
        return "https://archive.org/advancedsearch.php?q=subject%3A%22"+queary+"%22+AND+mediatype%3Aaudio&fl%5B%5D=avg_rating&fl%5B%5D=backup_location&fl%5B%5D=btih&fl%5B%5D=call_number&fl%5B%5D=collection&fl%5B%5D=contributor&fl%5B%5D=coverage&fl%5B%5D=creator&fl%5B%5D=date&fl%5B%5D=description&fl%5B%5D=downloads&fl%5B%5D=external-identifier&fl%5B%5D=foldoutcount&fl%5B%5D=format&fl%5B%5D=genre&fl%5B%5D=headerImage&fl%5B%5D=identifier&fl%5B%5D=imagecount&fl%5B%5D=indexflag&fl%5B%5D=item_size&fl%5B%5D=language&fl%5B%5D=licenseurl&fl%5B%5D=mediatype&fl%5B%5D=members&fl%5B%5D=month&fl%5B%5D=name&fl%5B%5D=noindex&fl%5B%5D=num_reviews&fl%5B%5D=oai_updatedate&fl%5B%5D=publicdate&fl%5B%5D=publisher&fl%5B%5D=related-external-id&fl%5B%5D=reviewdate&fl%5B%5D=rights&fl%5B%5D=scanningcentre&fl%5B%5D=source&fl%5B%5D=stripped_tags&fl%5B%5D=subject&fl%5B%5D=title&fl%5B%5D=type&fl%5B%5D=volume&fl%5B%5D=week&fl%5B%5D=year&sort%5B%5D="+sort+"&sort%5B%5D=&sort%5B%5D=&rows=100&page=1&output=json&callback=callback&save=yes"
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
        return try {
           "rating " +( jsonObj.getString("avg_rating"))
        } catch (e: Exception) {
            "rating " + "N/A"
        }
    }

    fun ExtractNoOfDownloads(jsonObject: JSONObject): String {
        return try {
            jsonObject.getString("downloads")
        } catch (e: Exception) {
            "N/A"
        }
    }

    fun ExtractDescription(jsonObject: JSONObject): String {
        return try {
            jsonObject.getString("description")
        } catch (e: Exception) {
            "N/A"
        }
    }

    fun ExtractNoReviews(jsonObject: JSONObject): String {
        return try {
            jsonObject.getString("num_reviews")
        } catch (e: Exception) {
            "N/A"
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

    fun ExtractData(jsonObject: JSONObject):String{
        return try {
         var time =    "date: "+ jsonObject.getString("publicdate")
            time= time.replace("date:","").trim()
            time=time.replace("T"," ");
            time=time.replaceAfterLast(":","");
            convertTimeWithTimeZome(time)
        }catch (e: Exception){
            e.printStackTrace()
            "N/A"
        }
    }
    fun EtracCreator(jsonObject: JSONObject) : String{
       return try {
           "by: "+ jsonObject.getString("creator")
        }catch (e:Exception){
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


    fun isResumeAble(context: Context,URL: String): Long{
        return try {
            MySharedPref.getSavedLongFromPreference(context.applicationContext, MySharedPref.SHARD_PREF_AUDIO_BOOK_FILE_NAME, URL);
        } catch (e: Exception) {
            e.printStackTrace()
            0;
        }

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
    fun getDir () :String{
        val saveFolder = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOWNLOADS
        ).absolutePath

        return saveFolder
    }

    fun bytesToMb(size: Long): String {

        val df = DecimalFormat("0.00")

        val sizeKb = 1024.0f
        val sizeMo = sizeKb * sizeKb
        val sizeGo = sizeMo * sizeKb
        val sizeTerra = sizeGo * sizeKb


        if (size < sizeMo)
            return df.format(size / sizeKb) + " KB"
        else if (size < sizeGo)
            return df.format(size / sizeMo) + " MB"
        else if (size < sizeTerra)
            return df.format(size / sizeGo) + " GB"

        return ""
    }

    fun drawableToBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        var width = drawable.intrinsicWidth
        width = if (width > 0) width else 1
        var height = drawable.intrinsicHeight
        height = if (height > 0) height else 1

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight())
        drawable.draw(canvas)

        return bitmap
    }


    private fun  convertTimeWithTimeZome( time : String) : String {

        val dateFormat = SimpleDateFormat("dd-MMM-yyyy E hh:mm a z")
        var date: Date? = null
        try {
                val  sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH)
                val dateThen = sdf.parse(time)
                var dateNow = Date(System.currentTimeMillis())
                dateNow = sdf.parse(sdf.format(dateNow))
                val diff = dateNow.time - dateThen.getTime()
                val diffSeconds = diff / 1000 % 60
                val diffMinutes = diff / (60 * 1000) % 60
                val diffHours = diff / (60 * 60 * 1000) % 24
                val diffDays = diff / (24 * 60 * 60 * 1000)

                return getAgo(diffSeconds, diffMinutes, diffHours, diffDays)
            } catch (e: ParseException) {
                e.printStackTrace()
                return "N/A"
            }


    }

    private fun getAgo(diffSeconds: Long, diffMinutes: Long, diffHours: Long, diffDays: Long): String {
        return if (diffDays > 0) {
            "submitted $diffDays day ago "
        } else if (diffHours > 0) {
            "submitted $diffHours hour ago "
        } else if (diffMinutes > 0) {
            "submitted $diffMinutes mint ago "
        } else if (diffSeconds > 0) {
            "submitted $diffSeconds second ago "
        } else {
            "N/A"
        }

    }

}

