package com.firstgpsapp.proudlikeagod.locationbasedadsfinder;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AdDetails extends AppCompatActivity {

    private int mAdId ;
    private TextView mAdTitle , mAdCategory , mAdSubCategory , mAdPublishDate , mAdDescription ,mAdAddress , mAdderNameAndLasNamt  ;
    private Button mContactByPhone , mContactByEmail , mContactBySms  ;
    private GoogleMap mGoogleMap ;
    private ImageButton mShareButton ;
    private MapView mAdLocationMap ;
    private ViewPager mImageSlider ;
    private TextView[] mDots;
    private ProgressDialog mProgressD ;
    private LinearLayout mDotsLayout;
    private Bitmap[] mImagesResources = new Bitmap[3];
    private ScrollView mScrollView ;

    private LinearLayoutManager MyManager ;
    private CommentsAdapter CA ;

    private static String mTokenInvalid = "{\"error\":\"Token is Invalid\"}";
    private static String mTokenExpired ="{\"error\":\"Token is Expired\"}";
    private static String mSomeWentWrong ="{\"error\":\"Something is wrong\"}";

    private AuthenticationAgent mAuthAgent ;


    private AdImagesSliderAdapter mAdImagesSliderAdapter ;

    private String get_ad_details = "http://192.168.43.18:8000/api/GetAdDetails";
    private String get_ad_photos = "http://192.168.43.18:8000/api/GetAdTriplePhotos";
    private String get_category = "http://192.168.43.18:8000/api/GetCatInfos";
    private String get_subcategory = "http://192.168.43.18:8000/api/GetSubCatInfos";
    private String get_user_infos = "http://192.168.43.18:8000/api/GetUserInfos";
    private String get_auth_user = "http://192.168.43.18:8000/api/getAuthUser";
    private String add_new_comment ="http://192.168.43.18:8000/api/AddNewComment";
    private String retrieve_comments_list ="http://192.168.43.18:8000/api/RetrieveCommentsList";
    private String retrieve_single_profile_image="http://192.168.43.18:8000/api/LoadProfileImage";
    private String get_profile_infos="http://192.168.43.18:8000/api/GetProfileInfos";
    private String get_publisher_photo="http://192.168.43.18:8000/api/RetrieveSingleProfilePhoto";


    //all variable related to the ad
    private String imageResource1 , imageResource2 , imageResource3 ;
    private Bitmap bitmap1 , bitmap2 , bitmap3 ;
    private String mTitle , mDescription , mCategory , mSubCategory , mPublishingDate ;
    private String mFirstName , mLastName , mTelephone , mEmail ,mAddress ;
    private double mLatitude , mLongitude ;
    private int mADID , mUserID ,mSubCategoryID ,mCategoryID ;
    private String mUsername ;
    private String mPublisherProfileImageResource , mPublisherNameResource ;


    private ArrayList<Comment> CommentsList = new ArrayList<>();
    private ArrayList<Photo> ProfileImagesList = new ArrayList<>();




    //comment section widgets
    private EditText mCommentField ;
    private Button mAddNewComment ;
    private RecyclerView mCommentSectionRecyclerView ;
    private TextView mPublisherName ;
    private ImageView mPubliserProfileImage ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_details_layout);
          loadWidgets();
          extractFromBundle();
          retrieveAdDetails();
          loadListeners();


    }
    public void loadAdDetails(){

        mAdTitle.setText(mTitle);
        mAdPublishDate.setText(mPublishingDate);
        mAdDescription.setText(mDescription);
        mAdAddress.setText(mAddress);
        mAdderNameAndLasNamt.setText(mFirstName+" "+mLastName);

    }
    public void loadCatSubDetails(){
        mAdSubCategory.setText(mSubCategory);
        mAdCategory.setText(mCategory);

    }
    public void loadAdPhotos(){
        mImagesResources[0]=bitmap1;
        mImagesResources[1]=bitmap2;
        mImagesResources[2]=bitmap3;
        mAdImagesSliderAdapter = new AdImagesSliderAdapter(this,mImagesResources);
        mImageSlider.setAdapter(mAdImagesSliderAdapter);

    }
    //get the data sent by the onClick callback
    public void extractFromBundle(){
        Intent i = getIntent();
        Bundle b ;
        b = i.getExtras();
        if(b!=null){
        mAdId = b.getInt("Ad ID");}
        if(b!=null){
            mUserID = b.getInt("User ID");
        }




    }
    //initialize layout widgets
    public void loadWidgets(){
        mAuthAgent = new AuthenticationAgent(getApplicationContext());
        //initializing the widgets
        mAdLocationMap = (MapView)findViewById(R.id.AdLocation);
        mImageSlider = (ViewPager)findViewById(R.id.AdImagesViewPager);
        mDotsLayout=(LinearLayout)findViewById(R.id.DotsLayout);
        mAdTitle =(TextView)findViewById(R.id.AdTitle);
        mAdCategory=(TextView)findViewById(R.id.AdCategorie);
        mAdSubCategory=(TextView)findViewById(R.id.AdSubCategorie);
        mAdPublishDate=(TextView)findViewById(R.id.AdPublishDate);
        mAdDescription =(TextView)findViewById(R.id.AdDescription);
        mContactByEmail = (Button)findViewById(R.id.ContactByEmail);
        mContactByPhone=(Button)findViewById(R.id.ContactByPhone);
        mContactBySms=(Button)findViewById(R.id.ContactBySMS);
        mAdAddress=(TextView)findViewById(R.id.AddressNameDetail);
        mAdderNameAndLasNamt=(TextView)findViewById(R.id.AdderNameAndLastName);
        mShareButton=(ImageButton)findViewById(R.id.shareAdButton);
        mScrollView =(ScrollView)findViewById(R.id.mScrollView);


        mCommentSectionRecyclerView = (RecyclerView)findViewById(R.id.CommentsRCV);
        mAddNewComment =(Button)findViewById(R.id.AddNewComment);
        mCommentField=(EditText)findViewById(R.id.CommentField);

        mPublisherName =(TextView)findViewById(R.id.PublisherName);
        mPubliserProfileImage =(ImageView)findViewById(R.id.PublisherProfileImage) ;
        addDotsIndicator(0);


        MyManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        mCommentSectionRecyclerView.setLayoutManager(MyManager);
        CA = new CommentsAdapter(CommentsList,ProfileImagesList);
        mCommentSectionRecyclerView.setAdapter(CA);






    }

    //register the event listener for the widgets
    public void loadListeners() {
        ViewPager.OnPageChangeListener sliderListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addDotsIndicator(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
        mImageSlider.addOnPageChangeListener(sliderListener);

        mContactBySms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("smsto:"+mTelephone);
                Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                startActivity(it);

            }
        });
        mContactByPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+mTelephone));
                startActivity(intent);


            }
        });
        mContactByEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto",mEmail, null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Don't forget to mention Revilio !!");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });

        mAddNewComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuthAgent = new AuthenticationAgent(getApplicationContext());
                if(mAuthAgent.checkToken()){
                    getAuthUser(mAuthAgent.getToken(),"Ad Details");
                }
                else {
                    Intent i = new Intent(AdDetails.this,MainActivity.class);
                    Bundle b = new Bundle();
                    b.putString("calling activity","Ad Details");
                    b.putInt("Ad ID",mADID);
                    i.putExtras(b);
                    startActivity(i);

                }
            }
        });

        mShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String DataToShare = "Detailles "+mDescription + "Annonceur :"+mFirstName+" "+mLastName+" "+"Informations de contacte  :"+mTelephone+" , "+mEmail;
              /*  String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap1,mTitle, null);
                Uri bitmapUri = Uri.parse(bitmapPath);*/
                Intent shareIntent = new Intent();
                shareIntent.setType("image/*");
                shareIntent.setAction(Intent.ACTION_SEND);
                //shareIntent.putExtra(Intent.EXTRA_STREAM, bitmapUri );
                shareIntent.putExtra(Intent.EXTRA_TEXT, DataToShare);
                startActivity(Intent.createChooser(shareIntent, "Share This Ad"));
            }
        });

    }

