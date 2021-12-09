package com.scrapper.i170303_i170364_project;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FloatingActionButton createFAB;


        View HomeView = inflater.inflate(R.layout.fragment_home,null);
        createFAB=HomeView.findViewById(R.id.fab);
        createFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),CreateBlogActivity.class);
                startActivity(intent);
            }
        });
        return HomeView;

    }
}