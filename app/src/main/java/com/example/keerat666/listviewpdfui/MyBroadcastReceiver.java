package com.example.keerat666.listviewpdfui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class MyBroadcastReceiver extends BroadcastReceiver {
   TextToSpeechManager textToSpeechManager;
   @Override
   public void onReceive(Context context, Intent intent) {

      //Toast.makeText(context, ""+intent.getStringExtra("play"), Toast.LENGTH_SHORT).show();
      // PDFViewer pdfViewer= new PDFViewer();

     /*  textToSpeechManager =new TextToSpeechManager();
       textToSpeechManager.shutDown();
       Bundle extras = intent.getExtras();
       if (extras != null) {
           String state = extras.getString(TelephonyManager.EXTRA_STATE);
           Log.i("MY_DEBUG_TAG", state);
           if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
               String phoneNumber = extras
                       .getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
               Log.i("MY_DEBUG_TAG", phoneNumber);
           }
       }
       */
   }
}
        //pdfViewer.pausevoice();
        //p//dfViewer.t1.stop();
        //String s    = intent.getStringExtra("value");
        //Log.i("value ",s);



