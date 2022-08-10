package com.brainmedia.wallpapersstudio.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
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
import java.util.Objects;


public class GamingCategory extends Fragment {

    private ProgressBar gamingProg;
    private DatabaseReference gamingRef;
    private RecyclerView gamingRV;
    private ArrayList<String> gamingLIst;
    private WallpaperAdapter gamingAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gaming_category, container, false);
        gamingRef = FirebaseDatabase.getInstance().getReference().child("Gaming");

        gamingProg = view.findViewById(R.id.gamingProgress);
        gamingRV = view.findViewById(R.id.gamingRecycler);

        getGamingData();

        return view;
    }

    private void getGamingData() {

        gamingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gamingProg.setVisibility(View.GONE);
                gamingLIst = new ArrayList<>();

                for(DataSnapshot shot: snapshot.getChildren()){

                    String gamingData= Objects.requireNonNull(shot.getValue()).toString();
                    gamingLIst.add(gamingData);

                }

                gamingRV.setLayoutManager(new GridLayoutManager(getContext(),2));
                gamingRV.setHasFixedSize(true);
                gamingRV.setItemViewCacheSize(13);
                gamingAdapter = new WallpaperAdapter(getContext(),gamingLIst);
                gamingRV.setAdapter(gamingAdapter);
                gamingProg.setVisibility(View.GONE);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                gamingProg.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}