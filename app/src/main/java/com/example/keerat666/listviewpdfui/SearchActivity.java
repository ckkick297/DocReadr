package com.example.keerat666.listviewpdfui;

import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.io.File;

public class SearchActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        GridView gridView = (GridView)findViewById(R.id.grid);
        File dir = Environment.getExternalStorageDirectory().getAbsoluteFile();
        String path = dir.getAbsolutePath();
        //  String path=Environment.getExternalStorageDirectory().getAbsolutePath();
        File ff = new File(String.valueOf(path));//converted string object to file
        String[] values = ff.list();//getting the list of files in string array
        //now presenting the data into screen
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext()
                , android.R.layout.simple_gallery_item, values);
        gridView.setAdapter(adapter);//setting the adapter
    }
}
