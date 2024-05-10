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
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class Quiz3 extends AppCompatActivity {
    Button next;
    RadioGroup rg;
    RadioButton rb;
    int score;
    String correctResponse = "Question3CorrectResponse";
    TextView textViewCountdown;
    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz3);
        EdgeToEdge.enable(this);
        next = findViewById(R.id.button);
        rg = findViewById(R.id.rg);
        textViewCountdown = findViewById(R.id.textViewCountdown); // Assuming you have this TextView in your layout

        Intent i3 = getIntent();
        score = i3.getIntExtra("score", 0);

        // Start the countdown timer
        startCountdown();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rg.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(), "Veuillez choisir une réponse", Toast.LENGTH_SHORT).show();
                } else {
                    rb = findViewById(rg.getCheckedRadioButtonId());
                    if (rb.getText().toString().equals(correctResponse)) score += 1;
                    Intent i1 = new Intent(getApplicationContext(), Quiz4.class);
                    i1.putExtra("score", score);
                    startActivity(i1);
                    finish();
                }
            }
        });
    }

    private void startCountdown() {
        countDownTimer = new CountDownTimer(10000, 1000) { // 10 seconds, 1 second interval
            @Override
            public void onTick(long millisUntilFinished) {
                // Update the countdown display if needed
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
                // Perform any action needed when the countdown finishes
                // For example, you can navigate to the next question or to the result screen
                // Here, we'll just display a toast message
                textViewCountdown.setText("Terminé!");
                Toast.makeText(Quiz3.this, "Time's up!", Toast.LENGTH_SHORT).show();
                // Wait for 1 second before moving to the next page
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i1=new Intent(getApplicationContext(), Quiz4.class);
                        i1.putExtra("score",score);
                        startActivity(i1);
                        finish();
                    }
                }, 1000); // 1 second
            }
        };
        countDownTimer.start(); // Start the countdown timer
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel(); // Stop the countdown timer to avoid memory leaks
        }
    }
}
