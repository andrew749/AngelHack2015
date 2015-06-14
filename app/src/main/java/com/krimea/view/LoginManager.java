package com.krimea.view;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by andrewcodispoti on 2015-06-13.
 */
public class LoginManager {
    public static LoginManager instance;
    private Context context;
    public static final String prefsFile = "krimeaprefs";

    public LoginManager(Context context) {
        this.context = context;
    }


    public void storeUserData(String name, String email, String password) {
        SharedPreferences prefs = context.getSharedPreferences(prefsFile, Context.MODE_PRIVATE);
        prefs.edit().putString("name", name);
        prefs.edit().putString("email", email);
        prefs.edit().putString("password", password);
    }

}
