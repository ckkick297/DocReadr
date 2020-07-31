package com.example.keerat666.listviewpdfui;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.keerat666.listviewpdfui.adapters.CustomGridViewAdapter;
import com.example.keerat666.listviewpdfui.adapters.item;
import com.itextpdf.text.pdf.PdfReader;
import com.leinardi.android.speeddial.SpeedDialView;
import com.obsez.android.lib.filechooser.ChooserDialog;
import com.victor.loading.rotate.RotateLoading;

import org.apache.commons.io.comparator.LastModifiedFileComparator;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class Main extends AppCompatActivity {

    ListView list;
    View v;
    Switch c;
    Dialog yourDialog;
    ImageButton ibut;
    TextView t;
    SearchView sview;
    int sl = 0;
    androidx.appcompat.widget.Toolbar tb;
    TextView emptytext;
    ArrayList<String> maintitle = new ArrayList<String>();
    ArrayList<String> subtitle = new ArrayList<String>();
    ArrayList<String> subtitle2 = new ArrayList<String>();
    ArrayList<Integer> imgid = new ArrayList<Integer>();
    ArrayList<Integer> playid = new ArrayList<Integer>();
    ArrayList<String> path = new ArrayList<>();
    ArrayList<String> maintitle_f = new ArrayList<String>();
    ArrayList<String> subtitle_f = new ArrayList<String>();
    ArrayList<Integer> imgid_f = new ArrayList<Integer>();
    ArrayList<String> path_f = new ArrayList<>();
    ArrayList<String> idd = new ArrayList<>();
    ArrayList<String> path_folder = new ArrayList<>();
    ArrayList<Date> datemodified = new ArrayList<>();
    ArrayList<File> filelist = new ArrayList<>();
    ArrayList<String> maintitle_b = new ArrayList<String>();
    ArrayList<String> subtitle_b = new ArrayList<String>();
    ArrayList<Integer> imgid_b = new ArrayList<Integer>();
    ArrayList<String> path_b = new ArrayList<>();
    ArrayList<Integer> cp = new ArrayList<>();
    ArrayList<Integer> nop = new ArrayList<>();

    private List<String> fileList = new ArrayList<String>();

    LinkedList items = new LinkedList<Object>();

    private final int REQ_CODE = 100;
    String pdfpath;
    SQLiteDatabase db;
    Button butdel;
    private File pdfFile;
    private String pth;
    private String txtpath;
    private String tpath;
    private File file;
    MyListView adapter;
    traverseList adapt;
    bookmark_layout ad;
    String lastPath = "";
    int lastPage = 0;
    String audiol;
    String wordsl;
    Menu menu;
    int count = 0;
    static final Integer READ_EXST = 0x4;
    static ArrayList<String> pdf = new ArrayList<String>();
    static ArrayList<String> txt = new ArrayList<String>();

    private RotateLoading rotateLoading;
    SpeedDialView speedDialView, speedDialView1;

    int set = 0;
    RadioButton buttonnew, btnnew, btndoc, btnweb;
    String pdfPattern = "";
    String pdfPatternn = "";

    NotificationCompat.Builder mBuilder;
    Button search_button;
    RadioGroup radiogrp;
    int getSelected;
    TextView textinsert;
    ImageButton imageButton;
    GridView gridView;
    TextView insertext;
    ArrayList<item> gridArray = new ArrayList<item>();
    CustomGridViewAdapter customGridAdapter;
    String directoryName;
    List<File> files;
    public String xx="";
    public int yy=0;
    int counter = 1;
    String parsedText = "";
    ArrayList<String> nm = new ArrayList<String>();
    int nof = 0;
    private Context mContext;
    private Activity mActivity;
    private CoordinatorLayout mCLayout;


    @SuppressLint({"WrongViewCast", "WrongConstant"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mContext = getApplicationContext();
        mActivity = Main.this;

        askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE, READ_EXST);


        pdf.clear();
        maintitle_f.clear();
        subtitle_f.clear();
        imgid_f.clear();
        path_f.clear();
        path_folder.clear();
        filelist.clear();

        pdfPattern = ".pdf";

        adapt = new traverseList(this, maintitle_f, subtitle_f, imgid_f, path_f, path_folder, idd);

        Log.d("Searching pdf", "TRUE");
        sl = 0;
        // btnnew.setVisibility(btnnew.GONE);


        // generatef("/storage/emulated/0/Download/1536252254408_SRM -Shortlisting-Technical Interview.pdf");
//            rotateLoading.start();
        dropdb();

        new Main.MyAsyncTask().execute();



        //  mCLayout = (CoordinatorLayout) findViewById(R.id.buttoncont);
        db = openOrCreateDatabase("Documents", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        Log.e("DATABASE", String.valueOf(db));

        try {
            Intent intent = getIntent();
            lastPath = intent.getStringExtra("PathTrack");
            lastPage = Integer.parseInt(intent.getStringExtra("PageTrack"));
            // Toast.makeText(this, "page: "+lastPage, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.d("intent error", "" + e);
        }



        imageButton = (ImageButton) findViewById(R.id.imageButton1);
        //textinsert =(TextView)findViewById(R.id.textinsert);
        // insertext=(TextView) findViewById(R.id.insertext);
        final String pathx = Environment.getExternalStorageDirectory().getAbsolutePath();
        final File f = new File(pathx);//converted string object to file
        final String[] values = f.list();//getting the list of files in string array
        //now presenting the data into screen
        rotateLoading = (RotateLoading) findViewById(R.id.rotateloading);
        list = (ListView) findViewById(R.id.list);




        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final String contactId = ((TextView) view.findViewById(R.id.title)).getText().toString();
                final String ffpath=path.get(position);
                // Toast.makeText(Main.this, ""+contactId, Toast.LENGTH_SHORT).show();
                Rect displayRectangle = new Rect();
                Window window = Main.this.getWindow();
                window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

                yourDialog = new Dialog(Main.this);
                LayoutInflater inflater = (LayoutInflater) Main.this.getSystemService(LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.delete_dialog, (ViewGroup) findViewById(R.id.ldelete));
                layout.setMinimumWidth((int) (displayRectangle.width() * 0.8f));
                layout.setMinimumHeight((int) (displayRectangle.height() * 0.1f));
                yourDialog.setContentView(layout);
                yourDialog.show();
                butdel=(Button)layout.findViewById(R.id.buttondel);
                butdel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            deleteentry(ffpath);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                ibut=(ImageButton)layout.findViewById(R.id.imageButton7);
                ibut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            deleteentry(ffpath);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                return true;
            }
        });

        adapter = new MyListView(this, maintitle, subtitle, imgid, playid, path, subtitle2);
        db = openOrCreateDatabase("Documents", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        try {
            db.execSQL("CREATE TABLE IF NOT EXISTS filemaster(fpath VARCHAR,fileName VARCHAR" +
                    " ," + "audioLenth VARCHAR,nop VARCHAR, type VARCHAR);");

            // Toast.makeText(Main.this, "table created ", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            //Toast.makeText(Main.this, "ERROR "+e.toString(), Toast.LENGTH_LONG).show();
            Log.d("error db", e.toString());
        }

        RelativeLayout buttonContainer = (RelativeLayout) findViewById(R.id.buttoncont);
        buttonnew = (RadioButton) findViewById(R.id.SEARCH_ALL);
        btnnew = (RadioButton) findViewById(R.id.TXT);
        // btndoc = (RadioButton) findViewById(R.id.DOC);
        btnweb = (RadioButton) findViewById(R.id.PDF);
        search_button = (Button) findViewById(R.id.search_button);
        sview = (SearchView) findViewById(R.id.searchView1);
        final ImageView speak = findViewById(R.id.speakVoice);
        speedDialView = findViewById(R.id.speedDial);
        tb = (Toolbar) findViewById(R.id.toolbar2);
        sview.setQueryHint("Search Keyword in Document");

        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

                if (intent.resolveActivity(getPackageManager()) !=null){
                    startActivityForResult(intent,REQ_CODE);
                }else {
                    Toast.makeText(getApplicationContext(),
                            "Sorry your device not supported",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });


        gridView = (GridView) findViewById(R.id.gridView1);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                folderimport();
            }
        });



        try {
            index();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE: {
                if (resultCode == RESULT_OK && data != data) {
                    ArrayList result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    //      insertext.setText((String) result.get(0));
                }
                break;
            }
        }
    }
    @SuppressLint("WrongConstant")
    public void folderWriteData(String folderpath) {
//        String check = folderpath.substring((folderpath.length() - 3));
        File x = new File(folderpath);
        String folderName = x.getName();
        SQLiteDatabase db;
        File pdfpath;
        {
            pdfpath = new File(folderpath);
            db = openOrCreateDatabase("Documents", SQLiteDatabase.CREATE_IF_NECESSARY, null);
            try {
                db.execSQL("CREATE TABLE IF NOT EXISTS folder(folderpath VARCHAR,folderName VARCHAR , type VARCHAR);");
                String sql;
                //w Toast.makeText(ImportActivity.this, "table created ", Toast.LENGTH_LONG).show();
                sql =
                        "INSERT or replace INTO folder VALUES('" + folderpath + "','" + folderName + "','pdf')";

                sql =
                        "INSERT or replace INTO folder VALUES('" + folderpath + "','" + folderName + "','txt')";


                Log.d("insert status", "" + true);


                db.execSQL(sql);
            } catch (Exception e) {
                Log.d("error db", e.toString());
            }

        }
    }


    public void ListDir(File f) {

        buttonnew.setVisibility(buttonnew.GONE);
        search_button.setVisibility(btnnew.GONE);
        gridView.setVisibility(gridView.VISIBLE);
        btnnew.setVisibility(btnnew.GONE);
        //  btndoc.setVisibility(btndoc.GONE);
        btnweb.setVisibility(btnweb.GONE);

        list = (ListView) findViewById(R.id.list);
        File dir = Environment.getExternalStorageDirectory().getAbsoluteFile();
        String path = dir.getAbsolutePath();
        //String path=Environment.getExternalStorageDirectory().getAbsolutePath();
        File ff = new File(String.valueOf(path));//converted string object to file
        String[] values = ff.list();
        gridView = (GridView) findViewById(R.id.gridView1);
       /* customGridAdapter = new CustomGridViewAdapter(this, R.layout.row_grid, values);
        gridView.setAdapter(customGridAdapter);
*/
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext()
                , R.layout.row_grid, R.id.item_text, values);
        gridView.setAdapter(adapter);//setting the adapter
    }


    public void PDF() {
        pdf.clear();
        maintitle_f.clear();
        subtitle_f.clear();
        imgid_f.clear();
        path_f.clear();
        path_folder.clear();
        filelist.clear();

        pdfPattern = ".pdf";

        adapt = new traverseList(this, maintitle_f, subtitle_f, imgid_f, path_f, path_folder, idd);

        Log.d("Searching pdf", "TRUE");
        sl = 0;
        // btnnew.setVisibility(btnnew.GONE);
        btnnew.setVisibility(btnnew.GONE);
        buttonnew.setVisibility(buttonnew.GONE);
        //      btndoc.setVisibility(btndoc.GONE);
        btnweb.setVisibility(btnweb.GONE);
        search_button.setVisibility(search_button.GONE);

        // generatef("/storage/emulated/0/Download/1536252254408_SRM -Shortlisting-Technical Interview.pdf");
        rotateLoading.start();
        dropdb();

        new Main.MyAsyncTask().execute();
    }


    public void files() {
        pdf.clear();
        maintitle_f.clear();
        subtitle_f.clear();
        imgid_f.clear();
        path_f.clear();
        path_folder.clear();
        filelist.clear();

        pdfPattern = ".txt";

        adapt = new traverseList(this, maintitle_f, subtitle_f, imgid_f, path_f, path_folder, idd);

        Log.d("Searching txt", "TRUE");
        sl = 0;
        // btnnew.setVisibility(btnnew.GONE); pdf.clear();
        maintitle_f.clear();
        subtitle_f.clear();
        imgid_f.clear();
        path_f.clear();
        path_folder.clear();
        filelist.clear();

        ArrayList<String> pdfPattern = new ArrayList<String>();
        String s1 = ".pdf";
        // String s2 = ".docx";
        String s3 = ".txt";
        pdfPattern.add(s1);
        //pdfPattern.add(s2);
        pdfPattern.add(s3);

      /*  pdfPattern = "docx";
        pdfPattern = ".txt";
        pdfPatternn= ".pdf";*/

        adapt = new traverseList(this, maintitle_f, subtitle_f, imgid_f, path_f, path_folder, idd);

        Log.d("Searching docx", "TRUE");
        sl = 0;
        // btnnew.setVisibility(btnnew.GONE);
        btnnew.setVisibility(btnnew.GONE);
        buttonnew.setVisibility(buttonnew.GONE);
        //     btndoc.setVisibility(btndoc.GONE);
        btnweb.setVisibility(btnweb.GONE);
        search_button.setVisibility(search_button.GONE);

        // generatef("/storage/emulated/0/Download/1536252254408_SRM -Shortlisting-Technical Interview.pdf");
        rotateLoading.start();
        dropdb();

        new Main.MyAsyncTask().execute();
    }



    public void ALLfiles() {
        pdf.clear();
        maintitle_f.clear();
        subtitle_f.clear();
        imgid_f.clear();
        path_f.clear();
        path_folder.clear();
        filelist.clear();

        ArrayList<String> pdfPattern = new ArrayList<String>();
        String s1 = ".pdf";
        // String s2 = ".docx";
        String s3 = ".txt";
        pdfPattern.add(s1);
        //pdfPattern.add(s2);
        pdfPattern.add(s3);

      /*  pdfPattern = "docx";
        pdfPattern = ".txt";
        pdfPatternn= ".pdf";*/

        adapt = new traverseList(this, maintitle_f, subtitle_f, imgid_f, path_f, path_folder, idd);

        Log.d("Searching docx", "TRUE");
        sl = 0;
        // btnnew.setVisibility(btnnew.GONE);

        // generatef("/storage/emulated/0/Download/1536252254408_SRM -Shortlisting-Technical Interview.pdf");
        rotateLoading.start();
        dropdb();

        new Main.MyAsyncTask().execute();
    }


    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(Main.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(Main.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(Main.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(Main.this, new String[]{permission}, requestCode);
            }
        } else {
            //Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }

    public class MyAsyncTask extends AsyncTask<Integer, Void, String> {
        @Override
        protected String doInBackground(Integer... params) {
            pdf.clear();
            path_folder.clear();
            Search_Dir(new File(Environment.getExternalStorageDirectory().getAbsolutePath()));
            return "OK";
        }

        @SuppressLint("WrongConstant")
        @Override
        protected void onPostExecute(String result) {


            int i = 0;
            sl = filelist.size();
            ListView l = (ListView) findViewById(R.id.list);
            l.setAdapter(adapt);
            Log.d("pdf list", "" + pdf);
            File[] flarray = new File[filelist.size()];
            filelist.toArray(flarray);
            Arrays.sort(flarray, LastModifiedFileComparator.LASTMODIFIED_COMPARATOR);
            filelist = new ArrayList<File>(Arrays.asList(flarray));
            Collections.reverse(filelist);
            path_folder.clear();

            for (int j = 0; j < sl; j++)
                path_folder.add(filelist.get(j).getAbsolutePath());
            sl = filelist.size();


            for (i = 0; i < sl; i++) {
                String p = filelist.get(i).getAbsolutePath();
                String sb = p.substring(p.length() - 3);
                Log.d("pdfpattern", "" + sb);

                String fnamese = filelist.get(i).getName();

                if (fnamese.endsWith("pdf"))
                    generatef(filelist.get(i));

//                else if (fnamese.endsWith("txt"))
//                    generatetxt_f(filelist.get(i));

//                else if (fnamese.endsWith("docx"))
//                    generateDOCX(filelist.get(i));
//
//                else if (fnamese.endsWith("pdf,txt"))
//                    generateALL(filelist.get(i));
                else if (fnamese.equals(fileList))
                    Toast.makeText(getApplicationContext(),"alredy exist",Toast.LENGTH_SHORT).show();

            }
            //   tb.setTitle(sl + " Files Found");
            //   Toast.makeText(Main.this, "Total "+sl+" files found !", Toast.LENGTH_SHORT).show();

            rotateLoading.stop();
            list.setAdapter(adapt);
            db = openOrCreateDatabase("Documents", SQLiteDatabase.CREATE_IF_NECESSARY, null);
            try {
                db.execSQL("CREATE TABLE IF NOT EXISTS filemaster(fpath VARCHAR,fileName VARCHAR ,audioLenth VARCHAR,nop VARCHAR, type VARCHAR);");

            } catch (Exception e) {
                Toast.makeText(Main.this, "ERROR " + e.toString(), Toast.LENGTH_LONG).show();
                Log.d("error db", e.toString());
            }
            set = 1;
        }
    }

    public void folderimport() {
        Intent importing = new Intent(this, ImportActivity.class);
        startActivity(importing);
    }

    public void nextFile(View view) {
        if (counter <= nof - 1) {
            setData(nm.get(counter), counter);
            counter++;
        } else {
            Intent i = new Intent(this, Main.class);
            startActivity(i);
        }

    }

    @SuppressLint("WrongConstant")
    public void setData(String d, int x) {
        //name = (TextView) findViewById(R.id.name);
        File temp = new File(d);
        //  name.setText(temp.getName());
        //Toast.makeText(this, " No of files left: "+(nof-x-1), Toast.LENGTH_SHORT).show();
        if (x == 0) {
            xx = d;
            yy = 1;
            rotateLoading = (RotateLoading) findViewById(R.id.rotateloading2);
            rotateLoading.start();


            // new ImportActivity.countwords().execute();


        } else {
            xx = d;
            yy = 0;
            rotateLoading = (RotateLoading) findViewById(R.id.rotateloading2);
            rotateLoading.start();
            ;
            //new ImportActivity.countwords().execute();

        }


    }



    public void Access() {

        int yes = 0;
        int no = 0;
        for (int i = 0; i <= list.getCount() - 1; i++) {
            v = list.getAdapter().getView(i, null, null);
            c = (Switch) v.findViewById(R.id.switch1);
            t = (TextView) v.findViewById(R.id.title);
            Boolean switchState = c.isChecked();
            Log.d("Status", "" + switchState + "");
            if (switchState) {
                yes++;
            } else
                no++;
        }
        Log.d("Yes", "" + yes);
        Log.d("No", "" + no);

    }

    public void Search_Dir(File dir) {

        File FileList[] = dir.listFiles();

        if (FileList != null) {
            for (int i = 0; i < FileList.length; i++) {

                if (FileList[i].isDirectory()) {
                    Search_Dir(FileList[i]);

                } else if (FileList[i].getName().endsWith(pdfPattern)) /*|| FileList[i].getName().endsWith("txt")
                        || FileList[i].getName().endsWith("docx"))*/ {
                    //here you have that file.
                    String name = FileList[i] + "";
                    Log.d("file found", "" + name);

                    if (FileList[i].getName().endsWith(pdfPattern) || FileList[i].getName().endsWith("txt"))
                        pdf.add(FileList[i].getName());

                    path_folder.add(FileList[i].toString());


                    Date lastModDate = new Date(FileList[i].lastModified());
                    datemodified.add(lastModDate);
                    filelist.add(FileList[i]);
                    count++;


                }
            }
        }
    }


/*
    public void itext() {
        try {
            db.delete("tempPath", null, null);

        } catch (Exception e) {
            Log.d("error temp txt", "" + e);
        }
        new ChooserDialog().with(this)
                .withStartFile(pth)
                .withChosenListener(new ChooserDialog.Result() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void onChoosePath(String path, File pathFile) {
                        String check = path.substring((path.length() - 3));
                        String fname = path.substring(0, (path.length() - 3));
                        if (check.equals("pdf")) {
                            pdfpath = path;
                            db = openOrCreateDatabase("Documents", 0, null);
                            try {
                                db.execSQL("CREATE TABLE IF NOT EXISTS tempPath(fpath VARCHAR);");
                                try {

                                    String sql =
                                            "INSERT or replace INTO tempPath VALUES('" + pdfpath + "')";
                                    Log.d("path", "" + pdfpath);
                                    db.execSQL(sql);
                                    Log.d("Written to db", "Written to db");
                                    goimport();
                                } catch (Exception e) {
                                    Log.d("error db", e.toString());
                                }
                            } catch (Exception e) {
                                Log.d("database open error", e + "");
                            }
                        } else
                            Toast.makeText(Main.this, "Please choose a pdf file", Toast.LENGTH_SHORT).show();


                    }
                })
                .build()
                .show();
    }
*/


    //modify for browse
    public void itext() {
        try {
            db.delete("tempPath", null, null);

        } catch (Exception e) {
            Log.d("error temp txt", "" + e);
        }
        new ChooserDialog().with(this)
                .withStartFile(pth)
                .withChosenListener(new ChooserDialog.Result() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void onChoosePath(String path, File pathFile) {
                        String check = path.substring((path.length() - 3));
                        String fname = path.substring(0, (path.length() - 3));

                        pdfpath = path;
                        db = openOrCreateDatabase("Documents", 0, null);
                        try {
                            db.execSQL("CREATE TABLE IF NOT EXISTS tempPath(fpath VARCHAR);");
                            try {
                                String sql = "INSERT or replace INTO tempPath VALUES('" + pdfpath + "')";
                                Log.d("path", "" + pdfpath);
                                db.execSQL(sql);
                                Log.d("Written to db", "Written to db");
                                goimport();
                            } catch (Exception e) {
                                Log.d("error db", e.toString());
                            }
                        } catch (Exception e) {
                            Log.d("database open error", e + "");
                        }
                    }
                })
                .build()
                .show();
    }


    private ArrayList<String> FetchImages() {
        String Path;
        ArrayList<String> filenames = new ArrayList<String>();


        Path = Environment.getExternalStorageDirectory().getAbsolutePath();
        Log.e("PATH 1 Line", Path);
/*
                + File.separator + "Your folder name";
*/

        File directory = new File(Path);
        File[] files = directory.listFiles();

        for (int i = 0; i < files.length; i++) {

            String file_name = files[i].getName();
            // you can store name to arraylist and use it later
            filenames.add(file_name);


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext()
                    , R.layout.row_grid, R.id.item_text, filenames);
            gridView.setAdapter(adapter);//setting the adapter
        }



/*        ArrayAdapter<String> fileList =
                new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, filenames);
        list.setAdapter(fileList);*/


        return filenames;
    }


    @SuppressLint("WrongConstant")
    public void search(String ss) {
        list = (ListView) findViewById(R.id.list);
        list.setAdapter(null);
        try {
            SQLiteDatabase db;
            db = openOrCreateDatabase("Documents", SQLiteDatabase.CREATE_IF_NECESSARY, null);

            Cursor resultSet = db.rawQuery("Select * from filemaster", null);
            String array[] = new String[resultSet.getCount()];
            String fileNames[] = new String[resultSet.getCount()];
            int i = 0;
            adapter = new MyListView(this, maintitle, subtitle, imgid, playid, path, subtitle2);
           // adapter.clear();
            resultSet.moveToFirst();
            while (!resultSet.isAfterLast()) {
                array[i] = resultSet.getString(0);
                fileNames[i] = resultSet.getString(4);
                resultSet.moveToNext();
                pdfFile = new File(array[i]);

                if (Pattern.compile(Pattern.quote(ss), Pattern.CASE_INSENSITIVE).matcher(pdfFile.getName()).find()) {
                    if (pdfFile.getName().length() > 10) {
                        String d = pdfFile.getName();
                        maintitle.add(d);

                    } else
                        maintitle.add(pdfFile.getName());

                    subtitle.add(array[i]);

                    if (fileNames[i].equals("pdf"))
                        imgid.add(R.drawable.pdfnew);
                    else
                        imgid.add(R.drawable.txt);


                    playid.add(R.drawable.play);
                    path.add(array[i]);
                    adapter.notifyDataSetChanged();
                }

                i++;

            }
            list.setAdapter(adapter);

            Log.d("array", "" + array[0] + " " + array[1]);
            Log.d("filename", "" + fileNames[0] + " " + fileNames[1]);

        } catch (Exception ex) {
            Toast.makeText(this, "" + ex.toString(), Toast.LENGTH_SHORT).show();
        }


    }

    public void txtcall() {
        try {
            db.delete("tempPath", null, null);

        } catch (Exception e) {
            Log.d("error temp txt", "" + e);
        }

        final ChooserDialog show = new ChooserDialog().with(this)
                .withStartFile(tpath)
                .withChosenListener(new ChooserDialog.Result() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void onChoosePath(String path, File pathFile) {
                        String check = path.substring((path.length() - 3));
                        if (check.equals("txt")) {
                            txtpath = path;
                            db = openOrCreateDatabase("Documents", 0, null);
                            try {
                                db.execSQL("CREATE TABLE IF NOT EXISTS tempPath(fpath VARCHAR);");
                                try {

                                    String sql =
                                            "INSERT or replace INTO tempPath VALUES('" + txtpath + "')";
                                    Log.d("path", "" + pdfpath);
                                    db.execSQL(sql);
                                    Log.d("Written to db", "Written to db");
                                    goimport();
                                } catch (Exception e) {
                                    Log.d("error db", e.toString());
                                }
                            } catch (Exception e) {
                                Log.d("database open error", e + "");
                            }
                        } else
                            Toast.makeText(Main.this, "Please choose a txt file", Toast.LENGTH_SHORT).show();
                    }
                })
                .build()
                .show();
    }

    public void goimport() {
        Intent intent = new Intent(this, ImportActivity.class);
        startActivity(intent);
    }

    public void generate(String p) throws IOException {
        pdfFile = new File(p);
        String parsedText = "";
        PdfReader reader = new PdfReader(p);
        int n = reader.getNumberOfPages();
        maintitle.add(pdfFile.getName());
        subtitle.add("Length :" + audiol);
        subtitle2.add("Words : " + wordsl + "");
        imgid.add(R.drawable.pdfnew);
        if (p.equals(lastPath)) {
            playid.add(R.drawable.index);
        } else
            playid.add(R.drawable.play);
        path.add(p);
        adapter.notifyDataSetChanged();

        if (maintitle.size() > 7) {
            maintitle.remove((maintitle.size() - 2));
            subtitle.remove((subtitle.size() - 2));
            imgid.remove((imgid.size() - 2));
            playid.remove(playid.size() - 2);
            subtitle2.remove(subtitle2.size() - 2);
            maintitle.add("");
            subtitle.add("");
            subtitle2.add("");
            imgid.add(0);
            playid.add(0);
            adapter.notifyDataSetChanged();

        } else if (maintitle.size() == 7) {
            maintitle.add("");
            subtitle.add("");
            imgid.add(0);
            playid.add(0);
            subtitle2.add("");
            adapter.notifyDataSetChanged();
        }

    }

    public void generatetxt(String p) {
        file = new File(p);
        if (file.getName().length() > 10) {
            String d = pdfFile.getName();
            maintitle.add(d);

        } else
            maintitle.add(file.getName());
        File file = new File(p);

        subtitle.add("Length :" + audiol);
        subtitle2.add("Words : " + wordsl + "");
        imgid.add(R.drawable.pdf);
        if (p.equals(lastPath)) {
            playid.add(R.drawable.pausemain);
        } else
            playid.add(R.drawable.play);
        path.add(p);
        adapter.notifyDataSetChanged();

        if (maintitle.size() > 7) {
            maintitle.remove((maintitle.size() - 2));
            subtitle.remove((subtitle.size() - 2));
            imgid.remove((imgid.size() - 2));
            playid.remove(playid.size() - 2);
            subtitle2.remove(subtitle2.size() - 2);
            maintitle.add("");
            subtitle.add("");
            subtitle2.add("");
            imgid.add(0);
            playid.add(0);

            adapter.notifyDataSetChanged();

        } else if (maintitle.size() == 7) {
            maintitle.add("");
            subtitle.add("");
            subtitle2.add("");
            imgid.add(0);
            playid.add(0);

            adapter.notifyDataSetChanged();
        }
    }

    public void ViewPDF(String path, int cp) {
        // Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, PDFViewer.class);
        i.putExtra("Pathid", path);
        i.putExtra("current_page", cp);
        startActivity(i);
    }

    public void TakeText(String p) {

        Intent i = new Intent(this, TxtMaker.class);
        i.putExtra("txtpath", p);
        startActivity(i);

    }

    @SuppressLint("WrongConstant")
    void index() throws IOException {

//        emptytext.setVisibility(emptytext.VISIBLE);
        //      btnnew.setVisibility(btnnew.GONE);
        tb.setVisibility(tb.VISIBLE);
        gridView.setVisibility(gridView.GONE);

        db = openOrCreateDatabase("Documents", SQLiteDatabase.CREATE_IF_NECESSARY, null);

        Cursor resultSet = db.rawQuery("Select * from filemaster", null);
        String array[] = new String[resultSet.getCount()];
        String fileNames[] = new String[resultSet.getCount()];
        final int i = 0;
        resultSet.moveToFirst();
        while (!resultSet.isAfterLast()) {
            Log.d("errorqwerty", resultSet.getString(0));
            array[i] = resultSet.getString(0);
            fileNames[i] = resultSet.getString(4);
            Log.d("now", "" + resultSet.getString(3));
            wordsl = "" + resultSet.getString(3);
            audiol = "" + resultSet.getString(2);
            resultSet.moveToNext();
            pdfFile = new File(array[i]);

            if (fileNames[i].equals("pdf"))
                generate(array[i]);
            else if (fileNames[i].equals("txt"))
                generatetxt(array[i]);

        }
        list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                Intent importing = new Intent(getApplicationContext(), text.class);
//               startActivity(importing);

                String p = path.get(position);

//               if (p==p)
//               {
//
//               }
                //String sb = p.substring(p.length() - 3);
                // Toast.makeText(Main.this,position, Toast.LENGTH_SHORT).show();
                //       if (sb.equals("pdf")) {
//                   Toast.makeText(Main.this,position, Toast.LENGTH_SHORT).show();
                //if (p.equals(lastPath) && lastPage >= 0)
                ViewPDF(p, lastPage);
                // Toast.makeText(Main.this,p, Toast.LENGTH_SHORT).show();

//
//                    else
//                        ViewPDF(p, 0);
                //      }
//                if (sb.equals("txt"))
//                    TakeText(p);
                // Toast.makeText(Main.this, ""+p, Toast.LENGTH_SHORT).show();
            }
        });

        // final ProgressBar simpleProgressBar = (ProgressBar) findViewById(R.id.simpleProgressBar);

        final SearchView searchView = (SearchView) findViewById(R.id.searchView1);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //          simpleProgressBar.setVisibility(View.VISIBLE);


                search(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//              if (searchView.isExpanded() && TextUtils.isEmpty(newText)) {
                if (searchView.isActivated()){
                    rotateLoading.start();
                }
                search(newText);
                return true;
            }

            public void callSearch(String query) {
                //Do searching
                search(query);
            }

        });
        final int sl = adapter.getCount();


    }

    public void generatef(File dd) {
        Log.d("gen f", "pdf");
        pdfFile = dd;
        int file_size = Integer.parseInt(String.valueOf(pdfFile.length()));
        if (pdfFile.getName().length() > 10) {
            String d = pdfFile.getName();
            maintitle_f.add(d);
        } else
            maintitle_f.add(pdfFile.getName());
        Date dt = new Date(pdfFile.lastModified());
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy hh:mm:ss");
        String strDate = dateFormat.format(dt);
        subtitle_f.add(strDate + "");
        imgid_f.add(R.drawable.pdfnew);
        path_f.add(pdfFile.getAbsolutePath());
        // path_folder.add(pf);
        adapt.notifyDataSetChanged();

        if (maintitle_f.size() > 7) {
            maintitle_f.remove((maintitle_f.size() - 2));
            subtitle_f.remove((subtitle_f.size() - 2));
            imgid_f.remove((imgid_f.size() - 2));
            maintitle_f.add("");
            subtitle_f.add("");
            imgid_f.add(0);
            adapt.notifyDataSetChanged();

        } else if (maintitle_f.size() == 7) {
            maintitle_f.add("");
            subtitle_f.add("");
            imgid_f.add(0);
            adapt.notifyDataSetChanged();
        }

    }




    public void generateALL(File docx) {
        Log.d("gen doc file", "docx");

        File file = docx;

        int size = 0;

        if (file.getName().length() > 10) {
            String d = file.getName();
            maintitle_f.add(d);
        } else
            maintitle_f.add(file.getName());


        Date dt = new Date(file.lastModified());
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy hh:mm:ss");
        String strDate = dateFormat.format(dt);
        subtitle_f.add(strDate + "");


        if (pdfPattern.endsWith("doc")) {
            imgid_f.add(R.drawable.doc_icon);

        } else if (pdfPattern.endsWith("pdf")) {
            imgid_f.add(R.drawable.pdfnew);

        } else if (pdfPattern.endsWith("txt")) {
            imgid_f.add(R.drawable.pdf);

        }

        path_f.add(file.getAbsolutePath());

        adapt.notifyDataSetChanged();

        if (maintitle_f.size() > 7) {
            maintitle_f.remove((maintitle_f.size() - 2));
            subtitle_f.remove((subtitle_f.size() - 2));
            imgid_f.remove((imgid_f.size() - 2));
            maintitle_f.add("");
            subtitle_f.add("");
            imgid_f.add(0);
            adapt.notifyDataSetChanged();

        } else if (maintitle_f.size() == 7) {
            maintitle_f.add("");
            subtitle_f.add("");
            imgid_f.add(0);

            adapt.notifyDataSetChanged();
        }
    }

    @SuppressLint("WrongConstant")
    public void dropdb() {

        db = openOrCreateDatabase("Documents", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        try {
            String DATABASE_TABLE = "tempPath";
            db.execSQL("DROP TABLE IF EXISTS '" + DATABASE_TABLE + "'");
            Log.d("del stat", "Deleted");
        } catch (Exception e) {
            Log.d("error db", e.toString());
        }

    }


    public void traverse(View view) {
        pdf.clear();
        maintitle_f.clear();
        subtitle_f.clear();
        imgid_f.clear();
        path_f.clear();
        path_folder.clear();
        filelist.clear();

        switch (view.getId()) {
            case R.id.SEARCH_ALL: {
                pdfPattern = ".pdf";
                adapt = new traverseList(this, maintitle_f, subtitle_f, imgid_f, path_f, path_folder, idd);

                Log.d("Searching pdf", "TRUE");
                sl = 0;
                // btnnew.setVisibility(btnnew.GONE);
                btnnew.setVisibility(btnnew.GONE);
                buttonnew.setVisibility(buttonnew.GONE);
                //     btndoc.setVisibility(btndoc.GONE);
                btnweb.setVisibility(btnweb.GONE);
                search_button.setVisibility(search_button.GONE);
                // generatef("/storage/emulated/0/Download/1536252254408_SRM -Shortlisting-Technical Interview.pdf");
                rotateLoading.start();
                dropdb();
                //pdf.clear();
                new Main.MyAsyncTask().execute();
                break;
            }





          /*  case R.id.DOC: {
                pdfPattern = ".docx";
                pdf.clear();
                path_folder.clear();

                adapt = new traverseList(this, maintitle_f, subtitle_f, imgid_f, path_f, path_folder, idd);

                ListView l = (ListView) findViewById(R.id.list);
                list.setAdapter(adapt);
                Log.d("Searching doc", "TRUE");
                sl = 0;
                btnnew.setVisibility(btnnew.GONE);
                buttonnew.setVisibility(buttonnew.GONE);
         //       btndoc.setVisibility(btndoc.GONE);
                btnweb.setVisibility(btnweb.GONE);
                search_button.setVisibility(search_button.GONE);


                // generatef("/storage/emulated/0/Download/1536252254408_SRM -Shortlisting-Technical Interview.pdf");
                rotateLoading.start();
                dropdb();

                new MyAsyncTask().execute();


                break;
            }*/

            case R.id.PDF: {
                pdfPattern = ".pdf";
                pdf.clear();
                path_folder.clear();
                adapt = new traverseList(this, maintitle_f, subtitle_f, imgid_f, path_f, path_folder, idd);

                ListView l = (ListView) findViewById(R.id.list);
                list.setAdapter(adapt);
                Log.d("Searching txt", "TRUE");
                sl = 0;
                btnnew.setVisibility(btnnew.GONE);
                buttonnew.setVisibility(buttonnew.GONE);
                //         btndoc.setVisibility(btndoc.GONE);
                btnweb.setVisibility(btnweb.GONE);
                search_button.setVisibility(search_button.GONE);


                // generatef("/storage/emulated/0/Download/1536252254408_SRM -Shortlisting-Technical Interview.pdf");
                rotateLoading.start();
                dropdb();
                //pdf.clear();
                new Main.MyAsyncTask().execute();
                break;
            }


// handle button A click;

/* case R.id.buttonnew2:
            {
                pdfPattern=".txt";
                pdf.clear();
                path_folder.clear();
                adapt = new traverseList(this, maintitle_f, subtitle_f, imgid_f,path_f,path_folder,idd);

                ListView l=(ListView)findViewById(R.id.list);
                list.setAdapter(adapt);
                Log.d("Searching txt","TRUE");
                sl=0;
                btnnew.setVisibility(btnnew.GONE);
                buttonnew.setVisibility(buttonnew.GONE);


                // generatef("/storage/emulated/0/Download/1536252254408_SRM -Shortlisting-Technical Interview.pdf");
                rotateLoading.start();
                dropdb();
                //pdf.clear();
                new MyAsyncTask().execute();
                break;


            }*//*


// handle button B click;
            default:
                throw new RuntimeException("Unknown button ID");
        }


    }
*/
        }
    }






    public void generatebm(String p , int c , int t) {
        pdfFile=new File(p);
        Log.d("Bookmark generate test","I came here");
        if(pdfFile.getName().length()>10)
        {
            String d=pdfFile.getName();
            maintitle_b.add(d);

        }
        else
            maintitle_b.add(pdfFile.getName());
        subtitle_b.add(p);
        imgid_b.add(R.drawable.pdfnew);
        path_b.add(p);
        cp.add(c);
        nop.add(t);
        ad.notifyDataSetChanged();

        if (maintitle_b.size() > 7) {
            maintitle_b.remove((maintitle_b.size() - 2));
            subtitle_b.remove((subtitle_b.size() - 2));
            imgid_b.remove((imgid_b.size() - 2));
            cp.remove(cp.size()-2);
            nop.remove(nop.size()-2);

            maintitle_b.add("");
            subtitle_b.add("");
            imgid_b.add(0);
            cp.add(0);
            nop.add(0);
            ad.notifyDataSetChanged();

        } else if (maintitle_b.size() == 7) {
            maintitle_b.add("");
            subtitle_b.add("");
            imgid_b.add(0);
            nop.add(0);
            cp.add(0);

            ad.notifyDataSetChanged();
        }

    }

    public void generatetxtbm(String p) {
        file=new File(p);

        maintitle.add(file.getName());

        subtitle.add(p);
        imgid.add(R.drawable.pdf);
        playid.add(R.drawable.play);
        path.add(p);
        adapter.notifyDataSetChanged();

        if (maintitle.size() > 7) {
            maintitle.remove((maintitle.size() - 2));
            subtitle.remove((subtitle.size() - 2));
            imgid.remove((imgid.size() - 2));
            playid.remove(playid.size() - 2);

            maintitle.add("");
            subtitle.add("");
            imgid.add(0);
            playid.add(0);

            adapter.notifyDataSetChanged();

        } else if (maintitle.size() == 7) {
            maintitle.add("");
            subtitle.add("");
            imgid.add(0);
            playid.add(0);

            adapter.notifyDataSetChanged();
        }
    }

    public void deleteentry(String s) throws IOException {
        db.execSQL("delete from "+"filemaster"+" where fpath='"+s+"'");
        try {
            maintitle.clear();
            subtitle.clear();
            subtitle2.clear();
            imgid.clear();
            playid.clear();
            path.clear();
            index();
            yourDialog.dismiss();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ham, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuNight: {
                Toast.makeText(this, "Night Mode status changed.", Toast.LENGTH_SHORT).show();
                break;
            }


            case R.id.tedit: {
                // highlight(1, 1);

            }


        }
        return true;
    }
    @Override
    public void onBackPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);

        builder.setTitle("Please confirm");
        builder.setMessage("Are you want to exit the app?");
        builder.setCancelable(true);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Do something when user want to exit the app
                // Let allow the system to handle the event, such as exit the app
                Main.super.onBackPressed();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Do something when want to stay in the app
                Toast.makeText(mContext,"thank you",Toast.LENGTH_LONG).show();
            }
        });

        // Create the alert dialog using alert dialog builder
        AlertDialog dialog = builder.create();

        dialog.show();
    }


}