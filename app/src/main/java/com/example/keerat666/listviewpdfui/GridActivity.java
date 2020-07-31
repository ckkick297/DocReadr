package com.example.keerat666.listviewpdfui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.GridView;

import com.example.keerat666.listviewpdfui.adapters.CustomGridViewAdapter;
import com.example.keerat666.listviewpdfui.adapters.item;

import java.util.ArrayList;

public class GridActivity extends AppCompatActivity {

    GridView gridView;
    ArrayList<item> gridArray = new ArrayList<item>();
    CustomGridViewAdapter customGridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_main);

        //set grid view item

        Bitmap homeIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_folder_black_24dp);
        Bitmap userIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_folder_black_24dp);

        gridArray.add(new item(homeIcon,"Home"));
        gridArray.add(new item(userIcon,"User"));
        gridArray.add(new item(homeIcon,"House"));
        gridArray.add(new item(userIcon,"Friend"));
        gridArray.add(new item(homeIcon,"Home"));
        gridArray.add(new item(userIcon,"Personal"));
        gridArray.add(new item(homeIcon,"Home"));
        gridArray.add(new item(userIcon,"User"));
        gridArray.add(new item(homeIcon,"Building"));
        gridArray.add(new item(userIcon,"User"));
        gridArray.add(new item(homeIcon,"Home"));
        gridArray.add(new item(userIcon,"xyz"));

        gridView = (GridView) findViewById(R.id.gridView1);
        customGridAdapter = new CustomGridViewAdapter(this, R.layout.row_grid, gridArray);
        gridView.setAdapter(customGridAdapter);
    }





}
