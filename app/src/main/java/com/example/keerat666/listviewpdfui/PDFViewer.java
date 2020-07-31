package com.example.keerat666.listviewpdfui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.keerat666.listviewpdfui.db.MyDbHelper;
import com.example.keerat666.listviewpdfui.models.BookJoinTblReading;
import com.example.keerat666.listviewpdfui.models.BookMeta;
import com.example.keerat666.listviewpdfui.models.BookNotesJoinBook;
import com.example.keerat666.listviewpdfui.models.BookReadingCurrentStatus;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnTapListener;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.leinardi.android.speeddial.SpeedDialView;
import com.shockwave.pdfium.PdfDocument;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PDFViewer extends AppCompatActivity implements OnLoadCompleteListener, OnPageChangeListener {

    private static final String TAG = "";
    PDFView pdf;
    Activity activity;
    String x;
    ListView list;
    Button chnge;
    RadioGroup rg;
    RadioButton rb1;
    Dialog yourDialog;
    RadioButton rb2;
    String idgen;
    androidx.appcompat.widget.Toolbar tb;
    ImageButton imgb;
    InputStream input = null;
    ImageButton imgbut;
    ArrayList<String> path = new ArrayList<>();
    SpeedDialView speedDialView, speedDialView1;
    int end;
    int i = 0;
    TextView texe1;
    String PATH;
    LinearLayout ll, ll1;
    RelativeLayout rl;
    String parsedText = "";
    MyListView adapter;
    TextToSpeech t1;
    Button b;
    int current_page = 0;
    String status = "play";
    int n;
    PdfReader reader = null;
    int previ;
    boolean nmode = false;
    Menu menu;
    SQLiteDatabase db;
    Spinner spin;
    TextToSpeechManager textToSpeechManager;
    String File_name;
    NotificationCompat.Builder mBuilder;
    String channelId = "hello";
    ImageView txt;
    Integer pageNumber = 0;
    private OnTapListener onTapListener;
    private ProgressBar progressBar;
    TextView seekbarstatus;

    TextView tvPDFViewerLanguage;
    TextView tvPDFViewerTitle;
    TextView tvPDFViewerPages;

    public void onPause(){
        super.onPause();
        pausevoice();
    }

    SeekBar simpleSeekBar;
    @SuppressLint("WrongConstant")
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

        setContentView(R.layout.activity_pdfviewer);
        getSupportActionBar().setElevation(2);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_keyboard_arrow_left_24);

        isBookmarked();

        book = Constants.bookJoinTblReading;

        db = openOrCreateDatabase("Documents", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        Log.e("DATABASE", String.valueOf(db));

        mBuilder = new NotificationCompat.Builder(this, channelId);

        try {
            db.execSQL("CREATE TABLE IF NOT EXISTS bookmarks(fpath VARCHAR,page_number VARCHAR ,total_pages VARCHAR, type VARCHAR);");
            Log.e("TABLEWE", String.valueOf(db));
          //  Toast.makeText(PDFViewer.this, "table created", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Toast.makeText(PDFViewer.this, "ERROR " + e.toString(), Toast.LENGTH_LONG).show();
            Log.d("error db", e.toString());
        }
        // imgb=(ImageButton)findViewById(R.id.imageButton12);
        imgbut = (ImageButton) findViewById(R.id.imageButton2);
        PATH = getIntent().getStringExtra("Pathid");

        int cp = getIntent().getIntExtra("current_page", 0);
        final int cpi = cp;

        try {
            reader = new PdfReader(PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
        n = reader.getNumberOfPages();
        simpleSeekBar = (SeekBar) findViewById(R.id.seekBar2); // initiate the Seekbar
        simpleSeekBar.setMax(n - 1);
        simpleSeekBar.setProgress(cp);

        final File file = new File(PATH);
        pdf = (PDFView) findViewById(R.id.pdfv);

       // tb = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar3);
        //tb.setTitle(file.getName());
        File_name = file.getName();
        //setSupportActionBar(tb);

        final com.github.barteksc.pdfviewer.listener.OnPageChangeListener onPageChangeListener =
                new com.github.barteksc.pdfviewer.listener.OnPageChangeListener() {
                    @Override
                    public void onPageChanged(int page, int pageCount) {
                        simpleSeekBar.setProgress(page);
                        current_page = page;
                        updateCurrentBookStatusDB();
                    }
                };
        pdf.fromFile(file)
                .enableAnnotationRendering(true)
                .onPageChange(onPageChangeListener)
                .pageFling(true)
                .nightMode(nmode)
                .defaultPage(cp)
                .spacing(8)
                .autoSpacing(false)
                //.swipeHorizontal(true)
                .swipeHorizontal(false)
                //.onPageChange(this)
                .onTap(onTapListener)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .load();



        simpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (fromUser == true) {
                    progressChangedValue = progress;
                    pdf.fromFile(file)
                            .defaultPage(progressChangedValue)
                            .onPageChange(onPageChangeListener)
                            .nightMode(nmode)
                            //.swipeHorizontal(true)
                            .swipeHorizontal(false)
                            .enableSwipe(true)
                            .enableAnnotationRendering(true)
                            .load();
                    current_page = progress;
                    clear();
                    //if (status.equals("play")) {
                      //  play(null);
                   // }
                   // extract(PATH, progress);
                    updateCurrentBookStatusDB();
                }
            }

            public void onPageChanged(int page, int pageCount) {
              //  pageNumber = page;
                setTitle(String.format("%s %s / %s", file, page + 1, pageCount));
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("progress", "" + progressChangedValue);
            }
        });
        ll1 = (LinearLayout) findViewById(R.id.ppcont);

        rl = (RelativeLayout) findViewById(R.id.rl);
        TranslateAnimation animate = new TranslateAnimation(0, 0, 0, ll1.getHeight());
        animate.setDuration(500);
        animate.setFillAfter(true);


        pdf.setOnClickListener(new View.OnClickListener() {

            private Object text;

            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager)
                        getSystemService(Context.CLIPBOARD_SERVICE);
                if (ll1.getVisibility() == (ll1.VISIBLE)) {

                    ll1.setVisibility(ll1.GONE);
                } else
                    ll1.setVisibility(ll1.VISIBLE);

            }
        });


        tvPDFViewerLanguage = findViewById(R.id.tvPDFViewerLanguage);
        tvPDFViewerTitle = findViewById(R.id.tvPDFViewerTitle);
        tvPDFViewerPages = findViewById(R.id.tvPDFViewerPages);

        getSupportActionBar().setTitle(R.string.app_name);

        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    //  t1.setLanguage(Locale.UK);
                    t1.setPitch(1.0f);
                    t1.setSpeechRate(1.0f);

                    Locale[] locales = Locale.getAvailableLocales();
                    List<Locale> localeList = new ArrayList<Locale>();

                    for (Locale locale : locales) {
                        int res = t1.isLanguageAvailable(locale);
                        if (res == TextToSpeech.LANG_COUNTRY_AVAILABLE) {
                            localeList.add(locale);
                            if(locale.getDisplayName().equals(book.getBook_lang())){
                                t1.setLanguage(locale);

                                //  Toast.makeText(getApplicationContext(), "book_lang :"+book.getBook_lang()
                                //                +" \ncountry lang code :"+locale.getCountry()
                                //              +" \ndefault code :"+locale.getLanguage()
                                //    ,Toast.LENGTH_LONG).show();

                                lang_code_default = locale.getLanguage();
                                break;
                            }
                        }
                    }

                    if (new MyDbHelper(PDFViewer.this).getAllBookMeta().size()!=0) {
                        BookMeta bookMeta = new MyDbHelper(PDFViewer.this).getAllBookMeta().get(new MyDbHelper(PDFViewer.this).getAllBookMeta().size() - 1);
                        float pitch = ((float)(bookMeta.getMeta_pitch())/50);
                        float speed = ((float)(bookMeta.getMeta_rate())/50);
                        //Toast.makeText(getApplicationContext(), "call with Pitch"+pitch+" rate"+speed, Toast.LENGTH_LONG).show();

                        init(pitch, speed, bookMeta.getMeta_lang_reading());
                    }else {
                        init(1.0f, 1.0f, "");
                    }
                    Log.d("voices", t1.getVoices() + "");
                    // Toast.makeText(TxtMaker.this, ""+t1.getVoices(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        RelativeLayout relativeLayoutMain = findViewById(R.id.rl);

        if (getString(R.string.theme_light).equals(theme)) {
            LinearLayout linearLayout = findViewById(R.id.ppcont);
            linearLayout.setBackgroundColor(getResources().getColor(R.color.lightColorDark));

            relativeLayoutMain.setBackgroundColor(getResources().getColor(R.color.lightColorLight));
        }
        if (getString(R.string.theme_warm).equals(theme)){
            LinearLayout linearLayout = findViewById(R.id.ppcont);
            linearLayout.setBackgroundColor(getResources().getColor(R.color.warmColorDark));

            relativeLayoutMain.setBackgroundColor(getResources().getColor(R.color.warmColorLight));
        }
        if (getString(R.string.theme_dark).equals(theme)){
            LinearLayout linearLayout = findViewById(R.id.ppcont);
            linearLayout.setBackgroundColor(getResources().getColor(R.color.darkColorDark));


            ImageButton prevPage = findViewById(R.id.imageButton3);
            prevPage.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary));

            ImageButton prevLine = findViewById(R.id.imageButton4);
            prevLine.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary));

            ImageButton play = findViewById(R.id.imageButton2);
            play.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary));

            ImageButton NextLine = findViewById(R.id.imageButton5);
            NextLine.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary));

            ImageButton NextPage = findViewById(R.id.imageButton6);
            NextPage.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary));

            ImageButton BookMarkPage = findViewById(R.id.imageButton8);
            BookMarkPage.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary));

            relativeLayoutMain.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDarkTheme));
        }

        Constants.isPlayedBookStatus = false;
    }


    public PDFViewer onTap(OnTapListener onTapListener) {
        this.onTapListener = onTapListener;
        return this;
    }

    private BookJoinTblReading book = null;
    private String lang_code_default = "en";
    ArrayList<String> categoriesLangList = new ArrayList<>();
    ArrayList<String> categoriesLangCodeList = new ArrayList<>();

    public void init(final float p, final float sr, final String voice) {
        book = Constants.bookJoinTblReading;
        try {
            getSupportActionBar().setTitle(book.getBook_name());
            getSupportActionBar().setSubtitle("Page "+(book.getReading_page_number()+1)+" of "+book.getBook_page_count());
            tvPDFViewerTitle.setText("" + book.getBook_name());
            tvPDFViewerPages.setText("Page "+(book.getReading_page_number()+1)+" of "+book.getBook_page_count());

            //current_page = book.getReading_page_number();
        }catch (Exception e){ }

        //t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {

           // @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

         //   @Override
         //   public void onInit(int status) {
                categoriesLangList.clear();
                categoriesLangCodeList.clear();

               // if (status != TextToSpeech.ERROR) {
                    //  t1.setLanguage(Locale.UK);
                    t1.setPitch(p);//1.0f);
                    t1.setSpeechRate(sr);//1.0f);

                    //  Toast.makeText(getApplicationContext(), "Pitch"+p+" rate"+sr, Toast.LENGTH_LONG).show();

                    Locale[] locales = Locale.getAvailableLocales();
                    List<Locale> localeList = new ArrayList<Locale>();

                    for (Locale locale : locales) {
                        int res = t1.isLanguageAvailable(locale);
                        if (res == TextToSpeech.LANG_COUNTRY_AVAILABLE) {
                            localeList.add(locale);
                            if (locale.getDisplayName().equals(book.getBook_lang())) {
                                t1.setLanguage(locale);

                                int result = t1.setLanguage(locale);

                                if (result == TextToSpeech.LANG_MISSING_DATA
                                        || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                                    Toast.makeText(getApplicationContext(), "This Language is not supported!", Toast.LENGTH_LONG).show();
                                    Log.e("TTS", "This Language is not supported");
                                    break;
                                }

                                // Toast.makeText(getApplicationContext(), "book_lang :" + book.getBook_lang()
                                //               + " \ncountry lang code :" + locale.getCountry()
                                //             + " \ndefault code :" + locale.getLanguage()
                                //   , Toast.LENGTH_LONG).show();

                                lang_code_default = locale.getLanguage();
                                break;
                            }
                        }
                    }

                    for (Locale locale : locales) {
                        int res = t1.isLanguageAvailable(locale);

                        boolean hasVariant = (null != locale.getVariant() && locale.getVariant().length() > 0);
                        boolean hasCountry = (null != locale.getCountry() && locale.getCountry().length() > 0);

                        boolean isLocaleSupported =
                                false == hasVariant && false == hasCountry && res == TextToSpeech.LANG_AVAILABLE ||
                                        false == hasVariant && true == hasCountry && res == TextToSpeech.LANG_COUNTRY_AVAILABLE ||
                                        res == TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE;

                        if (isLocaleSupported) {
                            localeList.add(locale);
                            categoriesLangList.add(locale.getDisplayName());
                            categoriesLangCodeList.add(locale.getLanguage());
                        }
                    }

//                    t1.setPitch(p);
                    //              t1.setSpeechRate(sr);
                    for (Voice tmpVoice : t1.getVoices()) {
                        if (tmpVoice.getName().equals(voice)) {
                            t1.setVoice(tmpVoice);

                            break;
                        }
                    }
                    //Toast.makeText(getApplicationContext(),"Init"+lang_code_default, Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            menu.findItem(R.id.lang_pdf_reader).setTitle("" + categoriesLangCodeList.get(categoriesLangList.indexOf(book.getBook_lang())));
                        }
                    }, 200);
                    tvPDFViewerLanguage.setText("" + categoriesLangCodeList.get(categoriesLangList.indexOf(book.getBook_lang())));//+ locale.getLanguage().toUpperCase());
                    Log.d("voices", t1.getVoices() + "");
               // }
          //  }
      //  });
    }

    public void changePitch(View view) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ham, menu);
        this.menu = menu;
      /**  MenuItem searchViewItem = menu.findItem(R.id.menu_pdf_Viewer_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchView.setQueryHint("Search..");
       // searchView.setIconified(false);
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.vesett_pdf_reader:
                change();
                break;

            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.setting_ham_menu_item:

                final SharedPreferences pref = getSharedPreferences("MyTheme", MODE_PRIVATE);
                final SharedPreferences.Editor editor = pref.edit();

                String items[] = {getString(R.string.theme_light), getString(R.string.theme_warm), getString(R.string.theme_dark)};
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.menu_title_theme))
                        .setSingleChoiceItems(items, 0, null)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                                int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();

                                if (selectedPosition == 0){
                                    editor.putString("MyAppTheme", getString(R.string.theme_light));
                                }
                                if (selectedPosition == 1){
                                    editor.putString("MyAppTheme", getString(R.string.theme_warm));
                                }
                                if (selectedPosition == 2){
                                    editor.putString("MyAppTheme", getString(R.string.theme_dark));
                                }
                                editor.commit();
                                Intent intent = new Intent(PDFViewer.this, PDFViewer.class);
                                intent.putExtra("Pathid", getIntent().getStringExtra("Pathid"));
                                intent.putExtra("current_page", getIntent().getIntExtra("current_page", 0));
                                startActivity(intent);
                            }
                        })
                        .show();
                break;

            case R.id.menuNight: {
                //Toast.makeText(this, "Night Mode status changed.", Toast.LENGTH_SHORT).show();
                final File file = new File(PATH);
                if (nmode) {
                    nmode = false;
                    menu.findItem(R.id.menuNight).setTitle("Enable Night Mode");
                } else {
                    nmode = true;
                    menu.findItem(R.id.menuNight).setTitle("Disable Night Mode");


                }
                final com.github.barteksc.pdfviewer.listener.OnPageChangeListener onPageChangeListener = new com.github.barteksc.pdfviewer.listener.OnPageChangeListener() {
                    @Override
                    public void onPageChanged(int page, int pageCount) {
                        current_page = page;
                        updateCurrentBookStatusDB();
                    }
                };
                pdf.fromFile(file)
                        .enableAnnotationRendering(true)
                        .onPageChange(onPageChangeListener)
                        .nightMode(nmode)
                        .defaultPage(current_page +1)
                        .load();
                break;
            }


//            case R.id.bmark: {
//                bookmark();
//                break;
//            }
//
//
//            case R.id.veset: {
//                change();
//                break;
//            }

            case R.id.tedit: {
                highlight(1, 1);
                if (t1.isSpeaking()) {
                    pausevoice();
                }
                //    textToSpeechManager.stops();
                pausevoice();

                Intent i = new Intent(this, edit_mode.class);
                i.putExtra("pdfpath", PATH);
                Log.e("pdfpath", PATH);
                startActivity(i);
            }


        }
        return true;
    }


    public void change() {
        //Toast.makeText(this, "Voice engine settings changed!", Toast.LENGTH_SHORT).show();
        //pausevoice();
        //if (status.equals("pause")) {
          //  play(null);
        //}
       // if (status.equals("pause")) {
         //   pausevoice();
         //   status = "play";
         //   imgbut.setImageDrawable(getDrawable(R.drawable.ic_icon_player_play));
         //   super.onResume();

         //   Constants.isPlayedBookStatus = false;
       // }
        //imgbut.setImageDrawable(getDrawable(R.drawable.ic_icon_player_play));
        Rect displayRectangle = new Rect();
        final Window window = this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        yourDialog = new Dialog(this);
        final LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_content, (ViewGroup) findViewById(R.id.lineardialog));
        layout.setMinimumWidth((int) (displayRectangle.width() * 0.8f));
        layout.setMinimumHeight((int) (displayRectangle.height() * 0.3f));
        yourDialog.setContentView(layout);
        rg = (RadioGroup) layout.findViewById(R.id.rg101);
        rb1 = (RadioButton) layout.findViewById(R.id.rb1);
        rb2 = (RadioButton) layout.findViewById(R.id.rb2);
        rb2 = (RadioButton) layout.findViewById(R.id.rb2);
        spin = (Spinner) layout.findViewById(R.id.spinner2);
        final List<String> country = new ArrayList<String>();
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(PDFViewer.this, android.R.layout.simple_spinner_item, country);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(spinnerArrayAdapter);

        try {
            rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    country.clear();
                    for (Voice tmpVoice : t1.getVoices()) {
                        if (rb1.isChecked()) {
                            if (tmpVoice.getName().startsWith(lang_code_default) && (tmpVoice.getName().contains("male"))
                                    && !(tmpVoice.getName().contains("female"))) {
                                country.add(tmpVoice.getName());
                            }
                        } else if (rb2.isChecked()) {
                            if (tmpVoice.getName().startsWith(lang_code_default) && tmpVoice.getName().contains("female")) {
                                country.add(tmpVoice.getName());

                            }
                        }
                    }
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(PDFViewer.this, android.R.layout.simple_spinner_item, country);
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin.setAdapter(spinnerArrayAdapter);
                }
            });

            spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    x = spin.getSelectedItem().toString();
                    // Toast.makeText(PDFViewer.this, ""+x, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

        } catch (Exception ex) {
            Log.e("spin error", ex.toString());
        }

        yourDialog.show();

        final float[] word_rate = new float[1];
        final float[] pitch = new float[1];

        final SeekBar p = (SeekBar) layout.findViewById(R.id.seekBar78);
        final SeekBar wr = (SeekBar) layout.findViewById(R.id.seekBar89);

        final Spinner spinnerLang = layout.findViewById(R.id.spinnerLang);
        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(PDFViewer.this, android.R.layout.simple_spinner_item, categoriesLangList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLang.setAdapter(dataAdapter);

        try {
            int spinnerPosition = dataAdapter.getPosition(book.getBook_lang());
            spinnerLang.setSelection(spinnerPosition);
        }catch (Exception e){ }
        spinnerLang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getApplicationContext(), ""+categoriesLangList.get(i)+" code:"+categoriesLangCodeList.get(i), Toast.LENGTH_SHORT).show();
                lang_code_default = categoriesLangCodeList.get(i);

                Constants.bookJoinTblReading.setBook_lang(categoriesLangList.get(i));

                new MyDbHelper(PDFViewer.this).updateBookLang(categoriesLangList.get(i), book.getBook_id());
                country.clear();
                for (Voice tmpVoice : t1.getVoices()) {
                    if (rb1.isChecked()) {
                        if (tmpVoice.getName().startsWith(lang_code_default) && (tmpVoice.getName().contains("male"))
                                && !(tmpVoice.getName().contains("female"))) {
                            country.add(tmpVoice.getName());
                        }
                    } else if (rb2.isChecked()) {
                        if (tmpVoice.getName().startsWith(lang_code_default) && tmpVoice.getName().contains("female")) {
                            country.add(tmpVoice.getName());

                        }
                    }
                }
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(PDFViewer.this, android.R.layout.simple_spinner_item, country);
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spin.setAdapter(spinnerArrayAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final TextView textViewPitch = layout.findViewById(R.id.textView2);
        final TextView textViewSpeed = layout.findViewById(R.id.textView5);

        chnge = (Button) layout.findViewById(R.id.apply);
        chnge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Toast.makeText(PDFViewer.this, ""+x, Toast.LENGTH_SHORT).show();
                t1.stop();
                book.setBook_lang(dataAdapter.getItem(spinnerLang.getSelectedItemPosition()));

                init(pitch[0], word_rate[0], x);
                yourDialog.dismiss();

                BookMeta bookMeta = new BookMeta();
                bookMeta.setMeta_pitch(p.getProgress());
                bookMeta.setMeta_rate(wr.getProgress());
                bookMeta.setMeta_lang_reading(x);
                bookMeta.setMeta_user_id(Integer.parseInt(new MyDbHelper(PDFViewer.this).getAllUser().getLogin_user_app_user_id()));
                new MyDbHelper(PDFViewer.this).insertBookMeta(bookMeta);

                //if (status.equals("play")) {
                  //  play(null);
                //}
                //play(null);
               // Toast.makeText(getApplicationContext(), "pitch :"+pitch[0]+"\nword rate:"+word_rate[0]+"\nvoice:"+x+"\nvoice gender:"+(rb1.isChecked()?"male":"female"), Toast.LENGTH_LONG).show();
            }
        });

        pitch[0] = 1.0f;

        word_rate[0] = 1.0f;
        p.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, final int progress, boolean fromUser) {
                pitch[0] = (float)seekBar.getProgress()/50f;
                textViewPitch.setText(progress+"%");
                //p.setProgress(progress);
              //  p.post(new Runnable() {
                //    @Override
                  //  public void run() {
                    //    p.setProgress(progress);

                   // }
               // });
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        wr.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, final int progress, boolean fromUser) {
                word_rate[0] = (float)seekBar.getProgress()/50f;
                textViewSpeed.setText(progress+"%");
                //wr.setProgress(progress);

              //  wr.post(new Runnable() {
                //    @Override
                  //  public void run() {
                    //    wr.setProgress(progress);
                   // }
               // });
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        if (new MyDbHelper(PDFViewer.this).getAllBookMeta().size()!=0) {
            BookMeta bookMeta = new MyDbHelper(PDFViewer.this).getAllBookMeta().get(new MyDbHelper(PDFViewer.this).getAllBookMeta().size() - 1);
            //Toast.makeText(getApplicationContext(),"lang setting avail "+bookMeta.getMeta_lang_reading(), Toast.LENGTH_LONG).show();
            pitch[0] = (float)bookMeta.getMeta_pitch()/50f;

            word_rate[0]  =(float)bookMeta.getMeta_rate()/50f;

            p.setProgress(bookMeta.getMeta_pitch());
            wr.setProgress(bookMeta.getMeta_rate());

            textViewPitch.setText(bookMeta.getMeta_pitch()+"%");
            textViewSpeed.setText(bookMeta.getMeta_rate()+"%");

            try {
                if (bookMeta.getMeta_lang_reading().contains("male") && (!bookMeta.getMeta_lang_reading().contains("female"))) {
                   // Toast.makeText(getApplicationContext(),"lang setting avail "+bookMeta.getMeta_lang_reading()+"{male", Toast.LENGTH_LONG).show();
                    rb1.setChecked(true);
                }
                if (bookMeta.getMeta_lang_reading().contains("female")) {
                    //Toast.makeText(getApplicationContext(),"lang setting avail "+bookMeta.getMeta_lang_reading()+"{female", Toast.LENGTH_LONG).show();
                    rb2.setChecked(true);
                }
                country.clear();
                for (Voice tmpVoice : t1.getVoices()) {

                    if (rb1.isChecked()) {
                        if (tmpVoice.getName().startsWith(lang_code_default) && (tmpVoice.getName().contains("male"))
                                && !(tmpVoice.getName().contains("female"))) {
                            country.add(tmpVoice.getName());
                        }
                    } else if (rb2.isChecked()) {
                        if (tmpVoice.getName().startsWith(lang_code_default) && tmpVoice.getName().contains("female")) {
                            country.add(tmpVoice.getName());

                        }
                    }
                }
            }catch (Exception e){}
        }else {
            rb2.setChecked(true);
            country.clear();
            for (Voice tmpVoice : t1.getVoices()) {
                if (rb1.isChecked()) {
                    if (tmpVoice.getName().startsWith(lang_code_default) && (tmpVoice.getName().contains("male"))
                            && !(tmpVoice.getName().contains("female"))) {
                        country.add(tmpVoice.getName());
                    }
                } else if (rb2.isChecked()) {
                    if (tmpVoice.getName().startsWith(lang_code_default) && tmpVoice.getName().contains("female")) {
                        country.add(tmpVoice.getName());

                    }
                }
            }
            spinnerArrayAdapter = new ArrayAdapter<String>(PDFViewer.this, android.R.layout.simple_spinner_item, country);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spin.setAdapter(spinnerArrayAdapter);
        }

        //Toast.makeText(getApplicationContext(), "pitch :"+pitch[0]+"\nword rate:"+word_rate[0]+"\nvoice:"+x+"\nvoice gender:"+(rb1.isChecked()?"male":"female"), Toast.LENGTH_LONG).show();
    }

    /** Bookmark **/
    ImageButton imageButtonBookmark;
    private boolean isBookmarkedPdf = false;
    BookNotesJoinBook bookNotesJoinBookFound = null;
    private void isBookmarked(){
        imageButtonBookmark = findViewById(R.id.imageButton8);
        findViewById(R.id.fabNotes).setVisibility(View.GONE);

        List<BookNotesJoinBook> list = new MyDbHelper(this).getAllBookNotesJoinBook();
        for (BookNotesJoinBook bookNotesJoinBook : list){
            if (bookNotesJoinBook.getNote_book_id() == Constants.bookJoinTblReading.getBook_id()){
                findViewById(R.id.fabNotes).setVisibility(View.VISIBLE);
                imageButtonBookmark.setColorFilter(Color.parseColor("#3FC3DD"));
                bookNotesJoinBookFound = bookNotesJoinBook;
                isBookmarkedPdf = true;
            }
        }
    }

    public void fabNotes(View view) {

        List<BookNotesJoinBook> list = new MyDbHelper(this).getAllBookNotesJoinBook();
        for (BookNotesJoinBook bookNotesJoinBook : list){
            if (bookNotesJoinBook.getNote_book_id() == Constants.bookJoinTblReading.getBook_id()){
                bookNotesJoinBookFound = bookNotesJoinBook;
                break;
            }
        }
        if (bookNotesJoinBookFound != null) {
            new AlertDialog.Builder(this).setTitle("Note").setMessage(bookNotesJoinBookFound.getNote_text()).create().show();
        }
        else {
            Toast.makeText(getApplicationContext(), "No notes added!", Toast.LENGTH_SHORT).show();
        }
    }

    private void addBookmark(String note){
        isBookmarkedPdf = true;
        findViewById(R.id.fabNotes).setVisibility(View.VISIBLE);
        imageButtonBookmark.setColorFilter(Color.parseColor("#3FC3DD"));

        int error = 0;
        Log.d("Total number of pages", "" + n);
        try {

            //db.execSQL("CREATE TABLE IF NOT EXISTS bookmarks(fpath VARCHAR,page_number VARCHAR ,total_pages VARCHAR, type VARCHAR);");
            BookNotesJoinBook bookNotes = new BookNotesJoinBook();
            bookNotes.setNote_page_number(current_page);
            bookNotes.setNote_text(note);
            bookNotes.setNote_book_id(Constants.bookJoinTblReading.getBook_id());
            bookNotes.setNote_user_id(Integer.parseInt(new MyDbHelper(PDFViewer.this).getAllUser().getLogin_user_app_user_id()));
            new MyDbHelper(PDFViewer.this).insertBookNotes(bookNotes);

            String sql = "INSERT or replace INTO bookmarks VALUES('" + PATH + "','" + current_page + "','" + n + "','pdf')";
            Log.d("path", "" + PATH);
            db.execSQL(sql);

        } catch (Exception e) {
            Toast.makeText(PDFViewer.this, "ERROR " + e.toString(), Toast.LENGTH_LONG).show();
            Log.d("error db", e.toString());
            error = 1;
        }
        if (error != 1) {
            Toast.makeText(this, "Bookmark successfully added!", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("WrongConstant")
    public void bookmark() {
        if (!isBookmarkedPdf) {
            final EditText editTextNote = new EditText(this);
            editTextNote.setHint("Write note... ");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(editTextNote);

            builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    addBookmark(editTextNote.getText().toString());
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    addBookmark("");
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.setTitle("Add Note");
            alertDialog.show();

        }else {
            String items[] = {"Edit", "Remove"};
            new AlertDialog.Builder(this)
                    .setSingleChoiceItems(items, 0, null)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                            int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                            // Do something useful withe the position of the selected radio button

                            if (selectedPosition == 0){
                                final EditText editTextNoteRe = new EditText(PDFViewer.this);
                                editTextNoteRe.setHint("Re-Write note... ");
                                AlertDialog.Builder builder = new AlertDialog.Builder(PDFViewer.this);
                                builder.setView(editTextNoteRe);

                                builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (bookNotesJoinBookFound != null) {
                                            if (new MyDbHelper(PDFViewer.this).updateNotes(bookNotesJoinBookFound.getNote_id(), editTextNoteRe.getText().toString())) {
                                                Toast.makeText(getApplicationContext(), "Note Updated", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Unable to remove", Toast.LENGTH_SHORT).show();
                                            }
                                        }else {
                                            Toast.makeText(getApplicationContext(), "Unable to remove", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                                AlertDialog alertDialog = builder.create();
                                alertDialog.setTitle("Edit Note");
                                alertDialog.show();
                            }
                            if (selectedPosition == 1){
                                isBookmarkedPdf = false;
                                findViewById(R.id.fabNotes).setVisibility(View.GONE);
                                imageButtonBookmark.setColorFilter(Color.parseColor("#2f2929"));
                                if (bookNotesJoinBookFound != null) {
                                    if (new MyDbHelper(PDFViewer.this).deleteNotes(bookNotesJoinBookFound.getNote_id())) {
                                        Toast.makeText(getApplicationContext(), "Bookmark removed", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Unable to remove", Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    Toast.makeText(getApplicationContext(), "Unable to remove", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    })
                    .show();
        }
    }


    public void stop(View view) {
        parsedText = "";
        t1.stop();
        imgbut.setImageDrawable(getDrawable(R.drawable.mdplay));
        final File file = new File(PATH);

        com.github.barteksc.pdfviewer.listener.OnPageChangeListener onPageChangeListener = new com.github.barteksc.pdfviewer.listener.OnPageChangeListener() {
            @Override
            public void onPageChanged(int page, int pageCount) {
                simpleSeekBar.setProgress(page);
                // initiate the Seekbar
                current_page = page;
                Log.d("current page ", "xxxTenacon" + 0);
                simpleSeekBar.post(new Runnable() {
                    @Override
                    public void run() {
                        simpleSeekBar.setProgress(current_page);
                    }
                });
            }
        };
        pdf.fromFile(file)
                .defaultPage(current_page + 1)
                .onPageChange(onPageChangeListener)
                .nightMode(nmode)
                .load();
        simpleSeekBar.setProgress(current_page);
        i = 0;
        simpleSeekBar.post(new Runnable() {
            @Override
            public void run() {
                simpleSeekBar.setProgress(current_page);
            }
        });
    }

    public static class PlayBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intentr) {
            Toast.makeText(context, ""+intentr.getStringExtra("play"), Toast.LENGTH_SHORT).show();

        }
    }

    public static class PauseBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intentr) {
            Toast.makeText(context, ""+intentr.getStringExtra("pause"), Toast.LENGTH_SHORT).show();
        }
    }

    public static class CancelBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intentr) {
           // Toast.makeText(context, ""+intentr.getStringExtra("cancel"), Toast.LENGTH_SHORT).show();
            NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(0);
        }
    }

    public class NotificationHelper {

        private Context mContext;
        private NotificationManager mNotificationManager;
        private NotificationCompat.Builder mBuilder;
        public static final String NOTIFICATION_CHANNEL_ID = "10001";

        public NotificationHelper(Context context) {
            mContext = context;
        }

        /**
         * Create and push the notification
         */
        public void createNotification(String title, String message) {

         //   RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification);
            //remoteViews.setTextViewText(R.id.name, File_name);
         //   mBuilder.setContent(remoteViews);

            /**Creates an explicit intent for an Activity in your app**/
            Intent resultIntent = new Intent(mContext , MainActivity.class);
            resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext, 0 /* Request code */, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Intent snoozeIntent = new Intent(PDFViewer.this, MyBroadcastReceiver.class);
            snoozeIntent.putExtra("value", "abc");
            PendingIntent snoozePendingIntent = PendingIntent.getBroadcast(PDFViewer.this, 0, snoozeIntent, PendingIntent.FLAG_UPDATE_CURRENT);


            Intent playIntent = new Intent(PDFViewer.this, PlayBroadCast.class);
            playIntent.putExtra("play", "Play");
            PendingIntent playPendingIntent = PendingIntent.getBroadcast(PDFViewer.this, 0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Intent pauseIntent = new Intent(PDFViewer.this, PauseBroadCast.class);
            pauseIntent.putExtra("pause", "Pause");
            PendingIntent pausePendingIntent = PendingIntent.getBroadcast(PDFViewer.this, 0, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Intent cancelIntent = new Intent(PDFViewer.this, CancelBroadCast.class);
            cancelIntent.putExtra("cancel", "Cancel");
            PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(PDFViewer.this, 0, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            mBuilder = new NotificationCompat.Builder(mContext);
            mBuilder.setSmallIcon(android.R.drawable.ic_media_play);
            mBuilder.setContentTitle(title)
                   // .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                    .setContentIntent(resultPendingIntent);
            //mBuilder.addAction(android.R.drawable.ic_media_play,"Play",playPendingIntent);
            //mBuilder.addAction(android.R.drawable.ic_media_pause,"Pause",pausePendingIntent);
            mBuilder.addAction(R.drawable.ic_baseline_close_24,"Cancel",cancelPendingIntent);
            mBuilder.setOngoing(true);

            mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
            {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.RED);
                notificationChannel.enableVibration(true);
                notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                assert mNotificationManager != null;
                mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
                mNotificationManager.createNotificationChannel(notificationChannel);
            }
            assert mNotificationManager != null;
            mNotificationManager.notify(0 /* Request Code */, mBuilder.build());
        }
    }

    Handler handler;
    Runnable runnable;
    int counterReadingSecond = 0;
    public void play(View view) {
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
        if (status.equals("play")) {
           // notificationService();

        //    new NotificationHelper(this).createNotification(File_name, "Message");

            extract(PATH, current_page);
            status = "pause";
            imgbut.setImageDrawable(getDrawable(R.drawable.ic_icon_player_pause));
            super.onPause();

            handler = new Handler();
            runnable = new Runnable() {
                @Override
                public void run() {
                    if (status.equals("pause"))
                        counterReadingSecond++;
                    handler.postDelayed(this, 1000);
                }
            };
            handler.postDelayed(runnable, 1000);

            Constants.isPlayedBookStatus = true;
            //prevLine(null);
           // Toast.makeText(getApplicationContext(), "play called", Toast.LENGTH_SHORT).show();
        } else if (status.equals("pause")) {
            pausevoice();
            status = "play";
            imgbut.setImageDrawable(getDrawable(R.drawable.ic_icon_player_play));
            super.onResume();

            Constants.isPlayedBookStatus = false;
        }

    }

    public void pausevoice() {
        if (t1.isSpeaking()) {
            parsedText = "";
            t1.stop();
            highlight(1, 10);
        }

        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
       // Toast.makeText(getApplicationContext(), "pause called", Toast.LENGTH_SHORT).show();
    }

    public void Bookmark(View view) {
        bookmark();
    }


    private void updateCurrentBookStatusDB(){
        getSupportActionBar().setSubtitle("Page "+(current_page+1)+" of "+book.getBook_page_count());
        tvPDFViewerPages.setText("Page "+(current_page+1)+" of "+book.getBook_page_count());

        List<BookReadingCurrentStatus> readingCurrentStatuses = new MyDbHelper(PDFViewer.this).getAllBookReadingCurrentStatus();
        for (int i=0; i<readingCurrentStatuses.size(); i++){
            if (Constants.bookJoinTblReading.getBook_id() == readingCurrentStatuses.get(i).getReading_book_id()){
                BookReadingCurrentStatus bookReadingCurrentStatus = readingCurrentStatuses.get(i);
                bookReadingCurrentStatus.setReading_page_number(current_page-1);
                bookReadingCurrentStatus.setReading_word_number(end);
                new MyDbHelper(PDFViewer.this).updateBookReadingCurrentStatus(bookReadingCurrentStatus);
                break;
            }
        }
    }


    public void nextpage(View view) {
        if (current_page < n-1) {
            final File file = new File(PATH);
            // final SeekBar simpleSeekBar = (SeekBar) findViewById(R.id.seekBar2);
            com.github.barteksc.pdfviewer.listener.OnPageChangeListener onPageChangeListener = new com.github.barteksc.pdfviewer.listener.OnPageChangeListener() {
                @Override
                public void onPageChanged(int page, int pageCount) {
                    simpleSeekBar.setProgress(page);
                    current_page = page;
                    //Log.d("current page ", "xxxTenacon" + current_page);
                    simpleSeekBar.post(new Runnable() {
                        @Override
                        public void run() {
                            simpleSeekBar.setProgress(current_page);
                        }
                    });
                    updateCurrentBookStatusDB();
                }
            };
            pdf.fromFile(file)
                    .defaultPage(current_page + 1)
                    .onPageChange(onPageChangeListener)
                    .nightMode(nmode)
                    // .swipeHorizontal(true)
                    .swipeHorizontal(false)
                    .enableSwipe(true)
                    .load();
            simpleSeekBar.setProgress(current_page);
            simpleSeekBar.post(new Runnable() {
                @Override
                public void run() {
                    simpleSeekBar.setProgress(current_page);
                }
            });

            clear();
            updateCurrentBookStatusDB();
        }else {
            Toast.makeText(getApplicationContext(), "Already on Last Page", Toast.LENGTH_SHORT).show();
        }
    }

    public void prevpage(View view) {
        if (current_page != 0) {
            final File file = new File(PATH);
            // final SeekBar simpleSeekBar = (SeekBar) findViewById(R.id.seekBar2);
            com.github.barteksc.pdfviewer.listener.OnPageChangeListener onPageChangeListener = new com.github.barteksc.pdfviewer.listener.OnPageChangeListener() {
                @Override
                public void onPageChanged(int page, int pageCount) {
                    simpleSeekBar.setProgress(page);
                    current_page = page;
                    //Log.d("current page ", "xxxTenacon" + current_page);
                    simpleSeekBar.post(new Runnable() {
                        @Override
                        public void run() {
                            simpleSeekBar.setProgress(current_page);
                        }
                    });
                    updateCurrentBookStatusDB();
                }
            };
            pdf.fromFile(file)
                    .defaultPage(current_page - 1)
                    .onPageChange(onPageChangeListener)
                    //.swipeHorizontal(true)
                    .swipeHorizontal(false)
                    .enableSwipe(true)
                    .nightMode(nmode)
                    .load();
            simpleSeekBar.setProgress(current_page);
            simpleSeekBar.post(new Runnable() {
                @Override
                public void run() {
                    simpleSeekBar.setProgress(current_page);
                    simpleSeekBar.post(new Runnable() {
                        @Override
                        public void run() {
                            simpleSeekBar.setProgress(current_page);
                        }
                    });
                }
            });
            clear();
            if (status.equals("pause"))
            extract(PATH, current_page - 1);
            updateCurrentBookStatusDB();
        }else {
            Toast.makeText(getApplicationContext(), "Already on First Page", Toast.LENGTH_SHORT).show();
        }
    }

    public void nextLine(View view) {
        try {
            t1.stop();
            i = end + 1;
        }catch (Exception e){

        }
    }

    public void prevLine(View view) {
        try {
            t1.stop();
            for (int j = previ - 2; j >= 0; j--) {
                if (parsedText.charAt(j) == '.') {
                    i = j + 1;
                    break;
                } else {
                    i = 0;
                }
            }
        }catch (Exception e){

        }

        //i = previ;
    }
    public void clear() {
        parsedText = "";
        i = 0;
        t1.stop();

        // Toast.makeText(this, "Stop!", Toast.LENGTH_SHORT).show();
        // Intent goback=new Intent(this,MainActivity.class);
        // startActivity(goback);
        // TextView f=(TextView)findViewById(R.id.textview);
        // f.setText(parsedText);
    }
    void extract(String yourPdfPath, int start_page) {
        Log.d("Extract call", "" + start_page);
        Log.d("Extract call", "" + n);

        if (start_page < (n)) {
            try {
                parsedText = "";
                PdfReader reader = new PdfReader(yourPdfPath);
                n = reader.getNumberOfPages();
                // for (int ig = start_page; ig <n; ig++)
                // {
                parsedText = parsedText + PdfTextExtractor.getTextFromPage(reader, start_page + 1); //Extracting the content from the different pages
                //}

                Log.d("Extract text:", parsedText + "sup");
                trigger(0, parsedText + ".");
                highlight(0, 0);

                reader.close();
            } catch (Exception e) {
                Log.d("extract method", "extract: " + e);
            }
        } else {
              //Toast.makeText(this, "End of document", Toast.LENGTH_SHORT).show();
        }
        //Toast.makeText(this, "extract method", Toast.LENGTH_SHORT).show();
    }

    public void trigger(final int l, final String PdfPath) {
        Log.d("speaking-highlight", t1.isSpeaking() + "");
        final Handler handler = new Handler();
        final int delay = 0; //milliseconds

        handler.postDelayed(new Runnable() {
            public void run() {
                //do something
                if (!t1.isSpeaking())
                    highlight(0, 0);
                handler.postDelayed(this, delay);
            }
        }, delay);
        //Toast.makeText(this, "trigeer method", Toast.LENGTH_SHORT).show();
    }


    public void highlight(int h, int g) {
        //Toast.makeText(this, "highlight method", Toast.LENGTH_SHORT).show();
        int temp = 0;
        getSupportActionBar().setSubtitle("Page "+(current_page+1)+" of "+book.getBook_page_count());
        tvPDFViewerPages.setText("Page "+(current_page+1)+" of "+book.getBook_page_count());

        if (i <= parsedText.length()) {
           // Toast.makeText(this, "current page "+current_page, Toast.LENGTH_SHORT).show();

            SpannableString str = new SpannableString(parsedText);

            try {
                end = parsedText.indexOf(".", i);
            } catch (Exception e) {
                Log.d("Ending exception", e + "");
            }

            if (end != -1 && h == 0) {

                str.setSpan(new BackgroundColorSpan(Color.YELLOW), i, end + 1, 0);
                Log.d("string length", "SL " + str.length() + " I " + i + " END " + end);
                String toSpeak = parsedText.substring(i, end);
                //Toast.makeText(getApplicationContext(), "i = "+i+" end"+end, Toast.LENGTH_SHORT).show();
                updateCurrentBookStatusDB();
                //   t1.setText(str);
                previ = i;
                i = end + 1;
                Log.d("high check", toSpeak + "testing");
                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                Log.d("speaking?", t1.isSpeaking() + "");

            } else if (current_page + 1 < n && t1.isSpeaking() == false) {

                final File file = new File(PATH);
               // final SeekBar simpleSeekBar = (SeekBar) findViewById(R.id.seekBar2);

                OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {
                    @Override
                    public void onPageChanged(int page, int pageCount) {
                        simpleSeekBar.setProgress(page);
                        current_page = page;
                        Log.d("current page ", "xxxTenacon666" + current_page);
                        simpleSeekBar.post(new Runnable() {
                            @Override
                            public void run() {
                                simpleSeekBar.setProgress(current_page);
                            }
                        });
                        updateCurrentBookStatusDB();
                    }
                };
                pdf.fromFile(file)
                        .defaultPage(current_page + 1)
                        .onPageChange(onPageChangeListener)
                        .nightMode(nmode)
                        .load();
                i = 0;
                if (current_page < n)
                    current_page++;
                extract(PATH, current_page);
                simpleSeekBar.setProgress(current_page);
                simpleSeekBar.post(new Runnable() {
                    @Override
                    public void run() {
                        simpleSeekBar.setProgress(current_page);
                    }
                });
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        pausevoice();
        Intent intent = new Intent(PDFViewer.this, MainActivity.class);
        intent.putExtra("PageTrack", current_page + "");
        intent.putExtra("PathTrack", PATH);
        startActivity(intent);

        if (counterReadingSecond != 0)
        new MyDbHelper(PDFViewer.this).insertBookStats(Constants.bookJoinTblReading.getBook_id(), Constants.bookJoinTblReading.getBook_user_id(), 0, counterReadingSecond);
        //Toast.makeText(getApplicationContext(), "Back to Home page", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdf.getDocumentMeta();
        printBookmarksTree(pdf.getTableOfContents(), "-");
    }

    private void printBookmarksTree(List<PdfDocument.Bookmark> tableOfContents, String s) {
        for (PdfDocument.Bookmark b : tableOfContents) {
            Log.e(TAG, String.format("%s %s, p %d", s, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), s + "-");
            }
        }
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
       // final SeekBar simpleSeekBar = (SeekBar) findViewById(R.id.seekBar2); // initiate the Seekbar
        simpleSeekBar.setProgress(page);
        current_page = page;
        updateCurrentBookStatusDB();

        pageNumber = page;
        setTitle(String.format("%s %s / %s", File_name, page + 1, pageCount));
    }
/*
    public void notificationService() {

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification);
        remoteViews.setTextViewText(R.id.name, File_name);
        mBuilder.setContent(remoteViews);

        // Function for Action Buttons on Notification
        Intent snoozeIntent = new Intent(this, MyBroadcastReceiver.class);
        snoozeIntent.putExtra("value", "abc");
        PendingIntent snoozePendingIntent =
                PendingIntent.getBroadcast(this, 0, snoozeIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Attach with notification
        mBuilder.setContentIntent(snoozePendingIntent);
        mBuilder.setSmallIcon(R.mipmap.doclauncher);
        mBuilder.setContentText("Hi,This is a Notification Detail!");
        mBuilder.addAction(R.drawable.ic_icon_player_play,"Play",snoozePendingIntent);
        mBuilder.addAction(R.drawable.ic_icon_player_pause,"Pause",snoozePendingIntent);
       // mBuilder.setVisibility(Notification.VISIBILITY_PUBLIC);
        mBuilder.setCategory(NotificationCompat.CATEGORY_TRANSPORT);
        mBuilder.setAutoCancel(true);
        mBuilder.setColor(Color.BLACK);
        mBuilder.build();
        // Tap on Notification do ->
        Intent resultIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(PDFViewer.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(7, mBuilder.build());

    }
*/

}

