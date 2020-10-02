package com.firstgpsapp.proudlikeagod.locationbasedadsfinder;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.RequestQueue;

import org.json.JSONObject;

public class AuthenticationAgent  {

    private  static SharedPreferences mSharedPreferences ;
    private static final  String mTokenKey = "Token Value" ;
    private static final String mDefValue="oops , tiny little creatures disrupted our storage";
    private Context mContext ;
    private static final String mTokenManagerIdentifier = "If i want to choose between one evil and another " +
            "I'd rather not choose at all";





    public AuthenticationAgent (Context mContext ){
        this.mContext = mContext ;
        mSharedPreferences =mContext.getSharedPreferences(mTokenManagerIdentifier,Context.MODE_PRIVATE);

    }


    public  void saveToken(String rawString){

        try{
            JSONObject tokenWrapper = new JSONObject(rawString);
            SharedPreferences.Editor  mEditor = mSharedPreferences.edit();
            mEditor.putString(mTokenKey,tokenWrapper.getString("result"));
            mEditor.apply();

        }catch (Throwable t){
            Log.e("My App", "Could not parse malformed JSON: \"" + rawString + "\"");
        }

    }
    public  String getToken(){
        return mSharedPreferences.getString(mTokenKey,mDefValue);
    }
    public  void deleteToken(){
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.clear();
        mEditor.apply();
    }

    public  boolean checkToken(){
        if(mSharedPreferences.getString(mTokenKey,mDefValue).equals(mDefValue)){
            return false ;
        }else return true ;


    }


}
