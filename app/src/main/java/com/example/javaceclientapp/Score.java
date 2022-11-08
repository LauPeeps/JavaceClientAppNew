package com.example.javaceclientapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Score extends AppCompatActivity {

    TextView score;
    Button finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        score = findViewById(R.id.scorepoint);
        finish = findViewById(R.id.finishBtn);

        String score_message = getIntent().getStringExtra("SCORE");
        score.setText(score_message);


        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Score.this, MainActivity.class);
                Score.this.startActivity(intent);
                Score.this.finish();
            }
        });

    }

}