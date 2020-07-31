package com.example.keerat666.listviewpdfui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.keerat666.listviewpdfui.db.MyDbHelper;
import com.example.keerat666.listviewpdfui.models.BookJoinTblReading;
import com.example.keerat666.listviewpdfui.models.BookMeta;
import com.example.keerat666.listviewpdfui.models.BookReadingCurrentStatus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TxtMaker extends AppCompatActivity {
    TextView t;
    Button chnge2;
    String parsedText;
    int statusNight;
    int i = 0;
    int end = 0;
    String p;
    String x;
    Spinner spin;
    ScrollView sv;
    TextToSpeech t1;
    String status = "play";
    ImageButton imgbut;
    RelativeLayout rl;
    int tempi;
    RadioGroup rg;
    RadioButton rb1;
    RadioButton rb2;
    LinearLayout ll1;
    int plength = 0;
    ArrayList<Integer> itracker = new ArrayList<>();
    int counter = 0;
    ArrayList<String> lines = new ArrayList<>();
    SeekBar s;
    Menu menu;
    androidx.appcompat.widget.Toolbar tb;
    Dialog yourDialog;

    private BookJoinTblReading book = null;
    private String lang_code_default = "en";

    private BookJoinTblReading book1 = null;
    ArrayList<String> categoriesLangList = new ArrayList<>();
    ArrayList<String> categoriesLangCodeList = new ArrayList<>();

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

        setContentView(R.layout.activity_txt_maker);
        getSupportActionBar().setTitle(Constants.bookJoinTblReading.getBook_name());
        getSupportActionBar().setElevation(2);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_keyboard_arrow_left_24);

        book = Constants.bookJoinTblReading;

        ll1 = (LinearLayout) findViewById(R.id.ppcont1);
        sv = (ScrollView) findViewById(R.id.sviewtxt);
        i = 0;
     //   tb = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar30);
     //   setSupportActionBar(tb);

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

                    if (new MyDbHelper(TxtMaker.this).getAllBookMeta().size()!=0) {
                        BookMeta bookMeta = new MyDbHelper(TxtMaker.this).getAllBookMeta().get(new MyDbHelper(TxtMaker.this).getAllBookMeta().size() - 1);
                        init(((float)(bookMeta.getMeta_pitch())/50), ((float)(bookMeta.getMeta_rate())/50), bookMeta.getMeta_lang_reading());
                    }else {
                        init(1.0f, 1.0f, "");
                    }

                    Log.d("voices", t1.getVoices() + "");
                    // Toast.makeText(TxtMaker.this, ""+t1.getVoices(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        p = getIntent().getStringExtra("txtpath");
        File file = new File(p);

        p = getIntent().getStringExtra("txtpath");
        sv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ll1.getVisibility() == ll1.VISIBLE) {
                    ll1.setVisibility(ll1.GONE);
                } else
                    ll1.setVisibility(ll1.VISIBLE);
            }
        });
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
            //You'll need to add proper error handling here
            Log.d("error", "" + e.toString());
        }
        t = (TextView) findViewById(R.id.tview);
        t.setText(text.toString());
        parsedText = text.toString()+".";
        s = (SeekBar) findViewById(R.id.seekBar3);
        s.setMax(parsedText.length());
        imgbut = (ImageButton) findViewById(R.id.imageButton2);

        s.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser == true) {
                    i = progress;
                    t1.stop();
                    Log.d("Seek", "" + progressChangedValue);
                }

            }


            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("progress", "" + progressChangedValue);
            }
        });

        RelativeLayout relativeLayoutMain = findViewById(R.id.rlcont);

        if (getString(R.string.theme_light).equals(theme)) {
            LinearLayout linearLayout = findViewById(R.id.ppcont1);
            linearLayout.setBackgroundColor(getResources().getColor(R.color.lightColorDark));

            relativeLayoutMain.setBackgroundColor(getResources().getColor(R.color.lightColorLight));
        }
        if (getString(R.string.theme_warm).equals(theme)){
            LinearLayout linearLayout = findViewById(R.id.ppcont1);
            linearLayout.setBackgroundColor(getResources().getColor(R.color.warmColorDark));

            relativeLayoutMain.setBackgroundColor(getResources().getColor(R.color.warmColorLight));
        }
        if (getString(R.string.theme_dark).equals(theme)){
            LinearLayout linearLayout = findViewById(R.id.ppcont1);
            linearLayout.setBackgroundColor(getResources().getColor(R.color.darkColorDark));

            ImageButton prevPage = findViewById(R.id.imageButton4);
            prevPage.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary));

            ImageButton play = findViewById(R.id.imageButton2);
            play.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary));

            ImageButton nextPage = findViewById(R.id.imageButton5);
            nextPage.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary));

            relativeLayoutMain.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDarkTheme));
        }

        Constants.isPlayedBookStatus = false;
    }

    public void txtextract() {
        p = getIntent().getStringExtra("txtpath");
        File file = new File(p);

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
            //You'll need to add proper error handling here
            Log.d("error", "" + e.toString());
        }
        t = (TextView) findViewById(R.id.tview);
        t.setText(text.toString());
        parsedText = text.toString() + ".";
        s.setMax(parsedText.length());
        trigger(0);
    }


    // Log.d("Inner Text",st);

    public void clear() {
        parsedText = "";
        i = 0;
        t1.stop();
    }

    public void trigger(final int i) {
        Log.d("speaking-highlight", t1.isSpeaking() + "");
        final Handler handler = new Handler();
        final int delay = 0; //milliseconds

        handler.postDelayed(new Runnable() {
            public void run() {
                //do something
                try{
                    if (!t1.isSpeaking()) {
                        highlight(i);
                    }
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT)
                            .show();
                }

                handler.postDelayed(this, delay);

                if ((s.getProgress()+1) == s.getMax()){
                   //pausevoice();
                }
            }
        }, delay);


    }

    public void pausevoice() {

        parsedText = "";
        i = tempi;
        t1.stop();

        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
    }

    public void nextLine(View view) {
        t1.stop();

    }

    public void prevLine(View view) {
        try {
            if (itracker.size() >= 2) {
                t1.stop();
                Log.d("i before", "" + i);
                i = i - itracker.get(itracker.size() - 1);
                i = i - itracker.get(itracker.size() - 2);
                Log.d("itracker last", "" + itracker.get(itracker.size() - 1));
                Log.d("I value", i + "");
                if (i<0){
                    i = 0;
                }
            } else
                Toast.makeText(this, "No previous line found!", Toast.LENGTH_SHORT).show();
        }catch (Exception e){}
    }

    Handler handler;
    Runnable runnable;
    int counterReadingSecond = 0;
    public void play(View view) {
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }

        if (status.equals("play")) {

            status = "pause";
            imgbut.setImageDrawable(getDrawable(R.drawable.ic_icon_player_pause));
            txtextract();

            handler = new Handler();
            runnable = new Runnable() {
                @Override
                public void run() {
                    if (status.equals("pause"))
                        counterReadingSecond++;
                    handler.postDelayed(this, 60000);
                }
            };
            handler.postDelayed(runnable, 60000);

            Constants.isPlayedBookStatus = true;
        } else if (status.equals("pause")) {
            pausevoice();
            status = "play";
            imgbut.setImageDrawable(getDrawable(R.drawable.ic_icon_player_play));

            Constants.isPlayedBookStatus = false;
        }

    }

    int wordCounter = 0;
    public void highlight(int h) {

        String span;
        if (i <= parsedText.length()) {
            SpannableString str = new SpannableString(parsedText);

            try {
                end = parsedText.indexOf(".", i);
                Log.d("end index", end + "");

            } catch (Exception e) {
                Log.d("Ending exception", e + "");
                // Toast.makeText(this, "End of i!", Toast.LENGTH_SHORT).show();

            }

            try {
                String subString = parsedText.substring(i, end);
                String countSubStringWord[] = subString.split(" ");
                wordCounter = wordCounter + (countSubStringWord.length);
            }catch (Exception e){

            }
            if (end != -1) {
                if(statusNight==1){
                    str.setSpan(new BackgroundColorSpan(Color.GRAY), i, end + 1, 0);
                }
                else
                str.setSpan(new BackgroundColorSpan(Color.YELLOW), i, end + 1, 0);
                Log.d("string length", "SL " + str.length() + " I " + i + " END " + end);
                String toSpeak = parsedText.substring(i, end);

                t.setText(str);
                tempi = i;

                s.setProgress(end);
                List<BookReadingCurrentStatus> readingCurrentStatuses = new MyDbHelper(TxtMaker.this).getAllBookReadingCurrentStatus();
                for (int i=0; i<readingCurrentStatuses.size(); i++){
                    if (Constants.bookJoinTblReading.getBook_id() == readingCurrentStatuses.get(i).getReading_book_id()){
                        BookReadingCurrentStatus bookReadingCurrentStatus = readingCurrentStatuses.get(i);
                        bookReadingCurrentStatus.setReading_word_number(i);
                        new MyDbHelper(TxtMaker.this).updateBookReadingCurrentStatus(bookReadingCurrentStatus);
                        break;
                    }
                }

               // Toast.makeText(getApplicationContext(), "reading status words:"+wordCounter, Toast.LENGTH_SHORT).show();
                wordCounter = wordCounter -1;
                i = end + 1;

                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                plength = end - tempi + 1;
                itracker.add(plength);
                Log.d("plength", itracker + "");
                Log.d("speaking?", t1.isSpeaking() + "");
            } else {
                i = 0;
            }


        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        pausevoice();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("PathTrack", p);
        startActivity(intent);

        new MyDbHelper(TxtMaker.this).insertBookStats(Constants.bookJoinTblReading.getBook_id(), Constants.bookJoinTblReading.getBook_user_id(), 0, counterReadingSecond);
        //Toast.makeText(getApplicationContext(), "Back to Home page", Toast.LENGTH_SHORT).show();
    }

    public void dark()
    {
        if(statusNight==0){
        sv.setBackgroundColor(getResources().getColor(R.color.nmode));
        t.setTextColor(getResources().getColor(R.color.background_material_dark));
        statusNight=1;
        }
        else{
            sv.setBackgroundColor(getResources().getColor(R.color.background_material_dark));
            t.setTextColor(getResources().getColor(R.color.nmode));
            statusNight=0;
        }


    }

    public void change() {
        //Toast.makeText(this, "Voice engine settings changed!", Toast.LENGTH_SHORT).show();
        //pausevoice();

       // if (status.equals("pause")) {
         //   pausevoice();
         //   status = "play";
         //   imgbut.setImageDrawable(getDrawable(R.drawable.ic_icon_player_play));

         //   Constants.isPlayedBookStatus = false;
       // }
        //status = "pause";
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
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(TxtMaker.this, android.R.layout.simple_spinner_item, country);
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
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(TxtMaker.this, android.R.layout.simple_spinner_item, country);
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
        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(TxtMaker.this, android.R.layout.simple_spinner_item, categoriesLangList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLang.setAdapter(dataAdapter);

        try {
            int spinnerPosition = dataAdapter.getPosition(book.getBook_lang());
            spinnerLang.setSelection(spinnerPosition);
        }catch (Exception e){ }
        spinnerLang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               // Toast.makeText(getApplicationContext(), ""+categoriesLangList.get(i)+" code:"+categoriesLangCodeList.get(i), Toast.LENGTH_SHORT).show();
                lang_code_default = categoriesLangCodeList.get(i);
                new MyDbHelper(TxtMaker.this).updateBookLang(categoriesLangList.get(i), book1.getBook_id());
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
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(TxtMaker.this, android.R.layout.simple_spinner_item, country);
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spin.setAdapter(spinnerArrayAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final TextView textViewPitch = layout.findViewById(R.id.textView2);
        final TextView textViewSpeed = layout.findViewById(R.id.textView5);

        Button chnge = (Button) layout.findViewById(R.id.apply);
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
                bookMeta.setMeta_user_id(Integer.parseInt(new MyDbHelper(TxtMaker.this).getAllUser().getLogin_user_app_user_id()));
                new MyDbHelper(TxtMaker.this).insertBookMeta(bookMeta);

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

        if (new MyDbHelper(TxtMaker.this).getAllBookMeta().size()!=0) {
            BookMeta bookMeta = new MyDbHelper(TxtMaker.this).getAllBookMeta().get(new MyDbHelper(TxtMaker.this).getAllBookMeta().size() - 1);
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
            spinnerArrayAdapter = new ArrayAdapter<String>(TxtMaker.this, android.R.layout.simple_spinner_item, country);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spin.setAdapter(spinnerArrayAdapter);
        }
        //Toast.makeText(getApplicationContext(), "pitch :"+pitch[0]+"\nword rate:"+word_rate[0]+"\nvoice:"+x+"\nvoice gender:"+(rb1.isChecked()?"male":"female"), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.hamt, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.menuNightt:
                dark();
                break;


            case R.id.vesett: {

                change();
                break;
            }

            case R.id.setting_hamt_menu_item :

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
                                Intent intent = new Intent(TxtMaker.this, TxtMaker.class);
                                intent.putExtra("txtpath", getIntent().getStringExtra("txtpath"));
                                startActivity(intent);
                            }
                        })
                        .show();
                break;

        }
        return true;

    }


    public void init(final float p, final float sr,final String voice) {

        book1 = Constants.bookJoinTblReading;

      //  t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
       //     @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
       //     @Override
        //    public void onInit(int status) {
               /* t1.setPitch(p);
                t1.setSpeechRate(sr);
                for (Voice tmpVoice : t1.getVoices()) {
                    if (tmpVoice.getName().equals(voice))
                    {
                        t1.setVoice(tmpVoice);

                        break;
                    }
                }
                Log.d("voices", t1.getVoices() + "");*/
              //  if (status != TextToSpeech.ERROR) {
                   // book1 = Constants.bookJoinTblReading;
                    //  t1.setLanguage(Locale.UK);
                    t1.setPitch(p);//1.0f);
                    t1.setSpeechRate(sr);//1.0f);

                    Locale[] locales = Locale.getAvailableLocales();
                    List<Locale> localeList = new ArrayList<Locale>();

                    for (Locale locale : locales) {
                        int res = t1.isLanguageAvailable(locale);
                        if (res == TextToSpeech.LANG_COUNTRY_AVAILABLE) {
                            localeList.add(locale);
                            if (locale.getDisplayName().equals(book1.getBook_lang())) {
                                t1.setLanguage(locale);

                                // Toast.makeText(getApplicationContext(), "book_lang :" + book.getBook_lang()
                                //               + " \ncountry lang code :" + locale.getCountry()
                                //             + " \ndefault code :" + locale.getLanguage()
                                //   , Toast.LENGTH_LONG).show();
                                //tvPDFViewerLanguage.setText(""+ locale.getLanguage().toUpperCase());

                                lang_code_default = locale.getLanguage();
                                break;
                            }
                        }
                    }

                    for (Locale locale : locales) {
                        int res = t1.isLanguageAvailable(locale);
                        if (res == TextToSpeech.LANG_COUNTRY_AVAILABLE) {
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
                    Log.d("voices", t1.getVoices() + "");

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            menu.findItem(R.id.lang_txt_reader).setTitle("" + categoriesLangCodeList.get(categoriesLangList.indexOf(book.getBook_lang())));
                        }
                    }, 200);
                }
       //     }
       // });
   // }

}



