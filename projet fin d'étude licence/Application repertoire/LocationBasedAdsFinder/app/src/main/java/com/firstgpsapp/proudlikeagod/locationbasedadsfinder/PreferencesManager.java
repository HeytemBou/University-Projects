package com.firstgpsapp.proudlikeagod.locationbasedadsfinder;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ProudLikeAGod on 19/03/2018.
 */

public class PreferencesManager {
    private Context mContext ;
    private SharedPreferences mSharedPreferences ;


    public PreferencesManager(Context mContext){
        this.mContext=mContext;
        getSharedPreferences();
    }
    private void  getSharedPreferences(){
        mSharedPreferences =mContext.getSharedPreferences(mContext.getString(R.string.MyPreference),Context.MODE_PRIVATE);

    }
    public void writePreference(){
        SharedPreferences.Editor  mEditor = mSharedPreferences.edit();
        mEditor.putString(mContext.getString(R.string.MyPreferenceKey),"OK");
        mEditor.commit();
    }
    public boolean checkPreference(){
        boolean status ;
        if(!mSharedPreferences.getString(mContext.getString(R.string.MyPreferenceKey),"null").matches("OK")){
            status = false ;
        }else {
            status=true ;
        }
        return  status ;
    }
    public void clearPreference(){
        mSharedPreferences.edit().clear().commit();
    }
}
