package com.example.javaceclientapp;

import static com.example.javaceclientapp.CategorySets.idOfSets;
import static com.example.javaceclientapp.SplashActivity.category_index;
import static com.example.javaceclientapp.SplashActivity.list;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
    int setNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);


        setNum = getIntent().getIntExtra("SETNUM",1);

        topicTitle = findViewById(R.id.topicTitle);
        topicContent = findViewById(R.id.topicContent);

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



        firestore.collection("Quiz").document(list.get(category_index).getCategory_id())
                .collection(idOfSets.get(setNum)).document("Topic_List").get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        topicTitle.setText(documentSnapshot.getString("Topic_Title"));
                        topicContent.setText(documentSnapshot.getString("Topic_Content"));
                    }
                });

    }


}