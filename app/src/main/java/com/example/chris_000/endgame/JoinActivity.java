package com.example.chris_000.endgame;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
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
                    Integer res = new joinGame().execute(codeText.getText().toString()).get();
                    if(res == 1) {
                        Intent changeActivity = new Intent(JoinActivity.this, DialogActivity.class);
                        JoinActivity.this.startActivity(changeActivity);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private class joinGame extends AsyncTask<String, Void, Integer>
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... strings)
        {
            server = new ServerHandler();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("tag","joining"));
            params.add(new BasicNameValuePair("name",strings[0]));
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
