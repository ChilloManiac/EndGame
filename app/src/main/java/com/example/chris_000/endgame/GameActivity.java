package com.example.chris_000.endgame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
    private ServerHandler server;
    private String gameName;
    private String player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //get playing field points
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            field = (ArrayList<FieldPoint>) extras.get("field");
            gameName = (String) extras.get("name");
            player = (String) extras.get("player");
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


        // ImageView reference.
        image = (ImageView) findViewById(R.id.gameImageView);
        image.getDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY);

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



        Runnable wonRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    while(new getWon().execute().get() != 1) {
                        Thread.sleep(2000);
                    }
                    Intent changeActivity = new Intent(GameActivity.this, DialogActivity.class);
                    changeActivity.putExtra("name",gameName);
                    changeActivity.putExtra("won", false);
                    GameActivity.this.startActivity(changeActivity);

                } catch (InterruptedException e) {
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread wonThread = new Thread(wonRunnable);
        wonThread.start();


    }

    @Override
    //Updates location when a new location is registered.
    public void onLocationChanged(Location location) {
        //Convert location to string
        Location locationPoint = new Location("");

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
                                    changeActivity.putExtra("won",true);
                                    changeActivity.putExtra("name",gameName);
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

    private class getWon extends AsyncTask<Void, Void, Integer>
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            server = new ServerHandler();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("tag", "getWon"));
            params.add(new BasicNameValuePair("name", gameName));
            String opponent = "";
            if(player.equals("player1")) {
                opponent = "player2";
            } else if(player.equals("player2")) {
                opponent = "player1";
            }
            params.add(new BasicNameValuePair("player", opponent));
            Log.i("FIELDFIELDFIELD", params.toString());
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
}


