package com.example.keerat666.listviewpdfui.ui.fragment.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.pdf.PdfRenderer;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.keerat666.listviewpdfui.Constants;
import com.example.keerat666.listviewpdfui.PDFViewer;
import com.example.keerat666.listviewpdfui.R;
import com.example.keerat666.listviewpdfui.TxtMaker;
import com.example.keerat666.listviewpdfui.db.MyDbHelper;
import com.example.keerat666.listviewpdfui.models.BookJoinTblReading;
import com.example.keerat666.listviewpdfui.models.BookNotesJoinBook;

import java.io.File;
import java.util.List;

public class RvBookmarkFragmentAdapter extends RecyclerView.Adapter<RvBookmarkFragmentAdapter.MyViewHolder> {

    List<BookNotesJoinBook> list;
    TextView textViewEmpty;

    public RvBookmarkFragmentAdapter(List<BookNotesJoinBook> list, TextView textView) {
        this.list = list;
        this.textViewEmpty = textView;
        isEmptyList();
    }

    private void isEmptyList(){
        if (list.size() != 0){
            textViewEmpty.setVisibility(View.GONE);
        }else {
            textViewEmpty.setVisibility(View.VISIBLE);
        }
    }

    public List<BookNotesJoinBook> getAdapterList(){
        return list;
    }