// This method takes two coordinates parameters ( latitude and longtitude) and displays the corresponding position on a map view
    public void displayAdLocation(final double Lat , final double Long) {

        if (mAdLocationMap != null) {
            mAdLocationMap.onCreate(null);
            mAdLocationMap.onResume();
            mAdLocationMap.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mGoogleMap = googleMap;
                    mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
                    // Add a marker in Sydney and move the camera
                    LatLng AliMendjli = new LatLng(Lat, Long);

                    mGoogleMap.addMarker(new MarkerOptions().position(AliMendjli).title("Hello"));
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(AliMendjli));
                    mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(13.0f));

                }

            });
        }


    }

    class AdImagesSliderAdapter extends PagerAdapter {
        Context mContext;
        LayoutInflater mLayoutInflater ;
        Bitmap[] mSliderImages;

        public AdImagesSliderAdapter (Context mContext , Bitmap[] mSliderImages){
            this.mContext=mContext;
            this.mSliderImages=mSliderImages;
        }
        //Arrays holding ids of the resources



        @Override
        public int getCount() {
            return mSliderImages.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {

            return view==(RelativeLayout)object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            mLayoutInflater =(LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
            View view = mLayoutInflater.inflate(R.layout.ad_images_swiper_layout,container,false);

            ImageView m = view.findViewById(R.id.AdImage);

            m.setImageBitmap(mSliderImages[position]);

            container.addView(view);

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            container.removeView((RelativeLayout)object);
        }
    }
    public void addDotsIndicator ( int position){
        mDots = new TextView[3];
        mDotsLayout.removeAllViews();

        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.OrangeLight));
            mDotsLayout.addView(mDots[i]);
        }

        if (mDots.length > 0) {
            mDots[position].setTextColor(getResources().getColor(R.color.Purple));
        }


    }

    public void retrieveAdDetails (){
        StringRequest adDetailsRequest = new StringRequest(Request.Method.POST, get_ad_details, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dismissProgressDialog();
                try{
                JSONArray j = new JSONArray(response);
                JSONObject o = j.getJSONObject(0);
                mADID = o.getInt("ID_annonce");
                mUserID = o.getInt("ID_user");
                mSubCategoryID=o.getInt("ID_sous_categorie");
                mTitle = o.getString("Titre");
                mDescription=o.getString("Description");
                mPublishingDate =formatDate(o.getString("DatePublication"));
                mFirstName=o.getString("Nom");
                mLastName=o.getString("Prenom");
                mEmail=o.getString("Email");
                mTelephone=o.getString("Telephone");
                mAddress=o.getString("Address");
                mLatitude=o.getDouble("Latitude");
                mLongitude=o.getDouble("Longitude");
                retrieveAdPhotos(mADID);
                displayAdLocation(mLatitude,mLongitude);
                retrieveSubCategory(mSubCategoryID);
                    loadAdDetails();
                    retrieveCommentsList(mADID);
                    getProfileInfos(mUserID);
                    getPublisherProfileImage(mUserID);



                }
                catch (Throwable t){

                }


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
                params.put("AdId",mAdId+"");
                params.put("Content-Type", "x-www-form-urlencoded");
                return params;
            }

        };
        VolleyAgent.getInstance(getApplicationContext()).addToRequestQueue(adDetailsRequest);
        showProgressDialog();


    }


    public void retrieveAdPhotos(final int AdId){
        StringRequest adDetailsRequest = new StringRequest(Request.Method.POST, get_ad_photos, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                 dismissProgressDialog();
                try{
                    JSONArray j = new JSONArray(response);
                        JSONObject o = j.getJSONObject(0);
                        imageResource1 = o.getString("Resource");
                        o=j.getJSONObject(1);
                        imageResource2 = o.getString("Resource");
                        o=j.getJSONObject(2);
                        imageResource3=o.getString("Resource");

                        bitmap1=fromStringToBitmap(imageResource1);
                        bitmap2=fromStringToBitmap(imageResource2);
                        bitmap3=fromStringToBitmap(imageResource3);
                        loadAdPhotos();
                    }


                catch (Throwable t){

                }


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
                params.put("ad_id",String.valueOf(AdId));
                params.put("Content-Type", "x-www-form-urlencoded");
                return params;
            }

        };
        VolleyAgent.getInstance(getApplicationContext()).addToRequestQueue(adDetailsRequest);
        // showProgressDialog();

    }
    public void retrieveSubCategory(final int SubCategoryId){
        StringRequest adDetailsRequest = new StringRequest(Request.Method.POST, get_subcategory, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // dismissProgressDialog();
                try{
                    JSONArray j = new JSONArray(response);
                    JSONObject o = j.getJSONObject(0);
                    mSubCategory = o.getString("name");
                    mCategoryID = o.getInt("ID_categorie");
                    retrieveCategory(mCategoryID);


                }


                catch (Throwable t){

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }){
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("sub_cat_id",SubCategoryId+"");
                params.put("Content-Type", "x-www-form-urlencoded");
                return params;
            }

        };
        VolleyAgent.getInstance(getApplicationContext()).addToRequestQueue(adDetailsRequest);
        // showProgressDialog();



    }
    public void retrieveCategory(final int CategoryId){
        StringRequest adDetailsRequest = new StringRequest(Request.Method.POST, get_category, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // dismissProgressDialog();
                try{
                    JSONArray j = new JSONArray(response);
                    JSONObject o = j.getJSONObject(0);
                    mCategory = o.getString("name");
                   loadCatSubDetails();
                   dismissProgressDialog();
                }


                catch (Throwable t){


                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AdDetails.this, "error from inside retr cat", Toast.LENGTH_SHORT).show();


            }
        }){
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("id",CategoryId+"");
                params.put("Content-Type", "x-www-form-urlencoded");
                return params;
            }

        };
        VolleyAgent.getInstance(getApplicationContext()).addToRequestQueue(adDetailsRequest);
        // showProgressDialog();

    }
    public void getProfileInfos(final int mUserID){
        StringRequest publisherRequest = new StringRequest(Request.Method.POST, get_profile_infos, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray j = new JSONArray(response);
                    JSONObject l = j.getJSONObject(0);
                    mPublisherNameResource = l.getString("name");
                    mPublisherName.setText(mPublisherNameResource);

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
            public Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("id_user",String.valueOf(mUserID));
                return headers;
            }

        };
        VolleyAgent.getInstance(getApplicationContext()).addToRequestQueue(publisherRequest);
        // showProgressDialog();


    }

    public void getPublisherProfileImage (final int mUserID){
        StringRequest adDetailsRequest = new StringRequest(Request.Method.POST, get_publisher_photo, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{
                    JSONArray j = new JSONArray(response);
                    JSONObject o = j.getJSONObject(0);
                    mPublisherProfileImageResource = o.getString("Resource");
                    setPublisherProfileImage(mPublisherProfileImageResource);
                }
                catch (Throwable t){

                }


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
                params.put("user_id",String.valueOf(mUserID));
                return params;
            }

        };
        VolleyAgent.getInstance(getApplicationContext()).addToRequestQueue(adDetailsRequest);




    }

    public void showProgressDialog(){
        mProgressD = new ProgressDialog(AdDetails.this);
        mProgressD.setCancelable(true);
        mProgressD.setMessage("Working on it , hold on ......");
        mProgressD.show();

    }
    public void dismissProgressDialog(){
        mProgressD.dismiss();

    }


    public void setPublisherProfileImage(String mPublisherPhoto){
        if(mPublisherPhoto.equals("")){
            setDefaultProfileImage();

        }else {
            mPubliserProfileImage.setImageBitmap(fromStringToBitmap(mPublisherPhoto));
        }


    }
    public Bitmap fromStringToBitmap(String resource){
        byte[] byteArray = Base64.decode(resource,Base64.DEFAULT);
        Bitmap b = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
        return b ;

    }


    public void setDefaultProfileImage(){
        mPubliserProfileImage.setImageResource(R.drawable.default_profile_icon_purple);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAdDetails();
    }

    public void getAuthUser(final String token , final String caller){
        StringRequest loginRequest = new StringRequest(Request.Method.POST, get_auth_user, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(response.equals(mTokenExpired)||response.equals(mTokenInvalid)||response.equals(mSomeWentWrong)){
                    Intent i = new Intent(AdDetails.this,MainActivity.class);
                    Bundle b = new Bundle();
                    b.putString("calling activity",caller);
                    b.putInt("Ad ID",mADID);
                    i.putExtras(b);
                    startActivity(i);
                }else {
                    try {
                    JSONObject j = new JSONObject(response);
                    JSONObject l = j.getJSONObject("result");
                    mUserID= l.getInt("id");
                    mUsername = l.getString("name");
                    insertNewComment(mUserID,mAdId,mCommentField.getText().toString(),currentDate());
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mScrollView.fullScroll(View.FOCUS_DOWN);

                            }
                        },450);
                    }
                    catch (Throwable t){
                        Log.e("error",t.getMessage());
                    }


                }






            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }){

            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("token",token);
                return headers;
            }

        };
        VolleyAgent.getInstance(AdDetails.this).addToRequestQueue(loginRequest);

    }
    public class CommentsAdapter extends RecyclerView.Adapter<AdDetails.CommentsAdapter.MyViewHolder>{

        private List<Comment> ListComments ;
        private List<Photo> ListPhotos ;


        public CommentsAdapter(List<Comment> ListeAnnonces ,List<Photo> ListPhotos ){
            this.ListComments=ListeAnnonces;
            this.ListPhotos = ListPhotos ;
        }

        public CommentsAdapter(List<Comment> ListeAnnonces){
            this.ListComments = ListeAnnonces;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder{
            public TextView CommmentPublicationDate ;
            public TextView CommentContent ;
            public TextView HiddenCommentId ;
            public ImageView ProfilePhoto ;
            public TextView CommentatorUserName ;

            public MyViewHolder(View view){
                super(view);
              CommmentPublicationDate = (TextView)view.findViewById(R.id.CommentPublicationDate);
              CommentContent =(TextView)view.findViewById(R.id.CommentContent);

              ProfilePhoto =(ImageView)view.findViewById(R.id.AccountImage);
              CommentatorUserName =(TextView)view.findViewById(R.id.UserName);


            }
        }




        public int getItemViewType(int position){
            return position ;

        }

        @Override
        public void onBindViewHolder(AdDetails.CommentsAdapter.MyViewHolder holder, int position) {
            Comment Co =ListComments.get(position);

            ((MyViewHolder)holder).CommentatorUserName.setText(Co.getUsername());
            ((MyViewHolder)holder).CommentContent.setText(Co.getCommentContent());
            ((MyViewHolder)holder).CommmentPublicationDate.setText(Co.getPublicationDate());
            ((MyViewHolder)holder).ProfilePhoto.setImageBitmap(getCorrespondingPhoto(Co.getUserId()));




        }


        public int getItemCount(){
            return ListComments.size();
        }

        public AdDetails.CommentsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_comment_layout,parent,false);

            return new MyViewHolder(v);}



        public Bitmap getCorrespondingPhoto( int AdId){
            for(Photo p:ListPhotos){
                if(p.getID_annonce()==AdId){
                    return fromStringToBitmap(p.getResource());
                }
            }
            return  BitmapFactory.decodeResource(getResources(),R.mipmap.default_profile_image_white) ;
        }
        public Bitmap fromStringToBitmap(String resource){
            byte[] byteArray = Base64.decode(resource,Base64.DEFAULT);
            Bitmap b = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
            return b ;

        }



    }
    public void insertNewComment(final int UserId , final int AdId ,final String Content ,final String PublicationDate){
       StringRequest adPublishingRequest = new StringRequest(Request.Method.POST,add_new_comment , new Response.Listener<String>() {
           @Override
           public void onResponse(String response) {

               CommentsList.add(new Comment(mUsername,Content,PublicationDate,AdId,mUserID));
               CA.notifyDataSetChanged();
               retrieveSingleProfileImage(mUserID);
               mCommentField.setText("");

           }



       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {



           }
       }){
           @Override
           public Map<String, String> getParams() throws AuthFailureError {
               HashMap<String, String> params = new HashMap<String, String>();
               params.put("UserId",String.valueOf(UserId));
               params.put("AdId",String.valueOf(AdId));
               params.put("CommentContent",Content);
               params.put("CommentPublicationDate",PublicationDate);
               params.put("Content-Type", "x-www-form-urlencoded");
               return params;
           }

       };
       VolleyAgent.getInstance(getApplicationContext()).addToRequestQueue(adPublishingRequest);


   }
    public void retrieveCommentsList(final int AdId){
           StringRequest adPublishingRequest = new StringRequest(Request.Method.POST,retrieve_comments_list , new Response.Listener<String>() {
               @Override
               public void onResponse(String response) {
                   try {
                       JSONArray  j = new JSONArray(response);
                       JSONObject o ;
                       for(int i =0 ; i<j.length() ; i++) {
                           o = j.getJSONObject(i);
                          CommentsList.add(new Comment(o.getString("name"),o.getString("Contenu"),o.getString("DatePublication"),o.getInt("id"),o.getInt("ID_user")));
                          CA.notifyDataSetChanged();
                          retrieveSingleProfileImage(o.getInt("ID_user"));
                       }
                       dismissProgressDialog();

                   }catch (Throwable t){
                       Log.e("j", "onResponse: "+t.getMessage() );
                   }





               }
           }, new Response.ErrorListener() {
               @Override
               public void onErrorResponse(VolleyError error) {

                   Toast.makeText(AdDetails.this, error.getMessage(), Toast.LENGTH_SHORT).show();

               }
           }){
               @Override
               public Map<String, String> getParams() throws AuthFailureError {
                   HashMap<String, String> params = new HashMap<String, String>();
                   params.put("AdId",String.valueOf(AdId));

                   return params;
               }

           };
           VolleyAgent.getInstance(getApplicationContext()).addToRequestQueue(adPublishingRequest);
           showProgressDialog();


       }


    public void retrieveSingleProfileImage(final int UserId){

           StringRequest retrieveProfileImage = new StringRequest(Request.Method.POST,retrieve_single_profile_image, new Response.Listener<String>() {
               @Override
               public void onResponse(String response) {
               try{
                   JSONArray j = new JSONArray(response);
                   JSONObject o ;
                       o = j.getJSONObject(0);
                       ProfileImagesList.add(new Photo(o.getInt("id"),o.getInt("id_user"),o.getString("Resource")));
                       CA.notifyDataSetChanged();


               }catch (Throwable t){
                   Log.e("j", "onResponse: "+t.getMessage() );
               }

               }
           }, new Response.ErrorListener() {
               @Override
               public void onErrorResponse(VolleyError error) {



               }
           }){
               @Override
               public Map<String, String> getParams() throws AuthFailureError {
                   HashMap<String, String> params = new HashMap<String, String>();
                   params.put("id_user",String.valueOf(UserId));
                   return params;
               }

           };
           VolleyAgent.getInstance(getApplicationContext()).addToRequestQueue(retrieveProfileImage);


       }




    public String currentDate(){

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);
        return formattedDate ;

    }
    public String formatDate(String s){
        try{
            Date c = new SimpleDateFormat("yyyy-MM-dd",Locale.US).parse(s);
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
            String formattedDate = df.format(c);
            return formattedDate ;}
        catch (ParseException p){
            Log.e("", "formatDate: "+p.getMessage());
        }
       return "";

    }

    public void scrollToBottom(){


    }
}
