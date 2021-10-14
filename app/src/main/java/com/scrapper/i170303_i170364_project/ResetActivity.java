package com.scrapper.i170303_i170364_project;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        Button resetBtn = findViewById(R.id.reset_btn);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView emailField = findViewById(R.id.emailField);
                if(areFieldsFilled(emailField)){
                    Intent intent = new Intent(ResetActivity.this, LoginActivity.class);
                }
            }
        });

        Button loginPageBtn = findViewById(R.id.loginPage);
        loginPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResetActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


    }
    boolean isEmailValid(String email) {   // checks if email is of invalid format
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    boolean areFieldsFilled(TextView emailField) { // checks if all the fields are filled and valid
        Boolean filledFields = true; // if all fields are filled

        if (TextUtils.isEmpty(emailField.getText().toString())) { // if email field is empty
            emailField.setError("Email Field can't be empty"); //
            filledFields = false;
        }

        else if(!isEmailValid(emailField.getText().toString())){ // if email is invalid
            emailField.setError("Invalid Email format");
            filledFields = false;
        }
        return filledFields;
    }


}
