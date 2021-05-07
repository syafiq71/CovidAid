package com.android.covidaid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class statusBorangKendiri extends AppCompatActivity {

    RecyclerView recViewStatus;
    statusAdapter adapter;
    ProgressBar progressBar;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_borang_kendiri);

        recViewStatus = (RecyclerView) findViewById(R.id.recViewStatus);
        recViewStatus.setLayoutManager(new LinearLayoutManager(this));
        progressBar = (ProgressBar) findViewById(R.id.progressBarStatusBorang);
        progressBar.setVisibility(View.VISIBLE);


        databaseReference = FirebaseDatabase.getInstance().getReference().child("Sumbangan");
        Query query = databaseReference.orderByChild("uid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());

        FirebaseRecyclerOptions<UserHelperClass> options =
                new FirebaseRecyclerOptions.Builder<UserHelperClass>()
                        .setQuery(query,UserHelperClass.class)
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