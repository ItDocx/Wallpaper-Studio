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


public class NatureCategory extends Fragment {
    private Toolbar toolbar;
    private ProgressBar natureProgress;
    private DatabaseReference natureRef;
    private RecyclerView natureRV;
    private ArrayList<String> natureLIst;
    private WallpaperAdapter natureAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nature_category, container, false);
        natureRef = FirebaseDatabase.getInstance().getReference().child("Nature");

        natureProgress = view.findViewById(R.id.natureProgress);
        natureRV = view.findViewById(R.id.natureRecycler);
        toolbar = view.findViewById(R.id.main_toolbar);




        getNatreData();

        return view;
    }

    private void getNatreData() {

        natureRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                natureProgress.setVisibility(View.GONE);
                natureLIst = new ArrayList<>();

                for(DataSnapshot shot: snapshot.getChildren()){

                    String data= shot.getValue().toString();
                    natureLIst.add(data);

                }

                natureRV.setLayoutManager(new GridLayoutManager(getContext(),2));
                natureRV.setHasFixedSize(true);
                natureRV.setItemViewCacheSize(13);
                natureAdapter= new WallpaperAdapter(getContext(),natureLIst);
                natureRV.setAdapter(natureAdapter);
                natureProgress.setVisibility(View.GONE);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                natureProgress.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}