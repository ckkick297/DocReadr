package com.example.keerat666.listviewpdfui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.keerat666.listviewpdfui.adapters.CustomGridViewAdapter;
import com.example.keerat666.listviewpdfui.adapters.item;
import com.example.keerat666.listviewpdfui.db.MyDbHelper;
import com.example.keerat666.listviewpdfui.models.Book;
import com.example.keerat666.listviewpdfui.models.BookJoinTblReading;
import com.example.keerat666.listviewpdfui.models.BookMeta;
import com.example.keerat666.listviewpdfui.models.BookReadingCurrentStatus;
import com.example.keerat666.listviewpdfui.ui.fragment.BookFragment;
import com.example.keerat666.listviewpdfui.ui.fragment.BookmarkFragment;
import com.example.keerat666.listviewpdfui.ui.fragment.DiscoverFragment;
import com.example.keerat666.listviewpdfui.ui.fragment.HomeFragment;
import com.freesoulapps.preview.android.Preview;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;
import com.obsez.android.lib.filechooser.ChooserDialog;
import com.victor.loading.rotate.RotateLoading;

import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.jsoup.Jsoup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    ListView list;
    Dialog yourDialog;
    ImageButton ibut;
    TextView t;
    SearchView sview;
    int sl = 0;
    Toolbar tb;
    TextView emptytext;
    ArrayList<String> maintitle = new ArrayList<>();
    ArrayList<String> subtitle = new ArrayList<>();
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
    private List<String> fileList = new ArrayList<String>();
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

    private LinearLayout linearLayoutMainContainer;
    boolean isPlaying = false;

    Handler handlerTimeTrack;
    Runnable runnableTimeTrack;

    String webUrlPageTitle = "";

    protected void onStop() {
        super.onStop();
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
        if (handlerTimeTrack!=null){
            handlerTimeTrack.removeCallbacks(runnableTimeTrack);
        }
    }

    int counterReadingSecond = 0;
    public void onPause(){
        super.onPause();

        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
        if (handlerTimeTrack!=null){
            handlerTimeTrack.removeCallbacks(runnableTimeTrack);
        }

        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }

        if (counterReadingSecond != 0)
        new MyDbHelper(MainActivity.this).insertBookStats(Constants.bookJoinTblReading.getBook_id(), Constants.bookJoinTblReading.getBook_user_id(), 0, counterReadingSecond);
        //Toast.makeText(getApplicationContext(), "on Pause", Toast.LENGTH_SHORT).show();
    }

    ArrayList<String> lines = new ArrayList<>();
    public void txtextract() {

        File file = new File(Constants.bookJoinTblReading.getBook_filepath());
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
                lines.add(line);
            }
            br.close();
        } catch (IOException e) {
            Log.d("error", "" + e.toString());
        }
        parsedText = text.toString() + ".";
        seekBar.setMax(parsedText.length());
        trigger(0);
    }

    public void trigger(final int i) {
        Log.d("speaking-highlight", t1.isSpeaking() + "");
        final Handler handler = new Handler();
        final int delay = 0; //milliseconds

        handler.postDelayed(new Runnable() {
            public void run() {
                //do something
                try {
                    if (!t1.isSpeaking()) {
                        highlight(i);
                        //Toast.makeText(getApplicationContext(), "Progress bar "+seekBar.getMax()+" curren pos "+seekBar.getProgress(), Toast.LENGTH_SHORT).show();
                        int cur_prog = seekBar.getProgress();
                        cur_prog++;
                        if (seekBar.getMax() == cur_prog){
                            imageButtonPlay.setImageDrawable(getDrawable(R.drawable.ic_icon_player_play));
                            //isPlaying = true;

                            if (t1.isSpeaking()){
                                t1.stop();
                            }
                            if (handler != null) {
                                handler.removeCallbacks(runnable);
                            }
                            if (Constants.bookJoinTblReading.getBook_type().equals("txt")||Constants.bookJoinTblReading.getBook_type().equals("htm")) {
                                parsedText = "";
                                t1.stop();
                            }
                            //Toast.makeText(getApplicationContext(), "Txt read Comp", Toast.LENGTH_SHORT).show();
                        }
                    }
                }catch (Exception e){

                }

                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    int i = 0;
    int end = 0;
    int tempi;
    int plength = 0;
    ArrayList<Integer> itracker = new ArrayList<>();

    public void highlight(int h) {

        if (i <= parsedText.length()) {
            try {
                end = parsedText.indexOf(".", i);
                Log.d("end index", end + "");

            } catch (Exception e) {
                Log.d("Ending exception", e + "");
            }

            if (end != -1) {

                try {
                    String toSpeak = parsedText.substring(i, end);
                    tempi = i;

                    //Toast.makeText(getApplicationContext(), "highlite call", Toast.LENGTH_SHORT).show();
                    seekBar.setProgress(end);
                    List<BookReadingCurrentStatus> readingCurrentStatuses = new MyDbHelper(MainActivity.this).getAllBookReadingCurrentStatus();
                    for (int i=0; i<readingCurrentStatuses.size(); i++){
                        if (Constants.bookJoinTblReading.getBook_id() == readingCurrentStatuses.get(i).getReading_book_id()){
                            BookReadingCurrentStatus bookReadingCurrentStatus = readingCurrentStatuses.get(i);
                            bookReadingCurrentStatus.setReading_word_number(end);
                            new MyDbHelper(MainActivity.this).updateBookReadingCurrentStatus(bookReadingCurrentStatus);
                            break;
                        }
                    }

                    // Toast.makeText(getApplicationContext(), "reading status words:"+wordCounter, Toast.LENGTH_SHORT).show();
                    i = end + 1;

                    t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                    plength = end - tempi + 1;
                    itracker.add(plength);
                    Log.d("plength", itracker + "");
                    Log.d("speaking?", t1.isSpeaking() + "");
                }catch (Exception e){

                }
            } else {
                i = 0;
            }
        }
    }

    int totalPages;
    int currentPage;
    PdfReader reader;
    Handler handler;
    Runnable runnable;

    private void playContent(){
        List<BookReadingCurrentStatus> list = new MyDbHelper(MainActivity.this).getAllBookReadingCurrentStatus();

        for (int i=0; i<list.size(); i++) {
            if (Constants.bookJoinTblReading.getBook_id() == list.get(i).getReading_book_id()) {
                if (Constants.bookJoinTblReading.getBook_type().equals("txt")||Constants.bookJoinTblReading.getBook_type().equals("htm")){
                    txtextract();
                }
                if (Constants.bookJoinTblReading.getBook_type().equals("pdf")) {
                    try {
                        if (seekBar.getProgress() == 0){
                            seekBar.setProgress(1);
                            currentPage = 1;
                        }
                        parsedText = PdfTextExtractor.getTextFromPage(reader, currentPage);
                        seekBar.setProgress(currentPage);
                        t1.speak(parsedText, TextToSpeech.QUEUE_FLUSH, null);

                        runnable = new Runnable() {
                            @Override
                            public void run() {
                                if (!t1.isSpeaking()) {
                                    List<BookReadingCurrentStatus> readingCurrentStatuses = new MyDbHelper(MainActivity.this).getAllBookReadingCurrentStatus();
                                    for (int i=0; i<readingCurrentStatuses.size(); i++){
                                        if (Constants.bookJoinTblReading.getBook_id() == readingCurrentStatuses.get(i).getReading_book_id()){
                                            BookReadingCurrentStatus bookReadingCurrentStatus = readingCurrentStatuses.get(i);
                                            bookReadingCurrentStatus.setReading_page_number(currentPage);
                                            //bookReadingCurrentStatus.setReading_word_number(end);
                                            new MyDbHelper(MainActivity.this).updateBookReadingCurrentStatus(bookReadingCurrentStatus);
                                            break;
                                        }
                                    }
                                    if (isPlaying) {
                                        playContent();
                                        // Toast.makeText(getApplicationContext(),"curr page :"+currentPage, Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    handler.postDelayed(this, 200);
                                }
                            }
                        };
                        handler = new Handler();
                        handler.postDelayed(runnable, 200);

                    } catch (Exception e) {
                        Log.d("extract method", "extract: " + e);
                    }
                }
            }
        }
    }

    private void playPausePlayer(){

        if (!isPlaying) {
            imageButtonPlay.setImageDrawable(getDrawable(R.drawable.ic_icon_player_pause));
            isPlaying = true;

            if (Constants.bookJoinTblReading.getBook_type().equals("txt") || Constants.bookJoinTblReading.getBook_type().equals("htm")){ playContent(); }

            if (Constants.bookJoinTblReading.getBook_type().equals("pdf")) { playContent(); }
            //Toast.makeText(getApplicationContext(), "play", Toast.LENGTH_SHORT).show();
        }else {
            //Toast.makeText(getApplicationContext(), "pause", Toast.LENGTH_SHORT).show();
            imageButtonPlay.setImageDrawable(getDrawable(R.drawable.ic_icon_player_play));
            isPlaying = false;

            if (t1.isSpeaking()){
                t1.stop();
            }
            if (handler != null) {
                handler.removeCallbacks(runnable);
            }
            if (Constants.bookJoinTblReading.getBook_type().equals("txt") || Constants.bookJoinTblReading.getBook_type().equals("htm")) {
                parsedText = "";
                i = tempi;
                t1.stop();
            }
            Constants.isPlayedBookStatus = false;
        }
    }

    SeekBar seekBar;
    ImageButton imageButtonPlay;
    private void playerGloble(){

        currentPage = 1;

        if (new MyDbHelper(MainActivity.this).getAllBookMeta().size()!=0) {
            BookMeta bookMeta = new MyDbHelper(MainActivity.this).getAllBookMeta().get(new MyDbHelper(MainActivity.this).getAllBookMeta().size() - 1);
            init(((float)bookMeta.getMeta_pitch())/50, ((float)bookMeta.getMeta_rate())/50, bookMeta.getMeta_lang_reading());
        }else {
            init(1.0f, 1.0f, "");
        }

        final LinearLayout playerLayout = findViewById(R.id.llMainPlayer);
        TextView textViewTitle = findViewById(R.id.tvMainPlayerTitle);
        TextView textViewFolderName = findViewById(R.id.tvMainPlayerSubTitle);
        imageButtonPlay = findViewById(R.id.ibMainPlayerPlay);
        ImageButton imageButtonPrev = findViewById(R.id.ibMainPlayerPrev);
        ImageButton imageButtonNext = findViewById(R.id.ibMainPlayerNext);
        ImageButton imageButtonStop = findViewById(R.id.ibMainPlayerClose);
        imageButtonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPlaying = true;
                playPausePlayer();
                Constants.bookJoinTblReading.setBook_id(0);
                playerLayout.setVisibility(View.GONE);

                Constants.isPlayedBookStatus = false;
            }
        });

        LinearLayout linearLayout = findViewById(R.id.main_player_title);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constants.bookJoinTblReading.getBook_type().equals("pdf")) {
                    Intent i = new Intent(MainActivity.this, PDFViewer.class);
                    i.putExtra("Pathid", Constants.bookJoinTblReading.getBook_filepath());
                    i.putExtra("current_page", Constants.bookJoinTblReading.getReading_page_number());
                    startActivity(i);
                }
                if (Constants.bookJoinTblReading.getBook_type().equals("txt") || Constants.bookJoinTblReading.getBook_type().equals("htm")){
                    Intent i = new Intent(MainActivity.this, TxtMaker.class);
                    i.putExtra("txtpath", Constants.bookJoinTblReading.getBook_filepath());
                    startActivity(i);
                }
            }
        });

        seekBar = findViewById(R.id.seekBarMainPlayer);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int k, boolean b) {

                if (Constants.bookJoinTblReading.getBook_type().equals("txt")||Constants.bookJoinTblReading.getBook_type().equals("htm")){
                    i = k;
                    t1.stop();
                }

                if (Constants.bookJoinTblReading.getBook_type().equals("pdf")) {
                    if (isPlaying) {

                        if (t1.isSpeaking()) { t1.stop(); }

                        if (handler != null) { handler.removeCallbacks(runnable); }

                        if (i != 0) {
                            seekBar.setProgress(i);
                            currentPage = i;
                            //Toast.makeText(getApplicationContext(), "cur pg" + currentPage + "\ntotal page" + totalPages + "\nprg max" + seekBar.getMax(), Toast.LENGTH_SHORT).show();
                        }

                        playContent();
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        if (Constants.bookJoinTblReading.getBook_id() != 0) {
            playerLayout.setVisibility(View.VISIBLE);

            if (Constants.bookJoinTblReading.getBook_type().equals("txt") || Constants.bookJoinTblReading.getBook_type().equals("htm")) {

                List<BookReadingCurrentStatus> list = new MyDbHelper(MainActivity.this).getAllBookReadingCurrentStatus();

                for (int i = 0; i < list.size(); i++) {
                    if (Constants.bookJoinTblReading.getBook_id() == list.get(i).getReading_book_id()) {
                        seekBar.setProgress(list.get(i).getReading_word_number());
                    }
                }
            }
            if (Constants.bookJoinTblReading.getBook_type().equals("pdf")) {
                try {
                    reader = new PdfReader(Constants.bookJoinTblReading.getBook_filepath());
                    totalPages = reader.getNumberOfPages();
                    seekBar.setMax(totalPages);
                    List<BookReadingCurrentStatus> list = new MyDbHelper(MainActivity.this).getAllBookReadingCurrentStatus();

                    for (int i = 0; i < list.size(); i++) {
                        if (Constants.bookJoinTblReading.getBook_id() == list.get(i).getReading_book_id()) {
                            currentPage = list.get(i).getReading_page_number();
                            parsedText = parsedText + PdfTextExtractor.getTextFromPage(reader, list.get(i).getReading_page_number()); //Extracting the content from the different pages
                            seekBar.setProgress(currentPage + 1);
                        }
                    }
                } catch (Exception e) {
                }
            }

            textViewTitle.setText(Constants.bookJoinTblReading.getBook_name());
            textViewFolderName.setText(new MyDbHelper(MainActivity.this).getFolderName(Constants.bookJoinTblReading.getBook_folder_id()));

            if (isPlaying) {
                imageButtonPlay.setImageDrawable(getDrawable(R.drawable.ic_icon_player_pause));
                //isPlaying = false;
            }else {
                imageButtonPlay.setImageDrawable(getDrawable(R.drawable.ic_icon_player_play));
                // isPlaying = true;
            }

            imageButtonPlay.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    playPausePlayer();
                }
            });



            imageButtonPrev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Constants.bookJoinTblReading.getBook_type().equals("txt")||Constants.bookJoinTblReading.getBook_type().equals("htm")){
                        try {
                            if (itracker.size() >= 2) {
                                t1.stop();
                                i = i - itracker.get(itracker.size() - 1);
                                i = i - itracker.get(itracker.size() - 2);
                                if (i<0) {
                                    Toast.makeText(MainActivity.this, "No previous line found!", Toast.LENGTH_SHORT).show();
                                    i=0;
                                }
                                Log.d("itracker last", "" + itracker.get(itracker.size() - 1));
                            } else {
                                Toast.makeText(MainActivity.this, "No previous line found!", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){}
                    }

                    if (Constants.bookJoinTblReading.getBook_type().equals("pdf")) {
                        if (isPlaying) {

                            if (t1.isSpeaking()) { t1.stop(); }

                            if (handler != null) { handler.removeCallbacks(runnable); }

                            if (currentPage < totalPages && currentPage != 1) { currentPage--; }

                            playContent();
                        }
                    }
                }
            });

            imageButtonNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Constants.bookJoinTblReading.getBook_type().equals("txt")||Constants.bookJoinTblReading.getBook_type().equals("htm")) {
                        try { t1.stop(); }catch (Exception e){}
                    }
                    if (Constants.bookJoinTblReading.getBook_type().equals("pdf")) {
                        if (isPlaying) {

                            if (t1.isSpeaking()) { t1.stop(); }

                            if (handler != null) { handler.removeCallbacks(runnable); }

                            if (currentPage < totalPages) { currentPage++; }

                            playContent();
                        }
                    }
                }
            });
        }else {
            playerLayout.setVisibility(View.GONE);
        }

        if (Constants.bookJoinTblReading.getBook_id() != 0 && Constants.isPlayedBookStatus == true) {
            isPlaying = true;

            if (isPlaying) {
                imageButtonPlay.setImageDrawable(getDrawable(R.drawable.ic_icon_player_pause));
                //isPlaying = false;

                if (Constants.bookJoinTblReading.getBook_type().equals("txt")||Constants.bookJoinTblReading.getBook_type().equals("htm")) { playContent(); }

                if (Constants.bookJoinTblReading.getBook_type().equals("pdf")) { playContent(); }

            } else {
                imageButtonPlay.setImageDrawable(getDrawable(R.drawable.ic_icon_player_play));
                //isPlaying = true;

                if (t1.isSpeaking()) { t1.stop(); }

                if (handler != null) { handler.removeCallbacks(runnable); }

                if (Constants.bookJoinTblReading.getBook_type().equals("txt")||Constants.bookJoinTblReading.getBook_type().equals("htm")) {
                    parsedText = "";
                    i = tempi;
                    t1.stop(); }
            }
        }
    }

    private BookJoinTblReading book = null;
    TextToSpeech t1;
    private String lang_code_default = "en";
    public void init(final float p, final float sr, final String voice) {

        book = Constants.bookJoinTblReading;

        try {
            //  tvPDFViewerTitle.setText("" + book.getBook_name());
            //  tvPDFViewerPages.setText("Page "+(current_page+1)+" of "+book.getBook_page_count());
        }catch (Exception e){ }

        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

            @Override
            public void onInit(int status) {

                if (status != TextToSpeech.ERROR) {
                    t1.setPitch(p);//1.0f);
                    t1.setSpeechRate(sr);//1.0f);
                    Locale[] locales = Locale.getAvailableLocales();
                    List<Locale> localeList = new ArrayList<Locale>();

                    for (Locale locale : locales) {
                        int res = t1.isLanguageAvailable(locale);
                        if (res == TextToSpeech.LANG_COUNTRY_AVAILABLE) {
                            localeList.add(locale);
                            if (locale.getDisplayName().equals(book.getBook_lang())) {
                                t1.setLanguage(locale);
                                lang_code_default = locale.getLanguage();
                                break;
                            }
                        }
                    }
                }
                for (Voice tmpVoice : t1.getVoices()) {
                    if (tmpVoice.getName().equals(voice)) {
                        t1.setVoice(tmpVoice);
                        break;
                    }
                }
                Log.d("voices", t1.getVoices() + "");
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //case R.id.btnUrlDialog:
            //  Toast.makeText(getApplicationContext(), "Web url add clicked", Toast.LENGTH_SHORT).show();
            //break;

            case R.id.search_button:
                getSelected = radiogrp.getCheckedRadioButtonId();
                buttonAddBooksMain.setVisibility(View.VISIBLE);

                if (buttonnew.isChecked()) {
                    ALLfiles();
                } else if (btnnew.isChecked()) {
                    files();
                }
                else if (btndoc.isChecked()) {
                    DOC();
                }
                else if (btnweb.isChecked()) {
                    PDF();
                } else {
                    Toast.makeText(MainActivity.this, "Please Select an Option", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    Button buttonAddBooksMain;
    @SuppressLint({"WrongConstant", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences pref = getSharedPreferences("MyTheme", MODE_PRIVATE);
        String theme = pref.getString("MyAppTheme", null);

        if (getString(R.string.theme_light).equals(theme)) {
            setTheme(R.style.AppTheme);
        }
        if (getString(R.string.theme_warm).equals(theme)){
            setTheme(R.style.AppThemeWarm);
        }
        if (getString(R.string.theme_dark).equals(theme)){
            setTheme(R.style.AppThemeDark);
        }

        setContentView(R.layout.activity_main);

        // If player is playing then insert time to db else do nothing and chk
        handlerTimeTrack = new Handler();
        runnableTimeTrack = new Runnable() {
            @Override
            public void run() {
                if (isPlaying){
                    // Toast.makeText(MainActivity.this, "play", Toast.LENGTH_SHORT).show();
                    //loadFragment(new HomeFragment(MainActivity.this));
                    counterReadingSecond++;
                }else {
                    // Toast.makeText(MainActivity.this, "pause", Toast.LENGTH_SHORT).show();
                }
                handlerTimeTrack.postDelayed(this, 1000);
            }
        };
        handlerTimeTrack.postDelayed(runnableTimeTrack, 1000);

        playerGloble();

        MyDbHelper myDbHelper = new MyDbHelper(MainActivity.this);
        List<Book> booksList = myDbHelper.getAllBooks();
        Constants.booksList = booksList;

        linearLayoutMainContainer = findViewById(R.id.containerMain);

        buttonAddBooksMain = findViewById(R.id.btnAddBookMain);
        buttonAddBooksMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                folderimport();
            }
        });

        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", false);

        if (isFirstRun) {
            //show sign up activity
            startActivity(new Intent(MainActivity.this, Main.class));
            //Toast.makeText(MainActivity.this, "Run only once", Toast.LENGTH_LONG).show();
        }
        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).commit();

        mContext = getApplicationContext();
        mActivity = MainActivity.this;
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
        radiogrp = (RadioGroup) findViewById(R.id.radiogrp);
        // insertext=(TextView) findViewById(R.id.insertext);
        final String pathx = Environment.getExternalStorageDirectory().getAbsolutePath();
        final File f = new File(pathx);//converted string object to file
        final String[] values = f.list();//getting the list of files in string array
        //now presenting the data into screen
        rotateLoading = (RotateLoading) findViewById(R.id.rotateloading);
        list = (ListView) findViewById(R.id.list);
        emptytext = (TextView) findViewById(R.id.empty);
        list.setEmptyView(emptytext);
        buttonAddBooksMain.setVisibility(View.GONE);

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final String contactId = ((TextView) view.findViewById(R.id.title)).getText().toString();
                final String ffpath=path.get(position);
                // Toast.makeText(MainActivity.this, ""+contactId, Toast.LENGTH_SHORT).show();

                Rect displayRectangle = new Rect();
                Window window = MainActivity.this.getWindow();
                window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

                yourDialog = new Dialog(MainActivity.this);
                LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.delete_dialog, (ViewGroup) findViewById(R.id.ldelete));
                layout.setMinimumWidth((int) (displayRectangle.width() * 0.8f));
                layout.setMinimumHeight((int) (displayRectangle.height() * 0.1f));
                yourDialog.setContentView(layout);
                yourDialog.show();
                butdel=layout.findViewById(R.id.buttondel);
                butdel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            deleteentry(ffpath, position);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                ibut=layout.findViewById(R.id.imageButton7);
                ibut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            deleteentry(ffpath, position);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                return true;
            }
        });
        askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE, READ_EXST);

        adapter = new MyListView(this, maintitle, subtitle, imgid, playid, path, subtitle2);
        db = openOrCreateDatabase("Documents", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        try {
            db.execSQL("CREATE TABLE IF NOT EXISTS filemaster(fpath VARCHAR,fileName VARCHAR" +
                    " ," + "audioLenth VARCHAR,nop VARCHAR, type VARCHAR);");

            // Toast.makeText(MainActivity.this, "table created ", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            //Toast.makeText(MainActivity.this, "ERROR "+e.toString(), Toast.LENGTH_LONG).show();
            Log.d("error db", e.toString());
        }

        RelativeLayout buttonContainer = (RelativeLayout) findViewById(R.id.buttoncont);

        buttonnew = (RadioButton) findViewById(R.id.SEARCH_ALL);
        btnnew = (RadioButton) findViewById(R.id.TXT);
        btndoc = (RadioButton) findViewById(R.id.DOC);
        btnweb = (RadioButton) findViewById(R.id.PDF);

        search_button = (Button) findViewById(R.id.search_button);
        search_button.setOnClickListener(MainActivity.this);

        sview = (SearchView) findViewById(R.id.searchView1);
        //  final ImageView speak = findViewById(R.id.speakVoice);
        speedDialView = findViewById(R.id.speedDial);
        // tb = (Toolbar) findViewById(R.id.toolbar2);
        // tb.setTitle(getString(R.string.app_name));
        // setSupportActionBar(tb);


//        sview.setQueryHint("Search ebooks..");

     /*   speak.setOnClickListener(new View.OnClickListener() {
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
        });*/

        BottomNavigationView bottomNavigationView1 = (BottomNavigationView) findViewById(R.id.navigation);


        RelativeLayout relativeLayoutActivityMain = findViewById(R.id.buttoncont);
        LinearLayout linearLayoutFragmentContainer = findViewById(R.id.containerMain);

        if (getString(R.string.theme_light).equals(theme)) {
            bottomNavigationView1.setBackgroundColor(getResources().getColor(R.color.lightColorDark));

            LinearLayout linearLayout = findViewById(R.id.llMainPlayer);
            linearLayout.setBackgroundColor(getResources().getColor(R.color.lightColorDark));

            relativeLayoutActivityMain.setBackgroundColor(getResources().getColor(R.color.lightColorLight));
            linearLayoutFragmentContainer.setBackgroundColor(getResources().getColor(R.color.lightColorLight));
        }
        if (getString(R.string.theme_warm).equals(theme)){
            bottomNavigationView1.setBackgroundColor(getResources().getColor(R.color.warmColorDark));

            LinearLayout linearLayout = findViewById(R.id.llMainPlayer);
            linearLayout.setBackgroundColor(getResources().getColor(R.color.warmColorDark));

            relativeLayoutActivityMain.setBackgroundColor(getResources().getColor(R.color.warmColorLight));
            linearLayoutFragmentContainer.setBackgroundColor(getResources().getColor(R.color.warmColorLight));
        }
        if (getString(R.string.theme_dark).equals(theme)){
            bottomNavigationView1.setBackgroundColor(getResources().getColor(R.color.darkColorDark));

            LinearLayout linearLayout = findViewById(R.id.llMainPlayer);
            linearLayout.setBackgroundColor(getResources().getColor(R.color.darkColorDark));

            LinearLayout linearLayoutTitle = findViewById(R.id.main_player_title);
            linearLayoutTitle.setBackgroundColor(getResources().getColor(R.color.colorAccentDarkTheme));

            TextView textViewTitle = findViewById(R.id.tvMainPlayerTitle);
            textViewTitle.setTextColor(getResources().getColor(R.color.colorPrimary));

            TextView textViewSubTitle = findViewById(R.id.tvMainPlayerSubTitle);
            textViewSubTitle.setTextColor(getResources().getColor(R.color.colorPrimary));

            ImageButton prevLine = findViewById(R.id.ibMainPlayerPrev);
            prevLine.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary));

            ImageButton play = findViewById(R.id.ibMainPlayerPlay);
            play.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary));

            ImageButton stop = findViewById(R.id.ibMainPlayerClose);
            stop.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary));

            ImageButton NextLine = findViewById(R.id.ibMainPlayerNext);
            NextLine.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary));


            speedDialView.setMainFabOpenedBackgroundColor(getResources().getColor(R.color.colorAccentDarkTheme));
            speedDialView.setMainFabClosedBackgroundColor(getResources().getColor(R.color.colorAccentDarkTheme));

            relativeLayoutActivityMain.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDarkTheme));
            linearLayoutFragmentContainer.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDarkTheme));
        }

        bottomNavigationView1.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.home1: {

                                getSupportActionBar().show();
                                speedDialView.setVisibility(View.VISIBLE);
                                linearLayoutMainContainer.setVisibility(View.VISIBLE);

                                loadFragment(new HomeFragment(MainActivity.this));

                                // imageButton is Arrow button
//                                imageButton.setVisibility(View.GONE);
                                //sview.setVisibility(View.VISIBLE);
                                buttonAddBooksMain.setVisibility(View.GONE);
                                break;
                            }
                            case R.id.books:

                                getSupportActionBar().show();
                                getSupportActionBar().setTitle("Books");
                                getSupportActionBar().setSubtitle("");
                                speedDialView.setVisibility(View.VISIBLE);
                                //  tb.setVisibility(View.GONE);
                                //tb.setTitle(getString(R.string.app_name));
                                linearLayoutMainContainer.setVisibility(View.VISIBLE);

                                loadFragment(new BookFragment(MainActivity.this));

                                // imageButton is Arrow button
                                //   imageButton.setVisibility(View.GONE);
                                //  sview.setVisibility(View.VISIBLE);
                                // getSupportActionBar().hide();
                                buttonAddBooksMain.setVisibility(View.GONE);
                                break;

                            case R.id.discover: {

                                getSupportActionBar().show();
                                getSupportActionBar().setTitle("Discover");
                                getSupportActionBar().setSubtitle("");
                                speedDialView.setVisibility(View.VISIBLE);

                                // imageButton.setVisibility(View.GONE);
                                // sview.setVisibility(View.GONE);
                                linearLayoutMainContainer.setVisibility(View.VISIBLE);
                                loadFragment(new DiscoverFragment(MainActivity.this));
                                //tb.setTitle("Discover");
                                //getSupportActionBar().show();
                                buttonAddBooksMain.setVisibility(View.GONE);
                                break;
                            }

                            case R.id.bookmark1: {

                                getSupportActionBar().show();
                                getSupportActionBar().setTitle("Bookmarks");
                                getSupportActionBar().setSubtitle("");
                                speedDialView.setVisibility(View.VISIBLE);

                                //tb.setVisibility(View.GONE);
                                //tb.setTitle(getString(R.string.app_name));
                                linearLayoutMainContainer.setVisibility(View.VISIBLE);

                                loadFragment(new BookmarkFragment(MainActivity.this));

                                // imageButton is Arrow button
                                // imageButton.setVisibility(View.GONE);
                                // sview.setVisibility(View.VISIBLE);
                                //getSupportActionBar().hide();
                                buttonAddBooksMain.setVisibility(View.GONE);
                                break;
                            }
                        }
                        return true;
                    }
                });

        gridView = (GridView) findViewById(R.id.gridView1);
       /* imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                folderimport();
            }
        });
*/
        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.fab_link, R.drawable.pdfnew)
                        .setFabBackgroundColor(getResources().getColor(R.color.colorPrimary))
                        .setLabel("PDF, TXT")
                        .setLabelColor(getResources().getColor(R.color.black))
                        .setLabelBackgroundColor(getResources().getColor(R.color.colorPrimary))
                        .create()
        );

        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.fab_link2, R.drawable.wwwnew)
                        .setFabBackgroundColor(getResources().getColor(R.color.colorPrimary))
                        .setLabel("WebUrl")
                        .setLabelColor(getResources().getColor(R.color.black))
                        .setLabelBackgroundColor(getResources().getColor(R.color.colorPrimary))
                        .create()
        );
        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.fab_link3, R.drawable.wordnew)
                        .setFabBackgroundColor(getResources().getColor(R.color.colorPrimary))
                        .setLabel("Browse From Folder")
                        .setLabelColor(getResources().getColor(R.color.black))
                        .setLabelBackgroundColor(getResources().getColor(R.color.colorPrimary))
                        .create()
        );

        speedDialView.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {
            @Override
            public boolean onActionSelected(SpeedDialActionItem speedDialActionItem) {
                linearLayoutMainContainer.setVisibility(View.GONE);
                getSupportActionBar().hide();

                switch (speedDialActionItem.getId()) {
                    case R.id.fab_link:
                        //Toast.makeText(getApplicationContext(), "pdf", Toast.LENGTH_LONG).show();
                        folder();
                        return false; //
                    case R.id.fab_link2:
                        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main_dialog_web_url_layout,null);
                        final Preview mPreview =(Preview)view.findViewById(R.id.preview);
                        mPreview.setVisibility(View.GONE);
                        final EditText editText = (EditText)view.findViewById(R.id.etUrlDialog);
                        editText.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                mPreview.setVisibility(View.GONE);
                                mPreview.setData(editText.getText().toString()); }

                            @Override
                            public void afterTextChanged(Editable editable) { }
                        });

                        mPreview.setListener(new Preview.PreviewListener() {
                            @Override
                            public void onDataReady(Preview preview) {

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mPreview.setVisibility(View.VISIBLE);
                                    }
                                });
                                mPreview.setMessage(preview.getLink());
                                webUrlPageTitle = preview.getTitle();
                            }
                        });


                        view.findViewById(R.id.btnUrlDialog).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {
                                    if (!editText.getText().toString().equals("") && editText.getText() != null) {
                                        WebView webView = new WebView(MainActivity.this);
                                        webView.getSettings().setJavaScriptEnabled(true);
                                        webView.setWebViewClient(new WebViewClient());
                                        //new AlertDialog.Builder(MainActivity.this)
                                        //        .setView(webView)
                                        //          .create()
                                        //           .show();
                                        new DownloadWebPageTask(webView).execute(new String[]{editText.getText().toString()});
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Please input Web URL link!", Toast.LENGTH_SHORT).show();
                                    }
                                }catch (Exception e){
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                        new AlertDialog.Builder(MainActivity.this).setView(view).create().show();
                        // ListDir(f);
                        return false;
                    case R.id.fab_link3:
                        itext();
                        return false;
                    default:
                        return false;
                }
            }
        });

        if (Constants.isBookAdded) {
            getSupportActionBar().show();
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setTitle("Books");

            speedDialView.setVisibility(View.VISIBLE);

            // tb.setTitle(getString(R.string.app_name));
            linearLayoutMainContainer.setVisibility(View.VISIBLE);

            loadFragment(new BookFragment(MainActivity.this));

            // imageButton.setVisibility(View.GONE);
            //sview.setVisibility(View.VISIBLE);
            //getSupportActionBar().hide();
            Constants.isBookAdded = false;

        }else {
            //  tb.setTitle(getString(R.string.app_name));
            getSupportActionBar().show();
            getSupportActionBar().setElevation(0);
            linearLayoutMainContainer.setVisibility(View.VISIBLE);
            loadFragment(new HomeFragment(MainActivity.this));
//            imageButton.setVisibility(View.GONE);
//            sview.setVisibility(View.VISIBLE);
            //Constants.isBookAdded = true;
        }
    }

    private class DownloadWebPageTask extends AsyncTask<String, Void, String> {
        WebView webView;
        String url = "";
        public DownloadWebPageTask(WebView view){
            this.webView = view;
        }
        @Override
        protected String doInBackground(String... urls) {
            url = urls[0];
            String response="";
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                int code = urlConnection.getResponseCode();
                if (code !=  200) {
                    throw new IOException("Invalid response from server: " + code);
                }

                BufferedReader rd = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream()));
                String line;
                while ((line = rd.readLine()) != null) {
                    Log.i("data", line);
                    response+= line;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            //textView.setText(Html.fromHtml(result));
            //result = result.replaceAll("<[^>]*>", "");
            //result = Html.fromHtml(result).toString();
            try {
                result = Jsoup.parse(result).body().text();
            }catch (Exception e){
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }

            if (result.length() != 0) {
                //webView.loadData(result, "text/html", "UTF-8");;
                try{
                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), ""+webUrlPageTitle+".htm");//FilenameUtils.getBaseName(url)+".htm");
                    FileOutputStream fout=new FileOutputStream(file);
                    byte b[] = result.getBytes();
                    fout.write(b);
                    fout.close();
                    //System.out.println("success...");
                    writetodb(file.getPath());
                    //Toast.makeText(getApplicationContext(), ""+file.getPath(), Toast.LENGTH_SHORT).show();
                    Constants.file_type = "htm";
                    folderimport();
                }catch(Exception e){Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();}
            }
            else {
                Toast.makeText(getApplicationContext(), "nothing found!", Toast.LENGTH_SHORT).show();
            }
        }

        public void writetodb(String pdfpath)
        {

            db = openOrCreateDatabase("Documents",0,null);

            db.execSQL("DROP TABLE IF EXISTS tempPath");
            try {
                db.execSQL("CREATE TABLE IF NOT EXISTS tempPath(fpath VARCHAR);");
                try {

                    String sql =
                            "INSERT or replace INTO tempPath VALUES('"+pdfpath+"')" ;
                    Log.d("path",""+pdfpath);
                    db.execSQL(sql);
                    Log.d("Written to db","Written to db");

                }
                catch (Exception e) {
                    Log.d("error db",e.toString());
                }

            }
            catch (Exception e) {
                Log.d("error db",e.toString());
            }
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.containerMain, fragment);
        fragmentTransaction.commit(); // save the changes
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

    public void ListDir(File f) {

        buttonnew.setVisibility(buttonnew.GONE);
        search_button.setVisibility(btnnew.GONE);
        gridView.setVisibility(gridView.VISIBLE);
        btnnew.setVisibility(btnnew.GONE);
        btndoc.setVisibility(btndoc.GONE);
        btnweb.setVisibility(btnweb.GONE);
        // sview.setVisibility(View.GONE);

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
        btndoc.setVisibility(btndoc.GONE);
        btnweb.setVisibility(btnweb.GONE);
        search_button.setVisibility(search_button.GONE);

        // generatef("/storage/emulated/0/Download/1536252254408_SRM -Shortlisting-Technical Interview.pdf");
        rotateLoading.start();
        dropdb();

        new MyAsyncTask().execute();
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

        Log.d("Searching pdf", "TRUE");
        sl = 0;
        // btnnew.setVisibility(btnnew.GONE);
        btnnew.setVisibility(btnnew.GONE);
        buttonnew.setVisibility(buttonnew.GONE);
        btndoc.setVisibility(btndoc.GONE);
        btnweb.setVisibility(btnweb.GONE);
        search_button.setVisibility(search_button.GONE);

        // generatef("/storage/emulated/0/Download/1536252254408_SRM -Shortlisting-Technical Interview.pdf");
        rotateLoading.start();
        dropdb();

        new MyAsyncTask().execute();
    }

    public void DOC() {
        pdf.clear();
        maintitle_f.clear();
        subtitle_f.clear();
        imgid_f.clear();
        path_f.clear();
        path_folder.clear();
        filelist.clear();

        pdfPattern = ".docx";
        adapt = new traverseList(this, maintitle_f, subtitle_f, imgid_f, path_f, path_folder, idd);

        Log.d("Searching docx", "TRUE");
        sl = 0;
        // btnnew.setVisibility(btnnew.GONE);
        btnnew.setVisibility(btnnew.GONE);
        buttonnew.setVisibility(buttonnew.GONE);
        btndoc.setVisibility(btndoc.GONE);
        btnweb.setVisibility(btnweb.GONE);
        search_button.setVisibility(search_button.GONE);

        // generatef("/storage/emulated/0/Download/1536252254408_SRM -Shortlisting-Technical Interview.pdf");
        rotateLoading.start();
        dropdb();

        new MyAsyncTask().execute();
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
        String s2 = ".docx";
        String s3 = ".txt";
        pdfPattern.add(s1);
        pdfPattern.add(s2);
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
        btndoc.setVisibility(btndoc.GONE);
        btnweb.setVisibility(btnweb.GONE);
        search_button.setVisibility(search_button.GONE);

        // generatef("/storage/emulated/0/Download/1536252254408_SRM -Shortlisting-Technical Interview.pdf");
        rotateLoading.start();
        dropdb();

        new MyAsyncTask().execute();
    }


    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission, Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);

            } else {

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission, Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
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
            if (MainActivity.class == MainActivity.class){
                // tb.setVisibility(tb.VISIBLE);
            }else if (MainActivity.class != MainActivity.class){
                // tb.setVisibility(tb.GONE);
            }

            speedDialView.setVisibility(speedDialView.GONE);
//            imageButton.setVisibility(imageButton.VISIBLE);
            buttonnew.setVisibility(buttonnew.GONE);
            btndoc.setVisibility(buttonnew.GONE);
            btnnew.setVisibility(buttonnew.GONE);
            btnweb.setVisibility(buttonnew.GONE);

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

                else if (fnamese.endsWith("txt"))
                    generatetxt_f(filelist.get(i));

                else if (fnamese.endsWith("docx"))
                    generateDOCX(filelist.get(i));
//
//                else if (fnamese.endsWith("pdf,txt"))
//                    generateALL(filelist.get(i));
                else if (fnamese.equals(fileList))
                    Toast.makeText(getApplicationContext(),"alredy exist",Toast.LENGTH_SHORT).show();

            }
            //   tb.setTitle(sl + " Files Found");
            //   Toast.makeText(MainActivity.this, "Total "+sl+" files found !", Toast.LENGTH_SHORT).show();

            rotateLoading.stop();
            list.setAdapter(adapt);
            db = openOrCreateDatabase("Documents", SQLiteDatabase.CREATE_IF_NECESSARY, null);
            try {
                db.execSQL("CREATE TABLE IF NOT EXISTS filemaster(fpath VARCHAR,fileName VARCHAR ,audioLenth VARCHAR,nop VARCHAR, type VARCHAR);");

            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "ERROR " + e.toString(), Toast.LENGTH_LONG).show();
                Log.d("error db", e.toString());
            }
            set = 1;
        }
    }

    public void folderimport() {

        try {
            Cursor resultSet = db.rawQuery("Select * from tempPath", null);

            Intent importing = new Intent(this, ImportActivity.class);
            startActivity(importing);
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Nothing Selected!", Toast.LENGTH_SHORT).show();
        }
    }

    public void nextFile(View view) {

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
        //name = (TextView) findViewById(R.id.name);
        File temp = new File(d);
        //  name.setText(temp.getName());
        //Toast.makeText(this, " No of files left: "+(nof-x-1), Toast.LENGTH_SHORT).show();
        if (x == 0) {
            xx = d;
            yy = 1;
            rotateLoading = (RotateLoading) findViewById(R.id.rotateloading2);
            rotateLoading.start();
        } else {
            xx = d;
            yy = 0;
            rotateLoading = (RotateLoading) findViewById(R.id.rotateloading2);
            rotateLoading.start();
            ;
            //new ImportActivity.countwords().execute();

        }


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

    //modify for browse
    public void itext() {

        // sview.setVisibility(View.GONE);
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

    public void goimport() {
        Intent intent = new Intent(this, ImportActivity.class);
        startActivity(intent);
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
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy, hh:mm a");
        String strDate = dateFormat.format(dt);
        subtitle_f.add(""+strDate + "");
        imgid_f.add(R.drawable.pdfnew);
        path_f.add(pdfFile.getAbsolutePath());
        // path_folder.add(pf);
        adapt.notifyDataSetChanged();
    }

    public void generatetxt_f(File dd) {
        Log.d("gen f", "txt");
        File f = dd;
        int size = 0;
        try (Cursor returnCursor = getContentResolver().query(Uri.fromFile(f),
                null, null, null, null)) {
            int sizeIndex = 0;
        }
        if (f.getName().length() > 10) {
            String d = f.getName();
            maintitle_f.add(d);
        } else
            maintitle_f.add(f.getName());
        Date dt = new Date(f.lastModified());
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy, hh:mm a");
        String strDate = dateFormat.format(dt);
        subtitle_f.add(strDate + "");
        imgid_f.add(R.drawable.pdf);
        path_f.add(f.getAbsolutePath());

        adapt.notifyDataSetChanged();

    }


    public void generateDOCX(File docx) {
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

        imgid_f.add(R.drawable.doc_icon);

        path_f.add(file.getAbsolutePath());

        adapt.notifyDataSetChanged();

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

    void folder() {

        //  btnnew.setVisibilitytb(btnnew.VISIBLE);
        // tb.setVisibility(View.VISIBLE);
        emptytext.setVisibility(View.GONE);
        gridView.setVisibility(View.GONE);
        // imageButton.setVisibility(imageButton.VISIBLE);
        // tb.setTitle("Select ebooks");
        // tb.setTitleTextColor(Color.BLACK);
//        sview.setVisibility(sview.GONE);

        ListView list1 = (ListView) findViewById(R.id.list);
        list1.setAdapter(null);
        buttonnew.setVisibility(buttonnew.VISIBLE);

        btnnew.setVisibility(btnnew.VISIBLE);
        btndoc.setVisibility(btndoc.VISIBLE);
        btnweb.setVisibility(btnweb.VISIBLE);
        search_button.setVisibility(search_button.VISIBLE);
        speedDialView.setVisibility(speedDialView.VISIBLE);
        // imageButton.setVisibility(imageButton.VISIBLE);
        try {
            db.execSQL("CREATE TABLE IF NOT EXISTS folder(id INT,name VARCHAR,parent_id INT);");
            Log.e("table", String.valueOf(db));
//            Toast.makeText(MainActivity.this, "table created ", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "ERROR " + e.toString(), Toast.LENGTH_LONG).show();
            Log.d("error db", e.toString());
        }



    }

    public void deleteentry(String s, int book_id) throws IOException {
        db.execSQL("delete from "+"filemaster"+" where fpath='"+s+"'");

        List<Book> booksList = new MyDbHelper(MainActivity.this).getAllBooks();
        new MyDbHelper(MainActivity.this).deleteBook(booksList.get(book_id).getBook_id());

        try {
            maintitle.clear();
            subtitle.clear();
            subtitle2.clear();
            imgid.clear();
            playid.clear();
            path.clear();
            yourDialog.dismiss();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_main, menu);//.ham, menu);
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


    //            case R.id.bmark: {
    //               // bookmark();
    //                break;
    //            }
    //
    //
    //            case R.id.veset: {
    //               // change();
    //                break;
    //            }

                case R.id.tedit: {
                   // highlight(1, 1);

                }

                case R.id.menu_main_next:
                    folderimport();
                    break;

                case R.id.menu_main_sign_out:
                    Toast.makeText(getApplicationContext(), "Not defined!", Toast.LENGTH_SHORT).show();
                    break;

            }
            return true;
        }

     */
    @Override
    public void onBackPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("Please confirm");
        builder.setMessage("Are you want to exit the app?");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finishAffinity();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(mContext,"thank you",Toast.LENGTH_LONG).show();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.dismiss();
        finishAffinity();
    }

}
