package com.example.keerat666.listviewpdfui.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.keerat666.listviewpdfui.Login;
import com.example.keerat666.listviewpdfui.MySharedPreference;
import com.example.keerat666.listviewpdfui.R;
import com.example.keerat666.listviewpdfui.db.MyDbHelper;
import com.example.keerat666.listviewpdfui.models.BookStatsGetDateHours;
import com.example.keerat666.listviewpdfui.ui.fragment.adapter.RecyclerViewHorizontalListAdapter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@SuppressLint("ValidFragment")
public class HomeFragment extends Fragment {

    AppCompatActivity activity;
    BarChart chart;
    List<BookStatsGetDateHours> bookStatsList;

    @SuppressLint("ValidFragment")
    public HomeFragment(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        Toolbar toolbar = v.findViewById(R.id.toolbarHomeFrag);
        toolbar.inflateMenu(R.menu.menu_home_fragment);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.fragment_home_sign_out:
                        MySharedPreference.setPrefLogIn(activity, false);
                        Toast.makeText(activity, getString(R.string.log_out), Toast.LENGTH_SHORT).show();

                        Intent intent=new Intent(activity, Login.class);
                        startActivity(intent);
                        activity.finish();
                        break;
                }
                return true;
            }
        });

        TextView textViewGreet = v.findViewById(R.id.tvGoodEvening);

        //setHasOptionsMenu(true);
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        String greet = "";

        if(timeOfDay >= 0 && timeOfDay < 12){
            greet = "Good Morning";
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            greet = "Good Afternoon";
        }else if(timeOfDay >= 16 && timeOfDay < 21){
            greet = "Good Evening";
        }else if(timeOfDay >= 21 && timeOfDay < 24){
            greet = "Good Night";
        }

        textViewGreet.setText(greet+"\n"+new MyDbHelper(activity).getAllUser().getUSER_NAME());
        //activity.getSupportActionBar().setTitle(greet+", "+new MyDbHelper(activity).getAllUser().getUSER_NAME());
        //activity.getSupportActionBar().setSubtitle("Continue reading!");
        activity.getSupportActionBar().hide();

        TextView totalStorage = v.findViewById(R.id.tvInternalStorage);
        TextView totalStoragePercentage = v.findViewById(R.id.tvInternalStoragePercentage);
        TextView totalStoragePercentageDown = v.findViewById(R.id.tvInternalStoragePercentageDown);
        TextView textViewNoChartData = v.findViewById(R.id.tvNoChartData);

        RecyclerView recyclerView = v.findViewById(R.id.rvMainScreen);
        // add a divider after each item for more clarity
       // recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.HORIZONTAL));

        TextView textViewRecent = v.findViewById(R.id.tvRecentHome);
        if (new MyDbHelper(activity).getAllBookWithJoinTblReading().size() == 0){
            textViewRecent.setVisibility(View.GONE);
            textViewNoChartData.setText("Seems like you don't have any ebooks, \nadd your first book to read");
        }
        RecyclerViewHorizontalListAdapter recyclerViewHorizontalListAdapter = new RecyclerViewHorizontalListAdapter(getActivity(), new MyDbHelper(activity).getAllBookWithJoinTblReading());
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        recyclerView.setAdapter(recyclerViewHorizontalListAdapter);


        BarChart chart = v.findViewById(R.id.barchart);

        String monthShortName[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        bookStatsList = new MyDbHelper(activity).getAllBookStats();

        if (bookStatsList.size() == 0){
            //totalStoragePercentage.setText("0 min");
            ((TextView)v.findViewById(R.id.tvContinueReading)).setVisibility(View.GONE);
            ((TextView)v.findViewById(R.id.tvMinutesConstantString)).setVisibility(View.GONE);
            ((TextView)v.findViewById(R.id.tvTodayReadingTime)).setVisibility(View.GONE);
            totalStoragePercentage.setVisibility(View.GONE);
            totalStoragePercentageDown.setVisibility(View.GONE);

        }else if (bookStatsList.size() == 1){
            int totalMin = bookStatsList.get(bookStatsList.size()-1).getTotalTime()/60;
            int hours = totalMin / 60; //since both are ints, you get an int
            int minutes = totalMin % 60;
            if (hours == 0){
                totalStoragePercentage.setText(minutes + "min");
                ((TextView)v.findViewById(R.id.tvTodayReadingTime)).setText(""+minutes);
            }else {
                totalStoragePercentage.setText("" + hours + "h:" + minutes + "min");
                ((TextView)v.findViewById(R.id.tvTodayReadingTime)).setText(hours+":"+minutes);
            }
            totalStoragePercentageDown.setVisibility(View.GONE);
            totalStoragePercentage.setText(String.format("%.2f", ((float)totalMin/1440)*100)+""+"%");

        }else{
            int totalMin = bookStatsList.get(bookStatsList.size()-1).getTotalTime()/60;
            int hours = totalMin / 60; //since both are ints, you get an int
            int minutes = totalMin % 60;

            int totalMin1 = bookStatsList.get(bookStatsList.size()-2).getTotalTime()/60;
            int hours1 = totalMin1 / 60; //since both are ints, you get an int
            int minutes1 = totalMin1 % 60;

            int readStat = totalMin - totalMin1;
            //Toast.makeText(getActivity(), ""+readStat, Toast.LENGTH_LONG).show();
            int hours2 = readStat / 60; //since both are ints, you get an int
            int minutes2 = readStat % 60;

            if (readStat<0){
                totalStoragePercentageDown.setVisibility(View.VISIBLE);
                totalStoragePercentage.setVisibility(View.GONE);

                float todayPerc = (float) totalMin/100;
                float yesterPerc = (float) totalMin1/100;
                totalStoragePercentageDown.setText(String.format("%.02f", todayPerc - yesterPerc)+""+"%");
             /*   if (hours2 == 0){
                    totalStoragePercentageDown.setText(minutes2 + "min");
                }else {
                    String time = hours2 + "h:" + minutes2 + "min";
                    totalStoragePercentageDown.setText(""+time.replace("-",""));
                }
              */
            }else {

                totalStoragePercentage.setVisibility(View.VISIBLE);
                totalStoragePercentageDown.setVisibility(View.GONE);

                float todayPerc = (float) totalMin/100;
                float yesterPerc = (float) totalMin1/100;
                totalStoragePercentage.setText(String.format("%.02f", todayPerc - yesterPerc)+""+"%");
              /*  if (hours2 == 0){
                    totalStoragePercentage.setText(""+minutes2 + "min");
                }else {
                    totalStoragePercentage.setText("" + hours2 + "h:" + minutes2 + "min");
                }
                */
            }

            if (hours == 0){
                //totalStoragePercentage.setText(minutes + "min");
                ((TextView)v.findViewById(R.id.tvTodayReadingTime)).setText(""+minutes);
            }else {
                //totalStoragePercentage.setText("" + hours + "h:" + minutes + "min");
                ((TextView)v.findViewById(R.id.tvTodayReadingTime)).setText(hours+":"+minutes);
            }
        }

        final List<BarEntry> entries = new ArrayList<>();
        ArrayList<String> xVals = new ArrayList<>();

        int last_date_of_chart = 0;
        int last_month_of_chart = 0;
        int last_date_of_month_cal = 0;

        if (bookStatsList.size() > 0 && bookStatsList.size() < 5){

            String dateSplit[] = bookStatsList.get(0).getDate().split("-");
            last_date_of_chart = Integer.parseInt(dateSplit[2]);
            last_month_of_chart = Integer.parseInt(dateSplit[1]) - 1;

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.MONTH, last_month_of_chart);
            last_date_of_month_cal =  calendar.getActualMaximum(Calendar.DATE);
        }

        //Toast.makeText(activity, last_date_of_chart+"/"+last_month_of_chart, Toast.LENGTH_SHORT).show();

        int listMax = 0;

        if (bookStatsList.size() == 1) {
            listMax = 4;
        } else if (bookStatsList.size() == 2) {
            listMax = 3;
        } else if (bookStatsList.size() == 3) {
            listMax = 2;
        } else if (bookStatsList.size() == 4) {
            listMax = 1;
        }


        if (bookStatsList.size() > 0 && bookStatsList.size() < 5) {

            final List<BarEntry> entries1 = new ArrayList<>();
            ArrayList<String> xVals1 = new ArrayList<>();

            for (int i = 0; i < listMax; i++) {
                entries1.add(new BarEntry(i, 0));
                if (last_date_of_chart == 1){
                    last_date_of_chart = last_date_of_month_cal;
                    last_date_of_chart--;
                    xVals1.add(""+last_date_of_chart+monthShortName[last_month_of_chart-1]);
                }else {
                    last_date_of_chart--;
                    xVals1.add(""+last_date_of_chart+monthShortName[last_month_of_chart]);
                }
            }

            for (int i = listMax -1; i >= 0; i--) {
                entries.add(entries1.get(i));
                xVals.add(xVals1.get(i));
            }

            int tmpListMax = listMax;
            for (int i=0; i<bookStatsList.size(); i++) {
                int totalMin = bookStatsList.get(i).getTotalTime()/60;
                entries.add(new BarEntry(tmpListMax, totalMin));

                String dateSplit[] = bookStatsList.get(i).getDate().split("-");
                xVals.add(""+dateSplit[2]+monthShortName[Integer.parseInt(dateSplit[1])-1]);
                tmpListMax++;
            }
        }

        //entries.add(new BarEntry(5, 5));
        //xVals.add("He");

        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xVals));
        chart.getAxisLeft().setEnabled(false);
        chart.getAxisRight().setEnabled(false);

        if (entries.size() > 6) {
            chart.zoom(1.4f, 0, 1.4f, 0);
        }
        chart.setMaxVisibleValueCount(6);
        chart.moveViewToX(bookStatsList.size());

        XAxis xAxis = chart.getXAxis();
        xAxis.setTextSize(12f);
        xAxis.setLabelCount(entries.size());
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);

        BarDataSet set = new BarDataSet(entries, "Daily Reading Hours");
        BarData data = new BarData(set);

        final int finalListMax = listMax;

        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getBarLabel(BarEntry barEntry) {
                if (barEntry.getX() >= finalListMax) {
                    int totalMin = bookStatsList.get((int) barEntry.getX() - finalListMax).getTotalTime()/60;

                    int hours = totalMin / 60; //since both are ints, you get an int
                    int minutes = totalMin % 60;
                    if (hours == 0){
                        return minutes + "min";
                    }else {
                        return "" + hours + "h:" + minutes + "m";
                    }
                }else {
                    return "";
                }
            }
        });


        if (entries.size() == 1) {
            data.setBarWidth(0.1f); // set custom bar width
        }
        else if (entries.size() == 2) {
            data.setBarWidth(0.2f); // set custom bar width
        }
        else if (entries.size() == 3) {
            data.setBarWidth(0.3f); // set custom bar width
        }
        else if (entries.size() == 4) {
            data.setBarWidth(0.4f); // set custom bar width
        }
        else {
            data.setBarWidth(0.5f); // set custom bar width
        }

        data.setValueTextSize(12f);

        if (entries.size()>0) {
            textViewNoChartData.setVisibility(View.GONE);
        }else if (new MyDbHelper(activity).getAllBookWithJoinTblReading().size() != 0){
            textViewNoChartData.setText("What would you like to read today?");
        }

        chart.setData(data);
        chart.getLegend().setEnabled(false);//.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        chart.animateY(1000);
        chart.getDescription().setEnabled(false);
        chart.setFitBars(false);
        chart.invalidate();




        try{
            StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
            long bytesAvailable = (long)stat.getBlockSize() *(long)stat.getBlockCount();
            long megAvailable = bytesAvailable / 1048576;
            Log.e("","Available MB : "+megAvailable);

            File path = Environment.getDataDirectory();
            StatFs stat2 = new StatFs(path.getPath());
            long blockSize = stat2.getBlockSize();
            long availableBlocks = stat2.getAvailableBlocks();
            String format =  Formatter.formatFileSize(getActivity(), availableBlocks * blockSize);
            Log.e("","Format : "+format);

          //  totalStorage.setText(""+megAvailable);
            String formatTotal = Formatter.formatFileSize(getActivity(), getTotalInternalMemorySize());
            //totalStorage.setText(""+formatTotal);

            int avail = Integer.parseInt(format.replaceAll("[^0-9]", ""));
            int total = Integer.parseInt(formatTotal.replaceAll("[^0-9]", ""));
            int percent = ((avail*100)/total);

            //totalStoragePercentage.setText(percent+"%");

        }catch (Exception e){
            Toast.makeText(getActivity(), "frag home error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return v;
    }

    static public long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }
}
