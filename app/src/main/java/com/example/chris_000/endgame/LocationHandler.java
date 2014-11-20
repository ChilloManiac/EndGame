package com.example.chris_000.endgame;

import android.location.Location;

/**
 * Created by AFA on 20-11-2014.
 */
public class LocationHandler {

    public LocationHandler(){}

    // Public method for converting a Location to a String, with parameters shown in degrees.
    public static String locationStringFromLocation(final Location location) {
        return Location.convert(location.getLatitude(), Location.FORMAT_DEGREES) + " "
                + Location.convert(location.getLongitude(), Location.FORMAT_DEGREES);
    }

}
