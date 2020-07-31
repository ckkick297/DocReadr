package com.example.keerat666.listviewpdfui;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import static com.example.keerat666.listviewpdfui.MainActivity.READ_EXST;

public class SecondScreen extends AppCompatActivity {

    Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_popup);

        askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE, READ_EXST);
//        myDialog = new Dialog(this);
    }


    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(SecondScreen.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(SecondScreen.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(SecondScreen.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(SecondScreen.this, new String[]{permission}, requestCode);
            }
        } else {
            //Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }
//    public void ShowPopup(View v) {
//        TextView txtclose;
//        Button btnFollow;
//        myDialog.setContentView(R.layout.custom_popup);
//        txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
//        txtclose.setText("x");
//        btnFollow = (Button) myDialog.findViewById(R.id.btnfollow);
//        txtclose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                myDialog.dismiss();
//            }
//        });
//        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        myDialog.show();
//    }
}
