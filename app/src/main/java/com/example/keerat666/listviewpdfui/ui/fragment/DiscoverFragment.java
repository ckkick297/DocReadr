package com.example.keerat666.listviewpdfui.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.keerat666.listviewpdfui.Login;
import com.example.keerat666.listviewpdfui.MySharedPreference;
import com.example.keerat666.listviewpdfui.R;

@SuppressLint("ValidFragment")
public class DiscoverFragment extends Fragment {

    AppCompatActivity appCompatActivity;

    public DiscoverFragment(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_discover, container, false);

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuNight: {
                Toast.makeText(appCompatActivity, "Night Mode status changed.", Toast.LENGTH_SHORT).show();
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

            case R.id.menu_main_sign_out:
                MySharedPreference.setPrefLogIn(appCompatActivity, false);
                Toast.makeText(appCompatActivity, getString(R.string.log_out), Toast.LENGTH_SHORT).show();

                Intent intent=new Intent(appCompatActivity, Login.class);
                startActivity(intent);
                appCompatActivity.finish();
                break;

        }
        return true;
    }
}
