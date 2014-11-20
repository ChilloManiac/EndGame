package com.example.chris_000.endgame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.PopupWindow;


public class GameActivity extends Activity implements LocationListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        Button debugWin = (Button) findViewById(R.id.ActivityGameDebugWin);
        debugWin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) GameActivity.this
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                // Setup for popup window
                View popupView = inflater.inflate(R.layout.popup_view, null, false);
                final PopupWindow pw = new PopupWindow(popupView);

                pw.showAtLocation(findViewById(R.id.activityGameLayout), Gravity.CENTER, 0, 0);
                Button close = (Button) popupView.findViewById(R.id.popUpButton);
                close.setOnClickListener(new View.OnClickListener() {
                    //onClick listener for popup window
                    public void onClick(View popupView) {
                        pw.dismiss();
                        Intent changeActivity = new Intent(GameActivity.this, DialogActivity.class);
                        GameActivity.this.startActivity(changeActivity);
                    }
                });

            }
        });

        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        String locationProvider = LocationManager.GPS_PROVIDER;

        // Get last known location
        Location location = locationManager.getLastKnownLocation(locationProvider);
        if(location!=null){
            onLocationChanged(location);
        }

        // Request location updates
        locationManager.requestLocationUpdates(locationProvider, 0, 0, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.v("Location changed","Entered onLocationChanged");

        //Convert location to string
        if (location != null) {
            LocationHandler locationHandler = new LocationHandler();
            String lastLocationString = LocationHandler.locationStringFromLocation(location);

            //Display location (for debug)
            TextView gameTextView = (TextView)findViewById(R.id.gameTextView); //debug
            gameTextView.setText(lastLocationString);  //debug
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
    @Override
    public void onProviderEnabled(String provider) {}
    @Override
    public void onProviderDisabled(String provider) {}
}

