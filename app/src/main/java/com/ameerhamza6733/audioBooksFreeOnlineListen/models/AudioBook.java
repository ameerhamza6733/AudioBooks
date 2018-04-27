package com.ameerhamza6733.audioBooksFreeOnlineListen.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by AmeerHamza on 2/8/2018.
 */

public class AudioBook implements Serializable{
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

    protected AudioBook(Parcel in) {
        avg_rating = in.readString();
        description = in.readString();
        downloads = in.readString();
        identifier = in.readString();
        num_reviews = in.readString();
        title = in.readString();
        publisher = in.readString();
        creator = in.readString();
        mediatype = in.readString();
        data=in.readString();
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
}
