package com.example.javaceclientapp;

import static com.example.javaceclientapp.CategorySets.idOfSets;
import static com.example.javaceclientapp.SplashActivity.category_index;
import static com.example.javaceclientapp.SplashActivity.list;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.ArrayMap;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Questions extends AppCompatActivity implements View.OnClickListener{

    TextView question, qCounter, qTimer;
    Button btn1, btn2, btn3, btn4;
    List<QuestionModel> questionModels;
    FirebaseFirestore firestore;
    private int questionNum, score, setNum;
    private CountDownTimer countDownTimer;
    Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);


        qCounter = findViewById(R.id.text1);
        question = findViewById(R.id.text2);
        qTimer = findViewById(R.id.timer);

        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);

        progressDialog = new Dialog(Questions.this);
        progressDialog.setContentView(R.layout.loading_progressbar);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.progressbar_background);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        progressDialog.show();

        questionModels = new ArrayList<>();


        setNum = getIntent().getIntExtra("SETNUM",1);

        firestore = FirebaseFirestore.getInstance();

        fetchQuestions();

        score = 0;
    }

    private void fetchQuestions() {

        questionModels.clear();


        firestore.collection("Quiz").document(list.get(category_index).getCategory_id())
                .collection(idOfSets.get(setNum)).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Map<String, QueryDocumentSnapshot> document_list = new ArrayMap<>();

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            document_list.put(documentSnapshot.getId(), documentSnapshot);
                        }
                        QueryDocumentSnapshot question_list_document = document_list.get("Question_List");

                        String questions_exist = question_list_document.getString("QNO");

                        for (int i = 0; i < Integer.valueOf(questions_exist); i++) {
                            String question_id = question_list_document.getString("Q" + String.valueOf(i + 1) + "_Id");

                            QueryDocumentSnapshot queryDocumentSnapshot = document_list.get(question_id);

                            questionModels.add(new QuestionModel(
                                    queryDocumentSnapshot.getString("Question"),
                                    queryDocumentSnapshot.getString("A"),
                                    queryDocumentSnapshot.getString("B"),
                                    queryDocumentSnapshot.getString("C"),
                                    queryDocumentSnapshot.getString("D"),
                                    Integer.valueOf(queryDocumentSnapshot.getString("Correct"))

                            ));
                        }

                        setQuestions();

                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Questions.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setQuestions() {
        qTimer.setText(String.valueOf(10));

        question.setText(questionModels.get(0).getQuestion());
        btn1.setText(questionModels.get(0).getOption1());
        btn2.setText(questionModels.get(0).getOption2());
        btn3.setText(questionModels.get(0).getOption3());
        btn4.setText(questionModels.get(0).getOption4());

        qCounter.setText(1 + "/" + questionModels.size());

        startTimer();

        questionNum = 0;
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(12000, 1000) {
            @Override
            public void onTick(long l) {
                if (l < 10000)
                    qTimer.setText(String.valueOf(l / 1000));
                if(l < 10000)
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }

            @Override
            public void onFinish() {
                nextQuestion();
            }
        };
        countDownTimer.start();
    }

    @Override
    public void onClick(View view) {
        int selectBtn = 0;

        switch (view.getId()) {
            case R.id.btn1:
                selectBtn = 1;
                break;
            case R.id.btn2:
                selectBtn = 2;
                break;
            case R.id.btn3:
                selectBtn = 3;
                break;
            case R.id.btn4:
                selectBtn = 4;
                break;

            default:
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        countDownTimer.cancel();
        checkAnswer(selectBtn, view);
    }
    private void checkAnswer(int selectBtn, View view) {
        if (selectBtn == questionModels.get(questionNum).getCorrect()) {
            ((Button) view).setBackgroundTintList(ColorStateList.valueOf(Color.YELLOW));
            score++;
        } else {
            ((Button) view).setBackgroundTintList(ColorStateList.valueOf(Color.RED));

            switch (questionModels.get(questionNum).getCorrect()) {
                case 1:
                    btn1.setBackgroundTintList(ColorStateList.valueOf(Color.YELLOW));
                    break;
                case 2:
                    btn2.setBackgroundTintList(ColorStateList.valueOf(Color.YELLOW));
                    break;
                case 3:
                    btn3.setBackgroundTintList(ColorStateList.valueOf(Color.YELLOW));
                    break;
                case 4:
                    btn4.setBackgroundTintList(ColorStateList.valueOf(Color.YELLOW));
                    break;
            }
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                nextQuestion();
            }
        }, 1000);
    }

    private void nextQuestion() {
        if (questionNum < questionModels.size() - 1) {

            questionNum++;

            nextAnimation(question, 0,0);
            nextAnimation(btn1, 0,1);
            nextAnimation(btn2, 0,2);
            nextAnimation(btn3, 0,3);
            nextAnimation(btn4, 0,4);

            qCounter.setText(String.valueOf(questionNum + 1) + "/" + String.valueOf(questionModels.size()));

            qTimer.setText(String.valueOf(10));
            startTimer();

        } else {
            Intent intent = new Intent(Questions.this, Score.class);
            intent.putExtra("SCORE", String.valueOf(score) + "/" + String.valueOf(questionModels.size()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }
    }

    private void nextAnimation(View view, final int value, int valueNum) {
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(600).setStartDelay(100).setInterpolator(new DecelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        if (value == 0) {
                            switch (valueNum) {
                                case 0:
                                    ((TextView) view).setText(questionModels.get(questionNum).getQuestion());
                                    break;
                                case 1:
                                    ((Button) view).setText(questionModels.get(questionNum).getOption1());
                                    break;
                                case 2:
                                    ((Button) view).setText(questionModels.get(questionNum).getOption2());
                                    break;
                                case 3:
                                    ((Button) view).setText(questionModels.get(questionNum).getOption3());
                                    break;
                                case 4:
                                    ((Button) view).setText(questionModels.get(questionNum).getOption4());
                                    break;
                            }

                            if (valueNum != 0) {
                                ((Button) view).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#2C1D28")));
                            }

                            nextAnimation(view, 1, valueNum);
                        }

                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        countDownTimer.cancel();
    }
}