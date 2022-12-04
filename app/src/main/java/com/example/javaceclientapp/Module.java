package com.example.javaceclientapp;

import static com.example.javaceclientapp.MainActivity.userNow;
import static com.example.javaceclientapp.Submodule.moduleName;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import java.util.Map;
import java.util.Objects;

public class Module extends AppCompatActivity {
    Dialog progressDialog;
    FirebaseFirestore firestore;
    static  String userNow;
    ProgressBar progressBar;
    List<Long> overall = new ArrayList<>();
    TextView overallText;


    List<ModuleModel> moduleModelList = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ModuleAdapter moduleAdapter;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.module_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Modules");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new Dialog(Module.this);
        progressDialog.setContentView(R.layout.loading_progressbar);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.progressbar_background);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        recyclerView = findViewById(R.id.module_recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        userNow = firebaseAuth.getCurrentUser().getUid();
        fetchModules();

        progressBar = findViewById(R.id.overallProgress);
        overallText = findViewById(R.id.overallProgressText);


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchModules();
    }


     void fetchOverallProgress() {
        firestore.collection("Users").document(userNow).collection(userNow).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                overall.clear();
                int total = 0;
                for (DocumentSnapshot documentSnapshot : task.getResult()) {
                    if (documentSnapshot.exists()) {
                         Long progressInFs = documentSnapshot.getLong("progress");
                         overall.add(progressInFs); //100 66
                    }
                }
                for (int i = 0; i < overall.size(); i++) {
                    total += overall.get(i);
                    int totalOf = total / overall.size();
                    progressBar.setProgress(totalOf);
                    overallText.setText(String.valueOf(totalOf) + "%");
                }
            }
        });
    }


     void createUserCollection(int pos, String id, String name) {
         DocumentReference documentReference = firestore.collection("Quizzes").document(id).collection(userNow).document("Progress_List");
         DocumentReference documentReference1 = firestore.collection("Users").document(userNow).collection(userNow).document(id);

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (!documentSnapshot.exists()) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("submodules", 0);
                    data.put("solve_exercise", "no");

                    documentReference.set(data);

                    documentReference1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (!documentSnapshot.exists()) {
                                Map<String, Object> data = new HashMap<>();
                                data.put("module_id", id);
                                data.put("module_name", name);
                                data.put("progress", 0);
                                documentReference1.set(data);
                            }
                        }
                    });
                }
            }
        });
    }


   /* void startProgress(String mid, int pos) {
        DocumentReference documentReference = firestore.collection("Quizzes").document(mid).collection(userNow).document(mid);

        Map<String, Object> data = new HashMap<>();
        data.put("null", "null");

       documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
           @Override
           public void onComplete(@NonNull Task<DocumentSnapshot> task) {

               if (task.isSuccessful()) {
                   DocumentSnapshot documentSnapshot = task.getResult();
                   if (!documentSnapshot.exists()) {
                       documentReference.set(data);

                       Map<String, Object> data1 = new HashMap<>();
                       data1.put(userNow, 0);

                       firestore.collection("Quizzes").document(mid).update(data1);
                   }
               }
           }
       });
    }


    */


    private void fetchModules() {
        progressDialog.show();

        firestore.collection("Quizzes").orderBy("module_created").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                moduleModelList.clear();
                progressDialog.dismiss();

                for (DocumentSnapshot documentSnapshot: task.getResult()) {

                    ModuleModel moduleModel = new ModuleModel(documentSnapshot.getString("module_id"),
                            documentSnapshot.getString("module_name"),
                            documentSnapshot.getTimestamp("module_created"),
                            documentSnapshot.getLong("submodules"));
                    moduleModelList.add(moduleModel);
                }
                moduleAdapter = new ModuleAdapter(Module.this, moduleModelList);

                recyclerView.setAdapter(moduleAdapter);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

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