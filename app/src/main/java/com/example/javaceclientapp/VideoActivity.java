package com.example.javaceclientapp;

import static com.example.javaceclientapp.MainActivity.userNow;
import static com.example.javaceclientapp.SubmoduleAdapter.moduleId;
import static com.example.javaceclientapp.SubmoduleAdapter.subId;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.Objects;

public class VideoActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    YouTubePlayerView youtube_player_view;
    Button takeQuiz, proceedTo, showMessage;
    Dialog addPage;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        firestore = FirebaseFirestore.getInstance();

        addPage = new Dialog(VideoActivity.this);
        addPage.setContentView(R.layout.already_taken_quiz_dialog);
        addPage.setCancelable(true);
        addPage.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        proceedTo = addPage.findViewById(R.id.practicePlayground);
        showMessage = findViewById(R.id.proceedTo);

        showMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPage.show();
            }
        });

        proceedTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(VideoActivity.this, CompilerActivity.class));
                finish();
            }
        });

        youtube_player_view = findViewById(R.id.youtube_player_view);

        youtube_player_view.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                firestore.collection("Quizzes").document(moduleId).collection(subId).document("Video").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String videoId = documentSnapshot.getString("video_id");
                            assert videoId != null;
                            youTubePlayer.cueVideo(videoId, 0);
                        } else {
                            String noVid = "RickRoll";
                            youTubePlayer.cueVideo(noVid, 0);
                        }
                    }
                });
            }
        });

        takeQuiz = findViewById(R.id.showQuizActivity);

        DocumentReference documentReference1 = firestore.collection("Quizzes").document(moduleId).collection(subId).document("Quiz_Taker");
        documentReference1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String quizzerNow = documentSnapshot.getString(userNow);
                    if (Objects.equals(quizzerNow, userNow)) {
                        showMessage.setVisibility(View.VISIBLE);
                        takeQuiz.setVisibility(View.GONE);
                        addPage.show();
                    }
                }
            }
        });

        DocumentReference documentReference = firestore.collection("Quizzes").document(moduleId).collection(subId).document("Question_List");
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String checkInAdvance = documentSnapshot.getString("QNO");
                if (Objects.equals(checkInAdvance, "0")) {
                    takeQuiz.setEnabled(false);
                    takeQuiz.setText("No quizzes available");
                } else {
                    takeQuiz.setEnabled(true);
                    takeQuiz.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(VideoActivity.this, Questions.class));
                            finish();
                        }
                    });
                }
            }
        });
    }
}