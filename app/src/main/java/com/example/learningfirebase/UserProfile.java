package com.example.learningfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String userID;
    FirebaseDatabase database;
    DatabaseReference myRef;
    private Button logoutBtn;
    private TextView fNameTV,emailTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        logoutBtn=(Button) findViewById(R.id.logoutBtn);
        fNameTV=(TextView) findViewById(R.id.fNameTV);
        emailTV=(TextView) findViewById(R.id.emailTV);

        mAuth=FirebaseAuth.getInstance();

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(UserProfile.this,MainActivity.class));
            }
        });
        user=mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance("https://learning-firebase-c6d0a-default-rtdb.firebaseio.com/");
        myRef = database.getReference("users");
        userID=user.getUid();

        myRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User userProfile=dataSnapshot.getValue(User.class);
                if(userProfile!=null)
                {
                    String fullName=userProfile.fName;
                    String email=userProfile.email;
                    fNameTV.setText("welcome "+fullName+"!");
                    emailTV.setText(email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UserProfile.this, "something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}