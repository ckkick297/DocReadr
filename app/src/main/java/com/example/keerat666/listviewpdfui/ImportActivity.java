package com.example.keerat666.listviewpdfui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import com.example.keerat666.listviewpdfui.db.MyDbHelper;
import com.example.keerat666.listviewpdfui.models.Book;
import com.example.keerat666.listviewpdfui.models.BookFolder;
import com.example.keerat666.listviewpdfui.models.BookReadingCurrentStatus;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.victor.loading.rotate.RotateLoading;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ImportActivity extends AppCompatActivity {
    int nof = 0;
    TextView name,nameh;
    TextView count;
    TextView nop;
    TextView length;
    Spinner spinner;
    int counter = 1;
    String parsedText = "";
    String audio;
    int now = 0;
    String xx = "";
    int yy = 0;
    int n;
    String[] words;
    ArrayList<String> nm = new ArrayList<String>();
    private RotateLoading rotateLoading;
    ImageView add_button,imageButton1;

    TextView editText;
    EditText edit;
    ArrayList<String> list = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    ImageView imageView;
    Button addBtn;
    EditText input;
    String[] items;
    ListView listView;
    EditText editText1;
    ArrayList<String> listview = new ArrayList<String>();
    ListView List;

    EditText textIn;
    ImageButton buttonAdd;
    LinearLayout container;
    ArrayList<CharSequence> itemList;
    String TAG = "AndroidDynamicView";

    TextToSpeech t1;
    List<String> categories;

    RelativeLayout relativeLayoutSearchingPage;
    TextView textViewEbookCount;

    List<BookFolder> folderList;
    Spinner spinnerFolder;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences app_preferences = getSharedPreferences("MyTheme", MODE_PRIVATE);
        String theme = app_preferences.getString("MyAppTheme", null);

        if (getString(R.string.theme_light).equals(theme)) {
            setTheme(R.style.AppTheme);
        }
        if (getString(R.string.theme_warm).equals(theme)){
            setTheme(R.style.AppThemeWarm);
        }
        if (getString(R.string.theme_dark).equals(theme)){
            setTheme(R.style.AppThemeDark);
        }

        setContentView(R.layout.activity_import);
        getSupportActionBar().hide();

        folderList = new MyDbHelper(this).getAllBookFolder();
        folderList.remove(0);

        ArrayList<String> folderListString = new ArrayList<>();
        for (int i=0; i<folderList.size(); i++){
            folderListString.add(folderList.get(i).getFolder_name());
        }

        spinnerFolder = findViewById(R.id.spinnerFolder);
        ArrayAdapter<String> folderAdp = new ArrayAdapter<String>(ImportActivity.this, android.R.layout.simple_spinner_item, folderListString);
        folderAdp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFolder.setAdapter(folderAdp);

        final ImageButton imageButtonAddFolder  = findViewById(R.id.ibAddFolder);
        imageButtonAddFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu menu = new PopupMenu(ImportActivity.this, imageButtonAddFolder);
                menu.getMenu().add(Menu.NONE,1,1,"Add");
                menu.getMenu().add(Menu.NONE,2,2,"Edit");
                menu.getMenu().add(Menu.NONE,3,3,"Remove");
                menu.show();

                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                       // Toast.makeText(getApplicationContext(), ""+item.getItemId(), Toast.LENGTH_SHORT).show();
                        switch (item.getItemId()){
                            case 1:
                                final EditText taskEditText = new EditText(ImportActivity.this);
                                taskEditText.setHint("Enter folder Name");
                                AlertDialog dialog = new AlertDialog.Builder(ImportActivity.this)
                                        .setTitle("Create folder")
                                        .setView(taskEditText)
                                        .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                String task = String.valueOf(taskEditText.getText());
                                                BookFolder bookFolder = new BookFolder();
                                                bookFolder.setFolder_name(task);
                                                bookFolder.setFolder_user_id(Integer.parseInt(new MyDbHelper(ImportActivity.this).getAllUser().getLogin_user_app_user_id()));
                                                new MyDbHelper(ImportActivity.this).insertBookFolder(bookFolder);

                                                folderList = new MyDbHelper(ImportActivity.this).getAllBookFolder();
                                                folderList.remove(0);

                                                ArrayList<String> folderListString = new ArrayList<>();
                                                for (int i=0; i<folderList.size(); i++){
                                                    folderListString.add(folderList.get(i).getFolder_name());
                                                }

                                                Spinner spinnerFolder = findViewById(R.id.spinnerFolder);
                                                ArrayAdapter<String> folderAdp = new ArrayAdapter<String>(ImportActivity.this, android.R.layout.simple_spinner_item, folderListString);
                                                folderAdp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                spinnerFolder.setAdapter(folderAdp);

                                                spinnerFolder.setSelection(folderListString.size()-1);

                                                Toast.makeText(getApplicationContext(), "Folder Added", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .setNegativeButton("Cancel", null)
                                        .create();
                                dialog.show();
                                break;
                            case 2:
                                final EditText editText = new EditText(ImportActivity.this);
                                editText.setHint("Enter folder Name");
                                editText.setText(folderList.get(spinnerFolder.getSelectedItemPosition()).getFolder_name());
                                AlertDialog dialog1 = new AlertDialog.Builder(ImportActivity.this)
                                        .setTitle("Rename folder")
                                        .setView(editText)
                                        .setPositiveButton("Rename", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                int pos = 0;

                                                String task = String.valueOf(editText.getText());
                                                BookFolder bookFolder = new BookFolder();

                                                pos = spinnerFolder.getSelectedItemPosition();
                                                bookFolder.setFolder_id(folderList.get(spinnerFolder.getSelectedItemPosition()).getFolder_id());
                                                bookFolder.setFolder_name(task);

                                                new MyDbHelper(ImportActivity.this).updateBookFolder(bookFolder);

                                                folderList = new MyDbHelper(ImportActivity.this).getAllBookFolder();
                                                folderList.remove(0);

                                                ArrayList<String> folderListString = new ArrayList<>();
                                                for (int i=0; i<folderList.size(); i++){
                                                    folderListString.add(folderList.get(i).getFolder_name());
                                                }

                                                Spinner spinnerFolder = findViewById(R.id.spinnerFolder);
                                                ArrayAdapter<String> folderAdp = new ArrayAdapter<String>(ImportActivity.this, android.R.layout.simple_spinner_item, folderListString);
                                                folderAdp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                spinnerFolder.setAdapter(folderAdp);

                                                spinnerFolder.setSelection(pos);

                                                Toast.makeText(getApplicationContext(), "Folder Renamed", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .setNegativeButton("Cancel", null)
                                        .create();
                                dialog1.show();
                                break;
                            case 3:
                                AlertDialog dialog2 = new AlertDialog.Builder(ImportActivity.this)
                                        .setTitle("Delete folder")
                                        .setMessage("Are you sure to Delete this Folder Permanently?")
                                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {


                                                if (folderList.get(spinnerFolder.getSelectedItemPosition()).getFolder_id() == 2) {
                                                    Toast.makeText(getApplicationContext(), "Can't Delete Default folder!", Toast.LENGTH_SHORT).show();
                                                }
                                                else {
                                                    int pos = 0;

                                                    pos = spinnerFolder.getSelectedItemPosition();

                                                    new MyDbHelper(ImportActivity.this).deleteBookFolder(folderList.get(spinnerFolder.getSelectedItemPosition()).getFolder_id());

                                                    folderList = new MyDbHelper(ImportActivity.this).getAllBookFolder();
                                                    folderList.remove(0);

                                                    ArrayList<String> folderListString = new ArrayList<>();
                                                    for (int i = 0; i < folderList.size(); i++) {
                                                        folderListString.add(folderList.get(i).getFolder_name());
                                                    }

                                                    Spinner spinnerFolder = findViewById(R.id.spinnerFolder);
                                                    ArrayAdapter<String> folderAdp = new ArrayAdapter<String>(ImportActivity.this, android.R.layout.simple_spinner_item, folderListString);
                                                    folderAdp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                    spinnerFolder.setAdapter(folderAdp);

                                                    spinnerFolder.setSelection(pos - 1);

                                                    Toast.makeText(getApplicationContext(), "Folder Deleted", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        })
                                        .setNegativeButton("Cancel", null)
                                        .create();
                                dialog2.show();
                                break;
                        }
                        return false;
                    }
                });
            }
        });

        ///////////////////

        final SQLiteDatabase db;
        relativeLayoutSearchingPage = findViewById(R.id.rlSearchingPage);
        textViewEbookCount = findViewById(R.id.tvImportBookFoundCount);

        if (Constants.file_type.equals("htm")){
            relativeLayoutSearchingPage.setVisibility(View.GONE);
        }else {
            relativeLayoutSearchingPage.setVisibility(View.VISIBLE);
        }
        Constants.file_type = "file";

        rotateLoading = (RotateLoading) findViewById(R.id.rotateloading);

        RelativeLayout relativeLayout = findViewById(R.id.rlimp);

        if (getString(R.string.theme_light).equals(app_preferences.getString("MyAppTheme", null))) {
            relativeLayoutSearchingPage.setBackgroundColor(getResources().getColor(R.color.lightColorDark));
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.lightColorDark));
        }
        if (getString(R.string.theme_warm).equals(app_preferences.getString("MyAppTheme", null))){
            relativeLayoutSearchingPage.setBackgroundColor(getResources().getColor(R.color.warmColorLight));
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.warmColorLight));
        }
        if (getString(R.string.theme_dark).equals(app_preferences.getString("MyAppTheme", null))){
            relativeLayoutSearchingPage.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }

        db = openOrCreateDatabase("Documents", SQLiteDatabase.CREATE_IF_NECESSARY, null);

        Cursor resultSet = db.rawQuery("Select * from tempPath", null);
        if (resultSet == null)
        {
            return;
        }

        nof = resultSet.getCount();

        textViewEbookCount.setText(nof+" ebooks found so far...");
        categories = new ArrayList<>();
        //categories.add("English");

        spinner = (Spinner) findViewById(R.id.spinner);

        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                    Locale[] locales = Locale.getAvailableLocales();
                    List<Locale> localeList = new ArrayList<Locale>();

                    for (Locale locale : locales) {
                        int res = t1.isLanguageAvailable(locale);
                        if (res == TextToSpeech.LANG_COUNTRY_AVAILABLE) {
                            localeList.add(locale);
                            categories.add(locale.getDisplayName());
                        }
                    }

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ImportActivity.this, android.R.layout.simple_spinner_item, categories);
                    // Drop down layout style - list view with radio button
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // attaching data adapter to spinner
                    spinner.setAdapter(dataAdapter);

                    try {
                        String compareValue = "English (United States)";
                        if (compareValue != null) {
                            int spinnerPosition = dataAdapter.getPosition(compareValue);
                            spinner.setSelection(spinnerPosition);
                        }
                    }catch (Exception e){

                    }
                }

                  /*
                if (status == TextToSpeech.SUCCESS) {
                    int ttsLang = t1.setLanguage(Locale.US);

                    if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                            || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(getApplicationContext(), "The Language is not supported!", Toast.LENGTH_SHORT).show();
                        Log.e("TTS", "The Language is not supported!");
                    } else {
                        Toast.makeText(getApplicationContext(), "Language Supported.", Toast.LENGTH_SHORT).show();
                        Log.i("TTS", "Language Supported.");
                    }
                    Log.i("TTS", "Initialization success.");
                    Toast.makeText(getApplicationContext(), "Initialization success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "TTS Initialization failed!", Toast.LENGTH_SHORT).show();
                }
           */
            }
        });

       // Toast.makeText(getApplicationContext(), "Country lang added ImportActivity.class", Toast.LENGTH_LONG).show();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        //Performing action onItemSelected and onNothing selected
       // editText = (TextView) findViewById(R.id.Add);
        //edit = (EditText) findViewById(R.id.editTextResult);
        /** Defining the ArrayAdapter to set items to ListView */


        //listView = (ListView)findViewById(R.id.listVIEW);

        imageButton1= (ImageView) findViewById(R.id.backarrow);
        add_button = (ImageView) findViewById(R.id.add_button);
        imageView = (ImageView) findViewById(R.id.btn_add2);


        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ImportActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


       /* textIn = (EditText) findViewById(R.id.textin);
        buttonAdd = (ImageButton) findViewById(R.id.add);
        container = (LinearLayout) findViewById(R.id.container);

        itemList = new ArrayList<CharSequence>();

        buttonAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                addNewView(textIn.getText().toString());

            }
        });
*/




        // add button listener
      /*  add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

               // Toast.makeText(getApplicationContext(), "Im here ", Toast.LENGTH_SHORT).show();
                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(ImportActivity.this);
                View promptsView = li.inflate(R.layout.dialog, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ImportActivity.this);
                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);
                TextView userInput = (TextView) promptsView.findViewById(R.id.txt);
                userInput.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        folderWriteData(xx);
                    }
                });


                imageView = (ImageView) promptsView.findViewById(R.id.btn_add2);
              //  List = (ListView) promptsView.findViewById(R.id.listVIEW);

                // set dialog message
                alertDialogBuilder.setCancelable(false).setPositiveButton("", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // get user input and set it to result
                        // edit text
                        // editText.setText(userInput.getText());
                    }
                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                });


                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LayoutInflater li = LayoutInflater.from(ImportActivity.this);
                        View promptsView = li.inflate(R.layout.prompts, null);
                        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ImportActivity.this);
                        // set prompts.xml to alertdialog builder
                        alertDialogBuilder.setView(promptsView);

                        TextView textView = (TextView) findViewById(R.id.textView1);
                        editText1 = (EditText) findViewById(R.id.editTextResult);

                        listView = (ListView) promptsView.findViewById(R.id.listVIEWP);

                        final ArrayList<String> listview = new ArrayList<String>();

                        adapter = new ArrayAdapter<String>(ImportActivity.this,
                                android.R.layout.simple_list_item_1, listview);
                        List.setAdapter(adapter);


                        editText1 = new EditText(ImportActivity.this);
                        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                listview.add(editText1.getText().toString());
                                //editText1.setText("");
                                adapter.notifyDataSetChanged();
                            }
                        });
                        alertDialogBuilder.show();
                    }
                });


                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();

            }
        });*/
        File pdff = null;
        String array[] = new String[resultSet.getCount()];
        int i = 0;
        resultSet.moveToFirst();
        while (!resultSet.isAfterLast()) {
            array[i] = resultSet.getString(0);
            nm.add(resultSet.getString(0));
            i++;
            resultSet.moveToNext();
        }

        for (int f = 0; f < array.length; f++)
            Log.d("list666", f + "" + array[f]);

        if (array.length >= 0)
            setData(array[0], 0);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);

        Log.i(TAG, "onSaveInstanceState()");

        outState.putCharSequenceArrayList("KEY_ITEMS", itemList);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onRestoreInstanceState(savedInstanceState);

        Log.i(TAG, "onRestoreInstanceState()");

        ArrayList<CharSequence> savedItemList = savedInstanceState.getCharSequenceArrayList("KEY_ITEMS");
        if(savedItemList != null){
            for(CharSequence s : savedItemList){
                addNewView(s);

            }
        }
    }

    private void addNewView(final CharSequence newText){
        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View newView = layoutInflater.inflate(R.layout.row, null);
        TextView textOut = (TextView) newView
                .findViewById(R.id.textout);
        textOut.setText(newText);
        Button buttonRemove = (Button) newView
                .findViewById(R.id.remove);
        buttonRemove.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ((LinearLayout) newView.getParent())
                        .removeView(newView);
                itemList.remove(newText);
            }
        });

        container.addView(newView);
        itemList.add(newText);
    }

    public void nextFile(View view) {

        Constants.isBookAdded = true;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (counter <= nof - 1) {
                    setData(nm.get(counter), counter);
                    counter++;
                } else {

                    Intent i = new Intent(ImportActivity.this, MainActivity.class);
                    startActivity(i);

                }
            }
        }, 500);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Toast.makeText(getApplicationContext(), "Im her", Toast.LENGTH_SHORT).show();
                writeData(xx);
                folderWriteData(xx);
            }
        }, 100);
    }


    public void nextFolderFile(View view) {
        if (counter <= nof - 1) {
            setData(nm.get(counter), counter);
            counter++;
        } else {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }

    }


    @SuppressLint("WrongConstant")
    public void setData(String d, int x) {
        name = (TextView) findViewById(R.id.name);

        File temp = new File(d);
        name.setText(temp.getName());
        //Toast.makeText(this, " No of files left: "+(nof-x-1), Toast.LENGTH_SHORT).show();
        if (x == 0) {
            xx = d;
            yy = 1;
            rotateLoading = (RotateLoading) findViewById(R.id.rotateloading2);
            rotateLoading.start();
            ;

            new countwords().execute();


        } else {
            xx = d;
            yy = 0;
            rotateLoading = (RotateLoading) findViewById(R.id.rotateloading2);
            rotateLoading.start();
            ;
            new countwords().execute();

        }


    }


    @SuppressLint("WrongConstant")
    public void folderWriteData(String folderpath) {
        String check = folderpath.substring((folderpath.length() - 3));
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
                if (check.equals("pdf")) {
                    sql =
                            "INSERT or replace INTO folder VALUES('" + folderpath + "','" + folderName + "','pdf')";

                } else if (check.equals("htm")){
                    sql =
                            "INSERT or replace INTO folder VALUES('" + folderpath + "','" + folderName + "','htm')";
                }else {
                    sql =
                            "INSERT or replace INTO folder VALUES('" + folderpath + "','" + folderName + "','txt')";
                }

                Log.d("insert status", "" + true);

                db.execSQL(sql);
            } catch (Exception e) {
                Log.d("error db", e.toString());
            }

        }
    }


    @SuppressLint("WrongConstant")
    public void writeData(String path) {
        String check = path.substring((path.length() - 3));
        File x = new File(path);
        String fname = x.getName();

        SQLiteDatabase db;
        File pdfpath;
        {
            pdfpath = new File(path);
            db = openOrCreateDatabase("Documents", SQLiteDatabase.CREATE_IF_NECESSARY, null);
            try {
                db.execSQL("CREATE TABLE IF NOT EXISTS filemaster(fpath VARCHAR,fileName VARCHAR ,audioLenth VARCHAR,nop VARCHAR, type VARCHAR);");
                String sql;
                //w Toast.makeText(ImportActivity.this, "table created ", Toast.LENGTH_LONG).show();
                final Book book = new Book();
                book.setBook_name(fname);
                book.setBook_filepath(path);
                book.setBook_lang(spinner.getSelectedItem().toString());
                book.setBook_length(words.length);
                book.setBook_folder_id(folderList.get(spinnerFolder.getSelectedItemPosition()).getFolder_id());
                book.setBook_user_id(Integer.parseInt(new MyDbHelper(ImportActivity.this).getAllUser().getLogin_user_app_user_id()));
                if (check.equals("pdf")) {
                    sql =
                            "INSERT or replace INTO filemaster VALUES('" + path + "','" + fname + "','" + audio + "','" + now + "','pdf')";
                    book.setBook_type("pdf");
                    book.setBook_page_count(n);
                }else if (check.equals("htm")){
                    sql =
                            "INSERT or replace INTO filemaster VALUES('" + path + "','" + fname + "','" + audio + "','" + now + "','htm')";
                    book.setBook_type("htm");
                    book.setBook_page_count(1);
                }else {
                    sql =
                            "INSERT or replace INTO filemaster VALUES('" + path + "','" + fname + "','" + audio + "','" + now + "','txt')";
                    book.setBook_type("txt");
                    book.setBook_page_count(1);
                }
                Log.d("insert status", "" + true);

                new MyDbHelper(ImportActivity.this).insertBook(book);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        BookReadingCurrentStatus bookReadingCurrentStatus = new BookReadingCurrentStatus();
                        List<Book> listBooks = new MyDbHelper(ImportActivity.this).getAllBooks();
                        bookReadingCurrentStatus.setReading_book_id(listBooks.get(listBooks.size()-1).getBook_id());
                        bookReadingCurrentStatus.setReading_user_id(Integer.parseInt(new MyDbHelper(ImportActivity.this).getAllUser().getLogin_user_app_user_id()));
                        new MyDbHelper(ImportActivity.this).insertBookReadingCurrentStatus(bookReadingCurrentStatus);
                    }
                }, 100);

                db.execSQL(sql);
            } catch (Exception e) {
                Toast.makeText(ImportActivity.this, "ERROR "+e.toString(), Toast.LENGTH_LONG).show();
                Log.d("error db", e.toString());
            }

        }
    }

    void extract(String yourPdfPath, int onc) {
        n = 1;
        String check = yourPdfPath.substring((yourPdfPath.length() - 3));

        rotateLoading = (RotateLoading) findViewById(R.id.rotateloading2);

        if (check.equals("pdf")) {
            Log.d("onc", "" + onc);
            try {
                //rotateLoading.start();
                parsedText = "";
                PdfReader reader = new PdfReader(yourPdfPath);
                n = reader.getNumberOfPages();
                for (int ig = 0; ig < n; ig++) {
                    parsedText = parsedText + PdfTextExtractor.getTextFromPage(reader, ig + 1).trim() + "\n"; //Extracting the content from the different pages
                }
                reader.close();
                Log.d("PARSED", "extract: " + parsedText);
            } catch (Exception e) {
                Log.d("Extract failed", "extract: " + e);
            }


        } else {
            Log.d("onc", "" + onc);
            try {
                // rotateLoading.start();
                parsedText = "";
                File file = new File(yourPdfPath);

                StringBuilder text = new StringBuilder();

                try {
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String line;

                    while ((line = br.readLine()) != null) {
                        text.append(line);
                        text.append('\n');
                    }
                    br.close();
                    parsedText = text.toString();
                } catch (IOException e) {
                    //You'll need to add proper error handling here
                    Log.d("error", "" + e.toString());
                }


            } catch (Exception ex) {
                Log.d("Exception", ex.toString());
            }
        }
    }

    public class countwords extends AsyncTask<Integer, Void, String> {

        @Override

        protected String doInBackground(Integer... params) {
            extract(xx, yy);
            return "OK";

        }

        @SuppressLint("WrongConstant")
        @Override

        protected void onPostExecute(String result) {

           // Toast.makeText(getApplicationContext(), parsedText, Toast.LENGTH_LONG).show();

            relativeLayoutSearchingPage.setVisibility(View.GONE);
            rotateLoading.stop();
            nop = (TextView) findViewById(R.id.pagesvalue);
            nop.setText(n + "");

            words = parsedText.split(" ");
            if (words.length != 0) {
                count = (TextView) findViewById(R.id.wordvalue);
                count.setText(words.length + "");
                now = words.length;
                Log.d("data", "" + count + " " + nop);
                length = (TextView) findViewById(R.id.lengthvalue);
                int ss = words.length / 3;
                length.setText(ss / 60 + "m " + ss % 60 + "s");
                audio = ss / 60 + "m " + ss % 60 + "s";
            }

            /*
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    writeData(xx);
                    folderWriteData(xx);
                }
            }, 2000);
             */
        }

    }


}
