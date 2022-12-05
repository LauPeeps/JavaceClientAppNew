package com.example.javaceclientapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Leaderboard extends AppCompatActivity {

    RecyclerView recyclerView;
    ProgressDialog progressDialog;

    List<LeaderboardModel> leaderboardModelList = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    LeaderboardAdapter leaderboardAdapter;
    FirebaseFirestore firestore;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Leaderboard");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching leaderboards data...");
        progressDialog.setCanceledOnTouchOutside(false);

        firestore = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.leaderboardRecycle);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        fetchLeaderboard();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchLeaderboard();
    }

    private void fetchLeaderboard() {
        progressDialog.show();
        firestore.collection("Users").orderBy("score", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                leaderboardModelList.clear();
                progressDialog.dismiss();
                for (DocumentSnapshot documentSnapshot : task.getResult()) {
                    LeaderboardModel leaderboardModel = new LeaderboardModel(documentSnapshot.getString("username"),
                            documentSnapshot.getString("score"));
                    leaderboardModelList.add(leaderboardModel);
                }
                leaderboardAdapter = new LeaderboardAdapter(Leaderboard.this, leaderboardModelList);

                recyclerView.setAdapter(leaderboardAdapter);
            }
        });
    }




    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}