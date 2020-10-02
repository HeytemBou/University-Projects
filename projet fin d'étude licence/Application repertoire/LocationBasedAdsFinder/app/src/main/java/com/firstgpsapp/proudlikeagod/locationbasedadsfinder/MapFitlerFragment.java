package com.firstgpsapp.proudlikeagod.locationbasedadsfinder;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Layout;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
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
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class MapFitlerFragment extends Fragment implements GoogleMap.OnMarkerClickListener{
    private GoogleMap mGoogleMap ;
    private MapView mMapView ;
    private View mView ;
    private DrawerLayout mDL ;
    private Button mB ,ff ;
    private ImageButton mOpenFilterButton ;
    private LinearLayout LinearL ;
    private RadioGroup mTypeGroup ;
    private Spinner mSpinnerCategory ;
    private LocationManager locationManager;
    private View  mHeaderLayout;
    private ProgressDialog mProgressD;
    private NavigationView mNavigationView ;
    private String retrieve_all_coordinates_url ="http://192.168.43.18:8000/api/GetCoordinates" ;
    private ArrayList<Marker> mMarkersList = new ArrayList<>() ;
    CustomInfosWindowAdapter mCustomMarkerAdapter ;
    private Spinner mCategorySpinner , mSubCategorySpinner  ;
    private RadioButton mBothRadio , mDemandRadio , mOfferRadio , m24HoursRadio , m7DaysRadio , m30DaysRadio , mAroundMeRadio , mInMyCityRadio , mInMyRegionRadio , mEveryWhere ;
    private RadioGroup mTypeRadioGroup , mDistanceRadioGroup , mTimeIntervalRadioGroup ;
    private Button mFilerButton ;
    private SpinnerAdapter mCategoryAdapter ;
    private SpinnerAdapter2 mSubCategoryAdapter ;
    private ArrayList<Category> mCategoriesList ;
    private ArrayList<SubCategory> mSubCategoriesList ;
    private String retrieve_categories_url="http://192.168.43.18:8000/api/RetrieveCategories" ;
    private String retrieve_subcategories_url="http://192.168.43.18:8000/api/RetrieveSubCategories" ;
    private int mSub_cat_id ;
    private Location mCurrentLocation ;
    private LocationListener locationChangedListener;
    // search parameters chosen by the user
    private int mChosenSubCat , mChosenTimeInterval =1 ,mChosenDistance =3;
    private String mChosenType ="Both" ;
    private SeekBar mDistanceSeekBar ;
    private TextView mPickedDistance , mKm , mGPSIndicator ;
    ArrayList<Marker> mDownloadedMarkersList = new ArrayList<>();





    public MapFitlerFragment() {
    }


    public static MapFitlerFragment newInstance() {
        MapFitlerFragment fragment = new MapFitlerFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView= inflater.inflate(R.layout.map_filter_fragment, container, false);
        return mView ;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapView=view.findViewById(R.id.AdsMap);
        mDL=view.findViewById(R.id.mDrawerLayout);
      /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ActivityCompat.checkSelfPermission(getActivity().getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity().getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 99);
        }else {getLocationupdates();}*/
      getLocationupdates();


        mNavigationView=view.findViewById(R.id.SearchParametersNavigationDrawer);
        mHeaderLayout= mNavigationView.inflateHeaderView(R.layout.drawer_layout_header);
        //initializing the header layout widgets : 2 spinners , 1 button , 10 radio buttons
        mCategorySpinner = mHeaderLayout.findViewById(R.id.CatSpinner);
        mSubCategorySpinner = mHeaderLayout.findViewById(R.id.SubCatSpinner);
        mFilerButton=mHeaderLayout.findViewById(R.id.FilterButton);
        mBothRadio=mHeaderLayout.findViewById(R.id.Both);
        mDemandRadio = mHeaderLayout.findViewById(R.id.Demand);
        mOfferRadio=mHeaderLayout.findViewById(R.id.Offer);
        m24HoursRadio=mHeaderLayout.findViewById(R.id.m24Hours);
        m7DaysRadio=mHeaderLayout.findViewById(R.id.m7Days);
        m30DaysRadio=mHeaderLayout.findViewById(R.id.m30Days);
        mAroundMeRadio=mHeaderLayout.findViewById(R.id.AroundMe);
        mInMyCityRadio = mHeaderLayout.findViewById(R.id.InMyCity);
        mInMyRegionRadio=mHeaderLayout.findViewById(R.id.InMyRegion);
        mEveryWhere=mHeaderLayout.findViewById(R.id.Everywhere);
        mGPSIndicator = mHeaderLayout.findViewById(R.id.GPSindicator);

        mTypeGroup =mHeaderLayout.findViewById(R.id.typeRadioGroup);
        mTimeIntervalRadioGroup =mHeaderLayout.findViewById(R.id.timeIntervalRadioGroup);
        mDistanceRadioGroup=mHeaderLayout.findViewById(R.id.distanceRadioGroup);
        mDistanceSeekBar = mHeaderLayout.findViewById(R.id.distancePicker);

        mKm = mHeaderLayout.findViewById(R.id.Km);
        mPickedDistance =mHeaderLayout.findViewById(R.id.seekBarValueDisplayer);
        mKm.setVisibility(View.INVISIBLE);
        mPickedDistance.setVisibility(View.INVISIBLE);
        mDistanceSeekBar.setVisibility(View.INVISIBLE);


        mCategoriesList = new ArrayList<Category>();
        mSubCategoriesList= new ArrayList<SubCategory>();




        getAllCoordinates();

        //inflate the drawer header layout to a usable java object
     //  View  mHeaderLayout = onGetLayoutInflater(savedInstanceState).inflate(R.layout.drawer_layout_header,null);
       // View  mSpinnerLayout = onGetLayoutInflater(savedInstanceState).inflate(R.layout.spinner_layout,null);
        //disabling gesture on the navigation drawer
        mOpenFilterButton=view.findViewById(R.id.OpenFilterButton);
        mDL.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);






        loadListeners();

        retrieveCategoriesList();





    }
  /*  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 99 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            getLocationupdates();
        }

    }*/
    private void getLocationupdates() {

        locationChangedListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {

                if(location!=null){
                    mCurrentLocation = location ;
                    mGPSIndicator.setVisibility(View.INVISIBLE);
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
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationChangedListener);
    }
    public void loadListeners(){
        mOpenFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDL.openDrawer(GravityCompat.START);

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
            mChosenSubCat = mSubCategoriesList.get(position).getID();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {


        }
    });

    mTypeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch(checkedId){
                case R.id.Both :
                    mChosenType = "Both";
                    break ;

                case R.id.Demand :
                    mChosenType="Demande";
                    break ;

                case R.id.Offer :
                    mChosenType="Offre";
                    break ;
                default:
                    mChosenType="Both";
                    break ;

            }
        }
    });
    mTimeIntervalRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId){
                case R.id.m24Hours :
                    mChosenTimeInterval = 1 ;
                    break ;
                case R.id.m7Days :
                    mChosenTimeInterval=7 ;
                    break ;
                case R.id.m30Days :
                    mChosenTimeInterval=30 ;
                    break ;
                 default:
                     mChosenTimeInterval=1 ;

            }
        }
    });
    mDistanceRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId){
                case R.id.AroundMe :
                    mChosenDistance=3 ;
                    mKm.setVisibility(View.INVISIBLE);
                    mPickedDistance.setVisibility(View.INVISIBLE);
                    mDistanceSeekBar.setVisibility(View.INVISIBLE);

                    break ;
                case R.id.InMyCity :
                    mKm.setVisibility(View.INVISIBLE);
                    mPickedDistance.setVisibility(View.INVISIBLE);
                    mDistanceSeekBar.setVisibility(View.INVISIBLE);
                    mChosenDistance=10 ;
                    break ;
                case R.id.InMyRegion :
                    mKm.setVisibility(View.INVISIBLE);
                    mPickedDistance.setVisibility(View.INVISIBLE);
                    mDistanceSeekBar.setVisibility(View.INVISIBLE);
                    mChosenDistance=30 ;
                    break ;
                case R.id.Everywhere :
                    mKm.setVisibility(View.INVISIBLE);
                    mPickedDistance.setVisibility(View.INVISIBLE);
                    mDistanceSeekBar.setVisibility(View.INVISIBLE);
                    mChosenDistance = -1 ;
                    break ;
                case R.id.LetMeChoose :
                    mChosenDistance=0 ;
                    mKm.setVisibility(View.VISIBLE);
                    mPickedDistance.setVisibility(View.VISIBLE);
                    mDistanceSeekBar.setVisibility(View.VISIBLE);
                    break ;
                default:
                    mChosenDistance = 3 ;
                    break ;

            }
        }
    });
    mFilerButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            displayCoordinates(filterCoordinates(mChosenSubCat,mChosenType,mChosenTimeInterval,mChosenDistance));
            mDL.closeDrawer(GravityCompat.START);
        }
    });

    mDistanceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            mChosenDistance = progress ;
            mPickedDistance.setText(String.valueOf(progress));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    });







}

    public void getAllCoordinates( ){

    StringRequest getCoordinates = new StringRequest(Request.Method.GET, retrieve_all_coordinates_url, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
             try {

                 JSONObject  j = new JSONObject();
                 JSONArray a = new JSONArray(response);
                 for(int i=0 ;i<a.length();i=i+3){
                     j = a.getJSONObject(i);
                     Marker m = new Marker(j.getInt("ID_annonce"),j.getDouble("Latitude"),j.getDouble("Longitude"),j.getString("Titre"),j.getString("Resource"),j.getString("Type"),j.getInt("ID_sous_categorie"),j.getString("DatePublication"));
                     mDownloadedMarkersList.add(m);
                 }
                 displayCoordinates(mDownloadedMarkersList);

             }catch (JSONException j){
                 Toast.makeText(getContext(), "error message :"+j.getMessage(), Toast.LENGTH_SHORT).show();
             }


        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(getContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();

        }
    });
    VolleyAgent.getInstance(getContext()).addToRequestQueue(getCoordinates);

}
    public void displayCoordinates( final ArrayList<Marker> mMarkersList){
    if(mMapView!=null){
        mMapView.onCreate(null);
        mMapView.onResume();
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mGoogleMap = googleMap;
                mGoogleMap.clear();
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
                mGoogleMap.setInfoWindowAdapter(mCustomMarkerAdapter);

                for(int i=0;i<mMarkersList.size();i++) {
                    LatLng c = new LatLng(mMarkersList.get(i).getLatitude(), mMarkersList.get(i).getLongitude());
                    com.google.android.gms.maps.model.Marker m = mGoogleMap.addMarker(new MarkerOptions().position(c).icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker_icon1)).title(String.valueOf(mMarkersList.get(i).getID_annonce())));
                    m.setTag(mMarkersList.get(i));
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(c));
                    mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(6.0f));

                    mCustomMarkerAdapter = new CustomInfosWindowAdapter(getActivity(), m);
                    mGoogleMap.setInfoWindowAdapter(mCustomMarkerAdapter);

                }

                mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(com.google.android.gms.maps.model.Marker marker) {
                        int id = Integer.parseInt(marker.getTitle());
                        Intent i = new Intent(getActivity(),AdDetails.class);
                        Bundle b = new Bundle();
                        b.putInt("Ad ID",id);
                        i.putExtras(b);
                        startActivity(i);
                    }
                });


            }

        });

    }

}




    @Override
    public boolean onMarkerClick(com.google.android.gms.maps.model.Marker marker) {

        return false;
    }
    public class CustomInfosWindowAdapter  implements GoogleMap.InfoWindowAdapter , View.OnClickListener{

        private Activity mContext ;
        private com.google.android.gms.maps.model.Marker mMarker ;

        public CustomInfosWindowAdapter (Activity mContext , com.google.android.gms.maps.model.Marker mMarker){
            this.mContext = mContext ;
            this.mMarker =  mMarker ;
        }



        @Override
        public View getInfoContents(com.google.android.gms.maps.model.Marker marker) {

            View view = mContext.getLayoutInflater().inflate(R.layout.custom_marker_popup_layout, null);
            Marker m =(Marker) marker.getTag();
            ImageView AdImage = view.findViewById(R.id.MarkerAdImage);
            TextView AdTitle = view.findViewById(R.id.MarkerAdTitle);

            TextView HiddenAdID = view.findViewById(R.id.MarkerHiddenAdId);
            HiddenAdID.setVisibility(View.INVISIBLE);
            AdTitle.setText(m.getTitle());
            if(!(m.getSingleAdImageSource()==null)){
            AdImage.setImageBitmap(resizeAdImage(fromStringToBitmap(m.getSingleAdImageSource())));}

            return view;
        }

        @Override
        public View getInfoWindow(com.google.android.gms.maps.model.Marker marker) {

            return null;
        }

        public void onClick(View view){


        }
    }

    public Bitmap fromStringToBitmap(String resource){
        byte[] byteArray = Base64.decode(resource,Base64.DEFAULT);
        Bitmap b = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
        return b ;

    }
    public Bitmap resizeAdImage(Bitmap singleAdImage){
      return  Bitmap.createScaledBitmap(singleAdImage,300,300,false);
    }
    public void showProgressDialog(){
        mProgressD = new ProgressDialog(getActivity());
        mProgressD.setCancelable(false);
        mProgressD.setMessage("Working on it , hold on ......");
        mProgressD.show();

    }
    public void dismissProgressDialog(){
        mProgressD.dismiss();

    }

    public ArrayList<Marker> filterCoordinates(int sub_cat_id ,String Type , long dateLimit , double distance ){
        ArrayList<Marker> filteredMarkersList = new ArrayList<>();

        for(Marker m : mDownloadedMarkersList){
            Location AdLocation = new Location("");
            AdLocation.setLongitude(m.getLongitude());
            AdLocation.setLatitude(m.getLatitude());

            long calculatedDateLimit =  calculateDatesDifference(currentDate(),m.getPublicationDate());

              if(mCurrentLocation!=null){
              double calculatedDistance = calculateDistance(mCurrentLocation,AdLocation);
                  if(Type.equals("Both")){
                      if(sub_cat_id==56){
                          if(calculatedDateLimit<=dateLimit){
                              if(distance==-1){
                                  filteredMarkersList.add(m);}else {
                                  if(calculatedDistance<=distance){
                                      filteredMarkersList.add(m);
                                  }}

                          }
                      }else if(m.getSubCategory_ID()==sub_cat_id&&calculatedDateLimit<=dateLimit){
                          if(distance==-1){
                              filteredMarkersList.add(m);}else {
                              if(calculatedDistance<=distance){
                                  filteredMarkersList.add(m);
                              }
                          }
                      }} else if(sub_cat_id==56){
                          if(calculatedDateLimit<=dateLimit&&Type.equals(m.getType())){
                              if(distance==-1){
                                  filteredMarkersList.add(m);}else {
                                  if(calculatedDistance<=distance){
                                      filteredMarkersList.add(m);
                                  }}

                          }
                      }else if(m.getSubCategory_ID()==sub_cat_id&&calculatedDateLimit<=dateLimit&&Type.equals(m.getType())){
                          if(distance==-1){
                              filteredMarkersList.add(m);}else {
                              if(calculatedDistance<=distance){
                                  filteredMarkersList.add(m);
                              }
                          }
                      }

                  }else {
                  if(Type.equals("Both")){
                      if(sub_cat_id==56){
                          if(calculatedDateLimit<=dateLimit){
                              filteredMarkersList.add(m);
                          }
                      }else if(m.getSubCategory_ID()==sub_cat_id&&calculatedDateLimit<=dateLimit){
                              filteredMarkersList.add(m);}
                              } else if(sub_cat_id==56){
                      if(calculatedDateLimit<=dateLimit&&Type.equals(m.getType())){
                          filteredMarkersList.add(m);
                      }
                  }else if(m.getSubCategory_ID()==sub_cat_id&&calculatedDateLimit<=dateLimit&&Type.equals(m.getType())){

                              filteredMarkersList.add(m);
                      }

                  }








        }
        return filteredMarkersList ;





    }
    public double calculateDistance(Location currentLocation , Location AdLocation){
        return Math.round(currentLocation.distanceTo(AdLocation)/1000) ;
    }

    public long calculateDatesDifference(String currentDate , String AdPublishingDate){
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        try {
            Date mCurrentDate = format.parse(currentDate);
            Date mAdPublishingDate = formatDate(AdPublishingDate);

            float timeInterval = mCurrentDate.getTime() - mAdPublishingDate.getTime() ;
        return    TimeUnit.DAYS.convert((long) timeInterval, TimeUnit.MILLISECONDS);




        }catch (ParseException d){
           d.printStackTrace();
        }
     return 0 ;

    }
    public String currentDate(){
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyy");
        String formattedDate = df.format(c);
        return formattedDate ;
    }
    public Date formatDate(String stringDate){
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd-MM-yyyy";
        Date outputDate = null ;
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(stringDate);
            str = outputFormat.format(date);
            outputDate = outputFormat.parse(str);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputDate ;

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


                mSubCategoryAdapter = new SpinnerAdapter2(getActivity(),mSubCategoriesList,android.R.layout.simple_list_item_1);
                mSubCategorySpinner.setAdapter(mSubCategoryAdapter);

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


                mCategoryAdapter = new SpinnerAdapter(getActivity(),mCategoriesList,android.R.layout.simple_list_item_1);
                mCategorySpinner.setAdapter(mCategoryAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });
        VolleyAgent.getInstance(getContext()).addToRequestQueue(RetrieveCategoriesRequest);



    }


}
