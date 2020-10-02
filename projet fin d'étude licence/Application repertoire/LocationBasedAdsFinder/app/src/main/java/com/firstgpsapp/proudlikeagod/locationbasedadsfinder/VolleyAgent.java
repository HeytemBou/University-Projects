package com.firstgpsapp.proudlikeagod.locationbasedadsfinder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.toolbox.Volley.newRequestQueue;

/**
 * Created by ProudLikeAGod on 24/03/2018.
 */

class VolleyAgent extends AppCompatActivity {
    private static VolleyAgent mInstance;
    private RequestQueue mRequestQueue;
    private static Context mContext;
    private String mCurrentToken ;

    private String login_url = "http://192.168.1.3:8000/api/login";
    private String register_url ="http://192.168.1.3:8000/api/register";

    private VolleyAgent(Context context){
        // Specify the application context
        mContext = context;
        // Get the request queue
        mRequestQueue = getRequestQueue();
    }

    public static synchronized VolleyAgent getInstance(Context context){
        // If Instance is null then initialize new Instance
        if(mInstance == null){
            mInstance = new VolleyAgent(context);
        }
        // Return MySingleton new Instance
        return mInstance;
    }

    public RequestQueue getRequestQueue(){
        // If RequestQueue is null the initialize new RequestQueue
        if(mRequestQueue == null){
            mRequestQueue = newRequestQueue(mContext.getApplicationContext());
        }

        // Return RequestQueue
        return mRequestQueue;
    }

    public<T> void addToRequestQueue(Request<T> request){
        // Add the specified request to the request queue
        getRequestQueue().add(request);
    }


        public void registerAccount(final String username, final String password , final String email ,final String caller ){

            StringRequest createNewAccountRequest =  new StringRequest(Request.Method.POST,register_url,new Response.Listener<String>(){
                public void onResponse(String response){
                    Toast.makeText(mContext,response,Toast.LENGTH_LONG).show();
                    Log.e("first reponse",response);
                    StringRequest loginRequest = new StringRequest(Request.Method.POST, login_url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Toast.makeText(mContext, response, Toast.LENGTH_LONG).show();
                                Log.e("second response",response);
                                JSONObject j = new JSONObject(response);
                                AuthenticationAgent mAgent = new AuthenticationAgent(mContext);
                                mCurrentToken = j.getString("result");
                                Toast.makeText(mContext, mCurrentToken, Toast.LENGTH_LONG).show();
                                Log.e("third response",mCurrentToken);
                                mAgent.saveToken(mCurrentToken);
                                startActivity(new Intent(VolleyAgent.this, AdCreation.class));
                            }

                            catch (Throwable t){
                                Log.e("exception",t.getMessage());

                            }
                          /*  switch (caller){
                                case "search fragment" :
                                    startActivity(new Intent(VolleyAgent.this,AdCreation.class));
                                    break ;
                                case "profile fragment":
                                    break ;
                                case "ad details":
                                    break ;
                            }*/


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> params = new HashMap<String,String>();
                            params.put("email",email);
                            params.put("password",password);
                            return params;
                        }
                      /*  public Map<String, String> getHeaders() throws AuthFailureError {
                            HashMap<String, String> headers = new HashMap<String, String>();
                            headers.put("token",mCurrentToken);
                            headers.put("Content-Type", "application/json");
                            return headers;
                        }/*/

                    };
                    mRequestQueue.add(loginRequest);

                }


            }
                    ,new Response.ErrorListener(){
                public void onErrorResponse(VolleyError volleyError){

                    Toast.makeText(mContext,"An error occured on our side , please try again",Toast.LENGTH_SHORT).show();


                }
            }){
                protected Map<String,String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String,String>();
                    params.put("name",username);
                    params.put("password",password);
                    params.put("email",email);
                    return params ;



                }



            };
            addToRequestQueue(createNewAccountRequest);




        }

        public void login(final String email , final String password , final String caller){
            StringRequest loginRequest = new StringRequest(Request.Method.POST, login_url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject j = new JSONObject(response);
                        AuthenticationAgent mAgent = new AuthenticationAgent(mContext);
                        mCurrentToken = j.getString("result");
                        mAgent.saveToken(mCurrentToken);
                        startActivity(new Intent(VolleyAgent.this,AdCreation.class));

                    }
                    catch (Throwable t){

                    }
                 /*   switch (caller){
                        case "search fragment" :
                            startActivity(new Intent(VolleyAgent.this,AdCreation.class));
                            break ;
                        case "profile fragment":

                            break ;
                        case "ad details":

                            break ;
                    }*/


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String,String>();
                    params.put("email",email);
                    params.put("password",password);
                    return params;
                }

            };
            mRequestQueue.add(loginRequest);


        }

        public void getProfileInfos(){

        }
    }


