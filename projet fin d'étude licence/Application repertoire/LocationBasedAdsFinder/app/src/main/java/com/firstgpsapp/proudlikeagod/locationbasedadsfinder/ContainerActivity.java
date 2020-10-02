package com.firstgpsapp.proudlikeagod.locationbasedadsfinder;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ProudLikeAGod on 01/03/2018.
 */

public class ContainerActivity extends AppCompatActivity {

    //progress dialog instance
    ProgressDialog mProgressD ;



    private static String mTokenInvalidError ="{\"error\":\"Token is Invalid\"}";
    private static String mTokenEpiredError="{\"error\":\"Token is Expired\"}"  ;
    private static String mMostUsefulError ="{\"error\":\"Something is wrong\"}" ;


    //declaring widgets
    private AppBarLayout myAppBarLayout ;
    private TabLayout myTabLayout ;
    private ViewPager myViewPager ;
    private String callingActivity ;
    private ImageButton mCreateAd , mAboutUsPopup , mCreateAdItem ;

    // instanciating an authentication agent object in addition to the pager adapter
    private AuthenticationAgent mAuthAgent ;
    private ProfileSectionsStatePagerAdapter adapter ;
    private int whereToGo ;
    private PopupMenu PM ;
    //url for the getAuthUser api route
    private String get_auth_user = "http://192.168.43.18:8000/api/getAuthUser";

    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container_activity_layout);
        Intent i = getIntent();
        Bundle b = i.getExtras();
        if(b!=null){
        whereToGo = b.getInt("fragment index");}
        callingActivity = "search fragment";
        loadWidgets();
        loadListeners();
        if(whereToGo!=0){
        myViewPager.setCurrentItem(whereToGo);
        }


    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_new_add_bottom_menu_button, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.aboutUsPopup) {
            Toast.makeText(this, "it works", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void loadListeners(){
        mCreateAdItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuthAgent = new AuthenticationAgent(getApplicationContext());
                if(mAuthAgent.checkToken()){
                    getAuthUser(mAuthAgent.getToken(),callingActivity);
                    }
                else {
                    Intent i = new Intent(ContainerActivity.this,MainActivity.class);
                    Bundle b = new Bundle();
                    b.putString("calling activity",callingActivity);
                    i.putExtras(b);
                    startActivity(i);

            }
        }
    });
    mAboutUsPopup.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            PM.show();


        }
    });}
    public void onBackPressed() {

        if (myViewPager.getCurrentItem() != 0) {
            myViewPager.setCurrentItem(myViewPager.getCurrentItem()-myViewPager.getOffscreenPageLimit(),true);
        }else{
            finish();
        }

    }
    public void loadWidgets(){

        myViewPager=(ViewPager)findViewById(R.id.MyViewPager);
        myTabLayout =(TabLayout)findViewById(R.id.MyTabLayout);
        mAboutUsPopup =(ImageButton)findViewById(R.id.aboutuspopup);
        mCreateAdItem=(ImageButton)findViewById(R.id.CreateAdItem);

        adapter = new ProfileSectionsStatePagerAdapter(getSupportFragmentManager());
        myViewPager.setAdapter(adapter);
        myViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(myTabLayout));
        myViewPager.setOffscreenPageLimit(3);
        myTabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(myViewPager));


        //inflate its layout
         PM = new PopupMenu(ContainerActivity.this,mAboutUsPopup);
        PM.getMenuInflater().inflate(R.menu.about_us_popup,PM.getMenu());
        // register a listener for it
        PM.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.AboutUs :
                        startActivity(new Intent(ContainerActivity.this,AboutUs.class));
                        break ;
                }
                return true ;

            }


        });

    }
    public class ProfileSectionsStatePagerAdapter extends FragmentStatePagerAdapter{
        private final List<Fragment> mFragmentsList = new ArrayList<>();
        private final List<String> mFragmentsTitles = new ArrayList<>();


        public ProfileSectionsStatePagerAdapter(FragmentManager fm  ){
            super(fm);

        }



        public Fragment getItem(int position){
            Fragment fragment = null ;
            switch(position){
                case 0 :
                    return SearchFragment.newInstance();
                case 1 :
                    return MapFitlerFragment.newInstance();
                case 2 :
                    return ProfileFragment.newInstance();
                case 3 :
                    return PersonalAdsFragment.newInstance();

            }
            return fragment ;


        }

        public int getCount(){
            return 4 ;
        }

       /* public CharSequence getPageTitle(int position){
            switch(position){
                case 0 :
                    return "Search";

                case 1 :
                    return "My ads";

                case 2 :
                    return "Find ads";

                case 3 :
                    return "Map filter";


            }
            return null ;
        }*/




    }
    public void getAuthUser(final String token , final String caller){
        StringRequest loginRequest = new StringRequest(Request.Method.POST, get_auth_user, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                    if(response.equals(mTokenEpiredError)||response.equals(mTokenInvalidError)||response.equals(mMostUsefulError)){
                        Intent i = new Intent(ContainerActivity.this,MainActivity.class);
                        Bundle b = new Bundle();
                        b.putString("calling activity",caller);
                        i.putExtras(b);
                        startActivity(i);
                    }else {
                        startActivity(new Intent(ContainerActivity.this,AdCreation.class));
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
        VolleyAgent.getInstance(ContainerActivity.this).addToRequestQueue(loginRequest);

    }

    public void showProgressDialog(){
        mProgressD = new ProgressDialog(ContainerActivity.this);
        mProgressD.setCancelable(false);
        mProgressD.setMessage("Working on it......");
        mProgressD.show();

    }
    public void dismissProgressDialog(){
        mProgressD.dismiss();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}
