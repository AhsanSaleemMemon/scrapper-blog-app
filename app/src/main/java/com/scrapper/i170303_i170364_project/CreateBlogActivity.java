package com.scrapper.i170303_i170364_project;

import static android.widget.Toast.LENGTH_SHORT;

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

public class CreateBlogActivity extends AppCompatActivity {

    ImageView mCoverImage;
    MaterialButton mPostButton;
    Uri imageUri;
    StorageReference storageReference;
    ActivityResultLauncher<String> mGetContent;
    Post post=new Post();
    User user;
    String postKey;
    String userKey;

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
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase mDatabase=FirebaseDatabase.getInstance();
        DatabaseReference mReference = mDatabase.getReference("Users");
        //userKey=mReference.push().getKey();

        //capturing today's date
        Date today = new Date();
        //displaying this date on IST timezone
        DateFormat df = new SimpleDateFormat("dd-MM-yy");
        df.setTimeZone(TimeZone.getTimeZone("Asia/Karachi"));
        String IST = df.format(today);
        post.setTimeStamp(IST);

        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //user=ds.getValue(User.class);
                    //Log.d("MyApp",user.name);
                    String usersInDatabase = ds.child(userID).getKey().toString();
                    Toast.makeText(CreateBlogActivity.this,usersInDatabase,LENGTH_SHORT).show();
                    if(usersInDatabase.equals(userID)){
                        User user=ds.getValue(User.class);
//                        String userName="";
//                        userName= (String) ds.child(userID).child("name").getValue();
//                        Toast.makeText(CreateBlogActivity.this,userName,LENGTH_SHORT).show();
                        post.setAuthor(user.name);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        mPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase mDatabase=FirebaseDatabase.getInstance();
                DatabaseReference mReference = mDatabase.getReference("Posts");
                //postKey=mReference.push().getKey();
                String title=mTitle.getText().toString();
                String content=mContent.getText().toString();
                post.setTitle(title);
                post.setContent(content);
                mReference.child(userID).child(postKey).setValue(post);
                Toast.makeText(CreateBlogActivity.this,"Successfully Uploaded rtdb", LENGTH_SHORT).show();

            }
        });

        mCoverImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetContent.launch("image/*");
            }
        });

        mGetContent=registerForActivityResult(
                new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        mCoverImage.setImageURI(result);
                        imageUri=result;
                        uploadImage();

                    }
                }
        );

    }


    public void uploadImage() {
        FirebaseDatabase mDatabase=FirebaseDatabase.getInstance();
        DatabaseReference mReference = mDatabase.getReference("Posts");
        postKey=mReference.push().getKey();
        String fileName = postKey;
        storageReference = FirebaseStorage.getInstance().getReference("Blogimages/"+fileName);
        storageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //selectImagebtn.setImageURI(imageUri);
                        Toast.makeText(CreateBlogActivity.this,"Image successfully Uploaded", LENGTH_SHORT).show();
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                post.setImageLink(uri.toString());
                            }
                        });


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateBlogActivity.this,"Failed to Upload", LENGTH_SHORT).show();
            }
        });
    }


}