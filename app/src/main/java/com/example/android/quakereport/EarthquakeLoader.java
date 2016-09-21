package com.example.android.quakereport;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by himanshuanand on 8/1/16.
 */
/**
 * Loads a list of earthquakes by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class EarthquakeLoader extends AsyncTaskLoader<ArrayList<Earthquake>> {

    /** Tag for log messages */
    private static final String LOG_TAG = EarthquakeLoader.class.getName();

    private String mUrl;

    /**
     * Constructs a new {@link EarthquakeLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    public EarthquakeLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<Earthquake> loadInBackground() {
        URL url = QueryUtils.createUrl(mUrl);

        // Perform the network request, parse the response, and extract a list of earthquakes.
        String jsonResponse = "";
        jsonResponse = QueryUtils.establishHttpUrlConnection(url);
        final ArrayList<Earthquake> earthquakes = QueryUtils.extractEarthquakes(jsonResponse);
        return earthquakes;
    }
}
