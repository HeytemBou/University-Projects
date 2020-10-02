package com.firstgpsapp.proudlikeagod.locationbasedadsfinder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class Photo {
    private int ID ;
    private int ID_annonce ;
    private int ID_user ;
    private String Resource ;

    public Bitmap fromStringToBitmap (){
        byte[] byteArray = Base64.decode(Resource,Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);

    }

    public Photo(int ID, int ID_annonce, String resource) {
        this.ID = ID;
        this.ID_annonce = ID_annonce;
        Resource = resource;
    }



    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID_annonce() {
        return ID_annonce;
    }

    public void setID_annonce(int ID_annonce) {
        this.ID_annonce = ID_annonce;
    }

    public String getResource() {
        return Resource;
    }

    public void setResource(String resource) {
        Resource = resource;
    }
}
