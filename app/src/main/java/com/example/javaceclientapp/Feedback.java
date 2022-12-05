package com.example.javaceclientapp;

import static com.example.javaceclientapp.MainActivity.userNow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Feedback extends AppCompatActivity {

    FirebaseFirestore firestore;

    Button sendFeedback;
    EditText feedbackMessage, feedbackTitle;
    ProgressDialog progressDialog;
    Dialog addPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);




        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Feedback");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firestore = FirebaseFirestore.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sending...");
        progressDialog.setCanceledOnTouchOutside(false);

        feedbackTitle = findViewById(R.id.feedbackTitle);
        feedbackMessage = findViewById(R.id.feedbackMessage);
        sendFeedback = findViewById(R.id.sendFeedbackBtn);

        addPage = new Dialog(Feedback.this);
        addPage.setContentView(R.layout.feedback_intro_dialog);
        addPage.setCancelable(true);
        addPage.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        addPage.show();

        sendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (feedbackMessage.getText().toString().isEmpty()) {
                    feedbackMessage.setError("Please enter your message");
                    feedbackMessage.setFocusable(true);
                } if (feedbackTitle.getText().toString().isEmpty()) {
                    feedbackTitle.setError("Please enter your title");
                    feedbackTitle.setFocusable(true);
                }else {
                    progressDialog.show();

                firestore.collection("Users").document(userNow).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String user = documentSnapshot.getString("username");
                        DocumentReference documentReference = firestore.collection("Feedbacks").document();
                        String id = documentReference.getId();
                        Map<String, Object> feedback_data = new HashMap<>();
                        feedback_data.put("feedback_id", id);
                        feedback_data.put("feedback_user", user);
                        feedback_data.put("feedback_title", feedbackTitle.getText().toString());
                        feedback_data.put("feedback_message", feedbackMessage.getText().toString());

                        documentReference.set(feedback_data);

                        progressDialog.dismiss();
                        Toast.makeText(Feedback.this, "Feedback sent", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(Feedback.this, MainActivity.class));
                        finish();
                    }

                });

                }

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