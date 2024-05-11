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

public class Quiz2 extends AppCompatActivity {
    Button next;
    RadioGroup rg;
    RadioButton rb;
    int score;
    String correctresponse;
    TextView textViewCountdown;
    CountDownTimer countDownTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quiz2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        next = findViewById(R.id.button);
        rg = findViewById(R.id.rg);
        textViewCountdown = findViewById(R.id.textViewCountdown);

        Intent i2 = getIntent();
        score = i2.getIntExtra("score", 0);

        startCountdown();

        DatabaseReference questionsRef = FirebaseDatabase.getInstance().getReference().child("questions");
        questionsRef.limitToFirst(2).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    if (dataSnapshot.getChildrenCount() >= 2)
                    {

                        Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                        iterator.next();
                        DataSnapshot secondQuestionSnapshot = iterator.next();
                        Question secondQuestion = secondQuestionSnapshot.getValue(Question.class);

                        if (secondQuestion != null) {
                            TextView questionTextView = findViewById(R.id.textView);
                            RadioButton option1RadioButton = findViewById(R.id.radioButton);
                            RadioButton option2RadioButton = findViewById(R.id.radioButton2);
                            RadioButton option3RadioButton = findViewById(R.id.radioButton3);
                            correctresponse = secondQuestionSnapshot.child("answer").getValue(String.class);

                            questionTextView.setText(secondQuestion.getQuestion());
                            option1RadioButton.setText(secondQuestion.getOption1());
                            option2RadioButton.setText(secondQuestion.getOption2());
                            option3RadioButton.setText(secondQuestion.getOption3());
                        }
                    } else {
                        Toast.makeText(Quiz2.this, "Only one question found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Quiz2.this, "No questions found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Quiz2.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
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


                    Intent i2 = new Intent(getApplicationContext(), Quiz3.class);
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
                long minutes = seconds / 60;
                long hours = minutes / 60;
                String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d",
                        hours % 24,
                        minutes % 60,
                        seconds % 60);
                textViewCountdown.setText(timeLeftFormatted);
            }

            @Override
            public void onFinish() {
                textViewCountdown.setText("Terminé!");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i1 = new Intent(getApplicationContext(), Quiz3.class);
                        i1.putExtra("score", score);
                        startActivity(i1);
                        finish();
                    }
                }, 1000);
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
