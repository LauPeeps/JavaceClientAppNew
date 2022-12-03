package com.example.javaceclientapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.collection.ArrayMap;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

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
    FirebaseAuth firebaseAuth;
    static String uid;
    static String moduleIdforMainActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submodule);

        Bundle bundle = getIntent().getExtras();
        moduleId = bundle.getString("moduleid");
        moduleIdforMainActivity = bundle.getString("moduleid");
        moduleName = bundle.getString("modulename");
        submodules = bundle.getLong("submodules");

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

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

      /*  Map<String, Object> map = new HashMap<>();
        String[] arrays = {submoduleModelList.get(i).getSubmodule_id()};
        map.put(uid, Arrays.asList(arrays));


        firestore.collection("Quizzes").document(moduleId).update(uid, FieldValue.arrayUnion(arrays)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(Submodule.this, "Noice", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Submodule.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        */
        uid = firebaseAuth.getCurrentUser().getUid();


    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchSubmodules();
    }

    /* void increaseProgress(String mid, String subid, int position) {

        DocumentReference documentReference = firestore.collection("Quizzes").document(mid).collection(uid).document(moduleId);
        DocumentReference documentReference1 = firestore.collection("Quizzes").document(mid);



        Map<String, Object> data = new HashMap<>();
        data.put(subid, subid);

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        String check = documentSnapshot.getString(subid);
                        if (!data.containsKey(check)) {
                            documentReference.update(data);
                            documentReference1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    Map<String, Object> numData = new HashMap<>();
                                    Long currentProgress = documentSnapshot.getLong(uid);
                                    numData.put(uid, currentProgress + 1);
                                    documentReference1.update(numData);
                                }
                            });
                        }


                    } else {

                    }
                }
            }
        });

    }

     */

    /* void addSubmoduleProgress(String mid, String subid, int pos) {
        DocumentReference documentReference = firestore.collection("Quizzes").document(uid);

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String check = documentSnapshot.getString(subid);

                Map<String, Object> data = new ArrayMap<>();
                data.put(subid, subid);

                if (!data.containsKey(check)) {
                    firestore.collection("Quizzes").document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Long currentValue = documentSnapshot.getLong(mid);
                            currentValue++;
                            data.put(mid, currentValue);
                            documentReference.update(data);
                        }
                    });

                }
            }
        });

    }

     */


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