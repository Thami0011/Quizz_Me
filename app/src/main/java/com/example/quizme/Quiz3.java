package com.example.quizme;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.Locale;

public class Quiz3 extends AppCompatActivity {
    Button next;
    RadioGroup rg;
    RadioButton rb;
    int score;
    TextView textViewCountdown;
    CountDownTimer countDownTimer;
    String correctresponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz3);
        next = findViewById(R.id.button);
        rg = findViewById(R.id.rg);
        textViewCountdown = findViewById(R.id.textViewCountdown);
        startCountdown();

        Intent i2 = getIntent();
        score = i2.getIntExtra("score", 0);


        DatabaseReference questionsRef = FirebaseDatabase.getInstance().getReference().child("questions");


        questionsRef.limitToFirst(3).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    if (dataSnapshot.getChildrenCount() >= 3) {

                        Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                        for (int i = 0; i < 2; i++) {
                            iterator.next();
                        }
                        DataSnapshot thirdQuestionSnapshot = iterator.next();
                        Question thirdQuestion = thirdQuestionSnapshot.getValue(Question.class);

                        if (thirdQuestion != null) {
                            TextView questionTextView = findViewById(R.id.textView);
                            RadioButton option1RadioButton = findViewById(R.id.radioButton);
                            RadioButton option2RadioButton = findViewById(R.id.radioButton2);
                            RadioButton option3RadioButton = findViewById(R.id.radioButton3);
                            correctresponse = thirdQuestionSnapshot.child("answer").getValue(String.class);


                            questionTextView.setText(thirdQuestion.getQuestion());
                            option1RadioButton.setText(thirdQuestion.getOption1());
                            option2RadioButton.setText(thirdQuestion.getOption2());
                            option3RadioButton.setText(thirdQuestion.getOption3());
                        }
                    } else {
                        Toast.makeText(Quiz3.this, "Only two questions found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Quiz3.this, "No questions found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Quiz3.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rg.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                } else {
                    int selectedRadioButtonId = rg.getCheckedRadioButtonId();
                    rb = findViewById(selectedRadioButtonId);


                    if(rb.getText().toString().equals(correctresponse)) {
                        score += 1;
                    }


                    Intent i2 = new Intent(getApplicationContext(), Quiz4.class);
                    i2.putExtra("score", score);
                    startActivity(i2);
                    finish();
                }
            }
        });

    }

    private void startCountdown() {
        countDownTimer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                long seconds = millisUntilFinished / 1000;
                String timeLeftFormatted = String.format(Locale.getDefault(), "%02d", seconds);
                textViewCountdown.setText(timeLeftFormatted);
            }

            @Override
            public void onFinish() {

                textViewCountdown.setText("Terminé!");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i1 = new Intent(getApplicationContext(), Quiz4.class);
                        i1.putExtra("score", score);
                        startActivity(i1);
                        finish();
                    }
                }, 2000);
            }
        };
        countDownTimer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}