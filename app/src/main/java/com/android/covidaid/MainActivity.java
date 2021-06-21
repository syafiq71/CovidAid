package com.android.covidaid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button textButtonLogin;
    private TextView register, forgotPass ;
    private TextInputLayout textFieldEmail, textFieldPassword;
    private ProgressBar progressBarLogin;
    private DatabaseReference dbReference;
    private String userID;

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

        forgotPass = (TextView) findViewById(R.id.forgotPass) ;
        forgotPass.setOnClickListener(this);


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
            case R.id.forgotPass:
                EditText resetMail = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password ?");
                passwordResetDialog.setMessage("Enter Your Email to Received Reset Link");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String mail = resetMail.getText().toString();
                        mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(MainActivity.this, "Reset Link sent to your email", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Toast.makeText(MainActivity.this, "Error ! Reset Link is Not Sent"+ e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                passwordResetDialog.create().show();
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

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText resetMail = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password ?");
                passwordResetDialog.setMessage("Enter Your Email to Received Reset Link");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String mail = resetMail.getText().toString();
                        mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(MainActivity.this, "Reset Link sent to your email", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Toast.makeText(MainActivity.this, "Error ! Reset Link is Not Sent"+ e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                passwordResetDialog.create().show();
            }
        });

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    dbReference = FirebaseDatabase.getInstance().getReference("Users");
                    userID = user.getUid();
                    dbReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User userProfile = snapshot.getValue(User.class);
                            String type = userProfile.type;
                            if(type.equals("admin")){
                                startActivity(new Intent(MainActivity.this, AdminPage.class));
                            }else{
                                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {
                            Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                        }
                    });
                }else {
                    Toast.makeText(MainActivity.this, "Please check your password", Toast.LENGTH_LONG).show();
                }

                        //                    if (user.isEmailVerified()){
//                        //redirect to home
//                      startActivity(new Intent(MainActivity.this, HomeActivity.class));
//
//                    }else{
//                        user.sendEmailVerification();
//                        Toast.makeText(MainActivity.this, "Please check your email to verify your account!", Toast.LENGTH_LONG).show();
//                        progressBarLogin.setVisibility(View.GONE);
//                    }



            }
        });
    }
}