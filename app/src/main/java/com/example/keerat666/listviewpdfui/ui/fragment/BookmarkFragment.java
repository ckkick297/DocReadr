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
import com.example.keerat666.listviewpdfui.models.BookNotesJoinBook;
import com.example.keerat666.listviewpdfui.ui.fragment.adapter.RvBookmarkFragmentAdapter;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class BookmarkFragment extends Fragment {

    AppCompatActivity activity;
    RecyclerView recyclerView;
    TextView textViewEmptyList;
    RvBookmarkFragmentAdapter adapter;

    public BookmarkFragment(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);
    //    Toolbar toolbar = view.findViewById(R.id.toolbarBookmarkFragment);
//        activity.setSupportActionBar(toolbar);
  //      activity.getSupportActionBar().setTitle("Bookmarks");

        setHasOptionsMenu(true);

        textViewEmptyList = view.findViewById(R.id.tvEmptyBookmarkFragment);
        recyclerView = view.findViewById(R.id.rvBookmarkFragment);

        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new RvBookmarkFragmentAdapter(new MyDbHelper(activity).getAllBookNotesJoinBook(), textViewEmptyList);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_bookmark_fragment, menu);

        MenuItem searchViewItem = menu.findItem(R.id.menu_bookmark_fragment_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchView.setQueryHint("Search ebooks..");
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                adapter.filter(new MyDbHelper(activity).getAllBookNotesJoinBook());

                List<BookNotesJoinBook> list = new ArrayList<>();
                for (BookNotesJoinBook bookNotesJoinBook : adapter.getAdapterList()){
                    if (bookNotesJoinBook.getBook_name().toLowerCase().contains(s)){
                        list.add(bookNotesJoinBook);
                    }
                }

                adapter.filter(list);
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                return false;
            }
        });


        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

         /*   case R.id.menu_bookmark_fragment_sign_out:
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
