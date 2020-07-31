package com.example.keerat666.listviewpdfui;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.ArrayList;
import java.util.Locale;

public class TextToSpeechManager {

    private TextToSpeech mTts = null;
    private boolean isLoaded = false;
    boolean media=false;
    String CONSTANT_MEDIA ="media_value";
    boolean b;
    int w;
    ArrayList<Integer> q;

    public  void  init(Context context,int a) {

        try {


            mTts = new TextToSpeech(context, initListener);
            q.add(a);
            w=a;
            System.out.println(a);
            Log.i("instance", String.valueOf(a));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private TextToSpeech.OnInitListener initListener = new TextToSpeech.OnInitListener() {

        @Override
        public void onInit(int i) {
            if (i == TextToSpeech.SUCCESS) {
                int result = mTts.setLanguage(Locale.US);
                isLoaded = true;

                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("error", "This Language is not supported");
                }
            } else {
                Log.e("error", "Initialization Failed!");
            }



        }
    };


    public void shutDown() {
        media = false;
        mTts.shutdown();

    }

    public void addQueue(String text) {
        if (isLoaded)
            mTts.speak(text, TextToSpeech.QUEUE_ADD, null);
        else
            Log.e("error", "TTS Not Initialized");
    }

    public void initQueue(String text,boolean a) {
        media =a;
        if (isLoaded)

            mTts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        else
            Log.e("error", "TTS Not Initialized");
    }

    public void stops(){
        mTts.stop();
    }


    public boolean status(){
       boolean a;
       if( mTts.isSpeaking()){

           a=true;
       }else{
           a=false;

       }
       return a;


    }

    public  void savePreferences(Context mContext, String key, boolean value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value).apply();
    }

    public Boolean getPreferences(Context context, String keyValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(keyValue, b);
    }

    public  void removeAllSharedPreferences(Context mContext) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().apply();
    }
    public int value(){
        return w;
    }




}
