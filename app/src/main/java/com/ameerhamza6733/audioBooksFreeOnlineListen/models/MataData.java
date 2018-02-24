package com.ameerhamza6733.audioBooksFreeOnlineListen.models;

/**
 * Created by AmeerHamza on 2/9/2018.
 */

public class MataData {

    private String name;
    private long size;
    private String URL;

    public MataData( String name,long size,String URL) {
        this.name=name;
        this.size=size;
        this.URL=URL;
    }


    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }

    public String getURL() {
        return URL;
    }
}
