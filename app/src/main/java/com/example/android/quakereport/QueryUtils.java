package com.example.android.quakereport;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by himanshuanand on 7/30/16.
 */

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    /**
     * Sample JSON response for a USGS query
     */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();
    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    public static URL createUrl(String url){
        URL usgsUrl = null;
        try {
            usgsUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return usgsUrl;
    }

    public static String establishHttpUrlConnection(URL url){
        InputStream inputStream = null;
        HttpURLConnection earthquakeUrlConnection = null;
        String jsonResponse = "";

        try {
            earthquakeUrlConnection = (HttpURLConnection) url.openConnection();
            earthquakeUrlConnection.setRequestMethod("GET");
            earthquakeUrlConnection.setReadTimeout(10000 /*milliseconds*/);
            earthquakeUrlConnection.setConnectTimeout(15000 /*milliseconds*/);
            earthquakeUrlConnection.connect();
            if (earthquakeUrlConnection.getResponseCode() == 200){
                inputStream = earthquakeUrlConnection.getInputStream();
                jsonResponse = getJsonResponse(inputStream);
                return jsonResponse;
            }else {
                Log.e(LOG_TAG,"Error fetching earthquake data from the server: "
                        + earthquakeUrlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG,"Problem retrieving the Earthquake JSON results",e);
        }finally {
            if (earthquakeUrlConnection != null){
                earthquakeUrlConnection.disconnect();
            }
            if (inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return jsonResponse;
    }

    public static String getJsonResponse(InputStream inputStream){
        StringBuilder output  = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            try {
                String line = reader.readLine();
                while (line != null){
                    output.append(line);
                    line = reader.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link Earthquake} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<Earthquake> extractEarthquakes(String url) {

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<Earthquake> earthquakes = new ArrayList<>();

        if (TextUtils.isEmpty(url)){
            return null;
        }
        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // build up a list of Earthquake objects with the corresponding data.
            JSONObject earthquakeDataObject = new JSONObject(url);

            JSONArray earthquakeFeaturesArray = earthquakeDataObject.optJSONArray("features");

            for (int index = 0;index < earthquakeFeaturesArray.length();index++){
                JSONObject earthquakeFeaturesObject = (JSONObject) earthquakeFeaturesArray.get(index);

                JSONObject earthquakePropertiesObject = (JSONObject) earthquakeFeaturesObject.get("properties");

                Double magnitude = earthquakePropertiesObject.getDouble("mag");
                String place = earthquakePropertiesObject.getString("place");
                long timeInMilliseconds = earthquakePropertiesObject.getLong("time");
                String earthquakeDetailUrl = earthquakePropertiesObject.getString("url");

                earthquakes.add(new Earthquake(magnitude,place,timeInMilliseconds,earthquakeDetailUrl));
            }


        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }

}