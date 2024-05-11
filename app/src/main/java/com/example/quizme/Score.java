package com.example.quizme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Score extends AppCompatActivity {

    Button rejouer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_score);
        rejouer = findViewById(R.id.buttonLogout);
        Button board = findViewById(R.id.button2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rejouer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Quiz1.class);
                startActivity(intent);
                finish();
            }
        });

        board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the authenticated user's display name
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    String username = user.getDisplayName();

                    // Pass username and score to the ScoreBoard activity
                    Intent intent2 = new Intent(getApplicationContext(), ScoreBoard.class);
                    intent2.putExtra("username", username);

                    // Assuming you already have the score stored in a variable named "score"
                    intent2.putExtra("score", getIntent().getIntExtra("score", 0));
                    startActivity(intent2);
                }
            }
        });

        int score = getIntent().getIntExtra("score", 0);
        TextView scoreTextView = findViewById(R.id.textViewPercentage);
        scoreTextView.setText((score * 20) + " %");
    }
}
