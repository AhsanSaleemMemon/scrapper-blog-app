package com.scrapper.i170303_i170364_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUpActivity extends AppCompatActivity {

    TextView goToLogin;
    FirebaseAuth mAuth;


    TextInputEditText email;
    TextInputEditText password;
    TextInputEditText username;

    MaterialButton signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email_input);
        password = findViewById(R.id.password_input);
        username = findViewById(R.id.username_input);

        signUpButton = findViewById(R.id.sign_up_btn);


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isEmpty(email) && !isEmpty(password) && !isEmpty(username)) {
                    Toast.makeText(SignUpActivity.this, "All field are filled", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(SignUpActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });





        // goes to LoginActivity from SignUpActivity
        goToLogin = findViewById(R.id.go_to_login);
        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    // checks if any TextInputEditText is empty
    private static boolean isEmpty(TextInputEditText field) {
        String fieldStr = field.getText().toString();
        if(fieldStr.isEmpty()) {
            return true;
        }
        else {
            return false;
        }
    }
}