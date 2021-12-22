package com.scrapper.i170303_i170364_project;

import static java.security.AccessController.getContext;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import de.hdodenhof.circleimageview.CircleImageView;

public class BlogPage extends AppCompatActivity {
    ShapeableImageView blogimg;
    TextView content, title, authorName, uploadTime;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    StorageReference storageReference;
    String userID = "hello";
    String postID;
    String uID;
    CircleImageView profilePic;
    ImageView downloadButton,deleteButton,editButton;
    InputStream inputStream = null;
    String userImagePath = "";
    String blogImagePath = "";
    String imageLink="";
    String prevTitle,prevContent;
    ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_blog);
        downloadButton = findViewById(R.id.downloadButton);
        editButton = findViewById(R.id.editButton);
        deleteButton = findViewById(R.id.deleteButton);
        editButton.setVisibility(View.GONE);
        deleteButton.setVisibility(View.GONE);
        profilePic = findViewById(R.id.pic);
        content = (TextView) findViewById(R.id.content);
        title = (TextView) findViewById(R.id.title);
        blogimg = (ShapeableImageView) findViewById(R.id.blogphoto);
        authorName = (TextView) findViewById(R.id.authorName);
        uploadTime = (TextView) findViewById(R.id.uploadTime);
        postID = getIntent().getStringExtra("postID");
        String postImgLink = getIntent().getStringExtra("postimage");
        Picasso.get().load(postImgLink).fit().into(blogimg);
        prevContent=getIntent().getStringExtra("content");
        prevTitle=getIntent().getStringExtra("title");


        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("Posts");

        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {

                    for (DataSnapshot postSnapshot : userSnapshot.getChildren()) {
                        //Post post = postSnapshot.getValue(Post.class);
                        if (postID.equals(postSnapshot.getRef().getKey())) {
                            uID = postSnapshot.getRef().getParent().getKey();
                            userID = uID;

                            ////

                            //Toast.makeText(BlogPage.this,"author id is "+userID,Toast.LENGTH_SHORT).show();
                            //Toast.makeText(BlogPage.this,"current id is "+FirebaseAuth.getInstance().getCurrentUser().getUid(),Toast.LENGTH_SHORT).show();

                            if (userID.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                                editButton.setVisibility(View.VISIBLE);
                                deleteButton.setVisibility(View.VISIBLE);
                                deleteButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mDatabase = FirebaseDatabase.getInstance();
                                        mReference = mDatabase.getReference("Posts");


                                        mReference.child(userID).child(postID).child("imageLink").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                imageLink = snapshot.getValue(String.class);
                                                //Toast.makeText(BlogPage.this,"current url is "+imageLink,Toast.LENGTH_SHORT).show();
                                                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                                                StorageReference photoRef = firebaseStorage.getReferenceFromUrl(imageLink);
                                                photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        //Toast.makeText(BlogPage.this,"deleted",Toast.LENGTH_SHORT).show();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception exception) {

                                                    }
                                                });


                                                //
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                        mReference.child(userID).child(postID).removeValue();
                                    }
                                });

                                //edit post

                                editButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        Intent intent=new Intent(BlogPage.this,EditBlog.class);
                                        intent.putExtra("postID",postID);
                                        intent.putExtra("authorID",userID);
                                        intent.putExtra("title",prevTitle);
                                        intent.putExtra("content", prevContent);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);

                                    }
                                });

                            }


                            ///
                            //Toast.makeText(BlogPage.this, postID, Toast.LENGTH_SHORT).show();
                            //Toast.makeText(BlogPage.this, userID, Toast.LENGTH_SHORT).show();

                            // getting user image
                            storageReference = FirebaseStorage.getInstance().getReference("images/" + userID);
                            try {
                                File localFile = File.createTempFile("tempfile", ".jpg");
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


                        }
                        //break;
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("bigfails", "onCancelled", databaseError.toException());
            }
        });

        // img.setImageResource(getIntent().getIntExtra("postimage",0));
        content.setText(prevContent);
        title.setText(prevTitle);
        authorName.setText(getIntent().getStringExtra("authorname"));
        uploadTime.setText(getIntent().getStringExtra("uploadtime"));





        //loadTextFile("AllAuthorsIDs");

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(BlogPage.this);
                progressDialog.setTitle("Downloading Blog....");
                progressDialog.show();
                String fileName = FirebaseAuth.getInstance().getCurrentUser().getUid() + "," + postID + "-" + "pTitle";
                try {
                    saveTextFile(fileName, title.getText().toString(), false, false);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                fileName = FirebaseAuth.getInstance().getCurrentUser().getUid() + "," + postID + "-" + "pContent";
                try {
                    saveTextFile(fileName, content.getText().toString(), false, false);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                fileName = FirebaseAuth.getInstance().getCurrentUser().getUid() + "," + postID + "-" + "pUploadTime";
                try {
                    saveTextFile(fileName, uploadTime.getText().toString(), false, false);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                fileName = FirebaseAuth.getInstance().getCurrentUser().getUid() + "," + postID + "-" + "pauthorName";
                try {
                    saveTextFile(fileName, authorName.getText().toString(), false, false);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

//                fileName = "AllAuthorsIDs";
//                try {
//                    saveTextFile(fileName, userID);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
                //currentuser+authidpostidlist
                fileName = FirebaseAuth.getInstance().getCurrentUser().getUid() + "-" + "authID" + "-" +
                        "postIDlist";
                try {
                    saveTextFile(fileName,
                            userID + "," + postID, true, true);
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
                storageReference = FirebaseStorage.getInstance().getReference("images/" + userID);

                try {
                    File localFile = File.createTempFile("tempfile", ".jpg");

                    storageReference.getFile(localFile)
                            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                                    //save user image path
                                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());

                                    userImagePath = saveImageToInternalStorage(bitmap, userID);
                                    userImagePath += "," + userID;
                                    try {
                                        saveTextFile(FirebaseAuth.getInstance().getCurrentUser().getUid() + "-" + userID + "-authorImageAdress", userImagePath, false, false);
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    }




                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Toast.makeText(getActivity(), "Fetching image failed", Toast.LENGTH_SHORT).show();
                        }
                    });


                } catch (IOException e) {
                    e.printStackTrace();
                }


                storageReference = FirebaseStorage.getInstance().getReference("Blogimages/" + postID);
                try {
                    File localFile2 = File.createTempFile("tempfile2", ".jpg");

                    storageReference.getFile(localFile2)
                            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                                    //save user image path

                                    Bitmap bitmap2 = BitmapFactory.decodeFile(localFile2.getAbsolutePath());
                                    blogImagePath = saveImageToInternalStorage(bitmap2, postID);
                                    blogImagePath += "," + postID;
                                    try {
                                        saveTextFile(FirebaseAuth.getInstance().getCurrentUser().getUid() + "," + postID + "-blogImageAdress", blogImagePath, false, false);
                                        progressDialog.dismiss();
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    }




                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            //Toast.makeText(getActivity(), "Fetching image failed", Toast.LENGTH_SHORT).show();
                        }
                    });


                } catch (IOException e) {
                    e.printStackTrace();
                }


                ///

            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        getSupportActionBar().hide();
    }

    public void saveTextFile(String fileName, String text, boolean append, boolean endLineDelimiter) throws FileNotFoundException {

        if (isBlogDownloaded(fileName, text))
            return;
        else {
            fileName = fileName + ".txt";
            FileOutputStream fos = null;

            try {
                fos = new FileOutputStream(getFilesDir() + "/" + fileName, append);
                fos.write(text.getBytes());
                if (endLineDelimiter)
                    fos.write('\n');


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

    private String saveImageToInternalStorage(Bitmap bitmapImage, String fileName) {
        fileName = fileName + ".jpg";
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, fileName);

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


    public boolean isBlogDownloaded(String fileName, String word) throws FileNotFoundException {

        fileName = fileName + ".txt";
        FileInputStream fis = null;

        boolean flag = false;
        String allText = "";
        try {
            fis = openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            String text;


            while ((text = br.readLine()) != null) {
                if (word.equals(text)) {
                    return true;
                }

            }


            //Toast.makeText(getContext(), allText, Toast.LENGTH_LONG).show();


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

        return false;

    }

}
