package com.android.covidaid;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class statusBorangKendiri extends AppCompatActivity {

    TextView username,date,ditolak;
    ProgressBar progressBar;
    DatabaseReference databaseReference;
    CardView progressCountParent, progressCountParent2,progressCountParent3;
    Toolbar toolbar1;
    ImageView imgwrong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_borang_kendiri);

        progressCountParent = findViewById(R.id.progressCountParent);
        progressCountParent2 = findViewById(R.id.progressCountParent2);
        progressCountParent3 = findViewById(R.id.progressCountParent3);
        ditolak = findViewById(R.id.textView7);
        imgwrong = findViewById(R.id.truetick);
        date = findViewById(R.id.dateTv);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Sumbangan");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    if (dataSnapshot.child("uid").getValue().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        if (dataSnapshot.child("status").getValue().equals("dihantar")){
                            progressCountParent.setCardBackgroundColor(Color.parseColor("#32CD32"));
                            date.setText(dataSnapshot.child("date").getValue(String.class));
                        } else if (dataSnapshot.child("status").getValue().equals("diterima")){
                            progressCountParent.setCardBackgroundColor(Color.parseColor("#32CD32"));
                            progressCountParent2.setCardBackgroundColor(Color.parseColor("#32CD32"));
                            progressCountParent3.setCardBackgroundColor(Color.parseColor("#32CD32"));
                            date.setText(dataSnapshot.child("date").getValue(String.class));
                        }
                        else if (dataSnapshot.child("status").getValue().equals("ditolak")){
                            progressCountParent.setCardBackgroundColor(Color.parseColor("#e50000"));
                            imgwrong.setImageResource(R.drawable.ic_baseline_remove_24);
                            ditolak.setText("Borang ditolak");
                            date.setText(dataSnapshot.child("date").getValue(String.class));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("Sumbangan").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        username = findViewById(R.id.userName);
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                username.setText(snapshot.child("fullName").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        progressBar = (ProgressBar) findViewById(R.id.progressBarStatusBorang);
//        progressBar.setVisibility(View.VISIBLE);


        toolbar1 = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
//
//        databaseReference = FirebaseDatabase.getInstance().getReference().child("Sumbangan");
//        Query query = databaseReference.orderByChild("uid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());

//        progressBar.setVisibility(View.GONE);
    }
}