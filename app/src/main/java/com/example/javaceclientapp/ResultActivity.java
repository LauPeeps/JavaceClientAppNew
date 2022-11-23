package com.example.javaceclientapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ResultActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    TextView result;
    Button buttonDecision;
    String scoreResultSolve;
    Dialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        progressDialog = new Dialog(ResultActivity.this);
        progressDialog.setContentView(R.layout.loading_progressbar);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.progressbar_background);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);



        result = findViewById(R.id.resultText);
        buttonDecision = findViewById(R.id.buttonDecision);


        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            scoreResultSolve = bundle.getString("scoreSuccess");
            result.setText("You earn "+ scoreResultSolve + " points");
            buttonDecision.setText("Submit Score");
        } else {
            result.setText("Wrong code solving!");
            buttonDecision.setText("Retry");
        }

        buttonDecision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle1 = getIntent().getExtras();

                if (bundle1 != null) {
                    String newScore = scoreResultSolve;
                    updateScoreUser(newScore);
                } else {
                    finish();
                }
            }
        });

    }

    private void updateScoreUser(String score) {
        progressDialog.show();
        DocumentReference documentReference = firestore.collection("Users").document(firebaseUser.getUid());

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        String currentScore = documentSnapshot.getString("score");
                        int newScoreParse = Integer.parseInt(currentScore) + Integer.parseInt(score);
                        String recordScore = String.valueOf(newScoreParse);
                        Map<String, Object> user_data = new HashMap<>();
                        user_data.put("score", recordScore);
                        documentReference.update(user_data);
                        progressDialog.dismiss();
                        Toast.makeText(ResultActivity.this, "Score recorded", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ResultActivity.this, MainActivity.class));
                        finish();
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(ResultActivity.this, "Error 400", Toast.LENGTH_SHORT).show();
            }
        });
    }

}