package com.scrapper.i170303_i170364_project;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class myviewholder extends RecyclerView.ViewHolder
{
    ImageView img;
    TextView t1,t2;
    View mView;
    public myviewholder(@NonNull View itemView)
    {
        super(itemView);
        img=(ImageView)itemView.findViewById(R.id.postimage);
        t1=(TextView)itemView.findViewById(R.id.posttitle);
        t2=(TextView)itemView.findViewById(R.id.postuploadtime);

        mView= itemView;
    }
}
