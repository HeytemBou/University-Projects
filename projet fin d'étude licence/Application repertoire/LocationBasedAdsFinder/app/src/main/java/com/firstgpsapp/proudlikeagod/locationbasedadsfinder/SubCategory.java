package com.firstgpsapp.proudlikeagod.locationbasedadsfinder;

public class SubCategory {
    private int ID ;
    private int CatID ;
    private String  Name ;
    private String Description ;

    public SubCategory(int ID , int CatID , String Name){
        this.CatID=CatID;
        this.ID=ID;
        this.Name=Name;
    }
    public SubCategory(int ID , int CatID , String Name,String Description){
        this.CatID=CatID;
        this.ID=ID;
        this.Name=Name;
        this.Description=Description;
    }
    public SubCategory(){

    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getCatID() {
        return CatID;
    }

    public void setCatID(int catID) {
        CatID = catID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }
}
