package com.firstgpsapp.proudlikeagod.locationbasedadsfinder;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<Category> {
    Activity activity ;
    List<Category> mCategoriesList ;
    int itemResourceId ;

    public SpinnerAdapter(Activity activity , List<Category> CategoriesList,int itemResourceId){

        super(activity,itemResourceId,CategoriesList);
        this.activity=activity;
        this.mCategoriesList=CategoriesList;
        this.itemResourceId=itemResourceId;

    }

   public View getView(int position , View convertView , ViewGroup parent){
        View MyCustomLayout = convertView ;
        if(MyCustomLayout== null){
            LayoutInflater Inflator = activity.getLayoutInflater();
            MyCustomLayout = Inflator.inflate(R.layout.custom_spinner_layout,parent,false);

        }
        TextView FullN = MyCustomLayout.findViewById(R.id.CatSubCatName);
        TextView ID = MyCustomLayout.findViewById(R.id.hiddenId);
        ID.setText(mCategoriesList.get(position).getID());


        FullN.setText(mCategoriesList.get(position).getName());



        return MyCustomLayout ;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View MyCustomLayout = convertView ;
        if(MyCustomLayout== null){
            LayoutInflater Inflator = activity.getLayoutInflater();
            MyCustomLayout = Inflator.inflate(R.layout.custom_spinner_layout,parent,false);

        }
        TextView FullN = MyCustomLayout.findViewById(R.id.CatSubCatName);
        TextView ID = MyCustomLayout.findViewById(R.id.hiddenId);
        ID.setText(mCategoriesList.get(position).getID());


        FullN.setText(mCategoriesList.get(position).getName());



        return MyCustomLayout ;
    }
}
