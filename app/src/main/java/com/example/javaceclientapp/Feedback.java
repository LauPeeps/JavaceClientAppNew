package com.example.javaceclientapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Feedback extends AppCompatActivity {

    FirebaseFirestore firestore;

    Button sendFeedback;
    EditText feedbackMessage, feedbackTitle;
    ProgressDialog progressDialog;
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

                    UUID uuid = UUID.randomUUID();
                    String uuidAsString = uuid.toString();

                    DocumentReference documentReference = firestore.collection("Feedbacks").document();
                    String id = documentReference.getId();
                    Map<String, Object> feedback_data = new HashMap<>();
                    feedback_data.put("feedback_id", id);
                    feedback_data.put("feedback_title", feedbackTitle.getText().toString());
                    feedback_data.put("feedback_message", feedbackMessage.getText().toString());

                    documentReference.set(feedback_data);

                    progressDialog.dismiss();
                    Toast.makeText(Feedback.this, "Feedback sent", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(Feedback.this, MainActivity.class));
                    finish();
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