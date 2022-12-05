package com.example.javaceclientapp;

import static com.example.javaceclientapp.MainActivity.userNow;
import static com.example.javaceclientapp.Submodule.moduleIdforMainActivity;
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
    Button takeQuiz, proceedTo, showMessage, takePractical;
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

        takePractical = findViewById(R.id.takePratical);

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

        takePractical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(VideoActivity.this, PracticeExercise.class));
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

        takeQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(VideoActivity.this, Questions.class));
            }
        });

        DocumentReference documentReference = firestore.collection("Quizzes").document(moduleId).collection(subId).document("Question_List");
        DocumentReference documentReference1 = firestore.collection("Quizzes").document(moduleId).collection(subId).document("Quiz_Taker");
        DocumentReference documentReference2 = firestore.collection("Quizzes").document(moduleId).collection(subId).document("Exercise_List");

        documentReference1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) { // quiz taker
                    String quizzerNow1 = documentSnapshot.getString(userNow);
                    documentReference2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) { //exercise list
                                String quizzerNow2 = documentSnapshot.getString(userNow);
                                if (Objects.equals(quizzerNow1, userNow)) { // pag naka quiz pero wala naka exercise
                                    takeQuiz.setVisibility(View.GONE);
                                    takePractical.setVisibility(View.VISIBLE);
                                }
                                if (Objects.equals(quizzerNow1, userNow) && Objects.equals(quizzerNow2, userNow)) { // if nahuman niya ang quiz ug exercise
                                    showMessage.setVisibility(View.VISIBLE); //paadtog practice playground
                                    takeQuiz.setVisibility(View.GONE); //walaon si take quiz button
                                    takePractical.setVisibility(View.GONE); // walaon si take pratical button
                                    addPage.show();
                                }
                            } else {
                                documentReference1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        String checkUser = documentSnapshot.getString(userNow);
                                        if (Objects.equals(checkUser, userNow)) {
                                            takeQuiz.setVisibility(View.GONE);
                                            showMessage.setVisibility(View.VISIBLE);
                                        } else {
                                            takeQuiz.setVisibility(View.VISIBLE);
                                            showMessage.setVisibility(View.GONE);
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String checkInAdvance = documentSnapshot.getString("QNO");
                if (Objects.equals(checkInAdvance, "0")) {
                    takeQuiz.setEnabled(false);
                    takeQuiz.setText("No quizzes available");
                }
            }
        });
    }
}