package com.scrapper.i170303_i170364_project;

import static android.widget.Toast.LENGTH_SHORT;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class EditBlog extends AppCompatActivity {

    ImageView mCoverImage;
    MaterialButton mPostButton;
    Uri imageUri;
    StorageReference storageReference;
    ActivityResultLauncher<String> mGetContent;
    String prevPostID;
    String prevAuthorID;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_blog);
        EditText mTitle;
        EditText mContent;
        mTitle = (EditText) findViewById(R.id.title_id);
        mContent = (EditText) findViewById(R.id.content_id);
        mCoverImage = findViewById(R.id.upload_image);
        mPostButton = findViewById(R.id.post_btn);

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mReference = mDatabase.getReference("Users");
        //userKey=mReference.push().getKey();

        //capturing today's date
        Date today = new Date();
        //displaying this date on IST timezone
        DateFormat df = new SimpleDateFormat("dd-MM-yy");
        df.setTimeZone(TimeZone.getTimeZone("Asia/Karachi"));
        String time = df.format(today);

        Intent intent = getIntent();
        prevPostID = intent.getStringExtra("postID");
        prevAuthorID = intent.getStringExtra("authorID");
        String prevTitle = intent.getStringExtra("title");
        String prevContent = intent.getStringExtra("content");

        mTitle.setText(prevTitle);
        mContent.setText(prevContent);


        mPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
                DatabaseReference mReference = mDatabase.getReference("Posts");
                //postKey=mReference.push().getKey();
                String newTitle = mTitle.getText().toString();
                String newContent = mContent.getText().toString();
                mReference.child(prevAuthorID).child(prevPostID).child("content").setValue(newContent);
                mReference.child(prevAuthorID).child(prevPostID).child("title").setValue(newTitle);
                mReference.child(prevAuthorID).child(prevPostID).child("timeStamp").setValue(time);
                Toast.makeText(EditBlog.this, "Successfully Uploaded rtdb", LENGTH_SHORT).show();


            }
        });

        mCoverImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetContent.launch("image/*");
            }
        });

        mGetContent = registerForActivityResult(
                new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        mCoverImage.setImageURI(result);
                        imageUri = result;
                        uploadImage();

                    }
                }
        );

    }


    public void uploadImage() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading Blog....");
        progressDialog.show();
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mReference = mDatabase.getReference("Posts");

        String fileName = prevPostID;
        storageReference = FirebaseStorage.getInstance().getReference("Blogimages/" + fileName);
        storageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //selectImagebtn.setImageURI(imageUri);
                        //Toast.makeText(CreateBlogActivity.this,"Image successfully Uploaded", LENGTH_SHORT).show();
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                mReference.child(prevAuthorID).child(prevPostID).child("imageLink").setValue(uri.toString());
                                if (progressDialog.isShowing())
                                    progressDialog.dismiss();
                            }
                        });


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                //Toast.makeText(CreateBlogActivity.this,"Failed to Upload", LENGTH_SHORT).show();
            }
        });
    }


}
