package com.example.javaceclientapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    FirebaseAuth firebaseAuth;
    DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        firebaseAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

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
            case R.id.settings:
                redirectActivity(MainActivity.this, SettingsActivity.class);
                break;
            case R.id.profile:
                redirectActivity(MainActivity.this, Profile.class);
                break;
            case R.id.progress:
                redirectActivity(MainActivity.this, Progress.class);
                break;

            case R.id.logout:
                firebaseAuth.signOut();
                redirectActivity(MainActivity.this, SplashActivity.class);
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