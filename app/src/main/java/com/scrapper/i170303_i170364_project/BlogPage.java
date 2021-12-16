package com.scrapper.i170303_i170364_project;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import de.hdodenhof.circleimageview.CircleImageView;

public class BlogPage extends AppCompatActivity
{
    ShapeableImageView blogimg;
    TextView content,title, authorName, uploadTime;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    StorageReference storageReference;
    String userID="";
    String postID;
    String uID;
    CircleImageView profilePic;
    boolean authorPic;
    ImageView downloadButton;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_blog);
        authorPic = false;
        downloadButton =findViewById(R.id.downloadButton);
        profilePic=findViewById(R.id.pic);
        content = (TextView) findViewById(R.id.content);
        title=(TextView) findViewById(R.id.title);
        blogimg = (ShapeableImageView) findViewById(R.id.blogphoto);
        authorName=(TextView) findViewById(R.id.authorName);
        uploadTime=(TextView) findViewById(R.id.uploadTime);

        postID=getIntent().getStringExtra("postID");
        String postImgLink =getIntent().getStringExtra("postimage");
        Picasso.get().load(postImgLink).fit().into(blogimg);


        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("Posts");

        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot: snapshot.getChildren()){

                    for (DataSnapshot postSnapshot: userSnapshot.getChildren()){
                        Post post=postSnapshot.getValue(Post.class);
                        if(postID.equals(postSnapshot.getRef().getKey())) {
                            uID = userSnapshot.getRef().getKey();
                            userID=uID;

//                            Toast.makeText(BlogPage.this, userID, Toast.LENGTH_SHORT).show();
                            authorPic = true;


                        //    Toast.makeText(BlogPage.this,userID,Toast.LENGTH_SHORT).show();

                            // getting user image
                            storageReference= FirebaseStorage.getInstance().getReference("images/"+userID);
                            try {
                                File localFile= File.createTempFile("tempfile",".jpg");
                                //Toast.makeText(getContext(),"Fetching image",Toast.LENGTH_SHORT).show();
                                //Toast.makeText(getActivity(),imageID,Toast.LENGTH_SHORT).show();
                                storageReference.getFile(localFile)
                                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                                                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());

                                                profilePic.setImageBitmap(bitmap);



                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        //Toast.makeText(getActivity(),"Fetching image failed",Toast.LENGTH_SHORT).show();
                                    }
                                });


                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            break;
                        }
                    }
                    if(authorPic) {
//                        Toast.makeText(BlogPage.this, "Loop Broken", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("bigfails", "onCancelled", databaseError.toException());
            }
        });

       // img.setImageResource(getIntent().getIntExtra("postimage",0));
        content.setText(getIntent().getStringExtra("content"));
        title.setText(getIntent().getStringExtra("title"));
        authorName.setText(getIntent().getStringExtra("authorname"));
        uploadTime.setText(getIntent().getStringExtra("uploadtime"));


        loadTextFile("AllAuthorsIDs");

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileName =userID+"-"+postID+"-"+"pTitle";
                try {
                    saveTextFile(fileName,title.getText().toString());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                fileName =userID+"-"+postID+"-"+"pContent";
                try {
                    saveTextFile(fileName,content.getText().toString());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                fileName =userID+"-"+postID+"-"+"pUploadTime";
                try {
                    saveTextFile(fileName,uploadTime.getText().toString());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                fileName =userID+"-"+postID+"-"+"pauthorName";
                try {
                    saveTextFile(fileName,authorName.getText().toString());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                fileName ="AllAuthorsIDs";
                try {
                    saveTextFile(fileName,userID);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                fileName ="AllPostsIDs";
                try {
                    saveTextFile(fileName,postID);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

//                fileName =userID+"-"+postID+"-"+"BlogImage";
//                Toast.makeText(BlogPage.this,postImgLink,Toast.LENGTH_SHORT).show();
//                Bitmap bitmap = decodeBase64(postImgLink);
//                String postImagePath=saveImageToInternalStorage(bitmap,fileName);
//                fileName = "AllPostsImagesPath";
//                saveTextFile(fileName,postImagePath);



                //download author's pic
//                storageReference = FirebaseStorage.getInstance().getReference("images/"+userID);
//                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        Bitmap bitmap = decodeBase64(uri.toString());
//                        String userImagePath=saveImageToInternalStorage(bitmap,userID);
//                        saveTextFile("AllUsersImagesPath",userImagePath);
//                    }
//                });


            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
            getSupportActionBar().hide();
    }

    public void saveTextFile(String fileName,String text) throws FileNotFoundException {

        fileName=fileName+".txt";
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(getFilesDir() + "/" +fileName,true);
            fos.write(text.getBytes());

            Toast.makeText(this, "Saved to " + getFilesDir() + "/" + fileName,
                    Toast.LENGTH_LONG).show();
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

    private String saveImageToInternalStorage(Bitmap bitmapImage,String fileName){
        fileName = fileName+".jpg";
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,fileName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }


    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }


    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public void loadTextFile(String fileName) {
        fileName =fileName+".txt";
        FileInputStream fis = null;

        try {
            fis = openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;
            String titles ="";

            while ((text = br.readLine()) != null) {
                titles+= text+" ";
            }

            Toast.makeText(BlogPage.this,titles,Toast.LENGTH_LONG).show();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
