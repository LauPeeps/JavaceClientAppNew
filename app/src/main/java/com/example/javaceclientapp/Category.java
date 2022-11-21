package com.example.javaceclientapp;

import static com.example.javaceclientapp.SplashActivity.list;


import android.os.Bundle;
import android.view.MenuItem;
import android.widget.GridView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class
Category extends AppCompatActivity {
    GridView gridView;
    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);


        Toolbar toolbar = findViewById(R.id.toolbarCategory);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Modules");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        gridView = findViewById(R.id.categoryGridView);

        firestore = FirebaseFirestore.getInstance();

        CategoryGridAdapter categoryGridAdapter = new CategoryGridAdapter(list);

        gridView.setAdapter(categoryGridAdapter);


    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            Category.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}