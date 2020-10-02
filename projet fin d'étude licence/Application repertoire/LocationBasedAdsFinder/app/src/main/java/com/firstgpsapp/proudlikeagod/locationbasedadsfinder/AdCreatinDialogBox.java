package com.firstgpsapp.proudlikeagod.locationbasedadsfinder;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AdCreatinDialogBox extends Dialog {

    public AdCreatinDialogBox(Context context){
        super(context);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_creatin_dialog_box);

    }
}
