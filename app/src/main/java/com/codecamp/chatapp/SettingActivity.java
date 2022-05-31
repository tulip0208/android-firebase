package com.codecamp.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.Arrays;

public class SettingActivity extends AppCompatActivity {
    private ConstraintLayout mBackButton;
    private ProgressBar mProgressBar;
    private TextView mSaveText;
    private EditText mNameEditText,mBioEditText;
    private ConstraintLayout mConstraintLayout,mSaveButton;
    private FirebaseAuth mUserAuth;
    private DatabaseReference mUserRef;
    private Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        dialog = new Dialog(SettingActivity.this);
        dialog.setContentView(R.layout.progress_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.cancel();
        mUserAuth = FirebaseAuth.getInstance();
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users");

        mBackButton = findViewById(R.id.backButton);
        mConstraintLayout = findViewById(R.id.constraintLayout);
        mSaveButton = findViewById(R.id.saveButton);
        mNameEditText = findViewById(R.id.nameEditText);
        mBioEditText = findViewById(R.id.bioEditText);
        mSaveText = findViewById(R.id.saveText);

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBackButton.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_animation));
                startActivity(new Intent(SettingActivity.this,CurrentUserActivity.class));
                finish();
            }
        });


        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSaveButton.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_animation));
//                Dialog dialog = new Dialog(SettingActivity.this);
//                dialog.setContentView(R.layout.progress_dialog);
//                dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.show();

                if(!TextUtils.isEmpty(mNameEditText.getText().toString())){
                    mUserRef.child(mUserAuth.getCurrentUser().getUid()).child("name").setValue(mNameEditText.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                dialog.cancel();
                                mNameEditText.getText().clear();
                                Toast.makeText(getApplicationContext(),"name updated",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    dialog.cancel();
                    mNameEditText.setError("empty name");
                }

                if(!TextUtils.isEmpty(mBioEditText.getText().toString())){
                    dialog.show();
                    mUserRef.child(mUserAuth.getCurrentUser().getUid()).child("bio").setValue(mBioEditText.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                dialog.cancel();
                                mBioEditText.getText().clear();
                                Toast.makeText(getApplicationContext(),"bio updated",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    mBioEditText.setError("empty bio");
                }


            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mConstraintLayout.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_left));
    }
}