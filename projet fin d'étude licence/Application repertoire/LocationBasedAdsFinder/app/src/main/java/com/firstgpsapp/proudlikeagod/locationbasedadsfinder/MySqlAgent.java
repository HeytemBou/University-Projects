package com.firstgpsapp.proudlikeagod.locationbasedadsfinder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;



/**
 * Created by ProudLikeAGod on 15/02/2018.
 */

public class MySqlAgent extends AsyncTask<String, String, String>  {
    private String RequestType ;
    HttpURLConnection conn;
    URL url = null;
    private Context context ;
    protected String doInBackground(String... params){
        RequestType = params[0];

        switch(RequestType){
            case"CreerCompte" :
                try {

                    // Enter URL address where your php file resides
                    url = new URL("http://192.168.1.5:80/LocationBasedAppScripts/SignUpScript.php");

                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return "exception";
                }
                try {

                    conn = (HttpURLConnection)url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("Username", params[1])
                            .appendQueryParameter("Password", params[2])
                            .appendQueryParameter("Email",params[3]);
                    if(params.length==5){
                        builder.appendQueryParameter("Telephone",params[4]);
                    }
                    String query = builder.build().getEncodedQuery();

                    // Open connection for sending data
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(query);
                    writer.flush();
                    writer.close();
                    os.close();
                    conn.connect();

                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                    return "exception";
                }

                try {

                    int response_code = conn.getResponseCode();

                    if (response_code == HttpURLConnection.HTTP_OK) {


                        InputStream input = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                        StringBuilder result = new StringBuilder();
                        String line;

                        while ((line = reader.readLine()) != null) {
                            result.append(line);
                        }

                        return(result.toString());

                    }else{

                        return("unsuccessful");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    return "exception";
                } finally {
                    conn.disconnect();
                }


                default:
                    return null ;



        }
    }


        @Override
        protected void onPostExecute(String result) {


            if(result.equalsIgnoreCase("success"))
            {


            }else if(result.equalsIgnoreCase("username already in use")){

            }


}
    }

