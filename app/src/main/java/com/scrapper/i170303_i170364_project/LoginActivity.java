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
import com.google.firebase.auth.FirebaseAuthSettings;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    TextView goToSignUp;
    TextView forgotPassword;
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

        email = findViewById(R.id.emailField);
        password = findViewById(R.id.passField);



        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // Runs when the login button is clicked

                TextView emailField = findViewById(R.id.emailField);
                TextView passField = findViewById(R.id.passField);

                String email = emailField.getText().toString().trim();
                String password = passField.getText().toString().trim();

                if (areFieldsFilled(emailField, passField)) { // if all the fields are filled
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Toast.makeText(LoginActivity.this, "Login Success!.",
                                                Toast.LENGTH_SHORT).show();
                                        try {
                                            saveTextFile("loggedInUser", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent); // Go to main page
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });
                }
            }
        });


        forgotPassword = findViewById(R.id.forgot_pass); // forgot password link
        // Redirects to reset page if user forgot password
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ResetActivity.class);
                startActivity(intent);
            }
        });

        TextView goToSignUp = findViewById(R.id.go_to_sign_up); // sign up page link
        // Redirects to sign up page if user is not registered
        goToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });




    }

    boolean isEmailValid(String email) {   // checks if email is of invalid format
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    boolean areFieldsFilled(TextView emailField, TextView passField) { // checks if all the fields are filled and valid
        Boolean filledFields = true; // if all fields are filled

        if (TextUtils.isEmpty(emailField.getText().toString())) { // if email field is empty
            emailField.setError("Email Field can't be empty"); //
            filledFields = false;
        } else if (!isEmailValid(emailField.getText().toString())) { // if email is invalid
            emailField.setError("Invalid Email format");
            filledFields = false;
        }

        if (TextUtils.isEmpty(passField.getText().toString())) { // if pass field is empty
            passField.setError("Pass Field can't be empty"); //
            filledFields = false;
        }


        return filledFields;
    }


    @Override
    public void onResume() {
        super.onResume();
        getSupportActionBar().hide();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }


    public void saveTextFile(String fileName, String text) throws FileNotFoundException {

        fileName = fileName + ".txt";
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(getFilesDir() + "/" + fileName, false);
            fos.write(text.getBytes());


            //Toast.makeText(this, "Saved to " + getFilesDir() + "/" + fileName,
            //Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}