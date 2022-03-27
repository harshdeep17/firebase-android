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
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private EditText emailET;
    private Button resetBtn;
    private ProgressBar resetPB;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailET=(EditText) findViewById(R.id.emailET);
        resetBtn=(Button) findViewById(R.id.resetBtn);
        resetPB=(ProgressBar) findViewById(R.id.resetPB);

        mAuth=FirebaseAuth.getInstance();

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }

    private void resetPassword() {
        String email=emailET.getText().toString().trim();

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
        resetPB.setVisibility(View.VISIBLE);

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(ForgotPassword.this, "Check your email to reset your password", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(ForgotPassword.this, "Try again! something went wrong!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}