package com.brainmedia.wallpapersstudio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.io.IOException;

public class ShowData extends AppCompatActivity {
    private ImageView fullImageView;
    private Button apply_Btn;
    private RelativeLayout fullscreenBannerContainer;
    InterstitialAd mInterstitialAd;
    private Toolbar toolbar;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);
        // Initialize Dialog
        dialog = new Dialog(ShowData.this);
        dialog.setContentView(R.layout.custom_dialog);

        // Toolbar Actions
        toolbar = findViewById(R.id.fullScreen_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        /*        // Add Action
                FragmentTransaction fragTransaction = getSupportFragmentManager().beginTransaction();
                fragTransaction.replace(R.id.main_container,new Aboutus()).commit();
                fragTransaction.disallowAddToBackStack();
         */
                getDialog();
                dialog.show();

            }
        });



        // Layout for Banner
        fullscreenBannerContainer =findViewById(R.id.fullScreen_Ad);

        // Initialize Full Scr een Activity Banner
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        // Getting Ad Function
        getBannerAd("https://wallpapers-studio-470ed-default-rtdb.firebaseio.com/banner2");



        // Interstetial Ad

        getInterstetial("https://wallpapers-studio-470ed-default-rtdb.firebaseio.com/Interstetial");

        fullImageView = findViewById(R.id.fullImage);
        apply_Btn = findViewById(R.id.apply_btn);

        // Getting Image from Glide
        Glide.with(this).load(getIntent().getStringExtra("image")).into(fullImageView);

        // Apply Btn Listener
        apply_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mInterstitialAd!=null){

                    mInterstitialAd.show(ShowData.this);
                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {

                            getInterstetial("https://wallpapers-studio-470ed-default-rtdb.firebaseio.com/Interstetial");
                            setBackground();

                            super.onAdDismissedFullScreenContent();
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                            super.onAdFailedToShowFullScreenContent(adError);

                            Toast.makeText(ShowData.this, "Error: "+adError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });



    }

    // About Us Dialog
    private void getDialog() {


        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT );
        dialog.setCancelable(false);

        ImageButton share_btn = dialog.findViewById(R.id.share_btn);
        Button close_btn = dialog.findViewById(R.id.close_btn);
        TextView contact_txt = dialog.findViewById(R.id.email);

        share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareApp();

            }
        });

        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    // Dialogue share Button function
    private void shareApp() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String link = "http://play.google.com";
        String desc = "Download the App";
        shareIntent.putExtra(Intent.EXTRA_TEXT,desc);
        shareIntent.putExtra(Intent.EXTRA_TEXT,link);
        startActivity(Intent.createChooser(shareIntent,"Share Via"));

    }

// Banner Ad

    public void getBannerAd(String ad_Id){

        Firebase.setAndroidContext(ShowData.this);
        Firebase showAdfireBase = new Firebase(ad_Id);

        showAdfireBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String show_Ad = dataSnapshot.getValue(String.class);

                AdView showBanner = new AdView(ShowData.this);
                showBanner.setAdUnitId(show_Ad);
                fullscreenBannerContainer.addView(showBanner);
                showBanner.setAdSize(AdSize.SMART_BANNER);
                AdRequest adRequest = new AdRequest.Builder().build();
                showBanner.loadAd(adRequest);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



    }



    // Set Background to the Screen
    private void setBackground() {
        // Getting Bitmap
        Bitmap bitmap = ((BitmapDrawable)fullImageView.getDrawable()).getBitmap();
        // Set Wallpaper
        WallpaperManager manager = WallpaperManager.getInstance(getApplicationContext());

        try {

            // Wallpaper Set
            manager.setBitmap(bitmap);
            Toast.makeText(this, "Wallpaper Applied", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            //Error Message
            Toast.makeText(this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    private void getInterstetial(String ad_Id){

        Firebase.setAndroidContext(ShowData.this);
        Firebase interFirebase = new Firebase(ad_Id);

        interFirebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String interData = dataSnapshot.getValue(String.class);
                setInterstetial(interData);


            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

                Toast.makeText(ShowData.this, "Error: "+firebaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setInterstetial(String interstetialId) {

        AdRequest adRequest = new AdRequest.Builder().build();

        mInterstitialAd.load(this, interstetialId, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);

                mInterstitialAd = null;
                Toast.makeText(ShowData.this, "Error: "+loadAdError.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);

                mInterstitialAd = interstitialAd;

            }
        });

    }

}