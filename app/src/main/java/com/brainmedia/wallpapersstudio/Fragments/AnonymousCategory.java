package com.brainmedia.wallpapersstudio.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.brainmedia.wallpapersstudio.Adapters.WallpaperAdapter;
import com.brainmedia.wallpapersstudio.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class AnonymousCategory extends Fragment {

    private Toolbar anonymousToolbar;
    private ProgressBar anonymousProg;
    private DatabaseReference anonymousRef;
    private RecyclerView anonymousRV;
    private ArrayList<String> anonymousLIst;
    private WallpaperAdapter anonymousAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_anonymous_category, container, false);

        anonymousRef = FirebaseDatabase.getInstance().getReference().child("Anonymous");

        anonymousProg = view.findViewById(R.id.anonymousProgress);
        anonymousRV = view.findViewById(R.id.anonymousRecycler);
        getAnonymousData();

        return view;
    }

    private void getAnonymousData() {

        anonymousRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                anonymousProg.setVisibility(View.GONE);
                anonymousLIst = new ArrayList<>();


                for(DataSnapshot shot: snapshot.getChildren()){

                    String artisticdata= shot.getValue().toString();
                    anonymousLIst.add(artisticdata);

                }

                anonymousRV.setLayoutManager(new GridLayoutManager(getContext(),2));
                anonymousRV.setHasFixedSize(true);
                anonymousRV.setItemViewCacheSize(13);
                anonymousAdapter = new WallpaperAdapter(getContext(),anonymousLIst);
                anonymousRV.setAdapter(anonymousAdapter);
                anonymousProg.setVisibility(View.GONE);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                anonymousProg.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}