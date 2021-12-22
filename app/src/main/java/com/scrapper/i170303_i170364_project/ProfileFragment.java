package com.scrapper.i170303_i170364_project;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faltenreich.skeletonlayout.Skeleton;
import com.faltenreich.skeletonlayout.SkeletonLayoutUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    private FirebaseAuth mAuth;
    private TextView profileName, profileEmail;
    MaterialButton download_Button, myBlogs_Button;
    StorageReference storageReference;
    SharedPreferences sharedPreferences;
    String scrapperPreference;
    String email;
    String name;
    private RecyclerView downloadRecyclerView, myBlogsRecyclerView;

    private OfflinePostAdapter offlinePostAdapter;
    private PostAdapter myBlogsAdapter;
    private ArrayList<String> postIDList;
    private Skeleton skeleton;
    private String postID;
    CircleImageView profilePic;
    ArrayList <OfflinePost> downloadedPostList;
    ArrayList <Post> myPostsList;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View ProfileView = inflater.inflate(R.layout.profile_fragment, null);
        MaterialButton logoutButton = ProfileView.findViewById(R.id.logout);
        downloadRecyclerView = ProfileView.findViewById(R.id.bookmarksordownloads_recyclerview);


        postIDList = new ArrayList<>();
        profilePic = ProfileView.findViewById(R.id.profilepic);
        profileEmail = ProfileView.findViewById(R.id.profileemail);
        profileName = ProfileView.findViewById(R.id.profilename);
        mAuth = FirebaseAuth.getInstance();
        download_Button = ProfileView.findViewById(R.id.download_Button);
        myBlogs_Button = ProfileView.findViewById(R.id.myblogs_button);
        scrapperPreference = "scrapperPreference";
        String namePreferance = "name";
        String imagePreferance = "image";
        String emailPreferance = "email";




        download_Button.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                skeleton = SkeletonLayoutUtils.applySkeleton(downloadRecyclerView, R.layout.foryouarticles_layout);
                skeleton.showSkeleton();

                downloadedPostList =new ArrayList<OfflinePost>();

