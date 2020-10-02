package com.firstgpsapp.proudlikeagod.locationbasedadsfinder;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SliderAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater ;
    private String mSliderString1 ;
    private String mSliderString2 ;
    private String mSliderString3 ;
    private String[] mSliderStrings = new String[3] ;



    public SliderAdapter (Context mContext ,String s1 , String s2 , String s3){
        this.mContext=mContext;
        mSliderStrings[0]=s1 ;
        mSliderStrings[1]=s2 ;
        mSliderStrings[2]=s3 ;

    }

    //Arrays holding ids of the resources
    public int[] mSliderImages ={
            R.drawable.fuckyouandroid,
            R.drawable.deal,
            R.drawable.account

    };



    @Override
    public int getCount() {
        return mSliderStrings.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {

        return view==(RelativeLayout)object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        mLayoutInflater =(LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        View view = mLayoutInflater.inflate(R.layout.tutorial2_slider_layout,container,false);

        ImageView m = view.findViewById(R.id.imageViewT);
        TextView t = view.findViewById(R.id.descriptionT);

        m.setImageResource(mSliderImages[position]);
        t.setText(mSliderStrings[position]);
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((RelativeLayout)object);
    }
}
