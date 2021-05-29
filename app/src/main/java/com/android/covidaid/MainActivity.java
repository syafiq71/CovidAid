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
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button textButtonLogin;
    private TextView register;
    private TextInputLayout textFieldEmail, textFieldPassword;
    private ProgressBar progressBarLogin;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register = (TextView) findViewById(R.id.tvCreate);
        register.setOnClickListener(this);

        textButtonLogin = (Button) findViewById(R.id.textButtonLogin);
        textButtonLogin.setOnClickListener(this);

        textFieldEmail = findViewById(R.id.textFieldEmail);
        textFieldPassword = findViewById(R.id.textFieldPassword);

        progressBarLogin = (ProgressBar) findViewById(R.id.progressBarLogin);
        progressBarLogin.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvCreate:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
                
            case R.id.textButtonLogin:
                userLogin();
                    break;

        }

    }

    private void userLogin() {
        String email = textFieldEmail.getEditText().getText().toString().trim();
        String password = textFieldPassword.getEditText().getText().toString().trim();

        if (email.isEmpty()) {
            textFieldEmail.setError("Email is required!");
            textFieldEmail.requestFocus();
            return;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            textFieldEmail.setError("Please insert valid email!");
            textFieldEmail.requestFocus();
            return;
        }else{
            textFieldEmail.setError(null);
        }
        if (password.isEmpty()) {
            textFieldPassword.setError("Password is required");
            textFieldPassword.requestFocus();
            return ;
        }
        else if (password.length()<6) {
            textFieldPassword.setError("Password must 6 character length");
            textFieldPassword.requestFocus();
            return;
        }else{
            textFieldPassword.setError(null);
        }


        progressBarLogin.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

//
//                    if (user.isEmailVerified()){
//                        //redirect to home
                      startActivity(new Intent(MainActivity.this, HomeActivity.class));
//
//                    }else{
//                        user.sendEmailVerification();
//                        Toast.makeText(MainActivity.this, "Please check your email to verify your account!", Toast.LENGTH_LONG).show();
//                        progressBarLogin.setVisibility(View.GONE);
//                    }


                }else{
                    Toast.makeText(MainActivity.this, "Failed to login! Please check your credentials", Toast.LENGTH_LONG).show();
                    progressBarLogin.setVisibility(View.GONE);
                }
            }
        });
    }
}