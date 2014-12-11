package com.example.chris_000.endgame;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class HostActivity extends Activity implements LocationListener {

    ServerHandler server;
    String gameName = "";
    Thread joinThread;
    Location currLoc;

    public void setEditText(String set) {
        EditText HostEditText = (EditText) findViewById(R.id.HosteditText);
        HostEditText.setText(set);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        String locationProvider = LocationManager.GPS_PROVIDER;

        Location location = locationManager.getLastKnownLocation(locationProvider);
        if (location != null) {
            onLocationChanged(location);
        } else {
            onLocationChanged(LocationHandler.getLastKnownLocation(locationManager));
        }

        locationManager.requestLocationUpdates(locationProvider, 0, 0, this);

        Runnable createRunnable = new Runnable() {

            @Override
            public void run() {
                if (currLoc == null) {
                    setEditText("No Connection to GPS");
                } else {
                    try {
                        setEditText(new getHostName().execute().get());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        runOnUiThread(createRunnable);

        final Runnable getFieldRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    ArrayList<FieldPoint> field = new ArrayList<FieldPoint>();
                    JSONArray json = new getField().execute().get();
                    if (json != null) {
                        for (int i = 0; i < json.length(); i++) {
                            Double lat = json.getJSONObject(i).getDouble("LAT");
                            Double lon = json.getJSONObject(i).getDouble("LON");
                            FieldPointType fpt = FieldPointType.EMPTY;
                            Integer status = json.getJSONObject(i).getInt("STATUS");
                            switch (status) {
                                case 1:
                                    fpt = FieldPointType.DANGERZONE;
                                    break;
                                case 2:
                                    fpt = FieldPointType.PRIMARY_GOAL;
                                    break;
                                case 3:
                                    fpt = FieldPointType.SECONDARY_GOAL;
                                    break;
                                case 4:
                                    fpt = FieldPointType.PLAYER1_START;
                                    break;
                                case 5:
                                    fpt = FieldPointType.PLAYER2_START;
                                    break;
                            }
                            FieldPoint fp = new FieldPoint(fpt, lat, lon);
                            field.add(fp);
                        }
                    }

                    Intent changeActivity = new Intent(HostActivity.this, DialogActivity.class);
                    changeActivity.putExtra("field", field);
                    changeActivity.putExtra("name", gameName);
                    changeActivity.putExtra("player", "player1");
                    HostActivity.this.startActivity(changeActivity);


                } catch (InterruptedException e) {
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Runnable joinRunnable = new Runnable() {
            @Override
            public void run() {
                try {

                    while (new getJoined().execute().get() != 1) {
                        Thread.sleep(2000);
                    }
                    Thread fieldThread = new Thread(getFieldRunnable);
                    fieldThread.start();


                } catch (InterruptedException e) {
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        };
        joinThread = new Thread(joinRunnable);
        joinThread.start();
    }

    private ArrayList<NameValuePair> getLatLon(double lat0, double lon0) {
        ArrayList res = new ArrayList<NameValuePair>();
        double dNE = 75;
        double lat1 = lat0 + dNE / 6378137 * (180 / Math.PI);
        double lon1 = lon0 + dNE / (6378137 * Math.cos(Math.PI * lat0 / 180)) * (180 / Math.PI);
        res.add(new BasicNameValuePair("lat0", String.valueOf(lat0)));
        res.add(new BasicNameValuePair("lat1", String.valueOf(lat1)));
        res.add(new BasicNameValuePair("lon0", String.valueOf(lon0)));
        res.add(new BasicNameValuePair("lon1", String.valueOf(lon1)));
        return res;
    }

    @Override
    protected void onPause() {
        super.onPause();
        joinThread.interrupt();
    }

    @Override
    public void onLocationChanged(Location location) {

        //Convert location to string
        if (location != null) {
            currLoc = location;
        }

        //Send location to server.
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

    private class getHostName extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            server = new ServerHandler();
            List<NameValuePair> params = getLatLon(currLoc.getLatitude(), currLoc.getLongitude());
            params.add(new BasicNameValuePair("tag", "create"));
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

        }
    }

    private class getJoined extends AsyncTask<Void, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            server = new ServerHandler();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("tag", "getJoined"));
            params.add(new BasicNameValuePair("name", gameName));
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

    private class getField extends AsyncTask<Void, Void, JSONArray> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONArray doInBackground(Void... voids) {
            server = new ServerHandler();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("tag", "getField"));
            params.add(new BasicNameValuePair("name", gameName));
            JSONArray hostJson = null;
            try {
                hostJson = server.connectArray(params);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                return hostJson;
            } catch (Exception e) {
                return null;
            }
        }
    }


}
