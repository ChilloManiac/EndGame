package com.example.chris_000.endgame;

import android.location.Location;

import java.io.Serializable;

/**
 * Created by Chris_000 on 04-12-2014.
 */
public class FieldPoint implements Serializable {
    private final Double lon;
    private final Double lat;
    private FieldPointType status;

    public FieldPoint(FieldPointType status, Double lat, Double lon) {
        this.status = status;
        this.lon = lon;
        this.lat = lat;
    }

    public FieldPointType getStatus() {
        return status;
    }

    public Double getLatitude() {
        return lat;
    }

    public Double getLongitude() {
        return lon;
    }

    @Override
    public String toString() {
        return lat + " : " + lon  + " : " + status;
    }

}
