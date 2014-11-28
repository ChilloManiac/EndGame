package com.example.chris_000.endgame;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class HostActivity extends Activity implements LocationListener{

    ServerHandler server;
    String gameName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);

        final Button hostButton = (Button) findViewById(R.id.host_back_button);
        hostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent changeActivity = new Intent(HostActivity.this, InitActivity.class);
                HostActivity.this.startActivity(changeActivity);
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

        EditText HostEditText = (EditText)findViewById(R.id.HosteditText);
        try {
            HostEditText.setText(new getHostName().execute().get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Runnable joinRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    while(new getJoined().execute().get() != 1) {
                        Thread.sleep(5000);
                    }
                    Intent changeActivity = new Intent(HostActivity.this, DialogActivity.class);
                    HostActivity.this.startActivity(changeActivity);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread joinThread = new Thread(joinRunnable);
        joinThread.start();

    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            Log.v("Location Changed", location.getLatitude() + " " + location.getLongitude());
        }

        if (location == null) {
            Log.v("Failed","Location is null");
        }

        //Convert location to string
        if (location != null) {
            LocationHandler locationHandler = new LocationHandler();
            String lastLocationString = LocationHandler.locationStringFromLocation(location);

            //EditText HostEditText = (EditText)findViewById(R.id.HosteditText); //debug
            //HostEditText.setText(lastLocationString);  //debug
        }

        //TODO
        //Send location to server.
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}

    private class getHostName extends AsyncTask<Void, Void, String>
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids)
        {
            server = new ServerHandler();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("tag","create"));
            JSONObject hostJson = null;
            try {
                hostJson = server.connect(params);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                gameName = hostJson.getString("NAME");
                return gameName;
            } catch (Exception e) {
                return "Error";
            }

        }
        @Override
        protected void onPostExecute(String results) {

        };
    }

    private class getJoined extends AsyncTask<Void, Void, Integer>
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Void... voids)
        {
            server = new ServerHandler();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("tag","getJoined"));
            params.add(new BasicNameValuePair("name",gameName));
            JSONArray hostJson = null;
            try {
                    hostJson = server.connectArray(params);

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                return hostJson.getJSONObject(0).getInt("Player2_Joined");
            } catch (Exception e) {
                return 0;
            }
        }
    }





}
