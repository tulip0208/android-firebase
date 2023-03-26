package com.codecamp.chatapp.ui.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AnimationUtils;

import static java.lang.Thread.sleep;

import com.codecamp.chatapp.R;

public class SplashScreenActivity extends AppCompatActivity {
    private ConstraintLayout mConstraintLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mConstraintLayout = findViewById(R.id.relativeLayoutID);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(5000);
                    startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                    finish();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        thread.start();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mConstraintLayout.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_left));
    }
}