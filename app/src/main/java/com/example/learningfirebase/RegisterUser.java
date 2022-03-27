package com.example.learningfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class RegisterUser extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    private EditText fNameET,emailET,passwordET;
    private Button registerBtn;
    private ProgressBar registerPB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        fNameET=(EditText) findViewById(R.id.fNameET);
        emailET=(EditText) findViewById(R.id.emailET);
        passwordET=findViewById(R.id.passwordET);
        registerPB=findViewById(R.id.registerPB);
        registerBtn=findViewById(R.id.registerBtn);

        // Write a message to the database
        database = FirebaseDatabase.getInstance("https://learning-firebase-c6d0a-default-rtdb.firebaseio.com/");
        myRef = database.getReference("users");

        mAuth = FirebaseAuth.getInstance();


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }
    public void registerUser()
    {
        String email=emailET.getText().toString().trim();
        String password=passwordET.getText().toString().trim();
        String fName=fNameET.getText().toString().trim();
        if(fName.isEmpty())
        {
            fNameET.setError("Full name is required");
            fNameET.requestFocus();
        }
        if (email.isEmpty())
        {
            emailET.setError("Email is required");
            emailET.requestFocus();
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            emailET.setError("Please provide valid email");
            emailET.requestFocus();
        }
        if(password.isEmpty())
        {
            passwordET.setError("Password is required!");
            passwordET.requestFocus();
        }
        if(password.length()<6)
        {
            passwordET.setError("Min password length should be 6 characters!");
            passwordET.requestFocus();
        }
        registerPB.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user=new User(fName,email);
                            myRef.child(mAuth.getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener((new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(RegisterUser.this, "User has been registered successfully!", Toast.LENGTH_SHORT).show();
                                        registerPB.setVisibility(View.GONE);
                                    }
                                    else
                                    {
                                        Toast.makeText(RegisterUser.this, "Failed to register! Try again!", Toast.LENGTH_SHORT).show();
                                        registerPB.setVisibility(View.GONE);
                                    }
                                }
                            }));
                        } else
                        {
                            Toast.makeText(RegisterUser.this, "Failed to register! Try again!", Toast.LENGTH_SHORT).show();
                            registerPB.setVisibility(View.GONE);
                        }
                    }
                });
    }
}