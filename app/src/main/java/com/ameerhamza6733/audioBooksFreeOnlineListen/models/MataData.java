package com.ameerhamza6733.audioBooksFreeOnlineListen.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by AmeerHamza on 2/9/2018.
 */

public class MataData implements Parcelable {

    private String name;
    private long size;
    private String URL;
    private boolean hasDownloaded;
    private String sdPath="";

    public MataData(String name, long size, String URL, boolean hasDownloaded) {
        this.name = name;
        this.size = size;
        this.URL = URL;
        this.hasDownloaded = hasDownloaded;
    }


    protected MataData(Parcel in) {
        name = in.readString();
        size = in.readLong();
        URL = in.readString();
        hasDownloaded = in.readByte() != 0;
        sdPath=in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeLong(size);
        dest.writeString(URL);
        dest.writeString(sdPath);
        dest.writeByte((byte) (hasDownloaded ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MataData> CREATOR = new Creator<MataData>() {
        @Override
        public MataData createFromParcel(Parcel in) {
            return new MataData(in);
        }

        @Override
        public MataData[] newArray(int size) {
            return new MataData[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setHasDownloaded(boolean hasDownloaded) {
        this.hasDownloaded = hasDownloaded;
    }

    public void setSdPath(String sdPath) {
        this.sdPath = sdPath;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getSize() {
        return size;
    }

    public String getURL() {
        return URL;
    }

    public boolean hasDownloaded() {
        return hasDownloaded;
    }

    public String getSdPath() {
        return sdPath;
    }

    public static Creator<MataData> getCREATOR() {
        return CREATOR;
    }
}
