package com.example.javaceclientapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

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
                if (exerciseContent.replaceAll(" ", "").equals(content.getText().toString().replaceAll(" ", ""))) {
                    Toast.makeText(ExerciseActivity.this, "Correct ka po", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}