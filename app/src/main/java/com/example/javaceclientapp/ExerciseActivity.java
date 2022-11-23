package com.example.javaceclientapp;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class ExerciseActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    Dialog progressDialog;
    String eId, exerciseTitle, exerciseDesc, exerciseContent, exerciseScore;
    TextView title, desc;
    EditText content;
    Button submitCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Exercise");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title = findViewById(R.id.exerciseTitle);
        desc = findViewById(R.id.exerciseDirection);
        desc.setMovementMethod(new ScrollingMovementMethod());
        content = findViewById(R.id.exerciseContent);
        content.setMovementMethod(new ScrollingMovementMethod());

        submitCode = findViewById(R.id.submitCode);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            eId = bundle.getString("eId");
            exerciseTitle = bundle.getString("title");
            exerciseDesc = bundle.getString("instruction");
            exerciseContent = bundle.getString("content");
            exerciseScore = bundle.getString("score");

            title.setText(exerciseTitle);
            desc.setText(exerciseDesc);
        }


        firestore = FirebaseFirestore.getInstance();

        progressDialog = new Dialog(ExerciseActivity.this);
        progressDialog.setContentView(R.layout.loading_progressbar);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.progressbar_background);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        submitCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                if (exerciseContent.replaceAll(" ", "").equals(content.getText().toString().replaceAll(" ", ""))) {
                    progressDialog.dismiss();
                    Intent intent = new Intent(ExerciseActivity.this, ResultActivity.class);
                    intent.putExtra("scoreSuccess", exerciseScore);
                    startActivity(intent);
                    finish();
                } else {
                    progressDialog.dismiss();
                    Intent intent = new Intent(ExerciseActivity.this, ResultActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            ExerciseActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}