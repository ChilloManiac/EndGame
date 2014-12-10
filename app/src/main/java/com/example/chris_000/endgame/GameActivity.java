package com.example.chris_000.endgame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
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

import java.util.ArrayList;

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
    ArrayList<FieldPoint> field = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //get playing field points
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            field = (ArrayList<FieldPoint>) extras.get("field");
        }

        //get waypoint for end goal
        for (FieldPoint fp :  field) {
            if(fp.getStatus() == FieldPointType.PRIMARY_GOAL){
                waypoint.setLongitude(fp.getLongitude());
                waypoint.setLatitude(fp.getLatitude());
            }
        }

        // *** temp debug win button ***
        Button debugWin = (Button) findViewById(R.id.ActivityGameDebugWin);
        debugWin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) GameActivity.this
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                // Setup for popup window
                View popupView = inflater.inflate(R.layout.popup_view, null, false);
                final PopupWindow pw = new PopupWindow(popupView);
                pw.showAtLocation(findViewById(R.id.activityGameLayout), Gravity.CENTER, 0, 0);
                pw.update(0, 0, 1000, 1000);

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
        // *** temp debug win button ***

        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        //Select gps provider.
        String locationProvider = LocationManager.GPS_PROVIDER;

        // Request location updates
        locationManager.requestLocationUpdates(locationProvider, 0, 0, this);

        // Get last known location
        Location location = locationManager.getLastKnownLocation(locationProvider);
        if(location!=null){
            onLocationChanged(location);
        }else{
            onLocationChanged(LocationHandler.getLastKnownLocation(locationManager));
        }

        // ImageView reference.
        image = (ImageView) findViewById(R.id.gameImageView);
        image.getDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY);
    }

    @Override
    //Updates location when a new location is registered.
    public void onLocationChanged(Location location) {
        //Convert location to string
        Location locationPoint = null;

        if (location != null) {
            LocationHandler locationHandler = new LocationHandler();
            String lastLocationString = locationHandler.locationStringFromLocation(location);

            //Check location in relation to field points
            if (field != null) {
                for (FieldPoint fp :  field) {
                    locationPoint.setLatitude(fp.getLatitude());
                    locationPoint.setLongitude(fp.getLongitude());
                    //Check distance between current location and
                    double dist = locationPoint.distanceTo(location);

                    if(dist <= 3){
                        if(fp.getStatus() == FieldPointType.DANGERZONE){
                            image.getDrawable().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        }
                        else image.getDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY);

                        if(fp.getStatus() == FieldPointType.PRIMARY_GOAL){
                            //todo tell server i won.


                            //I won, display victory popup window, and switch to dialogActivity
                            LayoutInflater inflater = (LayoutInflater) GameActivity.this
                                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                            // Setup for popup window
                            View popupView = inflater.inflate(R.layout.popup_view, null, false);
                            final PopupWindow pw = new PopupWindow(popupView);
                            pw.showAtLocation(findViewById(R.id.activityGameLayout), Gravity.CENTER, 0, 0);
                            pw.update(0, 0, 1000, 1000);

                            Button close = (Button) popupView.findViewById(R.id.popUpButton);
                            close.setOnClickListener(new View.OnClickListener() {
                                //onClick listener for popup window
                                public void onClick(View popupView) {
                                    pw.dismiss();
                                    Intent changeActivity = new Intent(GameActivity.this, DialogActivity.class);
                                    boolean iWon = true;
                                    changeActivity.putExtra("iWon",iWon);
                                    GameActivity.this.startActivity(changeActivity);
                                }
                            });
                        }
                    }
                }
            }

            //Display location (for debug)
            TextView gameTextView = (TextView)findViewById(R.id.gameTextView); //debug
            gameTextView.setText(lastLocationString);  //debug

            dist = location.distanceTo(waypoint);
            bearing = location.bearingTo(waypoint);    // -180 to 180
            myHeading = location.getBearing();         // 0 to 360

            // Calculate where the arrow should point
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

            // Start the animation
            image.startAnimation(ra);

            Log.i("currentDegree", Float.toString(currentDegree)); //debug
            Log.i("-arrow_rotation", Float.toString(-arrow_rotation)); //debug

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


