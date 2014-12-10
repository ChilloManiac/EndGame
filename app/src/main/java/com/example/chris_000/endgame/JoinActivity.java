package com.example.chris_000.endgame;

import android.app.Activity;
import android.content.Intent;
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


public class JoinActivity extends Activity {

    ServerHandler server;
    String gameName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);



        final Button backButton = (Button) findViewById(R.id.join_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent changeActivity = new Intent(JoinActivity.this, DialogActivity.class);
                JoinActivity.this.startActivity(changeActivity);
            }
        });

        final EditText codeText = (EditText) findViewById(R.id.editText2);
        codeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codeText.setText("");
            }
        });

        final Button submitButton = (Button) findViewById(R.id.join_submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    gameName = codeText.getText().toString();
                    Integer res = new joinGame().execute().get();
                    if(res == 1) {
                        Thread fieldThread = new Thread(getFieldRunnable);
                        fieldThread.start();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    final Runnable getFieldRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                ArrayList<FieldPoint> field = new ArrayList<FieldPoint>();
                JSONArray json = new getField().execute().get();
                if(json != null) {
                    for(int i = 0; i < json.length(); i++) {
                        Double lat = json.getJSONObject(i).getDouble("LAT");
                        Double lon = json.getJSONObject(i).getDouble("LON");
                        FieldPointType fpt = FieldPointType.EMPTY;
                        Integer status = json.getJSONObject(i).getInt("STATUS");
                        switch (status) {
                            case 1 : fpt = FieldPointType.DANGERZONE;
                                break;
                            case 2 : fpt = FieldPointType.PRIMARY_GOAL;
                                break;
                            case 3 : fpt = FieldPointType.SECONDARY_GOAL;
                                break;
                            case 4 : fpt = FieldPointType.PLAYER1_START;
                                break;
                            case 5 : fpt = FieldPointType.PLAYER2_START;
                                break;
                        }
                        FieldPoint fp = new FieldPoint(fpt, lat, lon);
                        field.add(fp);
                    }
                }

                Log.i("Crashandburn", field.toString());

                Intent changeActivity = new Intent(JoinActivity.this, DialogActivity.class);
                changeActivity.putExtra("field",field);
                changeActivity.putExtra("name",gameName);
                changeActivity.putExtra("player","player2");
                JoinActivity.this.startActivity(changeActivity);



            } catch (InterruptedException e) {
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private class joinGame extends AsyncTask<Void, Void, Integer>
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
            params.add(new BasicNameValuePair("tag","joining"));
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

    private class getField extends AsyncTask<Void, Void, JSONArray>
    {

        @Override
        protected void onPreExecute()
        {
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
