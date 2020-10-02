package com.firstgpsapp.proudlikeagod.locationbasedadsfinder;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class SearchFragment extends Fragment implements LocationListener {

   private RecyclerView mAdsRCV ;
   private AdsAdapter AD ;
   private Spinner mCategorySpinner ,mSubCategorySpinner;
   private ArrayList<Annonce> ListeAnnonces ;
   private Button mSearchButton ;
   private RadioGroup mTypeRadioGroup ;
   private CheckBox mOfferCheckBox , mDemandCheckBox , mBothCheckBox ;
   private String retrieve_categories_url="http://192.168.43.18:8000/api/RetrieveCategories" ;
   private String retrieve_subcategories_url="http://192.168.43.18:8000/api/RetrieveSubCategories" ;
   private String retrieve_all_ads_url="http://192.168.43.18:8000/api/GetFullAdsListWithPhotos" ;
   private String retrieve_all_photos_url="http://192.168.43.18:8000/api/GetAllPhotos" ;
   private String retrieve_all_ads_only_url="http://192.168.43.18:8000/api/GetFullAdsList" ;
    private String filter_ads_list_url="http://192.168.43.18:8000/api/FilterAdsList" ;
    private String get_ad_photos ="http://192.168.43.18:8000/api/GetAdPhotos";
   private ProgressDialog mProgressD ;
   private ArrayList<Category> mCategoriesList ;
   private ArrayList<SubCategory> mSubCategoriesList ;
   private ArrayList<Annonce> mAdsList = new ArrayList<>() ,backUpAdsList = new ArrayList<>() , notFilteredAdsList = new ArrayList<>();
   private ArrayList<Photo> mPhotosList =new ArrayList<>() ;
   private SpinnerAdapter mCategorySpinnerAdapter ;
   private SpinnerAdapter2 mSubCategorySpinnerAdapter ;
   private LocationManager locationManager;
   private String provider;
   private Location mCurrentLocation ;
   private  LocationListener locationChangedListener;
   private int mSub_cat_id , mSubIdSelected ;
   private String typeCriteria ;
    private Handler handler ;
    private  Runnable runnable ;
    private LinearLayout mEmptySearchHintLayout ;

   private LinearLayoutManager MyManager ;

   private FusedLocationProviderClient mFusedLocationClient ;






    public SearchFragment() {
        // Required empty public constructor
    }


    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.search_fragment_layout, container, false);
        //checking the build version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ActivityCompat.checkSelfPermission(getActivity().getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity().getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 99);
        }else {getLocationupdates();}


        mAdsRCV = view.findViewById(R.id.RCV);
        mEmptySearchHintLayout = view.findViewById(R.id.emptyResultHint);
        mEmptySearchHintLayout.setVisibility(View.INVISIBLE);

        //intiliazing the check boxes
        mDemandCheckBox = view.findViewById(R.id.DemandCheckBox);
        mOfferCheckBox = view.findViewById(R.id.OfferCheckBox);
        mBothCheckBox = view.findViewById(R.id.BothTypesCheckBox);
        mBothCheckBox.setChecked(true);

        //initializing the fused location provider
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);

        //define and initialize a criteria
        Criteria criteria = new Criteria();


        //initialize provider
        provider = locationManager.getBestProvider(criteria, false);

        mDemandCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                      if(isChecked){
                          mOfferCheckBox.setChecked(false);
                          mBothCheckBox.setChecked(false);

                      }else {
                          mDemandCheckBox .setChecked(false);
                    }


            }
        });
        mOfferCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    mDemandCheckBox.setChecked(false);
                    mBothCheckBox.setChecked(false);

                }else {

                    mOfferCheckBox.setChecked(false);}
            }

        });
        mBothCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                    mDemandCheckBox.setChecked(false);
                    mOfferCheckBox.setChecked(false);
                }else {

                    mBothCheckBox .setChecked(false);}
                }


        });

       MyManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
       // CustomLinearLayoutManager mManager = new CustomLinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        mAdsRCV.setLayoutManager(MyManager);

        mCategoriesList = new ArrayList<Category>();
        mSubCategoriesList= new ArrayList<SubCategory>();



        //initializing the spinners
        mCategorySpinner = view.findViewById(R.id.CategorySearchSpinner);
        mSubCategorySpinner =view.findViewById(R.id.SubCategorySearchSpinner);


        //get the ads list and set the adapter to the recycler view
        locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);



        //initialize provider



        AD = new AdsAdapter(mAdsList,mPhotosList);
        mAdsRCV.setAdapter(AD);
       retrieveAdsListAlone();





        //setting the listeners for the spinners
        mCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                mSubCategoriesList.clear();
                retrieveSubCategoriesList(mCategoriesList.get(pos).getID()); }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        mSubCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSubIdSelected = mSubCategoriesList.get(position).getID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //initializing the search button and the radio group
        mSearchButton = view.findViewById(R.id.SearchButton);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             if(mBothCheckBox.isChecked()){
                    filterAdsList("Both",mSubIdSelected);

                }else if(mOfferCheckBox.isChecked()){
                    filterAdsList("Offre",mSubIdSelected);

                }else if(mDemandCheckBox.isChecked()){
                   filterAdsList("Demande",mSubIdSelected);
                }


            }
        });


        retrieveCategoriesList();





        return view ;
    }
    class MyRecyItemClickLisetner implements  View.OnClickListener{
        public void onClick(View v){
            TextView t = v.findViewById(R.id.HiddenAdId);
            int id =Integer.parseInt(t.getText().toString());
            Intent i = new Intent(getActivity(),AdDetails.class);
            Bundle b = new Bundle();
            b.putInt("Ad ID",id);
            i.putExtras(b);
            startActivity(i);


        }
    }
    class MyRecycItemTouchListener implements  View.OnTouchListener{
        public boolean onTouch(View v , MotionEvent m){
            if( handler!=null){
                handler.removeCallbacks(runnable);

            }
            return  true ;

        }
    }

    public class AdsAdapter extends RecyclerView.Adapter<AdsAdapter.MyViewHolder>{

        private List<Annonce> ListeAnnonces ;
        private List<Photo> ListePhotos ;


        public AdsAdapter(List<Annonce> ListeAnnonces ,List<Photo> ListePhotos ){
            this.ListeAnnonces=ListeAnnonces;
            this.ListePhotos = ListePhotos ;
        }

        public AdsAdapter(List<Annonce> ListeAnnonces){
            this.ListeAnnonces = ListeAnnonces;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
            public TextView Titre ;
            public TextView Distance ;
            public TextView Location ;
            public TextView DatePublication ;
            public ImageView ImageAnnonce ;
            public TextView HiddenID ;
            public ProgressBar LoadingImageProgressBar ;
            public ProgressBar DistanceProgressBar ;
            public MyViewHolder(View view){
                super(view);
                Titre=(TextView)view.findViewById(R.id.Titre);
                Location=(TextView)view.findViewById(R.id.Location);
                DatePublication=(TextView)view.findViewById(R.id.Date_publication);
                ImageAnnonce=(ImageView)view.findViewById(R.id.Image);
                Distance =(TextView)view.findViewById(R.id.Distance);
                HiddenID =(TextView)view.findViewById(R.id.HiddenAdId);
                HiddenID.setVisibility(View.INVISIBLE);
                DistanceProgressBar = view.findViewById(R.id.distanceProgressBar);
                DistanceProgressBar.setVisibility(View.VISIBLE);
                LoadingImageProgressBar = view.findViewById(R.id.loadingImageProgressBar);
                LoadingImageProgressBar.setVisibility(View.VISIBLE);
                view.setOnClickListener(this);


            }
            public void onClick(View view){
                TextView t = view.findViewById(R.id.HiddenAdId);
                int id =Integer.parseInt(t.getText().toString());
                Intent i = new Intent(getActivity(),AdDetails.class);
                Bundle b = new Bundle();
                b.putInt("Ad ID",id);
                i.putExtras(b);
                startActivity(i);

            }
        }




        public int getItemViewType(int position){
            return position ;

        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            Annonce An =ListeAnnonces.get(position);

            ((MyViewHolder)holder).DatePublication.setText(formatDate(String.valueOf(An.getDateDePublication())));
            ((MyViewHolder)holder).Titre.setText(An.getTitre());
            ((MyViewHolder)holder).Location.setText(An.getAdresse());
            ((MyViewHolder)holder).HiddenID.setText(String.valueOf(An.getID()));

            //getting the current location
            Location current = new Location("");
            current.setLatitude(An.getLatitude());
            current.setLongitude(An.getLongitude());


            // calculate and set the distance between the current location and the ad location
            String s = calculateDistance(current);
            if(!s.equals("")){
                ((MyViewHolder)holder).DistanceProgressBar.setVisibility(View.INVISIBLE);
            }
            ((MyViewHolder)holder).Distance.setText(calculateDistance(current));

            //set one of the images of the ad
            Bitmap b = getCorrespondingPhoto(An.getID()) ;
           // ((MyViewHolder)holder).LoadingImageProgressBar.setVisibility(View.GONE);
            ((MyViewHolder)holder).ImageAnnonce.setImageBitmap(b);


        }


        public int getItemCount(){
            return ListeAnnonces.size();
        }

        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_annonce_search_horiz,parent,false);
           // v.setOnClickListener(new MyRecyItemClickLisetner());

            return new MyViewHolder(v);}

           public String calculateDistance(Location AdLocation){
            if(mCurrentLocation==null){
                return "";
           }else{

               return String.valueOf(Math.round(mCurrentLocation.distanceTo(AdLocation)/1000) )+"Km~";}

            }

            public Bitmap getCorrespondingPhoto( int AdId){
            for(Photo p:ListePhotos){
                if(p.getID_annonce()==AdId){
                    return fromStringToBitmap(p.getResource());
                }
            }
             return  null ;
            }
            public Bitmap fromStringToBitmap(String resource){
                byte[] byteArray = Base64.decode(resource,Base64.DEFAULT);
                Bitmap b = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
                return b ;

            }
        public String formatDate(String s){
            try{
                Date c = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(s);
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                String formattedDate = df.format(c);
                return formattedDate ;}
            catch (ParseException p){
                Log.e("", "formatDate: "+p.getMessage());
            }
            return "";

        }



    }

    public void autoScrollAdsList(){
        mAdsRCV.setNestedScrollingEnabled(false);
        final int scrollSpeed =80;   // Scroll Speed in Milliseconds
         handler = new Handler();
         runnable = new Runnable() {
            int x = 15;        // Pixels To Move/Scroll
            boolean flag = true;
            // Find Scroll Position By Accessing RecyclerView's LinearLayout's Visible Item Position
            int scrollPosition = MyManager.findFirstVisibleItemPosition();
            int arraySize = mAdsList.size();  // Gets RecyclerView's Adapter's Array Size

            @Override
            public void run() {
                if (scrollPosition < arraySize) {
                    if (scrollPosition == arraySize - 1) {
                        flag = false;
                    } else if (scrollPosition <= 1) {
                        flag = true;
                    }
                    if (!flag) {
                        try {
                            // Delay in Seconds So User Can Completely Read Till Last String
                            TimeUnit.SECONDS.sleep(1);
                            mAdsRCV.scrollToPosition(0);  // Jumps Back Scroll To Start Point
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    // Know The Last Visible Item
                    scrollPosition = MyManager.findLastCompletelyVisibleItemPosition();

                    mAdsRCV.smoothScrollBy(x, 0);
                    handler.postDelayed(this, scrollSpeed);
                }
            }
        };
        handler.postDelayed(runnable, scrollSpeed);
    }


    public void retrieveAdsList(){
        StringRequest RetrieveAdsRequest = new StringRequest(Request.Method.GET,retrieve_all_ads_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

               try {
                JSONArray Adsliste = new JSONArray(response);
                for (int i=0;i<Adsliste.length();i=i+3){
                    JSONObject obj = Adsliste.getJSONObject(i);
                    Annonce an= new Annonce(obj.getInt("ID_annonce"),obj.getInt("ID_user"),obj.getString("Titre"),obj.getString("Type"),obj.getInt("ID_sous_categorie"),obj.getString("Description"),obj.getDouble("Latitude"),obj.getDouble("Longitude"),obj.getString("Nom"),obj.getString("Prenom"),obj.getString("Email"),obj.getString("Telephone"),obj.getString("Address"),obj.getString("DatePublication"));
                     mAdsList.add(an);
                     AD.notifyDataSetChanged();
                     Photo ph = new Photo(obj.getInt("id"),obj.getInt("id_annonce"),obj.getString("Resource"));
                     mPhotosList.add(ph);
                     AD.notifyDataSetChanged();
                     JSONObject obj2 = Adsliste.getJSONObject(i+1);
                    Photo ph2 = new Photo(obj2.getInt("id"),obj2.getInt("id_annonce"),obj2.getString("Resource"));
                    mPhotosList.add(ph2);
                    AD.notifyDataSetChanged();
                    JSONObject obj3 = Adsliste.getJSONObject(i+2);
                    Photo ph3 = new Photo(obj3.getInt("id"),obj3.getInt("id_annonce"),obj3.getString("Resource"));
                    mPhotosList.add(ph3);
                    AD.notifyDataSetChanged();


                } }
                    catch(Throwable t){
                        Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                   }
                   backUpAdsList =(ArrayList<Annonce>) mAdsList.clone();
                dismissProgressDialog();

                //autoScrollAdsList();




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
               dismissProgressDialog();
            }
        });
        VolleyAgent.getInstance(getContext()).addToRequestQueue(RetrieveAdsRequest);
        showProgressDialog();





    }

    public void retrieveAdsListAlone(){
        StringRequest RetrieveAdsRequest = new StringRequest(Request.Method.GET,retrieve_all_ads_only_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray Adsliste = new JSONArray(response);
                    for (int i=0;i<Adsliste.length();i++){
                        JSONObject obj = Adsliste.getJSONObject(i);
                        Annonce an= new Annonce(obj.getInt("ID_annonce"),obj.getInt("ID_user"),obj.getString("Titre"),obj.getString("Type"),obj.getInt("ID_sous_categorie"),obj.getString("Description"),obj.getDouble("Latitude"),obj.getDouble("Longitude"),obj.getString("Nom"),obj.getString("Prenom"),obj.getString("Email"),obj.getString("Telephone"),obj.getString("Address"),obj.getString("DatePublication"));
                        mAdsList.add(an);
                        AD.notifyDataSetChanged();
                    }
                    dismissProgressDialog();
                retrieveAdsPhotos(mAdsList);}
                catch(Throwable t){
                    Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                }
                backUpAdsList =(ArrayList<Annonce>) mAdsList.clone();


                //autoScrollAdsList();




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissProgressDialog();

            }
        });
        VolleyAgent.getInstance(getContext()).addToRequestQueue(RetrieveAdsRequest);
         showProgressDialog();


    }
    public void retrieveAdsPhotosAlone(){
        StringRequest RetrieveAdsRequest = new StringRequest(Request.Method.GET,retrieve_all_photos_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray Adsliste = new JSONArray(response);
                    for (int i=0;i<Adsliste.length();i++){
                        JSONObject obj = Adsliste.getJSONObject(i);
                        Photo ph3 = new Photo(obj.getInt("id"),obj.getInt("id_annonce"),obj.getString("Resource"));
                        mPhotosList.add(ph3);
                        AD.notifyDataSetChanged();

                    } }
                catch(Throwable t){
                    Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                }


                //autoScrollAdsList();




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        VolleyAgent.getInstance(getContext()).addToRequestQueue(RetrieveAdsRequest);


    }

    public void retrievesingleAdImages(final int ad_id){
        StringRequest RetrieveAdsRequest = new StringRequest(Request.Method.POST,get_ad_photos, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject obj = new JSONObject(response);
                    Photo ph = new Photo(obj.getInt("id"),obj.getInt("id_annonce"),obj.getString("Resource"));
                    mPhotosList.add(ph);
                    AD.notifyDataSetChanged();}

                catch(JSONException t){


                }




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("ad_id",String.valueOf(ad_id));
                //headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }};
        VolleyAgent.getInstance(getContext()).addToRequestQueue(RetrieveAdsRequest);



    }

    public void  retrieveAdsPhotos(ArrayList<Annonce> UserCreatedAds){
        for(Annonce a : UserCreatedAds){
            retrievesingleAdImages(a.getID());
        }

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
                        mSub_cat_id = obj.getInt("ID_sous_categorie");
                        cat.setName(obj.getString("name"));
                        cat.setID(obj.getInt("ID_sous_categorie"));
                        mSubCategoriesList.add(cat);}}
                catch(Throwable t){

                }


                mSubCategorySpinnerAdapter = new SpinnerAdapter2(getActivity(),mSubCategoriesList,android.R.layout.simple_list_item_1);
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
        VolleyAgent.getInstance(getContext()).addToRequestQueue(RetrieveSubCategoriesRequest);


    }

    public void retrieveCategoriesList(){
        StringRequest RetrieveCategoriesRequest = new StringRequest(Request.Method.GET, retrieve_categories_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONArray liste = new JSONArray(response);
                    for (int i=0;i<liste.length();i++){
                        JSONObject obj = liste.getJSONObject(i);
                        Category cat= new Category(obj.getString("ID_categorie"),obj.getString("name"));
                        mCategoriesList.add(cat);}}
                catch(Throwable t){
                    t.printStackTrace();
                }


                mCategorySpinnerAdapter = new SpinnerAdapter(getActivity(),mCategoriesList,android.R.layout.simple_list_item_1);
                mCategorySpinner.setAdapter(mCategorySpinnerAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });
        VolleyAgent.getInstance(getContext()).addToRequestQueue(RetrieveCategoriesRequest);



    }

    public void filterAdsList(final String type , final int sub_cat_id){
        StringRequest FilterAdsListRequest = new StringRequest(Request.Method.POST,filter_ads_list_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    mAdsList.clear();
                    AD.notifyDataSetChanged();
                    JSONArray Adsliste = new JSONArray(response);
                    if(Adsliste.length()==0){
                        mEmptySearchHintLayout.setVisibility(View.VISIBLE);
                    }else mEmptySearchHintLayout.setVisibility(View.INVISIBLE);
                    for (int i=0;i<Adsliste.length();i=i+3){
                        JSONObject obj = Adsliste.getJSONObject(i);
                        Annonce an= new Annonce(obj.getInt("ID_annonce"),obj.getInt("ID_user"),obj.getString("Titre"),obj.getString("Type"),obj.getInt("ID_sous_categorie"),obj.getString("Description"),obj.getDouble("Latitude"),obj.getDouble("Longitude"),obj.getString("Nom"),obj.getString("Prenom"),obj.getString("Email"),obj.getString("Telephone"),obj.getString("Address"),obj.getString("DatePublication"));
                        mAdsList.add(an);
                        AD.notifyDataSetChanged();
                        Photo ph = new Photo(obj.getInt("id"),obj.getInt("id_annonce"),obj.getString("Resource"));
                        mPhotosList.add(ph);
                        AD.notifyDataSetChanged();
                        JSONObject obj2 = Adsliste.getJSONObject(i+1);
                        Photo ph2 = new Photo(obj2.getInt("id"),obj2.getInt("id_annonce"),obj2.getString("Resource"));
                        mPhotosList.add(ph2);
                        AD.notifyDataSetChanged();
                        JSONObject obj3 = Adsliste.getJSONObject(i+2);
                        Photo ph3 = new Photo(obj3.getInt("id"),obj3.getInt("id_annonce"),obj3.getString("Resource"));
                        mPhotosList.add(ph3);
                        AD.notifyDataSetChanged();


                    } dismissProgressDialog();}
                catch(Throwable t){

                }




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Network error , please check your connection and try again", Toast.LENGTH_LONG).show();
                dismissProgressDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                params.put("type",type);
                params.put("sub_cat",String.valueOf(sub_cat_id));
                return params;
            }

        };
        VolleyAgent.getInstance(getContext()).addToRequestQueue(FilterAdsListRequest);
        showProgressDialog();

    }


    public void filterAdsListOptimizedVersion(final String type , final int mSub_cat_id){


     mAdsList.clear();
     AD.notifyDataSetChanged();
     showProgressDialog();
     if( type.equals("Both")){
     for(Annonce n : backUpAdsList){
         if(n.getType().equals(type)&&n.getSousCategorie()==mSub_cat_id){
           mAdsList.add(n);
             AD.notifyDataSetChanged();
         }

     }}else {
         for(Annonce n : backUpAdsList){
             if(n.getSousCategorie()==mSub_cat_id){
                 mAdsList.add(n);
                 AD.notifyDataSetChanged();
             }

         }

     }
     dismissProgressDialog();

    }


    //******************getting the current location of the user

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 99 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
           getLocationupdates();
        }

    }







    private void getLocationupdates() {

        locationChangedListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {


            mCurrentLocation = location;
            if(location!=null){
                AD.notifyDataSetChanged();
            }

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 99);
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 50, locationChangedListener);
    }
    public void onShareClick(View view) {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        Toast.makeText(getContext(), ""+lastKnownLocation.getLatitude(), Toast.LENGTH_SHORT).show();

        if (locationChangedListener != null)
            locationManager.removeUpdates(locationChangedListener);
    }






    public void showProgressDialog(){
        mProgressD = new ProgressDialog(getActivity());
        mProgressD.setCancelable(true);
        mProgressD.setMessage("Working on it , hold on ......");
        mProgressD.show();

    }
    public void dismissProgressDialog(){
        mProgressD.dismiss();

    }

    @Override
    public void onResume() {
        super.onResume();
        if(handler!=null){
       handler.postDelayed(runnable,80);}

    }

    @Override
    public void onPause() {
        super.onPause();
        if(handler!=null){
        handler.removeCallbacks(runnable);}
    }

    public void getLastKnownLocation(){


    }
    public void onLocationChanged(Location location){
        double lat = location.getLatitude();
        double lng = location.getLongitude();

         mCurrentLocation = location;

    }

    public void onStatusChanged(String provider,int status ,Bundle extras){

    }

    public void onProviderEnabled(String provider){

    }

    public void onProviderDisabled(String provider){


    }
}
