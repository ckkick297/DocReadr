package com.example.keerat666.listviewpdfui;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.pdf.PdfRenderer;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class traverseList extends ArrayAdapter<String> {

    private final Activity context;
    ArrayList<String> maintitle = new ArrayList<>();
    ArrayList<String> subtitle = new ArrayList<>();
    ArrayList<Integer> imgid = new ArrayList<>();
    ArrayList<String> path = new ArrayList<>();
    ArrayList<String> path_folder = new ArrayList<>();
    ArrayList<String> idd = new ArrayList<>();

    SQLiteDatabase db;




    public traverseList(Activity context,   ArrayList<String>maintitle, ArrayList<String> subtitle, ArrayList<Integer> imgid,ArrayList<String> path,ArrayList<String> pathfolder , ArrayList<String> idd) {
        super(context, R.layout.toggle, maintitle);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.maintitle=maintitle;
        this.subtitle=subtitle;
        this.imgid=imgid;
        this.path=path;
        this.path_folder=pathfolder;

    }

    public void writetodb(String pdfpath)
    {

        db = context.openOrCreateDatabase("Documents",0,null);        try {
            db.execSQL("CREATE TABLE IF NOT EXISTS tempPath(fpath VARCHAR);");
        try {

            String sql =
                    "INSERT or replace INTO tempPath VALUES('"+pdfpath+"')" ;
            Log.d("path",""+pdfpath);
            db.execSQL(sql);
            Log.d("Written to db","Written to db");
            //Toast.makeText(context, ""+pdfpath, Toast.LENGTH_SHORT).show();
        }
        catch (Exception e) {
            Log.d("error db",e.toString());
        }

        }
        catch (Exception e) {
            Log.d("error db",e.toString());
        }
    }

    public void delfromdb(String pdfpath)
    {

        db = context.openOrCreateDatabase("Documents",0,null);        try {
        db.execSQL("CREATE TABLE IF NOT EXISTS tempPath(fpath VARCHAR);");
        try {

            db.delete("tempPath","fpath=?",new String[]{pdfpath});
            Log.d("del stat","Deleted");
        }
        catch (Exception e) {
            Log.d("error db",e.toString());
        }

    }
    catch (Exception e) {
        Log.d("error db",e.toString());
    }
    }


    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.toggle, null,true);

        final TextView titleText = (TextView) rowView.findViewById(R.id.title);

        final ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView subtitleText = (TextView) rowView.findViewById(R.id.subtitle4);
        ImageView imageView2 = (ImageView) rowView.findViewById(R.id.icon2);
        titleText.setText(maintitle.get(position));
        if (path.get(position).endsWith("pdf")){
            imageView.setImageResource(imgid.get(position));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    File file = new File(path.get(position));
                    try{


                        int REQ_WIDTH = imageView.getWidth();
                        int REQ_HEIGHT = imageView.getHeight();


                        Bitmap bitmap = Bitmap.createBitmap(REQ_WIDTH, REQ_HEIGHT, Bitmap.Config.ARGB_4444);

                        PdfRenderer renderer = new PdfRenderer(ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY));

                        //if (currentPage < 0){
                        //  currentPage = 0;
                        //}else if (currentPage > renderer.getPageCount()) {
                        //  currentPage = renderer.getPageCount() - 1;
                        // }
                        Matrix m = imageView.getImageMatrix();
                        Rect rect =new Rect(0, 0, REQ_WIDTH, REQ_HEIGHT);
                        renderer.openPage(0).render(bitmap, rect, m, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                        imageView.setImageMatrix(m);
                        imageView.setImageBitmap(bitmap);
                        imageView.invalidate();


                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 100);
        }else {
            imageView.setImageResource(imgid.get(position));
        }
        subtitleText.setText(subtitle.get(position));
        final Switch c=(Switch)rowView.findViewById(R.id.switch1);
        c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position

                if(isChecked)
                {
                    Log.d("sstaus",""+isChecked+""+path_folder.get(position));
                    c.setChecked(true);
                    writetodb(path.get(position));

                }
                else
                {
                    Log.d("sstaus",""+isChecked+""+path_folder.get(position));
                    c.setChecked(false);
                    delfromdb(path.get(position));
                }
            }
        });
        return rowView;

    };

}