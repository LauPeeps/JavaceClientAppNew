package com.example.javaceclientapp;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;



public class MainActivity extends AppCompatActivity{

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    Dialog progressDialog;
    ImageView logout, goToProfile, goToQuiz, goToResources, goToLeaderboard, goToFeedback, goToForum, goToAddForum, goToCompiler;
    TextView userName;
    String currentUser;
    static String userNow;
    static Long currentValueProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new Dialog(MainActivity.this);
        progressDialog.setContentView(R.layout.loading_progressbar);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.progressbar_background);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        logout = findViewById(R.id.logoutBtn);
        goToProfile = findViewById(R.id.goToProfile);
        goToQuiz = findViewById(R.id.goToQuiz);
        goToResources = findViewById(R.id.goToResources);
        goToLeaderboard = findViewById(R.id.goToLeaderboard);
        goToFeedback = findViewById(R.id.goToFeedback);
        goToForum = findViewById(R.id.goToForum);
        goToAddForum = findViewById(R.id.goToAddForum);
        goToCompiler = findViewById(R.id.goToCompiler);

        userName = findViewById(R.id.userName);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                firebaseAuth.signOut();
            }
        });

        goToCompiler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(MainActivity.this, CompilerActivity.class);
            }
        });

        goToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(MainActivity.this, Profile.class);
            }
        });

        goToQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(MainActivity.this, Module.class);
            }
        });

        goToResources.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(MainActivity.this, ResourcesActivity.class);
            }
        });

        goToLeaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(MainActivity.this, Leaderboard.class);
            }
        });
        goToFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(MainActivity.this, Feedback.class);
            }
        });

        goToForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(MainActivity.this, Forum.class);
            }
        });
        goToAddForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(MainActivity.this, AddForum.class);
            }
        });

        currentUser = firebaseAuth.getCurrentUser().getUid();
        DocumentReference documentReference = firestore.collection("Users").document(currentUser);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                userName.setText(value.getString("username"));
            }
        });

        userNow = firebaseAuth.getCurrentUser().getUid();






    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String uid = firebaseUser.getUid();
        firestore.collection("Users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()) {
                    //
                } else {
                    firebaseAuth.signOut();
                    redirectActivity(MainActivity.this, LoginActivity.class);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void redirectActivity(Activity activity, Class pointClass) {

        Intent intent = new Intent(activity,pointClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }
}