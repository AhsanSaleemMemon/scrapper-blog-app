package com.scrapper.i170303_i170364_project;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.faltenreich.skeletonlayout.Skeleton;
import com.faltenreich.skeletonlayout.SkeletonLayoutUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class HomeFragment extends Fragment {


    private  RecyclerView forYouRecyclerView;
    private RecyclerView todayReadRecyclerView;
    private PostAdapter postAdapter;
    private TodayReadPostAdapter todayReadPostAdapter;
    ArrayList<String> postIDList;
    ArrayList<String> sortedPostIDList;
    String postID;
    private List<Post> forYouList;
    private Skeleton todayReadSkeleton, forYouSkeleton;
    private List<Post> todayReadList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FloatingActionButton createFAB;

        sortedPostIDList = new ArrayList<>();
        postIDList = new ArrayList<>();
        postID = "";



        forYouList = new ArrayList<>();
        todayReadList = new ArrayList<>();

        View HomeView = inflater.inflate(R.layout.fragment_home,null);
        createFAB=HomeView.findViewById(R.id.fab);
        createFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),CreateBlogActivity.class);
                startActivity(intent);
            }
        });

     //   skeleton = HomeView.findViewById(R.id.skeletonLayout);

        forYouRecyclerView = HomeView.findViewById(R.id.foryou_recyclerview);
        forYouRecyclerView.setLayoutManager(new LinearLayoutManager(HomeView.getContext()));

        todayReadRecyclerView = HomeView.findViewById(R.id.todayread_recyclerview);
        todayReadRecyclerView.setLayoutManager(new LinearLayoutManager(HomeView.getContext(), LinearLayoutManager.HORIZONTAL, true));


        todayReadSkeleton = SkeletonLayoutUtils.applySkeleton(todayReadRecyclerView, R.layout.todaysreadarticles_layout);
        todayReadSkeleton.showSkeleton();

        forYouSkeleton = SkeletonLayoutUtils.applySkeleton(forYouRecyclerView, R.layout.foryouarticles_layout);
        forYouSkeleton.showSkeleton();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Posts");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapShot : snapshot.getChildren()) {
                    for (DataSnapshot postSnapShot: userSnapShot.getChildren()) {
                        Post post = postSnapShot.getValue(Post.class);

                        forYouList.add(post);
                        postID = postSnapShot.getRef().getKey();
                        postIDList.add(postID);
                    }

                }

                todayReadList = forYouList;
                Collections.sort(todayReadList, new Comparator<Post>() {
                    public int compare(Post p1, Post p2) {
                        return p2.getTimeStamp().compareTo(p1.getTimeStamp());
                    }
                });

                List<Post> updatedTodayReadList = new ArrayList<>();
                for(int i=4;i>=0;i--) {
                    updatedTodayReadList.add(todayReadList.get(i));
                }

                for (int i=4;i>=0;i--){
                    sortedPostIDList.add(
                            postIDList.get(forYouList.
                                    indexOf(updatedTodayReadList.get(i)))
                    );
                }



                todayReadPostAdapter = new TodayReadPostAdapter(updatedTodayReadList, getActivity(), sortedPostIDList);
                todayReadRecyclerView.setAdapter(todayReadPostAdapter);
                todayReadRecyclerView.scrollToPosition(updatedTodayReadList.size()-1);

                postAdapter=new PostAdapter(forYouList, getActivity(), postIDList);
                forYouRecyclerView.setAdapter(postAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        forYouRecyclerView.setAdapter(postAdapter);
        return HomeView;

    }

    @Override
    public void onResume() {

        super.onResume();
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.getSupportActionBar().hide();
        }
    }

    private void onDataLoaded() {
        todayReadSkeleton.showOriginal();
        forYouSkeleton.showOriginal();
    }
}