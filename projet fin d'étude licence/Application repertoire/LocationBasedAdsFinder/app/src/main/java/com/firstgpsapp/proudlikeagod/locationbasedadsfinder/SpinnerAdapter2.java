package com.firstgpsapp.proudlikeagod.locationbasedadsfinder;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class SpinnerAdapter2 extends ArrayAdapter<SubCategory>{
    Activity activity ;
    List<SubCategory> mSubCategoriesList ;
    int itemResourceId ;

    public SpinnerAdapter2(Activity activity , List<SubCategory> SubCategoriesList,int itemResourceId){

        super(activity,itemResourceId,SubCategoriesList);
        this.activity=activity;
        this.mSubCategoriesList=SubCategoriesList;
        this.itemResourceId=itemResourceId;

    }

    public View getView(int position , View convertView , ViewGroup parent){
        View MyCustomLayout = convertView ;
        if(MyCustomLayout== null){
            LayoutInflater FriendsInflator = activity.getLayoutInflater();
            MyCustomLayout = FriendsInflator.inflate(R.layout.custom_spinner_layout,parent,false);

        }
        TextView FullN = MyCustomLayout.findViewById(R.id.CatSubCatName);

        FullN.setText(mSubCategoriesList.get(position).getName());



        return MyCustomLayout ;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View MyCustomLayout = convertView ;
        if(MyCustomLayout== null){
            LayoutInflater FriendsInflator = activity.getLayoutInflater();
            MyCustomLayout = FriendsInflator.inflate(R.layout.custom_spinner_layout,parent,false);

        }
        TextView FullN = MyCustomLayout.findViewById(R.id.CatSubCatName);

        FullN.setText(mSubCategoriesList.get(position).getName());



        return MyCustomLayout ;
    }
}
