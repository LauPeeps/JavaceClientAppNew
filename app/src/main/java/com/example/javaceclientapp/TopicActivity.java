package com.example.javaceclientapp;

import static com.example.javaceclientapp.SubmoduleAdapter.moduleId;
import static com.example.javaceclientapp.SubmoduleAdapter.subId;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class TopicActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    TextView topicTitle, topicContent;
    Button questionBtn;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);


        topicTitle = findViewById(R.id.topicTitle);
        topicContent = findViewById(R.id.topicContent);

        topicContent.setMovementMethod(new ScrollingMovementMethod());

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();


        questionBtn = findViewById(R.id.qBtn);


        DocumentReference documentReference = firestore.collection("Quizzes").document(moduleId).collection(subId).document("Quiz_Taker");

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                   String checkCurrentQuizzer = documentSnapshot.getString(firebaseUser.getUid());
                    if (Objects.equals(checkCurrentQuizzer, String.valueOf(firebaseAuth.getUid()))) {
                        questionBtn.setVisibility(View.GONE);
                    }
                }
            }
        });


        questionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(TopicActivity.this, Questions.class);
                startActivity(intent);
                finish();

            }
        });




        firestore.collection("Quizzes").document(moduleId)
                .collection(subId).document("Topic_List").get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        topicTitle.setText(documentSnapshot.getString("topic_title"));
                        topicContent.setText(documentSnapshot.getString("topic_content"));
                    }
                });

        topicContent.setMovementMethod(new ScrollingMovementMethod());

    }


}