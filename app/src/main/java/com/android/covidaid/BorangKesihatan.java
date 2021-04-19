package com.android.covidaid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BorangKesihatan extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private ProgressBar progressBarKesihatan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borang_kesihatan);

        progressBarKesihatan = (ProgressBar) findViewById(R.id.progressBarKesihatan);
        progressBarKesihatan.setVisibility(View.GONE);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();



        final TextInputEditText fullnameTVBS = (TextInputEditText) findViewById(R.id.editFullnameKesihatan);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressBarKesihatan.setVisibility(View.VISIBLE);
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null){

                    String fullname = userProfile.fullName;

                    fullnameTVBS.setText(fullname);
                    Toast.makeText(BorangKesihatan.this, "Loaded from profile", Toast.LENGTH_SHORT).show();
                    progressBarKesihatan.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BorangKesihatan.this, "database error", Toast.LENGTH_LONG).show();
            }
        });
    }
}