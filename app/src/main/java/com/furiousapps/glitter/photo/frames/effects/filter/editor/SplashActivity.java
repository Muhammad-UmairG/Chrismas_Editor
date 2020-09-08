package com.furiousapps.glitter.photo.frames.effects.filter.editor;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

import android.view.WindowManager;


import androidx.appcompat.app.AppCompatActivity;



public class SplashActivity extends AppCompatActivity  {

    private static final int SPLASH_TIME_OUT = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


          new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
              // This method will be executed once the timer is over
                // Start your app main activity

                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }

        }, SPLASH_TIME_OUT);
    }

}
