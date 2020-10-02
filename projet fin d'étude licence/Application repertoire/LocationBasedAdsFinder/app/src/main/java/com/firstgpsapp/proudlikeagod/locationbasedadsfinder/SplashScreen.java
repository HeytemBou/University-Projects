package com.firstgpsapp.proudlikeagod.locationbasedadsfinder;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ProudLikeAGod on 11/02/2018.
 */

public class SplashScreen extends AppCompatActivity {
      private int DisplayDuration = 2000;
    private ImageView AdsLogo ;
    private ImageView FinderLogo ;
    private ImageView AppLogo ;
    private Animation fade_in_slide ;
    private Animation fade_in_slide_right ;
    private Animation zoom_in ;
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_layout);
        loadWidgets();
      //  loadAnimations();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(new PreferencesManager(SplashScreen.this).checkPreference()){
                    Intent i = new Intent(SplashScreen.this,ContainerActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.splash_anim,R.anim.mainactivity_anim);
                    finish();

                }else {
                Intent i = new Intent(SplashScreen.this,TutorialActivity2.class);
                startActivity(i);
                overridePendingTransition(R.anim.splash_anim,R.anim.mainactivity_anim);
                finish();}

            }
        },DisplayDuration);
    }

    public void loadWidgets(){

        FinderLogo =findViewById(R.id.FinderLogo);

    }
    public void loadAnimations(){
        fade_in_slide = AnimationUtils.loadAnimation(this,R.anim.slide_fade_in);
        fade_in_slide_right =AnimationUtils.loadAnimation(this,R.anim.slide_fade_in_right);
        zoom_in =AnimationUtils.loadAnimation(this,R.anim.zoom_in);
        AdsLogo.setAnimation(fade_in_slide_right);
        FinderLogo.setAnimation(fade_in_slide);
        AppLogo.setAnimation(zoom_in);

    }
}
