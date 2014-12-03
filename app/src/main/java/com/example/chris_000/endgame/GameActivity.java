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
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.PopupWindow;


public class GameActivity extends Activity implements LocationListener {

    private ImageView image;
    private float currentDegree = 0f;
    TextView txtDegrees;

    Location waypoint = new Location("");

    float dist = -1;
    float bearing = 0;
    float myHeading = 0;
    float arrow_rotation = 0;
    float arrow_rotationOld = 0;

    //debug goal
    double latitude = 56.171986;
    double longitude = 10.189495;

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

        //Select gps provider.
        String locationProvider = LocationManager.GPS_PROVIDER;

        //Select best provider.
        //Criteria criteria = new Criteria();
        //criteria.setAccuracy(Criteria.ACCURACY_FINE);
        //String locationProvider = locationManager.getBestProvider(criteria, true);

        // Request location updates
        locationManager.requestLocationUpdates(locationProvider, 0, 0, this);

        // Get last known location
        Location location = locationManager.getLastKnownLocation(locationProvider);
        if(location!=null){
            onLocationChanged(location);
        }
    }

    @Override
    //Updates location when a new location is registered.
    public void onLocationChanged(Location location) {
        //Convert location to string
        if (location != null) {
            LocationHandler locationHandler = new LocationHandler();
            String lastLocationString = LocationHandler.locationStringFromLocation(location);

            //Display location (for debug)
            TextView gameTextView = (TextView)findViewById(R.id.gameTextView); //debug
            gameTextView.setText(lastLocationString);  //debug

            waypoint.setLongitude(longitude);    //Cursor is from SimpleCursorAdapter
            waypoint.setLatitude(latitude);
            dist = location.distanceTo(waypoint);
            bearing = location.bearingTo(waypoint);    // -180 to 180
            myHeading = location.getBearing();         // 0 to 360

            // *** Code to calculate where the arrow should point ***
            arrow_rotation = (bearing - myHeading) * -1;
            if(arrow_rotation < 0){
                arrow_rotation = 180 + (180 + arrow_rotation);
            }

            Log.i("rotation", Float.toString(arrow_rotation)); //debug
            Log.i("distance", Float.toString(dist)); //debug

            //Animate compass with bearing to the waypoint.
            RotateAnimation ra = new RotateAnimation(currentDegree, -arrow_rotation,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);

            // how long the animation will take place
            ra.setDuration(210);

            // set the animation after the end of the reservation status
            ra.setFillAfter(true);


            // ImageView reference.
            image = (ImageView) findViewById(R.id.gameImageView);
            // Start the animation
            image.startAnimation(ra);

            Log.i("currentDegree", Float.toString(currentDegree));
            Log.i("-arrow_rotation", Float.toString(-arrow_rotation));

            currentDegree = -arrow_rotation;

            //Update old azimuth value
            arrow_rotationOld = arrow_rotation;

            //debug
            //TextView reference.
            txtDegrees = (TextView) findViewById(R.id.gameDegrees);
            txtDegrees.setText("Waypoint bearing: " +String.valueOf(arrow_rotation) + " degrees, "
            + String.valueOf(dist) + "m");
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
    @Override
    public void onProviderEnabled(String provider) {}
    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}


