package com.example.javaceclientapp;

import static com.example.javaceclientapp.SubmoduleAdapter.moduleId;
import static com.example.javaceclientapp.SubmoduleAdapter.subId;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
    Button takeQuiz;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        firestore = FirebaseFirestore.getInstance();

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