package com.android.covidaid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BorangBantuan extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference reference, refSumbangan;
    private FirebaseDatabase rootNode;
    private String userID;
    private TextInputLayout textFieldFullSumb, textFieldPhoneSumb, textFieldICsumb, textFieldAdrressSumb, textFieldJenisSumb;
    private Button textButtonSubmitS;
    private ProgressBar progressBarSumbangan;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borang_bantuan);

        progressBarSumbangan = (ProgressBar) findViewById(R.id.progressBarSumbangan);
        progressBarSumbangan.setVisibility(View.VISIBLE);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

//        final EditText fullnameTVBK = (TextInputEditText) findViewById(R.id.editFullname);

        textFieldFullSumb = findViewById(R.id.textFieldFullSumb);
        textFieldPhoneSumb = findViewById(R.id.textFieldPhoneSumb);
        textFieldICsumb = findViewById(R.id.textFieldICsumb);
        textFieldAdrressSumb = findViewById(R.id.textFieldAdrressSumb);
        textFieldJenisSumb = findViewById(R.id.textFieldJenisSumb);

        textButtonSubmitS = findViewById(R.id.textButtonSubmitS);



        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null){
                    String fullname = userProfile.fullName;

//                    fullnameTVBK.setText(fullname);
                    progressBarSumbangan.setVisibility(View.GONE);
                    Toast.makeText(BorangBantuan.this, "Loaded from profile", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BorangBantuan.this, "database error", Toast.LENGTH_LONG).show();
            }
        });

        textButtonSubmitS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarSumbangan.setVisibility(View.VISIBLE);

                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Sumbangan");
                refSumbangan = rootNode.getReference().child("Sumbangan");


                //Get all values
                String fullName = textFieldFullSumb.getEditText().getText().toString();
                String phoneNo = textFieldPhoneSumb.getEditText().getText().toString();
                String icNo = textFieldICsumb.getEditText().getText().toString();
                String userAddress = textFieldAdrressSumb.getEditText().getText().toString();
                String userAid = textFieldJenisSumb.getEditText().getText().toString();
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String idSumbangan = refSumbangan.push().getKey();

             UserHelperClass helperClass = new UserHelperClass(fullName, phoneNo, icNo, userAddress,userAid,uid);
                reference.setValue(helperClass);
                FirebaseDatabase.getInstance().getReference("Sumbangan").child(uid).setValue(helperClass);
                //refSumbangan.child(idSumbangan).setValue(helperClass);

                Toast.makeText(BorangBantuan.this, "Your form has been sent!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(BorangBantuan.this, HomeActivity.class));


            }
        });


    }
}