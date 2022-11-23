package com.example.javaceclientapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    FirebaseAuth firebaseAuth;
    DrawerLayout drawerLayout;
    FirebaseFirestore firestore;
    Dialog progressDialog;
    List<ExercisesModel> exercisesModels = new ArrayList<>();
    ExercisesAdapter exercisesAdapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Javace");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        progressDialog = new Dialog(MainActivity.this);
        progressDialog.setContentView(R.layout.loading_progressbar);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.progressbar_background);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


        recyclerView = findViewById(R.id.exerciseRecycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        fetchExercises();

    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchExercises();
    }

    private void fetchExercises() {
        progressDialog.show();
        firestore.collection("Exercises").orderBy("exercise_score", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                exercisesModels.clear();
                progressDialog.dismiss();

                for (DocumentSnapshot documentSnapshot: task.getResult()) {
                    ExercisesModel exercisesModel = new ExercisesModel(documentSnapshot.getString("eId"),
                            documentSnapshot.getString("exercise_title"),
                            documentSnapshot.getString("exercise_instruction"),
                            documentSnapshot.getString("exercise_content"),
                            documentSnapshot.getString("exercise_score"));
                    exercisesModels.add(exercisesModel);
                }
                exercisesAdapter = new ExercisesAdapter(MainActivity.this, exercisesModels);
                recyclerView.setAdapter(exercisesAdapter);
                exercisesAdapter.notifyItemInserted(exercisesModels.size());

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
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

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.feedback:
                redirectActivity(MainActivity.this, Feedback.class);
                break;

            case R.id.quiz:
                redirectActivity(MainActivity.this, Category.class);
                break;

            case R.id.resources:
                redirectActivity(MainActivity.this, ResourcesActivity.class);
                break;
            case R.id.forum:
                redirectActivity(MainActivity.this, Forum.class);
                break;
            case R.id.leaderboard:
                redirectActivity(MainActivity.this, Leaderboard.class);
                break;
            case R.id.profile:
                redirectActivity(MainActivity.this, Profile.class);
                break;
            case R.id.addForum:
                redirectActivity(MainActivity.this, AddForum.class);
                break;
            case R.id.logout:
                firebaseAuth.signOut();
                redirectActivity(MainActivity.this, LoginActivity.class);
                finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void redirectActivity(Activity activity, Class pointClass) {

        Intent intent = new Intent(activity,pointClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }
}