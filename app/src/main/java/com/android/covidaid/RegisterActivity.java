package com.android.covidaid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private Button textButtonRegister;
    private TextInputLayout textFieldEmail, textFieldName, textFieldConfirmPasswordR, textFieldPasswordR;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        mAuth = FirebaseAuth.getInstance();

        textButtonRegister = (Button) findViewById(R.id.textButtonRegister);
        textButtonRegister.setOnClickListener(this);

        textFieldEmail = findViewById(R.id.textFieldEmail);
        textFieldName = findViewById(R.id.textFieldName);
        textFieldPasswordR = findViewById(R.id.textFieldPasswordR);
        textFieldConfirmPasswordR = findViewById(R.id.textFieldConfirmPasswordR);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textButtonRegister:
                textButtonRegister();
                break;
        }
    }

    private void textButtonRegister() {
        String email = textFieldEmail.getEditText().getText().toString().trim();
        String fullname = textFieldName.getEditText().getText().toString().trim();
        String password = textFieldPasswordR.getEditText().getText().toString().trim();
        String confirmpassword = textFieldConfirmPasswordR.getEditText().getText().toString().trim();
        String type = "normal";

        if (email.isEmpty()) {
            textFieldEmail.setError("Email is required!");
            textFieldEmail.requestFocus();
            return ;
        }

        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            textFieldEmail.setError("Please insert valid email!");
            textFieldEmail.requestFocus();
            return ;
        }
        else
            textFieldEmail.setError(null);

        if (fullname.isEmpty()) {
            textFieldName.setError("Please insert your name");
            textFieldName.requestFocus();
            return ;
        }
        else
            textFieldName.setError(null);

         if (password.isEmpty()) {
            textFieldPasswordR.setError("Password is required");
             textFieldPasswordR.requestFocus();
            return ;
        }

         else if (password.length()<6) {
             textFieldPasswordR.setError("Password must 6 character length");
             textFieldPasswordR.requestFocus();
             return;
         }
         else
             textFieldPasswordR.setError(null);

        if (!confirmpassword.equals(textFieldPasswordR.getEditText().getText().toString().trim())) {
            textFieldConfirmPasswordR.setError("Password is not same");
            textFieldConfirmPasswordR.requestFocus();
            return ;
        }
        else
            textFieldConfirmPasswordR.setError(null);


        progressBar.setVisibility(View.VISIBLE);
         mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()){
                        User user = new User(fullname, email, type);
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(RegisterActivity.this, "User has been registered succesfully! Please check your email to verify your account!", Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                                    //redirect login
                                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                    }else {
                                    Toast.makeText(RegisterActivity.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });


                    }else{
                        Toast.makeText(RegisterActivity.this, "Failed to register! Email address already taken!", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
    }


}