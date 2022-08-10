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


public class ArtisticCategory extends Fragment {

    private ProgressBar artisticProg;
    private DatabaseReference artisticRef;
    private RecyclerView artisticRV;
    private ArrayList<String> artisticLIst;
    private WallpaperAdapter artisticAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_artistic_category, container, false);
        artisticRef = FirebaseDatabase.getInstance().getReference().child("Artistic");

        artisticProg = view.findViewById(R.id.artisticProgress);
        artisticRV = view.findViewById(R.id.artisticRecycler);

        getArtisticData();
        return view;
    }

    private void getArtisticData() {



        artisticRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                artisticProg.setVisibility(View.GONE);
                artisticLIst = new ArrayList<>();

                for(DataSnapshot shot: snapshot.getChildren()){

                    String artisticdata= shot.getValue().toString();
                    artisticLIst.add(artisticdata);

                }

                artisticRV.setLayoutManager(new GridLayoutManager(getContext(),2));
                artisticRV.setHasFixedSize(true);
                artisticRV.setItemViewCacheSize(13);
                artisticAdapter = new WallpaperAdapter(getContext(),artisticLIst);
                artisticRV.setAdapter(artisticAdapter);
                artisticProg.setVisibility(View.GONE);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                artisticProg.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

}