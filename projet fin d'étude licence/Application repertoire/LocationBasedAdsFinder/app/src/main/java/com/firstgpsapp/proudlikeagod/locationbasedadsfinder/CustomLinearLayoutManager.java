package com.firstgpsapp.proudlikeagod.locationbasedadsfinder;

import android.content.Context;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;

public class CustomLinearLayoutManager extends LinearLayoutManager {

    public CustomLinearLayoutManager(Context context){
        super(context);
    }
    public CustomLinearLayoutManager(Context context , int orientation ,boolean reverseLayout){
        super(context,orientation,reverseLayout);
    }
    public CustomLinearLayoutManager(Context context , AttributeSet attrs, int defStyleAttr, int defStyleRes){
        super(context,attrs,defStyleAttr,defStyleRes);
    }

    public void smoothScrollToPositon(RecyclerView recyclerView, int position){
        final LinearSmoothScroller linearSmoothScroller =
                new LinearSmoothScroller(recyclerView.getContext()){
            private static final float MILLISECONDS_PER_INCH = 200f ;

                    @Nullable
                    @Override
                    public PointF computeScrollVectorForPosition(int targetPosition) {
                        return CustomLinearLayoutManager.this.computeScrollVectorForPosition(targetPosition);
                    }

                    @Override
                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                        return MILLISECONDS_PER_INCH / displayMetrics.densityDpi ;
                    }
                };
        linearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(linearSmoothScroller);
    }
}
