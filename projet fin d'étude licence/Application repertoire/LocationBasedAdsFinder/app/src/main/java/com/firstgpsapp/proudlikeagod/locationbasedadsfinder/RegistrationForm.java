package com.firstgpsapp.proudlikeagod.locationbasedadsfinder;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
 * Created by ProudLikeAGod on 15/02/2018.
 */

public class RegistrationForm extends AppCompatActivity {
    private EditText mUsername_field ;
    private EditText mPassword_field ;
    private EditText mEmail_field ;
    private EditText mTelephone_field ;
    private Button   mSubmit ;
    private ProgressDialog mProgressD ;
    private Animation right_to_left , left_to_right ;
    private TextView mUsernameLabel , mPasswordLabel ,mEmailLabel , mTelephoneLabel ;
    private Context mContext ;
    private String callingActivity ;
    private String mCurrentToken ;
    private TextView mEmailUsedErrorHint , mUsernameErrorHint , mPasswordErrorHint , mEmailErrorHint ;
    private int mAdId ;
    private RequestQueue mRequestQueue ;
    private String register_url = "http://192.168.43.18:8000/api/register";
    private String login_url = "http://192.168.43.18:8000/api/login";

    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.registration_from);

        determineTheCaller();
        loadWidgets();
        loadAnimations();
        setOnClickListeners();
        setOnTextWatchers();
    }

    public void loadWidgets(){
        mContext = getApplicationContext();
        mUsername_field = (EditText)findViewById(R.id.Username);
        mPassword_field=(EditText)findViewById(R.id.Password);
        mEmail_field=(EditText)findViewById(R.id.Email);
       // mTelephone_field=(EditText)findViewById(R.id.Telephone);
        mSubmit=(Button) findViewById(R.id.CreateAccount);
        mUsernameLabel=(TextView)findViewById(R.id.UsernameLabel);
        mPasswordLabel=(TextView)findViewById(R.id.PasswordLabel);
       // mTelephoneLabel=(TextView)findViewById(R.id.TelephoneLabel);
        mEmailLabel=(TextView)findViewById(R.id.EmailLabel);
        mEmailUsedErrorHint = findViewById(R.id.emailUsedErrorHint);
        mEmailUsedErrorHint.setVisibility(View.INVISIBLE);
        mUsernameErrorHint = findViewById(R.id.usernameErrorHint);
        mUsernameErrorHint.setVisibility(View.INVISIBLE);
        mPasswordErrorHint = findViewById(R.id.passwordErrorHint);
        mPasswordErrorHint.setVisibility(View.INVISIBLE);
        mEmailErrorHint=findViewById(R.id.emailErrorHint);
        mEmailErrorHint.setVisibility(View.INVISIBLE);
        mSubmit.setEnabled(false);
        mSubmit.setAlpha(0.4f);

    }

    public void loadAnimations(){
        right_to_left= AnimationUtils.loadAnimation(this,R.anim.right_to_left);
        left_to_right=AnimationUtils.loadAnimation(this,R.anim.left_to_right);
        mUsername_field.setAnimation(right_to_left);
        mPassword_field.setAnimation(right_to_left);
        mEmail_field.setAnimation(right_to_left);
       // mTelephone_field.setAnimation(right_to_left);
        mUsernameLabel.setAnimation(left_to_right);
        mPasswordLabel.setAnimation(left_to_right);
        //mTelephoneLabel.setAnimation(left_to_right);
        mEmailLabel.setAnimation(left_to_right);

    }

    public void setOnTextWatchers(){
        mUsername_field.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ValidateRegistrationFields();
                if(!ValidateUsername()){
                    mUsernameErrorHint.setVisibility(View.VISIBLE);
                }else {
                    mUsernameErrorHint.setVisibility(View.INVISIBLE);
                }
                if(mUsername_field.getText().toString().equals("")){
                    mUsernameErrorHint.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mPassword_field.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ValidateRegistrationFields();
                 if(!ValidatePassword()){
                 mPasswordErrorHint.setVisibility(View.VISIBLE);
                 }else {
                   mPasswordErrorHint.setVisibility(View.INVISIBLE);
                 }
                 if(mPassword_field.getText().toString().equals("")){
                     mPasswordErrorHint.setVisibility(View.INVISIBLE);
                 }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mEmail_field.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ValidateRegistrationFields();
                if(!ValidateEmail()){
                  mEmailErrorHint.setVisibility(View.VISIBLE);
                }else {
                    mEmailErrorHint.setVisibility(View.INVISIBLE);
                }
                if(mEmail_field.getText().toString().equals("")){
                    mEmailErrorHint.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        /* mTelephone_field.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            ValidateRegistrationFields();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/
    }

    public boolean ValidateEmptiness(){
        if(!(mUsername_field.getText().toString().matches("")||mPassword_field.getText().toString().matches("")||mEmail_field.getText().toString().matches(""))){
            return true ;
        }else return false ;

        }


    public void ValidateRegistrationFields(){
        if(ValidateEmail()&&ValidatePassword()&&ValidateUsername()&&ValidateEmptiness()){
            mSubmit.setAlpha(1f);
            mSubmit.setEnabled(true);

        }else{
            mSubmit.setAlpha(0.4f);
            mSubmit.setEnabled(false);}

    }

    public boolean ValidateEmail(){
       return android.util.Patterns.EMAIL_ADDRESS.matcher(mEmail_field.getText().toString()).matches();
    }
   /* public boolean ValidateTelephoneNumber(){
       return Patterns.PHONE.matcher(mTelephone_field.getText().toString()).matches();
    }*/

    public boolean ValidatePassword(){
     String input =mPassword_field.getText().toString();
   return input.length()>=8 ;
    }
    public boolean ValidateUsername(){
        String input = mUsername_field.getText().toString();
        return(input.length()>=3&&input.length()<=20) ;
    }





    public void setOnClickListeners(){





        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //VolleyAgent.getInstance(getApplicationContext()).registerAccount(mUsername_field.getText().toString(),mPassword_field.getText().toString(),mEmail_field.getText().toString(),callingActivity);

                  registerAccount(mUsername_field.getText().toString(),mEmail_field.getText().toString(),mPassword_field.getText().toString(),callingActivity);


            }
        });
    }
    public void determineTheCaller(){
        Intent i = getIntent();
        Bundle b = i.getExtras();
        if(b!=null){
        callingActivity = b.getString("calling activity","");
        mAdId = b.getInt("Ad ID",0);
        }

    }
    public void registerAccount( final String username, final String email , final String password ,final String caller ){

        StringRequest createNewAccountRequest =  new StringRequest(Request.Method.POST,register_url,new Response.Listener<String>(){
            public void onResponse(String response){

                StringRequest loginRequest = new StringRequest(Request.Method.POST, login_url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                            mEmailUsedErrorHint.setVisibility(View.INVISIBLE);
                            AuthenticationAgent mAgent = new AuthenticationAgent(mContext);
                            mAgent.saveToken(response);
                            dismissProgressDialog();
                            switch (caller){
                                case "search fragment" :
                                    startActivity(new Intent(RegistrationForm.this,AdCreation.class));
                                    finish();
                                    break ;
                                case "profile settings":
                                    startActivity(new Intent(RegistrationForm.this,ProfileSettingsActivity.class));
                                    finish();
                                    break ;
                                case "Ad Details":
                                    Intent i = new Intent(RegistrationForm.this,AdDetails.class);
                                    Bundle b = new Bundle();
                                    b.putInt("Ad ID",mAdId);
                                    i.putExtras(b);
                                    startActivity(i);
                                    finish();
                                    break ;

                                case "profile fragment":
                                    Intent i3 = new Intent(RegistrationForm.this,ContainerActivity.class);
                                    Bundle b3 = new Bundle();
                                    b3.putInt("fragment index",2);
                                    i3.putExtras(b3);
                                    startActivity(i3);
                                    finish();
                                    break;
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

            }


        }
                ,new Response.ErrorListener(){
            public void onErrorResponse(VolleyError volleyError){
                mEmailUsedErrorHint.setVisibility(View.VISIBLE);
                dismissProgressDialog();


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
        VolleyAgent.getInstance(getApplicationContext()).addToRequestQueue(createNewAccountRequest);
        showProgressDialog();






    }



    public void showProgressDialog(){
        mProgressD = new ProgressDialog(RegistrationForm.this);
        mProgressD.setCancelable(false);

        mProgressD.setMessage("Signing you up , hold on ......");
        mProgressD.show();

    }
    public void dismissProgressDialog(){
        mProgressD.dismiss();

    }
}