//                if(downloadRecyclerView.getVisibility()==View.INVISIBLE) {
//                    downloadRecyclerView.setVisibility(View.VISIBLE);
//                }
                FileInputStream fis = null;
                String postID;
                List<Post> downloadList = new ArrayList<>();
                ArrayList<String> auth_postIDList = new ArrayList<String>();
                String currentUser = loadTextFile("loggedInUser");
                //currentUser = currentUser.replaceAll("\\s+","");
                //Toast.makeText(getContext(), currentUser, Toast.LENGTH_LONG).show();
                try {
                    fis = getContext().openFileInput(currentUser + "-authID-postIDlist.txt");
                    InputStreamReader isr = new InputStreamReader(fis);
                    BufferedReader br = new BufferedReader(isr);
                    //StringBuilder sb = new StringBuilder();
                    String text;

                    int i = 0;

                    while ((text = br.readLine()) != null) {
                        auth_postIDList.add(text);

                        i++;
                    }
                    //Toast.makeText(getContext(), "file exist", Toast.LENGTH_LONG).show();


                    for (int j = 0; j < auth_postIDList.size(); j++) {
                        String auth_postID = auth_postIDList.get(j);
                        String[] auth_postID_split = auth_postID.split(",");
                        //add comma between em too
                        String postTitleToDisplay = loadTextFile(currentUser + "," + auth_postID_split[1] + "-pTitle");
                        String postContentToDisplay = loadTextFile(currentUser + "," + auth_postID_split[1] + "-pContent");
                        String postUploadTimeToDisplay = loadTextFile(currentUser + "," + auth_postID_split[1] + "-pUploadTime");
                        String postAuthorNameToDisplay = loadTextFile(currentUser + "," + auth_postID_split[1] + "-pauthorName");
                        String postImageAdress = loadTextFile(currentUser + "," + auth_postID_split[1] + "-blogImageAdress");
                        String postAuthorImageAdress = loadTextFile(currentUser + "-" + auth_postID_split[0] + "-authorImageAdress");

                        OfflinePost post = new OfflinePost( postAuthorNameToDisplay, postUploadTimeToDisplay,  postTitleToDisplay, postContentToDisplay,postImageAdress, postAuthorImageAdress);
                        downloadedPostList.add(post);




//                        Toast.makeText(getContext(), String.valueOf(j), Toast.LENGTH_LONG).show();
//                        Toast.makeText(getContext(), postContentToDisplay, Toast.LENGTH_LONG).show();
//                        Toast.makeText(getContext(), postTitleToDisplay, Toast.LENGTH_LONG).show();
//                        Toast.makeText(getContext(), postUploadTimeToDisplay, Toast.LENGTH_LONG).show();
//                        Toast.makeText(getContext(), postAuthorNameToDisplay, Toast.LENGTH_LONG).show();
//                        Toast.makeText(getContext(), postImageAdress, Toast.LENGTH_LONG).show();
//                        Toast.makeText(getContext(), postAuthorImageAdress, Toast.LENGTH_LONG).show();



                    }

                } catch (FileNotFoundException e) {
                    Toast.makeText(getContext(), "file not found", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                offlinePostAdapter = new OfflinePostAdapter(downloadedPostList, getActivity(),auth_postIDList);
                downloadRecyclerView.setAdapter(offlinePostAdapter);

            }

        });

        myBlogs_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //downloadRecyclerView.setVisibility(View.INVISIBLE);

                if(isNetworkAvailable()) {
                    myPostsList = new ArrayList<>();
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                    DatabaseReference db = FirebaseDatabase.getInstance().getReference("Posts/" + currentUser.getUid());
                    db.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot postSnapShot : snapshot.getChildren()) {

                                Post post = postSnapShot.getValue(Post.class);
                                post.setpostID(postSnapShot.getRef().getKey());
                                myPostsList.add(post);
                                postID = postSnapShot.getRef().getKey();
                                postIDList.add(postID);


                            }

                            myBlogsAdapter =new PostAdapter(myPostsList, getActivity(), postIDList);
                            downloadRecyclerView.setAdapter(myBlogsAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else {
                    Toast.makeText(getContext(), "You are not connected to Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });



        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String imageID = currentUser.getUid();
        sharedPreferences = getActivity().getSharedPreferences(scrapperPreference, Context.MODE_PRIVATE);


        String currentUserEmail = currentUser.getEmail().toString();
        if (sharedPreferences.getString(currentUserEmail + "-email", "") != "") {
            //  Toast.makeText(getActivity(), "Already loaded", Toast.LENGTH_SHORT).show();

            String userName = sharedPreferences.getString(currentUserEmail + "-name", "");
            String userEmail = sharedPreferences.getString(currentUserEmail + "-email", "");

            Bitmap bitmap = decodeBase64(sharedPreferences.getString(currentUserEmail + "-image", ""));
            profilePic.setImageBitmap(bitmap);
            profileName.setText(userName);
            profileEmail.setText(userEmail);
        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();

            // getting user email and name

            DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.getUid());
            db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    email = dataSnapshot.child("email").getValue().toString();
                    name = dataSnapshot.child("name").getValue().toString();


                    profileEmail.setText(email);
                    profileName.setText(name);

                    editor.putString(email + "-name", name);
                    editor.putString(email + "-email", email);
                    editor.apply();


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            // getting user image
            storageReference = FirebaseStorage.getInstance().getReference("images/" + imageID);
            try {
                File localFile = File.createTempFile("tempfile", ".jpg");
                Toast.makeText(getActivity(), "Fetching image", Toast.LENGTH_SHORT).show();
                //Toast.makeText(getActivity(),imageID,Toast.LENGTH_SHORT).show();
                storageReference.getFile(localFile)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());

                                profilePic.setImageBitmap(bitmap);


                                editor.putString(email + "-image", encodeTobase64(bitmap));
                                editor.apply();


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Fetching image failed", Toast.LENGTH_SHORT).show();
                    }
                });


            } catch (IOException e) {
                e.printStackTrace();
            }

            editor.commit();

        }

//        String storedName = sharedPreferences.getString(currentUser.getEmail()+"name","");
//        if(storedName!=null) {
//            profileName.setText(storedName);
//        }

        //    loadTextFile("hello");


        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        downloadRecyclerView.setLayoutManager(layoutManager);
        setHasOptionsMenu(true);
        return ProfileView;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Fetching the stored data
        // from the SharedPreference
//
//        String currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
//        String s1 = sharedPreferences.getString(currentUserEmail+"-name","");
//        if(s1!=null){
//          // profileName.setText(s1);
//        }
//
//        else {
//
//        }
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

    public String loadTextFile(String fileName) {
        fileName = fileName + ".txt";
        FileInputStream fis = null;

        String allText = "";
        try {
            fis = getContext().openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;


            while ((text = br.readLine()) != null) {
                allText += text;
            }


            //Toast.makeText(getContext(), allText, Toast.LENGTH_LONG).show();

            return allText;

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
        return allText;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}

