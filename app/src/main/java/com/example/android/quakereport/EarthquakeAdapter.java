package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 * Created by himanshuanand on 7/30/16.
 */
public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    private static final String LOCATION_SEPARATOR = " of ";

    public EarthquakeAdapter(Context context, ArrayList<Earthquake> earthquakeArrayList) {
        super(context, 0,earthquakeArrayList);
    }

    static class ViewHolder{
        TextView magnitudeTextView;
        TextView streetTextView;
        TextView locationTextView;
        TextView dateTextView;
        TextView timeTextView;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
            holder = new ViewHolder();
            holder.magnitudeTextView = (TextView) convertView.findViewById(R.id.magnitude);
            holder.streetTextView = (TextView) convertView.findViewById(R.id.street);
            holder.locationTextView = (TextView) convertView.findViewById(R.id.location);
            holder.dateTextView = (TextView) convertView.findViewById(R.id.date);
            holder.timeTextView = (TextView) convertView.findViewById(R.id.time);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Earthquake earthquakeData = getItem(position);

        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) holder.magnitudeTextView.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(earthquakeData.getMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);

        DecimalFormat formatter = new DecimalFormat("0.0");
        String magnitude = formatter.format(earthquakeData.getMagnitude());
        holder.magnitudeTextView.setText(magnitude);

        String originalLocation = earthquakeData.getLocation();
        String locationOffset;
        String primaryLocation;
        if (originalLocation.contains(LOCATION_SEPARATOR)) {
            String[] placeArray = originalLocation.split("of");
            locationOffset = placeArray[0] + LOCATION_SEPARATOR;
            primaryLocation = placeArray[1];
        }else {
            locationOffset = getContext().getString(R.string.near_the);
            primaryLocation = originalLocation;
        }

        holder.streetTextView.setText(locationOffset);
        holder.locationTextView.setText(primaryLocation);

        Date dateObject = new Date(earthquakeData.getTimeInMilliseconds());

        // Format the date string (i.e. "Mar 3, 1984")
        String formattedDate = formatDate(dateObject);
        // Display the date of the current earthquake in that TextView
        holder.dateTextView.setText(formattedDate);

        // Format the time string (i.e. "4:30PM")
        String formattedTime = formatTime(dateObject);

        // Display the time of the current earthquake in that TextView
        holder.timeTextView.setText(formattedTime);


        return convertView;
    }

    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }


    private int getMagnitudeColor(double magnitude){

        int magnitudeColor = R.color.colorPrimary;
        int earthquakeMagnitude = (int) Math.floor(magnitude);
        switch (earthquakeMagnitude){
            case 0:
            case 1:
                magnitudeColor = R.color.magnitude1;
                break;
            case 2:
                magnitudeColor = R.color.magnitude2;
                break;
            case 3:
                magnitudeColor = R.color.magnitude3;
                break;
            case 4:
                magnitudeColor = R.color.magnitude4;
                break;
            case 5:
                magnitudeColor = R.color.magnitude5;
                break;
            case 6:
                magnitudeColor = R.color.magnitude6;
                break;
            case 7:
                magnitudeColor = R.color.magnitude7;
                break;
            case 8:
                magnitudeColor = R.color.magnitude8;
                break;
            case 9:
                magnitudeColor = R.color.magnitude9;
                break;
            default:
                magnitudeColor = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(),magnitudeColor);
    }
}
