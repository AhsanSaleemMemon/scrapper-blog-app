package com.scrapper.i170303_i170364_project;

import static android.content.ContentValues.TAG;
import static android.graphics.BlendMode.COLOR;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class SearchFragment extends Fragment {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    RecyclerView rcv;
    myadapter adapter;
    ArrayList<SearchModel> list =new ArrayList<SearchModel>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View SearchView = inflater.inflate(R.layout.recyclerview, null);
        //ActionBar bar = SearchView.getActionBar();
        //bar.setBackgroundDrawable(new ColorDrawable("COLOR"));

        rcv = SearchView.findViewById(R.id.recview);
        //  rcv.setLayoutManager(new LinearLayoutManager(this));




        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("Posts");
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot: snapshot.getChildren()){
                    for (DataSnapshot postSnapshot: userSnapshot.getChildren()){
                        Post post=postSnapshot.getValue(Post.class);
                        SearchModel sModel = new SearchModel(post.getTitle(),post.getTimeStamp(),post.getImageLink());
                        list.add(sModel);

                    }
                }
                adapter = new myadapter(list,getContext());
                rcv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("bigfails", "onCancelled", databaseError.toException());
            }
        });




        //LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        //rcv.setLayoutManager(layoutManager);

        GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),2);
        rcv.setLayoutManager(gridLayoutManager);
        setHasOptionsMenu(true);





        return SearchView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.mainmenu,menu);
        MenuItem item=menu.findItem(R.id.search_menu);

        SearchView searchView=(SearchView)item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu,inflater);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onResume() {

        super.onResume();
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {

            activity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
            activity.getSupportActionBar().show();
        }
    }

}
