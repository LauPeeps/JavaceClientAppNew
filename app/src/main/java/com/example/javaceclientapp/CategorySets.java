package com.example.javaceclientapp;

import static com.example.javaceclientapp.SplashActivity.category_index;
import static com.example.javaceclientapp.SplashActivity.list;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CategorySets extends AppCompatActivity {
    GridView setsGridView;
    FirebaseFirestore firestore;
    Dialog progressDialog;
    public static List<String> idOfSets = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_sets);

        Toolbar toolbar = findViewById(R.id.toolbarSets);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(list.get(category_index).getCategory_name());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        setsGridView = findViewById(R.id.setsGridView);

        progressDialog = new Dialog(CategorySets.this);
        progressDialog.setContentView(R.layout.loading_progressbar);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.progressbar_background);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        progressDialog.show();

        firestore = FirebaseFirestore.getInstance();
        fetchSets();




    }

    private void fetchSets() {

        idOfSets.clear();

        firestore.collection("Quiz").document(list.get(category_index).getCategory_id())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        long noOfSets = (long) documentSnapshot.get("Submodule");

                        for (int i = 1; i <= noOfSets; i++) {
                            idOfSets.add(documentSnapshot.getString("Submodule" + String.valueOf(i) + "_Id"));

                        }

                        SetsAdapater setsAdapater= new SetsAdapater(idOfSets.size());
                        setsGridView.setAdapter(setsAdapater);

                        progressDialog.dismiss();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(CategorySets.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            CategorySets.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}