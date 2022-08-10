package com.brainmedia.wallpapersstudio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.brainmedia.wallpapersstudio.Fragments.AnonymousCategory;
import com.brainmedia.wallpapersstudio.Fragments.ArtisticCategory;
import com.brainmedia.wallpapersstudio.Fragments.GamingCategory;
import com.brainmedia.wallpapersstudio.Fragments.NatureCategory;
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

public class MainActivity extends AppCompatActivity {

    private InterstitialAd dashBoardInter;
    private Boolean onBackPressed = false;
    private Toolbar toolbar;
    private Dialog dashBoardDialog;
    private RelativeLayout dashBannerCont;
    private CardView natureCard,artisticCrd,anonymousCard,gamingCard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        natureCard = findViewById(R.id.Nature_main_btn);
        artisticCrd = findViewById(R.id.artistic_main_btn);
        anonymousCard = findViewById(R.id.Anonymous_main_btn);
        gamingCard = findViewById(R.id.Games_main_btn);
        dashBannerCont = findViewById(R.id.dashboard_banner_container);

        // Initialize Ads
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        // Implement Bannner Ad
        dashBoardAds("https://wallpapers-studio-470ed-default-rtdb.firebaseio.com/mainBanner");
        // Implement Nature Interstitial Ad
        getNatureInter("https://wallpapers-studio-470ed-default-rtdb.firebaseio.com/natureInter");
        //Implement Artistic Interstetial
        getArtisticInter("https://wallpapers-studio-470ed-default-rtdb.firebaseio.com/artisticInter");
        //Implement Anonymous Interstetial
        getAnonymousInter("https://wallpapers-studio-470ed-default-rtdb.firebaseio.com/anonymousInter");
        //Implement Gaming Interstetial
        getGamingInter("https://wallpapers-studio-470ed-default-rtdb.firebaseio.com/gamingInter");


        // Adding Dialog
        dashBoardDialog = new Dialog(MainActivity.this);
        dashBoardDialog.setContentView(R.layout.custom_dialog);
        //Implement Toolbar
        toolbar = findViewById(R.id.main_toolbar);

