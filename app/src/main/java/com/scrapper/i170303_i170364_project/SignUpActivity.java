package com.scrapper.i170303_i170364_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends AppCompatActivity {

    Uri imageUri;
    TextView goToLogin;
    FirebaseAuth mAuth;
    TextInputEditText emailField;
    TextInputEditText passwordField;
    TextInputEditText nameField;
    CircleImageView selectImagebtn;
    MaterialButton signUpButton;
    ActivityResultLauncher<String> mGetContent;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        selectImagebtn =findViewById(R.id.profile_pic);
        mAuth = FirebaseAuth.getInstance();
        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        nameField = findViewById(R.id.userField);

        signUpButton = findViewById(R.id.sign_up_btn);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String email = emailField.getText().toString().trim();
                String password = passwordField.getText().toString().trim();
                String name = nameField.getText().toString().trim();

                if (areFieldsFilled(nameField, emailField, passwordField)) { // if all the fields are filled


                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        User user = new User(
                                                email,
                                                name
                                        );
                                        FirebaseDatabase.getInstance().getReference("Users")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()) {
                                                    Toast.makeText(SignUpActivity.this, "Sign up Success", Toast.LENGTH_LONG).show();
                                                    Intent intent =new Intent(SignUpActivity.this,UploadImageActivity.class);
                                                    startActivity(intent);

                                                } else {
                                                    //display a failure message
                                                    Toast.makeText(SignUpActivity.this, "Sign up failure 1", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });

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