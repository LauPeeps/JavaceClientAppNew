package com.example.javaceclientapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.collection.ArrayMap;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.javaceclientapp.SubmoduleAdapter;
import com.example.javaceclientapp.SubmoduleModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Submodule extends AppCompatActivity {

    Dialog progressDialog;
    String moduleId, moduleName;
    Long submodules;
    FirebaseFirestore firestore;
    List<SubmoduleModel> submoduleModelList = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    SubmoduleAdapter submoduleAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submodule);

        Bundle bundle = getIntent().getExtras();
        moduleId = bundle.getString("moduleid");
        moduleName = bundle.getString("modulename");
        submodules = bundle.getLong("submodules");

        firestore = FirebaseFirestore.getInstance();

        Toolbar toolbar = findViewById(R.id.submodule_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(moduleName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        progressDialog = new Dialog(Submodule.this);
        progressDialog.setContentView(R.layout.loading_progressbar);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.progressbar_background);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        recyclerView = findViewById(R.id.submodule_recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        fetchSubmodules();

    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchSubmodules();
    }


    private void fetchSubmodules() {

        progressDialog.show();
        firestore.collection("Quizzes").document(moduleId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                submoduleModelList.clear();
                long existingSubs = documentSnapshot.getLong("submodules");
                for (int i  = 1; i <= existingSubs; i++) {
                    SubmoduleModel submoduleModel = new SubmoduleModel(documentSnapshot.getString("submodule" + String.valueOf(i) + "_id"),
                            documentSnapshot.getString("submodule" + String.valueOf(i) + "_preview"));
                    submoduleModelList.add(submoduleModel);
                }
                progressDialog.dismiss();
                submoduleAdapter = new SubmoduleAdapter(Submodule.this, submoduleModelList);
                recyclerView.setAdapter(submoduleAdapter);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}