        // Toolbar Listener
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getDialog();
                dashBoardDialog.show();

            }
        });

        // Nature Listener
        natureCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (dashBoardInter != null){

                    toolbar.setTitle("Nature Wallpapers");
                    dashBoardInter.show(MainActivity.this);
                    dashBoardInter.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            super.onAdDismissedFullScreenContent();

                            // Calling Nature Fragment
                            FragmentManager natureFragManager = getSupportFragmentManager();
                            NatureCategory natureFrag = new NatureCategory();
                            natureFragManager.beginTransaction().replace(R.id.layout_scroll,natureFrag).addToBackStack(null).commit();

                            onBackPressed = true;

                            // Implement Nature Interstitial Ad
                            getNatureInter("https://wallpapers-studio-470ed-default-rtdb.firebaseio.com/natureInter");

                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                            super.onAdFailedToShowFullScreenContent(adError);

                            Toast.makeText(MainActivity.this, "Error: "+adError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


                }



            }
        });

        // Artistic Listener
        artisticCrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (dashBoardInter != null){
                    toolbar.setTitle("Artistic Wallpapers");
                    dashBoardInter.show(MainActivity.this);
                    dashBoardInter.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            FragmentManager natureFragManager = getSupportFragmentManager();
                            natureFragManager.beginTransaction().replace(R.id.layout_scroll,new ArtisticCategory(),null).addToBackStack(null).commit();
                            //Implement Artistic Interstetial
                            getArtisticInter("https://wallpapers-studio-470ed-default-rtdb.firebaseio.com/artisticInter");
                            super.onAdDismissedFullScreenContent();
                            onBackPressed = true;
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                            super.onAdFailedToShowFullScreenContent(adError);

                            Toast.makeText(MainActivity.this, "Error: "+adError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


                }



            }
        });

        // Anonymous Listener
        anonymousCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (dashBoardInter != null){
                    toolbar.setTitle("Anonymous Wallpapers");
                    dashBoardInter.show(MainActivity.this);
                    dashBoardInter.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            FragmentManager anonymousFragManager = getSupportFragmentManager();
                            anonymousFragManager.beginTransaction().replace(R.id.layout_scroll,new AnonymousCategory(),null).addToBackStack(null).commit();
                            //Implement Anonymous Interstetial
                            getAnonymousInter("https://wallpapers-studio-470ed-default-rtdb.firebaseio.com/anonymousInter");
                            super.onAdDismissedFullScreenContent();
                            onBackPressed = true;
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                            super.onAdFailedToShowFullScreenContent(adError);

                            Toast.makeText(MainActivity.this, "Error: "+adError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


                }



            }
        });

        // Gaming Listener
        gamingCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (dashBoardInter != null){
                    toolbar.setTitle("Gaming Wallpapers");
                    dashBoardInter.show(MainActivity.this);
                    dashBoardInter.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            FragmentManager anonymousFragManager = getSupportFragmentManager();
                            anonymousFragManager.beginTransaction().replace(R.id.layout_scroll,new GamingCategory(),null).addToBackStack(null).commit();
                            //Implement Gaming Interstetial
                            getGamingInter("https://wallpapers-studio-470ed-default-rtdb.firebaseio.com/gamingInter");

                            onBackPressed = true;
                            super.onAdDismissedFullScreenContent();
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                            super.onAdFailedToShowFullScreenContent(adError);

                            Toast.makeText(MainActivity.this, "Error: "+adError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


                }



            }
        });




    }


    private void dashBoardAds(String bannerId) {
        Firebase.setAndroidContext(this);

        Firebase firebase = new Firebase(bannerId);

        firebase.addValueEventListener(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {

                String data = dataSnapshot.getValue(String.class);

                AdView bannerAd = new AdView(MainActivity.this);
                bannerAd.setAdUnitId(data);
                dashBannerCont.addView(bannerAd);
                bannerAd.setAdSize(AdSize.SMART_BANNER);
                AdRequest adRequest = new AdRequest.Builder().build();
                bannerAd.loadAd(adRequest);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    // Initializing Dialogue
    private void getDialog() {


        dashBoardDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT );
        dashBoardDialog.setCancelable(false);

        ImageButton share_btn = dashBoardDialog.findViewById(R.id.share_btn);
        Button close_btn = dashBoardDialog.findViewById(R.id.close_btn);
        TextView contact_txt = dashBoardDialog.findViewById(R.id.email);

        share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareApp();

            }
        });

        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dashBoardDialog.dismiss();
            }
        });

    }

    // Initialize Share Ap Function
    private void shareApp() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String link = "http://play.google.com";
        String desc = "Download the App";
        shareIntent.putExtra(Intent.EXTRA_TEXT,desc);
        shareIntent.putExtra(Intent.EXTRA_TEXT,link);
        startActivity(Intent.createChooser(shareIntent,"Share Via"));

    }



    // Implement Double Back Pressed
    @Override
    public void onBackPressed() {

        if (onBackPressed){
            super.onBackPressed();
        }
        else {
            onBackPressed = true;
            Toast.makeText(this, "Press Again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onBackPressed = false;
                }
            },3000);
        }



    }

    // Adding Interstitial Ad
    private void getNatureInter(String Naturead_Id){

        Firebase.setAndroidContext(MainActivity.this);
        Firebase interFirebase = new Firebase(Naturead_Id);

        interFirebase.addValueEventListener(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                String natureinterData = dataSnapshot.getValue(String.class);
                setNatureInterstetial(natureinterData);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

                Toast.makeText(MainActivity.this, "Error: "+firebaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    // Set Nature Interstitial Ad
    private void setNatureInterstetial(String NatureinterstetialId) {

        AdRequest adRequest = new AdRequest.Builder().build();

        dashBoardInter.load(this, NatureinterstetialId, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);

                dashBoardInter = null;
                Toast.makeText(MainActivity.this, "Error: "+loadAdError.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);

                dashBoardInter = interstitialAd;

            }
        });

    }






    // Get Artistic Interstetial
    private void getArtisticInter(String artAdId) {

        Firebase.setAndroidContext(MainActivity.this);
        Firebase interFirebase = new Firebase(artAdId);

        interFirebase.addValueEventListener(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                String artisticinterData = dataSnapshot.getValue(String.class);
                setArtisticInterstetial(artisticinterData);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(MainActivity.this, "Error: "+firebaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }});


    }

    // Set Artistic Interstetial Ad
    private void setArtisticInterstetial(String ArtinterstetialId) {

        AdRequest adRequest = new AdRequest.Builder().build();

        dashBoardInter.load(this, ArtinterstetialId, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);

                dashBoardInter = null;
                Toast.makeText(MainActivity.this, "Error: "+loadAdError.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);

                dashBoardInter = interstitialAd;

            }
        });

    }


    // Get Anonymous Interstetial
    private void getAnonymousInter(String anonymousAdId) {

        Firebase.setAndroidContext(MainActivity.this);
        Firebase interFirebase = new Firebase(anonymousAdId);

        interFirebase.addValueEventListener(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                String anonymousInterData = dataSnapshot.getValue(String.class);
                setAnonymousInterstetial(anonymousInterData);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(MainActivity.this, "Error: "+firebaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }     });


    }

    // Set Anonymous Interstetial Ad
    private void setAnonymousInterstetial(String AnonymousinterstetialId) {

        AdRequest adRequest = new AdRequest.Builder().build();

        dashBoardInter.load(this, AnonymousinterstetialId, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);

                dashBoardInter = null;
                Toast.makeText(MainActivity.this, "Error: "+loadAdError.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);

                dashBoardInter = interstitialAd;

            }
        });

    }



    // Get Gaming Interstetial
    private void getGamingInter(String gamingAdId) {

        Firebase.setAndroidContext(MainActivity.this);
        Firebase interFirebase = new Firebase(gamingAdId);

        interFirebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                String gamingInterData = dataSnapshot.getValue(String.class);
                setGamingInterstetial(gamingInterData);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(MainActivity.this, "Error: "+firebaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }});


    }

    // Set Gaming Interstetial Ad
    private void setGamingInterstetial(String GaminginterstetialId) {

        AdRequest adRequest = new AdRequest.Builder().build();

        dashBoardInter.load(this, GaminginterstetialId, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);

                dashBoardInter = null;
                Toast.makeText(MainActivity.this, "Error: "+loadAdError.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);

                dashBoardInter = interstitialAd;

            }
        });

    }
}