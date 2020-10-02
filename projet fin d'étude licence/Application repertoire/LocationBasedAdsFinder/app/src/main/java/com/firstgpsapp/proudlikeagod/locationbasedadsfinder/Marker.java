package com.firstgpsapp.proudlikeagod.locationbasedadsfinder;

import android.graphics.Bitmap;

public class Marker {
    private int ID_annonce ;
    private String Title ;
    private double Latitude ;
    private double Longitude ;
    private Bitmap singleAdImage ;
    private String singleAdImageSource ;
    private int SubCategory_ID ;
    private String Type ;
    private String PublicationDate ;

    public String getPublicationDate() {
        return PublicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        PublicationDate = publicationDate;
    }

    public int getSubCategory_ID() {
        return SubCategory_ID;
    }

    public void setSubCategory_ID(int subCategory_ID) {
        SubCategory_ID = subCategory_ID;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public Marker(){

    }
    public Marker(int ID_annonce, double latitude, double longitude ,String Title ,String singleAdImageResource , String Type , int SubCategory_ID ,String PublicationDate) {
        this.ID_annonce = ID_annonce;
        Latitude = latitude;
        Longitude = longitude;
        this.Title =Title ;
        this.singleAdImageSource = singleAdImageResource ;
        this.SubCategory_ID = SubCategory_ID ;
        this.Type = Type;
        this.PublicationDate = PublicationDate ;

    }

    public Marker(int ID_annonce, double latitude, double longitude ,String Title ,String singleAdImageSource) {
        this.ID_annonce = ID_annonce;
        Latitude = latitude;
        Longitude = longitude;
        this.Title =Title ;
        this.singleAdImageSource = singleAdImageSource ;
    }
    public void resizeAdBitmapImage(){
        singleAdImage = Bitmap.createScaledBitmap(singleAdImage,50,50,false);

    }

    public Bitmap getSingleAdImage() {
        return singleAdImage;
    }

    public void setSingleAdImage(Bitmap singleAdImage) {
        this.singleAdImage = singleAdImage;
    }

    public String getSingleAdImageSource() {
        return singleAdImageSource;
    }

    public void setSingleAdImageSource(String singleAdImageSource) {
        this.singleAdImageSource = singleAdImageSource;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getID_annonce() {
        return ID_annonce;
    }

    public void setID_annonce(int ID_annonce) {
        this.ID_annonce = ID_annonce;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }
}
