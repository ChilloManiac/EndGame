package com.example.chris_000.endgame;

import android.location.Location;
import android.location.LocationManager;

import java.util.List;

public class LocationHandler {

    public LocationHandler() {
    }

    public static String locationStringFromLocation(final Location location) {
        return Location.convert(location.getLatitude(), Location.FORMAT_DEGREES) + " "
                + Location.convert(location.getLongitude(), Location.FORMAT_DEGREES);
    }

    public static Location getLastKnownLocation(LocationManager lm) {
        List<String> providers = lm.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = lm.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null
                    || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }
        if (bestLocation == null) {
            return null;
        }
        return bestLocation;
    }

}
