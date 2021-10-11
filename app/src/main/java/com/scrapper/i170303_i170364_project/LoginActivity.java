package com.scrapper.i170303_i170364_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;

public class LoginActivity extends AppCompatActivity {

    TextView goToSignUp;
    FirebaseAuth mAuth;

    TextInputEditText email;
    TextInputEditText password;

    MaterialButton loginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        mAuth = FirebaseAuth.getInstance();
        loginButton = findViewById(R.id.login_btn);

        email = findViewById(R.id.email_input);
        password = findViewById(R.id.password_input);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isEmpty(email) && !isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "All field are filled", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(LoginActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });



        goToSignUp = findViewById(R.id.go_to_sign_up);
        goToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

    }

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