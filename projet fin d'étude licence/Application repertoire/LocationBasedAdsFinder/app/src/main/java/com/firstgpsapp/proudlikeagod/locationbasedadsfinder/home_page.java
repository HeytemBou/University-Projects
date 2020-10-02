package com.firstgpsapp.proudlikeagod.locationbasedadsfinder;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

/**
 * Created by ProudLikeAGod on 23/02/2018.
 */

public class home_page extends AppCompatActivity {
    private Button mSignUp ,mSignIn ,mAnonymous ;
    private VideoView philomenaClip;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home_page);

        loadWidgets();
        loadVideo();
        onClickListeners();
    }
    public void loadVideo(){
        philomenaClip =(VideoView)findViewById(R.id.VideoClip);
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.worldmapanimation2);
        philomenaClip.setVideoURI(uri);
        philomenaClip.start();
        philomenaClip.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setVolume(0f,0f);
            }
        });

        philomenaClip.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                philomenaClip.start();
            }
        });
    }
    public void loadWidgets(){
        mSignIn =(Button)findViewById(R.id.sign_in);
        mSignUp =(Button)findViewById(R.id.sign_up);
        mAnonymous=(Button)findViewById(R.id.anonyme);

    }
    public void onClickListeners(){

        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(home_page.this,MainActivity.class);
                startActivity(i);


            }
        });
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(home_page.this,RegistrationForm.class);
                startActivity(i);

            }
        });
        mAnonymous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

    }

    protected void onResume(){
        super.onResume();
        philomenaClip.start();
    }
}
