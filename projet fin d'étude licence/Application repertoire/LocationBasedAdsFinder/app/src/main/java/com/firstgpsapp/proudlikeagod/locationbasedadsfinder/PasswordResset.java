package com.firstgpsapp.proudlikeagod.locationbasedadsfinder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PasswordResset extends AppCompatActivity {
  private Button mSendCode , mResetButton ;
  private TextView mConfirm ,mResend  , mNPHint , mNPCHint ;
  private EditText mContact ,mCodeField , mPasswordField,mPasswordConfirmField;
  private Animation bottoms_up ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_resset);

        loadWidgets();
        addListeneres();
        loadAnimations();


    }
    public void loadAnimations(){
        bottoms_up= AnimationUtils.loadAnimation(this,R.anim.bottoms_up);
    }

    public void loadWidgets(){
        mSendCode = (Button)findViewById(R.id.SendCode);
        mContact =(EditText)findViewById(R.id.ContactCode);
        mConfirm =(TextView)findViewById(R.id.ConfirmView);
        mResend =(TextView)findViewById(R.id.ResendView);
        mNPHint=(TextView)findViewById(R.id.S);
        mNPCHint=(TextView)findViewById(R.id.C);
        mCodeField=(EditText)findViewById(R.id.CodeField);
        mPasswordField=(EditText)findViewById(R.id.NewPasswordField);
        mResetButton=(Button)findViewById(R.id.ResetPassword);
        mPasswordConfirmField=(EditText)findViewById(R.id.NewPasswordConfirmField);


        mConfirm.setVisibility(View.INVISIBLE);
        mResend .setVisibility(View.INVISIBLE);

        mNPHint.setVisibility(View.INVISIBLE);
        mNPCHint.setVisibility(View.INVISIBLE);
        mCodeField.setVisibility(View.INVISIBLE);
        mPasswordField.setVisibility(View.INVISIBLE);
        mResetButton.setVisibility(View.INVISIBLE);
        mPasswordConfirmField.setVisibility(View.INVISIBLE);

        mResetButton.setEnabled(false);
        mResetButton.setAlpha(0.2f);




        mConfirm.setEnabled(false);
        mConfirm.setAlpha(0.5f);

        mSendCode.setAlpha(0.2f);
        mSendCode.setEnabled(false);
    }
    public void Validate(){
        if(!mContact.getText().toString().matches("")){
            mSendCode.setEnabled(true);
            mSendCode.setAlpha(1);
        }else {
            mSendCode.setEnabled(false);
            mSendCode.setAlpha(0.2f);
        }

    }
    public boolean checkEmailPhonePatterns(){

        return true ;
    }
    public void ValidateConfirmationCode(){
        if(!mCodeField.getText().toString().matches("")){
            mConfirm.setEnabled(true);
            mConfirm.setAlpha(1);
        }
    }


    public void addListeneres(){

        mSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCodeField.setVisibility(View.VISIBLE);
                mConfirm.setVisibility(View.VISIBLE);
                mResend.setVisibility(View.VISIBLE);

                mResend.setAnimation(bottoms_up);
                mCodeField.setAnimation(bottoms_up);
                mConfirm.setAnimation(bottoms_up);

            }
        });

        mContact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Validate();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mCodeField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                       ValidateConfirmationCode();


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNPHint.setVisibility(View.VISIBLE);
                mNPCHint.setVisibility(View.VISIBLE);
                mCodeField.setVisibility(View.VISIBLE);
                mPasswordField.setVisibility(View.VISIBLE);
                mResetButton.setVisibility(View.VISIBLE);
                mPasswordConfirmField.setVisibility(View.VISIBLE);

            }
        });
    }
}
