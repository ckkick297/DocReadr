package com.example.keerat666.listviewpdfui.ui.fragment.adapter;

import android.app.Activity;
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
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.keerat666.listviewpdfui.Constants;
import com.example.keerat666.listviewpdfui.PDFViewer;
import com.example.keerat666.listviewpdfui.R;
import com.example.keerat666.listviewpdfui.TxtMaker;
import com.example.keerat666.listviewpdfui.models.BookJoinTblReading;

import java.io.File;
import java.util.List;

public class RecyclerViewHorizontalListAdapter extends RecyclerView.Adapter<RecyclerViewHorizontalListAdapter.MyViewHolder>{

    Activity context;
    List<BookJoinTblReading> list;

    public RecyclerViewHorizontalListAdapter(Activity context, List<BookJoinTblReading> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_home_recycler_view_item_horizontal, parent, false);
        MyViewHolder gvh = new MyViewHolder(groceryProductView);
        return gvh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if (list.get(position).getBook_type().equals("pdf")) {
            try {

                holder.imageView.setImageResource(R.drawable.pdfnew);
                //holder.imageView.setImageResource(R.drawable.download1);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        File file = new File(list.get(position).getBook_filepath());
                        try{


                            int REQ_WIDTH = holder.imageView.getWidth();
                            int REQ_HEIGHT = holder.imageView.getHeight();


                            Bitmap bitmap = Bitmap.createBitmap(REQ_WIDTH, REQ_HEIGHT, Bitmap.Config.ARGB_4444);

                            PdfRenderer renderer = new PdfRenderer(ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY));

                            //if (currentPage < 0){
                            //  currentPage = 0;
                            //}else if (currentPage > renderer.getPageCount()) {
                            //  currentPage = renderer.getPageCount() - 1;
                            // }
                            Matrix m = holder.imageView.getImageMatrix();
                            Rect rect =new Rect(0, 0, REQ_WIDTH, REQ_HEIGHT);
                            renderer.openPage(0).render(bitmap, rect, m, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                            holder.imageView.setImageMatrix(m);
                            holder.imageView.setImageBitmap(bitmap);
                            holder.imageView.invalidate();


                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 100);

            } catch (Exception e) {
               // Toast.makeText(context, "RecycHoriError file image :" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        else if (list.get(position).getBook_type().equals("htm")){
            holder.imageView.setImageResource(R.drawable.www);
        }else {
            holder.imageView.setImageResource(R.drawable.pdf);
        }

        holder.txtview.setText(list.get(position).getBook_name());

       // holder.imageView.setImageResource(R.drawable.download1);
        //holder.txtview.setVisibility(View.GONE);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Constants.bookJoinTblReading = list.get(position);

                if (list.get(position).getBook_type().equals("pdf")) {
                    Intent i = new Intent(context, PDFViewer.class);
                    i.putExtra("Pathid", list.get(position).getBook_filepath());
                    i.putExtra("current_page", list.get(position).getReading_page_number());
                    context.startActivity(i);
                }
           //     if (list.get(position).getBook_type().equals("txt")) {
             else {       Intent i = new Intent(context, TxtMaker.class);
                    i.putExtra("txtpath", list.get(position).getBook_filepath());
                    context.startActivity(i);
                }
                context.overridePendingTransition(R.anim.left_to_right, R.anim.right_left_anim);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView txtview;

        public MyViewHolder(View view) {
            super(view);
            imageView=view.findViewById(R.id.ivBook);
            txtview=view.findViewById(R.id.tvBookTitle);
        }
    }
}