package com.scrapper.i170303_i170364_project;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class OfflinePostAdapter extends RecyclerView.Adapter<OfflinePostAdapter.ViewHolder> {


    private List<OfflinePost> list;
    private List<String> auth_postIDList;


    Context c;

    public OfflinePostAdapter(List<OfflinePost> list, Context c, ArrayList<String> auth_postIDList) {
        this.list = list;
        this.c = c;
        this.auth_postIDList =auth_postIDList;

    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    @NonNull
    @Override
    public OfflinePostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(c).inflate(R.layout.foryouarticles_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfflinePostAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.postUploadTime.setText(list.get(position).getUploadTime());

        String path_imageName = list.get(position).getPostImageAdress();
        String[] path_imageName_split = path_imageName.split(",");


        try {
            File f=new File(path_imageName_split[0], path_imageName_split[1]+".jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));

            holder.postImage.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
//        Picasso.get()
//                .load(list.get(position).getImageLink())
//                .fit()
//                .into(holder.postImage);

        String updatedPostTitle = list.get(position).getTitle();
        if(updatedPostTitle.length() > 25) {
            updatedPostTitle = updatedPostTitle.substring(0,25 ) + " ...";
        }


        holder.postTitle.setText(updatedPostTitle);

        int postWordCount = countWordsUsingSplit(list.get(position).getContent());

        if(postWordCount > 200) {
            int mins = postWordCount/200;
            holder.postReadTime.setText(Integer.toString(mins) + " min read" );

        }
        else {
            holder.postReadTime.setText("1 min read");
        }




        holder.itemView.setOnClickListener(new View.OnClickListener() {

            final OfflinePost post=list.get(position);

            @Override
            public void onClick(View v) {

                Intent intent=new Intent(c,OfflineBlogPage.class);
                //intent.putExtra("postID",post.getpostID());
                intent.putExtra("authorimage",post.getPostAuthorImageAdress());
                intent.putExtra("postimage",post.getPostImageAdress());
                intent.putExtra("title",post.getTitle());
                intent.putExtra("uploadtime",post.getUploadTime());
                intent.putExtra("authorname", post.getAuthorName());
                intent.putExtra("content", post.getContent());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                c.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView authorName, postTitle, postContent, postUploadTime, postReadTime;
        ShapeableImageView postImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            postTitle = (TextView) itemView.findViewById(R.id.posttitle);
            postImage = itemView.findViewById(R.id.postimage);
            postUploadTime = itemView.findViewById(R.id.postuploadtime);
            postReadTime = itemView.findViewById(R.id.postreadtime);

        }
    }

    public static int countWordsUsingSplit(String input) {
        if (input == null || input.isEmpty()) {
            return 0;
        }

        String[] words = input.split("\\s+");
        return words.length;
    }





}
