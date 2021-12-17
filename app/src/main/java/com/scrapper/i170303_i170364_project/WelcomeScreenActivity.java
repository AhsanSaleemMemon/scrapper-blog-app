package com.scrapper.i170303_i170364_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomeScreenActivity extends AppCompatActivity {

//    String prevStarted = "yes";
    MaterialButton signUpPage;
    MaterialButton loginPage;
    SharedPreferences sharedPreferences;
    FirebaseUser user;



//    String prevStarted = "yes";
//    @Override
//    protected void onResume() {
//        super.onResume();
//        SharedPreferences sharedpreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
//        if (!sharedpreferences.getBoolean(prevStarted, false)) {
//            SharedPreferences.Editor editor = sharedpreferences.edit();
//            editor.putBoolean(prevStarted, Boolean.TRUE);
//            editor.apply();
//        } else {
//            moveToLogin();
//        }
//    }
//
//    private void moveToLogin() {
//        Intent intent = new Intent(WelcomeScreenActivity.this, LoginActivity.class);
//        startActivity(intent);
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);


        sharedPreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();


        String welcomeScreenBool = sharedPreferences.getString("welcomeScreenBool", "");
       if(welcomeScreenBool.equals("true")) {
           Toast.makeText(WelcomeScreenActivity.this, "Preference already stored", Toast.LENGTH_SHORT).show();
           user = FirebaseAuth.getInstance().getCurrentUser();
           Intent i;
           if (user != null){
               i = new Intent(WelcomeScreenActivity.this, MainActivity.class);
           }
           else {
               i = new Intent(WelcomeScreenActivity.this, LoginActivity.class);
           }
           startActivity(i);
       }
       else {
           Toast.makeText(WelcomeScreenActivity.this, "Stored the Preference", Toast.LENGTH_SHORT).show();
           editor.putString("welcomeScreenBool", "true");
           editor.apply();
           editor.commit();
       }


        signUpPage = findViewById(R.id.go_to_sign_up);
        loginPage = findViewById(R.id.go_to_login);

        signUpPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeScreenActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });


        loginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeScreenActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        getSupportActionBar().hide();
    }


}