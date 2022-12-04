package com.example.javaceclientapp;

import static com.example.javaceclientapp.SubmoduleAdapter.moduleId;
import static com.example.javaceclientapp.SubmoduleAdapter.subId;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Score extends AppCompatActivity {

    List<QuestionModel> questModel;
    TextView score;
    Button finish, retry;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        score = findViewById(R.id.scorepoint);
        finish = findViewById(R.id.finishBtn);
        retry = findViewById(R.id.retryBtn);

        String score_message = getIntent().getStringExtra("SCORE");
        String passing_score = getIntent().getStringExtra("passing");


        score.setText(score_message);

        String separator = "/";
        int separatorPos = score.getText().toString().lastIndexOf(separator);



        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        DocumentReference documentReference = firestore.collection("Users").document(firebaseUser.getUid());

        int scoreResult = Integer.parseInt(score.getText().toString().substring(0,separatorPos));

        float data1 = Float.parseFloat(passing_score);
        float data2 = scoreResult;
        float resultInPercent = data2 / data1 * 100;
        int finalResult = (int) resultInPercent;



        if (finalResult < 50) {
            finish.setVisibility(View.GONE);
            retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Score.this, MainActivity.class));
                    finish();
                }
            });
        } else {
            retry.setVisibility(View.GONE);
            finish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot documentSnapshot = task.getResult();
                                if (documentSnapshot.exists()) {
                                    String currentScore = documentSnapshot.getString("score");
                                    int newScore = Integer.parseInt(currentScore) + Integer.parseInt(score.getText().toString().substring(0,separatorPos));
                                    String recordScore = String.valueOf(newScore);
                                    Map<String, Object> score_data = new HashMap<>();
                                    score_data.put("score", recordScore);
                                    documentReference.update(score_data);

                                    DocumentReference documentReference1 = firestore.collection("Quizzes").document(moduleId).collection(subId).document("Quiz_Taker");
                                    documentReference1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            String getQuizzer = documentSnapshot.getString("quizzer");
                                            int addQuizzer = Integer.parseInt(getQuizzer) + 1;
                                            Map<String, Object> user_data = new HashMap<>();
                                            user_data.put(firebaseUser.getUid(), firebaseUser.getUid());
                                            user_data.put("quizzer", String.valueOf(addQuizzer));
                                            firestore.collection("Quizzes").document(moduleId).collection(subId).document("Quiz_Taker").update(user_data);
                                            Toast.makeText(Score.this, "Score recorded", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Score.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    Intent intent = new Intent(Score.this, PracticeExercise.class);
                    Score.this.startActivity(intent);
                    Score.this.finish();
                }
            });

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Score.this, MainActivity.class));
        finish();
    }
}