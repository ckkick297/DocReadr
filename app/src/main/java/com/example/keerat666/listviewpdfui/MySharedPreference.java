package com.example.keerat666.listviewpdfui;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreference {

    static final String Pref_Login = "Login";
    static SharedPreferences.Editor editor;

    private static SharedPreferences getPref(Context context, String pref){
        return context.getSharedPreferences(pref, 0);
    }

    public static void setPrefLogIn(Context context, boolean state) {
        editor = getPref(context, Pref_Login).edit();
        editor.putBoolean("login_state", state);
        editor.commit();
    }

    public static boolean getPrefIsLogIn(Context context){
        return getPref(context, Pref_Login).getBoolean("login_state", false);
    }
}
