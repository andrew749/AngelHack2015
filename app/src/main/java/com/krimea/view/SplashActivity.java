package com.krimea.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.krimea.R;


public class SplashActivity extends Activity {
    private final int SPLASH_DURATION = 1500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new android.os.Handler().postDelayed(new Runnable() {
            public void run() {
                SplashActivity.this.startActivity(new Intent(SplashActivity.this, MainActivity.class));
                SplashActivity.this.finish();
            }
        }, SPLASH_DURATION);
    }

}
