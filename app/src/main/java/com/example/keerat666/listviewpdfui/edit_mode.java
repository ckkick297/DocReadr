package com.example.keerat666.listviewpdfui;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.keerat666.listviewpdfui.db.MyDbHelper;
import com.example.keerat666.listviewpdfui.models.BookJoinTblReading;
import com.example.keerat666.listviewpdfui.models.BookMeta;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class edit_mode extends AppCompatActivity {
    TextView t;
    String parsedText;
    int i = 0;
    int end = 0;
    String p;
    ScrollView sv;
    TextToSpeech t1;
    String status = "play";
    ImageButton imgbut;
    RelativeLayout rl;
    int tempi;
    String File_name;
    LinearLayout ll1;
    Locale.Builder mBuilder;
    int plength = 0;
    ArrayList<Integer> itracker = new ArrayList<>();
    int counter = 0;
    ArrayList<String> lines = new ArrayList<>();
    SeekBar s;

    public void onPause(){
        super.onPause();
        pausevoice();
    }

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

        setContentView(R.layout.activity_edit_mode);
        getSupportActionBar().setElevation(2);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_keyboard_arrow_left_24);

        ll1 = (LinearLayout) findViewById(R.id.ppcont1);
        rl = (RelativeLayout) findViewById(R.id.rlcont);
        sv = (ScrollView) findViewById(R.id.sviewtxt);
        i = 0;

        if (new MyDbHelper(edit_mode.this).getAllBookMeta().size()!=0) {
            BookMeta bookMeta = new MyDbHelper(edit_mode.this).getAllBookMeta().get(new MyDbHelper(edit_mode.this).getAllBookMeta().size() - 1);
            float pitch = ((float)(bookMeta.getMeta_pitch())/50);
            float speed = ((float)(bookMeta.getMeta_rate())/50);
            //Toast.makeText(getApplicationContext(), "call with Pitch"+pitch+" rate"+speed, Toast.LENGTH_LONG).show();

            init(pitch, speed, bookMeta.getMeta_lang_reading());
        }else {
            init(1.0f, 1.0f, "");
        }

        /*
        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                    t1.setPitch(1.0f);
                    t1.setSpeechRate(1.0f);
                    Log.d("voices", t1.getVoices() + "");

                    // Toast.makeText(TxtMaker.this, ""+t1.getVoices(), Toast.LENGTH_SHORT).show();
                }
            }
        });
*/
        p = getIntent().getStringExtra("pdfpath");
        File file = new File(p);

        StringBuilder text = new StringBuilder();
        try {
            parsedText = "";
            PdfReader reader = new PdfReader(p);
            int n = reader.getNumberOfPages();
            for (int ig = 1; ig < n; ig++) {
                parsedText = parsedText  +PdfTextExtractor.getTextFromPage(reader, ig); //Extracting the content from the different pages
            }
            String toSpeak = parsedText.substring(i, end);
            Log.d("Extract text:", parsedText + "sup");
            reader.close();
            t = (TextView) findViewById(R.id.tview);
            t.setText(parsedText);
            t.setTextIsSelectable(true);
            t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
        } catch (Exception e) {
            Log.d("extract method ", "extract: " + e);
        }

        s = (SeekBar) findViewById(R.id.seekBar3);
        s.setMax(parsedText.length());
        imgbut = (ImageButton) findViewById(R.id.imageButton2);
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Toast.makeText(edit_mode.this, "table created ", Toast.LENGTH_LONG).show();

                if (ll1.getVisibility() == (ll1.VISIBLE)) {

                    ll1.setVisibility(ll1.GONE);


                } else {


                    ll1.setVisibility(ll1.VISIBLE);

                }


            }
        });
        s.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser == true) {
                    i = progressChangedValue;
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


    }

    private BookJoinTblReading book1 = null;
    public void init(final float p, final float sr,final String voice) {

        book1 = Constants.bookJoinTblReading;
        getSupportActionBar().setTitle(book1.getBook_name());

        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onInit(int status) {
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
                if (status != TextToSpeech.ERROR) {
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

                                //lang_code_default = locale.getLanguage();
                                break;
                            }
                        }
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
            }
        });
    }

    void extract(String yourPdfPath) {

        try {
            parsedText = "";
            PdfReader reader = new PdfReader(yourPdfPath);
            int n = reader.getNumberOfPages();
            for (int ig = 1; ig <= n; ig++) {
                parsedText = parsedText + PdfTextExtractor.getTextFromPage(reader, ig); //Extracting the content from the different pages
            }
            parsedText += ".";
            Log.d("Extract text:", parsedText + "sup");
            trigger(0);
            reader.close();
        } catch (Exception e) {
            Log.d("extract method ", "extract: " + e);
        }
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
                if (!t1.isSpeaking()) {
                    highlight(i);
                }


                handler.postDelayed(this, delay);
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
        if (itracker.size() >= 2) {
            t1.stop();
            Log.d("i before", "" + i);
            i = i - itracker.get(itracker.size() - 1);
            i = i - itracker.get(itracker.size() - 2);
            Log.d("itracker last", "" + itracker.get(itracker.size() - 1));
            Log.d("I value", i + "");
        } else
            Toast.makeText(this, "No previous line found!", Toast.LENGTH_SHORT).show();
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
            extract(p);


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

        } else if (status.equals("pause")) {
            pausevoice();
            status = "play";

            imgbut.setImageDrawable(getDrawable(R.drawable.ic_icon_player_play));

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        new MyDbHelper(edit_mode.this).insertBookStats(Constants.bookJoinTblReading.getBook_id(), Constants.bookJoinTblReading.getBook_user_id(), 0, counterReadingSecond);
        //Toast.makeText(getApplicationContext(), "Back to Pdf page", Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

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

            if (end != -1) {

                str.setSpan(new BackgroundColorSpan(Color.YELLOW), i, end + 1, 0);
                Log.d("string length", "SL " + str.length() + " I " + i + " END " + end);
                String toSpeak = parsedText.substring(i, end);


                t.setText(str);
                tempi = i;
                s.setProgress(end);
                i = end + 1;
               // Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                t1.getVoices();
                plength = end - tempi + 1;
                itracker.add(plength);
                Log.d("plength", itracker + "");
                Log.d("speaking?", t1.isSpeaking() + "");

            } else {
                i = 0;
            }

        }

    }

}
