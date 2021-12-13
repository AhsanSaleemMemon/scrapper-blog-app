package com.scrapper.i170303_i170364_project;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Main2Activity extends AppCompatActivity
{
    ImageView img;
    TextView tv1,tv2;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);

        img=(ImageView)findViewById(R.id.blogimageview);
        tv1=(TextView)findViewById(R.id.blogtitle);
        tv2=(TextView)findViewById(R.id.blogtime);

        img.setImageResource(getIntent().getIntExtra("imagename",0));
        tv1.setText(getIntent().getStringExtra("header"));
        tv2.setText(getIntent().getStringExtra("desc"));
    }
}
