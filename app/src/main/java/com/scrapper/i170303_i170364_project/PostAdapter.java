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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {


    private List<Post> list;
    private List<String> postIDList;
    Context c;

    public PostAdapter(List<Post> list, Context c, List<String> postIDList) {
        this.list = list;
        this.c = c;
        this.postIDList = postIDList;
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(c).inflate(R.layout.foryouarticles_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.postUploadTime.setText(list.get(position).getTimeStamp());


        Picasso.get().load(list.get(position).getImageLink()).into(holder.postImage);

        String updatedPostTitle = list.get(position).getTitle();
        if(updatedPostTitle.length() > 20) {
            updatedPostTitle = updatedPostTitle.substring(0,20 ) + " ...";
        }


        holder.postTitle.setText(updatedPostTitle);


        holder.itemView.setOnClickListener(new View.OnClickListener() {

            final Post post=list.get(position);

            @Override
            public void onClick(View v) {

                Intent intent=new Intent(c,BlogPage.class);
                intent.putExtra("postID",postIDList.get(position));
                intent.putExtra("postimage",post.getImageLink());
                intent.putExtra("title",post.getTitle());
                intent.putExtra("uploadtime",post.getTimeStamp());
                intent.putExtra("authorname", post.getAuthor());
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
        TextView authorName, postTitle, postContent, postUploadTime;
        ShapeableImageView postImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            postTitle = (TextView) itemView.findViewById(R.id.posttitle);
            postImage = itemView.findViewById(R.id.postimage);
            postUploadTime = itemView.findViewById(R.id.postuploadtime);

        }
    }
}
