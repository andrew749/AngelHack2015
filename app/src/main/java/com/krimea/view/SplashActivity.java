package com.krimea.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.krimea.R;
import com.krimea.util.Constants;


public class SplashActivity extends Activity {
    private final int SPLASH_DURATION = 1500;
    private boolean hasLogged;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SharedPreferences prefs = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        hasLogged = prefs.getBoolean(Constants.hasLogged,false);
        new android.os.Handler().postDelayed(new Runnable() {
            public void run() {
                if(hasLogged) {
                    SplashActivity.this.startActivity(new Intent(SplashActivity.this, MainActivity.class));
                } else {
                    SplashActivity.this.startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
                SplashActivity.this.finish();
            }
        }, SPLASH_DURATION);
    }

}
