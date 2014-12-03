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

    //Calculate angle between two coordinates (offset for compass)
    /* Not in use!
    public float angleFromCoordinate(double lat1, double long1, double lat2, double long2) {

        double dLon = (long2 - long1);

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon);

        double brng = Math.atan2(y, x);

        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;
        brng = 360 - brng;

        return (float)brng;
    */

}
