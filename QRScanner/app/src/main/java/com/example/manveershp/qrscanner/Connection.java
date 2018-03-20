package com.example.manveershp.qrscanner;

/**
 * Created by spark on 20/3/18.
 */

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by radhe on 26/2/17.
 */

public class Connection  {

    public Connection(){

    }
    public JSONObject makeHttpRequest(String url, String method, HashMap<String,Object > params) throws IOException {
        JSONObject jobj = new JSONObject();
        StringBuilder response = new StringBuilder();
        String t= "";
        try{
            if(method.equals("GET")) {
                URL httpurl = new URL(url);
                HttpURLConnection urlConnection = (HttpURLConnection) httpurl.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoInput(true);
                urlConnection.connect();

                try {
                    BufferedReader breader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    String ans = "";
                    while ((ans=breader.readLine())!=null){
                        response.append(ans);
                    }
                    breader.close();

                    t = new String(response);
                    jobj = new JSONObject(t);
                }
                catch (IOException e){
                    e.printStackTrace();
                }
                finally {
                    if (urlConnection!=null){
                        urlConnection.disconnect();
                    }
                }
            }
            if(method.equals("POST")){

                StringBuilder postData = new StringBuilder();
                for (HashMap.Entry<String,Object> param : params.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                byte[] postDataBytes = postData.toString().getBytes("UTF-8");

                URL httpurl = new URL(url);
                HttpURLConnection conn = (HttpURLConnection)httpurl.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
                conn.setDoOutput(true);
                conn.getOutputStream().write(postDataBytes);

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                BufferedReader breader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String ans = "";
                while ((ans=breader.readLine())!=null){
                    response.append(ans);
                }
                breader.close();
                Log.d("XXXXX",response.toString());
                t = new String(response);
                Log.d("response",t);
                jobj = new JSONObject(t);

                return jobj ;




            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return jobj;
    }
}
