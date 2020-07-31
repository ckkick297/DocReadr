package com.example.keerat666.listviewpdfui;

import android.app.Activity;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.keerat666.listviewpdfui.db.MyDbHelper;
import com.example.keerat666.listviewpdfui.models.Book;
import com.example.keerat666.listviewpdfui.models.BookReadingCurrentStatus;

import java.util.ArrayList;
import java.util.List;

public class MyListView extends ArrayAdapter<String> {

    private Activity context;
    ArrayList<String> maintitle = new ArrayList<>();
    ArrayList<String> subtitle = new ArrayList<>();
    ArrayList<Integer> imgid = new ArrayList<>();
    ArrayList<Integer> imgid2 = new ArrayList<>();
    ArrayList<String> path = new ArrayList<>();
    ArrayList<String> subtitle2 = new ArrayList<>();
  ArrayList<String>progressDialog= new ArrayList<>();


    public List<Book> booksList;

    public MyListView(Activity context, ArrayList<String> maintitle,
                      ArrayList<String> subtitle, ArrayList<Integer> imgid, ArrayList<Integer> imgid2,
                      ArrayList<String> path, ArrayList<String> subtitle2){

        super(context, R.layout.mylist, maintitle);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.maintitle = maintitle;
        this.subtitle = subtitle;
        this.imgid = imgid;
        this.imgid2 = imgid2;
        this.path = path;
        this.subtitle2 = subtitle2;
    //    this.progressDialog = ProgressBar;

        this.booksList = Constants.booksList;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View rowView = inflater.inflate(R.layout.mylist, null, true);

        try {
          //  long timeSec= booksList.get(k).getBook_length()/3;
          //  int hours = (int) timeSec/ 3600;
          //  int temp = (int) timeSec- hours * 3600;
          //  int mins = temp / 60;
         //   temp = temp - mins * 60;
         //   int secs = temp

           // TextView textViewWordCount = rowView.findViewById(R.id.tvWordCount);
           // textViewWordCount.setText(words+" words");
        }catch (Exception e){

        }
        TextView titleText = (TextView) rowView.findViewById(R.id.title);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView subtitleText = (TextView) rowView.findViewById(R.id.subtitle);
        TextView subtitleText2 = (TextView) rowView.findViewById(R.id.subtitle4);
       ProgressBar progressDialog = (ProgressBar) rowView.findViewById(R.id.progress);
        ImageView imageView2 = (ImageView) rowView.findViewById(R.id.icon2);
        titleText.setText(maintitle.get(position));
        imageView.setImageResource(imgid.get(position));
        imageView2.setImageResource(imgid2.get(position));
        subtitleText.setText(subtitle.get(position));

        subtitleText2.setText(subtitle2.get(position));

        //Toast.makeText(getContext(), "subtitle :"+subtitleText.getText().toString()+"\n subtitle2 :"+subtitleText2.getText().toString(), Toast.LENGTH_LONG).show();
        if (booksList.size()>0 && position < booksList.size()) {
            //progressDialog.setMax(booksList.get(position).getBook_length());//95);

            List<BookReadingCurrentStatus> bookReadingCurrentStatuses = new MyDbHelper(context).getAllBookReadingCurrentStatus();
            for (int i = 0; i < bookReadingCurrentStatuses.size(); i++) {
                if (booksList.get(position).getBook_id() == bookReadingCurrentStatuses.get(i).getReading_book_id()) {
                    if (booksList.get(position).getBook_type().equals("txt")) {
                        progressDialog.setMax(booksList.get(position).getBook_length());//95);
                        progressDialog.setProgress(bookReadingCurrentStatuses.get(i).getReading_word_number());
                       // Toast.makeText(context, "position :" + position + "\n book type :" + booksList.get(position).getBook_type(), Toast.LENGTH_SHORT).show();
                    }
                    if (booksList.get(position).getBook_type().equals("pdf")) {
                        progressDialog.setMax(booksList.get(position).getBook_page_count());//95);
                        progressDialog.setProgress(bookReadingCurrentStatuses.get(i).getReading_page_number());
                        //Toast.makeText(context, "position :" + position + "\n book type :" + booksList.get(position).getBook_type(), Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            }
        }else {
            progressDialog.setProgress(95);
        }
        return rowView;


    }

    ;
}