package com.firstgpsapp.proudlikeagod.locationbasedadsfinder;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dd.CircularProgressButton;

import org.json.JSONObject;
import org.w3c.dom.Text;

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
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {
   private EditText mUsernameField ;
   private EditText mPasswordField ;
   private Button mSignIn ;
   private ProgressDialog mProgressD ;
   private String mCurrentToken ;
    private String login_url = "http://192.168.43.18:8000/api/login";
   private CircularProgressButton mProgressSignIn ;
   private Animation mShake ;
   private String callingActivity ;
   private TextView mPasswordForgot ,mSignUp ,mErrorHint;
   private int mUserId ;
    public static final int  CONNECTION_TIMEOUT =10000;
    public static final int READ_TIMEOUT =15000 ;
    private int mAdId ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        extractFromBundle();
        determineTheCaller();
        loadWidgets();
        loadAnimations();
        setListeners();



    }
    public void extractFromBundle(){
        Intent i = getIntent();
        Bundle b = new Bundle();
        b = i.getExtras();
        if(b!=null){
            mAdId = b.getInt("Ad ID",0);}



    }
    public void setListeners(){
        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String Username = mUsernameField.getText().toString();
                final String Password = mPasswordField.getText().toString();

               if(Username.matches("")||Password.matches("")){

                   if(Username.matches("")){
                       mUsernameField.startAnimation(mShake);
                   }else mPasswordField.startAnimation(mShake);
               }else {
                   //VolleyAgent.getInstance(getApplicationContext()).login(mUsernameField.getText().toString(),mPasswordField.getText().toString(),callingActivity);
                  login(mUsernameField.getText().toString(),mPasswordField.getText().toString(),callingActivity);


               }

            }
        });

        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this,RegistrationForm.class);
                Bundle b = new Bundle();
                b.putString("calling activity",callingActivity);
                if(mAdId!=0){
                    b.putInt("Ad ID",mAdId);
                }
                i.putExtras(b);
                startActivity(i);


                   }
        });
    /* mProgressSignIn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             if(mProgressSignIn.getProgress()==0){
                 mProgressSignIn.setProgress(30);
             }else if(mProgressSignIn.getProgress()==-1){
                 mProgressSignIn.setProgress(0);
             } else if (mProgressSignIn.getProgress()==100){
                 startActivity(new Intent(MainActivity.this,ContainerActivity.class));
             }
             new Handler().postDelayed(new Runnable() {
                 @Override
                 public void run() {
                     final String Username = mUsernameField.getText().toString();
                     final String Password = mPasswordField.getText().toString();

                     if(Username.matches("")||Password.matches("")){

                         if(Username.matches("")){
                             mUsernameField.startAnimation(mShake);
                             mProgressSignIn.setProgress(0);
                         }else mPasswordField.startAnimation(mShake);
                     mProgressSignIn.setProgress(0);}
                     else {
                         mProgressSignIn.setProgress(100);
                     }

                 }
             },3000);
         }
     });*/





    }
    public void loadWidgets(){
        mUsernameField = (EditText)findViewById(R.id.Username);
        mPasswordField = (EditText)findViewById(R.id.Password);
        mSignIn =(Button)findViewById(R.id.SignIn);

        mSignUp=(TextView)findViewById(R.id.CreateNewAccount);
        mErrorHint =(TextView)findViewById(R.id.ErrorHint);
        mErrorHint.setVisibility(View.INVISIBLE);
       // mProgressSignIn =(CircularProgressButton)findViewById(R.id.ProgressSignIn);
       // mProgressSignIn.setIndeterminateProgressMode(false);


    }
    public void loadAnimations(){

        mShake=AnimationUtils.loadAnimation(this,R.anim.shake);


    }

    public void onResume(){
        super.onResume();
    }
    public void determineTheCaller(){
        Intent i  =getIntent();
        Bundle b =i.getExtras();
        if(b!=null){
        callingActivity =b.getString("calling activity");}
    }

    public void login(final String email , final String password , final String caller){
        StringRequest loginRequest = new StringRequest(Request.Method.POST, login_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                 String s ="";
                try{
                    JSONObject tokenWrapper = new JSONObject(response);
                    s = tokenWrapper.getString("result");

                }catch (Throwable t){
                    Log.e("My App", "Could not parse malformed JSON: \"" + response + "\"");
                }

                    if( s.equals("wrong email or password.")){
                        mErrorHint.setVisibility(View.VISIBLE);
                        dismissProgressDialog();

                    } else {
                        AuthenticationAgent mAgent = new AuthenticationAgent(getApplicationContext());
                        mAgent.saveToken(response);
                        dismissProgressDialog();
                        switch (caller) {
                            case "search fragment":
                                startActivity(new Intent(MainActivity.this, AdCreation.class));
                                finish();
                                break;
                            case "profile settings":
                                startActivity(new Intent(MainActivity.this,ProfileSettingsActivity.class));
                                finish();
                                break;
                            case "Ad Details":
                                Intent i = new Intent(MainActivity.this,AdDetails.class);
                                Bundle b = new Bundle();
                                b.putInt("Ad ID",mAdId);
                                i.putExtras(b);
                                startActivity(i);
                                finish();
                                break;

                            case "profile fragment" :
                                Intent i3 = new Intent(MainActivity.this,ContainerActivity.class);
                                Bundle b3 = new Bundle();
                                b3.putInt("fragment index",2);
                                i3.putExtras(b3);
                                startActivity(i3);
                                finish();
                                break;



                        }


                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissProgressDialog();

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
        VolleyAgent.getInstance(getApplicationContext()).addToRequestQueue(loginRequest);
        showProgressDialog();


    }

    public void showProgressDialog(){
        mProgressD = new ProgressDialog(MainActivity.this);
        mProgressD.setCancelable(false);

        mProgressD.setMessage("Signing you in , hold on ......");
        mProgressD.show();

    }
    public void dismissProgressDialog(){
        mProgressD.dismiss();

    }


   

    }




