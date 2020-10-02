package com.firstgpsapp.proudlikeagod.locationbasedadsfinder;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class TutorialActivity extends AppCompatActivity implements View.OnClickListener{
    ViewPager mSlidesPager ;
    private int[] mSlidesLayouts ={R.layout.first_slide_layout,R.layout.second_slide_layout,R.layout.third_slide_layout,R.layout.fourth_slide_layout};
    private mSlidesPagerAdapter mAdapter ;
    private LinearLayout mDotsLayout ;
    private ImageView[] mDots ;
    private Button mSkpBtn;
    private Button mNxtBtn ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* if(new PreferencesManager(this).checkPreference()){
            loadHomeScreen();
        }*/

        setContentView(R.layout.activity_tutorial);

        //initializing the views
        mSlidesPager =(ViewPager)findViewById(R.id.SlidesPager);
        mDotsLayout=(LinearLayout)findViewById(R.id.DotAndNextAndSkip);
        mSkpBtn =(Button)findViewById(R.id.SkipButton);
        mNxtBtn =(Button)findViewById(R.id.NextButton);

        //initializing the adapter and positioning the linear layout containing the dots
        mAdapter = new mSlidesPagerAdapter(mSlidesLayouts,this);
        mDotsLayout.setGravity(Gravity.CENTER);

        //adding the listeners to the pager adapter and the two buttons , skip and next
        mSlidesPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
              createDots(position);
              if(position==mSlidesLayouts.length-1){
                  mNxtBtn.setText("START");
                  mSkpBtn.setVisibility(View.INVISIBLE);
              }else  {
                  mNxtBtn.setText("NEXT");
                  mSkpBtn.setVisibility(View.VISIBLE);
              }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mSkpBtn.setOnClickListener(this);
        mNxtBtn.setOnClickListener(this);


        mSlidesPager.setAdapter(mAdapter);
        createDots(0);

        if(Build.VERSION.SDK_INT>=19){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

    }
    public void onClick(View v){
        switch(v.getId()){
            case R.id.NextButton:
                loadNextSlide();
                break ;
            case R.id.SkipButton:
                new PreferencesManager(this).writePreference();
                loadHomeScreen();

                break ;
        }

    }
    private void loadNextSlide(){
        int next_slide = mSlidesPager.getCurrentItem()+1;
        if(next_slide<mSlidesLayouts.length){
            mSlidesPager.setCurrentItem(next_slide);
        }
        else {
            new PreferencesManager(this).writePreference();
            loadHomeScreen();

        }
    }

    private void loadHomeScreen(){
        startActivity(new Intent(this,home_page.class));
        finish();
    }
    private void createDots(int currentPosition){
        if(mDotsLayout!=null){
            mDotsLayout.removeAllViews();

            mDots = new ImageView[mSlidesLayouts.length];

            for(int i=0;i<mSlidesLayouts.length;i++){
                mDots[i]=new ImageView(this);
                if(i==currentPosition){
                    mDots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.active_dot));
                }
                else {
                    mDots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.not_active_dot));
                }
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(4,0,4,0);
                mDotsLayout.addView(mDots[i],params);
            }
        }

    }

    public class mSlidesPagerAdapter extends PagerAdapter {

        private int[] layouts ;
        private LayoutInflater mInflator ;
        private Context mContext ;


        public mSlidesPagerAdapter(int[] layouts ,Context mContext){
            this.layouts = layouts ;
            this.mContext = mContext ;
            mInflator =(LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        public Object instantiateItem(ViewGroup container , int position){
            View view = mInflator.inflate(layouts[position],container,false);
            container.addView(view);
            return view;

        }
        public void destroyItem(ViewGroup container,int position,Object object){
            View view = (View)object;
            container.removeView(view);

        }
    }
}
