package com.android.covidaid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class statusBorang extends AppCompatActivity {

    RecyclerView recViewStatus;
    statusAdapter adapter;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_borang);

        recViewStatus = (RecyclerView) findViewById(R.id.recViewStatus);
        recViewStatus.setLayoutManager(new LinearLayoutManager(this));
        progressBar = (ProgressBar) findViewById(R.id.progressBarStatusBorang);
        progressBar.setVisibility(View.VISIBLE);
        FirebaseRecyclerOptions<UserHelperClass> options =
                new FirebaseRecyclerOptions.Builder<UserHelperClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Sumbangan"), UserHelperClass.class)
                        .build();


        adapter= new statusAdapter(options);
        recViewStatus.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);


    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();


    }
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();

    }

}