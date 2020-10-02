package com.firstgpsapp.proudlikeagod.locationbasedadsfinder;

import android.support.v7.widget.CardView;

public class Category {
    private String ID ;
    private  String Name ;

    public Category(String ID ,String Name){
        this.ID=ID ;
        this.Name=Name;
    }
    public Category(){

    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
