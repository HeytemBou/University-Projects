package com.firstgpsapp.proudlikeagod.locationbasedadsfinder;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceReport;

import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.location.places.zzl;
import com.google.android.gms.location.places.PlaceReport;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AdCreation extends AppCompatActivity implements View.OnClickListener ,GoogleApiClient.OnConnectionFailedListener{
   private Button mCreateAd , mSpecifyLocation;
   private Button mTakePhoto, mLoadPhoto ;
   private ImageButton mAdPhoto1, mAdPhoto2 , mAdPhoto3 ;
   private ImageView mPreview1 , mPreview2 , mPreview3 ;
   private Spinner mCategorySpinner , mSubCategorySpinner ;
   private ArrayAdapter<String> mListCategoriesAdapter ;
   private String [] mListCategories ;
    private static AlertDialog mTakePhotoDialog ;
    private GoogleApiClient mGoogleApiClient;
    private int PLACE_PICKER_REQUEST = 99;
    private Animation mShake ;
    private CardView mCreateAdCard ;
    private String Latitude , Longtitude ;
    private ArrayList<Category> mCategoriesList ;
    private ArrayList<SubCategory> mSubCategoriesList ;
    private SpinnerAdapter mCategorySpinnerAdapter ;
    private SpinnerAdapter2 mSubCategorySpinnerAdapter ;
    private AuthenticationAgent mAuthAgent ;
    private EditText mAdTitle , mAdDescription , mFirstNameField , mLastNameField , mEmailField , mPhoneNumber ;
    private RadioGroup mTypeRadioGroup ;
    private RadioButton mOfferRadioButton , mDemandRadioButon ;
    private ProgressDialog mProgressD ;

    // three string variables to hold the result of image conversion
    private String mPhoto1 ,mPhoto2 , mPhoto3 ;
    //sub category id
    private int mSubCategoryId ;
    // remaining attributes of the ad
    private String mType="Demande" , mTitle , mDescription  , mAddress , mFirstName , mLastName , mTelephone , mEmail  ;
    private String mDate ;
    private double mLongitude , mLatitude ;
    //variable that holds the current user id
    private int mUserId ;

    //text views of ad creation form error hints
    private TextView mAdImagesErrorHint , mAdDetailsErrorHint , mContactInfosErrorHint ;

    private int mRecentAddedAdId ;

    //urls literals for networking tasks
    private String get_auth_user_url = "http://192.168.43.18:8000/api/getAuthUser";
    private String save_profile_image_url = "http://192.168.43.18:8000/api/SaveProfileImage";
    private String login_url = "http://192.168.43.18:8000/api/login";
    private String retrieve_categories_url="http://192.168.43.18:8000/api/RetrieveCategories" ;
    private String retrieve_subcategories_url="http://192.168.43.18:8000/api/RetrieveSubCategories" ;
    private String publish_ad_url_="http://192.168.43.18:8000/api/PublishAd" ;

    //image strings
    private String AdImage1=""  , AdImage2="" , AdImage3="" ;


    AlertDialog.Builder mBuilder ;

   private static final int CAMERA_REQUEST =1888 , GALLERY_REQUEST=1999;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_creation);
        mAuthAgent = new AuthenticationAgent(getApplicationContext());
        getProfileInfos();
        mCategoriesList = new ArrayList<Category>();
        mSubCategoriesList= new ArrayList<SubCategory>();
        retrieveCategoriesList();
        loadAnimations();
        loadWidget();
        loadListeners();


    }


    public void loadWidget(){
       // mCreateAd =(Button)findViewById(R.id.AdCreationButton);
        mCategorySpinner = (Spinner)findViewById(R.id.categorySpinner);
        mSubCategorySpinner =(Spinner)findViewById(R.id.subCategorieSpinner);
        mSubCategorySpinner.setAdapter(mListCategoriesAdapter);
          mCreateAdCard =findViewById(R.id.createAdCard);
        mCategorySpinner.setAdapter(mListCategoriesAdapter);
        mAdPhoto1=(ImageButton) findViewById(R.id.AdPhoto1);
        mAdPhoto2=(ImageButton) findViewById(R.id.AdPhoto2);
        mAdPhoto3=(ImageButton) findViewById(R.id.AdPhoto3);
        mSpecifyLocation=(Button)findViewById(R.id.SpecifyLocation);
        mCreateAd =(Button)findViewById(R.id.PublishAdButton);
        mTypeRadioGroup = findViewById(R.id.RadioType);
        mAdTitle =findViewById(R.id.AdTitle);
        mAdDescription =findViewById(R.id.Description);
        mEmailField =findViewById(R.id.Email);
        mPhoneNumber =findViewById(R.id.PhoneNumber);
        mFirstNameField =findViewById(R.id.FirstName);
        mLastNameField=findViewById(R.id.LastName);
        mOfferRadioButton =findViewById(R.id.radioOffer);
        mDemandRadioButon =findViewById(R.id.radioDemand);
        mAdImagesErrorHint =findViewById(R.id.adImagesErrorHint);
        mAdDetailsErrorHint=findViewById(R.id.adDetailsErrorHint);
        mContactInfosErrorHint=findViewById(R.id.contactInfosErrorHint);

        mAdImagesErrorHint.setVisibility(View.INVISIBLE);
        mAdDetailsErrorHint.setVisibility(View.INVISIBLE);
        mContactInfosErrorHint.setVisibility(View.INVISIBLE);

        mPreview1=findViewById(R.id.preview1);
        mPreview2=findViewById(R.id.preview2);
        mPreview3=findViewById(R.id.preview3);




        //initilize google api client
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        mDate = currentDate();






    }
    public void loadListeners(){
        mCreateAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                      if(validateCreationFields()) {
                          if(mType=="Demande"){
                              Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.demand);
                              AdImage2 = bitmapToStringForCapturedImage(bm);
                              AdImage1 = bitmapToStringForCapturedImage(bm);
                              AdImage3 = bitmapToStringForCapturedImage(bm );
                          }
                          publishAd(mTitle, mType, mDescription, mAddress, mFirstName, mLastName, mEmail, mTelephone, mDate, mLongitude, mLatitude, mUserId, mSubCategoryId);
                      }else {
                          addTextWatchers();
                          mCreateAdCard.setAnimation(mShake);
                          mCreateAdCard.startAnimation(mShake);
                      }

                    //saveAdsImages(mRecentAddedAdId);
                    // Toast.makeText(getApplicationContext(),mTitle+mType+mDescription,Toast.LENGTH_LONG).show();
                   // Toast.makeText(getApplicationContext(),mAddress+" "+mLatitude+" "+mLongitude,Toast.LENGTH_LONG).show();
                   // Toast.makeText(getApplicationContext(),mEmail+" "+mTelephone+" "+mFirstName+" "+mLastName,Toast.LENGTH_LONG).show();
                    //Toast.makeText(getApplicationContext(),mUserId+" "+mDate+" "+mType+" "+mSubCategoryId,Toast.LENGTH_LONG).show();
                }


        });
        mCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                mSubCategoriesList.clear();
                retrieveSubCategoriesList(mCategoriesList.get(pos).getID());
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mSubCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSubCategoryId = mSubCategoriesList.get(position).getID();
                mAdDescription.setHint(mSubCategoriesList.get(position).getDescription());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mAdPhoto1.setOnClickListener(this);
        mAdPhoto2.setOnClickListener(this);
        mAdPhoto3.setOnClickListener(this);

        mSpecifyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PlacePicker.IntentBuilder mBuilder = new PlacePicker.IntentBuilder();
                try{
                    startActivityForResult(mBuilder.build(AdCreation.this), PLACE_PICKER_REQUEST);


                }catch(Throwable e){
                    e.printStackTrace();


                }
            }
        });

        mTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(checkedId==R.id.radioOffer){

                    mType ="Offre";
                }else mType="Demande";


            }
        });
    }
    public void onClick(View v){
        final int imageViewClicked = v.getId();
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(AdCreation.this);
        View mView = getLayoutInflater().inflate(R.layout.photo_picker_dialog,null);
        mTakePhoto = mView.findViewById(R.id.TakePhoto);
        mLoadPhoto=mView.findViewById(R.id.LoadPhoto);
        mBuilder.setView(mView);
         mTakePhotoDialog = mBuilder.create();
        mTakePhotoDialog.show();

        mTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch(imageViewClicked){
                    case R.id.AdPhoto1 :
                        capturePhoto(11);

                        break ;
                    case R.id.AdPhoto2 :
                        capturePhoto(21);
                        break ;
                    case R.id.AdPhoto3 :
                        capturePhoto(31);
                        break ;

                }


            }
        });

        mLoadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(imageViewClicked){
                    case R.id.AdPhoto1 :
                        loadPictureFromGallery(12);
                        break ;
                    case R.id.AdPhoto2 :
                        loadPictureFromGallery(22);
                        break ;
                    case R.id.AdPhoto3 :
                        loadPictureFromGallery(32);
                        break ;

                }

            }
        });

      //  mTakePhotoDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));





    }

    public void addTextWatchers(){
        mAdDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                       if(mAdDescription.getText().toString().equals("")||mAdTitle.getText().toString().equals("")){
                           mAdDetailsErrorHint.setVisibility(View.VISIBLE);
                       }else mAdDetailsErrorHint.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mAdTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               if(mAdTitle.getText().toString().equals("")||mAdDescription.getText().toString().equals("")){
                   mAdDetailsErrorHint.setVisibility(View.VISIBLE);
               }else mAdDetailsErrorHint.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mFirstNameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               if(!validateContactInformationFields()){
                   mContactInfosErrorHint.setVisibility(View.VISIBLE);
               }else mContactInfosErrorHint.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mLastNameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(mFirstNameField.getText().toString().equals("")||mLastNameField.getText().toString().equals("")||mEmailField.getText().toString().equals("")||mPhoneNumber.getText().toString().equals("")){
                    mContactInfosErrorHint.setVisibility(View.VISIBLE);
                }else mContactInfosErrorHint.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mEmailField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!validateContactInformationFields()){
                    mContactInfosErrorHint.setVisibility(View.VISIBLE);
                }else mContactInfosErrorHint.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(mFirstNameField.getText().toString().equals("")||mLastNameField.getText().toString().equals("")||mEmailField.getText().toString().equals("")||mPhoneNumber.getText().toString().equals("")){
                    mContactInfosErrorHint.setVisibility(View.VISIBLE);
                }else mContactInfosErrorHint.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }





    public void capturePhoto (int imageViewRequest){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent,imageViewRequest);
    }

    public void loadPictureFromGallery(int imageViewRequest){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, imageViewRequest);

    }

    protected void onActivityResult(int requestCode , int resultCode ,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode==RESULT_OK){
            switch (requestCode){
                case 11:
                    Bitmap photo = (Bitmap)data.getExtras().get("data");
                    mPreview1.setImageBitmap(photo);
                    mTakePhotoDialog.dismiss();
                    AdImage1 = bitmapToStringForCapturedImage(photo);


                    break ;

                case 12 :
                    try {
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        AdImage1 = bitmapToStringForLoadedImage(imageUri,imageStream);
                        mPreview1.setImageBitmap(selectedImage);
                        mTakePhotoDialog.dismiss();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                    }

                    break ;
                case 21 :
                    Bitmap photo2 = (Bitmap)data.getExtras().get("data");
                    AdImage2 = bitmapToStringForCapturedImage(photo2);
                    mPreview2.setImageBitmap(photo2);
                    break ;
                case 22 :
                    try {
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        AdImage2 = bitmapToStringForLoadedImage(imageUri,imageStream);
                        mPreview2.setImageBitmap(selectedImage);
                        mTakePhotoDialog.dismiss();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                    break ;
                case 31 :
                    Bitmap photo3 = (Bitmap)data.getExtras().get("data");
                    AdImage3 =bitmapToStringForCapturedImage(photo3);
                    mPreview3.setImageBitmap(photo3);
                    mTakePhotoDialog.dismiss();
                    break ;
                case 32 :
                    try {
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        AdImage3 =bitmapToStringForLoadedImage(imageUri,imageStream);
                        mPreview3.setImageBitmap(selectedImage);
                        mTakePhotoDialog.dismiss();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                    break ;

                case 99 :
                    Place place = PlacePicker.getPlace(data, this);
                    StringBuilder stBuilder = new StringBuilder();
                    String placename = String.format("%s", place.getName());
                    String latitude = String.valueOf(place.getLatLng().latitude);
                    String longitude = String.valueOf(place.getLatLng().longitude);
                    String address = String.format("%s", place.getAddress());
                    mLongitude = place.getLatLng().longitude ; mLatitude = place.getLatLng().latitude ;
                    mSpecifyLocation.setText(address);



                    break ;

            }
        }
    }


    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Snackbar.make(mSpecifyLocation, connectionResult.getErrorMessage() + "", Snackbar.LENGTH_LONG).show();
    }

    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    public void publishAd(final String Title , final String Type , final String Description, final String Address , final String FirstName , final String LastName , final String Email , final String Telephone , final String PulishingDate , final double Longitude , final double Latitude , final int UserId , final int SubCategoryId){
        StringRequest adPublishingRequest = new StringRequest(Request.Method.POST , publish_ad_url_ , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mRecentAddedAdId = Integer.parseInt(response);
                saveAdImages();
                showAdCreatedDialog();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               dismissProgressDialog();

            }
        }){
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("ID_user",""+UserId);
                params.put("Title",Title);
                params.put("Description",Description);
                params.put("Type",Type);
                params.put("DatePublication",PulishingDate);
                params.put("SubCategoryId",""+SubCategoryId);
                params.put("FirstName",FirstName);
                params.put("LastName",LastName);
                params.put("Email",Email);
                params.put("Telephone",Telephone);
                params.put("Longitude",""+Longitude);
                params.put("Latitude",""+Latitude);
                params.put("Address",Address);
                params.put("Content-Type", "x-www-form-urlencoded");
                return params;
            }

        };
        VolleyAgent.getInstance(getApplicationContext()).addToRequestQueue(adPublishingRequest);
        showProgressDialog();


    }

    public void retrieveCategoriesList(){
        StringRequest RetrieveCategoriesRequest = new StringRequest(Request.Method.GET, retrieve_categories_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONArray liste = new JSONArray(response);
                    for (int i=0;i<liste.length();i++){
                        JSONObject obj = liste.getJSONObject(i);
                        Category cat= new Category();
                        if(obj.getInt("ID_categorie")!=56){
                        cat.setName(obj.getString("name"));
                        cat.setID(obj.getString("ID_categorie"));
                        mCategoriesList.add(cat);}}}
                catch(Throwable t){
                    t.printStackTrace();
                }


                mCategorySpinnerAdapter = new SpinnerAdapter(AdCreation.this,mCategoriesList,android.R.layout.simple_list_item_1);
                mCategorySpinner.setAdapter(mCategorySpinnerAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });
        VolleyAgent.getInstance(getApplicationContext()).addToRequestQueue(RetrieveCategoriesRequest);





    }


    public void retrieveSubCategoriesList(final String CatId){
        StringRequest RetrieveSubCategoriesRequest = new StringRequest(Request.Method.POST, retrieve_subcategories_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONArray liste = new JSONArray(response);
                    for (int i=0;i<liste.length();i++){
                        JSONObject obj = liste.getJSONObject(i);
                        SubCategory cat= new SubCategory();
                        cat.setName(obj.getString("name"));
                        mSubCategoryId = obj.getInt("ID_sous_categorie");
                        mAdDescription.setHint(obj.getString("description"));
                        cat.setID(obj.getInt("ID_sous_categorie"));
                        mSubCategoriesList.add(cat);}}
                catch(Throwable t){

                }


                mSubCategorySpinnerAdapter = new SpinnerAdapter2(AdCreation.this,mSubCategoriesList,android.R.layout.simple_list_item_1);
                mSubCategorySpinner.setAdapter(mSubCategorySpinnerAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                params.put("id",CatId);
                return params;
            }

        };
        VolleyAgent.getInstance(getApplicationContext()).addToRequestQueue(RetrieveSubCategoriesRequest);


    }

    public void getProfileInfos(){
        StringRequest loginRequest = new StringRequest(Request.Method.POST, get_auth_user_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    JSONObject j = new JSONObject(response);
                    JSONObject l = j.getJSONObject("result");
                    mUserId = l.getInt("id");


                }catch(Throwable j){
                    j.printStackTrace();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // dismissProgressDialog();

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("token",mAuthAgent.getToken());
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }

        };
        VolleyAgent.getInstance(getApplicationContext()).addToRequestQueue(loginRequest);
        // showProgressDialog();


    }

    public String currentDate(){

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);
        return formattedDate ;

    }

    public boolean validateCreationFields (){
        return validateAdsImages()&&validateAdDetails()&&validateContactInformationFields() ;
    }

    //validation methods
    public boolean validateContactInformationFields(){
        mLastName = mLastNameField.getText().toString();
        mFirstName = mFirstNameField.getText().toString();
        mEmail =mEmailField.getText().toString();
        mTelephone=mPhoneNumber.getText().toString();
        if(!(mLastName.equals("")||mFirstName.equals("")||mEmail.equals("")||mTelephone.equals(""))){
            if( android.util.Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()&&android.util.
            Patterns.PHONE.matcher(mTelephone).matches()){
                return true ;
            }

        }
        return false ;

    }

    public boolean validateAdsImagesEquality(String s1 , String s2 , String s3 ){
        return (s1.equals(s2)||s2.equals(s3)||s3.equals(s1));

    }
    public boolean validateAdsImages(){
        if(mType.equals("Demande")){
            if((AdImage1.equals(""))||(AdImage2.equals(""))||(AdImage3.equals(""))){
                mAdImagesErrorHint.setVisibility(View.VISIBLE);
                return false ;
            }else if ((AdImage1.equals(""))&&(AdImage2.equals(""))&&(AdImage3.equals(""))){
                return true ;
            }

        }else {
                if(AdImage1.equals("")||AdImage2.equals("")||AdImage3.equals("")){
                    mAdImagesErrorHint.setVisibility(View.VISIBLE);
                    return false ;

                }
            }
            mAdImagesErrorHint.setVisibility(View.INVISIBLE);
            return true ;
    }
    public boolean validateAdDetails(){
        mTitle = mAdTitle.getText().toString();
        mDescription = mAdDescription.getText().toString();
        mAddress =mSpecifyLocation.getText().toString();
        if(!(mTitle.equals("")||mDescription.equals("")||mAddress.equals("")||mAddress.equals("Specify your location")||mAddress.equals("Précisez votre emplacement"))){
            mAdDetailsErrorHint.setVisibility(View.INVISIBLE);
            return true ;
        }
        mAdDetailsErrorHint.setVisibility(View.VISIBLE);

        return false ;
    }
    public void loadAnimations(){

        mShake= AnimationUtils.loadAnimation(this,R.anim.shake);


    }

    public void showProgressDialog(){
        mProgressD = new ProgressDialog(AdCreation.this);
        mProgressD.setCancelable(false);
        mProgressD.setMessage("Working on it......");
        mProgressD.show();

    }
    public void dismissProgressDialog(){
        mProgressD.dismiss();

    }

    public void saveAdImages( ){

        String [] Resources = {AdImage1,AdImage2,AdImage3};

        for (int i =0;i<3;i++){
            saveSingleAdImage(Resources[i],mRecentAddedAdId);
        }


    }
    public void saveSingleAdImage(final String resource ,final int AdId ){
        StringRequest loginRequest = new StringRequest(Request.Method.POST, save_profile_image_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }){
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("ad_id",mRecentAddedAdId+"");
                params.put("ImageString",resource);
                params.put("Content-Type", "x-www-form-urlencoded");
                return params;
            }

        };
        VolleyAgent.getInstance(getApplicationContext()).addToRequestQueue(loginRequest);
        dismissProgressDialog();


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
        return  byteBuffer.toByteArray();
    }

    public String bitmapToStringForCapturedImage (Bitmap b){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] by = baos.toByteArray();
        return Base64.encodeToString(by, Base64.DEFAULT);
    }

    public String bitmapToStringForLoadedImage(Uri imageUri , InputStream imageStream){
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 70, baos);

            imageStream = getContentResolver().openInputStream(imageUri);

            // Convertir l'image en binaire
            byte[] imageArray=  InputStreamToByteArray(imageStream);

            // Convertir du binaire en chaine de caractères
            return  Base64.encodeToString(imageArray, Base64.DEFAULT);}
        catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    public Bitmap stringToBitmap(String s){
        byte[] byteArray = Base64.decode(s,Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);

    }

    public void showAdCreatedDialog(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("");
        builder1.setMessage("Ad published successfully");
        builder1.setCancelable(true);
        builder1.setNeutralButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent i = new Intent(AdCreation.this,ContainerActivity.class);
                        Bundle b = new Bundle();
                        b.putInt("fragment index",3);
                        i.putExtras(b);
                        startActivity(i);
                        finish();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
