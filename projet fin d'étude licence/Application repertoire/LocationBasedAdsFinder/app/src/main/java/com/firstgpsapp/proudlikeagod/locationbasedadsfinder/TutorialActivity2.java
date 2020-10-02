package com.firstgpsapp.proudlikeagod.locationbasedadsfinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class TutorialActivity2 extends AppCompatActivity {
    private ViewPager mOnBoardPager;
    private LinearLayout mDotsLayout;
    private SliderAdapter mSliderAdapter;
    private TextView[] mDots;
    private int mCurrentPage;
    private Button mNextButton, mBackButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial_activity2);
        loadWidgets();
        loadListeners();
        addDotsIndicator(0);
        mOnBoardPager.addOnPageChangeListener(sliderListener);
    }
    private void loadHomeScreen(){
        startActivity(new Intent(this,ContainerActivity.class));
        finish();
    }

    public void loadListeners() {
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBoardPager.setCurrentItem(mCurrentPage - 1);
            }
        });
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentPage ==2) {
                    new PreferencesManager(TutorialActivity2.this).writePreference();
                    startActivity(new Intent(TutorialActivity2.this, ContainerActivity.class));
                    finish();


                } else {
                    mOnBoardPager.setCurrentItem(mCurrentPage + 1);

                }

            }


        });}


        public void loadWidgets () {
            mOnBoardPager =(ViewPager) findViewById(R.id.OnBoardPager);
            mDotsLayout =(LinearLayout) findViewById(R.id.dotsLayout);
            mNextButton =(Button) findViewById(R.id.NextButton);
            mBackButton =(Button) findViewById(R.id.PreviousButton);

            mSliderAdapter = new SliderAdapter(this,getResources().getString(R.string.Slide1),getResources().getString(R.string.Slide2),getResources().getString(R.string.Slide3));
            mOnBoardPager.setAdapter(mSliderAdapter);

        }

        ViewPager.OnPageChangeListener sliderListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addDotsIndicator(position);
                mCurrentPage = position;
                if (mCurrentPage == 0) {
                    mBackButton.setEnabled(false);
                    mNextButton.setEnabled(true);
                    mBackButton.setVisibility(View.INVISIBLE);
                    mNextButton.setText(getResources().getText(R.string.next));
                    mBackButton.setText("");
                } else if (mCurrentPage == mDots.length - 1) {
                    mBackButton.setEnabled(true);
                    mNextButton.setEnabled(true);
                    mBackButton.setVisibility(View.VISIBLE);
                    mNextButton.setText(getResources().getText(R.string.start));
                    mBackButton.setText(getResources().getText(R.string.back));

                } else {
                    mBackButton.setEnabled(true);
                    mNextButton.setEnabled(true);
                    mBackButton.setVisibility(View.VISIBLE);
                    mNextButton.setText(getResources().getText(R.string.next));
                    mBackButton.setText(getResources().getText(R.string.back));
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };

        //create the dots indicator using html code !!
        public void addDotsIndicator ( int position){
            mDots = new TextView[3];
            mDotsLayout.removeAllViews();

            for (int i = 0; i < mDots.length; i++) {
                mDots[i] = new TextView(this);
                mDots[i].setText(Html.fromHtml("&#8226;"));
                mDots[i].setTextSize(35);
                mDots[i].setTextColor(getResources().getColor(R.color.Purple));
                mDotsLayout.addView(mDots[i]);
            }

            if (mDots.length > 0) {
                mDots[position].setTextColor(getResources().getColor(R.color.OrangeLight2));
            }


        }

    }

