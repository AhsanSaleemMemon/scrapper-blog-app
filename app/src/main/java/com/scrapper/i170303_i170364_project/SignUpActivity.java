package com.scrapper.i170303_i170364_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
        email = findViewById(R.id.emailField);
        password = findViewById(R.id.passwordField);
        username = findViewById(R.id.userField);

        signUpButton = findViewById(R.id.sign_up_btn);


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView usernameField = findViewById(R.id.userField);
                TextView emailField = findViewById(R.id.emailField);
                TextView passField = findViewById(R.id.passwordField);

                String email = emailField.getText().toString().trim();
                String password = passField.getText().toString().trim();
                String username = usernameField.getText().toString().trim();

                if (areFieldsFilled(usernameField, emailField, passField)) { // if all the fields are filled


                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Toast.makeText(SignUpActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                        startActivity(intent); // Go to main page

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(SignUpActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                                    }

                                    // ...
                                }
                            });

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

    boolean isEmailValid(String email) {   // checks if email is of invalid format
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    boolean areFieldsFilled(TextView userField, TextView emailField, TextView passField) { // checks if all the fields are filled and valid
        Boolean filledFields = true; // if all fields are filled

        if (TextUtils.isEmpty(userField.getText().toString())) { // if username field is empty
            userField.setError("username field can't be empty");
            filledFields = false;
        }

        if (TextUtils.isEmpty(emailField.getText().toString())) { // if email field is empty
            emailField.setError("Email Field can't be empty"); //
            filledFields = false;
        } else if (!isEmailValid(emailField.getText().toString())) { // if email is invalid
            emailField.setError("Invalid Email format");
            filledFields = false;
        }

        if (TextUtils.isEmpty(passField.getText().toString())) { // if email field is empty
            passField.setError("Pass Field can't be empty"); //
            filledFields = false;
        }
        return filledFields;
    }
}