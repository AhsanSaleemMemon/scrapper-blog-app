package com.scrapper.i170303_i170364_project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    private FirebaseAuth mAuth;
    private TextView profileName, profileEmail;
    StorageReference storageReference;
    SharedPreferences sharedPreferences;
    String scrapperPreference;
    String email;
    String name;

    CircleImageView profilePic;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View ProfileView = inflater.inflate(R.layout.profile_fragment,null);
        MaterialButton logoutButton=ProfileView.findViewById(R.id.logout);
        profilePic=ProfileView.findViewById(R.id.profilepic);
        profileEmail = ProfileView.findViewById(R.id.profileemail);
        profileName = ProfileView.findViewById(R.id.profilename);
        mAuth=FirebaseAuth.getInstance();

        scrapperPreference = "scrapperPreference";
        String namePreferance="name";
        String imagePreferance="image";
        String emailPreferance = "email";

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String imageID = currentUser.getUid();
        sharedPreferences = getActivity().getSharedPreferences(scrapperPreference, Context.MODE_PRIVATE);


         String currentUserEmail = currentUser.getEmail().toString();
         if(sharedPreferences.getString(currentUserEmail+"-email", "")!="") {
           //  Toast.makeText(getActivity(), "Already loaded", Toast.LENGTH_SHORT).show();

             String userName = sharedPreferences.getString(currentUserEmail+"-name", "");
             String userEmail = sharedPreferences.getString(currentUserEmail+"-email","");

             Bitmap bitmap = decodeBase64(sharedPreferences.getString(currentUserEmail+"-image",""));
             profilePic.setImageBitmap(bitmap);
             profileName.setText(userName);
             profileEmail.setText(userEmail);
         }
         else {
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

                     editor.putString(email+"-name", name);
                     editor.putString(email+"-email", email);
                     editor.apply();


                 }

                 @Override
                 public void onCancelled(@NonNull DatabaseError databaseError) {

                 }
             });


             // getting user image
             storageReference=FirebaseStorage.getInstance().getReference("images/"+imageID);
             try {
                 File localFile= File.createTempFile("tempfile",".jpg");
                 Toast.makeText(getActivity(),"Fetching image",Toast.LENGTH_SHORT).show();
                 //Toast.makeText(getActivity(),imageID,Toast.LENGTH_SHORT).show();
                 storageReference.getFile(localFile)
                         .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                             @Override
                             public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                                 Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());

                                 profilePic.setImageBitmap(bitmap);



                                 editor.putString(email+"-image", encodeTobase64(bitmap));
                                 editor.apply();


                             }
                         }).addOnFailureListener(new OnFailureListener() {
                     @Override
                     public void onFailure(@NonNull Exception e) {
                         Toast.makeText(getActivity(),"Fetching image failed",Toast.LENGTH_SHORT).show();
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


}

