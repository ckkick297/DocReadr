package com.example.keerat666.listviewpdfui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        SharedPreferences app_preferences = getSharedPreferences("MyTheme", MODE_PRIVATE);
        SharedPreferences.Editor editor = app_preferences.edit();

        if (!app_preferences.contains("MyAppTheme")) {
            editor.putString("MyAppTheme", getString(R.string.theme_light));
            editor.commit();
        }else{
            if (getString(R.string.theme_light).equals(app_preferences.getString("MyAppTheme", null))) {
             //   Toast.makeText(getApplicationContext(), "Theme Light", Toast.LENGTH_SHORT).show();
            }
            if (getString(R.string.theme_warm).equals(app_preferences.getString("MyAppTheme", null))){
               // Toast.makeText(getApplicationContext(), "Theme Warm", Toast.LENGTH_SHORT).show();
            }
            if (getString(R.string.theme_dark).equals(app_preferences.getString("MyAppTheme", null))){
               // Toast.makeText(getApplicationContext(), "Theme Dark", Toast.LENGTH_SHORT).show();
            }
        }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (MySharedPreference.getPrefIsLogIn(splash.this)){
                    startActivity( new Intent(splash.this,MainActivity.class));
                }else {
                    startActivity(new Intent(splash.this, Login.class));
                }
                finish();
            }
        },1000);
    }
}
