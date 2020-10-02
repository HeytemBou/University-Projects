package com.firstgpsapp.proudlikeagod.locationbasedadsfinder;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Comment {
 private String CommentContent ;
 private String PublicationDate ;
 private int CommentId ;
 private int UserId ;
 private String Username ;

 public Comment (String Username ,String CommentContent , String PublicationDate ,int CommentId , int UserId){
     this.CommentContent = CommentContent ;
     this.PublicationDate = PublicationDate;
     this.CommentId = CommentId;
     this.UserId = UserId;
     this.Username = Username;
 }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getCommentContent() {
        return CommentContent;
    }

    public void setCommentContent(String commentContent) {
        CommentContent = commentContent;
    }

    public String getPublicationDate() {
        return PublicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        PublicationDate = publicationDate;
    }

    public int getCommentId() {
        return CommentId;
    }

    public void setCommentId(int commentId) {
        CommentId = commentId;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String formatDate(String s){
        try{
            Date c = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(s);
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy  h:mm a", Locale.US);
            String formattedDate = df.format(c);
            return formattedDate ;}
        catch (ParseException p){
            Log.e("", "formatDate: "+p.getMessage());
        }
        return "";

    }
}
