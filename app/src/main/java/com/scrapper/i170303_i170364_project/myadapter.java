package com.scrapper.i170303_i170364_project;


import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.icu.text.Normalizer2;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class myadapter extends RecyclerView.Adapter<myviewholder> implements Filterable
{
    ArrayList<Post> data;
    ArrayList<Post> backup;
    ArrayList<String> postIDList;
    Context context;

    public myadapter(ArrayList<Post> data, Context context,ArrayList<String> postIDList)

    {
        this.data = data;
        this.postIDList = postIDList;
        this.context=context;
        backup=new ArrayList<>(data);
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.singlerow,parent,false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final myviewholder holder, @SuppressLint("RecyclerView") int position)
    {
        final Post temp=data.get(position);


        String updatedPostTitle = data.get(position).getTitle();
        if(updatedPostTitle.length() > 20) {
            updatedPostTitle = updatedPostTitle.substring(0,20 ) + " ...";
        }
        holder.t1.setText(updatedPostTitle);


        holder.t2.setText(data.get(position).getTimeStamp());
        Picasso.get().load(data.get(position).getImageLink()).into(holder.img);

      //  holder.img.setImageResource(data.get(position).getImgname());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context,BlogPage.class);
                intent.putExtra("postID",postIDList.get(position));
              intent.putExtra("postimage",temp.getImageLink());
                intent.putExtra("title",temp.getTitle());
                intent.putExtra("uploadtime",temp.getTimeStamp());
                intent.putExtra("authorname", temp.getAuthor());
                intent.putExtra("content", temp.getContent());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }


    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter=new Filter() {
        @Override
        // background thread
        protected FilterResults performFiltering(CharSequence keyword)
        {
            ArrayList<Post> filtereddata=new ArrayList<>();

            if(keyword.toString().isEmpty())
                filtereddata.addAll(backup);
            else
            {
                for(Post obj : backup)
                {
                    if(obj.getTitle().toString().toLowerCase().contains(keyword.toString().toLowerCase()))
                        filtereddata.add(obj);
                }
            }

            FilterResults results=new FilterResults();
            results.values=filtereddata;
            return results;
        }

        @Override  // main UI thread
        protected void publishResults(CharSequence constraint, FilterResults results)
        {
            data.clear();
            data.addAll((ArrayList<Post>)results.values);
            notifyDataSetChanged();
        }
    };
}
