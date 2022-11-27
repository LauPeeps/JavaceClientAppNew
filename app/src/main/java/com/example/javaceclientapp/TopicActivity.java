package com.example.javaceclientapp;

import static com.example.javaceclientapp.CategorySets.idOfSets;
import static com.example.javaceclientapp.CategorySets.set_index;
import static com.example.javaceclientapp.SplashActivity.category_index;
import static com.example.javaceclientapp.SplashActivity.list;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class TopicActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    TextView topicTitle, topicContent;
    Button questionBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);


        topicTitle = findViewById(R.id.topicTitle);
        topicContent = findViewById(R.id.topicContent);

        topicContent.setMovementMethod(new ScrollingMovementMethod());
        firestore = FirebaseFirestore.getInstance();


        questionBtn = findViewById(R.id.qBtn);

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