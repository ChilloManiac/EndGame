package com.example.chris_000.endgame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.hardware.GeomagneticField;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class GameActivity extends Activity implements LocationListener, SensorEventListener {

    private ImageView image;
    private float currentDegree = 0f;
    private TextView txtDegrees;
    private LocationManager locationManager;
    private SensorManager sensorManager;
    private String locationProvider;
    private Location currentLocation;

    Location waypoint = new Location("");

    float dist = -1;
    ArrayList<FieldPoint> field = null;
    private ServerHandler server;
    private String gameName;
    private String player;

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

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            field = (ArrayList<FieldPoint>) extras.get("field");
            gameName = (String) extras.get("name");
            player = (String) extras.get("player");
        }

        for (FieldPoint fp : field) {
            if (fp.getStatus() == FieldPointType.PRIMARY_GOAL) {
                waypoint.setLongitude(fp.getLongitude());
                waypoint.setLatitude(fp.getLatitude());
            }
        }
        image = (ImageView) findViewById(R.id.gameImageView);
        image.getDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationProvider = LocationManager.GPS_PROVIDER;
        locationManager.requestLocationUpdates(locationProvider, 1000, 5, this);


        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        Location location = locationManager.getLastKnownLocation(locationProvider);
        if (location != null) {
            onLocationChanged(location);
        } else {
            onLocationChanged(LocationHandler.getLastKnownLocation(locationManager));
        }

        Runnable getWonRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    while (new getWon().execute().get() != 1) {
                        Thread.sleep(2000);
                    }
                    Intent changeActivity = new Intent(GameActivity.this, DialogActivity.class);
                    changeActivity.putExtra("name", gameName);
                    changeActivity.putExtra("won", false);
                    changeActivity.putExtra("field", field);
                    changeActivity.putExtra("player", player);
                    GameActivity.this.startActivity(changeActivity);

                } catch (InterruptedException e) {
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread wonThread = new Thread(getWonRunnable);
        wonThread.start();

        Button debugWin = (Button) findViewById(R.id.ActivityGameDebugWin);
        debugWin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View popupView) {
                handleIWon();
            }
        });

    }

    @Override
    public void onLocationChanged(Location location) {
        Location locationPoint = new Location("");

        if (location != null) {
            LocationHandler locationHandler = new LocationHandler();
            String lastLocationString = locationHandler.locationStringFromLocation(location);

            if (field != null) {
                for (FieldPoint fp : field) {
                    locationPoint.setLatitude(fp.getLatitude());
                    locationPoint.setLongitude(fp.getLongitude());
                    double dist = locationPoint.distanceTo(location);

                    if (dist <= 8) {
                        image.getDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY);

                        if (fp.getStatus() == FieldPointType.PRIMARY_GOAL) {
                            handleIWon();
                        } else if (fp.getStatus() == FieldPointType.SECONDARY_GOAL){
                            image.getDrawable().setColorFilter(Color.YELLOW, PorterDuff.Mode.MULTIPLY);
                        } else if (fp.getStatus() == FieldPointType.DANGERZONE) {
                            image.getDrawable().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        }
                    }
                }
            }

            //Current location for compass
            currentLocation = location;

            //Display location (for debug)
            TextView gameTextView = (TextView) findViewById(R.id.gameTextView); //debug
            gameTextView.setText(lastLocationString);  //debug

            dist = location.distanceTo(waypoint);
        }
    }

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
        GeomagneticField geoField = new GeomagneticField(
                (float) currentLocation.getLatitude(),
                (float) currentLocation.getLongitude(),
                (float) currentLocation.getAltitude(),
                System.currentTimeMillis());
        floatazimuth += geoField.getDeclination(); // converts magnetic north into true north

        float bearing = currentLocation.bearingTo(waypoint); // (it's already in degrees)
        float direction = floatazimuth - bearing;

        //Create a rotation animation (reverse turn degree degrees)
        RotateAnimation ra = new RotateAnimation(
                currentDegree,
                -direction,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        // how long the animation will take place
        ra.setDuration(210);

        // set the animation after the end of the reservation status
        ra.setFillAfter(true);

        // Start the animation
        image.startAnimation(ra);
        currentDegree = -direction;

        txtDegrees = (TextView) findViewById(R.id.gameDegrees);
        txtDegrees.setText("Heading: " + String.valueOf(direction) + " degrees, "
                + String.valueOf(dist) + "m");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not in use
    }

    private void handleIWon() {
        new iWon().execute();

        //I won, display victory popup window, and switch to dialogActivity
        LayoutInflater inflater = (LayoutInflater) GameActivity.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Setup for popup window
        View popupView = inflater.inflate(R.layout.popup_view, null, false);
        final PopupWindow pw = new PopupWindow(popupView,
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        Button close = (Button) popupView.findViewById(R.id.popUpButton);
        close.setOnClickListener(new View.OnClickListener() {
            //onClick listener for popup window
            public void onClick(View popupView) {
                pw.dismiss();
                Intent changeActivity = new Intent(GameActivity.this, DialogActivity.class);
                changeActivity.putExtra("won", true);
                changeActivity.putExtra("name", gameName);
                changeActivity.putExtra("field", field);
                changeActivity.putExtra("player", player);
                GameActivity.this.startActivity(changeActivity);
            }
        });
        pw.showAtLocation(popupView, Gravity.CENTER, 0 ,0);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    protected void onResume() {
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_NORMAL);

        locationManager.requestLocationUpdates(locationProvider, 1000, 5, this);

        super.onResume();
    }

    @Override
    protected void onPause() {
        //stop the listener and save battery
        sensorManager.unregisterListener(this);
        locationManager.removeUpdates(this);
        super.onPause();
    }

    private class getWon extends AsyncTask<Void, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            server = new ServerHandler();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("tag", "getWon"));
            params.add(new BasicNameValuePair("name", gameName));
            String opponent = "";
            if (player.equals("player1")) {
                opponent = "player2";
            } else if (player.equals("player2")) {
                opponent = "player1";
            }
            params.add(new BasicNameValuePair("player", opponent));
            JSONArray hostJson = null;
            try {
                hostJson = server.connectArray(params);
            } catch (Exception e) {
                return 0;
            }

            try {
                return hostJson.getJSONObject(0).getInt(opponent);
            } catch (Exception e) {
                return 0;
            }
        }
    }

    private class iWon extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            server = new ServerHandler();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("tag", "winning"));
            params.add(new BasicNameValuePair("name", gameName));
            params.add(new BasicNameValuePair("player", player));
            JSONArray hostJson = null;
            try {
                hostJson = server.connectArray(params);
            } catch (Exception e) {

            }
            return null;


        }
    }

}


