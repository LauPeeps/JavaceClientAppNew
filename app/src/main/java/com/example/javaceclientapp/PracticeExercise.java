package com.example.javaceclientapp;

import static com.example.javaceclientapp.Module.userNow;
import static com.example.javaceclientapp.SubmoduleAdapter.moduleId;
import static com.example.javaceclientapp.SubmoduleAdapter.subId;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class PracticeExercise extends AppCompatActivity {
    FirebaseFirestore firestore;
    Dialog progressDialog, addPage;
    TextView exerciseTitle, exerciseInstruction, exerciseProblem;
    EditText answer1, answer2, answer3;
    Button submitExerciseBtn, doneBtn, practicePlayground;
    TextView who, what;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_exercise);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Toolbar toolbar = findViewById(R.id.practiceExer_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Practice Exercise");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addPage = new Dialog(PracticeExercise.this);
        addPage.setContentView(R.layout.exercise_congratulations_dialog);
        addPage.setCancelable(true);
        addPage.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        who = addPage.findViewById(R.id.congratsWho);
        what = addPage.findViewById(R.id.conquerWhat);
        doneBtn = addPage.findViewById(R.id.doneBtn);
        practicePlayground = addPage.findViewById(R.id.practicePlayground);

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PracticeExercise.this, MainActivity.class));
                finish();
            }
        });

        practicePlayground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PracticeExercise.this, CompilerActivity.class));
                finish();
            }
        });


        progressDialog = new Dialog(PracticeExercise.this);
        progressDialog.setContentView(R.layout.loading_progressbar);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.progressbar_background);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        exerciseTitle = findViewById(R.id.exerciseTitle);
        exerciseInstruction = findViewById(R.id.exerciseInstruction);
        exerciseProblem = findViewById(R.id.exerciseProblem);
        answer1 = findViewById(R.id.answer1);
        answer2 = findViewById(R.id.answer2);
        answer3 = findViewById(R.id.answer3);

        exerciseTitle.setMovementMethod(new ScrollingMovementMethod());
        exerciseInstruction.setMovementMethod(new ScrollingMovementMethod());
        exerciseProblem.setMovementMethod(new ScrollingMovementMethod());
        answer1.setMovementMethod(new ScrollingMovementMethod());
        answer2.setMovementMethod(new ScrollingMovementMethod());
        answer3.setMovementMethod(new ScrollingMovementMethod());

        submitExerciseBtn = findViewById(R.id.submitExercise);

        firestore = FirebaseFirestore.getInstance();

        firestore.collection("Quizzes").document(moduleId).collection(subId).document("Exercise_List").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    exerciseTitle.setText(documentSnapshot.getString("exercise_title"));
                    exerciseInstruction.setText(documentSnapshot.getString("exercise_instruction"));
                    exerciseProblem.setText(documentSnapshot.getString("exercise_problem"));
                }
            }
        });

        submitExerciseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(answer1.getText().toString(), answer2.getText().toString(), answer3.getText().toString());
            }
        });



    }

    private void checkAnswer(String ans1, String ans2, String ans3) {
        progressDialog.show();
        firestore.collection("Quizzes").document(moduleId).collection(subId).document("Exercise_List").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String answer1 = documentSnapshot.getString("answer1");
                String answer2 = documentSnapshot.getString("answer2");
                String answer3 = documentSnapshot.getString("answer3");

                if (Objects.equals(ans1, answer1) && Objects.equals(ans2, answer2) && Objects.equals(ans3, answer3)) {
                    what.setText("You conquered " + documentSnapshot.getString("exercise_title"));
                    firestore.collection("Users").document(userNow).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            who.setText("Congratulations, " + documentSnapshot.getString("username"));
                        }
                    });
                    addPage.show();
                    progressDialog.dismiss();
                } else {
                    Toast.makeText(PracticeExercise.this, "Wrong code, try again", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });
    }


}