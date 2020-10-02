package com.firstgpsapp.proudlikeagod.locationbasedadsfinder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationMenu;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;

import android.support.v4.app.Fragment;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class PersonalAdsFragment extends Fragment {

private RecyclerView RCV ;
    GridView myGridV ;
    private ImageButton mManageAds ;
    private  ArrayList<Annonce> mListeAnnonces = new ArrayList<>() ;
    private ArrayList<Photo> mPhotosList =new ArrayList<>() ;
    private BottomNavigationView bottomNavigationView ;
    private String retrieve_user_ads="http://192.168.43.18:8000/api/GetUserCreatedAds";
    private String retrieve_user_id="http://192.168.43.18:8000/api/getAuthUser";
    private String delete_user_created_ad="http://192.168.43.18:8000/api/DeleteUserCreatedAd";
    private String get_ad_photos ="http://192.168.43.18:8000/api/GetAdPhotos";
    private String get_user_created_ads ="http://192.168.43.18:8000/api/GetUserCreatedAdsOnly";
    private int mCurrentUserId;
    private ProgressDialog mProgressD ;
    private static String mTokenInvalid = "{\"error\":\"Token is Invalid\"}";
    private static String mTokenExpired ="{\"error\":\"Token is Expired\"}";
    private static String mSomeWentWrong ="{\"error\":\"Something is wrong\"}";
    private AnnonceListAdapter mPersonalAdsAdapter ;
    private AuthenticationAgent mAuthAgent  ;
    private TextView mAdsListEmptyHint ;


    public PersonalAdsFragment() {
    }



    public static PersonalAdsFragment newInstance() {
        PersonalAdsFragment fragment = new PersonalAdsFragment();
      /*  Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListeAnnonces = new ArrayList<Annonce>();
        mAuthAgent = new AuthenticationAgent(getActivity().getApplicationContext()) ;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.personal_ads_fragment_layout, container, false);
        GridView myGridV = view.findViewById(R.id.CapitalsGrid);
        mAdsListEmptyHint =view.findViewById(R.id.EmptyAdsListHint);
        mAdsListEmptyHint.setVisibility(View.INVISIBLE);
        mPersonalAdsAdapter =  new AnnonceListAdapter(getActivity(),mListeAnnonces,mPhotosList);
        myGridV.setAdapter(mPersonalAdsAdapter);
        myGridV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(),AdDetails.class);
                TextView t = view.findViewById(R.id.HiddenNativeID);
                String id_s = t.getText().toString();
                Bundle b = new Bundle();
                b.putInt("Ad ID",Integer.parseInt(id_s));
                i.putExtras(b);
                Toast.makeText(getContext(), id_s, Toast.LENGTH_SHORT).show();
               // startActivity(i);
            }
        });
       // loadListeners();
        getProfileInfos();
       /* if(mListeAnnonces.size()==0){
            mAdsListEmptyHint.setVisibility(View.VISIBLE);
        }*/
        return view ;
    }

    public class AnnonceListAdapter extends BaseAdapter {
        private Activity mContext;
        private ArrayList<Annonce> mAnnoncesList ;
        private ArrayList<Photo> ListePhotos ;


        public AnnonceListAdapter(Activity mContext,ArrayList<Annonce> ListeAnnonces ,ArrayList<Photo> ListePhotos ){
            this.mAnnoncesList=ListeAnnonces;
            this.ListePhotos = ListePhotos ;
            this.mContext=mContext ;
        }



        public AnnonceListAdapter(Activity a ,ArrayList<Annonce> mAannoncesList) {
            mContext = a;
            this.mAnnoncesList=mAnnoncesList;
        }

        public int getCount() {
            return mAnnoncesList.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(final int position, final View convertView, ViewGroup parent) {
            View singleAdLayout =convertView;

            if (convertView == null) {

                LayoutInflater inflater = mContext.getLayoutInflater();
                singleAdLayout =inflater.inflate(R.layout.element_annonce,parent,false);
                mManageAds = singleAdLayout.findViewById(R.id.ManageAd);}
                //initializing the widgets of the layout for each ad
                TextView Title = (TextView)singleAdLayout.findViewById(R.id.Titre2);
                TextView PublishingDate = (TextView)singleAdLayout.findViewById(R.id.Date_publication2);
                TextView Address =(TextView)singleAdLayout.findViewById(R.id.Location2);
                ImageView Image =(ImageView)singleAdLayout.findViewById(R.id.Image2);
            ProgressBar pb = (ProgressBar)singleAdLayout.findViewById(R.id.loadingImageProgressBar);
            pb.setVisibility(View.VISIBLE);
                final TextView HiddenAdId = (TextView)singleAdLayout.findViewById(R.id.HiddenNativeID);
                 HiddenAdId.setVisibility(View.INVISIBLE);

                //setting the values for the widgets
                Title.setText(mAnnoncesList.get(position).getTitre());
                PublishingDate.setText(formatDate(mAnnoncesList.get(position).getDateDePublication()));
                Address.setText(mAnnoncesList.get(position).getAdresse());
                //Image.setImageBitmap();
                HiddenAdId.setText(String.valueOf(mAnnoncesList.get(position).getID()));
                Image.setImageBitmap(getCorrespondingPhoto(mAnnoncesList.get(position).getID()));
                final View copy = singleAdLayout ;

                singleAdLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView t =copy.findViewById(R.id.HiddenNativeID);

                        Intent i = new Intent(getActivity(),AdDetails.class);
                        Bundle b = new Bundle();
                        b.putInt("Ad ID",Integer.parseInt(t.getText().toString()));
                        i.putExtras(b);
                        startActivity(i);
                    }
                });


                //initialize the pop up menu
                final PopupMenu PM = new PopupMenu(getContext(),mManageAds);
                //inflate its layout
                PM.getMenuInflater().inflate(R.menu.ad_update_popup_menu,PM.getMenu());
                // register a listener for it
                PM.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getItemId()){
                            case R.id.EditAdOpt :
                                Intent i2 = new Intent(getActivity(),AdEditing.class);
                                Bundle b2 = new Bundle();
                                b2.putInt("User Created Ad Id",Integer.parseInt(HiddenAdId.getText().toString()));
                                i2.putExtras(b2);
                                startActivity(i2);

                                break ;
                            case R.id.DeleteAdOpt:
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setMessage(R.string.ad_deletion_confirmation).setCancelable(true).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        deleteUserCreatedAd(mCurrentUserId,mAnnoncesList.get(position).getID());
                                        mListeAnnonces.remove(position);
                                        notifyDataSetChanged();
                                        Intent i = new Intent(getActivity(),ContainerActivity.class);
                                        Bundle b = new Bundle();
                                        b.putInt("fragment index",3);
                                        i.putExtras(b);
                                        startActivity(i);



                                    }
                                })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();


                                break;

                        }
                        return true;
                    }
                });
                //show the pop up when clicked on manage ad button
                mManageAds.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PM.show();
                    }
                });








            return singleAdLayout;
        }


        // references to our images
        public Bitmap getCorrespondingPhoto(int AdId){
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


 public void loadListeners(){

     myGridV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
         @Override
         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
             Intent i = new Intent(getActivity(),AdDetails.class);
             TextView t = view.findViewById(R.id.HiddenNativeID);
             String id_s = t.getText().toString();
             Bundle b = new Bundle();
             b.putInt("item position",Integer.parseInt(id_s));
             i.putExtras(b);
             startActivity(i);
         }
     });


 }






    public void showProgressDialog(){
        mProgressD = new ProgressDialog(getActivity());
      mProgressD.setCancelable(true);
       mProgressD.setCancelable(true);
        mProgressD.setMessage("Working on it , hold on ......");
        mProgressD.show();

    }
    public void dismissProgressDialog(){
        mProgressD.dismiss();

    }

    public void getProfileInfos(){
        StringRequest loginRequest = new StringRequest(Request.Method.POST, retrieve_user_id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dismissProgressDialog();
                try {
                    JSONObject j = new JSONObject(response);
                    JSONObject l = j.getJSONObject("result");
                    int  mUserId = l.getInt("id");
                    mCurrentUserId = mUserId ;
                    retrieveUserCreatedAdsOnly(mUserId);
                }catch(Throwable j){
                    j.printStackTrace();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                 dismissProgressDialog();

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
        VolleyAgent.getInstance(getContext()).addToRequestQueue(loginRequest);
         showProgressDialog();


    }
    public void retrieveAdsList(final int UserId){
        StringRequest RetrieveAdsRequest = new StringRequest(Request.Method.POST,retrieve_user_ads, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray Adsliste = new JSONArray(response);
                    for (int i=0;i<Adsliste.length();i=i+3){
                        JSONObject obj = Adsliste.getJSONObject(i);
                        Annonce an= new Annonce(obj.getInt("ID_annonce"),obj.getInt("ID_user"),obj.getString("Titre"),obj.getString("Type"),obj.getInt("ID_sous_categorie"),obj.getString("Description"),obj.getDouble("Latitude"),obj.getDouble("Longitude"),obj.getString("Nom"),obj.getString("Prenom"),obj.getString("Email"),obj.getString("Telephone"),obj.getString("Address"),obj.getString("DatePublication"));
                        mListeAnnonces.add(an);
                        Photo ph = new Photo(obj.getInt("id"),obj.getInt("id_annonce"),obj.getString("Resource"));
                        mPhotosList.add(ph);
                        mPersonalAdsAdapter.notifyDataSetChanged();
                        JSONObject obj2 = Adsliste.getJSONObject(i+1);
                        Photo ph2 = new Photo(obj2.getInt("id"),obj2.getInt("id_annonce"),obj2.getString("Resource"));
                        mPhotosList.add(ph2);
                        mPersonalAdsAdapter.notifyDataSetChanged();
                        JSONObject obj3 = Adsliste.getJSONObject(i+2);
                        Photo ph3 = new Photo(obj3.getInt("id"),obj3.getInt("id_annonce"),obj3.getString("Resource"));
                        mPhotosList.add(ph3);
                        mPersonalAdsAdapter.notifyDataSetChanged();

                    } if(mListeAnnonces.size()!=0){
                    mAdsListEmptyHint.setVisibility(View.INVISIBLE);}
                }
                catch(Throwable t){

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
                headers.put("ID_user",UserId+"");
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }};
        VolleyAgent.getInstance(getContext()).addToRequestQueue(RetrieveAdsRequest);






    }

    public void deleteUserCreatedAd(final int UserID , final int AdID){

        StringRequest DeleteUserCreatedAd = new StringRequest(Request.Method.POST,delete_user_created_ad, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dismissProgressDialog();
                           }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                dismissProgressDialog();
            }
        }){
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("id_user",String.valueOf(UserID));
                params.put("id_ad",String.valueOf(AdID));
                params.put("Content-Type", "x-www-form-urlencoded");
                return params;
            }};
        VolleyAgent.getInstance(getContext()).addToRequestQueue(DeleteUserCreatedAd);
        showProgressDialog();

    }

    public void retrieveUserCreatedAdsOnly( final int UserId){
        StringRequest RetrieveAdsRequest = new StringRequest(Request.Method.POST,get_user_created_ads, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray Adsliste = new JSONArray(response);
                    for (int i=0;i<Adsliste.length();i++){
                        JSONObject obj = Adsliste.getJSONObject(i);
                        Annonce an= new Annonce(obj.getInt("ID_annonce"),obj.getInt("ID_user"),obj.getString("Titre"),obj.getString("Type"),obj.getInt("ID_sous_categorie"),obj.getString("Description"),obj.getDouble("Latitude"),obj.getDouble("Longitude"),obj.getString("Nom"),obj.getString("Prenom"),obj.getString("Email"),obj.getString("Telephone"),obj.getString("Address"),obj.getString("DatePublication"));
                        mListeAnnonces.add(an);
                        mPersonalAdsAdapter.notifyDataSetChanged();
                    }
                    retrievePersonalAdsPhotos(mListeAnnonces);

                    if(mListeAnnonces.size()!=0){
                        mAdsListEmptyHint.setVisibility(View.INVISIBLE);}
                }
                catch(Throwable t){

                }






            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissProgressDialog();
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("ID_user",String.valueOf(UserId));
               // headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }};
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
                        mPersonalAdsAdapter.notifyDataSetChanged();}

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

    public void  retrievePersonalAdsPhotos(ArrayList<Annonce> UserCreatedAds){
        for(Annonce a : UserCreatedAds){
            retrievesingleAdImages(a.getID());
        }

    }






}
