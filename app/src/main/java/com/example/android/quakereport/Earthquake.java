package com.example.android.quakereport;

/**
 * Created by himanshuanand on 7/30/16.
 */
public class Earthquake {

    private Double mMagnitude;
    private String mLocation;
    private long mTimeInMilliseconds;
    private String mUrl;

    public Double getMagnitude() {
        return mMagnitude;
    }

    public long getTimeInMilliseconds() {
        return mTimeInMilliseconds;
    }

    public String getLocation() {
        return mLocation;
    }

    public Earthquake(Double magnitude, String location,long timeInMilliseconds,String url) {
        this.mMagnitude = magnitude;
        this.mTimeInMilliseconds = timeInMilliseconds;
        this.mLocation = location;
        this.mUrl = url;
    }

    public String getUrl() {
        return mUrl;
    }
}
