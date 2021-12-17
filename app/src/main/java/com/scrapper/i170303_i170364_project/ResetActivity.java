package com.scrapper.i170303_i170364_project;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetActivity extends AppCompatActivity {

    private  FirebaseAuth auth;
    private Button resetBtn, loginPageBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        resetBtn = findViewById(R.id.reset_btn);
        auth = FirebaseAuth.getInstance();

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView emailField = findViewById(R.id.emailField);
                if(areFieldsFilled(emailField)){

                    auth.sendPasswordResetEmail(emailField.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ResetActivity.this, "A reset email has been sent", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    Intent intent = new Intent(ResetActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

       loginPageBtn = findViewById(R.id.loginPage);
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

    @Override
    public void onResume() {
        super.onResume();
        getSupportActionBar().hide();
    }


}
