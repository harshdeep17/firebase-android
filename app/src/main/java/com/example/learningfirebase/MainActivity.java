package com.example.learningfirebase;

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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    private TextView registerTV,forgotPasswordTV;
    private EditText emailET,passwordET;
    private ProgressBar loginPB;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerTV=(TextView) findViewById(R.id.registerTV);
        forgotPasswordTV=(TextView) findViewById(R.id.forgotPasswordTV);
        emailET=(EditText)findViewById(R.id.emailET);
        passwordET=(EditText)findViewById(R.id.passwordET);
        loginPB=(ProgressBar)findViewById(R.id.loginPB);
        loginBtn=(Button)findViewById(R.id.loginBtn);

//        database = FirebaseDatabase.getInstance("https://learning-firebase-c6d0a-default-rtdb.firebaseio.com/");
//        myRef = database.getReference("users");

        mAuth = FirebaseAuth.getInstance();

        registerTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,RegisterUser.class);
                startActivity(intent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });

        forgotPasswordTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ForgotPassword.class);
                startActivity(intent);
            }
        });
    }

    private void userLogin() {
        String email=emailET.getText().toString().trim();
        String password=passwordET.getText().toString().trim();
        if(email.isEmpty())
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
        loginPB.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) 
                {
                    FirebaseUser user=mAuth.getCurrentUser();
                    if(user.isEmailVerified())
                    {
                        Intent intent=new Intent(MainActivity.this,UserProfile.class);
                        startActivity(intent);
                        loginPB.setVisibility(View.GONE);
                    }
                    else
                    {
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this, "Please check your email to verify your email!", Toast.LENGTH_SHORT).show();
                        loginPB.setVisibility(View.GONE);
                    }

                }
                else{
                    Toast.makeText(MainActivity.this, "Failed to login! Please check your credentials", Toast.LENGTH_SHORT).show();
                    loginPB.setVisibility(View.GONE);
                }
            }
        });
    }
}