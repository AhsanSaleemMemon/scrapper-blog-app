package com.scrapper.i170303_i170364_project;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class TodayReadPostAdapter extends RecyclerView.Adapter<TodayReadPostAdapter.ViewHolder> {


    private List<Post> list;
    Context c;
    private List<String> sortedPostIDList;
    public TodayReadPostAdapter(List<Post> list, Context c, List<String> sortedPostIDList) {
        this.list = list;
        this.c = c;
        this.sortedPostIDList = sortedPostIDList;
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    @NonNull
    @Override
    public TodayReadPostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(c).inflate(R.layout.todaysreadarticles_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {



        Picasso.get().load(list.get(position).getImageLink())
                .fit()
                .into(holder.postImage);

        String updatedPostTitle = list.get(position).getTitle();
        if(updatedPostTitle.length() > 20) {
            updatedPostTitle = updatedPostTitle.substring(0,20 ) + " ...";
        }
        holder.postTitle.setText(updatedPostTitle);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


             //   Toast.makeText(c, sortedPostIDList.get(position), Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(c,BlogPage.class);
                intent.putExtra("postID",sortedPostIDList.get(position));
                intent.putExtra("postimage",list.get(position).getImageLink());
                intent.putExtra("title",list.get(position).getTitle());
                intent.putExtra("uploadtime",list.get(position).getTimeStamp());
                intent.putExtra("authorname", list.get(position).getAuthor());
                intent.putExtra("content", list.get(position).getContent());
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


        }
    }
}
