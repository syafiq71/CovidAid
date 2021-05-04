package com.android.covidaid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.FrameLayout;

public class MainStatusActivity extends AppCompatActivity {

    FrameLayout container;
    private Fragment covidstatusfragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_status);

        container = findViewById(R.id.container);

        final Fragment mainFragment = new covidStatusFragment();

        if (savedInstanceState == null) {
            FragmentTransaction fragmentManager = getSupportFragmentManager().beginTransaction();
            fragmentManager
                    .setReorderingAllowed(true)
                    .replace(R.id.container, mainFragment, null)
                    .addToBackStack(null)
                    .commit();
        }
    }
}