    public void filter(List<BookNotesJoinBook> list){
        this.list = list;
        notifyDataSetChanged();
        isEmptyList();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.fragment_bookmark_recycler_view_jtems, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final Context context = holder.itemView.getContext();

        try {
            if (list.get(position).getBook_type().equals("pdf")) {
                holder.imageViewBookType.setImageResource(R.drawable.pdfnew);
                holder.progressBar.setMax(list.get(position).getBook_page_count());
                holder.progressBar.setProgress(list.get(position).getNote_page_number());
            }
            if (list.get(position).getBook_type().equals("htm")) {
                holder.imageViewBookType.setImageResource(R.drawable.www);
                holder.progressBar.setMax(list.get(position).getBook_length());
                holder.progressBar.setProgress(list.get(position).getNote_page_number());
            }
            if (list.get(position).getBook_type().equals("txt")) {
                holder.imageViewBookType.setImageResource(R.drawable.pdf);
                holder.progressBar.setMax(list.get(position).getBook_length());
                holder.progressBar.setProgress(list.get(position).getNote_page_number());
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    File file = new File(list.get(position).getBook_filepath());
                    try{


                        int REQ_WIDTH = holder.imageViewBookType.getWidth();
                        int REQ_HEIGHT = holder.imageViewBookType.getHeight();


                        Bitmap bitmap = Bitmap.createBitmap(REQ_WIDTH, REQ_HEIGHT, Bitmap.Config.ARGB_4444);

                        PdfRenderer renderer = new PdfRenderer(ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY));

                        //if (currentPage < 0){
                        //  currentPage = 0;
                        //}else if (currentPage > renderer.getPageCount()) {
                        //  currentPage = renderer.getPageCount() - 1;
                        // }
                        Matrix m = holder.imageViewBookType.getImageMatrix();
                        Rect rect =new Rect(0, 0, REQ_WIDTH, REQ_HEIGHT);
                        renderer.openPage(0).render(bitmap, rect, m, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                        holder.imageViewBookType.setImageMatrix(m);
                        holder.imageViewBookType.setImageBitmap(bitmap);
                        holder.imageViewBookType.invalidate();


                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 100);

            try {
                holder.textViewTitle.setText(list.get(position).getBook_name().replace(".pdf", ""));
            }catch (Exception e){}

            holder.textViewPages.setText("Page "+(list.get(position).getNote_page_number())+" of "+list.get(position).getBook_page_count());

            holder.textViewWords.setText(new MyDbHelper(context).getFolderName(list.get(position).getBook_folder_id())+" | "+list.get(position).getBook_lang().substring(0, list.get(position).getBook_lang().indexOf(" ")));
            //holder.textViewWords.setVisibility(View.GONE);

            long timeSec = list.get(position).getBook_length() / 3;
            int hours = (int) timeSec / 3600;
            int temp = (int) timeSec - hours * 3600;
            int mins = temp / 60;
            temp = temp - mins * 60;
            int secs = temp;

            //holder.textViewTime.setText(hours + ":" + mins + ":" + secs+" time");
            if (list.get(position).getNote_text().length() != 0)
                holder.textViewTime.setText("\""+list.get(position).getNote_text()+"\"");
            else
                holder.textViewTime.setVisibility(View.GONE);
            //holder.textViewTime.setVisibility(View.GONE);
        }catch (Exception e){
            //Toast.makeText(context, "Error :"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BookJoinTblReading bookJoinTblReading = new BookJoinTblReading();

                bookJoinTblReading.setBook_book_id(list.get(position).getBook_book_id());
                bookJoinTblReading.setBook_cover(list.get(position).getBook_cover());
                bookJoinTblReading.setBook_date_accessed(list.get(position).getBook_date_accessed());
                bookJoinTblReading.setBook_date_created(list.get(position).getBook_date_created());

                bookJoinTblReading.setBook_filepath(list.get(position).getBook_filepath());
                bookJoinTblReading.setBook_folder_id(list.get(position).getBook_folder_id());
                bookJoinTblReading.setBook_id(list.get(position).getBook_id());
                bookJoinTblReading.setBook_id_global(list.get(position).getBook_id_global());

                bookJoinTblReading.setBook_is_active(list.get(position).getBook_is_active());
                bookJoinTblReading.setBook_is_deleted(list.get(position).getBook_is_deleted());
                bookJoinTblReading.setBook_lang(list.get(position).getBook_lang());
                bookJoinTblReading.setBook_length(list.get(position).getBook_length());

                bookJoinTblReading.setBook_name(list.get(position).getBook_name());
                bookJoinTblReading.setBook_page_count(list.get(position).getBook_page_count());
                bookJoinTblReading.setBook_type(list.get(position).getBook_type());
                bookJoinTblReading.setBook_user_id(list.get(position).getBook_user_id());

                bookJoinTblReading.setReading_book_id(list.get(position).getNote_book_id());
                bookJoinTblReading.setReading_date_created(list.get(position).getNote_date_created());
                bookJoinTblReading.setReading_id(list.get(position).getNote_id());
                bookJoinTblReading.setReading_page_number(list.get(position).getNote_page_number());

                bookJoinTblReading.setReading_user_id(list.get(position).getNote_user_id());

                Constants.bookJoinTblReading = bookJoinTblReading;

                if (list.get(position).getBook_type().equals("pdf")) {
                    Intent i = new Intent(context, PDFViewer.class);
                    i.putExtra("Pathid", list.get(position).getBook_filepath());
                    i.putExtra("current_page", list.get(position).getNote_page_number());
                    context.startActivity(i);
                }
                //if (list.get(position).getBook_type().equals("txt")) {
                  else {
                      Intent i = new Intent(context, TxtMaker.class);
                    i.putExtra("txtpath", list.get(position).getBook_filepath());
                    context.startActivity(i);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("Do you want to Delete this book?");
                alertDialogBuilder.setPositiveButton("Delete",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                new MyDbHelper(context).deleteBook(list.get(position).getBook_id());
                                list.remove(position);
                                notifyDataSetChanged();
                                isEmptyList();
                                Toast.makeText(context,"Book deleted",Toast.LENGTH_LONG).show();
                            }
                        });

                alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageViewBookType;
        TextView textViewTitle;
        TextView textViewPages;
        TextView textViewWords;
        TextView textViewTime;
        ProgressBar progressBar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewBookType = itemView.findViewById(R.id.ivBookTypeBookFragment);
            textViewTitle= itemView.findViewById(R.id.tvTitleBookFragment);
            textViewPages = itemView.findViewById(R.id.tvPagesBookFragment);
            textViewWords = itemView.findViewById(R.id.tvWordsBookFragment);
            textViewTime = itemView.findViewById(R.id.tvTimeBookFragment);
            progressBar = itemView.findViewById(R.id.pbBookFragment);
        }
    }
}
