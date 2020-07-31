package com.example.keerat666.listviewpdfui.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.keerat666.listviewpdfui.R;
import com.example.keerat666.listviewpdfui.db.MyDbHelper;
import com.example.keerat666.listviewpdfui.models.BookFolder;
import com.example.keerat666.listviewpdfui.models.BookJoinTblReading;
import com.example.keerat666.listviewpdfui.ui.fragment.adapter.RvBookFragmentAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class BookFragment extends Fragment {

    AppCompatActivity activity;
    RecyclerView recyclerView;
    TabLayout tabLayout;
    RvBookFragmentAdapter adapter;
    TextView textViewEmptyList;
    int tabPosition;

    List<BookFolder> folderList;

    public BookFragment(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book, container, false);

       // Toolbar toolbar = view.findViewById(R.id.toolbarBookFragment);
       // activity.setSupportActionBar(toolbar);
       // activity.getSupportActionBar().setTitle("Books");

        setHasOptionsMenu(true);

        textViewEmptyList = view.findViewById(R.id.tvEmptyBookFragment);

        recyclerView = view.findViewById(R.id.rvBookFragment);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));

        adapter = new RvBookFragmentAdapter(activity, new MyDbHelper(activity).getAllBookWithJoinTblReading(), textViewEmptyList);
        recyclerView.setAdapter(adapter);

        folderList = new MyDbHelper(activity).getAllBookFolder();
        tabLayout = view.findViewById(R.id.tabLayoutBookFragment);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
               if (tab.getPosition() == 0){
                   adapter = new RvBookFragmentAdapter(activity, new MyDbHelper(activity).getAllBookWithJoinTblReading() , textViewEmptyList);
                   recyclerView.setAdapter(adapter);
               }else {
                   adapter = new RvBookFragmentAdapter(activity, new MyDbHelper(activity).getAllBookWithJoinTblReading(folderList.get(tab.getPosition()).getFolder_id()), textViewEmptyList);
                   recyclerView.setAdapter(adapter);
               }

               tabPosition = tab.getPosition();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        if (folderList.size() != 0){
            for (int i=0; i<folderList.size(); i++)
                tabLayout.addTab(tabLayout.newTab().setText(folderList.get(i).getFolder_name()));
        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_book_fragment, menu);

        MenuItem searchViewItem = menu.findItem(R.id.menu_book_fragment_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchView.setQueryHint("Search ebooks..");
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabLayout.setVisibility(View.GONE);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (tabPosition == 0){
                    adapter.filter(new MyDbHelper(activity).getAllBookWithJoinTblReading());
                }else {
                    adapter.filter(new MyDbHelper(activity).getAllBookWithJoinTblReading(folderList.get(tabPosition).getFolder_id()));
                }

                List<BookJoinTblReading> list = new ArrayList<>();
                for (BookJoinTblReading bookJoinTblReading : adapter.getAdapterList()){
                    if (bookJoinTblReading.getBook_name().toLowerCase().contains(s)){
                        list.add(bookJoinTblReading);
                    }
                }

                adapter.filter(list);
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                tabLayout.setVisibility(View.VISIBLE);
                if (tabPosition == 0){
                    adapter = new RvBookFragmentAdapter(activity, new MyDbHelper(activity).getAllBookWithJoinTblReading() , textViewEmptyList);
                    recyclerView.setAdapter(adapter);
                }else {
                    adapter = new RvBookFragmentAdapter(activity, new MyDbHelper(activity).getAllBookWithJoinTblReading(folderList.get(tabPosition).getFolder_id()), textViewEmptyList);
                    recyclerView.setAdapter(adapter);
                }
                //Toast.makeText(getActivity(), "Search Closed tab pos"+tabPosition, Toast.LENGTH_SHORT).show();
                return false;
            }
        });


        /** Necessary to being here**/
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

          /*  case R.id.menu_book_fragment_sign_out:
                MySharedPreference.setPrefLogIn(activity, false);
                Toast.makeText(activity, getString(R.string.log_out), Toast.LENGTH_SHORT).show();

                Intent intent=new Intent(activity, Login.class);
                startActivity(intent);
                activity.finish();
                break;
*/
        }
        return true;
    }
}
