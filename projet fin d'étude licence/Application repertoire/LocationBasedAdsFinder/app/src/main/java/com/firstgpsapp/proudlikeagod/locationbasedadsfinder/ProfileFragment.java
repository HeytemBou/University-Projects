package com.firstgpsapp.proudlikeagod.locationbasedadsfinder;


import android.app.ProgressDialog;
import android.support.v4.app.FragmentTransaction;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment {
    private static String mTokenInvalidError ="{\"error\":\"Token is Invalid\"}";
    private static String mTokenEpiredError="{\"error\":\"Token is Expired\"}"  ;
    private static String mMostUsefulError ="{\"error\":\"Something is wrong\"}" ;

    //progress dialog instance
    private ProgressDialog mProgressD ;

    private AuthenticationAgent mAuthAgent ;
    private String callingActivity ="profile settings" ;

    //url for the getAuthUser api route
    private String get_auth_user = "http://192.168.43.18:8000/api/getAuthUser";
    private String load_profile_image_url = "http://192.168.43.18:8000/api/LoadProfileImage";
    private String get_user_created_ads_number ="http://192.168.43.18:8000/api/GetUserCreatedAdsNumber";

    private Button mSendNotificationButton ;
    private static final int mUniqueID = 1234 ,CAMERA_REQUEST =1888;
    private NotificationCompat.Builder mNotification ;
    private ImageView mProfileImageView ,mProfileSettingsIcon;
    private ImageButton mProfileSettingButton , mAppSettingsButton , mAboutUsButton ;
    private String mImageResource ;
    private TextView mProfileUsername ,mProfileSettingsText , mUserCreatedAdsNumberDisplayer ;
    private LinearLayout mProfileSettingsLayout ;
    private RelativeLayout mStatusLayout , mNotLoggedInStatus ;
    private TextView mLoggedInDoor ;






    private int mUserId ;

    public ProfileFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view = inflater.inflate(R.layout.profile_fragment_layout, container, false);

         mProfileImageView=view.findViewById(R.id.ProfileImageView);
         mProfileUsername=view.findViewById(R.id.ProfileUsername);
         mProfileSettingsIcon=view.findViewById(R.id.ProfileSettingsIcon);
         mProfileSettingsText=view.findViewById(R.id.ProfileSettingsText);
         mUserCreatedAdsNumberDisplayer = view.findViewById(R.id.UserCreatedAdsNumberDisplayer);
         mProfileSettingsLayout  = view.findViewById(R.id.ProfileSettingsLayout);
         mStatusLayout = view.findViewById(R.id.StatusLayout);
         mNotLoggedInStatus = view.findViewById(R.id.NotLoggedInStatus);
         mLoggedInDoor =view.findViewById(R.id.SignInDoor);

         mNotLoggedInStatus.setVisibility(View.INVISIBLE);


         mLoggedInDoor.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent i = new Intent(getActivity(),MainActivity.class);
                 Bundle b = new Bundle();
                 b.putString("calling activity","profile fragment");
                 i.putExtras(b);
                 startActivity(i);
             }
         });




         mAuthAgent = new AuthenticationAgent(getActivity().getApplicationContext());
         setDefaultProfileImage();
         getAuthUser2(mAuthAgent.getToken());

                  //listener for profile settings button
        mProfileSettingsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mAuthAgent.checkToken()){

                getAuthUser(new AuthenticationAgent(getContext()).getToken(),callingActivity);
                }else {
                    Intent i = new Intent(getActivity(),MainActivity.class);
                    Bundle b = new Bundle();
                    b.putString("calling activity",callingActivity);
                    i.putExtras(b);
                    startActivity(i);
                }

               // startActivity(new Intent(getActivity(),ProfileSettingsActivity.class));
            }
        });
        mProfileSettingsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAuthAgent.checkToken()){

                    getAuthUser(new AuthenticationAgent(getContext()).getToken(),callingActivity);
                }else {
                    Intent i = new Intent(getActivity(),MainActivity.class);
                    Bundle b = new Bundle();
                    b.putString("calling activity",callingActivity);
                    i.putExtras(b);
                    startActivity(i);
                }

            }
        });



         //listener for profile image
        /* mProfileImageView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                 startActivityForResult(cameraIntent,CAMERA_REQUEST);
             }
         });*/

         return view ;


    }
    public void onActivityResult(int requestCode,int resultCode ,Intent data){
        if(requestCode==CAMERA_REQUEST&&resultCode==RESULT_OK){
            Bitmap photo = (Bitmap)data.getExtras().get("data");
            mProfileImageView.setImageBitmap(photo);
        }

    }

    public void getAuthUser(final String token , final String caller){
        StringRequest loginRequest = new StringRequest(Request.Method.POST, get_auth_user, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dismissProgressDialog();

                if(response.equals(mTokenEpiredError)||response.equals(mTokenInvalidError)||response.equals(mMostUsefulError)){
                    Intent i = new Intent(getActivity(),MainActivity.class);
                    Bundle b = new Bundle();
                    b.putString("calling activity",caller);
                    i.putExtras(b);
                    startActivity(i);
                }else {
                    startActivity(new Intent(getActivity(),ProfileSettingsActivity.class));                }






            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                dismissProgressDialog();
            }
        }){

            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("token",token);
                return headers;
            }

        };
        VolleyAgent.getInstance(getActivity()).addToRequestQueue(loginRequest);
        showProgressDialog();
    }


    public void getAuthUser2(final String token ){

        StringRequest loginRequest = new StringRequest(Request.Method.POST, get_auth_user, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dismissProgressDialog();

                if(response.equals(mTokenEpiredError)||response.equals(mTokenInvalidError)||response.equals(mMostUsefulError)){
                    setDefaultProfileImage();
                    mStatusLayout.setVisibility(View.INVISIBLE);
                    mProfileSettingsLayout.setVisibility(View.INVISIBLE);
                    mNotLoggedInStatus.setVisibility(View.VISIBLE);

                }else {
                    String username ;

                    try {
                        JSONObject j = new JSONObject(response);
                        JSONObject l = j.getJSONObject("result");
                        username = l.getString("name");
                        mUserId = l.getInt("id");
                        mProfileUsername.setText(username);
                        loadProfileImage(mUserId);
                        getUserCreatedAdsNumber(mUserId);
                        mProfileSettingsLayout.setVisibility(View.VISIBLE);
                        mStatusLayout.setVisibility(View.VISIBLE);
                        mNotLoggedInStatus.setVisibility(View.INVISIBLE);
                    }catch(Throwable j){
                        j.printStackTrace();

                    }
                           }






            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_LONG).show();

                dismissProgressDialog();
            }
        }){

            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("token",token);
                return headers;
            }

        };
        VolleyAgent.getInstance(getActivity()).addToRequestQueue(loginRequest);
        showProgressDialog();

    }
    public void getUserCreatedAdsNumber(final int UserId ){

        StringRequest UserCreatedAdsNumber = new StringRequest(Request.Method.POST, get_user_created_ads_number, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                 mUserCreatedAdsNumberDisplayer.setText(response);

                }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {



            }
        }){

            public Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("ID_user",String.valueOf(UserId));
                return params;
            }

        };
        VolleyAgent.getInstance(getActivity()).addToRequestQueue(UserCreatedAdsNumber);


    }


    public void setDefaultProfileImage(){
        mProfileImageView.setImageResource(R.drawable.default_profile_icon2);
    }

    public void showProgressDialog(){
        mProgressD = new ProgressDialog(getActivity());
        mProgressD.setCancelable(true);
        mProgressD.setMessage("Working on it......");
        mProgressD.show();

    }
    public void dismissProgressDialog(){
        mProgressD.dismiss();

    }

    public void setProfileImage(String ImageResource){
        byte[] byteArray = Base64.decode(ImageResource,Base64.DEFAULT);
        Bitmap bitmap1 = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
        mProfileImageView.setImageBitmap(bitmap1);

    }
    public void loadProfileImage(final int UserId){
        StringRequest loadProfileImage =  new StringRequest(Request.Method.POST,load_profile_image_url,new Response.Listener<String>() {
            public void onResponse(String response) {
                try {
                    JSONArray j = new JSONArray(response);
                    JSONObject l = j.getJSONObject(0);
                    mImageResource = l.getString("Resource");
                    setProfileImage(mImageResource);
                }catch(Throwable j){
                    j.printStackTrace();

                }

            }


        } ,new Response.ErrorListener(){
            public void onErrorResponse(VolleyError volleyError){

            }
        }){
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("id_user",""+UserId);
                params.put("Content-Type", "x-www-form-urlencoded");
                return params;
            }

        };
        VolleyAgent.getInstance(getActivity().getApplicationContext()).addToRequestQueue(loadProfileImage);


    }


}






