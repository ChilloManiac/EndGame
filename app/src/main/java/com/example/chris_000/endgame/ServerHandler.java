package com.example.chris_000.endgame;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ServerHandler {

    String URLString;

    public ServerHandler() {
        URLString = "http://194.239.204.235/index.php";
    }

    public ServerHandler(String url) {
        URLString = url;
    }

    public JSONObject connect(List<NameValuePair> params) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(URLString);
        HttpResponse response = null;

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            response = httpClient.execute(httpPost);


        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        InputStream is = null;
        try {
            is = response.getEntity().getContent();

        } catch (IOException e) {
            e.printStackTrace();
        }

        String res = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = null;
            res = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            is.close();
            res = sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject jObj = null;
        try {
            jObj = new JSONObject(res);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jObj;
    }

    public JSONArray connectArray(List<NameValuePair> params) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(URLString);
        HttpResponse response = null;

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            response = httpClient.execute(httpPost);


        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        InputStream is = null;
        try {
            is = response.getEntity().getContent();

        } catch (IOException e) {
            e.printStackTrace();
        }

        String res = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = null;
            res = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            is.close();
            res = sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONArray jArr = null;
        try {
            jArr = new JSONArray(res);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jArr;
    }

}
