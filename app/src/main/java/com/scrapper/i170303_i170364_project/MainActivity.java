package com.scrapper.i170303_i170364_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {


    FirebaseAuth mAuth;
    FirebaseUser user;

    MaterialButton username;
    MaterialButton signOutButton;

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);

        user = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();
        signOutButton = findViewById(R.id.sign_out);
        username = findViewById(R.id.username_btn);

        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(MainActivity.this, "Display clicked: " +user.getDisplayName(), Toast.LENGTH_SHORT).show();

            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}