package com.firstgpsapp.proudlikeagod.locationbasedadsfinder;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdEditing extends AppCompatActivity  implements View.OnClickListener{
    private Button mCreateAd, mSpecifyLocation;
    private Button mTakePhoto, mLoadPhoto;
    private ImageButton mAdPhoto1, mAdPhoto2, mAdPhoto3;
    private ImageView mPreview1, mPreview2, mPreview3;
    private Spinner mCategorySpinner, mSubCategorySpinner;
    private ArrayAdapter<String> mListCategoriesAdapter;
    private String[] mListCategories;
    private Animation mShake ;
    private static AlertDialog mTakePhotoDialog;
    private GoogleApiClient mGoogleApiClient;
    private int PLACE_PICKER_REQUEST = 99;
    private String Latitude, Longtitude;
    private ArrayList<Category> mCategoriesList;
    private ArrayList<SubCategory> mSubCategoriesList;
    private SpinnerAdapter mCategorySpinnerAdapter;
    private SpinnerAdapter2 mSubCategorySpinnerAdapter;
    private AuthenticationAgent mAuthAgent;
    private int mSubCategoryId;
    private EditText mAdTitle, mAdDescription, mFirstNameField, mLastNameField, mEmailField, mPhoneNumber;
    private RadioGroup mTypeRadioGroup;
    private RadioButton mOfferRadioButton, mDemandRadioButon;
    ProgressDialog mProgressD;
    private String get_ad_details = "http://192.168.43.18:8000/api/GetAdDetails";
    private String get_ad_photos = "http://192.168.43.18:8000/api/GetAdTriplePhotos";
    private String retrieve_categories_url = "http://192.168.43.18:8000/api/RetrieveCategories";
    private String retrieve_subcategories_url = "http://192.168.43.18:8000/api/RetrieveSubCategories";
    private String save_profile_image_url = "http://192.168.43.18:8000/api/SaveProfileImage";
    private String update_user_created_ad_url= "http://192.168.43.18:8000/api/UpdateUserCreatedAd";
    private String retrieve_subcategories_full_list_url= "http://192.168.43.18:8000/api/GetSubCategoriesFullList";
    private String imageResource1, imageResource2, imageResource3;
    private Bitmap bitmap1, bitmap2, bitmap3;
    private String mTitle, mDescription, mCategory, mSubCategory, mPublishingDate;
    private String mFirstName, mLastName, mTelephone, mEmail, mAddress;
    private double mLatitude, mLongitude;
    private int mADID, mUserID, mSubCategoryID, mCategoryID;
    private int mExtractedAdId;
    private String mType;
    private CardView mCreateAdCard ;
    private String AdImage1, AdImage2, AdImage3;
    private TextView mEditAdTitle , mEditAdDescription , mEditFirstName , mEditLastName , mEditPhoneNumber , mEditEmail ;
    private TextView mAdImagesErrorHint , mAdDetailsErrorHint , mContactInfosErrorHint ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_editing);
        extractUserCreatedAdID();
        loadWidgets();
        loadAnimations();
        loadListeners();
        mCategoriesList = new ArrayList<Category>();
        mSubCategoriesList= new ArrayList<SubCategory>();
        retrieveCategoriesList();
        retrieveSubCategoriesList(String.valueOf(1));
       // retrieveSubCategoriesFullList();

        retrieveAdDetails();
    }
    public void loadAnimations(){

        mShake= AnimationUtils.loadAnimation(this,R.anim.shake);


    }

    public void updateUserCreatedAd(final String Title , final String Type , final String Description, final String Address , final String FirstName , final String LastName , final String Email , final String Telephone , final double Longitude , final double Latitude , final int SubCategoryId){
        StringRequest adPublishingRequest = new StringRequest(Request.Method.POST,update_user_created_ad_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                saveAdImages();



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
                params.put("ID_ad",String.valueOf(mADID));
                params.put("Title",Title);
                params.put("Description",Description);
                params.put("Type",Type);
                params.put("SubCategoryId",""+SubCategoryId);
                params.put("FirstName",FirstName);
                params.put("LastName",LastName);
                params.put("Email",Email);
                params.put("Telephone",Telephone);
                params.put("Longitude",""+Longitude);
                params.put("Latitude",""+Latitude);
                params.put("Address",Address);
                return params;
            }

        };
        VolleyAgent.getInstance(getApplicationContext()).addToRequestQueue(adPublishingRequest);
        showProgressDialog();


    }

    public void retrieveAdDetails() {
        StringRequest adDetailsRequest = new StringRequest(Request.Method.POST, get_ad_details, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray j = new JSONArray(response);
                    JSONObject o = j.getJSONObject(0);
                    mADID = o.getInt("ID_annonce");
                    mUserID = o.getInt("ID_user");
                    mSubCategoryID = o.getInt("ID_sous_categorie");
                    mTitle = o.getString("Titre");
                    mType=o.getString("Type");
                    mDescription = o.getString("Description");
                    mFirstName = o.getString("Nom");
                    mLastName = o.getString("Prenom");
                    mEmail = o.getString("Email");
                    mTelephone = o.getString("Telephone");
                    mAddress = o.getString("Address");
                    mLatitude = o.getDouble("Latitude");
                    mLongitude = o.getDouble("Longitude");
                    retrieveAdPhotos(mADID);
                    loadAdDetails();


                } catch (Throwable t) {
                   // Toast.makeText(AdEditing.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
                params.put("AdId", String.valueOf(mExtractedAdId));
                params.put("Content-Type", "x-www-form-urlencoded");
                return params;
            }

        };
        VolleyAgent.getInstance(getApplicationContext()).addToRequestQueue(adDetailsRequest);


    }


    public void retrieveAdPhotos(final int AdId) {
        StringRequest adDetailsRequest = new StringRequest(Request.Method.POST, get_ad_photos, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dismissProgressDialog();

                try {
                    JSONArray j = new JSONArray(response);
                    JSONObject o = j.getJSONObject(0);
                    AdImage1 = o.getString("Resource");
                    o = j.getJSONObject(1);
                    AdImage2 = o.getString("Resource");
                    o = j.getJSONObject(2);
                    AdImage3 = o.getString("Resource");

                    bitmap1 = fromStringToBitmap(AdImage1);
                    bitmap2 = fromStringToBitmap(AdImage2);
                    bitmap3 = fromStringToBitmap(AdImage3);
                    loadAdPhotos();
                    setSpinnersValues(mSubCategoryID);

                } catch (Throwable t) {
                    Log.e("error in parsing ", "onResponse: " + t.getMessage());

                }


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
                params.put("ad_id", String.valueOf(AdId));
                params.put("Content-Type", "x-www-form-urlencoded");
                return params;
            }

        };
        VolleyAgent.getInstance(getApplicationContext()).addToRequestQueue(adDetailsRequest);
        showProgressDialog();

    }

    public void extractUserCreatedAdID() {
        Intent i = getIntent();
        Bundle b = i.getExtras();
        mExtractedAdId = b.getInt("User Created Ad Id");


    }

    public void loadWidgets() {
        // mCreateAd =(Button)findViewById(R.id.AdCreationButton);
        mCreateAdCard =findViewById(R.id.createAdCard);
        mCategorySpinner = (Spinner) findViewById(R.id.categorySpinner);
        mSubCategorySpinner = (Spinner) findViewById(R.id.subCategorieSpinner);
        mSubCategorySpinner.setAdapter(mListCategoriesAdapter);
        mCategorySpinner.setAdapter(mListCategoriesAdapter);
        mAdPhoto1 = (ImageButton) findViewById(R.id.AdPhoto1);
        mAdPhoto2 = (ImageButton) findViewById(R.id.AdPhoto2);
        mAdPhoto3 = (ImageButton) findViewById(R.id.AdPhoto3);
        mSpecifyLocation = (Button) findViewById(R.id.SpecifyLocation);
        mCreateAd = (Button) findViewById(R.id.PublishAdButton);
        mTypeRadioGroup = findViewById(R.id.RadioType);
        mAdTitle = findViewById(R.id.AdTitle);
        mAdDescription = findViewById(R.id.Description);
        mEmailField = findViewById(R.id.Email);
        mPhoneNumber = findViewById(R.id.PhoneNumber);
        mFirstNameField = findViewById(R.id.FirstName);
        mLastNameField = findViewById(R.id.LastName);
        mOfferRadioButton = findViewById(R.id.radioOffer);
        mDemandRadioButon = findViewById(R.id.radioDemand);
        mPreview1 = findViewById(R.id.preview1);
        mPreview2 = findViewById(R.id.preview2);
        mPreview3 = findViewById(R.id.preview3);
        mAdTitle.setEnabled(false);
        mAdDescription.setEnabled(false);
        mFirstNameField.setEnabled(false);
        mLastNameField.setEnabled(false);
        mPhoneNumber.setEnabled(false);
        mEmailField.setEnabled(false);

        mEditAdDescription = findViewById(R.id.editAdDescription);
        mEditAdTitle = findViewById(R.id.editAdTitle);
        mEditFirstName = findViewById(R.id.editTheFirstName);
        mEditEmail = findViewById(R.id.editTheEmail);
        mEditLastName = findViewById(R.id.editTheLastName);
        mEditPhoneNumber =findViewById(R.id.editThePhoneNumber);

        mAdImagesErrorHint =findViewById(R.id.adImagesErrorHint);
        mAdDetailsErrorHint=findViewById(R.id.adDetailsErrorHint);
        mContactInfosErrorHint=findViewById(R.id.contactInfosErrorHint);

        mAdImagesErrorHint.setVisibility(View.INVISIBLE);
        mAdDetailsErrorHint.setVisibility(View.INVISIBLE);
        mContactInfosErrorHint.setVisibility(View.INVISIBLE);

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
                if(mFirstNameField.getText().toString().equals("")||mLastNameField.getText().toString().equals("")||mEmailField.getText().toString().equals("")||mPhoneNumber.getText().toString().equals("")){
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
                if(mEmailField.getText().toString().equals("")){
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

    public void loadAdDetails() {
        mEmailField.setText(mEmail);
        mPhoneNumber.setText(mTelephone);
        mFirstNameField.setText(mFirstName);
        mLastNameField.setText(mLastName);
        mSpecifyLocation.setText(mAddress);
        mAdTitle.setText(mTitle);
        mAdDescription.setText(mDescription);

        if(mType.equals("Demande")){
            mDemandRadioButon.setChecked(true);

        }else {
            mOfferRadioButton.setChecked(true);

        }


    }

    public void loadAdPhotos() {
        mPreview1.setImageBitmap(bitmap1);
        mPreview2.setImageBitmap(bitmap2);
        mPreview3.setImageBitmap(bitmap3);
    }

    public Bitmap fromStringToBitmap(String resource) {
        byte[] byteArray = Base64.decode(resource, Base64.DEFAULT);
        Bitmap b = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        return b;
    }


    public void retrieveCategoriesList() {
        StringRequest RetrieveCategoriesRequest = new StringRequest(Request.Method.GET, retrieve_categories_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray liste = new JSONArray(response);
                    for (int i = 0; i < liste.length(); i++) {
                        JSONObject obj = liste.getJSONObject(i);
                        Category cat = new Category();
                        if(obj.getInt("ID_categorie")!=56){
                        cat.setName(obj.getString("name"));
                        cat.setID(obj.getString("ID_categorie"));
                        mCategoriesList.add(cat);}
                    }
                    mCategorySpinnerAdapter = new SpinnerAdapter(AdEditing.this, mCategoriesList, android.R.layout.simple_list_item_1);
                    mCategorySpinner.setAdapter(mCategorySpinnerAdapter);
                } catch (Throwable t) {
                    t.printStackTrace();
                }




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });
        VolleyAgent.getInstance(getApplicationContext()).addToRequestQueue(RetrieveCategoriesRequest);


    }


    public void retrieveSubCategoriesList(final String CatId) {
        StringRequest RetrieveSubCategoriesRequest = new StringRequest(Request.Method.POST, retrieve_subcategories_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONArray liste = new JSONArray(response);
                    for (int i = 0; i < liste.length(); i++) {
                        JSONObject obj = liste.getJSONObject(i);
                        SubCategory cat = new SubCategory();
                        cat.setName(obj.getString("name"));
                        mSubCategoryId = obj.getInt("ID_sous_categorie");
                        mAdDescription.setHint(obj.getString("description"));
                        cat.setID(obj.getInt("ID_sous_categorie"));
                        cat.setCatID(obj.getInt("ID_categorie"));
                        mSubCategoriesList.add(cat);
                    }
                } catch (Throwable t) {

                }


                mSubCategorySpinnerAdapter = new SpinnerAdapter2(AdEditing.this, mSubCategoriesList, android.R.layout.simple_list_item_1);
                mSubCategorySpinner.setAdapter(mSubCategorySpinnerAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", CatId);
                return params;
            }

        };
        VolleyAgent.getInstance(getApplicationContext()).addToRequestQueue(RetrieveSubCategoriesRequest);


    }
    public void retrieveSubCategoriesFullList() {
        StringRequest RetrieveSubCategoriesRequest = new StringRequest(Request.Method.GET, retrieve_subcategories_full_list_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONArray liste = new JSONArray(response);
                    for (int i = 0; i < liste.length(); i++) {
                        JSONObject obj = liste.getJSONObject(i);
                        SubCategory cat = new SubCategory();
                        cat.setName(obj.getString("name"));
                        mSubCategoryId = obj.getInt("ID_sous_categorie");
                        mAdDescription.setHint(obj.getString("description"));
                        cat.setID(obj.getInt("ID_sous_categorie"));
                        cat.setCatID(obj.getInt("ID_categorie"));
                        mSubCategoriesList.add(cat);
                    }
                } catch (Throwable t) {

                }


                mSubCategorySpinnerAdapter = new SpinnerAdapter2(AdEditing.this, mSubCategoriesList, android.R.layout.simple_list_item_1);
                mSubCategorySpinner.setAdapter(mSubCategorySpinnerAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });
        VolleyAgent.getInstance(getApplicationContext()).addToRequestQueue(RetrieveSubCategoriesRequest);


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
                    mPreview1.setImageBitmap(photo);
                    AdImage1 = bitmapToStringForCapturedImage(photo);


                    break;

                case 12:
                    try {
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        AdImage1 = bitmapToStringForLoadedImage(imageUri, imageStream);
                        mPreview1.setImageBitmap(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                    }

                    break;
                case 21:
                    Bitmap photo2 = (Bitmap) data.getExtras().get("data");
                    AdImage2 = bitmapToStringForCapturedImage(photo2);
                    mPreview2.setImageBitmap(photo2);
                    break;
                case 22:
                    try {
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        AdImage2 = bitmapToStringForLoadedImage(imageUri, imageStream);
                        mPreview2.setImageBitmap(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                    break;
                case 31:
                    Bitmap photo3 = (Bitmap) data.getExtras().get("data");
                    AdImage3 = bitmapToStringForCapturedImage(photo3);
                    mPreview3.setImageBitmap(photo3);
                    break;
                case 32:
                    try {
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        AdImage3 = bitmapToStringForLoadedImage(imageUri, imageStream);
                        mPreview3.setImageBitmap(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                    break;

                case 99:
                    Place place = PlacePicker.getPlace(data, this);
                    StringBuilder stBuilder = new StringBuilder();
                    String placename = String.format("%s", place.getName());
                    String latitude = String.valueOf(place.getLatLng().latitude);
                    String longitude = String.valueOf(place.getLatLng().longitude);
                    String address = String.format("%s", place.getAddress());
                    mLongitude = place.getLatLng().longitude;
                    mLatitude = place.getLatLng().latitude;
                    mSpecifyLocation.setText(address);


                    break;

            }
        }
    }

    public String bitmapToStringForCapturedImage(Bitmap b) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] by = baos.toByteArray();
        return Base64.encodeToString(by, Base64.DEFAULT);
    }

    public String bitmapToStringForLoadedImage(Uri imageUri, InputStream imageStream) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 70, baos);

            imageStream = getContentResolver().openInputStream(imageUri);

            // Convertir l'image en binaire
            byte[] imageArray = InputStreamToByteArray(imageStream);

            // Convertir du binaire en chaine de caractères
            return Base64.encodeToString(imageArray, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
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

    public void showProgressDialog() {
        mProgressD = new ProgressDialog(AdEditing.this);
        mProgressD.setCancelable(true);
        mProgressD.setIcon(R.mipmap.about_us_icon_2);
        mProgressD.setMessage("Working on it , hold on ......");
        mProgressD.show();

    }

    public void dismissProgressDialog() {
        mProgressD.dismiss();

    }


    public void loadListeners() {
        mEditPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhoneNumber.setEnabled(true);
            }
        });
        mEditLastName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLastNameField.setEnabled(true);
            }
        });
        mEditEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmailField.setEnabled(true);
            }
        });
        mEditFirstName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirstNameField.setEnabled(true);
            }
        });
        mEditAdTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdTitle.setEnabled(true);
            }
        });
        mEditAdDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdDescription.setEnabled(true);
            }
        });
        mCreateAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateCreationFields()){
                updateUserCreatedAd(mAdTitle.getText().toString(),mType,mAdDescription.getText().toString(),mAddress,mFirstNameField.getText().toString(),mLastNameField.getText().toString(),mEmailField.getText().toString(),mPhoneNumber.getText().toString(),mLongitude,mLatitude,mSubCategoryId);}
                else {
                    addTextWatchers();
                    mCreateAdCard.setAnimation(mShake);
                    mCreateAdCard.startAnimation(mShake);
                }
            }
        });




        mAdPhoto1.setOnClickListener(this);
        mAdPhoto2.setOnClickListener(this);
        mAdPhoto3.setOnClickListener(this);

        mSpecifyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PlacePicker.IntentBuilder mBuilder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(mBuilder.build(AdEditing.this), PLACE_PICKER_REQUEST);


                } catch (Throwable e) {
                    e.printStackTrace();


                }
            }
        });

        mTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.radioOffer) {

                    mType = "Offre";
                } else mType = "Demande";


            }
        });
    }
    public void onClick(View v) {
        final int imageViewClicked = v.getId();
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(AdEditing.this);
        View mView = getLayoutInflater().inflate(R.layout.photo_picker_dialog, null);
        mTakePhoto = mView.findViewById(R.id.TakePhoto);
        mLoadPhoto = mView.findViewById(R.id.LoadPhoto);
        mBuilder.setView(mView);
        AlertDialog mTakePhotoDialog = mBuilder.create();
        mTakePhotoDialog.show();

        mTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (imageViewClicked) {
                    case R.id.AdPhoto1:
                        capturePhoto(11);

                        break;
                    case R.id.AdPhoto2:
                        capturePhoto(21);
                        break;
                    case R.id.AdPhoto3:
                        capturePhoto(31);
                        break;

                }


            }
        });

        mLoadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (imageViewClicked) {
                    case R.id.AdPhoto1:
                        loadPictureFromGallery(12);
                        break;
                    case R.id.AdPhoto2:
                        loadPictureFromGallery(22);
                        break;
                    case R.id.AdPhoto3:
                        loadPictureFromGallery(32);
                        break;

                }

            }
        });

        //  mTakePhotoDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


    }

    public void setSpinnersValues(int subCatId){
        SubCategory s = new SubCategory()  ;
        int i =0;
        int p =0 ;
        int x = 0 ;
        Category s2 = new Category() ;
        for(int j = 0 ; j<mSubCategoriesList.size();j++){
            if(mSubCategoriesList.get(j).getID()==subCatId){
               i=j ;
               x = mSubCategoriesList.get(j).getCatID();
               break;
            }
        }
        for(int j =0;j<mCategoriesList.size();j++){
            if(mCategoriesList.get(j).getID().equals(String.valueOf(x))){

          p=j ;
          break ;
            }
        }


           mCategorySpinner.setSelection(i);
           mSubCategorySpinner.setSelection(p);

        mCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




    }


    public void saveAdImages( ){
        String [] Resources = {AdImage1,AdImage2,AdImage3};

        for (int i =0;i<3;i++){
            saveSingleAdImage(Resources[i],mADID);
        }
        showAdCreatedDialog();


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
                params.put("ad_id",String.valueOf(AdId));
                params.put("ImageString",resource);
                params.put("Content-Type", "x-www-form-urlencoded");
                return params;
            }

        };
        VolleyAgent.getInstance(getApplicationContext()).addToRequestQueue(loginRequest);
        dismissProgressDialog();


    }
    public void showAdCreatedDialog(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("");
        builder1.setMessage("Changes saved");
        builder1.setCancelable(true);
        builder1.setNeutralButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent i = new Intent(AdEditing.this,ContainerActivity.class);
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
                mContactInfosErrorHint.setVisibility(View.INVISIBLE);
                return true ;
            }

        }
        mContactInfosErrorHint.setVisibility(View.VISIBLE);
        return false ;

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
}
