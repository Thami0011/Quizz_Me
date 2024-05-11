package com.example.quizme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.example.quizme.Quiz2;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class Quiz1 extends AppCompatActivity {
    Button  next;
    RadioGroup rg;
    RadioButton rb;
    String correctresponse;
    int score;
    TextView textViewCountdown;
    CountDownTimer countDownTimer;
    RadioButton option1RadioButton;
    RadioButton option2RadioButton;
    RadioButton option3RadioButton;
    TextView  questionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz1);

        questionTextView = findViewById(R.id.textView);
        option1RadioButton =findViewById(R.id.radioButton);
        option2RadioButton = findViewById(R.id.radioButton2);
        option3RadioButton = findViewById(R.id.radioButton3);
        rg = findViewById(R.id.rg);

        DatabaseReference questionsRef = FirebaseDatabase.getInstance().getReference().child("questions");
        questionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                if (datasnapshot.exists())
                {
                    DataSnapshot firstQuestionSnapshot =datasnapshot.getChildren().iterator().next();
                    Question firstQuestion = firstQuestionSnapshot.getValue(Question.class);

                    if (firstQuestion != null)
                    {
                        questionTextView.setText(firstQuestion.getQuestion());
                        option1RadioButton.setText(firstQuestion.getOption1());
                        option2RadioButton.setText(firstQuestion.getOption2());
                        option3RadioButton.setText(firstQuestion.getOption3());
                        correctresponse = firstQuestionSnapshot.child("answer").getValue(String.class);
                    }
                } else {
                    Toast.makeText(Quiz1.this,"No questions found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Quiz1.this, "Error" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



        next=findViewById(R.id.button1);
        rg=findViewById(R.id.rg);
        textViewCountdown = findViewById(R.id.textViewCountdown);
        startCountdown();


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rg.getCheckedRadioButtonId()==-1) {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                }
                else {
                    rb=findViewById(rg.getCheckedRadioButtonId());
                    if(rb.getText().toString().equals(correctresponse))
                        score+=1;
                    Intent i1=new Intent(getApplicationContext(), Quiz2.class);
                    i1.putExtra("score",score);
                    startActivity(i1);
                    //overridePendingTransition(R.anim,R.anim.entry) Transition
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
                        Intent i1=new Intent(getApplicationContext(), Quiz2.class);
                        i1.putExtra("score",score);
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
