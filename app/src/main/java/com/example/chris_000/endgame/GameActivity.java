package com.example.chris_000.endgame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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



public class GameActivity extends Activity implements LocationListener, SensorEventListener{

    private ImageView image;
    private float currentDegree = 0f;
    private SensorManager sensorManager;
    TextView txtDegrees;

    float[] inR = new float[16];
    float[] I = new float[16];
    float[] gravity = new float[3];
    float[] geomag = new float[3];
    float[] orientVals = new float[3];

    double azimuth = 0;
    float floatazimuth = 0;

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

        //Setup for location START.
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
        //Setup for location END.

        //Setup for sensor and compass START.
        image = (ImageView) findViewById(R.id.gameImageView);
        txtDegrees = (TextView) findViewById(R.id.gameDegrees);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        //Setup for sensor and compass END.
    }

    @Override
    //Updates GPS location when a new location is registered.
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

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE)
            return;

        // Gets the value of the sensor that has been changed
        switch (sensorEvent.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                gravity = sensorEvent.values.clone();
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                geomag = sensorEvent.values.clone();
                break;
        }

        //If values are not null, then calculate the azimuth
        if (gravity != null && geomag != null) {
            //Create rotation matrix.
            boolean success = SensorManager.getRotationMatrix(inR, I, gravity, geomag);

            if (success) {
                //Use rotation matrix to calculate orientation.
                SensorManager.getOrientation(inR, orientVals);
                //Convert azimuth to degrees.
                azimuth = Math.toDegrees(orientVals[0]);
            }
        }

        //Typecast to float for use in animations.
        floatazimuth = (float)azimuth;

        txtDegrees.setText("Heading: " + Float.toString(floatazimuth) + " degrees");

        //Create a rotation animation (reverse turn degree degrees)
        RotateAnimation ra = new RotateAnimation(
                currentDegree,
                -floatazimuth,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        // how long the animation will take place
        ra.setDuration(210);

        // set the animation after the end of the reservation status
        ra.setFillAfter(true);

        // Start the animation
        image.startAnimation(ra);
        currentDegree = -floatazimuth;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not in use
    }

    @Override
    protected void onResume() {
        super.onResume();
        // for the system's orientation sensor registered listeners
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        // ...and the orientation sensor
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // to stop the listener and save battery
        sensorManager.unregisterListener(this);
    }
}


