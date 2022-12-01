package com.example.javaceclientapp;

import static com.example.javaceclientapp.SubmoduleAdapter.moduleId;
import static com.example.javaceclientapp.SubmoduleAdapter.subId;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TopicActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    Button questionBtn;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    ViewPager viewPager;
    List<TopicModel> topicModelList = new ArrayList<>();
    SlideAdapter slideAdapter;

    LinearLayout dotsLayout;
    TextView[] dotsForPage;

    int currentPage;
    TextView backForSubsBtn, nextForQuizBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        viewPager = findViewById(R.id.viewPager);

        dotsLayout = findViewById(R.id.layoutForDots);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();


        backForSubsBtn = findViewById(R.id.backBtn);
        nextForQuizBtn = findViewById(R.id.nextBtn);

        firestore.collection("Quizzes").document(moduleId)
                .collection(subId).document("Topic_List").get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        long topics = documentSnapshot.getLong("topic_exist");
                        for (int i = 1; i <= topics; i++) {
                            TopicModel topicModel = new TopicModel(documentSnapshot.getString("topic" +String.valueOf(i)+"_title"),
                                    documentSnapshot.getString("topic"+String.valueOf(i)+"_content"));
                            topicModelList.add(topicModel);
                        }
                        slideAdapter = new SlideAdapter(TopicActivity.this, topicModelList);
                        viewPager.setAdapter(slideAdapter);
                        pageIndicator(0, topicModelList.size());
                        viewPager.addOnPageChangeListener(onPageChangeListener);


                    }
                });

        backForSubsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(currentPage - 1);
            }
        });

        nextForQuizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(currentPage + 1);
            }
        });

       /* DocumentReference documentReference = firestore.collection("Quizzes").document(moduleId).collection(subId).document("Quiz_Taker");

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

        */


        /*questionBtn = findViewById(R.id.qBtn);
        questionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(TopicActivity.this, Questions.class);
                startActivity(intent);
                finish();

            }
        });

*/
    }

    private void pageIndicator(int position, int modelSize) {

        dotsForPage = new TextView[modelSize];
        dotsLayout.removeAllViews();

        for (int i = 0; i < dotsForPage.length; i++) {
            dotsForPage[i] = new TextView(TopicActivity.this);
            dotsForPage[i].setText(Html.fromHtml("&#8226;"));
            dotsForPage[i].setTextSize(35);
            dotsForPage[i].setTextColor(Color.GRAY);

            dotsLayout.addView(dotsForPage[i]);
        }
        if (dotsForPage.length > 0) {
            dotsForPage[position].setTextColor(Color.BLUE);
        }
    }

    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            int nothing = topicModelList.size();
            pageIndicator(position, nothing);
            currentPage = position;

            if (position == 0) {
                nextForQuizBtn.setEnabled(true);
                backForSubsBtn.setEnabled(false);
                backForSubsBtn.setVisibility(View.INVISIBLE);

                nextForQuizBtn.setText("Next");

            } else if(position == dotsForPage.length - 1) {
                nextForQuizBtn.setEnabled(true);
                backForSubsBtn.setEnabled(true);
                backForSubsBtn.setVisibility(View.VISIBLE);

                nextForQuizBtn.setText("Finish");
                backForSubsBtn.setText("Back");
            } else {
                nextForQuizBtn.setEnabled(true);
                backForSubsBtn.setEnabled(true);
                backForSubsBtn.setVisibility(View.VISIBLE);

                nextForQuizBtn.setText("Next");
                backForSubsBtn.setText("Back");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}