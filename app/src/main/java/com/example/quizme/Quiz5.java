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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.Locale;

public class Quiz5 extends AppCompatActivity {
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
        setContentView(R.layout.activity_quiz5);
        next = findViewById(R.id.button);
        rg = findViewById(R.id.rg);
        textViewCountdown = findViewById(R.id.textViewCountdown);
        startCountdown();

        Intent i2 = getIntent();
        score = i2.getIntExtra("score", 0);


        DatabaseReference questionsRef = FirebaseDatabase.getInstance().getReference().child("questions");


        questionsRef.limitToFirst(5).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    if (dataSnapshot.getChildrenCount() >= 5) {

                        Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                        for (int i = 0; i < 4; i++) {
                            iterator.next(); // Ignorer les quatre premières questions
                        }
                        DataSnapshot fifthQuestionSnapshot = iterator.next();
                        Question fifthQuestion = fifthQuestionSnapshot.getValue(Question.class);

                        if (fifthQuestion != null) {
                            TextView questionTextView = findViewById(R.id.textView);
                            RadioButton option1RadioButton = findViewById(R.id.radioButton);
                            RadioButton option2RadioButton = findViewById(R.id.radioButton2);
                            RadioButton option3RadioButton = findViewById(R.id.radioButton3);
                            correctresponse = fifthQuestionSnapshot.child("answer").getValue(String.class);

                            questionTextView.setText(fifthQuestion.getQuestion());
                            option1RadioButton.setText(fifthQuestion.getOption1());
                            option2RadioButton.setText(fifthQuestion.getOption2());
                            option3RadioButton.setText(fifthQuestion.getOption3());
                        }
                    } else {
                        Toast.makeText(Quiz5.this, "Only four questions found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Quiz5.this, "No questions found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Quiz5.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
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


                    Intent i2 = new Intent(getApplicationContext(), Score.class);
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
                        Intent i1 = new Intent(getApplicationContext(), Score.class);
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