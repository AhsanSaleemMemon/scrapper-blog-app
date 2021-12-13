package com.scrapper.i170303_i170364_project;

import android.content.Context;
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

    public TodayReadPostAdapter(List<Post> list, Context c) {
        this.list = list;
        this.c = c;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {



        Picasso.get().load(list.get(position).getImageLink())
                .fit()
                .into(holder.postImage);




        holder.postTitle.setText(list.get(position).getTitle());
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
