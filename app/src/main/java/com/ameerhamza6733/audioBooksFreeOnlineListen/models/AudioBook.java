package com.ameerhamza6733.audioBooksFreeOnlineListen.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by AmeerHamza on 2/8/2018.
 */

public class AudioBook {
    private String avg_rating;
    private String description;
    private String downloads;
    private String identifier;
    private String num_reviews;
    private String title;
    private String publisher;
    private String creator;
    private String mediatype;
    private String data;
    private List<MataData> mataData;

    public AudioBook(String avg_rating, String description, String downloads, String identifier, String num_reviews, String title,String publisher,String mediatype,String creator,String data) {
        this.avg_rating = avg_rating;
        this.description = description;
        this.downloads = downloads;
        this.identifier = identifier;
        this.num_reviews = num_reviews;
        this.title = title;
        this.publisher=publisher;
        this.mediatype=mediatype;
        this.creator=creator;
        this.data=data;
    }

    public AudioBook(String avg_rating, String description, String downloads, String identifier, String num_reviews, String title, String publisher, String creator, String mediatype, String data, List<MataData> mataData) {
        this.avg_rating = avg_rating;
        this.description = description;
        this.downloads = downloads;
        this.identifier = identifier;
        this.num_reviews = num_reviews;
        this.title = title;
        this.publisher = publisher;
        this.creator = creator;
        this.mediatype = mediatype;
        this.data = data;
        this.mataData = mataData;
    }


    public String getAvg_rating() {
        return avg_rating;
    }

    public String getDescription() {
        return description;
    }

    public String getDownloads() {
        return downloads;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getNum_reviews() {
        return num_reviews;
    }

    public String getTitle() {
        return title;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getMediatype() {
        return mediatype;
    }

    public String getCreator() {
        return creator;
    }

    public String getData() {
        return data;
    }

    public void setMataData(List<MataData> mataData) {
        this.mataData = mataData;
    }

    public List<MataData> getMataData() {
        return mataData;
    }
}
