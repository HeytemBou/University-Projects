package com.firstgpsapp.proudlikeagod.locationbasedadsfinder;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ParseException;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfileSettingsActivity extends AppCompatActivity {

    private EditText mPasswordReset, mUsernameReset, mEmailReset;
    private ImageButton mProfilePicker;
    private String downloadedImageString = "";
    private String loadedImageString = "";
    private Button mTakePhoto, mLoadPhoto, mLogOutBtn, mSaveChangesBtn, mDiscardChangesBtn, mDeleteAccountBtn, mChangeAccoutnBtn;
    private AlertDialog mTakePhotoDialog;
    private TextView mCurrentUsernameIndicator, mNextUsernameIndicator, mCurrentEmailIndicator, mNextEmailIndicator;
    private TextView mEditEmail, mEditUsername;
    private ImageView mProfilePic;
    private TextView mErrorHintUsername, mErrorHintEmail, mUsernameFormatErrorHint, mEmailEmptinessErrorHint;
    private ProgressDialog mProgressD;
    private AuthenticationAgent mAuthAgent;
    private int mUserId;
    private ArrayList<Integer> mUserCreatedAdsIdsList = new ArrayList<>() ;
    private boolean mHasProfilePhotoFlag = false;
    private String get_auth_user_url = "http://192.168.43.18:8000/api/getAuthUser";
    private String save_profile_image_url = "http://192.168.43.18:8000/api/SaveProfileImage";
    private String load_profile_image_url = "http://192.168.43.18:8000/api/LoadProfileImage";
    private String update_profile_image_url = "http://192.168.43.18:8000/api/UpdateProfilePhoto";
    private String update_user_infos = "http://192.168.43.18:8000/api/UpdateUserInfos";
    private String delete_user_account_url = "http://192.168.43.18:8000/api/DeleteUserAccount";
    private String delete_user_created_ad_url = "http://192.168.43.18:8000/api/DeleteAd";
    private String get_user_created_ads_ids = "http://192.168.43.18:8000/api/GetUserCreatedAdsIds";
    private String delete_user_profile_photo_url = "http://192.168.43.18:8000/api/DeleteProfilePhoto";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);
        loadWidgets();
        loadListeners();
        setUpTextWatchers();
        setDefaultProfileImage();
        mAuthAgent = new AuthenticationAgent(getApplicationContext());
        getProfileInfos();


    }

    public void loadWidgets() {
        mLogOutBtn = (Button) findViewById(R.id.LogOutButton);
        mSaveChangesBtn = (Button) findViewById(R.id.SaveChanges);
        mDiscardChangesBtn = (Button) findViewById(R.id.DiscardChanges);
        mProfilePicker = (ImageButton) findViewById(R.id.ProfilePicEditor);
        mCurrentEmailIndicator = (TextView) findViewById(R.id.CurrentStateIndicatorEmail);
        mCurrentUsernameIndicator = (TextView) findViewById(R.id.CurrentStateIndicatorUsername);
        mNextEmailIndicator = (TextView) findViewById(R.id.NextStateIndicatorEmail);
        mNextUsernameIndicator = (TextView) findViewById(R.id.NextStateIndicatorUsername);
        mEditEmail = (TextView) findViewById(R.id.EditEmail);
        mChangeAccoutnBtn = (Button) findViewById(R.id.ChangeAccountButton);
        mEditUsername = (TextView) findViewById(R.id.EditUsername);
        mEmailReset = (EditText) findViewById(R.id.ResetEmailField);
        mUsernameReset = (EditText) findViewById(R.id.ResetUsernameField);
        mProfilePic = (ImageView) findViewById(R.id.ProfileImageView);
        mErrorHintEmail = (TextView) findViewById(R.id.EmailErrorHint);
        mErrorHintUsername = (TextView) findViewById(R.id.UsernameErrorHint);
        mEmailEmptinessErrorHint = (TextView) findViewById(R.id.EmailEmptinessErrorHint);
        mUsernameFormatErrorHint = (TextView) findViewById(R.id.UsernameFormatErrorHint);
        mEmailEmptinessErrorHint.setVisibility(View.INVISIBLE);
        mUsernameFormatErrorHint.setVisibility(View.INVISIBLE);
        mErrorHintEmail.setVisibility(View.INVISIBLE);
        mErrorHintUsername.setVisibility(View.INVISIBLE);
        mDeleteAccountBtn = findViewById(R.id.DeleteAccountButton);

        mEmailReset.setEnabled(false);
        mUsernameReset.setEnabled(false);
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(ProfileSettingsActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.photo_picker_dialog, null);
        mTakePhoto = mView.findViewById(R.id.TakePhoto);
        mLoadPhoto = mView.findViewById(R.id.LoadPhoto);
        mBuilder.setView(mView);
        mTakePhotoDialog = mBuilder.create();
    }

    public void loadListeners() {
        mLogOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuthAgent = new AuthenticationAgent(ProfileSettingsActivity.this);
                mAuthAgent.deleteToken();
                Intent i = new Intent(ProfileSettingsActivity.this,ContainerActivity.class);
                Bundle b = new Bundle();
                b.putInt("fragment index",2);
                i.putExtras(b);
                startActivity(i);
                finish();
            }
        });

        mEditUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUsernameReset.setEnabled(true);
                mCurrentUsernameIndicator.setVisibility(View.INVISIBLE);
                mNextUsernameIndicator.setVisibility(View.VISIBLE);

            }
        });
        mEditEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmailReset.setEnabled(true);
                mCurrentEmailIndicator.setVisibility(View.INVISIBLE);
                mNextEmailIndicator.setVisibility(View.VISIBLE);
            }
        });
        mDiscardChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = 2;
                Intent in = new Intent(ProfileSettingsActivity.this, ContainerActivity.class);
                Bundle b = new Bundle();
                b.putInt("fragment index", i);
                in.putExtras(b);
                startActivity(in);
                finish();


            }
        });
        mProfilePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTakePhotoDialog.show();
            }
        });
        mTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capturePhoto(11);

            }
        });
        mLoadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadPictureFromGallery(12);

            }
        });
        mUsernameReset.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateProfileModifications();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mEmailReset.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateProfileModifications();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mSaveChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validateProfileModifications()) {
                    updateUserInfos(mUserId, mUsernameReset.getText().toString(), mEmailReset.getText().toString());
                }


            }
        });

        mChangeAccoutnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileSettingsActivity.this, MainActivity.class);
                Bundle b = new Bundle();
                b.putString("calling activity", "profile settings");
                i.putExtras(b);
                startActivity(i);
            }
        });

        mDeleteAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ProfileSettingsActivity.this);
                builder.setMessage(R.string.account_delete_confirmation).setCancelable(true).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mAuthAgent.deleteToken();
                      getUserCreatedAdsIds();

                    }
                })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                android.app.AlertDialog alert = builder.create();
                alert.show();


            }

        });
    }


    public void setUpTextWatchers() {
        mUsernameReset.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (validateUsernameFieldEmptiness()) {
                    mUsernameFormatErrorHint.setVisibility(View.INVISIBLE);
                    mErrorHintUsername.setVisibility(View.VISIBLE);
                } else {
                    mErrorHintUsername.setVisibility(View.INVISIBLE);
                    if (!validateNewUsernameFormat(mUsernameReset.getText().toString())) {
                        mUsernameFormatErrorHint.setVisibility(View.VISIBLE);
                    } else {
                        mUsernameFormatErrorHint.setVisibility(View.INVISIBLE);
                    }
                }
            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mEmailReset.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (validateEmailFieldEmptiness()) {
                    mErrorHintEmail.setVisibility(View.INVISIBLE);
                    mEmailEmptinessErrorHint.setVisibility(View.VISIBLE);
                } else {
                    mEmailEmptinessErrorHint.setVisibility(View.INVISIBLE);
                    if (!validateNewEmailFormat(mEmailReset.getText().toString())) {
                        mErrorHintEmail.setVisibility(View.VISIBLE);
                    } else {
                        mErrorHintEmail.setVisibility(View.INVISIBLE);
                    }

                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void getProfileInfos() {
        StringRequest loginRequest = new StringRequest(Request.Method.POST, get_auth_user_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                String username;
                String email;
                try {
                    JSONObject j = new JSONObject(response);
                    JSONObject l = j.getJSONObject("result");
                    username = l.getString("name");
                    email = l.getString("email");
                    mUserId = l.getInt("id");
                    mEmailReset.setText(email);
                    mUsernameReset.setText(username);
                    loadProfileImage(String.valueOf(mUserId));
                } catch (Throwable j) {
                    j.printStackTrace();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // dismissProgressDialog();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("token", mAuthAgent.getToken());
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }

        };
        VolleyAgent.getInstance(getApplicationContext()).addToRequestQueue(loginRequest);
        // showProgressDialog();


    }

    public void showProgressDialog() {
        mProgressD = new ProgressDialog(ProfileSettingsActivity.this);
        mProgressD.setCancelable(true);
        mProgressD.setMessage("Working on it......");
        mProgressD.show();

    }

    public void dismissProgressDialog() {
        mProgressD.dismiss();

    }

    public boolean validateProfileModifications() {
        return validateNewUsernameFormat(mUsernameReset.getText().toString()) && validateNewEmailFormat(mEmailReset.getText().toString()) && !validateEmailFieldEmptiness() && !validateUsernameFieldEmptiness();


    }

    public boolean validateNewUsernameFormat(String username) {
        return (username.length() >= 3 && username.length() <= 12);

    }

    public boolean validateNewEmailFormat(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();

    }

    public boolean validateUsernameFieldEmptiness() {
        return mUsernameReset.getText().toString().equals("");

    }

    public boolean validateEmailFieldEmptiness() {
        return mEmailReset.getText().toString().equals("");
    }


    public void capturePhoto(int imageViewRequest) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, imageViewRequest);
    }

    public void loadPictureFromGallery(int imageViewRequest) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, imageViewRequest);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 11:
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    mProfilePic.setImageBitmap(photo);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] b = baos.toByteArray();
                    loadedImageString = Base64.encodeToString(b, Base64.DEFAULT);
                    mTakePhotoDialog.dismiss();
                    break;

                case 12:
                    try {
                        final Uri imageUri = data.getData();
                        InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        mProfilePic.setImageBitmap(selectedImage);
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

                            imageStream = getContentResolver().openInputStream(imageUri);

                            // Convertir l'image en binaire
                            byte[] imageArray = InputStreamToByteArray(imageStream);

                            // Convertir du binaire en chaine de caractères
                            loadedImageString = Base64.encodeToString(imageArray, Base64.DEFAULT);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        mTakePhotoDialog.dismiss();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                    }

                    break;


            }
        }
    }

    public byte[] InputStreamToByteArray(InputStream stream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len = 0;
        if (stream != null) {
            while ((len = stream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }
        }
        return byteBuffer.toByteArray();
    }

    public void saveProfileImage(final String ImageResource) {
        StringRequest updateUserPhotoRequest = new StringRequest(Request.Method.POST, save_profile_image_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dismissProgressDialog();
                int i = 2;
                Intent in = new Intent(ProfileSettingsActivity.this, ContainerActivity.class);
                Bundle b = new Bundle();
                b.putInt("fragment index", i);
                in.putExtras(b);
                startActivity(in);
                finish();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                updateProfileImage(ImageResource);
            }
        }) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("profile_id", "" + mUserId);
                params.put("ImageString", ImageResource);
                params.put("Content-Type", "x-www-form-urlencoded");
                return params;
            }

        };
        VolleyAgent.getInstance(getApplicationContext()).addToRequestQueue(updateUserPhotoRequest);
        showProgressDialog();


    }

    public void updateProfileImage(final String ImageResource) {
        StringRequest updateUserPhotoRequest = new StringRequest(Request.Method.POST, update_profile_image_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dismissProgressDialog();
                int i = 2;
                Intent in = new Intent(ProfileSettingsActivity.this, ContainerActivity.class);
                Bundle b = new Bundle();
                b.putInt("fragment index", i);
                in.putExtras(b);
                startActivity(in);
                finish();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProfileSettingsActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();


            }
        }) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("user_id", "" + mUserId);
                params.put("ImageString", ImageResource);
                params.put("Content-Type", "x-www-form-urlencoded");
                return params;
            }

        };
        VolleyAgent.getInstance(getApplicationContext()).addToRequestQueue(updateUserPhotoRequest);
        showProgressDialog();


    }

    public void setProfileImage(String ImageResource) {
        byte[] byteArray = Base64.decode(ImageResource, Base64.DEFAULT);
        Bitmap bitmap1 = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        mProfilePic.setImageBitmap(bitmap1);

    }

    public void loadProfileImage(final String UserId) {
        StringRequest loadProfileImage = new StringRequest(Request.Method.POST, load_profile_image_url, new Response.Listener<String>() {
            public void onResponse(String response) {
                dismissProgressDialog();
                try {
                    JSONArray j = new JSONArray(response);
                    JSONObject l = j.getJSONObject(0);
                    downloadedImageString = l.getString("Resource");
                    setProfileImage(downloadedImageString);

                } catch (Throwable j) {
                    j.printStackTrace();

                }


            }


        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {

                dismissProgressDialog();

            }
        }) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("id_user", UserId);
                params.put("Content-Type", "x-www-form-urlencoded");
                return params;
            }

        };
        VolleyAgent.getInstance(getApplicationContext()).addToRequestQueue(loadProfileImage);
        showProgressDialog();


    }

    public void updateUserInfos(final int mUserId, final String username, final String email) {
        StringRequest updateUserInfosRequest = new StringRequest(Request.Method.POST, update_user_infos, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dismissProgressDialog();
                if (!loadedImageString.equals("")) {
                    saveProfileImage(loadedImageString);
                } else {
                    int i = 2;
                    Intent in = new Intent(ProfileSettingsActivity.this, ContainerActivity.class);
                    Bundle b = new Bundle();
                    b.putInt("fragment index", i);
                    in.putExtras(b);
                    startActivity(in);
                    finish();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("id_user", "" + mUserId);
                params.put("email", email);
                params.put("username", username);
                params.put("Content-Type", "x-www-form-urlencoded");
                return params;
            }

        };
        VolleyAgent.getInstance(getApplicationContext()).addToRequestQueue(updateUserInfosRequest);
        showProgressDialog();


    }

    //set a default profile photo if there is none
    public void setDefaultProfileImage() {
        mProfilePic.setImageResource(R.drawable.default_profile_icon_purple);
    }

    //send a delete request
    public void deleteUserAccount(final int mUserId) {
        StringRequest updateUserInfosRequest = new StringRequest(Request.Method.POST, delete_user_account_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dismissProgressDialog();
                   Intent i = new Intent(ProfileSettingsActivity.this,ContainerActivity.class);
                                        Bundle b = new Bundle();
                                        b.putInt("fragment index",2);
                                        i.putExtras(b);
                                        startActivity(i);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissProgressDialog();
            }
        }) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("id_user", String.valueOf(mUserId));
                return params;
            }

        };
        VolleyAgent.getInstance(getApplicationContext()).addToRequestQueue(updateUserInfosRequest);
        showProgressDialog();




    }
    public void deleteUserCreatedAd(final int mUserId){
        StringRequest deleteUserCreatedAdRequest = new StringRequest(Request.Method.POST, delete_user_created_ad_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("id_ad", String.valueOf(mUserId));
                return params;
            }

        };
        VolleyAgent.getInstance(getApplicationContext()).addToRequestQueue(deleteUserCreatedAdRequest);




    }

    public void getUserCreatedAdsIds(){
        StringRequest updateUserInfosRequest = new StringRequest(Request.Method.POST, get_user_created_ads_ids, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{

                JSONArray j = new JSONArray(response);
                    for(int i=0 ; i<j.length() ; i++){

                        mUserCreatedAdsIdsList.add(j.getJSONObject(i).getInt("ID_annonce"));

                    }
                    deleteUserProfileImage(mUserId);
                    deleteUserCreatedAds(mUserCreatedAdsIdsList);


                }
                catch (JSONException p){
                    Log.e("parse exception : ", "onResponse: "+p.getMessage() );

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("ID_user", String.valueOf(mUserId));
                return params;
            }

        };
        VolleyAgent.getInstance(getApplicationContext()).addToRequestQueue(updateUserInfosRequest);




    }

    public void deleteUserCreatedAds(ArrayList<Integer> mUserCreatedAdsIdsList){
        for(int i=0 ; i<mUserCreatedAdsIdsList.size() ; i ++){
            deleteUserCreatedAd(mUserCreatedAdsIdsList.get(i));
        }
        deleteUserAccount(mUserId);
    }

    public void deleteUserProfileImage(final int mUserId){
        StringRequest updateUserInfosRequest = new StringRequest(Request.Method.POST, delete_user_profile_photo_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("user_id", String.valueOf(mUserId));
                return params;
            }

        };
        VolleyAgent.getInstance(getApplicationContext()).addToRequestQueue(updateUserInfosRequest);



    }
}


