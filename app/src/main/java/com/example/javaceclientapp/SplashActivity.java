package com.example.javaceclientapp;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 5000;
    public static List<CategoryModel> list = new ArrayList<>();
    public static int category_index = 0;
    FirebaseFirestore firestore;

    Animation animation;
    ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        animation = AnimationUtils.loadAnimation(this, R.anim.myanim);

        image = findViewById(R.id.logo);

        image.setAnimation(animation);

        firestore = FirebaseFirestore.getInstance();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchData();
            }
        },SPLASH_SCREEN);

    }

    private void fetchData() {

        list.clear();

        firestore.collection("Quiz").document("Module").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        long count = (long) documentSnapshot.get("Exist");

                        for (int i = 1; i<= count; i++) {
                            String categoryName = documentSnapshot.getString("Module"+String.valueOf(i) + "_name");
                            String categoryId = documentSnapshot.getString("Module"+String.valueOf(i) + "_Id");
                            list.add(new CategoryModel(categoryId, categoryName));
                        }
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        SplashActivity.this.finish();
                    }
                    else {
                        Toast.makeText(SplashActivity.this, "No data exist", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                else {
                    Toast.makeText(SplashActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}