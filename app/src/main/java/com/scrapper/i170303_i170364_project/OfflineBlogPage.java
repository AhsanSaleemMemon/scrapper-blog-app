package com.scrapper.i170303_i170364_project;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class OfflineBlogPage extends AppCompatActivity {
    ShapeableImageView blogimg;
    TextView content, title, authorName, uploadTime;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    StorageReference storageReference;
    String userID = "hello";
    String postID;
    String uID;
    CircleImageView profilePic;
    ImageView downloadButton;
    InputStream inputStream = null;
    String userImagePath = "";
    String blogImagePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_blog);
        downloadButton = findViewById(R.id.downloadButton);
        downloadButton.setVisibility(View.GONE);
        content = (TextView) findViewById(R.id.content);
        authorName = (TextView) findViewById(R.id.authorName);
        uploadTime = (TextView) findViewById(R.id.uploadTime);
        title = (TextView) findViewById(R.id.title);
        blogimg = (ShapeableImageView) findViewById(R.id.blogphoto);
        profilePic = findViewById(R.id.pic);

        content.setText(getIntent().getStringExtra("content"));
        title.setText(getIntent().getStringExtra("title"));
        authorName.setText(getIntent().getStringExtra("authorname"));
        uploadTime.setText(getIntent().getStringExtra("uploadtime"));
        blogImagePath = getIntent().getStringExtra("postimage");
        userImagePath = getIntent().getStringExtra("authorimage");

        String path_imageName = blogImagePath;
        String[] path_imageName_split = path_imageName.split(",");



        try {
            File f=new File(path_imageName_split[0], path_imageName_split[1]+".jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));

            blogimg.setImageBitmap(Bitmap.createScaledBitmap(b, 200, 120, false));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        path_imageName = userImagePath;
        path_imageName_split = path_imageName.split(",");


        try {
            File f=new File(path_imageName_split[0], path_imageName_split[1]+".jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));

            profilePic.setImageBitmap(Bitmap.createScaledBitmap(b, 120, 120, false));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }




    }
}
