package com.firstgpsapp.proudlikeagod.locationbasedadsfinder;

import java.util.Date;



public class Annonce {
    private  int ID ;
    private int ID_user ;
    private String Titre ;
    private String Type ;
    private int SousCategorie ;
    private String Description ;
    private double Latitude , longitude ;
    private String Nom , Prenom , Email , Telephone ;
    private String Adresse ;

    public Annonce(int ID, int ID_user, String titre, String type, int sousCategorie, String description, double latitude, double longitude, String nom, String prenom, String email, String telephone, String adresse, String dateDePublication) {
        this.ID = ID;
        this.ID_user = ID_user;
        Titre = titre;
        Type = type;
        SousCategorie = sousCategorie;
        Description = description;
        Latitude = latitude;
        this.longitude = longitude;
        Nom = nom;
        Prenom = prenom;
        Email = email;
        Telephone = telephone;
        Adresse = adresse;
        DateDePublication = dateDePublication;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID_user() {
        return ID_user;
    }

    public void setID_user(int ID_user) {
        this.ID_user = ID_user;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public int getSousCategorie() {
        return SousCategorie;
    }

    public void setSousCategorie(int sousCategorie) {
        SousCategorie = sousCategorie;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getNom() {
        return Nom;
    }

    public void setNom(String nom) {
        Nom = nom;
    }

    public String getPrenom() {
        return Prenom;
    }

    public void setPrenom(String prenom) {
        Prenom = prenom;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getTelephone() {
        return Telephone;
    }

    public void setTelephone(String telephone) {
        Telephone = telephone;
    }

    public String getAdresse() {
        return Adresse;
    }

    public void setAdresse(String adresse) {
        Adresse = adresse;
    }

    private String DateDePublication ;


    public Annonce(String Titre , String DateDePublication){
        this.Titre=Titre ;
        this.DateDePublication=DateDePublication;
    }
    public Annonce (String Titre , String Description ,String Categorie , Date DateDeCreation , String DateDePublication ,String Type){
        this.Titre=Titre;
        this.Type=Type;
        this.Description=Description;
        this.DateDePublication=DateDePublication;
    }
    public Annonce(){

    }
    public Annonce(String Titre , String Description , String Categorie , Date DateDeCreation ,String Type ){
        this.Titre=Titre ;
        this.Type=Type;
        this.Description=Description ;

    }
    public String getTitre() {
        return Titre;
    }

    public void setTitre(String titre) {
        Titre = titre;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }


    public String getDateDePublication() {
        return DateDePublication;
    }

    public void setDateDePublication(String dateDePublication) {
        DateDePublication = dateDePublication;
    }
}
